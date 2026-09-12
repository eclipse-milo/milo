/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodHandlerResult;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part25/6.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part25/6.3.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface SerializationEntityType extends BaseObjectType {
  QualifiedProperty<NodeId[]> INCLUDE_REFERENCE_TYPES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "IncludeReferenceTypes",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          1,
          NodeId[].class);

  QualifiedProperty<NodeId[]> EXCLUDE_REFERENCE_TYPES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ExcludeReferenceTypes",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          1,
          NodeId[].class);

  QualifiedProperty<UShort> SERIALIZATION_DEPTH =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SerializationDepth",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<Boolean> CONSIDER_SUB_ELEMENT_SERIALIZATION_PROPERTIES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConsiderSubElementSerializationProperties",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<KeyValuePair[]> CUSTOM_META_DATA_PROPERTIES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CustomMetaDataProperties",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14533"),
          1,
          KeyValuePair[].class);

  QualifiedProperty<NodeId> CUSTOM_META_DATA_REF =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CustomMetaDataRef",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<Boolean> INCLUDE_STATUS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "IncludeStatus",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> INCLUDE_SOURCE_TIMESTAMP =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "IncludeSourceTimestamp",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> INCLUDE_DICTIONARY_REFERENCE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "IncludeDictionaryReference",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  /** Gets the existing node's local value. */
  @Nullable NodeId @Nullable [] getIncludeReferenceTypes();

  /** Sets the existing node's local value. */
  void setIncludeReferenceTypes(@Nullable NodeId @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getIncludeReferenceTypesNode();

  /** Gets the existing node's local value. */
  @Nullable NodeId @Nullable [] getExcludeReferenceTypes();

  /** Sets the existing node's local value. */
  void setExcludeReferenceTypes(@Nullable NodeId @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getExcludeReferenceTypesNode();

  /** Gets the existing node's local value. */
  @Nullable UShort getSerializationDepth();

  /** Sets the existing node's local value. */
  void setSerializationDepth(@Nullable UShort value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSerializationDepthNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getConsiderSubElementSerializationProperties();

  /** Sets the existing node's local value. */
  void setConsiderSubElementSerializationProperties(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getConsiderSubElementSerializationPropertiesNode();

  /** Gets the existing node's local value. */
  @Nullable KeyValuePair @Nullable [] getCustomMetaDataProperties();

  /** Sets the existing node's local value. */
  void setCustomMetaDataProperties(@Nullable KeyValuePair @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getCustomMetaDataPropertiesNode();

  /** Gets the existing node's local value. */
  @Nullable NodeId getCustomMetaDataRef();

  /** Sets the existing node's local value. */
  void setCustomMetaDataRef(@Nullable NodeId value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getCustomMetaDataRefNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getIncludeStatus();

  /** Sets the existing node's local value. */
  void setIncludeStatus(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getIncludeStatusNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getIncludeSourceTimestamp();

  /** Sets the existing node's local value. */
  void setIncludeSourceTimestamp(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getIncludeSourceTimestampNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getIncludeDictionaryReference();

  /** Sets the existing node's local value. */
  void setIncludeDictionaryReference(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getIncludeDictionaryReferenceNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSerializedDataNode();

  /** Gets the existing node's local value. */
  @Nullable UaStructuredType getSerializedData();

  /** Sets the existing node's local value. */
  void setSerializedData(@Nullable UaStructuredType value);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part25/6.3.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getConfigureSerializationMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part25/6.3.3 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindConfigureSerialization(
      MethodBindings bindings, ConfigureSerializationHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part25/6.3.3 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindConfigureSerializationDetailed(
      MethodBindings bindings, ConfigureSerializationDetailedHandler handler) throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part25/6.3.3 */
  @FunctionalInterface
  interface ConfigureSerializationHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable Integer @Nullable [] invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable KeyValuePair @Nullable [] serializationFilterProperties)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part25/6.3.3 */
  @FunctionalInterface
  interface ConfigureSerializationDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Integer @Nullable []> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable KeyValuePair @Nullable [] serializationFilterProperties)
        throws UaException;
  }
}
