package org.eclipse.milo.opcua.sdk.client.model.variables;

import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;

/**
 * Client API for the DiscreteItemType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.3/#5.3.3.1">Model
 *     documentation</a>
 */
public interface DiscreteItemType extends DataItemType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2372L);
}
