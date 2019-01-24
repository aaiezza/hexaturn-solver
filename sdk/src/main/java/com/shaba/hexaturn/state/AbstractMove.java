/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn.state;

import java.util.Set;

import com.google.common.collect.Sets;
import com.shaba.hexaturn.HexaturnBoard;
import com.shaba.state.Move;
import com.shaba.state.Move.Step;

/**
 * @author Alessandro Aiezza II
 *
 */
@lombok.Data
public abstract class AbstractMove <S extends Step<HexaturnBoard>> implements Move<HexaturnBoard, S>
{
    @lombok.Singular
    protected final Set<S> steps;

    @Override
    public int hashCode()
    {
        return getSteps().hashCode();
    }

    @SuppressWarnings ( "unchecked" )
    @Override
    public boolean equals( final Object obj )
    {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        final AbstractMove<S> other = (AbstractMove<S>) obj;
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
}
