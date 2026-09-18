package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.SemanticChangeStructureDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the SemanticChangeEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.33">Model
 *     documentation</a>
 */
public interface SemanticChangeEventType extends BaseEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2738L);

  /**
   * Returns the mandatory Changes child, a PropertyType with DataType
   * SemanticChangeStructureDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getChangesNode();

  /**
   * Returns the Value of the Changes child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable SemanticChangeStructureDataType @Nullable [] getChanges();

  /**
   * Sets the Value of the Changes child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setChanges(@Nullable SemanticChangeStructureDataType @Nullable [] value);
}
