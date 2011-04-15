/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa.data;

import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.NativeComparison;
import com.brweber2.protogrid.ssa.NativeNumber;

import java.math.BigDecimal;

/**
 * @version $Rev$
 */
public class DataNumber extends BaseDataPrototype implements NativeNumber, NativeComparison<NativeNumber,GridPrototype>
{
    private static GridPrototype NUMBER_PROTOTYPE = new DataPrototype( null, "Number" );
    String number;

    public DataNumber( String number )
    {
        super( NUMBER_PROTOTYPE, NUMBER_PROTOTYPE.getTypeName() );
        this.number = number;
    }

    public DataNumber( GridPrototype parent, String number )
    {
        super( parent, "Number" );
        this.number = number;
    }

    public String getNbr()
    {
        return number;
    }

    public NativeNumber calculate( char operation, NativeNumber nbr )
    {
        BigDecimal thisNumber = new BigDecimal( number );
        BigDecimal otherNumber = new BigDecimal( nbr.getNative() );

        BigDecimal result = null;
        switch ( operation )
        {
            case '+':
                result = thisNumber.add( otherNumber );
                break;
            case '-':
                result = thisNumber.subtract( otherNumber );
                break;
            case '*':
                result = thisNumber.multiply( otherNumber );
                break;
            case '/':
                result = thisNumber.divide( otherNumber );
                break;
            case '%':
                result = thisNumber.remainder( otherNumber );
                break;
            case '^':
                int exponent = Integer.parseInt( otherNumber.toPlainString() );
                result = thisNumber.pow( exponent );
                break;
        }

        if ( result != null )
        {
            return new DataNumber( getParentProto(), result.toPlainString() );
        }
        throw new RuntimeException( "Invalid operator " + operation );
    }

    public void setNbr( String number )
    {
        this.number = number;
    }

    @Override
    public String toString()
    {
        if ( number == null )
        {
            return "";  // todo no?
        }
        return number;
    }

    public int compareTo( NativeNumber n )
    {
        BigDecimal thisNumber = new BigDecimal( number );
        BigDecimal otherNumber = new BigDecimal( n.getNative() );
        return thisNumber.compareTo( otherNumber );
    }

    // todo move to built in... so other Comparator classes can be used!
    public boolean compare( String operator, GridPrototype o )
    {
        if ( !(o instanceof NativeNumber))
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
        NativeNumber other = (NativeNumber) o;
        int comparison = this.compareTo( other );
        if ( "<".equals( operator ) )
        {
            return comparison < 0;
        }
        else if ( "<=".equals( operator ) )
        {
            return comparison <= 0;
        }
        else if ( "==".equals( operator ) )
        {
            return comparison == 0;
        }
        else if ( "!=".equals( operator ) )
        {
            return comparison != 0;
        }
        else if ( ">".equals( operator ) )
        {
            return comparison > 0;
        }
        else if ( ">=".equals( operator ) )
        {
            return comparison >= 0;
        }
        else
        {
            throw new RuntimeException( "Invalid comparison operator " + operator );
        }
    }

    public String getNative()
    {
        return number;
    }
}
