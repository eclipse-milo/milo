package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AliasNameDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.AliasNameVerboseDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the AliasNameCategoryType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.1">Model
 *     documentation</a>
 */
public interface AliasNameCategoryType extends FolderType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 23456L);

  /**
   * Returns the optional LastChange child, a PropertyType with DataType VersionTime.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getLastChangeNode();

  /**
   * Returns the Value of the LastChange child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getLastChange();

  /**
   * Sets the Value of the LastChange child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLastChange(@Nullable UInteger value);

  /**
   * Returns the optional AddAliasesToCategory Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.4">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddAliasesToCategoryMethodNode();

  /**
   * Sets this instance's AddAliasesToCategory handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setAddAliasesToCategoryHandler(@Nullable AddAliasesToCategoryHandler handler);

  /**
   * Returns the optional DeleteAliasesFromCategory Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.5">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getDeleteAliasesFromCategoryMethodNode();

  /**
   * Sets this instance's DeleteAliasesFromCategory handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setDeleteAliasesFromCategoryHandler(@Nullable DeleteAliasesFromCategoryHandler handler);

  /**
   * Returns the mandatory FindAlias Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.2">Model
   *     documentation</a>
   */
  UaMethodNode getFindAliasMethodNode();

  /**
   * Sets this instance's FindAlias handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setFindAliasHandler(@Nullable FindAliasHandler handler);

  /**
   * Returns the optional FindAliasVerbose Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getFindAliasVerboseMethodNode();

  /**
   * Sets this instance's FindAliasVerbose handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setFindAliasVerboseHandler(@Nullable FindAliasVerboseHandler handler);

  /**
   * Sets this instance's Method handlers, including inherited handlers, from one implementation;
   * null clears them and restores Method-node fallback. Absent optional Methods are skipped.
   * Changes are applied in order; a failure does not roll back earlier changes.
   *
   * @throws UaRuntimeException if a mandatory Method is absent, or a Method is ambiguous or
   *     incompatible.
   */
  void setMethods(@Nullable Methods methods);

  /**
   * Handles calls to the AddAliasesToCategory Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.4">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface AddAliasesToCategoryHandler {
    /**
     * Handles a call to the AddAliasesToCategory Method.
     *
     * @throws UaException if the call fails.
     */
    StatusCode @Nullable [] addAliasesToCategory(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String @Nullable [] aliasNames,
        ExpandedNodeId @Nullable [] targetNodes,
        @Nullable String @Nullable [] targetServers,
        @Nullable NodeId targetReferenceType)
        throws UaException;
  }

  /**
   * Handles calls to the DeleteAliasesFromCategory Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.5">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface DeleteAliasesFromCategoryHandler {
    /**
     * Handles a call to the DeleteAliasesFromCategory Method.
     *
     * @throws UaException if the call fails.
     */
    StatusCode @Nullable [] deleteAliasesFromCategory(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String @Nullable [] aliasNames,
        ExpandedNodeId @Nullable [] targetNodes)
        throws UaException;
  }

  /**
   * Handles calls to the FindAlias Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.2">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface FindAliasHandler {
    /**
     * Handles a call to the FindAlias Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable AliasNameDataType @Nullable [] findAlias(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String aliasNameSearchPattern,
        @Nullable NodeId referenceTypeFilter)
        throws UaException;
  }

  /**
   * Handles calls to the FindAliasVerbose Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part17/6.3.3">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface FindAliasVerboseHandler {
    /**
     * Handles a call to the FindAliasVerbose Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable AliasNameVerboseDataType @Nullable [] findAliasVerbose(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String aliasNameSearchPattern,
        @Nullable NodeId referenceTypeFilter)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /**
     * Handles a call to the AddAliasesToCategory Method; see {@link
     * AddAliasesToCategoryHandler#addAliasesToCategory}.
     */
    default StatusCode @Nullable [] addAliasesToCategory(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String @Nullable [] aliasNames,
        ExpandedNodeId @Nullable [] targetNodes,
        @Nullable String @Nullable [] targetServers,
        @Nullable NodeId targetReferenceType)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the DeleteAliasesFromCategory Method; see {@link
     * DeleteAliasesFromCategoryHandler#deleteAliasesFromCategory}.
     */
    default StatusCode @Nullable [] deleteAliasesFromCategory(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String @Nullable [] aliasNames,
        ExpandedNodeId @Nullable [] targetNodes)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the FindAlias Method; see {@link FindAliasHandler#findAlias}. */
    default @Nullable AliasNameDataType @Nullable [] findAlias(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String aliasNameSearchPattern,
        @Nullable NodeId referenceTypeFilter)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the FindAliasVerbose Method; see {@link
     * FindAliasVerboseHandler#findAliasVerbose}.
     */
    default @Nullable AliasNameVerboseDataType @Nullable [] findAliasVerbose(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String aliasNameSearchPattern,
        @Nullable NodeId referenceTypeFilter)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
