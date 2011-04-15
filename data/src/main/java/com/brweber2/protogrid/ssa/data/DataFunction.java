/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa.data;

import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridParameters;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.NativeFunction;

/**
 * @version $Rev$
 */
public class DataFunction extends BaseDataFunction
{
    protected NativeFunction nativeFunction;

    public DataFunction( GridPrototype parent, NativeFunction nativeFunction )
    {
        super( parent, nativeFunction.getParameters() );
        this.nativeFunction = nativeFunction;
    }

    public GridPrototype apply( GridPrototype self, GridArguments args )
    {
        return nativeFunction.invoke( this, self, args );
    }

    public GridParameters getParameters()
    {
        return nativeFunction.getParameters();
    }

    @Override
    public String toString()
    {
        return nativeFunction.toString();
    }
}
