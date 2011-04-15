/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin;

import com.brweber2.protogrid.ast.ASTParameter;
import com.brweber2.protogrid.ast.ASTParameters;
import com.brweber2.protogrid.ast.ASTToken;
import com.brweber2.protogrid.evaluator.Bootstrap;
import com.brweber2.protogrid.evaluator.TheContext;
import com.brweber2.protogrid.evaluator.TheScope;
import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridFunction;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.data.BaseDataFunction;
import com.brweber2.protogrid.ssa.data.DataParameters;
import com.brweber2.protogrid.ssa.data.DataToken;

import java.util.Arrays;

/**
 * @version $Rev$
 */
public class BuiltInMacroDef extends BaseDataFunction
{
    public BuiltInMacroDef( GridPrototype parentProto )
    {
        super( parentProto, new DataParameters(parentProto, new BuiltInParameters( new ASTParameters( Bootstrap.noSource, Arrays.asList(new ASTParameter( Bootstrap.noSource, new ASTToken( Bootstrap.noSource, "__funcdef__" ) )) ) )) );
    }

    public GridPrototype apply( GridPrototype self, GridArguments args )
    {
        GridFunction func = (GridFunction) TheScope.getCurrentScope().resolveToken( new DataToken("__fundef__") );
        BuiltInMacro macro = new BuiltInMacro( TheScope.getCurrentScope().resolveToken( Bootstrap.getMacroToken() ), func );
        return TheContext.getCurrentContext().set( macro );
    }
}
