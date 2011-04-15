/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa.data;

import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.GridScope;
import com.brweber2.protogrid.ssa.NativeBlock;

/**
 * @version $Rev$
 */
public class DataBlock extends BaseDataBlock
{
    private NativeBlock nativeBlock;

    public DataBlock( GridPrototype parentProto, NativeBlock nativeBlock )
    {
        super( parentProto );
        this.nativeBlock = nativeBlock;
    }

    public GridPrototype apply( GridPrototype self )
    {
        if ( hasContext() )
        {
            self = getContext();
        }
        return nativeBlock.invoke( this, self );
    }

    public boolean isClosure()
    {
        return nativeBlock.isClosure();
    }

    public GridScope getScope()
    {
        return nativeBlock.getScope();
    }

    @Override
    public String toString()
    {
        return nativeBlock.toString();
    }
}
