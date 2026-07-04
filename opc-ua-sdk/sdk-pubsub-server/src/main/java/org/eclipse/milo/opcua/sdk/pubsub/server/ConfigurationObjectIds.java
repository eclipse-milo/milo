/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.server;

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

/**
 * Lookup of the information model object NodeIds for configuration elements, used to assemble the
 * {@code CloseAndUpdate} ConfigurationObjects output (pinned decision R4: "ConfigurationObjects
 * from the fragment's deterministic NodeIds or empty").
 *
 * <p>Implemented by the info model fragment: each lookup computes the deterministic R11 id (see
 * {@link PubSubNodeIds}) and returns it only if the node currently exists in the fragment's node
 * manager, so answers stay truthful during and after rebuilds. Obtained via {@link
 * ServerPubSub#configurationObjectIds()}, which returns {@code null} when the information model is
 * not exposed — the caller then returns the empty ConfigurationObjects array (the pin's "or empty"
 * arm).
 *
 * <p>Element kinds the fragment never materializes — SecurityGroups, standalone SubscribedDataSets,
 * and PushTargets — have no lookup here by design; callers fill their slots with {@link
 * NodeId#NULL_VALUE} per the D25 slot policy (see {@link ServerPubSub#configurationObjectIds()}).
 *
 * <p>Ordering contract: {@link ManagedPubSubService}'s post-apply hooks run synchronously inside
 * the mediator call, so when a remote-configuration handler regains control after {@code
 * managed.update(...)}, the nodes for surviving references already exist. All lookups take name
 * <em>components</em>, never joined paths (names may contain {@code '/'}).
 */
interface ConfigurationObjectIds {

  /**
   * @param connectionName the connection name.
   * @return the connection object's NodeId, or {@code null} if no such node exists.
   */
  @Nullable NodeId connectionObjectId(String connectionName);

  /**
   * @param connectionName the connection name.
   * @param groupName the writer group name.
   * @return the writer group object's NodeId, or {@code null} if no such node exists.
   */
  @Nullable NodeId writerGroupObjectId(String connectionName, String groupName);

  /**
   * @param connectionName the connection name.
   * @param groupName the writer group name.
   * @param writerName the dataset writer name.
   * @return the dataset writer object's NodeId, or {@code null} if no such node exists.
   */
  @Nullable NodeId dataSetWriterObjectId(
      String connectionName, String groupName, String writerName);

  /**
   * @param connectionName the connection name.
   * @param groupName the reader group name.
   * @return the reader group object's NodeId, or {@code null} if no such node exists.
   */
  @Nullable NodeId readerGroupObjectId(String connectionName, String groupName);

  /**
   * @param connectionName the connection name.
   * @param groupName the reader group name.
   * @param readerName the dataset reader name.
   * @return the dataset reader object's NodeId, or {@code null} if no such node exists.
   */
  @Nullable NodeId dataSetReaderObjectId(
      String connectionName, String groupName, String readerName);

  /**
   * @param name the PublishedDataSet name.
   * @return the published dataset object's NodeId, or {@code null} if no such node exists.
   */
  @Nullable NodeId publishedDataSetObjectId(String name);
}
