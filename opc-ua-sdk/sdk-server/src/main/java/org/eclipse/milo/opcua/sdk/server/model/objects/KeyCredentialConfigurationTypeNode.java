package org.eclipse.milo.opcua.sdk.server.model.objects;

import java.util.Objects;
import org.eclipse.milo.opcua.sdk.core.model.methods.KeyCredentialConfigurationTypeDeleteCredential;
import org.eclipse.milo.opcua.sdk.core.model.methods.KeyCredentialConfigurationTypeGetEncryptingKey;
import org.eclipse.milo.opcua.sdk.core.model.methods.KeyCredentialConfigurationTypeUpdateCredential;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link KeyCredentialConfigurationType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.5">Model
 *     documentation</a>
 */
public class KeyCredentialConfigurationTypeNode extends BaseObjectTypeNode
    implements KeyCredentialConfigurationType {
  public KeyCredentialConfigurationTypeNode(
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

  public KeyCredentialConfigurationTypeNode(
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
  public @Nullable PropertyTypeNode getCredentialIdNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "CredentialId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getCredentialId() {
    return ServerNodeSupport.read(this, getCredentialIdNode(), String.class, null);
  }

  @Override
  public void setCredentialId(@Nullable String value) {
    ServerNodeSupport.write(
        this, getCredentialIdNode(), Namespaces.OPC_UA, "CredentialId", value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getEndpointUrlsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "EndpointUrls",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String @Nullable [] getEndpointUrls() {
    return ServerNodeSupport.readArray(this, getEndpointUrlsNode(), String.class, null);
  }

  @Override
  public void setEndpointUrls(@Nullable String @Nullable [] value) {
    ServerNodeSupport.write(
        this, getEndpointUrlsNode(), Namespaces.OPC_UA, "EndpointUrls", value, true, false, false);
  }

  @Override
  public PropertyTypeNode getProfileUriNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ProfileUri",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getProfileUri() {
    return ServerNodeSupport.read(this, getProfileUriNode(), String.class, null);
  }

  @Override
  public void setProfileUri(@Nullable String value) {
    ServerNodeSupport.write(this, getProfileUriNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getResourceUriNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ResourceUri",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getResourceUri() {
    return ServerNodeSupport.read(this, getResourceUriNode(), String.class, null);
  }

  @Override
  public void setResourceUri(@Nullable String value) {
    ServerNodeSupport.write(this, getResourceUriNode(), value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getServiceStatusNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ServiceStatus",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 19L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable StatusCode getServiceStatus() {
    return ServerNodeSupport.read(this, getServiceStatusNode(), StatusCode.class, null);
  }

  @Override
  public void setServiceStatus(@Nullable StatusCode value) {
    ServerNodeSupport.write(
        this,
        getServiceStatusNode(),
        Namespaces.OPC_UA,
        "ServiceStatus",
        value,
        false,
        false,
        false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getCredentialIdNode();
    getEndpointUrlsNode();
    getProfileUriNode();
    getResourceUriNode();
    getServiceStatusNode();
  }

  @Override
  public @Nullable UaMethodNode getDeleteCredentialMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "DeleteCredential",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setDeleteCredentialHandler(
      KeyCredentialConfigurationType.@Nullable DeleteCredentialHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this,
            getDeleteCredentialMethodNode(),
            "http://opcfoundation.org/UA/",
            "DeleteCredential");
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
                    : KeyCredentialConfigurationTypeDeleteCredential.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : KeyCredentialConfigurationTypeDeleteCredential.outputArguments(
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
                handler.deleteCredential(context);
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
  public @Nullable UaMethodNode getGetEncryptingKeyMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "GetEncryptingKey",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setGetEncryptingKeyHandler(
      KeyCredentialConfigurationType.@Nullable GetEncryptingKeyHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this,
            getGetEncryptingKeyMethodNode(),
            "http://opcfoundation.org/UA/",
            "GetEncryptingKey");
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
                    : KeyCredentialConfigurationTypeGetEncryptingKey.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : KeyCredentialConfigurationTypeGetEncryptingKey.outputArguments(
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
                KeyCredentialConfigurationTypeGetEncryptingKey.Inputs input;
                try {
                  input =
                      KeyCredentialConfigurationTypeGetEncryptingKey.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                KeyCredentialConfigurationTypeGetEncryptingKey.Outputs output =
                    Objects.requireNonNull(
                        handler.getEncryptingKey(
                            context, input.credentialId(), input.requestedSecurityPolicyUri()),
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
  public @Nullable UaMethodNode getUpdateCredentialMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "UpdateCredential",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setUpdateCredentialHandler(
      KeyCredentialConfigurationType.@Nullable UpdateCredentialHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this,
            getUpdateCredentialMethodNode(),
            "http://opcfoundation.org/UA/",
            "UpdateCredential");
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
                    : KeyCredentialConfigurationTypeUpdateCredential.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : KeyCredentialConfigurationTypeUpdateCredential.outputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              protected int getRequiredInputArgumentCount(Argument[] arguments) {
                return 4;
              }

              @Override
              protected Variant[] invoke(
                  AbstractMethodInvocationHandler.InvocationContext context, Variant[] values)
                  throws UaException {
                KeyCredentialConfigurationTypeUpdateCredential.Inputs input;
                try {
                  input =
                      KeyCredentialConfigurationTypeUpdateCredential.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                handler.updateCredential(
                    context,
                    input.credentialId(),
                    input.credentialSecret(),
                    input.certificateThumbprint(),
                    input.securityPolicyUri());
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
  public void setMethods(KeyCredentialConfigurationType.@Nullable Methods methods) {
    if (getDeleteCredentialMethodNode() != null) {
      setDeleteCredentialHandler(methods == null ? null : methods::deleteCredential);
    }
    if (getGetEncryptingKeyMethodNode() != null) {
      setGetEncryptingKeyHandler(methods == null ? null : methods::getEncryptingKey);
    }
    if (getUpdateCredentialMethodNode() != null) {
      setUpdateCredentialHandler(methods == null ? null : methods::updateCredential);
    }
  }
}
