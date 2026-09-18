package org.eclipse.milo.opcua.sdk.client.model.objects;

import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;

/**
 * Client API for the SystemEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.28">Model
 *     documentation</a>
 */
public interface SystemEventType extends BaseEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2130L);
}
