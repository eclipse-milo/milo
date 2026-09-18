package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionSecurityDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the SessionSecurityDiagnosticsType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.16">Model
 *     documentation</a>
 */
public interface SessionSecurityDiagnosticsType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2244L);

  /**
   * Resolves the mandatory SecurityMode child, a BaseDataVariableType with DataType
   * MessageSecurityMode.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getSecurityModeNode() throws UaException;

  /** Asynchronous form of {@link #getSecurityModeNode()}. */
  CompletableFuture<? extends VariableNode> getSecurityModeNodeAsync();

  /**
   * Reads the Value of the SecurityMode child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable MessageSecurityMode readSecurityMode() throws UaException;

  /**
   * Writes the Value of the SecurityMode child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSecurityMode(@Nullable MessageSecurityMode value) throws UaException;

  /** Asynchronous form of {@link #readSecurityMode()}. */
  CompletableFuture<? extends @Nullable MessageSecurityMode> readSecurityModeAsync();

  /** Asynchronous form of {@link #writeSecurityMode}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSecurityModeAsync(@Nullable MessageSecurityMode value);

  /**
   * Resolves the mandatory ClientCertificate child, a BaseDataVariableType with DataType
   * ByteString.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getClientCertificateNode() throws UaException;

  /** Asynchronous form of {@link #getClientCertificateNode()}. */
  CompletableFuture<? extends VariableNode> getClientCertificateNodeAsync();

  /**
   * Reads the Value of the ClientCertificate child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ByteString readClientCertificate() throws UaException;

  /**
   * Writes the Value of the ClientCertificate child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeClientCertificate(@Nullable ByteString value) throws UaException;

  /** Asynchronous form of {@link #readClientCertificate()}. */
  CompletableFuture<? extends @Nullable ByteString> readClientCertificateAsync();

  /** Asynchronous form of {@link #writeClientCertificate}; completes with the operation status. */
  CompletableFuture<StatusCode> writeClientCertificateAsync(@Nullable ByteString value);

  /**
   * Resolves the mandatory SecurityPolicyUri child, a BaseDataVariableType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getSecurityPolicyUriNode() throws UaException;

  /** Asynchronous form of {@link #getSecurityPolicyUriNode()}. */
  CompletableFuture<? extends VariableNode> getSecurityPolicyUriNodeAsync();

  /**
   * Reads the Value of the SecurityPolicyUri child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readSecurityPolicyUri() throws UaException;

  /**
   * Writes the Value of the SecurityPolicyUri child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSecurityPolicyUri(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readSecurityPolicyUri()}. */
  CompletableFuture<? extends @Nullable String> readSecurityPolicyUriAsync();

  /** Asynchronous form of {@link #writeSecurityPolicyUri}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSecurityPolicyUriAsync(@Nullable String value);

  /**
   * Resolves the mandatory TransportProtocol child, a BaseDataVariableType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getTransportProtocolNode() throws UaException;

  /** Asynchronous form of {@link #getTransportProtocolNode()}. */
  CompletableFuture<? extends VariableNode> getTransportProtocolNodeAsync();

  /**
   * Reads the Value of the TransportProtocol child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readTransportProtocol() throws UaException;

  /**
   * Writes the Value of the TransportProtocol child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTransportProtocol(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readTransportProtocol()}. */
  CompletableFuture<? extends @Nullable String> readTransportProtocolAsync();

  /** Asynchronous form of {@link #writeTransportProtocol}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTransportProtocolAsync(@Nullable String value);

  /**
   * Resolves the mandatory ClientUserIdHistory child, a BaseDataVariableType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getClientUserIdHistoryNode() throws UaException;

  /** Asynchronous form of {@link #getClientUserIdHistoryNode()}. */
  CompletableFuture<? extends VariableNode> getClientUserIdHistoryNodeAsync();

  /**
   * Reads the Value of the ClientUserIdHistory child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String @Nullable [] readClientUserIdHistory() throws UaException;

  /**
   * Writes the Value of the ClientUserIdHistory child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeClientUserIdHistory(@Nullable String @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readClientUserIdHistory()}. */
  CompletableFuture<? extends @Nullable String @Nullable []> readClientUserIdHistoryAsync();

  /**
   * Asynchronous form of {@link #writeClientUserIdHistory}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeClientUserIdHistoryAsync(@Nullable String @Nullable [] value);

  /**
   * Resolves the mandatory ClientUserIdOfSession child, a BaseDataVariableType with DataType
   * String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getClientUserIdOfSessionNode() throws UaException;

  /** Asynchronous form of {@link #getClientUserIdOfSessionNode()}. */
  CompletableFuture<? extends VariableNode> getClientUserIdOfSessionNodeAsync();

  /**
   * Reads the Value of the ClientUserIdOfSession child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readClientUserIdOfSession() throws UaException;

  /**
   * Writes the Value of the ClientUserIdOfSession child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeClientUserIdOfSession(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readClientUserIdOfSession()}. */
  CompletableFuture<? extends @Nullable String> readClientUserIdOfSessionAsync();

  /**
   * Asynchronous form of {@link #writeClientUserIdOfSession}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeClientUserIdOfSessionAsync(@Nullable String value);

  /**
   * Resolves the mandatory AuthenticationMechanism child, a BaseDataVariableType with DataType
   * String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getAuthenticationMechanismNode() throws UaException;

  /** Asynchronous form of {@link #getAuthenticationMechanismNode()}. */
  CompletableFuture<? extends VariableNode> getAuthenticationMechanismNodeAsync();

  /**
   * Reads the Value of the AuthenticationMechanism child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readAuthenticationMechanism() throws UaException;

  /**
   * Writes the Value of the AuthenticationMechanism child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeAuthenticationMechanism(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readAuthenticationMechanism()}. */
  CompletableFuture<? extends @Nullable String> readAuthenticationMechanismAsync();

  /**
   * Asynchronous form of {@link #writeAuthenticationMechanism}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeAuthenticationMechanismAsync(@Nullable String value);

  /**
   * Resolves the mandatory Encoding child, a BaseDataVariableType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getEncodingNode() throws UaException;

  /** Asynchronous form of {@link #getEncodingNode()}. */
  CompletableFuture<? extends VariableNode> getEncodingNodeAsync();

  /**
   * Reads the Value of the Encoding child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readEncoding() throws UaException;

  /**
   * Writes the Value of the Encoding child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeEncoding(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readEncoding()}. */
  CompletableFuture<? extends @Nullable String> readEncodingAsync();

  /** Asynchronous form of {@link #writeEncoding}; completes with the operation status. */
  CompletableFuture<StatusCode> writeEncodingAsync(@Nullable String value);

  /**
   * Resolves the mandatory SessionId child, a BaseDataVariableType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getSessionIdNode() throws UaException;

  /** Asynchronous form of {@link #getSessionIdNode()}. */
  CompletableFuture<? extends VariableNode> getSessionIdNodeAsync();

  /**
   * Reads the Value of the SessionId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NodeId readSessionId() throws UaException;

  /**
   * Writes the Value of the SessionId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSessionId(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readSessionId()}. */
  CompletableFuture<? extends @Nullable NodeId> readSessionIdAsync();

  /** Asynchronous form of {@link #writeSessionId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSessionIdAsync(@Nullable NodeId value);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable SessionSecurityDiagnosticsDataType readTypedValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTypedValue(@Nullable SessionSecurityDiagnosticsDataType value) throws UaException;

  /** Asynchronous form of {@link #readTypedValue()}. */
  CompletableFuture<? extends @Nullable SessionSecurityDiagnosticsDataType> readTypedValueAsync();

  /** Asynchronous form of {@link #writeTypedValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTypedValueAsync(
      @Nullable SessionSecurityDiagnosticsDataType value);
}
