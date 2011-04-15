/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin;

import com.brweber2.protogrid.evaluator.Bootstrap;
import com.brweber2.protogrid.evaluator.TheContext;
import com.brweber2.protogrid.evaluator.TheScope;
import com.brweber2.protogrid.evaluator.builtin.empty.EmptyBuiltInParameters;
import com.brweber2.protogrid.ssa.GridArgument;
import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridBlock;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.data.BaseDataFunction;
import com.brweber2.protogrid.ssa.data.DataParameters;

/**
 * @version $Rev$
 */
public class BuiltInTry extends BaseDataFunction
{
    public BuiltInTry( GridPrototype parentProto )
    {
        super( parentProto, new DataParameters( parentProto,
            new EmptyBuiltInParameters()
//            new BuiltInParameters( new ASTParameters( Arrays.asList(
//                new ASTParameter( new ASTToken("__try_block__") ),
//                new ASTParameter( new ASTToken("__finally_block__") )
//            )))
        ) );
    }

    /*

    try( { } { } ) returns an object that can be pattern matched for

     */

    public GridPrototype apply( GridPrototype self, GridArguments args )
    {
        int nbrArgs = args.getSize();
        if ( !( nbrArgs == 1 || nbrArgs == 2) )
        {
            throw new RuntimeException( "try takes a block to try and an optional block to always execute" );
        }
        BuiltInTryResult tryResult = new BuiltInTryResult(self);
        try
        {
            tryResult.setResult( realize( args.get( 0 ) ) );
        }
        catch ( Exception e )
        {
            tryResult.setException( e );
        }
        finally
        {
            if( args.getSize() == 2 )
            {
                realize( args.get( 1 ) ); // execute the block for side effects
            }
        }
        return TheContext.getCurrentContext().set( tryResult );
    }

    private GridPrototype realize( GridArgument arg )
    {
        GridPrototype p = arg.getValue();
        if ( p instanceof GridBlock )
        {
            GridPrototype self = super.self;
            GridBlock block = (GridBlock) p;
            if ( block.hasSlot( Bootstrap.getThisToken() ) )
            {
                self = block.getSlot( Bootstrap.getThisToken() );
            }
            if ( block.isClosure() && block.getScope() != TheScope.getCurrentScope() )
            {
                TheScope.pushNewScope(block);
            }
            else
            {
                TheScope.pushNewScope();
            }
            try
            {
                TheScope.getCurrentScope().defineOrSet( Bootstrap.getThisToken(), self );
                GridPrototype theResult = block.apply( self );
                return TheContext.getCurrentContext().set( theResult );
            }
            finally
            {
                TheScope.popCurrentScope();
            }
        }
        else
        {
            return p;
        }
    }
}
