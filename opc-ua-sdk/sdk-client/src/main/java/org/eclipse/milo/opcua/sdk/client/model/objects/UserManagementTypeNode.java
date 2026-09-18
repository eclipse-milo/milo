package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.UserManagementTypeAddUser;
import org.eclipse.milo.opcua.sdk.core.model.methods.UserManagementTypeChangePassword;
import org.eclipse.milo.opcua.sdk.core.model.methods.UserManagementTypeModifyUser;
import org.eclipse.milo.opcua.sdk.core.model.methods.UserManagementTypeRemoveUser;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.PasswordOptionsMask;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserConfigurationMask;
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
      OpcUaClient client,
      NodeId nodeId,
      NodeClass nodeClass,
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
        client,
        nodeId,
        nodeClass,
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
  public PropertyTypeNode getPasswordLengthNode() throws UaException {
    return ClientNodeSupport.await(getPasswordLengthNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPasswordLengthNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "PasswordLength",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Range readPasswordLength() throws UaException {
    return ClientNodeSupport.await(readPasswordLengthAsync());
  }

  @Override
  public void writePasswordLength(@Nullable Range value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writePasswordLengthAsync(value)),
        "http://opcfoundation.org/UA/}PasswordLength");
  }

  @Override
  public CompletableFuture<? extends @Nullable Range> readPasswordLengthAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getPasswordLengthNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}PasswordLength",
                            true,
                            Range.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Range) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writePasswordLengthAsync(@Nullable Range value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getPasswordLengthNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}PasswordLength",
                        value,
                        Range.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getPasswordOptionsNode() throws UaException {
    return ClientNodeSupport.await(getPasswordOptionsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPasswordOptionsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "PasswordOptions",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable PasswordOptionsMask readPasswordOptions() throws UaException {
    return ClientNodeSupport.await(readPasswordOptionsAsync());
  }

  @Override
  public void writePasswordOptions(@Nullable PasswordOptionsMask value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writePasswordOptionsAsync(value)),
        "http://opcfoundation.org/UA/}PasswordOptions");
  }

  @Override
  public CompletableFuture<? extends @Nullable PasswordOptionsMask> readPasswordOptionsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getPasswordOptionsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}PasswordOptions",
                            true,
                            PasswordOptionsMask.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable PasswordOptionsMask) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writePasswordOptionsAsync(
      @Nullable PasswordOptionsMask value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getPasswordOptionsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}PasswordOptions",
                        value,
                        PasswordOptionsMask.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getPasswordRestrictionsNode() throws UaException {
    return ClientNodeSupport.await(getPasswordRestrictionsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getPasswordRestrictionsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "PasswordRestrictions",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable LocalizedText readPasswordRestrictions() throws UaException {
    return ClientNodeSupport.await(readPasswordRestrictionsAsync());
  }

  @Override
  public void writePasswordRestrictions(@Nullable LocalizedText value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writePasswordRestrictionsAsync(value)),
        "http://opcfoundation.org/UA/}PasswordRestrictions");
  }

  @Override
  public CompletableFuture<? extends @Nullable LocalizedText> readPasswordRestrictionsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getPasswordRestrictionsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}PasswordRestrictions",
                            false,
                            LocalizedText.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LocalizedText) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writePasswordRestrictionsAsync(
      @Nullable LocalizedText value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getPasswordRestrictionsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}PasswordRestrictions",
                        value,
                        LocalizedText.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getUsersNode() throws UaException {
    return ClientNodeSupport.await(getUsersNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUsersNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Users",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UserManagementDataType @Nullable [] readUsers() throws UaException {
    return ClientNodeSupport.await(readUsersAsync());
  }

  @Override
  public void writeUsers(@Nullable UserManagementDataType @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeUsersAsync(value)), "http://opcfoundation.org/UA/}Users");
  }

  @Override
  public CompletableFuture<? extends @Nullable UserManagementDataType @Nullable []>
      readUsersAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getUsersNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Users",
                            true,
                            UserManagementDataType.class,
                            1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable UserManagementDataType @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeUsersAsync(
      @Nullable UserManagementDataType @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getUsersNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Users",
                        value,
                        UserManagementDataType.class,
                        1,
                        null)));
  }

  @Override
  public UaMethodNode getAddUserMethodNode() throws UaException {
    return ClientNodeSupport.await(getAddUserMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getAddUserMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "AddUser",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void addUser(
      @Nullable String userName,
      @Nullable String password,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable String description)
      throws UaException {
    ClientNodeSupport.await(addUserAsync(userName, password, userConfiguration, description));
  }

  @Override
  public MethodCallResult<Void> callAddUser(
      @Nullable String userName,
      @Nullable String password,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable String description)
      throws UaException {
    return ClientNodeSupport.await(
        callAddUserAsync(userName, password, userConfiguration, description));
  }

  @Override
  public MethodCallResult<Void> callAddUserWith(
      MethodCallOptions options,
      @Nullable String userName,
      @Nullable String password,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable String description)
      throws UaException {
    return ClientNodeSupport.await(
        callAddUserWithAsync(options, userName, password, userConfiguration, description));
  }

  @Override
  public CompletableFuture<Void> addUserAsync(
      @Nullable String userName,
      @Nullable String password,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable String description) {
    return ClientNodeSupport.compose(
        callAddUserAsync(userName, password, userConfiguration, description),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callAddUserAsync(
      @Nullable String userName,
      @Nullable String password,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable String description) {
    return callAddUserWithAsync(
        MethodCallOptions.DEFAULT, userName, password, userConfiguration, description);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callAddUserWithAsync(
      MethodCallOptions options,
      @Nullable String userName,
      @Nullable String password,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable String description) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new UserManagementTypeAddUser.Inputs(
                      userName, password, userConfiguration, description)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getAddUserMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    if (values == null || values.length != 0)
                                      throw new UaException(
                                          StatusCodes.Bad_DecodingError,
                                          "expected no Method outputs");
                                    return null;
                                  }))));
        });
  }

  @Override
  public UaMethodNode getChangePasswordMethodNode() throws UaException {
    return ClientNodeSupport.await(getChangePasswordMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getChangePasswordMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "ChangePassword",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void changePassword(@Nullable String oldPassword, @Nullable String newPassword)
      throws UaException {
    ClientNodeSupport.await(changePasswordAsync(oldPassword, newPassword));
  }

  @Override
  public MethodCallResult<Void> callChangePassword(
      @Nullable String oldPassword, @Nullable String newPassword) throws UaException {
    return ClientNodeSupport.await(callChangePasswordAsync(oldPassword, newPassword));
  }

  @Override
  public MethodCallResult<Void> callChangePasswordWith(
      MethodCallOptions options, @Nullable String oldPassword, @Nullable String newPassword)
      throws UaException {
    return ClientNodeSupport.await(callChangePasswordWithAsync(options, oldPassword, newPassword));
  }

  @Override
  public CompletableFuture<Void> changePasswordAsync(
      @Nullable String oldPassword, @Nullable String newPassword) {
    return ClientNodeSupport.compose(
        callChangePasswordAsync(oldPassword, newPassword),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callChangePasswordAsync(
      @Nullable String oldPassword, @Nullable String newPassword) {
    return callChangePasswordWithAsync(MethodCallOptions.DEFAULT, oldPassword, newPassword);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callChangePasswordWithAsync(
      MethodCallOptions options, @Nullable String oldPassword, @Nullable String newPassword) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new UserManagementTypeChangePassword.Inputs(oldPassword, newPassword)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getChangePasswordMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    if (values == null || values.length != 0)
                                      throw new UaException(
                                          StatusCodes.Bad_DecodingError,
                                          "expected no Method outputs");
                                    return null;
                                  }))));
        });
  }

  @Override
  public UaMethodNode getModifyUserMethodNode() throws UaException {
    return ClientNodeSupport.await(getModifyUserMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getModifyUserMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "ModifyUser",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void modifyUser(
      @Nullable String userName,
      @Nullable Boolean modifyPassword,
      @Nullable String password,
      @Nullable Boolean modifyUserConfiguration,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable Boolean modifyDescription,
      @Nullable String description)
      throws UaException {
    ClientNodeSupport.await(
        modifyUserAsync(
            userName,
            modifyPassword,
            password,
            modifyUserConfiguration,
            userConfiguration,
            modifyDescription,
            description));
  }

  @Override
  public MethodCallResult<Void> callModifyUser(
      @Nullable String userName,
      @Nullable Boolean modifyPassword,
      @Nullable String password,
      @Nullable Boolean modifyUserConfiguration,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable Boolean modifyDescription,
      @Nullable String description)
      throws UaException {
    return ClientNodeSupport.await(
        callModifyUserAsync(
            userName,
            modifyPassword,
            password,
            modifyUserConfiguration,
            userConfiguration,
            modifyDescription,
            description));
  }

  @Override
  public MethodCallResult<Void> callModifyUserWith(
      MethodCallOptions options,
      @Nullable String userName,
      @Nullable Boolean modifyPassword,
      @Nullable String password,
      @Nullable Boolean modifyUserConfiguration,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable Boolean modifyDescription,
      @Nullable String description)
      throws UaException {
    return ClientNodeSupport.await(
        callModifyUserWithAsync(
            options,
            userName,
            modifyPassword,
            password,
            modifyUserConfiguration,
            userConfiguration,
            modifyDescription,
            description));
  }

  @Override
  public CompletableFuture<Void> modifyUserAsync(
      @Nullable String userName,
      @Nullable Boolean modifyPassword,
      @Nullable String password,
      @Nullable Boolean modifyUserConfiguration,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable Boolean modifyDescription,
      @Nullable String description) {
    return ClientNodeSupport.compose(
        callModifyUserAsync(
            userName,
            modifyPassword,
            password,
            modifyUserConfiguration,
            userConfiguration,
            modifyDescription,
            description),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callModifyUserAsync(
      @Nullable String userName,
      @Nullable Boolean modifyPassword,
      @Nullable String password,
      @Nullable Boolean modifyUserConfiguration,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable Boolean modifyDescription,
      @Nullable String description) {
    return callModifyUserWithAsync(
        MethodCallOptions.DEFAULT,
        userName,
        modifyPassword,
        password,
        modifyUserConfiguration,
        userConfiguration,
        modifyDescription,
        description);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callModifyUserWithAsync(
      MethodCallOptions options,
      @Nullable String userName,
      @Nullable Boolean modifyPassword,
      @Nullable String password,
      @Nullable Boolean modifyUserConfiguration,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable Boolean modifyDescription,
      @Nullable String description) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new UserManagementTypeModifyUser.Inputs(
                      userName,
                      modifyPassword,
                      password,
                      modifyUserConfiguration,
                      userConfiguration,
                      modifyDescription,
                      description)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getModifyUserMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    if (values == null || values.length != 0)
                                      throw new UaException(
                                          StatusCodes.Bad_DecodingError,
                                          "expected no Method outputs");
                                    return null;
                                  }))));
        });
  }

  @Override
  public UaMethodNode getRemoveUserMethodNode() throws UaException {
    return ClientNodeSupport.await(getRemoveUserMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getRemoveUserMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "RemoveUser",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void removeUser(@Nullable String userName) throws UaException {
    ClientNodeSupport.await(removeUserAsync(userName));
  }

  @Override
  public MethodCallResult<Void> callRemoveUser(@Nullable String userName) throws UaException {
    return ClientNodeSupport.await(callRemoveUserAsync(userName));
  }

  @Override
  public MethodCallResult<Void> callRemoveUserWith(
      MethodCallOptions options, @Nullable String userName) throws UaException {
    return ClientNodeSupport.await(callRemoveUserWithAsync(options, userName));
  }

  @Override
  public CompletableFuture<Void> removeUserAsync(@Nullable String userName) {
    return ClientNodeSupport.compose(
        callRemoveUserAsync(userName),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveUserAsync(@Nullable String userName) {
    return callRemoveUserWithAsync(MethodCallOptions.DEFAULT, userName);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveUserWithAsync(
      MethodCallOptions options, @Nullable String userName) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new UserManagementTypeRemoveUser.Inputs(userName)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getRemoveUserMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    if (values == null || values.length != 0)
                                      throw new UaException(
                                          StatusCodes.Bad_DecodingError,
                                          "expected no Method outputs");
                                    return null;
                                  }))));
        });
  }
}
