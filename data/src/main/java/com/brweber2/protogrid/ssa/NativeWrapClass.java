/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa;

/**
 * @version $Rev$
 */
public interface NativeWrapClass extends WrapNative<Class>
{
    public GridPrototype apply( GridPrototype self, GridArguments args );
    public GridPrototype getSlot( GridToken slotName );
    public boolean hasSlot( GridToken name );
}
