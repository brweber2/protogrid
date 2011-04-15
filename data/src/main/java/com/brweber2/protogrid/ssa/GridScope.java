/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa;

/**
 * @version $Rev$
 */
public interface GridScope extends GridPrototype
{
    GridScope getParentScope();
    GridPrototype resolveToken( GridToken name );
    GridPrototype defineOrSet( GridToken name, GridPrototype value );
    GridPrototype defineAndSet( GridToken name, GridPrototype value );
    GridPrototype defineToken( GridToken name );
    GridPrototype setToken( GridToken name, GridPrototype value );
}
