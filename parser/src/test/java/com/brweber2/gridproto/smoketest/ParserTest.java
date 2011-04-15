/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.gridproto.smoketest;

import com.brweber2.protogrid.ast.ASTArguments;
import com.brweber2.protogrid.ast.ASTAssign;
import com.brweber2.protogrid.ast.ASTElement;
import com.brweber2.protogrid.ast.ASTInvocation;
import com.brweber2.protogrid.ast.ASTToken;
import com.brweber2.protogrid.ast.ASTVarDefine;
import com.brweber2.protogrid.parser.FileSourceCode;
import com.brweber2.protogrid.parser.ParserStack;
import com.brweber2.protogrid.parser.ProtogridParser;
import com.brweber2.protogrid.ssa.GridList;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.Deque;

/**
 * @version $Rev$
 */
public class ParserTest
{
    @Test
    public void debugHello()
    {
        String src = "debug(\"hello\") ";
        Reader reader = new StringReader( src );
        FileSourceCode source = new FileSourceCode( "thesource", reader );

        ProtogridParser parser = new ProtogridParser();
        ParserStack parserStack = parser.parseAll( source );

        Deque<ASTElement> ast = parserStack.finishParsing();

        // todo verify contents of ast!
        System.out.println( "end of unit test" );
    }

    @Test
    public void testSimpleParse()
    {
        /*
        source;
            var obj = new(Object)

        AST


               slot assign (=)
             ---------------------
            |                    |
        var define           invocation
            |               ------------
          token            |           |
         ('obj')         token     arguments
                        ('new')        |
                                     token
                                   ('Object')

         */

        String src = "var obj = clone(Object) ";
        Reader reader = new StringReader( src );
        FileSourceCode source = new FileSourceCode( "unittestsource", reader );

        ProtogridParser parser = new ProtogridParser();
        ParserStack parserStack = parser.parseAll( source );

        Deque<ASTElement> ast = parserStack.finishParsing();

        Assert.assertNotNull( ast );
        Assert.assertEquals( ast.size(), 1 );
        ASTElement element = ast.pop();
        Assert.assertNotNull( element );
        Assert.assertTrue( element instanceof ASTAssign );
        ASTAssign assign = (ASTAssign) element;
        ASTElement leftHandSide = (ASTElement) assign.getSlot( ASTAssign.ASSIGN_LHS );
        ASTElement rightHandSide = (ASTElement) assign.getSlot( ASTAssign.ASSIGN_RHS );
        Assert.assertNotNull( assign.getComments() );

        Assert.assertTrue( leftHandSide instanceof ASTVarDefine );
        ASTVarDefine varDefine = (ASTVarDefine) leftHandSide;
        Assert.assertEquals( ((ASTToken)varDefine.getSlot(ASTVarDefine.VAR_DEFINE_KEY)).getToken(), "obj" );

        Assert.assertTrue( rightHandSide instanceof ASTInvocation );
        ASTInvocation invoc = (ASTInvocation) rightHandSide;
        Assert.assertNotNull( invoc.getSlot( ASTInvocation.INVOKE_TARGET_KEY ) );
        Assert.assertTrue( invoc.getSlot( ASTInvocation.INVOKE_TARGET_KEY ) instanceof ASTToken );
        Assert.assertEquals( ((ASTToken)invoc.getSlot( ASTInvocation.INVOKE_TARGET_KEY )).getToken(), "clone" );

        ASTArguments args = (ASTArguments) invoc.getSlot( ASTInvocation.INVOKE_ARGS_KEY );
        Assert.assertNotNull( args );
        Assert.assertEquals( ((GridList)args.getSlot(ASTInvocation.INVOKE_ARGS_KEY)).getSize(), 1 );
        Assert.assertTrue( ((GridList)args.getSlot(ASTInvocation.INVOKE_ARGS_KEY)).get( 0 ) instanceof ASTToken );
        Assert.assertEquals( ( (ASTToken) ((GridList)args.getSlot(ASTInvocation.INVOKE_ARGS_KEY)).get( 0 ) ).getToken(), "Object" );
    }
}
