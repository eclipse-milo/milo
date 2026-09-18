package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubConnectionTypeAddReaderGroup;
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubConnectionTypeAddWriterGroup;
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubConnectionTypeRemoveGroup;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.InvalidArgumentException;
import org.eclipse.milo.opcua.sdk.server.methods.MethodArgumentValidator;
import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SelectionListTypeNode;
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
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link PubSubConnectionType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.2">Model
 *     documentation</a>
 */
public class PubSubConnectionTypeNode extends BaseObjectTypeNode implements PubSubConnectionType {
  public PubSubConnectionTypeNode(
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

  public PubSubConnectionTypeNode(
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
  public NetworkAddressTypeNode getAddressNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Address",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21145L),
        null,
        -1,
        NetworkAddressTypeNode.class);
  }

  @Override
  public PropertyTypeNode getConnectionPropertiesNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ConnectionProperties",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 14533L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable KeyValuePair @Nullable [] getConnectionProperties() {
    return ServerNodeSupport.readArray(
        this, getConnectionPropertiesNode(), KeyValuePair.class, null);
  }

  @Override
  public void setConnectionProperties(@Nullable KeyValuePair @Nullable [] value) {
    ServerNodeSupport.write(this, getConnectionPropertiesNode(), value, true, false, true);
  }

  @Override
  public @Nullable PubSubDiagnosticsConnectionTypeNode getDiagnosticsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "Diagnostics",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 19786L),
        null,
        -1,
        PubSubDiagnosticsConnectionTypeNode.class);
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
  public SelectionListTypeNode getTransportProfileUriNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "TransportProfileUri",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 16309L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        SelectionListTypeNode.class);
  }

  @Override
  public @Nullable String getTransportProfileUri() {
    return ServerNodeSupport.read(this, getTransportProfileUriNode(), String.class, null);
  }

  @Override
  public void setTransportProfileUri(@Nullable String value) {
    ServerNodeSupport.write(this, getTransportProfileUriNode(), value, false, false, false);
  }

  @Override
  public @Nullable ConnectionTransportTypeNode getTransportSettingsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "TransportSettings",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17721L),
        null,
        -1,
        ConnectionTransportTypeNode.class);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getAddressNode();
    getConnectionPropertiesNode();
    getDiagnosticsNode();
    getPublisherIdNode();
    getStatusNode();
    getTransportProfileUriNode();
    getTransportSettingsNode();
  }

  @Override
  public @Nullable UaMethodNode getAddReaderGroupMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "AddReaderGroup",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setAddReaderGroupHandler(
      PubSubConnectionType.@Nullable AddReaderGroupHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this, getAddReaderGroupMethodNode(), "http://opcfoundation.org/UA/", "AddReaderGroup");
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
                    : PubSubConnectionTypeAddReaderGroup.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : PubSubConnectionTypeAddReaderGroup.outputArguments(
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
                PubSubConnectionTypeAddReaderGroup.Inputs input;
                try {
                  input =
                      PubSubConnectionTypeAddReaderGroup.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                PubSubConnectionTypeAddReaderGroup.Outputs output =
                    new PubSubConnectionTypeAddReaderGroup.Outputs(
                        handler.addReaderGroup(context, input.configuration()));
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
  public @Nullable UaMethodNode getAddWriterGroupMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "AddWriterGroup",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setAddWriterGroupHandler(
      PubSubConnectionType.@Nullable AddWriterGroupHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this, getAddWriterGroupMethodNode(), "http://opcfoundation.org/UA/", "AddWriterGroup");
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
                    : PubSubConnectionTypeAddWriterGroup.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : PubSubConnectionTypeAddWriterGroup.outputArguments(
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
                PubSubConnectionTypeAddWriterGroup.Inputs input;
                try {
                  input =
                      PubSubConnectionTypeAddWriterGroup.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                PubSubConnectionTypeAddWriterGroup.Outputs output =
                    new PubSubConnectionTypeAddWriterGroup.Outputs(
                        handler.addWriterGroup(context, input.configuration()));
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
  public @Nullable UaMethodNode getRemoveGroupMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "RemoveGroup",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setRemoveGroupHandler(PubSubConnectionType.@Nullable RemoveGroupHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this, getRemoveGroupMethodNode(), "http://opcfoundation.org/UA/", "RemoveGroup");
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
                    : PubSubConnectionTypeRemoveGroup.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : PubSubConnectionTypeRemoveGroup.outputArguments(
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
                PubSubConnectionTypeRemoveGroup.Inputs input;
                try {
                  input =
                      PubSubConnectionTypeRemoveGroup.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                handler.removeGroup(context, input.groupId());
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
  public void setMethods(PubSubConnectionType.@Nullable Methods methods) {
    if (getAddReaderGroupMethodNode() != null) {
      setAddReaderGroupHandler(methods == null ? null : methods::addReaderGroup);
    }
    if (getAddWriterGroupMethodNode() != null) {
      setAddWriterGroupHandler(methods == null ? null : methods::addWriterGroup);
    }
    if (getRemoveGroupMethodNode() != null) {
      setRemoveGroupHandler(methods == null ? null : methods::removeGroup);
    }
  }
}
