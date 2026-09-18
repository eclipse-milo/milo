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
import org.eclipse.milo.opcua.stack.core.types.structured.LinearConversionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link AlternativeUnitType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.2/#6.4.2.4">Model
 *     documentation</a>
 */
public class AlternativeUnitTypeNode extends UnitTypeNode implements AlternativeUnitType {
  public AlternativeUnitTypeNode(
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
  public @Nullable PropertyTypeNode getLinearConversionNode() throws UaException {
    return ClientNodeSupport.await(getLinearConversionNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getLinearConversionNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LinearConversion",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable LinearConversionDataType readLinearConversion() throws UaException {
    return ClientNodeSupport.await(readLinearConversionAsync());
  }

  @Override
  public void writeLinearConversion(@Nullable LinearConversionDataType value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeLinearConversionAsync(value)),
        "http://opcfoundation.org/UA/}LinearConversion");
  }

  @Override
  public CompletableFuture<? extends @Nullable LinearConversionDataType>
      readLinearConversionAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getLinearConversionNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}LinearConversion",
                            false,
                            LinearConversionDataType.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable LinearConversionDataType) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeLinearConversionAsync(
      @Nullable LinearConversionDataType value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getLinearConversionNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}LinearConversion",
                        value,
                        LinearConversionDataType.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMathMLConversionNode() throws UaException {
    return ClientNodeSupport.await(getMathMLConversionNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMathMLConversionNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MathMLConversion",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readMathMLConversion() throws UaException {
    return ClientNodeSupport.await(readMathMLConversionAsync());
  }

  @Override
  public void writeMathMLConversion(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMathMLConversionAsync(value)),
        "http://opcfoundation.org/UA/}MathMLConversion");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readMathMLConversionAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMathMLConversionNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MathMLConversion",
                            false,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMathMLConversionAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMathMLConversionNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MathMLConversion",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMathMLInverseConversionNode() throws UaException {
    return ClientNodeSupport.await(getMathMLInverseConversionNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMathMLInverseConversionNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MathMLInverseConversion",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readMathMLInverseConversion() throws UaException {
    return ClientNodeSupport.await(readMathMLInverseConversionAsync());
  }

  @Override
  public void writeMathMLInverseConversion(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMathMLInverseConversionAsync(value)),
        "http://opcfoundation.org/UA/}MathMLInverseConversion");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readMathMLInverseConversionAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMathMLInverseConversionNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MathMLInverseConversion",
                            false,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMathMLInverseConversionAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMathMLInverseConversionNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MathMLInverseConversion",
                        value,
                        String.class,
                        -1,
                        null)));
  }
}
