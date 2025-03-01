package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.StringJoiner;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.10/#6.2.10.3.4">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.10/#6.2.10.3.4</a>
 */
public class SubscribedDataSetMirrorDataType extends SubscribedDataSetDataType
    implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=15635");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15713");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=16012");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=16311");

  private final @Nullable String parentNodeName;

  private final RolePermissionType @Nullable [] rolePermissions;

  public SubscribedDataSetMirrorDataType(
      @Nullable String parentNodeName, RolePermissionType @Nullable [] rolePermissions) {
    this.parentNodeName = parentNodeName;
    this.rolePermissions = rolePermissions;
  }

  @Override
  public ExpandedNodeId getTypeId() {
    return TYPE_ID;
  }

  @Override
  public ExpandedNodeId getBinaryEncodingId() {
    return BINARY_ENCODING_ID;
  }

  @Override
  public ExpandedNodeId getXmlEncodingId() {
    return XML_ENCODING_ID;
  }

  @Override
  public ExpandedNodeId getJsonEncodingId() {
    return JSON_ENCODING_ID;
  }

  public @Nullable String getParentNodeName() {
    return parentNodeName;
  }

  public RolePermissionType @Nullable [] getRolePermissions() {
    return rolePermissions;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    SubscribedDataSetMirrorDataType that = (SubscribedDataSetMirrorDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getParentNodeName(), that.getParentNodeName());
    eqb.append(getRolePermissions(), that.getRolePermissions());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getParentNodeName());
    hcb.append(getRolePermissions());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", SubscribedDataSetMirrorDataType.class.getSimpleName() + "[", "]");
    joiner.add("parentNodeName='" + getParentNodeName() + "'");
    joiner.add("rolePermissions=" + java.util.Arrays.toString(getRolePermissions()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 15713),
        new NodeId(0, 15630),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ParentNodeName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RolePermissions",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 96),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<SubscribedDataSetMirrorDataType> {
    @Override
    public Class<SubscribedDataSetMirrorDataType> getType() {
      return SubscribedDataSetMirrorDataType.class;
    }

    @Override
    public SubscribedDataSetMirrorDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final String parentNodeName;
      final RolePermissionType[] rolePermissions;
      parentNodeName = decoder.decodeString("ParentNodeName");
      rolePermissions =
          (RolePermissionType[])
              decoder.decodeStructArray("RolePermissions", RolePermissionType.TYPE_ID);
      return new SubscribedDataSetMirrorDataType(parentNodeName, rolePermissions);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, SubscribedDataSetMirrorDataType value) {
      encoder.encodeString("ParentNodeName", value.getParentNodeName());
      encoder.encodeStructArray(
          "RolePermissions", value.getRolePermissions(), RolePermissionType.TYPE_ID);
    }
  }
}
