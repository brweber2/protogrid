/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator;

import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.GridScope;
import com.brweber2.protogrid.ssa.GridScopeAware;
import com.brweber2.protogrid.ssa.GridToken;
import com.brweber2.protogrid.ssa.data.BaseDataPrototype;
import com.brweber2.protogrid.ssa.data.DataNil;

import java.util.Set;

/**
 * @version $Rev$
 */
public class TheScope extends BaseDataPrototype implements GridScope
{

    private static ThreadLocal<GridScope> scope = new ThreadLocal<GridScope>()
    {
        @Override
        protected GridScope initialValue()
        {
            return new TheScope( null );
        }
    };

    public static GridScope getCurrentScope()
    {
        return scope.get();
    }

    public static void clearScope()
    {
        scope.set( new TheScope( null ) );
    }

    public static GridScope pushNewScope()
    {
        GridScope old = getCurrentScope();
        scope.set( new TheScope( old ) );
        return getCurrentScope();
    }

    public static GridScope pushNewScope(GridScopeAware old)
    {
        GridScope past = getCurrentScope();
        scope.set( new TheScope( past, old.getScope() ) );
        return getCurrentScope();
    }

    public static GridScope pushFunctionScope()
    {
        GridScope old = getCurrentScope();
        scope.set( new TheScope( old, false ) );
        return getCurrentScope();
    }

    public static GridScope popCurrentScope()
    {
        GridScope old = getCurrentScope();
        if ( old.getParentScope() != null )
        {
            scope.set( old.getParentScope() );
            return getCurrentScope();
        }
        throw new RuntimeException( "You can't pop the root scope!" );
    }

    // END STATIC SECTION

    //    private TheScope parentScope;
    private boolean isClosure = true;
    private GridScope toPop;

    private TheScope( GridScope parentScope )
    {
        this( parentScope, parentScope, true );
    }

    private TheScope( GridScope parentScope, boolean isClosure )
    {
        this( parentScope, parentScope, isClosure );
    }

    private TheScope( GridScope toPop, GridScope parent )
    {
        this( toPop, parent, true );
    }

    private TheScope( GridScope toPop, GridScope parentScope, boolean isClosure )
    {
        super( parentScope, Bootstrap.SCOPE_STRING );
        if ( parentScope == null || !isClosure )
        {
            this.slots.putAll( Bootstrap.getNativePrototypes() );
        }
        this.isClosure = isClosure;
        this.toPop = toPop; // this is what was on the queue and will be when we are gone (it is not used for looking up any values in this scope...
    }

    public static GridScope getRootScope()
    {
        GridScope s = getCurrentScope();
        while ( s.getParentScope() != null )
        {
            s = s.getParentScope();
        }
        return s;
    }

    public GridScope getParentScope()
    {
        return toPop;
    }

//    private Map<GridToken, GridPrototype> tokens = new HashMap<GridToken, GridPrototype>();


    public Set<GridToken> getTokens()
    {
        return slots.keySet();
    }

    public boolean hasToken( GridToken token )
    {
        return slots.keySet().contains( token );
    }

    public void addToken( GridToken token )
    {
        slots.put( token, DataNil.UNDEFINED ); // it exists, but is undefined
    }

    public GridPrototype defineOrSet( GridToken token, GridPrototype value )
    {
        slots.put( token, value );
        return value;
    }

    public boolean lookupToken( GridToken name )
    {
        if ( slots.containsKey( name ) )
        {
            return true;
        }
        else if ( isClosure && parentProto != null )
        {
            return ( (TheScope) parentProto ).lookupToken( name );
        }
        return false;
    }

    public GridPrototype resolveToken( GridToken name )
    {
        if ( slots.containsKey( name ) )
        {
            return slots.get( name );
        }
        else if ( isClosure && parentProto != null )
        {
            return ( (TheScope) parentProto ).resolveToken( name );
        }
        throw new RuntimeException( "Unable to resolve token " + name + " in current scope." );
    }

    public GridPrototype setToken( GridToken name, GridPrototype value )
    {
        if ( !lookupToken( name ) )
        {
            throw new RuntimeException( "No such token " + name + " in scope." );
        }
        if ( isClosure && parentProto != null )
        {
            if ( slots.containsKey( name ) )
            {
                slots.put( name, value );
            }
            else
            {
                ( (TheScope) parentProto ).setToken( name, value );
            }
        }
        else
        {
            slots.put( name, value );
        }
        return value;
    }

    public GridPrototype defineAndSet( GridToken name, GridPrototype value )
    {
        defineToken( name );
        setToken( name, value );
        return value;
    }

    public GridPrototype defineToken( GridToken name )
    {
        if ( lookupToken( name ) )
        {
            throw new RuntimeException( "Var " + name + " is already defined in scope." );
        }
        slots.put( name, DataNil.UNDEFINED );
        return DataNil.UNDEFINED;
    }

    public static GridScope getCrapScope()
    {
        GridScope parent = new GridScope() {

            public GridPrototype getParentProto()
            {
                throw new RuntimeException( "oy vey" );
            }

            public void setParentProto( GridPrototype proto )
            {
                throw new RuntimeException( "oy vey" );
            }

            public boolean hasSlot( GridToken name )
            {
                throw new RuntimeException( "oy vey" );
            }

            public GridPrototype getSlot( GridToken name )
            {
                throw new RuntimeException( "oy vey" );
            }

            public GridPrototype defineSlot( GridToken name )
            {
                throw new RuntimeException( "oy vey" );
            }

            public GridPrototype defineAndAssignSlot( GridToken name, GridPrototype value )
            {
                throw new RuntimeException( "oy vey" );
            }

            public GridPrototype setSlot( GridToken name, GridPrototype value )
            {
                throw new RuntimeException( "oy vey" );
            }

            public Set<GridToken> getSlotNames()
            {
                throw new RuntimeException( "oy vey" );
            }

            public String getTypeName()
            {
                throw new RuntimeException( "oy vey" );
            }

            public GridScope getParentScope()
            {
                throw new RuntimeException( "oy vey" );
            }

            public GridPrototype resolveToken( GridToken name )
            {
                throw new RuntimeException( "oy vey" );
            }

            public GridPrototype defineOrSet( GridToken name, GridPrototype value )
            {
                throw new RuntimeException( "oy vey" );
            }

            public GridPrototype defineAndSet( GridToken name, GridPrototype value )
            {
                throw new RuntimeException( "oy vey" );
            }

            public GridPrototype defineToken( GridToken name )
            {
                throw new RuntimeException( "oy vey" );
            }

            public GridPrototype setToken( GridToken name, GridPrototype value )
            {
                throw new RuntimeException( "oy vey" );
            }
        };
        TheScope child = new TheScope( parent );
        return new TheScope( child );
    }
}
