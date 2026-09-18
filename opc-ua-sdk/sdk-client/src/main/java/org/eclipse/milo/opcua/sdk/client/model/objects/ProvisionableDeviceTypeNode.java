package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.ProvisionableDeviceTypeRequestTickets;
import org.eclipse.milo.opcua.sdk.core.model.methods.ProvisionableDeviceTypeSetRegistrarEndpoints;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ProvisionableDeviceType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.3">Model
 *     documentation</a>
 */
public class ProvisionableDeviceTypeNode extends BaseObjectTypeNode
    implements ProvisionableDeviceType {
  public ProvisionableDeviceTypeNode(
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
  public PropertyTypeNode getIsSingletonNode() throws UaException {
    return ClientNodeSupport.await(getIsSingletonNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getIsSingletonNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "IsSingleton",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readIsSingleton() throws UaException {
    return ClientNodeSupport.await(readIsSingletonAsync());
  }

  @Override
  public void writeIsSingleton(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeIsSingletonAsync(value)),
        "http://opcfoundation.org/UA/}IsSingleton");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readIsSingletonAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getIsSingletonNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}IsSingleton",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeIsSingletonAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getIsSingletonNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}IsSingleton",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public UaMethodNode getRequestTicketsMethodNode() throws UaException {
    return ClientNodeSupport.await(getRequestTicketsMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getRequestTicketsMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "RequestTickets",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable String @Nullable [] requestTickets() throws UaException {
    return ClientNodeSupport.await(requestTicketsAsync());
  }

  @Override
  public MethodCallResult<@Nullable String @Nullable []> callRequestTickets() throws UaException {
    return ClientNodeSupport.await(callRequestTicketsAsync());
  }

  @Override
  public MethodCallResult<@Nullable String @Nullable []> callRequestTicketsWith(
      MethodCallOptions options) throws UaException {
    return ClientNodeSupport.await(callRequestTicketsWithAsync(options));
  }

  @Override
  public CompletableFuture<@Nullable String @Nullable []> requestTicketsAsync() {
    return ClientNodeSupport.compose(
        callRequestTicketsAsync(),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable String @Nullable []>>
      callRequestTicketsAsync() {
    return callRequestTicketsWithAsync(MethodCallOptions.DEFAULT);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable String @Nullable []>>
      callRequestTicketsWithAsync(MethodCallOptions options) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs = new Variant[0];
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getRequestTicketsMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return ProvisionableDeviceTypeRequestTickets.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values)
                                        .tickets();
                                  }))));
        });
  }

  @Override
  public @Nullable UaMethodNode getSetRegistrarEndpointsMethodNode() throws UaException {
    return ClientNodeSupport.await(getSetRegistrarEndpointsMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getSetRegistrarEndpointsMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "SetRegistrarEndpoints",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void setRegistrarEndpoints(@Nullable ApplicationDescription @Nullable [] registrars)
      throws UaException {
    ClientNodeSupport.await(setRegistrarEndpointsAsync(registrars));
  }

  @Override
  public MethodCallResult<Void> callSetRegistrarEndpoints(
      @Nullable ApplicationDescription @Nullable [] registrars) throws UaException {
    return ClientNodeSupport.await(callSetRegistrarEndpointsAsync(registrars));
  }

  @Override
  public MethodCallResult<Void> callSetRegistrarEndpointsWith(
      MethodCallOptions options, @Nullable ApplicationDescription @Nullable [] registrars)
      throws UaException {
    return ClientNodeSupport.await(callSetRegistrarEndpointsWithAsync(options, registrars));
  }

  @Override
  public CompletableFuture<Void> setRegistrarEndpointsAsync(
      @Nullable ApplicationDescription @Nullable [] registrars) {
    return ClientNodeSupport.compose(
        callSetRegistrarEndpointsAsync(registrars),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callSetRegistrarEndpointsAsync(
      @Nullable ApplicationDescription @Nullable [] registrars) {
    return callSetRegistrarEndpointsWithAsync(MethodCallOptions.DEFAULT, registrars);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callSetRegistrarEndpointsWithAsync(
      MethodCallOptions options, @Nullable ApplicationDescription @Nullable [] registrars) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new ProvisionableDeviceTypeSetRegistrarEndpoints.Inputs(registrars)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getSetRegistrarEndpointsMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    if (values == null || values.length != 0)
                                      throw new UaException(
                                          StatusCodes.Bad_DecodingError,
                                          "expected no Method outputs");
                                    return null;
                                  }))));
        });
  }
}
