package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the DialogConditionType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.2">Model
 *     documentation</a>
 */
public interface DialogConditionType extends ConditionType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2830L);

  /**
   * Returns the mandatory CancelResponse child, a PropertyType with DataType Int32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getCancelResponseNode();

  /**
   * Returns the Value of the CancelResponse child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Integer getCancelResponse();

  /**
   * Sets the Value of the CancelResponse child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCancelResponse(@Nullable Integer value);

  /**
   * Returns the mandatory DefaultResponse child, a PropertyType with DataType Int32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getDefaultResponseNode();

  /**
   * Returns the Value of the DefaultResponse child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Integer getDefaultResponse();

  /**
   * Sets the Value of the DefaultResponse child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDefaultResponse(@Nullable Integer value);

  /**
   * Returns the mandatory DialogState child, a TwoStateVariableType with DataType LocalizedText.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  TwoStateVariableTypeNode getDialogStateNode();

  /**
   * Returns the Value of the DialogState child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getDialogState();

  /**
   * Sets the Value of the DialogState child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDialogState(@Nullable LocalizedText value);

  /**
   * Returns the mandatory LastResponse child, a PropertyType with DataType Int32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getLastResponseNode();

  /**
   * Returns the Value of the LastResponse child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Integer getLastResponse();

  /**
   * Sets the Value of the LastResponse child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLastResponse(@Nullable Integer value);

  /**
   * Returns the mandatory OkResponse child, a PropertyType with DataType Int32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getOkResponseNode();

  /**
   * Returns the Value of the OkResponse child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Integer getOkResponse();

  /**
   * Sets the Value of the OkResponse child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setOkResponse(@Nullable Integer value);

  /**
   * Returns the mandatory Prompt child, a PropertyType with DataType LocalizedText.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getPromptNode();

  /**
   * Returns the Value of the Prompt child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getPrompt();

  /**
   * Sets the Value of the Prompt child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPrompt(@Nullable LocalizedText value);

  /**
   * Returns the mandatory ResponseOptionSet child, a PropertyType with DataType LocalizedText.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getResponseOptionSetNode();

  /**
   * Returns the Value of the ResponseOptionSet child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  LocalizedText @Nullable [] getResponseOptionSet();

  /**
   * Sets the Value of the ResponseOptionSet child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setResponseOptionSet(LocalizedText @Nullable [] value);

  /**
   * Returns the mandatory Respond Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.3">Model
   *     documentation</a>
   */
  UaMethodNode getRespondMethodNode();

  /**
   * Sets this instance's Respond handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setRespondHandler(@Nullable RespondHandler handler);

  /**
   * Returns the optional Respond2 Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.4">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRespond2MethodNode();

  /**
   * Sets this instance's Respond2 handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setRespond2Handler(@Nullable Respond2Handler handler);

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
   * Handles calls to the Respond Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.3">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface RespondHandler {
    /**
     * Handles a call to the Respond Method.
     *
     * @throws UaException if the call fails.
     */
    void respond(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable Integer selectedResponse)
        throws UaException;
  }

  /**
   * Handles calls to the Respond2 Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.4">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface Respond2Handler {
    /**
     * Handles a call to the Respond2 Method.
     *
     * @throws UaException if the call fails.
     */
    void respond2(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable Integer selectedResponse,
        @Nullable LocalizedText comment)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods extends ConditionType.Methods {
    /** Handles a call to the Respond Method; see {@link RespondHandler#respond}. */
    default void respond(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable Integer selectedResponse)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the Respond2 Method; see {@link Respond2Handler#respond2}. */
    default void respond2(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable Integer selectedResponse,
        @Nullable LocalizedText comment)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
