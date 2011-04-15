/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin;

import com.brweber2.protogrid.ast.ASTParameter;
import com.brweber2.protogrid.ast.ASTParameters;
import com.brweber2.protogrid.ast.ASTToken;
import com.brweber2.protogrid.evaluator.Bootstrap;
import com.brweber2.protogrid.evaluator.TheScope;
import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.NativeError;
import com.brweber2.protogrid.ssa.data.BaseDataFunction;
import com.brweber2.protogrid.ssa.data.DataParameters;
import com.brweber2.protogrid.ssa.data.DataToken;

import java.util.Arrays;

/**
 * @version $Rev$
 */
public class BuiltInRaise extends BaseDataFunction
{
    public BuiltInRaise( GridPrototype parentProto )
    {
        super( parentProto, new DataParameters( parentProto, new BuiltInParameters( new ASTParameters( Bootstrap.noSource, Arrays.asList(
        new ASTParameter( Bootstrap.noSource, new ASTToken( Bootstrap.noSource, "__error__" ) )
        ) ) ) ) );
    }

    public GridPrototype apply( GridPrototype self, GridArguments args )
    {
        NativeError error = (NativeError) TheScope.getCurrentScope().resolveToken( new DataToken("__error__") );
        throw error.getNative();
    }
}
