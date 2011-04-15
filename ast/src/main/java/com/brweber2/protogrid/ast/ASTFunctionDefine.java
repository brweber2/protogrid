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
public class ASTFunctionDefine extends ASTBase
{
    public static final GridToken FUNC_DEF_PARAMS_KEY = new DataToken( "parameters" );
    public static final GridToken FUNC_DEF_BLOCK_KEY = new DataToken( "block" );

    public ASTFunctionDefine( SourceCodeInfo sourceCodeInfo, ASTParameters params, ASTBlock block )
    {
        super( sourceCodeInfo, ASTFunctionDefine.class.getName() );
        setSlot( FUNC_DEF_PARAMS_KEY, params );
        setSlot( FUNC_DEF_BLOCK_KEY, block );
    }

    public String toString()
    {
        return getSlot( FUNC_DEF_PARAMS_KEY ) + " " + getSlot( FUNC_DEF_BLOCK_KEY ) + " ";
    }
}
