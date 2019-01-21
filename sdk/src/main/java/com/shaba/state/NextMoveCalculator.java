/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.state;

import java.util.stream.Stream;

import com.shaba.hexaturn.state.Move;

/**
 * @author Alessandro Aiezza II
 */
@FunctionalInterface
public interface NextMoveCalculator <T, M extends Move<? extends T>>
{
    /**
     * M is some kind of collection of moves that end in the state that t will
     * arrive to once a move of some kind is complete
     * 
     * @param t
     *            the state representation.
     * @return A {@link Stream} of some type that represents a unique, ordered
     *         set of moves that result in state that type t will arrive at.
     */
    public Stream<M> calculateNextMoves( final T t );
}
