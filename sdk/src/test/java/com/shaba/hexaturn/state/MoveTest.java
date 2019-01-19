/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn.state;

import static com.shaba.hexaturn.HexaturnSatelliteData.BORDER_HEX;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.shaba.hexaturn.Enemy;
import com.shaba.hexaturn.HexaturnBoard;
import com.shaba.hexaturn.HexaturnSatelliteData;
import com.shaba.hexaturn.HexaturnSatelliteData.HexaturnSatelliteDataBuilder;
import com.shaba.hexaturn.state.Move.MoveBuilder;
import com.shaba.hexaturn.state.step.BlockHexStep;
import com.shaba.hexaturn.state.step.BlockHexStep.BlockHexStepBuilder;

import one.util.streamex.StreamEx;

/**
 * @author Alessandro Aiezza II
 *
 */
@RunWith ( MockitoJUnitRunner.StrictStubs.class )
public class MoveTest
{
    private HexaturnBoard        board;

    private PlayerMoveCalculator moveCalculator;

    @Before
    public void setup()
    {
        buildBoard();
        moveCalculator = new PlayerMoveCalculator();
    }

    @Test
    public void testEquality()
    {
        final List<BlockHexStep> steps = moveCalculator.getBlockableHexes( board )
                .map( BlockHexStep.builder()::coordinate )
                .map( BlockHexStepBuilder::build )
                .toList();

        final Move m1 = getBlockHexStepMove( steps );
        Collections.reverse( steps );
        final Move m2 = getBlockHexStepMove( steps );

        System.out.println( m1 );
        System.out.println( m2 );

        assertThat( m1 ).isEqualTo( m2 );
    }

    private Move getBlockHexStepMove( final List<BlockHexStep> blockHexStep )
    {
        final MoveBuilder moveBuilder = Move.builder();
        blockHexStep.forEach( moveBuilder::addStep );
        return moveBuilder.build();
    }

    private void buildBoard()
    {
        board = StreamEx.<Supplier<HexaturnSatelliteDataBuilder>> of( BORDER_HEX::toBuilder,
            () -> sdb().occupant( Enemy::new ), this::sdb, this::sdb, () -> sdb().hasGoal( true ) )
                .map( s -> s.get().build() ).chain( hexStream ->
                {
                    final List<HexaturnSatelliteData> data = hexStream.toList();
                    return HexaturnBoard.builder().width( 1 ).height( data.size() ).data( data )
                            .build();
                } );
    }

    /**
     * Shortcut for a {@link HexaturnSatelliteDataBuilder}.
     */
    private HexaturnSatelliteDataBuilder sdb()
    {
        return HexaturnSatelliteData.builder();
    }
}
