/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin;

import com.brweber2.protogrid.evaluator.Bootstrap;
import com.brweber2.protogrid.evaluator.Coerce;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.data.BaseDataPrototype;
import com.brweber2.protogrid.ssa.data.DataBoolean;
import com.brweber2.protogrid.ssa.data.DataToken;

/**
 * @version $Rev$
 */
public class BuiltInTryResult extends BaseDataPrototype
{
    Object result = null;
    Exception exception = null;

    public BuiltInTryResult( GridPrototype parentProto )
    {
        super( parentProto, "TryResult" );
    }

    public void setResult( Object result )
    {
        this.result = result;
        setSlot( Bootstrap.getTryResultToken(), Coerce.coerceToGrid( result ) );
        setSlot( new DataToken("hasError"), new DataBoolean( parentProto, false ) );
    }

    public void setException( Exception exception )
    {
        this.exception = exception;
        setSlot( Bootstrap.getTryExceptionToken(), Coerce.coerceToGrid( exception ) );
        setSlot( new DataToken("hasError"), new DataBoolean( parentProto, true ) );
    }
}
