/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ast;

import com.brweber2.protogrid.ssa.GridToken;
import com.brweber2.protogrid.ssa.data.DataToken;

/**
 * @version $Rev$
 */
public class ASTMacro extends ASTBase
{
    public static final GridToken MACRO_PARAMS_KEY = new DataToken("params");
    public static final GridToken MACRO_BLOCK_KEY = new DataToken("block");

    public ASTMacro( SourceCodeInfo sourceCodeInfo, ASTParameters params, ASTBlock block )
    {
        super( sourceCodeInfo, ASTMacro.class.getName() );
        setSlot( MACRO_PARAMS_KEY, params );
        setSlot( MACRO_BLOCK_KEY, block );
    }

    public String toString()
    {
        return getSlot( MACRO_PARAMS_KEY ) + " " + getSlot( MACRO_BLOCK_KEY ) + " ";
    }
}
