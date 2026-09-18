package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.ConditionVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the ConditionType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.2">Model
 *     documentation</a>
 */
public interface ConditionType extends BaseEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2782L);

  /**
   * Returns the mandatory BranchId child, a PropertyType with DataType NodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getBranchIdNode();

  /**
   * Returns the Value of the BranchId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable NodeId getBranchId();

  /**
   * Sets the Value of the BranchId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setBranchId(@Nullable NodeId value);

  /**
   * Returns the mandatory ClientUserId child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getClientUserIdNode();

  /**
   * Returns the Value of the ClientUserId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getClientUserId();

  /**
   * Sets the Value of the ClientUserId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setClientUserId(@Nullable String value);

  /**
   * Returns the mandatory Comment child, a ConditionVariableType with DataType LocalizedText.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.3">ConditionVariableType
   *     documentation</a>
   */
  ConditionVariableTypeNode getCommentNode();

  /**
   * Returns the Value of the Comment child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getComment();

  /**
   * Sets the Value of the Comment child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setComment(@Nullable LocalizedText value);

  /**
   * Returns the mandatory ConditionClassId child, a PropertyType with DataType NodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getConditionClassIdNode();

  /**
   * Returns the mandatory ConditionClassName child, a PropertyType with DataType LocalizedText.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getConditionClassNameNode();

  /**
   * Returns the mandatory ConditionName child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getConditionNameNode();

  /**
   * Returns the Value of the ConditionName child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getConditionName();

  /**
   * Sets the Value of the ConditionName child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setConditionName(@Nullable String value);

  /**
   * Returns the mandatory EnabledState child, a TwoStateVariableType with DataType LocalizedText.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  TwoStateVariableTypeNode getEnabledStateNode();

  /**
   * Returns the Value of the EnabledState child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getEnabledState();

  /**
   * Sets the Value of the EnabledState child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setEnabledState(@Nullable LocalizedText value);

  /**
   * Returns the mandatory LastSeverity child, a ConditionVariableType with DataType UInt16.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.3">ConditionVariableType
   *     documentation</a>
   */
  ConditionVariableTypeNode getLastSeverityNode();

  /**
   * Returns the Value of the LastSeverity child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UShort getLastSeverity();

  /**
   * Sets the Value of the LastSeverity child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLastSeverity(@Nullable UShort value);

  /**
   * Returns the mandatory Quality child, a ConditionVariableType with DataType StatusCode.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.3">ConditionVariableType
   *     documentation</a>
   */
  ConditionVariableTypeNode getQualityNode();

  /**
   * Returns the Value of the Quality child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable StatusCode getQuality();

  /**
   * Sets the Value of the Quality child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setQuality(@Nullable StatusCode value);

  /**
   * Returns the mandatory Retain child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getRetainNode();

  /**
   * Returns the Value of the Retain child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getRetain();

  /**
   * Sets the Value of the Retain child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setRetain(@Nullable Boolean value);

  /**
   * Returns the mandatory AddComment Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.6">Model
   *     documentation</a>
   */
  UaMethodNode getAddCommentMethodNode();

  /**
   * Sets this instance's AddComment handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setAddCommentHandler(@Nullable AddCommentHandler handler);

  /**
   * Returns the optional ConditionRefresh Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.7">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getConditionRefreshMethodNode();

  /**
   * Returns the optional ConditionRefresh2 Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.8">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getConditionRefresh2MethodNode();

  /**
   * Returns the mandatory Disable Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.4">Model
   *     documentation</a>
   */
  UaMethodNode getDisableMethodNode();

  /**
   * Sets this instance's Disable handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setDisableHandler(@Nullable DisableHandler handler);

  /**
   * Returns the mandatory Enable Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.5">Model
   *     documentation</a>
   */
  UaMethodNode getEnableMethodNode();

  /**
   * Sets this instance's Enable handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setEnableHandler(@Nullable EnableHandler handler);

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
   * Handles calls to the AddComment Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.6">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface AddCommentHandler {
    /**
     * Handles a call to the AddComment Method.
     *
     * @param eventId the identifier for the event to comment.
     * @param comment the comment to add to the condition.
     * @throws UaException if the call fails.
     */
    void addComment(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable ByteString eventId,
        @Nullable LocalizedText comment)
        throws UaException;
  }

  /**
   * Handles calls to the Disable Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.4">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface DisableHandler {
    /**
     * Handles a call to the Disable Method.
     *
     * @throws UaException if the call fails.
     */
    void disable(AbstractMethodInvocationHandler.InvocationContext context) throws UaException;
  }

  /**
   * Handles calls to the Enable Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.5">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface EnableHandler {
    /**
     * Handles a call to the Enable Method.
     *
     * @throws UaException if the call fails.
     */
    void enable(AbstractMethodInvocationHandler.InvocationContext context) throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /** Handles a call to the AddComment Method; see {@link AddCommentHandler#addComment}. */
    default void addComment(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable ByteString eventId,
        @Nullable LocalizedText comment)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the Disable Method; see {@link DisableHandler#disable}. */
    default void disable(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the Enable Method; see {@link EnableHandler#enable}. */
    default void enable(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
