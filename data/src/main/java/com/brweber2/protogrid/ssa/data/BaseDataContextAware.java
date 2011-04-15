/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa.data;

import com.brweber2.protogrid.ssa.GridPrototype;

/**
 * @version $Rev$
 */
public abstract class BaseDataContextAware extends BaseDataPrototype
{
    protected GridPrototype self;

    public BaseDataContextAware( GridPrototype parentProto, String typeName )
    {
        super( parentProto, typeName );
    }

    public GridPrototype getContext()
    {
        return self;
    }

    public boolean hasContext()
    {
        return self != null;
    }

    public void setContext( GridPrototype self )
    {
        this.self = self;
    }
}
