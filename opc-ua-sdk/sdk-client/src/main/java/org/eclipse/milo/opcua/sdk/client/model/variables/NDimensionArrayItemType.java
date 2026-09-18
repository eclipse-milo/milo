package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.AxisInformation;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the NDimensionArrayItemType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.4/#5.3.4.6">Model
 *     documentation</a>
 */
public interface NDimensionArrayItemType extends ArrayItemType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 12068L);

  QualifiedProperty<AxisInformation[]> AxisDefinition_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "AxisDefinition",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12079L),
          1,
          AxisInformation[].class);

  /**
   * Resolves the mandatory AxisDefinition child, a PropertyType with DataType AxisInformation.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getAxisDefinitionNode() throws UaException;

  /** Asynchronous form of {@link #getAxisDefinitionNode()}. */
  CompletableFuture<? extends PropertyType> getAxisDefinitionNodeAsync();

  /**
   * Reads the Value of the AxisDefinition child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable AxisInformation @Nullable [] readAxisDefinition() throws UaException;

  /**
   * Writes the Value of the AxisDefinition child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeAxisDefinition(@Nullable AxisInformation @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readAxisDefinition()}. */
  CompletableFuture<? extends @Nullable AxisInformation @Nullable []> readAxisDefinitionAsync();

  /** Asynchronous form of {@link #writeAxisDefinition}; completes with the operation status. */
  CompletableFuture<StatusCode> writeAxisDefinitionAsync(
      @Nullable AxisInformation @Nullable [] value);
}
