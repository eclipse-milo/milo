package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.IdType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link NamespaceMetadataType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.13">Model
 *     documentation</a>
 */
public class NamespaceMetadataTypeNode extends BaseObjectTypeNode implements NamespaceMetadataType {
  public NamespaceMetadataTypeNode(
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
  public @Nullable PropertyTypeNode getModelVersionNode() throws UaException {
    return ClientNodeSupport.await(getModelVersionNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getModelVersionNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ModelVersion",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readModelVersion() throws UaException {
    return ClientNodeSupport.await(readModelVersionAsync());
  }

  @Override
  public void writeModelVersion(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeModelVersionAsync(value)),
        "http://opcfoundation.org/UA/}ModelVersion");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readModelVersionAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getModelVersionNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ModelVersion",
                            false,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeModelVersionAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getModelVersionNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ModelVersion",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getNamespaceUriNode() throws UaException {
    return ClientNodeSupport.await(getNamespaceUriNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getNamespaceUriNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "NamespaceUri",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readNamespaceUri() throws UaException {
    return ClientNodeSupport.await(readNamespaceUriAsync());
  }

  @Override
  public void writeNamespaceUri(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeNamespaceUriAsync(value)),
        "http://opcfoundation.org/UA/}NamespaceUri");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readNamespaceUriAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getNamespaceUriNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}NamespaceUri",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeNamespaceUriAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getNamespaceUriNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}NamespaceUri",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable AddressSpaceFileTypeNode getNamespaceFileNode() throws UaException {
    return ClientNodeSupport.await(getNamespaceFileNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable AddressSpaceFileTypeNode>
      getNamespaceFileNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "NamespaceFile",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        AddressSpaceFileTypeNode.class)));
  }

  @Override
  public PropertyTypeNode getNamespaceVersionNode() throws UaException {
    return ClientNodeSupport.await(getNamespaceVersionNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getNamespaceVersionNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "NamespaceVersion",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readNamespaceVersion() throws UaException {
    return ClientNodeSupport.await(readNamespaceVersionAsync());
  }

  @Override
  public void writeNamespaceVersion(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeNamespaceVersionAsync(value)),
        "http://opcfoundation.org/UA/}NamespaceVersion");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readNamespaceVersionAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getNamespaceVersionNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}NamespaceVersion",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeNamespaceVersionAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getNamespaceVersionNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}NamespaceVersion",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getIsNamespaceSubsetNode() throws UaException {
    return ClientNodeSupport.await(getIsNamespaceSubsetNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getIsNamespaceSubsetNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "IsNamespaceSubset",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readIsNamespaceSubset() throws UaException {
    return ClientNodeSupport.await(readIsNamespaceSubsetAsync());
  }

  @Override
  public void writeIsNamespaceSubset(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeIsNamespaceSubsetAsync(value)),
        "http://opcfoundation.org/UA/}IsNamespaceSubset");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readIsNamespaceSubsetAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getIsNamespaceSubsetNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}IsNamespaceSubset",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeIsNamespaceSubsetAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getIsNamespaceSubsetNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}IsNamespaceSubset",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getStaticNodeIdTypesNode() throws UaException {
    return ClientNodeSupport.await(getStaticNodeIdTypesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getStaticNodeIdTypesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "StaticNodeIdTypes",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public IdType @Nullable [] readStaticNodeIdTypes() throws UaException {
    return ClientNodeSupport.await(readStaticNodeIdTypesAsync());
  }

  @Override
  public void writeStaticNodeIdTypes(IdType @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeStaticNodeIdTypesAsync(value)),
        "http://opcfoundation.org/UA/}StaticNodeIdTypes");
  }

  @Override
  public CompletableFuture<? extends IdType @Nullable []> readStaticNodeIdTypesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getStaticNodeIdTypesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}StaticNodeIdTypes",
                            true,
                            IdType.class,
                            1,
                            IdType::from)),
                v -> CompletableFuture.completedFuture((IdType @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeStaticNodeIdTypesAsync(IdType @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getStaticNodeIdTypesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}StaticNodeIdTypes",
                        value,
                        IdType.class,
                        1,
                        IdType::from)));
  }

  @Override
  public @Nullable PropertyTypeNode getConfigurationVersionNode() throws UaException {
    return ClientNodeSupport.await(getConfigurationVersionNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getConfigurationVersionNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ConfigurationVersion",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readConfigurationVersion() throws UaException {
    return ClientNodeSupport.await(readConfigurationVersionAsync());
  }

  @Override
  public void writeConfigurationVersion(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeConfigurationVersionAsync(value)),
        "http://opcfoundation.org/UA/}ConfigurationVersion");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readConfigurationVersionAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getConfigurationVersionNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ConfigurationVersion",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeConfigurationVersionAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getConfigurationVersionNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ConfigurationVersion",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getDefaultRolePermissionsNode() throws UaException {
    return ClientNodeSupport.await(getDefaultRolePermissionsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getDefaultRolePermissionsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DefaultRolePermissions",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable RolePermissionType @Nullable [] readDefaultRolePermissions() throws UaException {
    return ClientNodeSupport.await(readDefaultRolePermissionsAsync());
  }

  @Override
  public void writeDefaultRolePermissions(@Nullable RolePermissionType @Nullable [] value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDefaultRolePermissionsAsync(value)),
        "http://opcfoundation.org/UA/}DefaultRolePermissions");
  }

  @Override
  public CompletableFuture<? extends @Nullable RolePermissionType @Nullable []>
      readDefaultRolePermissionsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDefaultRolePermissionsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DefaultRolePermissions",
                            false,
                            RolePermissionType.class,
                            1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable RolePermissionType @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDefaultRolePermissionsAsync(
      @Nullable RolePermissionType @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDefaultRolePermissionsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DefaultRolePermissions",
                        value,
                        RolePermissionType.class,
                        1,
                        null)));
  }

  @Override
  public PropertyTypeNode getNamespacePublicationDateNode() throws UaException {
    return ClientNodeSupport.await(getNamespacePublicationDateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getNamespacePublicationDateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "NamespacePublicationDate",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable DateTime readNamespacePublicationDate() throws UaException {
    return ClientNodeSupport.await(readNamespacePublicationDateAsync());
  }

  @Override
  public void writeNamespacePublicationDate(@Nullable DateTime value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeNamespacePublicationDateAsync(value)),
        "http://opcfoundation.org/UA/}NamespacePublicationDate");
  }

  @Override
  public CompletableFuture<? extends @Nullable DateTime> readNamespacePublicationDateAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getNamespacePublicationDateNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}NamespacePublicationDate",
                            true,
                            DateTime.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable DateTime) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeNamespacePublicationDateAsync(
      @Nullable DateTime value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getNamespacePublicationDateNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}NamespacePublicationDate",
                        value,
                        DateTime.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getStaticNumericNodeIdRangeNode() throws UaException {
    return ClientNodeSupport.await(getStaticNumericNodeIdRangeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getStaticNumericNodeIdRangeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "StaticNumericNodeIdRange",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String @Nullable [] readStaticNumericNodeIdRange() throws UaException {
    return ClientNodeSupport.await(readStaticNumericNodeIdRangeAsync());
  }

  @Override
  public void writeStaticNumericNodeIdRange(@Nullable String @Nullable [] value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeStaticNumericNodeIdRangeAsync(value)),
        "http://opcfoundation.org/UA/}StaticNumericNodeIdRange");
  }

  @Override
  public CompletableFuture<? extends @Nullable String @Nullable []>
      readStaticNumericNodeIdRangeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getStaticNumericNodeIdRangeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}StaticNumericNodeIdRange",
                            true,
                            String.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeStaticNumericNodeIdRangeAsync(
      @Nullable String @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getStaticNumericNodeIdRangeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}StaticNumericNodeIdRange",
                        value,
                        String.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getDefaultAccessRestrictionsNode() throws UaException {
    return ClientNodeSupport.await(getDefaultAccessRestrictionsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getDefaultAccessRestrictionsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DefaultAccessRestrictions",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable AccessRestrictionType readDefaultAccessRestrictions() throws UaException {
    return ClientNodeSupport.await(readDefaultAccessRestrictionsAsync());
  }

  @Override
  public void writeDefaultAccessRestrictions(@Nullable AccessRestrictionType value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDefaultAccessRestrictionsAsync(value)),
        "http://opcfoundation.org/UA/}DefaultAccessRestrictions");
  }

  @Override
  public CompletableFuture<? extends @Nullable AccessRestrictionType>
      readDefaultAccessRestrictionsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDefaultAccessRestrictionsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DefaultAccessRestrictions",
                            false,
                            AccessRestrictionType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable AccessRestrictionType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDefaultAccessRestrictionsAsync(
      @Nullable AccessRestrictionType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDefaultAccessRestrictionsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DefaultAccessRestrictions",
                        value,
                        AccessRestrictionType.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getStaticStringNodeIdPatternNode() throws UaException {
    return ClientNodeSupport.await(getStaticStringNodeIdPatternNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getStaticStringNodeIdPatternNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "StaticStringNodeIdPattern",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readStaticStringNodeIdPattern() throws UaException {
    return ClientNodeSupport.await(readStaticStringNodeIdPatternAsync());
  }

  @Override
  public void writeStaticStringNodeIdPattern(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeStaticStringNodeIdPatternAsync(value)),
        "http://opcfoundation.org/UA/}StaticStringNodeIdPattern");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readStaticStringNodeIdPatternAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getStaticStringNodeIdPatternNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}StaticStringNodeIdPattern",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeStaticStringNodeIdPatternAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getStaticStringNodeIdPatternNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}StaticStringNodeIdPattern",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getDefaultUserRolePermissionsNode() throws UaException {
    return ClientNodeSupport.await(getDefaultUserRolePermissionsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getDefaultUserRolePermissionsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "DefaultUserRolePermissions",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable RolePermissionType @Nullable [] readDefaultUserRolePermissions()
      throws UaException {
    return ClientNodeSupport.await(readDefaultUserRolePermissionsAsync());
  }

  @Override
  public void writeDefaultUserRolePermissions(@Nullable RolePermissionType @Nullable [] value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDefaultUserRolePermissionsAsync(value)),
        "http://opcfoundation.org/UA/}DefaultUserRolePermissions");
  }

  @Override
  public CompletableFuture<? extends @Nullable RolePermissionType @Nullable []>
      readDefaultUserRolePermissionsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDefaultUserRolePermissionsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}DefaultUserRolePermissions",
                            false,
                            RolePermissionType.class,
                            1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable RolePermissionType @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDefaultUserRolePermissionsAsync(
      @Nullable RolePermissionType @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDefaultUserRolePermissionsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}DefaultUserRolePermissions",
                        value,
                        RolePermissionType.class,
                        1,
                        null)));
  }
}
