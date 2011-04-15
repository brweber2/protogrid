/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin;

import com.brweber2.protogrid.ast.ASTParameter;
import com.brweber2.protogrid.ast.ASTParameters;
import com.brweber2.protogrid.ast.ASTToken;
import com.brweber2.protogrid.evaluator.Bootstrap;
import com.brweber2.protogrid.evaluator.TheScope;
import com.brweber2.protogrid.ssa.GridArgument;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.NativeArguments;
import com.brweber2.protogrid.ssa.NativeLogic;
import com.brweber2.protogrid.ssa.data.BaseDataFunction;
import com.brweber2.protogrid.ssa.data.DataParameters;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @version $Rev$
 */
public abstract class BuiltInLogic extends BaseDataFunction implements NativeLogic
{
    protected BuiltInLogic( GridPrototype parentProto )
    {
        super( parentProto, new DataParameters( parentProto, new BuiltInParameters( new ASTParameters( Bootstrap.noSource, Arrays.asList(
                new ASTParameter( Bootstrap.noSource, new ASTToken( Bootstrap.noSource, "__cond__" ) ),
                new ASTParameter( Bootstrap.noSource, new ASTToken( Bootstrap.noSource, "__trueArg__" ) ),
                new ASTParameter( Bootstrap.noSource, new ASTToken( Bootstrap.noSource, "__falseArg__" ) ) ) ) ) ) );
    }

    protected NativeArguments getNativeArgs( final GridArgument condArg, final GridArgument trueArg, final GridArgument falseArg )
    {
        return new NativeArguments()
        {
            public int getSize()
            {
                return 3;
            }

            public GridArgument get( int i )
            {
                switch ( i )
                {
                    case 0:
                        return condArg;
                    case 1:
                        return trueArg;
                    case 2:
                        return falseArg;
                }
                throw new RuntimeException( "Invalid argument index " + i );
            }

            public Iterator<GridArgument> iterator()
            {
                return new Iterator<GridArgument>()
                {
                    int index = 0;

                    public boolean hasNext()
                    {
                        return index < 3;
                    }

                    public GridArgument next()
                    {
                        GridArgument result = get( index );
                        index++;
                        return result;
                    }

                    public void remove()
                    {
                        throw new UnsupportedOperationException( "Can't remove from this iterator..." );
                    }
                };
            }
        };
    }

    protected GridArgument getTrue()
    {
        return new GridArgument()
        {
            public GridPrototype getValue()
            {
                return TheScope.getCurrentScope().resolveToken( Bootstrap.getTrueToken() );
            }

            public GridPrototype getValue( boolean forceEvaluation )
            {
                return getValue();
            }

            public Object getSource()
            {
                return null; // todo ok?
            }
        };
    }

    protected GridArgument getFalse()
    {
        return new GridArgument()
        {
            public GridPrototype getValue()
            {
                return TheScope.getCurrentScope().resolveToken( Bootstrap.getFalseToken() );
            }

            public GridPrototype getValue( boolean forceEvaluation )
            {
                return getValue();
            }

            public Object getSource()
            {
                return null;  // todo ok?
            }
        };
    }

}
