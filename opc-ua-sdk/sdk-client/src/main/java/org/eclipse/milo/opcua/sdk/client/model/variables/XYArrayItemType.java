package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.AxisInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the XYArrayItemType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.4/#5.3.4.3">Model
 *     documentation</a>
 */
public interface XYArrayItemType extends ArrayItemType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 12038L);

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
  @Nullable XVType @Nullable [] readXYArrayItemValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeXYArrayItemValue(@Nullable XVType @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readXYArrayItemValue()}. */
  CompletableFuture<? extends @Nullable XVType @Nullable []> readXYArrayItemValueAsync();

  /** Asynchronous form of {@link #writeXYArrayItemValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeXYArrayItemValueAsync(@Nullable XVType @Nullable [] value);
}
