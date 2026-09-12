/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.variables;

import com.digitalpetri.opcua.uanodeset.runtime.members.MemberDeclaration;
import com.digitalpetri.opcua.uanodeset.runtime.server.ServerMembers;
import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceCounterDataType;
import org.jspecify.annotations.Nullable;

public class SessionDiagnosticsVariableTypeNode extends BaseDataVariableTypeNode
    implements SessionDiagnosticsVariableType {
  public SessionDiagnosticsVariableTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger[] arrayDimensions,
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

  public SessionDiagnosticsVariableTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger[] arrayDimensions) {
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
  public BaseDataVariableTypeNode getSessionIdNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SessionId",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:SessionId (declaration i=2198, owner i=2197)"));
  }

  @Override
  public @Nullable NodeId getSessionId() {
    var node = getSessionIdNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SessionId (declaration i=2198, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (NodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setSessionId(@Nullable NodeId value) {
    var node = getSessionIdNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SessionId (declaration i=2198, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getSessionNameNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SessionName",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:SessionName (declaration i=2199, owner i=2197)"));
  }

  @Override
  public @Nullable String getSessionName() {
    var node = getSessionNameNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SessionName (declaration i=2199, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setSessionName(@Nullable String value) {
    var node = getSessionNameNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SessionName (declaration i=2199, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getClientDescriptionNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ClientDescription",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ClientDescription (declaration i=2200, owner i=2197)"));
  }

  @Override
  public @Nullable ApplicationDescription getClientDescription() {
    var node = getClientDescriptionNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ClientDescription (declaration i=2200, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ApplicationDescription) node.getValue().getValue().getValue();
  }

  @Override
  public void setClientDescription(@Nullable ApplicationDescription value) {
    var node = getClientDescriptionNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ClientDescription (declaration i=2200, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getServerUriNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ServerUri",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ServerUri (declaration i=2201, owner i=2197)"));
  }

  @Override
  public @Nullable String getServerUri() {
    var node = getServerUriNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ServerUri (declaration i=2201, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setServerUri(@Nullable String value) {
    var node = getServerUriNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ServerUri (declaration i=2201, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getEndpointUrlNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "EndpointUrl",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:EndpointUrl (declaration i=2202, owner i=2197)"));
  }

  @Override
  public @Nullable String getEndpointUrl() {
    var node = getEndpointUrlNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EndpointUrl (declaration i=2202, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setEndpointUrl(@Nullable String value) {
    var node = getEndpointUrlNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EndpointUrl (declaration i=2202, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getLocaleIdsNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "LocaleIds",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:LocaleIds (declaration i=2203, owner i=2197)"));
  }

  @Override
  public @Nullable String @Nullable [] getLocaleIds() {
    var node = getLocaleIdsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LocaleIds (declaration i=2203, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (String[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setLocaleIds(@Nullable String @Nullable [] value) {
    var node = getLocaleIdsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LocaleIds (declaration i=2203, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getActualSessionTimeoutNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ActualSessionTimeout",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ActualSessionTimeout (declaration i=2204, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable Double getActualSessionTimeout() {
    var node = getActualSessionTimeoutNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ActualSessionTimeout (declaration i=2204, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setActualSessionTimeout(@Nullable Double value) {
    var node = getActualSessionTimeoutNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ActualSessionTimeout (declaration i=2204, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getMaxResponseMessageSizeNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxResponseMessageSize",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MaxResponseMessageSize (declaration i=3050, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable UInteger getMaxResponseMessageSize() {
    var node = getMaxResponseMessageSizeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxResponseMessageSize (declaration i=3050, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxResponseMessageSize(@Nullable UInteger value) {
    var node = getMaxResponseMessageSizeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxResponseMessageSize (declaration i=3050, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getClientConnectionTimeNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ClientConnectionTime",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ClientConnectionTime (declaration i=2205, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable DateTime getClientConnectionTime() {
    var node = getClientConnectionTimeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ClientConnectionTime (declaration i=2205, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setClientConnectionTime(@Nullable DateTime value) {
    var node = getClientConnectionTimeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ClientConnectionTime (declaration i=2205, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getClientLastContactTimeNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ClientLastContactTime",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ClientLastContactTime (declaration i=2206, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable DateTime getClientLastContactTime() {
    var node = getClientLastContactTimeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ClientLastContactTime (declaration i=2206, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setClientLastContactTime(@Nullable DateTime value) {
    var node = getClientLastContactTimeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ClientLastContactTime (declaration i=2206, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getCurrentSubscriptionsCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "CurrentSubscriptionsCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:CurrentSubscriptionsCount (declaration i=2207, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable UInteger getCurrentSubscriptionsCount() {
    var node = getCurrentSubscriptionsCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CurrentSubscriptionsCount (declaration i=2207, owner"
              + " i=2197) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setCurrentSubscriptionsCount(@Nullable UInteger value) {
    var node = getCurrentSubscriptionsCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CurrentSubscriptionsCount (declaration i=2207, owner"
              + " i=2197) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getCurrentMonitoredItemsCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "CurrentMonitoredItemsCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:CurrentMonitoredItemsCount (declaration i=2208, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable UInteger getCurrentMonitoredItemsCount() {
    var node = getCurrentMonitoredItemsCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CurrentMonitoredItemsCount (declaration i=2208, owner"
              + " i=2197) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setCurrentMonitoredItemsCount(@Nullable UInteger value) {
    var node = getCurrentMonitoredItemsCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CurrentMonitoredItemsCount (declaration i=2208, owner"
              + " i=2197) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getCurrentPublishRequestsInQueueNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "CurrentPublishRequestsInQueue",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:CurrentPublishRequestsInQueue (declaration i=2209, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable UInteger getCurrentPublishRequestsInQueue() {
    var node = getCurrentPublishRequestsInQueueNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CurrentPublishRequestsInQueue (declaration i=2209, owner"
              + " i=2197) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setCurrentPublishRequestsInQueue(@Nullable UInteger value) {
    var node = getCurrentPublishRequestsInQueueNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CurrentPublishRequestsInQueue (declaration i=2209, owner"
              + " i=2197) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getTotalRequestCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "TotalRequestCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:TotalRequestCount (declaration i=8900, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getTotalRequestCount() {
    var node = getTotalRequestCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:TotalRequestCount (declaration i=8900, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setTotalRequestCount(@Nullable ServiceCounterDataType value) {
    var node = getTotalRequestCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:TotalRequestCount (declaration i=8900, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getUnauthorizedRequestCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "UnauthorizedRequestCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:UnauthorizedRequestCount (declaration i=11892, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable UInteger getUnauthorizedRequestCount() {
    var node = getUnauthorizedRequestCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UnauthorizedRequestCount (declaration i=11892, owner"
              + " i=2197) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setUnauthorizedRequestCount(@Nullable UInteger value) {
    var node = getUnauthorizedRequestCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UnauthorizedRequestCount (declaration i=11892, owner"
              + " i=2197) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getReadCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ReadCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ReadCount (declaration i=2217, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getReadCount() {
    var node = getReadCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ReadCount (declaration i=2217, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setReadCount(@Nullable ServiceCounterDataType value) {
    var node = getReadCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ReadCount (declaration i=2217, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getHistoryReadCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "HistoryReadCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:HistoryReadCount (declaration i=2218, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getHistoryReadCount() {
    var node = getHistoryReadCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:HistoryReadCount (declaration i=2218, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setHistoryReadCount(@Nullable ServiceCounterDataType value) {
    var node = getHistoryReadCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:HistoryReadCount (declaration i=2218, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getWriteCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "WriteCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:WriteCount (declaration i=2219, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getWriteCount() {
    var node = getWriteCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:WriteCount (declaration i=2219, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setWriteCount(@Nullable ServiceCounterDataType value) {
    var node = getWriteCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:WriteCount (declaration i=2219, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getHistoryUpdateCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "HistoryUpdateCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:HistoryUpdateCount (declaration i=2220, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getHistoryUpdateCount() {
    var node = getHistoryUpdateCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:HistoryUpdateCount (declaration i=2220, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setHistoryUpdateCount(@Nullable ServiceCounterDataType value) {
    var node = getHistoryUpdateCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:HistoryUpdateCount (declaration i=2220, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getCallCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "CallCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:CallCount (declaration i=2221, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getCallCount() {
    var node = getCallCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CallCount (declaration i=2221, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setCallCount(@Nullable ServiceCounterDataType value) {
    var node = getCallCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CallCount (declaration i=2221, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getCreateMonitoredItemsCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "CreateMonitoredItemsCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:CreateMonitoredItemsCount (declaration i=2222, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getCreateMonitoredItemsCount() {
    var node = getCreateMonitoredItemsCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CreateMonitoredItemsCount (declaration i=2222, owner"
              + " i=2197) on "
              + getNodeId());
    }
    return (ServiceCounterDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setCreateMonitoredItemsCount(@Nullable ServiceCounterDataType value) {
    var node = getCreateMonitoredItemsCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CreateMonitoredItemsCount (declaration i=2222, owner"
              + " i=2197) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getModifyMonitoredItemsCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ModifyMonitoredItemsCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ModifyMonitoredItemsCount (declaration i=2223, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getModifyMonitoredItemsCount() {
    var node = getModifyMonitoredItemsCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ModifyMonitoredItemsCount (declaration i=2223, owner"
              + " i=2197) on "
              + getNodeId());
    }
    return (ServiceCounterDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setModifyMonitoredItemsCount(@Nullable ServiceCounterDataType value) {
    var node = getModifyMonitoredItemsCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ModifyMonitoredItemsCount (declaration i=2223, owner"
              + " i=2197) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getSetMonitoringModeCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SetMonitoringModeCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:SetMonitoringModeCount (declaration i=2224, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getSetMonitoringModeCount() {
    var node = getSetMonitoringModeCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SetMonitoringModeCount (declaration i=2224, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setSetMonitoringModeCount(@Nullable ServiceCounterDataType value) {
    var node = getSetMonitoringModeCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SetMonitoringModeCount (declaration i=2224, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getSetTriggeringCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SetTriggeringCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:SetTriggeringCount (declaration i=2225, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getSetTriggeringCount() {
    var node = getSetTriggeringCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SetTriggeringCount (declaration i=2225, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setSetTriggeringCount(@Nullable ServiceCounterDataType value) {
    var node = getSetTriggeringCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SetTriggeringCount (declaration i=2225, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getDeleteMonitoredItemsCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DeleteMonitoredItemsCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:DeleteMonitoredItemsCount (declaration i=2226, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getDeleteMonitoredItemsCount() {
    var node = getDeleteMonitoredItemsCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DeleteMonitoredItemsCount (declaration i=2226, owner"
              + " i=2197) on "
              + getNodeId());
    }
    return (ServiceCounterDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setDeleteMonitoredItemsCount(@Nullable ServiceCounterDataType value) {
    var node = getDeleteMonitoredItemsCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DeleteMonitoredItemsCount (declaration i=2226, owner"
              + " i=2197) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getCreateSubscriptionCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "CreateSubscriptionCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:CreateSubscriptionCount (declaration i=2227, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getCreateSubscriptionCount() {
    var node = getCreateSubscriptionCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CreateSubscriptionCount (declaration i=2227, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setCreateSubscriptionCount(@Nullable ServiceCounterDataType value) {
    var node = getCreateSubscriptionCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CreateSubscriptionCount (declaration i=2227, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getModifySubscriptionCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ModifySubscriptionCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ModifySubscriptionCount (declaration i=2228, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getModifySubscriptionCount() {
    var node = getModifySubscriptionCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ModifySubscriptionCount (declaration i=2228, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setModifySubscriptionCount(@Nullable ServiceCounterDataType value) {
    var node = getModifySubscriptionCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ModifySubscriptionCount (declaration i=2228, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getSetPublishingModeCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SetPublishingModeCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:SetPublishingModeCount (declaration i=2229, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getSetPublishingModeCount() {
    var node = getSetPublishingModeCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SetPublishingModeCount (declaration i=2229, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setSetPublishingModeCount(@Nullable ServiceCounterDataType value) {
    var node = getSetPublishingModeCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SetPublishingModeCount (declaration i=2229, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getPublishCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "PublishCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:PublishCount (declaration i=2230, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getPublishCount() {
    var node = getPublishCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PublishCount (declaration i=2230, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setPublishCount(@Nullable ServiceCounterDataType value) {
    var node = getPublishCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PublishCount (declaration i=2230, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getRepublishCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "RepublishCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:RepublishCount (declaration i=2231, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getRepublishCount() {
    var node = getRepublishCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RepublishCount (declaration i=2231, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setRepublishCount(@Nullable ServiceCounterDataType value) {
    var node = getRepublishCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RepublishCount (declaration i=2231, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getTransferSubscriptionsCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "TransferSubscriptionsCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:TransferSubscriptionsCount (declaration i=2232, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getTransferSubscriptionsCount() {
    var node = getTransferSubscriptionsCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:TransferSubscriptionsCount (declaration i=2232, owner"
              + " i=2197) on "
              + getNodeId());
    }
    return (ServiceCounterDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setTransferSubscriptionsCount(@Nullable ServiceCounterDataType value) {
    var node = getTransferSubscriptionsCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:TransferSubscriptionsCount (declaration i=2232, owner"
              + " i=2197) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getDeleteSubscriptionsCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DeleteSubscriptionsCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:DeleteSubscriptionsCount (declaration i=2233, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getDeleteSubscriptionsCount() {
    var node = getDeleteSubscriptionsCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DeleteSubscriptionsCount (declaration i=2233, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setDeleteSubscriptionsCount(@Nullable ServiceCounterDataType value) {
    var node = getDeleteSubscriptionsCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DeleteSubscriptionsCount (declaration i=2233, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getAddNodesCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "AddNodesCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:AddNodesCount (declaration i=2234, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getAddNodesCount() {
    var node = getAddNodesCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AddNodesCount (declaration i=2234, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setAddNodesCount(@Nullable ServiceCounterDataType value) {
    var node = getAddNodesCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AddNodesCount (declaration i=2234, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getAddReferencesCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "AddReferencesCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:AddReferencesCount (declaration i=2235, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getAddReferencesCount() {
    var node = getAddReferencesCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AddReferencesCount (declaration i=2235, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setAddReferencesCount(@Nullable ServiceCounterDataType value) {
    var node = getAddReferencesCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AddReferencesCount (declaration i=2235, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getDeleteNodesCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DeleteNodesCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:DeleteNodesCount (declaration i=2236, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getDeleteNodesCount() {
    var node = getDeleteNodesCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DeleteNodesCount (declaration i=2236, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setDeleteNodesCount(@Nullable ServiceCounterDataType value) {
    var node = getDeleteNodesCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DeleteNodesCount (declaration i=2236, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getDeleteReferencesCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DeleteReferencesCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:DeleteReferencesCount (declaration i=2237, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getDeleteReferencesCount() {
    var node = getDeleteReferencesCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DeleteReferencesCount (declaration i=2237, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setDeleteReferencesCount(@Nullable ServiceCounterDataType value) {
    var node = getDeleteReferencesCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DeleteReferencesCount (declaration i=2237, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getBrowseCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "BrowseCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:BrowseCount (declaration i=2238, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getBrowseCount() {
    var node = getBrowseCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:BrowseCount (declaration i=2238, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setBrowseCount(@Nullable ServiceCounterDataType value) {
    var node = getBrowseCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:BrowseCount (declaration i=2238, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getBrowseNextCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "BrowseNextCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:BrowseNextCount (declaration i=2239, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getBrowseNextCount() {
    var node = getBrowseNextCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:BrowseNextCount (declaration i=2239, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setBrowseNextCount(@Nullable ServiceCounterDataType value) {
    var node = getBrowseNextCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:BrowseNextCount (declaration i=2239, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getTranslateBrowsePathsToNodeIdsCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "TranslateBrowsePathsToNodeIdsCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:TranslateBrowsePathsToNodeIdsCount (declaration i=2240,"
                + " owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getTranslateBrowsePathsToNodeIdsCount() {
    var node = getTranslateBrowsePathsToNodeIdsCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:TranslateBrowsePathsToNodeIdsCount (declaration i=2240,"
              + " owner i=2197) on "
              + getNodeId());
    }
    return (ServiceCounterDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setTranslateBrowsePathsToNodeIdsCount(@Nullable ServiceCounterDataType value) {
    var node = getTranslateBrowsePathsToNodeIdsCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:TranslateBrowsePathsToNodeIdsCount (declaration i=2240,"
              + " owner i=2197) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getQueryFirstCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "QueryFirstCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:QueryFirstCount (declaration i=2241, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getQueryFirstCount() {
    var node = getQueryFirstCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:QueryFirstCount (declaration i=2241, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setQueryFirstCount(@Nullable ServiceCounterDataType value) {
    var node = getQueryFirstCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:QueryFirstCount (declaration i=2241, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getQueryNextCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "QueryNextCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:QueryNextCount (declaration i=2242, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getQueryNextCount() {
    var node = getQueryNextCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:QueryNextCount (declaration i=2242, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setQueryNextCount(@Nullable ServiceCounterDataType value) {
    var node = getQueryNextCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:QueryNextCount (declaration i=2242, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getRegisterNodesCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "RegisterNodesCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:RegisterNodesCount (declaration i=2730, owner i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getRegisterNodesCount() {
    var node = getRegisterNodesCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RegisterNodesCount (declaration i=2730, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setRegisterNodesCount(@Nullable ServiceCounterDataType value) {
    var node = getRegisterNodesCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RegisterNodesCount (declaration i=2730, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getUnregisterNodesCountNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "UnregisterNodesCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:UnregisterNodesCount (declaration i=2731, owner"
                + " i=2197)"));
  }

  @Override
  public @Nullable ServiceCounterDataType getUnregisterNodesCount() {
    var node = getUnregisterNodesCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UnregisterNodesCount (declaration i=2731, owner i=2197)"
              + " on "
              + getNodeId());
    }
    return (ServiceCounterDataType) node.getValue().getValue().getValue();
  }

  @Override
  public void setUnregisterNodesCount(@Nullable ServiceCounterDataType value) {
    var node = getUnregisterNodesCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UnregisterNodesCount (declaration i=2731, owner i=2197)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }
}
