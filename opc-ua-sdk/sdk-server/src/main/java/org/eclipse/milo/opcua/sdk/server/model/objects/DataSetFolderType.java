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
import org.eclipse.milo.opcua.sdk.core.model.methods.DataSetFolderTypeAddPublishedDataItemsOutputs;
import org.eclipse.milo.opcua.sdk.core.model.methods.DataSetFolderTypeAddPublishedDataItemsTemplateOutputs;
import org.eclipse.milo.opcua.sdk.core.model.methods.DataSetFolderTypeAddPublishedEventsOutputs;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldFlags;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedVariableDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface DataSetFolderType extends FolderType {
  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.2
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getAddPublishedDataItemsMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.2 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddPublishedDataItems(
      MethodBindings bindings, AddPublishedDataItemsHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.2 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddPublishedDataItemsDetailed(
      MethodBindings bindings, AddPublishedDataItemsDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getAddPublishedEventsMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.3 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddPublishedEvents(MethodBindings bindings, AddPublishedEventsHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.3 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddPublishedEventsDetailed(
      MethodBindings bindings, AddPublishedEventsDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getAddPublishedDataItemsTemplateMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.4 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddPublishedDataItemsTemplate(
      MethodBindings bindings, AddPublishedDataItemsTemplateHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.4 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddPublishedDataItemsTemplateDetailed(
      MethodBindings bindings, AddPublishedDataItemsTemplateDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getAddPublishedEventsTemplateMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.5 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddPublishedEventsTemplate(
      MethodBindings bindings, AddPublishedEventsTemplateHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.5 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddPublishedEventsTemplateDetailed(
      MethodBindings bindings, AddPublishedEventsTemplateDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.6
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getRemovePublishedDataSetMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.6 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemovePublishedDataSet(
      MethodBindings bindings, RemovePublishedDataSetHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.6 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemovePublishedDataSetDetailed(
      MethodBindings bindings, RemovePublishedDataSetDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.7
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getAddDataSetFolderMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.7 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddDataSetFolder(MethodBindings bindings, AddDataSetFolderHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.7 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddDataSetFolderDetailed(
      MethodBindings bindings, AddDataSetFolderDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.8
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getRemoveDataSetFolderMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.8 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveDataSetFolder(MethodBindings bindings, RemoveDataSetFolderHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.8 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveDataSetFolderDetailed(
      MethodBindings bindings, RemoveDataSetFolderDetailedHandler handler) throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.2 */
  @FunctionalInterface
  interface AddPublishedDataItemsHandler {
    /**
     * @return a non-null container holding all output values
     * @throws UaException for an operation failure
     */
    DataSetFolderTypeAddPublishedDataItemsOutputs invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String name,
        @Nullable String @Nullable [] fieldNameAliases,
        @Nullable DataSetFieldFlags @Nullable [] fieldFlags,
        @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.2 */
  @FunctionalInterface
  interface AddPublishedDataItemsDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<DataSetFolderTypeAddPublishedDataItemsOutputs> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String name,
        @Nullable String @Nullable [] fieldNameAliases,
        @Nullable DataSetFieldFlags @Nullable [] fieldFlags,
        @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.3 */
  @FunctionalInterface
  interface AddPublishedEventsHandler {
    /**
     * @return a non-null container holding all output values
     * @throws UaException for an operation failure
     */
    DataSetFolderTypeAddPublishedEventsOutputs invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String name,
        @Nullable NodeId eventNotifier,
        @Nullable String @Nullable [] fieldNameAliases,
        @Nullable DataSetFieldFlags @Nullable [] fieldFlags,
        @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
        @Nullable ContentFilter filter)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.3 */
  @FunctionalInterface
  interface AddPublishedEventsDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<DataSetFolderTypeAddPublishedEventsOutputs> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String name,
        @Nullable NodeId eventNotifier,
        @Nullable String @Nullable [] fieldNameAliases,
        @Nullable DataSetFieldFlags @Nullable [] fieldFlags,
        @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
        @Nullable ContentFilter filter)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.4 */
  @FunctionalInterface
  interface AddPublishedDataItemsTemplateHandler {
    /**
     * @return a non-null container holding all output values
     * @throws UaException for an operation failure
     */
    DataSetFolderTypeAddPublishedDataItemsTemplateOutputs invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String name,
        @Nullable DataSetMetaDataType dataSetMetaData,
        @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.4 */
  @FunctionalInterface
  interface AddPublishedDataItemsTemplateDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<DataSetFolderTypeAddPublishedDataItemsTemplateOutputs> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String name,
        @Nullable DataSetMetaDataType dataSetMetaData,
        @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.5 */
  @FunctionalInterface
  interface AddPublishedEventsTemplateHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable NodeId invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String name,
        @Nullable DataSetMetaDataType dataSetMetaData,
        @Nullable NodeId eventNotifier,
        @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
        @Nullable ContentFilter filter)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.5 */
  @FunctionalInterface
  interface AddPublishedEventsTemplateDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable NodeId> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String name,
        @Nullable DataSetMetaDataType dataSetMetaData,
        @Nullable NodeId eventNotifier,
        @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
        @Nullable ContentFilter filter)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.6 */
  @FunctionalInterface
  interface RemovePublishedDataSetHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId dataSetNodeId)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.6 */
  @FunctionalInterface
  interface RemovePublishedDataSetDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId dataSetNodeId)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.7 */
  @FunctionalInterface
  interface AddDataSetFolderHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable NodeId invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String name)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.7 */
  @FunctionalInterface
  interface AddDataSetFolderDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable NodeId> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String name)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.8 */
  @FunctionalInterface
  interface RemoveDataSetFolderHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId dataSetFolderNodeId)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.8 */
  @FunctionalInterface
  interface RemoveDataSetFolderDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId dataSetFolderNodeId)
        throws UaException;
  }
}
