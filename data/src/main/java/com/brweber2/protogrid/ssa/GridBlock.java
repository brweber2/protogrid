/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa;

/**
 * @version $Rev$
 */
public interface GridBlock extends GridPrototype, GridContextAware, GridScopeAware
{
    GridPrototype apply( GridPrototype self );
}
