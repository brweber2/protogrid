/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa.data;

import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.NativeBoolean;

/**
 * @version $Rev$
 */
public class DataBoolean extends BaseDataPrototype implements NativeBoolean
{
    private boolean flag;

    public DataBoolean( GridPrototype parentProto, Boolean flag )
    {
        super( parentProto, "Boolean" );
        this.flag = flag;
    }

    public boolean isTrue()
    {
        return flag;
    }

    @Override
    public String toString()
    {
        return Boolean.toString( flag ).toLowerCase();
    }

    public boolean compare( String operator, Object other )
    {
        if ( "==".equals( operator ) )
        {
            int i = compareTo( other );
            if ( i == Integer.MIN_VALUE )
            {
                throw new RuntimeException( "Comparing non-boolean value? " + other );
            }
            return i == 0;
        }
        else if ( "!=".equals( operator ) )
        {
            int i = compareTo( other );
            if ( i == Integer.MIN_VALUE )
            {
                throw new RuntimeException( "Comparing non-boolean value? " + other );
            }
            return i != 0;
        }
        throw new RuntimeException( "Unsupported boolean operator " + operator + "." );
    }

    public int compareTo( Object o )
    {
        if ( o instanceof NativeBoolean )
        {
            NativeBoolean other = (NativeBoolean) o;
            if ( isTrue() == other.getNative() )
            {
                return 0;
            }
            else if ( isTrue() && !other.getNative() )
            {
                return 1;
            }
            else
            {
                return -1;
            }
        }
        return Integer.MIN_VALUE;
    }

    public Boolean getNative()
    {
        return flag;
    }
}
