package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.PriorityMappingTableTypeAddPriorityMappingEntry;
import org.eclipse.milo.opcua.sdk.core.model.methods.PriorityMappingTableTypeDeletePriorityMappingEntry;
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
import org.eclipse.milo.opcua.stack.core.types.structured.PriorityMappingEntryType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link PriorityMappingTableType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.2">Model
 *     documentation</a>
 */
public class PriorityMappingTableTypeNode extends BaseObjectTypeNode
    implements PriorityMappingTableType {
  public PriorityMappingTableTypeNode(
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
  public PropertyTypeNode getPriorityMapppingEntriesNode() throws UaException {
    return ClientNodeSupport.await(getPriorityMapppingEntriesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPriorityMapppingEntriesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "PriorityMapppingEntries",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable PriorityMappingEntryType @Nullable [] readPriorityMapppingEntries()
      throws UaException {
    return ClientNodeSupport.await(readPriorityMapppingEntriesAsync());
  }

  @Override
  public void writePriorityMapppingEntries(@Nullable PriorityMappingEntryType @Nullable [] value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writePriorityMapppingEntriesAsync(value)),
        "http://opcfoundation.org/UA/}PriorityMapppingEntries");
  }

  @Override
  public CompletableFuture<? extends @Nullable PriorityMappingEntryType @Nullable []>
      readPriorityMapppingEntriesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getPriorityMapppingEntriesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}PriorityMapppingEntries",
                            true,
                            PriorityMappingEntryType.class,
                            1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable PriorityMappingEntryType @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writePriorityMapppingEntriesAsync(
      @Nullable PriorityMappingEntryType @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getPriorityMapppingEntriesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}PriorityMapppingEntries",
                        value,
                        PriorityMappingEntryType.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable UaMethodNode getAddPriorityMappingEntryMethodNode() throws UaException {
    return ClientNodeSupport.await(getAddPriorityMappingEntryMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getAddPriorityMappingEntryMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "AddPriorityMappingEntry",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void addPriorityMappingEntry(
      @Nullable String mappingUri,
      @Nullable String priorityLabel,
      @Nullable UByte priorityValue_PCP,
      @Nullable UInteger priorityValue_DSCP)
      throws UaException {
    ClientNodeSupport.await(
        addPriorityMappingEntryAsync(
            mappingUri, priorityLabel, priorityValue_PCP, priorityValue_DSCP));
  }

  @Override
  public MethodCallResult<Void> callAddPriorityMappingEntry(
      @Nullable String mappingUri,
      @Nullable String priorityLabel,
      @Nullable UByte priorityValue_PCP,
      @Nullable UInteger priorityValue_DSCP)
      throws UaException {
    return ClientNodeSupport.await(
        callAddPriorityMappingEntryAsync(
            mappingUri, priorityLabel, priorityValue_PCP, priorityValue_DSCP));
  }

  @Override
  public MethodCallResult<Void> callAddPriorityMappingEntryWith(
      MethodCallOptions options,
      @Nullable String mappingUri,
      @Nullable String priorityLabel,
      @Nullable UByte priorityValue_PCP,
      @Nullable UInteger priorityValue_DSCP)
      throws UaException {
    return ClientNodeSupport.await(
        callAddPriorityMappingEntryWithAsync(
            options, mappingUri, priorityLabel, priorityValue_PCP, priorityValue_DSCP));
  }

  @Override
  public CompletableFuture<Void> addPriorityMappingEntryAsync(
      @Nullable String mappingUri,
      @Nullable String priorityLabel,
      @Nullable UByte priorityValue_PCP,
      @Nullable UInteger priorityValue_DSCP) {
    return ClientNodeSupport.compose(
        callAddPriorityMappingEntryAsync(
            mappingUri, priorityLabel, priorityValue_PCP, priorityValue_DSCP),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callAddPriorityMappingEntryAsync(
      @Nullable String mappingUri,
      @Nullable String priorityLabel,
      @Nullable UByte priorityValue_PCP,
      @Nullable UInteger priorityValue_DSCP) {
    return callAddPriorityMappingEntryWithAsync(
        MethodCallOptions.DEFAULT,
        mappingUri,
        priorityLabel,
        priorityValue_PCP,
        priorityValue_DSCP);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callAddPriorityMappingEntryWithAsync(
      MethodCallOptions options,
      @Nullable String mappingUri,
      @Nullable String priorityLabel,
      @Nullable UByte priorityValue_PCP,
      @Nullable UInteger priorityValue_DSCP) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new PriorityMappingTableTypeAddPriorityMappingEntry.Inputs(
                      mappingUri, priorityLabel, priorityValue_PCP, priorityValue_DSCP)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getAddPriorityMappingEntryMethodNodeAsync(),
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
  public @Nullable UaMethodNode getDeletePriorityMappingEntryMethodNode() throws UaException {
    return ClientNodeSupport.await(getDeletePriorityMappingEntryMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getDeletePriorityMappingEntryMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "DeletePriorityMappingEntry",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void deletePriorityMappingEntry(
      @Nullable String mappingUri, @Nullable String priorityLabel) throws UaException {
    ClientNodeSupport.await(deletePriorityMappingEntryAsync(mappingUri, priorityLabel));
  }

  @Override
  public MethodCallResult<Void> callDeletePriorityMappingEntry(
      @Nullable String mappingUri, @Nullable String priorityLabel) throws UaException {
    return ClientNodeSupport.await(callDeletePriorityMappingEntryAsync(mappingUri, priorityLabel));
  }

  @Override
  public MethodCallResult<Void> callDeletePriorityMappingEntryWith(
      MethodCallOptions options, @Nullable String mappingUri, @Nullable String priorityLabel)
      throws UaException {
    return ClientNodeSupport.await(
        callDeletePriorityMappingEntryWithAsync(options, mappingUri, priorityLabel));
  }

  @Override
  public CompletableFuture<Void> deletePriorityMappingEntryAsync(
      @Nullable String mappingUri, @Nullable String priorityLabel) {
    return ClientNodeSupport.compose(
        callDeletePriorityMappingEntryAsync(mappingUri, priorityLabel),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callDeletePriorityMappingEntryAsync(
      @Nullable String mappingUri, @Nullable String priorityLabel) {
    return callDeletePriorityMappingEntryWithAsync(
        MethodCallOptions.DEFAULT, mappingUri, priorityLabel);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callDeletePriorityMappingEntryWithAsync(
      MethodCallOptions options, @Nullable String mappingUri, @Nullable String priorityLabel) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new PriorityMappingTableTypeDeletePriorityMappingEntry.Inputs(
                      mappingUri, priorityLabel)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getDeletePriorityMappingEntryMethodNodeAsync(),
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
