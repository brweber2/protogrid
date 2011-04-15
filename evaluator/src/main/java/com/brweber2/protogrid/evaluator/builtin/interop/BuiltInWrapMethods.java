/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin.interop;

import com.brweber2.protogrid.evaluator.Coerce;
import com.brweber2.protogrid.evaluator.TheContext;
import com.brweber2.protogrid.evaluator.builtin.empty.EmptyBuiltInParameters;
import com.brweber2.protogrid.ssa.GridArgument;
import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.NativeBoolean;
import com.brweber2.protogrid.ssa.NativeWrapMethod;
import com.brweber2.protogrid.ssa.data.BaseDataFunction;
import com.brweber2.protogrid.ssa.data.DataParameters;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @version $Rev$
 */
public class BuiltInWrapMethods extends BaseDataFunction implements NativeWrapMethod<List<Method>>
{
    private List<Method> methods;
    private Object instance;

    public BuiltInWrapMethods( GridPrototype parentProto, Object instance, List<Method> methods )
    {
        super( parentProto, new DataParameters( parentProto, new EmptyBuiltInParameters() ) );
        this.methods = methods;
        this.instance = instance;
    }

    public GridPrototype apply( GridPrototype self, GridArguments args )
    {
        List<Object> values = new ArrayList<Object>(  );
        List<Class> types = new ArrayList<Class>(  );
        for ( GridArgument arg : args )
        {
            Object value = Coerce.coerceFromGrid( arg.getValue() );
            values.add( value );
            types.add( value.getClass() );
        }
        Method method = findMatchingMethod( types, values );
        try
        {
            List nativeValues = nativeCoerce( method.getParameterTypes(), values, types, method.isVarArgs() );
            return Coerce.coerceToGrid( method.invoke( instance, nativeValues.toArray() ) );
        }
        catch ( IllegalAccessException e )
        {
            throw new RuntimeException( "Unable to call method " + method.getName() );
        }
        catch ( InvocationTargetException e )
        {
            e.printStackTrace();
            throw new RuntimeException( "Unable to call method " + method.getName() );
        }
    }

    public static List<Object> nativeCoerce( Class[] paramTypes, List<Object> values, List<Class> types, boolean varargs )
    {
        List<Object> coerced = new ArrayList<Object>();
        for ( int i = 0; i < paramTypes.length; i++ )
        {
            Class argType = types.get(i);
            Object value = values.get(i);
            Class paramType = paramTypes[i];
            // if this is the last parameter of a varargs we'll have an array of our type
            boolean variadic = varargs && paramType.isArray() && i == paramTypes.length - 1;
            if ( variadic )
            {
                // if we already have an array...
                Object arr;
                if ( paramType.isAssignableFrom( types.get(i) ) )
                {
                    arr = values.get(i);
                }
                else
                {
                    paramType = paramType.getComponentType();
                    int start = paramTypes.length - 1;
                    int end = types.size();
                    arr = Array.newInstance( paramType, end - start );
                    for ( int a = 0; a < (end-start); a++ )
                    {
                        Array.set( arr, a, getCoerced( values.get( i + a ), paramType, types.get( i + a ) ) );
                    }
                }
                coerced.add( arr );
            }
            else
            {
                coerced.add( getCoerced( value, paramType, argType ) );
            }
        }
        return coerced;
    }

    public static Object getCoerced(Object value, Class paramType, Class argType)
    {
        if ( paramType.isAssignableFrom( argType ) )
        {
            // leave it alone...
            return value;
        }
        // integers
        else if ( Integer.class.isAssignableFrom( paramType ) )
        {
            String nbr = (String) value;
            return Integer.valueOf( nbr );
        }
        else if ( Integer.TYPE.isAssignableFrom( paramType ) )
        {
            String nbr = (String) value;
            return Integer.parseInt( nbr );
        }
        // longs
        else if ( Long.class.isAssignableFrom( paramType ) )
        {
            String nbr = (String) value;
            return Long.valueOf( nbr );
        }
        else if ( Long.TYPE.isAssignableFrom( paramType ) )
        {
            String nbr = (String) value;
            return Long.parseLong( nbr );
        }
        // shorts
        else if ( Short.class.isAssignableFrom( paramType ) )
        {
            String nbr = (String) value;
            return Short.valueOf( nbr );
        }
        else if ( Short.TYPE.isAssignableFrom( paramType ) )
        {
            String nbr = (String) value;
            return Short.parseShort( nbr );
        }
        // byte
        else if ( Byte.class.isAssignableFrom( paramType ) )
        {
            String nbr = (String) value;
            return Byte.valueOf( nbr );
        }
        else if ( Byte.TYPE.isAssignableFrom( paramType ) )
        {
            String nbr = (String) value;
            return Byte.parseByte( nbr );
        }
        // double
        else if ( Double.class.isAssignableFrom( paramType ) )
        {
            String nbr = (String) value;
            return Double.valueOf( nbr );
        }
        else if ( Double.TYPE.isAssignableFrom( paramType ) )
        {
            String nbr = (String) value;
            return Double.parseDouble( nbr );
        }
        // float
        else if ( Float.class.isAssignableFrom( paramType ) )
        {
            String nbr = (String) value;
            return Float.valueOf( nbr );
        }
        else if ( Float.TYPE.isAssignableFrom( paramType ) )
        {
            String nbr = (String) value;
            return Float.parseFloat( nbr );
        }
        // big int
        else if ( BigInteger.class.isAssignableFrom( paramType ) )
        {
            String nbr = (String) value;
            return new BigInteger( nbr );
        }
        // big decimal
        else if ( BigDecimal.class.isAssignableFrom( paramType ) )
        {
            String nbr = (String) value;
            return new BigDecimal( nbr );
        }
        // booleans
        else if ( Boolean.class.isAssignableFrom( paramType ) )
        {
            Boolean bool = ((NativeBoolean) value).getNative();
            return bool;
        }
        else if ( Boolean.TYPE.isAssignableFrom( paramType ) )
        {
            Boolean bool = (Boolean) value;
            return bool;
        }
        // characters
        else if ( Character.class.isAssignableFrom( paramType ) )
        {
            String str = (String) value;
            if ( str.length() != 1 )
            {
                throw new RuntimeException( "A character should not be a string..." );
            }
            return str.charAt( 0 );
        }
        else if ( Character.TYPE.isAssignableFrom( paramType ) )
        {
            String str = (String)value;
            if ( str.length() != 1 )
            {
                throw new RuntimeException( "A character should not be a string..." );
            }
            return str.charAt( 0 );
        }
        // strings
        else if ( String.class.isAssignableFrom( paramType ) )
        {
            String str = (String)value;
            return str;
        }
        // lists
        else if ( paramType.isAssignableFrom( List.class ) )
        {
            List l = (List)value;
            // todo do we have to iterate the list and native coerce each value???
            return l;
        }
        else
        {
            throw new RuntimeException( "Unable to native coerce?" );
        }

    }

    protected Method findMatchingMethod( List<Class> types, List values )
    {
        for ( Method method : methods )
        {
            // todo check length match first and only if they don't match check varargs....
            if ( method.isVarArgs() )
            {
                boolean allOK = true;
                for ( int i = 0; i < method.getParameterTypes().length - 1; i++ )
                {
                    if ( !argTypeOkForParameter( types.get( i ), method.getParameterTypes()[i], values.get( i ) ) )
                    {
                        allOK = false;
                        break;
                    }
                }
                if ( allOK )
                {
                    // the last param should be an array so check if our type
                    int index = method.getParameterTypes().length-1;
                    Class ptype = method.getParameterTypes()[index];
                    if ( !ptype.isArray() )
                    {
                        throw new RuntimeException( "expected an array in varargs method!" );
                    }
                    ptype = ptype.getComponentType();
                    int max = types.size();
                    // we could have an array...
                    int z = method.getParameterTypes().length - 1;
                    if ( argTypeOkForParameter( types.get( z ), method.getParameterTypes()[z], values.get( z ) ) )
                    {
                        if ( method.getParameterTypes().length != values.size() )
                        {
                            allOK = false;
                            break;
                        }
                        else
                        {
                            if ( argTypeOkForParameter( types.get( z ).getComponentType(), ptype, values.get( z ) ) )
                            {
                                return method;
                            }
                            else
                            {
                                allOK = false;
                                break;
                            }
                        }
                    }
                    else
                    {
                        System.out.println("arg type not ok");
                        // loop over the remaining args and make sure they are the correc type
                        for ( int a = index; a < max; a++ )
                        {
                            if ( !argTypeOkForParameter( types.get( a ), ptype, values.get( a ) ) )
                            {
                                allOK = false;
                                break;
                            }
                        }
                        if ( allOK )
                        {
                            return method;
                        }
                    }
                }
            }
            else if ( method.getParameterTypes().length == types.size() )
            {
                boolean allOK = true;
                for ( int i = 0; i < types.size(); i++ )
                {
                    if ( !argTypeOkForParameter( types.get( i ), method.getParameterTypes()[i], values.get( i )) )
                    {
                        allOK = false;
                        break;
                    }
                }
                if ( allOK )
                {
                    return method;
                }
            }
        }
        throw new RuntimeException( "No method found that matches the types." );
    }

    // todo refactor me!
    public static boolean argTypeOkForParameter( Class argType, Class paramType, Object value )
    {
        if ( paramType.isAssignableFrom( argType ) )
        {
            return true;
        }
        // we could have a primitive type...
//        if ( DataNil.class.isAssignableFrom( argType ) )
//        {
//            return true; // nil/undefined can match any type...
//        }
//        if ( NativeNumber.class.isAssignableFrom( argType) )
//        {
            else if ( Number.class.isAssignableFrom( paramType ) ||
                    Integer.TYPE.isAssignableFrom( paramType ) ||
                    Short.TYPE.isAssignableFrom( paramType ) ||
                    Long.TYPE.isAssignableFrom( paramType ) ||
                    Double.TYPE.isAssignableFrom( paramType ) ||
                    Float.TYPE.isAssignableFrom( paramType ) ||
                    Byte.TYPE.isAssignableFrom( paramType ) )
            {
                if ( String.class.isAssignableFrom( argType ) )
                {
                    try
                    {
                        getCoerced( value, paramType, argType );
                        return true;
                    }
                    catch ( Exception e )
                    {
                        return false;
                    }
                }
                else
                {
                    return false;
                }
            }
//        }
//        else if ( Boolean.class.isAssignableFrom( argType ) || Boolean.TYPE.isAssignableFrom( argType ) )
//        {
            else if ( Boolean.class.isAssignableFrom( paramType ) ||
                    Boolean.TYPE.isAssignableFrom( paramType ) )
            {
                if ( Boolean.class.isAssignableFrom( argType ) ||
                        Boolean.TYPE.isAssignableFrom( argType ) )
                {
                    try
                    {
                        getCoerced( value, paramType, argType );
                        return true;
                    }
                    catch ( Exception e )
                    {
                        return false;
                    }
                }
                else
                {
                    return false;
                }
            }
//        }
//        else if ( NativeString.class.isAssignableFrom( argType ) )
//        {
            else if (  String.class.isAssignableFrom( paramType ) ||
                    Character.class.isAssignableFrom( paramType ) ||
                    Character.TYPE.isAssignableFrom( paramType ) )
            {
                if ( String.class.isAssignableFrom( argType ) ||
                        Character.class.isAssignableFrom( argType ) ||
                        Character.TYPE.isAssignableFrom( argType ) )
                {
                    try
                    {
                        getCoerced( value, paramType, argType );
                        return true;
                    }
                    catch ( Exception e )
                    {
                        return false;
                    }
                }
                else
                {
                    return false;
                }
            }
//        }
//        else if ( NativeList.class.isAssignableFrom( argType ) )
//        {
            else if ( Collection.class.isAssignableFrom( paramType ) )
            {
                if ( Collection.class.isAssignableFrom( argType ) )
                {
                    try
                    {
                        getCoerced( value, paramType, argType );
                        return true;
                    }
                    catch ( Exception e )
                    {
                        return false;
                    }
                }
                else
                {
                    return false;
                }
            }
//        }

        return false;
    }

    public List<Method> getNative()
    {
        return methods;
    }

    public GridPrototype call( GridArguments args )
    {
        return apply( TheContext.getCurrentContext().getSelf(), args );
    }
}
