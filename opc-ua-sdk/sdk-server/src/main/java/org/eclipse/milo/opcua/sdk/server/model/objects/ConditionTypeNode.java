package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.model.methods.ConditionTypeAddComment;
import org.eclipse.milo.opcua.sdk.core.model.methods.ConditionTypeDisable;
import org.eclipse.milo.opcua.sdk.core.model.methods.ConditionTypeEnable;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.InvalidArgumentException;
import org.eclipse.milo.opcua.sdk.server.methods.MethodArgumentValidator;
import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.ConditionVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableTypeNode;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ConditionType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.2">Model
 *     documentation</a>
 */
public class ConditionTypeNode extends BaseEventTypeNode implements ConditionType {
  public ConditionTypeNode(
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

  public ConditionTypeNode(
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
  public PropertyTypeNode getBranchIdNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "BranchId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable NodeId getBranchId() {
    return ServerNodeSupport.read(this, getBranchIdNode(), NodeId.class, null);
  }

  @Override
  public void setBranchId(@Nullable NodeId value) {
    ServerNodeSupport.write(this, getBranchIdNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getClientUserIdNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ClientUserId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getClientUserId() {
    return ServerNodeSupport.read(this, getClientUserIdNode(), String.class, null);
  }

  @Override
  public void setClientUserId(@Nullable String value) {
    ServerNodeSupport.write(this, getClientUserIdNode(), value, false, false, false);
  }

  @Override
  public ConditionVariableTypeNode getCommentNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Comment",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 9002L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        ConditionVariableTypeNode.class);
  }

  @Override
  public @Nullable LocalizedText getComment() {
    return ServerNodeSupport.read(this, getCommentNode(), LocalizedText.class, null);
  }

  @Override
  public void setComment(@Nullable LocalizedText value) {
    ServerNodeSupport.write(this, getCommentNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getConditionClassIdNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ConditionClassId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public PropertyTypeNode getConditionClassNameNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ConditionClassName",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public PropertyTypeNode getConditionNameNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ConditionName",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getConditionName() {
    return ServerNodeSupport.read(this, getConditionNameNode(), String.class, null);
  }

  @Override
  public void setConditionName(@Nullable String value) {
    ServerNodeSupport.write(this, getConditionNameNode(), value, false, false, false);
  }

  @Override
  public TwoStateVariableTypeNode getEnabledStateNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "EnabledState",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 8995L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        TwoStateVariableTypeNode.class);
  }

  @Override
  public @Nullable LocalizedText getEnabledState() {
    return ServerNodeSupport.read(this, getEnabledStateNode(), LocalizedText.class, null);
  }

  @Override
  public void setEnabledState(@Nullable LocalizedText value) {
    ServerNodeSupport.write(this, getEnabledStateNode(), value, false, false, false);
  }

  @Override
  public ConditionVariableTypeNode getLastSeverityNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "LastSeverity",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 9002L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
        -1,
        ConditionVariableTypeNode.class);
  }

  @Override
  public @Nullable UShort getLastSeverity() {
    return ServerNodeSupport.read(this, getLastSeverityNode(), UShort.class, null);
  }

  @Override
  public void setLastSeverity(@Nullable UShort value) {
    ServerNodeSupport.write(this, getLastSeverityNode(), value, false, false, false);
  }

  @Override
  public ConditionVariableTypeNode getQualityNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Quality",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 9002L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 19L),
        -1,
        ConditionVariableTypeNode.class);
  }

  @Override
  public @Nullable StatusCode getQuality() {
    return ServerNodeSupport.read(this, getQualityNode(), StatusCode.class, null);
  }

  @Override
  public void setQuality(@Nullable StatusCode value) {
    ServerNodeSupport.write(this, getQualityNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getRetainNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Retain",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getRetain() {
    return ServerNodeSupport.read(this, getRetainNode(), Boolean.class, null);
  }

  @Override
  public void setRetain(@Nullable Boolean value) {
    ServerNodeSupport.write(this, getRetainNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getBranchIdNode();
    getClientUserIdNode();
    getCommentNode();
    getConditionNameNode();
    getEnabledStateNode();
    getLastSeverityNode();
    getQualityNode();
    getRetainNode();
  }

  @Override
  public UaMethodNode getAddCommentMethodNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        "http://opcfoundation.org/UA/",
        "AddComment",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setAddCommentHandler(ConditionType.@Nullable AddCommentHandler handler) {
    UaMethodNode method = getAddCommentMethodNode();
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
                    : ConditionTypeAddComment.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : ConditionTypeAddComment.outputArguments(
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
                ConditionTypeAddComment.Inputs input;
                try {
                  input =
                      ConditionTypeAddComment.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                handler.addComment(context, input.eventId(), input.comment());
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
  public @Nullable UaMethodNode getConditionRefreshMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "ConditionRefresh",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public @Nullable UaMethodNode getConditionRefresh2MethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "ConditionRefresh2",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public UaMethodNode getDisableMethodNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        "http://opcfoundation.org/UA/",
        "Disable",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setDisableHandler(ConditionType.@Nullable DisableHandler handler) {
    UaMethodNode method = getDisableMethodNode();
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
                    : ConditionTypeDisable.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : ConditionTypeDisable.outputArguments(
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
                handler.disable(context);
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
  public UaMethodNode getEnableMethodNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        "http://opcfoundation.org/UA/",
        "Enable",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setEnableHandler(ConditionType.@Nullable EnableHandler handler) {
    UaMethodNode method = getEnableMethodNode();
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
                    : ConditionTypeEnable.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : ConditionTypeEnable.outputArguments(
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
                handler.enable(context);
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
  public void setMethods(ConditionType.@Nullable Methods methods) {
    setAddCommentHandler(methods == null ? null : methods::addComment);
    setDisableHandler(methods == null ? null : methods::disable);
    setEnableHandler(methods == null ? null : methods::enable);
  }
}
