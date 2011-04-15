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
public class ASTParameter extends ASTBase
{
    public static final GridToken PARAMETER_KEY = new DataToken("parameter");

    // todo add default value

    public ASTParameter( SourceCodeInfo sourceCodeInfo, ASTToken name )
    {
        super( sourceCodeInfo, ASTParameter.class.getName() );
        setSlot( PARAMETER_KEY, name );
    }

    @Override
    public String toString()
    {
        return " " + getSlot( PARAMETER_KEY ) + " ";
    }
}
