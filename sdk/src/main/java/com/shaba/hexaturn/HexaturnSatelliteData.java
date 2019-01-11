/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn;

import java.util.Optional;

/**
 * @author Alessandro Aiezza II
 *
 */
@lombok.Data
@lombok.Builder ( toBuilder = true )
public class HexaturnSatelliteData implements org.hexworks.mixite.core.api.contract.SatelliteData
{
    public static final HexaturnSatelliteData BORDER_HEX = HexaturnSatelliteData.builder()
            .blockable( false )
            .passable( false )
            .movementCost( Double.MAX_VALUE )
            .build();
            
    @lombok.Builder.Default
    private final boolean            blockable    = true;
    @lombok.Builder.Default
    private final boolean            passable     = true;
    @lombok.Builder.Default
    private final double             movementCost = 1.0;
    @lombok.Builder.Default
    private final Optional<Occupant> occupant     = Optional.empty();

    public boolean getPassable()
    {
        return occupant.map(Occupant::isPassable).orElse(passable);
    }

    public boolean getOpaque()
    {
        return false;
    }

    public void setPassable( final boolean passable )
    {
        return;
    }

    public void setOpaque( final boolean opaque )
    {
        return;
    }

    public void setMovementCost( final double movementCost )
    {
        return;
    }
}
