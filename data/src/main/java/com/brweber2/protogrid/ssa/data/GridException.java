/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa.data;

import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.GridToken;
import com.brweber2.protogrid.ssa.NativeError;

import java.util.Set;

/**
 * @version $Rev$
 */
public class GridException extends RuntimeException implements GridPrototype, NativeError
{
    private String type;
    private String message;
    private GridPrototype proto;

    public GridException( String type, String message, GridPrototype proto )
    {
        super( type );
        this.message = message;
        this.proto = proto;
        this.type = type;
        setSlot( new DataToken("message"), new DataString( proto, message) ); // todo change this to be Strnig parent proto...
        setSlot( new DataToken("errType"), new DataString( proto, type ) );  // todo change this to be Strnig parent proto...
    }

    public GridPrototype getParentProto()
    {
        return proto.getParentProto();
    }

    public void setParentProto( GridPrototype proto )
    {
        proto.setParentProto( proto );
    }

    public boolean hasSlot( GridToken name )
    {
        return proto.hasSlot( name );
    }

    public GridPrototype getSlot( GridToken name )
    {
        return proto.getSlot( name );
    }

    public GridPrototype defineSlot( GridToken name )
    {
        return proto.defineSlot( name );
    }

    public GridPrototype defineAndAssignSlot( GridToken name, GridPrototype value )
    {
        return proto.defineAndAssignSlot( name, value );
    }

    public GridPrototype setSlot( GridToken name, GridPrototype value )
    {
        return proto.setSlot( name, value );
    }

    public Set<GridToken> getSlotNames()
    {
        return proto.getSlotNames();
    }

    public String getTypeName()
    {
        return type;
    }

    public RuntimeException getNative()
    {
        return this;
    }

    @Override
    public String toString()
    {
        return "error(" + type + ", " + message + ") ";
    }
}
