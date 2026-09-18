package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
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
import org.eclipse.milo.opcua.stack.core.types.structured.TransactionErrorType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link TransactionDiagnosticsType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.17">Model
 *     documentation</a>
 */
public class TransactionDiagnosticsTypeNode extends BaseObjectTypeNode
    implements TransactionDiagnosticsType {
  public TransactionDiagnosticsTypeNode(
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
  public PropertyTypeNode getAffectedTrustListsNode() throws UaException {
    return ClientNodeSupport.await(getAffectedTrustListsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getAffectedTrustListsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "AffectedTrustLists",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public NodeId @Nullable [] readAffectedTrustLists() throws UaException {
    return ClientNodeSupport.await(readAffectedTrustListsAsync());
  }

  @Override
  public void writeAffectedTrustLists(NodeId @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeAffectedTrustListsAsync(value)),
        "http://opcfoundation.org/UA/}AffectedTrustLists");
  }

  @Override
  public CompletableFuture<? extends NodeId @Nullable []> readAffectedTrustListsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getAffectedTrustListsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}AffectedTrustLists",
                            true,
                            NodeId.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((NodeId @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeAffectedTrustListsAsync(NodeId @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getAffectedTrustListsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}AffectedTrustLists",
                        value,
                        NodeId.class,
                        1,
                        null)));
  }

  @Override
  public PropertyTypeNode getAffectedCertificateGroupsNode() throws UaException {
    return ClientNodeSupport.await(getAffectedCertificateGroupsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getAffectedCertificateGroupsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "AffectedCertificateGroups",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public NodeId @Nullable [] readAffectedCertificateGroups() throws UaException {
    return ClientNodeSupport.await(readAffectedCertificateGroupsAsync());
  }

  @Override
  public void writeAffectedCertificateGroups(NodeId @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeAffectedCertificateGroupsAsync(value)),
        "http://opcfoundation.org/UA/}AffectedCertificateGroups");
  }

  @Override
  public CompletableFuture<? extends NodeId @Nullable []> readAffectedCertificateGroupsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getAffectedCertificateGroupsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}AffectedCertificateGroups",
                            true,
                            NodeId.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((NodeId @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeAffectedCertificateGroupsAsync(
      NodeId @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getAffectedCertificateGroupsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}AffectedCertificateGroups",
                        value,
                        NodeId.class,
                        1,
                        null)));
  }

  @Override
  public PropertyTypeNode getErrorsNode() throws UaException {
    return ClientNodeSupport.await(getErrorsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getErrorsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Errors",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable TransactionErrorType @Nullable [] readErrors() throws UaException {
    return ClientNodeSupport.await(readErrorsAsync());
  }

  @Override
  public void writeErrors(@Nullable TransactionErrorType @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeErrorsAsync(value)), "http://opcfoundation.org/UA/}Errors");
  }

  @Override
  public CompletableFuture<? extends @Nullable TransactionErrorType @Nullable []>
      readErrorsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getErrorsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Errors",
                            true,
                            TransactionErrorType.class,
                            1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable TransactionErrorType @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeErrorsAsync(
      @Nullable TransactionErrorType @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getErrorsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Errors",
                        value,
                        TransactionErrorType.class,
                        1,
                        null)));
  }

  @Override
  public PropertyTypeNode getResultNode() throws UaException {
    return ClientNodeSupport.await(getResultNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getResultNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Result",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable StatusCode readResult() throws UaException {
    return ClientNodeSupport.await(readResultAsync());
  }

  @Override
  public void writeResult(@Nullable StatusCode value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeResultAsync(value)), "http://opcfoundation.org/UA/}Result");
  }

  @Override
  public CompletableFuture<? extends @Nullable StatusCode> readResultAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getResultNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Result",
                            true,
                            StatusCode.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable StatusCode) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeResultAsync(@Nullable StatusCode value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getResultNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Result",
                        value,
                        StatusCode.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getEndTimeNode() throws UaException {
    return ClientNodeSupport.await(getEndTimeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getEndTimeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "EndTime",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable DateTime readEndTime() throws UaException {
    return ClientNodeSupport.await(readEndTimeAsync());
  }

  @Override
  public void writeEndTime(@Nullable DateTime value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeEndTimeAsync(value)), "http://opcfoundation.org/UA/}EndTime");
  }

  @Override
  public CompletableFuture<? extends @Nullable DateTime> readEndTimeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getEndTimeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}EndTime",
                            true,
                            DateTime.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable DateTime) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeEndTimeAsync(@Nullable DateTime value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getEndTimeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}EndTime",
                        value,
                        DateTime.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getStartTimeNode() throws UaException {
    return ClientNodeSupport.await(getStartTimeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getStartTimeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "StartTime",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable DateTime readStartTime() throws UaException {
    return ClientNodeSupport.await(readStartTimeAsync());
  }

  @Override
  public void writeStartTime(@Nullable DateTime value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeStartTimeAsync(value)),
        "http://opcfoundation.org/UA/}StartTime");
  }

  @Override
  public CompletableFuture<? extends @Nullable DateTime> readStartTimeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getStartTimeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}StartTime",
                            true,
                            DateTime.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable DateTime) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeStartTimeAsync(@Nullable DateTime value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getStartTimeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}StartTime",
                        value,
                        DateTime.class,
                        -1,
                        null)));
  }
}
