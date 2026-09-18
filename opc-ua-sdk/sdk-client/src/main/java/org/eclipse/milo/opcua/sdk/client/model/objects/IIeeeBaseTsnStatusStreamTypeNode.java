package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnFailureCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnListenerStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnTalkerStatus;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link IIeeeBaseTsnStatusStreamType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.9">Model
 *     documentation</a>
 */
public class IIeeeBaseTsnStatusStreamTypeNode extends BaseInterfaceTypeNode
    implements IIeeeBaseTsnStatusStreamType {
  public IIeeeBaseTsnStatusStreamTypeNode(
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
  public UaVariableNode getFailureCodeNode() throws UaException {
    return ClientNodeSupport.await(getFailureCodeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getFailureCodeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "FailureCode",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable TsnFailureCode readFailureCode() throws UaException {
    return ClientNodeSupport.await(readFailureCodeAsync());
  }

  @Override
  public void writeFailureCode(@Nullable TsnFailureCode value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeFailureCodeAsync(value)),
        "http://opcfoundation.org/UA/}FailureCode");
  }

  @Override
  public CompletableFuture<? extends @Nullable TsnFailureCode> readFailureCodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getFailureCodeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}FailureCode",
                            true,
                            TsnFailureCode.class,
                            -1,
                            TsnFailureCode::from)),
                v -> CompletableFuture.completedFuture((@Nullable TsnFailureCode) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeFailureCodeAsync(@Nullable TsnFailureCode value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getFailureCodeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}FailureCode",
                        value,
                        TsnFailureCode.class,
                        -1,
                        TsnFailureCode::from)));
  }

  @Override
  public @Nullable UaVariableNode getTalkerStatusNode() throws UaException {
    return ClientNodeSupport.await(getTalkerStatusNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable UaVariableNode> getTalkerStatusNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "TalkerStatus",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable TsnTalkerStatus readTalkerStatus() throws UaException {
    return ClientNodeSupport.await(readTalkerStatusAsync());
  }

  @Override
  public void writeTalkerStatus(@Nullable TsnTalkerStatus value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeTalkerStatusAsync(value)),
        "http://opcfoundation.org/UA/}TalkerStatus");
  }

  @Override
  public CompletableFuture<? extends @Nullable TsnTalkerStatus> readTalkerStatusAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getTalkerStatusNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}TalkerStatus",
                            false,
                            TsnTalkerStatus.class,
                            -1,
                            TsnTalkerStatus::from)),
                v -> CompletableFuture.completedFuture((@Nullable TsnTalkerStatus) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTalkerStatusAsync(@Nullable TsnTalkerStatus value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getTalkerStatusNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}TalkerStatus",
                        value,
                        TsnTalkerStatus.class,
                        -1,
                        TsnTalkerStatus::from)));
  }

  @Override
  public @Nullable UaVariableNode getListenerStatusNode() throws UaException {
    return ClientNodeSupport.await(getListenerStatusNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable UaVariableNode> getListenerStatusNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ListenerStatus",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable TsnListenerStatus readListenerStatus() throws UaException {
    return ClientNodeSupport.await(readListenerStatusAsync());
  }

  @Override
  public void writeListenerStatus(@Nullable TsnListenerStatus value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeListenerStatusAsync(value)),
        "http://opcfoundation.org/UA/}ListenerStatus");
  }

  @Override
  public CompletableFuture<? extends @Nullable TsnListenerStatus> readListenerStatusAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getListenerStatusNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ListenerStatus",
                            false,
                            TsnListenerStatus.class,
                            -1,
                            TsnListenerStatus::from)),
                v -> CompletableFuture.completedFuture((@Nullable TsnListenerStatus) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeListenerStatusAsync(@Nullable TsnListenerStatus value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getListenerStatusNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ListenerStatus",
                        value,
                        TsnListenerStatus.class,
                        -1,
                        TsnListenerStatus::from)));
  }

  @Override
  public UaVariableNode getFailureSystemIdentifierNode() throws UaException {
    return ClientNodeSupport.await(getFailureSystemIdentifierNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getFailureSystemIdentifierNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "FailureSystemIdentifier",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable Object readFailureSystemIdentifier() throws UaException {
    return ClientNodeSupport.await(readFailureSystemIdentifierAsync());
  }

  @Override
  public void writeFailureSystemIdentifier(@Nullable Object value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeFailureSystemIdentifierAsync(value)),
        "http://opcfoundation.org/UA/}FailureSystemIdentifier");
  }

  @Override
  public CompletableFuture<? extends @Nullable Object> readFailureSystemIdentifierAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getFailureSystemIdentifierNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}FailureSystemIdentifier",
                            true,
                            UByte.class,
                            2,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Object) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeFailureSystemIdentifierAsync(@Nullable Object value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getFailureSystemIdentifierNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}FailureSystemIdentifier",
                        value,
                        UByte.class,
                        2,
                        null)));
  }
}
