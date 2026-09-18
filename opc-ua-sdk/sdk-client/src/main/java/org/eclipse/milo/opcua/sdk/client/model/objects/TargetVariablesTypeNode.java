package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.TargetVariablesTypeAddTargetVariables;
import org.eclipse.milo.opcua.sdk.core.model.methods.TargetVariablesTypeRemoveTargetVariables;
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
import org.eclipse.milo.opcua.stack.core.types.structured.FieldTargetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link TargetVariablesType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.1">Model
 *     documentation</a>
 */
public class TargetVariablesTypeNode extends SubscribedDataSetTypeNode
    implements TargetVariablesType {
  public TargetVariablesTypeNode(
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
  public PropertyTypeNode getTargetVariablesNode() throws UaException {
    return ClientNodeSupport.await(getTargetVariablesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getTargetVariablesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "TargetVariables",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable FieldTargetDataType @Nullable [] readTargetVariables() throws UaException {
    return ClientNodeSupport.await(readTargetVariablesAsync());
  }

  @Override
  public void writeTargetVariables(@Nullable FieldTargetDataType @Nullable [] value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeTargetVariablesAsync(value)),
        "http://opcfoundation.org/UA/}TargetVariables");
  }

  @Override
  public CompletableFuture<? extends @Nullable FieldTargetDataType @Nullable []>
      readTargetVariablesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getTargetVariablesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}TargetVariables",
                            true,
                            FieldTargetDataType.class,
                            1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable FieldTargetDataType @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTargetVariablesAsync(
      @Nullable FieldTargetDataType @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getTargetVariablesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}TargetVariables",
                        value,
                        FieldTargetDataType.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable UaMethodNode getAddTargetVariablesMethodNode() throws UaException {
    return ClientNodeSupport.await(getAddTargetVariablesMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getAddTargetVariablesMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "AddTargetVariables",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public StatusCode @Nullable [] addTargetVariables(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd)
      throws UaException {
    return ClientNodeSupport.await(
        addTargetVariablesAsync(configurationVersion, targetVariablesToAdd));
  }

  @Override
  public MethodCallResult<StatusCode @Nullable []> callAddTargetVariables(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd)
      throws UaException {
    return ClientNodeSupport.await(
        callAddTargetVariablesAsync(configurationVersion, targetVariablesToAdd));
  }

  @Override
  public MethodCallResult<StatusCode @Nullable []> callAddTargetVariablesWith(
      MethodCallOptions options,
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd)
      throws UaException {
    return ClientNodeSupport.await(
        callAddTargetVariablesWithAsync(options, configurationVersion, targetVariablesToAdd));
  }

  @Override
  public CompletableFuture<StatusCode @Nullable []> addTargetVariablesAsync(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd) {
    return ClientNodeSupport.compose(
        callAddTargetVariablesAsync(configurationVersion, targetVariablesToAdd),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<StatusCode @Nullable []>> callAddTargetVariablesAsync(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd) {
    return callAddTargetVariablesWithAsync(
        MethodCallOptions.DEFAULT, configurationVersion, targetVariablesToAdd);
  }

  @Override
  public CompletableFuture<MethodCallResult<StatusCode @Nullable []>>
      callAddTargetVariablesWithAsync(
          MethodCallOptions options,
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new TargetVariablesTypeAddTargetVariables.Inputs(
                      configurationVersion, targetVariablesToAdd)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getAddTargetVariablesMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return TargetVariablesTypeAddTargetVariables.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values)
                                        .addResults();
                                  }))));
        });
  }

  @Override
  public @Nullable UaMethodNode getRemoveTargetVariablesMethodNode() throws UaException {
    return ClientNodeSupport.await(getRemoveTargetVariablesMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getRemoveTargetVariablesMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "RemoveTargetVariables",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public StatusCode @Nullable [] removeTargetVariables(
      @Nullable ConfigurationVersionDataType configurationVersion,
      UInteger @Nullable [] targetsToRemove)
      throws UaException {
    return ClientNodeSupport.await(
        removeTargetVariablesAsync(configurationVersion, targetsToRemove));
  }

  @Override
  public MethodCallResult<StatusCode @Nullable []> callRemoveTargetVariables(
      @Nullable ConfigurationVersionDataType configurationVersion,
      UInteger @Nullable [] targetsToRemove)
      throws UaException {
    return ClientNodeSupport.await(
        callRemoveTargetVariablesAsync(configurationVersion, targetsToRemove));
  }

  @Override
  public MethodCallResult<StatusCode @Nullable []> callRemoveTargetVariablesWith(
      MethodCallOptions options,
      @Nullable ConfigurationVersionDataType configurationVersion,
      UInteger @Nullable [] targetsToRemove)
      throws UaException {
    return ClientNodeSupport.await(
        callRemoveTargetVariablesWithAsync(options, configurationVersion, targetsToRemove));
  }

  @Override
  public CompletableFuture<StatusCode @Nullable []> removeTargetVariablesAsync(
      @Nullable ConfigurationVersionDataType configurationVersion,
      UInteger @Nullable [] targetsToRemove) {
    return ClientNodeSupport.compose(
        callRemoveTargetVariablesAsync(configurationVersion, targetsToRemove),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<StatusCode @Nullable []>>
      callRemoveTargetVariablesAsync(
          @Nullable ConfigurationVersionDataType configurationVersion,
          UInteger @Nullable [] targetsToRemove) {
    return callRemoveTargetVariablesWithAsync(
        MethodCallOptions.DEFAULT, configurationVersion, targetsToRemove);
  }

  @Override
  public CompletableFuture<MethodCallResult<StatusCode @Nullable []>>
      callRemoveTargetVariablesWithAsync(
          MethodCallOptions options,
          @Nullable ConfigurationVersionDataType configurationVersion,
          UInteger @Nullable [] targetsToRemove) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new TargetVariablesTypeRemoveTargetVariables.Inputs(
                      configurationVersion, targetsToRemove)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getRemoveTargetVariablesMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return TargetVariablesTypeRemoveTargetVariables.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values)
                                        .removeResults();
                                  }))));
        });
  }
}
