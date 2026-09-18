package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.UUID;
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
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link PublishedDataSetType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.1">Model
 *     documentation</a>
 */
public class PublishedDataSetTypeNode extends BaseObjectTypeNode implements PublishedDataSetType {
  public PublishedDataSetTypeNode(
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
  public @Nullable PropertyTypeNode getCyclicDataSetNode() throws UaException {
    return ClientNodeSupport.await(getCyclicDataSetNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getCyclicDataSetNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CyclicDataSet",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readCyclicDataSet() throws UaException {
    return ClientNodeSupport.await(readCyclicDataSetAsync());
  }

  @Override
  public void writeCyclicDataSet(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeCyclicDataSetAsync(value)),
        "http://opcfoundation.org/UA/}CyclicDataSet");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readCyclicDataSetAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getCyclicDataSetNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}CyclicDataSet",
                            false,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeCyclicDataSetAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getCyclicDataSetNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}CyclicDataSet",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getDataSetClassIdNode() throws UaException {
    return ClientNodeSupport.await(getDataSetClassIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getDataSetClassIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DataSetClassId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UUID readDataSetClassId() throws UaException {
    return ClientNodeSupport.await(readDataSetClassIdAsync());
  }

  @Override
  public void writeDataSetClassId(@Nullable UUID value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDataSetClassIdAsync(value)),
        "http://opcfoundation.org/UA/}DataSetClassId");
  }

  @Override
  public CompletableFuture<? extends @Nullable UUID> readDataSetClassIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDataSetClassIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DataSetClassId",
                            false,
                            UUID.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UUID) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDataSetClassIdAsync(@Nullable UUID value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDataSetClassIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DataSetClassId",
                        value,
                        UUID.class,
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
  public @Nullable ExtensionFieldsTypeNode getExtensionFieldsNode() throws UaException {
    return ClientNodeSupport.await(getExtensionFieldsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable ExtensionFieldsTypeNode>
      getExtensionFieldsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ExtensionFields",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        ExtensionFieldsTypeNode.class)));
  }

  @Override
  public PropertyTypeNode getConfigurationVersionNode() throws UaException {
    return ClientNodeSupport.await(getConfigurationVersionNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getConfigurationVersionNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ConfigurationVersion",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable ConfigurationVersionDataType readConfigurationVersion() throws UaException {
    return ClientNodeSupport.await(readConfigurationVersionAsync());
  }

  @Override
  public void writeConfigurationVersion(@Nullable ConfigurationVersionDataType value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeConfigurationVersionAsync(value)),
        "http://opcfoundation.org/UA/}ConfigurationVersion");
  }

  @Override
  public CompletableFuture<? extends @Nullable ConfigurationVersionDataType>
      readConfigurationVersionAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getConfigurationVersionNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ConfigurationVersion",
                            true,
                            ConfigurationVersionDataType.class,
                            -1,
                            null)),
                v ->
                    CompletableFuture.completedFuture((@Nullable ConfigurationVersionDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeConfigurationVersionAsync(
      @Nullable ConfigurationVersionDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getConfigurationVersionNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ConfigurationVersion",
                        value,
                        ConfigurationVersionDataType.class,
                        -1,
                        null)));
  }
}
