/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin.interop;

import com.brweber2.protogrid.evaluator.Coerce;
import com.brweber2.protogrid.ssa.GridArgument;
import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.NativeWrapConstructor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @version $Rev$
 */
public class BuiltInWrapConstructor implements NativeWrapConstructor
{
    private Constructor constructor;

    public BuiltInWrapConstructor( Constructor constructor )
    {
        this.constructor = constructor;
    }

    public GridPrototype newInstance( GridArguments args )
    {
        try
        {
            List list = new ArrayList();
            for ( GridArgument arg : args )
            {
                Object toadd = Coerce.coerceFromGrid( arg.getValue() );
//                System.out.println("passing " + toadd + " of type " + toadd.getClass().getName() + " to constructor " + constructor);
                list.add( toadd );
            }
            return Coerce.coerceToGrid( constructor.newInstance( list.toArray() ) );
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

    public Constructor getNative()
    {
        return constructor;
    }
}
