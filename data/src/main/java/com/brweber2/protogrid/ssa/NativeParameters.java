/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa;

import java.util.Map;

/**
 * @version $Rev$
 */
public interface NativeParameters
{
    public void map( GridArguments args );

    public Map<GridToken, GridPrototype> getMappedParameters();
}
