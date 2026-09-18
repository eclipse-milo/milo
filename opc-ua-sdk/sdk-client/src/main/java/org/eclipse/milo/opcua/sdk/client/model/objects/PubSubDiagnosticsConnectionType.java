package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;

/**
 * Client API for the PubSubDiagnosticsConnectionType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.8">Model
 *     documentation</a>
 */
public interface PubSubDiagnosticsConnectionType extends PubSubDiagnosticsType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 19786L);

  /**
   * Resolves the mandatory LiveValues child, a BaseObjectType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.2">BaseObjectType
   *     documentation</a>
   */
  ObjectNode getLiveValuesNode() throws UaException;

  /** Asynchronous form of {@link #getLiveValuesNode()}. */
  CompletableFuture<? extends ObjectNode> getLiveValuesNodeAsync();
}
