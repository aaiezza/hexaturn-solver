/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn.state;

import java.util.Set;

import com.shaba.hexaturn.HexaturnBoard;
import com.shaba.state.Move.Step;

/**
 * @author Alessandro Aiezza II
 *
 */
public class EnemyMove extends AbstractMove<Step<HexaturnBoard>>
{
    @lombok.Builder ( toBuilder = true )
    public EnemyMove( final Set<Step<HexaturnBoard>> steps )
    {
        super( steps );
    }
}
