package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ChassisIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PortIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpManagementAddressType;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpSystemCapabilitiesMap;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpTlvType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the LldpRemoteSystemType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.6">Model
 *     documentation</a>
 */
public interface LldpRemoteSystemType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 19033L);

  /**
   * Resolves the optional SystemName child, a BaseDataVariableType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable VariableNode getSystemNameNode() throws UaException;

  /** Asynchronous form of {@link #getSystemNameNode()}. */
  CompletableFuture<? extends @Nullable VariableNode> getSystemNameNodeAsync();

  /**
   * Reads the Value of the SystemName child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readSystemName() throws UaException;

  /**
   * Writes the Value of the SystemName child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSystemName(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readSystemName()}. */
  CompletableFuture<? extends @Nullable String> readSystemNameAsync();

  /** Asynchronous form of {@link #writeSystemName}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSystemNameAsync(@Nullable String value);

  /**
   * Resolves the mandatory RemoteIndex child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getRemoteIndexNode() throws UaException;

  /** Asynchronous form of {@link #getRemoteIndexNode()}. */
  CompletableFuture<? extends VariableNode> getRemoteIndexNodeAsync();

  /**
   * Reads the Value of the RemoteIndex child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readRemoteIndex() throws UaException;

  /**
   * Writes the Value of the RemoteIndex child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeRemoteIndex(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readRemoteIndex()}. */
  CompletableFuture<? extends @Nullable UInteger> readRemoteIndexAsync();

  /** Asynchronous form of {@link #writeRemoteIndex}; completes with the operation status. */
  CompletableFuture<StatusCode> writeRemoteIndexAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory PortIdSubtype child, a BaseDataVariableType with DataType PortIdSubtype.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getPortIdSubtypeNode() throws UaException;

  /** Asynchronous form of {@link #getPortIdSubtypeNode()}. */
  CompletableFuture<? extends VariableNode> getPortIdSubtypeNodeAsync();

  /**
   * Reads the Value of the PortIdSubtype child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable PortIdSubtype readPortIdSubtype() throws UaException;

  /**
   * Writes the Value of the PortIdSubtype child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePortIdSubtype(@Nullable PortIdSubtype value) throws UaException;

  /** Asynchronous form of {@link #readPortIdSubtype()}. */
  CompletableFuture<? extends @Nullable PortIdSubtype> readPortIdSubtypeAsync();

  /** Asynchronous form of {@link #writePortIdSubtype}; completes with the operation status. */
  CompletableFuture<StatusCode> writePortIdSubtypeAsync(@Nullable PortIdSubtype value);

  /**
   * Resolves the optional RemoteChanges child, a BaseDataVariableType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable VariableNode getRemoteChangesNode() throws UaException;

  /** Asynchronous form of {@link #getRemoteChangesNode()}. */
  CompletableFuture<? extends @Nullable VariableNode> getRemoteChangesNodeAsync();

  /**
   * Reads the Value of the RemoteChanges child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readRemoteChanges() throws UaException;

  /**
   * Writes the Value of the RemoteChanges child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeRemoteChanges(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readRemoteChanges()}. */
  CompletableFuture<? extends @Nullable Boolean> readRemoteChangesAsync();

  /** Asynchronous form of {@link #writeRemoteChanges}; completes with the operation status. */
  CompletableFuture<StatusCode> writeRemoteChangesAsync(@Nullable Boolean value);

  /**
   * Resolves the optional PortDescription child, a BaseDataVariableType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable VariableNode getPortDescriptionNode() throws UaException;

  /** Asynchronous form of {@link #getPortDescriptionNode()}. */
  CompletableFuture<? extends @Nullable VariableNode> getPortDescriptionNodeAsync();

  /**
   * Reads the Value of the PortDescription child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readPortDescription() throws UaException;

  /**
   * Writes the Value of the PortDescription child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePortDescription(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readPortDescription()}. */
  CompletableFuture<? extends @Nullable String> readPortDescriptionAsync();

  /** Asynchronous form of {@link #writePortDescription}; completes with the operation status. */
  CompletableFuture<StatusCode> writePortDescriptionAsync(@Nullable String value);

  /**
   * Resolves the mandatory ChassisIdSubtype child, a BaseDataVariableType with DataType
   * ChassisIdSubtype.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getChassisIdSubtypeNode() throws UaException;

  /** Asynchronous form of {@link #getChassisIdSubtypeNode()}. */
  CompletableFuture<? extends VariableNode> getChassisIdSubtypeNodeAsync();

  /**
   * Reads the Value of the ChassisIdSubtype child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ChassisIdSubtype readChassisIdSubtype() throws UaException;

  /**
   * Writes the Value of the ChassisIdSubtype child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeChassisIdSubtype(@Nullable ChassisIdSubtype value) throws UaException;

  /** Asynchronous form of {@link #readChassisIdSubtype()}. */
  CompletableFuture<? extends @Nullable ChassisIdSubtype> readChassisIdSubtypeAsync();

  /** Asynchronous form of {@link #writeChassisIdSubtype}; completes with the operation status. */
  CompletableFuture<StatusCode> writeChassisIdSubtypeAsync(@Nullable ChassisIdSubtype value);

  /**
   * Resolves the optional RemoteUnknownTlv child, a BaseDataVariableType with DataType LldpTlvType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable VariableNode getRemoteUnknownTlvNode() throws UaException;

  /** Asynchronous form of {@link #getRemoteUnknownTlvNode()}. */
  CompletableFuture<? extends @Nullable VariableNode> getRemoteUnknownTlvNodeAsync();

  /**
   * Reads the Value of the RemoteUnknownTlv child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LldpTlvType @Nullable [] readRemoteUnknownTlv() throws UaException;

  /**
   * Writes the Value of the RemoteUnknownTlv child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeRemoteUnknownTlv(@Nullable LldpTlvType @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readRemoteUnknownTlv()}. */
  CompletableFuture<? extends @Nullable LldpTlvType @Nullable []> readRemoteUnknownTlvAsync();

  /** Asynchronous form of {@link #writeRemoteUnknownTlv}; completes with the operation status. */
  CompletableFuture<StatusCode> writeRemoteUnknownTlvAsync(
      @Nullable LldpTlvType @Nullable [] value);

  /**
   * Resolves the optional ManagementAddress child, a BaseDataVariableType with DataType
   * LldpManagementAddressType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable VariableNode getManagementAddressNode() throws UaException;

  /** Asynchronous form of {@link #getManagementAddressNode()}. */
  CompletableFuture<? extends @Nullable VariableNode> getManagementAddressNodeAsync();

  /**
   * Reads the Value of the ManagementAddress child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LldpManagementAddressType @Nullable [] readManagementAddress() throws UaException;

  /**
   * Writes the Value of the ManagementAddress child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeManagementAddress(@Nullable LldpManagementAddressType @Nullable [] value)
      throws UaException;

  /** Asynchronous form of {@link #readManagementAddress()}. */
  CompletableFuture<? extends @Nullable LldpManagementAddressType @Nullable []>
      readManagementAddressAsync();

  /** Asynchronous form of {@link #writeManagementAddress}; completes with the operation status. */
  CompletableFuture<StatusCode> writeManagementAddressAsync(
      @Nullable LldpManagementAddressType @Nullable [] value);

  /**
   * Resolves the optional SystemDescription child, a BaseDataVariableType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable VariableNode getSystemDescriptionNode() throws UaException;

  /** Asynchronous form of {@link #getSystemDescriptionNode()}. */
  CompletableFuture<? extends @Nullable VariableNode> getSystemDescriptionNodeAsync();

  /**
   * Reads the Value of the SystemDescription child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readSystemDescription() throws UaException;

  /**
   * Writes the Value of the SystemDescription child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSystemDescription(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readSystemDescription()}. */
  CompletableFuture<? extends @Nullable String> readSystemDescriptionAsync();

  /** Asynchronous form of {@link #writeSystemDescription}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSystemDescriptionAsync(@Nullable String value);

  /**
   * Resolves the optional RemoteTooManyNeighbors child, a BaseDataVariableType with DataType
   * Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable VariableNode getRemoteTooManyNeighborsNode() throws UaException;

  /** Asynchronous form of {@link #getRemoteTooManyNeighborsNode()}. */
  CompletableFuture<? extends @Nullable VariableNode> getRemoteTooManyNeighborsNodeAsync();

  /**
   * Reads the Value of the RemoteTooManyNeighbors child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readRemoteTooManyNeighbors() throws UaException;

  /**
   * Writes the Value of the RemoteTooManyNeighbors child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeRemoteTooManyNeighbors(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readRemoteTooManyNeighbors()}. */
  CompletableFuture<? extends @Nullable Boolean> readRemoteTooManyNeighborsAsync();

  /**
   * Asynchronous form of {@link #writeRemoteTooManyNeighbors}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeRemoteTooManyNeighborsAsync(@Nullable Boolean value);

  /**
   * Resolves the optional SystemCapabilitiesEnabled child, a BaseDataVariableType with DataType
   * LldpSystemCapabilitiesMap.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable VariableNode getSystemCapabilitiesEnabledNode() throws UaException;

  /** Asynchronous form of {@link #getSystemCapabilitiesEnabledNode()}. */
  CompletableFuture<? extends @Nullable VariableNode> getSystemCapabilitiesEnabledNodeAsync();

  /**
   * Reads the Value of the SystemCapabilitiesEnabled child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LldpSystemCapabilitiesMap readSystemCapabilitiesEnabled() throws UaException;

  /**
   * Writes the Value of the SystemCapabilitiesEnabled child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSystemCapabilitiesEnabled(@Nullable LldpSystemCapabilitiesMap value) throws UaException;

  /** Asynchronous form of {@link #readSystemCapabilitiesEnabled()}. */
  CompletableFuture<? extends @Nullable LldpSystemCapabilitiesMap>
      readSystemCapabilitiesEnabledAsync();

  /**
   * Asynchronous form of {@link #writeSystemCapabilitiesEnabled}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeSystemCapabilitiesEnabledAsync(
      @Nullable LldpSystemCapabilitiesMap value);

  /**
   * Resolves the optional SystemCapabilitiesSupported child, a BaseDataVariableType with DataType
   * LldpSystemCapabilitiesMap.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable VariableNode getSystemCapabilitiesSupportedNode() throws UaException;

  /** Asynchronous form of {@link #getSystemCapabilitiesSupportedNode()}. */
  CompletableFuture<? extends @Nullable VariableNode> getSystemCapabilitiesSupportedNodeAsync();

  /**
   * Reads the Value of the SystemCapabilitiesSupported child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LldpSystemCapabilitiesMap readSystemCapabilitiesSupported() throws UaException;

  /**
   * Writes the Value of the SystemCapabilitiesSupported child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSystemCapabilitiesSupported(@Nullable LldpSystemCapabilitiesMap value)
      throws UaException;

  /** Asynchronous form of {@link #readSystemCapabilitiesSupported()}. */
  CompletableFuture<? extends @Nullable LldpSystemCapabilitiesMap>
      readSystemCapabilitiesSupportedAsync();

  /**
   * Asynchronous form of {@link #writeSystemCapabilitiesSupported}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeSystemCapabilitiesSupportedAsync(
      @Nullable LldpSystemCapabilitiesMap value);

  /**
   * Resolves the mandatory PortId child, a BaseDataVariableType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getPortIdNode() throws UaException;

  /** Asynchronous form of {@link #getPortIdNode()}. */
  CompletableFuture<? extends VariableNode> getPortIdNodeAsync();

  /**
   * Reads the Value of the PortId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readPortId() throws UaException;

  /**
   * Writes the Value of the PortId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePortId(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readPortId()}. */
  CompletableFuture<? extends @Nullable String> readPortIdAsync();

  /** Asynchronous form of {@link #writePortId}; completes with the operation status. */
  CompletableFuture<StatusCode> writePortIdAsync(@Nullable String value);

  /**
   * Resolves the mandatory TimeMark child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getTimeMarkNode() throws UaException;

  /** Asynchronous form of {@link #getTimeMarkNode()}. */
  CompletableFuture<? extends VariableNode> getTimeMarkNodeAsync();

  /**
   * Reads the Value of the TimeMark child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readTimeMark() throws UaException;

  /**
   * Writes the Value of the TimeMark child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTimeMark(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readTimeMark()}. */
  CompletableFuture<? extends @Nullable UInteger> readTimeMarkAsync();

  /** Asynchronous form of {@link #writeTimeMark}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTimeMarkAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory ChassisId child, a BaseDataVariableType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getChassisIdNode() throws UaException;

  /** Asynchronous form of {@link #getChassisIdNode()}. */
  CompletableFuture<? extends VariableNode> getChassisIdNodeAsync();

  /**
   * Reads the Value of the ChassisId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readChassisId() throws UaException;

  /**
   * Writes the Value of the ChassisId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeChassisId(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readChassisId()}. */
  CompletableFuture<? extends @Nullable String> readChassisIdAsync();

  /** Asynchronous form of {@link #writeChassisId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeChassisIdAsync(@Nullable String value);
}
