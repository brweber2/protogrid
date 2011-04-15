/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.repl;

import com.brweber2.protogrid.evaluator.TheContext;
import com.brweber2.protogrid.evaluator.TheRuntime;
import com.brweber2.protogrid.evaluator.TheScope;
import com.brweber2.protogrid.evaluator.builtin.empty.EmptyBuiltInParameters;
import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.data.BaseDataFunction;
import com.brweber2.protogrid.ssa.data.DataNil;
import com.brweber2.protogrid.ssa.data.DataParameters;

import java.io.PrintStream;

/**
 * @version $Rev$
 */
public class ClearFunction extends BaseDataFunction
{
    private PrintStream console;

    public ClearFunction( GridPrototype parentProto, PrintStream console )
    {
        super( parentProto, new DataParameters( parentProto, new EmptyBuiltInParameters() ) );
        this.console = console;
    }

    public GridPrototype apply( GridPrototype self, GridArguments args )
    {
        TheRuntime.clearRuntime();
        TheContext.clearContext();
        TheScope.clearScope();
        TheScope.pushNewScope(); // we're in a function, it will want to pop the scope...

        GridRepl.registerReplFunctions( console, TheRuntime.getRuntime() );
        return DataNil.NIL;
    }
}
