/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa;

/**
 * @version $Rev$
 */
public interface GridList<T extends GridPrototype> extends GridPrototype, Iterable<T>
{
    T get( int i );

    int getSize();

    void add( T prototype );
}
