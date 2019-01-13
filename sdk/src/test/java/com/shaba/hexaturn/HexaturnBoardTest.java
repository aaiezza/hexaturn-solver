/**
 *  COPYRIGHT (C) 2018 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn;

import java.util.concurrent.atomic.AtomicInteger;

import org.hexworks.mixite.core.api.HexagonOrientation;
import org.hexworks.mixite.core.api.HexagonalGridBuilder;
import org.hexworks.mixite.core.api.HexagonalGridLayout;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import one.util.streamex.StreamEx;

/**
 * @author Alessandro Aiezza II
 */
@RunWith ( MockitoJUnitRunner.StrictStubs.class )
public class HexaturnBoardTest
{
    private HexaturnBoard board;
    private int width  = 7;
    private int height = 7;

    @Before
    public void setup()
    {
        board = new HexaturnBoard( new HexagonalGridBuilder<HexaturnSatelliteData>()
            .setOrientation( HexagonOrientation.FLAT_TOP )
            .setGridLayout( HexagonalGridLayout.RECTANGULAR )
            .setRadius( 2.0 )
            .setGridWidth( width )
            .setGridHeight( height ) );
    }

    @Test
    public void testBoard()
    {
        final AtomicInteger count = new AtomicInteger();
        final long total = StreamEx.of( board.getGrid().getHexagons().iterator() )
            .peek( hex -> {
                hex.setSatelliteData( HexaturnSatelliteData.BORDER_HEX );

                System.out.printf( "%2d | [%2d, %2d, %2d] %s%n",
                    count.getAndIncrement(),
                    hex.getGridX(),
                    hex.getGridY(),
                    hex.getGridZ(),
                    hex.getSatelliteData() );
            } )
            .count();
        
        System.out.println( total );
    }
}
