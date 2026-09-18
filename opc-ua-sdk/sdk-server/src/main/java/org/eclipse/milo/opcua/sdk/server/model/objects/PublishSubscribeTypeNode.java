package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.model.methods.PublishSubscribeTypeAddConnection;
import org.eclipse.milo.opcua.sdk.core.model.methods.PublishSubscribeTypeRemoveConnection;
import org.eclipse.milo.opcua.sdk.core.model.methods.PublishSubscribeTypeSetSecurityKeys;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link PublishSubscribeType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.2">Model
 *     documentation</a>
 */
public class PublishSubscribeTypeNode extends PubSubKeyServiceTypeNode
    implements PublishSubscribeType {
  public PublishSubscribeTypeNode(
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

  public PublishSubscribeTypeNode(
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
  public @Nullable PropertyTypeNode getConfigurationPropertiesNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ConfigurationProperties",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 14533L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable KeyValuePair @Nullable [] getConfigurationProperties() {
    return ServerNodeSupport.readArray(
        this, getConfigurationPropertiesNode(), KeyValuePair.class, null);
  }

  @Override
  public void setConfigurationProperties(@Nullable KeyValuePair @Nullable [] value) {
    ServerNodeSupport.write(
        this,
        getConfigurationPropertiesNode(),
        Namespaces.OPC_UA,
        "ConfigurationProperties",
        value,
        true,
        false,
        true);
  }

  @Override
  public @Nullable PropertyTypeNode getConfigurationVersionNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ConfigurationVersion",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 20998L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getConfigurationVersion() {
    return ServerNodeSupport.read(this, getConfigurationVersionNode(), UInteger.class, null);
  }

  @Override
  public void setConfigurationVersion(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getConfigurationVersionNode(),
        Namespaces.OPC_UA,
        "ConfigurationVersion",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable FolderTypeNode getDataSetClassesNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "DataSetClasses",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 61L),
        null,
        -1,
        FolderTypeNode.class);
  }

  @Override
  public @Nullable PropertyTypeNode getDefaultDatagramPublisherIdNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "DefaultDatagramPublisherId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 9L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable ULong getDefaultDatagramPublisherId() {
    return ServerNodeSupport.read(this, getDefaultDatagramPublisherIdNode(), ULong.class, null);
  }

  @Override
  public void setDefaultDatagramPublisherId(@Nullable ULong value) {
    ServerNodeSupport.write(
        this,
        getDefaultDatagramPublisherIdNode(),
        Namespaces.OPC_UA,
        "DefaultDatagramPublisherId",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getDefaultSecurityKeyServicesNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "DefaultSecurityKeyServices",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 312L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable EndpointDescription @Nullable [] getDefaultSecurityKeyServices() {
    return ServerNodeSupport.readArray(
        this, getDefaultSecurityKeyServicesNode(), EndpointDescription.class, null);
  }

  @Override
  public void setDefaultSecurityKeyServices(@Nullable EndpointDescription @Nullable [] value) {
    ServerNodeSupport.write(
        this,
        getDefaultSecurityKeyServicesNode(),
        Namespaces.OPC_UA,
        "DefaultSecurityKeyServices",
        value,
        true,
        false,
        true);
  }

  @Override
  public @Nullable PubSubDiagnosticsRootTypeNode getDiagnosticsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "Diagnostics",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 19732L),
        null,
        -1,
        PubSubDiagnosticsRootTypeNode.class);
  }

  @Override
  public @Nullable PubSubCapabilitiesTypeNode getPubSubCapablitiesNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "PubSubCapablities",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 23832L),
        null,
        -1,
        PubSubCapabilitiesTypeNode.class);
  }

  @Override
  public @Nullable PubSubConfigurationTypeNode getPubSubConfigurationNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "PubSubConfiguration",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 25482L),
        null,
        -1,
        PubSubConfigurationTypeNode.class);
  }

  @Override
  public DataSetFolderTypeNode getPublishedDataSetsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "PublishedDataSets",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 14477L),
        null,
        -1,
        DataSetFolderTypeNode.class);
  }

  @Override
  public PubSubStatusTypeNode getStatusNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Status",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 14643L),
        null,
        -1,
        PubSubStatusTypeNode.class);
  }

  @Override
  public @Nullable SubscribedDataSetFolderTypeNode getSubscribedDataSetsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "SubscribedDataSets",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 23795L),
        null,
        -1,
        SubscribedDataSetFolderTypeNode.class);
  }

  @Override
  public PropertyTypeNode getSupportedTransportProfilesNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SupportedTransportProfiles",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String @Nullable [] getSupportedTransportProfiles() {
    return ServerNodeSupport.readArray(
        this, getSupportedTransportProfilesNode(), String.class, null);
  }

  @Override
  public void setSupportedTransportProfiles(@Nullable String @Nullable [] value) {
    ServerNodeSupport.write(this, getSupportedTransportProfilesNode(), value, true, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getConfigurationPropertiesNode();
    getConfigurationVersionNode();
    getDataSetClassesNode();
    getDefaultDatagramPublisherIdNode();
    getDefaultSecurityKeyServicesNode();
    getDiagnosticsNode();
    getPubSubCapablitiesNode();
    getPubSubConfigurationNode();
    getPublishedDataSetsNode();
    getStatusNode();
    getSubscribedDataSetsNode();
    getSupportedTransportProfilesNode();
  }

  @Override
  public @Nullable UaMethodNode getAddConnectionMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "AddConnection",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setAddConnectionHandler(PublishSubscribeType.@Nullable AddConnectionHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this, getAddConnectionMethodNode(), "http://opcfoundation.org/UA/", "AddConnection");
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
                    : PublishSubscribeTypeAddConnection.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : PublishSubscribeTypeAddConnection.outputArguments(
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
                PublishSubscribeTypeAddConnection.Inputs input;
                try {
                  input =
                      PublishSubscribeTypeAddConnection.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                PublishSubscribeTypeAddConnection.Outputs output =
                    new PublishSubscribeTypeAddConnection.Outputs(
                        handler.addConnection(context, input.configuration()));
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
  public @Nullable UaMethodNode getRemoveConnectionMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "RemoveConnection",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setRemoveConnectionHandler(
      PublishSubscribeType.@Nullable RemoveConnectionHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this,
            getRemoveConnectionMethodNode(),
            "http://opcfoundation.org/UA/",
            "RemoveConnection");
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
                    : PublishSubscribeTypeRemoveConnection.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : PublishSubscribeTypeRemoveConnection.outputArguments(
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
                PublishSubscribeTypeRemoveConnection.Inputs input;
                try {
                  input =
                      PublishSubscribeTypeRemoveConnection.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                handler.removeConnection(context, input.connectionId());
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
  public @Nullable UaMethodNode getSetSecurityKeysMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "SetSecurityKeys",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setSetSecurityKeysHandler(
      PublishSubscribeType.@Nullable SetSecurityKeysHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this,
            getSetSecurityKeysMethodNode(),
            "http://opcfoundation.org/UA/",
            "SetSecurityKeys");
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
                    : PublishSubscribeTypeSetSecurityKeys.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : PublishSubscribeTypeSetSecurityKeys.outputArguments(
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
                PublishSubscribeTypeSetSecurityKeys.Inputs input;
                try {
                  input =
                      PublishSubscribeTypeSetSecurityKeys.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                handler.setSecurityKeys(
                    context,
                    input.securityGroupId(),
                    input.securityPolicyUri(),
                    input.currentTokenId(),
                    input.currentKey(),
                    input.futureKeys(),
                    input.timeToNextKey(),
                    input.keyLifetime());
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
  public void setMethods(PublishSubscribeType.@Nullable Methods methods) {
    if (getAddConnectionMethodNode() != null) {
      setAddConnectionHandler(methods == null ? null : methods::addConnection);
    }
    if (getGetSecurityGroupMethodNode() != null) {
      setGetSecurityGroupHandler(methods == null ? null : methods::getSecurityGroup);
    }
    if (getGetSecurityKeysMethodNode() != null) {
      setGetSecurityKeysHandler(methods == null ? null : methods::getSecurityKeys);
    }
    if (getRemoveConnectionMethodNode() != null) {
      setRemoveConnectionHandler(methods == null ? null : methods::removeConnection);
    }
    if (getSetSecurityKeysMethodNode() != null) {
      setSetSecurityKeysHandler(methods == null ? null : methods::setSecurityKeys);
    }
  }
}
