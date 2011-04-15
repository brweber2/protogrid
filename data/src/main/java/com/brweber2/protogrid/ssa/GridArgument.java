/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa;

/**
 * @version $Rev$
 */
public interface GridArgument extends GridData
{
    GridPrototype getValue();

    GridPrototype getValue( boolean forceEvaluation );

    Object getSource();
}
