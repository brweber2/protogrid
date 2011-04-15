/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa.data;

import com.brweber2.protogrid.ssa.GridArgument;
import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.NativeArguments;

import java.util.Iterator;

/**
 * @version $Rev$
 */
public class DataArguments extends BaseDataPrototype implements GridArguments
{
    private NativeArguments args;

    public DataArguments( GridPrototype parent, NativeArguments args )
    {
        super( parent, "Arguments" );
        this.args = args;
    }

    public int getSize()
    {
        return args.getSize();
    }

    public GridArgument get( int i )
    {
        if ( i >= args.getSize() )
        {
            throw new RuntimeException( "No such argument " + i );
        }
        return args.get( i );
    }

    public Iterator<GridArgument> iterator()
    {
        return args.iterator();
    }

    public String toString()
    {
        return args.toString();
    }
}
