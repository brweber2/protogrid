/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa.data;

import com.brweber2.protogrid.ssa.GridFunction;
import com.brweber2.protogrid.ssa.GridParameters;
import com.brweber2.protogrid.ssa.GridPrototype;

/**
 * @version $Rev$
 */
public abstract class BaseDataFunction extends BaseDataContextAware implements GridFunction
{
    public static final String FUNCTION = "Function";

    protected GridParameters gridParameters;

    protected BaseDataFunction( GridPrototype parentProto, GridParameters gridParameters )
    {
        super( parentProto, FUNCTION );
        this.gridParameters = gridParameters;
    }

    public GridParameters getParameters()
    {
        return gridParameters;
    }
}
