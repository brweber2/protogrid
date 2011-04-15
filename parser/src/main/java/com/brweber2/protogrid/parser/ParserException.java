/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.parser;

/**
 * @version $Rev$
 */
public class ParserException extends RuntimeException
{
    public ParserException( String s )
    {
        super( s );
    }

    public ParserException( String s, Throwable throwable )
    {
        super( s, throwable );
    }

    public ParserException( Throwable throwable )
    {
        super( throwable );
    }
}
