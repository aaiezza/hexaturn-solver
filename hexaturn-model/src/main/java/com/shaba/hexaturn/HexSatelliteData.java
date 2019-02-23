package com.shaba.hexaturn;

import static java.lang.String.format;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import io.vavr.control.Option;
import lombok.AccessLevel;
import one.util.streamex.StreamEx;

/**
 * @author Alessandro Aiezza II
 *
 */
@lombok.Data
@lombok.AllArgsConstructor ( access = AccessLevel.PRIVATE )
@lombok.Builder ( toBuilder = true )
public class HexSatelliteData implements org.hexworks.mixite.core.api.contract.SatelliteData
{
    // @formatter:off
    public static final HexSatelliteData BORDER_HEX   = 
            new HexSatelliteData( false, false, false, 0, ImmutableList.of() ) 
            { @Override public String toString() { return "BORDER_HEX"; }
            @Override
            public HexSatelliteDataBuilder toBuilder()
            {
                return new HexSatelliteDataBuilder() {
                    @Override
                    public HexSatelliteData build()
                    {
                        return BORDER_HEX;
                    }
                };
            }
        };
        // @formatter:on

    @lombok.Builder.Default
    private final boolean                blockable           = true;
    @lombok.Builder.Default
    private final boolean                passable            = true;
    @lombok.Builder.Default
    private final boolean                hasGoal             = false;
    @lombok.Builder.Default
    private final int                    blocksBeforeBlocked = 1;
    @lombok.Builder.Default
    private final List<Occupant>         occupants           = Lists.newArrayList();

    @Deprecated
    public boolean getPassable()
    {
        return isPassable();
    }

    public boolean isPassable()
    {
        return StreamEx.of( occupants ).anyMatch( Occupant::isPassable ) ||
                ( passable && blocksBeforeBlocked > 0 );
    }

    public boolean canBlock()
    {
        return !occupants.isEmpty() ||
                ( passable && blockable && blocksBeforeBlocked > 0 && !hasGoal );
    }

    @Override
    public double getMovementCost()
    {
        return blocksBeforeBlocked > 0 ? 1.0f / blocksBeforeBlocked : Double.MAX_VALUE;
    }

    public List<Occupant> getOccupants() {
        return Collections.unmodifiableList( occupants );
    }

    public boolean hasGoal()
    {
        return hasGoal;
    }

    @Deprecated
    public boolean getOpaque()
    {
        return isOpaque();
    }

    public boolean isOpaque()
    {
        return false;
    }

    @Override
    public void setPassable( final boolean passable )
    { /* Immutable */ }

    @Override
    public void setOpaque( final boolean opaque )
    { /* Immutable */ }

    @Override
    public void setMovementCost( final double movementCost )
    { /* Immutable */ }

    public boolean attractsEnemy()
    {
        return StreamEx.of( occupants ).anyMatch(
            o -> o.getClass().isInstance( EnemyAttractingOccupant.class ) ) || hasGoal;
    }

    public boolean hasEnemy()
    {
        return occupants.stream().anyMatch( o -> o.getClass().isInstance( Enemy.class ) );
    }

    public HexSatelliteData block()
    {
        if ( hasGoal )
            return toBuilder().build();

        final int bbb = blocksBeforeBlocked > 0 ? blocksBeforeBlocked - 1 : 0;
        return toBuilder().blocksBeforeBlocked( bbb ).build();
    }

    public HexSatelliteData removeEnemy(final Enemy enemy)
    {
        return toBuilder().clearExactOccupant( enemy ).build();
    }

    @Override
    public String toString()
    {
        final StringBuilder out = new StringBuilder();

        if ( !blockable )
            out.append( "!b" );

        if ( !passable )
            out.append( "!p" );

        if ( blocksBeforeBlocked > 1 )
            out.append( format( "l%d;", blocksBeforeBlocked ) );
        else if ( blocksBeforeBlocked >= 0 && blocksBeforeBlocked != 1 )
            out.append( "X" );

        if ( hasGoal )
            out.append( "G" );

        occupants.forEach( o -> out.append( format( "%s", o ) ) );

        return out.toString();
    }

    public static class HexSatelliteDataBuilder
    {
        /**
         * Hide this version that takes a {@link List}.
         */
        private HexSatelliteDataBuilder occupants( final List<Occupant> occupants )
        {
            return this;
        }

        public HexSatelliteDataBuilder addOccupant( final Occupant occupant )
        {
            Option.of( occupant ).forEach( occupants::add );
            return this;
        }

        public HexSatelliteDataBuilder clearAllMatchingOccupants( final Occupant occupant )
        {
            occupants.removeIf( occupant::equals );
            return this;
        }

        public HexSatelliteDataBuilder clearFirstMatchingOccupant( final Occupant occupant )
        {
            occupants.remove( occupant );
            return this;
        }

        public HexSatelliteDataBuilder clearExactOccupant( final Occupant occupant )
        {
            occupants.removeIf( o -> o == occupant );
            return this;
        }

        public <O extends Occupant> HexSatelliteDataBuilder clearOccupantsOfType(
                final Class<O> clazz )
        {
            StreamEx.of( occupants ).filter( o -> o.getClass().isAssignableFrom( clazz ) )
                    .forEach( occupants::remove );
            return this;
        }

        public HexSatelliteDataBuilder occupant( final Supplier<Occupant> occupant )
        {
            addOccupant( occupant.get() );
            return this;
        }
    }

}
