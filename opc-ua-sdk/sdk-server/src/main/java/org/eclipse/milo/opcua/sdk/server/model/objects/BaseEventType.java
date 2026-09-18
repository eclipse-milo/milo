package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.TimeZoneDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the BaseEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.2">Model
 *     documentation</a>
 */
public interface BaseEventType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2041L);

  /**
   * Returns the optional ConditionClassId child, a PropertyType with DataType NodeId.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getConditionClassIdNode();

  /**
   * Returns the Value of the ConditionClassId child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable NodeId getConditionClassId();

  /**
   * Sets the Value of the ConditionClassId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setConditionClassId(@Nullable NodeId value);

  /**
   * Returns the optional ConditionClassName child, a PropertyType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getConditionClassNameNode();

  /**
   * Returns the Value of the ConditionClassName child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getConditionClassName();

  /**
   * Sets the Value of the ConditionClassName child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setConditionClassName(@Nullable LocalizedText value);

  /**
   * Returns the optional ConditionSubClassId child, a PropertyType with DataType NodeId.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getConditionSubClassIdNode();

  /**
   * Returns the Value of the ConditionSubClassId child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  NodeId @Nullable [] getConditionSubClassId();

  /**
   * Sets the Value of the ConditionSubClassId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setConditionSubClassId(NodeId @Nullable [] value);

  /**
   * Returns the optional ConditionSubClassName child, a PropertyType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getConditionSubClassNameNode();

  /**
   * Returns the Value of the ConditionSubClassName child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  LocalizedText @Nullable [] getConditionSubClassName();

  /**
   * Sets the Value of the ConditionSubClassName child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setConditionSubClassName(LocalizedText @Nullable [] value);

  /**
   * Returns the mandatory EventId child, a PropertyType with DataType ByteString.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getEventIdNode();

  /**
   * Returns the Value of the EventId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ByteString getEventId();

  /**
   * Sets the Value of the EventId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setEventId(@Nullable ByteString value);

  /**
   * Returns the mandatory EventType child, a PropertyType with DataType NodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getEventTypeNode();

  /**
   * Returns the Value of the EventType child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable NodeId getEventType();

  /**
   * Sets the Value of the EventType child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setEventType(@Nullable NodeId value);

  /**
   * Returns the optional LocalTime child, a PropertyType with DataType TimeZoneDataType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getLocalTimeNode();

  /**
   * Returns the Value of the LocalTime child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable TimeZoneDataType getLocalTime();

  /**
   * Sets the Value of the LocalTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLocalTime(@Nullable TimeZoneDataType value);

  /**
   * Returns the mandatory Message child, a PropertyType with DataType LocalizedText.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getMessageNode();

  /**
   * Returns the Value of the Message child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getMessage();

  /**
   * Sets the Value of the Message child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMessage(@Nullable LocalizedText value);

  /**
   * Returns the mandatory ReceiveTime child, a PropertyType with DataType UtcTime.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getReceiveTimeNode();

  /**
   * Returns the Value of the ReceiveTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getReceiveTime();

  /**
   * Sets the Value of the ReceiveTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setReceiveTime(@Nullable DateTime value);

  /**
   * Returns the mandatory Severity child, a PropertyType with DataType UInt16.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getSeverityNode();

  /**
   * Returns the Value of the Severity child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UShort getSeverity();

  /**
   * Sets the Value of the Severity child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSeverity(@Nullable UShort value);

  /**
   * Returns the mandatory SourceName child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getSourceNameNode();

  /**
   * Returns the Value of the SourceName child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getSourceName();

  /**
   * Sets the Value of the SourceName child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSourceName(@Nullable String value);

  /**
   * Returns the mandatory SourceNode child, a PropertyType with DataType NodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getSourceNodeNode();

  /**
   * Returns the Value of the SourceNode child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable NodeId getSourceNode();

  /**
   * Sets the Value of the SourceNode child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSourceNode(@Nullable NodeId value);

  /**
   * Returns the mandatory Time child, a PropertyType with DataType UtcTime.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getTimeNode();

  /**
   * Returns the Value of the Time child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getTime();

  /**
   * Sets the Value of the Time child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setTime(@Nullable DateTime value);
}
