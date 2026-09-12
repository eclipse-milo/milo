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
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface DialogConditionType extends ConditionType {
  QualifiedProperty<LocalizedText> PROMPT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Prompt",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          -1,
          LocalizedText.class);

  QualifiedProperty<LocalizedText[]> RESPONSE_OPTION_SET =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ResponseOptionSet",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          1,
          LocalizedText[].class);

  QualifiedProperty<Integer> DEFAULT_RESPONSE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DefaultResponse",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=6"),
          -1,
          Integer.class);

  QualifiedProperty<Integer> OK_RESPONSE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "OkResponse",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=6"),
          -1,
          Integer.class);

  QualifiedProperty<Integer> CANCEL_RESPONSE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CancelResponse",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=6"),
          -1,
          Integer.class);

  QualifiedProperty<Integer> LAST_RESPONSE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastResponse",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=6"),
          -1,
          Integer.class);

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getPrompt();

  /** Sets the existing node's local value. */
  void setPrompt(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPromptNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText @Nullable [] getResponseOptionSet();

  /** Sets the existing node's local value. */
  void setResponseOptionSet(@Nullable LocalizedText @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getResponseOptionSetNode();

  /** Gets the existing node's local value. */
  @Nullable Integer getDefaultResponse();

  /** Sets the existing node's local value. */
  void setDefaultResponse(@Nullable Integer value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDefaultResponseNode();

  /** Gets the existing node's local value. */
  @Nullable Integer getOkResponse();

  /** Sets the existing node's local value. */
  void setOkResponse(@Nullable Integer value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getOkResponseNode();

  /** Gets the existing node's local value. */
  @Nullable Integer getCancelResponse();

  /** Sets the existing node's local value. */
  void setCancelResponse(@Nullable Integer value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getCancelResponseNode();

  /** Gets the existing node's local value. */
  @Nullable Integer getLastResponse();

  /** Sets the existing node's local value. */
  void setLastResponse(@Nullable Integer value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getLastResponseNode();

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
  TwoStateVariableType getDialogStateNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getDialogState();

  /** Sets the existing node's local value. */
  void setDialogState(@Nullable LocalizedText value);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.3
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getRespondMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.3 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRespond(MethodBindings bindings, RespondHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.3 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRespondDetailed(MethodBindings bindings, RespondDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getRespond2MethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.4 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRespond2(MethodBindings bindings, Respond2Handler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.4 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRespond2Detailed(MethodBindings bindings, Respond2DetailedHandler handler)
      throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.3 */
  @FunctionalInterface
  interface RespondHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable Integer selectedResponse)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.3 */
  @FunctionalInterface
  interface RespondDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable Integer selectedResponse)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.4 */
  @FunctionalInterface
  interface Respond2Handler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable Integer selectedResponse,
        @Nullable LocalizedText comment)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.4 */
  @FunctionalInterface
  interface Respond2DetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable Integer selectedResponse,
        @Nullable LocalizedText comment)
        throws UaException;
  }
}
