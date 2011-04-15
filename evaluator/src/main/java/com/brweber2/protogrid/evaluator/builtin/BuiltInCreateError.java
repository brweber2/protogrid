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
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.NativeString;
import com.brweber2.protogrid.ssa.data.BaseDataFunction;
import com.brweber2.protogrid.ssa.data.DataParameters;
import com.brweber2.protogrid.ssa.data.DataPrototype;
import com.brweber2.protogrid.ssa.data.DataToken;
import com.brweber2.protogrid.ssa.data.GridException;

import java.util.Arrays;

/**
 * @version $Rev$
 */
public class BuiltInCreateError extends BaseDataFunction
{
    public BuiltInCreateError( GridPrototype parentProto )
    {
        super( parentProto, new DataParameters( parentProto, new BuiltInParameters( new ASTParameters( Bootstrap.noSource, Arrays.asList(
            new ASTParameter( Bootstrap.noSource, new ASTToken( Bootstrap.noSource, "__error_type__" ) ),
                new ASTParameter( Bootstrap.noSource, new ASTToken( Bootstrap.noSource, "__error_message__" ) )
        ) ) ) ) );
    }

    public GridPrototype apply( GridPrototype self, GridArguments args )
    {
        NativeString errorType = (NativeString) TheScope.getCurrentScope().resolveToken( new DataToken("__error_type__") );
        NativeString errorMessage = (NativeString) TheScope.getCurrentScope().resolveToken( new DataToken("__error_message__") );
        GridPrototype proto = new DataPrototype(TheScope.getCurrentScope().resolveToken( Bootstrap.getErrorToken() ), "GridError");
        GridException error = new GridException(errorType.getNative(), errorMessage.getNative(),proto);
        return TheContext.getCurrentContext().set( error );
    }
}
