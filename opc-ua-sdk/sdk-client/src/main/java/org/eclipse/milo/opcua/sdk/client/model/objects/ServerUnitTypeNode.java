package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaObjectNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ConversionLimitEnum;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ServerUnitType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.2/#6.4.2.3">Model
 *     documentation</a>
 */
public class ServerUnitTypeNode extends UnitTypeNode implements ServerUnitType {
  public ServerUnitTypeNode(
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
  public @Nullable UnitTypeNode getCoherentUnitNode() throws UaException {
    return ClientNodeSupport.await(getCoherentUnitNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable UnitTypeNode> getCoherentUnitNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CoherentUnit",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        UnitTypeNode.class)));
  }

  @Override
  public PropertyTypeNode getConversionLimitNode() throws UaException {
    return ClientNodeSupport.await(getConversionLimitNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getConversionLimitNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ConversionLimit",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable ConversionLimitEnum readConversionLimit() throws UaException {
    return ClientNodeSupport.await(readConversionLimitAsync());
  }

  @Override
  public void writeConversionLimit(@Nullable ConversionLimitEnum value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeConversionLimitAsync(value)),
        "http://opcfoundation.org/UA/}ConversionLimit");
  }

  @Override
  public CompletableFuture<? extends @Nullable ConversionLimitEnum> readConversionLimitAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getConversionLimitNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ConversionLimit",
                            true,
                            ConversionLimitEnum.class,
                            -1,
                            ConversionLimitEnum::from)),
                v -> CompletableFuture.completedFuture((@Nullable ConversionLimitEnum) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeConversionLimitAsync(
      @Nullable ConversionLimitEnum value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getConversionLimitNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ConversionLimit",
                        value,
                        ConversionLimitEnum.class,
                        -1,
                        ConversionLimitEnum::from)));
  }

  @Override
  public @Nullable UaObjectNode getAlternativeUnitsNode() throws UaException {
    return ClientNodeSupport.await(getAlternativeUnitsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable UaObjectNode> getAlternativeUnitsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "AlternativeUnits",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        UaObjectNode.class)));
  }
}
