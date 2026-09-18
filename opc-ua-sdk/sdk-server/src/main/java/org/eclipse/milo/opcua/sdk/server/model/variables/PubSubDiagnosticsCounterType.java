package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubDiagnosticsCounterClassification;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the PubSubDiagnosticsCounterType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.5">Model
 *     documentation</a>
 */
public interface PubSubDiagnosticsCounterType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 19725L);

  /**
   * Returns the mandatory Active child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getActiveNode();

  /**
   * Returns the Value of the Active child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getActive();

  /**
   * Sets the Value of the Active child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setActive(@Nullable Boolean value);

  /**
   * Returns the mandatory Classification child, a PropertyType with DataType
   * PubSubDiagnosticsCounterClassification.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getClassificationNode();

  /**
   * Returns the Value of the Classification child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable PubSubDiagnosticsCounterClassification getClassification();

  /**
   * Sets the Value of the Classification child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setClassification(@Nullable PubSubDiagnosticsCounterClassification value);

  /**
   * Returns the mandatory DiagnosticsLevel child, a PropertyType with DataType DiagnosticsLevel.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getDiagnosticsLevelNode();

  /**
   * Returns the Value of the DiagnosticsLevel child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DiagnosticsLevel getDiagnosticsLevel();

  /**
   * Sets the Value of the DiagnosticsLevel child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDiagnosticsLevel(@Nullable DiagnosticsLevel value);

  /**
   * Returns the optional TimeFirstChange child, a PropertyType with DataType DateTime.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getTimeFirstChangeNode();

  /**
   * Returns the Value of the TimeFirstChange child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getTimeFirstChange();

  /**
   * Sets the Value of the TimeFirstChange child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setTimeFirstChange(@Nullable DateTime value);

  /**
   * Returns this node's Value.
   *
   * @throws UaRuntimeException if the Value does not convert.
   */
  @Nullable UInteger getTypedValue();

  /**
   * Sets this node's Value.
   *
   * @throws UaRuntimeException if the value does not convert.
   */
  void setTypedValue(@Nullable UInteger value);
}
