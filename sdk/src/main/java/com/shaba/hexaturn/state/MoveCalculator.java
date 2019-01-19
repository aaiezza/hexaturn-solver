/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn.state;

import java.util.Optional;

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
        return StreamEx.generate( board.toBuilder()::build )
                .zipWith( getBlockableHexes( board ) )
                .mapKeyValue( this::blockHexAtCoordinate )
                .filter( Optional::isPresent )
                .map( Optional::get );
    }

    private StreamEx<CubeCoordinate> getBlockableHexes( final HexaturnBoard board )
    {
        return StreamEx.of( board.iterator() )
                .mapToEntry( Hexagon::getCubeCoordinate, Hexagon::getSatelliteData )
                .filterValues( Maybe::isPresent ).mapValues( Maybe::get )
                .filterValues( HexaturnSatelliteData::canBlock ).keys();
    }

    private Optional<HexaturnBoard> blockHexAtCoordinate(
            final HexaturnBoard nextBoard,
            final CubeCoordinate coordinate )
    {
        return nextBoard.getGrid().getByCubeCoordinate( coordinate )
                .map( this::blockHex )
                .map( hex -> nextBoard )
                .fold( Optional::empty, Optional::of );
    }

    private Optional<Hexagon<HexaturnSatelliteData>> blockHex( final Hexagon<HexaturnSatelliteData> hex )
    {
        return hex.getSatelliteData()
                .map( HexaturnSatelliteData::block )
                .map( sd -> {
                    hex.setSatelliteData(sd);
                    return hex;
                })
                .fold( Optional::empty, Optional::of );
    }
}
