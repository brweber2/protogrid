/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin;

import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridBlock;
import com.brweber2.protogrid.ssa.GridFunction;
import com.brweber2.protogrid.ssa.GridParameters;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.NativeBlock;
import com.brweber2.protogrid.ssa.NativeFunction;
import com.brweber2.protogrid.ssa.NativeParameters;
import com.brweber2.protogrid.ssa.data.DataParameters;

/**
 * @version $Rev$
 */
public class BuiltInFunction implements NativeFunction
{
    private GridParameters parameters;
    private GridBlock gridBlock;
    private NativeBlock nativeBlock;

    public BuiltInFunction( NativeParameters params, NativeBlock nativeBlock )
    {
        this.parameters = new DataParameters( null, params );
        this.nativeBlock = nativeBlock;
    }

    public BuiltInFunction( GridParameters params, GridBlock gridBlock )
    {
        this.parameters = params;
        this.gridBlock = gridBlock;
    }

    public GridPrototype invoke( GridFunction function, GridPrototype self, GridArguments args )
    {
        GridPrototype result;
        if ( nativeBlock == null )
        {
            result = gridBlock.apply( self );
        }
        else
        {
            result = nativeBlock.invoke( null, self ); // todo! can't pass function b/c it isn't a block... ugghh
        }
        return result;
    }

    public GridParameters getParameters()
    {
        return parameters;
    }

    @Override
    public String toString()
    {
        if ( nativeBlock != null )
        {
            return parameters.toString() + nativeBlock.toString();
        }
        return parameters.toString() + gridBlock.toString();
    }
}
