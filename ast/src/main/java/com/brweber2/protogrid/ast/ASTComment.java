/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ast;

import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.GridToken;
import com.brweber2.protogrid.ssa.data.DataString;
import com.brweber2.protogrid.ssa.data.DataToken;

/**
 * @version $Rev$
 */
public class ASTComment extends ASTBase
{
    public static final GridToken COMMENT_KEY = new DataToken( "comment" );

    public ASTComment( SourceCodeInfo sourceInfo, String str )
    {
        super( sourceInfo, ASTComment.class.getName() );
        setSlot( COMMENT_KEY, new DataString( parentProto, str ) );
    }

    public String toString()
    {
        return "/* " + getSlot( COMMENT_KEY ) + " /*\n";
    }
}
