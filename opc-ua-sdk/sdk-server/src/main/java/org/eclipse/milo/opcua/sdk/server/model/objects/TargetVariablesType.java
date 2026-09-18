package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldTargetDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the TargetVariablesType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.1">Model
 *     documentation</a>
 */
public interface TargetVariablesType extends SubscribedDataSetType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15111L);

  /**
   * Returns the mandatory TargetVariables child, a PropertyType with DataType FieldTargetDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getTargetVariablesNode();

  /**
   * Returns the Value of the TargetVariables child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable FieldTargetDataType @Nullable [] getTargetVariables();

  /**
   * Sets the Value of the TargetVariables child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setTargetVariables(@Nullable FieldTargetDataType @Nullable [] value);

  /**
   * Returns the optional AddTargetVariables Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.2">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddTargetVariablesMethodNode();

  /**
   * Sets this instance's AddTargetVariables handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setAddTargetVariablesHandler(@Nullable AddTargetVariablesHandler handler);

  /**
   * Returns the optional RemoveTargetVariables Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveTargetVariablesMethodNode();

  /**
   * Sets this instance's RemoveTargetVariables handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setRemoveTargetVariablesHandler(@Nullable RemoveTargetVariablesHandler handler);

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
   * Handles calls to the AddTargetVariables Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.2">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface AddTargetVariablesHandler {
    /**
     * Handles a call to the AddTargetVariables Method.
     *
     * @throws UaException if the call fails.
     */
    StatusCode @Nullable [] addTargetVariables(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable ConfigurationVersionDataType configurationVersion,
        @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd)
        throws UaException;
  }

  /**
   * Handles calls to the RemoveTargetVariables Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.3">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface RemoveTargetVariablesHandler {
    /**
     * Handles a call to the RemoveTargetVariables Method.
     *
     * @throws UaException if the call fails.
     */
    StatusCode @Nullable [] removeTargetVariables(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable ConfigurationVersionDataType configurationVersion,
        UInteger @Nullable [] targetsToRemove)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /**
     * Handles a call to the AddTargetVariables Method; see {@link
     * AddTargetVariablesHandler#addTargetVariables}.
     */
    default StatusCode @Nullable [] addTargetVariables(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable ConfigurationVersionDataType configurationVersion,
        @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the RemoveTargetVariables Method; see {@link
     * RemoveTargetVariablesHandler#removeTargetVariables}.
     */
    default StatusCode @Nullable [] removeTargetVariables(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable ConfigurationVersionDataType configurationVersion,
        UInteger @Nullable [] targetsToRemove)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
