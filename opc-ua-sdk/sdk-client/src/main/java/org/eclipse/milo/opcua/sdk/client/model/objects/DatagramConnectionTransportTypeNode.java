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
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.QosDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link DatagramConnectionTransportType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.1/#9.3.1.1">Model
 *     documentation</a>
 */
public class DatagramConnectionTransportTypeNode extends ConnectionTransportTypeNode
    implements DatagramConnectionTransportType {
  public DatagramConnectionTransportTypeNode(
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
  public @Nullable PropertyTypeNode getDatagramQosNode() throws UaException {
    return ClientNodeSupport.await(getDatagramQosNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getDatagramQosNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DatagramQos",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable QosDataType @Nullable [] readDatagramQos() throws UaException {
    return ClientNodeSupport.await(readDatagramQosAsync());
  }

  @Override
  public void writeDatagramQos(@Nullable QosDataType @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDatagramQosAsync(value)),
        "http://opcfoundation.org/UA/}DatagramQos");
  }

  @Override
  public CompletableFuture<? extends @Nullable QosDataType @Nullable []> readDatagramQosAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDatagramQosNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DatagramQos",
                            false,
                            QosDataType.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable QosDataType @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDatagramQosAsync(
      @Nullable QosDataType @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDatagramQosNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DatagramQos",
                        value,
                        QosDataType.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getQosCategoryNode() throws UaException {
    return ClientNodeSupport.await(getQosCategoryNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getQosCategoryNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "QosCategory",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readQosCategory() throws UaException {
    return ClientNodeSupport.await(readQosCategoryAsync());
  }

  @Override
  public void writeQosCategory(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeQosCategoryAsync(value)),
        "http://opcfoundation.org/UA/}QosCategory");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readQosCategoryAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getQosCategoryNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}QosCategory",
                            false,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeQosCategoryAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getQosCategoryNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}QosCategory",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public NetworkAddressTypeNode getDiscoveryAddressNode() throws UaException {
    return ClientNodeSupport.await(getDiscoveryAddressNodeAsync());
  }

  @Override
  public CompletableFuture<? extends NetworkAddressTypeNode> getDiscoveryAddressNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DiscoveryAddress",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        NetworkAddressTypeNode.class)));
  }

  @Override
  public @Nullable PropertyTypeNode getDiscoveryAnnounceRateNode() throws UaException {
    return ClientNodeSupport.await(getDiscoveryAnnounceRateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getDiscoveryAnnounceRateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DiscoveryAnnounceRate",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readDiscoveryAnnounceRate() throws UaException {
    return ClientNodeSupport.await(readDiscoveryAnnounceRateAsync());
  }

  @Override
  public void writeDiscoveryAnnounceRate(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDiscoveryAnnounceRateAsync(value)),
        "http://opcfoundation.org/UA/}DiscoveryAnnounceRate");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readDiscoveryAnnounceRateAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDiscoveryAnnounceRateNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DiscoveryAnnounceRate",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDiscoveryAnnounceRateAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDiscoveryAnnounceRateNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DiscoveryAnnounceRate",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getDiscoveryMaxMessageSizeNode() throws UaException {
    return ClientNodeSupport.await(getDiscoveryMaxMessageSizeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getDiscoveryMaxMessageSizeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DiscoveryMaxMessageSize",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readDiscoveryMaxMessageSize() throws UaException {
    return ClientNodeSupport.await(readDiscoveryMaxMessageSizeAsync());
  }

  @Override
  public void writeDiscoveryMaxMessageSize(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDiscoveryMaxMessageSizeAsync(value)),
        "http://opcfoundation.org/UA/}DiscoveryMaxMessageSize");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readDiscoveryMaxMessageSizeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDiscoveryMaxMessageSizeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DiscoveryMaxMessageSize",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDiscoveryMaxMessageSizeAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDiscoveryMaxMessageSizeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DiscoveryMaxMessageSize",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }
}
