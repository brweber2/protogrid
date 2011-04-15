/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin;

import com.brweber2.protogrid.ast.ASTBlock;
import com.brweber2.protogrid.ast.ASTElement;
import com.brweber2.protogrid.evaluator.Bootstrap;
import com.brweber2.protogrid.evaluator.ProtogridRuntime;
import com.brweber2.protogrid.evaluator.TheContext;
import com.brweber2.protogrid.evaluator.TheScope;
import com.brweber2.protogrid.ssa.GridData;
import com.brweber2.protogrid.ssa.GridList;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.GridScope;
import com.brweber2.protogrid.ssa.NativeBlock;

/**
 * @version $Rev$
 */
public class BuiltInBlock implements NativeBlock
{
    private ASTBlock astBlock;
    private ProtogridRuntime runtime;
    private GridScope scope;

    public BuiltInBlock( ASTBlock astBlock, ProtogridRuntime runtime, GridScope scope )
    {
        this.astBlock = astBlock;
        this.runtime = runtime;
        this.scope = scope;
    }

    public GridPrototype invoke( com.brweber2.protogrid.ssa.GridBlock block, GridPrototype self )
    {
        TheContext.getCurrentContext().setSelf( self );
        GridData data = Bootstrap.getNil();
        if ( astBlock != null )
        {
            for ( ASTElement astElement : (GridList<ASTElement>)astBlock.getSlot(ASTBlock.BLOCK_KEY) )
            {
                data = runtime.executeExpression( astElement, TheScope.getCurrentScope() );
                TheContext.getCurrentContext().setSelf( self ); // todo should this go inside execute expr method?
            }
        }
        return (GridPrototype) data;
    }

    public boolean isClosure()
    {
        return true;
    }

    public GridScope getScope()
    {
        return scope;
    }

    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder();
        str.append( "\n{\n" );
        if ( astBlock != null )
        {
            for ( ASTElement astElement : (GridList<ASTElement>) astBlock.getSlot( ASTBlock.BLOCK_KEY ) )
            {
                str.append( astElement.toString() );
                str.append( "\n" );
            }
        }
        str.append( "}\n" );
        return str.toString();
    }
}
