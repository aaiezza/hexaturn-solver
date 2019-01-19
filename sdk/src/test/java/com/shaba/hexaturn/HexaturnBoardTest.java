/**
 *  COPYRIGHT (C) 2018 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicInteger;

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
        board = HexaturnBoard.builder()
                    .width( width )
                    .height( height ).build();
    }

    @Test
    public void testBoard()
    {
        final AtomicInteger count = new AtomicInteger();
        final long total = StreamEx.of( board.iterator() )
            .peek( hex -> {
                System.out.printf( "%2d | [%2d, %2d, %2d] %s%n",
                    count.getAndIncrement(),
                    hex.getGridX(),
                    hex.getGridY(),
                    hex.getGridZ(),
                    hex.getSatelliteData() );
            } )
            .count();

        System.out.println( total );
        assertThat( total ).isEqualTo( width * height );
    }
}
