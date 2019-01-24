/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn.state;

import org.hexworks.mixite.core.api.CubeCoordinate;
import org.hexworks.mixite.core.api.Hexagon;
import org.hexworks.mixite.core.vendor.Maybe;

import com.shaba.hexaturn.HexaturnBoard;
import com.shaba.hexaturn.HexaturnSatelliteData;
import com.shaba.hexaturn.state.step.BlockHexStep;
import com.shaba.state.MoveApplier;
import com.shaba.state.NextMoveCalculator;

import one.util.streamex.StreamEx;

/**
 * @author Alessandro Aiezza II
 *
 */
public class PlayerMoveCalculator implements NextMoveCalculator<HexaturnBoard, Move>, MoveApplier<HexaturnBoard, Move>
{
    @Override
    public StreamEx<Move> calculateNextMoves( final HexaturnBoard board )
    {
        return board.isTerminal() ? StreamEx.empty() :
            StreamEx.of( getBlockableHexes( board ) )
                .map( cc -> Move.builder()
                        .addStep( BlockHexStep.builder()
                            .coordinate( cc ).build() )
                        .build() );
    }

    protected StreamEx<CubeCoordinate> getBlockableHexes( final HexaturnBoard board )
    {
        return StreamEx.of( board.iterator() )
                .mapToEntry( Hexagon::getCubeCoordinate, Hexagon::getSatelliteData )
                .filterValues( Maybe::isPresent ).mapValues( Maybe::get )
                .filterValues( HexaturnSatelliteData::canBlock ).keys();
    }
}
