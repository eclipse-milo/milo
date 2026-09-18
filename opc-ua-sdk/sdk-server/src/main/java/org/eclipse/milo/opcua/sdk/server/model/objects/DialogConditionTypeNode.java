package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.model.methods.DialogConditionTypeRespond;
import org.eclipse.milo.opcua.sdk.core.model.methods.DialogConditionTypeRespond2;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.InvalidArgumentException;
import org.eclipse.milo.opcua.sdk.server.methods.MethodArgumentValidator;
import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link DialogConditionType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.2">Model
 *     documentation</a>
 */
public class DialogConditionTypeNode extends ConditionTypeNode implements DialogConditionType {
  public DialogConditionTypeNode(
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

  public DialogConditionTypeNode(
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
  public PropertyTypeNode getCancelResponseNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "CancelResponse",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 6L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Integer getCancelResponse() {
    return ServerNodeSupport.read(this, getCancelResponseNode(), Integer.class, null);
  }

  @Override
  public void setCancelResponse(@Nullable Integer value) {
    ServerNodeSupport.write(this, getCancelResponseNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getDefaultResponseNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "DefaultResponse",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 6L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Integer getDefaultResponse() {
    return ServerNodeSupport.read(this, getDefaultResponseNode(), Integer.class, null);
  }

  @Override
  public void setDefaultResponse(@Nullable Integer value) {
    ServerNodeSupport.write(this, getDefaultResponseNode(), value, false, false, false);
  }

  @Override
  public TwoStateVariableTypeNode getDialogStateNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "DialogState",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 8995L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        TwoStateVariableTypeNode.class);
  }

  @Override
  public @Nullable LocalizedText getDialogState() {
    return ServerNodeSupport.read(this, getDialogStateNode(), LocalizedText.class, null);
  }

  @Override
  public void setDialogState(@Nullable LocalizedText value) {
    ServerNodeSupport.write(this, getDialogStateNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getLastResponseNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "LastResponse",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 6L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Integer getLastResponse() {
    return ServerNodeSupport.read(this, getLastResponseNode(), Integer.class, null);
  }

  @Override
  public void setLastResponse(@Nullable Integer value) {
    ServerNodeSupport.write(this, getLastResponseNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getOkResponseNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "OkResponse",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 6L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Integer getOkResponse() {
    return ServerNodeSupport.read(this, getOkResponseNode(), Integer.class, null);
  }

  @Override
  public void setOkResponse(@Nullable Integer value) {
    ServerNodeSupport.write(this, getOkResponseNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getPromptNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Prompt",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable LocalizedText getPrompt() {
    return ServerNodeSupport.read(this, getPromptNode(), LocalizedText.class, null);
  }

  @Override
  public void setPrompt(@Nullable LocalizedText value) {
    ServerNodeSupport.write(this, getPromptNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getResponseOptionSetNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ResponseOptionSet",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public LocalizedText @Nullable [] getResponseOptionSet() {
    return ServerNodeSupport.readArray(this, getResponseOptionSetNode(), LocalizedText.class, null);
  }

  @Override
  public void setResponseOptionSet(LocalizedText @Nullable [] value) {
    ServerNodeSupport.write(this, getResponseOptionSetNode(), value, true, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getCancelResponseNode();
    getDefaultResponseNode();
    getDialogStateNode();
    getLastResponseNode();
    getOkResponseNode();
    getPromptNode();
    getResponseOptionSetNode();
  }

  @Override
  public UaMethodNode getRespondMethodNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        "http://opcfoundation.org/UA/",
        "Respond",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setRespondHandler(DialogConditionType.@Nullable RespondHandler handler) {
    UaMethodNode method = getRespondMethodNode();
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
                    : DialogConditionTypeRespond.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : DialogConditionTypeRespond.outputArguments(
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
                DialogConditionTypeRespond.Inputs input;
                try {
                  input =
                      DialogConditionTypeRespond.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                handler.respond(context, input.selectedResponse());
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
  public @Nullable UaMethodNode getRespond2MethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "Respond2",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setRespond2Handler(DialogConditionType.@Nullable Respond2Handler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this, getRespond2MethodNode(), "http://opcfoundation.org/UA/", "Respond2");
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
                    : DialogConditionTypeRespond2.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : DialogConditionTypeRespond2.outputArguments(
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
                DialogConditionTypeRespond2.Inputs input;
                try {
                  input =
                      DialogConditionTypeRespond2.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                handler.respond2(context, input.selectedResponse(), input.comment());
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
  public void setMethods(DialogConditionType.@Nullable Methods methods) {
    setAddCommentHandler(methods == null ? null : methods::addComment);
    setDisableHandler(methods == null ? null : methods::disable);
    setEnableHandler(methods == null ? null : methods::enable);
    setRespondHandler(methods == null ? null : methods::respond);
    if (getRespond2MethodNode() != null) {
      setRespond2Handler(methods == null ? null : methods::respond2);
    }
  }
}
