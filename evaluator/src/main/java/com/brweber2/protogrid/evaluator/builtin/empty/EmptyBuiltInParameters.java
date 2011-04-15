/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin.empty;

import com.brweber2.protogrid.ast.ASTParameter;
import com.brweber2.protogrid.ast.ASTParameters;
import com.brweber2.protogrid.evaluator.Bootstrap;
import com.brweber2.protogrid.evaluator.builtin.BuiltInParameters;

import java.util.Collections;

/**
 * @version $Rev$
 */
public class EmptyBuiltInParameters extends BuiltInParameters
{
    // convenience only...
    public EmptyBuiltInParameters()
    {
        super( new ASTParameters( Bootstrap.noSource, Collections.<ASTParameter>emptyList() ) );
    }
}
