package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescriptionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceListEntryDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/** Server API for the ReferenceDescriptionVariableType VariableType. */
public interface ReferenceDescriptionVariableType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 32657L);

  /**
   * Returns the optional ReferenceRefinement child, a PropertyType with DataType
   * ReferenceListEntryDataType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getReferenceRefinementNode();

  /**
   * Returns the Value of the ReferenceRefinement child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ReferenceListEntryDataType @Nullable [] getReferenceRefinement();

  /**
   * Sets the Value of the ReferenceRefinement child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setReferenceRefinement(@Nullable ReferenceListEntryDataType @Nullable [] value);

  /**
   * Returns this node's Value.
   *
   * @throws UaRuntimeException if the Value does not convert.
   */
  @Nullable ReferenceDescriptionDataType getTypedValue();

  /**
   * Sets this node's Value.
   *
   * @throws UaRuntimeException if the value does not convert.
   */
  void setTypedValue(@Nullable ReferenceDescriptionDataType value);
}
