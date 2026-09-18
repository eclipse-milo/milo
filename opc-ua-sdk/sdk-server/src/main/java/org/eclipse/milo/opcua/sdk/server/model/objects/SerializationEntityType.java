package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.Structure;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the SerializationEntityType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part25/6.3.1">Model
 *     documentation</a>
 */
public interface SerializationEntityType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 19824L);

  /**
   * Returns the optional ConsiderSubElementSerializationProperties child, a PropertyType with
   * DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getConsiderSubElementSerializationPropertiesNode();

  /**
   * Returns the Value of the ConsiderSubElementSerializationProperties child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getConsiderSubElementSerializationProperties();

  /**
   * Sets the Value of the ConsiderSubElementSerializationProperties child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setConsiderSubElementSerializationProperties(@Nullable Boolean value);

  /**
   * Returns the optional CustomMetaDataProperties child, a PropertyType with DataType KeyValuePair.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getCustomMetaDataPropertiesNode();

  /**
   * Returns the Value of the CustomMetaDataProperties child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable KeyValuePair @Nullable [] getCustomMetaDataProperties();

  /**
   * Sets the Value of the CustomMetaDataProperties child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCustomMetaDataProperties(@Nullable KeyValuePair @Nullable [] value);

  /**
   * Returns the optional CustomMetaDataRef child, a PropertyType with DataType NodeId.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getCustomMetaDataRefNode();

  /**
   * Returns the Value of the CustomMetaDataRef child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable NodeId getCustomMetaDataRef();

  /**
   * Sets the Value of the CustomMetaDataRef child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCustomMetaDataRef(@Nullable NodeId value);

  /**
   * Returns the optional ExcludeReferenceTypes child, a PropertyType with DataType NodeId.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getExcludeReferenceTypesNode();

  /**
   * Returns the Value of the ExcludeReferenceTypes child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  NodeId @Nullable [] getExcludeReferenceTypes();

  /**
   * Sets the Value of the ExcludeReferenceTypes child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setExcludeReferenceTypes(NodeId @Nullable [] value);

  /**
   * Returns the optional IncludeDictionaryReference child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getIncludeDictionaryReferenceNode();

  /**
   * Returns the Value of the IncludeDictionaryReference child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getIncludeDictionaryReference();

  /**
   * Sets the Value of the IncludeDictionaryReference child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setIncludeDictionaryReference(@Nullable Boolean value);

  /**
   * Returns the optional IncludeReferenceTypes child, a PropertyType with DataType NodeId.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getIncludeReferenceTypesNode();

  /**
   * Returns the Value of the IncludeReferenceTypes child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  NodeId @Nullable [] getIncludeReferenceTypes();

  /**
   * Sets the Value of the IncludeReferenceTypes child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setIncludeReferenceTypes(NodeId @Nullable [] value);

  /**
   * Returns the optional IncludeSourceTimestamp child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getIncludeSourceTimestampNode();

  /**
   * Returns the Value of the IncludeSourceTimestamp child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getIncludeSourceTimestamp();

  /**
   * Sets the Value of the IncludeSourceTimestamp child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setIncludeSourceTimestamp(@Nullable Boolean value);

  /**
   * Returns the optional IncludeStatus child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getIncludeStatusNode();

  /**
   * Returns the Value of the IncludeStatus child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getIncludeStatus();

  /**
   * Sets the Value of the IncludeStatus child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setIncludeStatus(@Nullable Boolean value);

  /**
   * Returns the optional SerializationDepth child, a PropertyType with DataType UInt16.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getSerializationDepthNode();

  /**
   * Returns the Value of the SerializationDepth child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UShort getSerializationDepth();

  /**
   * Sets the Value of the SerializationDepth child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSerializationDepth(@Nullable UShort value);

  /**
   * Returns the mandatory SerializedData child, a BaseDataVariableType with DataType Structure.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getSerializedDataNode();

  /**
   * Returns the Value of the SerializedData child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Structure getSerializedData();

  /**
   * Sets the Value of the SerializedData child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSerializedData(@Nullable Structure value);

  /**
   * Returns the optional ConfigureSerialization Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part25/6.3.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getConfigureSerializationMethodNode();

  /**
   * Sets this instance's ConfigureSerialization handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setConfigureSerializationHandler(@Nullable ConfigureSerializationHandler handler);

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
   * Handles calls to the ConfigureSerialization Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part25/6.3.3">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface ConfigureSerializationHandler {
    /**
     * Handles a call to the ConfigureSerialization Method.
     *
     * @throws UaException if the call fails.
     */
    Integer @Nullable [] configureSerialization(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable KeyValuePair @Nullable [] serializationFilterProperties)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /**
     * Handles a call to the ConfigureSerialization Method; see {@link
     * ConfigureSerializationHandler#configureSerialization}.
     */
    default Integer @Nullable [] configureSerialization(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable KeyValuePair @Nullable [] serializationFilterProperties)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
