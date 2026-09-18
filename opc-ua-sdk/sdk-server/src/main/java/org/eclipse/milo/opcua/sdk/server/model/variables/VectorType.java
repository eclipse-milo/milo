package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.Vector;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the VectorType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.21">Model
 *     documentation</a>
 */
public interface VectorType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 17714L);

  /**
   * Returns the optional VectorUnit child, a PropertyType with DataType EUInformation.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getVectorUnitNode();

  /**
   * Returns the Value of the VectorUnit child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable EUInformation getVectorUnit();

  /**
   * Sets the Value of the VectorUnit child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setVectorUnit(@Nullable EUInformation value);

  /**
   * Returns this node's Value.
   *
   * @throws UaRuntimeException if the Value does not convert.
   */
  @Nullable Vector getTypedValue();

  /**
   * Sets this node's Value.
   *
   * @throws UaRuntimeException if the value does not convert.
   */
  void setTypedValue(@Nullable Vector value);
}
