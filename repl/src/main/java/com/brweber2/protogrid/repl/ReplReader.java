/*
 * $Id$
 * Copyright (C) 2011 brweber
 */
package com.brweber2.protogrid.repl;

import com.brweber2.protogrid.ast.ASTElement;
import com.brweber2.protogrid.parser.FileSourceCode;
import com.brweber2.protogrid.parser.ParserException;
import com.brweber2.protogrid.parser.ProtogridParser;
import com.brweber2.protogrid.parser.SourceCode;
import jline.ConsoleOperations;
import jline.ConsoleReader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringReader;
import java.util.Deque;

/**
 * @version $Rev$
 */
public class ReplReader
{
    private ConsoleReader reader;
    private ProtogridParser parser;
    private StringBuilder stringBuffer = new StringBuilder();

    public ReplReader()
    {
        try
        {
            this.reader = new ConsoleReader();
            this.parser = new ProtogridParser();
            // this doesn't work!!!
            this.reader.addTriggeredAction( ConsoleOperations.CTRL_C, new ActionListener()
            {
                public void actionPerformed( ActionEvent actionEvent )
                {
                    stringBuffer = new StringBuilder();
                    System.out.println( "control-c baby" );
                    throw new ReplInterrupt( "control-c" );
                }
            } );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }

    public Deque<ASTElement> read()
    {
        try
        {
            String line = reader.readLine();
            if ( "_crap".equals( line ) )
            {
                throw new ReplInterrupt( "bailing out..." );
            }
            stringBuffer.append( line );
            stringBuffer.append( "\n" );
            StringReader sr = new StringReader( stringBuffer.toString() );
            SourceCode source = new FileSourceCode( "repl", sr );
            Deque<ASTElement> result = parser.parseAll( source ).finishParsing();
            stringBuffer = new StringBuilder();
            return result;
        }
        catch ( ReplInterrupt e )
        {
            stringBuffer = new StringBuilder();
            throw e;
        }
        catch ( ParserException e )
        {
            throw e;
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }
}
