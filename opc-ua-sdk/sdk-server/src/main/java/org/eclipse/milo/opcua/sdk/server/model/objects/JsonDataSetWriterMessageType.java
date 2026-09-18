package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the JsonDataSetWriterMessageType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.2/#9.2.2.2">Model
 *     documentation</a>
 */
public interface JsonDataSetWriterMessageType extends DataSetWriterMessageType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 21128L);

  /**
   * Returns the mandatory DataSetMessageContentMask child, a PropertyType with DataType
   * JsonDataSetMessageContentMask.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getDataSetMessageContentMaskNode();

  /**
   * Returns the Value of the DataSetMessageContentMask child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable JsonDataSetMessageContentMask getDataSetMessageContentMask();

  /**
   * Sets the Value of the DataSetMessageContentMask child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDataSetMessageContentMask(@Nullable JsonDataSetMessageContentMask value);
}
