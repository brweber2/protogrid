/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin;

import com.brweber2.protogrid.evaluator.builtin.empty.EmptyBuiltInParameters;
import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.NativeString;
import com.brweber2.protogrid.ssa.data.BaseDataFunction;
import com.brweber2.protogrid.ssa.data.DataParameters;

/**
 * @version $Rev$
 */
public class BuiltInStringInterpolation extends BaseDataFunction
{
    private NativeString str;

    public BuiltInStringInterpolation( GridPrototype parentProto, NativeString str )
    {
        super( parentProto, new DataParameters( parentProto,  new EmptyBuiltInParameters() ) );
        this.str = str;
    }

    public GridPrototype apply( GridPrototype self, GridArguments args )
    {
//        String.format(  );
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
