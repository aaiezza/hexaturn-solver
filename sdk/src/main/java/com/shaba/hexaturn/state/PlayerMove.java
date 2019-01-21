/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn.state;

import java.util.LinkedHashSet;
import java.util.Optional;

import com.google.common.collect.Sets;
import com.shaba.hexaturn.HexaturnBoard;

/**
 * @author Alessandro Aiezza II
 *
 */
@lombok.Builder
public class PlayerMove extends Move<HexaturnBoard>
{
    public static final class PlayerMoveBuilder
    {
        private final LinkedHashSet<HexaturnBoard> moveSteps = Sets.newLinkedHashSet();

        public PlayerMoveBuilder moveStep( final HexaturnBoard board )
        {
            Optional.ofNullable( board ).ifPresent( moveSteps::add );
            return this;
        }

        public PlayerMove build()
        {
            final PlayerMove playerMove = new PlayerMove();
            moveSteps.forEach( playerMove.getMoveSteps()::add );
            return playerMove;
        }
    }

    @Override
    public int hashCode()
    {
        return getResultOfMoveSteps().hashCode();
    }

    @Override
    public boolean equals( final Object obj )
    {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        final PlayerMove other = (PlayerMove) obj;
        return getResultOfMoveSteps().equals( other.getResultOfMoveSteps() );
    }

    @Override
    public String toString()
    {
        final StringBuilder out = new StringBuilder( "PlayerMove\n" );

        getMoveSteps().forEach( out::append );

        return out.toString();
    }
}
