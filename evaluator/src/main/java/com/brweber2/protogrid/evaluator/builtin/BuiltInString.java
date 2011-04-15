/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin;

import com.brweber2.protogrid.ast.ASTParameter;
import com.brweber2.protogrid.ast.ASTParameters;
import com.brweber2.protogrid.evaluator.Bootstrap;
import com.brweber2.protogrid.evaluator.TheScope;
import com.brweber2.protogrid.ssa.GridBlock;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.GridScope;
import com.brweber2.protogrid.ssa.NativeBlock;
import com.brweber2.protogrid.ssa.NativeFunction;
import com.brweber2.protogrid.ssa.NativeString;
import com.brweber2.protogrid.ssa.data.BaseDataPrototype;
import com.brweber2.protogrid.ssa.data.DataFunction;
import com.brweber2.protogrid.ssa.data.DataNumber;
import com.brweber2.protogrid.ssa.data.DataString;
import com.brweber2.protogrid.ssa.data.DataToken;

import java.util.Collections;

/**
 * @version $Rev$
 */
public class BuiltInString extends BaseDataPrototype
{

    public BuiltInString( GridPrototype parentProto )
    {
        super( parentProto, Bootstrap.STRING_STRING );
        init();
    }

    public BuiltInString()
    {
        super( TheScope.getCurrentScope().resolveToken( Bootstrap.getObjectToken() ), Bootstrap.STRING_STRING );
        init();
    }

    private void init()
    {

        this.assignIfNotDefined( new DataToken( "reverse" ), getReverseFunction() );
        this.assignIfNotDefined( new DataToken( "length" ), getLengthFunction() );

        // todo fill out all of the methods
        this.defineIfNotDefined( new DataToken( "concat" ) );
        this.defineIfNotDefined( new DataToken( "substring" ) );
        this.defineIfNotDefined( new DataToken( "trim" ) );
        this.defineIfNotDefined( new DataToken( "sort" ) );
        this.defineIfNotDefined( new DataToken( "split" ) );
        this.defineIfNotDefined( new DataToken( "upper" ) );
        this.defineIfNotDefined( new DataToken( "lower" ) );
        this.defineIfNotDefined( new DataToken( "starts-with" ) );
        this.defineIfNotDefined( new DataToken( "ends-with" ) );
        this.defineIfNotDefined( new DataToken( "matches" ) );
        this.defineIfNotDefined( new DataToken( "contains" ) );
        this.defineIfNotDefined( new DataToken( "index-of" ) );
        this.defineIfNotDefined( new DataToken( "replace" ) );
        this.defineIfNotDefined( new DataToken( "partition" ) );
    }

    private com.brweber2.protogrid.ssa.GridFunction getLengthFunction()
    {
        NativeBlock nativeBlock = new NativeBlock()
        {
            public GridPrototype invoke( GridBlock block, GridPrototype self )
            {
                String value = ( (NativeString) self ).getNative();
                return new DataNumber( BuiltInString.this, "" + value.length() );
            }

            public boolean isClosure()
            {
                return false;
            }

            public GridScope getScope()
            {
                throw new RuntimeException( "You shouldn't be calling this!" );
            }
        };

        NativeFunction lengthBlock = new BuiltInFunction(
                new BuiltInParameters( new ASTParameters( Bootstrap.noSource, Collections.<ASTParameter>emptyList() ) ),
                nativeBlock );
        return new DataFunction(
                getParentProto(),
                lengthBlock );
    }

    private com.brweber2.protogrid.ssa.GridFunction getReverseFunction()
    {
        NativeBlock nativeBlock = new NativeBlock()
        {
            public GridPrototype invoke( GridBlock block, GridPrototype self )
            {
                String value = ( (NativeString) self ).getNative();
                StringBuilder builder = new StringBuilder();
                for ( int i = value.length(); i > 0; i-- )
                {
                    builder.append( value.charAt( i - 1 ) );
                }
                return new DataString( BuiltInString.this, builder.toString() );
            }

            public boolean isClosure()
            {
                return false;
            }

            public GridScope getScope()
            {
                throw new RuntimeException( "You shouldn't be calling this!" );
            }
        };

        NativeFunction reverseBlock = new BuiltInFunction(
                new BuiltInParameters( new ASTParameters( Bootstrap.noSource, Collections.<ASTParameter>emptyList() ) ),
                nativeBlock );
        return new DataFunction(
                getParentProto(),
                reverseBlock );
    }

}
