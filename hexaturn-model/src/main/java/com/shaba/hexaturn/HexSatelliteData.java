package com.shaba.hexaturn;

import static java.lang.String.format;

import java.util.List;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import io.vavr.control.Either;
import io.vavr.control.Option;
import one.util.streamex.StreamEx;

/**
 * @author Alessandro Aiezza II
 *
 */
@lombok.Data
@lombok.EqualsAndHashCode ( callSuper = true )
@lombok.Builder ( toBuilder = true )
public class HexSatelliteData extends AbstractSatelliteData
{
    @lombok.Builder.Default
    private final boolean                 hasGoal             = false;
    @lombok.Builder.Default
    private final int                     blocksBeforeBlocked = 1;
    @lombok.Builder.Default
    private final boolean                 wasNeverBlockable   = false;
    private final ImmutableList<Occupant> occupants;

    private HexSatelliteData(
        final boolean hasGoal,
        final int blocksBeforeBlocked,
        final boolean wasNeverBlockable,
        final List<Occupant> occupants )
    {
        this.hasGoal = hasGoal;
        this.blocksBeforeBlocked = blocksBeforeBlocked;
        this.wasNeverBlockable = wasNeverBlockable;
        this.occupants = ImmutableList.copyOf( occupants );
    }

    @Override
    public boolean hasGoal()
    {
        return hasGoal;
    }

    @Override
    public boolean wasNeverBlockable()
    {
        return wasNeverBlockable;
    }

    public Either<HexSatelliteData, HexSatelliteData> block()
    {
        if ( canBlock() )
            return Either.left( this );
        else
        {
            final int bbb = blocksBeforeBlocked > 0 ? blocksBeforeBlocked - 1 : 0;
            return Either.right( toBuilder().blocksBeforeBlocked( bbb ).build() );
        }
    }

    public HexSatelliteData removeEnemy( final Enemy enemy )
    {
        return toBuilder().clearExactOccupant( enemy ).build();
    }

    @Override
    public String toString()
    {
        final StringBuilder out = new StringBuilder();

        if ( wasNeverBlockable )
            out.append( "!b" );

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
        private final List<Occupant> occupants = Lists.newArrayList();

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
