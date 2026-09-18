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
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDCartesianCoordinates;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDFrame;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDOrientation;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ThreeDFrameType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.28">Model
 *     documentation</a>
 */
public class ThreeDFrameTypeNode extends FrameTypeNode implements ThreeDFrameType {
  public ThreeDFrameTypeNode(
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
  public ThreeDOrientationTypeNode getOrientationNode() throws UaException {
    return ClientNodeSupport.await(getOrientationNodeAsync());
  }

  @Override
  public CompletableFuture<? extends ThreeDOrientationTypeNode> getOrientationNodeAsync() {
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
                        ThreeDOrientationTypeNode.class)));
  }

  @Override
  public @Nullable ThreeDOrientation readThreeDFrameTypeOrientation() throws UaException {
    return ClientNodeSupport.await(readThreeDFrameTypeOrientationAsync());
  }

  @Override
  public void writeThreeDFrameTypeOrientation(@Nullable ThreeDOrientation value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeThreeDFrameTypeOrientationAsync(value)),
        "http://opcfoundation.org/UA/}Orientation");
  }

  @Override
  public CompletableFuture<? extends @Nullable ThreeDOrientation>
      readThreeDFrameTypeOrientationAsync() {
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
                            ThreeDOrientation.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ThreeDOrientation) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeThreeDFrameTypeOrientationAsync(
      @Nullable ThreeDOrientation value) {
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
                        ThreeDOrientation.class,
                        -1,
                        null)));
  }

  @Override
  public ThreeDCartesianCoordinatesTypeNode getCartesianCoordinatesNode() throws UaException {
    return ClientNodeSupport.await(getCartesianCoordinatesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends ThreeDCartesianCoordinatesTypeNode>
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
                        ThreeDCartesianCoordinatesTypeNode.class)));
  }

  @Override
  public @Nullable ThreeDCartesianCoordinates readThreeDFrameTypeCartesianCoordinates()
      throws UaException {
    return ClientNodeSupport.await(readThreeDFrameTypeCartesianCoordinatesAsync());
  }

  @Override
  public void writeThreeDFrameTypeCartesianCoordinates(@Nullable ThreeDCartesianCoordinates value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeThreeDFrameTypeCartesianCoordinatesAsync(value)),
        "http://opcfoundation.org/UA/}CartesianCoordinates");
  }

  @Override
  public CompletableFuture<? extends @Nullable ThreeDCartesianCoordinates>
      readThreeDFrameTypeCartesianCoordinatesAsync() {
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
                            ThreeDCartesianCoordinates.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ThreeDCartesianCoordinates) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeThreeDFrameTypeCartesianCoordinatesAsync(
      @Nullable ThreeDCartesianCoordinates value) {
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
                        ThreeDCartesianCoordinates.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable ThreeDFrame readThreeDFrameValue() throws UaException {
    return ClientNodeSupport.await(readThreeDFrameValueAsync());
  }

  @Override
  public void writeThreeDFrameValue(@Nullable ThreeDFrame value) throws UaException {
    ClientNodeSupport.good(ClientNodeSupport.await(writeThreeDFrameValueAsync(value)), "Value");
  }

  @Override
  public CompletableFuture<? extends @Nullable ThreeDFrame> readThreeDFrameValueAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    CompletableFuture.completedFuture(this),
                    n ->
                        ClientNodeSupport.read(
                            client, n, this, "Value", true, ThreeDFrame.class, -1, null)),
                v -> CompletableFuture.completedFuture((@Nullable ThreeDFrame) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeThreeDFrameValueAsync(@Nullable ThreeDFrame value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                n ->
                    ClientNodeSupport.write(
                        client, n, this, "Value", value, ThreeDFrame.class, -1, null)));
  }
}
