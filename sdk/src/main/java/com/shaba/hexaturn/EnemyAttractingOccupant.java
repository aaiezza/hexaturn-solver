/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn;

/**
 * @author Alessandro Aiezza II
 *
 */
public interface EnemyAttractingOccupant extends Occupant
{
    @Override
    public default boolean isPassable()
    {
        return true;
    }
}
