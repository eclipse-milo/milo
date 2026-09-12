/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.3">https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.3</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface StateVariableType extends BaseDataVariableType {
  QualifiedProperty<Object> ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Id",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24"),
          -1,
          Object.class);

  QualifiedProperty<QualifiedName> NAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Name",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20"),
          -1,
          QualifiedName.class);

  QualifiedProperty<UInteger> NUMBER =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Number",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<LocalizedText> EFFECTIVE_DISPLAY_NAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EffectiveDisplayName",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          -1,
          LocalizedText.class);

  /** Gets the existing node's local value. */
  @Nullable Object getId() throws UaException;

  /** Sets the existing node's local value. */
  void setId(@Nullable Object value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Object readId() throws UaException;

  /** Writes the value remotely. */
  void writeId(@Nullable Object value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Object> readIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeIdAsync(@Nullable Object value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable QualifiedName getName() throws UaException;

  /** Sets the existing node's local value. */
  void setName(@Nullable QualifiedName value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable QualifiedName readName() throws UaException;

  /** Writes the value remotely. */
  void writeName(@Nullable QualifiedName value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable QualifiedName> readNameAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeNameAsync(@Nullable QualifiedName value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getNameNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getNameNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getNumber() throws UaException;

  /** Sets the existing node's local value. */
  void setNumber(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readNumber() throws UaException;

  /** Writes the value remotely. */
  void writeNumber(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readNumberAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeNumberAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getNumberNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getNumberNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getEffectiveDisplayName() throws UaException;

  /** Sets the existing node's local value. */
  void setEffectiveDisplayName(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readEffectiveDisplayName() throws UaException;

  /** Writes the value remotely. */
  void writeEffectiveDisplayName(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readEffectiveDisplayNameAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEffectiveDisplayNameAsync(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getEffectiveDisplayNameNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getEffectiveDisplayNameNodeAsync();
}
