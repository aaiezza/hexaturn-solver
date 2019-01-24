/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn.state;

import java.util.LinkedHashSet;

import com.google.common.collect.Sets;
import com.shaba.hexaturn.HexaturnBoard;
import com.shaba.state.IMove;

/**
 * @author Alessandro Aiezza II
 *
 */
@lombok.Data
@lombok.Builder ( toBuilder = true )
public class Move implements IMove<HexaturnBoard>
{
    private final LinkedHashSet<Step<HexaturnBoard>> steps;

    @Override
    public int hashCode()
    {
        return getSteps().hashCode();
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
        final Move other = (Move) obj;
        return Sets.symmetricDifference( getSteps(), other.getSteps() ).isEmpty();
    }

    @Override
    public String toString()
    {
        final StringBuilder out = new StringBuilder(getClass().getSimpleName());
        out.append( "\n" );

        getSteps().forEach( out::append );

        return out.toString();
    }
    
    public static final class MoveBuilder
    {
        private final LinkedHashSet<Step<HexaturnBoard>> steps = Sets.newLinkedHashSet();

        public MoveBuilder addStep( final Step<HexaturnBoard> step )
        {
            steps.add( step );
            return this;
        }

        private MoveBuilder steps( final LinkedHashSet<Step<HexaturnBoard>> steps )
        {
            return this;
        }
    }
}
