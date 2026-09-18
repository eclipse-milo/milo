package org.eclipse.milo.opcua.sdk.client.model.objects;

import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;

/**
 * Client API for the AlarmGroupType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.3">Model
 *     documentation</a>
 */
public interface AlarmGroupType extends FolderType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 16405L);
}
