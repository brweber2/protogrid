/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa.data;

import com.brweber2.protogrid.ssa.GridComparison;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.GridToken;

import java.util.HashSet;
import java.util.Set;

/**
 * @version $Rev$
 */
public enum DataNil implements GridPrototype, GridComparison<GridPrototype>
{
    UNDEFINED, NIL;

    // todo needs type, slots, proto slot functions...

    private GridPrototype parent;

    public GridPrototype getParentProto()
    {
        return parent; // return nil instead?
    }

    public void setParentProto( GridPrototype proto )
    {
        this.parent = proto;
    }

    public boolean hasSlot( GridToken name )
    {
        return false;
    }

    public GridPrototype getSlot( GridToken name )
    {
        throw new RuntimeException( "Unsupported operation. No such slot " + name.getToken() );
    }

    public GridPrototype defineSlot( GridToken name )
    {
        throw new RuntimeException( "Unsupported operation." );
    }

    public GridPrototype defineAndAssignSlot( GridToken name, GridPrototype value )
    {
        throw new RuntimeException( "Unsupported operation." );
    }

    public GridPrototype setSlot( GridToken name, GridPrototype value )
    {
        throw new RuntimeException( "Unsupported operation." );
    }

    public Set<GridToken> getSlotNames()
    {
        return new HashSet<GridToken>(  );
    }

    public String getTypeName()
    {
        String n = name();
        n = n.substring( 0, 1 ).toUpperCase() + n.substring( 1, n.length() );
        return n;
    }

    @Override
    public String toString()
    {
        return this.name().toLowerCase();
    }

    public boolean compare( String operator, GridPrototype other )
    {
        if ( "==".equals( operator ) )
        {
            if ( other instanceof DataNil )
            {
                DataNil o = (DataNil) other;
                return this == o;
            }
            return false;
        }
        else if ( "!=".equals( operator ) )
        {
            if ( other instanceof DataNil )
            {
                DataNil o = (DataNil) other;
                return this != o;
            }
            return true;
        }
        throw new RuntimeException( "Operator " + operator + " is not supported on " + this );
    }

}
