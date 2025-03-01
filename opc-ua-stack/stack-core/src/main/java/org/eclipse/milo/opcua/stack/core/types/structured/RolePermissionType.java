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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.12/#12.2.12.9">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.12/#12.2.12.9</a>
 */
public class RolePermissionType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=96");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=128");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=16126");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15062");

  private final NodeId roleId;

  private final PermissionType permissions;

  public RolePermissionType(NodeId roleId, PermissionType permissions) {
    this.roleId = roleId;
    this.permissions = permissions;
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

  public NodeId getRoleId() {
    return roleId;
  }

  public PermissionType getPermissions() {
    return permissions;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    RolePermissionType that = (RolePermissionType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRoleId(), that.getRoleId());
    eqb.append(getPermissions(), that.getPermissions());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRoleId());
    hcb.append(getPermissions());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", RolePermissionType.class.getSimpleName() + "[", "]");
    joiner.add("roleId=" + getRoleId());
    joiner.add("permissions=" + getPermissions());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 128),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "RoleId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Permissions",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 94),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<RolePermissionType> {
    @Override
    public Class<RolePermissionType> getType() {
      return RolePermissionType.class;
    }

    @Override
    public RolePermissionType decodeType(EncodingContext context, UaDecoder decoder) {
      final NodeId roleId;
      final PermissionType permissions;
      roleId = decoder.decodeNodeId("RoleId");
      permissions = new PermissionType(decoder.decodeUInt32("Permissions"));
      return new RolePermissionType(roleId, permissions);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, RolePermissionType value) {
      encoder.encodeNodeId("RoleId", value.getRoleId());
      encoder.encodeUInt32("Permissions", value.getPermissions().getValue());
    }
  }
}
