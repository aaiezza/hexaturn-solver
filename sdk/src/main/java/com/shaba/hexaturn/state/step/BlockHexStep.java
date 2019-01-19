/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn.state.step;

import org.hexworks.mixite.core.api.CubeCoordinate;
import org.hexworks.mixite.core.api.Hexagon;
import org.hexworks.mixite.core.vendor.Maybe;

import com.shaba.hexaturn.HexaturnBoard;
import com.shaba.hexaturn.HexaturnSatelliteData;
import com.shaba.state.IMove.Step;
import com.shaba.state.IllegalStepException;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;

/**
 * @author Alessandro Aiezza II
 *
 */
@lombok.Data
@lombok.EqualsAndHashCode ( callSuper = true )
@lombok.Builder
public class BlockHexStep extends HexStep
{
    private final CubeCoordinate coordinate;

    @Override
    public Validation<Seq<IllegalStepException>, Step<HexaturnBoard>> verifyStep(
            final HexaturnBoard board )
    {
        return Validation.combine(
            validatePassable( board ),
            validateIsBlockable( board ),
            validateIsOccupied( board ),
            validateIsNotGoal( board ),
            validateIsNotAlreadyBlocked( board) )
                .ap( ( a, b, c, d, e ) -> a );
    }

    private Validation<IllegalStepException, Step<HexaturnBoard>> validatePassable(
            final HexaturnBoard board )
    {
        return validate( board, coordinate, HexaturnSatelliteData::isPassable, s -> Validation.invalid(
            new IllegalStepException( s, "Hex is not passable, and therefore not blockable" ) ) );
    }

    private Validation<IllegalStepException, Step<HexaturnBoard>> validateIsBlockable(
            final HexaturnBoard board )
    {
        return validate( board, coordinate, HexaturnSatelliteData::isBlockable,
            s -> Validation.invalid( new IllegalStepException( s, "Hex is not blockable" ) ) );
    }

    private Validation<IllegalStepException, Step<HexaturnBoard>> validateIsOccupied(
            final HexaturnBoard board )
    {
        return validate( board, coordinate, sd -> !sd.getOccupant().isPresent(),
            s -> Validation.invalid( new IllegalStepException( s, "Hex is occupied" ) ) );
    }

    private Validation<IllegalStepException, Step<HexaturnBoard>> validateIsNotGoal(
            final HexaturnBoard board )
    {
        return validate( board, coordinate, sd -> !sd.hasGoal(),
            s -> Validation.invalid( new IllegalStepException( s, "Hex has a goal" ) ) );
    }

    private Validation<IllegalStepException, Step<HexaturnBoard>> validateIsNotAlreadyBlocked(
            final HexaturnBoard board )
    {
        return validate( board, coordinate, sd -> sd.isBlockable() && sd.getBlocksBeforeBlocked() <= 0,
            s -> Validation.invalid( new IllegalStepException( s, "Hex is already blocked" ) ) );
    }

    @Override
    protected HexaturnBoard performStep( final HexaturnBoard board )
    {
        return blockHexAtCoordinate( board.toBuilder().build(), coordinate ).get();
    }

    private Maybe<HexaturnBoard> blockHexAtCoordinate(
            final HexaturnBoard nextBoard,
            final CubeCoordinate coordinate )
    {
        return nextBoard.getGrid().getByCubeCoordinate( coordinate )
                .map( this::blockHex )
                .map( hex -> nextBoard );
    }

    private Maybe<Hexagon<HexaturnSatelliteData>> blockHex(
            final Hexagon<HexaturnSatelliteData> hex )
    {
        return hex.getSatelliteData()
                .map( HexaturnSatelliteData::block )
                .map( sd -> {
                    hex.setSatelliteData( sd );
                    return hex;
                } );
    }
}
