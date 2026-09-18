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
import org.eclipse.milo.opcua.stack.core.types.enumerated.ChassisIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PortIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpManagementAddressType;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpSystemCapabilitiesMap;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpTlvType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link LldpRemoteSystemType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.6">Model
 *     documentation</a>
 */
public class LldpRemoteSystemTypeNode extends BaseObjectTypeNode implements LldpRemoteSystemType {
  public LldpRemoteSystemTypeNode(
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
  public @Nullable UaVariableNode getSystemNameNode() throws UaException {
    return ClientNodeSupport.await(getSystemNameNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable UaVariableNode> getSystemNameNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SystemName",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable String readSystemName() throws UaException {
    return ClientNodeSupport.await(readSystemNameAsync());
  }

  @Override
  public void writeSystemName(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSystemNameAsync(value)),
        "http://opcfoundation.org/UA/}SystemName");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readSystemNameAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSystemNameNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SystemName",
                            false,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSystemNameAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSystemNameNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SystemName",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getRemoteIndexNode() throws UaException {
    return ClientNodeSupport.await(getRemoteIndexNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getRemoteIndexNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "RemoteIndex",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readRemoteIndex() throws UaException {
    return ClientNodeSupport.await(readRemoteIndexAsync());
  }

  @Override
  public void writeRemoteIndex(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeRemoteIndexAsync(value)),
        "http://opcfoundation.org/UA/}RemoteIndex");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readRemoteIndexAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getRemoteIndexNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}RemoteIndex",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeRemoteIndexAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getRemoteIndexNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}RemoteIndex",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getPortIdSubtypeNode() throws UaException {
    return ClientNodeSupport.await(getPortIdSubtypeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getPortIdSubtypeNodeAsync() {
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
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
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
  public @Nullable UaVariableNode getRemoteChangesNode() throws UaException {
    return ClientNodeSupport.await(getRemoteChangesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable UaVariableNode> getRemoteChangesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "RemoteChanges",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable Boolean readRemoteChanges() throws UaException {
    return ClientNodeSupport.await(readRemoteChangesAsync());
  }

  @Override
  public void writeRemoteChanges(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeRemoteChangesAsync(value)),
        "http://opcfoundation.org/UA/}RemoteChanges");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readRemoteChangesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getRemoteChangesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}RemoteChanges",
                            false,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeRemoteChangesAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getRemoteChangesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}RemoteChanges",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable UaVariableNode getPortDescriptionNode() throws UaException {
    return ClientNodeSupport.await(getPortDescriptionNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable UaVariableNode> getPortDescriptionNodeAsync() {
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
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
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
  public UaVariableNode getChassisIdSubtypeNode() throws UaException {
    return ClientNodeSupport.await(getChassisIdSubtypeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getChassisIdSubtypeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ChassisIdSubtype",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ChassisIdSubtype readChassisIdSubtype() throws UaException {
    return ClientNodeSupport.await(readChassisIdSubtypeAsync());
  }

  @Override
  public void writeChassisIdSubtype(@Nullable ChassisIdSubtype value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeChassisIdSubtypeAsync(value)),
        "http://opcfoundation.org/UA/}ChassisIdSubtype");
  }

  @Override
  public CompletableFuture<? extends @Nullable ChassisIdSubtype> readChassisIdSubtypeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getChassisIdSubtypeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ChassisIdSubtype",
                            true,
                            ChassisIdSubtype.class,
                            -1,
                            ChassisIdSubtype::from)),
                v -> CompletableFuture.completedFuture((@Nullable ChassisIdSubtype) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeChassisIdSubtypeAsync(
      @Nullable ChassisIdSubtype value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getChassisIdSubtypeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ChassisIdSubtype",
                        value,
                        ChassisIdSubtype.class,
                        -1,
                        ChassisIdSubtype::from)));
  }

  @Override
  public @Nullable UaVariableNode getRemoteUnknownTlvNode() throws UaException {
    return ClientNodeSupport.await(getRemoteUnknownTlvNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable UaVariableNode> getRemoteUnknownTlvNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "RemoteUnknownTlv",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable LldpTlvType @Nullable [] readRemoteUnknownTlv() throws UaException {
    return ClientNodeSupport.await(readRemoteUnknownTlvAsync());
  }

  @Override
  public void writeRemoteUnknownTlv(@Nullable LldpTlvType @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeRemoteUnknownTlvAsync(value)),
        "http://opcfoundation.org/UA/}RemoteUnknownTlv");
  }

  @Override
  public CompletableFuture<? extends @Nullable LldpTlvType @Nullable []>
      readRemoteUnknownTlvAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getRemoteUnknownTlvNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}RemoteUnknownTlv",
                            false,
                            LldpTlvType.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LldpTlvType @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeRemoteUnknownTlvAsync(
      @Nullable LldpTlvType @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getRemoteUnknownTlvNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}RemoteUnknownTlv",
                        value,
                        LldpTlvType.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable UaVariableNode getManagementAddressNode() throws UaException {
    return ClientNodeSupport.await(getManagementAddressNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable UaVariableNode> getManagementAddressNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ManagementAddress",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable LldpManagementAddressType @Nullable [] readManagementAddress()
      throws UaException {
    return ClientNodeSupport.await(readManagementAddressAsync());
  }

  @Override
  public void writeManagementAddress(@Nullable LldpManagementAddressType @Nullable [] value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeManagementAddressAsync(value)),
        "http://opcfoundation.org/UA/}ManagementAddress");
  }

  @Override
  public CompletableFuture<? extends @Nullable LldpManagementAddressType @Nullable []>
      readManagementAddressAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getManagementAddressNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ManagementAddress",
                            false,
                            LldpManagementAddressType.class,
                            1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable LldpManagementAddressType @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeManagementAddressAsync(
      @Nullable LldpManagementAddressType @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getManagementAddressNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ManagementAddress",
                        value,
                        LldpManagementAddressType.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable UaVariableNode getSystemDescriptionNode() throws UaException {
    return ClientNodeSupport.await(getSystemDescriptionNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable UaVariableNode> getSystemDescriptionNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SystemDescription",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable String readSystemDescription() throws UaException {
    return ClientNodeSupport.await(readSystemDescriptionAsync());
  }

  @Override
  public void writeSystemDescription(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSystemDescriptionAsync(value)),
        "http://opcfoundation.org/UA/}SystemDescription");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readSystemDescriptionAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSystemDescriptionNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SystemDescription",
                            false,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSystemDescriptionAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSystemDescriptionNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SystemDescription",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable UaVariableNode getRemoteTooManyNeighborsNode() throws UaException {
    return ClientNodeSupport.await(getRemoteTooManyNeighborsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable UaVariableNode>
      getRemoteTooManyNeighborsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "RemoteTooManyNeighbors",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable Boolean readRemoteTooManyNeighbors() throws UaException {
    return ClientNodeSupport.await(readRemoteTooManyNeighborsAsync());
  }

  @Override
  public void writeRemoteTooManyNeighbors(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeRemoteTooManyNeighborsAsync(value)),
        "http://opcfoundation.org/UA/}RemoteTooManyNeighbors");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readRemoteTooManyNeighborsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getRemoteTooManyNeighborsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}RemoteTooManyNeighbors",
                            false,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeRemoteTooManyNeighborsAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getRemoteTooManyNeighborsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}RemoteTooManyNeighbors",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable UaVariableNode getSystemCapabilitiesEnabledNode() throws UaException {
    return ClientNodeSupport.await(getSystemCapabilitiesEnabledNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable UaVariableNode>
      getSystemCapabilitiesEnabledNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SystemCapabilitiesEnabled",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable LldpSystemCapabilitiesMap readSystemCapabilitiesEnabled() throws UaException {
    return ClientNodeSupport.await(readSystemCapabilitiesEnabledAsync());
  }

  @Override
  public void writeSystemCapabilitiesEnabled(@Nullable LldpSystemCapabilitiesMap value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSystemCapabilitiesEnabledAsync(value)),
        "http://opcfoundation.org/UA/}SystemCapabilitiesEnabled");
  }

  @Override
  public CompletableFuture<? extends @Nullable LldpSystemCapabilitiesMap>
      readSystemCapabilitiesEnabledAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSystemCapabilitiesEnabledNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SystemCapabilitiesEnabled",
                            false,
                            LldpSystemCapabilitiesMap.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LldpSystemCapabilitiesMap) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSystemCapabilitiesEnabledAsync(
      @Nullable LldpSystemCapabilitiesMap value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSystemCapabilitiesEnabledNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SystemCapabilitiesEnabled",
                        value,
                        LldpSystemCapabilitiesMap.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable UaVariableNode getSystemCapabilitiesSupportedNode() throws UaException {
    return ClientNodeSupport.await(getSystemCapabilitiesSupportedNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable UaVariableNode>
      getSystemCapabilitiesSupportedNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SystemCapabilitiesSupported",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable LldpSystemCapabilitiesMap readSystemCapabilitiesSupported() throws UaException {
    return ClientNodeSupport.await(readSystemCapabilitiesSupportedAsync());
  }

  @Override
  public void writeSystemCapabilitiesSupported(@Nullable LldpSystemCapabilitiesMap value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSystemCapabilitiesSupportedAsync(value)),
        "http://opcfoundation.org/UA/}SystemCapabilitiesSupported");
  }

  @Override
  public CompletableFuture<? extends @Nullable LldpSystemCapabilitiesMap>
      readSystemCapabilitiesSupportedAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSystemCapabilitiesSupportedNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SystemCapabilitiesSupported",
                            false,
                            LldpSystemCapabilitiesMap.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LldpSystemCapabilitiesMap) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSystemCapabilitiesSupportedAsync(
      @Nullable LldpSystemCapabilitiesMap value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSystemCapabilitiesSupportedNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SystemCapabilitiesSupported",
                        value,
                        LldpSystemCapabilitiesMap.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getPortIdNode() throws UaException {
    return ClientNodeSupport.await(getPortIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getPortIdNodeAsync() {
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
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
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

  @Override
  public UaVariableNode getTimeMarkNode() throws UaException {
    return ClientNodeSupport.await(getTimeMarkNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getTimeMarkNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "TimeMark",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readTimeMark() throws UaException {
    return ClientNodeSupport.await(readTimeMarkAsync());
  }

  @Override
  public void writeTimeMark(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeTimeMarkAsync(value)),
        "http://opcfoundation.org/UA/}TimeMark");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readTimeMarkAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getTimeMarkNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}TimeMark",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTimeMarkAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getTimeMarkNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}TimeMark",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getChassisIdNode() throws UaException {
    return ClientNodeSupport.await(getChassisIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getChassisIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ChassisId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable String readChassisId() throws UaException {
    return ClientNodeSupport.await(readChassisIdAsync());
  }

  @Override
  public void writeChassisId(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeChassisIdAsync(value)),
        "http://opcfoundation.org/UA/}ChassisId");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readChassisIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getChassisIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ChassisId",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeChassisIdAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getChassisIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ChassisId",
                        value,
                        String.class,
                        -1,
                        null)));
  }
}
