package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubDiagnosticsTypeReset;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.MethodArgumentValidator;
import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PubSubDiagnosticsCounterTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link PubSubDiagnosticsType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.2">Model
 *     documentation</a>
 */
public class PubSubDiagnosticsTypeNode extends BaseObjectTypeNode implements PubSubDiagnosticsType {
  public PubSubDiagnosticsTypeNode(
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

  public PubSubDiagnosticsTypeNode(
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
  public BaseObjectTypeNode getCountersNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Counters",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 58L),
        null,
        -1,
        BaseObjectTypeNode.class);
  }

  @Override
  public BaseDataVariableTypeNode getDiagnosticsLevelNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "DiagnosticsLevel",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 19723L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable DiagnosticsLevel getDiagnosticsLevel() {
    return ServerNodeSupport.read(
        this, getDiagnosticsLevelNode(), DiagnosticsLevel.class, DiagnosticsLevel::from);
  }

  @Override
  public void setDiagnosticsLevel(@Nullable DiagnosticsLevel value) {
    ServerNodeSupport.write(this, getDiagnosticsLevelNode(), value, false, true, false);
  }

  @Override
  public BaseObjectTypeNode getLiveValuesNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "LiveValues",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 58L),
        null,
        -1,
        BaseObjectTypeNode.class);
  }

  @Override
  public BaseDataVariableTypeNode getSubErrorNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SubError",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable Boolean getSubError() {
    return ServerNodeSupport.read(this, getSubErrorNode(), Boolean.class, null);
  }

  @Override
  public void setSubError(@Nullable Boolean value) {
    ServerNodeSupport.write(this, getSubErrorNode(), value, false, false, false);
  }

  @Override
  public PubSubDiagnosticsCounterTypeNode getTotalErrorNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "TotalError",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 19725L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PubSubDiagnosticsCounterTypeNode.class);
  }

  @Override
  public @Nullable UInteger getTotalError() {
    return ServerNodeSupport.read(this, getTotalErrorNode(), UInteger.class, null);
  }

  @Override
  public void setTotalError(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getTotalErrorNode(), value, false, false, false);
  }

  @Override
  public PubSubDiagnosticsCounterTypeNode getTotalInformationNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "TotalInformation",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 19725L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PubSubDiagnosticsCounterTypeNode.class);
  }

  @Override
  public @Nullable UInteger getTotalInformation() {
    return ServerNodeSupport.read(this, getTotalInformationNode(), UInteger.class, null);
  }

  @Override
  public void setTotalInformation(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getTotalInformationNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getCountersNode();
    getDiagnosticsLevelNode();
    getLiveValuesNode();
    getSubErrorNode();
    getTotalErrorNode();
    getTotalInformationNode();
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
  public void setResetHandler(PubSubDiagnosticsType.@Nullable ResetHandler handler) {
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
                    : PubSubDiagnosticsTypeReset.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : PubSubDiagnosticsTypeReset.outputArguments(
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
  public void setMethods(PubSubDiagnosticsType.@Nullable Methods methods) {
    setResetHandler(methods == null ? null : methods::reset);
  }
}
