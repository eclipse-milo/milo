package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SamplingIntervalDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ServerDiagnosticsSummaryTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SubscriptionDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.SamplingIntervalDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerDiagnosticsSummaryDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the ServerDiagnosticsType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.3">Model
 *     documentation</a>
 */
public interface ServerDiagnosticsType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2020L);

  /**
   * Returns the mandatory EnabledFlag child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getEnabledFlagNode();

  /**
   * Returns the Value of the EnabledFlag child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getEnabledFlag();

  /**
   * Sets the Value of the EnabledFlag child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setEnabledFlag(@Nullable Boolean value);

  /**
   * Returns the optional SamplingIntervalDiagnosticsArray child, a
   * SamplingIntervalDiagnosticsArrayType with DataType SamplingIntervalDiagnosticsDataType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.9">SamplingIntervalDiagnosticsArrayType
   *     documentation</a>
   */
  @Nullable SamplingIntervalDiagnosticsArrayTypeNode getSamplingIntervalDiagnosticsArrayNode();

  /**
   * Returns the Value of the SamplingIntervalDiagnosticsArray child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable SamplingIntervalDiagnosticsDataType @Nullable [] getSamplingIntervalDiagnosticsArray();

  /**
   * Sets the Value of the SamplingIntervalDiagnosticsArray child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSamplingIntervalDiagnosticsArray(
      @Nullable SamplingIntervalDiagnosticsDataType @Nullable [] value);

  /**
   * Returns the mandatory ServerDiagnosticsSummary child, a ServerDiagnosticsSummaryType with
   * DataType ServerDiagnosticsSummaryDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.8">ServerDiagnosticsSummaryType
   *     documentation</a>
   */
  ServerDiagnosticsSummaryTypeNode getServerDiagnosticsSummaryNode();

  /**
   * Returns the Value of the ServerDiagnosticsSummary child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServerDiagnosticsSummaryDataType getServerDiagnosticsSummary();

  /**
   * Sets the Value of the ServerDiagnosticsSummary child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setServerDiagnosticsSummary(@Nullable ServerDiagnosticsSummaryDataType value);

  /**
   * Returns the mandatory SessionsDiagnosticsSummary child, a SessionsDiagnosticsSummaryType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.4">SessionsDiagnosticsSummaryType
   *     documentation</a>
   */
  SessionsDiagnosticsSummaryTypeNode getSessionsDiagnosticsSummaryNode();

  /**
   * Returns the mandatory SubscriptionDiagnosticsArray child, a SubscriptionDiagnosticsArrayType
   * with DataType SubscriptionDiagnosticsDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.11">SubscriptionDiagnosticsArrayType
   *     documentation</a>
   */
  SubscriptionDiagnosticsArrayTypeNode getSubscriptionDiagnosticsArrayNode();

  /**
   * Returns the Value of the SubscriptionDiagnosticsArray child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable SubscriptionDiagnosticsDataType @Nullable [] getSubscriptionDiagnosticsArray();

  /**
   * Sets the Value of the SubscriptionDiagnosticsArray child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSubscriptionDiagnosticsArray(
      @Nullable SubscriptionDiagnosticsDataType @Nullable [] value);
}
