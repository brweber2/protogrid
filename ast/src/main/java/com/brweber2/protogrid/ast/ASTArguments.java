/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ast;

import com.brweber2.protogrid.ssa.GridList;
import com.brweber2.protogrid.ssa.GridToken;
import com.brweber2.protogrid.ssa.data.DataList;
import com.brweber2.protogrid.ssa.data.DataToken;

import java.util.Collection;
import java.util.Collections;

/**
 * @version $Rev$
 */
public class ASTArguments extends ASTBase
{
    public static final GridToken ARGUMENTS_KEY = new DataToken( "arguments" );

    public ASTArguments( SourceCodeInfo sourceCodeInfo )
    {
        super( sourceCodeInfo, ASTArguments.class.getName() );
        setSlot( ARGUMENTS_KEY, new DataList<ASTElement>( Collections.<ASTElement>emptyList() ) );
    }

    public ASTArguments( SourceCodeInfo sourceCodeInfo, Collection<ASTElement> arguments )
    {
        super( sourceCodeInfo, ASTArguments.class.getName() );
        setSlot( ARGUMENTS_KEY, new DataList<ASTElement>( arguments ) );
    }

    public String toString()
    {
        StringBuilder str = new StringBuilder();
        boolean any = false;
        for ( ASTElement argument : ( GridList <ASTElement>) getSlot( ARGUMENTS_KEY ) )
        {
            str.append( argument.toString() );
            str.append( ", " );
            any = true;
        }
        String result = str.toString();
        if ( any )
        {
            result = result.substring( 0, result.length() - 2 );
        }
        return result;
    }
}
