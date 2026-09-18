package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.SamplingIntervalDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.ServerDiagnosticsSummaryTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.SubscriptionDiagnosticsArrayTypeNode;
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
import org.eclipse.milo.opcua.stack.core.types.structured.SamplingIntervalDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerDiagnosticsSummaryDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ServerDiagnosticsType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.3">Model
 *     documentation</a>
 */
public class ServerDiagnosticsTypeNode extends BaseObjectTypeNode implements ServerDiagnosticsType {
  public ServerDiagnosticsTypeNode(
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
  public PropertyTypeNode getEnabledFlagNode() throws UaException {
    return ClientNodeSupport.await(getEnabledFlagNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getEnabledFlagNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "EnabledFlag",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readEnabledFlag() throws UaException {
    return ClientNodeSupport.await(readEnabledFlagAsync());
  }

  @Override
  public void writeEnabledFlag(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeEnabledFlagAsync(value)),
        "http://opcfoundation.org/UA/}EnabledFlag");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readEnabledFlagAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getEnabledFlagNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}EnabledFlag",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeEnabledFlagAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getEnabledFlagNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}EnabledFlag",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public ServerDiagnosticsSummaryTypeNode getServerDiagnosticsSummaryNode() throws UaException {
    return ClientNodeSupport.await(getServerDiagnosticsSummaryNodeAsync());
  }

  @Override
  public CompletableFuture<? extends ServerDiagnosticsSummaryTypeNode>
      getServerDiagnosticsSummaryNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ServerDiagnosticsSummary",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        ServerDiagnosticsSummaryTypeNode.class)));
  }

  @Override
  public @Nullable ServerDiagnosticsSummaryDataType readServerDiagnosticsSummary()
      throws UaException {
    return ClientNodeSupport.await(readServerDiagnosticsSummaryAsync());
  }

  @Override
  public void writeServerDiagnosticsSummary(@Nullable ServerDiagnosticsSummaryDataType value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeServerDiagnosticsSummaryAsync(value)),
        "http://opcfoundation.org/UA/}ServerDiagnosticsSummary");
  }

  @Override
  public CompletableFuture<? extends @Nullable ServerDiagnosticsSummaryDataType>
      readServerDiagnosticsSummaryAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getServerDiagnosticsSummaryNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ServerDiagnosticsSummary",
                            true,
                            ServerDiagnosticsSummaryDataType.class,
                            -1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable ServerDiagnosticsSummaryDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeServerDiagnosticsSummaryAsync(
      @Nullable ServerDiagnosticsSummaryDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getServerDiagnosticsSummaryNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ServerDiagnosticsSummary",
                        value,
                        ServerDiagnosticsSummaryDataType.class,
                        -1,
                        null)));
  }

  @Override
  public SessionsDiagnosticsSummaryTypeNode getSessionsDiagnosticsSummaryNode() throws UaException {
    return ClientNodeSupport.await(getSessionsDiagnosticsSummaryNodeAsync());
  }

  @Override
  public CompletableFuture<? extends SessionsDiagnosticsSummaryTypeNode>
      getSessionsDiagnosticsSummaryNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SessionsDiagnosticsSummary",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        SessionsDiagnosticsSummaryTypeNode.class)));
  }

  @Override
  public SubscriptionDiagnosticsArrayTypeNode getSubscriptionDiagnosticsArrayNode()
      throws UaException {
    return ClientNodeSupport.await(getSubscriptionDiagnosticsArrayNodeAsync());
  }

  @Override
  public CompletableFuture<? extends SubscriptionDiagnosticsArrayTypeNode>
      getSubscriptionDiagnosticsArrayNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SubscriptionDiagnosticsArray",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        SubscriptionDiagnosticsArrayTypeNode.class)));
  }

  @Override
  public @Nullable SubscriptionDiagnosticsDataType @Nullable [] readSubscriptionDiagnosticsArray()
      throws UaException {
    return ClientNodeSupport.await(readSubscriptionDiagnosticsArrayAsync());
  }

  @Override
  public void writeSubscriptionDiagnosticsArray(
      @Nullable SubscriptionDiagnosticsDataType @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSubscriptionDiagnosticsArrayAsync(value)),
        "http://opcfoundation.org/UA/}SubscriptionDiagnosticsArray");
  }

  @Override
  public CompletableFuture<? extends @Nullable SubscriptionDiagnosticsDataType @Nullable []>
      readSubscriptionDiagnosticsArrayAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSubscriptionDiagnosticsArrayNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SubscriptionDiagnosticsArray",
                            true,
                            SubscriptionDiagnosticsDataType.class,
                            1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable SubscriptionDiagnosticsDataType @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSubscriptionDiagnosticsArrayAsync(
      @Nullable SubscriptionDiagnosticsDataType @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSubscriptionDiagnosticsArrayNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SubscriptionDiagnosticsArray",
                        value,
                        SubscriptionDiagnosticsDataType.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable SamplingIntervalDiagnosticsArrayTypeNode
      getSamplingIntervalDiagnosticsArrayNode() throws UaException {
    return ClientNodeSupport.await(getSamplingIntervalDiagnosticsArrayNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable SamplingIntervalDiagnosticsArrayTypeNode>
      getSamplingIntervalDiagnosticsArrayNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SamplingIntervalDiagnosticsArray",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        SamplingIntervalDiagnosticsArrayTypeNode.class)));
  }

  @Override
  public @Nullable SamplingIntervalDiagnosticsDataType @Nullable []
      readSamplingIntervalDiagnosticsArray() throws UaException {
    return ClientNodeSupport.await(readSamplingIntervalDiagnosticsArrayAsync());
  }

  @Override
  public void writeSamplingIntervalDiagnosticsArray(
      @Nullable SamplingIntervalDiagnosticsDataType @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSamplingIntervalDiagnosticsArrayAsync(value)),
        "http://opcfoundation.org/UA/}SamplingIntervalDiagnosticsArray");
  }

  @Override
  public CompletableFuture<? extends @Nullable SamplingIntervalDiagnosticsDataType @Nullable []>
      readSamplingIntervalDiagnosticsArrayAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSamplingIntervalDiagnosticsArrayNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SamplingIntervalDiagnosticsArray",
                            false,
                            SamplingIntervalDiagnosticsDataType.class,
                            1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable SamplingIntervalDiagnosticsDataType @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSamplingIntervalDiagnosticsArrayAsync(
      @Nullable SamplingIntervalDiagnosticsDataType @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSamplingIntervalDiagnosticsArrayNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SamplingIntervalDiagnosticsArray",
                        value,
                        SamplingIntervalDiagnosticsDataType.class,
                        1,
                        null)));
  }
}
