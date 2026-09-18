package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ApplicationConfigurationType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.14">Model
 *     documentation</a>
 */
public class ApplicationConfigurationTypeNode extends ServerConfigurationTypeNode
    implements ApplicationConfigurationType {
  public ApplicationConfigurationTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      @Nullable LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType @Nullable [] rolePermissions,
      RolePermissionType @Nullable [] userRolePermissions,
      @Nullable AccessRestrictionType accessRestrictions) {
    super(
        context,
        nodeId,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions);
  }

  public ApplicationConfigurationTypeNode(
      UaNodeContext context,
      NodeId nodeId,
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
        context,
        nodeId,
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
  public PropertyTypeNode getApplicationTypeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ApplicationType",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 307L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public PropertyTypeNode getApplicationUriNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ApplicationUri",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 23751L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable AuthorizationServicesConfigurationFolderTypeNode getAuthorizationServicesNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "AuthorizationServices",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 23556L),
        null,
        -1,
        AuthorizationServicesConfigurationFolderTypeNode.class);
  }

  @Override
  public PropertyTypeNode getEnabledNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Enabled",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getEnabled() {
    return ServerNodeSupport.read(this, getEnabledNode(), Boolean.class, null);
  }

  @Override
  public void setEnabled(@Nullable Boolean value) {
    ServerNodeSupport.write(this, getEnabledNode(), value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getIsNonUaApplicationNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "IsNonUaApplication",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getIsNonUaApplication() {
    return ServerNodeSupport.read(this, getIsNonUaApplicationNode(), Boolean.class, null);
  }

  @Override
  public void setIsNonUaApplication(@Nullable Boolean value) {
    ServerNodeSupport.write(
        this,
        getIsNonUaApplicationNode(),
        Namespaces.OPC_UA,
        "IsNonUaApplication",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable KeyCredentialConfigurationFolderTypeNode getKeyCredentialsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "KeyCredentials",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17496L),
        null,
        -1,
        KeyCredentialConfigurationFolderTypeNode.class);
  }

  @Override
  public PropertyTypeNode getProductUriNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ProductUri",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 23751L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getAuthorizationServicesNode();
    getEnabledNode();
    getIsNonUaApplicationNode();
    getKeyCredentialsNode();
  }

  @Override
  public void setMethods(ApplicationConfigurationType.@Nullable Methods methods) {
    setApplyChangesHandler(methods == null ? null : methods::applyChanges);
    if (getCancelChangesMethodNode() != null) {
      setCancelChangesHandler(methods == null ? null : methods::cancelChanges);
    }
    if (getCreateSelfSignedCertificateMethodNode() != null) {
      setCreateSelfSignedCertificateHandler(
          methods == null ? null : methods::createSelfSignedCertificate);
    }
    setCreateSigningRequestHandler(methods == null ? null : methods::createSigningRequest);
    if (getDeleteCertificateMethodNode() != null) {
      setDeleteCertificateHandler(methods == null ? null : methods::deleteCertificate);
    }
    if (getGetCertificatesMethodNode() != null) {
      setGetCertificatesHandler(methods == null ? null : methods::getCertificates);
    }
    setGetRejectedListHandler(methods == null ? null : methods::getRejectedList);
    if (getResetToServerDefaultsMethodNode() != null) {
      setResetToServerDefaultsHandler(methods == null ? null : methods::resetToServerDefaults);
    }
    setUpdateCertificateHandler(methods == null ? null : methods::updateCertificate);
  }
}
