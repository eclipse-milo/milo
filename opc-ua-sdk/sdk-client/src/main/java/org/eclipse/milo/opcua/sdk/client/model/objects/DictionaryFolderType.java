package org.eclipse.milo.opcua.sdk.client.model.objects;

import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;

/**
 * Client API for the DictionaryFolderType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part19/5.2">Model
 *     documentation</a>
 */
public interface DictionaryFolderType extends FolderType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 17591L);
}
