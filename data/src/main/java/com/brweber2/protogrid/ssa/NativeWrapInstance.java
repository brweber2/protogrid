/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa;

/**
 * @version $Rev$
 */
public interface NativeWrapInstance extends WrapNative<Object>
{
    GridPrototype getSlot( GridToken name );
    GridPrototype apply( GridPrototype self, GridArguments args );
}
