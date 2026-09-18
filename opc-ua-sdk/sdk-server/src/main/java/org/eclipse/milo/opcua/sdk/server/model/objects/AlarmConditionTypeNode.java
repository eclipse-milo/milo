package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.model.methods.AlarmConditionTypeGetGroupMemberships;
import org.eclipse.milo.opcua.sdk.core.model.methods.AlarmConditionTypePlaceInService;
import org.eclipse.milo.opcua.sdk.core.model.methods.AlarmConditionTypePlaceInService2;
import org.eclipse.milo.opcua.sdk.core.model.methods.AlarmConditionTypeRemoveFromService;
import org.eclipse.milo.opcua.sdk.core.model.methods.AlarmConditionTypeRemoveFromService2;
import org.eclipse.milo.opcua.sdk.core.model.methods.AlarmConditionTypeReset;
import org.eclipse.milo.opcua.sdk.core.model.methods.AlarmConditionTypeReset2;
import org.eclipse.milo.opcua.sdk.core.model.methods.AlarmConditionTypeSilence;
import org.eclipse.milo.opcua.sdk.core.model.methods.AlarmConditionTypeSuppress;
import org.eclipse.milo.opcua.sdk.core.model.methods.AlarmConditionTypeSuppress2;
import org.eclipse.milo.opcua.sdk.core.model.methods.AlarmConditionTypeUnsuppress;
import org.eclipse.milo.opcua.sdk.core.model.methods.AlarmConditionTypeUnsuppress2;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.InvalidArgumentException;
import org.eclipse.milo.opcua.sdk.server.methods.MethodArgumentValidator;
import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.AudioVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaArgumentConversionException;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
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
 * Node implementation of {@link AlarmConditionType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.2">Model
 *     documentation</a>
 */
public class AlarmConditionTypeNode extends AcknowledgeableConditionTypeNode
    implements AlarmConditionType {
  public AlarmConditionTypeNode(
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

  public AlarmConditionTypeNode(
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
  public TwoStateVariableTypeNode getActiveStateNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ActiveState",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 8995L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        TwoStateVariableTypeNode.class);
  }

  @Override
  public @Nullable LocalizedText getActiveState() {
    return ServerNodeSupport.read(this, getActiveStateNode(), LocalizedText.class, null);
  }

  @Override
  public void setActiveState(@Nullable LocalizedText value) {
    ServerNodeSupport.write(this, getActiveStateNode(), value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getAudibleEnabledNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "AudibleEnabled",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getAudibleEnabled() {
    return ServerNodeSupport.read(this, getAudibleEnabledNode(), Boolean.class, null);
  }

  @Override
  public void setAudibleEnabled(@Nullable Boolean value) {
    ServerNodeSupport.write(
        this,
        getAudibleEnabledNode(),
        Namespaces.OPC_UA,
        "AudibleEnabled",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable AudioVariableTypeNode getAudibleSoundNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "AudibleSound",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17986L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 16307L),
        -1,
        AudioVariableTypeNode.class);
  }

  @Override
  public @Nullable ByteString getAudibleSound() {
    return ServerNodeSupport.read(this, getAudibleSoundNode(), ByteString.class, null);
  }

  @Override
  public void setAudibleSound(@Nullable ByteString value) {
    ServerNodeSupport.write(
        this, getAudibleSoundNode(), Namespaces.OPC_UA, "AudibleSound", value, false, false, false);
  }

  @Override
  public @Nullable AlarmGroupTypeNode getFirstInGroupNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "FirstInGroup",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 16405L),
        null,
        -1,
        AlarmGroupTypeNode.class);
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getFirstInGroupFlagNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "FirstInGroupFlag",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable Boolean getFirstInGroupFlag() {
    return ServerNodeSupport.read(this, getFirstInGroupFlagNode(), Boolean.class, null);
  }

  @Override
  public void setFirstInGroupFlag(@Nullable Boolean value) {
    ServerNodeSupport.write(
        this,
        getFirstInGroupFlagNode(),
        Namespaces.OPC_UA,
        "FirstInGroupFlag",
        value,
        false,
        false,
        false);
  }

  @Override
  public PropertyTypeNode getInputNodeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "InputNode",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable NodeId getInputNode() {
    return ServerNodeSupport.read(this, getInputNodeNode(), NodeId.class, null);
  }

  @Override
  public void setInputNode(@Nullable NodeId value) {
    ServerNodeSupport.write(this, getInputNodeNode(), value, false, false, false);
  }

  @Override
  public @Nullable TwoStateVariableTypeNode getLatchedStateNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "LatchedState",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 8995L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        TwoStateVariableTypeNode.class);
  }

  @Override
  public @Nullable LocalizedText getLatchedState() {
    return ServerNodeSupport.read(this, getLatchedStateNode(), LocalizedText.class, null);
  }

  @Override
  public void setLatchedState(@Nullable LocalizedText value) {
    ServerNodeSupport.write(
        this, getLatchedStateNode(), Namespaces.OPC_UA, "LatchedState", value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxTimeShelvedNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxTimeShelved",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getMaxTimeShelved() {
    return ServerNodeSupport.read(this, getMaxTimeShelvedNode(), Double.class, null);
  }

  @Override
  public void setMaxTimeShelved(@Nullable Double value) {
    ServerNodeSupport.write(
        this,
        getMaxTimeShelvedNode(),
        Namespaces.OPC_UA,
        "MaxTimeShelved",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getOffDelayNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "OffDelay",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getOffDelay() {
    return ServerNodeSupport.read(this, getOffDelayNode(), Double.class, null);
  }

  @Override
  public void setOffDelay(@Nullable Double value) {
    ServerNodeSupport.write(
        this, getOffDelayNode(), Namespaces.OPC_UA, "OffDelay", value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getOnDelayNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "OnDelay",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getOnDelay() {
    return ServerNodeSupport.read(this, getOnDelayNode(), Double.class, null);
  }

  @Override
  public void setOnDelay(@Nullable Double value) {
    ServerNodeSupport.write(
        this, getOnDelayNode(), Namespaces.OPC_UA, "OnDelay", value, false, false, false);
  }

  @Override
  public @Nullable TwoStateVariableTypeNode getOutOfServiceStateNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "OutOfServiceState",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 8995L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        TwoStateVariableTypeNode.class);
  }

  @Override
  public @Nullable LocalizedText getOutOfServiceState() {
    return ServerNodeSupport.read(this, getOutOfServiceStateNode(), LocalizedText.class, null);
  }

  @Override
  public void setOutOfServiceState(@Nullable LocalizedText value) {
    ServerNodeSupport.write(
        this,
        getOutOfServiceStateNode(),
        Namespaces.OPC_UA,
        "OutOfServiceState",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getReAlarmRepeatCountNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ReAlarmRepeatCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 4L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable Short getReAlarmRepeatCount() {
    return ServerNodeSupport.read(this, getReAlarmRepeatCountNode(), Short.class, null);
  }

  @Override
  public void setReAlarmRepeatCount(@Nullable Short value) {
    ServerNodeSupport.write(
        this,
        getReAlarmRepeatCountNode(),
        Namespaces.OPC_UA,
        "ReAlarmRepeatCount",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getReAlarmTimeNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ReAlarmTime",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getReAlarmTime() {
    return ServerNodeSupport.read(this, getReAlarmTimeNode(), Double.class, null);
  }

  @Override
  public void setReAlarmTime(@Nullable Double value) {
    ServerNodeSupport.write(
        this, getReAlarmTimeNode(), Namespaces.OPC_UA, "ReAlarmTime", value, false, false, false);
  }

  @Override
  public @Nullable ShelvedStateMachineTypeNode getShelvingStateNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ShelvingState",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 2929L),
        null,
        -1,
        ShelvedStateMachineTypeNode.class);
  }

  @Override
  public @Nullable TwoStateVariableTypeNode getSilenceStateNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "SilenceState",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 8995L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        TwoStateVariableTypeNode.class);
  }

  @Override
  public @Nullable LocalizedText getSilenceState() {
    return ServerNodeSupport.read(this, getSilenceStateNode(), LocalizedText.class, null);
  }

  @Override
  public void setSilenceState(@Nullable LocalizedText value) {
    ServerNodeSupport.write(
        this, getSilenceStateNode(), Namespaces.OPC_UA, "SilenceState", value, false, false, false);
  }

  @Override
  public PropertyTypeNode getSuppressedOrShelvedNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SuppressedOrShelved",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getSuppressedOrShelved() {
    return ServerNodeSupport.read(this, getSuppressedOrShelvedNode(), Boolean.class, null);
  }

  @Override
  public void setSuppressedOrShelved(@Nullable Boolean value) {
    ServerNodeSupport.write(this, getSuppressedOrShelvedNode(), value, false, false, false);
  }

  @Override
  public @Nullable TwoStateVariableTypeNode getSuppressedStateNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "SuppressedState",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 8995L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        TwoStateVariableTypeNode.class);
  }

  @Override
  public @Nullable LocalizedText getSuppressedState() {
    return ServerNodeSupport.read(this, getSuppressedStateNode(), LocalizedText.class, null);
  }

  @Override
  public void setSuppressedState(@Nullable LocalizedText value) {
    ServerNodeSupport.write(
        this,
        getSuppressedStateNode(),
        Namespaces.OPC_UA,
        "SuppressedState",
        value,
        false,
        false,
        false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getActiveStateNode();
    getAudibleEnabledNode();
    getAudibleSoundNode();
    getFirstInGroupNode();
    getFirstInGroupFlagNode();
    getInputNodeNode();
    getLatchedStateNode();
    getMaxTimeShelvedNode();
    getOffDelayNode();
    getOnDelayNode();
    getOutOfServiceStateNode();
    getReAlarmRepeatCountNode();
    getReAlarmTimeNode();
    getShelvingStateNode();
    getSilenceStateNode();
    getSuppressedOrShelvedNode();
    getSuppressedStateNode();
  }

  @Override
  public @Nullable UaMethodNode getGetGroupMembershipsMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "GetGroupMemberships",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setGetGroupMembershipsHandler(
      AlarmConditionType.@Nullable GetGroupMembershipsHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this,
            getGetGroupMembershipsMethodNode(),
            "http://opcfoundation.org/UA/",
            "GetGroupMemberships");
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
                    : AlarmConditionTypeGetGroupMemberships.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : AlarmConditionTypeGetGroupMemberships.outputArguments(
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
                AlarmConditionTypeGetGroupMemberships.Outputs output =
                    new AlarmConditionTypeGetGroupMemberships.Outputs(
                        handler.getGroupMemberships(context));
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
  public @Nullable UaMethodNode getPlaceInServiceMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "PlaceInService",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setPlaceInServiceHandler(AlarmConditionType.@Nullable PlaceInServiceHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this, getPlaceInServiceMethodNode(), "http://opcfoundation.org/UA/", "PlaceInService");
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
                    : AlarmConditionTypePlaceInService.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : AlarmConditionTypePlaceInService.outputArguments(
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
                handler.placeInService(context);
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
  public @Nullable UaMethodNode getPlaceInService2MethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "PlaceInService2",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setPlaceInService2Handler(
      AlarmConditionType.@Nullable PlaceInService2Handler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this,
            getPlaceInService2MethodNode(),
            "http://opcfoundation.org/UA/",
            "PlaceInService2");
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
                    : AlarmConditionTypePlaceInService2.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : AlarmConditionTypePlaceInService2.outputArguments(
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
                AlarmConditionTypePlaceInService2.Inputs input;
                try {
                  input =
                      AlarmConditionTypePlaceInService2.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                handler.placeInService2(context, input.comment());
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
  public @Nullable UaMethodNode getRemoveFromServiceMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "RemoveFromService",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setRemoveFromServiceHandler(
      AlarmConditionType.@Nullable RemoveFromServiceHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this,
            getRemoveFromServiceMethodNode(),
            "http://opcfoundation.org/UA/",
            "RemoveFromService");
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
                    : AlarmConditionTypeRemoveFromService.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : AlarmConditionTypeRemoveFromService.outputArguments(
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
                handler.removeFromService(context);
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
  public @Nullable UaMethodNode getRemoveFromService2MethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "RemoveFromService2",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setRemoveFromService2Handler(
      AlarmConditionType.@Nullable RemoveFromService2Handler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this,
            getRemoveFromService2MethodNode(),
            "http://opcfoundation.org/UA/",
            "RemoveFromService2");
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
                    : AlarmConditionTypeRemoveFromService2.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : AlarmConditionTypeRemoveFromService2.outputArguments(
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
                AlarmConditionTypeRemoveFromService2.Inputs input;
                try {
                  input =
                      AlarmConditionTypeRemoveFromService2.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                handler.removeFromService2(context, input.comment());
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
  public @Nullable UaMethodNode getResetMethodNode() {
    return ServerNodeSupport.optionalChild(
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
  public void setResetHandler(AlarmConditionType.@Nullable ResetHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this, getResetMethodNode(), "http://opcfoundation.org/UA/", "Reset");
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
                    : AlarmConditionTypeReset.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : AlarmConditionTypeReset.outputArguments(
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
  public @Nullable UaMethodNode getReset2MethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "Reset2",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setReset2Handler(AlarmConditionType.@Nullable Reset2Handler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this, getReset2MethodNode(), "http://opcfoundation.org/UA/", "Reset2");
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
                    : AlarmConditionTypeReset2.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : AlarmConditionTypeReset2.outputArguments(
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
                AlarmConditionTypeReset2.Inputs input;
                try {
                  input =
                      AlarmConditionTypeReset2.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                handler.reset2(context, input.comment());
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
  public @Nullable UaMethodNode getSilenceMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "Silence",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setSilenceHandler(AlarmConditionType.@Nullable SilenceHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this, getSilenceMethodNode(), "http://opcfoundation.org/UA/", "Silence");
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
                    : AlarmConditionTypeSilence.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : AlarmConditionTypeSilence.outputArguments(
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
                handler.silence(context);
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
  public @Nullable UaMethodNode getSuppressMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "Suppress",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setSuppressHandler(AlarmConditionType.@Nullable SuppressHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this, getSuppressMethodNode(), "http://opcfoundation.org/UA/", "Suppress");
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
                    : AlarmConditionTypeSuppress.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : AlarmConditionTypeSuppress.outputArguments(
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
                handler.suppress(context);
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
  public @Nullable UaMethodNode getSuppress2MethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "Suppress2",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setSuppress2Handler(AlarmConditionType.@Nullable Suppress2Handler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this, getSuppress2MethodNode(), "http://opcfoundation.org/UA/", "Suppress2");
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
                    : AlarmConditionTypeSuppress2.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : AlarmConditionTypeSuppress2.outputArguments(
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
                AlarmConditionTypeSuppress2.Inputs input;
                try {
                  input =
                      AlarmConditionTypeSuppress2.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                handler.suppress2(context, input.comment());
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
  public @Nullable UaMethodNode getUnsuppressMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "Unsuppress",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setUnsuppressHandler(AlarmConditionType.@Nullable UnsuppressHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this, getUnsuppressMethodNode(), "http://opcfoundation.org/UA/", "Unsuppress");
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
                    : AlarmConditionTypeUnsuppress.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : AlarmConditionTypeUnsuppress.outputArguments(
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
                handler.unsuppress(context);
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
  public @Nullable UaMethodNode getUnsuppress2MethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "Unsuppress2",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setUnsuppress2Handler(AlarmConditionType.@Nullable Unsuppress2Handler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this, getUnsuppress2MethodNode(), "http://opcfoundation.org/UA/", "Unsuppress2");
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
                    : AlarmConditionTypeUnsuppress2.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : AlarmConditionTypeUnsuppress2.outputArguments(
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
                AlarmConditionTypeUnsuppress2.Inputs input;
                try {
                  input =
                      AlarmConditionTypeUnsuppress2.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                handler.unsuppress2(context, input.comment());
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
  public void setMethods(AlarmConditionType.@Nullable Methods methods) {
    setAcknowledgeHandler(methods == null ? null : methods::acknowledge);
    setAddCommentHandler(methods == null ? null : methods::addComment);
    if (getConfirmMethodNode() != null) {
      setConfirmHandler(methods == null ? null : methods::confirm);
    }
    setDisableHandler(methods == null ? null : methods::disable);
    setEnableHandler(methods == null ? null : methods::enable);
    if (getGetGroupMembershipsMethodNode() != null) {
      setGetGroupMembershipsHandler(methods == null ? null : methods::getGroupMemberships);
    }
    if (getPlaceInServiceMethodNode() != null) {
      setPlaceInServiceHandler(methods == null ? null : methods::placeInService);
    }
    if (getPlaceInService2MethodNode() != null) {
      setPlaceInService2Handler(methods == null ? null : methods::placeInService2);
    }
    if (getRemoveFromServiceMethodNode() != null) {
      setRemoveFromServiceHandler(methods == null ? null : methods::removeFromService);
    }
    if (getRemoveFromService2MethodNode() != null) {
      setRemoveFromService2Handler(methods == null ? null : methods::removeFromService2);
    }
    if (getResetMethodNode() != null) {
      setResetHandler(methods == null ? null : methods::reset);
    }
    if (getReset2MethodNode() != null) {
      setReset2Handler(methods == null ? null : methods::reset2);
    }
    if (getSilenceMethodNode() != null) {
      setSilenceHandler(methods == null ? null : methods::silence);
    }
    if (getSuppressMethodNode() != null) {
      setSuppressHandler(methods == null ? null : methods::suppress);
    }
    if (getSuppress2MethodNode() != null) {
      setSuppress2Handler(methods == null ? null : methods::suppress2);
    }
    if (getUnsuppressMethodNode() != null) {
      setUnsuppressHandler(methods == null ? null : methods::unsuppress);
    }
    if (getUnsuppress2MethodNode() != null) {
      setUnsuppress2Handler(methods == null ? null : methods::unsuppress2);
    }
  }
}
