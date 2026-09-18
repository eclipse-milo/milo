package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
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
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link AggregateConfigurationType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part13/4.2.1/#4.2.1.2">Model
 *     documentation</a>
 */
public class AggregateConfigurationTypeNode extends BaseObjectTypeNode
    implements AggregateConfigurationType {
  public AggregateConfigurationTypeNode(
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
  public PropertyTypeNode getPercentDataBadNode() throws UaException {
    return ClientNodeSupport.await(getPercentDataBadNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPercentDataBadNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "PercentDataBad",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UByte readPercentDataBad() throws UaException {
    return ClientNodeSupport.await(readPercentDataBadAsync());
  }

  @Override
  public void writePercentDataBad(@Nullable UByte value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writePercentDataBadAsync(value)),
        "http://opcfoundation.org/UA/}PercentDataBad");
  }

  @Override
  public CompletableFuture<? extends @Nullable UByte> readPercentDataBadAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getPercentDataBadNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}PercentDataBad",
                            true,
                            UByte.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UByte) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writePercentDataBadAsync(@Nullable UByte value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getPercentDataBadNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}PercentDataBad",
                        value,
                        UByte.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getPercentDataGoodNode() throws UaException {
    return ClientNodeSupport.await(getPercentDataGoodNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPercentDataGoodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "PercentDataGood",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UByte readPercentDataGood() throws UaException {
    return ClientNodeSupport.await(readPercentDataGoodAsync());
  }

  @Override
  public void writePercentDataGood(@Nullable UByte value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writePercentDataGoodAsync(value)),
        "http://opcfoundation.org/UA/}PercentDataGood");
  }

  @Override
  public CompletableFuture<? extends @Nullable UByte> readPercentDataGoodAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getPercentDataGoodNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}PercentDataGood",
                            true,
                            UByte.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UByte) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writePercentDataGoodAsync(@Nullable UByte value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getPercentDataGoodNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}PercentDataGood",
                        value,
                        UByte.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getTreatUncertainAsBadNode() throws UaException {
    return ClientNodeSupport.await(getTreatUncertainAsBadNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getTreatUncertainAsBadNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "TreatUncertainAsBad",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readTreatUncertainAsBad() throws UaException {
    return ClientNodeSupport.await(readTreatUncertainAsBadAsync());
  }

  @Override
  public void writeTreatUncertainAsBad(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeTreatUncertainAsBadAsync(value)),
        "http://opcfoundation.org/UA/}TreatUncertainAsBad");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readTreatUncertainAsBadAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getTreatUncertainAsBadNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}TreatUncertainAsBad",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTreatUncertainAsBadAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getTreatUncertainAsBadNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}TreatUncertainAsBad",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getUseSlopedExtrapolationNode() throws UaException {
    return ClientNodeSupport.await(getUseSlopedExtrapolationNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUseSlopedExtrapolationNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "UseSlopedExtrapolation",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readUseSlopedExtrapolation() throws UaException {
    return ClientNodeSupport.await(readUseSlopedExtrapolationAsync());
  }

  @Override
  public void writeUseSlopedExtrapolation(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeUseSlopedExtrapolationAsync(value)),
        "http://opcfoundation.org/UA/}UseSlopedExtrapolation");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readUseSlopedExtrapolationAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getUseSlopedExtrapolationNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}UseSlopedExtrapolation",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeUseSlopedExtrapolationAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getUseSlopedExtrapolationNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}UseSlopedExtrapolation",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }
}
