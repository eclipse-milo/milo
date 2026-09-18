package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AliasNameDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.AliasNameVerboseDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AliasNameCategoryType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.1">Model
 *     documentation</a>
 */
public interface AliasNameCategoryType extends FolderType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 23456L);

  QualifiedProperty<UInteger> LastChange_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "LastChange",
          ExpandedNodeId.of(Namespaces.OPC_UA, 20998L),
          -1,
          UInteger.class);

  /**
   * Resolves the optional LastChange child, a PropertyType with DataType VersionTime.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getLastChangeNode() throws UaException;

  /** Asynchronous form of {@link #getLastChangeNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getLastChangeNodeAsync();

  /**
   * Reads the Value of the LastChange child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readLastChange() throws UaException;

  /**
   * Writes the Value of the LastChange child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLastChange(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readLastChange()}. */
  CompletableFuture<? extends @Nullable UInteger> readLastChangeAsync();

  /** Asynchronous form of {@link #writeLastChange}; completes with the operation status. */
  CompletableFuture<StatusCode> writeLastChangeAsync(@Nullable UInteger value);

  /**
   * Resolves the optional AddAliasesToCategory Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.4">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddAliasesToCategoryMethodNode() throws UaException;

  /** Asynchronous form of {@link #getAddAliasesToCategoryMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getAddAliasesToCategoryMethodNodeAsync();

  /**
   * Calls the AddAliasesToCategory Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.4">Model
   *     documentation</a>
   */
  StatusCode @Nullable [] addAliasesToCategory(
      @Nullable String @Nullable [] aliasNames,
      ExpandedNodeId @Nullable [] targetNodes,
      @Nullable String @Nullable [] targetServers,
      @Nullable NodeId targetReferenceType)
      throws UaException;

  /**
   * Calls the AddAliasesToCategory Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<StatusCode @Nullable []> callAddAliasesToCategory(
      @Nullable String @Nullable [] aliasNames,
      ExpandedNodeId @Nullable [] targetNodes,
      @Nullable String @Nullable [] targetServers,
      @Nullable NodeId targetReferenceType)
      throws UaException;

  /**
   * Calls the AddAliasesToCategory Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<StatusCode @Nullable []> callAddAliasesToCategoryWith(
      MethodCallOptions options,
      @Nullable String @Nullable [] aliasNames,
      ExpandedNodeId @Nullable [] targetNodes,
      @Nullable String @Nullable [] targetServers,
      @Nullable NodeId targetReferenceType)
      throws UaException;

  /** Asynchronous form of {@link #addAliasesToCategory}. */
  CompletableFuture<StatusCode @Nullable []> addAliasesToCategoryAsync(
      @Nullable String @Nullable [] aliasNames,
      ExpandedNodeId @Nullable [] targetNodes,
      @Nullable String @Nullable [] targetServers,
      @Nullable NodeId targetReferenceType);

  /** Asynchronous form of {@link #callAddAliasesToCategory}. */
  CompletableFuture<MethodCallResult<StatusCode @Nullable []>> callAddAliasesToCategoryAsync(
      @Nullable String @Nullable [] aliasNames,
      ExpandedNodeId @Nullable [] targetNodes,
      @Nullable String @Nullable [] targetServers,
      @Nullable NodeId targetReferenceType);

  /** Asynchronous form of {@link #callAddAliasesToCategoryWith}. */
  CompletableFuture<MethodCallResult<StatusCode @Nullable []>> callAddAliasesToCategoryWithAsync(
      MethodCallOptions options,
      @Nullable String @Nullable [] aliasNames,
      ExpandedNodeId @Nullable [] targetNodes,
      @Nullable String @Nullable [] targetServers,
      @Nullable NodeId targetReferenceType);

  /**
   * Resolves the optional DeleteAliasesFromCategory Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.5">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getDeleteAliasesFromCategoryMethodNode() throws UaException;

  /** Asynchronous form of {@link #getDeleteAliasesFromCategoryMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getDeleteAliasesFromCategoryMethodNodeAsync();

  /**
   * Calls the DeleteAliasesFromCategory Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.5">Model
   *     documentation</a>
   */
  StatusCode @Nullable [] deleteAliasesFromCategory(
      @Nullable String @Nullable [] aliasNames, ExpandedNodeId @Nullable [] targetNodes)
      throws UaException;

  /**
   * Calls the DeleteAliasesFromCategory Method and returns the complete result, including a Bad
   * status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<StatusCode @Nullable []> callDeleteAliasesFromCategory(
      @Nullable String @Nullable [] aliasNames, ExpandedNodeId @Nullable [] targetNodes)
      throws UaException;

  /**
   * Calls the DeleteAliasesFromCategory Method with explicit options and returns the complete
   * result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<StatusCode @Nullable []> callDeleteAliasesFromCategoryWith(
      MethodCallOptions options,
      @Nullable String @Nullable [] aliasNames,
      ExpandedNodeId @Nullable [] targetNodes)
      throws UaException;

  /** Asynchronous form of {@link #deleteAliasesFromCategory}. */
  CompletableFuture<StatusCode @Nullable []> deleteAliasesFromCategoryAsync(
      @Nullable String @Nullable [] aliasNames, ExpandedNodeId @Nullable [] targetNodes);

  /** Asynchronous form of {@link #callDeleteAliasesFromCategory}. */
  CompletableFuture<MethodCallResult<StatusCode @Nullable []>> callDeleteAliasesFromCategoryAsync(
      @Nullable String @Nullable [] aliasNames, ExpandedNodeId @Nullable [] targetNodes);

  /** Asynchronous form of {@link #callDeleteAliasesFromCategoryWith}. */
  CompletableFuture<MethodCallResult<StatusCode @Nullable []>>
      callDeleteAliasesFromCategoryWithAsync(
          MethodCallOptions options,
          @Nullable String @Nullable [] aliasNames,
          ExpandedNodeId @Nullable [] targetNodes);

  /**
   * Resolves the mandatory FindAlias Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.2">Model
   *     documentation</a>
   */
  UaMethodNode getFindAliasMethodNode() throws UaException;

  /** Asynchronous form of {@link #getFindAliasMethodNode()}. */
  CompletableFuture<UaMethodNode> getFindAliasMethodNodeAsync();

  /**
   * Calls the FindAlias Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.2">Model
   *     documentation</a>
   */
  @Nullable AliasNameDataType @Nullable [] findAlias(
      @Nullable String aliasNameSearchPattern, @Nullable NodeId referenceTypeFilter)
      throws UaException;

  /**
   * Calls the FindAlias Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable AliasNameDataType @Nullable []> callFindAlias(
      @Nullable String aliasNameSearchPattern, @Nullable NodeId referenceTypeFilter)
      throws UaException;

  /**
   * Calls the FindAlias Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable AliasNameDataType @Nullable []> callFindAliasWith(
      MethodCallOptions options,
      @Nullable String aliasNameSearchPattern,
      @Nullable NodeId referenceTypeFilter)
      throws UaException;

  /** Asynchronous form of {@link #findAlias}. */
  CompletableFuture<@Nullable AliasNameDataType @Nullable []> findAliasAsync(
      @Nullable String aliasNameSearchPattern, @Nullable NodeId referenceTypeFilter);

  /** Asynchronous form of {@link #callFindAlias}. */
  CompletableFuture<MethodCallResult<@Nullable AliasNameDataType @Nullable []>> callFindAliasAsync(
      @Nullable String aliasNameSearchPattern, @Nullable NodeId referenceTypeFilter);

  /** Asynchronous form of {@link #callFindAliasWith}. */
  CompletableFuture<MethodCallResult<@Nullable AliasNameDataType @Nullable []>>
      callFindAliasWithAsync(
          MethodCallOptions options,
          @Nullable String aliasNameSearchPattern,
          @Nullable NodeId referenceTypeFilter);

  /**
   * Resolves the optional FindAliasVerbose Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getFindAliasVerboseMethodNode() throws UaException;

  /** Asynchronous form of {@link #getFindAliasVerboseMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getFindAliasVerboseMethodNodeAsync();

  /**
   * Calls the FindAliasVerbose Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.3">Model
   *     documentation</a>
   */
  @Nullable AliasNameVerboseDataType @Nullable [] findAliasVerbose(
      @Nullable String aliasNameSearchPattern, @Nullable NodeId referenceTypeFilter)
      throws UaException;

  /**
   * Calls the FindAliasVerbose Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable AliasNameVerboseDataType @Nullable []> callFindAliasVerbose(
      @Nullable String aliasNameSearchPattern, @Nullable NodeId referenceTypeFilter)
      throws UaException;

  /**
   * Calls the FindAliasVerbose Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable AliasNameVerboseDataType @Nullable []> callFindAliasVerboseWith(
      MethodCallOptions options,
      @Nullable String aliasNameSearchPattern,
      @Nullable NodeId referenceTypeFilter)
      throws UaException;

  /** Asynchronous form of {@link #findAliasVerbose}. */
  CompletableFuture<@Nullable AliasNameVerboseDataType @Nullable []> findAliasVerboseAsync(
      @Nullable String aliasNameSearchPattern, @Nullable NodeId referenceTypeFilter);

  /** Asynchronous form of {@link #callFindAliasVerbose}. */
  CompletableFuture<MethodCallResult<@Nullable AliasNameVerboseDataType @Nullable []>>
      callFindAliasVerboseAsync(
          @Nullable String aliasNameSearchPattern, @Nullable NodeId referenceTypeFilter);

  /** Asynchronous form of {@link #callFindAliasVerboseWith}. */
  CompletableFuture<MethodCallResult<@Nullable AliasNameVerboseDataType @Nullable []>>
      callFindAliasVerboseWithAsync(
          MethodCallOptions options,
          @Nullable String aliasNameSearchPattern,
          @Nullable NodeId referenceTypeFilter);
}
