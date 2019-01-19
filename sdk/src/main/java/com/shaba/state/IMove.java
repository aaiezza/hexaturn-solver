/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.state;

import java.util.Set;

import com.shaba.hexaturn.HexaturnBoard;

import io.vavr.collection.Seq;
import io.vavr.collection.Vector;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import io.vavr.control.Validation;

/**
 * @author Alessandro Aiezza II
 *
 */
public interface IMove <T>
{
    public Set<Step<T>> getSteps();

    public static abstract class Step <T> implements VerifiedStepApplier<T, Step<T>>
    {
        final Either<IllegalStepException, T> apply( final T t )
        {
            return Try
                    .ofCallable( () -> Option.of( t ).map( this::performStep )
                            .getOrElseThrow( () -> new IllegalStepException( this,
                                    "State cannot be null", new NullPointerException() ) ) )
                    .toEither().mapLeft( this::wrapThrowable );
        }

        public Validation<Seq<IllegalStepException>, Step<HexaturnBoard>> verifyStep(
                final HexaturnBoard board,
                final Step<HexaturnBoard> step )
        {
            if ( step != this )
                return Validation.invalid( Vector.of( new IllegalStepException( step,
                        "Step being verified needs to be identicle" ) ) );
            return verifyStep( board );
        }

        public abstract Validation<Seq<IllegalStepException>, Step<HexaturnBoard>> verifyStep(
                final HexaturnBoard board );

        protected abstract T performStep( final T t );

        private IllegalStepException wrapThrowable( final Throwable t )
        {
            return t instanceof IllegalStepException ? (IllegalStepException) t
                                                     : new IllegalStepException( this, t );
        }
    }
}
