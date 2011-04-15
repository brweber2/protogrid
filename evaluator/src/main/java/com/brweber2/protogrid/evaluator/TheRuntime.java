/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator;

import com.brweber2.protogrid.ast.ASTArguments;
import com.brweber2.protogrid.ast.ASTAssign;
import com.brweber2.protogrid.ast.ASTBlock;
import com.brweber2.protogrid.ast.ASTComparison;
import com.brweber2.protogrid.ast.ASTElement;
import com.brweber2.protogrid.ast.ASTFunctionDefine;
import com.brweber2.protogrid.ast.ASTInvocation;
import com.brweber2.protogrid.ast.ASTLogic;
import com.brweber2.protogrid.ast.ASTLogicOperator;
import com.brweber2.protogrid.ast.ASTMathOperation;
import com.brweber2.protogrid.ast.ASTNumber;
import com.brweber2.protogrid.ast.ASTOperator;
import com.brweber2.protogrid.ast.ASTParameters;
import com.brweber2.protogrid.ast.ASTSlotAccess;
import com.brweber2.protogrid.ast.ASTString;
import com.brweber2.protogrid.ast.ASTToken;
import com.brweber2.protogrid.ast.ASTVarDefine;
import com.brweber2.protogrid.ast.GridMacro;
import com.brweber2.protogrid.evaluator.builtin.BuiltInAnd;
import com.brweber2.protogrid.evaluator.builtin.BuiltInArguments;
import com.brweber2.protogrid.evaluator.builtin.BuiltInBlock;
import com.brweber2.protogrid.evaluator.builtin.BuiltInFunction;
import com.brweber2.protogrid.evaluator.builtin.BuiltInOr;
import com.brweber2.protogrid.evaluator.builtin.BuiltInParameters;
import com.brweber2.protogrid.ssa.GridArgument;
import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridBlock;
import com.brweber2.protogrid.ssa.GridComparison;
import com.brweber2.protogrid.ssa.GridContextAware;
import com.brweber2.protogrid.ssa.GridData;
import com.brweber2.protogrid.ssa.GridFunction;
import com.brweber2.protogrid.ssa.GridParameters;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.GridScope;
import com.brweber2.protogrid.ssa.GridToken;
import com.brweber2.protogrid.ssa.NativeArguments;
import com.brweber2.protogrid.ssa.NativeBlock;
import com.brweber2.protogrid.ssa.NativeBoolean;
import com.brweber2.protogrid.ssa.NativeNumber;
import com.brweber2.protogrid.ssa.NativeParameters;
import com.brweber2.protogrid.ssa.data.DataArguments;
import com.brweber2.protogrid.ssa.data.DataBlock;
import com.brweber2.protogrid.ssa.data.DataBoolean;
import com.brweber2.protogrid.ssa.data.DataFunction;
import com.brweber2.protogrid.ssa.data.DataNil;
import com.brweber2.protogrid.ssa.data.DataNumber;
import com.brweber2.protogrid.ssa.data.DataParameters;
import com.brweber2.protogrid.ssa.data.DataString;
import com.brweber2.protogrid.ssa.data.DataToken;

import java.util.Arrays;

/**
 * @version $Rev$
 */
public class TheRuntime implements ProtogridRuntime
{
    private static ThreadLocal<ProtogridRuntime> runtime = new ThreadLocal<ProtogridRuntime>()
    {
        @Override
        protected ProtogridRuntime initialValue()
        {
            return new TheRuntime();
        }
    };

    public static ProtogridRuntime getRuntime()
    {
        return runtime.get();
    }

    public static void clearRuntime()
    {
        runtime.set( new TheRuntime() );
    }

    // END THREAD LOCAL

    private TheRuntime()
    {
    }

    private GridPrototype resolveInScope( GridToken name, GridScope scope )
    {
        return scope.resolveToken( name );
    }

    private GridPrototype getSelf( GridContextAware function )
    {
        if ( function.hasContext() )
        {
            return function.getContext();
        }
        else
        {
            return TheContext.getCurrentContext().getSelf();
        }
    }

    public GridPrototype executeExpression( ASTElement ast )
    {
        return executeExpression( ast, TheScope.getCurrentScope() );
    }

    public GridPrototype executeExpression( ASTElement ast, GridScope scope )
    {
        return TheContext.getCurrentContext().set( executePart( ast, scope ) );
    }

    public GridPrototype executeArgument( ASTElement ast, GridScope scope )
    {
        return executeExpression( ast, scope ); // should args be able to see previous work?
    }

    public GridPrototype executeParameters( ASTElement ast, GridScope scope )
    {
        return executePart( ast, scope ); // correct one to call?
    }

    public GridPrototype executePart( ASTElement ast, GridScope scope )
    {
        if ( ast instanceof ASTString )
        {
            ASTString astString = (ASTString) ast;
            return TheContext.getCurrentContext().set( new DataString( resolveInScope( Bootstrap.getStringToken(), scope ), astString.getNative() ) );
        }
        else if ( ast instanceof ASTNumber )
        {
            ASTNumber astNumber = (ASTNumber) ast;
            return TheContext.getCurrentContext().set( new DataNumber( resolveInScope( Bootstrap.getNumberToken(), scope ), astNumber.getNative() ) );
        }
        else if ( ast instanceof ASTLogic )
        {
            ASTLogic logic = (ASTLogic) ast;
            ASTElement lhs = (ASTElement) logic.getSlot( ASTOperator.LHS_KEY );
            ASTElement rhs = (ASTElement) logic.getSlot( ASTOperator.RHS_KEY );

            GridPrototype parent = TheScope.getCurrentScope().resolveToken( Bootstrap.getArgumentsToken() );
            DataArguments args = new DataArguments( parent, new BuiltInArguments( new ASTArguments( Bootstrap.noSource, Arrays.asList( lhs, rhs ) ), this, scope ) );
            GridFunction nativeLogic;
            switch ( ((ASTLogicOperator)logic.getSlot( ASTOperator.OPERATOR_KEY)).getNative() )
            {
                case AND:
                    nativeLogic = new BuiltInAnd( resolveInScope( Bootstrap.getFunctionToken(), scope ) );
                    break;
                case OR:
                    nativeLogic = new BuiltInOr( resolveInScope( Bootstrap.getFunctionToken(), scope ) );
                    break;
                default:
                    throw new RuntimeException( "Unable to handle logic operator..." );
            }
            NativeBoolean result = (NativeBoolean) nativeLogic.apply( nativeLogic, args );
            DataBoolean r = new DataBoolean( resolveInScope( Bootstrap.getObjectToken(), scope ), result.getNative() );
            return TheContext.getCurrentContext().set( r );
        }
        else if ( ast instanceof ASTComparison )
        {
            ASTComparison comparison = (ASTComparison) ast;
            ASTElement lhs = (ASTElement) comparison.getSlot(ASTOperator.LHS_KEY);
            ASTElement rhs = (ASTElement) comparison.getSlot(ASTOperator.RHS_KEY);
            // rhs could be ASTComparison, ASTMathOperation or ASTLogic...
            GridComparison first;
            Object second;
            first = (GridComparison) executeExpression( lhs, scope );
            second = executeExpression( rhs, scope );

            boolean result = first.compare( ((ASTOperator)comparison.getSlot(ASTOperator.OPERATOR_KEY)).getOperator().toString().trim(), second );
            DataBoolean r = new DataBoolean( resolveInScope( Bootstrap.getObjectToken(), scope ), result );
            return TheContext.getCurrentContext().set( r );
        }
        else if ( ast instanceof ASTMathOperation )
        {
            ASTMathOperation op = (ASTMathOperation) ast;
            ASTElement lhs = (ASTElement) op.getSlot(ASTOperator.LHS_KEY);
            ASTElement rhs = (ASTElement) op.getSlot(ASTOperator.RHS_KEY);

            NativeNumber first = (NativeNumber) executeExpression( lhs, scope );
            NativeNumber second = (NativeNumber) executeExpression( rhs, scope );
            NativeNumber rslt = first.calculate( op.getSlot(ASTOperator.OPERATOR_KEY).toString().trim().charAt( 0 ), second ); // todo hack...
            // check if your left has higher precedence (parser did this)
            // calculate the operation and set as current context
            DataNumber result = new DataNumber( resolveInScope( Bootstrap.getNumberToken(), scope ), rslt.getNative() );
            return TheContext.getCurrentContext().set( result );
        }
        else if ( ast instanceof ASTArguments )
        {
            NativeArguments args = new BuiltInArguments( (ASTArguments) ast, this, scope );
            return new DataArguments( TheScope.getCurrentScope().resolveToken( Bootstrap.getArgumentsToken() ), args );
        }
        else if ( ast instanceof ASTParameters )
        {
            NativeParameters params = new BuiltInParameters( (ASTParameters) ast );
            return new DataParameters( TheScope.getCurrentScope().resolveToken( Bootstrap.getParametersToken() ), params );
        }
        else if ( ast instanceof ASTBlock )
        {
            // create new block scope (closure)
            final ASTBlock astBlock = (ASTBlock) ast;
            GridBlock function = (GridBlock) resolveInScope( Bootstrap.getBlockToken(), scope );
            NativeBlock nativeBlock = new BuiltInBlock( astBlock, this, scope );
            return new DataBlock( function, nativeBlock );
        }
        else if ( ast instanceof ASTFunctionDefine )
        {
            ASTFunctionDefine astFunctionDefine = (ASTFunctionDefine) ast;
            GridParameters params = (GridParameters) executePart( (ASTElement)astFunctionDefine.getSlot(ASTFunctionDefine.FUNC_DEF_PARAMS_KEY), scope );
            GridBlock block = (GridBlock) executePart( (ASTElement)astFunctionDefine.getSlot(ASTFunctionDefine.FUNC_DEF_BLOCK_KEY), scope );
            BuiltInFunction bif = new BuiltInFunction( params, block );
            GridFunction dataFunction = new DataFunction( resolveInScope( Bootstrap.getFunctionToken(), scope ), bif );
            params.getMappedParameters().put( Bootstrap.getRecurToken(), dataFunction );
            return TheContext.getCurrentContext().set( dataFunction );
        }
        else if ( ast instanceof ASTInvocation )
        {
            // using current context ... invoke
            ASTInvocation astInvocation = (ASTInvocation) ast;
            ASTElement target = (ASTElement) astInvocation.getSlot(ASTInvocation.INVOKE_TARGET_KEY);
            if ( target == null )
            {
                // execute for precedence or side effects
                GridArguments args = (GridArguments) executePart( (ASTElement)astInvocation.getSlot(ASTInvocation.INVOKE_ARGS_KEY), scope );
                GridPrototype last = DataNil.NIL;
                for ( GridArgument arg : args )
                {
                    last = arg.getValue();
                }
                return TheContext.getCurrentContext().set( last );
            }
            GridData result = executePart( target, scope );
            if ( result instanceof GridMacro )
            {
                GridMacro macro = (GridMacro) target;
                ASTElement expandedAST = macro.expandOne( (ASTArguments)astInvocation.getSlot(ASTInvocation.INVOKE_ARGS_KEY) );
                return executeExpression( expandedAST, scope );
            }
            if ( result instanceof GridContextAware )
            {
                GridContextAware r = (GridContextAware) result; // now context is set
                GridPrototype self = getSelf( r );
                // r could be a block or a function
                if ( r instanceof GridBlock )
                {
                    GridBlock block = (GridBlock) r;
                    if ( block.hasSlot( Bootstrap.getThisToken() ) )
                    {
                        self = block.getSlot( Bootstrap.getThisToken() );
                    }
                    if ( block.isClosure() && block.getScope() != TheScope.getCurrentScope())
                    {
                        TheScope.pushNewScope(block);
                    }
                    else
                    {
                        TheScope.pushNewScope();
                    }
                    try
                    {
                        TheScope.getCurrentScope().defineOrSet( Bootstrap.getThisToken(), self );
                        GridPrototype theResult = block.apply( self );
                        return TheContext.getCurrentContext().set( theResult );
                    }
                    catch ( RuntimeException e )
                    {
                        e.printStackTrace(); // this just helps soooo much for debugging....
                        throw e;
                    }
                    finally
                    {
                        TheScope.popCurrentScope();
                    }
                }
                else
                {
                    GridFunction gridFunction = (GridFunction) r;
                    if ( gridFunction.hasSlot( Bootstrap.getThisToken() ) )
                    {
                        self = gridFunction.getSlot( Bootstrap.getThisToken() );
                    }
                    GridArguments args = (GridArguments) executePart( (ASTElement)astInvocation.getSlot(ASTInvocation.INVOKE_ARGS_KEY), scope );
                    GridParameters params = gridFunction.getParameters();
                    params.map( args );
                    TheScope.pushFunctionScope();
                    try
                    {
                        TheScope.getCurrentScope().defineAndSet( Bootstrap.getThisToken(), self );
                        for ( GridToken token : params.getMappedParameters().keySet() )
                        {
                            TheScope.getCurrentScope().defineAndSet( token, params.getMappedParameters().get( token ) );
                        }
                        GridPrototype theResult = gridFunction.apply( self, args );
                        return TheContext.getCurrentContext().set( theResult );
                    }
                    catch ( RuntimeException e )
                    {
                        e.printStackTrace(); // this just helps soooo much for debugging....
                        throw e;
                    }
                    finally
                    {
                        TheScope.popCurrentScope();
                    }
                }
            }
            throw new RuntimeException( "non context-aware invocation" );
        }
        else if ( ast instanceof ASTToken )
        {
            ASTToken astToken = (ASTToken) ast;
            return resolveInScope( new DataToken( astToken.getToken() ), scope );
        }
        else if ( ast instanceof ASTVarDefine )
        {
            ASTVarDefine astVarDefine = (ASTVarDefine) ast;
            DataToken token = new DataToken( ((ASTToken)astVarDefine.getSlot(ASTVarDefine.VAR_DEFINE_KEY)).getToken() );
            return TheScope.getCurrentScope().defineToken( token );
        }
        else if ( ast instanceof ASTSlotAccess )
        {
            ASTSlotAccess slot = (ASTSlotAccess) ast;
            TheContext.getCurrentContext().set( executePart( (ASTElement)slot.getSlot(ASTSlotAccess.SLOT_TARGET_KEY), scope ) ); // should set the context
            GridToken name = new DataToken( ((ASTToken) slot.getSlot(ASTSlotAccess.SLOT_NAME_KEY)).getToken() );
            GridPrototype proto = TheContext.getCurrentContext().getGridPrototype().getSlot( name );
            return TheContext.getCurrentContext().set( proto );
        }
        else if ( ast instanceof ASTAssign )
        {
            ASTAssign astAssign = (ASTAssign) ast;
            ASTElement lhs = (ASTElement)astAssign.getSlot(ASTAssign.ASSIGN_LHS);
            GridPrototype rhs = executePart( (ASTElement)astAssign.getSlot(ASTAssign.ASSIGN_RHS), scope );
            if ( lhs instanceof ASTToken )
            {
                ASTToken astToken = (ASTToken) lhs;
                DataToken dataToken = new DataToken( astToken.getToken() );
                return TheScope.getCurrentScope().setToken( dataToken, rhs );
            }
            else if ( lhs instanceof ASTSlotAccess )
            {
                ASTSlotAccess slot = (ASTSlotAccess) lhs;
                TheContext.getCurrentContext().set( executePart( (ASTElement)slot.getSlot(ASTSlotAccess.SLOT_TARGET_KEY), scope ) ); // should set the context
                if ( rhs instanceof GridContextAware )
                {
                    rhs.setSlot( Bootstrap.getThisToken(), TheContext.getCurrentContext().getGridPrototype() );
                }
                GridToken dataToken = new DataToken( ((ASTToken)slot.getSlot(ASTSlotAccess.SLOT_NAME_KEY)).getToken() );
                return TheContext.getCurrentContext().getGridPrototype().setSlot( dataToken, rhs );
            }
            else if ( lhs instanceof ASTVarDefine )
            {
                ASTVarDefine astVarDefine = (ASTVarDefine) lhs;
                return TheScope.getCurrentScope().defineAndSet( new DataToken( ((ASTToken)astVarDefine.getSlot(ASTVarDefine.VAR_DEFINE_KEY)).getToken() ), rhs );
            }
            else
            {
                throw new RuntimeException( "Unsupported lhs for = " + ast );
            }
        }
        throw new RuntimeException( "Unsupported AST type: " + ast );
    }

    public void registerNative( String name, GridFunction function )
    {
        TheScope.getRootScope().defineAndSet( new DataToken( name ), function );
    }
}
