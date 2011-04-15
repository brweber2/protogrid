/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ast;

import com.brweber2.protogrid.ssa.GridToken;
import com.brweber2.protogrid.ssa.NativeString;
import com.brweber2.protogrid.ssa.WrapNative;
import com.brweber2.protogrid.ssa.data.DataToken;

/**
 * @version $Rev$
 */
public class ASTVarDefine extends ASTBase implements WrapNative<String>
{
    public static final GridToken VAR_DEFINE_KEY = new DataToken("var");

    public ASTVarDefine( SourceCodeInfo sourceCodeInfo, ASTToken varName )
    {
        super( sourceCodeInfo, ASTToken.class.getName() );
        setSlot( VAR_DEFINE_KEY, varName );
    }

    public String toString()
    {
        return " var " + getNative() + " ";
    }

    public String getNative()
    {
        return ((NativeString)getSlot( VAR_DEFINE_KEY )).getNative();
    }
}
