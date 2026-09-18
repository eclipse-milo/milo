package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.AnalogUnitType;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceAdminStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceOperStatus;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the IIetfBaseNetworkInterfaceType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.1">Model
 *     documentation</a>
 */
public interface IIetfBaseNetworkInterfaceType extends BaseInterfaceType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 24148L);

  /**
   * Resolves the mandatory OperStatus child, a BaseDataVariableType with DataType
   * InterfaceOperStatus.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getOperStatusNode() throws UaException;

  /** Asynchronous form of {@link #getOperStatusNode()}. */
  CompletableFuture<? extends VariableNode> getOperStatusNodeAsync();

  /**
   * Reads the Value of the OperStatus child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable InterfaceOperStatus readOperStatus() throws UaException;

  /**
   * Writes the Value of the OperStatus child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeOperStatus(@Nullable InterfaceOperStatus value) throws UaException;

  /** Asynchronous form of {@link #readOperStatus()}. */
  CompletableFuture<? extends @Nullable InterfaceOperStatus> readOperStatusAsync();

  /** Asynchronous form of {@link #writeOperStatus}; completes with the operation status. */
  CompletableFuture<StatusCode> writeOperStatusAsync(@Nullable InterfaceOperStatus value);

  /**
   * Resolves the mandatory AdminStatus child, a BaseDataVariableType with DataType
   * InterfaceAdminStatus.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getAdminStatusNode() throws UaException;

  /** Asynchronous form of {@link #getAdminStatusNode()}. */
  CompletableFuture<? extends VariableNode> getAdminStatusNodeAsync();

  /**
   * Reads the Value of the AdminStatus child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable InterfaceAdminStatus readAdminStatus() throws UaException;

  /**
   * Writes the Value of the AdminStatus child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeAdminStatus(@Nullable InterfaceAdminStatus value) throws UaException;

  /** Asynchronous form of {@link #readAdminStatus()}. */
  CompletableFuture<? extends @Nullable InterfaceAdminStatus> readAdminStatusAsync();

  /** Asynchronous form of {@link #writeAdminStatus}; completes with the operation status. */
  CompletableFuture<StatusCode> writeAdminStatusAsync(@Nullable InterfaceAdminStatus value);

  /**
   * Resolves the optional PhysAddress child, a BaseDataVariableType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable VariableNode getPhysAddressNode() throws UaException;

  /** Asynchronous form of {@link #getPhysAddressNode()}. */
  CompletableFuture<? extends @Nullable VariableNode> getPhysAddressNodeAsync();

  /**
   * Reads the Value of the PhysAddress child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readPhysAddress() throws UaException;

  /**
   * Writes the Value of the PhysAddress child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePhysAddress(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readPhysAddress()}. */
  CompletableFuture<? extends @Nullable String> readPhysAddressAsync();

  /** Asynchronous form of {@link #writePhysAddress}; completes with the operation status. */
  CompletableFuture<StatusCode> writePhysAddressAsync(@Nullable String value);

  /**
   * Resolves the mandatory Speed child, a AnalogUnitType with DataType UInt64.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.2/#5.3.2.4">AnalogUnitType
   *     documentation</a>
   */
  AnalogUnitType getSpeedNode() throws UaException;

  /** Asynchronous form of {@link #getSpeedNode()}. */
  CompletableFuture<? extends AnalogUnitType> getSpeedNodeAsync();

  /**
   * Reads the Value of the Speed child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ULong readSpeed() throws UaException;

  /**
   * Writes the Value of the Speed child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSpeed(@Nullable ULong value) throws UaException;

  /** Asynchronous form of {@link #readSpeed()}. */
  CompletableFuture<? extends @Nullable ULong> readSpeedAsync();

  /** Asynchronous form of {@link #writeSpeed}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSpeedAsync(@Nullable ULong value);
}
