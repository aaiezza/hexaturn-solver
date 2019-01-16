/**
 *  COPYRIGHT (C) 2018 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn;

import java.util.Iterator;

import org.hexworks.mixite.core.api.Hexagon;
import org.hexworks.mixite.core.api.HexagonalGrid;
import org.hexworks.mixite.core.api.HexagonalGridBuilder;
import org.hexworks.mixite.core.api.HexagonalGridCalculator;

/**
 * @author Alessandro Aiezza II
 */
@lombok.Getter
@lombok.ToString
@lombok.EqualsAndHashCode
public class HexaturnBoard implements Iterable<Hexagon<HexaturnSatelliteData>>
{
    private final HexagonalGrid<HexaturnSatelliteData> grid;
    private final HexagonalGridCalculator<HexaturnSatelliteData> calculator;
    
    public HexaturnBoard(final HexagonalGridBuilder<HexaturnSatelliteData> hexagonalGridBuilder)
    {
        this.grid = hexagonalGridBuilder.build();
        this.calculator = hexagonalGridBuilder.buildCalculatorFor( grid );
    }

    @Override
    public Iterator<Hexagon<HexaturnSatelliteData>> iterator()
    {
        return getGrid().getHexagons().iterator();
    }
}
