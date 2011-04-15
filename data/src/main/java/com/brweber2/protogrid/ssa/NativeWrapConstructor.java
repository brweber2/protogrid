/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa;

import java.lang.reflect.Constructor;

/**
 * @version $Rev$
 */
public interface NativeWrapConstructor extends WrapNative<Constructor>
{
    GridPrototype newInstance(GridArguments args);
}
