package org.eclipse.milo.opcua.sdk.client.model.objects;

import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;

/**
 * Client API for the IrdiDictionaryEntryType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part19/5.3">Model
 *     documentation</a>
 */
public interface IrdiDictionaryEntryType extends DictionaryEntryType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 17598L);
}
