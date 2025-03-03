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
import org.eclipse.milo.opcua.stack.core.types.structured.LinearConversionDataType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.2/#6.4.2.4">https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.2/#6.4.2.4</a>
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

  /**
   * Get the local value of the LinearConversion Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LinearConversion Node.
   * @throws UaException if an error occurs creating or getting the LinearConversion Node.
   */
  LinearConversionDataType getLinearConversion() throws UaException;

  /**
   * Set the local value of the LinearConversion Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LinearConversion Node.
   * @throws UaException if an error occurs creating or getting the LinearConversion Node.
   */
  void setLinearConversion(LinearConversionDataType value) throws UaException;

  /**
   * Read the value of the LinearConversion Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LinearConversionDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LinearConversionDataType readLinearConversion() throws UaException;

  /**
   * Write a new value for the LinearConversion Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link LinearConversionDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLinearConversion(LinearConversionDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLinearConversion}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LinearConversionDataType> readLinearConversionAsync();

  /**
   * An asynchronous implementation of {@link #writeLinearConversion}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLinearConversionAsync(LinearConversionDataType value);

  /**
   * Get the LinearConversion {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LinearConversion {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getLinearConversionNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLinearConversionNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getLinearConversionNodeAsync();

  /**
   * Get the local value of the MathMLConversion Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MathMLConversion Node.
   * @throws UaException if an error occurs creating or getting the MathMLConversion Node.
   */
  String getMathMlConversion() throws UaException;

  /**
   * Set the local value of the MathMLConversion Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MathMLConversion Node.
   * @throws UaException if an error occurs creating or getting the MathMLConversion Node.
   */
  void setMathMlConversion(String value) throws UaException;

  /**
   * Read the value of the MathMLConversion Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readMathMlConversion() throws UaException;

  /**
   * Write a new value for the MathMLConversion Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMathMlConversion(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMathMlConversion}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readMathMlConversionAsync();

  /**
   * An asynchronous implementation of {@link #writeMathMlConversion}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMathMlConversionAsync(String value);

  /**
   * Get the MathMLConversion {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MathMLConversion {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMathMlConversionNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMathMlConversionNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMathMlConversionNodeAsync();

  /**
   * Get the local value of the MathMLInverseConversion Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MathMLInverseConversion Node.
   * @throws UaException if an error occurs creating or getting the MathMLInverseConversion Node.
   */
  String getMathMlInverseConversion() throws UaException;

  /**
   * Set the local value of the MathMLInverseConversion Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MathMLInverseConversion Node.
   * @throws UaException if an error occurs creating or getting the MathMLInverseConversion Node.
   */
  void setMathMlInverseConversion(String value) throws UaException;

  /**
   * Read the value of the MathMLInverseConversion Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readMathMlInverseConversion() throws UaException;

  /**
   * Write a new value for the MathMLInverseConversion Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMathMlInverseConversion(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMathMlInverseConversion}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readMathMlInverseConversionAsync();

  /**
   * An asynchronous implementation of {@link #writeMathMlInverseConversion}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMathMlInverseConversionAsync(String value);

  /**
   * Get the MathMLInverseConversion {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MathMLInverseConversion {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMathMlInverseConversionNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMathMlInverseConversionNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMathMlInverseConversionNodeAsync();
}
