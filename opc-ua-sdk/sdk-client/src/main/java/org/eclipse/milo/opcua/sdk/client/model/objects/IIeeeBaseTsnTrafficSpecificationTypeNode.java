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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UnsignedRationalNumber;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link IIeeeBaseTsnTrafficSpecificationType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.8">Model
 *     documentation</a>
 */
public class IIeeeBaseTsnTrafficSpecificationTypeNode extends BaseInterfaceTypeNode
    implements IIeeeBaseTsnTrafficSpecificationType {
  public IIeeeBaseTsnTrafficSpecificationTypeNode(
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
  public UaVariableNode getMaxFrameSizeNode() throws UaException {
    return ClientNodeSupport.await(getMaxFrameSizeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getMaxFrameSizeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxFrameSize",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxFrameSize() throws UaException {
    return ClientNodeSupport.await(readMaxFrameSizeAsync());
  }

  @Override
  public void writeMaxFrameSize(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxFrameSizeAsync(value)),
        "http://opcfoundation.org/UA/}MaxFrameSize");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxFrameSizeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxFrameSizeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxFrameSize",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxFrameSizeAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxFrameSizeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxFrameSize",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getMaxIntervalFramesNode() throws UaException {
    return ClientNodeSupport.await(getMaxIntervalFramesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getMaxIntervalFramesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxIntervalFrames",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UShort readMaxIntervalFrames() throws UaException {
    return ClientNodeSupport.await(readMaxIntervalFramesAsync());
  }

  @Override
  public void writeMaxIntervalFrames(@Nullable UShort value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxIntervalFramesAsync(value)),
        "http://opcfoundation.org/UA/}MaxIntervalFrames");
  }

  @Override
  public CompletableFuture<? extends @Nullable UShort> readMaxIntervalFramesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxIntervalFramesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxIntervalFrames",
                            true,
                            UShort.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UShort) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxIntervalFramesAsync(@Nullable UShort value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxIntervalFramesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxIntervalFrames",
                        value,
                        UShort.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getIntervalNode() throws UaException {
    return ClientNodeSupport.await(getIntervalNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getIntervalNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Interval",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UnsignedRationalNumber readInterval() throws UaException {
    return ClientNodeSupport.await(readIntervalAsync());
  }

  @Override
  public void writeInterval(@Nullable UnsignedRationalNumber value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeIntervalAsync(value)),
        "http://opcfoundation.org/UA/}Interval");
  }

  @Override
  public CompletableFuture<? extends @Nullable UnsignedRationalNumber> readIntervalAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getIntervalNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Interval",
                            true,
                            UnsignedRationalNumber.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UnsignedRationalNumber) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeIntervalAsync(@Nullable UnsignedRationalNumber value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getIntervalNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Interval",
                        value,
                        UnsignedRationalNumber.class,
                        -1,
                        null)));
  }
}
