package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubDiagnosticsCounterClassification;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link PubSubDiagnosticsCounterType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.5">Model
 *     documentation</a>
 */
public class PubSubDiagnosticsCounterTypeNode extends BaseDataVariableTypeNode
    implements PubSubDiagnosticsCounterType {
  public PubSubDiagnosticsCounterTypeNode(
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
  public PropertyTypeNode getClassificationNode() throws UaException {
    return ClientNodeSupport.await(getClassificationNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getClassificationNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Classification",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable PubSubDiagnosticsCounterClassification readClassification() throws UaException {
    return ClientNodeSupport.await(readClassificationAsync());
  }

  @Override
  public void writeClassification(@Nullable PubSubDiagnosticsCounterClassification value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeClassificationAsync(value)),
        "http://opcfoundation.org/UA/}Classification");
  }

  @Override
  public CompletableFuture<? extends @Nullable PubSubDiagnosticsCounterClassification>
      readClassificationAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getClassificationNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Classification",
                            true,
                            PubSubDiagnosticsCounterClassification.class,
                            -1,
                            PubSubDiagnosticsCounterClassification::from)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable PubSubDiagnosticsCounterClassification) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeClassificationAsync(
      @Nullable PubSubDiagnosticsCounterClassification value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getClassificationNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Classification",
                        value,
                        PubSubDiagnosticsCounterClassification.class,
                        -1,
                        PubSubDiagnosticsCounterClassification::from)));
  }

  @Override
  public @Nullable PropertyTypeNode getTimeFirstChangeNode() throws UaException {
    return ClientNodeSupport.await(getTimeFirstChangeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getTimeFirstChangeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "TimeFirstChange",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable DateTime readTimeFirstChange() throws UaException {
    return ClientNodeSupport.await(readTimeFirstChangeAsync());
  }

  @Override
  public void writeTimeFirstChange(@Nullable DateTime value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeTimeFirstChangeAsync(value)),
        "http://opcfoundation.org/UA/}TimeFirstChange");
  }

  @Override
  public CompletableFuture<? extends @Nullable DateTime> readTimeFirstChangeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getTimeFirstChangeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}TimeFirstChange",
                            false,
                            DateTime.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable DateTime) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTimeFirstChangeAsync(@Nullable DateTime value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getTimeFirstChangeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}TimeFirstChange",
                        value,
                        DateTime.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getDiagnosticsLevelNode() throws UaException {
    return ClientNodeSupport.await(getDiagnosticsLevelNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDiagnosticsLevelNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DiagnosticsLevel",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable DiagnosticsLevel readDiagnosticsLevel() throws UaException {
    return ClientNodeSupport.await(readDiagnosticsLevelAsync());
  }

  @Override
  public void writeDiagnosticsLevel(@Nullable DiagnosticsLevel value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDiagnosticsLevelAsync(value)),
        "http://opcfoundation.org/UA/}DiagnosticsLevel");
  }

  @Override
  public CompletableFuture<? extends @Nullable DiagnosticsLevel> readDiagnosticsLevelAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDiagnosticsLevelNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DiagnosticsLevel",
                            true,
                            DiagnosticsLevel.class,
                            -1,
                            DiagnosticsLevel::from)),
                v -> CompletableFuture.completedFuture((@Nullable DiagnosticsLevel) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDiagnosticsLevelAsync(
      @Nullable DiagnosticsLevel value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDiagnosticsLevelNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DiagnosticsLevel",
                        value,
                        DiagnosticsLevel.class,
                        -1,
                        DiagnosticsLevel::from)));
  }

  @Override
  public PropertyTypeNode getActiveNode() throws UaException {
    return ClientNodeSupport.await(getActiveNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getActiveNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Active",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readActive() throws UaException {
    return ClientNodeSupport.await(readActiveAsync());
  }

  @Override
  public void writeActive(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeActiveAsync(value)), "http://opcfoundation.org/UA/}Active");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readActiveAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getActiveNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Active",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeActiveAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getActiveNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Active",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable UInteger readTypedValue() throws UaException {
    return ClientNodeSupport.await(readTypedValueAsync());
  }

  @Override
  public void writeTypedValue(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(ClientNodeSupport.await(writeTypedValueAsync(value)), "Value");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readTypedValueAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    CompletableFuture.completedFuture(this),
                    n ->
                        ClientNodeSupport.read(
                            client, n, this, "Value", true, UInteger.class, -1, null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTypedValueAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                n ->
                    ClientNodeSupport.write(
                        client, n, this, "Value", value, UInteger.class, -1, null)));
  }
}
