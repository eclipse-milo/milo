package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;

/**
 * Server API for the RefreshRequiredEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.11.4">Model
 *     documentation</a>
 */
public interface RefreshRequiredEventType extends SystemEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2789L);
}
