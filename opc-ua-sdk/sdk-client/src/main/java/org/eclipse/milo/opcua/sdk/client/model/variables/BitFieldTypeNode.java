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
import org.eclipse.milo.opcua.stack.core.types.structured.BitFieldDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link BitFieldType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.29">Model
 *     documentation</a>
 */
public class BitFieldTypeNode extends BaseDataVariableTypeNode implements BitFieldType {
  public BitFieldTypeNode(
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
  public PropertyTypeNode getBitFieldsDefinitionsNode() throws UaException {
    return ClientNodeSupport.await(getBitFieldsDefinitionsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getBitFieldsDefinitionsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "BitFieldsDefinitions",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable BitFieldDefinition @Nullable [] readBitFieldsDefinitions() throws UaException {
    return ClientNodeSupport.await(readBitFieldsDefinitionsAsync());
  }

  @Override
  public void writeBitFieldsDefinitions(@Nullable BitFieldDefinition @Nullable [] value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeBitFieldsDefinitionsAsync(value)),
        "http://opcfoundation.org/UA/}BitFieldsDefinitions");
  }

  @Override
  public CompletableFuture<? extends @Nullable BitFieldDefinition @Nullable []>
      readBitFieldsDefinitionsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getBitFieldsDefinitionsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}BitFieldsDefinitions",
                            true,
                            BitFieldDefinition.class,
                            1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable BitFieldDefinition @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeBitFieldsDefinitionsAsync(
      @Nullable BitFieldDefinition @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getBitFieldsDefinitionsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}BitFieldsDefinitions",
                        value,
                        BitFieldDefinition.class,
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
