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

import lombok.AccessLevel;
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
                    .map(  List::stream )
                    .orElseGet( this::borderStream ) )
                .forKeyValue( Hexagon::setSatelliteData );
            return new HexaturnBoard( grid, calculator );
        }

        private Stream<HexaturnSatelliteData> borderStream()
        {
            return StreamEx.constant( BORDER_HEX,
                    gridBuilder.getGridWidth() *
                    gridBuilder.getGridHeight() );
        }
    }
}
