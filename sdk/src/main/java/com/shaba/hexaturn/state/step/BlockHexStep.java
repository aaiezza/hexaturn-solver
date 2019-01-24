/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn.state.step;

import java.util.function.Function;
import java.util.function.Predicate;

import org.hexworks.mixite.core.api.CubeCoordinate;
import org.hexworks.mixite.core.api.Hexagon;
import org.hexworks.mixite.core.vendor.Maybe;

import com.shaba.hexaturn.HexaturnBoard;
import com.shaba.hexaturn.HexaturnSatelliteData;
import com.shaba.state.IllegalStepException;
import com.shaba.state.Move.Step;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import one.util.streamex.StreamEx;

/**
 * @author Alessandro Aiezza II
 *
 */
@lombok.Data
@lombok.EqualsAndHashCode ( callSuper = true )
@lombok.Builder
public class BlockHexStep extends Step<HexaturnBoard>
{
    private final CubeCoordinate coordinate;

    @Override
    public Validation<Seq<IllegalStepException>, Step<HexaturnBoard>> verifyStep(
            final HexaturnBoard board,
            final Step<HexaturnBoard> step )
    {
        return Validation.combine( validatePassable( board, step ),
            validateIsBlockable( board, step ), validateIsOccupied( board, step ),
            validateIsNotGoal( board, step ), validateIsNotAlreadyBlocked( board, step ) )
                .ap( ( a, b, c, d, e ) -> a );
    }

    private Validation<IllegalStepException, Step<HexaturnBoard>> validate(
            final HexaturnBoard board,
            final Step<HexaturnBoard> step,
            final Predicate<HexaturnSatelliteData> testHex,
            final Function<Step<HexaturnBoard>, Validation<IllegalStepException, Step<HexaturnBoard>>> invalid )
    {
        return board.getGrid().getByCubeCoordinate( coordinate ).fold(
            () -> coordinateMustExistException( step ),
            hex -> hex.getSatelliteData().fold(
                () -> coordinateMustHaveHexWithDataException( step ),
                sd -> testHex.test( sd ) ? Validation.valid( step ) : invalid.apply( step ) ) );
    }

    private Validation<IllegalStepException, Step<HexaturnBoard>> validatePassable(
            final HexaturnBoard board,
            final Step<HexaturnBoard> step )
    {
        return validate( board, step, HexaturnSatelliteData::isPassable, s -> Validation.invalid(
            new IllegalStepException( s, "Hex is not passable, and therefore not blockable" ) ) );
    }

    private Validation<IllegalStepException, Step<HexaturnBoard>> validateIsBlockable(
            final HexaturnBoard board,
            final Step<HexaturnBoard> step )
    {
        return validate( board, step, HexaturnSatelliteData::isBlockable,
            s -> Validation.invalid( new IllegalStepException( s, "Hex is not blockable" ) ) );
    }

    private Validation<IllegalStepException, Step<HexaturnBoard>> validateIsOccupied(
            final HexaturnBoard board,
            final Step<HexaturnBoard> step )
    {
        return validate( board, step, sd -> !sd.getOccupant().isPresent(),
            s -> Validation.invalid( new IllegalStepException( s, "Hex is occupied" ) ) );
    }

    private Validation<IllegalStepException, Step<HexaturnBoard>> validateIsNotGoal(
            final HexaturnBoard board,
            final Step<HexaturnBoard> step )
    {
        return validate( board, step, sd -> !sd.hasGoal(),
            s -> Validation.invalid( new IllegalStepException( s, "Hex has a goal" ) ) );
    }

    private Validation<IllegalStepException, Step<HexaturnBoard>> validateIsNotAlreadyBlocked(
            final HexaturnBoard board,
            final Step<HexaturnBoard> step )
    {
        return validate( board, step, sd -> sd.isBlockable() && sd.getBlocksBeforeBlocked() <= 0,
            s -> Validation.invalid( new IllegalStepException( s, "Hex is already blocked" ) ) );
    }

    private Validation<IllegalStepException, Step<HexaturnBoard>> coordinateMustExistException(
            final Step<HexaturnBoard> step )
    {
        return Validation.invalid(
            new IllegalStepException( step, "Coordinate does not exist for this step" ) );
    }

    private Validation<IllegalStepException, Step<HexaturnBoard>> coordinateMustHaveHexWithDataException(
            final Step<HexaturnBoard> step )
    {
        return Validation.invalid(
            new IllegalStepException( step, "Hex at coordinate does not have satellite data" ) );
    }

    @Override
    protected HexaturnBoard performStep( final HexaturnBoard t )
    {
        return blockHexAtCoordinate( t.toBuilder().build(), coordinate ).get();
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
        return nextBoard.getGrid().getByCubeCoordinate( coordinate ).map( this::blockHex )
                .map( hex -> nextBoard );
    }

    private Maybe<Hexagon<HexaturnSatelliteData>> blockHex(
            final Hexagon<HexaturnSatelliteData> hex )
    {
        return hex.getSatelliteData().map( HexaturnSatelliteData::block ).map( sd -> {
            hex.setSatelliteData( sd );
            return hex;
        } );
    }

}
