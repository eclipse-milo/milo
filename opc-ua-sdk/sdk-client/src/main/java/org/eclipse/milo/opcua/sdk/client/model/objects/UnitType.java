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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.2/#6.4.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.2/#6.4.2.2</a>
 */
public interface UnitType extends BaseObjectType {
  QualifiedProperty<LocalizedText> SYMBOL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Symbol",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          -1,
          LocalizedText.class);

  QualifiedProperty<String> UNIT_SYSTEM =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UnitSystem",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> DISCIPLINE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Discipline",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /**
   * Get the local value of the Symbol Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Symbol Node.
   * @throws UaException if an error occurs creating or getting the Symbol Node.
   */
  LocalizedText getSymbol() throws UaException;

  /**
   * Set the local value of the Symbol Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Symbol Node.
   * @throws UaException if an error occurs creating or getting the Symbol Node.
   */
  void setSymbol(LocalizedText value) throws UaException;

  /**
   * Read the value of the Symbol Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readSymbol() throws UaException;

  /**
   * Write a new value for the Symbol Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSymbol(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSymbol}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readSymbolAsync();

  /**
   * An asynchronous implementation of {@link #writeSymbol}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSymbolAsync(LocalizedText value);

  /**
   * Get the Symbol {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Symbol {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSymbolNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSymbolNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSymbolNodeAsync();

  /**
   * Get the local value of the UnitSystem Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the UnitSystem Node.
   * @throws UaException if an error occurs creating or getting the UnitSystem Node.
   */
  String getUnitSystem() throws UaException;

  /**
   * Set the local value of the UnitSystem Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the UnitSystem Node.
   * @throws UaException if an error occurs creating or getting the UnitSystem Node.
   */
  void setUnitSystem(String value) throws UaException;

  /**
   * Read the value of the UnitSystem Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readUnitSystem() throws UaException;

  /**
   * Write a new value for the UnitSystem Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeUnitSystem(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readUnitSystem}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readUnitSystemAsync();

  /**
   * An asynchronous implementation of {@link #writeUnitSystem}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeUnitSystemAsync(String value);

  /**
   * Get the UnitSystem {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the UnitSystem {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getUnitSystemNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getUnitSystemNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getUnitSystemNodeAsync();

  /**
   * Get the local value of the Discipline Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Discipline Node.
   * @throws UaException if an error occurs creating or getting the Discipline Node.
   */
  String getDiscipline() throws UaException;

  /**
   * Set the local value of the Discipline Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Discipline Node.
   * @throws UaException if an error occurs creating or getting the Discipline Node.
   */
  void setDiscipline(String value) throws UaException;

  /**
   * Read the value of the Discipline Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readDiscipline() throws UaException;

  /**
   * Write a new value for the Discipline Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDiscipline(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDiscipline}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readDisciplineAsync();

  /**
   * An asynchronous implementation of {@link #writeDiscipline}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDisciplineAsync(String value);

  /**
   * Get the Discipline {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Discipline {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDisciplineNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDisciplineNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDisciplineNodeAsync();
}
