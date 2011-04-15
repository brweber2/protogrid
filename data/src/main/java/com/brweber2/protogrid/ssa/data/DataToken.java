/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa.data;

import com.brweber2.protogrid.ssa.GridToken;

/**
 * @version $Rev$
 */
public class DataToken implements GridToken
{
    private String token;

    public DataToken( String token )
    {
        this.token = token;
    }

    public void setToken( String token )
    {
        this.token = token;
    }

    public String getToken()
    {
        return token;
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

        DataToken dataToken = (DataToken) o;

        if ( !token.equals( dataToken.token ) )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return token.hashCode();
    }

    @Override
    public String toString()
    {
        return " " + token + " ";
    }
}
