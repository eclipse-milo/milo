package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;

/**
 * Server API for the TrainingConditionClassType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.9.8">Model
 *     documentation</a>
 */
public interface TrainingConditionClassType extends BaseConditionClassType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 17220L);
}
