/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.state;

import com.shaba.state.Move.Step;

/**
 * @author Alessandro Aiezza II
 *
 */
public class IllegalStepException extends RuntimeException
{
    private static final long serialVersionUID = -3456960244805980608L;

    @lombok.Getter
    private final Step<?>     step;

    public IllegalStepException( final Step<?> step )
    {
        super();
        this.step = step;
    }

    public IllegalStepException(
        final Step<?> step,
        final String message,
        final Throwable cause,
        final boolean enableSuppression,
        final boolean writableStackTrace )
    {
        super( message, cause, enableSuppression, writableStackTrace );
        this.step = step;
    }

    public IllegalStepException( final Step<?> step, final String message )
    {
        super( message );
        this.step = step;
    }

    public IllegalStepException( final Step<?> step, final String message, final Throwable cause )
    {
        super( message, cause );
        this.step = step;
    }

    public IllegalStepException( final Step<?> step, final Throwable cause )
    {
        super( cause );
        this.step = step;
    }
}
