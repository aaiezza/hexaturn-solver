/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn.state;

import java.util.Set;

import org.assertj.core.util.Sets;

import com.shaba.hexaturn.HexaturnBoard;
import com.shaba.state.Move.Step;

/**
 * @author Alessandro Aiezza II
 *
 */
public class PlayerMove extends AbstractMove<Step<HexaturnBoard>>
{
    @lombok.Builder ( toBuilder = true )
    public PlayerMove( final Set<Step<HexaturnBoard>> steps )
    {
        super( steps );
    }

    public static final class PlayerMoveBuilder
    {
        private final Set<Step<HexaturnBoard>> steps = Sets.newHashSet();

        public PlayerMoveBuilder addStep( final Step<HexaturnBoard> step )
        {
            steps.add( step );
            return this;
        }
        
        private PlayerMoveBuilder steps(final Set<Step<HexaturnBoard>> steps)
        {
            return this;
        }
    }
}
