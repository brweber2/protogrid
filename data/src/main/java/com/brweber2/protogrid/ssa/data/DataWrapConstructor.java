/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa.data;

import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridParameters;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.NativeWrapConstructor;

/**
 * @version $Rev$
 */
public class DataWrapConstructor extends BaseDataFunction
{
    private NativeWrapConstructor constructor;

    public DataWrapConstructor( GridPrototype parentProto, GridParameters params, NativeWrapConstructor constructor )
    {
        super( parentProto, params );
        this.constructor = constructor;
    }

    public NativeWrapConstructor getConstructor()
    {
        return constructor;
    }

    public void setConstructor( NativeWrapConstructor constructor )
    {
        this.constructor = constructor;
    }

    public GridPrototype apply( GridPrototype self, GridArguments args )
    {
        return constructor.newInstance( args );
    }

}
