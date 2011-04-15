/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa.data;

import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridParameters;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.GridToken;
import com.brweber2.protogrid.ssa.NativeParameters;

import java.util.Map;

/**
 * @version $Rev$
 */
public class DataParameters extends BaseDataPrototype implements GridParameters
{
    private NativeParameters params;

    public DataParameters( GridPrototype parent, NativeParameters params )
    {
        super( parent, "Parameters" );
        this.params = params;
    }

    public void map( GridArguments args )
    {
        params.map( args );
    }

    public Map<GridToken, GridPrototype> getMappedParameters()
    {
        return params.getMappedParameters();
    }

    public String toString()
    {
        return params.toString();
    }
}
