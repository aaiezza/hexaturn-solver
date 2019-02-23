/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn.util;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.joining;

import java.util.Map;
import java.util.Optional;
import java.util.PrimitiveIterator.OfInt;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

import com.shaba.hexaturn.DecoyGoal;
import com.shaba.hexaturn.Enemy;
import com.shaba.hexaturn.HexaturnSatelliteData;
import com.shaba.hexaturn.HexaturnSatelliteData.HexaturnSatelliteDataBuilder;
import com.shaba.hexaturn.Occupant;

import lombok.AccessLevel;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

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
        ENEMY("e", enemyInfo -> {
            final boolean frozen = enemyInfo.charAt( 0 ) == 'f';
            final int movesPerTurn = Integer.parseInt( enemyInfo.substring( frozen ? 1 : 0 ) );
            return Enemy.builder()
                    .movesPerTurn( movesPerTurn )
                    .frozen( frozen )
                    .build();
        }),
        DECOY_GOAL("D", noCode -> new DecoyGoal());

        private final String code;
        private final Function<String, Occupant> occupantParser;

        public Occupant makeOccupant( final String code )
        {
            return occupantParser.apply(  code );
        }

        public static Optional<OccupantCode> parseCode( final char code )
        {
            return StreamEx.of( values() )
                    .findFirst( oc -> oc.getCode().equals( String.valueOf( code ) ) );
        }
    }

    @lombok.Data
    public static class StringHex
    {
        private final int index;
        private final HexaturnSatelliteData data;

        public static Stream<StringHex> parse( final String hexInfo )
        {
            final String [] hex = hexInfo.split( ":" );
            final int index = Integer.parseInt( hex[0] );

            final HexaturnSatelliteDataBuilder dataBuilder = HexaturnSatelliteData.builder();

            final OfInt contents = ( hex.length > 1 ? hex[1] : "" ).chars().iterator();
            while ( contents.hasNext() )
            {
                char c = (char) contents.nextInt();
                final boolean hasAttribute = c != '!';

                if ( !hasAttribute )
                    c = (char) contents.next().intValue();

                switch ( c )
                {
                case 'b':
                    dataBuilder.blockable( hasAttribute );
                    break;
                case 'l':
                    dataBuilder.blocksBeforeBlocked( parseIntUntilSemicolon(contents) );
                    break;
                case 'X':
                    dataBuilder.blocksBeforeBlocked( 0 );
                    break;
                /* Occupant codes */
                default:
                    OccupantCode.parseCode( c )
                        .map( oc -> oc.makeOccupant( parseUntilSemicolon(contents) ) )
                        .ifPresent( dataBuilder::occupant );
                }
            }

            final int repeat = Optional.of( hex )
                    .filter( h -> h.length > 2 )
                    .map( h -> h[2] )
                    .map( Integer::parseInt )
                    .map( toIndex -> toIndex - index + 1 )
                    .orElse( 1 );
            final AtomicInteger indexCounter = new AtomicInteger( index );
            return StreamEx.generate( () -> new StringHex( indexCounter.getAndIncrement(), dataBuilder.build() ) ).limit( repeat );
        }

        private static String parseUntilSemicolon( final OfInt iter )
        {
            return StreamEx.of( iter )
                    .map( c -> Character.valueOf( (char) (int) c ) )
                    .takeWhile( c -> c != ';' )
                    .joining();
        }

        private static int parseIntUntilSemicolon( final OfInt iter )
        {
            return StreamEx.of( iter )
                    .map( c -> Character.valueOf( (char) (int) c ) )
                    .takeWhile( c -> c != ';' && Character.isDigit( c ) )
                    .map( String::valueOf )
                    .collect( collectingAndThen( joining(), Integer::parseInt ) );
        }
    }

    /**
     * Convert a board code to a map of coordinates and satellite data.
     * 
     * Example:
     * 
     * <pre>
     *    (0)    _ _    (2)
     *     B   /     \   B
     *    _ _ /   1   \ _ _
     *  /     \   e   /     \
     * /   3   \ _ _ /   5   \
     * \       /     \       /
     *  \ _ _ /   4   \ _ _ /
     *  /     \       /     \
     * /   6   \ _ _ /   8   \
     * \       /     \       /
     *  \ _ _ /   7   \ _ _ /
     *  /     \       /     \
     * /   9   \ _ _ /  11   \
     * \       /     \       /
     *  \ _ _ /  10   \ _ _ /
     *  /     \  G!b  /     \
     * /  12   \ _ _ /  14   \
     * \  !b   /     \  !b   /
     *  \ _ _ / (13)  \ _ _ /
     *            B
     * </pre>
     *
     * Would translate to a board code:<br/>
     * {@code 1:e1;,3::9,10:G;!b,11:,12:!b,14:!b} with {@code width = 3} and
     * {@code height = 5}.<br/>
     * <br/>
     * Using this code will result in the board described above that can then be
     * fed to the parser to create the hexagonal board.
     */
    @Override
    public Map<Integer, HexaturnSatelliteData> convertBoardCode(
            final String boardCode )
    {
        return convertBoardCodeStream( boardCode ).toImmutableMap();
    }

    public EntryStream<Integer, HexaturnSatelliteData> convertBoardCodeStream(
            final String boardCode )
    {
        return StreamEx.of( boardCode.split( "," ) )
                .flatMap( StringHex::parse )
                .mapToEntry( StringHex::getIndex, StringHex::getData );
    }

    @Override
    public String convertBoardData(
            final int width,
            final int height,
            final Map<Integer, HexaturnSatelliteData> boardData )
    {
        // TODO
        return null;
    }
}
