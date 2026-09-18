package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.model.methods.WriterGroupTypeAddDataSetWriter;
import org.eclipse.milo.opcua.sdk.core.model.methods.WriterGroupTypeRemoveDataSetWriter;
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
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link WriterGroupType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.3">Model
 *     documentation</a>
 */
public class WriterGroupTypeNode extends PubSubGroupTypeNode implements WriterGroupType {
  public WriterGroupTypeNode(
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

  public WriterGroupTypeNode(
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
  public @Nullable PubSubDiagnosticsWriterGroupTypeNode getDiagnosticsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "Diagnostics",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 19834L),
        null,
        -1,
        PubSubDiagnosticsWriterGroupTypeNode.class);
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
  public PropertyTypeNode getKeepAliveTimeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "KeepAliveTime",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getKeepAliveTime() {
    return ServerNodeSupport.read(this, getKeepAliveTimeNode(), Double.class, null);
  }

  @Override
  public void setKeepAliveTime(@Nullable Double value) {
    ServerNodeSupport.write(this, getKeepAliveTimeNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getLocaleIdsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "LocaleIds",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 295L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String @Nullable [] getLocaleIds() {
    return ServerNodeSupport.readArray(this, getLocaleIdsNode(), String.class, null);
  }

  @Override
  public void setLocaleIds(@Nullable String @Nullable [] value) {
    ServerNodeSupport.write(this, getLocaleIdsNode(), value, true, false, false);
  }

  @Override
  public @Nullable WriterGroupMessageTypeNode getMessageSettingsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MessageSettings",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17998L),
        null,
        -1,
        WriterGroupMessageTypeNode.class);
  }

  @Override
  public PropertyTypeNode getPriorityNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Priority",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 3L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UByte getPriority() {
    return ServerNodeSupport.read(this, getPriorityNode(), UByte.class, null);
  }

  @Override
  public void setPriority(@Nullable UByte value) {
    ServerNodeSupport.write(this, getPriorityNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getPublishingIntervalNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "PublishingInterval",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getPublishingInterval() {
    return ServerNodeSupport.read(this, getPublishingIntervalNode(), Double.class, null);
  }

  @Override
  public void setPublishingInterval(@Nullable Double value) {
    ServerNodeSupport.write(this, getPublishingIntervalNode(), value, false, false, false);
  }

  @Override
  public @Nullable WriterGroupTransportTypeNode getTransportSettingsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "TransportSettings",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17997L),
        null,
        -1,
        WriterGroupTransportTypeNode.class);
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
    getDiagnosticsNode();
    getHeaderLayoutUriNode();
    getKeepAliveTimeNode();
    getLocaleIdsNode();
    getMessageSettingsNode();
    getPriorityNode();
    getPublishingIntervalNode();
    getTransportSettingsNode();
    getWriterGroupIdNode();
  }

  @Override
  public @Nullable UaMethodNode getAddDataSetWriterMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "AddDataSetWriter",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setAddDataSetWriterHandler(
      WriterGroupType.@Nullable AddDataSetWriterHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this,
            getAddDataSetWriterMethodNode(),
            "http://opcfoundation.org/UA/",
            "AddDataSetWriter");
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
                    : WriterGroupTypeAddDataSetWriter.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : WriterGroupTypeAddDataSetWriter.outputArguments(
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
                WriterGroupTypeAddDataSetWriter.Inputs input;
                try {
                  input =
                      WriterGroupTypeAddDataSetWriter.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                WriterGroupTypeAddDataSetWriter.Outputs output =
                    new WriterGroupTypeAddDataSetWriter.Outputs(
                        handler.addDataSetWriter(context, input.configuration()));
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
  public @Nullable UaMethodNode getRemoveDataSetWriterMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "RemoveDataSetWriter",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setRemoveDataSetWriterHandler(
      WriterGroupType.@Nullable RemoveDataSetWriterHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this,
            getRemoveDataSetWriterMethodNode(),
            "http://opcfoundation.org/UA/",
            "RemoveDataSetWriter");
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
                    : WriterGroupTypeRemoveDataSetWriter.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : WriterGroupTypeRemoveDataSetWriter.outputArguments(
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
                WriterGroupTypeRemoveDataSetWriter.Inputs input;
                try {
                  input =
                      WriterGroupTypeRemoveDataSetWriter.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                handler.removeDataSetWriter(context, input.dataSetWriterNodeId());
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
  public void setMethods(WriterGroupType.@Nullable Methods methods) {
    if (getAddDataSetWriterMethodNode() != null) {
      setAddDataSetWriterHandler(methods == null ? null : methods::addDataSetWriter);
    }
    if (getRemoveDataSetWriterMethodNode() != null) {
      setRemoveDataSetWriterHandler(methods == null ? null : methods::removeDataSetWriter);
    }
  }
}
