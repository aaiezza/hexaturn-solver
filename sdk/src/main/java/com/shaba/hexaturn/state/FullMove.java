/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn.state;

import java.util.LinkedHashSet;

import com.shaba.hexaturn.HexaturnBoard;

import one.util.streamex.StreamEx;

/**
 * @author Alessandro Aiezza II
 *
 */
@lombok.Data
@lombok.EqualsAndHashCode ( callSuper = true )
public class FullMove extends AbstractMove
{
    private final Move playerMove;
    private final Move enemyMove;

    public FullMove( final Move playerMove, final Move enemyMove )
    {
        this.playerMove = playerMove;
        this.enemyMove = enemyMove;
    }

    @Override
    public LinkedHashSet<Step<HexaturnBoard>> getSteps()
    {
        return StreamEx.of( playerMove.getSteps() )
                .append( enemyMove.getSteps() )
                .toCollection( LinkedHashSet::new );
    }
}
