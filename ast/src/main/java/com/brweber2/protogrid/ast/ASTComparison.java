/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ast;

/**
 * @version $Rev$
 */
public class ASTComparison extends ASTBase implements ASTOperation
{
    public static ASTOperation ASTComparison( SourceCodeInfo sourceCodeInfo, ASTElement leftHandSide, ASTOperator operator, ASTElement rightHandSide )
    {
        if ( rightHandSide instanceof ASTLogic )
        {
            ASTLogic rhs = (ASTLogic) rightHandSide;
            return ASTLogic.ASTLogic( sourceCodeInfo,
                    ASTComparison.ASTComparison( sourceCodeInfo, leftHandSide, operator, (ASTElement) rhs.getSlot(ASTOperator.LHS_KEY) ),
                    (ASTOperator) rhs.getSlot( ASTOperator.OPERATOR_KEY ),
                    (ASTElement) rhs.getSlot( ASTOperator.RHS_KEY ) );
        }
        else if ( rightHandSide instanceof ASTComparison )
        {
            ASTComparison rhs = (ASTComparison) rightHandSide;
            return ASTLogic.ASTLogic( sourceCodeInfo,
                    ASTComparison.ASTComparison( sourceCodeInfo, leftHandSide, operator, (ASTElement) rhs.getSlot(ASTOperator.LHS_KEY) ),
                    new ASTLogicOperator( sourceCodeInfo, ASTLogicOperator.LogicOperator.AND ),
                    rhs );
        }
        else
        {
            return new ASTComparison( sourceCodeInfo, leftHandSide, operator, rightHandSide );
        }
    }

    private ASTComparison( SourceCodeInfo sourceCodeInfo, ASTElement leftHandSide, ASTOperator operator, ASTElement rightHandSide )
    {
        super( sourceCodeInfo, ASTComparison.class.getName() );
        setSlot( ASTOperator.LHS_KEY, leftHandSide );
        setSlot( ASTOperator.OPERATOR_KEY, operator );
        setSlot( ASTOperator.RHS_KEY, rightHandSide );
    }

    public String toString()
    {
        return " comp( " + getSlot( ASTOperator.LHS_KEY ) + " " + getSlot( ASTOperator.OPERATOR_KEY ) + " " + getSlot( ASTOperator.RHS_KEY ) + " ) ";
    }
}
