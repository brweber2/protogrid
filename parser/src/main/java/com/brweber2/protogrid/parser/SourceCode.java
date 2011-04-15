/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.parser;

import com.brweber2.protogrid.ast.SourceCodeInfo;
import com.brweber2.protogrid.ast.SourceInfoAware;

import java.math.BigInteger;

/**
 * @version $Rev$
 */
public interface SourceCode
{
    void close();

    SourceCodeInfo createSourceInfo();

    BigInteger getLineNumber();

    boolean hasAnyNext( char[] chars );

    boolean tokenNext( String closeToken );

    boolean tokenNext( char closeToken );

    void consumeToken( String token );

    void consumeToken( char token );

    boolean tokenNext();

    String readToken();

    boolean stringNext();

    String readString();

    boolean numberNext();

    String readNumber();

    boolean varDefineNext();

    void consumeVarDefine();

    boolean assignNext();

    void consumeAssign();

    boolean slotAccessNext();

    void cosumeSlotAccess();

    boolean commentNext();

    String readComment();

    boolean operatorNext();

    String readOperator();

    boolean functionInvocationNext();

    boolean blockNext();

    boolean functionDefineNext();

    boolean atEOF();

    boolean readStatementEnd();

    void consumeStatementEnd();

    boolean comparisonNext();

    String readComparison();

    boolean logicNext();

    String readLogic();

    void endSourceInfo( SourceInfoAware sourceInfoAware );
}
