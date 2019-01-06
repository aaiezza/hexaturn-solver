/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn.util;

import java.util.Map;

import org.hexworks.mixite.core.api.CubeCoordinate;

import com.shaba.hexaturn.HexaturnSatelliteData;

/**
 * @author Alessandro Aiezza II
 *
 */
public interface BoardStringConverter
{
    public Map<CubeCoordinate, HexaturnSatelliteData> convertBoardCode(
            final int width,
            final int height,
            final String boardCode );

    public String convertBoardData(
            final int width,
            final int height,
            final Map<CubeCoordinate, HexaturnSatelliteData> boardData );
}
