/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodHandlerResult;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface FileType extends BaseObjectType {
  QualifiedProperty<ULong> SIZE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Size",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=9"),
          -1,
          ULong.class);

  QualifiedProperty<Boolean> WRITABLE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Writable",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> USER_WRITABLE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UserWritable",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<UShort> OPEN_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "OpenCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<String> MIME_TYPE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MimeType",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<UInteger> MAX_BYTE_STRING_LENGTH =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxByteStringLength",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<DateTime> LAST_MODIFIED_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastModifiedTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=13"),
          -1,
          DateTime.class);

  /** Gets the existing node's local value. */
  @Nullable ULong getSize();

  /** Sets the existing node's local value. */
  void setSize(@Nullable ULong value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSizeNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getWritable();

  /** Sets the existing node's local value. */
  void setWritable(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getWritableNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getUserWritable();

  /** Sets the existing node's local value. */
  void setUserWritable(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getUserWritableNode();

  /** Gets the existing node's local value. */
  @Nullable UShort getOpenCount();

  /** Sets the existing node's local value. */
  void setOpenCount(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getOpenCountNode();

  /** Gets the existing node's local value. */
  @Nullable String getMimeType();

  /** Sets the existing node's local value. */
  void setMimeType(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMimeTypeNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxByteStringLength();

  /** Sets the existing node's local value. */
  void setMaxByteStringLength(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxByteStringLengthNode();

  /** Gets the existing node's local value. */
  @Nullable DateTime getLastModifiedTime();

  /** Sets the existing node's local value. */
  void setLastModifiedTime(@Nullable DateTime value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getLastModifiedTimeNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.2
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getOpenMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.2 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindOpen(MethodBindings bindings, OpenHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.2 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindOpenDetailed(MethodBindings bindings, OpenDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.3
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getCloseMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.3 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindClose(MethodBindings bindings, CloseHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.3 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindCloseDetailed(MethodBindings bindings, CloseDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.4
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getReadMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.4 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRead(MethodBindings bindings, ReadHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.4 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindReadDetailed(MethodBindings bindings, ReadDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.5
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getWriteMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.5 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindWrite(MethodBindings bindings, WriteHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.5 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindWriteDetailed(MethodBindings bindings, WriteDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.6
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getGetPositionMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.6 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindGetPosition(MethodBindings bindings, GetPositionHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.6 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindGetPositionDetailed(MethodBindings bindings, GetPositionDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.7
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getSetPositionMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.7 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindSetPosition(MethodBindings bindings, SetPositionHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.7 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindSetPositionDetailed(MethodBindings bindings, SetPositionDetailedHandler handler)
      throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.2 */
  @FunctionalInterface
  interface OpenHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable UInteger invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UByte mode)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.2 */
  @FunctionalInterface
  interface OpenDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable UInteger> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UByte mode)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.3 */
  @FunctionalInterface
  interface CloseHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger fileHandle)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.3 */
  @FunctionalInterface
  interface CloseDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger fileHandle)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.4 */
  @FunctionalInterface
  interface ReadHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable ByteString invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger fileHandle,
        @Nullable Integer length)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.4 */
  @FunctionalInterface
  interface ReadDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable ByteString> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger fileHandle,
        @Nullable Integer length)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.5 */
  @FunctionalInterface
  interface WriteHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger fileHandle,
        @Nullable ByteString data)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.5 */
  @FunctionalInterface
  interface WriteDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger fileHandle,
        @Nullable ByteString data)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.6 */
  @FunctionalInterface
  interface GetPositionHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable ULong invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger fileHandle)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.6 */
  @FunctionalInterface
  interface GetPositionDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable ULong> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger fileHandle)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.7 */
  @FunctionalInterface
  interface SetPositionHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger fileHandle,
        @Nullable ULong position)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.7 */
  @FunctionalInterface
  interface SetPositionDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger fileHandle,
        @Nullable ULong position)
        throws UaException;
  }
}
