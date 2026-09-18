package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.model.methods.DataSetReaderTypeCreateDataSetMirror;
import org.eclipse.milo.opcua.sdk.core.model.methods.DataSetReaderTypeCreateTargetVariables;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link DataSetReaderType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.2">Model
 *     documentation</a>
 */
public class DataSetReaderTypeNode extends BaseObjectTypeNode implements DataSetReaderType {
  public DataSetReaderTypeNode(
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

  public DataSetReaderTypeNode(
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
  public PropertyTypeNode getDataSetFieldContentMaskNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "DataSetFieldContentMask",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 15583L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable DataSetFieldContentMask getDataSetFieldContentMask() {
    return ServerNodeSupport.read(
        this, getDataSetFieldContentMaskNode(), DataSetFieldContentMask.class, null);
  }

  @Override
  public void setDataSetFieldContentMask(@Nullable DataSetFieldContentMask value) {
    ServerNodeSupport.write(this, getDataSetFieldContentMaskNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getDataSetMetaDataNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "DataSetMetaData",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 14523L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable DataSetMetaDataType getDataSetMetaData() {
    return ServerNodeSupport.read(this, getDataSetMetaDataNode(), DataSetMetaDataType.class, null);
  }

  @Override
  public void setDataSetMetaData(@Nullable DataSetMetaDataType value) {
    ServerNodeSupport.write(this, getDataSetMetaDataNode(), value, false, false, true);
  }

  @Override
  public PropertyTypeNode getDataSetReaderPropertiesNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "DataSetReaderProperties",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 14533L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable KeyValuePair @Nullable [] getDataSetReaderProperties() {
    return ServerNodeSupport.readArray(
        this, getDataSetReaderPropertiesNode(), KeyValuePair.class, null);
  }

  @Override
  public void setDataSetReaderProperties(@Nullable KeyValuePair @Nullable [] value) {
    ServerNodeSupport.write(this, getDataSetReaderPropertiesNode(), value, true, false, true);
  }

  @Override
  public PropertyTypeNode getDataSetWriterIdNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "DataSetWriterId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UShort getDataSetWriterId() {
    return ServerNodeSupport.read(this, getDataSetWriterIdNode(), UShort.class, null);
  }

  @Override
  public void setDataSetWriterId(@Nullable UShort value) {
    ServerNodeSupport.write(this, getDataSetWriterIdNode(), value, false, false, false);
  }

  @Override
  public @Nullable PubSubDiagnosticsDataSetReaderTypeNode getDiagnosticsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "Diagnostics",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 20027L),
        null,
        -1,
        PubSubDiagnosticsDataSetReaderTypeNode.class);
  }

  @Override
  public PropertyTypeNode getHeaderLayoutUriNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "HeaderLayoutUri",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getHeaderLayoutUri() {
    return ServerNodeSupport.read(this, getHeaderLayoutUriNode(), String.class, null);
  }

  @Override
  public void setHeaderLayoutUri(@Nullable String value) {
    ServerNodeSupport.write(this, getHeaderLayoutUriNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getKeyFrameCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "KeyFrameCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getKeyFrameCount() {
    return ServerNodeSupport.read(this, getKeyFrameCountNode(), UInteger.class, null);
  }

  @Override
  public void setKeyFrameCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getKeyFrameCountNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getMessageReceiveTimeoutNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MessageReceiveTimeout",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getMessageReceiveTimeout() {
    return ServerNodeSupport.read(this, getMessageReceiveTimeoutNode(), Double.class, null);
  }

  @Override
  public void setMessageReceiveTimeout(@Nullable Double value) {
    ServerNodeSupport.write(this, getMessageReceiveTimeoutNode(), value, false, false, false);
  }

  @Override
  public @Nullable DataSetReaderMessageTypeNode getMessageSettingsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MessageSettings",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21104L),
        null,
        -1,
        DataSetReaderMessageTypeNode.class);
  }

  @Override
  public PropertyTypeNode getPublisherIdNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "PublisherId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 24L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Variant getPublisherId() {
    return ServerNodeSupport.read(this, getPublisherIdNode(), Variant.class, null);
  }

  @Override
  public void setPublisherId(@Nullable Variant value) {
    ServerNodeSupport.write(this, getPublisherIdNode(), value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getSecurityGroupIdNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "SecurityGroupId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getSecurityGroupId() {
    return ServerNodeSupport.read(this, getSecurityGroupIdNode(), String.class, null);
  }

  @Override
  public void setSecurityGroupId(@Nullable String value) {
    ServerNodeSupport.write(
        this,
        getSecurityGroupIdNode(),
        Namespaces.OPC_UA,
        "SecurityGroupId",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getSecurityKeyServicesNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "SecurityKeyServices",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 312L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable EndpointDescription @Nullable [] getSecurityKeyServices() {
    return ServerNodeSupport.readArray(
        this, getSecurityKeyServicesNode(), EndpointDescription.class, null);
  }

  @Override
  public void setSecurityKeyServices(@Nullable EndpointDescription @Nullable [] value) {
    ServerNodeSupport.write(
        this,
        getSecurityKeyServicesNode(),
        Namespaces.OPC_UA,
        "SecurityKeyServices",
        value,
        true,
        false,
        true);
  }

  @Override
  public @Nullable PropertyTypeNode getSecurityModeNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "SecurityMode",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 302L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable MessageSecurityMode getSecurityMode() {
    return ServerNodeSupport.read(
        this, getSecurityModeNode(), MessageSecurityMode.class, MessageSecurityMode::from);
  }

  @Override
  public void setSecurityMode(@Nullable MessageSecurityMode value) {
    ServerNodeSupport.write(
        this, getSecurityModeNode(), Namespaces.OPC_UA, "SecurityMode", value, false, true, false);
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
  public SubscribedDataSetTypeNode getSubscribedDataSetNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SubscribedDataSet",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 15108L),
        null,
        -1,
        SubscribedDataSetTypeNode.class);
  }

  @Override
  public @Nullable DataSetReaderTransportTypeNode getTransportSettingsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "TransportSettings",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 15319L),
        null,
        -1,
        DataSetReaderTransportTypeNode.class);
  }

  @Override
  public PropertyTypeNode getWriterGroupIdNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "WriterGroupId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UShort getWriterGroupId() {
    return ServerNodeSupport.read(this, getWriterGroupIdNode(), UShort.class, null);
  }

  @Override
  public void setWriterGroupId(@Nullable UShort value) {
    ServerNodeSupport.write(this, getWriterGroupIdNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getDataSetFieldContentMaskNode();
    getDataSetMetaDataNode();
    getDataSetReaderPropertiesNode();
    getDataSetWriterIdNode();
    getDiagnosticsNode();
    getHeaderLayoutUriNode();
    getKeyFrameCountNode();
    getMessageReceiveTimeoutNode();
    getMessageSettingsNode();
    getPublisherIdNode();
    getSecurityGroupIdNode();
    getSecurityKeyServicesNode();
    getSecurityModeNode();
    getStatusNode();
    getSubscribedDataSetNode();
    getTransportSettingsNode();
    getWriterGroupIdNode();
  }

  @Override
  public @Nullable UaMethodNode getCreateDataSetMirrorMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "CreateDataSetMirror",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setCreateDataSetMirrorHandler(
      DataSetReaderType.@Nullable CreateDataSetMirrorHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this,
            getCreateDataSetMirrorMethodNode(),
            "http://opcfoundation.org/UA/",
            "CreateDataSetMirror");
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
                    : DataSetReaderTypeCreateDataSetMirror.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : DataSetReaderTypeCreateDataSetMirror.outputArguments(
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
                DataSetReaderTypeCreateDataSetMirror.Inputs input;
                try {
                  input =
                      DataSetReaderTypeCreateDataSetMirror.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                DataSetReaderTypeCreateDataSetMirror.Outputs output =
                    new DataSetReaderTypeCreateDataSetMirror.Outputs(
                        handler.createDataSetMirror(
                            context, input.parentNodeName(), input.rolePermissions()));
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
  public @Nullable UaMethodNode getCreateTargetVariablesMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "CreateTargetVariables",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setCreateTargetVariablesHandler(
      DataSetReaderType.@Nullable CreateTargetVariablesHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this,
            getCreateTargetVariablesMethodNode(),
            "http://opcfoundation.org/UA/",
            "CreateTargetVariables");
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
                    : DataSetReaderTypeCreateTargetVariables.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : DataSetReaderTypeCreateTargetVariables.outputArguments(
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
                DataSetReaderTypeCreateTargetVariables.Inputs input;
                try {
                  input =
                      DataSetReaderTypeCreateTargetVariables.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                DataSetReaderTypeCreateTargetVariables.Outputs output =
                    new DataSetReaderTypeCreateTargetVariables.Outputs(
                        handler.createTargetVariables(
                            context, input.configurationVersion(), input.targetVariablesToAdd()));
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
  public void setMethods(DataSetReaderType.@Nullable Methods methods) {
    if (getCreateDataSetMirrorMethodNode() != null) {
      setCreateDataSetMirrorHandler(methods == null ? null : methods::createDataSetMirror);
    }
    if (getCreateTargetVariablesMethodNode() != null) {
      setCreateTargetVariablesHandler(methods == null ? null : methods::createTargetVariables);
    }
  }
}
