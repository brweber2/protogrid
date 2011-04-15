/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin;

import com.brweber2.protogrid.evaluator.TheContext;
import com.brweber2.protogrid.evaluator.builtin.empty.EmptyBuiltInParameters;
import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridFunction;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.data.BaseDataFunction;
import com.brweber2.protogrid.ssa.data.DataParameters;

/**
 * @version $Rev$
 */
public class BuiltInThis extends BaseDataFunction implements GridFunction
{
    public BuiltInThis( GridPrototype parentProto )
    {
        super( parentProto, new DataParameters( parentProto, new EmptyBuiltInParameters() ) );
    }

    public GridPrototype apply( GridPrototype self, GridArguments args )
    {
        if ( args.iterator().hasNext() )
        {
            throw new RuntimeException( "this() does not take any arguments." );
        }
        return TheContext.getCurrentContext().getSelf();
    }
}
