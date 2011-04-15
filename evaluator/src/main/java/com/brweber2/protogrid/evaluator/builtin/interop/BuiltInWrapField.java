/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin.interop;

import com.brweber2.protogrid.evaluator.Bootstrap;
import com.brweber2.protogrid.evaluator.Coerce;
import com.brweber2.protogrid.evaluator.TheScope;
import com.brweber2.protogrid.evaluator.builtin.empty.EmptyBuiltInParameters;
import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.NativeWrapField;
import com.brweber2.protogrid.ssa.data.BaseDataFunction;
import com.brweber2.protogrid.ssa.data.DataParameters;

import java.lang.reflect.Field;

/**
 * @version $Rev$
 */
public class BuiltInWrapField extends BaseDataFunction implements NativeWrapField
{
    private Field field;
    private Object instance;

    public BuiltInWrapField( Object instance, Field field )
    {
        super( TheScope.getCurrentScope().resolveToken( Bootstrap.getWrapFieldToken() ), new DataParameters( null,
                new EmptyBuiltInParameters()
//                new BuiltInParameters( new ASTParameters( Arrays.asList( new ASTParameter( new ASTToken( "__instance__" ) )) ) )
        ) );
        this.field = field;
        this.instance = instance;
    }

    public Field getField()
    {
        return field;
    }

    public void setField( Field field )
    {
        this.field = field;
    }

    public GridPrototype apply( GridPrototype self, GridArguments args )
    {
        try
        {
            return Coerce.coerceToGrid( field.get( instance ) );
        }
        catch ( IllegalAccessException e )
        {
            throw new RuntimeException( "Can't get value for field.", e );
        }
    }

    public Field getNative()
    {
        return getField();
    }
}
