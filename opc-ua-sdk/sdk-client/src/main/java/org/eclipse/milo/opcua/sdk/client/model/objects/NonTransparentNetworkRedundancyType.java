package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.NetworkGroupDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the NonTransparentNetworkRedundancyType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.10">Model
 *     documentation</a>
 */
public interface NonTransparentNetworkRedundancyType extends NonTransparentRedundancyType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 11945L);

  QualifiedProperty<NetworkGroupDataType[]> ServerNetworkGroups_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ServerNetworkGroups",
          ExpandedNodeId.of(Namespaces.OPC_UA, 11944L),
          1,
          NetworkGroupDataType[].class);

  /**
   * Resolves the mandatory ServerNetworkGroups child, a PropertyType with DataType
   * NetworkGroupDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getServerNetworkGroupsNode() throws UaException;

  /** Asynchronous form of {@link #getServerNetworkGroupsNode()}. */
  CompletableFuture<? extends PropertyType> getServerNetworkGroupsNodeAsync();

  /**
   * Reads the Value of the ServerNetworkGroups child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NetworkGroupDataType @Nullable [] readServerNetworkGroups() throws UaException;

  /**
   * Writes the Value of the ServerNetworkGroups child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeServerNetworkGroups(@Nullable NetworkGroupDataType @Nullable [] value)
      throws UaException;

  /** Asynchronous form of {@link #readServerNetworkGroups()}. */
  CompletableFuture<? extends @Nullable NetworkGroupDataType @Nullable []>
      readServerNetworkGroupsAsync();

  /**
   * Asynchronous form of {@link #writeServerNetworkGroups}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeServerNetworkGroupsAsync(
      @Nullable NetworkGroupDataType @Nullable [] value);
}
