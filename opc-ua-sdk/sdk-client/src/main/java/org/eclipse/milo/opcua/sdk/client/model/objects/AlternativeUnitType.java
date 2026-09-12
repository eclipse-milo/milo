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
import org.eclipse.milo.opcua.stack.core.types.structured.LinearConversionDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.2/#6.4.2.4">https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.2/#6.4.2.4</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AlternativeUnitType extends UnitType {
  QualifiedProperty<LinearConversionDataType> LINEAR_CONVERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LinearConversion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=32435"),
          -1,
          LinearConversionDataType.class);

  QualifiedProperty<String> MATH_ML_CONVERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MathMLConversion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> MATH_ML_INVERSE_CONVERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MathMLInverseConversion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /** Gets the existing node's local value. */
  @Nullable LinearConversionDataType getLinearConversion() throws UaException;

  /** Sets the existing node's local value. */
  void setLinearConversion(@Nullable LinearConversionDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LinearConversionDataType readLinearConversion() throws UaException;

  /** Writes the value remotely. */
  void writeLinearConversion(@Nullable LinearConversionDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LinearConversionDataType> readLinearConversionAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLinearConversionAsync(
      @Nullable LinearConversionDataType value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getLinearConversionNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getLinearConversionNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getMathMlConversion() throws UaException;

  /** Sets the existing node's local value. */
  void setMathMlConversion(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readMathMlConversion() throws UaException;

  /** Writes the value remotely. */
  void writeMathMlConversion(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readMathMlConversionAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMathMlConversionAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMathMlConversionNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMathMlConversionNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getMathMlInverseConversion() throws UaException;

  /** Sets the existing node's local value. */
  void setMathMlInverseConversion(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readMathMlInverseConversion() throws UaException;

  /** Writes the value remotely. */
  void writeMathMlInverseConversion(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readMathMlInverseConversionAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMathMlInverseConversionAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMathMlInverseConversionNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMathMlInverseConversionNodeAsync();
}
