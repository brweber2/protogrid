/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.repl;

import com.brweber2.protogrid.ast.ASTParameter;
import com.brweber2.protogrid.ast.ASTParameters;
import com.brweber2.protogrid.evaluator.Bootstrap;
import com.brweber2.protogrid.evaluator.TheScope;
import com.brweber2.protogrid.evaluator.builtin.BuiltInFunction;
import com.brweber2.protogrid.evaluator.builtin.BuiltInParameters;
import com.brweber2.protogrid.ssa.GridBlock;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.GridScope;
import com.brweber2.protogrid.ssa.NativeBlock;
import com.brweber2.protogrid.ssa.data.DataFunction;
import com.brweber2.protogrid.ssa.data.DataNil;

import java.io.PrintStream;
import java.util.Collections;

/**
 * @version $Rev$
 */
public class HelpFunction extends DataFunction
{
    public HelpFunction( final PrintStream console )
    {
        super( TheScope.getCurrentScope().resolveToken( Bootstrap.getFunctionToken() ),
                new BuiltInFunction(
                        new BuiltInParameters( new ASTParameters( Bootstrap.noSource, Collections.<ASTParameter>emptyList() ) ),
                        new NativeBlock()
                        {
                            public GridPrototype invoke( GridBlock block, GridPrototype self )
                            {
                                console.println( "Type source code and hit 'enter', it is a REPL for crying out loud.\n\n\texit() to get out of here\n\thelp() to view this message." );
                                return DataNil.NIL;
                            }

                            public boolean isClosure()
                            {
                                return false;
                            }

                            public GridScope getScope()
                            {
                                throw new RuntimeException( "You shouldn't be calling this!" );
                            }
                        }
                ) );

    }

}
