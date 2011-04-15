/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.smoketest;

import org.testng.annotations.Test;


/**
 * @version $Rev$
 */
public class NewProtoTest
{
    /**
     * given the ast for the source:
     * var obj = new(Object)
     * <p/>
     * make sure the evaluator works
     */
    @Test
    public void newProtoTest()
    {
        /*
        AST

        var obj = new(Object)
        ast should look like:

               slot assign (=)
             -------------------
            |                  |
        var define           invocation
            |             ------------------
            |            |                 |
          token        token           arguments
         ('obj')      ('new')              |
                                           |
                                         token
                                       ('Object')


        */

//        // lhs  'var obj'
//        ASTToken varName = new ASTToken( "obj" );
//        ASTVarDefine varDefine = new ASTVarDefine( varName );
//
//        // rhs 'Object.new()'
//        ASTToken newName = new ASTToken( "new" );
//        ASTInvocation invocation = new ASTInvocation( newName );
//
//        ASTToken protoName = new ASTToken( "Object" );
//        invocation.getArguments().arguments().add( protoName );
//
//        // 'var obj = Object.new()'
//        ASTAssign assign = new ASTAssign( varDefine, invocation );
//
        /*
        EVALUATE

         */

//        Object result = GridRuntime.execute( assign );

        /*
        EXPECTED RESULT!
             new proto should have
             n.type
             n.proto
             n.slots
             n.scope  -- no!
             n.this

             this.scope???
             scope() ???
         */
//        Assert.assertNotNull( result );
//        Assert.assertTrue( result instanceof GridToken );
//        GridToken resultToken = (GridToken) result;
//        Assert.assertTrue( TheScope.getCurrentScope().lookupToken( resultToken ) );
//        GridProto createdObject = TheScope.getCurrentScope().resolveToken( resultToken );
//        Assert.assertNotNull( createdObject );

    }

    @Test
    public void bootstrapTest()
    {
        // things that should exist on this thread
        getRunnable().run();

        // and any new thread that gets created!
        new Thread( getRunnable() ).run();
    }

    private Runnable getRunnable()
    {
        return new Runnable()
        {
            public void run()
            {
//                Assert.assertNotNull( GridContext.getCurrentContext() );
//                Assert.assertNotNull( TheScope.getCurrentScope() );
//
//                Assert.assertTrue( TheScope.getCurrentScope().hasToken( GridToken.NativeGridTokens.UNDEFINED ) );
//                Assert.assertTrue( TheScope.getCurrentScope().hasToken( GridToken.NativeGridTokens.NIL ) );
//                Assert.assertTrue( TheScope.getCurrentScope().hasToken( GridToken.NativeGridTokens.TRUE ) );
//                Assert.assertTrue( TheScope.getCurrentScope().hasToken( GridToken.NativeGridTokens.FALSE ) );
            }
        };
    }


}
