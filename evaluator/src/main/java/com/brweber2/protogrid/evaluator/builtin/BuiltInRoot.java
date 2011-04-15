/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin;

import com.brweber2.protogrid.evaluator.Bootstrap;
import com.brweber2.protogrid.ssa.data.BaseDataPrototype;

import java.util.UUID;

/**
 * @version $Rev$
 */
public class BuiltInRoot extends BaseDataPrototype
{
    public static final String uuid = UUID.randomUUID().toString();

    public BuiltInRoot()
    {
        super( null, Bootstrap.OBJECT_STRING );
    }

    public boolean equals( Object o )
    {
        return o instanceof BuiltInRoot;
    }

    public int hashCode()
    {
        return uuid.hashCode();
    }

    public String toString()
    {
        return Bootstrap.OBJECT_STRING;
    }

    @Override
    public String getTypeName()
    {
        return Bootstrap.OBJECT_STRING;
    }
}
