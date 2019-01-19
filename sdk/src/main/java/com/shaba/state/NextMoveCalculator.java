/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.state;

import java.util.stream.Stream;

/**
 * @author Alessandro Aiezza II
 */
@FunctionalInterface
public interface NextMoveCalculator<T>
{
    public Stream<T> calculateNextMoves(final T t);
}
