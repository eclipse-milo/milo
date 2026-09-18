package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.PriorityMappingEntryType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the PriorityMappingTableType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.2">Model
 *     documentation</a>
 */
public interface PriorityMappingTableType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 25227L);

  /**
   * Returns the mandatory PriorityMapppingEntries child, a PropertyType with DataType
   * PriorityMappingEntryType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getPriorityMapppingEntriesNode();

  /**
   * Returns the Value of the PriorityMapppingEntries child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable PriorityMappingEntryType @Nullable [] getPriorityMapppingEntries();

  /**
   * Sets the Value of the PriorityMapppingEntries child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPriorityMapppingEntries(@Nullable PriorityMappingEntryType @Nullable [] value);

  /**
   * Returns the optional AddPriorityMappingEntry Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddPriorityMappingEntryMethodNode();

  /**
   * Sets this instance's AddPriorityMappingEntry handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setAddPriorityMappingEntryHandler(@Nullable AddPriorityMappingEntryHandler handler);

  /**
   * Returns the optional DeletePriorityMappingEntry Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.4">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getDeletePriorityMappingEntryMethodNode();

  /**
   * Sets this instance's DeletePriorityMappingEntry handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setDeletePriorityMappingEntryHandler(@Nullable DeletePriorityMappingEntryHandler handler);

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
   * Handles calls to the AddPriorityMappingEntry Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.3">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface AddPriorityMappingEntryHandler {
    /**
     * Handles a call to the AddPriorityMappingEntry Method.
     *
     * @throws UaException if the call fails.
     */
    void addPriorityMappingEntry(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String mappingUri,
        @Nullable String priorityLabel,
        @Nullable UByte priorityValue_PCP,
        @Nullable UInteger priorityValue_DSCP)
        throws UaException;
  }

  /**
   * Handles calls to the DeletePriorityMappingEntry Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.4">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface DeletePriorityMappingEntryHandler {
    /**
     * Handles a call to the DeletePriorityMappingEntry Method.
     *
     * @throws UaException if the call fails.
     */
    void deletePriorityMappingEntry(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String mappingUri,
        @Nullable String priorityLabel)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /**
     * Handles a call to the AddPriorityMappingEntry Method; see {@link
     * AddPriorityMappingEntryHandler#addPriorityMappingEntry}.
     */
    default void addPriorityMappingEntry(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String mappingUri,
        @Nullable String priorityLabel,
        @Nullable UByte priorityValue_PCP,
        @Nullable UInteger priorityValue_DSCP)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the DeletePriorityMappingEntry Method; see {@link
     * DeletePriorityMappingEntryHandler#deletePriorityMappingEntry}.
     */
    default void deletePriorityMappingEntry(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String mappingUri,
        @Nullable String priorityLabel)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
