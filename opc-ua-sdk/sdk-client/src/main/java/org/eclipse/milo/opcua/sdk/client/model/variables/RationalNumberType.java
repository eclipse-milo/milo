package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.RationalNumber;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the RationalNumberType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.20">Model
 *     documentation</a>
 */
public interface RationalNumberType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 17709L);

  /**
   * Resolves the mandatory Denominator child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getDenominatorNode() throws UaException;

  /** Asynchronous form of {@link #getDenominatorNode()}. */
  CompletableFuture<? extends VariableNode> getDenominatorNodeAsync();

  /**
   * Reads the Value of the Denominator child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readDenominator() throws UaException;

  /**
   * Writes the Value of the Denominator child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDenominator(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readDenominator()}. */
  CompletableFuture<? extends @Nullable UInteger> readDenominatorAsync();

  /** Asynchronous form of {@link #writeDenominator}; completes with the operation status. */
  CompletableFuture<StatusCode> writeDenominatorAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory Numerator child, a BaseDataVariableType with DataType Int32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getNumeratorNode() throws UaException;

  /** Asynchronous form of {@link #getNumeratorNode()}. */
  CompletableFuture<? extends VariableNode> getNumeratorNodeAsync();

  /**
   * Reads the Value of the Numerator child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Integer readNumerator() throws UaException;

  /**
   * Writes the Value of the Numerator child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeNumerator(@Nullable Integer value) throws UaException;

  /** Asynchronous form of {@link #readNumerator()}. */
  CompletableFuture<? extends @Nullable Integer> readNumeratorAsync();

  /** Asynchronous form of {@link #writeNumerator}; completes with the operation status. */
  CompletableFuture<StatusCode> writeNumeratorAsync(@Nullable Integer value);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable RationalNumber readTypedValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTypedValue(@Nullable RationalNumber value) throws UaException;

  /** Asynchronous form of {@link #readTypedValue()}. */
  CompletableFuture<? extends @Nullable RationalNumber> readTypedValueAsync();

  /** Asynchronous form of {@link #writeTypedValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTypedValueAsync(@Nullable RationalNumber value);
}
