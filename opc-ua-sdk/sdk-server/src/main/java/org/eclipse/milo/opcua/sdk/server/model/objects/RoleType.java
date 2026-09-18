package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointType;
import org.eclipse.milo.opcua.stack.core.types.structured.IdentityMappingRuleType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the RoleType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.1">Model
 *     documentation</a>
 */
public interface RoleType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15620L);

  /**
   * Returns the optional Applications child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getApplicationsNode();

  /**
   * Returns the Value of the Applications child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String @Nullable [] getApplications();

  /**
   * Sets the Value of the Applications child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setApplications(@Nullable String @Nullable [] value);

  /**
   * Returns the optional ApplicationsExclude child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getApplicationsExcludeNode();

  /**
   * Returns the Value of the ApplicationsExclude child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getApplicationsExclude();

  /**
   * Sets the Value of the ApplicationsExclude child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setApplicationsExclude(@Nullable Boolean value);

  /**
   * Returns the optional CustomConfiguration child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getCustomConfigurationNode();

  /**
   * Returns the Value of the CustomConfiguration child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getCustomConfiguration();

  /**
   * Sets the Value of the CustomConfiguration child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCustomConfiguration(@Nullable Boolean value);

  /**
   * Returns the optional Endpoints child, a PropertyType with DataType EndpointType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getEndpointsNode();

  /**
   * Returns the Value of the Endpoints child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable EndpointType @Nullable [] getEndpoints();

  /**
   * Sets the Value of the Endpoints child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setEndpoints(@Nullable EndpointType @Nullable [] value);

  /**
   * Returns the optional EndpointsExclude child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getEndpointsExcludeNode();

  /**
   * Returns the Value of the EndpointsExclude child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getEndpointsExclude();

  /**
   * Sets the Value of the EndpointsExclude child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setEndpointsExclude(@Nullable Boolean value);

  /**
   * Returns the mandatory Identities child, a PropertyType with DataType IdentityMappingRuleType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getIdentitiesNode();

  /**
   * Returns the Value of the Identities child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable IdentityMappingRuleType @Nullable [] getIdentities();

  /**
   * Sets the Value of the Identities child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setIdentities(@Nullable IdentityMappingRuleType @Nullable [] value);

  /**
   * Returns the optional AddApplication Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.7">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddApplicationMethodNode();

  /**
   * Sets this instance's AddApplication handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setAddApplicationHandler(@Nullable AddApplicationHandler handler);

  /**
   * Returns the optional AddEndpoint Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.9">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddEndpointMethodNode();

  /**
   * Sets this instance's AddEndpoint handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setAddEndpointHandler(@Nullable AddEndpointHandler handler);

  /**
   * Returns the optional AddIdentity Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.5">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddIdentityMethodNode();

  /**
   * Sets this instance's AddIdentity handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setAddIdentityHandler(@Nullable AddIdentityHandler handler);

  /**
   * Returns the optional RemoveApplication Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.8">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveApplicationMethodNode();

  /**
   * Sets this instance's RemoveApplication handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setRemoveApplicationHandler(@Nullable RemoveApplicationHandler handler);

  /**
   * Returns the optional RemoveEndpoint Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.10">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveEndpointMethodNode();

  /**
   * Sets this instance's RemoveEndpoint handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setRemoveEndpointHandler(@Nullable RemoveEndpointHandler handler);

  /**
   * Returns the optional RemoveIdentity Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.6">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveIdentityMethodNode();

  /**
   * Sets this instance's RemoveIdentity handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setRemoveIdentityHandler(@Nullable RemoveIdentityHandler handler);

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
   * Handles calls to the AddApplication Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.7">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface AddApplicationHandler {
    /**
     * Handles a call to the AddApplication Method.
     *
     * @throws UaException if the call fails.
     */
    void addApplication(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable String applicationUri)
        throws UaException;
  }

  /**
   * Handles calls to the AddEndpoint Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.9">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface AddEndpointHandler {
    /**
     * Handles a call to the AddEndpoint Method.
     *
     * @throws UaException if the call fails.
     */
    void addEndpoint(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable EndpointType endpoint)
        throws UaException;
  }

  /**
   * Handles calls to the AddIdentity Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.5">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface AddIdentityHandler {
    /**
     * Handles a call to the AddIdentity Method.
     *
     * @throws UaException if the call fails.
     */
    void addIdentity(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable IdentityMappingRuleType rule)
        throws UaException;
  }

  /**
   * Handles calls to the RemoveApplication Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.8">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface RemoveApplicationHandler {
    /**
     * Handles a call to the RemoveApplication Method.
     *
     * @throws UaException if the call fails.
     */
    void removeApplication(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable String applicationUri)
        throws UaException;
  }

  /**
   * Handles calls to the RemoveEndpoint Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.10">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface RemoveEndpointHandler {
    /**
     * Handles a call to the RemoveEndpoint Method.
     *
     * @throws UaException if the call fails.
     */
    void removeEndpoint(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable EndpointType endpoint)
        throws UaException;
  }

  /**
   * Handles calls to the RemoveIdentity Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.6">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface RemoveIdentityHandler {
    /**
     * Handles a call to the RemoveIdentity Method.
     *
     * @throws UaException if the call fails.
     */
    void removeIdentity(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable IdentityMappingRuleType rule)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /**
     * Handles a call to the AddApplication Method; see {@link
     * AddApplicationHandler#addApplication}.
     */
    default void addApplication(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable String applicationUri)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the AddEndpoint Method; see {@link AddEndpointHandler#addEndpoint}. */
    default void addEndpoint(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable EndpointType endpoint)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the AddIdentity Method; see {@link AddIdentityHandler#addIdentity}. */
    default void addIdentity(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable IdentityMappingRuleType rule)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the RemoveApplication Method; see {@link
     * RemoveApplicationHandler#removeApplication}.
     */
    default void removeApplication(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable String applicationUri)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the RemoveEndpoint Method; see {@link
     * RemoveEndpointHandler#removeEndpoint}.
     */
    default void removeEndpoint(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable EndpointType endpoint)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the RemoveIdentity Method; see {@link
     * RemoveIdentityHandler#removeIdentity}.
     */
    default void removeIdentity(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable IdentityMappingRuleType rule)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
