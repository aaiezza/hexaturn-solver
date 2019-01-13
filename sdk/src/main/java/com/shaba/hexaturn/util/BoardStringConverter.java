/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn.util;

import java.util.Map;

import com.shaba.hexaturn.HexaturnSatelliteData;

/**
 * @author Alessandro Aiezza II
 *
 */
public interface BoardStringConverter
{
    public Map<Integer, HexaturnSatelliteData> convertBoardCode(
            final int width,
            final int height,
            final String boardCode );

    public String convertBoardData(
            final int width,
            final int height,
            final Map<Integer, HexaturnSatelliteData> boardData );
}
