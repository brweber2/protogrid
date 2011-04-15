/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa;

import java.util.Set;

/**
 * @version $Rev$
 */
public interface GridPrototype extends GridData
{
    GridPrototype getParentProto();

    void setParentProto( GridPrototype proto );

    boolean hasSlot( GridToken name );

    GridPrototype getSlot( GridToken name );

    GridPrototype defineSlot( GridToken name );

    GridPrototype defineAndAssignSlot( GridToken name, GridPrototype value );

    GridPrototype setSlot( GridToken name, GridPrototype value );

    Set<GridToken> getSlotNames();

    String getTypeName();
}
