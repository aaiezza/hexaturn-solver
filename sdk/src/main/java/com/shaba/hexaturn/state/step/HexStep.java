/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn.state.step;

import java.util.function.Function;
import java.util.function.Predicate;

import org.hexworks.mixite.core.api.CubeCoordinate;

import com.shaba.hexaturn.HexaturnBoard;
import com.shaba.hexaturn.HexaturnSatelliteData;
import com.shaba.state.IMove.Step;
import com.shaba.state.IllegalStepException;

import io.vavr.control.Validation;

/**
 * @author Alessandro Aiezza II
 *
 */
public abstract class HexStep extends Step<HexaturnBoard>
{

    protected Validation<IllegalStepException, Step<HexaturnBoard>> validate(
            final HexaturnBoard board,
            final CubeCoordinate coordinate,
            final Predicate<HexaturnSatelliteData> testHex,
            final Function<Step<HexaturnBoard>, Validation<IllegalStepException, Step<HexaturnBoard>>> invalid )
    {
        return board.getGrid().getByCubeCoordinate( coordinate ).fold(
            () -> coordinateMustExistException( this ),
            hex -> hex.getSatelliteData().fold(
                () -> coordinateMustHaveHexWithDataException( this ),
                sd -> testHex.test( sd ) ? Validation.valid( this ) : invalid.apply( this ) ) );
    }

    protected Validation<IllegalStepException, Step<HexaturnBoard>> coordinateMustExistException(
            final Step<HexaturnBoard> step )
    {
        return Validation.invalid(
            new IllegalStepException( step, "Coordinate does not exist for this step" ) );
    }

    protected Validation<IllegalStepException, Step<HexaturnBoard>> coordinateMustHaveHexWithDataException(
            final Step<HexaturnBoard> step )
    {
        return Validation.invalid(
            new IllegalStepException( step, "Hex at coordinate does not have satellite data" ) );
    }
}
