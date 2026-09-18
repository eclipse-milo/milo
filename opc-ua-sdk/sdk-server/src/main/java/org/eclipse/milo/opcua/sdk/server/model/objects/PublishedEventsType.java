package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the PublishedEventsType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.1">Model
 *     documentation</a>
 */
public interface PublishedEventsType extends PublishedDataSetType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 14572L);

  /**
   * Returns the mandatory EventNotifier child, a PropertyType with DataType NodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getEventNotifier_Node();

  /**
   * Returns the Value of the EventNotifier child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable NodeId getEventNotifier_();

  /**
   * Sets the Value of the EventNotifier child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setEventNotifier_(@Nullable NodeId value);

  /**
   * Returns the mandatory Filter child, a PropertyType with DataType ContentFilter.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getFilterNode();

  /**
   * Returns the Value of the Filter child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ContentFilter getFilter();

  /**
   * Sets the Value of the Filter child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setFilter(@Nullable ContentFilter value);

  /**
   * Returns the mandatory SelectedFields child, a PropertyType with DataType
   * SimpleAttributeOperand.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getSelectedFieldsNode();

  /**
   * Returns the Value of the SelectedFields child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable SimpleAttributeOperand @Nullable [] getSelectedFields();

  /**
   * Sets the Value of the SelectedFields child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSelectedFields(@Nullable SimpleAttributeOperand @Nullable [] value);

  /**
   * Returns the optional ModifyFieldSelection Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.2">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getModifyFieldSelectionMethodNode();

  /**
   * Sets this instance's ModifyFieldSelection handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setModifyFieldSelectionHandler(@Nullable ModifyFieldSelectionHandler handler);

  /**
   * Sets this instance's Method handlers, including inherited handlers, from one implementation;
   * null clears them and restores Method-node fallback. Absent optional Methods are skipped.
   * Changes are applied in order; a failure does not roll back earlier changes.
   *
   * @throws UaRuntimeException if a mandatory Method is absent, or a Method is ambiguous or
   *     incompatible.
   */
  void setMethods(@Nullable Methods methods);

  /**
   * Handles calls to the ModifyFieldSelection Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.2">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface ModifyFieldSelectionHandler {
    /**
     * Handles a call to the ModifyFieldSelection Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable ConfigurationVersionDataType modifyFieldSelection(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable ConfigurationVersionDataType configurationVersion,
        @Nullable String @Nullable [] fieldNameAliases,
        Boolean @Nullable [] promotedFields,
        @Nullable SimpleAttributeOperand @Nullable [] selectedFields)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /**
     * Handles a call to the ModifyFieldSelection Method; see {@link
     * ModifyFieldSelectionHandler#modifyFieldSelection}.
     */
    default @Nullable ConfigurationVersionDataType modifyFieldSelection(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable ConfigurationVersionDataType configurationVersion,
        @Nullable String @Nullable [] fieldNameAliases,
        Boolean @Nullable [] promotedFields,
        @Nullable SimpleAttributeOperand @Nullable [] selectedFields)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
