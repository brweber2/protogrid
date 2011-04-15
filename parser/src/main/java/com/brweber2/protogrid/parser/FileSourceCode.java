/*
 * $Id$
 * Copyright (C) 2011 brweber2
 */
package com.brweber2.protogrid.parser;

import com.brweber2.protogrid.ast.SourceCodeInfo;
import com.brweber2.protogrid.ast.SourceInfoAware;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

/**
 * @version $Rev$
 */
public class FileSourceCode implements SourceCode
{
    private LineNumberReader reader;
    private String sourceName;

    public FileSourceCode( String sourceName, Reader r )
    {
        reader = new LineNumberReader( r );
        this.sourceName = sourceName;
    }

    public void close()
    {
        try
        {
            reader.close();
        }
        catch ( IOException e )
        {
            throw new ParserException( e );
        }
    }

    public SourceCodeInfo createSourceInfo()
    {
        return new SourceCodeInfo( sourceName, reader.getLineNumber() );
    }

    public BigInteger getLineNumber()
    {
        return BigInteger.valueOf( reader.getLineNumber() + 1 );
    }

    public boolean atEOF()
    {
        try
        {
            consumeWhitespace();
            reader.mark( 1 );
            char c = (char) reader.read();
            boolean result = c == -1 || c == Character.MAX_VALUE;
            if ( !result )
            {
                reader.reset();
            }
            return result;
        }
        catch ( IOException e )
        {
            throw new ParserException( e );
        }
    }

    public boolean readStatementEnd()
    {
        consumeWhitespace();
        return charNext( ProtogridParser.SEMI_COLON );
    }

    public void consumeStatementEnd()
    {
        consumeWhitespace();
        consumeToken( ProtogridParser.SEMI_COLON );
    }

    public boolean comparisonNext()
    {
        consumeWhitespace();
        return stringNext( ProtogridParser.COMPARISONS );
    }

    public boolean stringNext( String[] strings )
    {
        int longest = 0;
        Set<String> set = new HashSet<String>();
        for ( String compare : strings )
        {
            set.add( compare );
            if ( compare.length() > longest )
            {
                longest = compare.length();
            }
        }

        try
        {
            reader.mark( longest );
            Set<String> invalids = new HashSet<String>();
            for ( int i = 0; i < longest; i++ )
            {
                char c = (char) reader.read();
                if ( isEOFChar( c ) )
                {
                    return false;
                }
                for ( String s : set )
                {
                    if ( invalids.contains( s ) )
                    {
                        // skip
                    }
                    else if ( c != s.charAt( i ) )
                    {
                        invalids.add( s );
                    }
                    else if ( i >= s.length() - 1 )
                    {
                        // there could be a longer match, (for example, we've match '<' and it could be '<=' but we can return true at this point...
                        reader.reset();
                        return true;
                    }
                }
            }
            reader.reset();
            return false;
        }
        catch ( IOException e )
        {
            throw new ParserException( e );
        }

    }

    private boolean isEOFChar( char c )
    {
        return c == -1 || c == Character.MAX_VALUE;
    }

    // sure seems like this needs a more efficient algorithm ...
    public String readComparison()
    {
        consumeWhitespace();
        return readString( ProtogridParser.COMPARISONS );
    }

    public String readString( String[] strings )
    {
        int longest = 0;
        Set<String> set = new HashSet<String>();
        for ( String compare : strings )
        {
            set.add( compare );
            if ( compare.length() > longest )
            {
                longest = compare.length();
            }
        }

        try
        {
            reader.mark( longest );
            Set<String> matches = new HashSet<String>();
            Set<String> invalids = new HashSet<String>();
            for ( int i = 0; i < longest; i++ )
            {
                char c = (char) reader.read();
                if ( isEOFChar( c ) )
                {
                    throw new ParserException( "Reached EOF while parsing a comparison." );
                }
                for ( String s : set )
                {
                    if ( invalids.contains( s ) )
                    {

                    }
                    else if ( c != s.charAt( i ) )
                    {
                        invalids.add( s );
                    }
                    else if ( i >= s.length() - 1 )
                    {
                        matches.add( s );
                        invalids.add( s );
                    }
                }
            }
            // find the longest match
            String longestMatch = null;
            for ( String match : matches )
            {
                if ( longestMatch == null )
                {
                    longestMatch = match;
                }
                else if ( match.length() > longestMatch.length() )
                {
                    longestMatch = match;
                }
                else if ( match.length() == longestMatch.length() )
                {
                    throw new ParserException( "Should never happen exception..." );
                }
            }
            if ( longestMatch == null )
            {
                throw new ParserException( "Didn't find comparison operator." );
            }
            // we very well could have read past our winner, so rollback and read the correct number of chars
            reader.reset();
            for ( int a = 0; a < longestMatch.length(); a++ )
            {
                reader.read(); // throw it out
            }
            return longestMatch;
        }
        catch ( IOException e )
        {
            throw new ParserException( e );
        }
    }

    public boolean logicNext()
    {
        return stringNext( ProtogridParser.LOGIC_STRINGS );
    }

    public String readLogic()
    {
        return readString( ProtogridParser.LOGIC_STRINGS );
    }

    public void endSourceInfo( SourceInfoAware sourceInfoAware )
    {
        sourceInfoAware.getSourceCodeInfo().setEndLineNumber( getLineNumber() );
    }

    public boolean hasAnyNext( char[] chars )
    {
        try
        {
            consumeWhitespace();
            reader.mark( 1 );
            boolean flag = false;
            char c = (char) reader.read();
            if ( isEOFChar( c ) )
            {
                return false;
            }
            for ( char aChar : chars )
            {
                if ( c == aChar )
                {
                    flag = true;
                    break;
                }
            }
            reader.reset();
            return flag;
        }
        catch ( IOException e )
        {
            throw new ParserException( e );
        }
    }

    private void consumeWhitespace()
    {
        while ( consumeOneWhitespace() )
        {
            // keep going
        }
    }

    private boolean consumeOneWhitespace()
    {
        try
        {
            reader.mark( 1 );
            int read = reader.read();
            if ( isEOFChar( (char) read ) )
            {
                // EOF
                return false;
            }
            if ( !( Character.isWhitespace( read ) || read == ProtogridParser.COMMA ) )
            {
                reader.reset();
                return false;
            }
            return true;
        }
        catch ( IOException e )
        {
            throw new ParserException( e );
        }
    }

    public void consumeToken( String token )
    {
        try
        {
            consumeWhitespace();
            reader.mark( token.length() );
            for ( int i = 0; i < token.length(); i++ )
            {
                char read = (char) reader.read();
                if ( isEOFChar( read ) )
                {
                    throw new ParserException( "Unexpected EOF while reading token " + token + "." );
                }
                if ( token.charAt( i ) != read )
                {
                    reader.reset();
                    throw new ParserException( "Expected token " + token + " at " + reader.getLineNumber() );
                }
            }
        }
        catch ( IOException e )
        {
            throw new ParserException( e );
        }
    }

    public void consumeToken( char token )
    {
        try
        {
            consumeWhitespace();
            char c = (char) reader.read();
            if ( c != token )
            {
                throw new ParserException( "Expected to consume token '" + token + "' near line " + reader.getLineNumber() );
            }
        }
        catch ( IOException e )
        {
            throw new ParserException( e );
        }
    }

    public boolean stringNext()
    {
        return charNext( ProtogridParser.STRING_QUOTE );
    }

    public String readString()
    {
        // read "
        // read until done
        //    mode = off
        //      if \ mode = on
        //      if " and mode off done
        try
        {
            consumeToken( "\"" );
            boolean done = false;
            boolean quoting = false;
            StringBuffer str = new StringBuffer();
            while ( !done )
            {
                char c = (char) reader.read();
                if ( isEOFChar( c ) )
                {
                    throw new ParserException( "Unexpected EOF while reading string." );
                }
                if ( c == '\\' )
                {
                    if ( quoting )
                    {
                        str.append( c );
                        str.append( c );
                        quoting = false;
                    }
                    else
                    {
                        quoting = true;
                    }
                }
                else if ( c == '\"' )
                {
                    if ( quoting )
                    {
                        str.append( c );
                        quoting = false;
                    }
                    else
                    {
                        done = true;
                    }
                }
                else
                {
                    if ( quoting )
                    {
                        str.append( '\\' );
                    }
                    str.append( c );
                    quoting = false;
                }
            }
            return str.toString();
        }
        catch ( IOException e )
        {
            throw new ParserException( e );
        }
    }

    public boolean numberNext()
    {
        char min = '0';
        char max = '9';
        return inCharRange( min, max );
    }

    public String readNumber()
    {
        // read while 0-9 or .
        // keep count of . if more than 1 throw
        try
        {
            char min = '0';
            char max = '9';
            char dot = '.';
            StringBuilder nbr = new StringBuilder();
            boolean done = false;
            int dotCount = 0;
            boolean atLeastOneFound = false;
            while ( !done )
            {
                reader.mark( 1 );
                char c = (char) reader.read();
                if ( isEOFChar( c ) )
                {
                    if ( !atLeastOneFound )
                    {
                        throw new ParserException( "Unexpected EOF in the middle of a number?" );
                    }
                    return nbr.toString();
                }
                if ( ( c >= min && c <= max ) || c == dot )
                {
                    if ( c == dot )
                    {
                        dotCount++;
                    }
                    else
                    {
                        atLeastOneFound = true;
                    }
                    if ( dotCount > 1 )
                    {
                        // calling a method on a number 13.4.isPrime?()
                        reader.reset();
                        return nbr.toString();
                    }
                    nbr.append( c );
                }
                else
                {
                    reader.reset(); // put back one non-number character
                    done = true;
                }
            }
            if ( !atLeastOneFound )
            {
                throw new ParserException( "Unable to find number in expected location." );
            }
            return nbr.toString();
        }
        catch ( IOException e )
        {
            throw new ParserException( e );
        }
    }

    public boolean varDefineNext()
    {
        return tokenNext( ProtogridParser.KEYWORD_VAR );
    }

    public void consumeVarDefine()
    {
        consumeToken( ProtogridParser.KEYWORD_VAR );
    }

    public boolean assignNext()
    {
        return charNext( ProtogridParser.ASSIGN );
    }

    public void consumeAssign()
    {
        consumeToken( ProtogridParser.ASSIGN );
    }

    public boolean slotAccessNext()
    {
        return charNext( ProtogridParser.SLOT_ACCESS );
    }

    public void cosumeSlotAccess()
    {
        consumeToken( ProtogridParser.SLOT_ACCESS );
    }

    public boolean functionInvocationNext()
    {
        return charNext( ProtogridParser.FUNCTION_INVOKE_START );
    }

    public boolean blockNext()
    {
        return charNext( ProtogridParser.BLOCK_START );
    }

    public boolean functionDefineNext()
    {
        return charNext( ProtogridParser.FUNCTION_DEFINE_START );
    }

    public boolean commentNext()
    {
        return charNext( ProtogridParser.LINE_COMMENT ) || charNext( ProtogridParser.COMMENT_START );
    }

    public String readComment()
    {
        // single line comment #
        // multi line comment $* to *$
        try
        {
            if ( charNext( ProtogridParser.LINE_COMMENT ) )
            {
                // single line comment
                consumeToken( ProtogridParser.LINE_COMMENT );
                String comment = reader.readLine();
//                System.out.println("read comment " + comment);
                return comment;
            }
            else if ( charNext( ProtogridParser.COMMENT_START ) )
            {
                consumeToken( ProtogridParser.COMMENT_START );
                consumeToken( ProtogridParser.COMMENT_STAR );
                boolean done = false;
                boolean readyToExit = false;
                StringBuilder str = new StringBuilder();
                while ( !done )
                {
                    char c = (char) reader.read();
                    if ( isEOFChar( c ) )
                    {
                        throw new ParserException( "Unexpected EOF in the middle of a comment." );
                    }
                    if ( readyToExit )
                    {
                        if ( c == ProtogridParser.COMMENT_START )
                        {
                            done = true;
                        }
                        else
                        {
                            str.append( ProtogridParser.COMMENT_STAR );
                            str.append( c );
                            readyToExit = false;
                        }
                    }
                    else
                    {
                        if ( c == ProtogridParser.COMMENT_STAR )
                        {
                            readyToExit = true;
                        }
                        else
                        {
                            str.append( c );
                        }
                    }
                }
                String comment = str.toString();
//                System.out.println("read multi line comment " + comment);
                return comment;
            }
            throw new ParserException( "Comments must start with " + ProtogridParser.LINE_COMMENT + " or " + ProtogridParser.COMMENT_START + ProtogridParser.COMMENT_STAR );
        }
        catch ( IOException e )
        {
            throw new ParserException( e );
        }
    }

    public boolean operatorNext()
    {
        return hasAnyNext( ProtogridParser.OPERATOR_CHARS );
    }

    public String readOperator()
    {
        try
        {
            consumeWhitespace();
            char c = (char) reader.read();
            if ( isEOFChar( c ) )
            {
                throw new ParserException( "Unexpected EOF while looking for an operator near line " + reader.getLineNumber() );
            }
            for ( char o : ProtogridParser.OPERATOR_CHARS )
            {
                if ( c == o )
                {
                    return String.valueOf( c );
                }
            }
            throw new ParserException( "Unable to find an operator where one was expected near line " + reader.getLineNumber() );
        }
        catch ( IOException e )
        {
            throw new ParserException( e );
        }
    }

    public boolean tokenNext()
    {
        // upper case A to Z + lower case a to z
        char min = 'A'; // 65
        char max = 'z';  // 122
        return inCharRange( min, max );
    }

    public String readToken()
    {
        try
        {
            char minUpper = 'A';
            char maxUpper = 'Z';
            char minLower = 'a';
            char maxLower = 'z';
            boolean done = false;
            boolean tokenFound = false;
            StringBuilder str = new StringBuilder();
            while ( !done )
            {
                reader.mark( 1 );
                char c = (char) reader.read();
                if ( isEOFChar( c ) )
                {
                    if ( !tokenFound )
                    {
                        throw new ParserException( "Hit unexpected end of file when looking for a token." );
                    }
                    return str.toString();
                }
                if ( ( c >= minUpper && c <= maxUpper ) || ( c >= minLower && c <= maxLower ) )
                {
                    tokenFound = true;
                    str.append( c );
                }
                else
                {
                    reader.reset();
                    done = true;
                }
            }
            if ( !tokenFound )
            {
                throw new ParserException( "Did not find a token where it was expected." );
            }
            return str.toString();
        }
        catch ( IOException e )
        {
            throw new ParserException( e );
        }
    }

    public boolean tokenNext( String closeToken )
    {
        try
        {
            reader.mark( closeToken.length() );
            for ( int i = 0; i < closeToken.length(); i++ )
            {
                char c = (char) reader.read();
                if ( isEOFChar( c ) )
                {
                    return false;
                }
                if ( c != closeToken.charAt( i ) )
                {
                    reader.reset();
                    return false;
                }
            }
            reader.reset();
            return true;
        }
        catch ( IOException e )
        {
            throw new ParserException( e );
        }
    }

    public boolean tokenNext( char closeToken )
    {
        try
        {
            consumeWhitespace();
            reader.mark( 1 );
            char c = (char) reader.read();
            if ( isEOFChar( c ) )
            {
                return false;
            }
            boolean result = c == closeToken;
            reader.reset();
            return result;
        }
        catch ( IOException e )
        {
            throw new ParserException( e );
        }
    }

    private boolean charNext( char ch )
    {
        try
        {
            consumeWhitespace();
            reader.mark( 1 );
            char read = (char) reader.read();
            boolean result = read == ch;
            reader.reset();
            return result;
        }
        catch ( IOException e )
        {
            throw new ParserException( e );
        }
    }

    private boolean inCharRange( char min, char max )
    {
        try
        {
            consumeWhitespace();
            reader.mark( 1 );
            char read = (char) reader.read();
            if ( isEOFChar( read ) )
            {
                return false;
            }
            boolean result = read >= min && read <= max;
            reader.reset();
            return result;
        }
        catch ( IOException e )
        {
            throw new ParserException( e );
        }
    }
}
