/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ast;

/**
 * @version $Rev$
 */
public class ASTMathOperator extends ASTBase implements ASTOperator<ASTMathOperator.MathOperator>
{
    private MathOperator operator;

    public ASTMathOperator( SourceCodeInfo sourceCodeInfo, String operator )
    {
        super( sourceCodeInfo, ASTMathOperator.class.getName() );
        this.operator = MathOperator.fromString( operator );
    }

    public MathOperator getNative()
    {
        return operator;
    }

    public int getPrecedence()
    {
        return operator.getPrecedence();
    }

    public MathOperator getOperator()
    {
        return operator;
    }

    public enum MathOperator
    {
        PLUS( '+', 1 ),
        MINUS( '-', 1 ),
        MULTIPLY( '*', 2 ),
        DIVIDE( '/', 2 ),
        MODULUS( '%', 2 ),
        EXPONENT( '^', 3 );
        // todo -- square root? factorial? any others? NAH

        private final char operator;
        private final int precedence;

        public static MathOperator fromChar( char c )
        {
            for ( MathOperator astOperator : MathOperator.values() )
            {
                if ( astOperator.getOperator() == c )
                {
                    return astOperator;
                }
            }
            throw new RuntimeException( "No such operator '" + c + "'." );
        }

        MathOperator( char operator, int precedence )
        {
            this.operator = operator;
            this.precedence = precedence;
        }

        public int getPrecedence()
        {
            return this.precedence;
        }

        public char getOperator()
        {
            return operator;
        }

        public static MathOperator fromString( String s )
        {
            if ( s.length() == 1 )
            {
                char operator = s.charAt( 0 );
                switch ( operator )
                {
                    case '+':
                        return MathOperator.PLUS;
                    case '-':
                        return MathOperator.MINUS;
                    case '*':
                        return MathOperator.MULTIPLY;
                    case '/':
                        return MathOperator.DIVIDE;
                    case '%':
                        return MathOperator.MODULUS;
                    case '^':
                        return MathOperator.EXPONENT;
                }
            }
            throw new RuntimeException( "Invalid operator " + s );
        }

        public String toString()
        {
            return " " + operator + " ";
        }
    }

    public String toString()
    {
        return operator.toString();
    }

}
