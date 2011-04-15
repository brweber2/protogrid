/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ast;

/**
 * @version $Rev$
 */
public class ASTComparisonOperator extends ASTBase implements ASTOperator<ASTComparisonOperator.ComparisonOperator>
{
    private ComparisonOperator operator;

    public ASTComparisonOperator( SourceCodeInfo sourceCodeInfo, String operator )
    {
        super( sourceCodeInfo, ASTComparisonOperator.class.getName() );
        this.operator = ComparisonOperator.fromString( operator );
    }

    public int getPrecedence()
    {
        return 0;
    }

    public ComparisonOperator getOperator()
    {
        return operator;
    }

    public ComparisonOperator getNative()
    {
        return operator;
    }

    public enum ComparisonOperator
    {
        LESS_THAN( "<" ),
        LESS_THAN_OR_EQUAL_TO( "<=" ),
        EQUAL_TO( "==" ),
        NOT_EQUAL_TO( "!=" ),
        GREATER_THAN( ">" ),
        GREATER_THAN_OR_EQUAL_TO( ">=" );

        private String operator;

        ComparisonOperator( String operator )
        {
            this.operator = operator;
        }

        public String getOperator()
        {
            return operator;
        }

        public static ComparisonOperator fromString( String comparison )
        {
            for ( ComparisonOperator astComparisonOperator : ComparisonOperator.values() )
            {
                if ( astComparisonOperator.getOperator().equals( comparison ) )
                {
                    return astComparisonOperator;
                }
            }
            throw new RuntimeException( "Invalid comparison operator " + comparison );
        }

        public String toString()
        {
            return operator;
        }
    }

}
