/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa;

/**
 * @version $Rev$
 */
public interface GridFunction extends GridPrototype, GridContextAware
{
    GridPrototype apply( GridPrototype self, GridArguments args );

    GridParameters getParameters();
}
