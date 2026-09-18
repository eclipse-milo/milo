package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the IPriorityMappingEntryType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.15">Model
 *     documentation</a>
 */
public interface IPriorityMappingEntryType extends BaseInterfaceType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 24205L);

  /**
   * Resolves the mandatory MappingUri child, a BaseDataVariableType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getMappingUriNode() throws UaException;

  /** Asynchronous form of {@link #getMappingUriNode()}. */
  CompletableFuture<? extends VariableNode> getMappingUriNodeAsync();

  /**
   * Reads the Value of the MappingUri child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readMappingUri() throws UaException;

  /**
   * Writes the Value of the MappingUri child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMappingUri(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readMappingUri()}. */
  CompletableFuture<? extends @Nullable String> readMappingUriAsync();

  /** Asynchronous form of {@link #writeMappingUri}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMappingUriAsync(@Nullable String value);

  /**
   * Resolves the mandatory PriorityLabel child, a BaseDataVariableType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getPriorityLabelNode() throws UaException;

  /** Asynchronous form of {@link #getPriorityLabelNode()}. */
  CompletableFuture<? extends VariableNode> getPriorityLabelNodeAsync();

  /**
   * Reads the Value of the PriorityLabel child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readPriorityLabel() throws UaException;

  /**
   * Writes the Value of the PriorityLabel child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePriorityLabel(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readPriorityLabel()}. */
  CompletableFuture<? extends @Nullable String> readPriorityLabelAsync();

  /** Asynchronous form of {@link #writePriorityLabel}; completes with the operation status. */
  CompletableFuture<StatusCode> writePriorityLabelAsync(@Nullable String value);

  /**
   * Resolves the optional PriorityValue_PCP child, a BaseDataVariableType with DataType Byte.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable VariableNode getPriorityValue_PCPNode() throws UaException;

  /** Asynchronous form of {@link #getPriorityValue_PCPNode()}. */
  CompletableFuture<? extends @Nullable VariableNode> getPriorityValue_PCPNodeAsync();

  /**
   * Reads the Value of the PriorityValue_PCP child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UByte readPriorityValue_PCP() throws UaException;

  /**
   * Writes the Value of the PriorityValue_PCP child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePriorityValue_PCP(@Nullable UByte value) throws UaException;

  /** Asynchronous form of {@link #readPriorityValue_PCP()}. */
  CompletableFuture<? extends @Nullable UByte> readPriorityValue_PCPAsync();

  /** Asynchronous form of {@link #writePriorityValue_PCP}; completes with the operation status. */
  CompletableFuture<StatusCode> writePriorityValue_PCPAsync(@Nullable UByte value);

  /**
   * Resolves the optional PriorityValue_DSCP child, a BaseDataVariableType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable VariableNode getPriorityValue_DSCPNode() throws UaException;

  /** Asynchronous form of {@link #getPriorityValue_DSCPNode()}. */
  CompletableFuture<? extends @Nullable VariableNode> getPriorityValue_DSCPNodeAsync();

  /**
   * Reads the Value of the PriorityValue_DSCP child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readPriorityValue_DSCP() throws UaException;

  /**
   * Writes the Value of the PriorityValue_DSCP child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePriorityValue_DSCP(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readPriorityValue_DSCP()}. */
  CompletableFuture<? extends @Nullable UInteger> readPriorityValue_DSCPAsync();

  /** Asynchronous form of {@link #writePriorityValue_DSCP}; completes with the operation status. */
  CompletableFuture<StatusCode> writePriorityValue_DSCPAsync(@Nullable UInteger value);
}
