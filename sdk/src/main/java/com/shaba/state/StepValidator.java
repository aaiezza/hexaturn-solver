/**
 *  COPYRIGHT (C) 2015 Alessandro Aiezza II. All Rights Reserved.
 */
package com.shaba.state;

import com.shaba.state.Move.Step;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;

/**
 * @author Alessandro Aiezza II
 *
 */
public interface StepValidator<T, S extends Step<T>>
{
    public Validation<Seq<IllegalStepException>, S> verifyStep(final T t, final S s);
}
