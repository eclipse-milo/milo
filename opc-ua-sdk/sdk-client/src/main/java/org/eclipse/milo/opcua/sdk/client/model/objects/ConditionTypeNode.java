package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.ConditionVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.ConditionTypeAddComment;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ConditionType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.2">Model
 *     documentation</a>
 */
public class ConditionTypeNode extends BaseEventTypeNode implements ConditionType {
  public ConditionTypeNode(
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
  public PropertyTypeNode getClientUserIdNode() throws UaException {
    return ClientNodeSupport.await(getClientUserIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getClientUserIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ClientUserId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readClientUserId() throws UaException {
    return ClientNodeSupport.await(readClientUserIdAsync());
  }

  @Override
  public void writeClientUserId(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeClientUserIdAsync(value)),
        "http://opcfoundation.org/UA/}ClientUserId");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readClientUserIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getClientUserIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ClientUserId",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeClientUserIdAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getClientUserIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ClientUserId",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public TwoStateVariableTypeNode getEnabledStateNode() throws UaException {
    return ClientNodeSupport.await(getEnabledStateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends TwoStateVariableTypeNode> getEnabledStateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "EnabledState",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        TwoStateVariableTypeNode.class)));
  }

  @Override
  public @Nullable LocalizedText readEnabledState() throws UaException {
    return ClientNodeSupport.await(readEnabledStateAsync());
  }

  @Override
  public void writeEnabledState(@Nullable LocalizedText value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeEnabledStateAsync(value)),
        "http://opcfoundation.org/UA/}EnabledState");
  }

  @Override
  public CompletableFuture<? extends @Nullable LocalizedText> readEnabledStateAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getEnabledStateNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}EnabledState",
                            true,
                            LocalizedText.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LocalizedText) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeEnabledStateAsync(@Nullable LocalizedText value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getEnabledStateNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}EnabledState",
                        value,
                        LocalizedText.class,
                        -1,
                        null)));
  }

  @Override
  public ConditionVariableTypeNode getLastSeverityNode() throws UaException {
    return ClientNodeSupport.await(getLastSeverityNodeAsync());
  }

  @Override
  public CompletableFuture<? extends ConditionVariableTypeNode> getLastSeverityNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LastSeverity",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        ConditionVariableTypeNode.class)));
  }

  @Override
  public @Nullable UShort readLastSeverity() throws UaException {
    return ClientNodeSupport.await(readLastSeverityAsync());
  }

  @Override
  public void writeLastSeverity(@Nullable UShort value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeLastSeverityAsync(value)),
        "http://opcfoundation.org/UA/}LastSeverity");
  }

  @Override
  public CompletableFuture<? extends @Nullable UShort> readLastSeverityAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getLastSeverityNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}LastSeverity",
                            true,
                            UShort.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UShort) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeLastSeverityAsync(@Nullable UShort value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getLastSeverityNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}LastSeverity",
                        value,
                        UShort.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getConditionNameNode() throws UaException {
    return ClientNodeSupport.await(getConditionNameNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getConditionNameNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ConditionName",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readConditionName() throws UaException {
    return ClientNodeSupport.await(readConditionNameAsync());
  }

  @Override
  public void writeConditionName(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeConditionNameAsync(value)),
        "http://opcfoundation.org/UA/}ConditionName");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readConditionNameAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getConditionNameNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ConditionName",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeConditionNameAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getConditionNameNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ConditionName",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getConditionClassIdNode() throws UaException {
    return ClientNodeSupport.await(getConditionClassIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getConditionClassIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ConditionClassId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public PropertyTypeNode getConditionClassNameNode() throws UaException {
    return ClientNodeSupport.await(getConditionClassNameNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getConditionClassNameNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ConditionClassName",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable PropertyTypeNode getConditionSubClassIdNode() throws UaException {
    return ClientNodeSupport.await(getConditionSubClassIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getConditionSubClassIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ConditionSubClassId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable PropertyTypeNode getConditionSubClassNameNode() throws UaException {
    return ClientNodeSupport.await(getConditionSubClassNameNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getConditionSubClassNameNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ConditionSubClassName",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public PropertyTypeNode getRetainNode() throws UaException {
    return ClientNodeSupport.await(getRetainNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getRetainNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Retain",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readRetain() throws UaException {
    return ClientNodeSupport.await(readRetainAsync());
  }

  @Override
  public void writeRetain(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeRetainAsync(value)), "http://opcfoundation.org/UA/}Retain");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readRetainAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getRetainNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Retain",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeRetainAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getRetainNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Retain",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public ConditionVariableTypeNode getCommentNode() throws UaException {
    return ClientNodeSupport.await(getCommentNodeAsync());
  }

  @Override
  public CompletableFuture<? extends ConditionVariableTypeNode> getCommentNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Comment",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        ConditionVariableTypeNode.class)));
  }

  @Override
  public @Nullable LocalizedText readComment() throws UaException {
    return ClientNodeSupport.await(readCommentAsync());
  }

  @Override
  public void writeComment(@Nullable LocalizedText value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeCommentAsync(value)), "http://opcfoundation.org/UA/}Comment");
  }

  @Override
  public CompletableFuture<? extends @Nullable LocalizedText> readCommentAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getCommentNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Comment",
                            true,
                            LocalizedText.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LocalizedText) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeCommentAsync(@Nullable LocalizedText value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getCommentNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Comment",
                        value,
                        LocalizedText.class,
                        -1,
                        null)));
  }

  @Override
  public ConditionVariableTypeNode getQualityNode() throws UaException {
    return ClientNodeSupport.await(getQualityNodeAsync());
  }

  @Override
  public CompletableFuture<? extends ConditionVariableTypeNode> getQualityNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Quality",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        ConditionVariableTypeNode.class)));
  }

  @Override
  public @Nullable StatusCode readQuality() throws UaException {
    return ClientNodeSupport.await(readQualityAsync());
  }

  @Override
  public void writeQuality(@Nullable StatusCode value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeQualityAsync(value)), "http://opcfoundation.org/UA/}Quality");
  }

  @Override
  public CompletableFuture<? extends @Nullable StatusCode> readQualityAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getQualityNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Quality",
                            true,
                            StatusCode.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable StatusCode) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeQualityAsync(@Nullable StatusCode value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getQualityNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Quality",
                        value,
                        StatusCode.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getBranchIdNode() throws UaException {
    return ClientNodeSupport.await(getBranchIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getBranchIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "BranchId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable NodeId readBranchId() throws UaException {
    return ClientNodeSupport.await(readBranchIdAsync());
  }

  @Override
  public void writeBranchId(@Nullable NodeId value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeBranchIdAsync(value)),
        "http://opcfoundation.org/UA/}BranchId");
  }

  @Override
  public CompletableFuture<? extends @Nullable NodeId> readBranchIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getBranchIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}BranchId",
                            true,
                            NodeId.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable NodeId) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeBranchIdAsync(@Nullable NodeId value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getBranchIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}BranchId",
                        value,
                        NodeId.class,
                        -1,
                        null)));
  }

  @Override
  public UaMethodNode getAddCommentMethodNode() throws UaException {
    return ClientNodeSupport.await(getAddCommentMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getAddCommentMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "AddComment",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void addComment(@Nullable ByteString eventId, @Nullable LocalizedText comment)
      throws UaException {
    ClientNodeSupport.await(addCommentAsync(eventId, comment));
  }

  @Override
  public MethodCallResult<Void> callAddComment(
      @Nullable ByteString eventId, @Nullable LocalizedText comment) throws UaException {
    return ClientNodeSupport.await(callAddCommentAsync(eventId, comment));
  }

  @Override
  public MethodCallResult<Void> callAddCommentWith(
      MethodCallOptions options, @Nullable ByteString eventId, @Nullable LocalizedText comment)
      throws UaException {
    return ClientNodeSupport.await(callAddCommentWithAsync(options, eventId, comment));
  }

  @Override
  public CompletableFuture<Void> addCommentAsync(
      @Nullable ByteString eventId, @Nullable LocalizedText comment) {
    return ClientNodeSupport.compose(
        callAddCommentAsync(eventId, comment),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callAddCommentAsync(
      @Nullable ByteString eventId, @Nullable LocalizedText comment) {
    return callAddCommentWithAsync(MethodCallOptions.DEFAULT, eventId, comment);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callAddCommentWithAsync(
      MethodCallOptions options, @Nullable ByteString eventId, @Nullable LocalizedText comment) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new ConditionTypeAddComment.Inputs(eventId, comment)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getAddCommentMethodNodeAsync(),
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
  public @Nullable UaMethodNode getConditionRefreshMethodNode() throws UaException {
    return ClientNodeSupport.await(getConditionRefreshMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getConditionRefreshMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "ConditionRefresh",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable UaMethodNode getConditionRefresh2MethodNode() throws UaException {
    return ClientNodeSupport.await(getConditionRefresh2MethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getConditionRefresh2MethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "ConditionRefresh2",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public UaMethodNode getDisableMethodNode() throws UaException {
    return ClientNodeSupport.await(getDisableMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getDisableMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "Disable",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void disable() throws UaException {
    ClientNodeSupport.await(disableAsync());
  }

  @Override
  public MethodCallResult<Void> callDisable() throws UaException {
    return ClientNodeSupport.await(callDisableAsync());
  }

  @Override
  public MethodCallResult<Void> callDisableWith(MethodCallOptions options) throws UaException {
    return ClientNodeSupport.await(callDisableWithAsync(options));
  }

  @Override
  public CompletableFuture<Void> disableAsync() {
    return ClientNodeSupport.compose(
        callDisableAsync(),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callDisableAsync() {
    return callDisableWithAsync(MethodCallOptions.DEFAULT);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callDisableWithAsync(MethodCallOptions options) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs = new Variant[0];
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getDisableMethodNodeAsync(),
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
  public UaMethodNode getEnableMethodNode() throws UaException {
    return ClientNodeSupport.await(getEnableMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getEnableMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "Enable",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void enable() throws UaException {
    ClientNodeSupport.await(enableAsync());
  }

  @Override
  public MethodCallResult<Void> callEnable() throws UaException {
    return ClientNodeSupport.await(callEnableAsync());
  }

  @Override
  public MethodCallResult<Void> callEnableWith(MethodCallOptions options) throws UaException {
    return ClientNodeSupport.await(callEnableWithAsync(options));
  }

  @Override
  public CompletableFuture<Void> enableAsync() {
    return ClientNodeSupport.compose(
        callEnableAsync(),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callEnableAsync() {
    return callEnableWithAsync(MethodCallOptions.DEFAULT);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callEnableWithAsync(MethodCallOptions options) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs = new Variant[0];
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getEnableMethodNodeAsync(),
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
