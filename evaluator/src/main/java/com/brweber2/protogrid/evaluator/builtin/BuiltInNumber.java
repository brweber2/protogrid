/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin;

import com.brweber2.protogrid.ast.ASTParameter;
import com.brweber2.protogrid.ast.ASTParameters;
import com.brweber2.protogrid.evaluator.Bootstrap;
import com.brweber2.protogrid.evaluator.TheScope;
import com.brweber2.protogrid.ssa.GridBlock;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.GridScope;
import com.brweber2.protogrid.ssa.NativeBlock;
import com.brweber2.protogrid.ssa.NativeFunction;
import com.brweber2.protogrid.ssa.data.BaseDataPrototype;
import com.brweber2.protogrid.ssa.data.DataFunction;
import com.brweber2.protogrid.ssa.data.DataToken;

import java.util.Collections;

/**
 * @version $Rev$
 */
public class BuiltInNumber extends BaseDataPrototype
{
    public BuiltInNumber( GridPrototype parentProto )
    {
        super( parentProto, Bootstrap.NUMBER_STRING );
    }

    public BuiltInNumber()
    {
        super( TheScope.getCurrentScope().resolveToken( Bootstrap.getObjectToken() ), Bootstrap.NUMBER_STRING );
    }

    private void init()
    {
        this.defineAndAssignSlot( new DataToken( "add" ), new DataFunction(
                getParentProto(),
                getAddFunction()
        ) );

        // todo add impls all over this method
        this.defineSlot( new DataToken( "subtract" ) );
        this.defineSlot( new DataToken( "multiply" ) );
        this.defineSlot( new DataToken( "divide" ) );
        this.defineSlot( new DataToken( "modulus" ) );
        this.defineSlot( new DataToken( "exponent" ) );
        this.defineSlot( new DataToken( "before-decimal" ) );
        this.defineSlot( new DataToken( "after-decimal" ) );
    }

    private NativeFunction getAddFunction()
    {
        NativeBlock block = new NativeBlock()
        {
            public GridPrototype invoke( GridBlock block, GridPrototype self )
            {
                return null; // todo
            }

            public boolean isClosure()
            {
                return false;
            }

            public GridScope getScope()
            {
                throw new RuntimeException( "You shouldn't be calling this!" );
            }
        };

        return new BuiltInFunction(
                new BuiltInParameters( new ASTParameters( Bootstrap.noSource, Collections.<ASTParameter>emptyList() ) ),  // make it take params!
                block );
    }
}
