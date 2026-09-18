package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionDiagnosticsVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionSecurityDiagnosticsTypeNode;
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
import org.eclipse.milo.opcua.stack.core.types.structured.SessionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionSecurityDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link SessionDiagnosticsObjectType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.5">Model
 *     documentation</a>
 */
public class SessionDiagnosticsObjectTypeNode extends BaseObjectTypeNode
    implements SessionDiagnosticsObjectType {
  public SessionDiagnosticsObjectTypeNode(
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
  public @Nullable PropertyTypeNode getCurrentRoleIdsNode() throws UaException {
    return ClientNodeSupport.await(getCurrentRoleIdsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getCurrentRoleIdsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CurrentRoleIds",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public NodeId @Nullable [] readCurrentRoleIds() throws UaException {
    return ClientNodeSupport.await(readCurrentRoleIdsAsync());
  }

  @Override
  public void writeCurrentRoleIds(NodeId @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeCurrentRoleIdsAsync(value)),
        "http://opcfoundation.org/UA/}CurrentRoleIds");
  }

  @Override
  public CompletableFuture<? extends NodeId @Nullable []> readCurrentRoleIdsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getCurrentRoleIdsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}CurrentRoleIds",
                            false,
                            NodeId.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((NodeId @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeCurrentRoleIdsAsync(NodeId @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getCurrentRoleIdsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}CurrentRoleIds",
                        value,
                        NodeId.class,
                        1,
                        null)));
  }

  @Override
  public SessionDiagnosticsVariableTypeNode getSessionDiagnosticsNode() throws UaException {
    return ClientNodeSupport.await(getSessionDiagnosticsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends SessionDiagnosticsVariableTypeNode>
      getSessionDiagnosticsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SessionDiagnostics",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        SessionDiagnosticsVariableTypeNode.class)));
  }

  @Override
  public @Nullable SessionDiagnosticsDataType readSessionDiagnostics() throws UaException {
    return ClientNodeSupport.await(readSessionDiagnosticsAsync());
  }

  @Override
  public void writeSessionDiagnostics(@Nullable SessionDiagnosticsDataType value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSessionDiagnosticsAsync(value)),
        "http://opcfoundation.org/UA/}SessionDiagnostics");
  }

  @Override
  public CompletableFuture<? extends @Nullable SessionDiagnosticsDataType>
      readSessionDiagnosticsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSessionDiagnosticsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SessionDiagnostics",
                            true,
                            SessionDiagnosticsDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable SessionDiagnosticsDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSessionDiagnosticsAsync(
      @Nullable SessionDiagnosticsDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSessionDiagnosticsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SessionDiagnostics",
                        value,
                        SessionDiagnosticsDataType.class,
                        -1,
                        null)));
  }

  @Override
  public SessionSecurityDiagnosticsTypeNode getSessionSecurityDiagnosticsNode() throws UaException {
    return ClientNodeSupport.await(getSessionSecurityDiagnosticsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends SessionSecurityDiagnosticsTypeNode>
      getSessionSecurityDiagnosticsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SessionSecurityDiagnostics",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        SessionSecurityDiagnosticsTypeNode.class)));
  }

  @Override
  public @Nullable SessionSecurityDiagnosticsDataType readSessionSecurityDiagnostics()
      throws UaException {
    return ClientNodeSupport.await(readSessionSecurityDiagnosticsAsync());
  }

  @Override
  public void writeSessionSecurityDiagnostics(@Nullable SessionSecurityDiagnosticsDataType value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSessionSecurityDiagnosticsAsync(value)),
        "http://opcfoundation.org/UA/}SessionSecurityDiagnostics");
  }

  @Override
  public CompletableFuture<? extends @Nullable SessionSecurityDiagnosticsDataType>
      readSessionSecurityDiagnosticsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSessionSecurityDiagnosticsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SessionSecurityDiagnostics",
                            true,
                            SessionSecurityDiagnosticsDataType.class,
                            -1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable SessionSecurityDiagnosticsDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSessionSecurityDiagnosticsAsync(
      @Nullable SessionSecurityDiagnosticsDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSessionSecurityDiagnosticsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SessionSecurityDiagnostics",
                        value,
                        SessionSecurityDiagnosticsDataType.class,
                        -1,
                        null)));
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
}
