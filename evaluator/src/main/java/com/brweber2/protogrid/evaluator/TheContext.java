/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator;

import com.brweber2.protogrid.ssa.GridFunction;
import com.brweber2.protogrid.ssa.GridPrototype;

/**
 * @version $Rev$
 */
public class TheContext
{
    private static ThreadLocal<TheContext> context = new ThreadLocal<TheContext>()
    {
        @Override
        protected TheContext initialValue()
        {
            return new TheContext( Bootstrap.getGridRoot() );
        }
    };

    public static TheContext getCurrentContext()
    {
        return context.get();
    }

    public static void clearContext()
    {
        context.set( new TheContext( Bootstrap.getGridRoot() ) );
    }

    // end Static section

    private GridPrototype gridPrototype;
    private GridPrototype self;

    private TheContext( GridPrototype gridPrototype )
    {
        set( gridPrototype );
    }

    public GridPrototype getGridPrototype()
    {
        return gridPrototype;
    }

    public void setGridPrototype( GridPrototype gridPrototype )
    {
        this.gridPrototype = gridPrototype;
    }

    public GridPrototype getSelf()
    {
        return self;
    }

    public void setSelf( GridPrototype self )
    {
        this.self = self;
    }

    public GridPrototype set( GridPrototype gridPrototype )
    {
        setGridPrototype( gridPrototype );
        if ( !( gridPrototype instanceof GridFunction ) )
        {
            setSelf( gridPrototype );
        }
        return gridPrototype;
    }
}
