package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.model.methods.AliasNameCategoryTypeAddAliasesToCategory;
import org.eclipse.milo.opcua.sdk.core.model.methods.AliasNameCategoryTypeDeleteAliasesFromCategory;
import org.eclipse.milo.opcua.sdk.core.model.methods.AliasNameCategoryTypeFindAlias;
import org.eclipse.milo.opcua.sdk.core.model.methods.AliasNameCategoryTypeFindAliasVerbose;
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
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link AliasNameCategoryType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.1">Model
 *     documentation</a>
 */
public class AliasNameCategoryTypeNode extends FolderTypeNode implements AliasNameCategoryType {
  public AliasNameCategoryTypeNode(
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

  public AliasNameCategoryTypeNode(
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
  public @Nullable PropertyTypeNode getLastChangeNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "LastChange",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 20998L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getLastChange() {
    return ServerNodeSupport.read(this, getLastChangeNode(), UInteger.class, null);
  }

  @Override
  public void setLastChange(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this, getLastChangeNode(), Namespaces.OPC_UA, "LastChange", value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getLastChangeNode();
  }

  @Override
  public @Nullable UaMethodNode getAddAliasesToCategoryMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "AddAliasesToCategory",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setAddAliasesToCategoryHandler(
      AliasNameCategoryType.@Nullable AddAliasesToCategoryHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this,
            getAddAliasesToCategoryMethodNode(),
            "http://opcfoundation.org/UA/",
            "AddAliasesToCategory");
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
                    : AliasNameCategoryTypeAddAliasesToCategory.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : AliasNameCategoryTypeAddAliasesToCategory.outputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              protected int getRequiredInputArgumentCount(Argument[] arguments) {
                return 4;
              }

              @Override
              protected Variant[] invoke(
                  AbstractMethodInvocationHandler.InvocationContext context, Variant[] values)
                  throws UaException {
                AliasNameCategoryTypeAddAliasesToCategory.Inputs input;
                try {
                  input =
                      AliasNameCategoryTypeAddAliasesToCategory.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                AliasNameCategoryTypeAddAliasesToCategory.Outputs output =
                    new AliasNameCategoryTypeAddAliasesToCategory.Outputs(
                        handler.addAliasesToCategory(
                            context,
                            input.aliasNames(),
                            input.targetNodes(),
                            input.targetServers(),
                            input.targetReferenceType()));
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
  public @Nullable UaMethodNode getDeleteAliasesFromCategoryMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "DeleteAliasesFromCategory",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setDeleteAliasesFromCategoryHandler(
      AliasNameCategoryType.@Nullable DeleteAliasesFromCategoryHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this,
            getDeleteAliasesFromCategoryMethodNode(),
            "http://opcfoundation.org/UA/",
            "DeleteAliasesFromCategory");
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
                    : AliasNameCategoryTypeDeleteAliasesFromCategory.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : AliasNameCategoryTypeDeleteAliasesFromCategory.outputArguments(
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
                AliasNameCategoryTypeDeleteAliasesFromCategory.Inputs input;
                try {
                  input =
                      AliasNameCategoryTypeDeleteAliasesFromCategory.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                AliasNameCategoryTypeDeleteAliasesFromCategory.Outputs output =
                    new AliasNameCategoryTypeDeleteAliasesFromCategory.Outputs(
                        handler.deleteAliasesFromCategory(
                            context, input.aliasNames(), input.targetNodes()));
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
  public UaMethodNode getFindAliasMethodNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        "http://opcfoundation.org/UA/",
        "FindAlias",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setFindAliasHandler(AliasNameCategoryType.@Nullable FindAliasHandler handler) {
    UaMethodNode method = getFindAliasMethodNode();
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
                    : AliasNameCategoryTypeFindAlias.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : AliasNameCategoryTypeFindAlias.outputArguments(
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
                AliasNameCategoryTypeFindAlias.Inputs input;
                try {
                  input =
                      AliasNameCategoryTypeFindAlias.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                AliasNameCategoryTypeFindAlias.Outputs output =
                    new AliasNameCategoryTypeFindAlias.Outputs(
                        handler.findAlias(
                            context, input.aliasNameSearchPattern(), input.referenceTypeFilter()));
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
  public @Nullable UaMethodNode getFindAliasVerboseMethodNode() {
    return ServerNodeSupport.optionalChild(
        this,
        "http://opcfoundation.org/UA/",
        "FindAliasVerbose",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setFindAliasVerboseHandler(
      AliasNameCategoryType.@Nullable FindAliasVerboseHandler handler) {
    UaMethodNode method =
        ServerNodeSupport.present(
            this,
            getFindAliasVerboseMethodNode(),
            "http://opcfoundation.org/UA/",
            "FindAliasVerbose");
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
                    : AliasNameCategoryTypeFindAliasVerbose.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : AliasNameCategoryTypeFindAliasVerbose.outputArguments(
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
                AliasNameCategoryTypeFindAliasVerbose.Inputs input;
                try {
                  input =
                      AliasNameCategoryTypeFindAliasVerbose.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                AliasNameCategoryTypeFindAliasVerbose.Outputs output =
                    new AliasNameCategoryTypeFindAliasVerbose.Outputs(
                        handler.findAliasVerbose(
                            context, input.aliasNameSearchPattern(), input.referenceTypeFilter()));
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
  public void setMethods(AliasNameCategoryType.@Nullable Methods methods) {
    if (getAddAliasesToCategoryMethodNode() != null) {
      setAddAliasesToCategoryHandler(methods == null ? null : methods::addAliasesToCategory);
    }
    if (getDeleteAliasesFromCategoryMethodNode() != null) {
      setDeleteAliasesFromCategoryHandler(
          methods == null ? null : methods::deleteAliasesFromCategory);
    }
    setFindAliasHandler(methods == null ? null : methods::findAlias);
    if (getFindAliasVerboseMethodNode() != null) {
      setFindAliasVerboseHandler(methods == null ? null : methods::findAliasVerbose);
    }
  }
}
