package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.AlarmMask;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link AlarmStateVariableType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/8.2">Model
 *     documentation</a>
 */
public class AlarmStateVariableTypeNode extends BaseDataVariableTypeNode
    implements AlarmStateVariableType {
  public AlarmStateVariableTypeNode(
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
  public PropertyTypeNode getActiveCountNode() throws UaException {
    return ClientNodeSupport.await(getActiveCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getActiveCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ActiveCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readActiveCount() throws UaException {
    return ClientNodeSupport.await(readActiveCountAsync());
  }

  @Override
  public void writeActiveCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeActiveCountAsync(value)),
        "http://opcfoundation.org/UA/}ActiveCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readActiveCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getActiveCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ActiveCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeActiveCountAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getActiveCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ActiveCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getUnconfirmedCountNode() throws UaException {
    return ClientNodeSupport.await(getUnconfirmedCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUnconfirmedCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "UnconfirmedCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readUnconfirmedCount() throws UaException {
    return ClientNodeSupport.await(readUnconfirmedCountAsync());
  }

  @Override
  public void writeUnconfirmedCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeUnconfirmedCountAsync(value)),
        "http://opcfoundation.org/UA/}UnconfirmedCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readUnconfirmedCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getUnconfirmedCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}UnconfirmedCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeUnconfirmedCountAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getUnconfirmedCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}UnconfirmedCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getUnacknowledgedCountNode() throws UaException {
    return ClientNodeSupport.await(getUnacknowledgedCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUnacknowledgedCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "UnacknowledgedCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readUnacknowledgedCount() throws UaException {
    return ClientNodeSupport.await(readUnacknowledgedCountAsync());
  }

  @Override
  public void writeUnacknowledgedCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeUnacknowledgedCountAsync(value)),
        "http://opcfoundation.org/UA/}UnacknowledgedCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readUnacknowledgedCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getUnacknowledgedCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}UnacknowledgedCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeUnacknowledgedCountAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getUnacknowledgedCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}UnacknowledgedCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getHighestUnackSeverityNode() throws UaException {
    return ClientNodeSupport.await(getHighestUnackSeverityNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getHighestUnackSeverityNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "HighestUnackSeverity",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UShort readHighestUnackSeverity() throws UaException {
    return ClientNodeSupport.await(readHighestUnackSeverityAsync());
  }

  @Override
  public void writeHighestUnackSeverity(@Nullable UShort value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeHighestUnackSeverityAsync(value)),
        "http://opcfoundation.org/UA/}HighestUnackSeverity");
  }

  @Override
  public CompletableFuture<? extends @Nullable UShort> readHighestUnackSeverityAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getHighestUnackSeverityNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}HighestUnackSeverity",
                            true,
                            UShort.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UShort) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeHighestUnackSeverityAsync(@Nullable UShort value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getHighestUnackSeverityNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}HighestUnackSeverity",
                        value,
                        UShort.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getHighestActiveSeverityNode() throws UaException {
    return ClientNodeSupport.await(getHighestActiveSeverityNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getHighestActiveSeverityNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "HighestActiveSeverity",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UShort readHighestActiveSeverity() throws UaException {
    return ClientNodeSupport.await(readHighestActiveSeverityAsync());
  }

  @Override
  public void writeHighestActiveSeverity(@Nullable UShort value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeHighestActiveSeverityAsync(value)),
        "http://opcfoundation.org/UA/}HighestActiveSeverity");
  }

  @Override
  public CompletableFuture<? extends @Nullable UShort> readHighestActiveSeverityAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getHighestActiveSeverityNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}HighestActiveSeverity",
                            true,
                            UShort.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UShort) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeHighestActiveSeverityAsync(@Nullable UShort value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getHighestActiveSeverityNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}HighestActiveSeverity",
                        value,
                        UShort.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getFilterNode() throws UaException {
    return ClientNodeSupport.await(getFilterNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getFilterNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Filter",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable ContentFilter readFilter() throws UaException {
    return ClientNodeSupport.await(readFilterAsync());
  }

  @Override
  public void writeFilter(@Nullable ContentFilter value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeFilterAsync(value)), "http://opcfoundation.org/UA/}Filter");
  }

  @Override
  public CompletableFuture<? extends @Nullable ContentFilter> readFilterAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getFilterNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Filter",
                            true,
                            ContentFilter.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ContentFilter) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeFilterAsync(@Nullable ContentFilter value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getFilterNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Filter",
                        value,
                        ContentFilter.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable AlarmMask readTypedValue() throws UaException {
    return ClientNodeSupport.await(readTypedValueAsync());
  }

  @Override
  public void writeTypedValue(@Nullable AlarmMask value) throws UaException {
    ClientNodeSupport.good(ClientNodeSupport.await(writeTypedValueAsync(value)), "Value");
  }

  @Override
  public CompletableFuture<? extends @Nullable AlarmMask> readTypedValueAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    CompletableFuture.completedFuture(this),
                    n ->
                        ClientNodeSupport.read(
                            client, n, this, "Value", true, AlarmMask.class, -1, null)),
                v -> CompletableFuture.completedFuture((@Nullable AlarmMask) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTypedValueAsync(@Nullable AlarmMask value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                n ->
                    ClientNodeSupport.write(
                        client, n, this, "Value", value, AlarmMask.class, -1, null)));
  }
}
