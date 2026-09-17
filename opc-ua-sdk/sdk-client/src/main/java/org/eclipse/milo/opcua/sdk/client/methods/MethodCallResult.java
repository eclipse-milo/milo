/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.methods;

import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.jspecify.annotations.Nullable;

/**
 * The complete outcome of one Method call: the operation status, per-argument statuses and
 * diagnostics, the raw outputs, and, when the status is not Bad, the outputs converted to {@code
 * T}.
 *
 * <p>A Bad operation status is a result, not an exception. Outputs are converted for Good and
 * Uncertain statuses; a conversion that fails is kept apart from the operation status and reported
 * by {@link #conversionFailure()}. Use {@link #requireGood()} to collapse the result back to the
 * throwing contract of a plain call.
 *
 * <p>Milo produces {@code MethodCallResult<Variant[]>} from a single-operation Call. Typed views
 * are derived with {@link #map(OutputConverter)}.
 *
 * @param <T> the converted output type.
 */
public interface MethodCallResult<T> {

  /**
   * The operation-level status of the call.
   *
   * @return the operation status.
   */
  StatusCode statusCode();

  /**
   * The status of each supplied input argument, when the server returned them.
   *
   * @return one status per supplied input, or an empty array.
   */
  StatusCode[] inputArgumentResults();

  /**
   * The diagnostics of each supplied input argument, when the server returned them.
   *
   * <p>Diagnostics reference strings by index into {@link ResponseHeader#getStringTable()} of
   * {@link #responseHeader()}.
   *
   * @return one entry per supplied input, or an empty array.
   */
  DiagnosticInfo[] inputArgumentDiagnosticInfos();

  /**
   * The output values as received, before any conversion.
   *
   * <p>A conforming server sends no outputs with a Bad status; whatever it did send is returned
   * here unchanged.
   *
   * @return the raw outputs, or an empty array if the server sent none.
   */
  Variant[] rawOutputs();

  /**
   * The converted outputs.
   *
   * <p>Present only when {@link #hasOutputs()} is {@code true}. A {@code null} return with {@link
   * #hasOutputs()} {@code true} is a null output value.
   *
   * @return the converted outputs, or {@code null} if none were decoded.
   */
  @Nullable T outputs();

  /**
   * Whether {@link #outputs()} holds a converted value.
   *
   * <p>False for a Bad status and when conversion failed.
   *
   * @return {@code true} if outputs were converted.
   */
  boolean hasOutputs();

  /**
   * The failure that prevented output conversion, if any.
   *
   * <p>Set only when the status was Good or Uncertain and the outputs did not convert. {@link
   * #rawOutputs()} remains available.
   *
   * @return the conversion failure, or {@code null}.
   */
  @Nullable UaException conversionFailure();

  /**
   * Return the converted outputs of a Good call, or throw.
   *
   * @return the converted outputs.
   * @throws UaMethodException if the operation status is not Good. The exception carries the
   *     operation status, the argument statuses and diagnostics, and the response header.
   * @throws UaException if the status was Good but the outputs did not convert.
   */
  @Nullable T requireGood() throws UaException;

  /**
   * The result exactly as the server returned it.
   *
   * @return the raw result.
   */
  CallMethodResult raw();

  /**
   * The header of the Call response, including the string table its diagnostics index.
   *
   * @return the response header.
   */
  ResponseHeader responseHeader();

  /**
   * Derive a result whose outputs are converted with {@code converter}.
   *
   * <p>The converter runs only when this result has converted outputs. A converter that throws
   * produces a result with no outputs and that failure as {@link #conversionFailure()}; a {@link
   * RuntimeException} is reported as a {@link UaException} with Bad_DecodingError. Every other
   * field is carried over unchanged.
   *
   * @param converter converts this result's outputs.
   * @param <U> the converted output type.
   * @return the derived result.
   */
  <U> MethodCallResult<U> map(OutputConverter<? super T, ? extends U> converter);

  /**
   * Wrap a raw single-operation result.
   *
   * <p>Outputs are present for any status that is not Bad.
   *
   * @param raw the result of the operation.
   * @param responseHeader the header of the response that carried it.
   * @return the wrapped result.
   */
  static MethodCallResult<Variant[]> of(CallMethodResult raw, ResponseHeader responseHeader) {
    return DefaultMethodCallResult.of(raw, responseHeader);
  }

  /**
   * Converts the outputs of a {@link MethodCallResult} to another representation.
   *
   * @param <T> the input type.
   * @param <U> the output type.
   */
  @FunctionalInterface
  interface OutputConverter<T, U> {

    /**
     * Convert {@code outputs}.
     *
     * @param outputs the outputs to convert; may be {@code null} when the source result's output
     *     value is null.
     * @return the converted value.
     * @throws UaException if the outputs cannot be converted.
     */
    @Nullable U convert(@Nullable T outputs) throws UaException;
  }
}
