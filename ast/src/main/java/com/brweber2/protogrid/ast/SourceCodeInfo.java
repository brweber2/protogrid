/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ast;

import java.math.BigInteger;

/**
 * @version $Rev$
 */
public class SourceCodeInfo implements LineNumberAware
{
    private String sourceName;
    private BigInteger startLineNmber = BigInteger.ZERO;
    private BigInteger endLineNumber = BigInteger.ZERO;

    public SourceCodeInfo( String sourceName, long startLineNumber )
    {
        this.startLineNmber = BigInteger.valueOf( startLineNumber );
        this.sourceName = sourceName;
    }

    public SourceCodeInfo( String sourceName, long startLineNumber, long endLineNumber )
    {
        this.startLineNmber = BigInteger.valueOf( startLineNumber );
        this.endLineNumber = BigInteger.valueOf( endLineNumber );
        this.sourceName = sourceName;
    }

    public String getSourceName()
    {
        return sourceName;
    }

    public BigInteger getStartLineNumber()
    {
        return startLineNmber;
    }

    public void setStartLineNumber( BigInteger i )
    {
        this.startLineNmber = i;
    }

    public BigInteger getEndLineNumber()
    {
        return endLineNumber;
    }

    public void setEndLineNumber( BigInteger i )
    {
        this.endLineNumber = i;
    }

    public void setSourceLines( BigInteger start, BigInteger end )
    {
        this.startLineNmber = start;
        this.endLineNumber = end;
    }
}
