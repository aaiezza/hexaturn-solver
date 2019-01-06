/**
 *  COPYRIGHT (C) 2018 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn;

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

    @Before
    public void setup()
    {
        board = new HexaturnBoard( new HexagonalGridBuilder<HexaturnSatelliteData>()
            .setOrientation( HexagonOrientation.FLAT_TOP )
            .setGridLayout( HexagonalGridLayout.RECTANGULAR )
            .setRadius( 2.0 )
            .setGridWidth( 7 )
            .setGridHeight( 5 ) );
    }

    @Test
    public void testBoard()
    {
        final long total = StreamEx.of( board.getGrid().getHexagons().iterator() )
            .peek( hex -> {
                hex.setSatelliteData( HexaturnSatelliteData.BORDER_HEX );
                System.out.printf( "[%2d, %2d] %s %s%n",
                    hex.getGridX(), hex.getGridY(),
                    hex.getId(),
                    hex.getSatelliteData() );
            } )
            .count();
        
        System.out.println( total );
    }
}
