package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.PublishedDataItemsTypeAddVariables;
import org.eclipse.milo.opcua.sdk.core.model.methods.PublishedDataItemsTypeRemoveVariables;
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
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedVariableDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link PublishedDataItemsType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.1">Model
 *     documentation</a>
 */
public class PublishedDataItemsTypeNode extends PublishedDataSetTypeNode
    implements PublishedDataItemsType {
  public PublishedDataItemsTypeNode(
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
  public PropertyTypeNode getPublishedDataNode() throws UaException {
    return ClientNodeSupport.await(getPublishedDataNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPublishedDataNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "PublishedData",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable PublishedVariableDataType @Nullable [] readPublishedData() throws UaException {
    return ClientNodeSupport.await(readPublishedDataAsync());
  }

  @Override
  public void writePublishedData(@Nullable PublishedVariableDataType @Nullable [] value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writePublishedDataAsync(value)),
        "http://opcfoundation.org/UA/}PublishedData");
  }

  @Override
  public CompletableFuture<? extends @Nullable PublishedVariableDataType @Nullable []>
      readPublishedDataAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getPublishedDataNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}PublishedData",
                            true,
                            PublishedVariableDataType.class,
                            1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable PublishedVariableDataType @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writePublishedDataAsync(
      @Nullable PublishedVariableDataType @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getPublishedDataNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}PublishedData",
                        value,
                        PublishedVariableDataType.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable UaMethodNode getAddVariablesMethodNode() throws UaException {
    return ClientNodeSupport.await(getAddVariablesMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getAddVariablesMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "AddVariables",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public PublishedDataItemsTypeAddVariables.Outputs addVariables(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable String @Nullable [] fieldNameAliases,
      Boolean @Nullable [] promotedFields,
      @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
      throws UaException {
    return ClientNodeSupport.await(
        addVariablesAsync(configurationVersion, fieldNameAliases, promotedFields, variablesToAdd));
  }

  @Override
  public MethodCallResult<PublishedDataItemsTypeAddVariables.Outputs> callAddVariables(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable String @Nullable [] fieldNameAliases,
      Boolean @Nullable [] promotedFields,
      @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
      throws UaException {
    return ClientNodeSupport.await(
        callAddVariablesAsync(
            configurationVersion, fieldNameAliases, promotedFields, variablesToAdd));
  }

  @Override
  public MethodCallResult<PublishedDataItemsTypeAddVariables.Outputs> callAddVariablesWith(
      MethodCallOptions options,
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable String @Nullable [] fieldNameAliases,
      Boolean @Nullable [] promotedFields,
      @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
      throws UaException {
    return ClientNodeSupport.await(
        callAddVariablesWithAsync(
            options, configurationVersion, fieldNameAliases, promotedFields, variablesToAdd));
  }

  @Override
  public CompletableFuture<PublishedDataItemsTypeAddVariables.Outputs> addVariablesAsync(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable String @Nullable [] fieldNameAliases,
      Boolean @Nullable [] promotedFields,
      @Nullable PublishedVariableDataType @Nullable [] variablesToAdd) {
    return ClientNodeSupport.compose(
        callAddVariablesAsync(
            configurationVersion, fieldNameAliases, promotedFields, variablesToAdd),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<PublishedDataItemsTypeAddVariables.Outputs>>
      callAddVariablesAsync(
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable String @Nullable [] fieldNameAliases,
          Boolean @Nullable [] promotedFields,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd) {
    return callAddVariablesWithAsync(
        MethodCallOptions.DEFAULT,
        configurationVersion,
        fieldNameAliases,
        promotedFields,
        variablesToAdd);
  }

  @Override
  public CompletableFuture<MethodCallResult<PublishedDataItemsTypeAddVariables.Outputs>>
      callAddVariablesWithAsync(
          MethodCallOptions options,
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable String @Nullable [] fieldNameAliases,
          Boolean @Nullable [] promotedFields,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new PublishedDataItemsTypeAddVariables.Inputs(
                      configurationVersion, fieldNameAliases, promotedFields, variablesToAdd)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getAddVariablesMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return PublishedDataItemsTypeAddVariables.Outputs.fromVariants(
                                        client.getStaticEncodingContext(), values);
                                  }))));
        });
  }

  @Override
  public @Nullable UaMethodNode getRemoveVariablesMethodNode() throws UaException {
    return ClientNodeSupport.await(getRemoveVariablesMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getRemoveVariablesMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "RemoveVariables",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public PublishedDataItemsTypeRemoveVariables.Outputs removeVariables(
      @Nullable ConfigurationVersionDataType configurationVersion,
      UInteger @Nullable [] variablesToRemove)
      throws UaException {
    return ClientNodeSupport.await(removeVariablesAsync(configurationVersion, variablesToRemove));
  }

  @Override
  public MethodCallResult<PublishedDataItemsTypeRemoveVariables.Outputs> callRemoveVariables(
      @Nullable ConfigurationVersionDataType configurationVersion,
      UInteger @Nullable [] variablesToRemove)
      throws UaException {
    return ClientNodeSupport.await(
        callRemoveVariablesAsync(configurationVersion, variablesToRemove));
  }

  @Override
  public MethodCallResult<PublishedDataItemsTypeRemoveVariables.Outputs> callRemoveVariablesWith(
      MethodCallOptions options,
      @Nullable ConfigurationVersionDataType configurationVersion,
      UInteger @Nullable [] variablesToRemove)
      throws UaException {
    return ClientNodeSupport.await(
        callRemoveVariablesWithAsync(options, configurationVersion, variablesToRemove));
  }

  @Override
  public CompletableFuture<PublishedDataItemsTypeRemoveVariables.Outputs> removeVariablesAsync(
      @Nullable ConfigurationVersionDataType configurationVersion,
      UInteger @Nullable [] variablesToRemove) {
    return ClientNodeSupport.compose(
        callRemoveVariablesAsync(configurationVersion, variablesToRemove),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<PublishedDataItemsTypeRemoveVariables.Outputs>>
      callRemoveVariablesAsync(
          @Nullable ConfigurationVersionDataType configurationVersion,
          UInteger @Nullable [] variablesToRemove) {
    return callRemoveVariablesWithAsync(
        MethodCallOptions.DEFAULT, configurationVersion, variablesToRemove);
  }

  @Override
  public CompletableFuture<MethodCallResult<PublishedDataItemsTypeRemoveVariables.Outputs>>
      callRemoveVariablesWithAsync(
          MethodCallOptions options,
          @Nullable ConfigurationVersionDataType configurationVersion,
          UInteger @Nullable [] variablesToRemove) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new PublishedDataItemsTypeRemoveVariables.Inputs(
                      configurationVersion, variablesToRemove)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getRemoveVariablesMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return PublishedDataItemsTypeRemoveVariables.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values);
                                  }))));
        });
  }
}
