/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.state;

import java.util.Set;

import com.shaba.state.Move.Step;

import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;

/**
 * @author Alessandro Aiezza II
 *
 */
public interface Move <T, S extends Step<T>>
{
    public Set<S> getSteps();

    public static abstract class Step <T> implements VerifiedStepApplier<T, Step<T>>
    {
        final Either<IllegalStepException, T> apply( final T t )
        {
            return Try.ofCallable( () -> Option.of( t )
                        .map( this::performStep )
                        .getOrElseThrow( () -> new IllegalStepException(this,
                                    "State cannot be null", new NullPointerException() ) ) )
                    .toEither()
                    .mapLeft( this::wrapThrowable );
        }

        protected abstract T performStep( final T t );

        private IllegalStepException wrapThrowable( final Throwable t )
        {
            return t instanceof IllegalStepException ? (IllegalStepException) t
                                                     : new IllegalStepException( this, t );
        }
    }
}
