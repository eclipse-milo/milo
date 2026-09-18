package org.eclipse.milo.opcua.sdk.server.model.objects;

import java.util.Objects;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerConfigurationTypeApplyChanges;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerConfigurationTypeCancelChanges;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerConfigurationTypeCreateSelfSignedCertificate;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerConfigurationTypeCreateSigningRequest;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerConfigurationTypeDeleteCertificate;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerConfigurationTypeGetCertificates;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerConfigurationTypeGetRejectedList;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerConfigurationTypeResetToServerDefaults;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerConfigurationTypeUpdateCertificate;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.InvalidArgumentException;
import org.eclipse.milo.opcua.sdk.server.methods.MethodArgumentValidator;
import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaArgumentConversionException;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
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

  public ServerConfigurationTypeNode(
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
  public @Nullable PropertyTypeNode getApplicationNamesNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ApplicationNames",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public LocalizedText @Nullable [] getApplicationNames() {
    return ServerNodeSupport.readArray(this, getApplicationNamesNode(), LocalizedText.class, null);
  }

  @Override
  public void setApplicationNames(LocalizedText @Nullable [] value) {
    ServerNodeSupport.write(
        this,
        getApplicationNamesNode(),
        Namespaces.OPC_UA,
        "ApplicationNames",
        value,
        true,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getApplicationTypeNode() {
    return ServerNodeSupport.optionalChild(
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
  public @Nullable ApplicationType getApplicationType() {
    return ServerNodeSupport.read(
        this, getApplicationTypeNode(), ApplicationType.class, ApplicationType::from);
  }

  @Override
  public void setApplicationType(@Nullable ApplicationType value) {
    ServerNodeSupport.write(
        this,
        getApplicationTypeNode(),
        Namespaces.OPC_UA,
        "ApplicationType",
        value,
        false,
        true,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getApplicationUriNode() {
    return ServerNodeSupport.optionalChild(
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
  public @Nullable String getApplicationUri() {
    return ServerNodeSupport.read(this, getApplicationUriNode(), String.class, null);
  }

  @Override
  public void setApplicationUri(@Nullable String value) {
    ServerNodeSupport.write(
        this,
        getApplicationUriNode(),
        Namespaces.OPC_UA,
        "ApplicationUri",
        value,
        false,
        false,
        false);
  }

  @Override
  public CertificateGroupFolderTypeNode getCertificateGroupsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "CertificateGroups",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 13813L),
        null,
        -1,
        CertificateGroupFolderTypeNode.class);
  }

  @Override
  public @Nullable ApplicationConfigurationFileTypeNode getConfigurationFileNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ConfigurationFile",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 15550L),
        null,
        -1,
        ApplicationConfigurationFileTypeNode.class);
  }

  @Override
  public @Nullable PropertyTypeNode getHasSecureElementNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "HasSecureElement",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getHasSecureElement() {
    return ServerNodeSupport.read(this, getHasSecureElementNode(), Boolean.class, null);
  }

  @Override
  public void setHasSecureElement(@Nullable Boolean value) {
    ServerNodeSupport.write(
        this,
        getHasSecureElementNode(),
        Namespaces.OPC_UA,
        "HasSecureElement",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getInApplicationSetupNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "InApplicationSetup",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getInApplicationSetup() {
    return ServerNodeSupport.read(this, getInApplicationSetupNode(), Boolean.class, null);
  }

  @Override
  public void setInApplicationSetup(@Nullable Boolean value) {
    ServerNodeSupport.write(
        this,
        getInApplicationSetupNode(),
        Namespaces.OPC_UA,
        "InApplicationSetup",
        value,
        false,
        false,
        false);
  }

  @Override
  public PropertyTypeNode getMaxTrustListSizeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MaxTrustListSize",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxTrustListSize() {
    return ServerNodeSupport.read(this, getMaxTrustListSizeNode(), UInteger.class, null);
  }

  @Override
  public void setMaxTrustListSize(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getMaxTrustListSizeNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getMulticastDnsEnabledNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MulticastDnsEnabled",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getMulticastDnsEnabled() {
    return ServerNodeSupport.read(this, getMulticastDnsEnabledNode(), Boolean.class, null);
  }

  @Override
  public void setMulticastDnsEnabled(@Nullable Boolean value) {
    ServerNodeSupport.write(this, getMulticastDnsEnabledNode(), value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getProductUriNode() {
    return ServerNodeSupport.optionalChild(
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
  public @Nullable String getProductUri() {
    return ServerNodeSupport.read(this, getProductUriNode(), String.class, null);
  }

  @Override
  public void setProductUri(@Nullable String value) {
    ServerNodeSupport.write(
        this, getProductUriNode(), Namespaces.OPC_UA, "ProductUri", value, false, false, false);
  }

  @Override
  public PropertyTypeNode getServerCapabilitiesNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ServerCapabilities",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String @Nullable [] getServerCapabilities() {
    return ServerNodeSupport.readArray(this, getServerCapabilitiesNode(), String.class, null);
  }

  @Override
  public void setServerCapabilities(@Nullable String @Nullable [] value) {
    ServerNodeSupport.write(this, getServerCapabilitiesNode(), value, true, false, false);
  }

  @Override
  public PropertyTypeNode getSupportedPrivateKeyFormatsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SupportedPrivateKeyFormats",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String @Nullable [] getSupportedPrivateKeyFormats() {
    return ServerNodeSupport.readArray(
        this, getSupportedPrivateKeyFormatsNode(), String.class, null);
  }

  @Override
  public void setSupportedPrivateKeyFormats(@Nullable String @Nullable [] value) {
    ServerNodeSupport.write(this, getSupportedPrivateKeyFormatsNode(), value, true, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getSupportsTransactionsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "SupportsTransactions",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getSupportsTransactions() {
    return ServerNodeSupport.read(this, getSupportsTransactionsNode(), Boolean.class, null);
  }

  @Override
  public void setSupportsTransactions(@Nullable Boolean value) {
    ServerNodeSupport.write(
        this,
        getSupportsTransactionsNode(),
        Namespaces.OPC_UA,
        "SupportsTransactions",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable TransactionDiagnosticsTypeNode getTransactionDiagnosticsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "TransactionDiagnostics",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 32286L),
        null,
        -1,
        TransactionDiagnosticsTypeNode.class);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getApplicationNamesNode();
    getApplicationTypeNode();
    getApplicationUriNode();
    getCertificateGroupsNode();
    getConfigurationFileNode();
    getHasSecureElementNode();
    getInApplicationSetupNode();
    getMaxTrustListSizeNode();
    getMulticastDnsEnabledNode();
    getProductUriNode();
    getServerCapabilitiesNode();
    getSupportedPrivateKeyFormatsNode();
    getSupportsTransactionsNode();
    getTransactionDiagnosticsNode();
  }

  @Override
  public UaMethodNode getApplyChangesMethodNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        "http://opcfoundation.org/UA/",
        "ApplyChanges",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setApplyChangesHandler(
      ServerConfigurationType.@Nullable ApplyChangesHandler handler) {
    UaMethodNode method = getApplyChangesMethodNode();
    setMethodHandler(
        method.getNodeId(),
        handler == null
            ? null
            : new AbstractMethodInvocationHandler(method) {
              private final MethodArgumentValidator outputValidator =
                  new MethodArgumentValidator(getNodeContext().getServer());

              @Override
              public Argument[] getInputArguments() {
                Argument[] declared = method.getInputArguments();
                return declared != null
                    ? declared
                    : ServerConfigurationTypeApplyChanges.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : ServerConfigurationTypeApplyChanges.outputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              protected int getRequiredInputArgumentCount(Argument[] arguments) {
                return 0;
              }

              @Override
              protected Variant[] invoke(
                  AbstractMethodInvocationHandler.InvocationContext context, Variant[] values)
                  throws UaException {
                handler.applyChanges(context);
                Variant[] encoded = new Variant[0];
                try {
                  outputValidator.validate(getOutputArguments(), encoded);
                } catch (UaException failure) {
                  throw new UaException(StatusCodes.Bad_TypeMismatch, failure);
                }
                return encoded;
              }
            });
  }

  @Override
  public @Nullable UaMethodNode getCancelChangesMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "CancelChanges",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setCancelChangesHandler(
      ServerConfigurationType.@Nullable CancelChangesHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this, getCancelChangesMethodNode(), "http://opcfoundation.org/UA/", "CancelChanges");
    setMethodHandler(
        method.getNodeId(),
        handler == null
            ? null
            : new AbstractMethodInvocationHandler(method) {
              private final MethodArgumentValidator outputValidator =
                  new MethodArgumentValidator(getNodeContext().getServer());

              @Override
              public Argument[] getInputArguments() {
                Argument[] declared = method.getInputArguments();
                return declared != null
                    ? declared
                    : ServerConfigurationTypeCancelChanges.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : ServerConfigurationTypeCancelChanges.outputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              protected int getRequiredInputArgumentCount(Argument[] arguments) {
                return 0;
              }

              @Override
              protected Variant[] invoke(
                  AbstractMethodInvocationHandler.InvocationContext context, Variant[] values)
                  throws UaException {
                handler.cancelChanges(context);
                Variant[] encoded = new Variant[0];
                try {
                  outputValidator.validate(getOutputArguments(), encoded);
                } catch (UaException failure) {
                  throw new UaException(StatusCodes.Bad_TypeMismatch, failure);
                }
                return encoded;
              }
            });
  }

  @Override
  public @Nullable UaMethodNode getCreateSelfSignedCertificateMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "CreateSelfSignedCertificate",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setCreateSelfSignedCertificateHandler(
      ServerConfigurationType.@Nullable CreateSelfSignedCertificateHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this,
            getCreateSelfSignedCertificateMethodNode(),
            "http://opcfoundation.org/UA/",
            "CreateSelfSignedCertificate");
    setMethodHandler(
        method.getNodeId(),
        handler == null
            ? null
            : new AbstractMethodInvocationHandler(method) {
              private final MethodArgumentValidator outputValidator =
                  new MethodArgumentValidator(getNodeContext().getServer());

              @Override
              public Argument[] getInputArguments() {
                Argument[] declared = method.getInputArguments();
                return declared != null
                    ? declared
                    : ServerConfigurationTypeCreateSelfSignedCertificate.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : ServerConfigurationTypeCreateSelfSignedCertificate.outputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              protected int getRequiredInputArgumentCount(Argument[] arguments) {
                return 7;
              }

              @Override
              protected Variant[] invoke(
                  AbstractMethodInvocationHandler.InvocationContext context, Variant[] values)
                  throws UaException {
                ServerConfigurationTypeCreateSelfSignedCertificate.Inputs input;
                try {
                  input =
                      ServerConfigurationTypeCreateSelfSignedCertificate.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                ServerConfigurationTypeCreateSelfSignedCertificate.Outputs output =
                    new ServerConfigurationTypeCreateSelfSignedCertificate.Outputs(
                        handler.createSelfSignedCertificate(
                            context,
                            input.certificateGroupId(),
                            input.certificateTypeId(),
                            input.subjectName(),
                            input.dnsNames(),
                            input.ipAddresses(),
                            input.lifetimeInDays(),
                            input.keySizeInBits()));
                Variant[] encoded =
                    output.toVariants(getNodeContext().getServer().getStaticEncodingContext());
                try {
                  outputValidator.validate(getOutputArguments(), encoded);
                } catch (UaException failure) {
                  throw new UaException(StatusCodes.Bad_TypeMismatch, failure);
                }
                return encoded;
              }
            });
  }

  @Override
  public UaMethodNode getCreateSigningRequestMethodNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        "http://opcfoundation.org/UA/",
        "CreateSigningRequest",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setCreateSigningRequestHandler(
      ServerConfigurationType.@Nullable CreateSigningRequestHandler handler) {
    UaMethodNode method = getCreateSigningRequestMethodNode();
    setMethodHandler(
        method.getNodeId(),
        handler == null
            ? null
            : new AbstractMethodInvocationHandler(method) {
              private final MethodArgumentValidator outputValidator =
                  new MethodArgumentValidator(getNodeContext().getServer());

              @Override
              public Argument[] getInputArguments() {
                Argument[] declared = method.getInputArguments();
                return declared != null
                    ? declared
                    : ServerConfigurationTypeCreateSigningRequest.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : ServerConfigurationTypeCreateSigningRequest.outputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              protected int getRequiredInputArgumentCount(Argument[] arguments) {
                return 5;
              }

              @Override
              protected Variant[] invoke(
                  AbstractMethodInvocationHandler.InvocationContext context, Variant[] values)
                  throws UaException {
                ServerConfigurationTypeCreateSigningRequest.Inputs input;
                try {
                  input =
                      ServerConfigurationTypeCreateSigningRequest.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                ServerConfigurationTypeCreateSigningRequest.Outputs output =
                    new ServerConfigurationTypeCreateSigningRequest.Outputs(
                        handler.createSigningRequest(
                            context,
                            input.certificateGroupId(),
                            input.certificateTypeId(),
                            input.subjectName(),
                            input.regeneratePrivateKey(),
                            input.nonce()));
                Variant[] encoded =
                    output.toVariants(getNodeContext().getServer().getStaticEncodingContext());
                try {
                  outputValidator.validate(getOutputArguments(), encoded);
                } catch (UaException failure) {
                  throw new UaException(StatusCodes.Bad_TypeMismatch, failure);
                }
                return encoded;
              }
            });
  }

  @Override
  public @Nullable UaMethodNode getDeleteCertificateMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "DeleteCertificate",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setDeleteCertificateHandler(
      ServerConfigurationType.@Nullable DeleteCertificateHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this,
            getDeleteCertificateMethodNode(),
            "http://opcfoundation.org/UA/",
            "DeleteCertificate");
    setMethodHandler(
        method.getNodeId(),
        handler == null
            ? null
            : new AbstractMethodInvocationHandler(method) {
              private final MethodArgumentValidator outputValidator =
                  new MethodArgumentValidator(getNodeContext().getServer());

              @Override
              public Argument[] getInputArguments() {
                Argument[] declared = method.getInputArguments();
                return declared != null
                    ? declared
                    : ServerConfigurationTypeDeleteCertificate.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : ServerConfigurationTypeDeleteCertificate.outputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              protected int getRequiredInputArgumentCount(Argument[] arguments) {
                return 2;
              }

              @Override
              protected Variant[] invoke(
                  AbstractMethodInvocationHandler.InvocationContext context, Variant[] values)
                  throws UaException {
                ServerConfigurationTypeDeleteCertificate.Inputs input;
                try {
                  input =
                      ServerConfigurationTypeDeleteCertificate.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                handler.deleteCertificate(
                    context, input.certificateGroupId(), input.certificateTypeId());
                Variant[] encoded = new Variant[0];
                try {
                  outputValidator.validate(getOutputArguments(), encoded);
                } catch (UaException failure) {
                  throw new UaException(StatusCodes.Bad_TypeMismatch, failure);
                }
                return encoded;
              }
            });
  }

  @Override
  public @Nullable UaMethodNode getGetCertificatesMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "GetCertificates",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setGetCertificatesHandler(
      ServerConfigurationType.@Nullable GetCertificatesHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this,
            getGetCertificatesMethodNode(),
            "http://opcfoundation.org/UA/",
            "GetCertificates");
    setMethodHandler(
        method.getNodeId(),
        handler == null
            ? null
            : new AbstractMethodInvocationHandler(method) {
              private final MethodArgumentValidator outputValidator =
                  new MethodArgumentValidator(getNodeContext().getServer());

              @Override
              public Argument[] getInputArguments() {
                Argument[] declared = method.getInputArguments();
                return declared != null
                    ? declared
                    : ServerConfigurationTypeGetCertificates.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : ServerConfigurationTypeGetCertificates.outputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              protected int getRequiredInputArgumentCount(Argument[] arguments) {
                return 1;
              }

              @Override
              protected Variant[] invoke(
                  AbstractMethodInvocationHandler.InvocationContext context, Variant[] values)
                  throws UaException {
                ServerConfigurationTypeGetCertificates.Inputs input;
                try {
                  input =
                      ServerConfigurationTypeGetCertificates.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                ServerConfigurationTypeGetCertificates.Outputs output =
                    Objects.requireNonNull(
                        handler.getCertificates(context, input.certificateGroupId()),
                        "null Method outputs");
                Variant[] encoded =
                    output.toVariants(getNodeContext().getServer().getStaticEncodingContext());
                try {
                  outputValidator.validate(getOutputArguments(), encoded);
                } catch (UaException failure) {
                  throw new UaException(StatusCodes.Bad_TypeMismatch, failure);
                }
                return encoded;
              }
            });
  }

  @Override
  public UaMethodNode getGetRejectedListMethodNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        "http://opcfoundation.org/UA/",
        "GetRejectedList",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setGetRejectedListHandler(
      ServerConfigurationType.@Nullable GetRejectedListHandler handler) {
    UaMethodNode method = getGetRejectedListMethodNode();
    setMethodHandler(
        method.getNodeId(),
        handler == null
            ? null
            : new AbstractMethodInvocationHandler(method) {
              private final MethodArgumentValidator outputValidator =
                  new MethodArgumentValidator(getNodeContext().getServer());

              @Override
              public Argument[] getInputArguments() {
                Argument[] declared = method.getInputArguments();
                return declared != null
                    ? declared
                    : ServerConfigurationTypeGetRejectedList.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : ServerConfigurationTypeGetRejectedList.outputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              protected int getRequiredInputArgumentCount(Argument[] arguments) {
                return 0;
              }

              @Override
              protected Variant[] invoke(
                  AbstractMethodInvocationHandler.InvocationContext context, Variant[] values)
                  throws UaException {
                ServerConfigurationTypeGetRejectedList.Outputs output =
                    new ServerConfigurationTypeGetRejectedList.Outputs(
                        handler.getRejectedList(context));
                Variant[] encoded =
                    output.toVariants(getNodeContext().getServer().getStaticEncodingContext());
                try {
                  outputValidator.validate(getOutputArguments(), encoded);
                } catch (UaException failure) {
                  throw new UaException(StatusCodes.Bad_TypeMismatch, failure);
                }
                return encoded;
              }
            });
  }

  @Override
  public @Nullable UaMethodNode getResetToServerDefaultsMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "ResetToServerDefaults",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setResetToServerDefaultsHandler(
      ServerConfigurationType.@Nullable ResetToServerDefaultsHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this,
            getResetToServerDefaultsMethodNode(),
            "http://opcfoundation.org/UA/",
            "ResetToServerDefaults");
    setMethodHandler(
        method.getNodeId(),
        handler == null
            ? null
            : new AbstractMethodInvocationHandler(method) {
              private final MethodArgumentValidator outputValidator =
                  new MethodArgumentValidator(getNodeContext().getServer());

              @Override
              public Argument[] getInputArguments() {
                Argument[] declared = method.getInputArguments();
                return declared != null
                    ? declared
                    : ServerConfigurationTypeResetToServerDefaults.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : ServerConfigurationTypeResetToServerDefaults.outputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              protected int getRequiredInputArgumentCount(Argument[] arguments) {
                return 0;
              }

              @Override
              protected Variant[] invoke(
                  AbstractMethodInvocationHandler.InvocationContext context, Variant[] values)
                  throws UaException {
                handler.resetToServerDefaults(context);
                Variant[] encoded = new Variant[0];
                try {
                  outputValidator.validate(getOutputArguments(), encoded);
                } catch (UaException failure) {
                  throw new UaException(StatusCodes.Bad_TypeMismatch, failure);
                }
                return encoded;
              }
            });
  }

  @Override
  public UaMethodNode getUpdateCertificateMethodNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        "http://opcfoundation.org/UA/",
        "UpdateCertificate",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setUpdateCertificateHandler(
      ServerConfigurationType.@Nullable UpdateCertificateHandler handler) {
    UaMethodNode method = getUpdateCertificateMethodNode();
    setMethodHandler(
        method.getNodeId(),
        handler == null
            ? null
            : new AbstractMethodInvocationHandler(method) {
              private final MethodArgumentValidator outputValidator =
                  new MethodArgumentValidator(getNodeContext().getServer());

              @Override
              public Argument[] getInputArguments() {
                Argument[] declared = method.getInputArguments();
                return declared != null
                    ? declared
                    : ServerConfigurationTypeUpdateCertificate.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : ServerConfigurationTypeUpdateCertificate.outputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              protected int getRequiredInputArgumentCount(Argument[] arguments) {
                return 6;
              }

              @Override
              protected Variant[] invoke(
                  AbstractMethodInvocationHandler.InvocationContext context, Variant[] values)
                  throws UaException {
                ServerConfigurationTypeUpdateCertificate.Inputs input;
                try {
                  input =
                      ServerConfigurationTypeUpdateCertificate.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                ServerConfigurationTypeUpdateCertificate.Outputs output =
                    new ServerConfigurationTypeUpdateCertificate.Outputs(
                        handler.updateCertificate(
                            context,
                            input.certificateGroupId(),
                            input.certificateTypeId(),
                            input.certificate(),
                            input.issuerCertificates(),
                            input.privateKeyFormat(),
                            input.privateKey()));
                Variant[] encoded =
                    output.toVariants(getNodeContext().getServer().getStaticEncodingContext());
                try {
                  outputValidator.validate(getOutputArguments(), encoded);
                } catch (UaException failure) {
                  throw new UaException(StatusCodes.Bad_TypeMismatch, failure);
                }
                return encoded;
              }
            });
  }

  @Override
  public void setMethods(ServerConfigurationType.@Nullable Methods methods) {
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
