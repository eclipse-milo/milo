package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the ProvisionableDeviceType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.3">Model
 *     documentation</a>
 */
public interface ProvisionableDeviceType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 26871L);

  /**
   * Returns the mandatory IsSingleton child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getIsSingletonNode();

  /**
   * Returns the Value of the IsSingleton child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getIsSingleton();

  /**
   * Sets the Value of the IsSingleton child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setIsSingleton(@Nullable Boolean value);

  /**
   * Returns the mandatory RequestTickets Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.4">Model
   *     documentation</a>
   */
  UaMethodNode getRequestTicketsMethodNode();

  /**
   * Sets this instance's RequestTickets handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setRequestTicketsHandler(@Nullable RequestTicketsHandler handler);

  /**
   * Returns the optional SetRegistrarEndpoints Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.5">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getSetRegistrarEndpointsMethodNode();

  /**
   * Sets this instance's SetRegistrarEndpoints handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setSetRegistrarEndpointsHandler(@Nullable SetRegistrarEndpointsHandler handler);

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
   * Handles calls to the RequestTickets Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.4">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface RequestTicketsHandler {
    /**
     * Handles a call to the RequestTickets Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable String @Nullable [] requestTickets(
        AbstractMethodInvocationHandler.InvocationContext context) throws UaException;
  }

  /**
   * Handles calls to the SetRegistrarEndpoints Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.5">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface SetRegistrarEndpointsHandler {
    /**
     * Handles a call to the SetRegistrarEndpoints Method.
     *
     * @throws UaException if the call fails.
     */
    void setRegistrarEndpoints(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable ApplicationDescription @Nullable [] registrars)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /**
     * Handles a call to the RequestTickets Method; see {@link
     * RequestTicketsHandler#requestTickets}.
     */
    default @Nullable String @Nullable [] requestTickets(
        AbstractMethodInvocationHandler.InvocationContext context) throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the SetRegistrarEndpoints Method; see {@link
     * SetRegistrarEndpointsHandler#setRegistrarEndpoints}.
     */
    default void setRegistrarEndpoints(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable ApplicationDescription @Nullable [] registrars)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
