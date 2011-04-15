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
public class ASTInvocation extends ASTBase
{
    public static final GridToken INVOKE_TARGET_KEY = new DataToken("target");
    public static final GridToken INVOKE_ARGS_KEY = new DataToken("arguments");

    public ASTInvocation( SourceCodeInfo sourceCodeInfo, ASTElement target, ASTArguments arguments )
    {
        super( sourceCodeInfo, ASTInvocation.class.getName() );
        setSlot( INVOKE_TARGET_KEY, target );
        setSlot( INVOKE_ARGS_KEY, arguments );
    }

    public String toString()
    {
        if ( !hasSlot( INVOKE_TARGET_KEY ) )
        {
            return " ( " + getSlot( INVOKE_ARGS_KEY ) + " ) ";
        }
        return " " + getSlot( INVOKE_TARGET_KEY ) + " ( " + getSlot( INVOKE_ARGS_KEY ) + " ) ";
    }
}
