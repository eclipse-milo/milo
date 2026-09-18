package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.AliasNameCategoryTypeAddAliasesToCategory;
import org.eclipse.milo.opcua.sdk.core.model.methods.AliasNameCategoryTypeDeleteAliasesFromCategory;
import org.eclipse.milo.opcua.sdk.core.model.methods.AliasNameCategoryTypeFindAlias;
import org.eclipse.milo.opcua.sdk.core.model.methods.AliasNameCategoryTypeFindAliasVerbose;
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
import org.eclipse.milo.opcua.stack.core.types.structured.AliasNameDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.AliasNameVerboseDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link AliasNameCategoryType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.1">Model
 *     documentation</a>
 */
public class AliasNameCategoryTypeNode extends FolderTypeNode implements AliasNameCategoryType {
  public AliasNameCategoryTypeNode(
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
  public @Nullable PropertyTypeNode getLastChangeNode() throws UaException {
    return ClientNodeSupport.await(getLastChangeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getLastChangeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LastChange",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readLastChange() throws UaException {
    return ClientNodeSupport.await(readLastChangeAsync());
  }

  @Override
  public void writeLastChange(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeLastChangeAsync(value)),
        "http://opcfoundation.org/UA/}LastChange");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readLastChangeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getLastChangeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}LastChange",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeLastChangeAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getLastChangeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}LastChange",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable UaMethodNode getAddAliasesToCategoryMethodNode() throws UaException {
    return ClientNodeSupport.await(getAddAliasesToCategoryMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getAddAliasesToCategoryMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "AddAliasesToCategory",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public StatusCode @Nullable [] addAliasesToCategory(
      @Nullable String @Nullable [] aliasNames,
      ExpandedNodeId @Nullable [] targetNodes,
      @Nullable String @Nullable [] targetServers,
      @Nullable NodeId targetReferenceType)
      throws UaException {
    return ClientNodeSupport.await(
        addAliasesToCategoryAsync(aliasNames, targetNodes, targetServers, targetReferenceType));
  }

  @Override
  public MethodCallResult<StatusCode @Nullable []> callAddAliasesToCategory(
      @Nullable String @Nullable [] aliasNames,
      ExpandedNodeId @Nullable [] targetNodes,
      @Nullable String @Nullable [] targetServers,
      @Nullable NodeId targetReferenceType)
      throws UaException {
    return ClientNodeSupport.await(
        callAddAliasesToCategoryAsync(aliasNames, targetNodes, targetServers, targetReferenceType));
  }

  @Override
  public MethodCallResult<StatusCode @Nullable []> callAddAliasesToCategoryWith(
      MethodCallOptions options,
      @Nullable String @Nullable [] aliasNames,
      ExpandedNodeId @Nullable [] targetNodes,
      @Nullable String @Nullable [] targetServers,
      @Nullable NodeId targetReferenceType)
      throws UaException {
    return ClientNodeSupport.await(
        callAddAliasesToCategoryWithAsync(
            options, aliasNames, targetNodes, targetServers, targetReferenceType));
  }

  @Override
  public CompletableFuture<StatusCode @Nullable []> addAliasesToCategoryAsync(
      @Nullable String @Nullable [] aliasNames,
      ExpandedNodeId @Nullable [] targetNodes,
      @Nullable String @Nullable [] targetServers,
      @Nullable NodeId targetReferenceType) {
    return ClientNodeSupport.compose(
        callAddAliasesToCategoryAsync(aliasNames, targetNodes, targetServers, targetReferenceType),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<StatusCode @Nullable []>> callAddAliasesToCategoryAsync(
      @Nullable String @Nullable [] aliasNames,
      ExpandedNodeId @Nullable [] targetNodes,
      @Nullable String @Nullable [] targetServers,
      @Nullable NodeId targetReferenceType) {
    return callAddAliasesToCategoryWithAsync(
        MethodCallOptions.DEFAULT, aliasNames, targetNodes, targetServers, targetReferenceType);
  }

  @Override
  public CompletableFuture<MethodCallResult<StatusCode @Nullable []>>
      callAddAliasesToCategoryWithAsync(
          MethodCallOptions options,
          @Nullable String @Nullable [] aliasNames,
          ExpandedNodeId @Nullable [] targetNodes,
          @Nullable String @Nullable [] targetServers,
          @Nullable NodeId targetReferenceType) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new AliasNameCategoryTypeAddAliasesToCategory.Inputs(
                      aliasNames, targetNodes, targetServers, targetReferenceType)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getAddAliasesToCategoryMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return AliasNameCategoryTypeAddAliasesToCategory.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values)
                                        .errorCodes();
                                  }))));
        });
  }

  @Override
  public @Nullable UaMethodNode getDeleteAliasesFromCategoryMethodNode() throws UaException {
    return ClientNodeSupport.await(getDeleteAliasesFromCategoryMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getDeleteAliasesFromCategoryMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "DeleteAliasesFromCategory",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public StatusCode @Nullable [] deleteAliasesFromCategory(
      @Nullable String @Nullable [] aliasNames, ExpandedNodeId @Nullable [] targetNodes)
      throws UaException {
    return ClientNodeSupport.await(deleteAliasesFromCategoryAsync(aliasNames, targetNodes));
  }

  @Override
  public MethodCallResult<StatusCode @Nullable []> callDeleteAliasesFromCategory(
      @Nullable String @Nullable [] aliasNames, ExpandedNodeId @Nullable [] targetNodes)
      throws UaException {
    return ClientNodeSupport.await(callDeleteAliasesFromCategoryAsync(aliasNames, targetNodes));
  }

  @Override
  public MethodCallResult<StatusCode @Nullable []> callDeleteAliasesFromCategoryWith(
      MethodCallOptions options,
      @Nullable String @Nullable [] aliasNames,
      ExpandedNodeId @Nullable [] targetNodes)
      throws UaException {
    return ClientNodeSupport.await(
        callDeleteAliasesFromCategoryWithAsync(options, aliasNames, targetNodes));
  }

  @Override
  public CompletableFuture<StatusCode @Nullable []> deleteAliasesFromCategoryAsync(
      @Nullable String @Nullable [] aliasNames, ExpandedNodeId @Nullable [] targetNodes) {
    return ClientNodeSupport.compose(
        callDeleteAliasesFromCategoryAsync(aliasNames, targetNodes),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<StatusCode @Nullable []>>
      callDeleteAliasesFromCategoryAsync(
          @Nullable String @Nullable [] aliasNames, ExpandedNodeId @Nullable [] targetNodes) {
    return callDeleteAliasesFromCategoryWithAsync(
        MethodCallOptions.DEFAULT, aliasNames, targetNodes);
  }

  @Override
  public CompletableFuture<MethodCallResult<StatusCode @Nullable []>>
      callDeleteAliasesFromCategoryWithAsync(
          MethodCallOptions options,
          @Nullable String @Nullable [] aliasNames,
          ExpandedNodeId @Nullable [] targetNodes) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new AliasNameCategoryTypeDeleteAliasesFromCategory.Inputs(aliasNames, targetNodes)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getDeleteAliasesFromCategoryMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return AliasNameCategoryTypeDeleteAliasesFromCategory.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values)
                                        .errorCodes();
                                  }))));
        });
  }

  @Override
  public UaMethodNode getFindAliasMethodNode() throws UaException {
    return ClientNodeSupport.await(getFindAliasMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getFindAliasMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "FindAlias",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable AliasNameDataType @Nullable [] findAlias(
      @Nullable String aliasNameSearchPattern, @Nullable NodeId referenceTypeFilter)
      throws UaException {
    return ClientNodeSupport.await(findAliasAsync(aliasNameSearchPattern, referenceTypeFilter));
  }

  @Override
  public MethodCallResult<@Nullable AliasNameDataType @Nullable []> callFindAlias(
      @Nullable String aliasNameSearchPattern, @Nullable NodeId referenceTypeFilter)
      throws UaException {
    return ClientNodeSupport.await(callFindAliasAsync(aliasNameSearchPattern, referenceTypeFilter));
  }

  @Override
  public MethodCallResult<@Nullable AliasNameDataType @Nullable []> callFindAliasWith(
      MethodCallOptions options,
      @Nullable String aliasNameSearchPattern,
      @Nullable NodeId referenceTypeFilter)
      throws UaException {
    return ClientNodeSupport.await(
        callFindAliasWithAsync(options, aliasNameSearchPattern, referenceTypeFilter));
  }

  @Override
  public CompletableFuture<@Nullable AliasNameDataType @Nullable []> findAliasAsync(
      @Nullable String aliasNameSearchPattern, @Nullable NodeId referenceTypeFilter) {
    return ClientNodeSupport.compose(
        callFindAliasAsync(aliasNameSearchPattern, referenceTypeFilter),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable AliasNameDataType @Nullable []>>
      callFindAliasAsync(
          @Nullable String aliasNameSearchPattern, @Nullable NodeId referenceTypeFilter) {
    return callFindAliasWithAsync(
        MethodCallOptions.DEFAULT, aliasNameSearchPattern, referenceTypeFilter);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable AliasNameDataType @Nullable []>>
      callFindAliasWithAsync(
          MethodCallOptions options,
          @Nullable String aliasNameSearchPattern,
          @Nullable NodeId referenceTypeFilter) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new AliasNameCategoryTypeFindAlias.Inputs(aliasNameSearchPattern, referenceTypeFilter)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getFindAliasMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return AliasNameCategoryTypeFindAlias.Outputs.fromVariants(
                                            client.getStaticEncodingContext(), values)
                                        .aliasNodeList();
                                  }))));
        });
  }

  @Override
  public @Nullable UaMethodNode getFindAliasVerboseMethodNode() throws UaException {
    return ClientNodeSupport.await(getFindAliasVerboseMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getFindAliasVerboseMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "FindAliasVerbose",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable AliasNameVerboseDataType @Nullable [] findAliasVerbose(
      @Nullable String aliasNameSearchPattern, @Nullable NodeId referenceTypeFilter)
      throws UaException {
    return ClientNodeSupport.await(
        findAliasVerboseAsync(aliasNameSearchPattern, referenceTypeFilter));
  }

  @Override
  public MethodCallResult<@Nullable AliasNameVerboseDataType @Nullable []> callFindAliasVerbose(
      @Nullable String aliasNameSearchPattern, @Nullable NodeId referenceTypeFilter)
      throws UaException {
    return ClientNodeSupport.await(
        callFindAliasVerboseAsync(aliasNameSearchPattern, referenceTypeFilter));
  }

  @Override
  public MethodCallResult<@Nullable AliasNameVerboseDataType @Nullable []> callFindAliasVerboseWith(
      MethodCallOptions options,
      @Nullable String aliasNameSearchPattern,
      @Nullable NodeId referenceTypeFilter)
      throws UaException {
    return ClientNodeSupport.await(
        callFindAliasVerboseWithAsync(options, aliasNameSearchPattern, referenceTypeFilter));
  }

  @Override
  public CompletableFuture<@Nullable AliasNameVerboseDataType @Nullable []> findAliasVerboseAsync(
      @Nullable String aliasNameSearchPattern, @Nullable NodeId referenceTypeFilter) {
    return ClientNodeSupport.compose(
        callFindAliasVerboseAsync(aliasNameSearchPattern, referenceTypeFilter),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable AliasNameVerboseDataType @Nullable []>>
      callFindAliasVerboseAsync(
          @Nullable String aliasNameSearchPattern, @Nullable NodeId referenceTypeFilter) {
    return callFindAliasVerboseWithAsync(
        MethodCallOptions.DEFAULT, aliasNameSearchPattern, referenceTypeFilter);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable AliasNameVerboseDataType @Nullable []>>
      callFindAliasVerboseWithAsync(
          MethodCallOptions options,
          @Nullable String aliasNameSearchPattern,
          @Nullable NodeId referenceTypeFilter) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new AliasNameCategoryTypeFindAliasVerbose.Inputs(
                      aliasNameSearchPattern, referenceTypeFilter)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getFindAliasVerboseMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return AliasNameCategoryTypeFindAliasVerbose.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values)
                                        .aliasNodeList();
                                  }))));
        });
  }
}
