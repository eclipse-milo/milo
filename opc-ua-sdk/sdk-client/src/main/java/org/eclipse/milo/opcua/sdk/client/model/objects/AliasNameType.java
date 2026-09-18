package org.eclipse.milo.opcua.sdk.client.model.objects;

import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;

/**
 * Client API for the AliasNameType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part17/6.2">Model
 *     documentation</a>
 */
public interface AliasNameType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 23455L);
}
