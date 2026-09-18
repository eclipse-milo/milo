package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubKeyPushTargetTypeConnectSecurityGroups;
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubKeyPushTargetTypeDisconnectSecurityGroups;
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubKeyPushTargetTypeTriggerKeyUpdate;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link PubSubKeyPushTargetType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.1">Model
 *     documentation</a>
 */
public class PubSubKeyPushTargetTypeNode extends BaseObjectTypeNode
    implements PubSubKeyPushTargetType {
  public PubSubKeyPushTargetTypeNode(
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

  public PubSubKeyPushTargetTypeNode(
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
  public PropertyTypeNode getApplicationUriNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ApplicationUri",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getApplicationUri() {
    return ServerNodeSupport.read(this, getApplicationUriNode(), String.class, null);
  }

  @Override
  public void setApplicationUri(@Nullable String value) {
    ServerNodeSupport.write(this, getApplicationUriNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getEndpointUrlNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "EndpointUrl",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getEndpointUrl() {
    return ServerNodeSupport.read(this, getEndpointUrlNode(), String.class, null);
  }

  @Override
  public void setEndpointUrl(@Nullable String value) {
    ServerNodeSupport.write(this, getEndpointUrlNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getLastPushErrorTimeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "LastPushErrorTime",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 13L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable DateTime getLastPushErrorTime() {
    return ServerNodeSupport.read(this, getLastPushErrorTimeNode(), DateTime.class, null);
  }

  @Override
  public void setLastPushErrorTime(@Nullable DateTime value) {
    ServerNodeSupport.write(this, getLastPushErrorTimeNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getLastPushExecutionTimeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "LastPushExecutionTime",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 13L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable DateTime getLastPushExecutionTime() {
    return ServerNodeSupport.read(this, getLastPushExecutionTimeNode(), DateTime.class, null);
  }

  @Override
  public void setLastPushExecutionTime(@Nullable DateTime value) {
    ServerNodeSupport.write(this, getLastPushExecutionTimeNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getRequestedKeyCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "RequestedKeyCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UShort getRequestedKeyCount() {
    return ServerNodeSupport.read(this, getRequestedKeyCountNode(), UShort.class, null);
  }

  @Override
  public void setRequestedKeyCount(@Nullable UShort value) {
    ServerNodeSupport.write(this, getRequestedKeyCountNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getRetryIntervalNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "RetryInterval",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getRetryInterval() {
    return ServerNodeSupport.read(this, getRetryIntervalNode(), Double.class, null);
  }

  @Override
  public void setRetryInterval(@Nullable Double value) {
    ServerNodeSupport.write(this, getRetryIntervalNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getSecurityPolicyUriNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SecurityPolicyUri",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getSecurityPolicyUri() {
    return ServerNodeSupport.read(this, getSecurityPolicyUriNode(), String.class, null);
  }

  @Override
  public void setSecurityPolicyUri(@Nullable String value) {
    ServerNodeSupport.write(this, getSecurityPolicyUriNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getUserTokenTypeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "UserTokenType",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 304L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UserTokenPolicy getUserTokenType() {
    return ServerNodeSupport.read(this, getUserTokenTypeNode(), UserTokenPolicy.class, null);
  }

  @Override
  public void setUserTokenType(@Nullable UserTokenPolicy value) {
    ServerNodeSupport.write(this, getUserTokenTypeNode(), value, false, false, true);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getApplicationUriNode();
    getEndpointUrlNode();
    getLastPushErrorTimeNode();
    getLastPushExecutionTimeNode();
    getRequestedKeyCountNode();
    getRetryIntervalNode();
    getSecurityPolicyUriNode();
    getUserTokenTypeNode();
  }

  @Override
  public UaMethodNode getConnectSecurityGroupsMethodNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        "http://opcfoundation.org/UA/",
        "ConnectSecurityGroups",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setConnectSecurityGroupsHandler(
      PubSubKeyPushTargetType.@Nullable ConnectSecurityGroupsHandler handler) {
    UaMethodNode method = getConnectSecurityGroupsMethodNode();
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
                    : PubSubKeyPushTargetTypeConnectSecurityGroups.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : PubSubKeyPushTargetTypeConnectSecurityGroups.outputArguments(
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
                PubSubKeyPushTargetTypeConnectSecurityGroups.Inputs input;
                try {
                  input =
                      PubSubKeyPushTargetTypeConnectSecurityGroups.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                PubSubKeyPushTargetTypeConnectSecurityGroups.Outputs output =
                    new PubSubKeyPushTargetTypeConnectSecurityGroups.Outputs(
                        handler.connectSecurityGroups(context, input.securityGroupIds()));
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
  public UaMethodNode getDisconnectSecurityGroupsMethodNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        "http://opcfoundation.org/UA/",
        "DisconnectSecurityGroups",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setDisconnectSecurityGroupsHandler(
      PubSubKeyPushTargetType.@Nullable DisconnectSecurityGroupsHandler handler) {
    UaMethodNode method = getDisconnectSecurityGroupsMethodNode();
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
                    : PubSubKeyPushTargetTypeDisconnectSecurityGroups.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : PubSubKeyPushTargetTypeDisconnectSecurityGroups.outputArguments(
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
                PubSubKeyPushTargetTypeDisconnectSecurityGroups.Inputs input;
                try {
                  input =
                      PubSubKeyPushTargetTypeDisconnectSecurityGroups.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                PubSubKeyPushTargetTypeDisconnectSecurityGroups.Outputs output =
                    new PubSubKeyPushTargetTypeDisconnectSecurityGroups.Outputs(
                        handler.disconnectSecurityGroups(context, input.securityGroupIds()));
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
  public UaMethodNode getTriggerKeyUpdateMethodNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        "http://opcfoundation.org/UA/",
        "TriggerKeyUpdate",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setTriggerKeyUpdateHandler(
      PubSubKeyPushTargetType.@Nullable TriggerKeyUpdateHandler handler) {
    UaMethodNode method = getTriggerKeyUpdateMethodNode();
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
                    : PubSubKeyPushTargetTypeTriggerKeyUpdate.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : PubSubKeyPushTargetTypeTriggerKeyUpdate.outputArguments(
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
                handler.triggerKeyUpdate(context);
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
  public void setMethods(PubSubKeyPushTargetType.@Nullable Methods methods) {
    setConnectSecurityGroupsHandler(methods == null ? null : methods::connectSecurityGroups);
    setDisconnectSecurityGroupsHandler(methods == null ? null : methods::disconnectSecurityGroups);
    setTriggerKeyUpdateHandler(methods == null ? null : methods::triggerKeyUpdate);
  }
}
