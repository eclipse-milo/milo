package org.eclipse.milo.opcua.sdk.server.model.objects;

import java.util.Objects;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerTypeGetMonitoredItems;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerTypeRequestServerStateChange;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerTypeResendData;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerTypeSetSubscriptionDurable;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.InvalidArgumentException;
import org.eclipse.milo.opcua.sdk.server.methods.MethodArgumentValidator;
import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ServerStatusTypeNode;
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
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerStatusDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.TimeZoneDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ServerType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.1">Model
 *     documentation</a>
 */
public class ServerTypeNode extends BaseObjectTypeNode implements ServerType {
  public ServerTypeNode(
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

  public ServerTypeNode(
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
  public PropertyTypeNode getAuditingNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Auditing",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getAuditing() {
    return ServerNodeSupport.read(this, getAuditingNode(), Boolean.class, null);
  }

  @Override
  public void setAuditing(@Nullable Boolean value) {
    ServerNodeSupport.write(this, getAuditingNode(), value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getEstimatedReturnTimeNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "EstimatedReturnTime",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 13L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable DateTime getEstimatedReturnTime() {
    return ServerNodeSupport.read(this, getEstimatedReturnTimeNode(), DateTime.class, null);
  }

  @Override
  public void setEstimatedReturnTime(@Nullable DateTime value) {
    ServerNodeSupport.write(
        this,
        getEstimatedReturnTimeNode(),
        Namespaces.OPC_UA,
        "EstimatedReturnTime",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getLocalTimeNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "LocalTime",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 8912L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable TimeZoneDataType getLocalTime() {
    return ServerNodeSupport.read(this, getLocalTimeNode(), TimeZoneDataType.class, null);
  }

  @Override
  public void setLocalTime(@Nullable TimeZoneDataType value) {
    ServerNodeSupport.write(
        this, getLocalTimeNode(), Namespaces.OPC_UA, "LocalTime", value, false, false, true);
  }

  @Override
  public PropertyTypeNode getNamespaceArrayNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "NamespaceArray",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String @Nullable [] getNamespaceArray() {
    return ServerNodeSupport.readArray(this, getNamespaceArrayNode(), String.class, null);
  }

  @Override
  public void setNamespaceArray(@Nullable String @Nullable [] value) {
    ServerNodeSupport.write(this, getNamespaceArrayNode(), value, true, false, false);
  }

  @Override
  public @Nullable NamespacesTypeNode getNamespacesNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "Namespaces",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 11645L),
        null,
        -1,
        NamespacesTypeNode.class);
  }

  @Override
  public PropertyTypeNode getServerArrayNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ServerArray",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String @Nullable [] getServerArray() {
    return ServerNodeSupport.readArray(this, getServerArrayNode(), String.class, null);
  }

  @Override
  public void setServerArray(@Nullable String @Nullable [] value) {
    ServerNodeSupport.write(this, getServerArrayNode(), value, true, false, false);
  }

  @Override
  public ServerCapabilitiesTypeNode getServerCapabilitiesNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ServerCapabilities",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 2013L),
        null,
        -1,
        ServerCapabilitiesTypeNode.class);
  }

  @Override
  public ServerDiagnosticsTypeNode getServerDiagnosticsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ServerDiagnostics",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 2020L),
        null,
        -1,
        ServerDiagnosticsTypeNode.class);
  }

  @Override
  public ServerRedundancyTypeNode getServerRedundancyNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ServerRedundancy",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 2034L),
        null,
        -1,
        ServerRedundancyTypeNode.class);
  }

  @Override
  public ServerStatusTypeNode getServerStatusNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ServerStatus",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 2138L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 862L),
        -1,
        ServerStatusTypeNode.class);
  }

  @Override
  public @Nullable ServerStatusDataType getServerStatus() {
    return ServerNodeSupport.read(this, getServerStatusNode(), ServerStatusDataType.class, null);
  }

  @Override
  public void setServerStatus(@Nullable ServerStatusDataType value) {
    ServerNodeSupport.write(this, getServerStatusNode(), value, false, false, true);
  }

  @Override
  public PropertyTypeNode getServiceLevelNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ServiceLevel",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 3L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UByte getServiceLevel() {
    return ServerNodeSupport.read(this, getServiceLevelNode(), UByte.class, null);
  }

  @Override
  public void setServiceLevel(@Nullable UByte value) {
    ServerNodeSupport.write(this, getServiceLevelNode(), value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getUrisVersionNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "UrisVersion",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 20998L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getUrisVersion() {
    return ServerNodeSupport.read(this, getUrisVersionNode(), UInteger.class, null);
  }

  @Override
  public void setUrisVersion(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this, getUrisVersionNode(), Namespaces.OPC_UA, "UrisVersion", value, false, false, false);
  }

  @Override
  public VendorServerInfoTypeNode getVendorServerInfoNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "VendorServerInfo",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 2033L),
        null,
        -1,
        VendorServerInfoTypeNode.class);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getAuditingNode();
    getEstimatedReturnTimeNode();
    getLocalTimeNode();
    getNamespaceArrayNode();
    getNamespacesNode();
    getServerArrayNode();
    getServerCapabilitiesNode();
    getServerDiagnosticsNode();
    getServerRedundancyNode();
    getServerStatusNode();
    getServiceLevelNode();
    getUrisVersionNode();
    getVendorServerInfoNode();
  }

  @Override
  public @Nullable UaMethodNode getGetMonitoredItemsMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "GetMonitoredItems",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setGetMonitoredItemsHandler(ServerType.@Nullable GetMonitoredItemsHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this,
            getGetMonitoredItemsMethodNode(),
            "http://opcfoundation.org/UA/",
            "GetMonitoredItems");
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
                    : ServerTypeGetMonitoredItems.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : ServerTypeGetMonitoredItems.outputArguments(
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
                ServerTypeGetMonitoredItems.Inputs input;
                try {
                  input =
                      ServerTypeGetMonitoredItems.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                ServerTypeGetMonitoredItems.Outputs output =
                    Objects.requireNonNull(
                        handler.getMonitoredItems(context, input.subscriptionId()),
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
  public @Nullable UaMethodNode getRequestServerStateChangeMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "RequestServerStateChange",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setRequestServerStateChangeHandler(
      ServerType.@Nullable RequestServerStateChangeHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this,
            getRequestServerStateChangeMethodNode(),
            "http://opcfoundation.org/UA/",
            "RequestServerStateChange");
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
                    : ServerTypeRequestServerStateChange.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : ServerTypeRequestServerStateChange.outputArguments(
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
                ServerTypeRequestServerStateChange.Inputs input;
                try {
                  input =
                      ServerTypeRequestServerStateChange.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                handler.requestServerStateChange(
                    context,
                    input.state(),
                    input.estimatedReturnTime(),
                    input.secondsTillShutdown(),
                    input.reason(),
                    input.restart());
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
  public @Nullable UaMethodNode getResendDataMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "ResendData",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setResendDataHandler(ServerType.@Nullable ResendDataHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this, getResendDataMethodNode(), "http://opcfoundation.org/UA/", "ResendData");
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
                    : ServerTypeResendData.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : ServerTypeResendData.outputArguments(
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
                ServerTypeResendData.Inputs input;
                try {
                  input =
                      ServerTypeResendData.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                handler.resendData(context, input.subscriptionId());
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
  public @Nullable UaMethodNode getSetSubscriptionDurableMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "SetSubscriptionDurable",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setSetSubscriptionDurableHandler(
      ServerType.@Nullable SetSubscriptionDurableHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this,
            getSetSubscriptionDurableMethodNode(),
            "http://opcfoundation.org/UA/",
            "SetSubscriptionDurable");
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
                    : ServerTypeSetSubscriptionDurable.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : ServerTypeSetSubscriptionDurable.outputArguments(
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
                ServerTypeSetSubscriptionDurable.Inputs input;
                try {
                  input =
                      ServerTypeSetSubscriptionDurable.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                ServerTypeSetSubscriptionDurable.Outputs output =
                    new ServerTypeSetSubscriptionDurable.Outputs(
                        handler.setSubscriptionDurable(
                            context, input.subscriptionId(), input.lifetimeInHours()));
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
  public void setMethods(ServerType.@Nullable Methods methods) {
    if (getGetMonitoredItemsMethodNode() != null) {
      setGetMonitoredItemsHandler(methods == null ? null : methods::getMonitoredItems);
    }
    if (getRequestServerStateChangeMethodNode() != null) {
      setRequestServerStateChangeHandler(
          methods == null ? null : methods::requestServerStateChange);
    }
    if (getResendDataMethodNode() != null) {
      setResendDataHandler(methods == null ? null : methods::resendData);
    }
    if (getSetSubscriptionDurableMethodNode() != null) {
      setSetSubscriptionDurableHandler(methods == null ? null : methods::setSubscriptionDurable);
    }
  }
}
