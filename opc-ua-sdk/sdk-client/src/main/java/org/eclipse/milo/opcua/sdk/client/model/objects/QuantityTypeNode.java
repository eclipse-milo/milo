package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaObjectNode;
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
import org.eclipse.milo.opcua.stack.core.types.structured.AnnotationDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.QuantityDimension;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link QuantityType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.1">Model
 *     documentation</a>
 */
public class QuantityTypeNode extends BaseObjectTypeNode implements QuantityType {
  public QuantityTypeNode(
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
  public @Nullable PropertyTypeNode getAnnotationNode() throws UaException {
    return ClientNodeSupport.await(getAnnotationNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getAnnotationNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Annotation",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable AnnotationDataType @Nullable [] readAnnotation() throws UaException {
    return ClientNodeSupport.await(readAnnotationAsync());
  }

  @Override
  public void writeAnnotation(@Nullable AnnotationDataType @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeAnnotationAsync(value)),
        "http://opcfoundation.org/UA/}Annotation");
  }

  @Override
  public CompletableFuture<? extends @Nullable AnnotationDataType @Nullable []>
      readAnnotationAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getAnnotationNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Annotation",
                            false,
                            AnnotationDataType.class,
                            1,
                            null)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable AnnotationDataType @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeAnnotationAsync(
      @Nullable AnnotationDataType @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getAnnotationNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Annotation",
                        value,
                        AnnotationDataType.class,
                        1,
                        null)));
  }

  @Override
  public UaObjectNode getServerUnitsNode() throws UaException {
    return ClientNodeSupport.await(getServerUnitsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaObjectNode> getServerUnitsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ServerUnits",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        UaObjectNode.class)));
  }

  @Override
  public @Nullable PropertyTypeNode getConversionServiceNode() throws UaException {
    return ClientNodeSupport.await(getConversionServiceNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getConversionServiceNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ConversionService",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readConversionService() throws UaException {
    return ClientNodeSupport.await(readConversionServiceAsync());
  }

  @Override
  public void writeConversionService(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeConversionServiceAsync(value)),
        "http://opcfoundation.org/UA/}ConversionService");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readConversionServiceAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getConversionServiceNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ConversionService",
                            false,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeConversionServiceAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getConversionServiceNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ConversionService",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getSymbolNode() throws UaException {
    return ClientNodeSupport.await(getSymbolNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getSymbolNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Symbol",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable LocalizedText readSymbol() throws UaException {
    return ClientNodeSupport.await(readSymbolAsync());
  }

  @Override
  public void writeSymbol(@Nullable LocalizedText value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSymbolAsync(value)), "http://opcfoundation.org/UA/}Symbol");
  }

  @Override
  public CompletableFuture<? extends @Nullable LocalizedText> readSymbolAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSymbolNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Symbol",
                            false,
                            LocalizedText.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LocalizedText) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSymbolAsync(@Nullable LocalizedText value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSymbolNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Symbol",
                        value,
                        LocalizedText.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getDimensionNode() throws UaException {
    return ClientNodeSupport.await(getDimensionNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDimensionNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Dimension",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable QuantityDimension readDimension() throws UaException {
    return ClientNodeSupport.await(readDimensionAsync());
  }

  @Override
  public void writeDimension(@Nullable QuantityDimension value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeDimensionAsync(value)),
        "http://opcfoundation.org/UA/}Dimension");
  }

  @Override
  public CompletableFuture<? extends @Nullable QuantityDimension> readDimensionAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getDimensionNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Dimension",
                            true,
                            QuantityDimension.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable QuantityDimension) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeDimensionAsync(@Nullable QuantityDimension value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getDimensionNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Dimension",
                        value,
                        QuantityDimension.class,
                        -1,
                        null)));
  }
}
