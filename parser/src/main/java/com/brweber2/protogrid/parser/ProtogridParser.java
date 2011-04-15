/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.parser;

import com.brweber2.protogrid.ast.ASTArguments;
import com.brweber2.protogrid.ast.ASTAssign;
import com.brweber2.protogrid.ast.ASTBlock;
import com.brweber2.protogrid.ast.ASTComment;
import com.brweber2.protogrid.ast.ASTComparison;
import com.brweber2.protogrid.ast.ASTComparisonOperator;
import com.brweber2.protogrid.ast.ASTElement;
import com.brweber2.protogrid.ast.ASTFunctionDefine;
import com.brweber2.protogrid.ast.ASTInvocation;
import com.brweber2.protogrid.ast.ASTLogic;
import com.brweber2.protogrid.ast.ASTLogicOperator;
import com.brweber2.protogrid.ast.ASTMathOperation;
import com.brweber2.protogrid.ast.ASTMathOperator;
import com.brweber2.protogrid.ast.ASTNumber;
import com.brweber2.protogrid.ast.ASTOperation;
import com.brweber2.protogrid.ast.ASTParameter;
import com.brweber2.protogrid.ast.ASTParameters;
import com.brweber2.protogrid.ast.ASTSlotAccess;
import com.brweber2.protogrid.ast.ASTString;
import com.brweber2.protogrid.ast.ASTToken;
import com.brweber2.protogrid.ast.ASTVarDefine;
import com.brweber2.protogrid.ast.SourceCodeInfo;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * @version $Rev$
 */
public class ProtogridParser
{
    public static char FUNCTION_INVOKE_START = '(';
    public static char FUNCTION_INVOKE_CLOSE = ')';

    public static char FUNCTION_DEFINE_START = '[';
    public static char FUNCTION_DEFINE_CLOSE = ']';

    public static char BLOCK_START = '{';
    public static char BLOCK_CLOSE = '}';

    public static char SLOT_ACCESS = '.';

    public static char ASSIGN = '=';

    public static char COMMA = ',';

    public static char STRING_QUOTE = '"';

    public static String KEYWORD_VAR = "var";

    public static char LINE_COMMENT = '#';

    public static char COMMENT_START = '$';

    public static char COMMENT_STAR = '*';

    public static char SEMI_COLON = ';';

    public static String[] COMPARISONS = {"<", "<=", "==", "!=", ">=", ">"};

    public static String[] LOGIC_STRINGS = {"&&", "||"};

    public static char[] OPERATOR_CHARS = {'+', '-', '*', '/', '%', '^'};

    private static char[] MORE_TO_COME = {'(', '.', '+', '-', '*', '/', '%', '^', '=', '<', '!', '>', '&', '|'};

    public ParserStack parseAll( SourceCode source )
    {
        ParserStack result = new ParserStack();

        while ( !source.atEOF() )
        {
            ASTElement expr = parseExpression( source );
            if ( expr != null )
            {
//                System.out.println( expr );
                result.pushASTElement( expr );
            }
        }
        return result;
    }

    public ASTElement parseExpression( SourceCode source )
    {
        ParserStack parserStack = new ParserStack();
        parseExpressionPart( parserStack, source );
        if ( parserStack.debug().size() != 1 )
        {
            System.out.println( "stack size: " + parserStack.debug().size() );
            throw new ParserException( "Parsed more than one expression!" );
        }
        return parserStack.popASTElement();
    }

    public void parseExpressionPart( ParserStack parserStack, SourceCode source )
    {
        if ( source.atEOF() )
        {
            return;
        }
        parse( parserStack, source );
        ASTElement ast = parserStack.peekASTElement();
        if ( notExpression( ast ) )
        {
            // parse more!
            parseExpressionPart( parserStack, source );
        }
        else if ( source.hasAnyNext( MORE_TO_COME ) && !source.readStatementEnd() )
        {
            parseExpressionPart( parserStack, source );
        }
    }

    protected void parse( ParserStack parserStack, SourceCode source )
    {
        if ( source.readStatementEnd() )
        {
            source.consumeStatementEnd();
            return;
        }
        if ( source.tokenNext( FUNCTION_INVOKE_CLOSE ) )
        {
            throw new ParserException( "Unmatched closing parenthesis ')' near line " + source.getLineNumber() );
        }
        else if ( source.tokenNext( FUNCTION_DEFINE_CLOSE ) )
        {
            throw new ParserException( "Unmatched closing bracket ']' near line " + source.getLineNumber() );
        }
        else if ( source.tokenNext( BLOCK_CLOSE ) )
        {
            throw new ParserException( "Unmatched closing curly brace '}' near line " + source.getLineNumber() );
        }
        else if ( source.operatorNext() )
        {
            parseOperation( parserStack, source );
        }
        // this must come before assignment b/c of = conflict....  == vs. =
        // it also must come before logic b/c of a conflict.... != vs. !
        else if ( source.comparisonNext() )
        {
            parseComparison( parserStack, source );
        }
        else if ( source.logicNext() )
        {
            parseLogic( parserStack, source );
        }
        else if ( source.blockNext() )
        {
            // { ... }
            parseBlock( parserStack, source );
        }
        else if ( source.functionDefineNext() )
        {
            parseFunctionDefine( parserStack, source );
        }
        else if ( source.commentNext() )
        {
            // /* comment */
            parseComment( parserStack, source );
            // keep parsing until a non-comment
//            System.out.println("reading more...");
            parse( parserStack, source );
        }
        else if ( source.stringNext() )
        {
            // "hi"
            parseString( parserStack, source );
        }
        else if ( source.numberNext() )
        {
            // 123.45
            parseNumber( parserStack, source );
        }
        else if ( source.varDefineNext() )
        {
            // var foo
            parseVarDefine( parserStack, source );
        }
        else if ( source.tokenNext() )
        {
            // foo
            parseToken( parserStack, source );
        }
        else if ( source.assignNext() )
        {
            // lhs = rhs
            parseAssign( parserStack, source );
        }
        else if ( source.slotAccessNext() )
        {
            // foo.bar
            parseSlotAccess( parserStack, source );
        }
        else if ( source.functionInvocationNext() )
        {
            // ("hi")
            parseFunctionInvocation( parserStack, source );
        }
        else
        {
            if ( !source.atEOF() )
            {
                throw new ParserException( "Unsupported source code at " + source.getLineNumber() );
            }
        }

    }

    private void parseComment( ParserStack parserStack, SourceCode source )
    {
        ASTComment comment = new ASTComment( source.createSourceInfo(), source.readComment() );
        source.endSourceInfo( comment );
        parserStack.getCurrentStack().addComment( comment );
    }

    private void parseString( ParserStack parserStack, SourceCode source )
    {
        ASTString str = new ASTString( source.createSourceInfo(), source.readString() );
        source.endSourceInfo( str );
        parserStack.getCurrentStack().pushASTElement( str );
    }

    private void parseNumber( ParserStack parserStack, SourceCode source )
    {
        ASTNumber nbr = new ASTNumber( source.createSourceInfo(), source.readNumber() );
        source.endSourceInfo( nbr );
        parserStack.getCurrentStack().pushASTElement( nbr );
    }

    private void parseToken( ParserStack parserStack, SourceCode source )
    {
        ASTToken token = new ASTToken( source.createSourceInfo(), source.readToken() );
        source.endSourceInfo( token );
        parserStack.getCurrentStack().pushASTElement( token );
    }

    private void parseFunctionInvocation( ParserStack parserStack, SourceCode source )
    {
        SourceCodeInfo sourceCodeInfo = source.createSourceInfo();
        ASTElement target = parserStack.getCurrentStack().pollASTElement();

        SourceCodeInfo argsSourceCodeInfo = source.createSourceInfo();
        source.consumeToken( FUNCTION_INVOKE_START );
        parserStack.pushStack();
        parseUntil( parserStack, source, FUNCTION_INVOKE_CLOSE );
        Deque<ASTElement> astElements = parserStack.popStack();

        ASTArguments astArguments = new ASTArguments( argsSourceCodeInfo, astElements );
        ASTInvocation inv = new ASTInvocation( sourceCodeInfo, target, astArguments );
        source.endSourceInfo( inv );
        source.endSourceInfo( astArguments );
        parserStack.getCurrentStack().pushASTElement( inv );
    }

    private void parseSlotAccess( ParserStack parserStack, SourceCode source )
    {
        SourceCodeInfo sourceCodeInfo = source.createSourceInfo();
        ASTElement target = parserStack.getCurrentStack().popASTElement();

        source.cosumeSlotAccess();
        Deque<ASTElement> slot = readOnNewQueue( parserStack, source );
        ASTToken slotName = readTokenFromDeque( source, slot );
        ASTSlotAccess access = new ASTSlotAccess( sourceCodeInfo, target, slotName );
        source.endSourceInfo( access );
        parserStack.getCurrentStack().pushASTElement( access );
    }

    private void parseAssign( ParserStack parserStack, SourceCode source )
    {
        SourceCodeInfo sourceCodeInfo = source.createSourceInfo();
        ASTElement lhs = parserStack.getCurrentStack().popASTElement();
        source.consumeAssign();
        ASTElement rhs = readExpressionOnNewQueue( parserStack, source );
        ASTAssign assign = new ASTAssign( sourceCodeInfo, lhs, rhs );
        source.endSourceInfo( assign );
        parserStack.getCurrentStack().pushASTElement( assign );
    }

    private void parseVarDefine( ParserStack parserStack, SourceCode source )
    {
        SourceCodeInfo sourceCodeInfo = source.createSourceInfo();
        source.consumeVarDefine();
        Deque<ASTElement> deque = readOnNewQueue( parserStack, source );
        ASTToken ast = readTokenFromDeque( source, deque );
        ASTVarDefine varDefine = new ASTVarDefine( sourceCodeInfo, ast );
        source.endSourceInfo( varDefine );
        parserStack.getCurrentStack().pushASTElement( varDefine );
    }

    private void parseFunctionDefine( ParserStack parserStack, SourceCode source )
    {
        SourceCodeInfo sourceCodeInfo = source.createSourceInfo();
        source.consumeToken( FUNCTION_DEFINE_START );
        parserStack.pushStack();
        parseUntil( parserStack, source, FUNCTION_DEFINE_CLOSE );
        Deque<ASTElement> astElements = parserStack.popStack();
        ASTParameters params = readParametersFromDeque( source, astElements );

        SourceCodeInfo blockSourceInfo = source.createSourceInfo();
        source.consumeToken( BLOCK_START );
        parserStack.pushStack();
        parseUntil( parserStack, source, BLOCK_CLOSE );
        Deque<ASTElement> blockElements = parserStack.popStack();
        ASTBlock block = new ASTBlock( blockSourceInfo, blockElements );

        ASTFunctionDefine function = new ASTFunctionDefine( sourceCodeInfo, params, block );
        source.endSourceInfo( function );
        parserStack.getCurrentStack().pushASTElement( function );
    }

    private void parseLogic( ParserStack parserStack, SourceCode source )
    {
        SourceCodeInfo sourceCodeInfo = source.createSourceInfo();
        ASTElement lhs = parserStack.getCurrentStack().popASTElement();

        SourceCodeInfo operatorInfo = source.createSourceInfo();
        ASTLogicOperator op = new ASTLogicOperator( operatorInfo, source.readLogic() );
        source.endSourceInfo( op );

        parseExpressionPart( parserStack, source );
        ASTElement rhs = parserStack.popASTElement();

        ASTOperation log = ASTLogic.ASTLogic( sourceCodeInfo, lhs, op, rhs );
        source.endSourceInfo( log );
        parserStack.getCurrentStack().pushASTElement( log );
    }

    private void parseComparison( ParserStack parserStack, SourceCode source )
    {
        SourceCodeInfo sourceCodeInfo = source.createSourceInfo();
        ASTElement lhs = parserStack.getCurrentStack().popASTElement();

        SourceCodeInfo opInfo = source.createSourceInfo();
        ASTComparisonOperator op = new ASTComparisonOperator( opInfo, source.readComparison() );
        source.endSourceInfo( op );

        parseExpressionPart( parserStack, source );
        ASTElement rhs = parserStack.popASTElement();

        ASTOperation operation = ASTComparison.ASTComparison( sourceCodeInfo, lhs, op, rhs );
        source.endSourceInfo( operation );
        parserStack.getCurrentStack().pushASTElement( operation );
    }

    private void parseOperation( ParserStack parserStack, SourceCode source )
    {
        SourceCodeInfo sourceCodeInfo = source.createSourceInfo();
        ASTElement lhs = parserStack.getCurrentStack().popASTElement();

        SourceCodeInfo opInfo = source.createSourceInfo();
        ASTMathOperator operator = new ASTMathOperator( opInfo, source.readOperator() );
        source.endSourceInfo( operator );

        parseExpressionPart( parserStack, source );
        ASTElement rhs = parserStack.popASTElement();

        ASTOperation operation = ASTMathOperation.ASTMathOperation( sourceCodeInfo, lhs, operator, rhs );
        source.endSourceInfo( operation );
        parserStack.getCurrentStack().pushASTElement( operation );
    }

    private void parseBlock( ParserStack parserStack, SourceCode source )
    {
        SourceCodeInfo sourceCodeInfo = source.createSourceInfo();
        source.consumeToken( BLOCK_START );
        parserStack.pushStack();
        parseUntil( parserStack, source, BLOCK_CLOSE );
        Deque<ASTElement> blockElements = parserStack.popStack();
        ASTBlock block = new ASTBlock( sourceCodeInfo, blockElements );
        source.endSourceInfo( block );
        parserStack.getCurrentStack().pushASTElement( block );
    }

    // helper methods

    protected boolean notExpression( ASTElement ast )
    {
        return ast instanceof ASTComment;
    }

    private void parseUntil( ParserStack parserStack, SourceCode source, char closeToken )
    {
        while ( !source.tokenNext( closeToken ) )
        {
            if ( source.atEOF() )
            {
                throw new ParserException( "Reached end of file before " + closeToken );
            }
            parserStack.getCurrentStack().pushASTElement( parseExpression( source ) );
        }
        source.consumeToken( closeToken );
    }

    private Deque<ASTElement> readOnNewQueue( ParserStack parserStack, SourceCode source )
    {
//        return parseExpression( source );
        parserStack.pushStack();
        parse( parserStack, source );
        return parserStack.popStack();
    }

    private ASTElement readExpressionOnNewQueue( ParserStack parserStack, SourceCode source )
    {
        return parseExpression( source );
    }

    private ASTToken readTokenFromDeque( SourceCode source, Deque<ASTElement> deque )
    {
        if ( deque.size() != 1 )
        {
            throw new ParserException( "Read too many elements from deque." );
        }
        if ( !( deque.peek() instanceof ASTToken ) )
        {
            throw new ParserException( "Expected to read an AST token around line " + source.getLineNumber() );
        }
        return (ASTToken) deque.pop();
    }

    private ASTParameters readParametersFromDeque( SourceCode source, Deque<ASTElement> deque )
    {
        SourceCodeInfo sourceCodeInfo = source.createSourceInfo();
        List<ASTParameter> list = new ArrayList<ASTParameter>();
        for ( ASTElement astElement : deque )
        {
            if ( astElement instanceof ASTToken )
            {
                list.add( new ASTParameter( sourceCodeInfo, (ASTToken) astElement ) );
            }
            else
            {
                throw new ParserException( "Invalid parameter near line " + source.getLineNumber() );
            }
        }
        return new ASTParameters( sourceCodeInfo, list );
    }

}
