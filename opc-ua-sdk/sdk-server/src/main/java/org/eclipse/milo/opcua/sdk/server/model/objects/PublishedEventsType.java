/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodHandlerResult;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface PublishedEventsType extends PublishedDataSetType {
  QualifiedProperty<NodeId> PUB_SUB_EVENT_NOTIFIER =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EventNotifier",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<SimpleAttributeOperand[]> SELECTED_FIELDS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SelectedFields",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=601"),
          1,
          SimpleAttributeOperand[].class);

  QualifiedProperty<ContentFilter> FILTER =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Filter",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=586"),
          -1,
          ContentFilter.class);

  /** Gets the existing node's local value. */
  @Nullable NodeId getPubSubEventNotifier();

  /** Sets the existing node's local value. */
  void setPubSubEventNotifier(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPubSubEventNotifierNode();

  /** Gets the existing node's local value. */
  @Nullable SimpleAttributeOperand @Nullable [] getSelectedFields();

  /** Sets the existing node's local value. */
  void setSelectedFields(@Nullable SimpleAttributeOperand @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSelectedFieldsNode();

  /** Gets the existing node's local value. */
  @Nullable ContentFilter getFilter();

  /** Sets the existing node's local value. */
  void setFilter(@Nullable ContentFilter value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getFilterNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.2
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getModifyFieldSelectionMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.2 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindModifyFieldSelection(
      MethodBindings bindings, ModifyFieldSelectionHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.2 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindModifyFieldSelectionDetailed(
      MethodBindings bindings, ModifyFieldSelectionDetailedHandler handler) throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.2 */
  @FunctionalInterface
  interface ModifyFieldSelectionHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable ConfigurationVersionDataType invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable ConfigurationVersionDataType configurationVersion,
        @Nullable String @Nullable [] fieldNameAliases,
        @Nullable Boolean @Nullable [] promotedFields,
        @Nullable SimpleAttributeOperand @Nullable [] selectedFields)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.2 */
  @FunctionalInterface
  interface ModifyFieldSelectionDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable ConfigurationVersionDataType> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable ConfigurationVersionDataType configurationVersion,
        @Nullable String @Nullable [] fieldNameAliases,
        @Nullable Boolean @Nullable [] promotedFields,
        @Nullable SimpleAttributeOperand @Nullable [] selectedFields)
        throws UaException;
  }
}
