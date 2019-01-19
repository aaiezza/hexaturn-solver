/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn.state;

import java.util.Optional;
import java.util.stream.Stream;

import com.shaba.hexaturn.HexaturnBoard;
import com.shaba.state.MoveApplier;
import com.shaba.state.NextMoveCalculator;

import io.vavr.control.Either;

/**
 * @author Alessandro Aiezza II
 *
 */
@lombok.Data
public class NextFullMoveCalculator implements NextMoveCalculator<HexaturnBoard, FullMove>, MoveApplier<HexaturnBoard, FullMove>
{
    private final PlayerMoveCalculator playerMoveCalculator;
    private final EnemyMoveCalculator  enemyMoveCalculator;

    @Override
    public Stream<FullMove> calculateNextMoves( final HexaturnBoard board )
    {
        return playerMoveCalculator.calculateNextMoves( board )
            .mapToEntry( playerMove -> playerMoveCalculator.applyMove( board, playerMove ) )
            .filterValues( Either::isRight ).mapValues( Either::get )
            .mapValues( enemyMoveCalculator::calculateNextMove )
            .filterValues( Optional::isPresent ).mapValues( Optional::get )
            .mapKeyValue( FullMove::new );
    }
}
