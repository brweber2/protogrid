/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin;

import com.brweber2.protogrid.ast.ASTArguments;
import com.brweber2.protogrid.ast.ASTBlock;
import com.brweber2.protogrid.ast.ASTElement;
import com.brweber2.protogrid.ast.GridMacro;
import com.brweber2.protogrid.evaluator.Bootstrap;
import com.brweber2.protogrid.evaluator.builtin.empty.EmptyBuiltInParameters;
import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridFunction;
import com.brweber2.protogrid.ssa.GridList;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.data.BaseDataFunction;
import com.brweber2.protogrid.ssa.data.DataParameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @version $Rev$
 */
public class BuiltInMacro extends BaseDataFunction implements GridMacro
{
    private GridFunction func;

    public BuiltInMacro( GridPrototype parentProto, GridFunction func )
    {
        super( parentProto, new DataParameters( parentProto, new EmptyBuiltInParameters() ) );
        this.func = func;
    }

    public ASTElement expandOne( ASTArguments args )
    {
        // wrap each arg in a block so it can be lazily evaluated...
        List<ASTElement> wrappeds = new ArrayList<ASTElement>();
        for ( ASTElement arg : (GridList<ASTElement>)args.getSlot( ASTArguments.ARGUMENTS_KEY ) )
        {
            wrappeds.add( new ASTBlock( Bootstrap.noSource, Arrays.asList( arg ) ) );
        }
        // todo TheRuntime.getRuntime().executeExpression(  ) new ASTArguments( wrappeds ) );
        return null;
    }

    public GridPrototype apply( GridPrototype self, GridArguments args )
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
