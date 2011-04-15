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

/**
 * @version $Rev$
 */
public class ASTBlock extends ASTBase
{
    public static final GridToken BLOCK_KEY = new DataToken("block");

    public ASTBlock( SourceCodeInfo sourceCodeInfo, Collection<ASTElement> code )
    {
        super( sourceCodeInfo, ASTBlock.class.getName() );
        setSlot( BLOCK_KEY, new DataList<ASTElement>( code ) );
    }

    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder();
        str.append( "\n{\n" );
        for ( ASTElement astElement : (GridList<ASTElement>) getSlot( BLOCK_KEY ) )
        {
            str.append( astElement.toString() );
            str.append( "\n" );
        }
        str.append( "}\n" );
        return str.toString();
    }
}
