/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa.data;

import com.brweber2.protogrid.ssa.GridArgument;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.NativeArgument;

/**
 * @version $Rev$
 */
public class DataArgument implements GridArgument
{
    private NativeArgument nativeArgument;

    public DataArgument( NativeArgument nativeArgument )
    {
        this.nativeArgument = nativeArgument;
    }

    public GridPrototype getValue()
    {
        return nativeArgument.getValue();
    }

    public GridPrototype getValue( boolean forceEvaluation )
    {
        return nativeArgument.getValue( forceEvaluation );
    }

    public Object getSource()
    {
        return nativeArgument;
    }

    public String toString()
    {
        return nativeArgument.toString();
    }
}
