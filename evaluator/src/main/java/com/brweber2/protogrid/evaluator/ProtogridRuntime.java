/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator;

import com.brweber2.protogrid.ast.ASTElement;
import com.brweber2.protogrid.ssa.GridFunction;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.GridScope;

/**
 * @version $Rev$
 */
public interface ProtogridRuntime
{
    GridPrototype executeExpression( ASTElement ast );

    GridPrototype executeExpression( ASTElement ast, GridScope scope );

    GridPrototype executeArgument( ASTElement ast, GridScope scope );

    GridPrototype executeParameters( ASTElement ast, GridScope scope );

    void registerNative( String name, GridFunction function );
}
