package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.AudioVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.AlarmConditionTypeGetGroupMemberships;
import org.eclipse.milo.opcua.sdk.core.model.methods.AlarmConditionTypePlaceInService2;
import org.eclipse.milo.opcua.sdk.core.model.methods.AlarmConditionTypeRemoveFromService2;
import org.eclipse.milo.opcua.sdk.core.model.methods.AlarmConditionTypeReset2;
import org.eclipse.milo.opcua.sdk.core.model.methods.AlarmConditionTypeSuppress2;
import org.eclipse.milo.opcua.sdk.core.model.methods.AlarmConditionTypeUnsuppress2;
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link AlarmConditionType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.2">Model
 *     documentation</a>
 */
public class AlarmConditionTypeNode extends AcknowledgeableConditionTypeNode
    implements AlarmConditionType {
  public AlarmConditionTypeNode(
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
  public TwoStateVariableTypeNode getActiveStateNode() throws UaException {
    return ClientNodeSupport.await(getActiveStateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends TwoStateVariableTypeNode> getActiveStateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ActiveState",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        TwoStateVariableTypeNode.class)));
  }

  @Override
  public @Nullable LocalizedText readActiveState() throws UaException {
    return ClientNodeSupport.await(readActiveStateAsync());
  }

  @Override
  public void writeActiveState(@Nullable LocalizedText value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeActiveStateAsync(value)),
        "http://opcfoundation.org/UA/}ActiveState");
  }

  @Override
  public CompletableFuture<? extends @Nullable LocalizedText> readActiveStateAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getActiveStateNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ActiveState",
                            true,
                            LocalizedText.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LocalizedText) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeActiveStateAsync(@Nullable LocalizedText value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getActiveStateNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ActiveState",
                        value,
                        LocalizedText.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getReAlarmTimeNode() throws UaException {
    return ClientNodeSupport.await(getReAlarmTimeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getReAlarmTimeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ReAlarmTime",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Double readReAlarmTime() throws UaException {
    return ClientNodeSupport.await(readReAlarmTimeAsync());
  }

  @Override
  public void writeReAlarmTime(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeReAlarmTimeAsync(value)),
        "http://opcfoundation.org/UA/}ReAlarmTime");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readReAlarmTimeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getReAlarmTimeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ReAlarmTime",
                            false,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeReAlarmTimeAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getReAlarmTimeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ReAlarmTime",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable AudioVariableTypeNode getAudibleSoundNode() throws UaException {
    return ClientNodeSupport.await(getAudibleSoundNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable AudioVariableTypeNode> getAudibleSoundNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "AudibleSound",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        AudioVariableTypeNode.class)));
  }

  @Override
  public @Nullable ByteString readAudibleSound() throws UaException {
    return ClientNodeSupport.await(readAudibleSoundAsync());
  }

  @Override
  public void writeAudibleSound(@Nullable ByteString value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeAudibleSoundAsync(value)),
        "http://opcfoundation.org/UA/}AudibleSound");
  }

  @Override
  public CompletableFuture<? extends @Nullable ByteString> readAudibleSoundAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getAudibleSoundNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}AudibleSound",
                            false,
                            ByteString.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ByteString) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeAudibleSoundAsync(@Nullable ByteString value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getAudibleSoundNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}AudibleSound",
                        value,
                        ByteString.class,
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
  public @Nullable AlarmGroupTypeNode getFirstInGroupNode() throws UaException {
    return ClientNodeSupport.await(getFirstInGroupNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable AlarmGroupTypeNode> getFirstInGroupNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "FirstInGroup",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        AlarmGroupTypeNode.class)));
  }

  @Override
  public @Nullable TwoStateVariableTypeNode getLatchedStateNode() throws UaException {
    return ClientNodeSupport.await(getLatchedStateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable TwoStateVariableTypeNode>
      getLatchedStateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LatchedState",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        TwoStateVariableTypeNode.class)));
  }

  @Override
  public @Nullable LocalizedText readLatchedState() throws UaException {
    return ClientNodeSupport.await(readLatchedStateAsync());
  }

  @Override
  public void writeLatchedState(@Nullable LocalizedText value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeLatchedStateAsync(value)),
        "http://opcfoundation.org/UA/}LatchedState");
  }

  @Override
  public CompletableFuture<? extends @Nullable LocalizedText> readLatchedStateAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getLatchedStateNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}LatchedState",
                            false,
                            LocalizedText.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LocalizedText) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeLatchedStateAsync(@Nullable LocalizedText value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getLatchedStateNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}LatchedState",
                        value,
                        LocalizedText.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable TwoStateVariableTypeNode getSilenceStateNode() throws UaException {
    return ClientNodeSupport.await(getSilenceStateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable TwoStateVariableTypeNode>
      getSilenceStateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SilenceState",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        TwoStateVariableTypeNode.class)));
  }

  @Override
  public @Nullable LocalizedText readSilenceState() throws UaException {
    return ClientNodeSupport.await(readSilenceStateAsync());
  }

  @Override
  public void writeSilenceState(@Nullable LocalizedText value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSilenceStateAsync(value)),
        "http://opcfoundation.org/UA/}SilenceState");
  }

  @Override
  public CompletableFuture<? extends @Nullable LocalizedText> readSilenceStateAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSilenceStateNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SilenceState",
                            false,
                            LocalizedText.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LocalizedText) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSilenceStateAsync(@Nullable LocalizedText value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSilenceStateNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SilenceState",
                        value,
                        LocalizedText.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable ShelvedStateMachineTypeNode getShelvingStateNode() throws UaException {
    return ClientNodeSupport.await(getShelvingStateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable ShelvedStateMachineTypeNode>
      getShelvingStateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ShelvingState",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        ShelvedStateMachineTypeNode.class)));
  }

  @Override
  public @Nullable PropertyTypeNode getAudibleEnabledNode() throws UaException {
    return ClientNodeSupport.await(getAudibleEnabledNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getAudibleEnabledNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "AudibleEnabled",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readAudibleEnabled() throws UaException {
    return ClientNodeSupport.await(readAudibleEnabledAsync());
  }

  @Override
  public void writeAudibleEnabled(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeAudibleEnabledAsync(value)),
        "http://opcfoundation.org/UA/}AudibleEnabled");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readAudibleEnabledAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getAudibleEnabledNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}AudibleEnabled",
                            false,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeAudibleEnabledAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getAudibleEnabledNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}AudibleEnabled",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxTimeShelvedNode() throws UaException {
    return ClientNodeSupport.await(getMaxTimeShelvedNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxTimeShelvedNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxTimeShelved",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Double readMaxTimeShelved() throws UaException {
    return ClientNodeSupport.await(readMaxTimeShelvedAsync());
  }

  @Override
  public void writeMaxTimeShelved(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxTimeShelvedAsync(value)),
        "http://opcfoundation.org/UA/}MaxTimeShelved");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readMaxTimeShelvedAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxTimeShelvedNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxTimeShelved",
                            false,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxTimeShelvedAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxTimeShelvedNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxTimeShelved",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable TwoStateVariableTypeNode getSuppressedStateNode() throws UaException {
    return ClientNodeSupport.await(getSuppressedStateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable TwoStateVariableTypeNode>
      getSuppressedStateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SuppressedState",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        TwoStateVariableTypeNode.class)));
  }

  @Override
  public @Nullable LocalizedText readSuppressedState() throws UaException {
    return ClientNodeSupport.await(readSuppressedStateAsync());
  }

  @Override
  public void writeSuppressedState(@Nullable LocalizedText value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSuppressedStateAsync(value)),
        "http://opcfoundation.org/UA/}SuppressedState");
  }

  @Override
  public CompletableFuture<? extends @Nullable LocalizedText> readSuppressedStateAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSuppressedStateNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SuppressedState",
                            false,
                            LocalizedText.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LocalizedText) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSuppressedStateAsync(@Nullable LocalizedText value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSuppressedStateNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SuppressedState",
                        value,
                        LocalizedText.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable UaVariableNode getFirstInGroupFlagNode() throws UaException {
    return ClientNodeSupport.await(getFirstInGroupFlagNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable UaVariableNode> getFirstInGroupFlagNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "FirstInGroupFlag",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable Boolean readFirstInGroupFlag() throws UaException {
    return ClientNodeSupport.await(readFirstInGroupFlagAsync());
  }

  @Override
  public void writeFirstInGroupFlag(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeFirstInGroupFlagAsync(value)),
        "http://opcfoundation.org/UA/}FirstInGroupFlag");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readFirstInGroupFlagAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getFirstInGroupFlagNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}FirstInGroupFlag",
                            false,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeFirstInGroupFlagAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getFirstInGroupFlagNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}FirstInGroupFlag",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable TwoStateVariableTypeNode getOutOfServiceStateNode() throws UaException {
    return ClientNodeSupport.await(getOutOfServiceStateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable TwoStateVariableTypeNode>
      getOutOfServiceStateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "OutOfServiceState",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        TwoStateVariableTypeNode.class)));
  }

  @Override
  public @Nullable LocalizedText readOutOfServiceState() throws UaException {
    return ClientNodeSupport.await(readOutOfServiceStateAsync());
  }

  @Override
  public void writeOutOfServiceState(@Nullable LocalizedText value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeOutOfServiceStateAsync(value)),
        "http://opcfoundation.org/UA/}OutOfServiceState");
  }

  @Override
  public CompletableFuture<? extends @Nullable LocalizedText> readOutOfServiceStateAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getOutOfServiceStateNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}OutOfServiceState",
                            false,
                            LocalizedText.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LocalizedText) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeOutOfServiceStateAsync(@Nullable LocalizedText value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getOutOfServiceStateNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}OutOfServiceState",
                        value,
                        LocalizedText.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable UaVariableNode getReAlarmRepeatCountNode() throws UaException {
    return ClientNodeSupport.await(getReAlarmRepeatCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable UaVariableNode> getReAlarmRepeatCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ReAlarmRepeatCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable Short readReAlarmRepeatCount() throws UaException {
    return ClientNodeSupport.await(readReAlarmRepeatCountAsync());
  }

  @Override
  public void writeReAlarmRepeatCount(@Nullable Short value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeReAlarmRepeatCountAsync(value)),
        "http://opcfoundation.org/UA/}ReAlarmRepeatCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable Short> readReAlarmRepeatCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getReAlarmRepeatCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ReAlarmRepeatCount",
                            false,
                            Short.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Short) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeReAlarmRepeatCountAsync(@Nullable Short value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getReAlarmRepeatCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ReAlarmRepeatCount",
                        value,
                        Short.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getSuppressedOrShelvedNode() throws UaException {
    return ClientNodeSupport.await(getSuppressedOrShelvedNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSuppressedOrShelvedNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SuppressedOrShelved",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readSuppressedOrShelved() throws UaException {
    return ClientNodeSupport.await(readSuppressedOrShelvedAsync());
  }

  @Override
  public void writeSuppressedOrShelved(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSuppressedOrShelvedAsync(value)),
        "http://opcfoundation.org/UA/}SuppressedOrShelved");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readSuppressedOrShelvedAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSuppressedOrShelvedNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SuppressedOrShelved",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSuppressedOrShelvedAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSuppressedOrShelvedNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SuppressedOrShelved",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getOnDelayNode() throws UaException {
    return ClientNodeSupport.await(getOnDelayNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getOnDelayNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "OnDelay",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Double readOnDelay() throws UaException {
    return ClientNodeSupport.await(readOnDelayAsync());
  }

  @Override
  public void writeOnDelay(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeOnDelayAsync(value)), "http://opcfoundation.org/UA/}OnDelay");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readOnDelayAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getOnDelayNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}OnDelay",
                            false,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeOnDelayAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getOnDelayNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}OnDelay",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getOffDelayNode() throws UaException {
    return ClientNodeSupport.await(getOffDelayNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getOffDelayNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "OffDelay",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Double readOffDelay() throws UaException {
    return ClientNodeSupport.await(readOffDelayAsync());
  }

  @Override
  public void writeOffDelay(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeOffDelayAsync(value)),
        "http://opcfoundation.org/UA/}OffDelay");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readOffDelayAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getOffDelayNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}OffDelay",
                            false,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeOffDelayAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getOffDelayNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}OffDelay",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getInputNodeNode() throws UaException {
    return ClientNodeSupport.await(getInputNodeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getInputNodeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "InputNode",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable NodeId readInputNode() throws UaException {
    return ClientNodeSupport.await(readInputNodeAsync());
  }

  @Override
  public void writeInputNode(@Nullable NodeId value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeInputNodeAsync(value)),
        "http://opcfoundation.org/UA/}InputNode");
  }

  @Override
  public CompletableFuture<? extends @Nullable NodeId> readInputNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getInputNodeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}InputNode",
                            true,
                            NodeId.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable NodeId) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeInputNodeAsync(@Nullable NodeId value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getInputNodeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}InputNode",
                        value,
                        NodeId.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable UaMethodNode getGetGroupMembershipsMethodNode() throws UaException {
    return ClientNodeSupport.await(getGetGroupMembershipsMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getGetGroupMembershipsMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "GetGroupMemberships",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public NodeId @Nullable [] getGroupMemberships() throws UaException {
    return ClientNodeSupport.await(getGroupMembershipsAsync());
  }

  @Override
  public MethodCallResult<NodeId @Nullable []> callGetGroupMemberships() throws UaException {
    return ClientNodeSupport.await(callGetGroupMembershipsAsync());
  }

  @Override
  public MethodCallResult<NodeId @Nullable []> callGetGroupMembershipsWith(
      MethodCallOptions options) throws UaException {
    return ClientNodeSupport.await(callGetGroupMembershipsWithAsync(options));
  }

  @Override
  public CompletableFuture<NodeId @Nullable []> getGroupMembershipsAsync() {
    return ClientNodeSupport.compose(
        callGetGroupMembershipsAsync(),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<NodeId @Nullable []>> callGetGroupMembershipsAsync() {
    return callGetGroupMembershipsWithAsync(MethodCallOptions.DEFAULT);
  }

  @Override
  public CompletableFuture<MethodCallResult<NodeId @Nullable []>> callGetGroupMembershipsWithAsync(
      MethodCallOptions options) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs = new Variant[0];
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getGetGroupMembershipsMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return AlarmConditionTypeGetGroupMemberships.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values)
                                        .groups();
                                  }))));
        });
  }

  @Override
  public @Nullable UaMethodNode getPlaceInServiceMethodNode() throws UaException {
    return ClientNodeSupport.await(getPlaceInServiceMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getPlaceInServiceMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "PlaceInService",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void placeInService() throws UaException {
    ClientNodeSupport.await(placeInServiceAsync());
  }

  @Override
  public MethodCallResult<Void> callPlaceInService() throws UaException {
    return ClientNodeSupport.await(callPlaceInServiceAsync());
  }

  @Override
  public MethodCallResult<Void> callPlaceInServiceWith(MethodCallOptions options)
      throws UaException {
    return ClientNodeSupport.await(callPlaceInServiceWithAsync(options));
  }

  @Override
  public CompletableFuture<Void> placeInServiceAsync() {
    return ClientNodeSupport.compose(
        callPlaceInServiceAsync(),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callPlaceInServiceAsync() {
    return callPlaceInServiceWithAsync(MethodCallOptions.DEFAULT);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callPlaceInServiceWithAsync(
      MethodCallOptions options) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs = new Variant[0];
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getPlaceInServiceMethodNodeAsync(),
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
  public @Nullable UaMethodNode getPlaceInService2MethodNode() throws UaException {
    return ClientNodeSupport.await(getPlaceInService2MethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getPlaceInService2MethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "PlaceInService2",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void placeInService2(@Nullable LocalizedText comment) throws UaException {
    ClientNodeSupport.await(placeInService2Async(comment));
  }

  @Override
  public MethodCallResult<Void> callPlaceInService2(@Nullable LocalizedText comment)
      throws UaException {
    return ClientNodeSupport.await(callPlaceInService2Async(comment));
  }

  @Override
  public MethodCallResult<Void> callPlaceInService2With(
      MethodCallOptions options, @Nullable LocalizedText comment) throws UaException {
    return ClientNodeSupport.await(callPlaceInService2WithAsync(options, comment));
  }

  @Override
  public CompletableFuture<Void> placeInService2Async(@Nullable LocalizedText comment) {
    return ClientNodeSupport.compose(
        callPlaceInService2Async(comment),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callPlaceInService2Async(
      @Nullable LocalizedText comment) {
    return callPlaceInService2WithAsync(MethodCallOptions.DEFAULT, comment);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callPlaceInService2WithAsync(
      MethodCallOptions options, @Nullable LocalizedText comment) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new AlarmConditionTypePlaceInService2.Inputs(comment)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getPlaceInService2MethodNodeAsync(),
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
  public @Nullable UaMethodNode getRemoveFromServiceMethodNode() throws UaException {
    return ClientNodeSupport.await(getRemoveFromServiceMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getRemoveFromServiceMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "RemoveFromService",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void removeFromService() throws UaException {
    ClientNodeSupport.await(removeFromServiceAsync());
  }

  @Override
  public MethodCallResult<Void> callRemoveFromService() throws UaException {
    return ClientNodeSupport.await(callRemoveFromServiceAsync());
  }

  @Override
  public MethodCallResult<Void> callRemoveFromServiceWith(MethodCallOptions options)
      throws UaException {
    return ClientNodeSupport.await(callRemoveFromServiceWithAsync(options));
  }

  @Override
  public CompletableFuture<Void> removeFromServiceAsync() {
    return ClientNodeSupport.compose(
        callRemoveFromServiceAsync(),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveFromServiceAsync() {
    return callRemoveFromServiceWithAsync(MethodCallOptions.DEFAULT);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveFromServiceWithAsync(
      MethodCallOptions options) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs = new Variant[0];
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getRemoveFromServiceMethodNodeAsync(),
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
  public @Nullable UaMethodNode getRemoveFromService2MethodNode() throws UaException {
    return ClientNodeSupport.await(getRemoveFromService2MethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getRemoveFromService2MethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "RemoveFromService2",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void removeFromService2(@Nullable LocalizedText comment) throws UaException {
    ClientNodeSupport.await(removeFromService2Async(comment));
  }

  @Override
  public MethodCallResult<Void> callRemoveFromService2(@Nullable LocalizedText comment)
      throws UaException {
    return ClientNodeSupport.await(callRemoveFromService2Async(comment));
  }

  @Override
  public MethodCallResult<Void> callRemoveFromService2With(
      MethodCallOptions options, @Nullable LocalizedText comment) throws UaException {
    return ClientNodeSupport.await(callRemoveFromService2WithAsync(options, comment));
  }

  @Override
  public CompletableFuture<Void> removeFromService2Async(@Nullable LocalizedText comment) {
    return ClientNodeSupport.compose(
        callRemoveFromService2Async(comment),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveFromService2Async(
      @Nullable LocalizedText comment) {
    return callRemoveFromService2WithAsync(MethodCallOptions.DEFAULT, comment);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveFromService2WithAsync(
      MethodCallOptions options, @Nullable LocalizedText comment) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new AlarmConditionTypeRemoveFromService2.Inputs(comment)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getRemoveFromService2MethodNodeAsync(),
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
  public @Nullable UaMethodNode getResetMethodNode() throws UaException {
    return ClientNodeSupport.await(getResetMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getResetMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "Reset",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void reset() throws UaException {
    ClientNodeSupport.await(resetAsync());
  }

  @Override
  public MethodCallResult<Void> callReset() throws UaException {
    return ClientNodeSupport.await(callResetAsync());
  }

  @Override
  public MethodCallResult<Void> callResetWith(MethodCallOptions options) throws UaException {
    return ClientNodeSupport.await(callResetWithAsync(options));
  }

  @Override
  public CompletableFuture<Void> resetAsync() {
    return ClientNodeSupport.compose(
        callResetAsync(),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callResetAsync() {
    return callResetWithAsync(MethodCallOptions.DEFAULT);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callResetWithAsync(MethodCallOptions options) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs = new Variant[0];
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getResetMethodNodeAsync(),
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
  public @Nullable UaMethodNode getReset2MethodNode() throws UaException {
    return ClientNodeSupport.await(getReset2MethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getReset2MethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "Reset2",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void reset2(@Nullable LocalizedText comment) throws UaException {
    ClientNodeSupport.await(reset2Async(comment));
  }

  @Override
  public MethodCallResult<Void> callReset2(@Nullable LocalizedText comment) throws UaException {
    return ClientNodeSupport.await(callReset2Async(comment));
  }

  @Override
  public MethodCallResult<Void> callReset2With(
      MethodCallOptions options, @Nullable LocalizedText comment) throws UaException {
    return ClientNodeSupport.await(callReset2WithAsync(options, comment));
  }

  @Override
  public CompletableFuture<Void> reset2Async(@Nullable LocalizedText comment) {
    return ClientNodeSupport.compose(
        callReset2Async(comment),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callReset2Async(
      @Nullable LocalizedText comment) {
    return callReset2WithAsync(MethodCallOptions.DEFAULT, comment);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callReset2WithAsync(
      MethodCallOptions options, @Nullable LocalizedText comment) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new AlarmConditionTypeReset2.Inputs(comment)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getReset2MethodNodeAsync(),
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
  public @Nullable UaMethodNode getSilenceMethodNode() throws UaException {
    return ClientNodeSupport.await(getSilenceMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getSilenceMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "Silence",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void silence() throws UaException {
    ClientNodeSupport.await(silenceAsync());
  }

  @Override
  public MethodCallResult<Void> callSilence() throws UaException {
    return ClientNodeSupport.await(callSilenceAsync());
  }

  @Override
  public MethodCallResult<Void> callSilenceWith(MethodCallOptions options) throws UaException {
    return ClientNodeSupport.await(callSilenceWithAsync(options));
  }

  @Override
  public CompletableFuture<Void> silenceAsync() {
    return ClientNodeSupport.compose(
        callSilenceAsync(),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callSilenceAsync() {
    return callSilenceWithAsync(MethodCallOptions.DEFAULT);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callSilenceWithAsync(MethodCallOptions options) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs = new Variant[0];
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getSilenceMethodNodeAsync(),
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
  public @Nullable UaMethodNode getSuppressMethodNode() throws UaException {
    return ClientNodeSupport.await(getSuppressMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getSuppressMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "Suppress",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void suppress() throws UaException {
    ClientNodeSupport.await(suppressAsync());
  }

  @Override
  public MethodCallResult<Void> callSuppress() throws UaException {
    return ClientNodeSupport.await(callSuppressAsync());
  }

  @Override
  public MethodCallResult<Void> callSuppressWith(MethodCallOptions options) throws UaException {
    return ClientNodeSupport.await(callSuppressWithAsync(options));
  }

  @Override
  public CompletableFuture<Void> suppressAsync() {
    return ClientNodeSupport.compose(
        callSuppressAsync(),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callSuppressAsync() {
    return callSuppressWithAsync(MethodCallOptions.DEFAULT);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callSuppressWithAsync(
      MethodCallOptions options) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs = new Variant[0];
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getSuppressMethodNodeAsync(),
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
  public @Nullable UaMethodNode getSuppress2MethodNode() throws UaException {
    return ClientNodeSupport.await(getSuppress2MethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getSuppress2MethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "Suppress2",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void suppress2(@Nullable LocalizedText comment) throws UaException {
    ClientNodeSupport.await(suppress2Async(comment));
  }

  @Override
  public MethodCallResult<Void> callSuppress2(@Nullable LocalizedText comment) throws UaException {
    return ClientNodeSupport.await(callSuppress2Async(comment));
  }

  @Override
  public MethodCallResult<Void> callSuppress2With(
      MethodCallOptions options, @Nullable LocalizedText comment) throws UaException {
    return ClientNodeSupport.await(callSuppress2WithAsync(options, comment));
  }

  @Override
  public CompletableFuture<Void> suppress2Async(@Nullable LocalizedText comment) {
    return ClientNodeSupport.compose(
        callSuppress2Async(comment),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callSuppress2Async(
      @Nullable LocalizedText comment) {
    return callSuppress2WithAsync(MethodCallOptions.DEFAULT, comment);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callSuppress2WithAsync(
      MethodCallOptions options, @Nullable LocalizedText comment) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new AlarmConditionTypeSuppress2.Inputs(comment)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getSuppress2MethodNodeAsync(),
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
  public @Nullable UaMethodNode getUnsuppressMethodNode() throws UaException {
    return ClientNodeSupport.await(getUnsuppressMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getUnsuppressMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "Unsuppress",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void unsuppress() throws UaException {
    ClientNodeSupport.await(unsuppressAsync());
  }

  @Override
  public MethodCallResult<Void> callUnsuppress() throws UaException {
    return ClientNodeSupport.await(callUnsuppressAsync());
  }

  @Override
  public MethodCallResult<Void> callUnsuppressWith(MethodCallOptions options) throws UaException {
    return ClientNodeSupport.await(callUnsuppressWithAsync(options));
  }

  @Override
  public CompletableFuture<Void> unsuppressAsync() {
    return ClientNodeSupport.compose(
        callUnsuppressAsync(),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callUnsuppressAsync() {
    return callUnsuppressWithAsync(MethodCallOptions.DEFAULT);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callUnsuppressWithAsync(
      MethodCallOptions options) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs = new Variant[0];
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getUnsuppressMethodNodeAsync(),
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
  public @Nullable UaMethodNode getUnsuppress2MethodNode() throws UaException {
    return ClientNodeSupport.await(getUnsuppress2MethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getUnsuppress2MethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "Unsuppress2",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void unsuppress2(@Nullable LocalizedText comment) throws UaException {
    ClientNodeSupport.await(unsuppress2Async(comment));
  }

  @Override
  public MethodCallResult<Void> callUnsuppress2(@Nullable LocalizedText comment)
      throws UaException {
    return ClientNodeSupport.await(callUnsuppress2Async(comment));
  }

  @Override
  public MethodCallResult<Void> callUnsuppress2With(
      MethodCallOptions options, @Nullable LocalizedText comment) throws UaException {
    return ClientNodeSupport.await(callUnsuppress2WithAsync(options, comment));
  }

  @Override
  public CompletableFuture<Void> unsuppress2Async(@Nullable LocalizedText comment) {
    return ClientNodeSupport.compose(
        callUnsuppress2Async(comment),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callUnsuppress2Async(
      @Nullable LocalizedText comment) {
    return callUnsuppress2WithAsync(MethodCallOptions.DEFAULT, comment);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callUnsuppress2WithAsync(
      MethodCallOptions options, @Nullable LocalizedText comment) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new AlarmConditionTypeUnsuppress2.Inputs(comment)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getUnsuppress2MethodNodeAsync(),
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
