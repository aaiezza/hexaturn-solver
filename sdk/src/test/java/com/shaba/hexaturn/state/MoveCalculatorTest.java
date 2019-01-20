/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn.state;

import static com.shaba.hexaturn.HexaturnSatelliteData.BORDER_HEX;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.function.Supplier;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.shaba.hexaturn.Enemy;
import com.shaba.hexaturn.Goal;
import com.shaba.hexaturn.HexaturnBoard;
import com.shaba.hexaturn.HexaturnSatelliteData;
import com.shaba.hexaturn.HexaturnSatelliteData.HexaturnSatelliteDataBuilder;

import one.util.streamex.StreamEx;

/**
 * @author Alessandro Aiezza II
 *
 */
@RunWith ( MockitoJUnitRunner.StrictStubs.class )
public class MoveCalculatorTest
{
    private HexaturnBoard  board;

    private MoveCalculator moveCalculator;

    @Before
    public void setup()
    {
        buildBoard();
        moveCalculator = new MoveCalculator();
    }

    @Test
    public void shouldHaveTwoNextMoves()
    {
        assertThat( moveCalculator.calculateNextMoves( board ).toList() ).hasSize( 2 )
                .satisfies( next -> next.forEach( System.out::println ) );
        System.out.println(  );
    }

    private void buildBoard()
    {
        board = StreamEx.<Supplier<HexaturnSatelliteDataBuilder>> of(
            BORDER_HEX::toBuilder,
            () -> sdb().occupant( Enemy::new ),
            this::sdb, this::sdb,
            () -> sdb().occupant( Goal::new ) )
        .map( s -> s.get().build() )
        .chain( hexStream -> {
            final List<HexaturnSatelliteData> data = hexStream.toList();
            return HexaturnBoard.builder()
                .width( 1 )
                .height( data.size() )
                .data( data )
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
