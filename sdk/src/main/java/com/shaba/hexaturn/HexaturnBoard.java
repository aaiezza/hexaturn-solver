/**
 *  COPYRIGHT (C) 2018 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn;

import static com.shaba.hexaturn.HexaturnSatelliteData.BORDER_HEX;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.hexworks.mixite.core.api.Hexagon;
import org.hexworks.mixite.core.api.HexagonOrientation;
import org.hexworks.mixite.core.api.HexagonalGrid;
import org.hexworks.mixite.core.api.HexagonalGridBuilder;
import org.hexworks.mixite.core.api.HexagonalGridCalculator;
import org.hexworks.mixite.core.api.HexagonalGridLayout;
import org.hexworks.mixite.core.vendor.Maybe;

import lombok.AccessLevel;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

/**
 * @author Alessandro Aiezza II
 */
@lombok.Getter
@lombok.EqualsAndHashCode
@lombok.AllArgsConstructor ( access = AccessLevel.PRIVATE )
@lombok.Builder
public class HexaturnBoard implements Iterable<Hexagon<HexaturnSatelliteData>>
{
    private final HexagonalGrid<HexaturnSatelliteData>           grid;
    private final HexagonalGridCalculator<HexaturnSatelliteData> calculator;
    private final EnemyTrappedCalculator                         enemyTrappedCalculator;

    @Override
    public Iterator<Hexagon<HexaturnSatelliteData>> iterator()
    {
        return getGrid().getHexagons().iterator();
    }

    public HexaturnBoardBuilder toBuilder()
    {
        return HexaturnBoard.builder()
                .width( grid.getGridData().getGridWidth() )
                .height( grid.getGridData().getGridHeight() )
                .data( StreamEx.of( iterator() )
                    .map( Hexagon::getSatelliteData )
                    .map( data -> data.orElse( BORDER_HEX ) )
                    .toImmutableList() );
    }

    public boolean allEnemiesTrapped()
    {
        return enemyTrappedCalculator.allEnemiesTrapped( this );
    }

    public int enemiesAtGoal()
    {
        return (int) StreamEx.of( iterator() )
                .map( Hexagon::getSatelliteData )
                .filter( Maybe::isPresent ).map( Maybe::get )
                .filter( HexaturnSatelliteData::hasGoal )
                .map( HexaturnSatelliteData::getOccupant )
                .filter( Optional::isPresent ).map( Optional::get )
                .filter( o -> o instanceof Enemy )
                .count();
    }

    public boolean isTerminal()
    {
        return enemiesAtGoal() > 0 || allEnemiesTrapped();
    }

    public EntryStream<Hexagon<HexaturnSatelliteData>, HexaturnSatelliteData> streamSatelliteData()
    {
        return StreamEx.of( iterator() )
                .mapToEntry( Hexagon::getSatelliteData )
                .mapValues( sd -> sd.orElse( BORDER_HEX ) );
    }

    public int size()
    {
        return grid.getGridData().getGridWidth() * grid.getGridData().getGridHeight();
    }

    @Override
    public String toString()
    {
        final AtomicInteger count = new AtomicInteger();
        return StreamEx.of( iterator() )
            .map( hex -> {
                return String.format( "%2d | [%2d, %2d, %2d] %s%n",
                    count.getAndIncrement(),
                    hex.getGridX(),
                    hex.getGridY(),
                    hex.getGridZ(),
                    hex.getSatelliteData() );
            } )
            .joining();
    }

    public static final class HexaturnBoardBuilder
    {
        private List<HexaturnSatelliteData> data;
        private final HexagonalGridBuilder<HexaturnSatelliteData> gridBuilder =
                new HexagonalGridBuilder<HexaturnSatelliteData>()
                        .setOrientation( HexagonOrientation.FLAT_TOP )
                        .setGridLayout( HexagonalGridLayout.RECTANGULAR )
                        .setRadius( 2.0 );
        private EnemyTrappedCalculator enemyTrappedCalculator = new EnemyCalculator();

        public HexaturnBoardBuilder width( final int width )
        {
            gridBuilder.setGridWidth( width );
            return this;
        }

        public HexaturnBoardBuilder height( final int height )
        {
            gridBuilder.setGridHeight( height );
            return this;
        }

        public HexaturnBoardBuilder data( final List<HexaturnSatelliteData> data )
        {
            this.data = data;
            return this;
        }

        @SuppressWarnings ( "unused" )
        private HexaturnBoardBuilder grid( final HexagonalGrid<HexaturnSatelliteData> grid )
        {
            return this;
        }

        @SuppressWarnings ( "unused" )
        private HexaturnBoardBuilder calculator(
                final HexagonalGridCalculator<HexaturnSatelliteData> calculator )
        {
            return this;
        }

        public HexaturnBoard build()
        {
            this.grid = gridBuilder.build();
            this.calculator = gridBuilder.buildCalculatorFor( grid );
            StreamEx.of( grid.getHexagons().iterator() )
                .zipWith( Optional.ofNullable( data )
                    .map( List::stream )
                    .orElseGet( this::borderStream ) )
                .forKeyValue( Hexagon::setSatelliteData );
            return new HexaturnBoard( grid, calculator, enemyTrappedCalculator );
        }

        private Stream<HexaturnSatelliteData> borderStream()
        {
            return StreamEx.constant( BORDER_HEX,
                    gridBuilder.getGridWidth() *
                    gridBuilder.getGridHeight() );
        }
    }
}
