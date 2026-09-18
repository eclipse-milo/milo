package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.model.methods.UserManagementTypeAddUser;
import org.eclipse.milo.opcua.sdk.core.model.methods.UserManagementTypeChangePassword;
import org.eclipse.milo.opcua.sdk.core.model.methods.UserManagementTypeModifyUser;
import org.eclipse.milo.opcua.sdk.core.model.methods.UserManagementTypeRemoveUser;
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
import org.eclipse.milo.opcua.stack.core.types.structured.PasswordOptionsMask;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserManagementDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link UserManagementType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.1">Model
 *     documentation</a>
 */
public class UserManagementTypeNode extends BaseObjectTypeNode implements UserManagementType {
  public UserManagementTypeNode(
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

  public UserManagementTypeNode(
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
  public PropertyTypeNode getPasswordLengthNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "PasswordLength",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 884L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Range getPasswordLength() {
    return ServerNodeSupport.read(this, getPasswordLengthNode(), Range.class, null);
  }

  @Override
  public void setPasswordLength(@Nullable Range value) {
    ServerNodeSupport.write(this, getPasswordLengthNode(), value, false, false, true);
  }

  @Override
  public PropertyTypeNode getPasswordOptionsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "PasswordOptions",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 24277L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable PasswordOptionsMask getPasswordOptions() {
    return ServerNodeSupport.read(this, getPasswordOptionsNode(), PasswordOptionsMask.class, null);
  }

  @Override
  public void setPasswordOptions(@Nullable PasswordOptionsMask value) {
    ServerNodeSupport.write(this, getPasswordOptionsNode(), value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getPasswordRestrictionsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "PasswordRestrictions",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable LocalizedText getPasswordRestrictions() {
    return ServerNodeSupport.read(this, getPasswordRestrictionsNode(), LocalizedText.class, null);
  }

  @Override
  public void setPasswordRestrictions(@Nullable LocalizedText value) {
    ServerNodeSupport.write(
        this,
        getPasswordRestrictionsNode(),
        Namespaces.OPC_UA,
        "PasswordRestrictions",
        value,
        false,
        false,
        false);
  }

  @Override
  public PropertyTypeNode getUsersNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Users",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 24281L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UserManagementDataType @Nullable [] getUsers() {
    return ServerNodeSupport.readArray(this, getUsersNode(), UserManagementDataType.class, null);
  }

  @Override
  public void setUsers(@Nullable UserManagementDataType @Nullable [] value) {
    ServerNodeSupport.write(this, getUsersNode(), value, true, false, true);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getPasswordLengthNode();
    getPasswordOptionsNode();
    getPasswordRestrictionsNode();
    getUsersNode();
  }

  @Override
  public UaMethodNode getAddUserMethodNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        "http://opcfoundation.org/UA/",
        "AddUser",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setAddUserHandler(UserManagementType.@Nullable AddUserHandler handler) {
    UaMethodNode method = getAddUserMethodNode();
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
                    : UserManagementTypeAddUser.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : UserManagementTypeAddUser.outputArguments(
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
                UserManagementTypeAddUser.Inputs input;
                try {
                  input =
                      UserManagementTypeAddUser.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                handler.addUser(
                    context,
                    input.userName(),
                    input.password(),
                    input.userConfiguration(),
                    input.description());
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
  public UaMethodNode getChangePasswordMethodNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        "http://opcfoundation.org/UA/",
        "ChangePassword",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setChangePasswordHandler(UserManagementType.@Nullable ChangePasswordHandler handler) {
    UaMethodNode method = getChangePasswordMethodNode();
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
                    : UserManagementTypeChangePassword.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : UserManagementTypeChangePassword.outputArguments(
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
                UserManagementTypeChangePassword.Inputs input;
                try {
                  input =
                      UserManagementTypeChangePassword.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                handler.changePassword(context, input.oldPassword(), input.newPassword());
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
  public UaMethodNode getModifyUserMethodNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        "http://opcfoundation.org/UA/",
        "ModifyUser",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setModifyUserHandler(UserManagementType.@Nullable ModifyUserHandler handler) {
    UaMethodNode method = getModifyUserMethodNode();
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
                    : UserManagementTypeModifyUser.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : UserManagementTypeModifyUser.outputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              protected int getRequiredInputArgumentCount(Argument[] arguments) {
                return 7;
              }

              @Override
              protected Variant[] invoke(
                  AbstractMethodInvocationHandler.InvocationContext context, Variant[] values)
                  throws UaException {
                UserManagementTypeModifyUser.Inputs input;
                try {
                  input =
                      UserManagementTypeModifyUser.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                handler.modifyUser(
                    context,
                    input.userName(),
                    input.modifyPassword(),
                    input.password(),
                    input.modifyUserConfiguration(),
                    input.userConfiguration(),
                    input.modifyDescription(),
                    input.description());
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
  public UaMethodNode getRemoveUserMethodNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        "http://opcfoundation.org/UA/",
        "RemoveUser",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.NULL_VALUE,
        null,
        -1,
        UaMethodNode.class);
  }

  @Override
  public void setRemoveUserHandler(UserManagementType.@Nullable RemoveUserHandler handler) {
    UaMethodNode method = getRemoveUserMethodNode();
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
                    : UserManagementTypeRemoveUser.inputArguments(
                        getNodeContext().getServer().getNamespaceTable());
              }

              @Override
              public Argument[] getOutputArguments() {
                Argument[] declared = method.getOutputArguments();
                return declared != null
                    ? declared
                    : UserManagementTypeRemoveUser.outputArguments(
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
                UserManagementTypeRemoveUser.Inputs input;
                try {
                  input =
                      UserManagementTypeRemoveUser.Inputs.fromVariants(
                          getNodeContext().getServer().getStaticEncodingContext(), values);
                } catch (UaArgumentConversionException failure) {
                  throw InvalidArgumentException.builder()
                      .argument(
                          failure.getArgumentIndex(),
                          StatusCodes.Bad_TypeMismatch,
                          failure.getMessage())
                      .build();
                }
                handler.removeUser(context, input.userName());
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
  public void setMethods(UserManagementType.@Nullable Methods methods) {
    setAddUserHandler(methods == null ? null : methods::addUser);
    setChangePasswordHandler(methods == null ? null : methods::changePassword);
    setModifyUserHandler(methods == null ? null : methods::modifyUser);
    setRemoveUserHandler(methods == null ? null : methods::removeUser);
  }
}
