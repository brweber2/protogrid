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
public class ASTParameters extends ASTBase
{
    public static final GridToken PARAMS_KEY = new DataToken("parameters");

    public ASTParameters( SourceCodeInfo sourceCodeInfo, Collection<ASTParameter> params )
    {
        super( sourceCodeInfo, ASTParameters.class.getName() );
        setSlot( PARAMS_KEY, new DataList<ASTParameter>( params ) );
    }

    public String toString()
    {
        StringBuilder str = new StringBuilder();
        str.append( " [" );
        boolean any = false;
        for ( ASTParameter param : (GridList<ASTParameter>) getSlot( PARAMS_KEY ) )
        {
            str.append( param );
            str.append( ", " );
            any = true;
        }
        String result = str.toString();
        if ( any )
        {
            result = result.substring( 0, result.length() - 2 );
        }
        result += " ] ";
        return result;
    }
}
