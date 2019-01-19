/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.state;

import io.vavr.collection.Seq;
import io.vavr.collection.Vector;
import io.vavr.control.Either;
import one.util.streamex.StreamEx;

/**
 * @author Alessandro Aiezza II
 *
 */
public interface MoveApplier <T, M extends IMove<T>>
{
    /**
     * By iteratively applying an ordered collection of steps to a model
     * {@code t}, a move is applied. The input object should remain
     * <strong>unmodified</strong>.
     */
    public default Either<Seq<IllegalStepException>, T> applyMove( final T initialState, final M m )
    {
        final Vector<IllegalStepException> issues = Vector.empty();

        final T afterMove = StreamEx.of( m.getSteps() ).foldLeft( initialState, ( state, step ) -> {
            final Either<Seq<IllegalStepException>, T> verification = step.applyStep( state, step );
            verification.peekLeft( issues::appendAll );

            return verification.isRight() ? step.apply( state ).mapLeft( issues::append )
                    .getOrElse( state ) : state;
        } );

        return issues.nonEmpty() ? Either.left( issues ) : Either.right( afterMove );
    }
}
