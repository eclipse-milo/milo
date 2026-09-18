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
import org.eclipse.milo.opcua.stack.core.types.enumerated.PortIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpManagementAddressTxPortType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link LldpPortInformationType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.5">Model
 *     documentation</a>
 */
public class LldpPortInformationTypeNode extends BaseObjectTypeNode
    implements LldpPortInformationType {
  public LldpPortInformationTypeNode(
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
  public PropertyTypeNode getPortIdSubtypeNode() throws UaException {
    return ClientNodeSupport.await(getPortIdSubtypeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPortIdSubtypeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "PortIdSubtype",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable PortIdSubtype readPortIdSubtype() throws UaException {
    return ClientNodeSupport.await(readPortIdSubtypeAsync());
  }

  @Override
  public void writePortIdSubtype(@Nullable PortIdSubtype value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writePortIdSubtypeAsync(value)),
        "http://opcfoundation.org/UA/}PortIdSubtype");
  }

  @Override
  public CompletableFuture<? extends @Nullable PortIdSubtype> readPortIdSubtypeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getPortIdSubtypeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}PortIdSubtype",
                            true,
                            PortIdSubtype.class,
                            -1,
                            PortIdSubtype::from)),
                v -> CompletableFuture.completedFuture((@Nullable PortIdSubtype) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writePortIdSubtypeAsync(@Nullable PortIdSubtype value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getPortIdSubtypeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}PortIdSubtype",
                        value,
                        PortIdSubtype.class,
                        -1,
                        PortIdSubtype::from)));
  }

  @Override
  public PropertyTypeNode getDestMacAddressNode() throws UaException {
    return ClientNodeSupport.await(getDestMacAddressNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDestMacAddressNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DestMacAddress",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public UByte @Nullable [] readDestMacAddress() throws UaException {
    return ClientNodeSupport.await(readDestMacAddressAsync());
  }

  @Override
  public void writeDestMacAddress(UByte @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDestMacAddressAsync(value)),
        "http://opcfoundation.org/UA/}DestMacAddress");
  }

  @Override
  public CompletableFuture<? extends UByte @Nullable []> readDestMacAddressAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDestMacAddressNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DestMacAddress",
                            true,
                            UByte.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((UByte @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDestMacAddressAsync(UByte @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDestMacAddressNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DestMacAddress",
                        value,
                        UByte.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getPortDescriptionNode() throws UaException {
    return ClientNodeSupport.await(getPortDescriptionNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getPortDescriptionNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "PortDescription",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readPortDescription() throws UaException {
    return ClientNodeSupport.await(readPortDescriptionAsync());
  }

  @Override
  public void writePortDescription(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writePortDescriptionAsync(value)),
        "http://opcfoundation.org/UA/}PortDescription");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readPortDescriptionAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getPortDescriptionNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}PortDescription",
                            false,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writePortDescriptionAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getPortDescriptionNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}PortDescription",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable FolderTypeNode getRemoteSystemsDataNode() throws UaException {
    return ClientNodeSupport.await(getRemoteSystemsDataNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable FolderTypeNode> getRemoteSystemsDataNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "RemoteSystemsData",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        FolderTypeNode.class)));
  }

  @Override
  public @Nullable PropertyTypeNode getManagementAddressTxPortNode() throws UaException {
    return ClientNodeSupport.await(getManagementAddressTxPortNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getManagementAddressTxPortNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ManagementAddressTxPort",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable LldpManagementAddressTxPortType @Nullable [] readManagementAddressTxPort()
      throws UaException {
    return ClientNodeSupport.await(readManagementAddressTxPortAsync());
  }

  @Override
  public void writeManagementAddressTxPort(
      @Nullable LldpManagementAddressTxPortType @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeManagementAddressTxPortAsync(value)),
        "http://opcfoundation.org/UA/}ManagementAddressTxPort");
  }

  @Override
  public CompletableFuture<? extends @Nullable LldpManagementAddressTxPortType @Nullable []>
      readManagementAddressTxPortAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getManagementAddressTxPortNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ManagementAddressTxPort",
                            false,
                            LldpManagementAddressTxPortType.class,
                            1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable LldpManagementAddressTxPortType @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeManagementAddressTxPortAsync(
      @Nullable LldpManagementAddressTxPortType @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getManagementAddressTxPortNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ManagementAddressTxPort",
                        value,
                        LldpManagementAddressTxPortType.class,
                        1,
                        null)));
  }

  @Override
  public PropertyTypeNode getIetfBaseNetworkInterfaceNameNode() throws UaException {
    return ClientNodeSupport.await(getIetfBaseNetworkInterfaceNameNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getIetfBaseNetworkInterfaceNameNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "IetfBaseNetworkInterfaceName",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readIetfBaseNetworkInterfaceName() throws UaException {
    return ClientNodeSupport.await(readIetfBaseNetworkInterfaceNameAsync());
  }

  @Override
  public void writeIetfBaseNetworkInterfaceName(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeIetfBaseNetworkInterfaceNameAsync(value)),
        "http://opcfoundation.org/UA/}IetfBaseNetworkInterfaceName");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readIetfBaseNetworkInterfaceNameAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getIetfBaseNetworkInterfaceNameNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}IetfBaseNetworkInterfaceName",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeIetfBaseNetworkInterfaceNameAsync(
      @Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getIetfBaseNetworkInterfaceNameNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}IetfBaseNetworkInterfaceName",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getPortIdNode() throws UaException {
    return ClientNodeSupport.await(getPortIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPortIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "PortId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readPortId() throws UaException {
    return ClientNodeSupport.await(readPortIdAsync());
  }

  @Override
  public void writePortId(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writePortIdAsync(value)), "http://opcfoundation.org/UA/}PortId");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readPortIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getPortIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}PortId",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writePortIdAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getPortIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}PortId",
                        value,
                        String.class,
                        -1,
                        null)));
  }
}
