/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn.util;

import java.util.Map;
import java.util.function.Function;

import org.hexworks.mixite.core.api.CubeCoordinate;

import com.shaba.hexaturn.DecoyGoal;
import com.shaba.hexaturn.Enemy;
import com.shaba.hexaturn.Goal;
import com.shaba.hexaturn.HexaturnSatelliteData;
import com.shaba.hexaturn.Occupant;

import lombok.AccessLevel;

/**
 * @author Alessandro Aiezza II
 *
 */
public class HexaturnBoardStringConverter implements BoardStringConverter
{
    @lombok.Getter
    @lombok.ToString
    @lombok.AllArgsConstructor ( access = AccessLevel.PRIVATE )
    public enum OccupantCode
    {
        ENEMY("E", code -> {
            final int movesPerTurn = Integer.parseInt( code.substring( 1 ) );
            final boolean frozen = code.charAt( 0 ) == 'f';
            return Enemy.builder()
                    .movesPerTurn( movesPerTurn )
                    .frozen( frozen )
                    .build();
        }),
        GOAL("G", code -> new Goal()),
        DECOY_GOAL("D", code -> new DecoyGoal());

        private final String code;
        private final Function<String, Occupant> occupant;

    }

    @Override
    public Map<CubeCoordinate, HexaturnSatelliteData> convertBoardCode(
            int width,
            int height,
            String boardCode )
    {
        return null;
    }

    @Override
    public String convertBoardData(
            int width,
            int height,
            Map<CubeCoordinate, HexaturnSatelliteData> boardData )
    {
        return null;
    }
}
