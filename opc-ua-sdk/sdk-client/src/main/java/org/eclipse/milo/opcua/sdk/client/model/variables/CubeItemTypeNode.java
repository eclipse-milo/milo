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
import org.eclipse.milo.opcua.stack.core.types.structured.AxisInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link CubeItemType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.4/#5.3.4.5">Model
 *     documentation</a>
 */
public class CubeItemTypeNode extends ArrayItemTypeNode implements CubeItemType {
  public CubeItemTypeNode(
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
  public PropertyTypeNode getXAxisDefinitionNode() throws UaException {
    return ClientNodeSupport.await(getXAxisDefinitionNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getXAxisDefinitionNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "XAxisDefinition",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable AxisInformation readXAxisDefinition() throws UaException {
    return ClientNodeSupport.await(readXAxisDefinitionAsync());
  }

  @Override
  public void writeXAxisDefinition(@Nullable AxisInformation value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeXAxisDefinitionAsync(value)),
        "http://opcfoundation.org/UA/}XAxisDefinition");
  }

  @Override
  public CompletableFuture<? extends @Nullable AxisInformation> readXAxisDefinitionAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getXAxisDefinitionNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}XAxisDefinition",
                            true,
                            AxisInformation.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable AxisInformation) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeXAxisDefinitionAsync(@Nullable AxisInformation value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getXAxisDefinitionNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}XAxisDefinition",
                        value,
                        AxisInformation.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getYAxisDefinitionNode() throws UaException {
    return ClientNodeSupport.await(getYAxisDefinitionNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getYAxisDefinitionNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "YAxisDefinition",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable AxisInformation readYAxisDefinition() throws UaException {
    return ClientNodeSupport.await(readYAxisDefinitionAsync());
  }

  @Override
  public void writeYAxisDefinition(@Nullable AxisInformation value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeYAxisDefinitionAsync(value)),
        "http://opcfoundation.org/UA/}YAxisDefinition");
  }

  @Override
  public CompletableFuture<? extends @Nullable AxisInformation> readYAxisDefinitionAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getYAxisDefinitionNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}YAxisDefinition",
                            true,
                            AxisInformation.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable AxisInformation) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeYAxisDefinitionAsync(@Nullable AxisInformation value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getYAxisDefinitionNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}YAxisDefinition",
                        value,
                        AxisInformation.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getZAxisDefinitionNode() throws UaException {
    return ClientNodeSupport.await(getZAxisDefinitionNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getZAxisDefinitionNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ZAxisDefinition",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable AxisInformation readZAxisDefinition() throws UaException {
    return ClientNodeSupport.await(readZAxisDefinitionAsync());
  }

  @Override
  public void writeZAxisDefinition(@Nullable AxisInformation value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeZAxisDefinitionAsync(value)),
        "http://opcfoundation.org/UA/}ZAxisDefinition");
  }

  @Override
  public CompletableFuture<? extends @Nullable AxisInformation> readZAxisDefinitionAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getZAxisDefinitionNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ZAxisDefinition",
                            true,
                            AxisInformation.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable AxisInformation) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeZAxisDefinitionAsync(@Nullable AxisInformation value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getZAxisDefinitionNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ZAxisDefinition",
                        value,
                        AxisInformation.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable Variant readTypedValue() throws UaException {
    return ClientNodeSupport.await(readTypedValueAsync());
  }

  @Override
  public void writeTypedValue(@Nullable Variant value) throws UaException {
    ClientNodeSupport.good(ClientNodeSupport.await(writeTypedValueAsync(value)), "Value");
  }

  @Override
  public CompletableFuture<? extends @Nullable Variant> readTypedValueAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    CompletableFuture.completedFuture(this),
                    n ->
                        ClientNodeSupport.read(
                            client, n, this, "Value", true, Variant.class, 3, null)),
                v -> CompletableFuture.completedFuture((@Nullable Variant) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTypedValueAsync(@Nullable Variant value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                n ->
                    ClientNodeSupport.write(
                        client, n, this, "Value", value, Variant.class, 3, null)));
  }
}
