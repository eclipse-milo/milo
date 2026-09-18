package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.SerializationEntityTypeConfigureSerialization;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Structure;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link SerializationEntityType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part25/6.3.1">Model
 *     documentation</a>
 */
public class SerializationEntityTypeNode extends BaseObjectTypeNode
    implements SerializationEntityType {
  public SerializationEntityTypeNode(
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
  public @Nullable PropertyTypeNode getIncludeStatusNode() throws UaException {
    return ClientNodeSupport.await(getIncludeStatusNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getIncludeStatusNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "IncludeStatus",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readIncludeStatus() throws UaException {
    return ClientNodeSupport.await(readIncludeStatusAsync());
  }

  @Override
  public void writeIncludeStatus(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeIncludeStatusAsync(value)),
        "http://opcfoundation.org/UA/}IncludeStatus");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readIncludeStatusAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getIncludeStatusNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}IncludeStatus",
                            false,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeIncludeStatusAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getIncludeStatusNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}IncludeStatus",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getSerializedDataNode() throws UaException {
    return ClientNodeSupport.await(getSerializedDataNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getSerializedDataNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SerializedData",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable Structure readSerializedData() throws UaException {
    return ClientNodeSupport.await(readSerializedDataAsync());
  }

  @Override
  public void writeSerializedData(@Nullable Structure value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSerializedDataAsync(value)),
        "http://opcfoundation.org/UA/}SerializedData");
  }

  @Override
  public CompletableFuture<? extends @Nullable Structure> readSerializedDataAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSerializedDataNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SerializedData",
                            true,
                            Structure.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Structure) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSerializedDataAsync(@Nullable Structure value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSerializedDataNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SerializedData",
                        value,
                        Structure.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getCustomMetaDataRefNode() throws UaException {
    return ClientNodeSupport.await(getCustomMetaDataRefNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getCustomMetaDataRefNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CustomMetaDataRef",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable NodeId readCustomMetaDataRef() throws UaException {
    return ClientNodeSupport.await(readCustomMetaDataRefAsync());
  }

  @Override
  public void writeCustomMetaDataRef(@Nullable NodeId value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeCustomMetaDataRefAsync(value)),
        "http://opcfoundation.org/UA/}CustomMetaDataRef");
  }

  @Override
  public CompletableFuture<? extends @Nullable NodeId> readCustomMetaDataRefAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getCustomMetaDataRefNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}CustomMetaDataRef",
                            false,
                            NodeId.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable NodeId) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeCustomMetaDataRefAsync(@Nullable NodeId value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getCustomMetaDataRefNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}CustomMetaDataRef",
                        value,
                        NodeId.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getSerializationDepthNode() throws UaException {
    return ClientNodeSupport.await(getSerializationDepthNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getSerializationDepthNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SerializationDepth",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UShort readSerializationDepth() throws UaException {
    return ClientNodeSupport.await(readSerializationDepthAsync());
  }

  @Override
  public void writeSerializationDepth(@Nullable UShort value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSerializationDepthAsync(value)),
        "http://opcfoundation.org/UA/}SerializationDepth");
  }

  @Override
  public CompletableFuture<? extends @Nullable UShort> readSerializationDepthAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSerializationDepthNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SerializationDepth",
                            false,
                            UShort.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UShort) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSerializationDepthAsync(@Nullable UShort value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSerializationDepthNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SerializationDepth",
                        value,
                        UShort.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getExcludeReferenceTypesNode() throws UaException {
    return ClientNodeSupport.await(getExcludeReferenceTypesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getExcludeReferenceTypesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ExcludeReferenceTypes",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public NodeId @Nullable [] readExcludeReferenceTypes() throws UaException {
    return ClientNodeSupport.await(readExcludeReferenceTypesAsync());
  }

  @Override
  public void writeExcludeReferenceTypes(NodeId @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeExcludeReferenceTypesAsync(value)),
        "http://opcfoundation.org/UA/}ExcludeReferenceTypes");
  }

  @Override
  public CompletableFuture<? extends NodeId @Nullable []> readExcludeReferenceTypesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getExcludeReferenceTypesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ExcludeReferenceTypes",
                            false,
                            NodeId.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((NodeId @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeExcludeReferenceTypesAsync(NodeId @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getExcludeReferenceTypesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ExcludeReferenceTypes",
                        value,
                        NodeId.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getIncludeReferenceTypesNode() throws UaException {
    return ClientNodeSupport.await(getIncludeReferenceTypesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getIncludeReferenceTypesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "IncludeReferenceTypes",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public NodeId @Nullable [] readIncludeReferenceTypes() throws UaException {
    return ClientNodeSupport.await(readIncludeReferenceTypesAsync());
  }

  @Override
  public void writeIncludeReferenceTypes(NodeId @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeIncludeReferenceTypesAsync(value)),
        "http://opcfoundation.org/UA/}IncludeReferenceTypes");
  }

  @Override
  public CompletableFuture<? extends NodeId @Nullable []> readIncludeReferenceTypesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getIncludeReferenceTypesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}IncludeReferenceTypes",
                            false,
                            NodeId.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((NodeId @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeIncludeReferenceTypesAsync(NodeId @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getIncludeReferenceTypesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}IncludeReferenceTypes",
                        value,
                        NodeId.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getIncludeSourceTimestampNode() throws UaException {
    return ClientNodeSupport.await(getIncludeSourceTimestampNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getIncludeSourceTimestampNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "IncludeSourceTimestamp",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readIncludeSourceTimestamp() throws UaException {
    return ClientNodeSupport.await(readIncludeSourceTimestampAsync());
  }

  @Override
  public void writeIncludeSourceTimestamp(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeIncludeSourceTimestampAsync(value)),
        "http://opcfoundation.org/UA/}IncludeSourceTimestamp");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readIncludeSourceTimestampAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getIncludeSourceTimestampNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}IncludeSourceTimestamp",
                            false,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeIncludeSourceTimestampAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getIncludeSourceTimestampNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}IncludeSourceTimestamp",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getCustomMetaDataPropertiesNode() throws UaException {
    return ClientNodeSupport.await(getCustomMetaDataPropertiesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getCustomMetaDataPropertiesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CustomMetaDataProperties",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable KeyValuePair @Nullable [] readCustomMetaDataProperties() throws UaException {
    return ClientNodeSupport.await(readCustomMetaDataPropertiesAsync());
  }

  @Override
  public void writeCustomMetaDataProperties(@Nullable KeyValuePair @Nullable [] value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeCustomMetaDataPropertiesAsync(value)),
        "http://opcfoundation.org/UA/}CustomMetaDataProperties");
  }

  @Override
  public CompletableFuture<? extends @Nullable KeyValuePair @Nullable []>
      readCustomMetaDataPropertiesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getCustomMetaDataPropertiesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}CustomMetaDataProperties",
                            false,
                            KeyValuePair.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable KeyValuePair @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeCustomMetaDataPropertiesAsync(
      @Nullable KeyValuePair @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getCustomMetaDataPropertiesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}CustomMetaDataProperties",
                        value,
                        KeyValuePair.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getIncludeDictionaryReferenceNode() throws UaException {
    return ClientNodeSupport.await(getIncludeDictionaryReferenceNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getIncludeDictionaryReferenceNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "IncludeDictionaryReference",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readIncludeDictionaryReference() throws UaException {
    return ClientNodeSupport.await(readIncludeDictionaryReferenceAsync());
  }

  @Override
  public void writeIncludeDictionaryReference(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeIncludeDictionaryReferenceAsync(value)),
        "http://opcfoundation.org/UA/}IncludeDictionaryReference");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readIncludeDictionaryReferenceAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getIncludeDictionaryReferenceNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}IncludeDictionaryReference",
                            false,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeIncludeDictionaryReferenceAsync(
      @Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getIncludeDictionaryReferenceNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}IncludeDictionaryReference",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getConsiderSubElementSerializationPropertiesNode()
      throws UaException {
    return ClientNodeSupport.await(getConsiderSubElementSerializationPropertiesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getConsiderSubElementSerializationPropertiesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ConsiderSubElementSerializationProperties",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readConsiderSubElementSerializationProperties() throws UaException {
    return ClientNodeSupport.await(readConsiderSubElementSerializationPropertiesAsync());
  }

  @Override
  public void writeConsiderSubElementSerializationProperties(@Nullable Boolean value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeConsiderSubElementSerializationPropertiesAsync(value)),
        "http://opcfoundation.org/UA/}ConsiderSubElementSerializationProperties");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean>
      readConsiderSubElementSerializationPropertiesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getConsiderSubElementSerializationPropertiesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ConsiderSubElementSerializationProperties",
                            false,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeConsiderSubElementSerializationPropertiesAsync(
      @Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getConsiderSubElementSerializationPropertiesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ConsiderSubElementSerializationProperties",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable UaMethodNode getConfigureSerializationMethodNode() throws UaException {
    return ClientNodeSupport.await(getConfigureSerializationMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getConfigureSerializationMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "ConfigureSerialization",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public Integer @Nullable [] configureSerialization(
      @Nullable KeyValuePair @Nullable [] serializationFilterProperties) throws UaException {
    return ClientNodeSupport.await(configureSerializationAsync(serializationFilterProperties));
  }

  @Override
  public MethodCallResult<Integer @Nullable []> callConfigureSerialization(
      @Nullable KeyValuePair @Nullable [] serializationFilterProperties) throws UaException {
    return ClientNodeSupport.await(callConfigureSerializationAsync(serializationFilterProperties));
  }

  @Override
  public MethodCallResult<Integer @Nullable []> callConfigureSerializationWith(
      MethodCallOptions options, @Nullable KeyValuePair @Nullable [] serializationFilterProperties)
      throws UaException {
    return ClientNodeSupport.await(
        callConfigureSerializationWithAsync(options, serializationFilterProperties));
  }

  @Override
  public CompletableFuture<Integer @Nullable []> configureSerializationAsync(
      @Nullable KeyValuePair @Nullable [] serializationFilterProperties) {
    return ClientNodeSupport.compose(
        callConfigureSerializationAsync(serializationFilterProperties),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Integer @Nullable []>> callConfigureSerializationAsync(
      @Nullable KeyValuePair @Nullable [] serializationFilterProperties) {
    return callConfigureSerializationWithAsync(
        MethodCallOptions.DEFAULT, serializationFilterProperties);
  }

  @Override
  public CompletableFuture<MethodCallResult<Integer @Nullable []>>
      callConfigureSerializationWithAsync(
          MethodCallOptions options,
          @Nullable KeyValuePair @Nullable [] serializationFilterProperties) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new SerializationEntityTypeConfigureSerialization.Inputs(
                      serializationFilterProperties)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getConfigureSerializationMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return SerializationEntityTypeConfigureSerialization.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values)
                                        .results();
                                  }))));
        });
  }
}
