/*
 * $Id$
 * Copyright (C) 2011 brweber
 */
package com.brweber2.protogrid.ssa.data;

import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.NativeWrapField;

/**
 * @version $Rev$
 */
public class DataWrapField extends BaseDataPrototype
{
    private NativeWrapField field;

    public DataWrapField( GridPrototype parentProto, NativeWrapField field )
    {
        super( parentProto, "Field" );
        this.field = field;
    }

    public NativeWrapField getField()
    {
        return field;
    }

    public void setField( NativeWrapField field )
    {
        this.field = field;
    }
}
