/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ast;

import com.brweber2.protogrid.ast.ASTArguments;
import com.brweber2.protogrid.ast.ASTElement;
import com.brweber2.protogrid.ssa.GridFunction;

/**
 * todo I belong elsewhere!  I can't be in SSA though b/c I depend on AST classes...
 *
 * @version $Rev$
 */
public interface GridMacro extends GridFunction
{
    ASTElement expandOne( ASTArguments args );
}
