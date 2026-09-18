package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.model.methods.LogObjectTypeGetRecords;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.LogRecordMask;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the LogObjectType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part26/5.2">Model
 *     documentation</a>
 */
public interface LogObjectType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 19352L);

  /**
   * Returns the optional MaxRecords child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxRecordsNode();

  /**
   * Returns the Value of the MaxRecords child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxRecords();

  /**
   * Sets the Value of the MaxRecords child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxRecords(@Nullable UInteger value);

  /**
   * Returns the optional MaxStorageDuration child, a PropertyType with DataType Duration.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxStorageDurationNode();

  /**
   * Returns the Value of the MaxStorageDuration child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getMaxStorageDuration();

  /**
   * Sets the Value of the MaxStorageDuration child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxStorageDuration(@Nullable Double value);

  /**
   * Returns the optional MinimumSeverity child, a PropertyType with DataType UInt16.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMinimumSeverityNode();

  /**
   * Returns the Value of the MinimumSeverity child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UShort getMinimumSeverity();

  /**
   * Sets the Value of the MinimumSeverity child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMinimumSeverity(@Nullable UShort value);

  /**
   * Returns the mandatory GetRecords Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part26/5.3">Model
   *     documentation</a>
   */
  UaMethodNode getGetRecordsMethodNode();

  /**
   * Sets this instance's GetRecords handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setGetRecordsHandler(@Nullable GetRecordsHandler handler);

  /**
   * Returns the optional ReleaseContinuationPoint Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part26/5.4">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getReleaseContinuationPointMethodNode();

  /**
   * Sets this instance's ReleaseContinuationPoint handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setReleaseContinuationPointHandler(@Nullable ReleaseContinuationPointHandler handler);

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
   * Handles calls to the GetRecords Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part26/5.3">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface GetRecordsHandler {
    /**
     * Handles a call to the GetRecords Method.
     *
     * @throws UaException if the call fails.
     */
    LogObjectTypeGetRecords.Outputs getRecords(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable DateTime startTime,
        @Nullable DateTime endTime,
        @Nullable UInteger maxReturnRecords,
        @Nullable UShort minimumSeverity,
        @Nullable LogRecordMask requestMask,
        @Nullable ByteString continuationPointIn)
        throws UaException;
  }

  /**
   * Handles calls to the ReleaseContinuationPoint Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part26/5.4">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface ReleaseContinuationPointHandler {
    /**
     * Handles a call to the ReleaseContinuationPoint Method.
     *
     * @throws UaException if the call fails.
     */
    void releaseContinuationPoint(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable ByteString continuationPointIn)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /** Handles a call to the GetRecords Method; see {@link GetRecordsHandler#getRecords}. */
    default LogObjectTypeGetRecords.Outputs getRecords(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable DateTime startTime,
        @Nullable DateTime endTime,
        @Nullable UInteger maxReturnRecords,
        @Nullable UShort minimumSeverity,
        @Nullable LogRecordMask requestMask,
        @Nullable ByteString continuationPointIn)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the ReleaseContinuationPoint Method; see {@link
     * ReleaseContinuationPointHandler#releaseContinuationPoint}.
     */
    default void releaseContinuationPoint(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable ByteString continuationPointIn)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
