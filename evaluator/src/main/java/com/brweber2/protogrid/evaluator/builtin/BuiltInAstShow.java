/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin;

import com.brweber2.protogrid.evaluator.builtin.empty.EmptyBuiltInParameters;
import com.brweber2.protogrid.ssa.GridArgument;
import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridFunction;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.data.BaseDataFunction;
import com.brweber2.protogrid.ssa.data.DataNil;
import com.brweber2.protogrid.ssa.data.DataParameters;

/**
 * @version $Rev$
 */
public class BuiltInAstShow extends BaseDataFunction implements GridFunction
{
    public BuiltInAstShow( GridPrototype parentProto )
    {
        super( parentProto, new DataParameters( parentProto, new EmptyBuiltInParameters() ) );
    }

    public GridPrototype apply( GridPrototype self, GridArguments args )
    {
        // todo we should probably return a list of the items, but...
        for ( GridArgument arg : args )
        {
            System.out.println( arg.getSource() );
        }
        return DataNil.NIL;
    }

}
