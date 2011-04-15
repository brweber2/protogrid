/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.parser;

import com.brweber2.protogrid.ast.ASTComment;
import com.brweber2.protogrid.ast.ASTElement;
import com.brweber2.protogrid.ast.SupportsComments;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * @version $Rev$
 */
public class ParserStack implements SupportsComments
{
    private final List<ASTComment> comments = new ArrayList<ASTComment>();
    private ParserStack parent;
    private Deque<ASTElement> parsedItems = new ArrayDeque<ASTElement>();
    private ParserStack currentStack = this;

    protected ParserStack()
    {
        this.parent = null;
    }

    protected ParserStack( ParserStack parent )
    {
        this.parent = parent;
    }

    protected ParserStack getParent()
    {
        return parent;
    }

    public Deque<ASTElement> debug()
    {
        return parsedItems;
    }

    public ASTElement pushASTElement( ASTElement ast )
    {
        parsedItems.add( ast );
        return ast;
    }

    public ASTElement peekASTElement()
    {
        return parsedItems.peekLast();
    }

    public void resetASTElements()
    {
        parsedItems = new ArrayDeque<ASTElement>();
    }

    public ASTElement pollASTElement()
    {
        ASTElement element = parsedItems.poll();
        // is this the correct place to add the comments???
        if ( element != null )
        {
            if ( !comments.isEmpty() )
            {
                for ( ASTComment comment : comments )
                {
    //                System.out.println("adding comment " + comment.getStr() + " to " + element);
                    element.addComment( comment );
                }
                comments.clear();
            }
        }
        return element;
    }

    public ASTElement popASTElement()
    {
        ASTElement element = parsedItems.pop();
        // is this the correct place to add the comments???
        if ( !comments.isEmpty() )
        {
            for ( ASTComment comment : comments )
            {
//                System.out.println("adding comment " + comment.getStr() + " to " + element);
                element.addComment( comment );
            }
            comments.clear();
        }
        return element;
    }

    public void addComment( ASTComment comment )
    {
        comments.add( comment );
    }

    public List<ASTComment> getComments()
    {
        return comments;
    }

    // stack related operations

    public ParserStack getCurrentStack()
    {
        return currentStack;
    }

    public void pushStack()
    {
        currentStack = new ParserStack( currentStack );
    }

    public Deque<ASTElement> popStack()
    {
        if ( currentStack.getParent() == null )
        {
            throw new ParserException( "Parser bug, should never pop the root parser stack!" );
        }
        Deque<ASTElement> returnValue = currentStack.parsedItems;
        if ( !currentStack.comments.isEmpty() )
        {
            for ( ASTComment comment : currentStack.comments )
            {
//                System.out.println("adding comment " + comment.getStr() + " to " + returnValue.peekFirst() );
                returnValue.peekFirst().addComment( comment );
            }
            currentStack.comments.clear();
        }
        currentStack = currentStack.getParent();
        return returnValue;
    }

    public void resetStack()
    {
        currentStack = new ParserStack();
        parsedItems = new ArrayDeque<ASTElement>();
    }

    public Deque<ASTElement> finishParsing()
    {
        if ( currentStack.getParent() != null )
        {
            throw new ParserException( "Called finish when the stack depth isn't 1!" );
        }
        Deque<ASTElement> parsedItems1 = currentStack.parsedItems;
        resetStack();
        return parsedItems1;
    }
}
