/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn;

import static java.lang.String.format;

import java.util.Optional;
import java.util.function.Supplier;

import lombok.AccessLevel;

/**
 * @author Alessandro Aiezza II
 *
 */
@lombok.Data
@lombok.AllArgsConstructor ( access = AccessLevel.PRIVATE )
@lombok.Builder ( toBuilder = true )
public class HexaturnSatelliteData implements org.hexworks.mixite.core.api.contract.SatelliteData
{
    public static final HexaturnSatelliteData BORDER_HEX   = 
            new HexaturnSatelliteData( false, false, false, 0, Optional.empty() ) 
            { @Override public String toString() { return "BORDER_HEX"; }
            @Override
            public HexaturnSatelliteDataBuilder toBuilder()
            {
                return new HexaturnSatelliteDataBuilder() {
                    @Override
                    public HexaturnSatelliteData build()
                    {
                        return BORDER_HEX;
                    }
                };
            }
        };

    @lombok.Builder.Default
    private final boolean                     blockable           = true;
    @lombok.Builder.Default
    private final boolean                     passable            = true;
    @lombok.Builder.Default
    private final boolean                     hasGoal             = false;
    @lombok.Builder.Default
    private final int                         blocksBeforeBlocked = 1;
    private final Optional<Occupant>          occupant;

    @Deprecated
    public boolean getPassable()
    {
        return isPassable();
    }

    public boolean isPassable()
    {
        return occupant.map( Occupant::isPassable ).orElse( passable && blocksBeforeBlocked > 0 );
    }

    public boolean canBlock()
    {
        return occupant.map( o -> false ).orElse( passable &&
            blockable && blocksBeforeBlocked > 0 && !hasGoal );
    }

    @Override
    public double getMovementCost()
    {
        return blocksBeforeBlocked > 0 ? 1 / blocksBeforeBlocked : Double.MAX_VALUE;
    }

    private boolean isHasGoal()
    {
        return hasGoal();
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

    public void setPassable( final boolean passable )
    {
        return;
    }

    public void setOpaque( final boolean opaque )
    {
        return;
    }

    public void setMovementCost( final double movementCost )
    {
        return;
    }

    public boolean attractsEnemy()
    {
        return occupant.map( o -> o.getClass().isInstance( EnemyAttractingOccupant.class ) )
                .orElse( hasGoal );
    }

    public boolean hasEnemy()
    {
        return occupant.map( o -> o.getClass().isInstance( Enemy.class ) ).orElse( false );
    }

    public HexaturnSatelliteData block()
    {
        if ( hasGoal )
            return toBuilder().build();

        final int bbb = blocksBeforeBlocked > 0 ? blocksBeforeBlocked - 1 : 0;
        return toBuilder().blocksBeforeBlocked( bbb ).build();
    }

    public HexaturnSatelliteData removeEnemy()
    {
        return occupant.filter( o -> o.getClass().isInstance( Enemy.class ) )
                .map( o -> toBuilder().clearOccupant().build() )
                .orElseGet( toBuilder()::build );
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

        occupant.ifPresent( o -> out.append( format( "%s", o ) ) );

        return out.toString();
    }

    public static class HexaturnSatelliteDataBuilder
    {
        private Optional<Occupant> occupant = Optional.empty();

        /**
         * Hide this version that takes an {@link Optional}.
         */
        private HexaturnSatelliteDataBuilder occupant( final Optional<Occupant> occupant )
        {
            return this;
        }

        public HexaturnSatelliteDataBuilder occupant( final Occupant occupant )
        {
            this.occupant = Optional.ofNullable( occupant );
            return this;
        }

        public HexaturnSatelliteDataBuilder clearOccupant()
        {
            this.occupant = Optional.empty();
            return this;
        }

        public HexaturnSatelliteDataBuilder occupant( final Supplier<Occupant> occupant )
        {
            this.occupant = Optional.ofNullable( occupant ).map( Supplier::get );
            return this;
        }
    }
}
