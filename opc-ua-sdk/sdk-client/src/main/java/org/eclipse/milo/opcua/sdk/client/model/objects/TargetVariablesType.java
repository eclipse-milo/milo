package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldTargetDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the TargetVariablesType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.1">Model
 *     documentation</a>
 */
public interface TargetVariablesType extends SubscribedDataSetType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15111L);

  QualifiedProperty<FieldTargetDataType[]> TargetVariables_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "TargetVariables",
          ExpandedNodeId.of(Namespaces.OPC_UA, 14744L),
          1,
          FieldTargetDataType[].class);

  /**
   * Resolves the mandatory TargetVariables child, a PropertyType with DataType FieldTargetDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getTargetVariablesNode() throws UaException;

  /** Asynchronous form of {@link #getTargetVariablesNode()}. */
  CompletableFuture<? extends PropertyType> getTargetVariablesNodeAsync();

  /**
   * Reads the Value of the TargetVariables child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable FieldTargetDataType @Nullable [] readTargetVariables() throws UaException;

  /**
   * Writes the Value of the TargetVariables child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTargetVariables(@Nullable FieldTargetDataType @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readTargetVariables()}. */
  CompletableFuture<? extends @Nullable FieldTargetDataType @Nullable []>
      readTargetVariablesAsync();

  /** Asynchronous form of {@link #writeTargetVariables}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTargetVariablesAsync(
      @Nullable FieldTargetDataType @Nullable [] value);

  /**
   * Resolves the optional AddTargetVariables Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.2">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddTargetVariablesMethodNode() throws UaException;

  /** Asynchronous form of {@link #getAddTargetVariablesMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getAddTargetVariablesMethodNodeAsync();

  /**
   * Calls the AddTargetVariables Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.2">Model
   *     documentation</a>
   */
  StatusCode @Nullable [] addTargetVariables(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd)
      throws UaException;

  /**
   * Calls the AddTargetVariables Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<StatusCode @Nullable []> callAddTargetVariables(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd)
      throws UaException;

  /**
   * Calls the AddTargetVariables Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<StatusCode @Nullable []> callAddTargetVariablesWith(
      MethodCallOptions options,
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd)
      throws UaException;

  /** Asynchronous form of {@link #addTargetVariables}. */
  CompletableFuture<StatusCode @Nullable []> addTargetVariablesAsync(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd);

  /** Asynchronous form of {@link #callAddTargetVariables}. */
  CompletableFuture<MethodCallResult<StatusCode @Nullable []>> callAddTargetVariablesAsync(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd);

  /** Asynchronous form of {@link #callAddTargetVariablesWith}. */
  CompletableFuture<MethodCallResult<StatusCode @Nullable []>> callAddTargetVariablesWithAsync(
      MethodCallOptions options,
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd);

  /**
   * Resolves the optional RemoveTargetVariables Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveTargetVariablesMethodNode() throws UaException;

  /** Asynchronous form of {@link #getRemoveTargetVariablesMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getRemoveTargetVariablesMethodNodeAsync();

  /**
   * Calls the RemoveTargetVariables Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.3">Model
   *     documentation</a>
   */
  StatusCode @Nullable [] removeTargetVariables(
      @Nullable ConfigurationVersionDataType configurationVersion,
      UInteger @Nullable [] targetsToRemove)
      throws UaException;

  /**
   * Calls the RemoveTargetVariables Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<StatusCode @Nullable []> callRemoveTargetVariables(
      @Nullable ConfigurationVersionDataType configurationVersion,
      UInteger @Nullable [] targetsToRemove)
      throws UaException;

  /**
   * Calls the RemoveTargetVariables Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<StatusCode @Nullable []> callRemoveTargetVariablesWith(
      MethodCallOptions options,
      @Nullable ConfigurationVersionDataType configurationVersion,
      UInteger @Nullable [] targetsToRemove)
      throws UaException;

  /** Asynchronous form of {@link #removeTargetVariables}. */
  CompletableFuture<StatusCode @Nullable []> removeTargetVariablesAsync(
      @Nullable ConfigurationVersionDataType configurationVersion,
      UInteger @Nullable [] targetsToRemove);

  /** Asynchronous form of {@link #callRemoveTargetVariables}. */
  CompletableFuture<MethodCallResult<StatusCode @Nullable []>> callRemoveTargetVariablesAsync(
      @Nullable ConfigurationVersionDataType configurationVersion,
      UInteger @Nullable [] targetsToRemove);

  /** Asynchronous form of {@link #callRemoveTargetVariablesWith}. */
  CompletableFuture<MethodCallResult<StatusCode @Nullable []>> callRemoveTargetVariablesWithAsync(
      MethodCallOptions options,
      @Nullable ConfigurationVersionDataType configurationVersion,
      UInteger @Nullable [] targetsToRemove);
}
