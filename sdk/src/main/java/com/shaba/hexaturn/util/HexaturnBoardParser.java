/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn.util;

import static com.shaba.hexaturn.HexaturnSatelliteData.BORDER_HEX;

import java.util.Map;
import java.util.function.Supplier;

import org.hexworks.mixite.core.api.Hexagon;
import org.hexworks.mixite.core.api.HexagonOrientation;
import org.hexworks.mixite.core.api.HexagonalGridBuilder;
import org.hexworks.mixite.core.api.HexagonalGridLayout;

import com.shaba.hexaturn.HexaturnBoard;
import com.shaba.hexaturn.HexaturnSatelliteData;

import lombok.AccessLevel;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

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

    private HexagonalGridBuilder<HexaturnSatelliteData> generateHexagonalGridBuilder()
    {
        return new HexagonalGridBuilder<HexaturnSatelliteData>()
                .setOrientation( HexagonOrientation.FLAT_TOP )
                .setGridLayout( HexagonalGridLayout.RECTANGULAR )
                .setRadius( 2.0 )
                .setGridWidth( width )
                .setGridHeight( height );
    }

    private HexaturnBoard initializeBoard( final HexaturnBoard board )
    {
        board.forEach( hex -> hex.setSatelliteData( HexaturnSatelliteData.BORDER_HEX ) );
        return board;
    }

    private HexaturnBoard applyConfiguration( final HexaturnBoard board )
    {
        final Map<Integer, HexaturnSatelliteData> config = converter.get()
                .convertBoardCode(width, height, boardCode);

        EntryStream.of( StreamEx.of( board.iterator() ).toList() ).invert()
                .mapValues( hexIndex -> config.getOrDefault( hexIndex, BORDER_HEX ) )
                .forKeyValue( Hexagon::setSatelliteData );

        return board;
    }

    public final HexaturnBoard parseBoard()
    {
        final HexaturnBoard board = new HexaturnBoard( generateHexagonalGridBuilder() );
        initializeBoard( board );
        applyConfiguration( board );
        return board;
    }
}
