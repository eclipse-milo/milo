package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the IOrderedObjectType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.11">Model
 *     documentation</a>
 */
public interface IOrderedObjectType extends BaseInterfaceType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 23513L);

  QualifiedProperty<Variant> NumberInList_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "NumberInList",
          ExpandedNodeId.of(Namespaces.OPC_UA, 26L),
          -1,
          Variant.class);

  /**
   * Resolves the mandatory NumberInList child, a PropertyType with DataType Number.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getNumberInListNode() throws UaException;

  /** Asynchronous form of {@link #getNumberInListNode()}. */
  CompletableFuture<? extends PropertyType> getNumberInListNodeAsync();

  /**
   * Reads the Value of the NumberInList child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Variant readNumberInList() throws UaException;

  /**
   * Writes the Value of the NumberInList child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeNumberInList(@Nullable Variant value) throws UaException;

  /** Asynchronous form of {@link #readNumberInList()}. */
  CompletableFuture<? extends @Nullable Variant> readNumberInListAsync();

  /** Asynchronous form of {@link #writeNumberInList}; completes with the operation status. */
  CompletableFuture<StatusCode> writeNumberInListAsync(@Nullable Variant value);
}
