package com.shaba.hexaturn;

import com.google.common.collect.ImmutableList;

import io.vavr.control.Either;
import io.vavr.control.Either.Left;
import io.vavr.control.Either.Right;
import one.util.streamex.StreamEx;

/**
 * @author Alessandro Aiezza II
 *
 */
@lombok.EqualsAndHashCode
public abstract class AbstractSatelliteData
        implements org.hexworks.mixite.core.api.contract.SatelliteData
{
    private static final ImmutableList<Occupant> NO_OCCUPANTS = ImmutableList.of();
    // @formatter:off
    public static final AbstractSatelliteData BORDER_HEX   = 
        new AbstractSatelliteData() {
            @Override public String toString() { return "BORDER_HEX"; }
            @Override public int getBlocksBeforeBlocked() { return 0; }
            @Override public ImmutableList<Occupant> getOccupants() { return NO_OCCUPANTS; }
            @Override public boolean hasGoal() { return false; }
            @Override public Either<AbstractSatelliteData, AbstractSatelliteData> block() { return Either.left(this); }
            @Override public boolean wasNeverBlockable() { return true; }
        };
    // @formatter:on

    public abstract boolean hasGoal();

    public boolean isPassable()
    {
        return !isBlocked() && streamOccupants().allMatch( Occupant::isPassable );
    }

    /**
     * A hex can be blocked if:<br/>
     * <ul>
     * <li>There are <b>no occupants</b> in it,</li>
     * <li>it is <b>not already blocked</b>,</li>
     * <li>or if a <b>goal is not present</b> at the hex</li>
     * </ul>
     * 
     * @return whether or not this hex can be blocked.
     */
    public boolean canBlock()
    {
        return getOccupants().isEmpty() || ( !isBlocked() && !hasGoal() );
    }

    /**
     * Perform a "block" action on this hex.<br/>
     * If the hex <b>cannot</b> be blocked via the output of
     * {@link AbstractSatelliteData#canBlock()}, return this instance of the
     * {@link AbstractSatelliteData}.<br/>
     * Otherwise, proceed to create a new, immutable instance of
     * {@link AbstractSatelliteData} where this hex is not blocked preferably
     * with a {@link AbstractSatelliteData#getBlocksBeforeBlocked()
     * blocksBeforeBlocked} value of 1 less than this satellite data object.
     * 
     * @return {@link Either} the same instance of the SatelliteData as an
     *         {@link Left Either.Left} if the hex could not be blocked, or an
     *         {@link Right Either.Right} with SatelliteData returning 1 less
     *         {@code blocksBeforeBlocked}.
     */
    public abstract <D extends AbstractSatelliteData> Either<D, D> block();

    /**
     * A hex is blocked when the number of "blocks" needing to fully block it
     * reach, or recede below, 0.
     * 
     * @return whether or not the hex is (already) blocked.
     */
    public boolean isBlocked()
    {
        return getBlocksBeforeBlocked() <= 0;
    }

    /**
     * A hex sometimes must be "blocked" more than once to be considered fully
     * impassable by {@link Occupant occupants} that may wish to enter this hex.
     * 
     * @return the number of "blocks" needed before this hex is considered
     *         {@code blocked}.
     */
    public abstract int getBlocksBeforeBlocked();

    /**
     * This trait notably refers to whether or not a hex was ever blockable in
     * the first place. In the game, these are represented as white hexes.
     * 
     * @return whether or not the hex was ever blockable in the first place.
     */
    public abstract boolean wasNeverBlockable();

    /**
     * The cost of enter into this hex that may be made less severe from an
     * enemies perspective if more "blocks" are required to prevent this hex
     * from being {@link AbstractSatelliteData#isBlocked() passable}.<br/>
     * <br/>
     * This movement cost however, notably ignores the state of surrounding
     * hexes, which should be accounted for when calculating enemy movements
     * which would more closely replicate the game.
     */
    @Override
    public double getMovementCost()
    {
        return getBlocksBeforeBlocked() > 0 ? 1.0f / getBlocksBeforeBlocked() : Double.MAX_VALUE;
    }

    /**
     * @return the {@link Occupant occupants} on this hex.
     */
    public abstract ImmutableList<Occupant> getOccupants();

    /**
     * @return a stream of the {@link Occupant occupants} on this hex.
     */
    public final StreamEx<Occupant> streamOccupants()
    {
        return StreamEx.of( getOccupants() );
    }

    public boolean hasOccupants()
    {
        return !getOccupants().isEmpty();
    }

    /**
     * An {@link Enemy} should find a hex "attractive" if:<br/>
     * <ul>
     * <li>the hex is occupied by an {@link EnemyAttractingOccupant}</li>
     * <li>the hex contains a goal</li>
     * </ul>
     * 
     * @return whether or not this hex attracts other enemies.
     */
    public boolean attractsEnemy()
    {
        return streamOccupants().anyMatch(
            o -> o.getClass().isAssignableFrom( EnemyAttractingOccupant.class ) ) || hasGoal();
    }

    /**
     * Simply streams through the hex's occupants in search of any that are
     * assignable from {@link Enemy}.
     * 
     * @return whether or not there are any {@link Enemy Enemies} occupying this
     *         hex.
     */
    public boolean hasEnemy()
    {
        return streamOccupants().anyMatch( o -> o.getClass().isAssignableFrom( Enemy.class ) );
    }

    /**
     * @deprecated Kotlin automatically produces this method in the super type,
     *             but it does not abide by naming conventions that I would
     *             like.
     */
    @Override
    @Deprecated
    public final boolean getPassable()
    {
        return isPassable();
    }

    /**
     * @deprecated There are visibility algorithms in the Hex library spec that
     *             are not utilized here.
     */
    @Deprecated
    public final boolean getOpaque()
    {
        return isOpaque();
    }

    public boolean isOpaque()
    {
        return false;
    }

    @Override
    public final void setPassable( final boolean passable )
    { /* Immutable */ }

    @Override
    public final void setOpaque( final boolean opaque )
    { /* Immutable */ }

    @Override
    public final void setMovementCost( final double movementCost )
    { /* Immutable */ }
}
