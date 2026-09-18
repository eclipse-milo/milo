package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the ProvisionableDeviceType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.3">Model
 *     documentation</a>
 */
public interface ProvisionableDeviceType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 26871L);

  QualifiedProperty<Boolean> IsSingleton_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "IsSingleton",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  /**
   * Resolves the mandatory IsSingleton child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getIsSingletonNode() throws UaException;

  /** Asynchronous form of {@link #getIsSingletonNode()}. */
  CompletableFuture<? extends PropertyType> getIsSingletonNodeAsync();

  /**
   * Reads the Value of the IsSingleton child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readIsSingleton() throws UaException;

  /**
   * Writes the Value of the IsSingleton child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeIsSingleton(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readIsSingleton()}. */
  CompletableFuture<? extends @Nullable Boolean> readIsSingletonAsync();

  /** Asynchronous form of {@link #writeIsSingleton}; completes with the operation status. */
  CompletableFuture<StatusCode> writeIsSingletonAsync(@Nullable Boolean value);

  /**
   * Resolves the mandatory RequestTickets Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.4">Model
   *     documentation</a>
   */
  UaMethodNode getRequestTicketsMethodNode() throws UaException;

  /** Asynchronous form of {@link #getRequestTicketsMethodNode()}. */
  CompletableFuture<UaMethodNode> getRequestTicketsMethodNodeAsync();

  /**
   * Calls the RequestTickets Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.4">Model
   *     documentation</a>
   */
  @Nullable String @Nullable [] requestTickets() throws UaException;

  /**
   * Calls the RequestTickets Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable String @Nullable []> callRequestTickets() throws UaException;

  /**
   * Calls the RequestTickets Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable String @Nullable []> callRequestTicketsWith(MethodCallOptions options)
      throws UaException;

  /** Asynchronous form of {@link #requestTickets}. */
  CompletableFuture<@Nullable String @Nullable []> requestTicketsAsync();

  /** Asynchronous form of {@link #callRequestTickets}. */
  CompletableFuture<MethodCallResult<@Nullable String @Nullable []>> callRequestTicketsAsync();

  /** Asynchronous form of {@link #callRequestTicketsWith}. */
  CompletableFuture<MethodCallResult<@Nullable String @Nullable []>> callRequestTicketsWithAsync(
      MethodCallOptions options);

  /**
   * Resolves the optional SetRegistrarEndpoints Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.5">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getSetRegistrarEndpointsMethodNode() throws UaException;

  /** Asynchronous form of {@link #getSetRegistrarEndpointsMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getSetRegistrarEndpointsMethodNodeAsync();

  /**
   * Calls the SetRegistrarEndpoints Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.5">Model
   *     documentation</a>
   */
  void setRegistrarEndpoints(@Nullable ApplicationDescription @Nullable [] registrars)
      throws UaException;

  /**
   * Calls the SetRegistrarEndpoints Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callSetRegistrarEndpoints(
      @Nullable ApplicationDescription @Nullable [] registrars) throws UaException;

  /**
   * Calls the SetRegistrarEndpoints Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callSetRegistrarEndpointsWith(
      MethodCallOptions options, @Nullable ApplicationDescription @Nullable [] registrars)
      throws UaException;

  /** Asynchronous form of {@link #setRegistrarEndpoints}. */
  CompletableFuture<Void> setRegistrarEndpointsAsync(
      @Nullable ApplicationDescription @Nullable [] registrars);

  /** Asynchronous form of {@link #callSetRegistrarEndpoints}. */
  CompletableFuture<MethodCallResult<Void>> callSetRegistrarEndpointsAsync(
      @Nullable ApplicationDescription @Nullable [] registrars);

  /** Asynchronous form of {@link #callSetRegistrarEndpointsWith}. */
  CompletableFuture<MethodCallResult<Void>> callSetRegistrarEndpointsWithAsync(
      MethodCallOptions options, @Nullable ApplicationDescription @Nullable [] registrars);
}
