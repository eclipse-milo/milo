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
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link StandaloneSubscribedDataSetType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.5">Model
 *     documentation</a>
 */
public class StandaloneSubscribedDataSetTypeNode extends BaseObjectTypeNode
    implements StandaloneSubscribedDataSetType {
  public StandaloneSubscribedDataSetTypeNode(
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
  public PropertyTypeNode getIsConnectedNode() throws UaException {
    return ClientNodeSupport.await(getIsConnectedNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getIsConnectedNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "IsConnected",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readIsConnected() throws UaException {
    return ClientNodeSupport.await(readIsConnectedAsync());
  }

  @Override
  public void writeIsConnected(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeIsConnectedAsync(value)),
        "http://opcfoundation.org/UA/}IsConnected");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readIsConnectedAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getIsConnectedNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}IsConnected",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeIsConnectedAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getIsConnectedNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}IsConnected",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getDataSetMetaDataNode() throws UaException {
    return ClientNodeSupport.await(getDataSetMetaDataNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataSetMetaDataNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DataSetMetaData",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable DataSetMetaDataType readDataSetMetaData() throws UaException {
    return ClientNodeSupport.await(readDataSetMetaDataAsync());
  }

  @Override
  public void writeDataSetMetaData(@Nullable DataSetMetaDataType value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDataSetMetaDataAsync(value)),
        "http://opcfoundation.org/UA/}DataSetMetaData");
  }

  @Override
  public CompletableFuture<? extends @Nullable DataSetMetaDataType> readDataSetMetaDataAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDataSetMetaDataNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DataSetMetaData",
                            true,
                            DataSetMetaDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable DataSetMetaDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDataSetMetaDataAsync(
      @Nullable DataSetMetaDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDataSetMetaDataNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DataSetMetaData",
                        value,
                        DataSetMetaDataType.class,
                        -1,
                        null)));
  }

  @Override
  public SubscribedDataSetTypeNode getSubscribedDataSetNode() throws UaException {
    return ClientNodeSupport.await(getSubscribedDataSetNodeAsync());
  }

  @Override
  public CompletableFuture<? extends SubscribedDataSetTypeNode> getSubscribedDataSetNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SubscribedDataSet",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        SubscribedDataSetTypeNode.class)));
  }
}
