/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin.empty;

import com.brweber2.protogrid.ast.ASTBlock;
import com.brweber2.protogrid.ast.ASTElement;
import com.brweber2.protogrid.evaluator.Bootstrap;
import com.brweber2.protogrid.evaluator.TheRuntime;
import com.brweber2.protogrid.evaluator.builtin.BuiltInBlock;
import com.brweber2.protogrid.ssa.GridScope;

import java.util.Collections;

/**
 * @version $Rev$
 */
public class EmptyBuiltInBlock extends BuiltInBlock
{
    public EmptyBuiltInBlock( GridScope scope)
    {
        super( new ASTBlock( Bootstrap.noSource, Collections.<ASTElement>emptyList() ), TheRuntime.getRuntime(), scope );
    }
}
