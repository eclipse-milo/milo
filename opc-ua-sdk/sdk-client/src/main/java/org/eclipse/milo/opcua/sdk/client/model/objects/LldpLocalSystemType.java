/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.4">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.4</a>
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

  /**
   * Get the local value of the ChassisIdSubtype Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ChassisIdSubtype Node.
   * @throws UaException if an error occurs creating or getting the ChassisIdSubtype Node.
   */
  ChassisIdSubtype getChassisIdSubtype() throws UaException;

  /**
   * Set the local value of the ChassisIdSubtype Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ChassisIdSubtype Node.
   * @throws UaException if an error occurs creating or getting the ChassisIdSubtype Node.
   */
  void setChassisIdSubtype(ChassisIdSubtype value) throws UaException;

  /**
   * Read the value of the ChassisIdSubtype Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ChassisIdSubtype} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ChassisIdSubtype readChassisIdSubtype() throws UaException;

  /**
   * Write a new value for the ChassisIdSubtype Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link ChassisIdSubtype} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeChassisIdSubtype(ChassisIdSubtype value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readChassisIdSubtype}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ChassisIdSubtype> readChassisIdSubtypeAsync();

  /**
   * An asynchronous implementation of {@link #writeChassisIdSubtype}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeChassisIdSubtypeAsync(ChassisIdSubtype value);

  /**
   * Get the ChassisIdSubtype {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ChassisIdSubtype {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getChassisIdSubtypeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getChassisIdSubtypeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getChassisIdSubtypeNodeAsync();

  /**
   * Get the local value of the ChassisId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ChassisId Node.
   * @throws UaException if an error occurs creating or getting the ChassisId Node.
   */
  String getChassisId() throws UaException;

  /**
   * Set the local value of the ChassisId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ChassisId Node.
   * @throws UaException if an error occurs creating or getting the ChassisId Node.
   */
  void setChassisId(String value) throws UaException;

  /**
   * Read the value of the ChassisId Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readChassisId() throws UaException;

  /**
   * Write a new value for the ChassisId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeChassisId(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readChassisId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readChassisIdAsync();

  /**
   * An asynchronous implementation of {@link #writeChassisId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeChassisIdAsync(String value);

  /**
   * Get the ChassisId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ChassisId {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getChassisIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getChassisIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getChassisIdNodeAsync();

  /**
   * Get the local value of the SystemName Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SystemName Node.
   * @throws UaException if an error occurs creating or getting the SystemName Node.
   */
  String getSystemName() throws UaException;

  /**
   * Set the local value of the SystemName Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SystemName Node.
   * @throws UaException if an error occurs creating or getting the SystemName Node.
   */
  void setSystemName(String value) throws UaException;

  /**
   * Read the value of the SystemName Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readSystemName() throws UaException;

  /**
   * Write a new value for the SystemName Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSystemName(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSystemName}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readSystemNameAsync();

  /**
   * An asynchronous implementation of {@link #writeSystemName}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSystemNameAsync(String value);

  /**
   * Get the SystemName {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SystemName {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSystemNameNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSystemNameNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSystemNameNodeAsync();

  /**
   * Get the local value of the SystemDescription Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SystemDescription Node.
   * @throws UaException if an error occurs creating or getting the SystemDescription Node.
   */
  String getSystemDescription() throws UaException;

  /**
   * Set the local value of the SystemDescription Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SystemDescription Node.
   * @throws UaException if an error occurs creating or getting the SystemDescription Node.
   */
  void setSystemDescription(String value) throws UaException;

  /**
   * Read the value of the SystemDescription Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readSystemDescription() throws UaException;

  /**
   * Write a new value for the SystemDescription Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSystemDescription(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSystemDescription}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readSystemDescriptionAsync();

  /**
   * An asynchronous implementation of {@link #writeSystemDescription}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSystemDescriptionAsync(String value);

  /**
   * Get the SystemDescription {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SystemDescription {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSystemDescriptionNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSystemDescriptionNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSystemDescriptionNodeAsync();

  /**
   * Get the local value of the SystemCapabilitiesSupported Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SystemCapabilitiesSupported Node.
   * @throws UaException if an error occurs creating or getting the SystemCapabilitiesSupported
   *     Node.
   */
  LldpSystemCapabilitiesMap getSystemCapabilitiesSupported() throws UaException;

  /**
   * Set the local value of the SystemCapabilitiesSupported Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SystemCapabilitiesSupported Node.
   * @throws UaException if an error occurs creating or getting the SystemCapabilitiesSupported
   *     Node.
   */
  void setSystemCapabilitiesSupported(LldpSystemCapabilitiesMap value) throws UaException;

  /**
   * Read the value of the SystemCapabilitiesSupported Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link LldpSystemCapabilitiesMap} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LldpSystemCapabilitiesMap readSystemCapabilitiesSupported() throws UaException;

  /**
   * Write a new value for the SystemCapabilitiesSupported Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link LldpSystemCapabilitiesMap} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSystemCapabilitiesSupported(LldpSystemCapabilitiesMap value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSystemCapabilitiesSupported}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LldpSystemCapabilitiesMap> readSystemCapabilitiesSupportedAsync();

  /**
   * An asynchronous implementation of {@link #writeSystemCapabilitiesSupported}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSystemCapabilitiesSupportedAsync(
      LldpSystemCapabilitiesMap value);

  /**
   * Get the SystemCapabilitiesSupported {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SystemCapabilitiesSupported {@link PropertyType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSystemCapabilitiesSupportedNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSystemCapabilitiesSupportedNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSystemCapabilitiesSupportedNodeAsync();

  /**
   * Get the local value of the SystemCapabilitiesEnabled Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SystemCapabilitiesEnabled Node.
   * @throws UaException if an error occurs creating or getting the SystemCapabilitiesEnabled Node.
   */
  LldpSystemCapabilitiesMap getSystemCapabilitiesEnabled() throws UaException;

  /**
   * Set the local value of the SystemCapabilitiesEnabled Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SystemCapabilitiesEnabled Node.
   * @throws UaException if an error occurs creating or getting the SystemCapabilitiesEnabled Node.
   */
  void setSystemCapabilitiesEnabled(LldpSystemCapabilitiesMap value) throws UaException;

  /**
   * Read the value of the SystemCapabilitiesEnabled Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link LldpSystemCapabilitiesMap} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LldpSystemCapabilitiesMap readSystemCapabilitiesEnabled() throws UaException;

  /**
   * Write a new value for the SystemCapabilitiesEnabled Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link LldpSystemCapabilitiesMap} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSystemCapabilitiesEnabled(LldpSystemCapabilitiesMap value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSystemCapabilitiesEnabled}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LldpSystemCapabilitiesMap> readSystemCapabilitiesEnabledAsync();

  /**
   * An asynchronous implementation of {@link #writeSystemCapabilitiesEnabled}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSystemCapabilitiesEnabledAsync(
      LldpSystemCapabilitiesMap value);

  /**
   * Get the SystemCapabilitiesEnabled {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SystemCapabilitiesEnabled {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSystemCapabilitiesEnabledNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSystemCapabilitiesEnabledNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSystemCapabilitiesEnabledNodeAsync();
}
