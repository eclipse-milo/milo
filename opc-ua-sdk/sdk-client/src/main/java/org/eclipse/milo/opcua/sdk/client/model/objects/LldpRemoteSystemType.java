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
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ChassisIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PortIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpManagementAddressType;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpSystemCapabilitiesMap;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpTlvType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.6">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.6</a>
 */
public interface LldpRemoteSystemType extends BaseObjectType {
  /**
   * Get the local value of the TimeMark Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the TimeMark Node.
   * @throws UaException if an error occurs creating or getting the TimeMark Node.
   */
  UInteger getTimeMark() throws UaException;

  /**
   * Set the local value of the TimeMark Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the TimeMark Node.
   * @throws UaException if an error occurs creating or getting the TimeMark Node.
   */
  void setTimeMark(UInteger value) throws UaException;

  /**
   * Read the value of the TimeMark Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readTimeMark() throws UaException;

  /**
   * Write a new value for the TimeMark Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeTimeMark(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readTimeMark}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readTimeMarkAsync();

  /**
   * An asynchronous implementation of {@link #writeTimeMark}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeTimeMarkAsync(UInteger value);

  /**
   * Get the TimeMark {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the TimeMark {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getTimeMarkNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTimeMarkNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getTimeMarkNodeAsync();

  /**
   * Get the local value of the RemoteIndex Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the RemoteIndex Node.
   * @throws UaException if an error occurs creating or getting the RemoteIndex Node.
   */
  UInteger getRemoteIndex() throws UaException;

  /**
   * Set the local value of the RemoteIndex Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the RemoteIndex Node.
   * @throws UaException if an error occurs creating or getting the RemoteIndex Node.
   */
  void setRemoteIndex(UInteger value) throws UaException;

  /**
   * Read the value of the RemoteIndex Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readRemoteIndex() throws UaException;

  /**
   * Write a new value for the RemoteIndex Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeRemoteIndex(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readRemoteIndex}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readRemoteIndexAsync();

  /**
   * An asynchronous implementation of {@link #writeRemoteIndex}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeRemoteIndexAsync(UInteger value);

  /**
   * Get the RemoteIndex {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RemoteIndex {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getRemoteIndexNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRemoteIndexNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getRemoteIndexNodeAsync();

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
   * Get the ChassisIdSubtype {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ChassisIdSubtype {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getChassisIdSubtypeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getChassisIdSubtypeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getChassisIdSubtypeNodeAsync();

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
   * Get the ChassisId {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ChassisId {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getChassisIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getChassisIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getChassisIdNodeAsync();

  /**
   * Get the local value of the PortIdSubtype Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the PortIdSubtype Node.
   * @throws UaException if an error occurs creating or getting the PortIdSubtype Node.
   */
  PortIdSubtype getPortIdSubtype() throws UaException;

  /**
   * Set the local value of the PortIdSubtype Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the PortIdSubtype Node.
   * @throws UaException if an error occurs creating or getting the PortIdSubtype Node.
   */
  void setPortIdSubtype(PortIdSubtype value) throws UaException;

  /**
   * Read the value of the PortIdSubtype Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link PortIdSubtype} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  PortIdSubtype readPortIdSubtype() throws UaException;

  /**
   * Write a new value for the PortIdSubtype Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link PortIdSubtype} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePortIdSubtype(PortIdSubtype value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPortIdSubtype}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends PortIdSubtype> readPortIdSubtypeAsync();

  /**
   * An asynchronous implementation of {@link #writePortIdSubtype}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePortIdSubtypeAsync(PortIdSubtype value);

  /**
   * Get the PortIdSubtype {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the PortIdSubtype {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getPortIdSubtypeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPortIdSubtypeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getPortIdSubtypeNodeAsync();

  /**
   * Get the local value of the PortId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the PortId Node.
   * @throws UaException if an error occurs creating or getting the PortId Node.
   */
  String getPortId() throws UaException;

  /**
   * Set the local value of the PortId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the PortId Node.
   * @throws UaException if an error occurs creating or getting the PortId Node.
   */
  void setPortId(String value) throws UaException;

  /**
   * Read the value of the PortId Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readPortId() throws UaException;

  /**
   * Write a new value for the PortId Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePortId(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPortId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readPortIdAsync();

  /**
   * An asynchronous implementation of {@link #writePortId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePortIdAsync(String value);

  /**
   * Get the PortId {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the PortId {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getPortIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPortIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getPortIdNodeAsync();

  /**
   * Get the local value of the PortDescription Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the PortDescription Node.
   * @throws UaException if an error occurs creating or getting the PortDescription Node.
   */
  String getPortDescription() throws UaException;

  /**
   * Set the local value of the PortDescription Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the PortDescription Node.
   * @throws UaException if an error occurs creating or getting the PortDescription Node.
   */
  void setPortDescription(String value) throws UaException;

  /**
   * Read the value of the PortDescription Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readPortDescription() throws UaException;

  /**
   * Write a new value for the PortDescription Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePortDescription(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPortDescription}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readPortDescriptionAsync();

  /**
   * An asynchronous implementation of {@link #writePortDescription}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePortDescriptionAsync(String value);

  /**
   * Get the PortDescription {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the PortDescription {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getPortDescriptionNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPortDescriptionNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getPortDescriptionNodeAsync();

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
   * Get the SystemName {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SystemName {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getSystemNameNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSystemNameNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSystemNameNodeAsync();

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
   * Get the SystemDescription {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SystemDescription {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getSystemDescriptionNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSystemDescriptionNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSystemDescriptionNodeAsync();

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
   * Get the SystemCapabilitiesSupported {@link BaseDataVariableType} Node, or {@code null} if it
   * does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SystemCapabilitiesSupported {@link BaseDataVariableType} Node, or {@code null} if
   *     it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getSystemCapabilitiesSupportedNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSystemCapabilitiesSupportedNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSystemCapabilitiesSupportedNodeAsync();

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
   * Get the SystemCapabilitiesEnabled {@link BaseDataVariableType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SystemCapabilitiesEnabled {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getSystemCapabilitiesEnabledNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSystemCapabilitiesEnabledNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSystemCapabilitiesEnabledNodeAsync();

  /**
   * Get the local value of the RemoteChanges Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the RemoteChanges Node.
   * @throws UaException if an error occurs creating or getting the RemoteChanges Node.
   */
  Boolean getRemoteChanges() throws UaException;

  /**
   * Set the local value of the RemoteChanges Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the RemoteChanges Node.
   * @throws UaException if an error occurs creating or getting the RemoteChanges Node.
   */
  void setRemoteChanges(Boolean value) throws UaException;

  /**
   * Read the value of the RemoteChanges Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readRemoteChanges() throws UaException;

  /**
   * Write a new value for the RemoteChanges Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeRemoteChanges(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readRemoteChanges}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readRemoteChangesAsync();

  /**
   * An asynchronous implementation of {@link #writeRemoteChanges}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeRemoteChangesAsync(Boolean value);

  /**
   * Get the RemoteChanges {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RemoteChanges {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getRemoteChangesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRemoteChangesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getRemoteChangesNodeAsync();

  /**
   * Get the local value of the RemoteTooManyNeighbors Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the RemoteTooManyNeighbors Node.
   * @throws UaException if an error occurs creating or getting the RemoteTooManyNeighbors Node.
   */
  Boolean getRemoteTooManyNeighbors() throws UaException;

  /**
   * Set the local value of the RemoteTooManyNeighbors Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the RemoteTooManyNeighbors Node.
   * @throws UaException if an error occurs creating or getting the RemoteTooManyNeighbors Node.
   */
  void setRemoteTooManyNeighbors(Boolean value) throws UaException;

  /**
   * Read the value of the RemoteTooManyNeighbors Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readRemoteTooManyNeighbors() throws UaException;

  /**
   * Write a new value for the RemoteTooManyNeighbors Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeRemoteTooManyNeighbors(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readRemoteTooManyNeighbors}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readRemoteTooManyNeighborsAsync();

  /**
   * An asynchronous implementation of {@link #writeRemoteTooManyNeighbors}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeRemoteTooManyNeighborsAsync(Boolean value);

  /**
   * Get the RemoteTooManyNeighbors {@link BaseDataVariableType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RemoteTooManyNeighbors {@link BaseDataVariableType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getRemoteTooManyNeighborsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRemoteTooManyNeighborsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getRemoteTooManyNeighborsNodeAsync();

  /**
   * Get the local value of the ManagementAddress Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ManagementAddress Node.
   * @throws UaException if an error occurs creating or getting the ManagementAddress Node.
   */
  LldpManagementAddressType[] getManagementAddress() throws UaException;

  /**
   * Set the local value of the ManagementAddress Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ManagementAddress Node.
   * @throws UaException if an error occurs creating or getting the ManagementAddress Node.
   */
  void setManagementAddress(LldpManagementAddressType[] value) throws UaException;

  /**
   * Read the value of the ManagementAddress Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LldpManagementAddressType[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LldpManagementAddressType[] readManagementAddress() throws UaException;

  /**
   * Write a new value for the ManagementAddress Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link LldpManagementAddressType[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeManagementAddress(LldpManagementAddressType[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readManagementAddress}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LldpManagementAddressType[]> readManagementAddressAsync();

  /**
   * An asynchronous implementation of {@link #writeManagementAddress}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeManagementAddressAsync(LldpManagementAddressType[] value);

  /**
   * Get the ManagementAddress {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ManagementAddress {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getManagementAddressNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getManagementAddressNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getManagementAddressNodeAsync();

  /**
   * Get the local value of the RemoteUnknownTlv Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the RemoteUnknownTlv Node.
   * @throws UaException if an error occurs creating or getting the RemoteUnknownTlv Node.
   */
  LldpTlvType[] getRemoteUnknownTlv() throws UaException;

  /**
   * Set the local value of the RemoteUnknownTlv Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the RemoteUnknownTlv Node.
   * @throws UaException if an error occurs creating or getting the RemoteUnknownTlv Node.
   */
  void setRemoteUnknownTlv(LldpTlvType[] value) throws UaException;

  /**
   * Read the value of the RemoteUnknownTlv Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LldpTlvType[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LldpTlvType[] readRemoteUnknownTlv() throws UaException;

  /**
   * Write a new value for the RemoteUnknownTlv Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link LldpTlvType[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeRemoteUnknownTlv(LldpTlvType[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readRemoteUnknownTlv}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LldpTlvType[]> readRemoteUnknownTlvAsync();

  /**
   * An asynchronous implementation of {@link #writeRemoteUnknownTlv}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeRemoteUnknownTlvAsync(LldpTlvType[] value);

  /**
   * Get the RemoteUnknownTlv {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RemoteUnknownTlv {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getRemoteUnknownTlvNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRemoteUnknownTlvNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getRemoteUnknownTlvNodeAsync();
}
