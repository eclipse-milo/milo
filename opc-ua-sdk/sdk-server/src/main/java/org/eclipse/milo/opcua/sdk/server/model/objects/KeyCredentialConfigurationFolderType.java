package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the KeyCredentialConfigurationFolderType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.2">Model
 *     documentation</a>
 */
public interface KeyCredentialConfigurationFolderType extends FolderType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 17496L);

  /**
   * Returns the optional CreateCredential Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getCreateCredentialMethodNode();

  /**
   * Sets this instance's CreateCredential handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setCreateCredentialHandler(@Nullable CreateCredentialHandler handler);

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
   * Handles calls to the CreateCredential Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.3">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface CreateCredentialHandler {
    /**
     * Handles a call to the CreateCredential Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable NodeId createCredential(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String name,
        @Nullable String resourceUri,
        @Nullable String profileUri,
        @Nullable String @Nullable [] endpointUrls)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /**
     * Handles a call to the CreateCredential Method; see {@link
     * CreateCredentialHandler#createCredential}.
     */
    default @Nullable NodeId createCredential(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String name,
        @Nullable String resourceUri,
        @Nullable String profileUri,
        @Nullable String @Nullable [] endpointUrls)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
