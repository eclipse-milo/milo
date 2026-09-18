package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteTransitionVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ProgramDiagnostic2TypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.ProgramDiagnostic2DataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the ProgramStateMachineType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part10/5.2.1">Model
 *     documentation</a>
 */
public interface ProgramStateMachineType extends FiniteStateMachineType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2391L);

  /**
   * Returns the mandatory AutoDelete child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getAutoDeleteNode();

  /**
   * Returns the Value of the AutoDelete child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getAutoDelete();

  /**
   * Sets the Value of the AutoDelete child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setAutoDelete(@Nullable Boolean value);

  /**
   * Returns the mandatory Deletable child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getDeletableNode();

  /**
   * Returns the Value of the Deletable child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getDeletable();

  /**
   * Sets the Value of the Deletable child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDeletable(@Nullable Boolean value);

  /**
   * Returns the optional FinalResultData child, a BaseObjectType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part10/A.2.6/#A.2.6.2">Model
   *     documentation</a>
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.2">BaseObjectType
   *     documentation</a>
   */
  @Nullable BaseObjectTypeNode getFinalResultDataNode();

  /**
   * Returns the mandatory LastTransition child, a FiniteTransitionVariableType with DataType
   * LocalizedText.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.7">FiniteTransitionVariableType
   *     documentation</a>
   */
  FiniteTransitionVariableTypeNode getLastTransitionNode();

  /**
   * Returns the optional ProgramDiagnostic child, a ProgramDiagnostic2Type with DataType
   * ProgramDiagnostic2DataType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part10/5.2.9">ProgramDiagnostic2Type
   *     documentation</a>
   */
  @Nullable ProgramDiagnostic2TypeNode getProgramDiagnosticNode();

  /**
   * Returns the Value of the ProgramDiagnostic child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ProgramDiagnostic2DataType getProgramDiagnostic();

  /**
   * Sets the Value of the ProgramDiagnostic child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setProgramDiagnostic(@Nullable ProgramDiagnostic2DataType value);

  /**
   * Returns the mandatory RecycleCount child, a PropertyType with DataType Int32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getRecycleCountNode();

  /**
   * Returns the Value of the RecycleCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Integer getRecycleCount();

  /**
   * Sets the Value of the RecycleCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setRecycleCount(@Nullable Integer value);
}
