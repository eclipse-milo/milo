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
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ChassisIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpSystemCapabilitiesMap;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.4">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.4</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface LldpLocalSystemType extends BaseObjectType {
  QualifiedProperty<ChassisIdSubtype> CHASSIS_ID_SUBTYPE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ChassisIdSubtype",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=18947"),
          -1,
          ChassisIdSubtype.class);

  QualifiedProperty<String> CHASSIS_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ChassisId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> SYSTEM_NAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SystemName",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> SYSTEM_DESCRIPTION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SystemDescription",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<LldpSystemCapabilitiesMap> SYSTEM_CAPABILITIES_SUPPORTED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SystemCapabilitiesSupported",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=18956"),
          -1,
          LldpSystemCapabilitiesMap.class);

  QualifiedProperty<LldpSystemCapabilitiesMap> SYSTEM_CAPABILITIES_ENABLED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SystemCapabilitiesEnabled",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=18956"),
          -1,
          LldpSystemCapabilitiesMap.class);

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
  PropertyType getChassisIdSubtypeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getChassisIdSubtypeNodeAsync();

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
  PropertyType getChassisIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getChassisIdNodeAsync();

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
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSystemNameNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getSystemNameNodeAsync();

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
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSystemDescriptionNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getSystemDescriptionNodeAsync();

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
  @Nullable PropertyType getSystemCapabilitiesSupportedNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getSystemCapabilitiesSupportedNodeAsync();

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
  @Nullable PropertyType getSystemCapabilitiesEnabledNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getSystemCapabilitiesEnabledNodeAsync();
}
