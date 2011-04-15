/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ast;

import com.brweber2.protogrid.ssa.GridToken;
import com.brweber2.protogrid.ssa.WrapNative;
import com.brweber2.protogrid.ssa.data.DataToken;

/**
 * @version $Rev$
 */
public interface ASTOperator<T> extends ASTElement, WrapNative<T>
{
    public static final GridToken LHS_KEY = new DataToken( "lhs" );
    public static final GridToken OPERATOR_KEY = new DataToken( "operator" );
    public static final GridToken RHS_KEY = new DataToken( "rhs" );

    public int getPrecedence();
    public T getOperator();
}
