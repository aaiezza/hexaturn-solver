/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn.string.converter;

import java.util.Map;

import com.shaba.hexaturn.HexSatelliteData;

/**
 * @author Alessandro Aiezza II
 *
 */
public interface BoardStringConverter
{
    public Map<Integer, HexSatelliteData> convertBoardCode(final String boardCode);

    public String convertBoardData(
            final int width,
            final int height,
            final Map<Integer, HexSatelliteData> boardData );
}
