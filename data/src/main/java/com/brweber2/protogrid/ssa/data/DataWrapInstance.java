/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa.data;

import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.GridToken;
import com.brweber2.protogrid.ssa.NativeParameters;
import com.brweber2.protogrid.ssa.NativeWrapInstance;

import java.util.HashMap;
import java.util.Map;

/**
 * @version $Rev$
 */
public class DataWrapInstance extends BaseDataFunction implements NativeWrapInstance
{
    NativeWrapInstance nativeWrapInstance;

    public DataWrapInstance( GridPrototype parentProto, NativeWrapInstance nativeWrapInstance )
    {
        super( parentProto, new DataParameters( parentProto, new NativeParameters()
        {
            public void map( GridArguments args )
            {

            }

            public Map<GridToken, GridPrototype> getMappedParameters()
            {
                return new HashMap<GridToken,GridPrototype>();
            }
        } ) );
        this.nativeWrapInstance = nativeWrapInstance;
    }

    public NativeWrapInstance getNativeWrapInstance()
    {
        return nativeWrapInstance;
    }

    public void setNativeWrapInstance( NativeWrapInstance nativeWrapInstance )
    {
        this.nativeWrapInstance = nativeWrapInstance;
    }

    @Override
    public GridPrototype getSlot( GridToken name )
    {
        return nativeWrapInstance.getSlot( name );
    }

    public Object getNative()
    {
        return nativeWrapInstance.getNative();
    }

    public GridPrototype apply( GridPrototype self, GridArguments args )
    {
        return nativeWrapInstance.apply( self, args );
    }
}
