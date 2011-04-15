/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin;

import com.brweber2.protogrid.ast.ASTParameter;
import com.brweber2.protogrid.ast.ASTParameters;
import com.brweber2.protogrid.ast.ASTToken;
import com.brweber2.protogrid.evaluator.Bootstrap;
import com.brweber2.protogrid.evaluator.TheContext;
import com.brweber2.protogrid.evaluator.TheScope;
import com.brweber2.protogrid.ssa.GridArgument;
import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridFunction;
import com.brweber2.protogrid.ssa.GridList;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.GridToken;
import com.brweber2.protogrid.ssa.data.BaseDataFunction;
import com.brweber2.protogrid.ssa.data.DataArguments;
import com.brweber2.protogrid.ssa.data.DataNil;
import com.brweber2.protogrid.ssa.data.DataNumber;
import com.brweber2.protogrid.ssa.data.DataParameters;
import com.brweber2.protogrid.ssa.data.DataToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @version $Rev$
 */
public class BuiltInLoop extends BaseDataFunction
{
    public BuiltInLoop( GridPrototype parentProto )
    {
        super( parentProto, new DataParameters( parentProto,
//                new EmptyBuiltInParameters()
            new BuiltInParameters( new ASTParameters( Bootstrap.noSource, Arrays.asList(
                    new ASTParameter(Bootstrap.noSource, new ASTToken(Bootstrap.noSource, "__iterable__" ) ),
                    new ASTParameter(Bootstrap.noSource, new ASTToken(Bootstrap.noSource, "__function__" ) )
            ) ) )
        ) );
    }

    public GridPrototype apply( GridPrototype self, final GridArguments args )
    {
        if ( args.getSize() != 2 )
        {
            throw new RuntimeException( "loop takes the object to loop over and a function (of 2 params - object and index)." );
        }
        final GridList list = (GridList) TheScope.getCurrentScope().resolveToken( new DataToken("__iterable__") );
        GridFunction func = (GridFunction) TheScope.getCurrentScope().resolveToken( new DataToken("__function__") );
//        final GridList list = (GridList) args.get( 0 ).getValue();
//        GridFunction func = (GridFunction) args.get( 1 ).getValue();

        GridPrototype last = DataNil.NIL;
        for ( int i = 0; i < list.getSize(); i++ )
        {
//            System.out.println("in loop index " + i);
            final int index = i;
            final List<GridArgument> arguments = new ArrayList<GridArgument>();
            arguments.add( new GridArgument()
            {
                public GridPrototype getValue()
                {
                    return list.get( index );
                }

                public GridPrototype getValue( boolean forceEvaluation )
                {
                    return getValue();
                }

                public Object getSource()
                {
                    return list.get( index ).toString();
                }
            } );
            arguments.add( new GridArgument()
            {
                public GridPrototype getValue()
                {
                    return new DataNumber( TheScope.getCurrentScope().resolveToken( Bootstrap.getNumberToken() ), "" + index );
                }

                public GridPrototype getValue( boolean forceEvaluation )
                {
                    return getValue();
                }

                public Object getSource()
                {
                    return "" + index;
                }
            } );
            GridArguments gridArguments = new DataArguments(null,null)
            {
                public int getSize()
                {
                    return arguments.size();
                }

                public GridArgument get( int i )
                {
//                    System.out.println("getting " + arguments.get(i));
                    return arguments.get( i );
                }

                public Iterator<GridArgument> iterator()
                {
                    return arguments.iterator();
                }
            };
            // todo this now duplicates code from the Runtime...
            func.getParameters().map( gridArguments );
            if ( func.hasSlot( Bootstrap.getThisToken() ) )
            {
                self = func.getSlot( Bootstrap.getThisToken() );
            }
            TheScope.pushFunctionScope();
            try
            {
                TheScope.getCurrentScope().defineAndSet( Bootstrap.getThisToken(), self );
                for ( GridToken token : func.getParameters().getMappedParameters().keySet() )
                {
                    GridPrototype value = func.getParameters().getMappedParameters().get( token );
//                    System.out.println("setting " + token + " to " + value );
                    TheScope.getCurrentScope().defineAndSet( token, value );
                }
//                System.out.println("applying function " + func);
                last = func.apply( self, gridArguments );
            }
            finally {
                TheScope.popCurrentScope();
            }
        }
        return TheContext.getCurrentContext().set( last );
    }
}
