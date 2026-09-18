package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.model.methods.RoleTypeAddApplication;
import org.eclipse.milo.opcua.sdk.core.model.methods.RoleTypeAddEndpoint;
import org.eclipse.milo.opcua.sdk.core.model.methods.RoleTypeAddIdentity;
import org.eclipse.milo.opcua.sdk.core.model.methods.RoleTypeRemoveApplication;
import org.eclipse.milo.opcua.sdk.core.model.methods.RoleTypeRemoveEndpoint;
import org.eclipse.milo.opcua.sdk.core.model.methods.RoleTypeRemoveIdentity;
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
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointType;
import org.eclipse.milo.opcua.stack.core.types.structured.IdentityMappingRuleType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link RoleType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.1">Model
 *     documentation</a>
 */
public class RoleTypeNode extends BaseObjectTypeNode implements RoleType {
  public RoleTypeNode(
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

  public RoleTypeNode(
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
  public @Nullable PropertyTypeNode getApplicationsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "Applications",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String @Nullable [] getApplications() {
    return ServerNodeSupport.readArray(this, getApplicationsNode(), String.class, null);
  }

  @Override
  public void setApplications(@Nullable String @Nullable [] value) {
    ServerNodeSupport.write(
        this, getApplicationsNode(), Namespaces.OPC_UA, "Applications", value, true, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getApplicationsExcludeNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ApplicationsExclude",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getApplicationsExclude() {
    return ServerNodeSupport.read(this, getApplicationsExcludeNode(), Boolean.class, null);
  }

  @Override
  public void setApplicationsExclude(@Nullable Boolean value) {
    ServerNodeSupport.write(
        this,
        getApplicationsExcludeNode(),
        Namespaces.OPC_UA,
        "ApplicationsExclude",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getCustomConfigurationNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "CustomConfiguration",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getCustomConfiguration() {
    return ServerNodeSupport.read(this, getCustomConfigurationNode(), Boolean.class, null);
  }

  @Override
  public void setCustomConfiguration(@Nullable Boolean value) {
    ServerNodeSupport.write(
        this,
        getCustomConfigurationNode(),
        Namespaces.OPC_UA,
        "CustomConfiguration",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getEndpointsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "Endpoints",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 15528L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable EndpointType @Nullable [] getEndpoints() {
    return ServerNodeSupport.readArray(this, getEndpointsNode(), EndpointType.class, null);
  }

  @Override
  public void setEndpoints(@Nullable EndpointType @Nullable [] value) {
    ServerNodeSupport.write(
        this, getEndpointsNode(), Namespaces.OPC_UA, "Endpoints", value, true, false, true);
  }

  @Override
  public @Nullable PropertyTypeNode getEndpointsExcludeNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "EndpointsExclude",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getEndpointsExclude() {
    return ServerNodeSupport.read(this, getEndpointsExcludeNode(), Boolean.class, null);
  }

  @Override
  public void setEndpointsExclude(@Nullable Boolean value) {
    ServerNodeSupport.write(
        this,
        getEndpointsExcludeNode(),
        Namespaces.OPC_UA,
        "EndpointsExclude",
        value,
        false,
        false,
        false);
  }

  @Override
  public PropertyTypeNode getIdentitiesNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Identities",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 15634L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable IdentityMappingRuleType @Nullable [] getIdentities() {
    return ServerNodeSupport.readArray(
        this, getIdentitiesNode(), IdentityMappingRuleType.class, null);
  }

  @Override
  public void setIdentities(@Nullable IdentityMappingRuleType @Nullable [] value) {
    ServerNodeSupport.write(this, getIdentitiesNode(), value, true, false, true);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getApplicationsNode();
    getApplicationsExcludeNode();
    getCustomConfigurationNode();
    getEndpointsNode();
    getEndpointsExcludeNode();
    getIdentitiesNode();
  }

  @Override
  public @Nullable UaMethodNode getAddApplicationMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "AddApplication",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setAddApplicationHandler(RoleType.@Nullable AddApplicationHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this, getAddApplicationMethodNode(), "http://opcfoundation.org/UA/", "AddApplication");
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
                    : RoleTypeAddApplication.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : RoleTypeAddApplication.outputArguments(
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
                RoleTypeAddApplication.Inputs input;
                try {
                  input =
                      RoleTypeAddApplication.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                handler.addApplication(context, input.applicationUri());
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
  public @Nullable UaMethodNode getAddEndpointMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "AddEndpoint",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setAddEndpointHandler(RoleType.@Nullable AddEndpointHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this, getAddEndpointMethodNode(), "http://opcfoundation.org/UA/", "AddEndpoint");
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
                    : RoleTypeAddEndpoint.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : RoleTypeAddEndpoint.outputArguments(
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
                RoleTypeAddEndpoint.Inputs input;
                try {
                  input =
                      RoleTypeAddEndpoint.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                handler.addEndpoint(context, input.endpoint());
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
  public @Nullable UaMethodNode getAddIdentityMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "AddIdentity",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setAddIdentityHandler(RoleType.@Nullable AddIdentityHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this, getAddIdentityMethodNode(), "http://opcfoundation.org/UA/", "AddIdentity");
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
                    : RoleTypeAddIdentity.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : RoleTypeAddIdentity.outputArguments(
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
                RoleTypeAddIdentity.Inputs input;
                try {
                  input =
                      RoleTypeAddIdentity.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                handler.addIdentity(context, input.rule());
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
  public @Nullable UaMethodNode getRemoveApplicationMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "RemoveApplication",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setRemoveApplicationHandler(RoleType.@Nullable RemoveApplicationHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this,
            getRemoveApplicationMethodNode(),
            "http://opcfoundation.org/UA/",
            "RemoveApplication");
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
                    : RoleTypeRemoveApplication.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : RoleTypeRemoveApplication.outputArguments(
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
                RoleTypeRemoveApplication.Inputs input;
                try {
                  input =
                      RoleTypeRemoveApplication.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                handler.removeApplication(context, input.applicationUri());
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
  public @Nullable UaMethodNode getRemoveEndpointMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "RemoveEndpoint",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setRemoveEndpointHandler(RoleType.@Nullable RemoveEndpointHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this, getRemoveEndpointMethodNode(), "http://opcfoundation.org/UA/", "RemoveEndpoint");
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
                    : RoleTypeRemoveEndpoint.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : RoleTypeRemoveEndpoint.outputArguments(
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
                RoleTypeRemoveEndpoint.Inputs input;
                try {
                  input =
                      RoleTypeRemoveEndpoint.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                handler.removeEndpoint(context, input.endpoint());
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
  public @Nullable UaMethodNode getRemoveIdentityMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "RemoveIdentity",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setRemoveIdentityHandler(RoleType.@Nullable RemoveIdentityHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this, getRemoveIdentityMethodNode(), "http://opcfoundation.org/UA/", "RemoveIdentity");
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
                    : RoleTypeRemoveIdentity.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : RoleTypeRemoveIdentity.outputArguments(
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
                RoleTypeRemoveIdentity.Inputs input;
                try {
                  input =
                      RoleTypeRemoveIdentity.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                handler.removeIdentity(context, input.rule());
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
  public void setMethods(RoleType.@Nullable Methods methods) {
    if (getAddApplicationMethodNode() != null) {
      setAddApplicationHandler(methods == null ? null : methods::addApplication);
    }
    if (getAddEndpointMethodNode() != null) {
      setAddEndpointHandler(methods == null ? null : methods::addEndpoint);
    }
    if (getAddIdentityMethodNode() != null) {
      setAddIdentityHandler(methods == null ? null : methods::addIdentity);
    }
    if (getRemoveApplicationMethodNode() != null) {
      setRemoveApplicationHandler(methods == null ? null : methods::removeApplication);
    }
    if (getRemoveEndpointMethodNode() != null) {
      setRemoveEndpointHandler(methods == null ? null : methods::removeEndpoint);
    }
    if (getRemoveIdentityMethodNode() != null) {
      setRemoveIdentityHandler(methods == null ? null : methods::removeIdentity);
    }
  }
}
