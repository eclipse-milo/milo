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
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link StateVariableType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.3">Model
 *     documentation</a>
 */
public class StateVariableTypeNode extends BaseDataVariableTypeNode implements StateVariableType {
  public StateVariableTypeNode(
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
  public @Nullable PropertyTypeNode getEffectiveDisplayNameNode() throws UaException {
    return ClientNodeSupport.await(getEffectiveDisplayNameNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getEffectiveDisplayNameNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "EffectiveDisplayName",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable LocalizedText readEffectiveDisplayName() throws UaException {
    return ClientNodeSupport.await(readEffectiveDisplayNameAsync());
  }

  @Override
  public void writeEffectiveDisplayName(@Nullable LocalizedText value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeEffectiveDisplayNameAsync(value)),
        "http://opcfoundation.org/UA/}EffectiveDisplayName");
  }

  @Override
  public CompletableFuture<? extends @Nullable LocalizedText> readEffectiveDisplayNameAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getEffectiveDisplayNameNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}EffectiveDisplayName",
                            false,
                            LocalizedText.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LocalizedText) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeEffectiveDisplayNameAsync(
      @Nullable LocalizedText value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getEffectiveDisplayNameNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}EffectiveDisplayName",
                        value,
                        LocalizedText.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getIdNode() throws UaException {
    return ClientNodeSupport.await(getIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Id",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Variant readId() throws UaException {
    return ClientNodeSupport.await(readIdAsync());
  }

  @Override
  public void writeId(@Nullable Variant value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeIdAsync(value)), "http://opcfoundation.org/UA/}Id");
  }

  @Override
  public CompletableFuture<? extends @Nullable Variant> readIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Id",
                            true,
                            Variant.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Variant) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeIdAsync(@Nullable Variant value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Id",
                        value,
                        Variant.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getNameNode() throws UaException {
    return ClientNodeSupport.await(getNameNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getNameNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Name",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable QualifiedName readName() throws UaException {
    return ClientNodeSupport.await(readNameAsync());
  }

  @Override
  public void writeName(@Nullable QualifiedName value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeNameAsync(value)), "http://opcfoundation.org/UA/}Name");
  }

  @Override
  public CompletableFuture<? extends @Nullable QualifiedName> readNameAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getNameNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Name",
                            false,
                            QualifiedName.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable QualifiedName) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeNameAsync(@Nullable QualifiedName value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getNameNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Name",
                        value,
                        QualifiedName.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getNumberNode() throws UaException {
    return ClientNodeSupport.await(getNumberNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getNumberNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Number",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readNumber() throws UaException {
    return ClientNodeSupport.await(readNumberAsync());
  }

  @Override
  public void writeNumber(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeNumberAsync(value)), "http://opcfoundation.org/UA/}Number");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readNumberAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getNumberNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Number",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeNumberAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getNumberNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Number",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable LocalizedText readTypedValue() throws UaException {
    return ClientNodeSupport.await(readTypedValueAsync());
  }

  @Override
  public void writeTypedValue(@Nullable LocalizedText value) throws UaException {
    ClientNodeSupport.good(ClientNodeSupport.await(writeTypedValueAsync(value)), "Value");
  }

  @Override
  public CompletableFuture<? extends @Nullable LocalizedText> readTypedValueAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    CompletableFuture.completedFuture(this),
                    n ->
                        ClientNodeSupport.read(
                            client, n, this, "Value", true, LocalizedText.class, -1, null)),
                v -> CompletableFuture.completedFuture((@Nullable LocalizedText) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTypedValueAsync(@Nullable LocalizedText value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                n ->
                    ClientNodeSupport.write(
                        client, n, this, "Value", value, LocalizedText.class, -1, null)));
  }
}
