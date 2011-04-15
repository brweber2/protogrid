/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ast;

/**
 * @version $Rev$
 */
public class ASTLogicOperator extends ASTBase implements ASTOperator<ASTLogicOperator.LogicOperator>
{
    private LogicOperator operator;

    public ASTLogicOperator( SourceCodeInfo sourceCodeInfo, LogicOperator operator )
    {
        super( sourceCodeInfo, ASTLogicOperator.class.getName() );
        this.operator = operator;
    }

    public ASTLogicOperator( SourceCodeInfo sourceCodeInfo, String operator )
    {
        super( sourceCodeInfo, ASTLogicOperator.class.getName() );
        this.operator = LogicOperator.fromString( operator );
    }

    public int getPrecedence()
    {
        return 0;
    }

    public LogicOperator getOperator()
    {
        return operator;
    }

    public LogicOperator getNative()
    {
        return operator;
    }

    public enum LogicOperator
    {
        AND( "&&" ),
        OR( "||" );

        private String operator;

        LogicOperator( String operator )
        {
            this.operator = operator;
        }

        public String getOperator()
        {
            return operator;
        }

        public static LogicOperator fromString( String str )
        {
            for ( LogicOperator astLogicOperator : LogicOperator.values() )
            {
                if ( astLogicOperator.getOperator().equals( str ) )
                {
                    return astLogicOperator;
                }
            }
            throw new RuntimeException( "Invalid logic operator " + str );
        }

        public String toString()
        {
            return " " + operator + " ";
        }
    }

}
