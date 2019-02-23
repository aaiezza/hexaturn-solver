/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn;

/**
 * @author Alessandro Aiezza II
 *
 */
@FunctionalInterface
public interface Occupant {
    /**
     * The presence of this occupant in a hex may cause it to prevent other
     * {@link Occupant occupants} from entering the hex also. That property is
     * called {@code passibility} and is to be defined by this method.
     * 
     * @return {@code true} when the presence of this occupant in a hex does
     *         <b>not</b> make said hex impassable to other {@link Occupant
     *         occupants}; Otherwise, {@code false}.
     */
    public boolean isPassable();
}
