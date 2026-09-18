package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the SecurityGroupType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.1">Model
 *     documentation</a>
 */
public interface SecurityGroupType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15471L);

  /**
   * Returns the mandatory KeyLifetime child, a PropertyType with DataType Duration.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getKeyLifetimeNode();

  /**
   * Returns the Value of the KeyLifetime child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getKeyLifetime();

  /**
   * Sets the Value of the KeyLifetime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setKeyLifetime(@Nullable Double value);

  /**
   * Returns the mandatory MaxFutureKeyCount child, a PropertyType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getMaxFutureKeyCountNode();

  /**
   * Returns the Value of the MaxFutureKeyCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxFutureKeyCount();

  /**
   * Sets the Value of the MaxFutureKeyCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxFutureKeyCount(@Nullable UInteger value);

  /**
   * Returns the mandatory MaxPastKeyCount child, a PropertyType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getMaxPastKeyCountNode();

  /**
   * Returns the Value of the MaxPastKeyCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxPastKeyCount();

  /**
   * Sets the Value of the MaxPastKeyCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxPastKeyCount(@Nullable UInteger value);

  /**
   * Returns the mandatory SecurityGroupId child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getSecurityGroupIdNode();

  /**
   * Returns the Value of the SecurityGroupId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getSecurityGroupId();

  /**
   * Sets the Value of the SecurityGroupId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSecurityGroupId(@Nullable String value);

  /**
   * Returns the mandatory SecurityPolicyUri child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getSecurityPolicyUriNode();

  /**
   * Returns the Value of the SecurityPolicyUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getSecurityPolicyUri();

  /**
   * Sets the Value of the SecurityPolicyUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSecurityPolicyUri(@Nullable String value);

  /**
   * Returns the optional ForceKeyRotation Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getForceKeyRotationMethodNode();

  /**
   * Sets this instance's ForceKeyRotation handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setForceKeyRotationHandler(@Nullable ForceKeyRotationHandler handler);

  /**
   * Returns the optional InvalidateKeys Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.2">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getInvalidateKeysMethodNode();

  /**
   * Sets this instance's InvalidateKeys handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setInvalidateKeysHandler(@Nullable InvalidateKeysHandler handler);

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
   * Handles calls to the ForceKeyRotation Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.3">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface ForceKeyRotationHandler {
    /**
     * Handles a call to the ForceKeyRotation Method.
     *
     * @throws UaException if the call fails.
     */
    void forceKeyRotation(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException;
  }

  /**
   * Handles calls to the InvalidateKeys Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.2">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface InvalidateKeysHandler {
    /**
     * Handles a call to the InvalidateKeys Method.
     *
     * @throws UaException if the call fails.
     */
    void invalidateKeys(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /**
     * Handles a call to the ForceKeyRotation Method; see {@link
     * ForceKeyRotationHandler#forceKeyRotation}.
     */
    default void forceKeyRotation(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the InvalidateKeys Method; see {@link
     * InvalidateKeysHandler#invalidateKeys}.
     */
    default void invalidateKeys(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
