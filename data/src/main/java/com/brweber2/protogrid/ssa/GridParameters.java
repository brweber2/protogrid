/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa;

import java.util.Map;

/**
 * @version $Rev$
 */
public interface GridParameters extends GridPrototype
{
    public void map( GridArguments args );

    Map<GridToken, GridPrototype> getMappedParameters();
}
