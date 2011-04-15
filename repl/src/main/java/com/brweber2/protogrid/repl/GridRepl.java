/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.repl;

import com.brweber2.protogrid.ast.ASTArguments;
import com.brweber2.protogrid.ast.ASTElement;
import com.brweber2.protogrid.ast.ASTString;
import com.brweber2.protogrid.evaluator.Bootstrap;
import com.brweber2.protogrid.evaluator.ProtogridRuntime;
import com.brweber2.protogrid.evaluator.TheRuntime;
import com.brweber2.protogrid.evaluator.TheScope;
import com.brweber2.protogrid.evaluator.builtin.BuiltInArguments;
import com.brweber2.protogrid.parser.ParserException;
import com.brweber2.protogrid.printer.ProtogridPrinter;
import com.brweber2.protogrid.ssa.GridData;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.GridToken;
import com.brweber2.protogrid.ssa.data.DataArguments;
import com.brweber2.protogrid.ssa.data.DataToken;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @version $Rev$
 */
public class GridRepl
{
    public static String PROMPT = ">";

    public static void registerReplFunctions( PrintStream console, ProtogridRuntime runtime )
    {
        runtime.registerNative( "exit", new ExitFunction( console ) );
        runtime.registerNative( "help", new HelpFunction( console ) );
        runtime.registerNative( "clear", new ClearFunction( TheScope.getCurrentScope().resolveToken( Bootstrap.getFunctionToken() ), console ) );

        LoadFunction loadFunc = new LoadFunction( TheScope.getCurrentScope().resolveToken( Bootstrap.getFunctionToken() ) );
        runtime.registerNative( "load", loadFunc );
        Bootstrap.others.put( new DataToken("load"), loadFunc );
        File f = new File("rt.grid");
        if ( f.exists() && f.isFile() && f.canRead() )
        {
            List<ASTElement> bootstrapFiles = new ArrayList<ASTElement>(  );
            bootstrapFiles.add( new ASTString( Bootstrap.noSource, "rt.grid" ) ); // this means we need a file in the same directory we are running from...
            GridPrototype theScope = loadFunc.apply( loadFunc, new DataArguments( null, new BuiltInArguments( new ASTArguments(Bootstrap.noSource, bootstrapFiles ), TheRuntime.getRuntime(), TheScope.getCurrentScope() ) ) );
            for ( GridToken gridToken : theScope.getSlotNames() )
            {
                Bootstrap.others.put( gridToken, theScope.getSlot( gridToken ) );
            }
        }
    }

    public static void main( String[] args ) throws Exception
    {
        PrintStream console = System.out;  // todo only printer should use this
        ProtogridRuntime evaluator = TheRuntime.getRuntime();
        ProtogridPrinter printer = new ProtogridPrinter( console );
        ReplReader reader = new ReplReader();
        registerReplFunctions( console, evaluator );
        while ( true )
        {
            try
            {
                console.print( PROMPT );
                // READ
                for ( ASTElement astElement : reader.read() )
                {
                    // EVALUATE
                    GridData result = evaluator.executeExpression( astElement );
                    // PRINT
                    printer.write( result );
                }
                // LOOP
            }
            catch ( ParserException e )
            {
                printer.write( "\n.." );
            }
            catch ( ReplInterrupt e )
            {
                // go back for more!
            }
            catch ( Exception e )
            {
                printer.error( e );
            }
        }
    }

}
