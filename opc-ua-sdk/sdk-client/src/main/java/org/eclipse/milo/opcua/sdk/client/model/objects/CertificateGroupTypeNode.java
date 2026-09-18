package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.CertificateGroupTypeGetRejectedList;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
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
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link CertificateGroupType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.1">Model
 *     documentation</a>
 */
public class CertificateGroupTypeNode extends BaseObjectTypeNode implements CertificateGroupType {
  public CertificateGroupTypeNode(
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
  public PropertyTypeNode getCertificateTypesNode() throws UaException {
    return ClientNodeSupport.await(getCertificateTypesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getCertificateTypesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CertificateTypes",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public NodeId @Nullable [] readCertificateTypes() throws UaException {
    return ClientNodeSupport.await(readCertificateTypesAsync());
  }

  @Override
  public void writeCertificateTypes(NodeId @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeCertificateTypesAsync(value)),
        "http://opcfoundation.org/UA/}CertificateTypes");
  }

  @Override
  public CompletableFuture<? extends NodeId @Nullable []> readCertificateTypesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getCertificateTypesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}CertificateTypes",
                            true,
                            NodeId.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((NodeId @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeCertificateTypesAsync(NodeId @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getCertificateTypesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}CertificateTypes",
                        value,
                        NodeId.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable CertificateExpirationAlarmTypeNode getCertificateExpiredNode()
      throws UaException {
    return ClientNodeSupport.await(getCertificateExpiredNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable CertificateExpirationAlarmTypeNode>
      getCertificateExpiredNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CertificateExpired",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        CertificateExpirationAlarmTypeNode.class)));
  }

  @Override
  public @Nullable TrustListOutOfDateAlarmTypeNode getTrustListOutOfDateNode() throws UaException {
    return ClientNodeSupport.await(getTrustListOutOfDateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable TrustListOutOfDateAlarmTypeNode>
      getTrustListOutOfDateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "TrustListOutOfDate",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        TrustListOutOfDateAlarmTypeNode.class)));
  }

  @Override
  public @Nullable PropertyTypeNode getPurposeNode() throws UaException {
    return ClientNodeSupport.await(getPurposeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getPurposeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Purpose",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable NodeId readPurpose() throws UaException {
    return ClientNodeSupport.await(readPurposeAsync());
  }

  @Override
  public void writePurpose(@Nullable NodeId value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writePurposeAsync(value)), "http://opcfoundation.org/UA/}Purpose");
  }

  @Override
  public CompletableFuture<? extends @Nullable NodeId> readPurposeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getPurposeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Purpose",
                            false,
                            NodeId.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable NodeId) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writePurposeAsync(@Nullable NodeId value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getPurposeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Purpose",
                        value,
                        NodeId.class,
                        -1,
                        null)));
  }

  @Override
  public TrustListTypeNode getTrustListNode() throws UaException {
    return ClientNodeSupport.await(getTrustListNodeAsync());
  }

  @Override
  public CompletableFuture<? extends TrustListTypeNode> getTrustListNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "TrustList",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        TrustListTypeNode.class)));
  }

  @Override
  public @Nullable UaMethodNode getGetRejectedListMethodNode() throws UaException {
    return ClientNodeSupport.await(getGetRejectedListMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getGetRejectedListMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "GetRejectedList",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public ByteString @Nullable [] getRejectedList() throws UaException {
    return ClientNodeSupport.await(getRejectedListAsync());
  }

  @Override
  public MethodCallResult<ByteString @Nullable []> callGetRejectedList() throws UaException {
    return ClientNodeSupport.await(callGetRejectedListAsync());
  }

  @Override
  public MethodCallResult<ByteString @Nullable []> callGetRejectedListWith(
      MethodCallOptions options) throws UaException {
    return ClientNodeSupport.await(callGetRejectedListWithAsync(options));
  }

  @Override
  public CompletableFuture<ByteString @Nullable []> getRejectedListAsync() {
    return ClientNodeSupport.compose(
        callGetRejectedListAsync(),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<ByteString @Nullable []>> callGetRejectedListAsync() {
    return callGetRejectedListWithAsync(MethodCallOptions.DEFAULT);
  }

  @Override
  public CompletableFuture<MethodCallResult<ByteString @Nullable []>> callGetRejectedListWithAsync(
      MethodCallOptions options) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs = new Variant[0];
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getGetRejectedListMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return CertificateGroupTypeGetRejectedList.Outputs.fromVariants(
                                            client.getStaticEncodingContext(), values)
                                        .certificates();
                                  }))));
        });
  }
}
