/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.util.Sets;
import org.hexworks.mixite.core.api.Hexagon;
import org.hexworks.mixite.core.vendor.Maybe;

import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

/**
 * @author Alessandro Aiezza II
 *
 */
public class EnemyCalculator implements EnemyTrappedCalculator
{
    @Override
    public boolean allEnemiesTrapped( final HexaturnBoard board )
    {
        if ( board.enemiesAtGoal() > 0 )
            return false;

        return board.streamSatelliteData()
                .chain( this::filterAndMapEnemies )
                .keys()
                .allMatch( enemyHex ->
                    hexesReachable( board, enemyHex )
                        .stream().map( Hexagon::getSatelliteData )
                        .filter( Maybe::isPresent ).map( Maybe::get )
                        .noneMatch( HexaturnSatelliteData::hasGoal )
                );
    }

    private Set<Hexagon<HexaturnSatelliteData>> hexesReachable(
            final HexaturnBoard board,
            final Hexagon<HexaturnSatelliteData> start )
    {
        final Set<Hexagon<HexaturnSatelliteData>> visited = Sets.newHashSet();
        visited.add( start );
        final Map<Integer, Set<Hexagon<HexaturnSatelliteData>>> fringes = new HashMap<>();
        fringes.put(0, StreamEx.of( start ).toSet());

        for ( int i = 1; i < board.size(); i++ )
        {
            final int k = i;
            fringes.put( k, Sets.newHashSet() );
            fringes.get(  k - 1  ).stream()
                .forEach( hex -> {
                    board.getGrid().getNeighborsOf( hex ).stream()
                        .filter( h -> h.getSatelliteData()
                            .fold( () -> false,
                                sd -> sd.isPassable()
                                    && !visited.contains( h )
                                 ) )
                        .forEach( neighbor -> {
                            fringes.get( k ).add( neighbor );
                            visited.add( neighbor );
                        } );
                } );
        }
        return visited;
    }

    private EntryStream<Hexagon<HexaturnSatelliteData>, Enemy> filterAndMapEnemies(
            final EntryStream<Hexagon<HexaturnSatelliteData>, HexaturnSatelliteData> hexStream )
    {
        return hexStream.mapValues( HexaturnSatelliteData::getOccupant )
                .filterValues( Optional::isPresent )
                .mapValues( Optional::get )
                .filterValues( o -> o instanceof Enemy )
                .mapValues( Enemy.class::cast );
    }
}
