package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ChassisIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpSystemCapabilitiesMap;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link LldpLocalSystemType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.4">Model
 *     documentation</a>
 */
public class LldpLocalSystemTypeNode extends BaseObjectTypeNode implements LldpLocalSystemType {
  public LldpLocalSystemTypeNode(
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
  public PropertyTypeNode getSystemNameNode() throws UaException {
    return ClientNodeSupport.await(getSystemNameNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSystemNameNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SystemName",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readSystemName() throws UaException {
    return ClientNodeSupport.await(readSystemNameAsync());
  }

  @Override
  public void writeSystemName(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSystemNameAsync(value)),
        "http://opcfoundation.org/UA/}SystemName");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readSystemNameAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSystemNameNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SystemName",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSystemNameAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSystemNameNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SystemName",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getChassisIdSubtypeNode() throws UaException {
    return ClientNodeSupport.await(getChassisIdSubtypeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getChassisIdSubtypeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ChassisIdSubtype",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable ChassisIdSubtype readChassisIdSubtype() throws UaException {
    return ClientNodeSupport.await(readChassisIdSubtypeAsync());
  }

  @Override
  public void writeChassisIdSubtype(@Nullable ChassisIdSubtype value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeChassisIdSubtypeAsync(value)),
        "http://opcfoundation.org/UA/}ChassisIdSubtype");
  }

  @Override
  public CompletableFuture<? extends @Nullable ChassisIdSubtype> readChassisIdSubtypeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getChassisIdSubtypeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ChassisIdSubtype",
                            true,
                            ChassisIdSubtype.class,
                            -1,
                            ChassisIdSubtype::from)),
                v -> CompletableFuture.completedFuture((@Nullable ChassisIdSubtype) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeChassisIdSubtypeAsync(
      @Nullable ChassisIdSubtype value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getChassisIdSubtypeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ChassisIdSubtype",
                        value,
                        ChassisIdSubtype.class,
                        -1,
                        ChassisIdSubtype::from)));
  }

  @Override
  public PropertyTypeNode getSystemDescriptionNode() throws UaException {
    return ClientNodeSupport.await(getSystemDescriptionNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSystemDescriptionNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SystemDescription",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readSystemDescription() throws UaException {
    return ClientNodeSupport.await(readSystemDescriptionAsync());
  }

  @Override
  public void writeSystemDescription(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSystemDescriptionAsync(value)),
        "http://opcfoundation.org/UA/}SystemDescription");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readSystemDescriptionAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSystemDescriptionNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SystemDescription",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSystemDescriptionAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSystemDescriptionNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SystemDescription",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getSystemCapabilitiesEnabledNode() throws UaException {
    return ClientNodeSupport.await(getSystemCapabilitiesEnabledNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getSystemCapabilitiesEnabledNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SystemCapabilitiesEnabled",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable LldpSystemCapabilitiesMap readSystemCapabilitiesEnabled() throws UaException {
    return ClientNodeSupport.await(readSystemCapabilitiesEnabledAsync());
  }

  @Override
  public void writeSystemCapabilitiesEnabled(@Nullable LldpSystemCapabilitiesMap value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSystemCapabilitiesEnabledAsync(value)),
        "http://opcfoundation.org/UA/}SystemCapabilitiesEnabled");
  }

  @Override
  public CompletableFuture<? extends @Nullable LldpSystemCapabilitiesMap>
      readSystemCapabilitiesEnabledAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSystemCapabilitiesEnabledNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SystemCapabilitiesEnabled",
                            false,
                            LldpSystemCapabilitiesMap.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LldpSystemCapabilitiesMap) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSystemCapabilitiesEnabledAsync(
      @Nullable LldpSystemCapabilitiesMap value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSystemCapabilitiesEnabledNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SystemCapabilitiesEnabled",
                        value,
                        LldpSystemCapabilitiesMap.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getSystemCapabilitiesSupportedNode() throws UaException {
    return ClientNodeSupport.await(getSystemCapabilitiesSupportedNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getSystemCapabilitiesSupportedNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SystemCapabilitiesSupported",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable LldpSystemCapabilitiesMap readSystemCapabilitiesSupported() throws UaException {
    return ClientNodeSupport.await(readSystemCapabilitiesSupportedAsync());
  }

  @Override
  public void writeSystemCapabilitiesSupported(@Nullable LldpSystemCapabilitiesMap value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSystemCapabilitiesSupportedAsync(value)),
        "http://opcfoundation.org/UA/}SystemCapabilitiesSupported");
  }

  @Override
  public CompletableFuture<? extends @Nullable LldpSystemCapabilitiesMap>
      readSystemCapabilitiesSupportedAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSystemCapabilitiesSupportedNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SystemCapabilitiesSupported",
                            false,
                            LldpSystemCapabilitiesMap.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LldpSystemCapabilitiesMap) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSystemCapabilitiesSupportedAsync(
      @Nullable LldpSystemCapabilitiesMap value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSystemCapabilitiesSupportedNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SystemCapabilitiesSupported",
                        value,
                        LldpSystemCapabilitiesMap.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getChassisIdNode() throws UaException {
    return ClientNodeSupport.await(getChassisIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getChassisIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ChassisId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readChassisId() throws UaException {
    return ClientNodeSupport.await(readChassisIdAsync());
  }

  @Override
  public void writeChassisId(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeChassisIdAsync(value)),
        "http://opcfoundation.org/UA/}ChassisId");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readChassisIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getChassisIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ChassisId",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeChassisIdAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getChassisIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ChassisId",
                        value,
                        String.class,
                        -1,
                        null)));
  }
}
