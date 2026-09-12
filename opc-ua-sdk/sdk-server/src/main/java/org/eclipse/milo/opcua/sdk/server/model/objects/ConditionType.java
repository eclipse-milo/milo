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
import org.eclipse.milo.opcua.sdk.server.model.variables.ConditionVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ConditionType extends BaseEventType {
  QualifiedProperty<NodeId> CONDITION_CLASS_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConditionClassId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<LocalizedText> CONDITION_CLASS_NAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConditionClassName",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          -1,
          LocalizedText.class);

  QualifiedProperty<NodeId[]> CONDITION_SUB_CLASS_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConditionSubClassId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          1,
          NodeId[].class);

  QualifiedProperty<LocalizedText[]> CONDITION_SUB_CLASS_NAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConditionSubClassName",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          1,
          LocalizedText[].class);

  QualifiedProperty<String> CONDITION_NAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConditionName",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<NodeId> BRANCH_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "BranchId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<Boolean> RETAIN =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Retain",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> SUPPORTS_FILTERED_RETAIN =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SupportsFilteredRetain",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<String> CLIENT_USER_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ClientUserId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /** Gets the existing node's local value. */
  @Nullable NodeId getConditionClassId();

  /** Sets the existing node's local value. */
  void setConditionClassId(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getConditionClassIdNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getConditionClassName();

  /** Sets the existing node's local value. */
  void setConditionClassName(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getConditionClassNameNode();

  /** Gets the existing node's local value. */
  @Nullable NodeId @Nullable [] getConditionSubClassId();

  /** Sets the existing node's local value. */
  void setConditionSubClassId(@Nullable NodeId @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getConditionSubClassIdNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText @Nullable [] getConditionSubClassName();

  /** Sets the existing node's local value. */
  void setConditionSubClassName(@Nullable LocalizedText @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getConditionSubClassNameNode();

  /** Gets the existing node's local value. */
  @Nullable String getConditionName();

  /** Sets the existing node's local value. */
  void setConditionName(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getConditionNameNode();

  /** Gets the existing node's local value. */
  @Nullable NodeId getBranchId();

  /** Sets the existing node's local value. */
  void setBranchId(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getBranchIdNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getRetain();

  /** Sets the existing node's local value. */
  void setRetain(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getRetainNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getSupportsFilteredRetain();

  /** Sets the existing node's local value. */
  void setSupportsFilteredRetain(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSupportsFilteredRetainNode();

  /** Gets the existing node's local value. */
  @Nullable String getClientUserId();

  /** Sets the existing node's local value. */
  void setClientUserId(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getClientUserIdNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TwoStateVariableType getEnabledStateNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getEnabledState();

  /** Sets the existing node's local value. */
  void setEnabledState(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  ConditionVariableType getQualityNode();

  /** Gets the existing node's local value. */
  @Nullable StatusCode getQuality();

  /** Sets the existing node's local value. */
  void setQuality(@Nullable StatusCode value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  ConditionVariableType getLastSeverityNode();

  /** Gets the existing node's local value. */
  @Nullable UShort getLastSeverity();

  /** Sets the existing node's local value. */
  void setLastSeverity(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  ConditionVariableType getCommentNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getComment();

  /** Sets the existing node's local value. */
  void setComment(@Nullable LocalizedText value);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.4
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getDisableMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.4 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindDisable(MethodBindings bindings, DisableHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.4 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindDisableDetailed(MethodBindings bindings, DisableDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.5
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getEnableMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.5 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindEnable(MethodBindings bindings, EnableHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.5 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindEnableDetailed(MethodBindings bindings, EnableDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.6
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getAddCommentMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.6 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddComment(MethodBindings bindings, AddCommentHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.6 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddCommentDetailed(MethodBindings bindings, AddCommentDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.7
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getConditionRefreshMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.7 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindConditionRefresh(MethodBindings bindings, ConditionRefreshHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.7 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindConditionRefreshDetailed(
      MethodBindings bindings, ConditionRefreshDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.8
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getConditionRefresh2MethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.8 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindConditionRefresh2(MethodBindings bindings, ConditionRefresh2Handler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.8 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindConditionRefresh2Detailed(
      MethodBindings bindings, ConditionRefresh2DetailedHandler handler) throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.4 */
  @FunctionalInterface
  interface DisableHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.4 */
  @FunctionalInterface
  interface DisableDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.5 */
  @FunctionalInterface
  interface EnableHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.5 */
  @FunctionalInterface
  interface EnableDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.6 */
  @FunctionalInterface
  interface AddCommentHandler {
    /**
     * @param eventId The identifier for the event to comment.
     * @param comment The comment to add to the condition.
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable ByteString eventId,
        @Nullable LocalizedText comment)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.6 */
  @FunctionalInterface
  interface AddCommentDetailedHandler {
    /**
     * @param eventId The identifier for the event to comment.
     * @param comment The comment to add to the condition.
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable ByteString eventId,
        @Nullable LocalizedText comment)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.7 */
  @FunctionalInterface
  interface ConditionRefreshHandler {
    /**
     * @param subscriptionId The identifier for the subscription to refresh.
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger subscriptionId)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.7 */
  @FunctionalInterface
  interface ConditionRefreshDetailedHandler {
    /**
     * @param subscriptionId The identifier for the subscription to refresh.
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger subscriptionId)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.8 */
  @FunctionalInterface
  interface ConditionRefresh2Handler {
    /**
     * @param subscriptionId The identifier for the subscription to refresh.
     * @param monitoredItemId The identifier for the monitored item to refresh.
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger subscriptionId,
        @Nullable UInteger monitoredItemId)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.8 */
  @FunctionalInterface
  interface ConditionRefresh2DetailedHandler {
    /**
     * @param subscriptionId The identifier for the subscription to refresh.
     * @param monitoredItemId The identifier for the monitored item to refresh.
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger subscriptionId,
        @Nullable UInteger monitoredItemId)
        throws UaException;
  }
}
