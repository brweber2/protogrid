/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.printer;

import com.brweber2.protogrid.ssa.GridData;

import java.io.PrintStream;

/**
 * @version $Rev$
 */
public class ProtogridPrinter
{
    private PrintStream console;

    public ProtogridPrinter( PrintStream console )
    {
        this.console = console;
    }

    public void write( GridData prototype )
    {
        console.println( prototype.toString() );
    }

    /**
     * @deprecated
     */
    public void write( String message )
    {
        console.print( message );
    }

    public void error( Exception e )
    {
        console.println( "***** ERROR *****" );
        e.printStackTrace( console );
        console.println( "***** END ERROR *****" );
    }
}
