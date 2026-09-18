package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link AuditHistoryAtTimeDeleteEventType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.7">Model
 *     documentation</a>
 */
public class AuditHistoryAtTimeDeleteEventTypeNode extends AuditHistoryDeleteEventTypeNode
    implements AuditHistoryAtTimeDeleteEventType {
  public AuditHistoryAtTimeDeleteEventTypeNode(
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
  public PropertyTypeNode getReqTimesNode() throws UaException {
    return ClientNodeSupport.await(getReqTimesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getReqTimesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ReqTimes",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public DateTime @Nullable [] readReqTimes() throws UaException {
    return ClientNodeSupport.await(readReqTimesAsync());
  }

  @Override
  public void writeReqTimes(DateTime @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeReqTimesAsync(value)),
        "http://opcfoundation.org/UA/}ReqTimes");
  }

  @Override
  public CompletableFuture<? extends DateTime @Nullable []> readReqTimesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getReqTimesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ReqTimes",
                            true,
                            DateTime.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((DateTime @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeReqTimesAsync(DateTime @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getReqTimesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ReqTimes",
                        value,
                        DateTime.class,
                        1,
                        null)));
  }

  @Override
  public PropertyTypeNode getOldValuesNode() throws UaException {
    return ClientNodeSupport.await(getOldValuesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getOldValuesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "OldValues",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public DataValue @Nullable [] readOldValues() throws UaException {
    return ClientNodeSupport.await(readOldValuesAsync());
  }

  @Override
  public void writeOldValues(DataValue @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeOldValuesAsync(value)),
        "http://opcfoundation.org/UA/}OldValues");
  }

  @Override
  public CompletableFuture<? extends DataValue @Nullable []> readOldValuesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getOldValuesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}OldValues",
                            true,
                            DataValue.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((DataValue @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeOldValuesAsync(DataValue @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getOldValuesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}OldValues",
                        value,
                        DataValue.class,
                        1,
                        null)));
  }
}
