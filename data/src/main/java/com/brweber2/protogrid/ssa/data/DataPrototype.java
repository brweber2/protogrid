/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa.data;

import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.GridToken;

import java.util.Map;

/**
 * @version $Rev$
 */
public class DataPrototype extends BaseDataPrototype
{
    public DataPrototype( GridPrototype parentProto, String typeName )
    {
        super( parentProto, typeName );
    }

    public DataPrototype( GridPrototype parentProto, String typeName, Map<GridToken,GridPrototype> map )
    {
        super( parentProto, typeName );
        for ( GridToken key : map.keySet() )
        {
            setSlot( key, map.get(key) );
        }
    }

}
