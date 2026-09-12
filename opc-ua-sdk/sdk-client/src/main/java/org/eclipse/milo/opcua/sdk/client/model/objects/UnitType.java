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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.2/#6.4.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.2/#6.4.2.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
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

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getSymbol() throws UaException;

  /** Sets the existing node's local value. */
  void setSymbol(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readSymbol() throws UaException;

  /** Writes the value remotely. */
  void writeSymbol(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readSymbolAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSymbolAsync(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSymbolNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getSymbolNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getUnitSystem() throws UaException;

  /** Sets the existing node's local value. */
  void setUnitSystem(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readUnitSystem() throws UaException;

  /** Writes the value remotely. */
  void writeUnitSystem(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readUnitSystemAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeUnitSystemAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getUnitSystemNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getUnitSystemNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getDiscipline() throws UaException;

  /** Sets the existing node's local value. */
  void setDiscipline(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readDiscipline() throws UaException;

  /** Writes the value remotely. */
  void writeDiscipline(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readDisciplineAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDisciplineAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getDisciplineNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getDisciplineNodeAsync();
}
