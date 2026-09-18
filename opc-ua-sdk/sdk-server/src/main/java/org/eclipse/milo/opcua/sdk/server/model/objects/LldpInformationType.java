package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the LldpInformationType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.3">Model
 *     documentation</a>
 */
public interface LldpInformationType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 18973L);

  /**
   * Returns the mandatory LocalSystemData child, a LldpLocalSystemType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.4">LldpLocalSystemType
   *     documentation</a>
   */
  LldpLocalSystemTypeNode getLocalSystemDataNode();

  /**
   * Returns the mandatory Ports child, a FolderType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.6">FolderType
   *     documentation</a>
   */
  FolderTypeNode getPortsNode();

  /**
   * Returns the optional RemoteStatistics child, a LldpRemoteStatisticsType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.4">LldpRemoteStatisticsType
   *     documentation</a>
   */
  @Nullable LldpRemoteStatisticsTypeNode getRemoteStatisticsNode();
}
