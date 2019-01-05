/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn;

/**
 * @author Alessandro Aiezza II
 *
 */
public interface Enemy extends Occupant
{
    public default int getMovesPerTurn() {
        return 1;
    }
}
