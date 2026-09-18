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
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link IPriorityMappingEntryType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.15">Model
 *     documentation</a>
 */
public class IPriorityMappingEntryTypeNode extends BaseInterfaceTypeNode
    implements IPriorityMappingEntryType {
  public IPriorityMappingEntryTypeNode(
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
  public UaVariableNode getMappingUriNode() throws UaException {
    return ClientNodeSupport.await(getMappingUriNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getMappingUriNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MappingUri",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable String readMappingUri() throws UaException {
    return ClientNodeSupport.await(readMappingUriAsync());
  }

  @Override
  public void writeMappingUri(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMappingUriAsync(value)),
        "http://opcfoundation.org/UA/}MappingUri");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readMappingUriAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMappingUriNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MappingUri",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMappingUriAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMappingUriNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MappingUri",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getPriorityLabelNode() throws UaException {
    return ClientNodeSupport.await(getPriorityLabelNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getPriorityLabelNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "PriorityLabel",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable String readPriorityLabel() throws UaException {
    return ClientNodeSupport.await(readPriorityLabelAsync());
  }

  @Override
  public void writePriorityLabel(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writePriorityLabelAsync(value)),
        "http://opcfoundation.org/UA/}PriorityLabel");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readPriorityLabelAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getPriorityLabelNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}PriorityLabel",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writePriorityLabelAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getPriorityLabelNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}PriorityLabel",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable UaVariableNode getPriorityValue_PCPNode() throws UaException {
    return ClientNodeSupport.await(getPriorityValue_PCPNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable UaVariableNode> getPriorityValue_PCPNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "PriorityValue_PCP",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UByte readPriorityValue_PCP() throws UaException {
    return ClientNodeSupport.await(readPriorityValue_PCPAsync());
  }

  @Override
  public void writePriorityValue_PCP(@Nullable UByte value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writePriorityValue_PCPAsync(value)),
        "http://opcfoundation.org/UA/}PriorityValue_PCP");
  }

  @Override
  public CompletableFuture<? extends @Nullable UByte> readPriorityValue_PCPAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getPriorityValue_PCPNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}PriorityValue_PCP",
                            false,
                            UByte.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UByte) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writePriorityValue_PCPAsync(@Nullable UByte value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getPriorityValue_PCPNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}PriorityValue_PCP",
                        value,
                        UByte.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable UaVariableNode getPriorityValue_DSCPNode() throws UaException {
    return ClientNodeSupport.await(getPriorityValue_DSCPNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable UaVariableNode> getPriorityValue_DSCPNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "PriorityValue_DSCP",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readPriorityValue_DSCP() throws UaException {
    return ClientNodeSupport.await(readPriorityValue_DSCPAsync());
  }

  @Override
  public void writePriorityValue_DSCP(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writePriorityValue_DSCPAsync(value)),
        "http://opcfoundation.org/UA/}PriorityValue_DSCP");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readPriorityValue_DSCPAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getPriorityValue_DSCPNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}PriorityValue_DSCP",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writePriorityValue_DSCPAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getPriorityValue_DSCPNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}PriorityValue_DSCP",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }
}
