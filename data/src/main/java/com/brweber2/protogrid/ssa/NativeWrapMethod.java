/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa;

/**
 * @version $Rev$
 */
public interface NativeWrapMethod<T> extends WrapNative<T>
{
    GridPrototype call(GridArguments args);
}
