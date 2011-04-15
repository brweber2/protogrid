/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa;

/**
 * @version $Rev$
 */
public interface NativeNumber extends WrapNative<String>
{
    NativeNumber calculate( char operation, NativeNumber nbr );
}
