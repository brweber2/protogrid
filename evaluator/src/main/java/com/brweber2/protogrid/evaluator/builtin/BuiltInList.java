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
import com.brweber2.protogrid.evaluator.builtin.empty.EmptyBuiltInParameters;
import com.brweber2.protogrid.evaluator.builtin.interop.BuiltInWrapInstance;
import com.brweber2.protogrid.ssa.GridArgument;
import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridBlock;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.GridScope;
import com.brweber2.protogrid.ssa.NativeBlock;
import com.brweber2.protogrid.ssa.NativeFunction;
import com.brweber2.protogrid.ssa.NativeList;
import com.brweber2.protogrid.ssa.NativeNumber;
import com.brweber2.protogrid.ssa.data.BaseDataFunction;
import com.brweber2.protogrid.ssa.data.DataFunction;
import com.brweber2.protogrid.ssa.data.DataList;
import com.brweber2.protogrid.ssa.data.DataNumber;
import com.brweber2.protogrid.ssa.data.DataParameters;
import com.brweber2.protogrid.ssa.data.DataToken;
import com.brweber2.protogrid.ssa.data.DataWrapInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @version $Rev$
 */
public class BuiltInList extends BaseDataFunction
{
    public BuiltInList( GridPrototype parentProto )
    {
        super( parentProto, new DataParameters( parentProto, new EmptyBuiltInParameters() ) );
        init();
    }

    public GridPrototype apply( GridPrototype self, GridArguments args )
    {
        List<GridPrototype> list = new ArrayList<GridPrototype>();
        for ( GridArgument arg : args )
        {
            list.add( arg.getValue() );
        }
        return new DataList( this, list );
    }

    private void init()
    {
        final GridPrototype func = getParentProto();

        NativeBlock toarrblock = new NativeBlock()
        {

            public GridPrototype invoke( GridBlock block, GridPrototype self )
            {
                List l = ( (NativeList) self ).getNative();
                return new DataWrapInstance( func, new BuiltInWrapInstance( l.toArray() ) );
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
        NativeFunction toarr = new BuiltInFunction(
            new BuiltInParameters( new ASTParameters( Bootstrap.noSource, new ArrayList<ASTParameter>() ) ),
            toarrblock
        );
        setSlot( new DataToken( "toarray" ), new DataFunction( func, toarr ) );

        NativeBlock addBlock = new NativeBlock()
        {
            public GridPrototype invoke( GridBlock block, GridPrototype self )
            {
                List l = ( (NativeList) self ).getNative();
                GridPrototype value = TheScope.getCurrentScope().resolveToken( new DataToken( "value" ) );
                l.add( value );
                return self;
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
        NativeFunction addFunc = new BuiltInFunction(
                new BuiltInParameters( new ASTParameters( Bootstrap.noSource, Arrays.asList( new ASTParameter( Bootstrap.noSource, new ASTToken(Bootstrap.noSource,  "value" ) ) ) ) ),
                addBlock
        );
        setSlot( new DataToken( "add" ), new DataFunction( func, addFunc ) );

        NativeBlock getBlock = new NativeBlock()
        {
            public GridPrototype invoke( GridBlock block, GridPrototype self )
            {
                List l = ( (NativeList) self ).getNative();
                NativeNumber index = (NativeNumber) TheScope.getCurrentScope().resolveToken( new DataToken( "index" ) );
                return (GridPrototype) l.get( Integer.parseInt( index.getNative() ) );
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
        NativeFunction getFunc = new BuiltInFunction(
                new BuiltInParameters( new ASTParameters( Bootstrap.noSource, Arrays.asList( new ASTParameter( Bootstrap.noSource, new ASTToken(Bootstrap.noSource,  "index" ) ) ) ) ),
                getBlock
        );
        setSlot( new DataToken( "get" ), new DataFunction( func, getFunc ) );

        NativeBlock deleteBlock = new NativeBlock()
        {
            public GridPrototype invoke( GridBlock block, GridPrototype self )
            {
                List l = ( (NativeList) self ).getNative();
                NativeNumber index = (NativeNumber) TheScope.getCurrentScope().resolveToken( new DataToken( "index" ) );
                return (GridPrototype) l.remove( Integer.parseInt( index.getNative() ) );
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
        NativeFunction deleteFunc = new BuiltInFunction(
                new BuiltInParameters( new ASTParameters( Bootstrap.noSource, Arrays.asList( new ASTParameter( Bootstrap.noSource, new ASTToken(Bootstrap.noSource,  "index" ) ) ) ) ),
                deleteBlock
        );
        DataFunction removeFunction = new DataFunction( func, deleteFunc );
        setSlot( new DataToken( "delete" ), removeFunction );
        setSlot( new DataToken( "remove" ), removeFunction );

        NativeBlock sizeBlock = new NativeBlock()
        {
            public GridPrototype invoke( GridBlock block, GridPrototype self )
            {
                List l = ( (NativeList) self ).getNative();
                GridPrototype nbr = TheScope.getCurrentScope().resolveToken( Bootstrap.getNumberToken() );
                return new DataNumber( nbr, "" + l.size() );
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
        NativeFunction sizeFunc = new BuiltInFunction(
                new BuiltInParameters( new ASTParameters(Bootstrap.noSource,  Collections.<ASTParameter>emptyList() ) ),
                sizeBlock
        );
        setSlot( new DataToken( "size" ), new DataFunction( func, sizeFunc ) );
    }

}
