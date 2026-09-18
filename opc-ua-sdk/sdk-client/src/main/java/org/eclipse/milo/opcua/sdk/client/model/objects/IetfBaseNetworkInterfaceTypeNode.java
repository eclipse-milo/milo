package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.AnalogUnitTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceAdminStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceOperStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link IetfBaseNetworkInterfaceType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.1/#5.5.1.2">Model
 *     documentation</a>
 */
public class IetfBaseNetworkInterfaceTypeNode extends BaseObjectTypeNode
    implements IetfBaseNetworkInterfaceType {
  public IetfBaseNetworkInterfaceTypeNode(
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
  public UaVariableNode getOperStatusNode() throws UaException {
    return ClientNodeSupport.await(getOperStatusNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getOperStatusNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "OperStatus",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable InterfaceOperStatus readOperStatus() throws UaException {
    return ClientNodeSupport.await(readOperStatusAsync());
  }

  @Override
  public void writeOperStatus(@Nullable InterfaceOperStatus value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeOperStatusAsync(value)),
        "http://opcfoundation.org/UA/}OperStatus");
  }

  @Override
  public CompletableFuture<? extends @Nullable InterfaceOperStatus> readOperStatusAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getOperStatusNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}OperStatus",
                            true,
                            InterfaceOperStatus.class,
                            -1,
                            InterfaceOperStatus::from)),
                v -> CompletableFuture.completedFuture((@Nullable InterfaceOperStatus) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeOperStatusAsync(@Nullable InterfaceOperStatus value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getOperStatusNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}OperStatus",
                        value,
                        InterfaceOperStatus.class,
                        -1,
                        InterfaceOperStatus::from)));
  }

  @Override
  public UaVariableNode getAdminStatusNode() throws UaException {
    return ClientNodeSupport.await(getAdminStatusNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getAdminStatusNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "AdminStatus",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable InterfaceAdminStatus readAdminStatus() throws UaException {
    return ClientNodeSupport.await(readAdminStatusAsync());
  }

  @Override
  public void writeAdminStatus(@Nullable InterfaceAdminStatus value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeAdminStatusAsync(value)),
        "http://opcfoundation.org/UA/}AdminStatus");
  }

  @Override
  public CompletableFuture<? extends @Nullable InterfaceAdminStatus> readAdminStatusAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getAdminStatusNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}AdminStatus",
                            true,
                            InterfaceAdminStatus.class,
                            -1,
                            InterfaceAdminStatus::from)),
                v -> CompletableFuture.completedFuture((@Nullable InterfaceAdminStatus) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeAdminStatusAsync(@Nullable InterfaceAdminStatus value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getAdminStatusNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}AdminStatus",
                        value,
                        InterfaceAdminStatus.class,
                        -1,
                        InterfaceAdminStatus::from)));
  }

  @Override
  public @Nullable UaVariableNode getPhysAddressNode() throws UaException {
    return ClientNodeSupport.await(getPhysAddressNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable UaVariableNode> getPhysAddressNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "PhysAddress",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable String readPhysAddress() throws UaException {
    return ClientNodeSupport.await(readPhysAddressAsync());
  }

  @Override
  public void writePhysAddress(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writePhysAddressAsync(value)),
        "http://opcfoundation.org/UA/}PhysAddress");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readPhysAddressAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getPhysAddressNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}PhysAddress",
                            false,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writePhysAddressAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getPhysAddressNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}PhysAddress",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public AnalogUnitTypeNode getSpeedNode() throws UaException {
    return ClientNodeSupport.await(getSpeedNodeAsync());
  }

  @Override
  public CompletableFuture<? extends AnalogUnitTypeNode> getSpeedNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Speed",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        AnalogUnitTypeNode.class)));
  }

  @Override
  public @Nullable ULong readSpeed() throws UaException {
    return ClientNodeSupport.await(readSpeedAsync());
  }

  @Override
  public void writeSpeed(@Nullable ULong value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSpeedAsync(value)), "http://opcfoundation.org/UA/}Speed");
  }

  @Override
  public CompletableFuture<? extends @Nullable ULong> readSpeedAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSpeedNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Speed",
                            true,
                            ULong.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ULong) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSpeedAsync(@Nullable ULong value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSpeedNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Speed",
                        value,
                        ULong.class,
                        -1,
                        null)));
  }
}
