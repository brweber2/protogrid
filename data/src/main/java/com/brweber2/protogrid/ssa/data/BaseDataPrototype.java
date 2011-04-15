/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ssa.data;

import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridFunction;
import com.brweber2.protogrid.ssa.GridParameters;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.GridToken;
import com.brweber2.protogrid.ssa.NativeFunction;
import com.brweber2.protogrid.ssa.NativeParameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @version $Rev$
 */
public abstract class BaseDataPrototype implements GridPrototype
{
    protected Map<GridToken, GridPrototype> slots = new HashMap<GridToken, GridPrototype>();
    protected GridPrototype parentProto;
    protected String typeName;

    private GridToken noSlotToken = new DataToken( "noslot" );

    protected BaseDataPrototype( GridPrototype parentProto, String typeName )
    {
        this.parentProto = parentProto;
        this.typeName = typeName;
        if ( parentProto == null )
        {
            return;
        }
        DataFunction typeFunc = new DataFunction( null, new NativeFunction()
        {
            public GridPrototype invoke( GridFunction function, GridPrototype self, GridArguments args )
            {
                return new DataString( null, BaseDataPrototype.this.typeName );
            }

            public GridParameters getParameters()
            {
                return new DataParameters(null, new NativeParameters()
                {
                    public void map( GridArguments args )
                    {
                    }

                    public Map<GridToken, GridPrototype> getMappedParameters()
                    {
                        return new HashMap<GridToken, GridPrototype>();
                    }
                } );
            }
        } );
        slots.put( new DataToken( "type" ), typeFunc );
        DataFunction slotFunc = new DataFunction( null, new NativeFunction()
        {
            public GridPrototype invoke( GridFunction function, GridPrototype self, GridArguments args )
            {
                List<GridPrototype> list = new ArrayList<GridPrototype>();
                for ( GridToken gridToken : BaseDataPrototype.this.slots.keySet() )
                {
                    list.add( new DataString( null, gridToken.getToken() ) );
                }
                return new DataList( null, list );
            }

            public GridParameters getParameters()
            {
                return new DataParameters( null, new NativeParameters()
                {
                    public void map( GridArguments args )
                    {
                    }

                    public Map<GridToken, GridPrototype> getMappedParameters()
                    {
                        return new HashMap<GridToken, GridPrototype>();
                    }
                } );
            }
        } );
        slots.put( new DataToken( "slots" ), slotFunc );
        DataFunction parentFunc = new DataFunction( null, new NativeFunction()
        {
            public GridPrototype invoke( GridFunction function, GridPrototype self, GridArguments args )
            {
                return BaseDataPrototype.this.parentProto;
            }

            public GridParameters getParameters()
            {
                return new DataParameters( null, new NativeParameters()
                {
                    public void map( GridArguments args )
                    {
                    }

                    public Map<GridToken, GridPrototype> getMappedParameters()
                    {
                        return new HashMap<GridToken, GridPrototype>();
                    }
                } );
            }
        } );
        slots.put( new DataToken( "proto" ), parentFunc );
    }

    public GridPrototype getParentProto()
    {
        return parentProto;
    }

    public void setParentProto( GridPrototype proto )
    {
        this.parentProto = proto;
    }

    public boolean containsSlot( GridToken name )
    {
        return slots.containsKey( name );
    }

    public boolean hasSlot( GridToken name )
    {
        if ( slots.containsKey( name ) )
        {
            return true;
        }
        else if ( parentProto != null )
        {
            return parentProto.hasSlot( name );
        }
        return false;
    }

    public GridPrototype getSlot( GridToken name )
    {
        if ( slots.containsKey( name ) )
        {
            return slots.get( name );
        }
        if ( parentProto != null && parentProto.hasSlot( name ) )
        {
            return parentProto.getSlot( name );
        }
        if ( slots.containsKey( noSlotToken ) )
        {
            return slots.get( noSlotToken );
        }
        if ( parentProto != null && parentProto.hasSlot( noSlotToken ) )
        {
            return parentProto.getSlot( noSlotToken );
        }
        throw new RuntimeException( "No such slot " + name );
    }

    public GridPrototype defineSlot( GridToken name )
    {
        if ( hasSlot( name ) )
        {
            throw new RuntimeException( "Slot " + name + " is already defined." );
        }
        slots.put( name, DataNil.UNDEFINED );
        return DataNil.UNDEFINED;
    }

    public GridToken defineIfNotDefined( GridToken name )
    {
        if ( !hasSlot( name ) )
        {
            slots.put( name, DataNil.UNDEFINED );
        }
        return name;
    }

    public GridPrototype defineAndAssignSlot( GridToken name, GridPrototype value )
    {
        if ( hasSlot( name ) )
        {
            throw new RuntimeException( "Slot " + name + " is already defined." );
        }
        slots.put( name, value );
        return value;
    }

    public GridToken assignIfNotDefined( GridToken name, GridPrototype value )
    {
        if ( !hasSlot( name ) )
        {
            slots.put( name, value );
        }
        return name;
    }

    public GridPrototype setSlot( GridToken name, GridPrototype value )
    {
        // todo make sure already defined?
        slots.put( name, value );
        return value;
    }

    public String toString()
    {
        return typeName;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public Set<GridToken> getSlotNames()
    {
        return slots.keySet();
    }
}
