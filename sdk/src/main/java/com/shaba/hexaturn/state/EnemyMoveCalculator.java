/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn.state;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.hexworks.mixite.core.api.Hexagon;
import org.hexworks.mixite.core.vendor.Maybe;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.shaba.hexaturn.Enemy;
import com.shaba.hexaturn.EnemyTrappedCalculator;
import com.shaba.hexaturn.HexaturnBoard;
import com.shaba.hexaturn.HexaturnSatelliteData;
import com.shaba.state.NextMoveCalculator;

import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

/**
 * @author Alessandro Aiezza II
 *
 */
public class EnemyMoveCalculator implements EnemyTrappedCalculator, NextMoveCalculator<HexaturnBoard, Move>
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
                    StreamEx.ofValues( hexesReachable( board, enemyHex ) )
                        .flatMap( Set::stream )
                        .map( Hexagon::getSatelliteData )
                        .filter( Maybe::isPresent ).map( Maybe::get )
                        .noneMatch( HexaturnSatelliteData::hasGoal )
                );
    }

    @SuppressWarnings ( "unchecked" )
    public Map<Integer, Set<Hexagon<HexaturnSatelliteData>>> hexesReachable(
            final HexaturnBoard board,
            final Hexagon<HexaturnSatelliteData> start )
    {
        final Set<Hexagon<HexaturnSatelliteData>> visited = Sets.newHashSet();
        visited.add( start );
        final Map<Integer, Set<Hexagon<HexaturnSatelliteData>>> fringes = Maps.newHashMap();
        fringes.put( 0, Sets.newHashSet( start ) );

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
        return fringes;
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
    
    public Optional<Move> calculateNextMove( final HexaturnBoard board )
    {
        // TODO
        // Find the enemies, for each
        //  find out if it can move (frozen? trapped? has movesPerTurn > 1)
        //  find out where it wants to go (closest enemyAttractingHex)
        //  
        return board.isTerminal() ? Optional.empty() : null;
    }

    @Override
    public StreamEx<Move> calculateNextMoves( final HexaturnBoard board )
    {
        return StreamEx.of( calculateNextMove( board ) );
    }
}
