package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerStatusDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the ServerStatusType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.6">Model
 *     documentation</a>
 */
public interface ServerStatusType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2138L);

  /**
   * Returns the mandatory BuildInfo child, a BuildInfoType with DataType BuildInfo.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.7">BuildInfoType
   *     documentation</a>
   */
  BuildInfoTypeNode getBuildInfoNode();

  /**
   * Returns the Value of the BuildInfo child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable BuildInfo getBuildInfo();

  /**
   * Sets the Value of the BuildInfo child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setBuildInfo(@Nullable BuildInfo value);

  /**
   * Returns the mandatory CurrentTime child, a BaseDataVariableType with DataType UtcTime.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getCurrentTimeNode();

  /**
   * Returns the Value of the CurrentTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getCurrentTime();

  /**
   * Sets the Value of the CurrentTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCurrentTime(@Nullable DateTime value);

  /**
   * Returns the mandatory SecondsTillShutdown child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getSecondsTillShutdownNode();

  /**
   * Returns the Value of the SecondsTillShutdown child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getSecondsTillShutdown();

  /**
   * Sets the Value of the SecondsTillShutdown child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSecondsTillShutdown(@Nullable UInteger value);

  /**
   * Returns the mandatory ShutdownReason child, a BaseDataVariableType with DataType LocalizedText.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getShutdownReasonNode();

  /**
   * Returns the Value of the ShutdownReason child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getShutdownReason();

  /**
   * Sets the Value of the ShutdownReason child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setShutdownReason(@Nullable LocalizedText value);

  /**
   * Returns the mandatory StartTime child, a BaseDataVariableType with DataType UtcTime.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getStartTimeNode();

  /**
   * Returns the Value of the StartTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getStartTime();

  /**
   * Sets the Value of the StartTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setStartTime(@Nullable DateTime value);

  /**
   * Returns the mandatory State child, a BaseDataVariableType with DataType ServerState.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getStateNode();

  /**
   * Returns the Value of the State child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServerState getState();

  /**
   * Sets the Value of the State child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setState(@Nullable ServerState value);

  /**
   * Returns this node's Value.
   *
   * @throws UaRuntimeException if the Value does not convert.
   */
  @Nullable ServerStatusDataType getTypedValue();

  /**
   * Sets this node's Value.
   *
   * @throws UaRuntimeException if the value does not convert.
   */
  void setTypedValue(@Nullable ServerStatusDataType value);
}
