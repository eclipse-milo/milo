package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceCounterDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link SessionDiagnosticsVariableType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.14">Model
 *     documentation</a>
 */
public class SessionDiagnosticsVariableTypeNode extends BaseDataVariableTypeNode
    implements SessionDiagnosticsVariableType {
  public SessionDiagnosticsVariableTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger @Nullable [] arrayDimensions,
      UByte accessLevel,
      UByte userAccessLevel,
      Double minimumSamplingInterval,
      Boolean historizing,
      @Nullable AccessLevelExType accessLevelEx) {
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
        value,
        dataType,
        valueRank,
        arrayDimensions,
        accessLevel,
        userAccessLevel,
        minimumSamplingInterval,
        historizing,
        accessLevelEx);
  }

  @Override
  public UaVariableNode getWriteCountNode() throws UaException {
    return ClientNodeSupport.await(getWriteCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getWriteCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "WriteCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServiceCounterDataType readWriteCount() throws UaException {
    return ClientNodeSupport.await(readWriteCountAsync());
  }

  @Override
  public void writeWriteCount(@Nullable ServiceCounterDataType value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeWriteCountAsync(value)),
        "http://opcfoundation.org/UA/}WriteCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServiceCounterDataType> readWriteCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getWriteCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}WriteCount",
                            true,
                            ServiceCounterDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServiceCounterDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeWriteCountAsync(
      @Nullable ServiceCounterDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getWriteCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}WriteCount",
                        value,
                        ServiceCounterDataType.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getBrowseCountNode() throws UaException {
    return ClientNodeSupport.await(getBrowseCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getBrowseCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "BrowseCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServiceCounterDataType readBrowseCount() throws UaException {
    return ClientNodeSupport.await(readBrowseCountAsync());
  }

  @Override
  public void writeBrowseCount(@Nullable ServiceCounterDataType value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeBrowseCountAsync(value)),
        "http://opcfoundation.org/UA/}BrowseCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServiceCounterDataType> readBrowseCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getBrowseCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}BrowseCount",
                            true,
                            ServiceCounterDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServiceCounterDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeBrowseCountAsync(
      @Nullable ServiceCounterDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getBrowseCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}BrowseCount",
                        value,
                        ServiceCounterDataType.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getEndpointUrlNode() throws UaException {
    return ClientNodeSupport.await(getEndpointUrlNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getEndpointUrlNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "EndpointUrl",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable String readEndpointUrl() throws UaException {
    return ClientNodeSupport.await(readEndpointUrlAsync());
  }

  @Override
  public void writeEndpointUrl(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeEndpointUrlAsync(value)),
        "http://opcfoundation.org/UA/}EndpointUrl");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readEndpointUrlAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getEndpointUrlNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}EndpointUrl",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeEndpointUrlAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getEndpointUrlNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}EndpointUrl",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getSessionNameNode() throws UaException {
    return ClientNodeSupport.await(getSessionNameNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getSessionNameNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SessionName",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable String readSessionName() throws UaException {
    return ClientNodeSupport.await(readSessionNameAsync());
  }

  @Override
  public void writeSessionName(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSessionNameAsync(value)),
        "http://opcfoundation.org/UA/}SessionName");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readSessionNameAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSessionNameNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SessionName",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSessionNameAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSessionNameNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SessionName",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getPublishCountNode() throws UaException {
    return ClientNodeSupport.await(getPublishCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getPublishCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "PublishCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServiceCounterDataType readPublishCount() throws UaException {
    return ClientNodeSupport.await(readPublishCountAsync());
  }

  @Override
  public void writePublishCount(@Nullable ServiceCounterDataType value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writePublishCountAsync(value)),
        "http://opcfoundation.org/UA/}PublishCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServiceCounterDataType> readPublishCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getPublishCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}PublishCount",
                            true,
                            ServiceCounterDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServiceCounterDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writePublishCountAsync(
      @Nullable ServiceCounterDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getPublishCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}PublishCount",
                        value,
                        ServiceCounterDataType.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getAddNodesCountNode() throws UaException {
    return ClientNodeSupport.await(getAddNodesCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getAddNodesCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "AddNodesCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServiceCounterDataType readAddNodesCount() throws UaException {
    return ClientNodeSupport.await(readAddNodesCountAsync());
  }

  @Override
  public void writeAddNodesCount(@Nullable ServiceCounterDataType value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeAddNodesCountAsync(value)),
        "http://opcfoundation.org/UA/}AddNodesCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServiceCounterDataType> readAddNodesCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getAddNodesCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}AddNodesCount",
                            true,
                            ServiceCounterDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServiceCounterDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeAddNodesCountAsync(
      @Nullable ServiceCounterDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getAddNodesCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}AddNodesCount",
                        value,
                        ServiceCounterDataType.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getQueryNextCountNode() throws UaException {
    return ClientNodeSupport.await(getQueryNextCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getQueryNextCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "QueryNextCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServiceCounterDataType readQueryNextCount() throws UaException {
    return ClientNodeSupport.await(readQueryNextCountAsync());
  }

  @Override
  public void writeQueryNextCount(@Nullable ServiceCounterDataType value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeQueryNextCountAsync(value)),
        "http://opcfoundation.org/UA/}QueryNextCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServiceCounterDataType> readQueryNextCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getQueryNextCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}QueryNextCount",
                            true,
                            ServiceCounterDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServiceCounterDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeQueryNextCountAsync(
      @Nullable ServiceCounterDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getQueryNextCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}QueryNextCount",
                        value,
                        ServiceCounterDataType.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getRepublishCountNode() throws UaException {
    return ClientNodeSupport.await(getRepublishCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getRepublishCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "RepublishCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServiceCounterDataType readRepublishCount() throws UaException {
    return ClientNodeSupport.await(readRepublishCountAsync());
  }

  @Override
  public void writeRepublishCount(@Nullable ServiceCounterDataType value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeRepublishCountAsync(value)),
        "http://opcfoundation.org/UA/}RepublishCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServiceCounterDataType> readRepublishCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getRepublishCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}RepublishCount",
                            true,
                            ServiceCounterDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServiceCounterDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeRepublishCountAsync(
      @Nullable ServiceCounterDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getRepublishCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}RepublishCount",
                        value,
                        ServiceCounterDataType.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getBrowseNextCountNode() throws UaException {
    return ClientNodeSupport.await(getBrowseNextCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getBrowseNextCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "BrowseNextCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServiceCounterDataType readBrowseNextCount() throws UaException {
    return ClientNodeSupport.await(readBrowseNextCountAsync());
  }

  @Override
  public void writeBrowseNextCount(@Nullable ServiceCounterDataType value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeBrowseNextCountAsync(value)),
        "http://opcfoundation.org/UA/}BrowseNextCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServiceCounterDataType> readBrowseNextCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getBrowseNextCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}BrowseNextCount",
                            true,
                            ServiceCounterDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServiceCounterDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeBrowseNextCountAsync(
      @Nullable ServiceCounterDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getBrowseNextCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}BrowseNextCount",
                        value,
                        ServiceCounterDataType.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getQueryFirstCountNode() throws UaException {
    return ClientNodeSupport.await(getQueryFirstCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getQueryFirstCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "QueryFirstCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServiceCounterDataType readQueryFirstCount() throws UaException {
    return ClientNodeSupport.await(readQueryFirstCountAsync());
  }

  @Override
  public void writeQueryFirstCount(@Nullable ServiceCounterDataType value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeQueryFirstCountAsync(value)),
        "http://opcfoundation.org/UA/}QueryFirstCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServiceCounterDataType> readQueryFirstCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getQueryFirstCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}QueryFirstCount",
                            true,
                            ServiceCounterDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServiceCounterDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeQueryFirstCountAsync(
      @Nullable ServiceCounterDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getQueryFirstCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}QueryFirstCount",
                        value,
                        ServiceCounterDataType.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getDeleteNodesCountNode() throws UaException {
    return ClientNodeSupport.await(getDeleteNodesCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getDeleteNodesCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DeleteNodesCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServiceCounterDataType readDeleteNodesCount() throws UaException {
    return ClientNodeSupport.await(readDeleteNodesCountAsync());
  }

  @Override
  public void writeDeleteNodesCount(@Nullable ServiceCounterDataType value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDeleteNodesCountAsync(value)),
        "http://opcfoundation.org/UA/}DeleteNodesCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServiceCounterDataType> readDeleteNodesCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDeleteNodesCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DeleteNodesCount",
                            true,
                            ServiceCounterDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServiceCounterDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDeleteNodesCountAsync(
      @Nullable ServiceCounterDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDeleteNodesCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DeleteNodesCount",
                        value,
                        ServiceCounterDataType.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getHistoryReadCountNode() throws UaException {
    return ClientNodeSupport.await(getHistoryReadCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getHistoryReadCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "HistoryReadCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServiceCounterDataType readHistoryReadCount() throws UaException {
    return ClientNodeSupport.await(readHistoryReadCountAsync());
  }

  @Override
  public void writeHistoryReadCount(@Nullable ServiceCounterDataType value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeHistoryReadCountAsync(value)),
        "http://opcfoundation.org/UA/}HistoryReadCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServiceCounterDataType> readHistoryReadCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getHistoryReadCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}HistoryReadCount",
                            true,
                            ServiceCounterDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServiceCounterDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeHistoryReadCountAsync(
      @Nullable ServiceCounterDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getHistoryReadCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}HistoryReadCount",
                        value,
                        ServiceCounterDataType.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getClientDescriptionNode() throws UaException {
    return ClientNodeSupport.await(getClientDescriptionNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getClientDescriptionNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ClientDescription",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ApplicationDescription readClientDescription() throws UaException {
    return ClientNodeSupport.await(readClientDescriptionAsync());
  }

  @Override
  public void writeClientDescription(@Nullable ApplicationDescription value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeClientDescriptionAsync(value)),
        "http://opcfoundation.org/UA/}ClientDescription");
  }

  @Override
  public CompletableFuture<? extends @Nullable ApplicationDescription>
      readClientDescriptionAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getClientDescriptionNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ClientDescription",
                            true,
                            ApplicationDescription.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ApplicationDescription) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeClientDescriptionAsync(
      @Nullable ApplicationDescription value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getClientDescriptionNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ClientDescription",
                        value,
                        ApplicationDescription.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getTotalRequestCountNode() throws UaException {
    return ClientNodeSupport.await(getTotalRequestCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getTotalRequestCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "TotalRequestCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServiceCounterDataType readTotalRequestCount() throws UaException {
    return ClientNodeSupport.await(readTotalRequestCountAsync());
  }

  @Override
  public void writeTotalRequestCount(@Nullable ServiceCounterDataType value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeTotalRequestCountAsync(value)),
        "http://opcfoundation.org/UA/}TotalRequestCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readTotalRequestCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getTotalRequestCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}TotalRequestCount",
                            true,
                            ServiceCounterDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServiceCounterDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTotalRequestCountAsync(
      @Nullable ServiceCounterDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getTotalRequestCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}TotalRequestCount",
                        value,
                        ServiceCounterDataType.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getAddReferencesCountNode() throws UaException {
    return ClientNodeSupport.await(getAddReferencesCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getAddReferencesCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "AddReferencesCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServiceCounterDataType readAddReferencesCount() throws UaException {
    return ClientNodeSupport.await(readAddReferencesCountAsync());
  }

  @Override
  public void writeAddReferencesCount(@Nullable ServiceCounterDataType value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeAddReferencesCountAsync(value)),
        "http://opcfoundation.org/UA/}AddReferencesCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readAddReferencesCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getAddReferencesCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}AddReferencesCount",
                            true,
                            ServiceCounterDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServiceCounterDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeAddReferencesCountAsync(
      @Nullable ServiceCounterDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getAddReferencesCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}AddReferencesCount",
                        value,
                        ServiceCounterDataType.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getHistoryUpdateCountNode() throws UaException {
    return ClientNodeSupport.await(getHistoryUpdateCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getHistoryUpdateCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "HistoryUpdateCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServiceCounterDataType readHistoryUpdateCount() throws UaException {
    return ClientNodeSupport.await(readHistoryUpdateCountAsync());
  }

  @Override
  public void writeHistoryUpdateCount(@Nullable ServiceCounterDataType value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeHistoryUpdateCountAsync(value)),
        "http://opcfoundation.org/UA/}HistoryUpdateCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readHistoryUpdateCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getHistoryUpdateCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}HistoryUpdateCount",
                            true,
                            ServiceCounterDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServiceCounterDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeHistoryUpdateCountAsync(
      @Nullable ServiceCounterDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getHistoryUpdateCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}HistoryUpdateCount",
                        value,
                        ServiceCounterDataType.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getRegisterNodesCountNode() throws UaException {
    return ClientNodeSupport.await(getRegisterNodesCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getRegisterNodesCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "RegisterNodesCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServiceCounterDataType readRegisterNodesCount() throws UaException {
    return ClientNodeSupport.await(readRegisterNodesCountAsync());
  }

  @Override
  public void writeRegisterNodesCount(@Nullable ServiceCounterDataType value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeRegisterNodesCountAsync(value)),
        "http://opcfoundation.org/UA/}RegisterNodesCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readRegisterNodesCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getRegisterNodesCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}RegisterNodesCount",
                            true,
                            ServiceCounterDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServiceCounterDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeRegisterNodesCountAsync(
      @Nullable ServiceCounterDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getRegisterNodesCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}RegisterNodesCount",
                        value,
                        ServiceCounterDataType.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getSetTriggeringCountNode() throws UaException {
    return ClientNodeSupport.await(getSetTriggeringCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getSetTriggeringCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SetTriggeringCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServiceCounterDataType readSetTriggeringCount() throws UaException {
    return ClientNodeSupport.await(readSetTriggeringCountAsync());
  }

  @Override
  public void writeSetTriggeringCount(@Nullable ServiceCounterDataType value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSetTriggeringCountAsync(value)),
        "http://opcfoundation.org/UA/}SetTriggeringCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readSetTriggeringCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSetTriggeringCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SetTriggeringCount",
                            true,
                            ServiceCounterDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServiceCounterDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSetTriggeringCountAsync(
      @Nullable ServiceCounterDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSetTriggeringCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SetTriggeringCount",
                        value,
                        ServiceCounterDataType.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getActualSessionTimeoutNode() throws UaException {
    return ClientNodeSupport.await(getActualSessionTimeoutNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getActualSessionTimeoutNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ActualSessionTimeout",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable Double readActualSessionTimeout() throws UaException {
    return ClientNodeSupport.await(readActualSessionTimeoutAsync());
  }

  @Override
  public void writeActualSessionTimeout(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeActualSessionTimeoutAsync(value)),
        "http://opcfoundation.org/UA/}ActualSessionTimeout");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readActualSessionTimeoutAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getActualSessionTimeoutNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ActualSessionTimeout",
                            true,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeActualSessionTimeoutAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getActualSessionTimeoutNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ActualSessionTimeout",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getClientConnectionTimeNode() throws UaException {
    return ClientNodeSupport.await(getClientConnectionTimeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getClientConnectionTimeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ClientConnectionTime",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable DateTime readClientConnectionTime() throws UaException {
    return ClientNodeSupport.await(readClientConnectionTimeAsync());
  }

  @Override
  public void writeClientConnectionTime(@Nullable DateTime value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeClientConnectionTimeAsync(value)),
        "http://opcfoundation.org/UA/}ClientConnectionTime");
  }

  @Override
  public CompletableFuture<? extends @Nullable DateTime> readClientConnectionTimeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getClientConnectionTimeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ClientConnectionTime",
                            true,
                            DateTime.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable DateTime) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeClientConnectionTimeAsync(@Nullable DateTime value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getClientConnectionTimeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ClientConnectionTime",
                        value,
                        DateTime.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getUnregisterNodesCountNode() throws UaException {
    return ClientNodeSupport.await(getUnregisterNodesCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getUnregisterNodesCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "UnregisterNodesCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServiceCounterDataType readUnregisterNodesCount() throws UaException {
    return ClientNodeSupport.await(readUnregisterNodesCountAsync());
  }

  @Override
  public void writeUnregisterNodesCount(@Nullable ServiceCounterDataType value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeUnregisterNodesCountAsync(value)),
        "http://opcfoundation.org/UA/}UnregisterNodesCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readUnregisterNodesCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getUnregisterNodesCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}UnregisterNodesCount",
                            true,
                            ServiceCounterDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServiceCounterDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeUnregisterNodesCountAsync(
      @Nullable ServiceCounterDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getUnregisterNodesCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}UnregisterNodesCount",
                        value,
                        ServiceCounterDataType.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getClientLastContactTimeNode() throws UaException {
    return ClientNodeSupport.await(getClientLastContactTimeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getClientLastContactTimeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ClientLastContactTime",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable DateTime readClientLastContactTime() throws UaException {
    return ClientNodeSupport.await(readClientLastContactTimeAsync());
  }

  @Override
  public void writeClientLastContactTime(@Nullable DateTime value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeClientLastContactTimeAsync(value)),
        "http://opcfoundation.org/UA/}ClientLastContactTime");
  }

  @Override
  public CompletableFuture<? extends @Nullable DateTime> readClientLastContactTimeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getClientLastContactTimeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ClientLastContactTime",
                            true,
                            DateTime.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable DateTime) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeClientLastContactTimeAsync(@Nullable DateTime value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getClientLastContactTimeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ClientLastContactTime",
                        value,
                        DateTime.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getDeleteReferencesCountNode() throws UaException {
    return ClientNodeSupport.await(getDeleteReferencesCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getDeleteReferencesCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DeleteReferencesCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServiceCounterDataType readDeleteReferencesCount() throws UaException {
    return ClientNodeSupport.await(readDeleteReferencesCountAsync());
  }

  @Override
  public void writeDeleteReferencesCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDeleteReferencesCountAsync(value)),
        "http://opcfoundation.org/UA/}DeleteReferencesCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readDeleteReferencesCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDeleteReferencesCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DeleteReferencesCount",
                            true,
                            ServiceCounterDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServiceCounterDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDeleteReferencesCountAsync(
      @Nullable ServiceCounterDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDeleteReferencesCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DeleteReferencesCount",
                        value,
                        ServiceCounterDataType.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getMaxResponseMessageSizeNode() throws UaException {
    return ClientNodeSupport.await(getMaxResponseMessageSizeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getMaxResponseMessageSizeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxResponseMessageSize",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxResponseMessageSize() throws UaException {
    return ClientNodeSupport.await(readMaxResponseMessageSizeAsync());
  }

  @Override
  public void writeMaxResponseMessageSize(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxResponseMessageSizeAsync(value)),
        "http://opcfoundation.org/UA/}MaxResponseMessageSize");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxResponseMessageSizeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxResponseMessageSizeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxResponseMessageSize",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxResponseMessageSizeAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxResponseMessageSizeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxResponseMessageSize",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getSetMonitoringModeCountNode() throws UaException {
    return ClientNodeSupport.await(getSetMonitoringModeCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getSetMonitoringModeCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SetMonitoringModeCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServiceCounterDataType readSetMonitoringModeCount() throws UaException {
    return ClientNodeSupport.await(readSetMonitoringModeCountAsync());
  }

  @Override
  public void writeSetMonitoringModeCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSetMonitoringModeCountAsync(value)),
        "http://opcfoundation.org/UA/}SetMonitoringModeCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readSetMonitoringModeCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSetMonitoringModeCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SetMonitoringModeCount",
                            true,
                            ServiceCounterDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServiceCounterDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSetMonitoringModeCountAsync(
      @Nullable ServiceCounterDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSetMonitoringModeCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SetMonitoringModeCount",
                        value,
                        ServiceCounterDataType.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getSetPublishingModeCountNode() throws UaException {
    return ClientNodeSupport.await(getSetPublishingModeCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getSetPublishingModeCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SetPublishingModeCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServiceCounterDataType readSetPublishingModeCount() throws UaException {
    return ClientNodeSupport.await(readSetPublishingModeCountAsync());
  }

  @Override
  public void writeSetPublishingModeCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSetPublishingModeCountAsync(value)),
        "http://opcfoundation.org/UA/}SetPublishingModeCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readSetPublishingModeCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSetPublishingModeCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SetPublishingModeCount",
                            true,
                            ServiceCounterDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServiceCounterDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSetPublishingModeCountAsync(
      @Nullable ServiceCounterDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSetPublishingModeCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SetPublishingModeCount",
                        value,
                        ServiceCounterDataType.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getCreateSubscriptionCountNode() throws UaException {
    return ClientNodeSupport.await(getCreateSubscriptionCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getCreateSubscriptionCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CreateSubscriptionCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServiceCounterDataType readCreateSubscriptionCount() throws UaException {
    return ClientNodeSupport.await(readCreateSubscriptionCountAsync());
  }

  @Override
  public void writeCreateSubscriptionCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeCreateSubscriptionCountAsync(value)),
        "http://opcfoundation.org/UA/}CreateSubscriptionCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readCreateSubscriptionCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getCreateSubscriptionCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}CreateSubscriptionCount",
                            true,
                            ServiceCounterDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServiceCounterDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeCreateSubscriptionCountAsync(
      @Nullable ServiceCounterDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getCreateSubscriptionCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}CreateSubscriptionCount",
                        value,
                        ServiceCounterDataType.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getModifySubscriptionCountNode() throws UaException {
    return ClientNodeSupport.await(getModifySubscriptionCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getModifySubscriptionCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ModifySubscriptionCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServiceCounterDataType readModifySubscriptionCount() throws UaException {
    return ClientNodeSupport.await(readModifySubscriptionCountAsync());
  }

  @Override
  public void writeModifySubscriptionCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeModifySubscriptionCountAsync(value)),
        "http://opcfoundation.org/UA/}ModifySubscriptionCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readModifySubscriptionCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getModifySubscriptionCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ModifySubscriptionCount",
                            true,
                            ServiceCounterDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServiceCounterDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeModifySubscriptionCountAsync(
      @Nullable ServiceCounterDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getModifySubscriptionCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ModifySubscriptionCount",
                        value,
                        ServiceCounterDataType.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getDeleteSubscriptionsCountNode() throws UaException {
    return ClientNodeSupport.await(getDeleteSubscriptionsCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getDeleteSubscriptionsCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DeleteSubscriptionsCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServiceCounterDataType readDeleteSubscriptionsCount() throws UaException {
    return ClientNodeSupport.await(readDeleteSubscriptionsCountAsync());
  }

  @Override
  public void writeDeleteSubscriptionsCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDeleteSubscriptionsCountAsync(value)),
        "http://opcfoundation.org/UA/}DeleteSubscriptionsCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readDeleteSubscriptionsCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDeleteSubscriptionsCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DeleteSubscriptionsCount",
                            true,
                            ServiceCounterDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServiceCounterDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDeleteSubscriptionsCountAsync(
      @Nullable ServiceCounterDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDeleteSubscriptionsCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DeleteSubscriptionsCount",
                        value,
                        ServiceCounterDataType.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getUnauthorizedRequestCountNode() throws UaException {
    return ClientNodeSupport.await(getUnauthorizedRequestCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getUnauthorizedRequestCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "UnauthorizedRequestCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readUnauthorizedRequestCount() throws UaException {
    return ClientNodeSupport.await(readUnauthorizedRequestCountAsync());
  }

  @Override
  public void writeUnauthorizedRequestCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeUnauthorizedRequestCountAsync(value)),
        "http://opcfoundation.org/UA/}UnauthorizedRequestCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readUnauthorizedRequestCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getUnauthorizedRequestCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}UnauthorizedRequestCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeUnauthorizedRequestCountAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getUnauthorizedRequestCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}UnauthorizedRequestCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getCreateMonitoredItemsCountNode() throws UaException {
    return ClientNodeSupport.await(getCreateMonitoredItemsCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getCreateMonitoredItemsCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CreateMonitoredItemsCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServiceCounterDataType readCreateMonitoredItemsCount() throws UaException {
    return ClientNodeSupport.await(readCreateMonitoredItemsCountAsync());
  }

  @Override
  public void writeCreateMonitoredItemsCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeCreateMonitoredItemsCountAsync(value)),
        "http://opcfoundation.org/UA/}CreateMonitoredItemsCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readCreateMonitoredItemsCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getCreateMonitoredItemsCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}CreateMonitoredItemsCount",
                            true,
                            ServiceCounterDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServiceCounterDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeCreateMonitoredItemsCountAsync(
      @Nullable ServiceCounterDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getCreateMonitoredItemsCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}CreateMonitoredItemsCount",
                        value,
                        ServiceCounterDataType.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getCurrentSubscriptionsCountNode() throws UaException {
    return ClientNodeSupport.await(getCurrentSubscriptionsCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getCurrentSubscriptionsCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CurrentSubscriptionsCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readCurrentSubscriptionsCount() throws UaException {
    return ClientNodeSupport.await(readCurrentSubscriptionsCountAsync());
  }

  @Override
  public void writeCurrentSubscriptionsCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeCurrentSubscriptionsCountAsync(value)),
        "http://opcfoundation.org/UA/}CurrentSubscriptionsCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readCurrentSubscriptionsCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getCurrentSubscriptionsCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}CurrentSubscriptionsCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeCurrentSubscriptionsCountAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getCurrentSubscriptionsCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}CurrentSubscriptionsCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getDeleteMonitoredItemsCountNode() throws UaException {
    return ClientNodeSupport.await(getDeleteMonitoredItemsCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getDeleteMonitoredItemsCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DeleteMonitoredItemsCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServiceCounterDataType readDeleteMonitoredItemsCount() throws UaException {
    return ClientNodeSupport.await(readDeleteMonitoredItemsCountAsync());
  }

  @Override
  public void writeDeleteMonitoredItemsCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDeleteMonitoredItemsCountAsync(value)),
        "http://opcfoundation.org/UA/}DeleteMonitoredItemsCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readDeleteMonitoredItemsCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDeleteMonitoredItemsCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DeleteMonitoredItemsCount",
                            true,
                            ServiceCounterDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServiceCounterDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDeleteMonitoredItemsCountAsync(
      @Nullable ServiceCounterDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDeleteMonitoredItemsCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DeleteMonitoredItemsCount",
                        value,
                        ServiceCounterDataType.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getModifyMonitoredItemsCountNode() throws UaException {
    return ClientNodeSupport.await(getModifyMonitoredItemsCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getModifyMonitoredItemsCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ModifyMonitoredItemsCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServiceCounterDataType readModifyMonitoredItemsCount() throws UaException {
    return ClientNodeSupport.await(readModifyMonitoredItemsCountAsync());
  }

  @Override
  public void writeModifyMonitoredItemsCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeModifyMonitoredItemsCountAsync(value)),
        "http://opcfoundation.org/UA/}ModifyMonitoredItemsCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readModifyMonitoredItemsCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getModifyMonitoredItemsCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ModifyMonitoredItemsCount",
                            true,
                            ServiceCounterDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServiceCounterDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeModifyMonitoredItemsCountAsync(
      @Nullable ServiceCounterDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getModifyMonitoredItemsCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ModifyMonitoredItemsCount",
                        value,
                        ServiceCounterDataType.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getCurrentMonitoredItemsCountNode() throws UaException {
    return ClientNodeSupport.await(getCurrentMonitoredItemsCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getCurrentMonitoredItemsCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CurrentMonitoredItemsCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readCurrentMonitoredItemsCount() throws UaException {
    return ClientNodeSupport.await(readCurrentMonitoredItemsCountAsync());
  }

  @Override
  public void writeCurrentMonitoredItemsCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeCurrentMonitoredItemsCountAsync(value)),
        "http://opcfoundation.org/UA/}CurrentMonitoredItemsCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readCurrentMonitoredItemsCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getCurrentMonitoredItemsCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}CurrentMonitoredItemsCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeCurrentMonitoredItemsCountAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getCurrentMonitoredItemsCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}CurrentMonitoredItemsCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getTransferSubscriptionsCountNode() throws UaException {
    return ClientNodeSupport.await(getTransferSubscriptionsCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getTransferSubscriptionsCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "TransferSubscriptionsCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServiceCounterDataType readTransferSubscriptionsCount() throws UaException {
    return ClientNodeSupport.await(readTransferSubscriptionsCountAsync());
  }

  @Override
  public void writeTransferSubscriptionsCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeTransferSubscriptionsCountAsync(value)),
        "http://opcfoundation.org/UA/}TransferSubscriptionsCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readTransferSubscriptionsCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getTransferSubscriptionsCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}TransferSubscriptionsCount",
                            true,
                            ServiceCounterDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServiceCounterDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTransferSubscriptionsCountAsync(
      @Nullable ServiceCounterDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getTransferSubscriptionsCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}TransferSubscriptionsCount",
                        value,
                        ServiceCounterDataType.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getCurrentPublishRequestsInQueueNode() throws UaException {
    return ClientNodeSupport.await(getCurrentPublishRequestsInQueueNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getCurrentPublishRequestsInQueueNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CurrentPublishRequestsInQueue",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readCurrentPublishRequestsInQueue() throws UaException {
    return ClientNodeSupport.await(readCurrentPublishRequestsInQueueAsync());
  }

  @Override
  public void writeCurrentPublishRequestsInQueue(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeCurrentPublishRequestsInQueueAsync(value)),
        "http://opcfoundation.org/UA/}CurrentPublishRequestsInQueue");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readCurrentPublishRequestsInQueueAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getCurrentPublishRequestsInQueueNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}CurrentPublishRequestsInQueue",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeCurrentPublishRequestsInQueueAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getCurrentPublishRequestsInQueueNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}CurrentPublishRequestsInQueue",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getTranslateBrowsePathsToNodeIdsCountNode() throws UaException {
    return ClientNodeSupport.await(getTranslateBrowsePathsToNodeIdsCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode>
      getTranslateBrowsePathsToNodeIdsCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "TranslateBrowsePathsToNodeIdsCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServiceCounterDataType readTranslateBrowsePathsToNodeIdsCount()
      throws UaException {
    return ClientNodeSupport.await(readTranslateBrowsePathsToNodeIdsCountAsync());
  }

  @Override
  public void writeTranslateBrowsePathsToNodeIdsCount(@Nullable ServiceCounterDataType value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeTranslateBrowsePathsToNodeIdsCountAsync(value)),
        "http://opcfoundation.org/UA/}TranslateBrowsePathsToNodeIdsCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readTranslateBrowsePathsToNodeIdsCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getTranslateBrowsePathsToNodeIdsCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}TranslateBrowsePathsToNodeIdsCount",
                            true,
                            ServiceCounterDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServiceCounterDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTranslateBrowsePathsToNodeIdsCountAsync(
      @Nullable ServiceCounterDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getTranslateBrowsePathsToNodeIdsCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}TranslateBrowsePathsToNodeIdsCount",
                        value,
                        ServiceCounterDataType.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getCallCountNode() throws UaException {
    return ClientNodeSupport.await(getCallCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getCallCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CallCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServiceCounterDataType readCallCount() throws UaException {
    return ClientNodeSupport.await(readCallCountAsync());
  }

  @Override
  public void writeCallCount(@Nullable ServiceCounterDataType value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeCallCountAsync(value)),
        "http://opcfoundation.org/UA/}CallCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServiceCounterDataType> readCallCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getCallCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}CallCount",
                            true,
                            ServiceCounterDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServiceCounterDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeCallCountAsync(@Nullable ServiceCounterDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getCallCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}CallCount",
                        value,
                        ServiceCounterDataType.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getLocaleIdsNode() throws UaException {
    return ClientNodeSupport.await(getLocaleIdsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getLocaleIdsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LocaleIds",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable String @Nullable [] readLocaleIds() throws UaException {
    return ClientNodeSupport.await(readLocaleIdsAsync());
  }

  @Override
  public void writeLocaleIds(@Nullable String @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeLocaleIdsAsync(value)),
        "http://opcfoundation.org/UA/}LocaleIds");
  }

  @Override
  public CompletableFuture<? extends @Nullable String @Nullable []> readLocaleIdsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getLocaleIdsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}LocaleIds",
                            true,
                            String.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeLocaleIdsAsync(@Nullable String @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getLocaleIdsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}LocaleIds",
                        value,
                        String.class,
                        1,
                        null)));
  }

  @Override
  public UaVariableNode getReadCountNode() throws UaException {
    return ClientNodeSupport.await(getReadCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getReadCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ReadCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable ServiceCounterDataType readReadCount() throws UaException {
    return ClientNodeSupport.await(readReadCountAsync());
  }

  @Override
  public void writeReadCount(@Nullable ServiceCounterDataType value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeReadCountAsync(value)),
        "http://opcfoundation.org/UA/}ReadCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServiceCounterDataType> readReadCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getReadCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ReadCount",
                            true,
                            ServiceCounterDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ServiceCounterDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeReadCountAsync(@Nullable ServiceCounterDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getReadCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ReadCount",
                        value,
                        ServiceCounterDataType.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getServerUriNode() throws UaException {
    return ClientNodeSupport.await(getServerUriNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getServerUriNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ServerUri",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable String readServerUri() throws UaException {
    return ClientNodeSupport.await(readServerUriAsync());
  }

  @Override
  public void writeServerUri(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeServerUriAsync(value)),
        "http://opcfoundation.org/UA/}ServerUri");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readServerUriAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getServerUriNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ServerUri",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeServerUriAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getServerUriNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ServerUri",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getSessionIdNode() throws UaException {
    return ClientNodeSupport.await(getSessionIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getSessionIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SessionId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable NodeId readSessionId() throws UaException {
    return ClientNodeSupport.await(readSessionIdAsync());
  }

  @Override
  public void writeSessionId(@Nullable NodeId value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSessionIdAsync(value)),
        "http://opcfoundation.org/UA/}SessionId");
  }

  @Override
  public CompletableFuture<? extends @Nullable NodeId> readSessionIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSessionIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SessionId",
                            true,
                            NodeId.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable NodeId) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSessionIdAsync(@Nullable NodeId value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSessionIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SessionId",
                        value,
                        NodeId.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable SessionDiagnosticsDataType readTypedValue() throws UaException {
    return ClientNodeSupport.await(readTypedValueAsync());
  }

  @Override
  public void writeTypedValue(@Nullable SessionDiagnosticsDataType value) throws UaException {
    ClientNodeSupport.good(ClientNodeSupport.await(writeTypedValueAsync(value)), "Value");
  }

  @Override
  public CompletableFuture<? extends @Nullable SessionDiagnosticsDataType> readTypedValueAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    CompletableFuture.completedFuture(this),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "Value",
                            true,
                            SessionDiagnosticsDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable SessionDiagnosticsDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTypedValueAsync(
      @Nullable SessionDiagnosticsDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "Value",
                        value,
                        SessionDiagnosticsDataType.class,
                        -1,
                        null)));
  }
}
