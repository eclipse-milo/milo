package org.eclipse.milo.opcua.sdk.server.model.objects;

import java.util.UUID;
import org.eclipse.milo.opcua.sdk.core.model.methods.ConfigurationFileTypeCloseAndUpdate;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationUpdateTargetType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the ConfigurationFileType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.1">Model
 *     documentation</a>
 */
public interface ConfigurationFileType extends FileType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15437L);

  /**
   * Returns the mandatory ActivityTimeout child, a PropertyType with DataType Duration.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getActivityTimeoutNode();

  /**
   * Returns the Value of the ActivityTimeout child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getActivityTimeout();

  /**
   * Sets the Value of the ActivityTimeout child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setActivityTimeout(@Nullable Double value);

  /**
   * Returns the mandatory CurrentVersion child, a PropertyType with DataType VersionTime.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getCurrentVersionNode();

  /**
   * Returns the Value of the CurrentVersion child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getCurrentVersion();

  /**
   * Sets the Value of the CurrentVersion child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCurrentVersion(@Nullable UInteger value);

  /**
   * Returns the mandatory LastUpdateTime child, a PropertyType with DataType UtcTime.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getLastUpdateTimeNode();

  /**
   * Returns the Value of the LastUpdateTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getLastUpdateTime();

  /**
   * Sets the Value of the LastUpdateTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLastUpdateTime(@Nullable DateTime value);

  /**
   * Returns the mandatory SupportedDataType child, a PropertyType with DataType NodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getSupportedDataTypeNode();

  /**
   * Returns the Value of the SupportedDataType child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable NodeId getSupportedDataType();

  /**
   * Sets the Value of the SupportedDataType child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSupportedDataType(@Nullable NodeId value);

  /**
   * Returns the mandatory CloseAndUpdate Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.2">Model
   *     documentation</a>
   */
  UaMethodNode getCloseAndUpdateMethodNode();

  /**
   * Sets this instance's CloseAndUpdate handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setCloseAndUpdateHandler(@Nullable CloseAndUpdateHandler handler);

  /**
   * Returns the mandatory ConfirmUpdate Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.3">Model
   *     documentation</a>
   */
  UaMethodNode getConfirmUpdateMethodNode();

  /**
   * Sets this instance's ConfirmUpdate handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setConfirmUpdateHandler(@Nullable ConfirmUpdateHandler handler);

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
   * Handles calls to the CloseAndUpdate Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.2">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface CloseAndUpdateHandler {
    /**
     * Handles a call to the CloseAndUpdate Method.
     *
     * @throws UaException if the call fails.
     */
    ConfigurationFileTypeCloseAndUpdate.Outputs closeAndUpdate(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable UInteger fileHandle,
        @Nullable UInteger versionToUpdate,
        @Nullable ConfigurationUpdateTargetType @Nullable [] targets,
        @Nullable Double revertAfterTime,
        @Nullable Double restartDelayTime)
        throws UaException;
  }

  /**
   * Handles calls to the ConfirmUpdate Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.3">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface ConfirmUpdateHandler {
    /**
     * Handles a call to the ConfirmUpdate Method.
     *
     * @throws UaException if the call fails.
     */
    void confirmUpdate(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable UUID updateId)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods extends FileType.Methods {
    /**
     * Handles a call to the CloseAndUpdate Method; see {@link
     * CloseAndUpdateHandler#closeAndUpdate}.
     */
    default ConfigurationFileTypeCloseAndUpdate.Outputs closeAndUpdate(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable UInteger fileHandle,
        @Nullable UInteger versionToUpdate,
        @Nullable ConfigurationUpdateTargetType @Nullable [] targets,
        @Nullable Double revertAfterTime,
        @Nullable Double restartDelayTime)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the ConfirmUpdate Method; see {@link ConfirmUpdateHandler#confirmUpdate}.
     */
    default void confirmUpdate(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable UUID updateId)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
