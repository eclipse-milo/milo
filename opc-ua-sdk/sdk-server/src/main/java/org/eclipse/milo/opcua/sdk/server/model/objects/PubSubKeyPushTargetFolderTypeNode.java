package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubKeyPushTargetFolderTypeAddPushTarget;
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubKeyPushTargetFolderTypeAddPushTargetFolder;
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubKeyPushTargetFolderTypeRemovePushTarget;
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubKeyPushTargetFolderTypeRemovePushTargetFolder;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.InvalidArgumentException;
import org.eclipse.milo.opcua.sdk.server.methods.MethodArgumentValidator;
import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
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
 * Node implementation of {@link PubSubKeyPushTargetFolderType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.1">Model
 *     documentation</a>
 */
public class PubSubKeyPushTargetFolderTypeNode extends FolderTypeNode
    implements PubSubKeyPushTargetFolderType {
  public PubSubKeyPushTargetFolderTypeNode(
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

  public PubSubKeyPushTargetFolderTypeNode(
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
  public UaMethodNode getAddPushTargetMethodNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        "http://opcfoundation.org/UA/",
        "AddPushTarget",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setAddPushTargetHandler(
      PubSubKeyPushTargetFolderType.@Nullable AddPushTargetHandler handler) {
    UaMethodNode method = getAddPushTargetMethodNode();
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
                    : PubSubKeyPushTargetFolderTypeAddPushTarget.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : PubSubKeyPushTargetFolderTypeAddPushTarget.outputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              protected int getRequiredInputArgumentCount(Argument[] arguments) {
                return 6;
              }

              @Override
              protected Variant[] invoke(
                  AbstractMethodInvocationHandler.InvocationContext context, Variant[] values)
                  throws UaException {
                PubSubKeyPushTargetFolderTypeAddPushTarget.Inputs input;
                try {
                  input =
                      PubSubKeyPushTargetFolderTypeAddPushTarget.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                PubSubKeyPushTargetFolderTypeAddPushTarget.Outputs output =
                    new PubSubKeyPushTargetFolderTypeAddPushTarget.Outputs(
                        handler.addPushTarget(
                            context,
                            input.applicationUri(),
                            input.endpointUrl(),
                            input.securityPolicyUri(),
                            input.userTokenType(),
                            input.requestedKeyCount(),
                            input.retryInterval()));
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
  public @Nullable UaMethodNode getAddPushTargetFolderMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "AddPushTargetFolder",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setAddPushTargetFolderHandler(
      PubSubKeyPushTargetFolderType.@Nullable AddPushTargetFolderHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this,
            getAddPushTargetFolderMethodNode(),
            "http://opcfoundation.org/UA/",
            "AddPushTargetFolder");
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
                    : PubSubKeyPushTargetFolderTypeAddPushTargetFolder.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : PubSubKeyPushTargetFolderTypeAddPushTargetFolder.outputArguments(
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
                PubSubKeyPushTargetFolderTypeAddPushTargetFolder.Inputs input;
                try {
                  input =
                      PubSubKeyPushTargetFolderTypeAddPushTargetFolder.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                PubSubKeyPushTargetFolderTypeAddPushTargetFolder.Outputs output =
                    new PubSubKeyPushTargetFolderTypeAddPushTargetFolder.Outputs(
                        handler.addPushTargetFolder(context, input.name()));
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
  public UaMethodNode getRemovePushTargetMethodNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        "http://opcfoundation.org/UA/",
        "RemovePushTarget",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setRemovePushTargetHandler(
      PubSubKeyPushTargetFolderType.@Nullable RemovePushTargetHandler handler) {
    UaMethodNode method = getRemovePushTargetMethodNode();
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
                    : PubSubKeyPushTargetFolderTypeRemovePushTarget.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : PubSubKeyPushTargetFolderTypeRemovePushTarget.outputArguments(
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
                PubSubKeyPushTargetFolderTypeRemovePushTarget.Inputs input;
                try {
                  input =
                      PubSubKeyPushTargetFolderTypeRemovePushTarget.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                handler.removePushTarget(context, input.pushTargetId());
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
  public @Nullable UaMethodNode getRemovePushTargetFolderMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "RemovePushTargetFolder",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setRemovePushTargetFolderHandler(
      PubSubKeyPushTargetFolderType.@Nullable RemovePushTargetFolderHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this,
            getRemovePushTargetFolderMethodNode(),
            "http://opcfoundation.org/UA/",
            "RemovePushTargetFolder");
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
                    : PubSubKeyPushTargetFolderTypeRemovePushTargetFolder.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : PubSubKeyPushTargetFolderTypeRemovePushTargetFolder.outputArguments(
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
                PubSubKeyPushTargetFolderTypeRemovePushTargetFolder.Inputs input;
                try {
                  input =
                      PubSubKeyPushTargetFolderTypeRemovePushTargetFolder.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                handler.removePushTargetFolder(context, input.pushTargetFolderNodeId());
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
  public void setMethods(PubSubKeyPushTargetFolderType.@Nullable Methods methods) {
    setAddPushTargetHandler(methods == null ? null : methods::addPushTarget);
    if (getAddPushTargetFolderMethodNode() != null) {
      setAddPushTargetFolderHandler(methods == null ? null : methods::addPushTargetFolder);
    }
    setRemovePushTargetHandler(methods == null ? null : methods::removePushTarget);
    if (getRemovePushTargetFolderMethodNode() != null) {
      setRemovePushTargetFolderHandler(methods == null ? null : methods::removePushTargetFolder);
    }
  }
}
