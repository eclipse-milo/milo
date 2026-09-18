package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link CertificateGroupFolderType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.3">Model
 *     documentation</a>
 */
public class CertificateGroupFolderTypeNode extends FolderTypeNode
    implements CertificateGroupFolderType {
  public CertificateGroupFolderTypeNode(
      OpcUaClient client,
      NodeId nodeId,
      NodeClass nodeClass,
      QualifiedName browseName,
      LocalizedText displayName,
      @Nullable LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType @Nullable [] rolePermissions,
      RolePermissionType @Nullable [] userRolePermissions,
      @Nullable AccessRestrictionType accessRestrictions,
      UByte eventNotifier) {
    super(
        client,
        nodeId,
        nodeClass,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions,
        eventNotifier);
  }

  @Override
  public @Nullable CertificateGroupTypeNode getDefaultHttpsGroupNode() throws UaException {
    return ClientNodeSupport.await(getDefaultHttpsGroupNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable CertificateGroupTypeNode>
      getDefaultHttpsGroupNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DefaultHttpsGroup",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        CertificateGroupTypeNode.class)));
  }

  @Override
  public @Nullable CertificateGroupTypeNode getDefaultUserTokenGroupNode() throws UaException {
    return ClientNodeSupport.await(getDefaultUserTokenGroupNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable CertificateGroupTypeNode>
      getDefaultUserTokenGroupNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DefaultUserTokenGroup",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        CertificateGroupTypeNode.class)));
  }

  @Override
  public CertificateGroupTypeNode getDefaultApplicationGroupNode() throws UaException {
    return ClientNodeSupport.await(getDefaultApplicationGroupNodeAsync());
  }

  @Override
  public CompletableFuture<? extends CertificateGroupTypeNode>
      getDefaultApplicationGroupNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DefaultApplicationGroup",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        CertificateGroupTypeNode.class)));
  }
}
