/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn.state;

import com.google.common.collect.Sets;
import com.shaba.hexaturn.HexaturnBoard;
import com.shaba.state.IMove;

import one.util.streamex.StreamEx;

/**
 * @author Alessandro Aiezza II
 *
 */
public abstract class AbstractMove implements IMove<HexaturnBoard>
{
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
        return StreamEx.of( getSteps() ).joining( ", ", getClass().getSimpleName() + "[", "]" );
    }
}
