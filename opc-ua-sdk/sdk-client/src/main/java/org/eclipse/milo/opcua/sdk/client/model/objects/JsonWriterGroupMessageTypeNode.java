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
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonNetworkMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link JsonWriterGroupMessageType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.2/#9.2.2.1">Model
 *     documentation</a>
 */
public class JsonWriterGroupMessageTypeNode extends WriterGroupMessageTypeNode
    implements JsonWriterGroupMessageType {
  public JsonWriterGroupMessageTypeNode(
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
  public PropertyTypeNode getNetworkMessageContentMaskNode() throws UaException {
    return ClientNodeSupport.await(getNetworkMessageContentMaskNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getNetworkMessageContentMaskNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "NetworkMessageContentMask",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable JsonNetworkMessageContentMask readNetworkMessageContentMask()
      throws UaException {
    return ClientNodeSupport.await(readNetworkMessageContentMaskAsync());
  }

  @Override
  public void writeNetworkMessageContentMask(@Nullable JsonNetworkMessageContentMask value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeNetworkMessageContentMaskAsync(value)),
        "http://opcfoundation.org/UA/}NetworkMessageContentMask");
  }

  @Override
  public CompletableFuture<? extends @Nullable JsonNetworkMessageContentMask>
      readNetworkMessageContentMaskAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getNetworkMessageContentMaskNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}NetworkMessageContentMask",
                            true,
                            JsonNetworkMessageContentMask.class,
                            -1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable JsonNetworkMessageContentMask) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeNetworkMessageContentMaskAsync(
      @Nullable JsonNetworkMessageContentMask value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getNetworkMessageContentMaskNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}NetworkMessageContentMask",
                        value,
                        JsonNetworkMessageContentMask.class,
                        -1,
                        null)));
  }
}
