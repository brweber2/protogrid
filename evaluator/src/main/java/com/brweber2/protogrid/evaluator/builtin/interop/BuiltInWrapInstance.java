/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin.interop;

import com.brweber2.protogrid.evaluator.Bootstrap;
import com.brweber2.protogrid.evaluator.Coerce;
import com.brweber2.protogrid.evaluator.TheScope;
import com.brweber2.protogrid.evaluator.builtin.empty.EmptyBuiltInParameters;
import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.GridToken;
import com.brweber2.protogrid.ssa.NativeWrapInstance;
import com.brweber2.protogrid.ssa.data.BaseDataFunction;
import com.brweber2.protogrid.ssa.data.DataParameters;
import com.brweber2.protogrid.ssa.data.DataWrapField;
import com.brweber2.protogrid.ssa.data.DataWrapMethod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @version $Rev$
 */
public class BuiltInWrapInstance extends BaseDataFunction implements NativeWrapInstance
{
    private Object object;

    public BuiltInWrapInstance( Object object )
    {
        super( TheScope.getCurrentScope().resolveToken( Bootstrap.getWrapInstanceToken() ), new DataParameters( null, new EmptyBuiltInParameters() ) );
        this.object = object;
//        System.out.println("wrapping instance " + object);
    }

    public Object getNative()
    {
        return object;
    }

    @Override
    public GridPrototype getSlot( GridToken name )
    {
        if ( hasSlot( name ) )
        {
            return super.getSlot( name );
        }
        String slotName = name.getToken();
        return foo( object, object.getClass(), slotName );
    }

    // todo used by BuiltInWrappedClass as well... refactor me
    public static GridPrototype foo( Object instance, Class cls, String slotName )
    {
//        System.out.println("looking for " + slotName + " on " + instance + " which is a " + cls.getName());
        // what does java look for first, field or method?
        try
        {
            Field field = cls.getField( slotName );
            return new DataWrapField( TheScope.getCurrentScope().resolveToken( Bootstrap.getWrapFieldToken() ), new BuiltInWrapField( instance, field ) );
        }
        catch ( NoSuchFieldException e )
        {
            // ok, try method
            // the one with the least number of arguments wins for now
            List<Method> matches = new ArrayList<Method>();
            for ( Method method : cls.getMethods() )
            {
//                System.out.println("comparing "+ method.getName() + " and " + slotName);
                if ( method.getName().equals( slotName ) )
                {
//                    System.out.println("found method " + method.getName());
                    matches.add( method );
                }
            }
            if ( !matches.isEmpty() )
            {
                GridPrototype parent = TheScope.getCurrentScope().resolveToken( Bootstrap.getWrapMethodToken() );
                return new DataWrapMethod( parent, new BuiltInWrapMethods(parent, instance, matches ) );
            }
        }
        throw new RuntimeException( "No such slot " + slotName );
    }

    public GridPrototype apply( GridPrototype self, GridArguments args )
    {
        System.out.println("coercing " + object + " to grid type");
        return Coerce.coerceToGrid( object );
    }
}
