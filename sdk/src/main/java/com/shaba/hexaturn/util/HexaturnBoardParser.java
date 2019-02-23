/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn.util;

import static com.shaba.hexaturn.HexaturnSatelliteData.BORDER_HEX;
import static java.util.stream.Collectors.toList;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import com.shaba.hexaturn.HexaturnBoard;
import com.shaba.hexaturn.HexaturnBoard.HexaturnBoardBuilder;
import com.shaba.hexaturn.HexaturnSatelliteData;

import lombok.AccessLevel;

/**
 * A parser to take a string of non-bordering hexaturn board data.
 * 
 * @author Alessandro Aiezza II
 *
 */
@lombok.Data
@lombok.AllArgsConstructor ( access = AccessLevel.PRIVATE )
@lombok.Builder
public class HexaturnBoardParser
{
    private final int                                    width;
    private final int                                    height;
    private final String                                 boardCode;
    @lombok.Builder.Default
    private final Supplier<HexaturnBoardStringConverter> converter = HexaturnBoardStringConverter::new;

    private HexaturnBoardBuilder generateHexaturnBoardBuilder()
    {
        return HexaturnBoard.builder()
                .width( width )
                .height( height );
    }

    private HexaturnBoard applyConfiguration( final HexaturnBoardBuilder boardBuilder )
    {
        final Map<Integer, HexaturnSatelliteData> config = converter.get()
                .convertBoardCode( boardCode );

        boardBuilder.data( IntStream.range( 0, width * height )
            .mapToObj( hexIndex -> config.getOrDefault( hexIndex, BORDER_HEX ) )
            .collect( toList() ));

        return boardBuilder.build();
    }

    public final HexaturnBoard parseBoard()
    {
        return applyConfiguration( generateHexaturnBoardBuilder() );
    }
}
