/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin;

import com.brweber2.protogrid.evaluator.builtin.empty.EmptyBuiltInParameters;
import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridFunction;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.NativeBoolean;
import com.brweber2.protogrid.ssa.data.BaseDataFunction;
import com.brweber2.protogrid.ssa.data.DataNil;
import com.brweber2.protogrid.ssa.data.DataParameters;

/**
 * @version $Rev$
 */
public class BuiltInIf extends BaseDataFunction implements GridFunction
{
    public BuiltInIf( GridPrototype parentProto )
    {
        super( parentProto, new DataParameters(parentProto,
                new EmptyBuiltInParameters()
//                new BuiltInParameters( new ASTParameters( Arrays.asList(
//                        new ASTParameter( new ASTToken( "__cond__" ) ),
//                        new ASTParameter( new ASTToken( "__trueBlock__" ) ),
//                        new ASTParameter( new ASTToken( "__falseBlock__" ) )
//                ) ) )
        ) );
    }

    public GridPrototype apply( GridPrototype self, GridArguments args )
    {
        if ( args.getSize() != 3 )
        {
            throw new RuntimeException( "native 'if' function takes only 3 parameter, the condition, the true expression and the false expression." );
        }
        GridPrototype cond = args.get( 0 ).getValue();
        boolean truthy;
        if ( cond instanceof NativeBoolean )
        {
            NativeBoolean condition = (NativeBoolean) cond;
            truthy = condition.getNative();
        }
        else
        {
            truthy = !( cond == DataNil.NIL || cond == DataNil.UNDEFINED );
        }
        if ( truthy )
        {
            return args.get( 1 ).getValue();
        }
        else
        {
            return args.get( 2 ).getValue();
        }
    }
}
