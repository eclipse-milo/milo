package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the CertificateGroupFolderType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.3">Model
 *     documentation</a>
 */
public interface CertificateGroupFolderType extends FolderType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 13813L);

  /**
   * Returns the mandatory DefaultApplicationGroup child, a CertificateGroupType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.1">CertificateGroupType
   *     documentation</a>
   */
  CertificateGroupTypeNode getDefaultApplicationGroupNode();

  /**
   * Returns the optional DefaultHttpsGroup child, a CertificateGroupType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.1">CertificateGroupType
   *     documentation</a>
   */
  @Nullable CertificateGroupTypeNode getDefaultHttpsGroupNode();

  /**
   * Returns the optional DefaultUserTokenGroup child, a CertificateGroupType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.1">CertificateGroupType
   *     documentation</a>
   */
  @Nullable CertificateGroupTypeNode getDefaultUserTokenGroupNode();
}
