package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.LogObjectTypeGetRecords;
import org.eclipse.milo.opcua.sdk.core.model.methods.LogObjectTypeReleaseContinuationPoint;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.LogRecordMask;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link LogObjectType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part26/5.2">Model
 *     documentation</a>
 */
public class LogObjectTypeNode extends BaseObjectTypeNode implements LogObjectType {
  public LogObjectTypeNode(
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
  public @Nullable PropertyTypeNode getMaxRecordsNode() throws UaException {
    return ClientNodeSupport.await(getMaxRecordsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxRecordsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxRecords",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxRecords() throws UaException {
    return ClientNodeSupport.await(readMaxRecordsAsync());
  }

  @Override
  public void writeMaxRecords(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxRecordsAsync(value)),
        "http://opcfoundation.org/UA/}MaxRecords");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxRecordsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxRecordsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxRecords",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxRecordsAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxRecordsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxRecords",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMinimumSeverityNode() throws UaException {
    return ClientNodeSupport.await(getMinimumSeverityNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMinimumSeverityNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MinimumSeverity",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UShort readMinimumSeverity() throws UaException {
    return ClientNodeSupport.await(readMinimumSeverityAsync());
  }

  @Override
  public void writeMinimumSeverity(@Nullable UShort value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMinimumSeverityAsync(value)),
        "http://opcfoundation.org/UA/}MinimumSeverity");
  }

  @Override
  public CompletableFuture<? extends @Nullable UShort> readMinimumSeverityAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMinimumSeverityNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MinimumSeverity",
                            false,
                            UShort.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UShort) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMinimumSeverityAsync(@Nullable UShort value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMinimumSeverityNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MinimumSeverity",
                        value,
                        UShort.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxStorageDurationNode() throws UaException {
    return ClientNodeSupport.await(getMaxStorageDurationNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxStorageDurationNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxStorageDuration",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Double readMaxStorageDuration() throws UaException {
    return ClientNodeSupport.await(readMaxStorageDurationAsync());
  }

  @Override
  public void writeMaxStorageDuration(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxStorageDurationAsync(value)),
        "http://opcfoundation.org/UA/}MaxStorageDuration");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readMaxStorageDurationAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxStorageDurationNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxStorageDuration",
                            false,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxStorageDurationAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxStorageDurationNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxStorageDuration",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public UaMethodNode getGetRecordsMethodNode() throws UaException {
    return ClientNodeSupport.await(getGetRecordsMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getGetRecordsMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "GetRecords",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public LogObjectTypeGetRecords.Outputs getRecords(
      @Nullable DateTime startTime,
      @Nullable DateTime endTime,
      @Nullable UInteger maxReturnRecords,
      @Nullable UShort minimumSeverity,
      @Nullable LogRecordMask requestMask,
      @Nullable ByteString continuationPointIn)
      throws UaException {
    return ClientNodeSupport.await(
        getRecordsAsync(
            startTime,
            endTime,
            maxReturnRecords,
            minimumSeverity,
            requestMask,
            continuationPointIn));
  }

  @Override
  public MethodCallResult<LogObjectTypeGetRecords.Outputs> callGetRecords(
      @Nullable DateTime startTime,
      @Nullable DateTime endTime,
      @Nullable UInteger maxReturnRecords,
      @Nullable UShort minimumSeverity,
      @Nullable LogRecordMask requestMask,
      @Nullable ByteString continuationPointIn)
      throws UaException {
    return ClientNodeSupport.await(
        callGetRecordsAsync(
            startTime,
            endTime,
            maxReturnRecords,
            minimumSeverity,
            requestMask,
            continuationPointIn));
  }

  @Override
  public MethodCallResult<LogObjectTypeGetRecords.Outputs> callGetRecordsWith(
      MethodCallOptions options,
      @Nullable DateTime startTime,
      @Nullable DateTime endTime,
      @Nullable UInteger maxReturnRecords,
      @Nullable UShort minimumSeverity,
      @Nullable LogRecordMask requestMask,
      @Nullable ByteString continuationPointIn)
      throws UaException {
    return ClientNodeSupport.await(
        callGetRecordsWithAsync(
            options,
            startTime,
            endTime,
            maxReturnRecords,
            minimumSeverity,
            requestMask,
            continuationPointIn));
  }

  @Override
  public CompletableFuture<LogObjectTypeGetRecords.Outputs> getRecordsAsync(
      @Nullable DateTime startTime,
      @Nullable DateTime endTime,
      @Nullable UInteger maxReturnRecords,
      @Nullable UShort minimumSeverity,
      @Nullable LogRecordMask requestMask,
      @Nullable ByteString continuationPointIn) {
    return ClientNodeSupport.compose(
        callGetRecordsAsync(
            startTime,
            endTime,
            maxReturnRecords,
            minimumSeverity,
            requestMask,
            continuationPointIn),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<LogObjectTypeGetRecords.Outputs>> callGetRecordsAsync(
      @Nullable DateTime startTime,
      @Nullable DateTime endTime,
      @Nullable UInteger maxReturnRecords,
      @Nullable UShort minimumSeverity,
      @Nullable LogRecordMask requestMask,
      @Nullable ByteString continuationPointIn) {
    return callGetRecordsWithAsync(
        MethodCallOptions.DEFAULT,
        startTime,
        endTime,
        maxReturnRecords,
        minimumSeverity,
        requestMask,
        continuationPointIn);
  }

  @Override
  public CompletableFuture<MethodCallResult<LogObjectTypeGetRecords.Outputs>>
      callGetRecordsWithAsync(
          MethodCallOptions options,
          @Nullable DateTime startTime,
          @Nullable DateTime endTime,
          @Nullable UInteger maxReturnRecords,
          @Nullable UShort minimumSeverity,
          @Nullable LogRecordMask requestMask,
          @Nullable ByteString continuationPointIn) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new LogObjectTypeGetRecords.Inputs(
                      startTime,
                      endTime,
                      maxReturnRecords,
                      minimumSeverity,
                      requestMask,
                      continuationPointIn)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getGetRecordsMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return LogObjectTypeGetRecords.Outputs.fromVariants(
                                        client.getStaticEncodingContext(), values);
                                  }))));
        });
  }

  @Override
  public @Nullable UaMethodNode getReleaseContinuationPointMethodNode() throws UaException {
    return ClientNodeSupport.await(getReleaseContinuationPointMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getReleaseContinuationPointMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "ReleaseContinuationPoint",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void releaseContinuationPoint(@Nullable ByteString continuationPointIn)
      throws UaException {
    ClientNodeSupport.await(releaseContinuationPointAsync(continuationPointIn));
  }

  @Override
  public MethodCallResult<Void> callReleaseContinuationPoint(
      @Nullable ByteString continuationPointIn) throws UaException {
    return ClientNodeSupport.await(callReleaseContinuationPointAsync(continuationPointIn));
  }

  @Override
  public MethodCallResult<Void> callReleaseContinuationPointWith(
      MethodCallOptions options, @Nullable ByteString continuationPointIn) throws UaException {
    return ClientNodeSupport.await(
        callReleaseContinuationPointWithAsync(options, continuationPointIn));
  }

  @Override
  public CompletableFuture<Void> releaseContinuationPointAsync(
      @Nullable ByteString continuationPointIn) {
    return ClientNodeSupport.compose(
        callReleaseContinuationPointAsync(continuationPointIn),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callReleaseContinuationPointAsync(
      @Nullable ByteString continuationPointIn) {
    return callReleaseContinuationPointWithAsync(MethodCallOptions.DEFAULT, continuationPointIn);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callReleaseContinuationPointWithAsync(
      MethodCallOptions options, @Nullable ByteString continuationPointIn) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new LogObjectTypeReleaseContinuationPoint.Inputs(continuationPointIn)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getReleaseContinuationPointMethodNodeAsync(),
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
