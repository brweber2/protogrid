/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa.data;

import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.GridToken;
import com.brweber2.protogrid.ssa.NativeParameters;
import com.brweber2.protogrid.ssa.NativeWrapMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * @version $Rev$
 */
public class DataWrapMethod extends BaseDataFunction
{
    private NativeWrapMethod method;

    public DataWrapMethod( GridPrototype parentProto, NativeWrapMethod method )
    {
        super( parentProto, new DataParameters( parentProto, new NativeParameters()
        {
            public void map( GridArguments args )
            {

            }

            public Map<GridToken, GridPrototype> getMappedParameters()
            {
                return new HashMap<GridToken,GridPrototype>();
            }
        } ) );
        this.method = method;
    }

    public NativeWrapMethod getMethod()
    {
        return method;
    }

    public void setMethod( NativeWrapMethod method )
    {
        this.method = method;
    }

    public GridPrototype apply( GridPrototype self, GridArguments args )
    {
        return method.call( args );
    }
}
