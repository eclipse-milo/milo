package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.AxisInformation;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the YArrayItemType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.4/#5.3.4.2">Model
 *     documentation</a>
 */
public interface YArrayItemType extends ArrayItemType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 12029L);

  QualifiedProperty<AxisInformation> XAxisDefinition_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "XAxisDefinition",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12079L),
          -1,
          AxisInformation.class);

  /**
   * Resolves the mandatory XAxisDefinition child, a PropertyType with DataType AxisInformation.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getXAxisDefinitionNode() throws UaException;

  /** Asynchronous form of {@link #getXAxisDefinitionNode()}. */
  CompletableFuture<? extends PropertyType> getXAxisDefinitionNodeAsync();

  /**
   * Reads the Value of the XAxisDefinition child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable AxisInformation readXAxisDefinition() throws UaException;

  /**
   * Writes the Value of the XAxisDefinition child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeXAxisDefinition(@Nullable AxisInformation value) throws UaException;

  /** Asynchronous form of {@link #readXAxisDefinition()}. */
  CompletableFuture<? extends @Nullable AxisInformation> readXAxisDefinitionAsync();

  /** Asynchronous form of {@link #writeXAxisDefinition}; completes with the operation status. */
  CompletableFuture<StatusCode> writeXAxisDefinitionAsync(@Nullable AxisInformation value);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Variant @Nullable [] readYArrayItemValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeYArrayItemValue(@Nullable Variant @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readYArrayItemValue()}. */
  CompletableFuture<? extends @Nullable Variant @Nullable []> readYArrayItemValueAsync();

  /** Asynchronous form of {@link #writeYArrayItemValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeYArrayItemValueAsync(@Nullable Variant @Nullable [] value);
}
