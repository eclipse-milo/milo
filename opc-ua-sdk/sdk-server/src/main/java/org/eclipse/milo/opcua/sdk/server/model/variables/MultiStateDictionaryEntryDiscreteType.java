package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;

/**
 * Server API for the MultiStateDictionaryEntryDiscreteType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part19/7.2">Model
 *     documentation</a>
 */
public interface MultiStateDictionaryEntryDiscreteType
    extends MultiStateDictionaryEntryDiscreteBaseType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 19084L);

  /**
   * Returns the mandatory ValueAsDictionaryEntries child, a PropertyType with DataType NodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getValueAsDictionaryEntriesNode();
}
