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
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link IOrderedObjectType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.11">Model
 *     documentation</a>
 */
public class IOrderedObjectTypeNode extends BaseInterfaceTypeNode implements IOrderedObjectType {
  public IOrderedObjectTypeNode(
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
  public PropertyTypeNode getNumberInListNode() throws UaException {
    return ClientNodeSupport.await(getNumberInListNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getNumberInListNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "NumberInList",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Variant readNumberInList() throws UaException {
    return ClientNodeSupport.await(readNumberInListAsync());
  }

  @Override
  public void writeNumberInList(@Nullable Variant value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeNumberInListAsync(value)),
        "http://opcfoundation.org/UA/}NumberInList");
  }

  @Override
  public CompletableFuture<? extends @Nullable Variant> readNumberInListAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getNumberInListNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}NumberInList",
                            true,
                            Variant.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Variant) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeNumberInListAsync(@Nullable Variant value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getNumberInListNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}NumberInList",
                        value,
                        Variant.class,
                        -1,
                        null)));
  }
}
