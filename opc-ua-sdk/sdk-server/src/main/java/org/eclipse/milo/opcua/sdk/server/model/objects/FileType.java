package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the FileType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.1">Model
 *     documentation</a>
 */
public interface FileType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 11575L);

  /**
   * Returns the optional LastModifiedTime child, a PropertyType with DataType DateTime.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getLastModifiedTimeNode();

  /**
   * Returns the Value of the LastModifiedTime child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getLastModifiedTime();

  /**
   * Sets the Value of the LastModifiedTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLastModifiedTime(@Nullable DateTime value);

  /**
   * Returns the optional MaxByteStringLength child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxByteStringLengthNode();

  /**
   * Returns the Value of the MaxByteStringLength child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxByteStringLength();

  /**
   * Sets the Value of the MaxByteStringLength child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxByteStringLength(@Nullable UInteger value);

  /**
   * Returns the optional MimeType child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMimeTypeNode();

  /**
   * Returns the Value of the MimeType child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getMimeType();

  /**
   * Sets the Value of the MimeType child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMimeType(@Nullable String value);

  /**
   * Returns the mandatory OpenCount child, a PropertyType with DataType UInt16.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getOpenCountNode();

  /**
   * Returns the Value of the OpenCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UShort getOpenCount();

  /**
   * Sets the Value of the OpenCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setOpenCount(@Nullable UShort value);

  /**
   * Returns the mandatory Size child, a PropertyType with DataType UInt64.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getSizeNode();

  /**
   * Returns the Value of the Size child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ULong getSize();

  /**
   * Sets the Value of the Size child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSize(@Nullable ULong value);

  /**
   * Returns the mandatory UserWritable child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getUserWritableNode();

  /**
   * Returns the Value of the UserWritable child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getUserWritable();

  /**
   * Sets the Value of the UserWritable child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setUserWritable(@Nullable Boolean value);

  /**
   * Returns the mandatory Writable child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getWritableNode();

  /**
   * Returns the Value of the Writable child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getWritable();

  /**
   * Sets the Value of the Writable child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setWritable(@Nullable Boolean value);

  /**
   * Returns the mandatory Close Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.3">Model
   *     documentation</a>
   */
  UaMethodNode getCloseMethodNode();

  /**
   * Sets this instance's Close handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setCloseHandler(@Nullable CloseHandler handler);

  /**
   * Returns the mandatory GetPosition Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.6">Model
   *     documentation</a>
   */
  UaMethodNode getGetPositionMethodNode();

  /**
   * Sets this instance's GetPosition handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setGetPositionHandler(@Nullable GetPositionHandler handler);

  /**
   * Returns the mandatory Open Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.2">Model
   *     documentation</a>
   */
  UaMethodNode getOpenMethodNode();

  /**
   * Sets this instance's Open handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setOpenHandler(@Nullable OpenHandler handler);

  /**
   * Returns the mandatory Read Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.4">Model
   *     documentation</a>
   */
  UaMethodNode getReadMethodNode();

  /**
   * Sets this instance's Read handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setReadHandler(@Nullable ReadHandler handler);

  /**
   * Returns the mandatory SetPosition Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.7">Model
   *     documentation</a>
   */
  UaMethodNode getSetPositionMethodNode();

  /**
   * Sets this instance's SetPosition handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setSetPositionHandler(@Nullable SetPositionHandler handler);

  /**
   * Returns the mandatory Write Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.5">Model
   *     documentation</a>
   */
  UaMethodNode getWriteMethodNode();

  /**
   * Sets this instance's Write handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setWriteHandler(@Nullable WriteHandler handler);

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
   * Handles calls to the Close Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.3">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface CloseHandler {
    /**
     * Handles a call to the Close Method.
     *
     * @throws UaException if the call fails.
     */
    void close(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable UInteger fileHandle)
        throws UaException;
  }

  /**
   * Handles calls to the GetPosition Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.6">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface GetPositionHandler {
    /**
     * Handles a call to the GetPosition Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable ULong getPosition(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable UInteger fileHandle)
        throws UaException;
  }

  /**
   * Handles calls to the Open Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.2">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface OpenHandler {
    /**
     * Handles a call to the Open Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable UInteger open(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable UByte mode)
        throws UaException;
  }

  /**
   * Handles calls to the Read Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.4">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface ReadHandler {
    /**
     * Handles a call to the Read Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable ByteString read(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable UInteger fileHandle,
        @Nullable Integer length)
        throws UaException;
  }

  /**
   * Handles calls to the SetPosition Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.7">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface SetPositionHandler {
    /**
     * Handles a call to the SetPosition Method.
     *
     * @throws UaException if the call fails.
     */
    void setPosition(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable UInteger fileHandle,
        @Nullable ULong position)
        throws UaException;
  }

  /**
   * Handles calls to the Write Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.5">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface WriteHandler {
    /**
     * Handles a call to the Write Method.
     *
     * @throws UaException if the call fails.
     */
    void write(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable UInteger fileHandle,
        @Nullable ByteString data)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /** Handles a call to the Close Method; see {@link CloseHandler#close}. */
    default void close(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable UInteger fileHandle)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the GetPosition Method; see {@link GetPositionHandler#getPosition}. */
    default @Nullable ULong getPosition(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable UInteger fileHandle)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the Open Method; see {@link OpenHandler#open}. */
    default @Nullable UInteger open(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable UByte mode)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the Read Method; see {@link ReadHandler#read}. */
    default @Nullable ByteString read(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable UInteger fileHandle,
        @Nullable Integer length)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the SetPosition Method; see {@link SetPositionHandler#setPosition}. */
    default void setPosition(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable UInteger fileHandle,
        @Nullable ULong position)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the Write Method; see {@link WriteHandler#write}. */
    default void write(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable UInteger fileHandle,
        @Nullable ByteString data)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
