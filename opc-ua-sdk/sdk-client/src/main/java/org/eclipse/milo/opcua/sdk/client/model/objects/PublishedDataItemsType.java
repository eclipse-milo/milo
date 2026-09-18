package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.model.methods.PublishedDataItemsTypeAddVariables;
import org.eclipse.milo.opcua.sdk.core.model.methods.PublishedDataItemsTypeRemoveVariables;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedVariableDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the PublishedDataItemsType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.1">Model
 *     documentation</a>
 */
public interface PublishedDataItemsType extends PublishedDataSetType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 14534L);

  QualifiedProperty<PublishedVariableDataType[]> PublishedData_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "PublishedData",
          ExpandedNodeId.of(Namespaces.OPC_UA, 14273L),
          1,
          PublishedVariableDataType[].class);

  /**
   * Resolves the mandatory PublishedData child, a PropertyType with DataType
   * PublishedVariableDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getPublishedDataNode() throws UaException;

  /** Asynchronous form of {@link #getPublishedDataNode()}. */
  CompletableFuture<? extends PropertyType> getPublishedDataNodeAsync();

  /**
   * Reads the Value of the PublishedData child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable PublishedVariableDataType @Nullable [] readPublishedData() throws UaException;

  /**
   * Writes the Value of the PublishedData child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePublishedData(@Nullable PublishedVariableDataType @Nullable [] value)
      throws UaException;

  /** Asynchronous form of {@link #readPublishedData()}. */
  CompletableFuture<? extends @Nullable PublishedVariableDataType @Nullable []>
      readPublishedDataAsync();

  /** Asynchronous form of {@link #writePublishedData}; completes with the operation status. */
  CompletableFuture<StatusCode> writePublishedDataAsync(
      @Nullable PublishedVariableDataType @Nullable [] value);

  /**
   * Resolves the optional AddVariables Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.2">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddVariablesMethodNode() throws UaException;

  /** Asynchronous form of {@link #getAddVariablesMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getAddVariablesMethodNodeAsync();

  /**
   * Calls the AddVariables Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.2">Model
   *     documentation</a>
   */
  PublishedDataItemsTypeAddVariables.Outputs addVariables(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable String @Nullable [] fieldNameAliases,
      Boolean @Nullable [] promotedFields,
      @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
      throws UaException;

  /**
   * Calls the AddVariables Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<PublishedDataItemsTypeAddVariables.Outputs> callAddVariables(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable String @Nullable [] fieldNameAliases,
      Boolean @Nullable [] promotedFields,
      @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
      throws UaException;

  /**
   * Calls the AddVariables Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<PublishedDataItemsTypeAddVariables.Outputs> callAddVariablesWith(
      MethodCallOptions options,
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable String @Nullable [] fieldNameAliases,
      Boolean @Nullable [] promotedFields,
      @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
      throws UaException;

  /** Asynchronous form of {@link #addVariables}. */
  CompletableFuture<PublishedDataItemsTypeAddVariables.Outputs> addVariablesAsync(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable String @Nullable [] fieldNameAliases,
      Boolean @Nullable [] promotedFields,
      @Nullable PublishedVariableDataType @Nullable [] variablesToAdd);

  /** Asynchronous form of {@link #callAddVariables}. */
  CompletableFuture<MethodCallResult<PublishedDataItemsTypeAddVariables.Outputs>>
      callAddVariablesAsync(
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable String @Nullable [] fieldNameAliases,
          Boolean @Nullable [] promotedFields,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd);

  /** Asynchronous form of {@link #callAddVariablesWith}. */
  CompletableFuture<MethodCallResult<PublishedDataItemsTypeAddVariables.Outputs>>
      callAddVariablesWithAsync(
          MethodCallOptions options,
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable String @Nullable [] fieldNameAliases,
          Boolean @Nullable [] promotedFields,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd);

  /**
   * Resolves the optional RemoveVariables Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveVariablesMethodNode() throws UaException;

  /** Asynchronous form of {@link #getRemoveVariablesMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getRemoveVariablesMethodNodeAsync();

  /**
   * Calls the RemoveVariables Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.3">Model
   *     documentation</a>
   */
  PublishedDataItemsTypeRemoveVariables.Outputs removeVariables(
      @Nullable ConfigurationVersionDataType configurationVersion,
      UInteger @Nullable [] variablesToRemove)
      throws UaException;

  /**
   * Calls the RemoveVariables Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<PublishedDataItemsTypeRemoveVariables.Outputs> callRemoveVariables(
      @Nullable ConfigurationVersionDataType configurationVersion,
      UInteger @Nullable [] variablesToRemove)
      throws UaException;

  /**
   * Calls the RemoveVariables Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<PublishedDataItemsTypeRemoveVariables.Outputs> callRemoveVariablesWith(
      MethodCallOptions options,
      @Nullable ConfigurationVersionDataType configurationVersion,
      UInteger @Nullable [] variablesToRemove)
      throws UaException;

  /** Asynchronous form of {@link #removeVariables}. */
  CompletableFuture<PublishedDataItemsTypeRemoveVariables.Outputs> removeVariablesAsync(
      @Nullable ConfigurationVersionDataType configurationVersion,
      UInteger @Nullable [] variablesToRemove);

  /** Asynchronous form of {@link #callRemoveVariables}. */
  CompletableFuture<MethodCallResult<PublishedDataItemsTypeRemoveVariables.Outputs>>
      callRemoveVariablesAsync(
          @Nullable ConfigurationVersionDataType configurationVersion,
          UInteger @Nullable [] variablesToRemove);

  /** Asynchronous form of {@link #callRemoveVariablesWith}. */
  CompletableFuture<MethodCallResult<PublishedDataItemsTypeRemoveVariables.Outputs>>
      callRemoveVariablesWithAsync(
          MethodCallOptions options,
          @Nullable ConfigurationVersionDataType configurationVersion,
          UInteger @Nullable [] variablesToRemove);
}
