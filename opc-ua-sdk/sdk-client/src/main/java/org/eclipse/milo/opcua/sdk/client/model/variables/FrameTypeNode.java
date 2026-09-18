package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.CartesianCoordinates;
import org.eclipse.milo.opcua.stack.core.types.structured.Frame;
import org.eclipse.milo.opcua.stack.core.types.structured.Orientation;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link FrameType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.27">Model
 *     documentation</a>
 */
public class FrameTypeNode extends BaseDataVariableTypeNode implements FrameType {
  public FrameTypeNode(
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
  public OrientationTypeNode getOrientationNode() throws UaException {
    return ClientNodeSupport.await(getOrientationNodeAsync());
  }

  @Override
  public CompletableFuture<? extends OrientationTypeNode> getOrientationNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Orientation",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        OrientationTypeNode.class)));
  }

  @Override
  public @Nullable Orientation readOrientation() throws UaException {
    return ClientNodeSupport.await(readOrientationAsync());
  }

  @Override
  public void writeOrientation(@Nullable Orientation value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeOrientationAsync(value)),
        "http://opcfoundation.org/UA/}Orientation");
  }

  @Override
  public CompletableFuture<? extends @Nullable Orientation> readOrientationAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getOrientationNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Orientation",
                            true,
                            Orientation.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Orientation) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeOrientationAsync(@Nullable Orientation value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getOrientationNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Orientation",
                        value,
                        Orientation.class,
                        -1,
                        null)));
  }

  @Override
  public CartesianCoordinatesTypeNode getCartesianCoordinatesNode() throws UaException {
    return ClientNodeSupport.await(getCartesianCoordinatesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends CartesianCoordinatesTypeNode>
      getCartesianCoordinatesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CartesianCoordinates",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        CartesianCoordinatesTypeNode.class)));
  }

  @Override
  public @Nullable CartesianCoordinates readCartesianCoordinates() throws UaException {
    return ClientNodeSupport.await(readCartesianCoordinatesAsync());
  }

  @Override
  public void writeCartesianCoordinates(@Nullable CartesianCoordinates value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeCartesianCoordinatesAsync(value)),
        "http://opcfoundation.org/UA/}CartesianCoordinates");
  }

  @Override
  public CompletableFuture<? extends @Nullable CartesianCoordinates>
      readCartesianCoordinatesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getCartesianCoordinatesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}CartesianCoordinates",
                            true,
                            CartesianCoordinates.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable CartesianCoordinates) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeCartesianCoordinatesAsync(
      @Nullable CartesianCoordinates value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getCartesianCoordinatesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}CartesianCoordinates",
                        value,
                        CartesianCoordinates.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getConstantNode() throws UaException {
    return ClientNodeSupport.await(getConstantNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getConstantNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Constant",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readConstant() throws UaException {
    return ClientNodeSupport.await(readConstantAsync());
  }

  @Override
  public void writeConstant(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeConstantAsync(value)),
        "http://opcfoundation.org/UA/}Constant");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readConstantAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getConstantNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Constant",
                            false,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeConstantAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getConstantNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Constant",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable UaVariableNode getBaseFrameNode() throws UaException {
    return ClientNodeSupport.await(getBaseFrameNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable UaVariableNode> getBaseFrameNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "BaseFrame",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable NodeId readBaseFrame() throws UaException {
    return ClientNodeSupport.await(readBaseFrameAsync());
  }

  @Override
  public void writeBaseFrame(@Nullable NodeId value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeBaseFrameAsync(value)),
        "http://opcfoundation.org/UA/}BaseFrame");
  }

  @Override
  public CompletableFuture<? extends @Nullable NodeId> readBaseFrameAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getBaseFrameNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}BaseFrame",
                            false,
                            NodeId.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable NodeId) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeBaseFrameAsync(@Nullable NodeId value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getBaseFrameNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}BaseFrame",
                        value,
                        NodeId.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getFixedBaseNode() throws UaException {
    return ClientNodeSupport.await(getFixedBaseNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getFixedBaseNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "FixedBase",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readFixedBase() throws UaException {
    return ClientNodeSupport.await(readFixedBaseAsync());
  }

  @Override
  public void writeFixedBase(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeFixedBaseAsync(value)),
        "http://opcfoundation.org/UA/}FixedBase");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readFixedBaseAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getFixedBaseNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}FixedBase",
                            false,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeFixedBaseAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getFixedBaseNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}FixedBase",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable Frame readTypedValue() throws UaException {
    return ClientNodeSupport.await(readTypedValueAsync());
  }

  @Override
  public void writeTypedValue(@Nullable Frame value) throws UaException {
    ClientNodeSupport.good(ClientNodeSupport.await(writeTypedValueAsync(value)), "Value");
  }

  @Override
  public CompletableFuture<? extends @Nullable Frame> readTypedValueAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    CompletableFuture.completedFuture(this),
                    n ->
                        ClientNodeSupport.read(
                            client, n, this, "Value", true, Frame.class, -1, null)),
                v -> CompletableFuture.completedFuture((@Nullable Frame) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTypedValueAsync(@Nullable Frame value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                n ->
                    ClientNodeSupport.write(
                        client, n, this, "Value", value, Frame.class, -1, null)));
  }
}
