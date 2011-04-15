/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.ast;

import java.util.List;

/**
 * @version $Rev$
 */
public interface SupportsComments
{
    List<ASTComment> getComments();
    void addComment( ASTComment comment );
}
