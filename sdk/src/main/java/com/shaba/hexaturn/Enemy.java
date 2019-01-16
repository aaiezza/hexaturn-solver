/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn;

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
    private final int     movesPerTurn;
    private final boolean frozen;

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
}
