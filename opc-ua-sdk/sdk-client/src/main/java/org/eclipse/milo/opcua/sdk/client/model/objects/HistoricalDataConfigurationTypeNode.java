package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ExceptionDeviationFormat;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link HistoricalDataConfigurationType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.2.2">Model
 *     documentation</a>
 */
public class HistoricalDataConfigurationTypeNode extends BaseObjectTypeNode
    implements HistoricalDataConfigurationType {
  public HistoricalDataConfigurationTypeNode(
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
  public @Nullable PropertyTypeNode getDefinitionNode() throws UaException {
    return ClientNodeSupport.await(getDefinitionNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getDefinitionNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Definition",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readDefinition() throws UaException {
    return ClientNodeSupport.await(readDefinitionAsync());
  }

  @Override
  public void writeDefinition(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDefinitionAsync(value)),
        "http://opcfoundation.org/UA/}Definition");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readDefinitionAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDefinitionNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Definition",
                            false,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDefinitionAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDefinitionNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Definition",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getStartOfArchiveNode() throws UaException {
    return ClientNodeSupport.await(getStartOfArchiveNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getStartOfArchiveNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "StartOfArchive",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable DateTime readStartOfArchive() throws UaException {
    return ClientNodeSupport.await(readStartOfArchiveAsync());
  }

  @Override
  public void writeStartOfArchive(@Nullable DateTime value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeStartOfArchiveAsync(value)),
        "http://opcfoundation.org/UA/}StartOfArchive");
  }

  @Override
  public CompletableFuture<? extends @Nullable DateTime> readStartOfArchiveAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getStartOfArchiveNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}StartOfArchive",
                            false,
                            DateTime.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable DateTime) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeStartOfArchiveAsync(@Nullable DateTime value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getStartOfArchiveNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}StartOfArchive",
                        value,
                        DateTime.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxTimeIntervalNode() throws UaException {
    return ClientNodeSupport.await(getMaxTimeIntervalNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxTimeIntervalNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxTimeInterval",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Double readMaxTimeInterval() throws UaException {
    return ClientNodeSupport.await(readMaxTimeIntervalAsync());
  }

  @Override
  public void writeMaxTimeInterval(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxTimeIntervalAsync(value)),
        "http://opcfoundation.org/UA/}MaxTimeInterval");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readMaxTimeIntervalAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxTimeIntervalNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxTimeInterval",
                            false,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxTimeIntervalAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxTimeIntervalNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxTimeInterval",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMinTimeIntervalNode() throws UaException {
    return ClientNodeSupport.await(getMinTimeIntervalNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMinTimeIntervalNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MinTimeInterval",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Double readMinTimeInterval() throws UaException {
    return ClientNodeSupport.await(readMinTimeIntervalAsync());
  }

  @Override
  public void writeMinTimeInterval(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMinTimeIntervalAsync(value)),
        "http://opcfoundation.org/UA/}MinTimeInterval");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readMinTimeIntervalAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMinTimeIntervalNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MinTimeInterval",
                            false,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMinTimeIntervalAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMinTimeIntervalNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MinTimeInterval",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable FolderTypeNode getAggregateFunctionsNode() throws UaException {
    return ClientNodeSupport.await(getAggregateFunctionsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable FolderTypeNode> getAggregateFunctionsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "AggregateFunctions",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        FolderTypeNode.class)));
  }

  @Override
  public @Nullable PropertyTypeNode getExceptionDeviationNode() throws UaException {
    return ClientNodeSupport.await(getExceptionDeviationNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getExceptionDeviationNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ExceptionDeviation",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Double readExceptionDeviation() throws UaException {
    return ClientNodeSupport.await(readExceptionDeviationAsync());
  }

  @Override
  public void writeExceptionDeviation(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeExceptionDeviationAsync(value)),
        "http://opcfoundation.org/UA/}ExceptionDeviation");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readExceptionDeviationAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getExceptionDeviationNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ExceptionDeviation",
                            false,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeExceptionDeviationAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getExceptionDeviationNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ExceptionDeviation",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxTimeStoredValuesNode() throws UaException {
    return ClientNodeSupport.await(getMaxTimeStoredValuesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxTimeStoredValuesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxTimeStoredValues",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Double readMaxTimeStoredValues() throws UaException {
    return ClientNodeSupport.await(readMaxTimeStoredValuesAsync());
  }

  @Override
  public void writeMaxTimeStoredValues(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxTimeStoredValuesAsync(value)),
        "http://opcfoundation.org/UA/}MaxTimeStoredValues");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readMaxTimeStoredValuesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxTimeStoredValuesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxTimeStoredValues",
                            false,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxTimeStoredValuesAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxTimeStoredValuesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxTimeStoredValues",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxCountStoredValuesNode() throws UaException {
    return ClientNodeSupport.await(getMaxCountStoredValuesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxCountStoredValuesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxCountStoredValues",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxCountStoredValues() throws UaException {
    return ClientNodeSupport.await(readMaxCountStoredValuesAsync());
  }

  @Override
  public void writeMaxCountStoredValues(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxCountStoredValuesAsync(value)),
        "http://opcfoundation.org/UA/}MaxCountStoredValues");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxCountStoredValuesAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxCountStoredValuesNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxCountStoredValues",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxCountStoredValuesAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxCountStoredValuesNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxCountStoredValues",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getStartOfOnlineArchiveNode() throws UaException {
    return ClientNodeSupport.await(getStartOfOnlineArchiveNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getStartOfOnlineArchiveNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "StartOfOnlineArchive",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable DateTime readStartOfOnlineArchive() throws UaException {
    return ClientNodeSupport.await(readStartOfOnlineArchiveAsync());
  }

  @Override
  public void writeStartOfOnlineArchive(@Nullable DateTime value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeStartOfOnlineArchiveAsync(value)),
        "http://opcfoundation.org/UA/}StartOfOnlineArchive");
  }

  @Override
  public CompletableFuture<? extends @Nullable DateTime> readStartOfOnlineArchiveAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getStartOfOnlineArchiveNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}StartOfOnlineArchive",
                            false,
                            DateTime.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable DateTime) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeStartOfOnlineArchiveAsync(@Nullable DateTime value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getStartOfOnlineArchiveNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}StartOfOnlineArchive",
                        value,
                        DateTime.class,
                        -1,
                        null)));
  }

  @Override
  public AggregateConfigurationTypeNode getAggregateConfigurationNode() throws UaException {
    return ClientNodeSupport.await(getAggregateConfigurationNodeAsync());
  }

  @Override
  public CompletableFuture<? extends AggregateConfigurationTypeNode>
      getAggregateConfigurationNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "AggregateConfiguration",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        AggregateConfigurationTypeNode.class)));
  }

  @Override
  public @Nullable PropertyTypeNode getExceptionDeviationFormatNode() throws UaException {
    return ClientNodeSupport.await(getExceptionDeviationFormatNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getExceptionDeviationFormatNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ExceptionDeviationFormat",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable ExceptionDeviationFormat readExceptionDeviationFormat() throws UaException {
    return ClientNodeSupport.await(readExceptionDeviationFormatAsync());
  }

  @Override
  public void writeExceptionDeviationFormat(@Nullable ExceptionDeviationFormat value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeExceptionDeviationFormatAsync(value)),
        "http://opcfoundation.org/UA/}ExceptionDeviationFormat");
  }

  @Override
  public CompletableFuture<? extends @Nullable ExceptionDeviationFormat>
      readExceptionDeviationFormatAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getExceptionDeviationFormatNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ExceptionDeviationFormat",
                            false,
                            ExceptionDeviationFormat.class,
                            -1,
                            ExceptionDeviationFormat::from)),
                v -> CompletableFuture.completedFuture((@Nullable ExceptionDeviationFormat) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeExceptionDeviationFormatAsync(
      @Nullable ExceptionDeviationFormat value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getExceptionDeviationFormatNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ExceptionDeviationFormat",
                        value,
                        ExceptionDeviationFormat.class,
                        -1,
                        ExceptionDeviationFormat::from)));
  }

  @Override
  public @Nullable PropertyTypeNode getServerTimestampSupportedNode() throws UaException {
    return ClientNodeSupport.await(getServerTimestampSupportedNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getServerTimestampSupportedNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ServerTimestampSupported",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readServerTimestampSupported() throws UaException {
    return ClientNodeSupport.await(readServerTimestampSupportedAsync());
  }

  @Override
  public void writeServerTimestampSupported(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeServerTimestampSupportedAsync(value)),
        "http://opcfoundation.org/UA/}ServerTimestampSupported");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readServerTimestampSupportedAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getServerTimestampSupportedNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ServerTimestampSupported",
                            false,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeServerTimestampSupportedAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getServerTimestampSupportedNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ServerTimestampSupported",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getSteppedNode() throws UaException {
    return ClientNodeSupport.await(getSteppedNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSteppedNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Stepped",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readStepped() throws UaException {
    return ClientNodeSupport.await(readSteppedAsync());
  }

  @Override
  public void writeStepped(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSteppedAsync(value)), "http://opcfoundation.org/UA/}Stepped");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readSteppedAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSteppedNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Stepped",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSteppedAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSteppedNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Stepped",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }
}
