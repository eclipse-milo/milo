package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.AnalogUnitTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.Duplex;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link IIeeeBaseEthernetPortType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.2">Model
 *     documentation</a>
 */
public class IIeeeBaseEthernetPortTypeNode extends BaseInterfaceTypeNode
    implements IIeeeBaseEthernetPortType {
  public IIeeeBaseEthernetPortTypeNode(
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
  public UaVariableNode getMaxFrameLengthNode() throws UaException {
    return ClientNodeSupport.await(getMaxFrameLengthNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getMaxFrameLengthNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxFrameLength",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable UShort readMaxFrameLength() throws UaException {
    return ClientNodeSupport.await(readMaxFrameLengthAsync());
  }

  @Override
  public void writeMaxFrameLength(@Nullable UShort value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxFrameLengthAsync(value)),
        "http://opcfoundation.org/UA/}MaxFrameLength");
  }

  @Override
  public CompletableFuture<? extends @Nullable UShort> readMaxFrameLengthAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxFrameLengthNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxFrameLength",
                            true,
                            UShort.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UShort) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxFrameLengthAsync(@Nullable UShort value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxFrameLengthNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxFrameLength",
                        value,
                        UShort.class,
                        -1,
                        null)));
  }

  @Override
  public AnalogUnitTypeNode getSpeedNode() throws UaException {
    return ClientNodeSupport.await(getSpeedNodeAsync());
  }

  @Override
  public CompletableFuture<? extends AnalogUnitTypeNode> getSpeedNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Speed",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        AnalogUnitTypeNode.class)));
  }

  @Override
  public @Nullable ULong readSpeed() throws UaException {
    return ClientNodeSupport.await(readSpeedAsync());
  }

  @Override
  public void writeSpeed(@Nullable ULong value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSpeedAsync(value)), "http://opcfoundation.org/UA/}Speed");
  }

  @Override
  public CompletableFuture<? extends @Nullable ULong> readSpeedAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSpeedNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Speed",
                            true,
                            ULong.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ULong) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSpeedAsync(@Nullable ULong value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSpeedNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Speed",
                        value,
                        ULong.class,
                        -1,
                        null)));
  }

  @Override
  public UaVariableNode getDuplexNode() throws UaException {
    return ClientNodeSupport.await(getDuplexNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getDuplexNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Duplex",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable Duplex readDuplex() throws UaException {
    return ClientNodeSupport.await(readDuplexAsync());
  }

  @Override
  public void writeDuplex(@Nullable Duplex value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDuplexAsync(value)), "http://opcfoundation.org/UA/}Duplex");
  }

  @Override
  public CompletableFuture<? extends @Nullable Duplex> readDuplexAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDuplexNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Duplex",
                            true,
                            Duplex.class,
                            -1,
                            Duplex::from)),
                v -> CompletableFuture.completedFuture((@Nullable Duplex) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDuplexAsync(@Nullable Duplex value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDuplexNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Duplex",
                        value,
                        Duplex.class,
                        -1,
                        Duplex::from)));
  }
}
