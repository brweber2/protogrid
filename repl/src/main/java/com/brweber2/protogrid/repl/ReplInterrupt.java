/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.repl;

/**
 * @version $Rev$
 */
public class ReplInterrupt extends RuntimeException
{
    public ReplInterrupt( String s )
    {
        super( s );
    }
}
