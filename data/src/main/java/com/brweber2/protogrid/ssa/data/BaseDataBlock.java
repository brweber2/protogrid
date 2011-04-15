/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa.data;

import com.brweber2.protogrid.ssa.GridBlock;
import com.brweber2.protogrid.ssa.GridPrototype;

/**
 * @version $Rev$
 */
public abstract class BaseDataBlock extends BaseDataContextAware implements GridBlock
{
    protected BaseDataBlock( GridPrototype parentProto )
    {
        super( parentProto, "Block" );
    }
}
