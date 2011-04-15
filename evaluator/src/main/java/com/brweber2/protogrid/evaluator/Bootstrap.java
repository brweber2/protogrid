/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator;

import com.brweber2.protogrid.ast.ASTBlock;
import com.brweber2.protogrid.ast.ASTElement;
import com.brweber2.protogrid.ast.ASTToken;
import com.brweber2.protogrid.ast.SourceCodeInfo;
import com.brweber2.protogrid.evaluator.builtin.BuiltInAstShow;
import com.brweber2.protogrid.evaluator.builtin.BuiltInBlock;
import com.brweber2.protogrid.evaluator.builtin.BuiltInCreateError;
import com.brweber2.protogrid.evaluator.builtin.BuiltInFunction;
import com.brweber2.protogrid.evaluator.builtin.BuiltInIf;
import com.brweber2.protogrid.evaluator.builtin.BuiltInList;
import com.brweber2.protogrid.evaluator.builtin.BuiltInLoop;
import com.brweber2.protogrid.evaluator.builtin.BuiltInMacroDef;
import com.brweber2.protogrid.evaluator.builtin.BuiltInNew;
import com.brweber2.protogrid.evaluator.builtin.BuiltInNumber;
import com.brweber2.protogrid.evaluator.builtin.BuiltInPrintln;
import com.brweber2.protogrid.evaluator.builtin.BuiltInRaise;
import com.brweber2.protogrid.evaluator.builtin.BuiltInRoot;
import com.brweber2.protogrid.evaluator.builtin.BuiltInString;
import com.brweber2.protogrid.evaluator.builtin.BuiltInStringInterpolation;
import com.brweber2.protogrid.evaluator.builtin.BuiltInThis;
import com.brweber2.protogrid.evaluator.builtin.BuiltInTry;
import com.brweber2.protogrid.evaluator.builtin.BuiltInWhile;
import com.brweber2.protogrid.evaluator.builtin.empty.EmptyBuiltInParameters;
import com.brweber2.protogrid.evaluator.builtin.interop.BuiltInWrapClass;
import com.brweber2.protogrid.ssa.GridBlock;
import com.brweber2.protogrid.ssa.GridFunction;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.GridToken;
import com.brweber2.protogrid.ssa.data.DataBlock;
import com.brweber2.protogrid.ssa.data.DataBoolean;
import com.brweber2.protogrid.ssa.data.DataFunction;
import com.brweber2.protogrid.ssa.data.DataNil;
import com.brweber2.protogrid.ssa.data.DataPrototype;
import com.brweber2.protogrid.ssa.data.DataToken;
import com.brweber2.protogrid.ssa.data.DataWrapField;
import com.brweber2.protogrid.ssa.data.DataWrapInstance;
import com.brweber2.protogrid.ssa.data.DataWrapMethod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @version $Rev$
 */
public class Bootstrap
{
    public static final String SCOPE_STRING = "Scope";
    private static final GridToken scopeToken = new DataToken( SCOPE_STRING );

    public static final String OBJECT_STRING = "Object";
    private static final GridToken objectToken = new DataToken( OBJECT_STRING );

    private static final String BLOCK_STRING = "Block";
    private static final GridToken blockToken = new DataToken( BLOCK_STRING );

    private static final String FUNCTION_STRING = "Function";
    private static final GridToken functionToken = new DataToken( FUNCTION_STRING );

    public static final String STRING_STRING = "String";
    private static final GridToken stringToken = new DataToken( STRING_STRING );

    public static final String NUMBER_STRING = "Number";
    private static final GridToken numberToken = new DataToken( NUMBER_STRING );

    private static final String LIST_STRING = "List";
    private static final GridToken listToken = new DataToken( LIST_STRING );

    private static final String UNDEFINED_STRING = "undefined";
    private static final GridToken undefinedToken = new DataToken( UNDEFINED_STRING );

    private static final String NIL_STRING = "nil";
    private static final GridToken nilToken = new DataToken( NIL_STRING );

    private static final String TRUE_STRING = "true";
    private static final GridToken trueToken = new DataToken( TRUE_STRING );

    private static final String FALSE_STRING = "false";
    private static final GridToken falseToken = new DataToken( FALSE_STRING );

    private static final String IF_STRING = "if";
    private static final GridToken ifToken = new DataToken( IF_STRING );

    private static final String AST_SHOW_STRING = "ast";
    private static final GridToken astShowToken = new DataToken( AST_SHOW_STRING );

    private static final String LOOP_STRING = "loop";
    private static final GridToken loopToken = new DataToken( LOOP_STRING );

    private static final String NEW_STRING = "clone";
    private static final GridToken newToken = new DataToken( NEW_STRING );

    private static final String SELF_STRING = "self";
    private static final GridToken selfToken = new DataToken( SELF_STRING );

    private static final String THIS_STRING = "this";
    private static final GridToken thisToken = new DataToken( THIS_STRING );

    private static final String RECUR_STRING = "recur";
    private static final GridToken recurToken = new DataToken( RECUR_STRING );

    private static final String PRINT_STRING = "print";
    private static final GridToken printToken = new DataToken( PRINT_STRING );

    private static final String PRINTLN_STRING = "println";
    private static final GridToken printlnToken = new DataToken( PRINTLN_STRING );

    private static final String NO_SUCH_SLOT_STRING = "noslot";
    private static final GridToken noSuchSlotToken = new DataToken( NO_SUCH_SLOT_STRING );

    private static final BuiltInRoot gridRoot = new BuiltInRoot();

    private static final DataBoolean falseProto = new DataBoolean( gridRoot, Boolean.FALSE );
    private static final DataBoolean trueProto = new DataBoolean( gridRoot, Boolean.TRUE );

    public static final String WRAP_CLASS_STRING = "WrapClass";
    private static GridToken wrapClassToken = new DataToken( WRAP_CLASS_STRING );

    public static final String WRAP_FIELD_STRING = "WrapField";
    private static GridToken wrapFieldToken = new DataToken( WRAP_FIELD_STRING );

    public static final String WRAP_METHOD_STRING = "WrapMethod";
    private static GridToken wrapMethodToken = new DataToken( WRAP_METHOD_STRING );

    public static final String COERCE_STRING = "Coerce";
    private static GridToken coerceToken = new DataToken( COERCE_STRING );

    public static final String INTERPOLATION_STRING = "str";
    private static GridToken interpolationToken = new DataToken( INTERPOLATION_STRING );

    public static final String WRAP_INSTANCE_STRING = "WrapInstance";
    private static GridToken wrapInstanceToken = new DataToken( WRAP_INSTANCE_STRING );

    public static Map<GridToken, GridPrototype> others = new HashMap<GridToken, GridPrototype>();

    private static final String TRY_RESULT_STRING = "result";
    private static GridToken tryResultToken = new DataToken( TRY_RESULT_STRING );

    private static final String TRY_EXCEPTION_STRING = "error";
    private static GridToken tryExceptionToken = new DataToken( TRY_EXCEPTION_STRING );

    private static final String TRY_STRING = "try";
    private static GridToken tryToken = new DataToken( TRY_STRING );

    private static final String RAISE_STRING = "raise";
    private static GridToken raiseToken = new DataToken( RAISE_STRING );

    private static final String ERROR_STRING = "error";
    private static GridToken errorToken = new DataToken( ERROR_STRING );

    private static final String WHILE_STRING = "while";
    private static GridToken whileToken = new DataToken( WHILE_STRING );

    private static final String ARGUMENTS_STRING = "args";
    private static GridToken argumentsToken = new DataToken( ARGUMENTS_STRING );

    private static final String PARAMS_STRING = "params";
    private static GridToken paramsToken = new DataToken( PARAMS_STRING );

    private static final String MACRO_STRING = "macro";
    private static GridToken macroToken = new DataToken( MACRO_STRING );

    public static final SourceCodeInfo noSource = new SourceCodeInfo("no source", 0, 0);

    public static Map<GridToken, GridPrototype> getNativePrototypes()
    {
        Map<GridToken, GridPrototype> map = new HashMap<GridToken, GridPrototype>();

        GridPrototype root = Bootstrap.getGridRoot();
        map.put( getObjectToken(), root );

        map.put( getStringToken(), new BuiltInString( root ) );
        map.put( getNumberToken(), new BuiltInNumber( root ) );

        GridBlock block = new DataBlock(
                root,
                new BuiltInBlock( new ASTBlock( noSource, Collections.<ASTElement>emptyList() ), TheRuntime.getRuntime(), null ) );
        map.put( getBlockToken(), block );

        Collection<ASTElement> nocode = new ArrayList<ASTElement>();
        nocode.add( new ASTToken( noSource, "nil" ));
        GridFunction function = new DataFunction(
                root,
                new BuiltInFunction(
                        new EmptyBuiltInParameters(),
                        new BuiltInBlock(new ASTBlock( noSource, nocode ),TheRuntime.getRuntime(),TheScope.getCrapScope()) ) );
        map.put( getFunctionToken(), function );
        map.put( getArgumentsToken(), DataNil.NIL );
        map.put( getParametersToken(), DataNil.NIL );

        map.put( getNewToken(), new BuiltInNew( function ) );
        map.put( getSelfToken(), new BuiltInThis( function ) );
        map.put( getPrintToken(), new BuiltInPrintln( function, false ) );
        map.put( getPrintlnToken(), new BuiltInPrintln( function, true ) );
        map.put( getListToken(), new BuiltInList( function ) );
        map.put( getIfToken(), new BuiltInIf( function ) );
        map.put( getAstShowToken(), new BuiltInAstShow( function ) );
        map.put( getLoopToken(), new BuiltInLoop( function ) );
        map.put( getWhileToken(), new BuiltInWhile( function ) );
        map.put( getMacroToken(), new BuiltInMacroDef( function ) );

        // java interop
        map.put( getWrapClassToken(), new BuiltInWrapClass( root ) );
        map.put( getWrapFieldToken(), new DataWrapField( root, null ) );
        map.put( getWrapMethodToken(), new DataWrapMethod( root, null ) );
        map.put( getWrapInstanceToken(), new DataWrapInstance( root, null ) );

        map.put( getCoerceToken(), new DataPrototype( root, COERCE_STRING ) );

        map.put( getInterpolationToken(), new BuiltInStringInterpolation( function, null) );
        map.put( getTryToken(), new BuiltInTry(function) );
        map.put( getRaiseToken(), new BuiltInRaise(function) );
        map.put( getErrorToken(), new BuiltInCreateError(function) );

        /*
        add:

        list()
        if()

        scope()
        try()

        return()
        break()
        continue()
        raise()
        switch()
         */

        map.put( getUndefinedToken(), DataNil.UNDEFINED );
        map.put( getNilToken(), DataNil.NIL );
        map.put( getTrueToken(), trueProto );
        map.put( getFalseToken(), falseProto );

        map.putAll( others );

        return map;
    }

    public static GridToken getObjectToken()
    {
        return objectToken;
    }

    public static GridToken getBlockToken()
    {
        return blockToken;
    }

    public static GridToken getFunctionToken()
    {
        return functionToken;
    }

    public static GridToken getInterpolationToken()
    {
        return interpolationToken;
    }

    public static GridToken getNumberToken()
    {
        return numberToken;
    }

    public static GridToken getStringToken()
    {
        return stringToken;
    }

    public static GridToken getFalseToken()
    {
        return falseToken;
    }

    public static GridToken getNilToken()
    {
        return nilToken;
    }

    public static GridToken getTrueToken()
    {
        return trueToken;
    }

    public static GridToken getIfToken()
    {
        return ifToken;
    }

    public static GridToken getAstShowToken()
    {
        return astShowToken;
    }

    public static GridToken getLoopToken()
    {
        return loopToken;
    }

    public static GridToken getNewToken()
    {
        return newToken;
    }

    public static GridToken getListToken()
    {
        return listToken;
    }

    public static GridToken getPrintToken()
    {
        return printToken;
    }

    public static GridToken getPrintlnToken()
    {
        return printlnToken;
    }

    public static GridToken getSelfToken()
    {
        return selfToken;
    }

    public static GridToken getThisToken()
    {
        return thisToken;
    }

    public static GridToken getRecurToken()
    {
        return recurToken;
    }

    public static GridToken getUndefinedToken()
    {
        return undefinedToken;
    }

    public static GridToken getNoSuchSlotToken()
    {
        return noSuchSlotToken;
    }

    public static GridPrototype getGridRoot()
    {
        return gridRoot;
    }

    public static GridPrototype getNil()
    {
        return DataNil.NIL;
    }

    public static GridToken getWrapClassToken()
    {
        return wrapClassToken;
    }

    public static GridToken getWrapFieldToken()
    {
        return wrapFieldToken;
    }

    public static GridToken getWrapMethodToken()
    {
        return wrapMethodToken;
    }

    public static GridToken getCoerceToken()
    {
        return coerceToken;
    }

    public static GridToken getWrapInstanceToken()
    {
        return wrapInstanceToken;
    }

    public static GridToken getTryResultToken()
    {
        return tryResultToken;
    }

    public static GridToken getTryExceptionToken()
    {
        return tryExceptionToken;
    }

    public static GridToken getTryToken()
    {
        return tryToken;
    }

    public static GridToken getRaiseToken()
    {
        return raiseToken;
    }

    public static GridToken getErrorToken()
    {
        return errorToken;
    }

    public static GridToken getWhileToken()
    {
        return whileToken;
    }

    public static GridToken getArgumentsToken()
    {
        return argumentsToken;
    }

    public static GridToken getParametersToken()
    {
        return paramsToken;
    }

    public static GridToken getMacroToken()
    {
        return macroToken;
    }
}
