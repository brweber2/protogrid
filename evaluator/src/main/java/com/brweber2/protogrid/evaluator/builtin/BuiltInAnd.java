/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin;

import com.brweber2.protogrid.ssa.GridArgument;
import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.data.DataArguments;

/**
 * @version $Rev$
 */
public class BuiltInAnd extends BuiltInLogic
{
    public BuiltInAnd( GridPrototype parentProto )
    {
        super( parentProto );
    }

    public GridPrototype apply( GridPrototype self, final GridArguments args )
    {
        if ( args.getSize() != 2 )
        {
            throw new RuntimeException( "and function takes two arguments" );
        }
        return new BuiltInIf( parentProto ).apply( this, new DataArguments( parentProto, getNativeArgs( args.get( 0 ),
                new GridArgument()
                {
                    public GridPrototype getValue()
                    {
                        return new BuiltInIf( parentProto ).apply( BuiltInAnd.this, new DataArguments( parentProto, getNativeArgs( args.get( 1 ), getTrue(), getFalse() ) ) );
                    }

                    public GridPrototype getValue( boolean forceEvaluation )
                    {
                        return getValue();
                    }

                    public Object getSource()
                    {
                        return null; // todo ok?
                    }
                }, getFalse() ) ) );
    }

}
