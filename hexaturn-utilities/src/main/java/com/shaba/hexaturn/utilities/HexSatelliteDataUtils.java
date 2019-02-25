/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.hexaturn.utilities;

import com.shaba.hexaturn.AbstractSatelliteData;

import io.vavr.control.Either;
import io.vavr.control.Either.Left;
import io.vavr.control.Either.Right;

/**
 * @author Alessandro Aiezza II
 *
 */
public interface HexSatelliteDataUtils {

    /**
     * Perform a "block" action on this hex.<br/>
     * If the hex <b>cannot</b> be blocked via the output of
     * {@link AbstractSatelliteData#canBlock()}, return this instance of the
     * {@link AbstractSatelliteData}.<br/>
     * Otherwise, proceed to create a new, immutable instance of
     * {@link AbstractSatelliteData} where this hex is not blocked preferably
     * with a {@link AbstractSatelliteData#getBlocksBeforeBlocked()
     * blocksBeforeBlocked} value of 1 less than this satellite data object.
     * 
     * @return {@link Either} the same instance of the SatelliteData as an
     *         {@link Left Either.Left} if the hex could not be blocked, or an
     *         {@link Right Either.Right} with SatelliteData returning 1 less
     *         {@code blocksBeforeBlocked}.
     */
    public <D extends AbstractSatelliteData> Either<D, D> block();
}
