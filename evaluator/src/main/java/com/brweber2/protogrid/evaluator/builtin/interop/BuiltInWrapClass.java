/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin.interop;

import com.brweber2.protogrid.ast.ASTParameter;
import com.brweber2.protogrid.ast.ASTParameters;
import com.brweber2.protogrid.ast.ASTSlotAccess;
import com.brweber2.protogrid.ast.ASTToken;
import com.brweber2.protogrid.evaluator.Bootstrap;
import com.brweber2.protogrid.evaluator.TheScope;
import com.brweber2.protogrid.evaluator.builtin.BuiltInFunction;
import com.brweber2.protogrid.evaluator.builtin.BuiltInParameters;
import com.brweber2.protogrid.evaluator.builtin.empty.EmptyBuiltInParameters;
import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridBlock;
import com.brweber2.protogrid.ssa.GridFunction;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.GridScope;
import com.brweber2.protogrid.ssa.NativeBlock;
import com.brweber2.protogrid.ssa.NativeList;
import com.brweber2.protogrid.ssa.NativeString;
import com.brweber2.protogrid.ssa.NativeWrapClass;
import com.brweber2.protogrid.ssa.data.BaseDataFunction;
import com.brweber2.protogrid.ssa.data.DataFunction;
import com.brweber2.protogrid.ssa.data.DataList;
import com.brweber2.protogrid.ssa.data.DataParameters;
import com.brweber2.protogrid.ssa.data.DataString;
import com.brweber2.protogrid.ssa.data.DataToken;
import com.brweber2.protogrid.ssa.data.DataWrapClass;
import com.brweber2.protogrid.ssa.data.DataWrapConstructor;
import com.brweber2.protogrid.ssa.data.DataWrapField;
import com.brweber2.protogrid.ssa.data.DataWrapMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @version $Rev$
 */
public class BuiltInWrapClass extends BaseDataFunction
{
    public BuiltInWrapClass( GridPrototype parentProto )
    {
        super( parentProto, new DataParameters( parentProto, new EmptyBuiltInParameters(
                //new ASTParameters( Arrays.asList( new ASTParameter( new ASTToken("__classname__") )  ) )
        ) ) );
    }

    public GridPrototype apply( GridPrototype self, GridArguments args )
    {
        if ( args.getSize() != 1 )
        {
            throw new RuntimeException( "wrapclass takes one parameter, the fully qualified name of the class to wrap!" );
        }
        // treat slot access as a class name...  ie WrapClass(java.util.ArrayList) instead of WrapClass("java.util.ArrayList")
        String fqname;
//        System.out.println("ast? " + args.get(0).getSource().getClass().getName());
        if ( args.get(0).getSource() instanceof ASTSlotAccess )
        {
            fqname = getClassName( (ASTSlotAccess) args.get(0).getSource() );
        }
        else
        {
            NativeString str = (NativeString) args.get(0).getValue();
            fqname = str.getNative();
        }

        Class cls = null;
        try
        {
            cls = Class.forName( fqname );
        }
        catch ( ClassNotFoundException e )
        {
            throw new RuntimeException( "No such class" + fqname );
        }
        GridPrototype wrapClass = TheScope.getCurrentScope().resolveToken( Bootstrap.getWrapClassToken() );
        BuiltInWrappedClass biwc = new BuiltInWrappedClass( wrapClass, cls );
        DataWrapClass dwc = new DataWrapClass( wrapClass, biwc );
        dwc.setSlot( new DataToken("classname"), new DataString(TheScope.getCurrentScope().resolveToken( Bootstrap.getStringToken() ), cls.getName()) );
        dwc.setSlot( new DataToken("fields"), getFields( cls.getFields() ) );
        dwc.setSlot( new DataToken("methods"), getMethods( cls.getMethods() ) );
        dwc.setSlot( new DataToken("constructors"), getConstructors( cls.getConstructors() ) );
        dwc.setSlot( new DataToken("field"), getFieldFunction( cls ) );
        dwc.setSlot( new DataToken("method"), getMethodFunction( cls ) );
        dwc.setSlot( new DataToken("constructor"), getConstructorFunction( cls ) );
        // todo add version for instance fields/methods and static fields/methods
        return dwc;
    }

    private String getClassName(ASTSlotAccess ast )
    {
        if ( ast.getSlot(ASTSlotAccess.SLOT_TARGET_KEY) instanceof ASTSlotAccess )
        {
            return getClassName( (ASTSlotAccess) ast.getSlot(ASTSlotAccess.SLOT_TARGET_KEY) ) + "." + ((ASTToken)ast.getSlot(ASTSlotAccess.SLOT_NAME_KEY)).getToken();
        }
        else if ( ast.getSlot(ASTSlotAccess.SLOT_TARGET_KEY) instanceof ASTToken )
        {
            return ((ASTToken) ast.getSlot(ASTSlotAccess.SLOT_TARGET_KEY)).getToken() + "." + ((ASTToken)ast.getSlot(ASTSlotAccess.SLOT_NAME_KEY)).getToken();
        }
        throw new RuntimeException( "Invalid class name for " + ast );
    }

    private GridFunction getFieldFunction(final Class cls)
    {
        GridPrototype func = TheScope.getCurrentScope().resolveToken( Bootstrap.getFunctionToken() );
        return new DataFunction(func, new BuiltInFunction( new BuiltInParameters( new ASTParameters( Bootstrap.noSource, Arrays.asList(
                new ASTParameter( Bootstrap.noSource, new ASTToken( Bootstrap.noSource, "__fieldname__" ) )
        ) ) ),
                new NativeBlock()
                {
                    public GridPrototype invoke( GridBlock block, GridPrototype self )
                    {
                        NativeString ns = (NativeString) TheScope.getCurrentScope().resolveToken( new DataToken("__fieldname__") );
                        try
                        {
                            GridPrototype prnt = TheScope.getCurrentScope().resolveToken( Bootstrap.getWrapFieldToken() );
                            BuiltInWrapField bif = new BuiltInWrapField( null, cls.getField( ns.getNative() ));
                            return new DataWrapField( prnt , bif );
                        }
                        catch ( NoSuchFieldException e )
                        {
                            throw new RuntimeException( "No such field " + ns.getNative(),e );
                        }
                    }

                    public boolean isClosure()
                    {
                        return false;
                    }

                    public GridScope getScope()
                    {
                        throw new RuntimeException( "You shouldn't be calling this!" );
                    }
                }));
    }

    private GridFunction getMethodFunction(final Class cls)
    {
        GridPrototype func = TheScope.getCurrentScope().resolveToken( Bootstrap.getFunctionToken() );
        return new DataFunction(func, new BuiltInFunction( new BuiltInParameters( new ASTParameters( Bootstrap.noSource, Arrays.asList(
                new ASTParameter( Bootstrap.noSource, new ASTToken( Bootstrap.noSource, "__methodname__" ) ),
                new ASTParameter( Bootstrap.noSource, new ASTToken( Bootstrap.noSource, "__paramtypes__" ) )
        ) ) ),
                new NativeBlock()
                {
                    public GridPrototype invoke( GridBlock block, GridPrototype self )
                    {
                        NativeString ns = (NativeString) TheScope.getCurrentScope().resolveToken( new DataToken("__methodname__") );
                        NativeList nl = (NativeList) TheScope.getCurrentScope().resolveToken( new DataToken("__paramtypes__") );
                        try
                        {
                            List<Class> classList = new ArrayList<Class>();
                            for ( Object o : nl.getNative() )
                            {
                                NativeWrapClass nwc = (NativeWrapClass) o;
                                classList.add( nwc.getNative() );
                            }
                            Class[] classes  = classList.toArray( new Class[classList.size()] );
                            return new DataWrapMethod( TheScope.getCurrentScope().resolveToken( Bootstrap.getWrapMethodToken() ),
                                    new BuiltInWrapMethod( cls.getMethod( ns.getNative(), classes ), null ) );
                        }
                        catch ( NoSuchMethodException e )
                        {
                            throw new RuntimeException( "No such method " + ns.getNative(),e );
                        }
                    }

                    public boolean isClosure()
                    {
                        return false;
                    }

                    public GridScope getScope()
                    {
                        throw new RuntimeException( "You shouldn't be calling this." );
                    }
                }));
    }

    private GridFunction getConstructorFunction(final Class cls)
    {
        GridPrototype func = TheScope.getCurrentScope().resolveToken( Bootstrap.getFunctionToken() );
        return new DataFunction(func, new BuiltInFunction( new BuiltInParameters( new ASTParameters( Bootstrap.noSource, Arrays.asList(
                new ASTParameter( Bootstrap.noSource, new ASTToken( Bootstrap.noSource, "__paramtypes__" ) )
        ) ) ),
                new NativeBlock()
                {
                    public GridPrototype invoke( GridBlock block, GridPrototype self )
                    {
                        NativeList nl = (NativeList) TheScope.getCurrentScope().resolveToken( new DataToken("__paramtypes__") );
                        try
                        {
                            List<Class> classList = new ArrayList<Class>();
                            for ( Object o : nl.getNative() )
                            {
                                NativeWrapClass nwc = (NativeWrapClass) o;
                                classList.add( nwc.getNative() );
                            }
                            Class[] classes  = classList.toArray( new Class[classList.size()] );
                            return new DataWrapConstructor( TheScope.getCurrentScope().resolveToken( Bootstrap.getWrapMethodToken() ), new DataParameters( parentProto, new EmptyBuiltInParameters() ), new BuiltInWrapConstructor( cls.getConstructor( classes ) ) );
                        }
                        catch ( NoSuchMethodException e )
                        {
                            throw new RuntimeException( "No such constructor.",e );
                        }
                    }

                    public boolean isClosure()
                    {
                        return false;
                    }

                    public GridScope getScope()
                    {
                        throw new RuntimeException( "You shouldn't be calling this!" );
                    }
                }));
    }

    private DataList getFields(Field[] fields)
    {
        GridPrototype str = TheScope.getCurrentScope().resolveToken( Bootstrap.getStringToken() );
        List<GridPrototype> list =new ArrayList<GridPrototype>();
        for ( Field field : fields )
        {
            list.add( new DataString( str, field.getName() ) );
        }
        return new DataList(TheScope.getCurrentScope().resolveToken( Bootstrap.getListToken() ), list);
    }

    private DataList getMethods(Method[] methods)
    {
        GridPrototype str = TheScope.getCurrentScope().resolveToken( Bootstrap.getStringToken() );
        List<GridPrototype> list =new ArrayList<GridPrototype>();
        for ( Method method : methods )
        {
            list.add( new DataString( str, method.getName() ) );
        }
        return new DataList(TheScope.getCurrentScope().resolveToken( Bootstrap.getListToken() ), list);
    }

    private DataList getConstructors(Constructor[] constructors)
    {
        GridPrototype str = TheScope.getCurrentScope().resolveToken( Bootstrap.getStringToken() );
        List<GridPrototype> list =new ArrayList<GridPrototype>();
        for ( Constructor constructor : constructors )
        {
            list.add( new DataString( str, constructor.getName() ) );
        }
        return new DataList(TheScope.getCurrentScope().resolveToken( Bootstrap.getListToken() ), list);
    }
}
