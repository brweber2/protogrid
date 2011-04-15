/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin;

import com.brweber2.protogrid.ast.ASTArguments;
import com.brweber2.protogrid.ast.ASTElement;
import com.brweber2.protogrid.evaluator.ProtogridRuntime;
import com.brweber2.protogrid.ssa.GridArgument;
import com.brweber2.protogrid.ssa.GridList;
import com.brweber2.protogrid.ssa.GridScope;
import com.brweber2.protogrid.ssa.NativeArguments;

import java.util.Iterator;

/**
 * @version $Rev$
 */
public class BuiltInArguments implements NativeArguments
{
    // todo add named parameters
    private ASTArguments astArguments;
    private ProtogridRuntime runtime;
    private GridScope scope;

    public BuiltInArguments( ASTArguments astArguments, ProtogridRuntime runtime, GridScope scope )
    {
        this.astArguments = astArguments;
        this.runtime = runtime;
        this.scope = scope;
    }

    public int getSize()
    {
        return ((GridList)astArguments.getSlot( ASTArguments.ARGUMENTS_KEY )).getSize();
    }

    public GridArgument get( int i )
    {
        if ( i >= getSize() )
        {
            throw new RuntimeException( "No such argument " + i );
        }
        return new BuiltInArgument( (ASTElement)((GridList)astArguments.getSlot( ASTArguments.ARGUMENTS_KEY)).get( i ), runtime, scope );
    }

    public Iterator<GridArgument> iterator()
    {
        final Iterator<ASTElement> astIterator = ((GridList<ASTElement>)astArguments.getSlot( ASTArguments.ARGUMENTS_KEY )).iterator();
        return new Iterator<GridArgument>()
        {
            public boolean hasNext()
            {
                return astIterator.hasNext();
            }

            public GridArgument next()
            {
                return new BuiltInArgument( astIterator.next(), runtime, scope );
            }

            public void remove()
            {
                astIterator.remove();
            }
        };
    }
}
