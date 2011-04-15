/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator;

import com.brweber2.protogrid.evaluator.builtin.interop.BuiltInWrapInstance;
import com.brweber2.protogrid.evaluator.builtin.interop.BuiltInWrappedClass;
import com.brweber2.protogrid.ssa.GridData;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.WrapNative;
import com.brweber2.protogrid.ssa.data.DataBoolean;
import com.brweber2.protogrid.ssa.data.DataList;
import com.brweber2.protogrid.ssa.data.DataNil;
import com.brweber2.protogrid.ssa.data.DataNumber;
import com.brweber2.protogrid.ssa.data.DataString;
import com.brweber2.protogrid.ssa.data.DataWrapClass;
import com.brweber2.protogrid.ssa.data.DataWrapInstance;
import com.brweber2.protogrid.ssa.data.GridException;

import java.util.Collection;

/**
 * @version $Rev$
 */
public class Coerce
{
    public static GridPrototype coerceToGrid( Object o )
    {
        GridPrototype parent = TheScope.getCurrentScope().resolveToken( Bootstrap.getCoerceToken() );
        if ( o instanceof GridPrototype )
        {
            return (GridPrototype) o;
        }
        else if ( o instanceof String )
        {
            return new DataString( TheScope.getCurrentScope().resolveToken( Bootstrap.getStringToken() ), (String) o );
        }
        else if ( o instanceof Character)
        {
            return new DataString( TheScope.getCurrentScope().resolveToken( Bootstrap.getStringToken() ), o.toString() );
        }
        else if ( o instanceof Boolean )
        {
            return new DataBoolean( parent, (Boolean) o );
        }
        else if ( o instanceof Number )
        {
            return new DataNumber( parent, "" + o ); // todo this probably needs to be smarter...
        }
        else if ( o instanceof Collection )
        {
            return new DataList( parent, (Collection) o );
        }
        // todo we probably don't want to wrap classes?
        else if ( o instanceof Class )
        {
            return new DataWrapClass( parent, new BuiltInWrappedClass( parent, (Class) o ) );
        }
        else if ( o == null )
        {
            return DataNil.NIL;
        }
        else if ( o instanceof Exception )
        {
            return new GridException( "grid", ((Exception)o).getMessage(), parent );
        }
        else if ( o instanceof WrapNative )
        {
            WrapNative w = (WrapNative) o;
            System.out.println("you shouldn't see me :(");
            return new DataWrapInstance( parent, new BuiltInWrapInstance( w.getNative() ) );
        }
        return new DataWrapInstance( parent, new BuiltInWrapInstance( o ) );
    }

    public static Object coerceFromGrid( GridData p )
    {
        if ( p == DataNil.NIL || p == DataNil.UNDEFINED )
        {
            return null;
        }
        if ( p instanceof WrapNative )
        {
            return ((WrapNative) p).getNative();
        }
        System.out.println("not coercing " + p);
        return p;
    }
}
