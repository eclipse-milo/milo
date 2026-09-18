package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the CertificateGroupFolderType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.3">Model
 *     documentation</a>
 */
public interface CertificateGroupFolderType extends FolderType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 13813L);

  /**
   * Resolves the optional DefaultHttpsGroup child, a CertificateGroupType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.1">CertificateGroupType
   *     documentation</a>
   */
  @Nullable CertificateGroupType getDefaultHttpsGroupNode() throws UaException;

  /** Asynchronous form of {@link #getDefaultHttpsGroupNode()}. */
  CompletableFuture<? extends @Nullable CertificateGroupType> getDefaultHttpsGroupNodeAsync();

  /**
   * Resolves the optional DefaultUserTokenGroup child, a CertificateGroupType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.1">CertificateGroupType
   *     documentation</a>
   */
  @Nullable CertificateGroupType getDefaultUserTokenGroupNode() throws UaException;

  /** Asynchronous form of {@link #getDefaultUserTokenGroupNode()}. */
  CompletableFuture<? extends @Nullable CertificateGroupType> getDefaultUserTokenGroupNodeAsync();

  /**
   * Resolves the mandatory DefaultApplicationGroup child, a CertificateGroupType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.1">CertificateGroupType
   *     documentation</a>
   */
  CertificateGroupType getDefaultApplicationGroupNode() throws UaException;

  /** Asynchronous form of {@link #getDefaultApplicationGroupNode()}. */
  CompletableFuture<? extends CertificateGroupType> getDefaultApplicationGroupNodeAsync();
}
