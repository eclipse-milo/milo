package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerConfigurationTypeCreateSelfSignedCertificate;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerConfigurationTypeCreateSigningRequest;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerConfigurationTypeDeleteCertificate;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerConfigurationTypeGetCertificates;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerConfigurationTypeGetRejectedList;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerConfigurationTypeUpdateCertificate;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ServerConfigurationType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.3">Model
 *     documentation</a>
 */
public class ServerConfigurationTypeNode extends BaseObjectTypeNode
    implements ServerConfigurationType {
  public ServerConfigurationTypeNode(
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
  public @Nullable PropertyTypeNode getProductUriNode() throws UaException {
    return ClientNodeSupport.await(getProductUriNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getProductUriNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ProductUri",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readProductUri() throws UaException {
    return ClientNodeSupport.await(readProductUriAsync());
  }

  @Override
  public void writeProductUri(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeProductUriAsync(value)),
        "http://opcfoundation.org/UA/}ProductUri");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readProductUriAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getProductUriNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ProductUri",
                            false,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeProductUriAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getProductUriNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ProductUri",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getApplicationUriNode() throws UaException {
    return ClientNodeSupport.await(getApplicationUriNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getApplicationUriNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ApplicationUri",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readApplicationUri() throws UaException {
    return ClientNodeSupport.await(readApplicationUriAsync());
  }

  @Override
  public void writeApplicationUri(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeApplicationUriAsync(value)),
        "http://opcfoundation.org/UA/}ApplicationUri");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readApplicationUriAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getApplicationUriNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ApplicationUri",
                            false,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeApplicationUriAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getApplicationUriNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ApplicationUri",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getApplicationTypeNode() throws UaException {
    return ClientNodeSupport.await(getApplicationTypeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getApplicationTypeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ApplicationType",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable ApplicationType readApplicationType() throws UaException {
    return ClientNodeSupport.await(readApplicationTypeAsync());
  }

  @Override
  public void writeApplicationType(@Nullable ApplicationType value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeApplicationTypeAsync(value)),
        "http://opcfoundation.org/UA/}ApplicationType");
  }

  @Override
  public CompletableFuture<? extends @Nullable ApplicationType> readApplicationTypeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getApplicationTypeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ApplicationType",
                            false,
                            ApplicationType.class,
                            -1,
                            ApplicationType::from)),
                v -> CompletableFuture.completedFuture((@Nullable ApplicationType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeApplicationTypeAsync(@Nullable ApplicationType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getApplicationTypeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ApplicationType",
                        value,
                        ApplicationType.class,
                        -1,
                        ApplicationType::from)));
  }

  @Override
  public @Nullable PropertyTypeNode getApplicationNamesNode() throws UaException {
    return ClientNodeSupport.await(getApplicationNamesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getApplicationNamesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ApplicationNames",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public LocalizedText @Nullable [] readApplicationNames() throws UaException {
    return ClientNodeSupport.await(readApplicationNamesAsync());
  }

  @Override
  public void writeApplicationNames(LocalizedText @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeApplicationNamesAsync(value)),
        "http://opcfoundation.org/UA/}ApplicationNames");
  }

  @Override
  public CompletableFuture<? extends LocalizedText @Nullable []> readApplicationNamesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getApplicationNamesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ApplicationNames",
                            false,
                            LocalizedText.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((LocalizedText @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeApplicationNamesAsync(
      LocalizedText @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getApplicationNamesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ApplicationNames",
                        value,
                        LocalizedText.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getHasSecureElementNode() throws UaException {
    return ClientNodeSupport.await(getHasSecureElementNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getHasSecureElementNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "HasSecureElement",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readHasSecureElement() throws UaException {
    return ClientNodeSupport.await(readHasSecureElementAsync());
  }

  @Override
  public void writeHasSecureElement(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeHasSecureElementAsync(value)),
        "http://opcfoundation.org/UA/}HasSecureElement");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readHasSecureElementAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getHasSecureElementNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}HasSecureElement",
                            false,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeHasSecureElementAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getHasSecureElementNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}HasSecureElement",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getMaxTrustListSizeNode() throws UaException {
    return ClientNodeSupport.await(getMaxTrustListSizeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxTrustListSizeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxTrustListSize",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxTrustListSize() throws UaException {
    return ClientNodeSupport.await(readMaxTrustListSizeAsync());
  }

  @Override
  public void writeMaxTrustListSize(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxTrustListSizeAsync(value)),
        "http://opcfoundation.org/UA/}MaxTrustListSize");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxTrustListSizeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxTrustListSizeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxTrustListSize",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxTrustListSizeAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxTrustListSizeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxTrustListSize",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public CertificateGroupFolderTypeNode getCertificateGroupsNode() throws UaException {
    return ClientNodeSupport.await(getCertificateGroupsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends CertificateGroupFolderTypeNode>
      getCertificateGroupsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CertificateGroups",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        CertificateGroupFolderTypeNode.class)));
  }

  @Override
  public @Nullable ApplicationConfigurationFileTypeNode getConfigurationFileNode()
      throws UaException {
    return ClientNodeSupport.await(getConfigurationFileNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable ApplicationConfigurationFileTypeNode>
      getConfigurationFileNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ConfigurationFile",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        ApplicationConfigurationFileTypeNode.class)));
  }

  @Override
  public @Nullable PropertyTypeNode getInApplicationSetupNode() throws UaException {
    return ClientNodeSupport.await(getInApplicationSetupNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getInApplicationSetupNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "InApplicationSetup",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readInApplicationSetup() throws UaException {
    return ClientNodeSupport.await(readInApplicationSetupAsync());
  }

  @Override
  public void writeInApplicationSetup(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeInApplicationSetupAsync(value)),
        "http://opcfoundation.org/UA/}InApplicationSetup");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readInApplicationSetupAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getInApplicationSetupNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}InApplicationSetup",
                            false,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeInApplicationSetupAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getInApplicationSetupNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}InApplicationSetup",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getServerCapabilitiesNode() throws UaException {
    return ClientNodeSupport.await(getServerCapabilitiesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getServerCapabilitiesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ServerCapabilities",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String @Nullable [] readServerCapabilities() throws UaException {
    return ClientNodeSupport.await(readServerCapabilitiesAsync());
  }

  @Override
  public void writeServerCapabilities(@Nullable String @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeServerCapabilitiesAsync(value)),
        "http://opcfoundation.org/UA/}ServerCapabilities");
  }

  @Override
  public CompletableFuture<? extends @Nullable String @Nullable []> readServerCapabilitiesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getServerCapabilitiesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ServerCapabilities",
                            true,
                            String.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeServerCapabilitiesAsync(
      @Nullable String @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getServerCapabilitiesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ServerCapabilities",
                        value,
                        String.class,
                        1,
                        null)));
  }

  @Override
  public PropertyTypeNode getMulticastDnsEnabledNode() throws UaException {
    return ClientNodeSupport.await(getMulticastDnsEnabledNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMulticastDnsEnabledNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MulticastDnsEnabled",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readMulticastDnsEnabled() throws UaException {
    return ClientNodeSupport.await(readMulticastDnsEnabledAsync());
  }

  @Override
  public void writeMulticastDnsEnabled(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMulticastDnsEnabledAsync(value)),
        "http://opcfoundation.org/UA/}MulticastDnsEnabled");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readMulticastDnsEnabledAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMulticastDnsEnabledNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MulticastDnsEnabled",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMulticastDnsEnabledAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMulticastDnsEnabledNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MulticastDnsEnabled",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getSupportsTransactionsNode() throws UaException {
    return ClientNodeSupport.await(getSupportsTransactionsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getSupportsTransactionsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SupportsTransactions",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readSupportsTransactions() throws UaException {
    return ClientNodeSupport.await(readSupportsTransactionsAsync());
  }

  @Override
  public void writeSupportsTransactions(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSupportsTransactionsAsync(value)),
        "http://opcfoundation.org/UA/}SupportsTransactions");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readSupportsTransactionsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSupportsTransactionsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SupportsTransactions",
                            false,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSupportsTransactionsAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSupportsTransactionsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SupportsTransactions",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable TransactionDiagnosticsTypeNode getTransactionDiagnosticsNode()
      throws UaException {
    return ClientNodeSupport.await(getTransactionDiagnosticsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable TransactionDiagnosticsTypeNode>
      getTransactionDiagnosticsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "TransactionDiagnostics",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        TransactionDiagnosticsTypeNode.class)));
  }

  @Override
  public PropertyTypeNode getSupportedPrivateKeyFormatsNode() throws UaException {
    return ClientNodeSupport.await(getSupportedPrivateKeyFormatsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSupportedPrivateKeyFormatsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SupportedPrivateKeyFormats",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String @Nullable [] readSupportedPrivateKeyFormats() throws UaException {
    return ClientNodeSupport.await(readSupportedPrivateKeyFormatsAsync());
  }

  @Override
  public void writeSupportedPrivateKeyFormats(@Nullable String @Nullable [] value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSupportedPrivateKeyFormatsAsync(value)),
        "http://opcfoundation.org/UA/}SupportedPrivateKeyFormats");
  }

  @Override
  public CompletableFuture<? extends @Nullable String @Nullable []>
      readSupportedPrivateKeyFormatsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSupportedPrivateKeyFormatsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SupportedPrivateKeyFormats",
                            true,
                            String.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSupportedPrivateKeyFormatsAsync(
      @Nullable String @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSupportedPrivateKeyFormatsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SupportedPrivateKeyFormats",
                        value,
                        String.class,
                        1,
                        null)));
  }

  @Override
  public UaMethodNode getApplyChangesMethodNode() throws UaException {
    return ClientNodeSupport.await(getApplyChangesMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getApplyChangesMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "ApplyChanges",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void applyChanges() throws UaException {
    ClientNodeSupport.await(applyChangesAsync());
  }

  @Override
  public MethodCallResult<Void> callApplyChanges() throws UaException {
    return ClientNodeSupport.await(callApplyChangesAsync());
  }

  @Override
  public MethodCallResult<Void> callApplyChangesWith(MethodCallOptions options) throws UaException {
    return ClientNodeSupport.await(callApplyChangesWithAsync(options));
  }

  @Override
  public CompletableFuture<Void> applyChangesAsync() {
    return ClientNodeSupport.compose(
        callApplyChangesAsync(),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callApplyChangesAsync() {
    return callApplyChangesWithAsync(MethodCallOptions.DEFAULT);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callApplyChangesWithAsync(
      MethodCallOptions options) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs = new Variant[0];
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getApplyChangesMethodNodeAsync(),
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
  public @Nullable UaMethodNode getCancelChangesMethodNode() throws UaException {
    return ClientNodeSupport.await(getCancelChangesMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getCancelChangesMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "CancelChanges",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void cancelChanges() throws UaException {
    ClientNodeSupport.await(cancelChangesAsync());
  }

  @Override
  public MethodCallResult<Void> callCancelChanges() throws UaException {
    return ClientNodeSupport.await(callCancelChangesAsync());
  }

  @Override
  public MethodCallResult<Void> callCancelChangesWith(MethodCallOptions options)
      throws UaException {
    return ClientNodeSupport.await(callCancelChangesWithAsync(options));
  }

  @Override
  public CompletableFuture<Void> cancelChangesAsync() {
    return ClientNodeSupport.compose(
        callCancelChangesAsync(),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callCancelChangesAsync() {
    return callCancelChangesWithAsync(MethodCallOptions.DEFAULT);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callCancelChangesWithAsync(
      MethodCallOptions options) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs = new Variant[0];
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getCancelChangesMethodNodeAsync(),
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
  public @Nullable UaMethodNode getCreateSelfSignedCertificateMethodNode() throws UaException {
    return ClientNodeSupport.await(getCreateSelfSignedCertificateMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getCreateSelfSignedCertificateMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "CreateSelfSignedCertificate",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable ByteString createSelfSignedCertificate(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      @Nullable String @Nullable [] dnsNames,
      @Nullable String @Nullable [] ipAddresses,
      @Nullable UShort lifetimeInDays,
      @Nullable UShort keySizeInBits)
      throws UaException {
    return ClientNodeSupport.await(
        createSelfSignedCertificateAsync(
            certificateGroupId,
            certificateTypeId,
            subjectName,
            dnsNames,
            ipAddresses,
            lifetimeInDays,
            keySizeInBits));
  }

  @Override
  public MethodCallResult<@Nullable ByteString> callCreateSelfSignedCertificate(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      @Nullable String @Nullable [] dnsNames,
      @Nullable String @Nullable [] ipAddresses,
      @Nullable UShort lifetimeInDays,
      @Nullable UShort keySizeInBits)
      throws UaException {
    return ClientNodeSupport.await(
        callCreateSelfSignedCertificateAsync(
            certificateGroupId,
            certificateTypeId,
            subjectName,
            dnsNames,
            ipAddresses,
            lifetimeInDays,
            keySizeInBits));
  }

  @Override
  public MethodCallResult<@Nullable ByteString> callCreateSelfSignedCertificateWith(
      MethodCallOptions options,
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      @Nullable String @Nullable [] dnsNames,
      @Nullable String @Nullable [] ipAddresses,
      @Nullable UShort lifetimeInDays,
      @Nullable UShort keySizeInBits)
      throws UaException {
    return ClientNodeSupport.await(
        callCreateSelfSignedCertificateWithAsync(
            options,
            certificateGroupId,
            certificateTypeId,
            subjectName,
            dnsNames,
            ipAddresses,
            lifetimeInDays,
            keySizeInBits));
  }

  @Override
  public CompletableFuture<@Nullable ByteString> createSelfSignedCertificateAsync(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      @Nullable String @Nullable [] dnsNames,
      @Nullable String @Nullable [] ipAddresses,
      @Nullable UShort lifetimeInDays,
      @Nullable UShort keySizeInBits) {
    return ClientNodeSupport.compose(
        callCreateSelfSignedCertificateAsync(
            certificateGroupId,
            certificateTypeId,
            subjectName,
            dnsNames,
            ipAddresses,
            lifetimeInDays,
            keySizeInBits),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable ByteString>>
      callCreateSelfSignedCertificateAsync(
          @Nullable NodeId certificateGroupId,
          @Nullable NodeId certificateTypeId,
          @Nullable String subjectName,
          @Nullable String @Nullable [] dnsNames,
          @Nullable String @Nullable [] ipAddresses,
          @Nullable UShort lifetimeInDays,
          @Nullable UShort keySizeInBits) {
    return callCreateSelfSignedCertificateWithAsync(
        MethodCallOptions.DEFAULT,
        certificateGroupId,
        certificateTypeId,
        subjectName,
        dnsNames,
        ipAddresses,
        lifetimeInDays,
        keySizeInBits);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable ByteString>>
      callCreateSelfSignedCertificateWithAsync(
          MethodCallOptions options,
          @Nullable NodeId certificateGroupId,
          @Nullable NodeId certificateTypeId,
          @Nullable String subjectName,
          @Nullable String @Nullable [] dnsNames,
          @Nullable String @Nullable [] ipAddresses,
          @Nullable UShort lifetimeInDays,
          @Nullable UShort keySizeInBits) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new ServerConfigurationTypeCreateSelfSignedCertificate.Inputs(
                      certificateGroupId,
                      certificateTypeId,
                      subjectName,
                      dnsNames,
                      ipAddresses,
                      lifetimeInDays,
                      keySizeInBits)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getCreateSelfSignedCertificateMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return ServerConfigurationTypeCreateSelfSignedCertificate
                                        .Outputs.fromVariants(
                                            client.getStaticEncodingContext(), values)
                                        .certificate();
                                  }))));
        });
  }

  @Override
  public UaMethodNode getCreateSigningRequestMethodNode() throws UaException {
    return ClientNodeSupport.await(getCreateSigningRequestMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getCreateSigningRequestMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "CreateSigningRequest",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable ByteString createSigningRequest(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      @Nullable Boolean regeneratePrivateKey,
      @Nullable ByteString nonce)
      throws UaException {
    return ClientNodeSupport.await(
        createSigningRequestAsync(
            certificateGroupId, certificateTypeId, subjectName, regeneratePrivateKey, nonce));
  }

  @Override
  public MethodCallResult<@Nullable ByteString> callCreateSigningRequest(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      @Nullable Boolean regeneratePrivateKey,
      @Nullable ByteString nonce)
      throws UaException {
    return ClientNodeSupport.await(
        callCreateSigningRequestAsync(
            certificateGroupId, certificateTypeId, subjectName, regeneratePrivateKey, nonce));
  }

  @Override
  public MethodCallResult<@Nullable ByteString> callCreateSigningRequestWith(
      MethodCallOptions options,
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      @Nullable Boolean regeneratePrivateKey,
      @Nullable ByteString nonce)
      throws UaException {
    return ClientNodeSupport.await(
        callCreateSigningRequestWithAsync(
            options,
            certificateGroupId,
            certificateTypeId,
            subjectName,
            regeneratePrivateKey,
            nonce));
  }

  @Override
  public CompletableFuture<@Nullable ByteString> createSigningRequestAsync(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      @Nullable Boolean regeneratePrivateKey,
      @Nullable ByteString nonce) {
    return ClientNodeSupport.compose(
        callCreateSigningRequestAsync(
            certificateGroupId, certificateTypeId, subjectName, regeneratePrivateKey, nonce),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable ByteString>> callCreateSigningRequestAsync(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      @Nullable Boolean regeneratePrivateKey,
      @Nullable ByteString nonce) {
    return callCreateSigningRequestWithAsync(
        MethodCallOptions.DEFAULT,
        certificateGroupId,
        certificateTypeId,
        subjectName,
        regeneratePrivateKey,
        nonce);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable ByteString>>
      callCreateSigningRequestWithAsync(
          MethodCallOptions options,
          @Nullable NodeId certificateGroupId,
          @Nullable NodeId certificateTypeId,
          @Nullable String subjectName,
          @Nullable Boolean regeneratePrivateKey,
          @Nullable ByteString nonce) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new ServerConfigurationTypeCreateSigningRequest.Inputs(
                      certificateGroupId,
                      certificateTypeId,
                      subjectName,
                      regeneratePrivateKey,
                      nonce)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getCreateSigningRequestMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return ServerConfigurationTypeCreateSigningRequest.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values)
                                        .certificateRequest();
                                  }))));
        });
  }

  @Override
  public @Nullable UaMethodNode getDeleteCertificateMethodNode() throws UaException {
    return ClientNodeSupport.await(getDeleteCertificateMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getDeleteCertificateMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "DeleteCertificate",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void deleteCertificate(
      @Nullable NodeId certificateGroupId, @Nullable NodeId certificateTypeId) throws UaException {
    ClientNodeSupport.await(deleteCertificateAsync(certificateGroupId, certificateTypeId));
  }

  @Override
  public MethodCallResult<Void> callDeleteCertificate(
      @Nullable NodeId certificateGroupId, @Nullable NodeId certificateTypeId) throws UaException {
    return ClientNodeSupport.await(
        callDeleteCertificateAsync(certificateGroupId, certificateTypeId));
  }

  @Override
  public MethodCallResult<Void> callDeleteCertificateWith(
      MethodCallOptions options,
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId)
      throws UaException {
    return ClientNodeSupport.await(
        callDeleteCertificateWithAsync(options, certificateGroupId, certificateTypeId));
  }

  @Override
  public CompletableFuture<Void> deleteCertificateAsync(
      @Nullable NodeId certificateGroupId, @Nullable NodeId certificateTypeId) {
    return ClientNodeSupport.compose(
        callDeleteCertificateAsync(certificateGroupId, certificateTypeId),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callDeleteCertificateAsync(
      @Nullable NodeId certificateGroupId, @Nullable NodeId certificateTypeId) {
    return callDeleteCertificateWithAsync(
        MethodCallOptions.DEFAULT, certificateGroupId, certificateTypeId);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callDeleteCertificateWithAsync(
      MethodCallOptions options,
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new ServerConfigurationTypeDeleteCertificate.Inputs(
                      certificateGroupId, certificateTypeId)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getDeleteCertificateMethodNodeAsync(),
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
  public @Nullable UaMethodNode getGetCertificatesMethodNode() throws UaException {
    return ClientNodeSupport.await(getGetCertificatesMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getGetCertificatesMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "GetCertificates",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public ServerConfigurationTypeGetCertificates.Outputs getCertificates(
      @Nullable NodeId certificateGroupId) throws UaException {
    return ClientNodeSupport.await(getCertificatesAsync(certificateGroupId));
  }

  @Override
  public MethodCallResult<ServerConfigurationTypeGetCertificates.Outputs> callGetCertificates(
      @Nullable NodeId certificateGroupId) throws UaException {
    return ClientNodeSupport.await(callGetCertificatesAsync(certificateGroupId));
  }

  @Override
  public MethodCallResult<ServerConfigurationTypeGetCertificates.Outputs> callGetCertificatesWith(
      MethodCallOptions options, @Nullable NodeId certificateGroupId) throws UaException {
    return ClientNodeSupport.await(callGetCertificatesWithAsync(options, certificateGroupId));
  }

  @Override
  public CompletableFuture<ServerConfigurationTypeGetCertificates.Outputs> getCertificatesAsync(
      @Nullable NodeId certificateGroupId) {
    return ClientNodeSupport.compose(
        callGetCertificatesAsync(certificateGroupId),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<ServerConfigurationTypeGetCertificates.Outputs>>
      callGetCertificatesAsync(@Nullable NodeId certificateGroupId) {
    return callGetCertificatesWithAsync(MethodCallOptions.DEFAULT, certificateGroupId);
  }

  @Override
  public CompletableFuture<MethodCallResult<ServerConfigurationTypeGetCertificates.Outputs>>
      callGetCertificatesWithAsync(MethodCallOptions options, @Nullable NodeId certificateGroupId) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new ServerConfigurationTypeGetCertificates.Inputs(certificateGroupId)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getGetCertificatesMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return ServerConfigurationTypeGetCertificates.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values);
                                  }))));
        });
  }

  @Override
  public UaMethodNode getGetRejectedListMethodNode() throws UaException {
    return ClientNodeSupport.await(getGetRejectedListMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getGetRejectedListMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
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
                                    return ServerConfigurationTypeGetRejectedList.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values)
                                        .certificates();
                                  }))));
        });
  }

  @Override
  public @Nullable UaMethodNode getResetToServerDefaultsMethodNode() throws UaException {
    return ClientNodeSupport.await(getResetToServerDefaultsMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getResetToServerDefaultsMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "ResetToServerDefaults",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void resetToServerDefaults() throws UaException {
    ClientNodeSupport.await(resetToServerDefaultsAsync());
  }

  @Override
  public MethodCallResult<Void> callResetToServerDefaults() throws UaException {
    return ClientNodeSupport.await(callResetToServerDefaultsAsync());
  }

  @Override
  public MethodCallResult<Void> callResetToServerDefaultsWith(MethodCallOptions options)
      throws UaException {
    return ClientNodeSupport.await(callResetToServerDefaultsWithAsync(options));
  }

  @Override
  public CompletableFuture<Void> resetToServerDefaultsAsync() {
    return ClientNodeSupport.compose(
        callResetToServerDefaultsAsync(),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callResetToServerDefaultsAsync() {
    return callResetToServerDefaultsWithAsync(MethodCallOptions.DEFAULT);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callResetToServerDefaultsWithAsync(
      MethodCallOptions options) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs = new Variant[0];
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getResetToServerDefaultsMethodNodeAsync(),
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
  public UaMethodNode getUpdateCertificateMethodNode() throws UaException {
    return ClientNodeSupport.await(getUpdateCertificateMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getUpdateCertificateMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "UpdateCertificate",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable Boolean updateCertificate(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable ByteString certificate,
      ByteString @Nullable [] issuerCertificates,
      @Nullable String privateKeyFormat,
      @Nullable ByteString privateKey)
      throws UaException {
    return ClientNodeSupport.await(
        updateCertificateAsync(
            certificateGroupId,
            certificateTypeId,
            certificate,
            issuerCertificates,
            privateKeyFormat,
            privateKey));
  }

  @Override
  public MethodCallResult<@Nullable Boolean> callUpdateCertificate(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable ByteString certificate,
      ByteString @Nullable [] issuerCertificates,
      @Nullable String privateKeyFormat,
      @Nullable ByteString privateKey)
      throws UaException {
    return ClientNodeSupport.await(
        callUpdateCertificateAsync(
            certificateGroupId,
            certificateTypeId,
            certificate,
            issuerCertificates,
            privateKeyFormat,
            privateKey));
  }

  @Override
  public MethodCallResult<@Nullable Boolean> callUpdateCertificateWith(
      MethodCallOptions options,
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable ByteString certificate,
      ByteString @Nullable [] issuerCertificates,
      @Nullable String privateKeyFormat,
      @Nullable ByteString privateKey)
      throws UaException {
    return ClientNodeSupport.await(
        callUpdateCertificateWithAsync(
            options,
            certificateGroupId,
            certificateTypeId,
            certificate,
            issuerCertificates,
            privateKeyFormat,
            privateKey));
  }

  @Override
  public CompletableFuture<@Nullable Boolean> updateCertificateAsync(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable ByteString certificate,
      ByteString @Nullable [] issuerCertificates,
      @Nullable String privateKeyFormat,
      @Nullable ByteString privateKey) {
    return ClientNodeSupport.compose(
        callUpdateCertificateAsync(
            certificateGroupId,
            certificateTypeId,
            certificate,
            issuerCertificates,
            privateKeyFormat,
            privateKey),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable Boolean>> callUpdateCertificateAsync(
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable ByteString certificate,
      ByteString @Nullable [] issuerCertificates,
      @Nullable String privateKeyFormat,
      @Nullable ByteString privateKey) {
    return callUpdateCertificateWithAsync(
        MethodCallOptions.DEFAULT,
        certificateGroupId,
        certificateTypeId,
        certificate,
        issuerCertificates,
        privateKeyFormat,
        privateKey);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable Boolean>> callUpdateCertificateWithAsync(
      MethodCallOptions options,
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable ByteString certificate,
      ByteString @Nullable [] issuerCertificates,
      @Nullable String privateKeyFormat,
      @Nullable ByteString privateKey) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new ServerConfigurationTypeUpdateCertificate.Inputs(
                      certificateGroupId,
                      certificateTypeId,
                      certificate,
                      issuerCertificates,
                      privateKeyFormat,
                      privateKey)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getUpdateCertificateMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return ServerConfigurationTypeUpdateCertificate.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values)
                                        .applyChangesRequired();
                                  }))));
        });
  }
}
