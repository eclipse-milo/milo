package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.SignedSoftwareCertificate;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ServerCapabilitiesType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.2">Model
 *     documentation</a>
 */
public class ServerCapabilitiesTypeNode extends BaseObjectTypeNode
    implements ServerCapabilitiesType {
  public ServerCapabilitiesTypeNode(
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
  public @Nullable PropertyTypeNode getMaxSessionsNode() throws UaException {
    return ClientNodeSupport.await(getMaxSessionsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxSessionsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxSessions",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxSessions() throws UaException {
    return ClientNodeSupport.await(readMaxSessionsAsync());
  }

  @Override
  public void writeMaxSessions(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxSessionsAsync(value)),
        "http://opcfoundation.org/UA/}MaxSessions");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxSessionsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxSessionsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxSessions",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxSessionsAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxSessionsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxSessions",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getLocaleIdArrayNode() throws UaException {
    return ClientNodeSupport.await(getLocaleIdArrayNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLocaleIdArrayNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LocaleIdArray",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String @Nullable [] readLocaleIdArray() throws UaException {
    return ClientNodeSupport.await(readLocaleIdArrayAsync());
  }

  @Override
  public void writeLocaleIdArray(@Nullable String @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeLocaleIdArrayAsync(value)),
        "http://opcfoundation.org/UA/}LocaleIdArray");
  }

  @Override
  public CompletableFuture<? extends @Nullable String @Nullable []> readLocaleIdArrayAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getLocaleIdArrayNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}LocaleIdArray",
                            true,
                            String.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeLocaleIdArrayAsync(
      @Nullable String @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getLocaleIdArrayNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}LocaleIdArray",
                        value,
                        String.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxArrayLengthNode() throws UaException {
    return ClientNodeSupport.await(getMaxArrayLengthNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxArrayLengthNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxArrayLength",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxArrayLength() throws UaException {
    return ClientNodeSupport.await(readMaxArrayLengthAsync());
  }

  @Override
  public void writeMaxArrayLength(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxArrayLengthAsync(value)),
        "http://opcfoundation.org/UA/}MaxArrayLength");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxArrayLengthAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxArrayLengthNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxArrayLength",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxArrayLengthAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxArrayLengthNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxArrayLength",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public FolderTypeNode getModellingRulesNode() throws UaException {
    return ClientNodeSupport.await(getModellingRulesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends FolderTypeNode> getModellingRulesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ModellingRules",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        FolderTypeNode.class)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxStringLengthNode() throws UaException {
    return ClientNodeSupport.await(getMaxStringLengthNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxStringLengthNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxStringLength",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxStringLength() throws UaException {
    return ClientNodeSupport.await(readMaxStringLengthAsync());
  }

  @Override
  public void writeMaxStringLength(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxStringLengthAsync(value)),
        "http://opcfoundation.org/UA/}MaxStringLength");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxStringLengthAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxStringLengthNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxStringLength",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxStringLengthAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxStringLengthNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxStringLength",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable OperationLimitsTypeNode getOperationLimitsNode() throws UaException {
    return ClientNodeSupport.await(getOperationLimitsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable OperationLimitsTypeNode>
      getOperationLimitsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "OperationLimits",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        OperationLimitsTypeNode.class)));
  }

  @Override
  public @Nullable PropertyTypeNode getConformanceUnitsNode() throws UaException {
    return ClientNodeSupport.await(getConformanceUnitsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getConformanceUnitsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ConformanceUnits",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public QualifiedName @Nullable [] readConformanceUnits() throws UaException {
    return ClientNodeSupport.await(readConformanceUnitsAsync());
  }

  @Override
  public void writeConformanceUnits(QualifiedName @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeConformanceUnitsAsync(value)),
        "http://opcfoundation.org/UA/}ConformanceUnits");
  }

  @Override
  public CompletableFuture<? extends QualifiedName @Nullable []> readConformanceUnitsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getConformanceUnitsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ConformanceUnits",
                            false,
                            QualifiedName.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((QualifiedName @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeConformanceUnitsAsync(
      QualifiedName @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getConformanceUnitsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ConformanceUnits",
                        value,
                        QualifiedName.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxSubscriptionsNode() throws UaException {
    return ClientNodeSupport.await(getMaxSubscriptionsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxSubscriptionsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxSubscriptions",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxSubscriptions() throws UaException {
    return ClientNodeSupport.await(readMaxSubscriptionsAsync());
  }

  @Override
  public void writeMaxSubscriptions(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxSubscriptionsAsync(value)),
        "http://opcfoundation.org/UA/}MaxSubscriptions");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxSubscriptionsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxSubscriptionsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxSubscriptions",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxSubscriptionsAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxSubscriptionsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxSubscriptions",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxMonitoredItemsNode() throws UaException {
    return ClientNodeSupport.await(getMaxMonitoredItemsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxMonitoredItemsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxMonitoredItems",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxMonitoredItems() throws UaException {
    return ClientNodeSupport.await(readMaxMonitoredItemsAsync());
  }

  @Override
  public void writeMaxMonitoredItems(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxMonitoredItemsAsync(value)),
        "http://opcfoundation.org/UA/}MaxMonitoredItems");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxMonitoredItemsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxMonitoredItemsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxMonitoredItems",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxMonitoredItemsAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxMonitoredItemsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxMonitoredItems",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public FolderTypeNode getAggregateFunctionsNode() throws UaException {
    return ClientNodeSupport.await(getAggregateFunctionsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends FolderTypeNode> getAggregateFunctionsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "AggregateFunctions",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        FolderTypeNode.class)));
  }

  @Override
  public PropertyTypeNode getServerProfileArrayNode() throws UaException {
    return ClientNodeSupport.await(getServerProfileArrayNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getServerProfileArrayNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ServerProfileArray",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String @Nullable [] readServerProfileArray() throws UaException {
    return ClientNodeSupport.await(readServerProfileArrayAsync());
  }

  @Override
  public void writeServerProfileArray(@Nullable String @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeServerProfileArrayAsync(value)),
        "http://opcfoundation.org/UA/}ServerProfileArray");
  }

  @Override
  public CompletableFuture<? extends @Nullable String @Nullable []> readServerProfileArrayAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getServerProfileArrayNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ServerProfileArray",
                            true,
                            String.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeServerProfileArrayAsync(
      @Nullable String @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getServerProfileArrayNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ServerProfileArray",
                        value,
                        String.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxByteStringLengthNode() throws UaException {
    return ClientNodeSupport.await(getMaxByteStringLengthNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxByteStringLengthNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxByteStringLength",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxByteStringLength() throws UaException {
    return ClientNodeSupport.await(readMaxByteStringLengthAsync());
  }

  @Override
  public void writeMaxByteStringLength(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxByteStringLengthAsync(value)),
        "http://opcfoundation.org/UA/}MaxByteStringLength");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxByteStringLengthAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxByteStringLengthNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxByteStringLength",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxByteStringLengthAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxByteStringLengthNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxByteStringLength",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getSoftwareCertificatesNode() throws UaException {
    return ClientNodeSupport.await(getSoftwareCertificatesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSoftwareCertificatesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SoftwareCertificates",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable SignedSoftwareCertificate @Nullable [] readSoftwareCertificates()
      throws UaException {
    return ClientNodeSupport.await(readSoftwareCertificatesAsync());
  }

  @Override
  public void writeSoftwareCertificates(@Nullable SignedSoftwareCertificate @Nullable [] value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSoftwareCertificatesAsync(value)),
        "http://opcfoundation.org/UA/}SoftwareCertificates");
  }

  @Override
  public CompletableFuture<? extends @Nullable SignedSoftwareCertificate @Nullable []>
      readSoftwareCertificatesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSoftwareCertificatesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SoftwareCertificates",
                            true,
                            SignedSoftwareCertificate.class,
                            1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable SignedSoftwareCertificate @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSoftwareCertificatesAsync(
      @Nullable SignedSoftwareCertificate @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSoftwareCertificatesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SoftwareCertificates",
                        value,
                        SignedSoftwareCertificate.class,
                        1,
                        null)));
  }

  @Override
  public PropertyTypeNode getMinSupportedSampleRateNode() throws UaException {
    return ClientNodeSupport.await(getMinSupportedSampleRateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMinSupportedSampleRateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MinSupportedSampleRate",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Double readMinSupportedSampleRate() throws UaException {
    return ClientNodeSupport.await(readMinSupportedSampleRateAsync());
  }

  @Override
  public void writeMinSupportedSampleRate(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMinSupportedSampleRateAsync(value)),
        "http://opcfoundation.org/UA/}MinSupportedSampleRate");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readMinSupportedSampleRateAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMinSupportedSampleRateNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MinSupportedSampleRate",
                            true,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMinSupportedSampleRateAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMinSupportedSampleRateNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MinSupportedSampleRate",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxWhereClauseParametersNode() throws UaException {
    return ClientNodeSupport.await(getMaxWhereClauseParametersNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxWhereClauseParametersNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxWhereClauseParameters",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxWhereClauseParameters() throws UaException {
    return ClientNodeSupport.await(readMaxWhereClauseParametersAsync());
  }

  @Override
  public void writeMaxWhereClauseParameters(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxWhereClauseParametersAsync(value)),
        "http://opcfoundation.org/UA/}MaxWhereClauseParameters");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxWhereClauseParametersAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxWhereClauseParametersNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxWhereClauseParameters",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxWhereClauseParametersAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxWhereClauseParametersNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxWhereClauseParameters",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxSelectClauseParametersNode() throws UaException {
    return ClientNodeSupport.await(getMaxSelectClauseParametersNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxSelectClauseParametersNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxSelectClauseParameters",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxSelectClauseParameters() throws UaException {
    return ClientNodeSupport.await(readMaxSelectClauseParametersAsync());
  }

  @Override
  public void writeMaxSelectClauseParameters(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxSelectClauseParametersAsync(value)),
        "http://opcfoundation.org/UA/}MaxSelectClauseParameters");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxSelectClauseParametersAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxSelectClauseParametersNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxSelectClauseParameters",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxSelectClauseParametersAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxSelectClauseParametersNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxSelectClauseParameters",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxMonitoredItemsQueueSizeNode() throws UaException {
    return ClientNodeSupport.await(getMaxMonitoredItemsQueueSizeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxMonitoredItemsQueueSizeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxMonitoredItemsQueueSize",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxMonitoredItemsQueueSize() throws UaException {
    return ClientNodeSupport.await(readMaxMonitoredItemsQueueSizeAsync());
  }

  @Override
  public void writeMaxMonitoredItemsQueueSize(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxMonitoredItemsQueueSizeAsync(value)),
        "http://opcfoundation.org/UA/}MaxMonitoredItemsQueueSize");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxMonitoredItemsQueueSizeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxMonitoredItemsQueueSizeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxMonitoredItemsQueueSize",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxMonitoredItemsQueueSizeAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxMonitoredItemsQueueSizeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxMonitoredItemsQueueSize",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getMaxQueryContinuationPointsNode() throws UaException {
    return ClientNodeSupport.await(getMaxQueryContinuationPointsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxQueryContinuationPointsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxQueryContinuationPoints",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UShort readMaxQueryContinuationPoints() throws UaException {
    return ClientNodeSupport.await(readMaxQueryContinuationPointsAsync());
  }

  @Override
  public void writeMaxQueryContinuationPoints(@Nullable UShort value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxQueryContinuationPointsAsync(value)),
        "http://opcfoundation.org/UA/}MaxQueryContinuationPoints");
  }

  @Override
  public CompletableFuture<? extends @Nullable UShort> readMaxQueryContinuationPointsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxQueryContinuationPointsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxQueryContinuationPoints",
                            true,
                            UShort.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UShort) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxQueryContinuationPointsAsync(
      @Nullable UShort value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxQueryContinuationPointsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxQueryContinuationPoints",
                        value,
                        UShort.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxSubscriptionsPerSessionNode() throws UaException {
    return ClientNodeSupport.await(getMaxSubscriptionsPerSessionNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxSubscriptionsPerSessionNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxSubscriptionsPerSession",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxSubscriptionsPerSession() throws UaException {
    return ClientNodeSupport.await(readMaxSubscriptionsPerSessionAsync());
  }

  @Override
  public void writeMaxSubscriptionsPerSession(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxSubscriptionsPerSessionAsync(value)),
        "http://opcfoundation.org/UA/}MaxSubscriptionsPerSession");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxSubscriptionsPerSessionAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxSubscriptionsPerSessionNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxSubscriptionsPerSession",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxSubscriptionsPerSessionAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxSubscriptionsPerSessionNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxSubscriptionsPerSession",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getMaxBrowseContinuationPointsNode() throws UaException {
    return ClientNodeSupport.await(getMaxBrowseContinuationPointsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxBrowseContinuationPointsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxBrowseContinuationPoints",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UShort readMaxBrowseContinuationPoints() throws UaException {
    return ClientNodeSupport.await(readMaxBrowseContinuationPointsAsync());
  }

  @Override
  public void writeMaxBrowseContinuationPoints(@Nullable UShort value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxBrowseContinuationPointsAsync(value)),
        "http://opcfoundation.org/UA/}MaxBrowseContinuationPoints");
  }

  @Override
  public CompletableFuture<? extends @Nullable UShort> readMaxBrowseContinuationPointsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxBrowseContinuationPointsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxBrowseContinuationPoints",
                            true,
                            UShort.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UShort) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxBrowseContinuationPointsAsync(
      @Nullable UShort value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxBrowseContinuationPointsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxBrowseContinuationPoints",
                        value,
                        UShort.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getMaxHistoryContinuationPointsNode() throws UaException {
    return ClientNodeSupport.await(getMaxHistoryContinuationPointsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxHistoryContinuationPointsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxHistoryContinuationPoints",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UShort readMaxHistoryContinuationPoints() throws UaException {
    return ClientNodeSupport.await(readMaxHistoryContinuationPointsAsync());
  }

  @Override
  public void writeMaxHistoryContinuationPoints(@Nullable UShort value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxHistoryContinuationPointsAsync(value)),
        "http://opcfoundation.org/UA/}MaxHistoryContinuationPoints");
  }

  @Override
  public CompletableFuture<? extends @Nullable UShort> readMaxHistoryContinuationPointsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxHistoryContinuationPointsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxHistoryContinuationPoints",
                            true,
                            UShort.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UShort) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxHistoryContinuationPointsAsync(
      @Nullable UShort value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxHistoryContinuationPointsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxHistoryContinuationPoints",
                        value,
                        UShort.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxLogObjectContinuationPointsNode() throws UaException {
    return ClientNodeSupport.await(getMaxLogObjectContinuationPointsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxLogObjectContinuationPointsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxLogObjectContinuationPoints",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UShort readMaxLogObjectContinuationPoints() throws UaException {
    return ClientNodeSupport.await(readMaxLogObjectContinuationPointsAsync());
  }

  @Override
  public void writeMaxLogObjectContinuationPoints(@Nullable UShort value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxLogObjectContinuationPointsAsync(value)),
        "http://opcfoundation.org/UA/}MaxLogObjectContinuationPoints");
  }

  @Override
  public CompletableFuture<? extends @Nullable UShort> readMaxLogObjectContinuationPointsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxLogObjectContinuationPointsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxLogObjectContinuationPoints",
                            false,
                            UShort.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UShort) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxLogObjectContinuationPointsAsync(
      @Nullable UShort value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxLogObjectContinuationPointsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxLogObjectContinuationPoints",
                        value,
                        UShort.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxMonitoredItemsPerSubscriptionNode() throws UaException {
    return ClientNodeSupport.await(getMaxMonitoredItemsPerSubscriptionNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxMonitoredItemsPerSubscriptionNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxMonitoredItemsPerSubscription",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxMonitoredItemsPerSubscription() throws UaException {
    return ClientNodeSupport.await(readMaxMonitoredItemsPerSubscriptionAsync());
  }

  @Override
  public void writeMaxMonitoredItemsPerSubscription(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxMonitoredItemsPerSubscriptionAsync(value)),
        "http://opcfoundation.org/UA/}MaxMonitoredItemsPerSubscription");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger>
      readMaxMonitoredItemsPerSubscriptionAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxMonitoredItemsPerSubscriptionNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxMonitoredItemsPerSubscription",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxMonitoredItemsPerSubscriptionAsync(
      @Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxMonitoredItemsPerSubscriptionNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxMonitoredItemsPerSubscription",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable RoleSetTypeNode getRoleSetNode() throws UaException {
    return ClientNodeSupport.await(getRoleSetNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable RoleSetTypeNode> getRoleSetNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "RoleSet",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        RoleSetTypeNode.class)));
  }
}
