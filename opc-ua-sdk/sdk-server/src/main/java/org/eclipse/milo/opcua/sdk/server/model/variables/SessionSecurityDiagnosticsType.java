package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionSecurityDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the SessionSecurityDiagnosticsType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.16">Model
 *     documentation</a>
 */
public interface SessionSecurityDiagnosticsType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2244L);

  /**
   * Returns the mandatory AuthenticationMechanism child, a BaseDataVariableType with DataType
   * String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getAuthenticationMechanismNode();

  /**
   * Returns the Value of the AuthenticationMechanism child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getAuthenticationMechanism();

  /**
   * Sets the Value of the AuthenticationMechanism child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setAuthenticationMechanism(@Nullable String value);

  /**
   * Returns the mandatory ClientCertificate child, a BaseDataVariableType with DataType ByteString.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getClientCertificateNode();

  /**
   * Returns the Value of the ClientCertificate child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ByteString getClientCertificate();

  /**
   * Sets the Value of the ClientCertificate child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setClientCertificate(@Nullable ByteString value);

  /**
   * Returns the mandatory ClientUserIdHistory child, a BaseDataVariableType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getClientUserIdHistoryNode();

  /**
   * Returns the Value of the ClientUserIdHistory child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String @Nullable [] getClientUserIdHistory();

  /**
   * Sets the Value of the ClientUserIdHistory child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setClientUserIdHistory(@Nullable String @Nullable [] value);

  /**
   * Returns the mandatory ClientUserIdOfSession child, a BaseDataVariableType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getClientUserIdOfSessionNode();

  /**
   * Returns the Value of the ClientUserIdOfSession child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getClientUserIdOfSession();

  /**
   * Sets the Value of the ClientUserIdOfSession child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setClientUserIdOfSession(@Nullable String value);

  /**
   * Returns the mandatory Encoding child, a BaseDataVariableType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getEncodingNode();

  /**
   * Returns the Value of the Encoding child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getEncoding();

  /**
   * Sets the Value of the Encoding child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setEncoding(@Nullable String value);

  /**
   * Returns the mandatory SecurityMode child, a BaseDataVariableType with DataType
   * MessageSecurityMode.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getSecurityModeNode();

  /**
   * Returns the Value of the SecurityMode child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable MessageSecurityMode getSecurityMode();

  /**
   * Sets the Value of the SecurityMode child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSecurityMode(@Nullable MessageSecurityMode value);

  /**
   * Returns the mandatory SecurityPolicyUri child, a BaseDataVariableType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getSecurityPolicyUriNode();

  /**
   * Returns the Value of the SecurityPolicyUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getSecurityPolicyUri();

  /**
   * Sets the Value of the SecurityPolicyUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSecurityPolicyUri(@Nullable String value);

  /**
   * Returns the mandatory SessionId child, a BaseDataVariableType with DataType NodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getSessionIdNode();

  /**
   * Returns the Value of the SessionId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable NodeId getSessionId();

  /**
   * Sets the Value of the SessionId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSessionId(@Nullable NodeId value);

  /**
   * Returns the mandatory TransportProtocol child, a BaseDataVariableType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getTransportProtocolNode();

  /**
   * Returns the Value of the TransportProtocol child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getTransportProtocol();

  /**
   * Sets the Value of the TransportProtocol child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setTransportProtocol(@Nullable String value);

  /**
   * Returns this node's Value.
   *
   * @throws UaRuntimeException if the Value does not convert.
   */
  @Nullable SessionSecurityDiagnosticsDataType getTypedValue();

  /**
   * Sets this node's Value.
   *
   * @throws UaRuntimeException if the value does not convert.
   */
  void setTypedValue(@Nullable SessionSecurityDiagnosticsDataType value);
}
