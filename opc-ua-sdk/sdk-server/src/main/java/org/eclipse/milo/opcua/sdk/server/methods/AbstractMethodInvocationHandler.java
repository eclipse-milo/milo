/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.methods;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;

import java.util.Arrays;
import java.util.Optional;
import org.eclipse.milo.opcua.sdk.server.AccessContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.CallContext;
import org.eclipse.milo.opcua.sdk.server.DiagnosticsContext;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A partial implementation of {@link MethodInvocationHandler} that validates input argument counts,
 * shapes and data types before invoking application code.
 *
 * <p>Validation is performed by {@link MethodArgumentValidator}. Callers are responsible for access
 * checks. Normal server Method dispatch applies the configured access controller before invoking
 * this handler.
 */
public abstract class AbstractMethodInvocationHandler implements MethodInvocationHandler {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(AbstractMethodInvocationHandler.class);

  private final UaMethodNode node;
  private final MethodArgumentValidator argumentValidator;

  /**
   * @param node the {@link UaMethodNode} this handler will be installed on.
   */
  public AbstractMethodInvocationHandler(UaMethodNode node) {
    this.node = node;
    argumentValidator = new MethodArgumentValidator(node.getNodeContext().getServer());
  }

  public UaMethodNode getNode() {
    return node;
  }

  @Override
  public final CallMethodResult invoke(AccessContext accessContext, CallMethodRequest request) {
    Variant[] suppliedInputs = requireNonNullElse(request.getInputArguments(), new Variant[0]);
    int suppliedInputCount = suppliedInputs.length;

    CallMethodResult result;

    try {
      // Read the metadata once so the count check and validation see the same Arguments.
      Argument[] inputArguments = getInputArguments();
      int requiredCount = getRequiredInputArgumentCount(inputArguments);

      OpcUaServer server = node.getNodeContext().getServer();

      Variant[] inputArgumentValues =
          argumentValidator.validate(inputArguments, requiredCount, suppliedInputs);

      validateInputArgumentValues(inputArgumentValues);

      var invocationContext =
          new DefaultInvocationContext(server, node, accessContext, request, suppliedInputCount);

      result = invokeResult(invocationContext, inputArgumentValues);
    } catch (InvalidArgumentException e) {
      result = invalidArgumentResult(e, suppliedInputCount);
    } catch (UaException e) {
      return failure(e.getStatusCode());
    }

    String problem = problemWith(result, suppliedInputCount);

    if (problem == null) {
      return result;
    } else {
      LOGGER.warn("Invalid result for methodId={}: {}", node.getNodeId(), problem);
      return failure(new StatusCode(StatusCodes.Bad_InternalError));
    }
  }

  /**
   * Check {@code result} against the CallMethodResult rules in OPC 10000-4, 5.12.2.2: a Bad status
   * carries no outputs, and input argument results are populated only for Bad_InvalidArgument, one
   * per supplied input. Argument diagnostics, when present, are also one per supplied input.
   *
   * @return a description of the first rule {@code result} breaks, or {@code null} if it is valid.
   */
  private static @Nullable String problemWith(@Nullable CallMethodResult result, int inputCount) {
    if (result == null || result.getStatusCode() == null) {
      return "null result or status";
    }
    StatusCode status = result.getStatusCode();
    int results = lengthOf(result.getInputArgumentResults());
    int diagnostics = lengthOf(result.getInputArgumentDiagnosticInfos());
    int outputs = lengthOf(result.getOutputArguments());

    if (status.isBad() && outputs != 0) {
      return "Bad status with output arguments";
    }
    if (results != 0 && status.getValue() != StatusCodes.Bad_InvalidArgument) {
      return "input argument results with a status other than Bad_InvalidArgument";
    }
    if (results != 0 && results != inputCount) {
      return results + " input argument results for " + inputCount + " inputs";
    }
    if (diagnostics != 0 && diagnostics != inputCount) {
      return diagnostics + " input argument diagnostics for " + inputCount + " inputs";
    }
    return null;
  }

  private static int lengthOf(Object @Nullable [] array) {
    return array == null ? 0 : array.length;
  }

  /**
   * Build the result for an {@link InvalidArgumentException}. A sparse exception, built by its
   * builder, mentions only the arguments it rejects, so its arrays are resized to one entry per
   * supplied input: unmentioned inputs are Good with no diagnostics, and an index beyond the
   * supplied inputs is dropped rather than failing the whole call as an invalid result.
   */
  private static CallMethodResult invalidArgumentResult(
      InvalidArgumentException e, int suppliedInputCount) {

    StatusCode[] results = e.getInputArgumentResults();
    DiagnosticInfo[] diagnostics = e.getInputArgumentDiagnosticInfos();

    if (e.isSparse()) {
      results = resize(results, suppliedInputCount, StatusCode.GOOD);
      if (diagnostics.length > 0) {
        diagnostics = resize(diagnostics, suppliedInputCount, DiagnosticInfo.NULL_VALUE);
      }
    }

    return new CallMethodResult(e.getStatusCode(), results, diagnostics, new Variant[0]);
  }

  private static <T> T[] resize(T[] array, int length, T filler) {
    if (array.length == length) {
      return array;
    }
    T[] resized = Arrays.copyOf(array, length);
    Arrays.fill(resized, Math.min(array.length, length), length, filler);
    return resized;
  }

  private static CallMethodResult failure(StatusCode statusCode) {
    return new CallMethodResult(
        statusCode, new StatusCode[0], new DiagnosticInfo[0], new Variant[0]);
  }

  /**
   * Get the input {@link Argument}s expected by the Method this handler is installed on.
   *
   * @return the input {@link Argument}s expected by the Method this handler is installed on.
   */
  public abstract Argument[] getInputArguments();

  /**
   * Get the output {@link Argument}s expected by the Method this handler is installed on.
   *
   * @return the output {@link Argument}s expected by the Method this handler is installed on.
   */
  public abstract Argument[] getOutputArguments();

  /**
   * Get the number of leading inputs a caller must supply; any inputs after them are optional.
   *
   * <p>The default requires every declared input. Override this to accept calls that omit trailing
   * inputs. Omitted inputs are absent from the array passed to {@link #invokeResult}, whereas a
   * supplied null value keeps its position; {@link InvocationContext#suppliedInputCount()} tells
   * the two apart. A count outside the valid range fails the call with Bad_InternalError before any
   * application code runs.
   *
   * @param inputArguments the input {@link Argument}s this call is validated against; do not
   *     modify.
   * @return a count between zero and {@code inputArguments.length}, inclusive.
   */
  protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
    return inputArguments.length;
  }

  /**
   * Invoke this Method with its validated inputs and return the complete result.
   *
   * <p>The default delegates to {@link #invoke(InvocationContext, Variant[])} and reports the
   * status of the context, which is Good unless the callback changed it with {@link
   * InvocationContext#setStatusCode(StatusCode)}. Override this to assemble a different result. A
   * Bad status carries no outputs. Input argument results may be populated only with
   * Bad_InvalidArgument, one per supplied input; argument diagnostics, when present, are also one
   * per supplied input. A result that breaks these rules, or a null result, is logged and reported
   * as Bad_InternalError.
   *
   * @param context the {@link InvocationContext}.
   * @param suppliedValues the supplied inputs, validated and decoded as described by {@link
   *     #invoke(InvocationContext, Variant[])}. Omitted optional inputs are absent, not padded.
   * @return the complete result.
   * @throws UaException if invocation fails; its status is reported without outputs.
   */
  protected CallMethodResult invokeResult(InvocationContext context, Variant[] suppliedValues)
      throws UaException {

    Variant[] outputs = invoke(context, suppliedValues);

    return new CallMethodResult(
        context.getStatusCode(), new StatusCode[0], new DiagnosticInfo[0], outputs);
  }

  /**
   * Invoke this method and return the values for its output arguments, if any.
   *
   * <p>Input arguments have already passed shape, data type and application value validation.
   * Callers remain responsible for access checks.
   *
   * <p>The result is reported with the status of {@code invocationContext}, Good by default. Call
   * {@link InvocationContext#setStatusCode(StatusCode)} to return the outputs with a Good subcode
   * or an Uncertain status instead. Failures are reported by throwing.
   *
   * @param invocationContext the {@link InvocationContext}.
   * @param inputValues the user-supplied values for the input arguments. Each value has been
   *     verified to be of the type specified by its {@link Argument}. Values for arguments with a
   *     structured DataType carry the decoded {@link UaStructuredType} value (or, for array
   *     arguments, an array of the DataType's registered class, e.g. {@code XVType[]}) rather than
   *     the raw {@link ExtensionObject}(s) received in the request. A null ExtensionObject, whether
   *     scalar or an array element, is delivered as {@code null}. Matrix arguments retain their
   *     original representation; their elements are decoded only for validation. An empty value for
   *     an argument of ValueRank 2 or greater is delivered as an empty {@link Matrix} of the
   *     declared rank, because its wire form carries no dimensions.
   * @return this output values matching this Method's output arguments, if any.
   * @throws UaException if invocation has failed for some reason.
   */
  protected Variant[] invoke(InvocationContext invocationContext, Variant[] inputValues)
      throws UaException {

    throw new UaException(StatusCodes.Bad_NotImplemented);
  }

  /**
   * Validate the input values against the expected input arguments.
   *
   * <p>The DataType of each input value has already been verified; implementations need only verify
   * the value is "valid", if applicable, and throw InvalidArgumentException with a StatusCode of
   * Bad_OutOfRange for any invalid input values.
   *
   * @param inputArgumentValues the input values provided by the client for the current method call.
   *     Values for arguments with a structured DataType carry decoded {@link UaStructuredType}
   *     values rather than raw {@link ExtensionObject}s.
   * @throws InvalidArgumentException if one or more input argument values are invalid.
   */
  protected void validateInputArgumentValues(Variant[] inputArgumentValues)
      throws InvalidArgumentException {}

  /**
   * Extends {@link AccessContext} to provide additional context to implementations of {@link
   * AbstractMethodInvocationHandler}.
   */
  public interface InvocationContext extends AccessContext {

    /**
     * Get the request-wide Call diagnostics context, when invoked through the Call service.
     *
     * @return the context used to intern diagnostic strings, or empty for independent invocations.
     */
    default Optional<DiagnosticsContext<CallMethodRequest>> getCallDiagnostics() {
      return Optional.empty();
    }

    /**
     * Get the {@link OpcUaServer} instance.
     *
     * @return the {@link OpcUaServer} instance.
     */
    OpcUaServer getServer();

    /**
     * Get the {@link NodeId} of the ObjectNode the method being invoked belongs to.
     *
     * @return the {@link NodeId} of the ObjectNode the method being invoked belongs to.
     */
    NodeId getObjectId();

    /**
     * Get the {@link UaMethodNode} being invoked.
     *
     * @return the {@link UaMethodNode} being invoked.
     */
    UaMethodNode getMethodNode();

    /**
     * Get the number of input values the caller actually sent.
     *
     * <p>This distinguishes an omitted optional trailing input from one supplied as null: an
     * omitted input is not counted, a null one is.
     *
     * @return the number of supplied inputs.
     */
    int suppliedInputCount();

    /**
     * Get the status the result will carry on normal completion.
     *
     * @return the status; Good unless changed by {@link #setStatusCode(StatusCode)}.
     */
    StatusCode getStatusCode();

    /**
     * Set the status the result will carry on normal completion, alongside the outputs.
     *
     * <p>Any status that is not Bad is accepted, so Good subcodes such as Good_Overload and
     * Uncertain codes are allowed. Failures are reported by throwing, not by setting a Bad status.
     *
     * @param statusCode the status to return with the outputs.
     * @throws IllegalArgumentException if {@code statusCode} is Bad.
     */
    void setStatusCode(StatusCode statusCode);
  }

  /** The {@link InvocationContext} created for each invocation. */
  private static final class DefaultInvocationContext implements InvocationContext {

    private volatile StatusCode statusCode = StatusCode.GOOD;

    private final OpcUaServer server;
    private final UaMethodNode node;
    private final AccessContext accessContext;
    private final CallMethodRequest request;
    private final int suppliedInputCount;

    DefaultInvocationContext(
        OpcUaServer server,
        UaMethodNode node,
        AccessContext accessContext,
        CallMethodRequest request,
        int suppliedInputCount) {

      this.server = server;
      this.node = node;
      this.accessContext = accessContext;
      this.request = request;
      this.suppliedInputCount = suppliedInputCount;
    }

    @Override
    public OpcUaServer getServer() {
      return server;
    }

    @Override
    public NodeId getObjectId() {
      return request.getObjectId();
    }

    @Override
    public UaMethodNode getMethodNode() {
      return node;
    }

    @Override
    public Optional<Session> getSession() {
      return accessContext.getSession();
    }

    @Override
    public Optional<DiagnosticsContext<CallMethodRequest>> getCallDiagnostics() {
      return accessContext instanceof CallContext callContext
          ? Optional.of(callContext.getDiagnosticsContext())
          : Optional.empty();
    }

    @Override
    public int suppliedInputCount() {
      return suppliedInputCount;
    }

    @Override
    public StatusCode getStatusCode() {
      return statusCode;
    }

    @Override
    public void setStatusCode(StatusCode statusCode) {
      requireNonNull(statusCode);
      if (statusCode.isBad()) {
        throw new IllegalArgumentException(
            "a Bad status must be reported by throwing, not set on the context: " + statusCode);
      }
      this.statusCode = statusCode;
    }
  }
}
