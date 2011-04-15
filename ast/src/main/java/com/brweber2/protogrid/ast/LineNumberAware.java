/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ast;

import java.math.BigInteger;

/**
 * @version $Rev$
 */
public interface LineNumberAware
{
    BigInteger getStartLineNumber();
    void setStartLineNumber( BigInteger i );

    BigInteger getEndLineNumber();
    void setEndLineNumber( BigInteger i );

    void setSourceLines( BigInteger start, BigInteger end );
}
