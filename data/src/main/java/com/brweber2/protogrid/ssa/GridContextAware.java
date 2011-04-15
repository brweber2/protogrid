/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa;

/**
 * @version $Rev$
 */
public interface GridContextAware
{
    // do I need this?  self can be set, we'll worry about meta stuff later...
    void setContext( GridPrototype self );

    GridPrototype getContext();

    boolean hasContext();
}
