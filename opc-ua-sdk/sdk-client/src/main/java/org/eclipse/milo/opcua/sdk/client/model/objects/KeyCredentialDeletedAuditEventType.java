package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;

/**
 * Client API for the KeyCredentialDeletedAuditEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.10">Model
 *     documentation</a>
 */
public interface KeyCredentialDeletedAuditEventType extends KeyCredentialAuditEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 18047L);

  /**
   * Resolves the mandatory ResourceUri child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getResourceUriNode() throws UaException;

  /** Asynchronous form of {@link #getResourceUriNode()}. */
  CompletableFuture<? extends PropertyType> getResourceUriNodeAsync();
}
