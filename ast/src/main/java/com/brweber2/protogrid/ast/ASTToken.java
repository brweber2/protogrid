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
public class ASTToken extends ASTBase implements NativeString
{
    public static final GridToken TOKEN_KEY = new DataToken( "token" );

    public ASTToken( SourceCodeInfo sourceCodeInfo, String token )
    {
        super( sourceCodeInfo, ASTToken.class.getName() );
        setSlot( TOKEN_KEY, new DataString(token) );
    }


    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }

        return this.getToken().equals( ( (ASTToken) o ).getToken() );
    }

    @Override
    public int hashCode()
    {
        return getToken().hashCode();
    }

    public String getToken()
    {
        return ((NativeString)getSlot( TOKEN_KEY ) ).getNative();
    }

    @Override
    public String toString()
    {
        return " " + getToken() + " ";
    }

    public String getNative()
    {
        return getToken();
    }
}
