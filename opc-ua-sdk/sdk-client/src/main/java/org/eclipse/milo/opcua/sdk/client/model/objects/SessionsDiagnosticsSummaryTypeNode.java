package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionSecurityDiagnosticsArrayTypeNode;
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
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link SessionsDiagnosticsSummaryType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.4">Model
 *     documentation</a>
 */
public class SessionsDiagnosticsSummaryTypeNode extends BaseObjectTypeNode
    implements SessionsDiagnosticsSummaryType {
  public SessionsDiagnosticsSummaryTypeNode(
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
  public SessionDiagnosticsArrayTypeNode getSessionDiagnosticsArrayNode() throws UaException {
    return ClientNodeSupport.await(getSessionDiagnosticsArrayNodeAsync());
  }

  @Override
  public CompletableFuture<? extends SessionDiagnosticsArrayTypeNode>
      getSessionDiagnosticsArrayNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SessionDiagnosticsArray",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        SessionDiagnosticsArrayTypeNode.class)));
  }

  @Override
  public @Nullable SessionDiagnosticsDataType @Nullable [] readSessionDiagnosticsArray()
      throws UaException {
    return ClientNodeSupport.await(readSessionDiagnosticsArrayAsync());
  }

  @Override
  public void writeSessionDiagnosticsArray(@Nullable SessionDiagnosticsDataType @Nullable [] value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSessionDiagnosticsArrayAsync(value)),
        "http://opcfoundation.org/UA/}SessionDiagnosticsArray");
  }

  @Override
  public CompletableFuture<? extends @Nullable SessionDiagnosticsDataType @Nullable []>
      readSessionDiagnosticsArrayAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSessionDiagnosticsArrayNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SessionDiagnosticsArray",
                            true,
                            SessionDiagnosticsDataType.class,
                            1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable SessionDiagnosticsDataType @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSessionDiagnosticsArrayAsync(
      @Nullable SessionDiagnosticsDataType @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSessionDiagnosticsArrayNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SessionDiagnosticsArray",
                        value,
                        SessionDiagnosticsDataType.class,
                        1,
                        null)));
  }

  @Override
  public SessionSecurityDiagnosticsArrayTypeNode getSessionSecurityDiagnosticsArrayNode()
      throws UaException {
    return ClientNodeSupport.await(getSessionSecurityDiagnosticsArrayNodeAsync());
  }

  @Override
  public CompletableFuture<? extends SessionSecurityDiagnosticsArrayTypeNode>
      getSessionSecurityDiagnosticsArrayNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SessionSecurityDiagnosticsArray",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        SessionSecurityDiagnosticsArrayTypeNode.class)));
  }

  @Override
  public @Nullable SessionSecurityDiagnosticsDataType @Nullable []
      readSessionSecurityDiagnosticsArray() throws UaException {
    return ClientNodeSupport.await(readSessionSecurityDiagnosticsArrayAsync());
  }

  @Override
  public void writeSessionSecurityDiagnosticsArray(
      @Nullable SessionSecurityDiagnosticsDataType @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSessionSecurityDiagnosticsArrayAsync(value)),
        "http://opcfoundation.org/UA/}SessionSecurityDiagnosticsArray");
  }

  @Override
  public CompletableFuture<? extends @Nullable SessionSecurityDiagnosticsDataType @Nullable []>
      readSessionSecurityDiagnosticsArrayAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSessionSecurityDiagnosticsArrayNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SessionSecurityDiagnosticsArray",
                            true,
                            SessionSecurityDiagnosticsDataType.class,
                            1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable SessionSecurityDiagnosticsDataType @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSessionSecurityDiagnosticsArrayAsync(
      @Nullable SessionSecurityDiagnosticsDataType @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSessionSecurityDiagnosticsArrayNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SessionSecurityDiagnosticsArray",
                        value,
                        SessionSecurityDiagnosticsDataType.class,
                        1,
                        null)));
  }
}
