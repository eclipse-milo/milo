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
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link HistoryServerCapabilitiesType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.7.2">Model
 *     documentation</a>
 */
public class HistoryServerCapabilitiesTypeNode extends BaseObjectTypeNode
    implements HistoryServerCapabilitiesType {
  public HistoryServerCapabilitiesTypeNode(
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
  public FolderTypeNode getAggregateFunctionsNode() throws UaException {
    return ClientNodeSupport.await(getAggregateFunctionsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends FolderTypeNode> getAggregateFunctionsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "AggregateFunctions",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        FolderTypeNode.class)));
  }

  @Override
  public PropertyTypeNode getDeleteRawCapabilityNode() throws UaException {
    return ClientNodeSupport.await(getDeleteRawCapabilityNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDeleteRawCapabilityNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DeleteRawCapability",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readDeleteRawCapability() throws UaException {
    return ClientNodeSupport.await(readDeleteRawCapabilityAsync());
  }

  @Override
  public void writeDeleteRawCapability(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDeleteRawCapabilityAsync(value)),
        "http://opcfoundation.org/UA/}DeleteRawCapability");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readDeleteRawCapabilityAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDeleteRawCapabilityNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DeleteRawCapability",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDeleteRawCapabilityAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDeleteRawCapabilityNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DeleteRawCapability",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getMaxReturnDataValuesNode() throws UaException {
    return ClientNodeSupport.await(getMaxReturnDataValuesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxReturnDataValuesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxReturnDataValues",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxReturnDataValues() throws UaException {
    return ClientNodeSupport.await(readMaxReturnDataValuesAsync());
  }

  @Override
  public void writeMaxReturnDataValues(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxReturnDataValuesAsync(value)),
        "http://opcfoundation.org/UA/}MaxReturnDataValues");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxReturnDataValuesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxReturnDataValuesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxReturnDataValues",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxReturnDataValuesAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxReturnDataValuesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxReturnDataValues",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getInsertDataCapabilityNode() throws UaException {
    return ClientNodeSupport.await(getInsertDataCapabilityNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getInsertDataCapabilityNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "InsertDataCapability",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readInsertDataCapability() throws UaException {
    return ClientNodeSupport.await(readInsertDataCapabilityAsync());
  }

  @Override
  public void writeInsertDataCapability(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeInsertDataCapabilityAsync(value)),
        "http://opcfoundation.org/UA/}InsertDataCapability");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readInsertDataCapabilityAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getInsertDataCapabilityNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}InsertDataCapability",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeInsertDataCapabilityAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getInsertDataCapabilityNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}InsertDataCapability",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getMaxReturnEventValuesNode() throws UaException {
    return ClientNodeSupport.await(getMaxReturnEventValuesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxReturnEventValuesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxReturnEventValues",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxReturnEventValues() throws UaException {
    return ClientNodeSupport.await(readMaxReturnEventValuesAsync());
  }

  @Override
  public void writeMaxReturnEventValues(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxReturnEventValuesAsync(value)),
        "http://opcfoundation.org/UA/}MaxReturnEventValues");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxReturnEventValuesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxReturnEventValuesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxReturnEventValues",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxReturnEventValuesAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxReturnEventValuesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxReturnEventValues",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getUpdateDataCapabilityNode() throws UaException {
    return ClientNodeSupport.await(getUpdateDataCapabilityNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUpdateDataCapabilityNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "UpdateDataCapability",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readUpdateDataCapability() throws UaException {
    return ClientNodeSupport.await(readUpdateDataCapabilityAsync());
  }

  @Override
  public void writeUpdateDataCapability(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeUpdateDataCapabilityAsync(value)),
        "http://opcfoundation.org/UA/}UpdateDataCapability");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readUpdateDataCapabilityAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getUpdateDataCapabilityNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}UpdateDataCapability",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeUpdateDataCapabilityAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getUpdateDataCapabilityNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}UpdateDataCapability",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getDeleteEventCapabilityNode() throws UaException {
    return ClientNodeSupport.await(getDeleteEventCapabilityNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDeleteEventCapabilityNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DeleteEventCapability",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readDeleteEventCapability() throws UaException {
    return ClientNodeSupport.await(readDeleteEventCapabilityAsync());
  }

  @Override
  public void writeDeleteEventCapability(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDeleteEventCapabilityAsync(value)),
        "http://opcfoundation.org/UA/}DeleteEventCapability");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readDeleteEventCapabilityAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDeleteEventCapabilityNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DeleteEventCapability",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDeleteEventCapabilityAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDeleteEventCapabilityNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DeleteEventCapability",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getInsertEventCapabilityNode() throws UaException {
    return ClientNodeSupport.await(getInsertEventCapabilityNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getInsertEventCapabilityNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "InsertEventCapability",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readInsertEventCapability() throws UaException {
    return ClientNodeSupport.await(readInsertEventCapabilityAsync());
  }

  @Override
  public void writeInsertEventCapability(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeInsertEventCapabilityAsync(value)),
        "http://opcfoundation.org/UA/}InsertEventCapability");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readInsertEventCapabilityAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getInsertEventCapabilityNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}InsertEventCapability",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeInsertEventCapabilityAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getInsertEventCapabilityNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}InsertEventCapability",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getReplaceDataCapabilityNode() throws UaException {
    return ClientNodeSupport.await(getReplaceDataCapabilityNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getReplaceDataCapabilityNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ReplaceDataCapability",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readReplaceDataCapability() throws UaException {
    return ClientNodeSupport.await(readReplaceDataCapabilityAsync());
  }

  @Override
  public void writeReplaceDataCapability(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeReplaceDataCapabilityAsync(value)),
        "http://opcfoundation.org/UA/}ReplaceDataCapability");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readReplaceDataCapabilityAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getReplaceDataCapabilityNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ReplaceDataCapability",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeReplaceDataCapabilityAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getReplaceDataCapabilityNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ReplaceDataCapability",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getUpdateEventCapabilityNode() throws UaException {
    return ClientNodeSupport.await(getUpdateEventCapabilityNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUpdateEventCapabilityNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "UpdateEventCapability",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readUpdateEventCapability() throws UaException {
    return ClientNodeSupport.await(readUpdateEventCapabilityAsync());
  }

  @Override
  public void writeUpdateEventCapability(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeUpdateEventCapabilityAsync(value)),
        "http://opcfoundation.org/UA/}UpdateEventCapability");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readUpdateEventCapabilityAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getUpdateEventCapabilityNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}UpdateEventCapability",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeUpdateEventCapabilityAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getUpdateEventCapabilityNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}UpdateEventCapability",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getDeleteAtTimeCapabilityNode() throws UaException {
    return ClientNodeSupport.await(getDeleteAtTimeCapabilityNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDeleteAtTimeCapabilityNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DeleteAtTimeCapability",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readDeleteAtTimeCapability() throws UaException {
    return ClientNodeSupport.await(readDeleteAtTimeCapabilityAsync());
  }

  @Override
  public void writeDeleteAtTimeCapability(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDeleteAtTimeCapabilityAsync(value)),
        "http://opcfoundation.org/UA/}DeleteAtTimeCapability");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readDeleteAtTimeCapabilityAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDeleteAtTimeCapabilityNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DeleteAtTimeCapability",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDeleteAtTimeCapabilityAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDeleteAtTimeCapabilityNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DeleteAtTimeCapability",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getReplaceEventCapabilityNode() throws UaException {
    return ClientNodeSupport.await(getReplaceEventCapabilityNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getReplaceEventCapabilityNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ReplaceEventCapability",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readReplaceEventCapability() throws UaException {
    return ClientNodeSupport.await(readReplaceEventCapabilityAsync());
  }

  @Override
  public void writeReplaceEventCapability(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeReplaceEventCapabilityAsync(value)),
        "http://opcfoundation.org/UA/}ReplaceEventCapability");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readReplaceEventCapabilityAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getReplaceEventCapabilityNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ReplaceEventCapability",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeReplaceEventCapabilityAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getReplaceEventCapabilityNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ReplaceEventCapability",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getServerTimestampSupportedNode() throws UaException {
    return ClientNodeSupport.await(getServerTimestampSupportedNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getServerTimestampSupportedNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ServerTimestampSupported",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readServerTimestampSupported() throws UaException {
    return ClientNodeSupport.await(readServerTimestampSupportedAsync());
  }

  @Override
  public void writeServerTimestampSupported(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeServerTimestampSupportedAsync(value)),
        "http://opcfoundation.org/UA/}ServerTimestampSupported");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readServerTimestampSupportedAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getServerTimestampSupportedNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ServerTimestampSupported",
                            false,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeServerTimestampSupportedAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getServerTimestampSupportedNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ServerTimestampSupported",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getInsertAnnotationCapabilityNode() throws UaException {
    return ClientNodeSupport.await(getInsertAnnotationCapabilityNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getInsertAnnotationCapabilityNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "InsertAnnotationCapability",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readInsertAnnotationCapability() throws UaException {
    return ClientNodeSupport.await(readInsertAnnotationCapabilityAsync());
  }

  @Override
  public void writeInsertAnnotationCapability(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeInsertAnnotationCapabilityAsync(value)),
        "http://opcfoundation.org/UA/}InsertAnnotationCapability");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readInsertAnnotationCapabilityAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getInsertAnnotationCapabilityNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}InsertAnnotationCapability",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeInsertAnnotationCapabilityAsync(
      @Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getInsertAnnotationCapabilityNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}InsertAnnotationCapability",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getAccessHistoryDataCapabilityNode() throws UaException {
    return ClientNodeSupport.await(getAccessHistoryDataCapabilityNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getAccessHistoryDataCapabilityNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "AccessHistoryDataCapability",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readAccessHistoryDataCapability() throws UaException {
    return ClientNodeSupport.await(readAccessHistoryDataCapabilityAsync());
  }

  @Override
  public void writeAccessHistoryDataCapability(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeAccessHistoryDataCapabilityAsync(value)),
        "http://opcfoundation.org/UA/}AccessHistoryDataCapability");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readAccessHistoryDataCapabilityAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getAccessHistoryDataCapabilityNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}AccessHistoryDataCapability",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeAccessHistoryDataCapabilityAsync(
      @Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getAccessHistoryDataCapabilityNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}AccessHistoryDataCapability",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getAccessHistoryEventsCapabilityNode() throws UaException {
    return ClientNodeSupport.await(getAccessHistoryEventsCapabilityNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getAccessHistoryEventsCapabilityNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "AccessHistoryEventsCapability",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readAccessHistoryEventsCapability() throws UaException {
    return ClientNodeSupport.await(readAccessHistoryEventsCapabilityAsync());
  }

  @Override
  public void writeAccessHistoryEventsCapability(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeAccessHistoryEventsCapabilityAsync(value)),
        "http://opcfoundation.org/UA/}AccessHistoryEventsCapability");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readAccessHistoryEventsCapabilityAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getAccessHistoryEventsCapabilityNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}AccessHistoryEventsCapability",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeAccessHistoryEventsCapabilityAsync(
      @Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getAccessHistoryEventsCapabilityNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}AccessHistoryEventsCapability",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }
}
