/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.evaluator.builtin;

import com.brweber2.protogrid.ast.ASTParameter;
import com.brweber2.protogrid.ast.ASTParameters;
import com.brweber2.protogrid.ast.ASTToken;
import com.brweber2.protogrid.ssa.GridArgument;
import com.brweber2.protogrid.ssa.GridArguments;
import com.brweber2.protogrid.ssa.GridList;
import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.GridToken;
import com.brweber2.protogrid.ssa.NativeParameters;
import com.brweber2.protogrid.ssa.data.DataToken;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @version $Rev$
 */
public class BuiltInParameters implements NativeParameters
{
    private ASTParameters astParameters;
    private Map<GridToken, GridPrototype> mappedParameters;

    public BuiltInParameters( ASTParameters astParameters )
    {
        this.astParameters = astParameters;
        this.mappedParameters = new LinkedHashMap<GridToken, GridPrototype>();
    }

    public void map( GridArguments args )
    {

        // todo add default values
        int argSize = args.getSize();
        int paramSize = ((GridList<ASTParameter>)astParameters.getSlot(ASTParameters.PARAMS_KEY)).getSize();

        int count = Math.min( argSize, paramSize );

        for ( int i = 0; i < count; i++ )
        {
            ASTParameter param = ((GridList<ASTParameter>)astParameters.getSlot(ASTParameters.PARAMS_KEY)).get( i );
            GridToken token = new DataToken( ((ASTToken)param.getSlot(ASTParameter.PARAMETER_KEY)).getToken() );
            GridArgument arg = args.get( i );
            GridPrototype value = arg.getValue();
            mappedParameters.put( token, value );
        }

        // additional args ignored for now... should be added to implicit value?

    }

    public Map<GridToken, GridPrototype> getMappedParameters()
    {
        return mappedParameters;
    }

    public String toString()
    {
        StringBuilder str = new StringBuilder();
        str.append( " [ " );
        boolean hasany = false;
        for ( ASTParameter astParameter : (GridList<ASTParameter>) astParameters.getSlot(ASTParameters.PARAMS_KEY) )
        {
            str.append( astParameter.toString() );
            str.append( " , " );
            hasany = true;
        }
        String result = str.toString();
        if ( hasany )
        {
            result = result.substring( 0, result.length() - 2 );
        }
        return result + " ] ";
    }
}
