/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.state;

import com.shaba.state.Move.Step;

import io.vavr.collection.Seq;
import io.vavr.collection.Vector;
import io.vavr.control.Either;
import io.vavr.control.Validation;

/**
 * @author Alessandro Aiezza II
 *
 */
public interface StepApplier <T, S extends Step<T>, V extends StepValidator<T, S>> extends StepValidator<T, S>
{
    public default Either<Seq<IllegalStepException>, T> applyStep( final T state, final S step )
    {
        final Vector<IllegalStepException> issues = Vector.empty();

        final Validation<Seq<IllegalStepException>, S> verification = verifyStep( state, step );
        verification.toEither().peekLeft( issues::appendAll );

        final T afterMove = verification.isValid() ? step.apply( state ).mapLeft( issues::append )
                .getOrElse( state ) : state;

        return issues.nonEmpty() ? Either.left( issues ) : Either.right( afterMove );
    }
}
