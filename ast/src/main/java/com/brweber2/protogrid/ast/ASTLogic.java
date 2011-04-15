/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ast;

/**
 * @version $Rev$
 */
public class ASTLogic extends ASTBase implements ASTOperation
{
    public static ASTOperation ASTLogic( SourceCodeInfo sourceCodeInfo, ASTElement lhs, ASTOperator operator, ASTElement rhs )
    {
        if ( rhs instanceof ASTLogic )
        {
            ASTLogic right = (ASTLogic) rhs;
            return ASTLogic.ASTLogic( sourceCodeInfo,
                    ASTLogic.ASTLogic( sourceCodeInfo, lhs, operator, (ASTElement) right.getSlot(ASTOperator.LHS_KEY) ),
                    (ASTOperator) right.getSlot(ASTOperator.OPERATOR_KEY),
                    (ASTElement) right.getSlot(ASTOperator.RHS_KEY) );
        }
        else
        {
            return new ASTLogic( sourceCodeInfo, lhs, operator, rhs );
        }
    }

    private ASTLogic( SourceCodeInfo sourceCodeInfo, ASTElement lhs, ASTOperator operator, ASTElement rhs )
    {
        super( sourceCodeInfo, ASTLogic.class.getName() );
        setSlot( ASTOperator.LHS_KEY, lhs );
        setSlot( ASTOperator.OPERATOR_KEY, operator );
        setSlot( ASTOperator.RHS_KEY, rhs );
    }

    public String toString()
    {
        return " logic( " + getSlot( ASTOperator.LHS_KEY ) + " " + getSlot( ASTOperator.OPERATOR_KEY ) + " " + getSlot( ASTOperator.RHS_KEY ) + " ) ";
    }
}
