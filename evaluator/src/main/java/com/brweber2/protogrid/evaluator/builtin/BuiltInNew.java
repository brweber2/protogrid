/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin;

import com.brweber2.protogrid.ast.ASTElement;
import com.brweber2.protogrid.ast.ASTParameter;
import com.brweber2.protogrid.ast.ASTParameters;
import com.brweber2.protogrid.ast.ASTToken;
import com.brweber2.protogrid.evaluator.Bootstrap;
import com.brweber2.protogrid.evaluator.TheContext;
import com.brweber2.protogrid.evaluator.TheRuntime;
import com.brweber2.protogrid.evaluator.TheScope;
import com.brweber2.protogrid.ssa.GridArgument;
import com.brweber2.protogrid.ssa.GridFunction;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.GridScope;
import com.brweber2.protogrid.ssa.data.BaseDataFunction;
import com.brweber2.protogrid.ssa.data.DataParameters;
import com.brweber2.protogrid.ssa.data.DataPrototype;

import java.util.Arrays;

/**
 * @version $Rev$
 */
public class BuiltInNew extends BaseDataFunction implements GridFunction
{
    public BuiltInNew( GridPrototype proto )
    {
        super( proto, new DataParameters( proto, new BuiltInParameters( new ASTParameters( Bootstrap.noSource, Arrays.asList(
                new ASTParameter( Bootstrap.noSource, new ASTToken( Bootstrap.noSource, "__parent__" ) )
        ) ) ) ) );
    }

    public GridPrototype apply( GridPrototype self, com.brweber2.protogrid.ssa.GridArguments args )
    {
        if ( args.getSize() != 1 )
        {
            throw new RuntimeException( "native 'new' function takes only 1 parameter, the parent prototype to use." );
        }
        GridArgument arg = args.iterator().next();
        GridPrototype parentObject = (GridPrototype) arg.getValue();
        Object s = arg.getSource();
        String tName;
        if ( s instanceof ASTToken )
        {
            ASTToken token = (ASTToken) s;

            if ( Character.isUpperCase( token.getToken().charAt( 0 ) ) )
            {
                tName = token.getToken();
            }
            else
            {
                tName = parentObject.getTypeName();
            }
        }
        else
        {
            GridScope scope = TheScope.getCurrentScope();
            GridPrototype gp = (GridPrototype) TheRuntime.getRuntime().executeExpression( (ASTElement) s, scope );
            tName = gp.getTypeName();
        }
        GridPrototype newObject = new DataPrototype( parentObject, tName );
        TheContext.getCurrentContext().set( newObject );

        return newObject;
    }

}
