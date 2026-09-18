package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubConfigurationTypeCloseAndUpdate;
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubConfigurationTypeReserveIds;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the PubSubConfigurationType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.1">Model
 *     documentation</a>
 */
public interface PubSubConfigurationType extends FileType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 25482L);

  /**
   * Returns the mandatory CloseAndUpdate Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.6">Model
   *     documentation</a>
   */
  UaMethodNode getCloseAndUpdateMethodNode();

  /**
   * Sets this instance's CloseAndUpdate handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setCloseAndUpdateHandler(@Nullable CloseAndUpdateHandler handler);

  /**
   * Returns the mandatory ReserveIds Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.5">Model
   *     documentation</a>
   */
  UaMethodNode getReserveIdsMethodNode();

  /**
   * Sets this instance's ReserveIds handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setReserveIdsHandler(@Nullable ReserveIdsHandler handler);

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
   * Handles calls to the CloseAndUpdate Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.6">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface CloseAndUpdateHandler {
    /**
     * Handles a call to the CloseAndUpdate Method.
     *
     * @throws UaException if the call fails.
     */
    PubSubConfigurationTypeCloseAndUpdate.Outputs closeAndUpdate(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable UInteger fileHandle,
        @Nullable Boolean requireCompleteUpdate,
        @Nullable PubSubConfigurationRefDataType @Nullable [] configurationReferences)
        throws UaException;
  }

  /**
   * Handles calls to the ReserveIds Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.5">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface ReserveIdsHandler {
    /**
     * Handles a call to the ReserveIds Method.
     *
     * @throws UaException if the call fails.
     */
    PubSubConfigurationTypeReserveIds.Outputs reserveIds(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String transportProfileUri,
        @Nullable UShort numReqWriterGroupIds,
        @Nullable UShort numReqDataSetWriterIds)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods extends FileType.Methods {
    /**
     * Handles a call to the CloseAndUpdate Method; see {@link
     * CloseAndUpdateHandler#closeAndUpdate}.
     */
    default PubSubConfigurationTypeCloseAndUpdate.Outputs closeAndUpdate(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable UInteger fileHandle,
        @Nullable Boolean requireCompleteUpdate,
        @Nullable PubSubConfigurationRefDataType @Nullable [] configurationReferences)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the ReserveIds Method; see {@link ReserveIdsHandler#reserveIds}. */
    default PubSubConfigurationTypeReserveIds.Outputs reserveIds(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String transportProfileUri,
        @Nullable UShort numReqWriterGroupIds,
        @Nullable UShort numReqDataSetWriterIds)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
