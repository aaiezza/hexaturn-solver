/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn.state;

import org.hexworks.mixite.core.api.CubeCoordinate;
import org.hexworks.mixite.core.api.Hexagon;
import org.hexworks.mixite.core.vendor.Maybe;

import com.shaba.hexaturn.HexaturnBoard;
import com.shaba.hexaturn.HexaturnSatelliteData;
import com.shaba.state.NextMoveCalculator;

import one.util.streamex.StreamEx;

/**
 * @author Alessandro Aiezza II
 *
 */
public class MoveCalculator implements NextMoveCalculator<HexaturnBoard>
{
    @Override
    public StreamEx<HexaturnBoard> calculateNextMoves( final HexaturnBoard board )
    {
        return board.isTerminal() ? StreamEx.empty() :
                StreamEx.of( getBlockableHexes( board ) )
                    .mapToEntry( cc -> board.toBuilder().build() )
                    .invert()
                    .mapKeyValue( this::blockHexAtCoordinate )
                    .filter( Maybe::isPresent )
                    .map( Maybe::get )
                    .distinct();
    }

    private StreamEx<CubeCoordinate> getBlockableHexes( final HexaturnBoard board )
    {
        return StreamEx.of( board.iterator() )
                .mapToEntry( Hexagon::getCubeCoordinate, Hexagon::getSatelliteData )
                .filterValues( Maybe::isPresent ).mapValues( Maybe::get )
                .filterValues( HexaturnSatelliteData::canBlock ).keys();
    }

    private Maybe<HexaturnBoard> blockHexAtCoordinate(
            final HexaturnBoard nextBoard,
            final CubeCoordinate coordinate )
    {
        return nextBoard.getGrid().getByCubeCoordinate( coordinate )
                .map( this::blockHex )
                .map( hex -> nextBoard );
    }

    private Maybe<Hexagon<HexaturnSatelliteData>> blockHex( final Hexagon<HexaturnSatelliteData> hex )
    {
        return hex.getSatelliteData()
                .map( HexaturnSatelliteData::block )
                .map( sd -> {
                    hex.setSatelliteData(sd);
                    return hex;
                });
    }
}
