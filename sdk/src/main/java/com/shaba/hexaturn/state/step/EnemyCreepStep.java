/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn.state.step;

import java.util.Optional;

import org.hexworks.mixite.core.api.CubeCoordinate;
import org.hexworks.mixite.core.api.Hexagon;
import org.hexworks.mixite.core.vendor.Maybe;

import com.shaba.hexaturn.Enemy;
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
public class EnemyCreepStep extends HexStep
{
    private final CubeCoordinate start;
    private final CubeCoordinate end;
    private final int movesLeftForEnemy;

    public Optional<EnemyCreepStepBuilder> nextBuilder()
    {
        return movesLeftForEnemy > 0 ? Optional.of(
            EnemyCreepStep.builder().start( end ).movesLeftForEnemy( movesLeftForEnemy - 1 ) )
                                     : Optional.empty();
    }

    @Override
    public Validation<Seq<IllegalStepException>, Step<HexaturnBoard>> verifyStep(
            final HexaturnBoard board )
    {
        return Validation.combine(
            validateEnemyAtStart( board ),
            validateEndIsPassable( board ),
            validateEnemyIsNotFrozen( board ),
            validateEnemyHasMoves( board ),
            validateMovesLeftGreaterThanOne( board ) )
                .ap( ( a, b, c, d, e ) -> a );
    }

    private Validation<IllegalStepException, Step<HexaturnBoard>> validateEnemyAtStart(
            final HexaturnBoard board )
    {
        return validate( board, start, HexaturnSatelliteData::hasEnemy, s -> Validation.invalid(
            new IllegalStepException( s, "There is no enemy to move" ) ) );
    }

    private Validation<IllegalStepException, Step<HexaturnBoard>> validateEndIsPassable(
            final HexaturnBoard board )
    {
        return validate( board, end, sd -> sd.isPassable(), s -> Validation.invalid(
            new IllegalStepException( s, "The destination hex is not passable" ) ) );
    }

    private Validation<IllegalStepException, Step<HexaturnBoard>> validateEnemyIsNotFrozen(
            final HexaturnBoard board )
    {
        return validate( board, start,
            sd -> sd.getOccupant().filter( o -> o.getClass().isInstance( Enemy.class ) )
                    .map( o -> (Enemy) o ).map( e -> e.isFrozen() ).orElse( false ),
            s -> Validation.invalid( new IllegalStepException( s,
                    "Enemy is frozen and cannot move" ) ) );
    }

    private Validation<IllegalStepException, Step<HexaturnBoard>> validateEnemyHasMoves(
            final HexaturnBoard board )
    {
        return validate( board, start,
            sd -> sd.getOccupant().filter( o -> o.getClass().isInstance( Enemy.class ) )
                    .map( o -> (Enemy) o ).map( e -> e.getMovesPerTurn() > 0 ).orElse( false ),
            s -> Validation.invalid( new IllegalStepException( s,
                    "This enemy does not appear to have moves even when free" ) ) );
    }

    private Validation<IllegalStepException, Step<HexaturnBoard>> validateMovesLeftGreaterThanOne(
            final HexaturnBoard board )
    {
        return validate( board, start, sd -> getMovesLeftForEnemy() > 0, s -> Validation.invalid(
            new IllegalStepException( s, "The enemy has gone as far as it can" ) ) );
    }

    @Override
    protected HexaturnBoard performStep( final HexaturnBoard board )
    {
        final Enemy movingEnemy = getEnemyAtStart( board ).get();
        return removeEnemyAtCoordinate( board.toBuilder().build(), start )
            .flatMap( nextBoard -> addEnemyAtCoordinate( nextBoard, movingEnemy, end ) )
            .get();
    }

    private Optional<Enemy> getEnemyAtStart(final HexaturnBoard board)
    {
        return board.getGrid().getByCubeCoordinate( start )
            .flatMap( Hexagon::getSatelliteData )
            .map( sd -> sd.getOccupant() )
            .orElseGet( Optional::empty )
            .filter( o -> o.getClass().isInstance( Enemy.class ) )
            .map( o -> (Enemy) o );
    }

    private Maybe<HexaturnBoard> removeEnemyAtCoordinate(
            final HexaturnBoard nextBoard,
            final CubeCoordinate coordinate)
    {
        return nextBoard.getGrid().getByCubeCoordinate( coordinate )
            .map( this::removeEnemy )
            .map( hex -> nextBoard );
    }

    private Maybe<Hexagon<HexaturnSatelliteData>> removeEnemy(
            final Hexagon<HexaturnSatelliteData> hex )
    {
        return hex.getSatelliteData()
                .map( HexaturnSatelliteData::removeEnemy )
                .map( sd -> {
                    hex.setSatelliteData( sd );
                    return hex;
                } );
    }

    private Maybe<HexaturnBoard> addEnemyAtCoordinate(
            final HexaturnBoard nextBoard,
            final Enemy movingEnemy,
            final CubeCoordinate coordinate)
    {
        return nextBoard.getGrid().getByCubeCoordinate( coordinate )
            .map( hex -> addEnemy( hex, movingEnemy ) )
            .map( hex -> nextBoard );
    }

    private Maybe<Hexagon<HexaturnSatelliteData>> addEnemy(
            final Hexagon<HexaturnSatelliteData> hex,
            final Enemy movingEnemy )
    {
        return hex.getSatelliteData()
                .map( sd -> {
                    hex.setSatelliteData( sd.toBuilder().occupant( movingEnemy ).build() );
                    return hex;
                } );
    }
}
