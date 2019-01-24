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
public class PlayerMoveCalculator implements NextMoveCalculator<HexaturnBoard, PlayerMove>, MoveApplier<HexaturnBoard, PlayerMove>
{
    @Override
    public StreamEx<PlayerMove> calculateNextMoves( final HexaturnBoard board )
    {
        return board.isTerminal() ? StreamEx.empty() : StreamEx.of( getBlockableHexes( board ) )
                .mapToEntry( cc -> board.toBuilder().build() ).invert()
                .mapKeyValue( ( b, cc ) -> PlayerMove.builder()
                        .addStep( BlockHexStep.builder().coordinate( cc ).build() ).build() );
    }

    private StreamEx<CubeCoordinate> getBlockableHexes( final HexaturnBoard board )
    {
        return StreamEx.of( board.iterator() )
                .mapToEntry( Hexagon::getCubeCoordinate, Hexagon::getSatelliteData )
                .filterValues( Maybe::isPresent ).mapValues( Maybe::get )
                .filterValues( HexaturnSatelliteData::canBlock ).keys();
    }
}
