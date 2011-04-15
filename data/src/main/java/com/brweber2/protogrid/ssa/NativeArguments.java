/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa;

/**
 * @version $Rev$
 */
public interface NativeArguments extends Iterable<GridArgument>
{
    int getSize();

    GridArgument get( int i );
}
