/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ast;

import com.brweber2.protogrid.ssa.GridPrototype;
import com.brweber2.protogrid.ssa.data.BaseDataPrototype;
import com.brweber2.protogrid.ssa.data.DataPrototype;

import java.util.ArrayList;
import java.util.List;

/**
 * @version $Rev$
 */
public abstract class ASTBase extends BaseDataPrototype implements ASTElement
{
    private static final GridPrototype ASTObject = new DataPrototype(null, "ASTObject");

    private List<ASTComment> comments = new ArrayList<ASTComment>();
    private SourceCodeInfo sourceCodeInfo;

    public ASTBase( SourceCodeInfo sourceCodeInfo, String typeName )
    {
        super( ASTObject, typeName );
        this.sourceCodeInfo = sourceCodeInfo;
    }

    public ASTBase( GridPrototype parentProto, String typeName )
    {
        super( parentProto, typeName );
    }

    public void addComment( ASTComment comment )
    {
        comments.add( comment );
    }

    public List<ASTComment> getComments()
    {
        return comments;
    }

    public SourceCodeInfo getSourceCodeInfo()
    {
        return sourceCodeInfo;
    }

    public void setSourceCodeInfo( SourceCodeInfo sourceCodeInfo )
    {
        this.sourceCodeInfo = sourceCodeInfo;
    }
}
