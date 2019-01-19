/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn;

import static java.lang.String.format;

import lombok.AccessLevel;

/**
 * @author Alessandro Aiezza II
 *
 */
@lombok.Data
@lombok.AllArgsConstructor ( access = AccessLevel.PRIVATE )
@lombok.Builder ( toBuilder = true )
public class Enemy implements Occupant
{
    @lombok.Builder.Default
    private final int     movesPerTurn = 1;
    @lombok.Builder.Default
    private final boolean frozen       = false;

    public Enemy()
    {
        movesPerTurn = 1;
        frozen = false;
    }

    public Enemy thaw()
    {
        return toBuilder().frozen( false ).build();
    }

    public Enemy freeze()
    {
        return toBuilder().frozen( true ).build();
    }

    @Override
    public boolean isPassable()
    {
        return false;
    }

    @Override
    public String toString()
    {
        final StringBuilder out = new StringBuilder( "e" );

        if ( frozen )
            out.append( "f" );

        out.append( format( "%d;", movesPerTurn ) );

        return out.toString();
    }
}
