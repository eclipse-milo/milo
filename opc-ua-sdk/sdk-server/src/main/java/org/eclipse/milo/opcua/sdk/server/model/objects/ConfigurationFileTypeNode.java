package org.eclipse.milo.opcua.sdk.server.model.objects;

import java.util.Objects;
import org.eclipse.milo.opcua.sdk.core.model.methods.ConfigurationFileTypeCloseAndUpdate;
import org.eclipse.milo.opcua.sdk.core.model.methods.ConfigurationFileTypeConfirmUpdate;
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
 * Node implementation of {@link ConfigurationFileType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.1">Model
 *     documentation</a>
 */
public class ConfigurationFileTypeNode extends FileTypeNode implements ConfigurationFileType {
  public ConfigurationFileTypeNode(
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

  public ConfigurationFileTypeNode(
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
  public PropertyTypeNode getActivityTimeoutNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ActivityTimeout",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getActivityTimeout() {
    return ServerNodeSupport.read(this, getActivityTimeoutNode(), Double.class, null);
  }

  @Override
  public void setActivityTimeout(@Nullable Double value) {
    ServerNodeSupport.write(this, getActivityTimeoutNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getCurrentVersionNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "CurrentVersion",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 20998L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getCurrentVersion() {
    return ServerNodeSupport.read(this, getCurrentVersionNode(), UInteger.class, null);
  }

  @Override
  public void setCurrentVersion(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getCurrentVersionNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getLastUpdateTimeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "LastUpdateTime",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable DateTime getLastUpdateTime() {
    return ServerNodeSupport.read(this, getLastUpdateTimeNode(), DateTime.class, null);
  }

  @Override
  public void setLastUpdateTime(@Nullable DateTime value) {
    ServerNodeSupport.write(this, getLastUpdateTimeNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getSupportedDataTypeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SupportedDataType",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable NodeId getSupportedDataType() {
    return ServerNodeSupport.read(this, getSupportedDataTypeNode(), NodeId.class, null);
  }

  @Override
  public void setSupportedDataType(@Nullable NodeId value) {
    ServerNodeSupport.write(this, getSupportedDataTypeNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getActivityTimeoutNode();
    getCurrentVersionNode();
    getLastUpdateTimeNode();
    getSupportedDataTypeNode();
  }

  @Override
  public UaMethodNode getCloseAndUpdateMethodNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        "http://opcfoundation.org/UA/",
        "CloseAndUpdate",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setCloseAndUpdateHandler(
      ConfigurationFileType.@Nullable CloseAndUpdateHandler handler) {
    UaMethodNode method = getCloseAndUpdateMethodNode();
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
                    : ConfigurationFileTypeCloseAndUpdate.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : ConfigurationFileTypeCloseAndUpdate.outputArguments(
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
                ConfigurationFileTypeCloseAndUpdate.Inputs input;
                try {
                  input =
                      ConfigurationFileTypeCloseAndUpdate.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                ConfigurationFileTypeCloseAndUpdate.Outputs output =
                    Objects.requireNonNull(
                        handler.closeAndUpdate(
                            context,
                            input.fileHandle(),
                            input.versionToUpdate(),
                            input.targets(),
                            input.revertAfterTime(),
                            input.restartDelayTime()),
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
  public UaMethodNode getConfirmUpdateMethodNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        "http://opcfoundation.org/UA/",
        "ConfirmUpdate",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setConfirmUpdateHandler(
      ConfigurationFileType.@Nullable ConfirmUpdateHandler handler) {
    UaMethodNode method = getConfirmUpdateMethodNode();
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
                    : ConfigurationFileTypeConfirmUpdate.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : ConfigurationFileTypeConfirmUpdate.outputArguments(
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
                ConfigurationFileTypeConfirmUpdate.Inputs input;
                try {
                  input =
                      ConfigurationFileTypeConfirmUpdate.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                handler.confirmUpdate(context, input.updateId());
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
  public void setMethods(ConfigurationFileType.@Nullable Methods methods) {
    setCloseHandler(methods == null ? null : methods::close);
    setCloseAndUpdateHandler(methods == null ? null : methods::closeAndUpdate);
    setConfirmUpdateHandler(methods == null ? null : methods::confirmUpdate);
    setGetPositionHandler(methods == null ? null : methods::getPosition);
    setOpenHandler(methods == null ? null : methods::open);
    setReadHandler(methods == null ? null : methods::read);
    setSetPositionHandler(methods == null ? null : methods::setPosition);
    setWriteHandler(methods == null ? null : methods::write);
  }
}
