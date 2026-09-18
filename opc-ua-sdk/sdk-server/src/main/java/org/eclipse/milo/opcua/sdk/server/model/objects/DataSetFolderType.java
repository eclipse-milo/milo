package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.model.methods.DataSetFolderTypeAddPublishedDataItems;
import org.eclipse.milo.opcua.sdk.core.model.methods.DataSetFolderTypeAddPublishedDataItemsTemplate;
import org.eclipse.milo.opcua.sdk.core.model.methods.DataSetFolderTypeAddPublishedEvents;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldFlags;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedVariableDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the DataSetFolderType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.1">Model
 *     documentation</a>
 */
public interface DataSetFolderType extends FolderType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 14477L);

  /**
   * Returns the optional AddDataSetFolder Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.7">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddDataSetFolderMethodNode();

  /**
   * Sets this instance's AddDataSetFolder handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setAddDataSetFolderHandler(@Nullable AddDataSetFolderHandler handler);

  /**
   * Returns the optional AddPublishedDataItems Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.2">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddPublishedDataItemsMethodNode();

  /**
   * Sets this instance's AddPublishedDataItems handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setAddPublishedDataItemsHandler(@Nullable AddPublishedDataItemsHandler handler);

  /**
   * Returns the optional AddPublishedDataItemsTemplate Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.4">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddPublishedDataItemsTemplateMethodNode();

  /**
   * Sets this instance's AddPublishedDataItemsTemplate handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setAddPublishedDataItemsTemplateHandler(
      @Nullable AddPublishedDataItemsTemplateHandler handler);

  /**
   * Returns the optional AddPublishedEvents Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddPublishedEventsMethodNode();

  /**
   * Sets this instance's AddPublishedEvents handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setAddPublishedEventsHandler(@Nullable AddPublishedEventsHandler handler);

  /**
   * Returns the optional AddPublishedEventsTemplate Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.5">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddPublishedEventsTemplateMethodNode();

  /**
   * Sets this instance's AddPublishedEventsTemplate handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setAddPublishedEventsTemplateHandler(@Nullable AddPublishedEventsTemplateHandler handler);

  /**
   * Returns the optional RemoveDataSetFolder Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.8">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveDataSetFolderMethodNode();

  /**
   * Sets this instance's RemoveDataSetFolder handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setRemoveDataSetFolderHandler(@Nullable RemoveDataSetFolderHandler handler);

  /**
   * Returns the optional RemovePublishedDataSet Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.6">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemovePublishedDataSetMethodNode();

  /**
   * Sets this instance's RemovePublishedDataSet handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setRemovePublishedDataSetHandler(@Nullable RemovePublishedDataSetHandler handler);

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
   * Handles calls to the AddDataSetFolder Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.7">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface AddDataSetFolderHandler {
    /**
     * Handles a call to the AddDataSetFolder Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable NodeId addDataSetFolder(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable String name)
        throws UaException;
  }

  /**
   * Handles calls to the AddPublishedDataItems Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.2">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface AddPublishedDataItemsHandler {
    /**
     * Handles a call to the AddPublishedDataItems Method.
     *
     * @throws UaException if the call fails.
     */
    DataSetFolderTypeAddPublishedDataItems.Outputs addPublishedDataItems(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String name,
        @Nullable String @Nullable [] fieldNameAliases,
        DataSetFieldFlags @Nullable [] fieldFlags,
        @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
        throws UaException;
  }

  /**
   * Handles calls to the AddPublishedDataItemsTemplate Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.4">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface AddPublishedDataItemsTemplateHandler {
    /**
     * Handles a call to the AddPublishedDataItemsTemplate Method.
     *
     * @throws UaException if the call fails.
     */
    DataSetFolderTypeAddPublishedDataItemsTemplate.Outputs addPublishedDataItemsTemplate(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String name,
        @Nullable DataSetMetaDataType dataSetMetaData,
        @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
        throws UaException;
  }

  /**
   * Handles calls to the AddPublishedEvents Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.3">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface AddPublishedEventsHandler {
    /**
     * Handles a call to the AddPublishedEvents Method.
     *
     * @throws UaException if the call fails.
     */
    DataSetFolderTypeAddPublishedEvents.Outputs addPublishedEvents(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String name,
        @Nullable NodeId eventNotifier,
        @Nullable String @Nullable [] fieldNameAliases,
        DataSetFieldFlags @Nullable [] fieldFlags,
        @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
        @Nullable ContentFilter filter)
        throws UaException;
  }

  /**
   * Handles calls to the AddPublishedEventsTemplate Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.5">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface AddPublishedEventsTemplateHandler {
    /**
     * Handles a call to the AddPublishedEventsTemplate Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable NodeId addPublishedEventsTemplate(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String name,
        @Nullable DataSetMetaDataType dataSetMetaData,
        @Nullable NodeId eventNotifier,
        @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
        @Nullable ContentFilter filter)
        throws UaException;
  }

  /**
   * Handles calls to the RemoveDataSetFolder Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.8">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface RemoveDataSetFolderHandler {
    /**
     * Handles a call to the RemoveDataSetFolder Method.
     *
     * @throws UaException if the call fails.
     */
    void removeDataSetFolder(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable NodeId dataSetFolderNodeId)
        throws UaException;
  }

  /**
   * Handles calls to the RemovePublishedDataSet Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.6">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface RemovePublishedDataSetHandler {
    /**
     * Handles a call to the RemovePublishedDataSet Method.
     *
     * @throws UaException if the call fails.
     */
    void removePublishedDataSet(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable NodeId dataSetNodeId)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /**
     * Handles a call to the AddDataSetFolder Method; see {@link
     * AddDataSetFolderHandler#addDataSetFolder}.
     */
    default @Nullable NodeId addDataSetFolder(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable String name)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the AddPublishedDataItems Method; see {@link
     * AddPublishedDataItemsHandler#addPublishedDataItems}.
     */
    default DataSetFolderTypeAddPublishedDataItems.Outputs addPublishedDataItems(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String name,
        @Nullable String @Nullable [] fieldNameAliases,
        DataSetFieldFlags @Nullable [] fieldFlags,
        @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the AddPublishedDataItemsTemplate Method; see {@link
     * AddPublishedDataItemsTemplateHandler#addPublishedDataItemsTemplate}.
     */
    default DataSetFolderTypeAddPublishedDataItemsTemplate.Outputs addPublishedDataItemsTemplate(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String name,
        @Nullable DataSetMetaDataType dataSetMetaData,
        @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the AddPublishedEvents Method; see {@link
     * AddPublishedEventsHandler#addPublishedEvents}.
     */
    default DataSetFolderTypeAddPublishedEvents.Outputs addPublishedEvents(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String name,
        @Nullable NodeId eventNotifier,
        @Nullable String @Nullable [] fieldNameAliases,
        DataSetFieldFlags @Nullable [] fieldFlags,
        @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
        @Nullable ContentFilter filter)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the AddPublishedEventsTemplate Method; see {@link
     * AddPublishedEventsTemplateHandler#addPublishedEventsTemplate}.
     */
    default @Nullable NodeId addPublishedEventsTemplate(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String name,
        @Nullable DataSetMetaDataType dataSetMetaData,
        @Nullable NodeId eventNotifier,
        @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
        @Nullable ContentFilter filter)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the RemoveDataSetFolder Method; see {@link
     * RemoveDataSetFolderHandler#removeDataSetFolder}.
     */
    default void removeDataSetFolder(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable NodeId dataSetFolderNodeId)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the RemovePublishedDataSet Method; see {@link
     * RemovePublishedDataSetHandler#removePublishedDataSet}.
     */
    default void removePublishedDataSet(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable NodeId dataSetNodeId)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
