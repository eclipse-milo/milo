package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
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
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link SubscriptionDiagnosticsArrayType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.11">Model
 *     documentation</a>
 */
public class SubscriptionDiagnosticsArrayTypeNode extends BaseDataVariableTypeNode
    implements SubscriptionDiagnosticsArrayType {
  public SubscriptionDiagnosticsArrayTypeNode(
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
  public @Nullable SubscriptionDiagnosticsDataType @Nullable [] readTypedValue()
      throws UaException {
    return ClientNodeSupport.await(readTypedValueAsync());
  }

  @Override
  public void writeTypedValue(@Nullable SubscriptionDiagnosticsDataType @Nullable [] value)
      throws UaException {
    ClientNodeSupport.good(ClientNodeSupport.await(writeTypedValueAsync(value)), "Value");
  }

  @Override
  public CompletableFuture<? extends @Nullable SubscriptionDiagnosticsDataType @Nullable []>
      readTypedValueAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    CompletableFuture.completedFuture(this),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "Value",
                            true,
                            SubscriptionDiagnosticsDataType.class,
                            1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable SubscriptionDiagnosticsDataType @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTypedValueAsync(
      @Nullable SubscriptionDiagnosticsDataType @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "Value",
                        value,
                        SubscriptionDiagnosticsDataType.class,
                        1,
                        null)));
  }
}
