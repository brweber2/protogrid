/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ast;

import com.brweber2.protogrid.ssa.GridToken;
import com.brweber2.protogrid.ssa.NativeString;
import com.brweber2.protogrid.ssa.data.DataString;
import com.brweber2.protogrid.ssa.data.DataToken;

/**
 * @version $Rev$
 */
public class ASTString extends ASTBase implements NativeString
{
    private static final GridToken STRING_KEY = new DataToken( "text" );

    public ASTString( SourceCodeInfo sourceCodeInfo, String str )
    {
        super( sourceCodeInfo, ASTString.class.getName() );
        setSlot( STRING_KEY, new DataString( str ) );
    }

    @Override
    public String toString()
    {
        if ( getNative() == null )
        {
            return "\"\"";
        }
        return "\"" + getNative().replaceAll( "\"", "\\\\\"" ) + "\"";
    }

    public String getNative()
    {
        return ((NativeString) getSlot( STRING_KEY ) ).getNative();
    }
}
