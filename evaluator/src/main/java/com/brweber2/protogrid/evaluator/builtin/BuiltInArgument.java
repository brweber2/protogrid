/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin;

import com.brweber2.protogrid.ast.ASTElement;
import com.brweber2.protogrid.evaluator.ProtogridRuntime;
import com.brweber2.protogrid.ssa.GridArgument;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.GridScope;
import com.brweber2.protogrid.ssa.NativeArgument;

/**
 * @version $Rev$
 */
public class BuiltInArgument implements GridArgument, NativeArgument
{
    private ProtogridRuntime runtime;
    private GridScope scope;
    private ASTElement astElement;
    private boolean evaluated;
    private GridPrototype value;

    public BuiltInArgument( ASTElement astElement, ProtogridRuntime runtime, GridScope scope )
    {
        this.astElement = astElement;
        this.runtime = runtime;
        this.scope = scope;
    }

    public ASTElement getRawAstElement()
    {
        return astElement;
    }

    public void setRawAstElement( ASTElement astElement )
    {
        this.astElement = astElement;
    }

    public GridPrototype getValue()
    {
        return getValue( false );
    }

    public GridPrototype getValue ( boolean force )
    {
        if ( force || !evaluated )
        {
            value = (GridPrototype) runtime.executeArgument( astElement, scope );
            evaluated = true;
        }
        return value;
    }

    public Object getSource()
    {
        return astElement;
    }

    public String toString()
    {
        return getValue().toString(); // todo safe to do?
    }
}
