/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin.interop;

import com.brweber2.protogrid.evaluator.Bootstrap;
import com.brweber2.protogrid.evaluator.Coerce;
import com.brweber2.protogrid.evaluator.TheContext;
import com.brweber2.protogrid.evaluator.TheScope;
import com.brweber2.protogrid.evaluator.builtin.empty.EmptyBuiltInParameters;
import com.brweber2.protogrid.ssa.GridArgument;
import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridFunction;
import com.brweber2.protogrid.ssa.GridParameters;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.GridToken;
import com.brweber2.protogrid.ssa.NativeParameters;
import com.brweber2.protogrid.ssa.NativeWrapMethod;
import com.brweber2.protogrid.ssa.data.BaseDataFunction;
import com.brweber2.protogrid.ssa.data.DataParameters;
import com.brweber2.protogrid.ssa.data.DataToken;
import com.brweber2.protogrid.ssa.data.DataWrapClass;
import com.brweber2.protogrid.ssa.data.DataWrapInstance;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version $Rev$
 */
public class BuiltInWrapMethod extends BaseDataFunction implements GridFunction, NativeWrapMethod<Method>
{
    private Method method;
    private Object instance;

    public BuiltInWrapMethod( Method method, Object instance )
    {
        super( TheScope.getCurrentScope().resolveToken( Bootstrap.getWrapMethodToken() ), new DataParameters( null, new EmptyBuiltInParameters() ) );
        this.method = method;
        this.instance = instance;
    }

    protected static GridParameters getParamsFromMethod( final Method method )
    {
        final GridPrototype parent = TheScope.getCurrentScope().resolveToken( Bootstrap.getWrapClassToken() );

        List list = new ArrayList();
        for ( Class<?> aClass : method.getParameterTypes() )
        {
            list.add( new DataWrapClass( parent, new BuiltInWrappedClass( parent, aClass ) ) );
        }
        GridParameters params = new DataParameters( parent,  new NativeParameters()
        {
            Map<GridToken,GridPrototype> values = new HashMap<GridToken,GridPrototype>();

            public void map( GridArguments args )
            {
                if ( method.getParameterTypes().length != args.getSize() )
                {
                    throw new RuntimeException( "not supported for now" );
                }
                for ( int i = 0; i < args.getSize(); i++ )
                {
                    Object arg = args.get( i ).getValue();
                    if ( arg != null && !arg.getClass().isAssignableFrom( method.getParameterTypes()[i] ))
                    {
                        throw new RuntimeException( "Arg type doesn't match." );
                    }
                    values.put( new DataToken("" + i), new DataWrapInstance( parent, new BuiltInWrapInstance( arg ) ) );
                }
            }

            public Map<GridToken, GridPrototype> getMappedParameters()
            {
                return values;
            }
        }  );
        return params;
    }

    public Method getNative()
    {
        return method;
    }

    public Object getInstance()
    {
        return instance;
    }

    public void setInstance( Object instance )
    {
        this.instance = instance;
    }

    public Method getMethod()
    {
        return method;
    }

    public void setMethod( Method method )
    {
        this.method = method;
    }

    public GridPrototype apply( GridPrototype self, GridArguments args )
    {
        List list = new ArrayList();
        for ( GridArgument arg : args )
        {
            list.add( Coerce.coerceFromGrid( arg.getValue() ) );
        }
        try
        {
//            System.out.println("calling method " + method.getName() + " on " + instance.getClass().getName() + " on " + instance.toString());
            Object result = method.invoke( instance, list.toArray() );
            return TheContext.getCurrentContext().set( Coerce.coerceToGrid( result ) );
        }
        catch ( IllegalAccessException e )
        {
            throw new RuntimeException( "Unable to invoke method.", e );
        }
        catch ( InvocationTargetException e )
        {
            throw new RuntimeException( "Unable to invoke method.", e );
        }
    }

    public GridPrototype call( GridArguments args )
    {
        return apply( TheContext.getCurrentContext().getSelf(), args );
    }
}
