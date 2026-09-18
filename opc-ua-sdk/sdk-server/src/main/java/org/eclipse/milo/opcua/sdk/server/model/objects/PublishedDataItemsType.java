package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.model.methods.PublishedDataItemsTypeAddVariables;
import org.eclipse.milo.opcua.sdk.core.model.methods.PublishedDataItemsTypeRemoveVariables;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedVariableDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the PublishedDataItemsType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.1">Model
 *     documentation</a>
 */
public interface PublishedDataItemsType extends PublishedDataSetType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 14534L);

  /**
   * Returns the mandatory PublishedData child, a PropertyType with DataType
   * PublishedVariableDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getPublishedDataNode();

  /**
   * Returns the Value of the PublishedData child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable PublishedVariableDataType @Nullable [] getPublishedData();

  /**
   * Sets the Value of the PublishedData child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPublishedData(@Nullable PublishedVariableDataType @Nullable [] value);

  /**
   * Returns the optional AddVariables Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.2">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddVariablesMethodNode();

  /**
   * Sets this instance's AddVariables handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setAddVariablesHandler(@Nullable AddVariablesHandler handler);

  /**
   * Returns the optional RemoveVariables Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveVariablesMethodNode();

  /**
   * Sets this instance's RemoveVariables handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setRemoveVariablesHandler(@Nullable RemoveVariablesHandler handler);

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
   * Handles calls to the AddVariables Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.2">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface AddVariablesHandler {
    /**
     * Handles a call to the AddVariables Method.
     *
     * @throws UaException if the call fails.
     */
    PublishedDataItemsTypeAddVariables.Outputs addVariables(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable ConfigurationVersionDataType configurationVersion,
        @Nullable String @Nullable [] fieldNameAliases,
        Boolean @Nullable [] promotedFields,
        @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
        throws UaException;
  }

  /**
   * Handles calls to the RemoveVariables Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.3">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface RemoveVariablesHandler {
    /**
     * Handles a call to the RemoveVariables Method.
     *
     * @throws UaException if the call fails.
     */
    PublishedDataItemsTypeRemoveVariables.Outputs removeVariables(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable ConfigurationVersionDataType configurationVersion,
        UInteger @Nullable [] variablesToRemove)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /** Handles a call to the AddVariables Method; see {@link AddVariablesHandler#addVariables}. */
    default PublishedDataItemsTypeAddVariables.Outputs addVariables(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable ConfigurationVersionDataType configurationVersion,
        @Nullable String @Nullable [] fieldNameAliases,
        Boolean @Nullable [] promotedFields,
        @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the RemoveVariables Method; see {@link
     * RemoveVariablesHandler#removeVariables}.
     */
    default PublishedDataItemsTypeRemoveVariables.Outputs removeVariables(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable ConfigurationVersionDataType configurationVersion,
        UInteger @Nullable [] variablesToRemove)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
