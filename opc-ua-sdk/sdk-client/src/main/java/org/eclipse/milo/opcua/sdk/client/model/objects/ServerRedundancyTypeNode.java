package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.RedundancySupport;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RedundantServerDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ServerRedundancyType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.7">Model
 *     documentation</a>
 */
public class ServerRedundancyTypeNode extends BaseObjectTypeNode implements ServerRedundancyType {
  public ServerRedundancyTypeNode(
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
  public PropertyTypeNode getRedundancySupportNode() throws UaException {
    return ClientNodeSupport.await(getRedundancySupportNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getRedundancySupportNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "RedundancySupport",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable RedundancySupport readRedundancySupport() throws UaException {
    return ClientNodeSupport.await(readRedundancySupportAsync());
  }

  @Override
  public void writeRedundancySupport(@Nullable RedundancySupport value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeRedundancySupportAsync(value)),
        "http://opcfoundation.org/UA/}RedundancySupport");
  }

  @Override
  public CompletableFuture<? extends @Nullable RedundancySupport> readRedundancySupportAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getRedundancySupportNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}RedundancySupport",
                            true,
                            RedundancySupport.class,
                            -1,
                            RedundancySupport::from)),
                v -> CompletableFuture.completedFuture((@Nullable RedundancySupport) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeRedundancySupportAsync(
      @Nullable RedundancySupport value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getRedundancySupportNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}RedundancySupport",
                        value,
                        RedundancySupport.class,
                        -1,
                        RedundancySupport::from)));
  }

  @Override
  public @Nullable PropertyTypeNode getRedundantServerArrayNode() throws UaException {
    return ClientNodeSupport.await(getRedundantServerArrayNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getRedundantServerArrayNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "RedundantServerArray",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable RedundantServerDataType @Nullable [] readRedundantServerArray()
      throws UaException {
    return ClientNodeSupport.await(readRedundantServerArrayAsync());
  }

  @Override
  public void writeRedundantServerArray(@Nullable RedundantServerDataType @Nullable [] value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeRedundantServerArrayAsync(value)),
        "http://opcfoundation.org/UA/}RedundantServerArray");
  }

  @Override
  public CompletableFuture<? extends @Nullable RedundantServerDataType @Nullable []>
      readRedundantServerArrayAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getRedundantServerArrayNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}RedundantServerArray",
                            false,
                            RedundantServerDataType.class,
                            1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable RedundantServerDataType @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeRedundantServerArrayAsync(
      @Nullable RedundantServerDataType @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getRedundantServerArrayNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}RedundantServerArray",
                        value,
                        RedundantServerDataType.class,
                        1,
                        null)));
  }
}
