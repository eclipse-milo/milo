package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.LinearConversionDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AlternativeUnitType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.2/#6.4.2.4">Model
 *     documentation</a>
 */
public interface AlternativeUnitType extends UnitType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 32467L);

  QualifiedProperty<LinearConversionDataType> LinearConversion_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "LinearConversion",
          ExpandedNodeId.of(Namespaces.OPC_UA, 32435L),
          -1,
          LinearConversionDataType.class);

  QualifiedProperty<String> MathMLConversion_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MathMLConversion",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<String> MathMLInverseConversion_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MathMLInverseConversion",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  /**
   * Resolves the optional LinearConversion child, a PropertyType with DataType
   * LinearConversionDataType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getLinearConversionNode() throws UaException;

  /** Asynchronous form of {@link #getLinearConversionNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getLinearConversionNodeAsync();

  /**
   * Reads the Value of the LinearConversion child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LinearConversionDataType readLinearConversion() throws UaException;

  /**
   * Writes the Value of the LinearConversion child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLinearConversion(@Nullable LinearConversionDataType value) throws UaException;

  /** Asynchronous form of {@link #readLinearConversion()}. */
  CompletableFuture<? extends @Nullable LinearConversionDataType> readLinearConversionAsync();

  /** Asynchronous form of {@link #writeLinearConversion}; completes with the operation status. */
  CompletableFuture<StatusCode> writeLinearConversionAsync(
      @Nullable LinearConversionDataType value);

  /**
   * Resolves the optional MathMLConversion child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMathMLConversionNode() throws UaException;

  /** Asynchronous form of {@link #getMathMLConversionNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMathMLConversionNodeAsync();

  /**
   * Reads the Value of the MathMLConversion child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readMathMLConversion() throws UaException;

  /**
   * Writes the Value of the MathMLConversion child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMathMLConversion(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readMathMLConversion()}. */
  CompletableFuture<? extends @Nullable String> readMathMLConversionAsync();

  /** Asynchronous form of {@link #writeMathMLConversion}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMathMLConversionAsync(@Nullable String value);

  /**
   * Resolves the optional MathMLInverseConversion child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMathMLInverseConversionNode() throws UaException;

  /** Asynchronous form of {@link #getMathMLInverseConversionNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMathMLInverseConversionNodeAsync();

  /**
   * Reads the Value of the MathMLInverseConversion child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readMathMLInverseConversion() throws UaException;

  /**
   * Writes the Value of the MathMLInverseConversion child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMathMLInverseConversion(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readMathMLInverseConversion()}. */
  CompletableFuture<? extends @Nullable String> readMathMLInverseConversionAsync();

  /**
   * Asynchronous form of {@link #writeMathMLInverseConversion}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeMathMLInverseConversionAsync(@Nullable String value);
}
