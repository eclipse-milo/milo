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
import com.digitalpetri.opcua.uanodeset.runtime.server.ServerPropertyValues;
import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.SignedSoftwareCertificate;
import org.jspecify.annotations.Nullable;

public class ServerCapabilitiesTypeNode extends BaseObjectTypeNode
    implements ServerCapabilitiesType {
  public ServerCapabilitiesTypeNode(
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

  public ServerCapabilitiesTypeNode(
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
  public PropertyTypeNode getServerProfileArrayNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ServerProfileArray",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ServerProfileArray (declaration i=2014, owner i=2013)"));
  }

  @Override
  public @Nullable String @Nullable [] getServerProfileArray() {
    var node = getServerProfileArrayNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ServerProfileArray (declaration i=2014, owner i=2013)"
              + " on "
              + getNodeId());
    }
    return (String[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setServerProfileArray(@Nullable String @Nullable [] value) {
    var node = getServerProfileArrayNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ServerProfileArray (declaration i=2014, owner i=2013)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getLocaleIdArrayNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "LocaleIdArray",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:LocaleIdArray (declaration i=2016, owner i=2013)"));
  }

  @Override
  public @Nullable String @Nullable [] getLocaleIdArray() {
    var node = getLocaleIdArrayNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LocaleIdArray (declaration i=2016, owner i=2013)"
              + " on "
              + getNodeId());
    }
    return (String[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setLocaleIdArray(@Nullable String @Nullable [] value) {
    var node = getLocaleIdArrayNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LocaleIdArray (declaration i=2016, owner i=2013)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getMinSupportedSampleRateNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MinSupportedSampleRate",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MinSupportedSampleRate (declaration i=2017, owner"
                + " i=2013)"));
  }

  @Override
  public @Nullable Double getMinSupportedSampleRate() {
    var node = getMinSupportedSampleRateNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MinSupportedSampleRate (declaration i=2017, owner i=2013)"
              + " on "
              + getNodeId());
    }
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setMinSupportedSampleRate(@Nullable Double value) {
    var node = getMinSupportedSampleRateNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MinSupportedSampleRate (declaration i=2017, owner i=2013)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getMaxBrowseContinuationPointsNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxBrowseContinuationPoints",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MaxBrowseContinuationPoints (declaration i=2732, owner"
                + " i=2013)"));
  }

  @Override
  public @Nullable UShort getMaxBrowseContinuationPoints() {
    var node = getMaxBrowseContinuationPointsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxBrowseContinuationPoints (declaration i=2732, owner"
              + " i=2013) on "
              + getNodeId());
    }
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxBrowseContinuationPoints(@Nullable UShort value) {
    var node = getMaxBrowseContinuationPointsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxBrowseContinuationPoints (declaration i=2732, owner"
              + " i=2013) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getMaxQueryContinuationPointsNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxQueryContinuationPoints",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MaxQueryContinuationPoints (declaration i=2733, owner"
                + " i=2013)"));
  }

  @Override
  public @Nullable UShort getMaxQueryContinuationPoints() {
    var node = getMaxQueryContinuationPointsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxQueryContinuationPoints (declaration i=2733, owner"
              + " i=2013) on "
              + getNodeId());
    }
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxQueryContinuationPoints(@Nullable UShort value) {
    var node = getMaxQueryContinuationPointsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxQueryContinuationPoints (declaration i=2733, owner"
              + " i=2013) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getMaxHistoryContinuationPointsNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxHistoryContinuationPoints",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MaxHistoryContinuationPoints (declaration i=2734, owner"
                + " i=2013)"));
  }

  @Override
  public @Nullable UShort getMaxHistoryContinuationPoints() {
    var node = getMaxHistoryContinuationPointsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxHistoryContinuationPoints (declaration i=2734, owner"
              + " i=2013) on "
              + getNodeId());
    }
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxHistoryContinuationPoints(@Nullable UShort value) {
    var node = getMaxHistoryContinuationPointsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxHistoryContinuationPoints (declaration i=2734, owner"
              + " i=2013) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxLogObjectContinuationPointsNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxLogObjectContinuationPoints",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxLogObjectContinuationPoints (declaration i=19809,"
                + " owner i=2013)"));
  }

  @Override
  public @Nullable UShort getMaxLogObjectContinuationPoints() {
    var node = getMaxLogObjectContinuationPointsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxLogObjectContinuationPoints (declaration i=19809, owner"
              + " i=2013) on "
              + getNodeId());
    }
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxLogObjectContinuationPoints(@Nullable UShort value) {
    var node = getMaxLogObjectContinuationPointsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxLogObjectContinuationPoints (declaration i=19809, owner"
              + " i=2013) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getSoftwareCertificatesNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SoftwareCertificates",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:SoftwareCertificates (declaration i=3049, owner"
                + " i=2013)"));
  }

  @Override
  public @Nullable SignedSoftwareCertificate @Nullable [] getSoftwareCertificates() {
    var node = getSoftwareCertificatesNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SoftwareCertificates (declaration i=3049, owner i=2013)"
              + " on "
              + getNodeId());
    }
    return ServerPropertyValues.decode(
        getNodeContext().getServer().getStaticEncodingContext(),
        node.getValue().getValue().getValue(),
        SignedSoftwareCertificate[].class,
        SignedSoftwareCertificate.class,
        "http://opcfoundation.org/UA/:SoftwareCertificates (declaration i=3049, owner i=2013)");
  }

  @Override
  public void setSoftwareCertificates(@Nullable SignedSoftwareCertificate @Nullable [] value) {
    var node = getSoftwareCertificatesNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SoftwareCertificates (declaration i=3049, owner i=2013)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxArrayLengthNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxArrayLength",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxArrayLength (declaration i=11549, owner i=2013)"));
  }

  @Override
  public @Nullable UInteger getMaxArrayLength() {
    var node = getMaxArrayLengthNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxArrayLength (declaration i=11549, owner i=2013)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxArrayLength(@Nullable UInteger value) {
    var node = getMaxArrayLengthNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxArrayLength (declaration i=11549, owner i=2013)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxStringLengthNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxStringLength",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxStringLength (declaration i=11550, owner i=2013)"));
  }

  @Override
  public @Nullable UInteger getMaxStringLength() {
    var node = getMaxStringLengthNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxStringLength (declaration i=11550, owner i=2013)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxStringLength(@Nullable UInteger value) {
    var node = getMaxStringLengthNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxStringLength (declaration i=11550, owner i=2013)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxByteStringLengthNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxByteStringLength",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxByteStringLength (declaration i=12910, owner"
                + " i=2013)"));
  }

  @Override
  public @Nullable UInteger getMaxByteStringLength() {
    var node = getMaxByteStringLengthNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxByteStringLength (declaration i=12910, owner i=2013)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxByteStringLength(@Nullable UInteger value) {
    var node = getMaxByteStringLengthNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxByteStringLength (declaration i=12910, owner i=2013)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxSessionsNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxSessions",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxSessions (declaration i=24088, owner i=2013)"));
  }

  @Override
  public @Nullable UInteger getMaxSessions() {
    var node = getMaxSessionsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxSessions (declaration i=24088, owner i=2013)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxSessions(@Nullable UInteger value) {
    var node = getMaxSessionsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxSessions (declaration i=24088, owner i=2013)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxSubscriptionsNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxSubscriptions",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxSubscriptions (declaration i=24089, owner i=2013)"));
  }

  @Override
  public @Nullable UInteger getMaxSubscriptions() {
    var node = getMaxSubscriptionsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxSubscriptions (declaration i=24089, owner i=2013)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxSubscriptions(@Nullable UInteger value) {
    var node = getMaxSubscriptionsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxSubscriptions (declaration i=24089, owner i=2013)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxMonitoredItemsNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxMonitoredItems",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxMonitoredItems (declaration i=24090, owner i=2013)"));
  }

  @Override
  public @Nullable UInteger getMaxMonitoredItems() {
    var node = getMaxMonitoredItemsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxMonitoredItems (declaration i=24090, owner i=2013)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxMonitoredItems(@Nullable UInteger value) {
    var node = getMaxMonitoredItemsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxMonitoredItems (declaration i=24090, owner i=2013)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxSubscriptionsPerSessionNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxSubscriptionsPerSession",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxSubscriptionsPerSession (declaration i=24091, owner"
                + " i=2013)"));
  }

  @Override
  public @Nullable UInteger getMaxSubscriptionsPerSession() {
    var node = getMaxSubscriptionsPerSessionNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxSubscriptionsPerSession (declaration i=24091, owner"
              + " i=2013) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxSubscriptionsPerSession(@Nullable UInteger value) {
    var node = getMaxSubscriptionsPerSessionNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxSubscriptionsPerSession (declaration i=24091, owner"
              + " i=2013) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxMonitoredItemsPerSubscriptionNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxMonitoredItemsPerSubscription",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxMonitoredItemsPerSubscription (declaration i=24103,"
                + " owner i=2013)"));
  }

  @Override
  public @Nullable UInteger getMaxMonitoredItemsPerSubscription() {
    var node = getMaxMonitoredItemsPerSubscriptionNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxMonitoredItemsPerSubscription (declaration i=24103,"
              + " owner i=2013) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxMonitoredItemsPerSubscription(@Nullable UInteger value) {
    var node = getMaxMonitoredItemsPerSubscriptionNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxMonitoredItemsPerSubscription (declaration i=24103,"
              + " owner i=2013) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxSelectClauseParametersNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxSelectClauseParameters",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxSelectClauseParameters (declaration i=24092, owner"
                + " i=2013)"));
  }

  @Override
  public @Nullable UInteger getMaxSelectClauseParameters() {
    var node = getMaxSelectClauseParametersNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxSelectClauseParameters (declaration i=24092, owner"
              + " i=2013) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxSelectClauseParameters(@Nullable UInteger value) {
    var node = getMaxSelectClauseParametersNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxSelectClauseParameters (declaration i=24092, owner"
              + " i=2013) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxWhereClauseParametersNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxWhereClauseParameters",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxWhereClauseParameters (declaration i=24093, owner"
                + " i=2013)"));
  }

  @Override
  public @Nullable UInteger getMaxWhereClauseParameters() {
    var node = getMaxWhereClauseParametersNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxWhereClauseParameters (declaration i=24093, owner"
              + " i=2013) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxWhereClauseParameters(@Nullable UInteger value) {
    var node = getMaxWhereClauseParametersNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxWhereClauseParameters (declaration i=24093, owner"
              + " i=2013) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxMonitoredItemsQueueSizeNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxMonitoredItemsQueueSize",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxMonitoredItemsQueueSize (declaration i=31770, owner"
                + " i=2013)"));
  }

  @Override
  public @Nullable UInteger getMaxMonitoredItemsQueueSize() {
    var node = getMaxMonitoredItemsQueueSizeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxMonitoredItemsQueueSize (declaration i=31770, owner"
              + " i=2013) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxMonitoredItemsQueueSize(@Nullable UInteger value) {
    var node = getMaxMonitoredItemsQueueSizeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxMonitoredItemsQueueSize (declaration i=31770, owner"
              + " i=2013) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getConformanceUnitsNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ConformanceUnits",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:ConformanceUnits (declaration i=24094, owner i=2013)"));
  }

  @Override
  public @Nullable QualifiedName @Nullable [] getConformanceUnits() {
    var node = getConformanceUnitsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ConformanceUnits (declaration i=24094, owner i=2013)"
              + " on "
              + getNodeId());
    }
    return (QualifiedName[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setConformanceUnits(@Nullable QualifiedName @Nullable [] value) {
    var node = getConformanceUnitsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ConformanceUnits (declaration i=24094, owner i=2013)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable OperationLimitsTypeNode getOperationLimitsNode() {
    return ServerMembers.lookup(
        this,
        OperationLimitsTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "OperationLimits",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            true,
            "http://opcfoundation.org/UA/:OperationLimits (declaration i=11551, owner i=2013)"));
  }

  @Override
  public FolderTypeNode getModellingRulesNode() {
    return ServerMembers.lookup(
        this,
        FolderTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ModellingRules",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:ModellingRules (declaration i=2019, owner i=2013)"));
  }

  @Override
  public FolderTypeNode getAggregateFunctionsNode() {
    return ServerMembers.lookup(
        this,
        FolderTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "AggregateFunctions",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:AggregateFunctions (declaration i=2754, owner i=2013)"));
  }

  @Override
  public @Nullable RoleSetTypeNode getRoleSetNode() {
    return ServerMembers.lookup(
        this,
        RoleSetTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "RoleSet",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            true,
            "http://opcfoundation.org/UA/:RoleSet (declaration i=16295, owner i=2013)"));
  }
}
