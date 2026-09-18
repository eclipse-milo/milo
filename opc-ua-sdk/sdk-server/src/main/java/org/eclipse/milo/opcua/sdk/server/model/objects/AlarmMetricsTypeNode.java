package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.model.methods.AlarmMetricsTypeReset;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.MethodArgumentValidator;
import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.AlarmRateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
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
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link AlarmMetricsType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/9.2">Model
 *     documentation</a>
 */
public class AlarmMetricsTypeNode extends BaseObjectTypeNode implements AlarmMetricsType {
  public AlarmMetricsTypeNode(
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

  public AlarmMetricsTypeNode(
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
  public BaseDataVariableTypeNode getAlarmCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "AlarmCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getAlarmCount() {
    return ServerNodeSupport.read(this, getAlarmCountNode(), UInteger.class, null);
  }

  @Override
  public void setAlarmCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getAlarmCountNode(), value, false, false, false);
  }

  @Override
  public AlarmRateVariableTypeNode getAverageAlarmRateNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "AverageAlarmRate",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17277L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
        -1,
        AlarmRateVariableTypeNode.class);
  }

  @Override
  public @Nullable Double getAverageAlarmRate() {
    return ServerNodeSupport.read(this, getAverageAlarmRateNode(), Double.class, null);
  }

  @Override
  public void setAverageAlarmRate(@Nullable Double value) {
    ServerNodeSupport.write(this, getAverageAlarmRateNode(), value, false, false, false);
  }

  @Override
  public AlarmRateVariableTypeNode getCurrentAlarmRateNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "CurrentAlarmRate",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17277L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
        -1,
        AlarmRateVariableTypeNode.class);
  }

  @Override
  public @Nullable Double getCurrentAlarmRate() {
    return ServerNodeSupport.read(this, getCurrentAlarmRateNode(), Double.class, null);
  }

  @Override
  public void setCurrentAlarmRate(@Nullable Double value) {
    ServerNodeSupport.write(this, getCurrentAlarmRateNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getMaximumActiveStateNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MaximumActiveState",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable Double getMaximumActiveState() {
    return ServerNodeSupport.read(this, getMaximumActiveStateNode(), Double.class, null);
  }

  @Override
  public void setMaximumActiveState(@Nullable Double value) {
    ServerNodeSupport.write(this, getMaximumActiveStateNode(), value, false, false, false);
  }

  @Override
  public AlarmRateVariableTypeNode getMaximumAlarmRateNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MaximumAlarmRate",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17277L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
        -1,
        AlarmRateVariableTypeNode.class);
  }

  @Override
  public @Nullable Double getMaximumAlarmRate() {
    return ServerNodeSupport.read(this, getMaximumAlarmRateNode(), Double.class, null);
  }

  @Override
  public void setMaximumAlarmRate(@Nullable Double value) {
    ServerNodeSupport.write(this, getMaximumAlarmRateNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getMaximumReAlarmCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MaximumReAlarmCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaximumReAlarmCount() {
    return ServerNodeSupport.read(this, getMaximumReAlarmCountNode(), UInteger.class, null);
  }

  @Override
  public void setMaximumReAlarmCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getMaximumReAlarmCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getMaximumUnAckNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MaximumUnAck",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable Double getMaximumUnAck() {
    return ServerNodeSupport.read(this, getMaximumUnAckNode(), Double.class, null);
  }

  @Override
  public void setMaximumUnAck(@Nullable Double value) {
    ServerNodeSupport.write(this, getMaximumUnAckNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getStartTimeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "StartTime",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable DateTime getStartTime() {
    return ServerNodeSupport.read(this, getStartTimeNode(), DateTime.class, null);
  }

  @Override
  public void setStartTime(@Nullable DateTime value) {
    ServerNodeSupport.write(this, getStartTimeNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getAlarmCountNode();
    getAverageAlarmRateNode();
    getCurrentAlarmRateNode();
    getMaximumActiveStateNode();
    getMaximumAlarmRateNode();
    getMaximumReAlarmCountNode();
    getMaximumUnAckNode();
    getStartTimeNode();
  }

  @Override
  public UaMethodNode getResetMethodNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        "http://opcfoundation.org/UA/",
        "Reset",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setResetHandler(AlarmMetricsType.@Nullable ResetHandler handler) {
    UaMethodNode method = getResetMethodNode();
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
                    : AlarmMetricsTypeReset.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : AlarmMetricsTypeReset.outputArguments(
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
                handler.reset(context);
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
  public void setMethods(AlarmMetricsType.@Nullable Methods methods) {
    setResetHandler(methods == null ? null : methods::reset);
  }
}
