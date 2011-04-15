/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa.data;

import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.NativeComparison;
import com.brweber2.protogrid.ssa.NativeString;

/**
 * @version $Rev$
 */
public class DataString extends BaseDataPrototype implements NativeString, NativeComparison<NativeString,GridPrototype>
{
    private static final GridPrototype StringPrototype = new DataPrototype( null, "String");
    private String str;

    public DataString( String str )
    {
        super( StringPrototype, StringPrototype.getTypeName() );
        this.str = str;
    }

    public DataString( GridPrototype parent, DataString str )
    {
        super( parent, "String" );
        this.str = str.str;
    }

    public DataString( GridPrototype parent, String str )
    {
        super( parent, "String" );  // the parent should always be GridString!
        this.str = str;
    }

    public String getStr()
    {
        return str;
    }

    public void setStr( String str )
    {
        this.str = str;
    }

    @Override
    public String toString()
    {
        if ( str == null )
        {
            return "";
        }
        return str;
//        if ( str == null )
//        {
//            return "\"\"";
//        }
//        return "\"" + str.replaceAll( "\"", "\\\\\"" ) + "\"";
    }

    public boolean compare( String operator, GridPrototype o )
    {
        if ( !( o instanceof NativeString ) )
        {
            if ( "==".equals( operator ) )
            {
                return false;
            }
            else if ( "!=".equals( operator ) )
            {
                return true;
            }
            throw new RuntimeException( "Unsupported operator " + operator );
        }
        NativeString other = (NativeString) o;
        if ( "==".equals( operator ) )
        {
            return str.compareTo( other.getNative() ) == 0;
        }
        else if ( "!=".equals( operator ) )
        {
            return str.compareTo( other.getNative() ) != 0;
        }
        throw new RuntimeException( "Invalid string comparison operator: " + operator );
    }

    public int compareTo( NativeString nativeString )
    {
        return str.compareTo( nativeString.getNative() );
    }

    public String getNative()
    {
        return str;
    }
}
