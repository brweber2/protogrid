/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.repl;

import com.brweber2.protogrid.ast.ASTElement;
import com.brweber2.protogrid.ast.ASTParameter;
import com.brweber2.protogrid.ast.ASTParameters;
import com.brweber2.protogrid.ast.ASTToken;
import com.brweber2.protogrid.evaluator.Bootstrap;
import com.brweber2.protogrid.evaluator.TheRuntime;
import com.brweber2.protogrid.evaluator.TheScope;
import com.brweber2.protogrid.evaluator.builtin.BuiltInParameters;
import com.brweber2.protogrid.parser.FileSourceCode;
import com.brweber2.protogrid.parser.ProtogridParser;
import com.brweber2.protogrid.parser.SourceCode;
import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridFunction;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.GridScope;
import com.brweber2.protogrid.ssa.NativeString;
import com.brweber2.protogrid.ssa.data.BaseDataFunction;
import com.brweber2.protogrid.ssa.data.DataParameters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Deque;

/**
 * @version $Rev$
 */
public class LoadFunction extends BaseDataFunction implements GridFunction
{
    public LoadFunction( GridPrototype parentProto )
    {
        super( parentProto, new DataParameters( parentProto, new BuiltInParameters(
                new ASTParameters( Bootstrap.noSource, Arrays.asList( new ASTParameter( Bootstrap.noSource, new ASTToken( Bootstrap.noSource, "__fileToLoad__" ) ) ) ) ) ) );
    }

    public GridPrototype apply( GridPrototype self, GridArguments args )
    {
        ProtogridParser parser = new ProtogridParser();

        if ( args.getSize() != 1 )
        {
            throw new RuntimeException( "Load requires the name of the file to load." );
        }
        NativeString fileToLoad = (NativeString) args.get( 0 ).getValue();
        return load( fileToLoad.getNative(), parser );
    }

    private GridPrototype load( String str, ProtogridParser parser )
    {
        try
        {
            File f = new File( str );
            if ( f.exists() && f.isFile() && f.canRead() )
            {
                SourceCode source = new FileSourceCode( f.getAbsolutePath(), new FileReader( f ) );
                Deque<ASTElement> code = parser.parseAll( source ).finishParsing();

                GridScope scope = TheScope.getCurrentScope();
                for ( ASTElement astElement : code )
                {
                    TheRuntime.getRuntime().executeExpression( astElement, scope );
                }
                return TheScope.getCurrentScope();
            }
            else
            {
                throw new RuntimeException( "Unable to load file " + str );
            }
        }
        catch ( FileNotFoundException e )
        {
            throw new RuntimeException( "Unable to load file " + str, e );
        }
    }
}
