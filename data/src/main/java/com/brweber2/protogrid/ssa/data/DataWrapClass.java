/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa.data;

import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.GridToken;
import com.brweber2.protogrid.ssa.NativeParameters;
import com.brweber2.protogrid.ssa.NativeWrapClass;

import java.util.HashMap;
import java.util.Map;

/**
 * @version $Rev$
 */
public class DataWrapClass extends BaseDataFunction implements NativeWrapClass
{
    private NativeWrapClass cls;

    public DataWrapClass( GridPrototype parentProto, NativeWrapClass cls )
    {
        super( parentProto, new DataParameters( parentProto, new NativeParameters() {
            public void map( GridArguments args )
            {
            }

            public Map<GridToken, GridPrototype> getMappedParameters()
            {
                return new HashMap<GridToken,GridPrototype>();
            }
        } ) );
        this.cls = cls;
    }

    public NativeWrapClass getTheClass()
    {
        return cls;
    }

    public void setTheClass( NativeWrapClass cls )
    {
        this.cls = cls;
    }

    public GridPrototype apply( GridPrototype self, GridArguments args )
    {
        return cls.apply( self, args );
    }

    public Class getNative()
    {
        return cls.getNative();
    }

    @Override
    public GridPrototype getSlot( GridToken name )
    {
        return cls.getSlot( name );
    }

    @Override
    public boolean hasSlot( GridToken name )
    {
        return cls.hasSlot( name );
    }
}
