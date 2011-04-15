/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin;

import com.brweber2.protogrid.evaluator.Bootstrap;
import com.brweber2.protogrid.evaluator.TheScope;
import com.brweber2.protogrid.evaluator.builtin.empty.EmptyBuiltInParameters;
import com.brweber2.protogrid.ssa.GridArgument;
import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridFunction;
import com.brweber2.protogrid.ssa.GridList;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.data.BaseDataFunction;
import com.brweber2.protogrid.ssa.data.DataList;
import com.brweber2.protogrid.ssa.data.DataNil;
import com.brweber2.protogrid.ssa.data.DataParameters;

/**
 * @version $Rev$
 */
public class BuiltInPrintln extends BaseDataFunction implements GridFunction
{
    private boolean printNewline;

    public BuiltInPrintln( GridPrototype proto, boolean printNewline )
    {
        super( proto, new DataParameters( proto, new EmptyBuiltInParameters() ) );
        this.printNewline = printNewline;
    }

    public GridPrototype apply( GridPrototype self, GridArguments args )
    {
        GridPrototype objProto = TheScope.getCurrentScope().resolveToken( Bootstrap.getObjectToken() );

        GridPrototype ret;
        if ( args.getSize() > 1 )
        {
            GridList list = new DataList( objProto );
            for ( GridArgument arg : args )
            {
                list.add( arg.getValue() );
            }
            ret = list;
        }
        else if ( args.getSize() <= 0 )
        {
            ret = DataNil.NIL;
        }
        else
        {
            ret = args.get( 0 ).getValue();
        }

        if ( printNewline )
        {
            System.out.println( ret );
        }
        else
        {
            System.out.print( ret );
        }

        return ret;
    }
}
