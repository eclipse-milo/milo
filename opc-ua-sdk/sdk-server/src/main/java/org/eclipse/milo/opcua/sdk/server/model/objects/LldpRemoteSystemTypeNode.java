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

import com.digitalpetri.opcua.uanodeset.runtime.members.MemberDeclaration;
import com.digitalpetri.opcua.uanodeset.runtime.server.ServerMembers;
import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ChassisIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PortIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpManagementAddressType;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpSystemCapabilitiesMap;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpTlvType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;

public class LldpRemoteSystemTypeNode extends BaseObjectTypeNode implements LldpRemoteSystemType {
  public LldpRemoteSystemTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType[] rolePermissions,
      RolePermissionType[] userRolePermissions,
      AccessRestrictionType accessRestrictions,
      UByte eventNotifier) {
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
        eventNotifier);
  }

  public LldpRemoteSystemTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType[] rolePermissions,
      RolePermissionType[] userRolePermissions,
      AccessRestrictionType accessRestrictions) {
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
        accessRestrictions);
  }

  @Override
  public Optional<VariableNode> getPropertyNode(QualifiedName browseName) {
    return findNode(
            browseName,
            n -> n instanceof VariableNode,
            r ->
                r.isForward()
                    && (r.getReferenceTypeId().equals(NodeIds.HasProperty)
                        || getNodeContext()
                            .getServer()
                            .getReferenceTypeTree()
                            .isSubtypeOf(r.getReferenceTypeId(), NodeIds.HasProperty)))
        .map(n -> (VariableNode) n);
  }

  @Override
  public BaseDataVariableTypeNode getTimeMarkNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "TimeMark",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:TimeMark (declaration i=19034, owner i=19033)"));
  }

  @Override
  public @Nullable UInteger getTimeMark() {
    var node = getTimeMarkNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:TimeMark (declaration i=19034, owner i=19033)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setTimeMark(@Nullable UInteger value) {
    var node = getTimeMarkNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:TimeMark (declaration i=19034, owner i=19033)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getRemoteIndexNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "RemoteIndex",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:RemoteIndex (declaration i=19035, owner i=19033)"));
  }

  @Override
  public @Nullable UInteger getRemoteIndex() {
    var node = getRemoteIndexNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RemoteIndex (declaration i=19035, owner i=19033)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setRemoteIndex(@Nullable UInteger value) {
    var node = getRemoteIndexNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RemoteIndex (declaration i=19035, owner i=19033)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getChassisIdSubtypeNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ChassisIdSubtype",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ChassisIdSubtype (declaration i=19036, owner i=19033)"));
  }

  @Override
  public @Nullable ChassisIdSubtype getChassisIdSubtype() {
    var node = getChassisIdSubtypeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ChassisIdSubtype (declaration i=19036, owner i=19033)"
              + " on "
              + getNodeId());
    }
    Object value = node.getValue().getValue().getValue();
    Object convertedValue;
    {
      if (value == null || value instanceof Matrix && ((Matrix) value).isNull()) {
        convertedValue = null;
      } else {
        Object elements = value instanceof Matrix ? ((Matrix) value).getElements() : value;
        int rank =
            value instanceof Matrix
                ? ((Matrix) value).getValueRank()
                : ArrayUtil.getValueRank(value);
        boolean permitted = rank == -1;
        if (!permitted) {
          throw new UaRuntimeException(
              StatusCodes.Bad_TypeMismatch,
              "ChassisIdSubtype: ValueRank=-1 does not permit rank " + rank);
        }
        if (value != null && !((Object) value instanceof ChassisIdSubtype)) {
          if (!(value instanceof Integer)) {
            throw new UaRuntimeException(
                StatusCodes.Bad_TypeMismatch,
                "ChassisIdSubtype: expected"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.ChassisIdSubtype or"
                    + " Int32, got "
                    + value);
          }
          if (ChassisIdSubtype.from((Integer) value) == null) {
            throw new UaRuntimeException(
                StatusCodes.Bad_OutOfRange,
                "ChassisIdSubtype: unknown"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.ChassisIdSubtype value "
                    + value);
          }
        }
        convertedValue =
            value == null || value instanceof ChassisIdSubtype
                ? (ChassisIdSubtype) value
                : ChassisIdSubtype.from((Integer) value);
      }
    }
    return (ChassisIdSubtype) convertedValue;
  }

  @Override
  public void setChassisIdSubtype(@Nullable ChassisIdSubtype value) {
    var node = getChassisIdSubtypeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ChassisIdSubtype (declaration i=19036, owner i=19033)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getChassisIdNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ChassisId",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ChassisId (declaration i=19037, owner i=19033)"));
  }

  @Override
  public @Nullable String getChassisId() {
    var node = getChassisIdNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ChassisId (declaration i=19037, owner i=19033)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setChassisId(@Nullable String value) {
    var node = getChassisIdNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ChassisId (declaration i=19037, owner i=19033)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getPortIdSubtypeNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "PortIdSubtype",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:PortIdSubtype (declaration i=19038, owner i=19033)"));
  }

  @Override
  public @Nullable PortIdSubtype getPortIdSubtype() {
    var node = getPortIdSubtypeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PortIdSubtype (declaration i=19038, owner i=19033)"
              + " on "
              + getNodeId());
    }
    Object value = node.getValue().getValue().getValue();
    Object convertedValue;
    {
      if (value == null || value instanceof Matrix && ((Matrix) value).isNull()) {
        convertedValue = null;
      } else {
        Object elements = value instanceof Matrix ? ((Matrix) value).getElements() : value;
        int rank =
            value instanceof Matrix
                ? ((Matrix) value).getValueRank()
                : ArrayUtil.getValueRank(value);
        boolean permitted = rank == -1;
        if (!permitted) {
          throw new UaRuntimeException(
              StatusCodes.Bad_TypeMismatch,
              "PortIdSubtype: ValueRank=-1 does not permit rank " + rank);
        }
        if (value != null && !((Object) value instanceof PortIdSubtype)) {
          if (!(value instanceof Integer)) {
            throw new UaRuntimeException(
                StatusCodes.Bad_TypeMismatch,
                "PortIdSubtype: expected"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.PortIdSubtype or Int32,"
                    + " got "
                    + value);
          }
          if (PortIdSubtype.from((Integer) value) == null) {
            throw new UaRuntimeException(
                StatusCodes.Bad_OutOfRange,
                "PortIdSubtype: unknown"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.PortIdSubtype value "
                    + value);
          }
        }
        convertedValue =
            value == null || value instanceof PortIdSubtype
                ? (PortIdSubtype) value
                : PortIdSubtype.from((Integer) value);
      }
    }
    return (PortIdSubtype) convertedValue;
  }

  @Override
  public void setPortIdSubtype(@Nullable PortIdSubtype value) {
    var node = getPortIdSubtypeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PortIdSubtype (declaration i=19038, owner i=19033)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getPortIdNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "PortId",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:PortId (declaration i=19039, owner i=19033)"));
  }

  @Override
  public @Nullable String getPortId() {
    var node = getPortIdNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PortId (declaration i=19039, owner i=19033)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setPortId(@Nullable String value) {
    var node = getPortIdNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PortId (declaration i=19039, owner i=19033)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getPortDescriptionNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "PortDescription",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:PortDescription (declaration i=19040, owner i=19033)"));
  }

  @Override
  public @Nullable String getPortDescription() {
    var node = getPortDescriptionNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PortDescription (declaration i=19040, owner i=19033)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setPortDescription(@Nullable String value) {
    var node = getPortDescriptionNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PortDescription (declaration i=19040, owner i=19033)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getSystemNameNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SystemName",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:SystemName (declaration i=19041, owner i=19033)"));
  }

  @Override
  public @Nullable String getSystemName() {
    var node = getSystemNameNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SystemName (declaration i=19041, owner i=19033)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setSystemName(@Nullable String value) {
    var node = getSystemNameNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SystemName (declaration i=19041, owner i=19033)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getSystemDescriptionNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SystemDescription",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:SystemDescription (declaration i=19042, owner i=19033)"));
  }

  @Override
  public @Nullable String getSystemDescription() {
    var node = getSystemDescriptionNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SystemDescription (declaration i=19042, owner i=19033)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setSystemDescription(@Nullable String value) {
    var node = getSystemDescriptionNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SystemDescription (declaration i=19042, owner i=19033)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getSystemCapabilitiesSupportedNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SystemCapabilitiesSupported",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:SystemCapabilitiesSupported (declaration i=19043, owner"
                + " i=19033)"));
  }

  @Override
  public @Nullable LldpSystemCapabilitiesMap getSystemCapabilitiesSupported() {
    var node = getSystemCapabilitiesSupportedNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SystemCapabilitiesSupported (declaration i=19043, owner"
              + " i=19033) on "
              + getNodeId());
    }
    return (LldpSystemCapabilitiesMap) node.getValue().getValue().getValue();
  }

  @Override
  public void setSystemCapabilitiesSupported(@Nullable LldpSystemCapabilitiesMap value) {
    var node = getSystemCapabilitiesSupportedNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SystemCapabilitiesSupported (declaration i=19043, owner"
              + " i=19033) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getSystemCapabilitiesEnabledNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SystemCapabilitiesEnabled",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:SystemCapabilitiesEnabled (declaration i=19044, owner"
                + " i=19033)"));
  }

  @Override
  public @Nullable LldpSystemCapabilitiesMap getSystemCapabilitiesEnabled() {
    var node = getSystemCapabilitiesEnabledNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SystemCapabilitiesEnabled (declaration i=19044, owner"
              + " i=19033) on "
              + getNodeId());
    }
    return (LldpSystemCapabilitiesMap) node.getValue().getValue().getValue();
  }

  @Override
  public void setSystemCapabilitiesEnabled(@Nullable LldpSystemCapabilitiesMap value) {
    var node = getSystemCapabilitiesEnabledNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SystemCapabilitiesEnabled (declaration i=19044, owner"
              + " i=19033) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getRemoteChangesNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "RemoteChanges",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:RemoteChanges (declaration i=19045, owner i=19033)"));
  }

  @Override
  public @Nullable Boolean getRemoteChanges() {
    var node = getRemoteChangesNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RemoteChanges (declaration i=19045, owner i=19033)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setRemoteChanges(@Nullable Boolean value) {
    var node = getRemoteChangesNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RemoteChanges (declaration i=19045, owner i=19033)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getRemoteTooManyNeighborsNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "RemoteTooManyNeighbors",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:RemoteTooManyNeighbors (declaration i=19046, owner"
                + " i=19033)"));
  }

  @Override
  public @Nullable Boolean getRemoteTooManyNeighbors() {
    var node = getRemoteTooManyNeighborsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RemoteTooManyNeighbors (declaration i=19046, owner i=19033)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setRemoteTooManyNeighbors(@Nullable Boolean value) {
    var node = getRemoteTooManyNeighborsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RemoteTooManyNeighbors (declaration i=19046, owner i=19033)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getManagementAddressNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ManagementAddress",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:ManagementAddress (declaration i=19047, owner i=19033)"));
  }

  @Override
  public @Nullable LldpManagementAddressType @Nullable [] getManagementAddress() {
    var node = getManagementAddressNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ManagementAddress (declaration i=19047, owner i=19033)"
              + " on "
              + getNodeId());
    }
    return (LldpManagementAddressType[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setManagementAddress(@Nullable LldpManagementAddressType @Nullable [] value) {
    var node = getManagementAddressNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ManagementAddress (declaration i=19047, owner i=19033)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getRemoteUnknownTlvNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "RemoteUnknownTlv",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:RemoteUnknownTlv (declaration i=19078, owner i=19033)"));
  }

  @Override
  public @Nullable LldpTlvType @Nullable [] getRemoteUnknownTlv() {
    var node = getRemoteUnknownTlvNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RemoteUnknownTlv (declaration i=19078, owner i=19033)"
              + " on "
              + getNodeId());
    }
    return (LldpTlvType[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setRemoteUnknownTlv(@Nullable LldpTlvType @Nullable [] value) {
    var node = getRemoteUnknownTlvNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RemoteUnknownTlv (declaration i=19078, owner i=19033)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }
}
