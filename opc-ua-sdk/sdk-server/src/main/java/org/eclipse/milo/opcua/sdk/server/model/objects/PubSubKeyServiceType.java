package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubKeyServiceTypeGetSecurityKeys;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the PubSubKeyServiceType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.1">Model
 *     documentation</a>
 */
public interface PubSubKeyServiceType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15906L);

  /**
   * Returns the optional KeyPushTargets child, a PubSubKeyPushTargetFolderType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.1">PubSubKeyPushTargetFolderType
   *     documentation</a>
   */
  @Nullable PubSubKeyPushTargetFolderTypeNode getKeyPushTargetsNode();

  /**
   * Returns the optional SecurityGroups child, a SecurityGroupFolderType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.1">SecurityGroupFolderType
   *     documentation</a>
   */
  @Nullable SecurityGroupFolderTypeNode getSecurityGroupsNode();

  /**
   * Returns the optional GetSecurityGroup Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getGetSecurityGroupMethodNode();

  /**
   * Sets this instance's GetSecurityGroup handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setGetSecurityGroupHandler(@Nullable GetSecurityGroupHandler handler);

  /**
   * Returns the optional GetSecurityKeys Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.2">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getGetSecurityKeysMethodNode();

  /**
   * Sets this instance's GetSecurityKeys handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setGetSecurityKeysHandler(@Nullable GetSecurityKeysHandler handler);

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
   * Handles calls to the GetSecurityGroup Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.3">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface GetSecurityGroupHandler {
    /**
     * Handles a call to the GetSecurityGroup Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable NodeId getSecurityGroup(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable String securityGroupId)
        throws UaException;
  }

  /**
   * Handles calls to the GetSecurityKeys Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.2">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface GetSecurityKeysHandler {
    /**
     * Handles a call to the GetSecurityKeys Method.
     *
     * @throws UaException if the call fails.
     */
    PubSubKeyServiceTypeGetSecurityKeys.Outputs getSecurityKeys(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String securityGroupId,
        @Nullable UInteger startingTokenId,
        @Nullable UInteger requestedKeyCount)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /**
     * Handles a call to the GetSecurityGroup Method; see {@link
     * GetSecurityGroupHandler#getSecurityGroup}.
     */
    default @Nullable NodeId getSecurityGroup(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable String securityGroupId)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the GetSecurityKeys Method; see {@link
     * GetSecurityKeysHandler#getSecurityKeys}.
     */
    default PubSubKeyServiceTypeGetSecurityKeys.Outputs getSecurityKeys(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String securityGroupId,
        @Nullable UInteger startingTokenId,
        @Nullable UInteger requestedKeyCount)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
