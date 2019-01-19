/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn.state;

import java.util.LinkedHashSet;

import com.google.common.collect.Sets;
import com.shaba.hexaturn.HexaturnBoard;

/**
 * @author Alessandro Aiezza II
 */
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = true)
@lombok.Builder ( toBuilder = true )
public class Move extends AbstractMove
{
    private final LinkedHashSet<Step<HexaturnBoard>> steps;

    public static final class MoveBuilder
    {
        private final LinkedHashSet<Step<HexaturnBoard>> steps = Sets.newLinkedHashSet();

        public MoveBuilder addStep( final Step<HexaturnBoard> step )
        {
            steps.add( step );
            return this;
        }

        private MoveBuilder steps( final LinkedHashSet<Step<HexaturnBoard>> steps )
        {
            return this;
        }
    }
}
