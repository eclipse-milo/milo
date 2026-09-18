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
 * Node implementation of {@link OptionSetType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.17">Model
 *     documentation</a>
 */
public class OptionSetTypeNode extends BaseDataVariableTypeNode implements OptionSetType {
  public OptionSetTypeNode(
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
  public PropertyTypeNode getOptionSetValuesNode() throws UaException {
    return ClientNodeSupport.await(getOptionSetValuesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getOptionSetValuesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "OptionSetValues",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public LocalizedText @Nullable [] readOptionSetValues() throws UaException {
    return ClientNodeSupport.await(readOptionSetValuesAsync());
  }

  @Override
  public void writeOptionSetValues(LocalizedText @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeOptionSetValuesAsync(value)),
        "http://opcfoundation.org/UA/}OptionSetValues");
  }

  @Override
  public CompletableFuture<? extends LocalizedText @Nullable []> readOptionSetValuesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getOptionSetValuesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}OptionSetValues",
                            true,
                            LocalizedText.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((LocalizedText @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeOptionSetValuesAsync(LocalizedText @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getOptionSetValuesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}OptionSetValues",
                        value,
                        LocalizedText.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getBitMaskNode() throws UaException {
    return ClientNodeSupport.await(getBitMaskNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getBitMaskNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "BitMask",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public Boolean @Nullable [] readBitMask() throws UaException {
    return ClientNodeSupport.await(readBitMaskAsync());
  }

  @Override
  public void writeBitMask(Boolean @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeBitMaskAsync(value)), "http://opcfoundation.org/UA/}BitMask");
  }

  @Override
  public CompletableFuture<? extends Boolean @Nullable []> readBitMaskAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getBitMaskNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}BitMask",
                            false,
                            Boolean.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((Boolean @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeBitMaskAsync(Boolean @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getBitMaskNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}BitMask",
                        value,
                        Boolean.class,
                        1,
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
                            client, n, this, "Value", true, Variant.class, -1, null)),
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
                        client, n, this, "Value", value, Variant.class, -1, null)));
  }
}
