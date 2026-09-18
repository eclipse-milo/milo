package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the OffNormalAlarmType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.24/#5.8.24.2">Model
 *     documentation</a>
 */
public interface OffNormalAlarmType extends DiscreteAlarmType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 10637L);

  QualifiedProperty<NodeId> NormalState_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "NormalState",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          -1,
          NodeId.class);

  /**
   * Resolves the mandatory NormalState child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getNormalStateNode() throws UaException;

  /** Asynchronous form of {@link #getNormalStateNode()}. */
  CompletableFuture<? extends PropertyType> getNormalStateNodeAsync();

  /**
   * Reads the Value of the NormalState child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NodeId readNormalState() throws UaException;

  /**
   * Writes the Value of the NormalState child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeNormalState(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readNormalState()}. */
  CompletableFuture<? extends @Nullable NodeId> readNormalStateAsync();

  /** Asynchronous form of {@link #writeNormalState}; completes with the operation status. */
  CompletableFuture<StatusCode> writeNormalStateAsync(@Nullable NodeId value);
}
