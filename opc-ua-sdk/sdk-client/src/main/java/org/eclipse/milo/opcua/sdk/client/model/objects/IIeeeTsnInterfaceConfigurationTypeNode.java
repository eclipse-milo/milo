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
 * Node implementation of {@link IIeeeTsnInterfaceConfigurationType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.10">Model
 *     documentation</a>
 */
public class IIeeeTsnInterfaceConfigurationTypeNode extends BaseInterfaceTypeNode
    implements IIeeeTsnInterfaceConfigurationType {
  public IIeeeTsnInterfaceConfigurationTypeNode(
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
  public UaVariableNode getMacAddressNode() throws UaException {
    return ClientNodeSupport.await(getMacAddressNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getMacAddressNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MacAddress",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable String readMacAddress() throws UaException {
    return ClientNodeSupport.await(readMacAddressAsync());
  }

  @Override
  public void writeMacAddress(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMacAddressAsync(value)),
        "http://opcfoundation.org/UA/}MacAddress");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readMacAddressAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMacAddressNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MacAddress",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMacAddressAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMacAddressNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MacAddress",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable UaVariableNode getInterfaceNameNode() throws UaException {
    return ClientNodeSupport.await(getInterfaceNameNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable UaVariableNode> getInterfaceNameNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "InterfaceName",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable String readInterfaceName() throws UaException {
    return ClientNodeSupport.await(readInterfaceNameAsync());
  }

  @Override
  public void writeInterfaceName(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeInterfaceNameAsync(value)),
        "http://opcfoundation.org/UA/}InterfaceName");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readInterfaceNameAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getInterfaceNameNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}InterfaceName",
                            false,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeInterfaceNameAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getInterfaceNameNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}InterfaceName",
                        value,
                        String.class,
                        -1,
                        null)));
  }
}
