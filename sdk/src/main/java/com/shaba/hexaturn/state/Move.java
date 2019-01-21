/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn.state;

import java.util.LinkedHashSet;
import java.util.Optional;

import com.google.common.collect.Sets;

/**
 * @author Alessandro Aiezza II
 *
 */
public abstract class Move <T> implements Comparable<Move<T>>
{
    private final LinkedHashSet<T> moveSteps = Sets.newLinkedHashSet();

    public final LinkedHashSet<T> getMoveSteps()
    {
        return moveSteps;
    }

    public final Optional<T> getResultOfMoveSteps()
    {
        return getMoveSteps().stream().reduce( this::lastElement );
    }

    @Override
    public final int compareTo( final Move<T> o )
    {
        return getResultOfMoveSteps().hashCode() - o.getResultOfMoveSteps().hashCode();
    }

    private T lastElement( final T t1, final T t2 )
    {
        return t2;
    }
}
