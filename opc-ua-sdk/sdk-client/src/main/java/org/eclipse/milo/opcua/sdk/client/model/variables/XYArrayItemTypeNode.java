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
import org.eclipse.milo.opcua.stack.core.types.structured.AxisInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link XYArrayItemType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.4/#5.3.4.3">Model
 *     documentation</a>
 */
public class XYArrayItemTypeNode extends ArrayItemTypeNode implements XYArrayItemType {
  public XYArrayItemTypeNode(
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
  public @Nullable XVType @Nullable [] readXYArrayItemValue() throws UaException {
    return ClientNodeSupport.await(readXYArrayItemValueAsync());
  }

  @Override
  public void writeXYArrayItemValue(@Nullable XVType @Nullable [] value) throws UaException {
    ClientNodeSupport.good(ClientNodeSupport.await(writeXYArrayItemValueAsync(value)), "Value");
  }

  @Override
  public CompletableFuture<? extends @Nullable XVType @Nullable []> readXYArrayItemValueAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    CompletableFuture.completedFuture(this),
                    n ->
                        ClientNodeSupport.read(
                            client, n, this, "Value", true, XVType.class, 1, null)),
                v -> CompletableFuture.completedFuture((@Nullable XVType @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeXYArrayItemValueAsync(
      @Nullable XVType @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                n ->
                    ClientNodeSupport.write(
                        client, n, this, "Value", value, XVType.class, 1, null)));
  }
}
