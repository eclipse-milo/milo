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
import org.eclipse.milo.opcua.stack.core.types.enumerated.AxisScaleEnumeration;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ArrayItemType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.4/#5.3.4.1">Model
 *     documentation</a>
 */
public class ArrayItemTypeNode extends DataItemTypeNode implements ArrayItemType {
  public ArrayItemTypeNode(
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
  public PropertyTypeNode getAxisScaleTypeNode() throws UaException {
    return ClientNodeSupport.await(getAxisScaleTypeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getAxisScaleTypeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "AxisScaleType",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable AxisScaleEnumeration readAxisScaleType() throws UaException {
    return ClientNodeSupport.await(readAxisScaleTypeAsync());
  }

  @Override
  public void writeAxisScaleType(@Nullable AxisScaleEnumeration value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeAxisScaleTypeAsync(value)),
        "http://opcfoundation.org/UA/}AxisScaleType");
  }

  @Override
  public CompletableFuture<? extends @Nullable AxisScaleEnumeration> readAxisScaleTypeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getAxisScaleTypeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}AxisScaleType",
                            true,
                            AxisScaleEnumeration.class,
                            -1,
                            AxisScaleEnumeration::from)),
                v -> CompletableFuture.completedFuture((@Nullable AxisScaleEnumeration) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeAxisScaleTypeAsync(
      @Nullable AxisScaleEnumeration value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getAxisScaleTypeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}AxisScaleType",
                        value,
                        AxisScaleEnumeration.class,
                        -1,
                        AxisScaleEnumeration::from)));
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
  public PropertyTypeNode getEngineeringUnits_Node() throws UaException {
    return ClientNodeSupport.await(getEngineeringUnits_NodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getEngineeringUnits_NodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
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
                            true,
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
  public PropertyTypeNode getTitleNode() throws UaException {
    return ClientNodeSupport.await(getTitleNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getTitleNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Title",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable LocalizedText readTitle() throws UaException {
    return ClientNodeSupport.await(readTitleAsync());
  }

  @Override
  public void writeTitle(@Nullable LocalizedText value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeTitleAsync(value)), "http://opcfoundation.org/UA/}Title");
  }

  @Override
  public CompletableFuture<? extends @Nullable LocalizedText> readTitleAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getTitleNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Title",
                            true,
                            LocalizedText.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LocalizedText) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTitleAsync(@Nullable LocalizedText value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getTitleNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Title",
                        value,
                        LocalizedText.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getEURangeNode() throws UaException {
    return ClientNodeSupport.await(getEURangeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getEURangeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
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
                            true,
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
                            client, n, this, "Value", true, Variant.class, 0, null)),
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
                        client, n, this, "Value", value, Variant.class, 0, null)));
  }
}
