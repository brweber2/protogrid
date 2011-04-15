/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa;

/**
 * @version $Rev$
 */
public interface NativeFunction
{
    GridPrototype invoke( GridFunction function, GridPrototype self, GridArguments args );

    GridParameters getParameters();
}
