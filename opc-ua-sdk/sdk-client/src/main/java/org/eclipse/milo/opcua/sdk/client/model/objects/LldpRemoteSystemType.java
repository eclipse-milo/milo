/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ChassisIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PortIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpManagementAddressType;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpSystemCapabilitiesMap;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpTlvType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.6">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.6</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface LldpRemoteSystemType extends BaseObjectType {
  /** Gets the existing node's local value. */
  @Nullable UInteger getTimeMark() throws UaException;

  /** Sets the existing node's local value. */
  void setTimeMark(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readTimeMark() throws UaException;

  /** Writes the value remotely. */
  void writeTimeMark(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readTimeMarkAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeTimeMarkAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getTimeMarkNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getTimeMarkNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getRemoteIndex() throws UaException;

  /** Sets the existing node's local value. */
  void setRemoteIndex(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readRemoteIndex() throws UaException;

  /** Writes the value remotely. */
  void writeRemoteIndex(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readRemoteIndexAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeRemoteIndexAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getRemoteIndexNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getRemoteIndexNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ChassisIdSubtype getChassisIdSubtype() throws UaException;

  /** Sets the existing node's local value. */
  void setChassisIdSubtype(@Nullable ChassisIdSubtype value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ChassisIdSubtype readChassisIdSubtype() throws UaException;

  /** Writes the value remotely. */
  void writeChassisIdSubtype(@Nullable ChassisIdSubtype value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ChassisIdSubtype> readChassisIdSubtypeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeChassisIdSubtypeAsync(@Nullable ChassisIdSubtype value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getChassisIdSubtypeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getChassisIdSubtypeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getChassisId() throws UaException;

  /** Sets the existing node's local value. */
  void setChassisId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readChassisId() throws UaException;

  /** Writes the value remotely. */
  void writeChassisId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readChassisIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeChassisIdAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getChassisIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getChassisIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable PortIdSubtype getPortIdSubtype() throws UaException;

  /** Sets the existing node's local value. */
  void setPortIdSubtype(@Nullable PortIdSubtype value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable PortIdSubtype readPortIdSubtype() throws UaException;

  /** Writes the value remotely. */
  void writePortIdSubtype(@Nullable PortIdSubtype value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable PortIdSubtype> readPortIdSubtypeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePortIdSubtypeAsync(@Nullable PortIdSubtype value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getPortIdSubtypeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getPortIdSubtypeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getPortId() throws UaException;

  /** Sets the existing node's local value. */
  void setPortId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readPortId() throws UaException;

  /** Writes the value remotely. */
  void writePortId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readPortIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePortIdAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getPortIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getPortIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getPortDescription() throws UaException;

  /** Sets the existing node's local value. */
  void setPortDescription(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readPortDescription() throws UaException;

  /** Writes the value remotely. */
  void writePortDescription(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readPortDescriptionAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePortDescriptionAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getPortDescriptionNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable BaseDataVariableType> getPortDescriptionNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getSystemName() throws UaException;

  /** Sets the existing node's local value. */
  void setSystemName(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readSystemName() throws UaException;

  /** Writes the value remotely. */
  void writeSystemName(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readSystemNameAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSystemNameAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getSystemNameNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable BaseDataVariableType> getSystemNameNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getSystemDescription() throws UaException;

  /** Sets the existing node's local value. */
  void setSystemDescription(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readSystemDescription() throws UaException;

  /** Writes the value remotely. */
  void writeSystemDescription(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readSystemDescriptionAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSystemDescriptionAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getSystemDescriptionNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable BaseDataVariableType> getSystemDescriptionNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LldpSystemCapabilitiesMap getSystemCapabilitiesSupported() throws UaException;

  /** Sets the existing node's local value. */
  void setSystemCapabilitiesSupported(@Nullable LldpSystemCapabilitiesMap value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LldpSystemCapabilitiesMap readSystemCapabilitiesSupported() throws UaException;

  /** Writes the value remotely. */
  void writeSystemCapabilitiesSupported(@Nullable LldpSystemCapabilitiesMap value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LldpSystemCapabilitiesMap>
      readSystemCapabilitiesSupportedAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSystemCapabilitiesSupportedAsync(
      @Nullable LldpSystemCapabilitiesMap value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getSystemCapabilitiesSupportedNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable BaseDataVariableType>
      getSystemCapabilitiesSupportedNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LldpSystemCapabilitiesMap getSystemCapabilitiesEnabled() throws UaException;

  /** Sets the existing node's local value. */
  void setSystemCapabilitiesEnabled(@Nullable LldpSystemCapabilitiesMap value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LldpSystemCapabilitiesMap readSystemCapabilitiesEnabled() throws UaException;

  /** Writes the value remotely. */
  void writeSystemCapabilitiesEnabled(@Nullable LldpSystemCapabilitiesMap value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LldpSystemCapabilitiesMap>
      readSystemCapabilitiesEnabledAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSystemCapabilitiesEnabledAsync(
      @Nullable LldpSystemCapabilitiesMap value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getSystemCapabilitiesEnabledNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable BaseDataVariableType>
      getSystemCapabilitiesEnabledNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getRemoteChanges() throws UaException;

  /** Sets the existing node's local value. */
  void setRemoteChanges(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readRemoteChanges() throws UaException;

  /** Writes the value remotely. */
  void writeRemoteChanges(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readRemoteChangesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeRemoteChangesAsync(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getRemoteChangesNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable BaseDataVariableType> getRemoteChangesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getRemoteTooManyNeighbors() throws UaException;

  /** Sets the existing node's local value. */
  void setRemoteTooManyNeighbors(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readRemoteTooManyNeighbors() throws UaException;

  /** Writes the value remotely. */
  void writeRemoteTooManyNeighbors(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readRemoteTooManyNeighborsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeRemoteTooManyNeighborsAsync(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getRemoteTooManyNeighborsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable BaseDataVariableType> getRemoteTooManyNeighborsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LldpManagementAddressType @Nullable [] getManagementAddress() throws UaException;

  /** Sets the existing node's local value. */
  void setManagementAddress(@Nullable LldpManagementAddressType @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LldpManagementAddressType @Nullable [] readManagementAddress() throws UaException;

  /** Writes the value remotely. */
  void writeManagementAddress(@Nullable LldpManagementAddressType @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LldpManagementAddressType @Nullable []>
      readManagementAddressAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeManagementAddressAsync(
      @Nullable LldpManagementAddressType @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getManagementAddressNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable BaseDataVariableType> getManagementAddressNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LldpTlvType @Nullable [] getRemoteUnknownTlv() throws UaException;

  /** Sets the existing node's local value. */
  void setRemoteUnknownTlv(@Nullable LldpTlvType @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LldpTlvType @Nullable [] readRemoteUnknownTlv() throws UaException;

  /** Writes the value remotely. */
  void writeRemoteUnknownTlv(@Nullable LldpTlvType @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LldpTlvType @Nullable []> readRemoteUnknownTlvAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeRemoteUnknownTlvAsync(
      @Nullable LldpTlvType @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getRemoteUnknownTlvNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable BaseDataVariableType> getRemoteUnknownTlvNodeAsync();
}
