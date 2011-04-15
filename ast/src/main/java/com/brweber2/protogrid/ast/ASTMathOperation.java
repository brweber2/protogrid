/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ast;

/**
 * @version $Rev$
 */
public class ASTMathOperation extends ASTBase implements ASTOperation
{
    public static ASTOperation ASTMathOperation( SourceCodeInfo sourceCodeInfo, ASTElement leftHandSide, ASTOperator operator, ASTElement rightHandSide )
    {
        if ( rightHandSide instanceof ASTMathOperation && ( (ASTOperator) rightHandSide.getSlot(ASTOperator.OPERATOR_KEY) ).getPrecedence() <= operator.getPrecedence() )
        {
            ASTOperation rhsO = (ASTOperation) rightHandSide;
            return ASTMathOperation.ASTMathOperation( sourceCodeInfo,
                    ASTMathOperation.ASTMathOperation( sourceCodeInfo, leftHandSide, operator, (ASTElement) rhsO.getSlot( ASTOperator.LHS_KEY ) ),
                    (ASTOperator) rhsO.getSlot(ASTOperator.OPERATOR_KEY),
                    (ASTElement) rhsO.getSlot(ASTOperator.RHS_KEY) );
        }
        else if ( rightHandSide instanceof ASTComparison )
        {
            ASTComparison rhsC = (ASTComparison) rightHandSide;
            return ASTComparison.ASTComparison( sourceCodeInfo,
                    ASTMathOperation.ASTMathOperation( sourceCodeInfo, leftHandSide, operator, (ASTElement) rhsC.getSlot(ASTOperator.LHS_KEY) ),
                    (ASTOperator) rhsC.getSlot(ASTOperator.OPERATOR_KEY),
                    (ASTElement) rhsC.getSlot(ASTOperator.RHS_KEY) );
        }
        else if ( rightHandSide instanceof ASTLogic )
        {
            ASTLogic rhsL = (ASTLogic) rightHandSide;
            return ASTLogic.ASTLogic( sourceCodeInfo,
                    ASTMathOperation.ASTMathOperation( sourceCodeInfo, leftHandSide, operator, (ASTElement) rhsL.getSlot(ASTOperator.LHS_KEY) ),
                    (ASTOperator) rhsL.getSlot(ASTOperator.OPERATOR_KEY),
                    (ASTElement) rhsL.getSlot(ASTOperator.RHS_KEY) );
        }
        else
        {
            return new ASTMathOperation( sourceCodeInfo, leftHandSide, operator, rightHandSide );
        }
    }

    private ASTMathOperation( SourceCodeInfo sourceCodeInfo, ASTElement leftHandSide, ASTOperator operator, ASTElement rightHandSide )
    {
        super( sourceCodeInfo, ASTMathOperation.class.getName() );
        setSlot( ASTOperator.LHS_KEY, leftHandSide );
        setSlot( ASTOperator.OPERATOR_KEY, operator );
        setSlot( ASTOperator.RHS_KEY, rightHandSide );
    }

    public String toString()
    {
        return " math( " + getSlot( ASTOperator.LHS_KEY ) + " " + getSlot( ASTOperator.OPERATOR_KEY ) + " " + getSlot( ASTOperator.RHS_KEY ) + " ) ";
    }

}
