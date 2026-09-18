package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the LldpInformationType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.3">Model
 *     documentation</a>
 */
public interface LldpInformationType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 18973L);

  /**
   * Resolves the mandatory LocalSystemData child, a LldpLocalSystemType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.4">LldpLocalSystemType
   *     documentation</a>
   */
  LldpLocalSystemType getLocalSystemDataNode() throws UaException;

  /** Asynchronous form of {@link #getLocalSystemDataNode()}. */
  CompletableFuture<? extends LldpLocalSystemType> getLocalSystemDataNodeAsync();

  /**
   * Resolves the optional RemoteStatistics child, a LldpRemoteStatisticsType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.4">LldpRemoteStatisticsType
   *     documentation</a>
   */
  @Nullable LldpRemoteStatisticsType getRemoteStatisticsNode() throws UaException;

  /** Asynchronous form of {@link #getRemoteStatisticsNode()}. */
  CompletableFuture<? extends @Nullable LldpRemoteStatisticsType> getRemoteStatisticsNodeAsync();

  /**
   * Resolves the mandatory Ports child, a FolderType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.6">FolderType
   *     documentation</a>
   */
  FolderType getPortsNode() throws UaException;

  /** Asynchronous form of {@link #getPortsNode()}. */
  CompletableFuture<? extends FolderType> getPortsNodeAsync();
}
