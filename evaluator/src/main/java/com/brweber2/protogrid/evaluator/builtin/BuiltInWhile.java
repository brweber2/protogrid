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
import com.brweber2.protogrid.ssa.GridBlock;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.NativeBoolean;
import com.brweber2.protogrid.ssa.data.BaseDataFunction;
import com.brweber2.protogrid.ssa.data.DataNil;
import com.brweber2.protogrid.ssa.data.DataParameters;

/**
 * @version $Rev$
 */
public class BuiltInWhile extends BaseDataFunction
{
    public BuiltInWhile( GridPrototype parentProto )
    {
        super( parentProto, new DataParameters( parentProto,
                new EmptyBuiltInParameters()
//            new BuiltInParameters( new ASTParameters( Arrays.asList(
//                new ASTParameter( new ASTToken( "__cond__" ) ),
//                new ASTParameter( new ASTToken( "__block__" ) )
//        ) ) )
        ) );
    }

    public GridPrototype apply( GridPrototype self, GridArguments args )
    {
        if ( args.getSize() != 2 )
        {
            throw new RuntimeException( "while takes a condition and a block, 0 arg function or expression" );
        }
        GridArgument cond = args.get( 0 );
        GridArgument expr = args.get( 1 );

        NativeBoolean more = (NativeBoolean) cond.getValue();
        if ( !more.getNative() )
        {
            return DataNil.NIL;
        }

        GridPrototype result = DataNil.NIL;
        GridPrototype initial = expr.getValue();

        if ( initial instanceof GridBlock )
        {
            GridBlock block = (GridBlock) initial;
            if ( block.isClosure() && block.getScope() != TheScope.getCurrentScope())
            {
                System.out.println("new scope... ok");
                TheScope.pushNewScope(block);
            }
            else
            {
                System.out.println("uh oh");
                System.out.println("closure? " + block.isClosure());
                System.out.println("comparing " + block.getScope() + " and " + TheScope.getCurrentScope());
                TheScope.pushNewScope();
            }
            try
            {
                result = loopIt( cond, expr );
            }
            catch ( RuntimeException e )
            {
                e.printStackTrace(); // this just helps soooo much for debugging....
                throw e;
            }
            finally
            {
                TheScope.popCurrentScope();
            }
        }
        else
        {
            result = loopIt( cond, expr );
        }


        return result;
    }

    private GridPrototype loopIt( GridArgument cond, GridArgument expr )
    {
        GridPrototype result = DataNil.NIL;
        NativeBoolean more = (NativeBoolean) cond.getValue();
        while ( more.getNative() )
        {
            System.out.println("looping...");
            result = realize( expr.getValue( true ) );
            System.out.println("got value " + result);
            more = (NativeBoolean) cond.getValue( true );
        }
        return result;
    }

    private GridPrototype realize( GridPrototype item )
    {
        if ( item instanceof GridBlock )
        {
            GridBlock block = (GridBlock) item;

            GridPrototype s = self;
            if ( block.hasSlot( Bootstrap.getThisToken() ) )
            {
                s = block.getSlot( Bootstrap.getThisToken() );
            }

            return block.apply( s );
        }
        // todo add support for 0 arg functions...
        return item;
    }

}
