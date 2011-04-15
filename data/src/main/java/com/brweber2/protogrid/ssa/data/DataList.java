/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa.data;

import com.brweber2.protogrid.ssa.GridList;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.NativeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @version $Rev$
 */
public class DataList<T extends GridPrototype> extends BaseDataPrototype implements GridList<T>, NativeList
{
    private static GridPrototype LIST_PROTOTYPE = new DataPrototype( null, "List" );
    private List<T> items = new ArrayList<T>();

    public DataList( Collection<T> items )
    {
        super( LIST_PROTOTYPE, LIST_PROTOTYPE.getTypeName() );
        this.items = new ArrayList<T>( items );
    }

    public DataList( GridPrototype parent, Collection<T> items )
    {
        super( parent, "List" );
        this.items.addAll( items );
    }

    public DataList( GridPrototype parent, T... items )
    {
        super( parent, "List" );
        if ( items != null )
        {
            this.items.addAll( Arrays.asList( items ) );
        }
    }

    public T get( int i )
    {
        return items.get( i );
    }

    public int getSize()
    {
        return items.size();
    }

    public void add( T prototype )
    {
        items.add( prototype );
    }

    public Iterator<T> iterator()
    {
        return items.iterator();
    }

    public List getList()
    {
        return items;
    }

    public String toString()
    {
        StringBuilder str = new StringBuilder();
        str.append( " List" ); // has to match value from Bootstrap
        str.append( "(" );
        int count = 0;
        for ( GridPrototype item : items )
        {
            str.append( item.toString() );
            if ( count < getSize() - 1 )
            {
                str.append( "," );
            }
            count++;
        }
        str.append( ") " );
        return str.toString();
    }

    public List getNative()
    {
        return items;
    }
}
