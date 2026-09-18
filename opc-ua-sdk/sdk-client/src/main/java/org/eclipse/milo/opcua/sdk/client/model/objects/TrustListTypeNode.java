package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.TrustListTypeAddCertificate;
import org.eclipse.milo.opcua.sdk.core.model.methods.TrustListTypeCloseAndUpdate;
import org.eclipse.milo.opcua.sdk.core.model.methods.TrustListTypeOpenWithMasks;
import org.eclipse.milo.opcua.sdk.core.model.methods.TrustListTypeRemoveCertificate;
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.TrustListValidationOptions;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link TrustListType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.1">Model
 *     documentation</a>
 */
public class TrustListTypeNode extends FileTypeNode implements TrustListType {
  public TrustListTypeNode(
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
  public PropertyTypeNode getLastUpdateTimeNode() throws UaException {
    return ClientNodeSupport.await(getLastUpdateTimeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLastUpdateTimeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LastUpdateTime",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable DateTime readLastUpdateTime() throws UaException {
    return ClientNodeSupport.await(readLastUpdateTimeAsync());
  }

  @Override
  public void writeLastUpdateTime(@Nullable DateTime value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeLastUpdateTimeAsync(value)),
        "http://opcfoundation.org/UA/}LastUpdateTime");
  }

  @Override
  public CompletableFuture<? extends @Nullable DateTime> readLastUpdateTimeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getLastUpdateTimeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}LastUpdateTime",
                            true,
                            DateTime.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable DateTime) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeLastUpdateTimeAsync(@Nullable DateTime value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getLastUpdateTimeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}LastUpdateTime",
                        value,
                        DateTime.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getActivityTimeoutNode() throws UaException {
    return ClientNodeSupport.await(getActivityTimeoutNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getActivityTimeoutNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ActivityTimeout",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Double readActivityTimeout() throws UaException {
    return ClientNodeSupport.await(readActivityTimeoutAsync());
  }

  @Override
  public void writeActivityTimeout(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeActivityTimeoutAsync(value)),
        "http://opcfoundation.org/UA/}ActivityTimeout");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readActivityTimeoutAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getActivityTimeoutNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ActivityTimeout",
                            false,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeActivityTimeoutAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getActivityTimeoutNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ActivityTimeout",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getUpdateFrequencyNode() throws UaException {
    return ClientNodeSupport.await(getUpdateFrequencyNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getUpdateFrequencyNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "UpdateFrequency",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Double readUpdateFrequency() throws UaException {
    return ClientNodeSupport.await(readUpdateFrequencyAsync());
  }

  @Override
  public void writeUpdateFrequency(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeUpdateFrequencyAsync(value)),
        "http://opcfoundation.org/UA/}UpdateFrequency");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readUpdateFrequencyAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getUpdateFrequencyNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}UpdateFrequency",
                            false,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeUpdateFrequencyAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getUpdateFrequencyNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}UpdateFrequency",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getDefaultValidationOptionsNode() throws UaException {
    return ClientNodeSupport.await(getDefaultValidationOptionsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getDefaultValidationOptionsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DefaultValidationOptions",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable TrustListValidationOptions readDefaultValidationOptions() throws UaException {
    return ClientNodeSupport.await(readDefaultValidationOptionsAsync());
  }

  @Override
  public void writeDefaultValidationOptions(@Nullable TrustListValidationOptions value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDefaultValidationOptionsAsync(value)),
        "http://opcfoundation.org/UA/}DefaultValidationOptions");
  }

  @Override
  public CompletableFuture<? extends @Nullable TrustListValidationOptions>
      readDefaultValidationOptionsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDefaultValidationOptionsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DefaultValidationOptions",
                            false,
                            TrustListValidationOptions.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable TrustListValidationOptions) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDefaultValidationOptionsAsync(
      @Nullable TrustListValidationOptions value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDefaultValidationOptionsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DefaultValidationOptions",
                        value,
                        TrustListValidationOptions.class,
                        -1,
                        null)));
  }

  @Override
  public UaMethodNode getAddCertificateMethodNode() throws UaException {
    return ClientNodeSupport.await(getAddCertificateMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getAddCertificateMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "AddCertificate",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void addCertificate(
      @Nullable ByteString certificate, @Nullable Boolean isTrustedCertificate) throws UaException {
    ClientNodeSupport.await(addCertificateAsync(certificate, isTrustedCertificate));
  }

  @Override
  public MethodCallResult<Void> callAddCertificate(
      @Nullable ByteString certificate, @Nullable Boolean isTrustedCertificate) throws UaException {
    return ClientNodeSupport.await(callAddCertificateAsync(certificate, isTrustedCertificate));
  }

  @Override
  public MethodCallResult<Void> callAddCertificateWith(
      MethodCallOptions options,
      @Nullable ByteString certificate,
      @Nullable Boolean isTrustedCertificate)
      throws UaException {
    return ClientNodeSupport.await(
        callAddCertificateWithAsync(options, certificate, isTrustedCertificate));
  }

  @Override
  public CompletableFuture<Void> addCertificateAsync(
      @Nullable ByteString certificate, @Nullable Boolean isTrustedCertificate) {
    return ClientNodeSupport.compose(
        callAddCertificateAsync(certificate, isTrustedCertificate),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callAddCertificateAsync(
      @Nullable ByteString certificate, @Nullable Boolean isTrustedCertificate) {
    return callAddCertificateWithAsync(
        MethodCallOptions.DEFAULT, certificate, isTrustedCertificate);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callAddCertificateWithAsync(
      MethodCallOptions options,
      @Nullable ByteString certificate,
      @Nullable Boolean isTrustedCertificate) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new TrustListTypeAddCertificate.Inputs(certificate, isTrustedCertificate)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getAddCertificateMethodNodeAsync(),
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
  public UaMethodNode getCloseAndUpdateMethodNode() throws UaException {
    return ClientNodeSupport.await(getCloseAndUpdateMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getCloseAndUpdateMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "CloseAndUpdate",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable Boolean closeAndUpdate(@Nullable UInteger fileHandle) throws UaException {
    return ClientNodeSupport.await(closeAndUpdateAsync(fileHandle));
  }

  @Override
  public MethodCallResult<@Nullable Boolean> callCloseAndUpdate(@Nullable UInteger fileHandle)
      throws UaException {
    return ClientNodeSupport.await(callCloseAndUpdateAsync(fileHandle));
  }

  @Override
  public MethodCallResult<@Nullable Boolean> callCloseAndUpdateWith(
      MethodCallOptions options, @Nullable UInteger fileHandle) throws UaException {
    return ClientNodeSupport.await(callCloseAndUpdateWithAsync(options, fileHandle));
  }

  @Override
  public CompletableFuture<@Nullable Boolean> closeAndUpdateAsync(@Nullable UInteger fileHandle) {
    return ClientNodeSupport.compose(
        callCloseAndUpdateAsync(fileHandle),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable Boolean>> callCloseAndUpdateAsync(
      @Nullable UInteger fileHandle) {
    return callCloseAndUpdateWithAsync(MethodCallOptions.DEFAULT, fileHandle);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable Boolean>> callCloseAndUpdateWithAsync(
      MethodCallOptions options, @Nullable UInteger fileHandle) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new TrustListTypeCloseAndUpdate.Inputs(fileHandle)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getCloseAndUpdateMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return TrustListTypeCloseAndUpdate.Outputs.fromVariants(
                                            client.getStaticEncodingContext(), values)
                                        .applyChangesRequired();
                                  }))));
        });
  }

  @Override
  public UaMethodNode getOpenWithMasksMethodNode() throws UaException {
    return ClientNodeSupport.await(getOpenWithMasksMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getOpenWithMasksMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "OpenWithMasks",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable UInteger openWithMasks(@Nullable UInteger masks) throws UaException {
    return ClientNodeSupport.await(openWithMasksAsync(masks));
  }

  @Override
  public MethodCallResult<@Nullable UInteger> callOpenWithMasks(@Nullable UInteger masks)
      throws UaException {
    return ClientNodeSupport.await(callOpenWithMasksAsync(masks));
  }

  @Override
  public MethodCallResult<@Nullable UInteger> callOpenWithMasksWith(
      MethodCallOptions options, @Nullable UInteger masks) throws UaException {
    return ClientNodeSupport.await(callOpenWithMasksWithAsync(options, masks));
  }

  @Override
  public CompletableFuture<@Nullable UInteger> openWithMasksAsync(@Nullable UInteger masks) {
    return ClientNodeSupport.compose(
        callOpenWithMasksAsync(masks),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable UInteger>> callOpenWithMasksAsync(
      @Nullable UInteger masks) {
    return callOpenWithMasksWithAsync(MethodCallOptions.DEFAULT, masks);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable UInteger>> callOpenWithMasksWithAsync(
      MethodCallOptions options, @Nullable UInteger masks) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new TrustListTypeOpenWithMasks.Inputs(masks)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getOpenWithMasksMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return TrustListTypeOpenWithMasks.Outputs.fromVariants(
                                            client.getStaticEncodingContext(), values)
                                        .fileHandle();
                                  }))));
        });
  }

  @Override
  public UaMethodNode getRemoveCertificateMethodNode() throws UaException {
    return ClientNodeSupport.await(getRemoveCertificateMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getRemoveCertificateMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "RemoveCertificate",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void removeCertificate(@Nullable String thumbprint, @Nullable Boolean isTrustedCertificate)
      throws UaException {
    ClientNodeSupport.await(removeCertificateAsync(thumbprint, isTrustedCertificate));
  }

  @Override
  public MethodCallResult<Void> callRemoveCertificate(
      @Nullable String thumbprint, @Nullable Boolean isTrustedCertificate) throws UaException {
    return ClientNodeSupport.await(callRemoveCertificateAsync(thumbprint, isTrustedCertificate));
  }

  @Override
  public MethodCallResult<Void> callRemoveCertificateWith(
      MethodCallOptions options,
      @Nullable String thumbprint,
      @Nullable Boolean isTrustedCertificate)
      throws UaException {
    return ClientNodeSupport.await(
        callRemoveCertificateWithAsync(options, thumbprint, isTrustedCertificate));
  }

  @Override
  public CompletableFuture<Void> removeCertificateAsync(
      @Nullable String thumbprint, @Nullable Boolean isTrustedCertificate) {
    return ClientNodeSupport.compose(
        callRemoveCertificateAsync(thumbprint, isTrustedCertificate),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveCertificateAsync(
      @Nullable String thumbprint, @Nullable Boolean isTrustedCertificate) {
    return callRemoveCertificateWithAsync(
        MethodCallOptions.DEFAULT, thumbprint, isTrustedCertificate);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveCertificateWithAsync(
      MethodCallOptions options,
      @Nullable String thumbprint,
      @Nullable Boolean isTrustedCertificate) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new TrustListTypeRemoveCertificate.Inputs(thumbprint, isTrustedCertificate)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getRemoveCertificateMethodNodeAsync(),
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
