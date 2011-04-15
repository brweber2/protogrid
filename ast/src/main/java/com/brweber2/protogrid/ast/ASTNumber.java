/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ast;

import com.brweber2.protogrid.ssa.GridToken;
import com.brweber2.protogrid.ssa.NativeNumber;
import com.brweber2.protogrid.ssa.WrapNative;
import com.brweber2.protogrid.ssa.data.DataNumber;
import com.brweber2.protogrid.ssa.data.DataToken;

/**
 * @version $Rev$
 */
public class ASTNumber extends ASTBase implements WrapNative<String>
{
    private static final GridToken NUMBER_KEY = new DataToken("number");

    public ASTNumber( SourceCodeInfo sourceCodeInfo, String number )
    {
        super( sourceCodeInfo, ASTNumber.class.getName() );
        setSlot( NUMBER_KEY, new DataNumber( number ) );
    }

    public String toString()
    {
        return " " + getNative() + " ";
    }

    public String getNative()
    {
        return ((NativeNumber) getSlot( NUMBER_KEY )).getNative();
    }
}
