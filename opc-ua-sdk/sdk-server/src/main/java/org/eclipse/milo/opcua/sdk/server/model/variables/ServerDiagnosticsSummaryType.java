package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerDiagnosticsSummaryDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the ServerDiagnosticsSummaryType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.8">Model
 *     documentation</a>
 */
public interface ServerDiagnosticsSummaryType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2150L);

  /**
   * Returns the mandatory CumulatedSessionCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getCumulatedSessionCountNode();

  /**
   * Returns the Value of the CumulatedSessionCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getCumulatedSessionCount();

  /**
   * Sets the Value of the CumulatedSessionCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCumulatedSessionCount(@Nullable UInteger value);

  /**
   * Returns the mandatory CumulatedSubscriptionCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getCumulatedSubscriptionCountNode();

  /**
   * Returns the Value of the CumulatedSubscriptionCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getCumulatedSubscriptionCount();

  /**
   * Sets the Value of the CumulatedSubscriptionCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCumulatedSubscriptionCount(@Nullable UInteger value);

  /**
   * Returns the mandatory CurrentSessionCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getCurrentSessionCountNode();

  /**
   * Returns the Value of the CurrentSessionCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getCurrentSessionCount();

  /**
   * Sets the Value of the CurrentSessionCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCurrentSessionCount(@Nullable UInteger value);

  /**
   * Returns the mandatory CurrentSubscriptionCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getCurrentSubscriptionCountNode();

  /**
   * Returns the Value of the CurrentSubscriptionCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getCurrentSubscriptionCount();

  /**
   * Sets the Value of the CurrentSubscriptionCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCurrentSubscriptionCount(@Nullable UInteger value);

  /**
   * Returns the mandatory PublishingIntervalCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getPublishingIntervalCountNode();

  /**
   * Returns the Value of the PublishingIntervalCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getPublishingIntervalCount();

  /**
   * Sets the Value of the PublishingIntervalCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPublishingIntervalCount(@Nullable UInteger value);

  /**
   * Returns the mandatory RejectedRequestsCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getRejectedRequestsCountNode();

  /**
   * Returns the Value of the RejectedRequestsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getRejectedRequestsCount();

  /**
   * Sets the Value of the RejectedRequestsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setRejectedRequestsCount(@Nullable UInteger value);

  /**
   * Returns the mandatory RejectedSessionCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getRejectedSessionCountNode();

  /**
   * Returns the Value of the RejectedSessionCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getRejectedSessionCount();

  /**
   * Sets the Value of the RejectedSessionCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setRejectedSessionCount(@Nullable UInteger value);

  /**
   * Returns the mandatory SecurityRejectedRequestsCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getSecurityRejectedRequestsCountNode();

  /**
   * Returns the Value of the SecurityRejectedRequestsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getSecurityRejectedRequestsCount();

  /**
   * Sets the Value of the SecurityRejectedRequestsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSecurityRejectedRequestsCount(@Nullable UInteger value);

  /**
   * Returns the mandatory SecurityRejectedSessionCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getSecurityRejectedSessionCountNode();

  /**
   * Returns the Value of the SecurityRejectedSessionCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getSecurityRejectedSessionCount();

  /**
   * Sets the Value of the SecurityRejectedSessionCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSecurityRejectedSessionCount(@Nullable UInteger value);

  /**
   * Returns the mandatory ServerViewCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getServerViewCountNode();

  /**
   * Returns the Value of the ServerViewCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getServerViewCount();

  /**
   * Sets the Value of the ServerViewCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setServerViewCount(@Nullable UInteger value);

  /**
   * Returns the mandatory SessionAbortCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getSessionAbortCountNode();

  /**
   * Returns the Value of the SessionAbortCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getSessionAbortCount();

  /**
   * Sets the Value of the SessionAbortCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSessionAbortCount(@Nullable UInteger value);

  /**
   * Returns the mandatory SessionTimeoutCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getSessionTimeoutCountNode();

  /**
   * Returns the Value of the SessionTimeoutCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getSessionTimeoutCount();

  /**
   * Sets the Value of the SessionTimeoutCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSessionTimeoutCount(@Nullable UInteger value);

  /**
   * Returns this node's Value.
   *
   * @throws UaRuntimeException if the Value does not convert.
   */
  @Nullable ServerDiagnosticsSummaryDataType getTypedValue();

  /**
   * Sets this node's Value.
   *
   * @throws UaRuntimeException if the value does not convert.
   */
  void setTypedValue(@Nullable ServerDiagnosticsSummaryDataType value);
}
