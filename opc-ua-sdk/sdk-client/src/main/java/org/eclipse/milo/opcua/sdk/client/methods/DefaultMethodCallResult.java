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

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.jspecify.annotations.Nullable;

/** The {@link MethodCallResult} Milo builds from a Call response. */
final class DefaultMethodCallResult<T> implements MethodCallResult<T> {

  private final CallMethodResult raw;
  private final ResponseHeader responseHeader;
  private final @Nullable T outputs;
  private final @Nullable UaException conversionFailure;

  private DefaultMethodCallResult(
      CallMethodResult raw,
      ResponseHeader responseHeader,
      @Nullable T outputs,
      @Nullable UaException conversionFailure) {
    this.raw = raw;
    this.responseHeader = responseHeader;
    this.outputs = outputs;
    this.conversionFailure = conversionFailure;
  }

  static MethodCallResult<Variant[]> of(CallMethodResult raw, ResponseHeader responseHeader) {
    requireNonNull(raw);
    requireNonNull(responseHeader);
    var result = new DefaultMethodCallResult<Variant[]>(raw, responseHeader, null, null);
    return result.hasOutputs()
        ? new DefaultMethodCallResult<>(raw, responseHeader, result.rawOutputs(), null)
        : result;
  }

  private static StatusCode statusOf(CallMethodResult raw) {
    return requireNonNullElse(raw.getStatusCode(), StatusCode.BAD);
  }

  @Override
  public StatusCode statusCode() {
    return statusOf(raw);
  }

  @Override
  public StatusCode[] inputArgumentResults() {
    return requireNonNullElse(raw.getInputArgumentResults(), new StatusCode[0]);
  }

  @Override
  public DiagnosticInfo[] inputArgumentDiagnosticInfos() {
    return requireNonNullElse(raw.getInputArgumentDiagnosticInfos(), new DiagnosticInfo[0]);
  }

  @Override
  public Variant[] rawOutputs() {
    return requireNonNullElse(raw.getOutputArguments(), new Variant[0]);
  }

  @Override
  public @Nullable T outputs() {
    return outputs;
  }

  @Override
  public boolean hasOutputs() {
    return !statusCode().isBad() && conversionFailure == null;
  }

  @Override
  public @Nullable UaException conversionFailure() {
    return conversionFailure;
  }

  @Override
  public @Nullable T requireGood() throws UaException {
    StatusCode statusCode = statusCode();
    if (!statusCode.isGood()) {
      throw new UaMethodException(
          statusCode, inputArgumentResults(), inputArgumentDiagnosticInfos(), responseHeader);
    }
    if (conversionFailure != null) {
      throw conversionFailure;
    }
    return outputs;
  }

  @Override
  public CallMethodResult raw() {
    return raw;
  }

  @Override
  public ResponseHeader responseHeader() {
    return responseHeader;
  }

  @Override
  public <U> MethodCallResult<U> map(OutputConverter<? super T, ? extends U> converter) {
    requireNonNull(converter);
    if (!hasOutputs()) {
      return new DefaultMethodCallResult<>(raw, responseHeader, null, conversionFailure);
    }
    try {
      U converted = converter.convert(outputs);
      return new DefaultMethodCallResult<>(raw, responseHeader, converted, null);
    } catch (UaException e) {
      return new DefaultMethodCallResult<>(raw, responseHeader, null, e);
    } catch (RuntimeException e) {
      return new DefaultMethodCallResult<>(
          raw,
          responseHeader,
          null,
          new UaException(StatusCodes.Bad_DecodingError, "output conversion failed", e));
    }
  }

  @Override
  public String toString() {
    return "MethodCallResult{statusCode="
        + statusCode()
        + ", hasOutputs="
        + hasOutputs()
        + ", conversionFailure="
        + conversionFailure
        + "}";
  }
}
