package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NegotiationStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link IIeeeAutoNegotiationStatusType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.3">Model
 *     documentation</a>
 */
public class IIeeeAutoNegotiationStatusTypeNode extends BaseInterfaceTypeNode
    implements IIeeeAutoNegotiationStatusType {
  public IIeeeAutoNegotiationStatusTypeNode(
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
  public UaVariableNode getNegotiationStatusNode() throws UaException {
    return ClientNodeSupport.await(getNegotiationStatusNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getNegotiationStatusNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "NegotiationStatus",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable NegotiationStatus readNegotiationStatus() throws UaException {
    return ClientNodeSupport.await(readNegotiationStatusAsync());
  }

  @Override
  public void writeNegotiationStatus(@Nullable NegotiationStatus value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeNegotiationStatusAsync(value)),
        "http://opcfoundation.org/UA/}NegotiationStatus");
  }

  @Override
  public CompletableFuture<? extends @Nullable NegotiationStatus> readNegotiationStatusAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getNegotiationStatusNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}NegotiationStatus",
                            true,
                            NegotiationStatus.class,
                            -1,
                            NegotiationStatus::from)),
                v -> CompletableFuture.completedFuture((@Nullable NegotiationStatus) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeNegotiationStatusAsync(
      @Nullable NegotiationStatus value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getNegotiationStatusNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}NegotiationStatus",
                        value,
                        NegotiationStatus.class,
                        -1,
                        NegotiationStatus::from)));
  }
}
