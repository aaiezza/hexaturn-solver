/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn.string.converter;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.shaba.hexaturn.HexaturnBoard;

import one.util.streamex.StreamEx;

/**
 * @author Alessandro Aiezza II
 *
 */
@RunWith ( MockitoJUnitRunner.StrictStubs.class )
public class HexaturnBoardParserTest
{
    private HexaturnBoardParser parser;

    private int width = 3;
    private int height = 5;
    private String boardCode = "1:ef2;e3;,3:l2;:5,6::9,10:G;!b,11,12:!b,14:!b";

    @Before
    public void setup()
    {
        parser = HexaturnBoardParser.builder()
                .converter( HexaturnBoardStringConverter::new )
                .width( width )
                .height( height )
                .boardCode( boardCode )
                .build();
    }

    @Test
    public void testBoard()
    {
        final HexaturnBoard board = parser.parseBoard();

        final AtomicInteger count = new AtomicInteger();
        final long total = StreamEx.of( board.getGrid().getHexagons().iterator() )
                .peek( hex -> {
                    System.out.printf( "%2d | [%2d, %2d, %2d] %s%n",
                        count.getAndIncrement(),
                        hex.getGridX(),
                        hex.getGridY(),
                        hex.getGridZ(),
                        hex.getSatelliteData() );
                } ).count();

        System.out.println( total );
    }
}
