package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.model.methods.SerializationEntityTypeConfigureSerialization;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.InvalidArgumentException;
import org.eclipse.milo.opcua.sdk.server.methods.MethodArgumentValidator;
import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Structure;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link SerializationEntityType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part25/6.3.1">Model
 *     documentation</a>
 */
public class SerializationEntityTypeNode extends BaseObjectTypeNode
    implements SerializationEntityType {
  public SerializationEntityTypeNode(
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

  public SerializationEntityTypeNode(
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
  public @Nullable PropertyTypeNode getConsiderSubElementSerializationPropertiesNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ConsiderSubElementSerializationProperties",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getConsiderSubElementSerializationProperties() {
    return ServerNodeSupport.read(
        this, getConsiderSubElementSerializationPropertiesNode(), Boolean.class, null);
  }

  @Override
  public void setConsiderSubElementSerializationProperties(@Nullable Boolean value) {
    ServerNodeSupport.write(
        this,
        getConsiderSubElementSerializationPropertiesNode(),
        Namespaces.OPC_UA,
        "ConsiderSubElementSerializationProperties",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getCustomMetaDataPropertiesNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "CustomMetaDataProperties",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 14533L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable KeyValuePair @Nullable [] getCustomMetaDataProperties() {
    return ServerNodeSupport.readArray(
        this, getCustomMetaDataPropertiesNode(), KeyValuePair.class, null);
  }

  @Override
  public void setCustomMetaDataProperties(@Nullable KeyValuePair @Nullable [] value) {
    ServerNodeSupport.write(
        this,
        getCustomMetaDataPropertiesNode(),
        Namespaces.OPC_UA,
        "CustomMetaDataProperties",
        value,
        true,
        false,
        true);
  }

  @Override
  public @Nullable PropertyTypeNode getCustomMetaDataRefNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "CustomMetaDataRef",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable NodeId getCustomMetaDataRef() {
    return ServerNodeSupport.read(this, getCustomMetaDataRefNode(), NodeId.class, null);
  }

  @Override
  public void setCustomMetaDataRef(@Nullable NodeId value) {
    ServerNodeSupport.write(
        this,
        getCustomMetaDataRefNode(),
        Namespaces.OPC_UA,
        "CustomMetaDataRef",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getExcludeReferenceTypesNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ExcludeReferenceTypes",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public NodeId @Nullable [] getExcludeReferenceTypes() {
    return ServerNodeSupport.readArray(this, getExcludeReferenceTypesNode(), NodeId.class, null);
  }

  @Override
  public void setExcludeReferenceTypes(NodeId @Nullable [] value) {
    ServerNodeSupport.write(
        this,
        getExcludeReferenceTypesNode(),
        Namespaces.OPC_UA,
        "ExcludeReferenceTypes",
        value,
        true,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getIncludeDictionaryReferenceNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "IncludeDictionaryReference",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getIncludeDictionaryReference() {
    return ServerNodeSupport.read(this, getIncludeDictionaryReferenceNode(), Boolean.class, null);
  }

  @Override
  public void setIncludeDictionaryReference(@Nullable Boolean value) {
    ServerNodeSupport.write(
        this,
        getIncludeDictionaryReferenceNode(),
        Namespaces.OPC_UA,
        "IncludeDictionaryReference",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getIncludeReferenceTypesNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "IncludeReferenceTypes",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public NodeId @Nullable [] getIncludeReferenceTypes() {
    return ServerNodeSupport.readArray(this, getIncludeReferenceTypesNode(), NodeId.class, null);
  }

  @Override
  public void setIncludeReferenceTypes(NodeId @Nullable [] value) {
    ServerNodeSupport.write(
        this,
        getIncludeReferenceTypesNode(),
        Namespaces.OPC_UA,
        "IncludeReferenceTypes",
        value,
        true,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getIncludeSourceTimestampNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "IncludeSourceTimestamp",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getIncludeSourceTimestamp() {
    return ServerNodeSupport.read(this, getIncludeSourceTimestampNode(), Boolean.class, null);
  }

  @Override
  public void setIncludeSourceTimestamp(@Nullable Boolean value) {
    ServerNodeSupport.write(
        this,
        getIncludeSourceTimestampNode(),
        Namespaces.OPC_UA,
        "IncludeSourceTimestamp",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getIncludeStatusNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "IncludeStatus",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getIncludeStatus() {
    return ServerNodeSupport.read(this, getIncludeStatusNode(), Boolean.class, null);
  }

  @Override
  public void setIncludeStatus(@Nullable Boolean value) {
    ServerNodeSupport.write(
        this,
        getIncludeStatusNode(),
        Namespaces.OPC_UA,
        "IncludeStatus",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getSerializationDepthNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "SerializationDepth",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UShort getSerializationDepth() {
    return ServerNodeSupport.read(this, getSerializationDepthNode(), UShort.class, null);
  }

  @Override
  public void setSerializationDepth(@Nullable UShort value) {
    ServerNodeSupport.write(
        this,
        getSerializationDepthNode(),
        Namespaces.OPC_UA,
        "SerializationDepth",
        value,
        false,
        false,
        false);
  }

  @Override
  public BaseDataVariableTypeNode getSerializedDataNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SerializedData",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 22L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable Structure getSerializedData() {
    return ServerNodeSupport.read(this, getSerializedDataNode(), Structure.class, null);
  }

  @Override
  public void setSerializedData(@Nullable Structure value) {
    ServerNodeSupport.write(this, getSerializedDataNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getConsiderSubElementSerializationPropertiesNode();
    getCustomMetaDataPropertiesNode();
    getCustomMetaDataRefNode();
    getExcludeReferenceTypesNode();
    getIncludeDictionaryReferenceNode();
    getIncludeReferenceTypesNode();
    getIncludeSourceTimestampNode();
    getIncludeStatusNode();
    getSerializationDepthNode();
    getSerializedDataNode();
  }

  @Override
  public @Nullable UaMethodNode getConfigureSerializationMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "ConfigureSerialization",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setConfigureSerializationHandler(
      SerializationEntityType.@Nullable ConfigureSerializationHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this,
            getConfigureSerializationMethodNode(),
            "http://opcfoundation.org/UA/",
            "ConfigureSerialization");
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
                    : SerializationEntityTypeConfigureSerialization.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : SerializationEntityTypeConfigureSerialization.outputArguments(
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
                SerializationEntityTypeConfigureSerialization.Inputs input;
                try {
                  input =
                      SerializationEntityTypeConfigureSerialization.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                SerializationEntityTypeConfigureSerialization.Outputs output =
                    new SerializationEntityTypeConfigureSerialization.Outputs(
                        handler.configureSerialization(
                            context, input.serializationFilterProperties()));
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
  public void setMethods(SerializationEntityType.@Nullable Methods methods) {
    if (getConfigureSerializationMethodNode() != null) {
      setConfigureSerializationHandler(methods == null ? null : methods::configureSerialization);
    }
  }
}
