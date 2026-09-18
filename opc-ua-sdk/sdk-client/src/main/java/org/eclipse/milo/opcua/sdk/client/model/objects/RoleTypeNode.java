package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.RoleTypeAddApplication;
import org.eclipse.milo.opcua.sdk.core.model.methods.RoleTypeAddEndpoint;
import org.eclipse.milo.opcua.sdk.core.model.methods.RoleTypeAddIdentity;
import org.eclipse.milo.opcua.sdk.core.model.methods.RoleTypeRemoveApplication;
import org.eclipse.milo.opcua.sdk.core.model.methods.RoleTypeRemoveEndpoint;
import org.eclipse.milo.opcua.sdk.core.model.methods.RoleTypeRemoveIdentity;
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
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointType;
import org.eclipse.milo.opcua.stack.core.types.structured.IdentityMappingRuleType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link RoleType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.1">Model
 *     documentation</a>
 */
public class RoleTypeNode extends BaseObjectTypeNode implements RoleType {
  public RoleTypeNode(
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
  public PropertyTypeNode getIdentitiesNode() throws UaException {
    return ClientNodeSupport.await(getIdentitiesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getIdentitiesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Identities",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable IdentityMappingRuleType @Nullable [] readIdentities() throws UaException {
    return ClientNodeSupport.await(readIdentitiesAsync());
  }

  @Override
  public void writeIdentities(@Nullable IdentityMappingRuleType @Nullable [] value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeIdentitiesAsync(value)),
        "http://opcfoundation.org/UA/}Identities");
  }

  @Override
  public CompletableFuture<? extends @Nullable IdentityMappingRuleType @Nullable []>
      readIdentitiesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getIdentitiesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Identities",
                            true,
                            IdentityMappingRuleType.class,
                            1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable IdentityMappingRuleType @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeIdentitiesAsync(
      @Nullable IdentityMappingRuleType @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getIdentitiesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Identities",
                        value,
                        IdentityMappingRuleType.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getApplicationsNode() throws UaException {
    return ClientNodeSupport.await(getApplicationsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getApplicationsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Applications",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String @Nullable [] readApplications() throws UaException {
    return ClientNodeSupport.await(readApplicationsAsync());
  }

  @Override
  public void writeApplications(@Nullable String @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeApplicationsAsync(value)),
        "http://opcfoundation.org/UA/}Applications");
  }

  @Override
  public CompletableFuture<? extends @Nullable String @Nullable []> readApplicationsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getApplicationsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Applications",
                            false,
                            String.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeApplicationsAsync(@Nullable String @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getApplicationsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Applications",
                        value,
                        String.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getEndpointsExcludeNode() throws UaException {
    return ClientNodeSupport.await(getEndpointsExcludeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getEndpointsExcludeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "EndpointsExclude",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readEndpointsExclude() throws UaException {
    return ClientNodeSupport.await(readEndpointsExcludeAsync());
  }

  @Override
  public void writeEndpointsExclude(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeEndpointsExcludeAsync(value)),
        "http://opcfoundation.org/UA/}EndpointsExclude");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readEndpointsExcludeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getEndpointsExcludeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}EndpointsExclude",
                            false,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeEndpointsExcludeAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getEndpointsExcludeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}EndpointsExclude",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getApplicationsExcludeNode() throws UaException {
    return ClientNodeSupport.await(getApplicationsExcludeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getApplicationsExcludeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ApplicationsExclude",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readApplicationsExclude() throws UaException {
    return ClientNodeSupport.await(readApplicationsExcludeAsync());
  }

  @Override
  public void writeApplicationsExclude(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeApplicationsExcludeAsync(value)),
        "http://opcfoundation.org/UA/}ApplicationsExclude");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readApplicationsExcludeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getApplicationsExcludeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ApplicationsExclude",
                            false,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeApplicationsExcludeAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getApplicationsExcludeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ApplicationsExclude",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getCustomConfigurationNode() throws UaException {
    return ClientNodeSupport.await(getCustomConfigurationNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getCustomConfigurationNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CustomConfiguration",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readCustomConfiguration() throws UaException {
    return ClientNodeSupport.await(readCustomConfigurationAsync());
  }

  @Override
  public void writeCustomConfiguration(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeCustomConfigurationAsync(value)),
        "http://opcfoundation.org/UA/}CustomConfiguration");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readCustomConfigurationAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getCustomConfigurationNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}CustomConfiguration",
                            false,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeCustomConfigurationAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getCustomConfigurationNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}CustomConfiguration",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getEndpointsNode() throws UaException {
    return ClientNodeSupport.await(getEndpointsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getEndpointsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Endpoints",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable EndpointType @Nullable [] readEndpoints() throws UaException {
    return ClientNodeSupport.await(readEndpointsAsync());
  }

  @Override
  public void writeEndpoints(@Nullable EndpointType @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeEndpointsAsync(value)),
        "http://opcfoundation.org/UA/}Endpoints");
  }

  @Override
  public CompletableFuture<? extends @Nullable EndpointType @Nullable []> readEndpointsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getEndpointsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Endpoints",
                            false,
                            EndpointType.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable EndpointType @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeEndpointsAsync(
      @Nullable EndpointType @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getEndpointsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Endpoints",
                        value,
                        EndpointType.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable UaMethodNode getAddApplicationMethodNode() throws UaException {
    return ClientNodeSupport.await(getAddApplicationMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getAddApplicationMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "AddApplication",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void addApplication(@Nullable String applicationUri) throws UaException {
    ClientNodeSupport.await(addApplicationAsync(applicationUri));
  }

  @Override
  public MethodCallResult<Void> callAddApplication(@Nullable String applicationUri)
      throws UaException {
    return ClientNodeSupport.await(callAddApplicationAsync(applicationUri));
  }

  @Override
  public MethodCallResult<Void> callAddApplicationWith(
      MethodCallOptions options, @Nullable String applicationUri) throws UaException {
    return ClientNodeSupport.await(callAddApplicationWithAsync(options, applicationUri));
  }

  @Override
  public CompletableFuture<Void> addApplicationAsync(@Nullable String applicationUri) {
    return ClientNodeSupport.compose(
        callAddApplicationAsync(applicationUri),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callAddApplicationAsync(
      @Nullable String applicationUri) {
    return callAddApplicationWithAsync(MethodCallOptions.DEFAULT, applicationUri);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callAddApplicationWithAsync(
      MethodCallOptions options, @Nullable String applicationUri) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new RoleTypeAddApplication.Inputs(applicationUri)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getAddApplicationMethodNodeAsync(),
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

  @Override
  public @Nullable UaMethodNode getAddEndpointMethodNode() throws UaException {
    return ClientNodeSupport.await(getAddEndpointMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getAddEndpointMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "AddEndpoint",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void addEndpoint(@Nullable EndpointType endpoint) throws UaException {
    ClientNodeSupport.await(addEndpointAsync(endpoint));
  }

  @Override
  public MethodCallResult<Void> callAddEndpoint(@Nullable EndpointType endpoint)
      throws UaException {
    return ClientNodeSupport.await(callAddEndpointAsync(endpoint));
  }

  @Override
  public MethodCallResult<Void> callAddEndpointWith(
      MethodCallOptions options, @Nullable EndpointType endpoint) throws UaException {
    return ClientNodeSupport.await(callAddEndpointWithAsync(options, endpoint));
  }

  @Override
  public CompletableFuture<Void> addEndpointAsync(@Nullable EndpointType endpoint) {
    return ClientNodeSupport.compose(
        callAddEndpointAsync(endpoint),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callAddEndpointAsync(
      @Nullable EndpointType endpoint) {
    return callAddEndpointWithAsync(MethodCallOptions.DEFAULT, endpoint);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callAddEndpointWithAsync(
      MethodCallOptions options, @Nullable EndpointType endpoint) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new RoleTypeAddEndpoint.Inputs(endpoint)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getAddEndpointMethodNodeAsync(),
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

  @Override
  public @Nullable UaMethodNode getAddIdentityMethodNode() throws UaException {
    return ClientNodeSupport.await(getAddIdentityMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getAddIdentityMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "AddIdentity",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void addIdentity(@Nullable IdentityMappingRuleType rule) throws UaException {
    ClientNodeSupport.await(addIdentityAsync(rule));
  }

  @Override
  public MethodCallResult<Void> callAddIdentity(@Nullable IdentityMappingRuleType rule)
      throws UaException {
    return ClientNodeSupport.await(callAddIdentityAsync(rule));
  }

  @Override
  public MethodCallResult<Void> callAddIdentityWith(
      MethodCallOptions options, @Nullable IdentityMappingRuleType rule) throws UaException {
    return ClientNodeSupport.await(callAddIdentityWithAsync(options, rule));
  }

  @Override
  public CompletableFuture<Void> addIdentityAsync(@Nullable IdentityMappingRuleType rule) {
    return ClientNodeSupport.compose(
        callAddIdentityAsync(rule),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callAddIdentityAsync(
      @Nullable IdentityMappingRuleType rule) {
    return callAddIdentityWithAsync(MethodCallOptions.DEFAULT, rule);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callAddIdentityWithAsync(
      MethodCallOptions options, @Nullable IdentityMappingRuleType rule) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new RoleTypeAddIdentity.Inputs(rule).toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getAddIdentityMethodNodeAsync(),
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

  @Override
  public @Nullable UaMethodNode getRemoveApplicationMethodNode() throws UaException {
    return ClientNodeSupport.await(getRemoveApplicationMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getRemoveApplicationMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "RemoveApplication",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void removeApplication(@Nullable String applicationUri) throws UaException {
    ClientNodeSupport.await(removeApplicationAsync(applicationUri));
  }

  @Override
  public MethodCallResult<Void> callRemoveApplication(@Nullable String applicationUri)
      throws UaException {
    return ClientNodeSupport.await(callRemoveApplicationAsync(applicationUri));
  }

  @Override
  public MethodCallResult<Void> callRemoveApplicationWith(
      MethodCallOptions options, @Nullable String applicationUri) throws UaException {
    return ClientNodeSupport.await(callRemoveApplicationWithAsync(options, applicationUri));
  }

  @Override
  public CompletableFuture<Void> removeApplicationAsync(@Nullable String applicationUri) {
    return ClientNodeSupport.compose(
        callRemoveApplicationAsync(applicationUri),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveApplicationAsync(
      @Nullable String applicationUri) {
    return callRemoveApplicationWithAsync(MethodCallOptions.DEFAULT, applicationUri);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveApplicationWithAsync(
      MethodCallOptions options, @Nullable String applicationUri) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new RoleTypeRemoveApplication.Inputs(applicationUri)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getRemoveApplicationMethodNodeAsync(),
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

  @Override
  public @Nullable UaMethodNode getRemoveEndpointMethodNode() throws UaException {
    return ClientNodeSupport.await(getRemoveEndpointMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getRemoveEndpointMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "RemoveEndpoint",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void removeEndpoint(@Nullable EndpointType endpoint) throws UaException {
    ClientNodeSupport.await(removeEndpointAsync(endpoint));
  }

  @Override
  public MethodCallResult<Void> callRemoveEndpoint(@Nullable EndpointType endpoint)
      throws UaException {
    return ClientNodeSupport.await(callRemoveEndpointAsync(endpoint));
  }

  @Override
  public MethodCallResult<Void> callRemoveEndpointWith(
      MethodCallOptions options, @Nullable EndpointType endpoint) throws UaException {
    return ClientNodeSupport.await(callRemoveEndpointWithAsync(options, endpoint));
  }

  @Override
  public CompletableFuture<Void> removeEndpointAsync(@Nullable EndpointType endpoint) {
    return ClientNodeSupport.compose(
        callRemoveEndpointAsync(endpoint),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveEndpointAsync(
      @Nullable EndpointType endpoint) {
    return callRemoveEndpointWithAsync(MethodCallOptions.DEFAULT, endpoint);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveEndpointWithAsync(
      MethodCallOptions options, @Nullable EndpointType endpoint) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new RoleTypeRemoveEndpoint.Inputs(endpoint)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getRemoveEndpointMethodNodeAsync(),
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

  @Override
  public @Nullable UaMethodNode getRemoveIdentityMethodNode() throws UaException {
    return ClientNodeSupport.await(getRemoveIdentityMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getRemoveIdentityMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "RemoveIdentity",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void removeIdentity(@Nullable IdentityMappingRuleType rule) throws UaException {
    ClientNodeSupport.await(removeIdentityAsync(rule));
  }

  @Override
  public MethodCallResult<Void> callRemoveIdentity(@Nullable IdentityMappingRuleType rule)
      throws UaException {
    return ClientNodeSupport.await(callRemoveIdentityAsync(rule));
  }

  @Override
  public MethodCallResult<Void> callRemoveIdentityWith(
      MethodCallOptions options, @Nullable IdentityMappingRuleType rule) throws UaException {
    return ClientNodeSupport.await(callRemoveIdentityWithAsync(options, rule));
  }

  @Override
  public CompletableFuture<Void> removeIdentityAsync(@Nullable IdentityMappingRuleType rule) {
    return ClientNodeSupport.compose(
        callRemoveIdentityAsync(rule),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveIdentityAsync(
      @Nullable IdentityMappingRuleType rule) {
    return callRemoveIdentityWithAsync(MethodCallOptions.DEFAULT, rule);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveIdentityWithAsync(
      MethodCallOptions options, @Nullable IdentityMappingRuleType rule) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new RoleTypeRemoveIdentity.Inputs(rule).toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getRemoveIdentityMethodNodeAsync(),
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
