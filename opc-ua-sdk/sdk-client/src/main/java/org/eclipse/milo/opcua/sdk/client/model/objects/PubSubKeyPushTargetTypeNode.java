package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubKeyPushTargetTypeConnectSecurityGroups;
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubKeyPushTargetTypeDisconnectSecurityGroups;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
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
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link PubSubKeyPushTargetType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.1">Model
 *     documentation</a>
 */
public class PubSubKeyPushTargetTypeNode extends BaseObjectTypeNode
    implements PubSubKeyPushTargetType {
  public PubSubKeyPushTargetTypeNode(
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
  public PropertyTypeNode getEndpointUrlNode() throws UaException {
    return ClientNodeSupport.await(getEndpointUrlNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getEndpointUrlNodeAsync() {
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
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
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
  public PropertyTypeNode getRetryIntervalNode() throws UaException {
    return ClientNodeSupport.await(getRetryIntervalNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getRetryIntervalNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "RetryInterval",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Double readRetryInterval() throws UaException {
    return ClientNodeSupport.await(readRetryIntervalAsync());
  }

  @Override
  public void writeRetryInterval(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeRetryIntervalAsync(value)),
        "http://opcfoundation.org/UA/}RetryInterval");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readRetryIntervalAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getRetryIntervalNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}RetryInterval",
                            true,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeRetryIntervalAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getRetryIntervalNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}RetryInterval",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getUserTokenTypeNode() throws UaException {
    return ClientNodeSupport.await(getUserTokenTypeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUserTokenTypeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "UserTokenType",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UserTokenPolicy readUserTokenType() throws UaException {
    return ClientNodeSupport.await(readUserTokenTypeAsync());
  }

  @Override
  public void writeUserTokenType(@Nullable UserTokenPolicy value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeUserTokenTypeAsync(value)),
        "http://opcfoundation.org/UA/}UserTokenType");
  }

  @Override
  public CompletableFuture<? extends @Nullable UserTokenPolicy> readUserTokenTypeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getUserTokenTypeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}UserTokenType",
                            true,
                            UserTokenPolicy.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UserTokenPolicy) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeUserTokenTypeAsync(@Nullable UserTokenPolicy value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getUserTokenTypeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}UserTokenType",
                        value,
                        UserTokenPolicy.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getApplicationUriNode() throws UaException {
    return ClientNodeSupport.await(getApplicationUriNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getApplicationUriNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ApplicationUri",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readApplicationUri() throws UaException {
    return ClientNodeSupport.await(readApplicationUriAsync());
  }

  @Override
  public void writeApplicationUri(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeApplicationUriAsync(value)),
        "http://opcfoundation.org/UA/}ApplicationUri");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readApplicationUriAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getApplicationUriNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ApplicationUri",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeApplicationUriAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getApplicationUriNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ApplicationUri",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getLastPushErrorTimeNode() throws UaException {
    return ClientNodeSupport.await(getLastPushErrorTimeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLastPushErrorTimeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LastPushErrorTime",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable DateTime readLastPushErrorTime() throws UaException {
    return ClientNodeSupport.await(readLastPushErrorTimeAsync());
  }

  @Override
  public void writeLastPushErrorTime(@Nullable DateTime value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeLastPushErrorTimeAsync(value)),
        "http://opcfoundation.org/UA/}LastPushErrorTime");
  }

  @Override
  public CompletableFuture<? extends @Nullable DateTime> readLastPushErrorTimeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getLastPushErrorTimeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}LastPushErrorTime",
                            true,
                            DateTime.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable DateTime) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeLastPushErrorTimeAsync(@Nullable DateTime value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getLastPushErrorTimeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}LastPushErrorTime",
                        value,
                        DateTime.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getRequestedKeyCountNode() throws UaException {
    return ClientNodeSupport.await(getRequestedKeyCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getRequestedKeyCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "RequestedKeyCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UShort readRequestedKeyCount() throws UaException {
    return ClientNodeSupport.await(readRequestedKeyCountAsync());
  }

  @Override
  public void writeRequestedKeyCount(@Nullable UShort value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeRequestedKeyCountAsync(value)),
        "http://opcfoundation.org/UA/}RequestedKeyCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UShort> readRequestedKeyCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getRequestedKeyCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}RequestedKeyCount",
                            true,
                            UShort.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UShort) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeRequestedKeyCountAsync(@Nullable UShort value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getRequestedKeyCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}RequestedKeyCount",
                        value,
                        UShort.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getSecurityPolicyUriNode() throws UaException {
    return ClientNodeSupport.await(getSecurityPolicyUriNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSecurityPolicyUriNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SecurityPolicyUri",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readSecurityPolicyUri() throws UaException {
    return ClientNodeSupport.await(readSecurityPolicyUriAsync());
  }

  @Override
  public void writeSecurityPolicyUri(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSecurityPolicyUriAsync(value)),
        "http://opcfoundation.org/UA/}SecurityPolicyUri");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readSecurityPolicyUriAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSecurityPolicyUriNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SecurityPolicyUri",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSecurityPolicyUriAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSecurityPolicyUriNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SecurityPolicyUri",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getLastPushExecutionTimeNode() throws UaException {
    return ClientNodeSupport.await(getLastPushExecutionTimeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLastPushExecutionTimeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LastPushExecutionTime",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable DateTime readLastPushExecutionTime() throws UaException {
    return ClientNodeSupport.await(readLastPushExecutionTimeAsync());
  }

  @Override
  public void writeLastPushExecutionTime(@Nullable DateTime value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeLastPushExecutionTimeAsync(value)),
        "http://opcfoundation.org/UA/}LastPushExecutionTime");
  }

  @Override
  public CompletableFuture<? extends @Nullable DateTime> readLastPushExecutionTimeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getLastPushExecutionTimeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}LastPushExecutionTime",
                            true,
                            DateTime.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable DateTime) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeLastPushExecutionTimeAsync(@Nullable DateTime value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getLastPushExecutionTimeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}LastPushExecutionTime",
                        value,
                        DateTime.class,
                        -1,
                        null)));
  }

  @Override
  public UaMethodNode getConnectSecurityGroupsMethodNode() throws UaException {
    return ClientNodeSupport.await(getConnectSecurityGroupsMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getConnectSecurityGroupsMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "ConnectSecurityGroups",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public StatusCode @Nullable [] connectSecurityGroups(NodeId @Nullable [] securityGroupIds)
      throws UaException {
    return ClientNodeSupport.await(connectSecurityGroupsAsync(securityGroupIds));
  }

  @Override
  public MethodCallResult<StatusCode @Nullable []> callConnectSecurityGroups(
      NodeId @Nullable [] securityGroupIds) throws UaException {
    return ClientNodeSupport.await(callConnectSecurityGroupsAsync(securityGroupIds));
  }

  @Override
  public MethodCallResult<StatusCode @Nullable []> callConnectSecurityGroupsWith(
      MethodCallOptions options, NodeId @Nullable [] securityGroupIds) throws UaException {
    return ClientNodeSupport.await(callConnectSecurityGroupsWithAsync(options, securityGroupIds));
  }

  @Override
  public CompletableFuture<StatusCode @Nullable []> connectSecurityGroupsAsync(
      NodeId @Nullable [] securityGroupIds) {
    return ClientNodeSupport.compose(
        callConnectSecurityGroupsAsync(securityGroupIds),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<StatusCode @Nullable []>>
      callConnectSecurityGroupsAsync(NodeId @Nullable [] securityGroupIds) {
    return callConnectSecurityGroupsWithAsync(MethodCallOptions.DEFAULT, securityGroupIds);
  }

  @Override
  public CompletableFuture<MethodCallResult<StatusCode @Nullable []>>
      callConnectSecurityGroupsWithAsync(
          MethodCallOptions options, NodeId @Nullable [] securityGroupIds) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new PubSubKeyPushTargetTypeConnectSecurityGroups.Inputs(securityGroupIds)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getConnectSecurityGroupsMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return PubSubKeyPushTargetTypeConnectSecurityGroups.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values)
                                        .connectResults();
                                  }))));
        });
  }

  @Override
  public UaMethodNode getDisconnectSecurityGroupsMethodNode() throws UaException {
    return ClientNodeSupport.await(getDisconnectSecurityGroupsMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getDisconnectSecurityGroupsMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "DisconnectSecurityGroups",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public StatusCode @Nullable [] disconnectSecurityGroups(NodeId @Nullable [] securityGroupIds)
      throws UaException {
    return ClientNodeSupport.await(disconnectSecurityGroupsAsync(securityGroupIds));
  }

  @Override
  public MethodCallResult<StatusCode @Nullable []> callDisconnectSecurityGroups(
      NodeId @Nullable [] securityGroupIds) throws UaException {
    return ClientNodeSupport.await(callDisconnectSecurityGroupsAsync(securityGroupIds));
  }

  @Override
  public MethodCallResult<StatusCode @Nullable []> callDisconnectSecurityGroupsWith(
      MethodCallOptions options, NodeId @Nullable [] securityGroupIds) throws UaException {
    return ClientNodeSupport.await(
        callDisconnectSecurityGroupsWithAsync(options, securityGroupIds));
  }

  @Override
  public CompletableFuture<StatusCode @Nullable []> disconnectSecurityGroupsAsync(
      NodeId @Nullable [] securityGroupIds) {
    return ClientNodeSupport.compose(
        callDisconnectSecurityGroupsAsync(securityGroupIds),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<StatusCode @Nullable []>>
      callDisconnectSecurityGroupsAsync(NodeId @Nullable [] securityGroupIds) {
    return callDisconnectSecurityGroupsWithAsync(MethodCallOptions.DEFAULT, securityGroupIds);
  }

  @Override
  public CompletableFuture<MethodCallResult<StatusCode @Nullable []>>
      callDisconnectSecurityGroupsWithAsync(
          MethodCallOptions options, NodeId @Nullable [] securityGroupIds) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new PubSubKeyPushTargetTypeDisconnectSecurityGroups.Inputs(securityGroupIds)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getDisconnectSecurityGroupsMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return PubSubKeyPushTargetTypeDisconnectSecurityGroups.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values)
                                        .disconnectResults();
                                  }))));
        });
  }

  @Override
  public UaMethodNode getTriggerKeyUpdateMethodNode() throws UaException {
    return ClientNodeSupport.await(getTriggerKeyUpdateMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getTriggerKeyUpdateMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "TriggerKeyUpdate",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void triggerKeyUpdate() throws UaException {
    ClientNodeSupport.await(triggerKeyUpdateAsync());
  }

  @Override
  public MethodCallResult<Void> callTriggerKeyUpdate() throws UaException {
    return ClientNodeSupport.await(callTriggerKeyUpdateAsync());
  }

  @Override
  public MethodCallResult<Void> callTriggerKeyUpdateWith(MethodCallOptions options)
      throws UaException {
    return ClientNodeSupport.await(callTriggerKeyUpdateWithAsync(options));
  }

  @Override
  public CompletableFuture<Void> triggerKeyUpdateAsync() {
    return ClientNodeSupport.compose(
        callTriggerKeyUpdateAsync(),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callTriggerKeyUpdateAsync() {
    return callTriggerKeyUpdateWithAsync(MethodCallOptions.DEFAULT);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callTriggerKeyUpdateWithAsync(
      MethodCallOptions options) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs = new Variant[0];
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getTriggerKeyUpdateMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    if (values == null || values.length != 0)
                                      throw new UaException(
                                          StatusCodes.Bad_DecodingError,
                                          "expected no Method outputs");
                                    return null;
                                  }))));
        });
  }
}
