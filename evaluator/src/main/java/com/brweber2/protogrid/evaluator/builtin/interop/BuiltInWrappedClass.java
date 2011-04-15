/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin.interop;

import com.brweber2.protogrid.evaluator.Coerce;
import com.brweber2.protogrid.evaluator.builtin.empty.EmptyBuiltInParameters;
import com.brweber2.protogrid.ssa.GridArgument;
import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.GridToken;
import com.brweber2.protogrid.ssa.NativeWrapClass;
import com.brweber2.protogrid.ssa.data.BaseDataFunction;
import com.brweber2.protogrid.ssa.data.DataParameters;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @version $Rev$
 */
public class BuiltInWrappedClass extends BaseDataFunction implements NativeWrapClass
{
    private Class cls;

    public BuiltInWrappedClass( GridPrototype parentProto, Class cls )
    {
        super( parentProto, new DataParameters( parentProto, new EmptyBuiltInParameters() ));
        this.cls = cls;
    }

    public GridPrototype apply( GridPrototype self, GridArguments args )
    {
        Constructor[] constructors = cls.getConstructors(); // filterBySize( cls.getConstructors(), args.getSize() );
        List<Object> values = new ArrayList<Object>();
        List<Class> types = new ArrayList<Class>();
        for ( GridArgument arg : args )
        {
            Object value = Coerce.coerceFromGrid( arg.getValue() );
            values.add( value );
            types.add( value.getClass() );
        }
        try
        {
            Constructor constructor = findMatch( constructors, types, values );
            List nativevalues = BuiltInWrapMethods.nativeCoerce( constructor.getParameterTypes(), values, types, constructor.isVarArgs() );
            return Coerce.coerceToGrid( constructor.newInstance( nativevalues.toArray() ) );
        }
        catch ( InstantiationException e )
        {
            throw new RuntimeException( "Unable to construct an instance.", e );
        }
        catch ( IllegalAccessException e )
        {
            throw new RuntimeException( "Unable to construct an instance.", e );
        }
        catch ( InvocationTargetException e )
        {
            throw new RuntimeException( "Unable to construct an instance.", e );
        }
    }

    protected Constructor findMatch( Constructor[] constructors, List<Class> argTypes, List values )
    {
        for ( Constructor constructor : constructors )
        {
            if ( constructor.isVarArgs() )
            {
                boolean allOK = true;
                for ( int i = 0; i < constructor.getParameterTypes().length - 1; i++ )
                {
                    if ( !BuiltInWrapMethods.argTypeOkForParameter( argTypes.get( i ), constructor.getParameterTypes()[i], values.get( i ) ) )
                    {
                        allOK = false;
                        break;
                    }
                }
                if ( allOK )
                {
                    // the last param should be an array so check if our type
                    int index = constructor.getParameterTypes().length-1;
                    Class ptype = constructor.getParameterTypes()[index];
                    if ( !ptype.isArray() )
                    {
                        throw new RuntimeException( "expected an array in varargs method!" );
                    }
                    ptype = ptype.getComponentType();
                    int max = argTypes.size();
                    // loop over the remaining args and make sure they are the correc type
                    for ( int a = index; a < max; a++ )
                    {
                        if ( !BuiltInWrapMethods.argTypeOkForParameter( argTypes.get( a ), ptype, values.get( a ) ) )
                        {
                            allOK = false;
                            break;
                        }
                    }
                    if ( allOK )
                    {
                        return constructor;
                    }
                }
            }
            else if ( constructor.getParameterTypes().length == argTypes.size() )
            {
                boolean allMatch = true;
                for ( int i = 0; i < argTypes.size(); i++ )
                {
                    Class paramType = constructor.getParameterTypes()[i];
                    Class argType = argTypes.get(i);
                    if ( !BuiltInWrapMethods.argTypeOkForParameter( argType, paramType, values.get( i ) ) )
                    {
                        allMatch = false;
                        break;
                    }
                }
                if ( allMatch )
                {
                    return constructor;
                }
            }
        }
        throw new RuntimeException( "No matching constructor found." );
    }

    protected Constructor[] filterBySize( Constructor[] constructors, int numberOfParams )
    {
        // todo should rip this out to support varargs!
        List<Constructor> keepers = new ArrayList<Constructor>();
        for ( Constructor constructor : constructors )
        {
            // todo add vararg support...
            if ( constructor.getParameterTypes().length == numberOfParams )
            {
                keepers.add( constructor );
            }
        }
        return keepers.toArray( new Constructor[keepers.size()] );
    }

    public Class getNative()
    {
        return cls;
    }

    @Override
    public GridPrototype getSlot( GridToken name )
    {
        if ( hasSlot( name ) )
        {
            return super.getSlot( name );
        }
        String slotName = name.getToken();

        return BuiltInWrapInstance.foo( null, cls, slotName );
    }
}
