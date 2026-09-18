package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the OrderedListType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.10">Model
 *     documentation</a>
 */
public interface OrderedListType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 23518L);

  QualifiedProperty<String> NodeVersion_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "NodeVersion",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  /**
   * Resolves the optional NodeVersion child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getNodeVersionNode() throws UaException;

  /** Asynchronous form of {@link #getNodeVersionNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getNodeVersionNodeAsync();

  /**
   * Reads the Value of the NodeVersion child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readNodeVersion() throws UaException;

  /**
   * Writes the Value of the NodeVersion child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeNodeVersion(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readNodeVersion()}. */
  CompletableFuture<? extends @Nullable String> readNodeVersionAsync();

  /** Asynchronous form of {@link #writeNodeVersion}; completes with the operation status. */
  CompletableFuture<StatusCode> writeNodeVersionAsync(@Nullable String value);
}
