package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link MultiStateDictionaryEntryDiscreteBaseType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part19/7.1">Model
 *     documentation</a>
 */
public class MultiStateDictionaryEntryDiscreteBaseTypeNode extends MultiStateValueDiscreteTypeNode
    implements MultiStateDictionaryEntryDiscreteBaseType {
  public MultiStateDictionaryEntryDiscreteBaseTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      @Nullable LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType @Nullable [] rolePermissions,
      RolePermissionType @Nullable [] userRolePermissions,
      @Nullable AccessRestrictionType accessRestrictions,
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger @Nullable [] arrayDimensions) {
    super(
        context,
        nodeId,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions,
        value,
        dataType,
        valueRank,
        arrayDimensions);
  }

  public MultiStateDictionaryEntryDiscreteBaseTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      @Nullable LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType @Nullable [] rolePermissions,
      RolePermissionType @Nullable [] userRolePermissions,
      @Nullable AccessRestrictionType accessRestrictions,
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger @Nullable [] arrayDimensions,
      UByte accessLevel,
      UByte userAccessLevel,
      Double minimumSamplingInterval,
      boolean historizing,
      AccessLevelExType accessLevelEx) {
    super(
        context,
        nodeId,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions,
        value,
        dataType,
        valueRank,
        arrayDimensions,
        accessLevel,
        userAccessLevel,
        minimumSamplingInterval,
        historizing,
        accessLevelEx);
  }

  @Override
  public PropertyTypeNode getEnumDictionaryEntriesNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "EnumDictionaryEntries",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        2,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Variant getEnumDictionaryEntries() {
    return ServerNodeSupport.readVariant(
        this, getEnumDictionaryEntriesNode(), 2, NodeId.class, null);
  }

  @Override
  public void setEnumDictionaryEntries(@Nullable Variant value) {
    ServerNodeSupport.writeVariant(
        this,
        getEnumDictionaryEntriesNode(),
        null,
        "EnumDictionaryEntries",
        value,
        2,
        NodeId.class,
        null);
  }

  @Override
  public @Nullable PropertyTypeNode getValueAsDictionaryEntriesNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ValueAsDictionaryEntries",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public NodeId @Nullable [] getValueAsDictionaryEntries() {
    return ServerNodeSupport.readArray(this, getValueAsDictionaryEntriesNode(), NodeId.class, null);
  }

  @Override
  public void setValueAsDictionaryEntries(NodeId @Nullable [] value) {
    ServerNodeSupport.write(
        this,
        getValueAsDictionaryEntriesNode(),
        Namespaces.OPC_UA,
        "ValueAsDictionaryEntries",
        value,
        true,
        false,
        false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getEnumDictionaryEntriesNode();
    getValueAsDictionaryEntriesNode();
  }

  @Override
  public @Nullable Variant getTypedValue() {
    return ServerNodeSupport.read(this, this, Variant.class, null);
  }

  @Override
  public void setTypedValue(@Nullable Variant value) {
    ServerNodeSupport.write(this, this, value, false, false, false);
  }
}
