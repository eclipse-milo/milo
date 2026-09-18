package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.SemanticChangeStructureDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the SemanticChangeEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.33">Model
 *     documentation</a>
 */
public interface SemanticChangeEventType extends BaseEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2738L);

  QualifiedProperty<SemanticChangeStructureDataType[]> Changes_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Changes",
          ExpandedNodeId.of(Namespaces.OPC_UA, 897L),
          1,
          SemanticChangeStructureDataType[].class);

  /**
   * Resolves the mandatory Changes child, a PropertyType with DataType
   * SemanticChangeStructureDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getChangesNode() throws UaException;

  /** Asynchronous form of {@link #getChangesNode()}. */
  CompletableFuture<? extends PropertyType> getChangesNodeAsync();

  /**
   * Reads the Value of the Changes child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable SemanticChangeStructureDataType @Nullable [] readChanges() throws UaException;

  /**
   * Writes the Value of the Changes child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeChanges(@Nullable SemanticChangeStructureDataType @Nullable [] value)
      throws UaException;

  /** Asynchronous form of {@link #readChanges()}. */
  CompletableFuture<? extends @Nullable SemanticChangeStructureDataType @Nullable []>
      readChangesAsync();

  /** Asynchronous form of {@link #writeChanges}; completes with the operation status. */
  CompletableFuture<StatusCode> writeChangesAsync(
      @Nullable SemanticChangeStructureDataType @Nullable [] value);
}
