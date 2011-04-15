/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ast;

import com.brweber2.protogrid.ssa.GridToken;
import com.brweber2.protogrid.ssa.data.DataToken;

/**
 * @version $Rev$
 */
public class ASTAssign extends ASTBase
{
    public static final GridToken ASSIGN_LHS = new DataToken("lhs");
    public static final GridToken ASSIGN_RHS = new DataToken("rhs");

    public ASTAssign( SourceCodeInfo sourceCodeInfo, ASTElement leftHandSide, ASTElement rightHandSide )
    {
        super( sourceCodeInfo, ASTAssign.class.getName() );
        setSlot( ASSIGN_LHS, leftHandSide );
        setSlot( ASSIGN_RHS, rightHandSide );
    }

    @Override
    public String toString()
    {
        return getSlot( ASSIGN_LHS ) + " = " + getSlot( ASSIGN_RHS );
    }
}
