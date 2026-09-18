package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.NumberRange;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link BaseAnalogType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.2/#5.3.2.2">Model
 *     documentation</a>
 */
public class BaseAnalogTypeNode extends DataItemTypeNode implements BaseAnalogType {
  public BaseAnalogTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger @Nullable [] arrayDimensions,
      UByte accessLevel,
      UByte userAccessLevel,
      Double minimumSamplingInterval,
      Boolean historizing,
      @Nullable AccessLevelExType accessLevelEx) {
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
        value,
        dataType,
        valueRank,
        arrayDimensions,
        accessLevel,
        userAccessLevel,
        minimumSamplingInterval,
        historizing,
        accessLevelEx);
  }

  @Override
  public @Nullable PropertyTypeNode getEUNumberRangeNode() throws UaException {
    return ClientNodeSupport.await(getEUNumberRangeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getEUNumberRangeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "EUNumberRange",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable NumberRange readEUNumberRange() throws UaException {
    return ClientNodeSupport.await(readEUNumberRangeAsync());
  }

  @Override
  public void writeEUNumberRange(@Nullable NumberRange value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeEUNumberRangeAsync(value)),
        "http://opcfoundation.org/UA/}EUNumberRange");
  }

  @Override
  public CompletableFuture<? extends @Nullable NumberRange> readEUNumberRangeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getEUNumberRangeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}EUNumberRange",
                            false,
                            NumberRange.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable NumberRange) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeEUNumberRangeAsync(@Nullable NumberRange value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getEUNumberRangeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}EUNumberRange",
                        value,
                        NumberRange.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getInstrumentRangeNode() throws UaException {
    return ClientNodeSupport.await(getInstrumentRangeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getInstrumentRangeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "InstrumentRange",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Range readInstrumentRange() throws UaException {
    return ClientNodeSupport.await(readInstrumentRangeAsync());
  }

  @Override
  public void writeInstrumentRange(@Nullable Range value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeInstrumentRangeAsync(value)),
        "http://opcfoundation.org/UA/}InstrumentRange");
  }

  @Override
  public CompletableFuture<? extends @Nullable Range> readInstrumentRangeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getInstrumentRangeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}InstrumentRange",
                            false,
                            Range.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Range) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeInstrumentRangeAsync(@Nullable Range value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getInstrumentRangeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}InstrumentRange",
                        value,
                        Range.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getEngineeringUnits_Node() throws UaException {
    return ClientNodeSupport.await(getEngineeringUnits_NodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getEngineeringUnits_NodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "EngineeringUnits",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable EUInformation readEngineeringUnits_() throws UaException {
    return ClientNodeSupport.await(readEngineeringUnits_Async());
  }

  @Override
  public void writeEngineeringUnits_(@Nullable EUInformation value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeEngineeringUnits_Async(value)),
        "http://opcfoundation.org/UA/}EngineeringUnits");
  }

  @Override
  public CompletableFuture<? extends @Nullable EUInformation> readEngineeringUnits_Async() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getEngineeringUnits_NodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}EngineeringUnits",
                            false,
                            EUInformation.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable EUInformation) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeEngineeringUnits_Async(@Nullable EUInformation value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getEngineeringUnits_NodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}EngineeringUnits",
                        value,
                        EUInformation.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getInstrumentNumberRangeNode() throws UaException {
    return ClientNodeSupport.await(getInstrumentNumberRangeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getInstrumentNumberRangeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "InstrumentNumberRange",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable NumberRange readInstrumentNumberRange() throws UaException {
    return ClientNodeSupport.await(readInstrumentNumberRangeAsync());
  }

  @Override
  public void writeInstrumentNumberRange(@Nullable NumberRange value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeInstrumentNumberRangeAsync(value)),
        "http://opcfoundation.org/UA/}InstrumentNumberRange");
  }

  @Override
  public CompletableFuture<? extends @Nullable NumberRange> readInstrumentNumberRangeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getInstrumentNumberRangeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}InstrumentNumberRange",
                            false,
                            NumberRange.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable NumberRange) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeInstrumentNumberRangeAsync(
      @Nullable NumberRange value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getInstrumentNumberRangeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}InstrumentNumberRange",
                        value,
                        NumberRange.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getEURangeNode() throws UaException {
    return ClientNodeSupport.await(getEURangeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getEURangeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "EURange",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Range readEURange() throws UaException {
    return ClientNodeSupport.await(readEURangeAsync());
  }

  @Override
  public void writeEURange(@Nullable Range value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeEURangeAsync(value)), "http://opcfoundation.org/UA/}EURange");
  }

  @Override
  public CompletableFuture<? extends @Nullable Range> readEURangeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getEURangeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}EURange",
                            false,
                            Range.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Range) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeEURangeAsync(@Nullable Range value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getEURangeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}EURange",
                        value,
                        Range.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable Variant readTypedValue() throws UaException {
    return ClientNodeSupport.await(readTypedValueAsync());
  }

  @Override
  public void writeTypedValue(@Nullable Variant value) throws UaException {
    ClientNodeSupport.good(ClientNodeSupport.await(writeTypedValueAsync(value)), "Value");
  }

  @Override
  public CompletableFuture<? extends @Nullable Variant> readTypedValueAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    CompletableFuture.completedFuture(this),
                    n ->
                        ClientNodeSupport.read(
                            client, n, this, "Value", true, Variant.class, -2, null)),
                v -> CompletableFuture.completedFuture((@Nullable Variant) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTypedValueAsync(@Nullable Variant value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                n ->
                    ClientNodeSupport.write(
                        client, n, this, "Value", value, Variant.class, -2, null)));
  }
}
