/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ast;

import com.brweber2.protogrid.ssa.GridToken;
import com.brweber2.protogrid.ssa.data.DataToken;

/**
 * @version $Rev$
 */
public class ASTSlotAccess extends ASTBase
{
    public static final GridToken SLOT_TARGET_KEY = new DataToken("slotTarget");
    public static final GridToken SLOT_NAME_KEY = new DataToken("slotName");

    public ASTSlotAccess( SourceCodeInfo sourceCodeInfo, ASTElement target, ASTToken slotName )
    {
        super( sourceCodeInfo, ASTSlotAccess.class.getName() );
        setSlot( SLOT_TARGET_KEY, target );
        setSlot( SLOT_NAME_KEY, slotName );
    }

    public String toString()
    {
        return " " + getSlot( SLOT_TARGET_KEY ) + "." + getSlot( SLOT_NAME_KEY ) + " ";
    }
}
