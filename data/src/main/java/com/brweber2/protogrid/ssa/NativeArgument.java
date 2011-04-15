/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa;

/**
 * @version $Rev$
 */
public interface NativeArgument
{
    GridPrototype getValue();
    GridPrototype getValue(boolean forceEvaluation);
}
