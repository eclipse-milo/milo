/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.servicesets;

import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;
import org.slf4j.LoggerFactory;

public enum Service {
  DISCOVERY_FIND_SERVERS,
  DISCOVERY_FIND_SERVERS_ON_NETWORK,
  DISCOVERY_GET_ENDPOINTS,
  DISCOVERY_REGISTER_SERVER,
  DISCOVERY_REGISTER_SERVER_2,

  SECURE_CHANNEL_OPEN_SECURE_CHANNEL,
  SECURE_CHANNEL_CLOSE_SECURE_CHANNEL,

  SESSION_CREATE_SESSION,
  SESSION_ACTIVATE_SESSION,
  SESSION_CLOSE_SESSION,
  SESSION_CANCEL,

  NODE_MANAGEMENT_ADD_NODES,
  NODE_MANAGEMENT_ADD_REFERENCES,
  NODE_MANAGEMENT_DELETE_NODES,
  NODE_MANAGEMENT_DELETE_REFERENCES,

  VIEW_BROWSE,
  VIEW_BROWSE_NEXT,
  VIEW_TRANSLATE_BROWSE_PATHS,
  VIEW_REGISTER_NODES,
  VIEW_UNREGISTER_NODES,

  QUERY_QUERY_FIRST,
  QUERY_QUERY_NEXT,

  ATTRIBUTE_READ,
  ATTRIBUTE_HISTORY_READ,
  ATTRIBUTE_WRITE,
  ATTRIBUTE_HISTORY_UPDATE,

  METHOD_CALL,

  MONITORED_ITEM_CREATE_MONITORED_ITEMS,
  MONITORED_ITEM_MODIFY_MONITORED_ITEMS,
  MONITORED_ITEM_SET_MONITORING_MODE,
  MONITORED_ITEM_SET_TRIGGERING,
  MONITORED_ITEM_DELETE_MONITORED_ITEMS,

  SUBSCRIPTION_CREATE_SUBSCRIPTION,
  SUBSCRIPTION_MODIFY_SUBSCRIPTION,
  SUBSCRIPTION_SET_PUBLISHING_MODE,
  SUBSCRIPTION_PUBLISH,
  SUBSCRIPTION_REPUBLISH,
  SUBSCRIPTION_TRANSFER_SUBSCRIPTIONS,
  SUBSCRIPTION_DELETE_SUBSCRIPTIONS;

  public static @Nullable Service from(ExpandedNodeId typeId) {
    Object identifier = typeId.getIdentifier();
    if (!(identifier instanceof UInteger)) {
      return null;
    }
    int id = ((UInteger) identifier).intValue();

    switch (id) {
      // @formatter:off
      case 420:
        return DISCOVERY_FIND_SERVERS;
      case 12190:
        return DISCOVERY_FIND_SERVERS_ON_NETWORK;
      case 426:
        return DISCOVERY_GET_ENDPOINTS;
      case 435:
        return DISCOVERY_REGISTER_SERVER;
      case 12193:
        return DISCOVERY_REGISTER_SERVER_2;

      case 444:
        return SECURE_CHANNEL_OPEN_SECURE_CHANNEL;
      case 450:
        return SECURE_CHANNEL_CLOSE_SECURE_CHANNEL;

      case 459:
        return SESSION_CREATE_SESSION;
      case 465:
        return SESSION_ACTIVATE_SESSION;
      case 471:
        return SESSION_CLOSE_SESSION;
      case 477:
        return SESSION_CANCEL;

      case 486:
        return NODE_MANAGEMENT_ADD_NODES;
      case 492:
        return NODE_MANAGEMENT_ADD_REFERENCES;
      case 498:
        return NODE_MANAGEMENT_DELETE_NODES;
      case 504:
        return NODE_MANAGEMENT_DELETE_REFERENCES;

      case 525:
        return VIEW_BROWSE;
      case 531:
        return VIEW_BROWSE_NEXT;
      case 552:
        return VIEW_TRANSLATE_BROWSE_PATHS;
      case 558:
        return VIEW_REGISTER_NODES;
      case 564:
        return VIEW_UNREGISTER_NODES;

      case 613:
        return QUERY_QUERY_FIRST;
      case 619:
        return QUERY_QUERY_NEXT;

      case 629:
        return ATTRIBUTE_READ;
      case 662:
        return ATTRIBUTE_HISTORY_READ;
      case 671:
        return ATTRIBUTE_WRITE;
      case 698:
        return ATTRIBUTE_HISTORY_UPDATE;

      case 710:
        return METHOD_CALL;

      case 749:
        return MONITORED_ITEM_CREATE_MONITORED_ITEMS;
      case 761:
        return MONITORED_ITEM_MODIFY_MONITORED_ITEMS;
      case 767:
        return MONITORED_ITEM_SET_MONITORING_MODE;
      case 773:
        return MONITORED_ITEM_SET_TRIGGERING;
      case 779:
        return MONITORED_ITEM_DELETE_MONITORED_ITEMS;

      case 785:
        return SUBSCRIPTION_CREATE_SUBSCRIPTION;
      case 791:
        return SUBSCRIPTION_MODIFY_SUBSCRIPTION;
      case 797:
        return SUBSCRIPTION_SET_PUBLISHING_MODE;
      case 824:
        return SUBSCRIPTION_PUBLISH;
      case 830:
        return SUBSCRIPTION_REPUBLISH;
      case 839:
        return SUBSCRIPTION_TRANSFER_SUBSCRIPTIONS;
      case 845:
        return SUBSCRIPTION_DELETE_SUBSCRIPTIONS;
      // @formatter:on
      default:
        LoggerFactory.getLogger(Service.class).warn("Unknown service id: " + id);
        return null;
    }
  }
}
