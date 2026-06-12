/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.internal;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MqttConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.StandaloneSubscribedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.StandaloneSubscribedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.jspecify.annotations.Nullable;

/**
 * Path-based diff between two {@link PubSubConfig}s, driving {@code reconfigure}.
 *
 * <p>The diff is minimal and non-overlapping: when a component's own ("shell") settings changed, a
 * single CHANGED entry is emitted for it and its subtree is not descended into; otherwise the diff
 * recurses to group and then writer/reader granularity, relying on the config types'
 * equals/hashCode. Changes to PublishedDataSets and standalone SubscribedDataSets are translated
 * into CHANGED entries for the writers/readers that reference them.
 *
 * <p>Exception: on broker (non-UDP) connections any reader-side change escalates to a
 * connection-level CHANGED, because broker subscriber channels derive their subscription set from
 * the connection config snapshot taken at channel open and have no refresh hook.
 */
final class ConfigDiff {

  private ConfigDiff() {}

  enum Kind {
    ADDED,
    REMOVED,
    CHANGED
  }

  enum Level {
    CONNECTION,
    WRITER_GROUP,
    READER_GROUP,
    DATA_SET_WRITER,
    DATA_SET_READER,
    /** PublishedDataSets, standalone SubscribedDataSets, SecurityGroups: reported only. */
    OTHER
  }

  /**
   * One element of the diff. Name components are carried explicitly so the apply step never has to
   * parse paths (names may contain '/').
   */
  record Change(
      Kind kind,
      Level level,
      String path,
      @Nullable String connectionName,
      @Nullable String groupName,
      @Nullable String componentName) {}

  /** The full diff: per-component changes plus whether the root enabled flag changed. */
  record Result(boolean rootEnabledChanged, List<Change> changes) {}

  static Result diff(PubSubConfig oldConfig, PubSubConfig newConfig) {
    var changes = new ArrayList<Change>();

    Set<String> changedDataSets =
        diffOther(
            byName(oldConfig.publishedDataSets(), PublishedDataSetConfig::getName),
            byName(newConfig.publishedDataSets(), PublishedDataSetConfig::getName),
            changes);

    Set<String> changedStandalone =
        diffOther(
            byName(
                oldConfig.standaloneSubscribedDataSets(),
                StandaloneSubscribedDataSetConfig::getName),
            byName(
                newConfig.standaloneSubscribedDataSets(),
                StandaloneSubscribedDataSetConfig::getName),
            changes);

    diffOther(
        byName(oldConfig.securityGroups(), SecurityGroupConfig::getName),
        byName(newConfig.securityGroups(), SecurityGroupConfig::getName),
        changes);

    Map<String, PubSubConnectionConfig> oldConnections =
        byName(oldConfig.connections(), PubSubConnectionConfig::name);
    Map<String, PubSubConnectionConfig> newConnections =
        byName(newConfig.connections(), PubSubConnectionConfig::name);

    var names = new LinkedHashSet<String>();
    names.addAll(oldConnections.keySet());
    names.addAll(newConnections.keySet());

    for (String name : names) {
      PubSubConnectionConfig oldConnection = oldConnections.get(name);
      PubSubConnectionConfig newConnection = newConnections.get(name);

      if (oldConnection == null) {
        changes.add(new Change(Kind.ADDED, Level.CONNECTION, name, name, null, null));
      } else if (newConnection == null) {
        changes.add(new Change(Kind.REMOVED, Level.CONNECTION, name, name, null, null));
      } else if (oldConnection.equals(newConnection)) {
        // unchanged
      } else if (!connectionShellEquals(oldConnection, newConnection)) {
        changes.add(new Change(Kind.CHANGED, Level.CONNECTION, name, name, null, null));
      } else if (!(newConnection instanceof UdpConnectionConfig)
          && !oldConnection.readerGroups().equals(newConnection.readerGroups())) {
        // broker subscriber channels compute their SUBSCRIBE set once, from the connection config
        // captured at channel open; reader-side adds/changes/removals therefore escalate to a
        // connection rebuild so the new channel subscribes the new queue set (UDP subscriber
        // sockets depend only on connection-level config and keep the granular diff)
        changes.add(new Change(Kind.CHANGED, Level.CONNECTION, name, name, null, null));
      } else {
        diffWriterGroups(name, oldConnection.writerGroups(), newConnection.writerGroups(), changes);
        diffReaderGroups(name, oldConnection.readerGroups(), newConnection.readerGroups(), changes);
      }
    }

    addDataSetInducedChanges(newConfig, changedDataSets, changes);
    addStandaloneInducedChanges(newConfig, changedStandalone, changes);

    boolean rootEnabledChanged = oldConfig.isEnabled() != newConfig.isEnabled();

    return new Result(rootEnabledChanged, List.copyOf(changes));
  }

  private static <T> Map<String, T> byName(List<T> values, Function<T, String> nameOf) {
    var map = new LinkedHashMap<String, T>(values.size());
    for (T value : values) {
      map.put(nameOf.apply(value), value);
    }
    return map;
  }

  /** Diff non-connection components by name; returns the set of changed names. */
  private static <T> Set<String> diffOther(
      Map<String, T> oldValues, Map<String, T> newValues, List<Change> changes) {

    var changed = new LinkedHashSet<String>();

    var names = new LinkedHashSet<String>();
    names.addAll(oldValues.keySet());
    names.addAll(newValues.keySet());

    for (String name : names) {
      T oldValue = oldValues.get(name);
      T newValue = newValues.get(name);

      if (oldValue == null) {
        changes.add(new Change(Kind.ADDED, Level.OTHER, name, null, null, null));
      } else if (newValue == null) {
        changes.add(new Change(Kind.REMOVED, Level.OTHER, name, null, null, null));
      } else if (!oldValue.equals(newValue)) {
        changes.add(new Change(Kind.CHANGED, Level.OTHER, name, null, null, null));
        changed.add(name);
      }
    }

    return changed;
  }

  private static void diffWriterGroups(
      String connectionName,
      List<WriterGroupConfig> oldGroups,
      List<WriterGroupConfig> newGroups,
      List<Change> changes) {

    Map<String, WriterGroupConfig> oldByName = byName(oldGroups, WriterGroupConfig::getName);
    Map<String, WriterGroupConfig> newByName = byName(newGroups, WriterGroupConfig::getName);

    var names = new LinkedHashSet<String>();
    names.addAll(oldByName.keySet());
    names.addAll(newByName.keySet());

    for (String name : names) {
      WriterGroupConfig oldGroup = oldByName.get(name);
      WriterGroupConfig newGroup = newByName.get(name);
      String path = connectionName + "/" + name;

      if (oldGroup == null) {
        changes.add(new Change(Kind.ADDED, Level.WRITER_GROUP, path, connectionName, name, null));
      } else if (newGroup == null) {
        changes.add(new Change(Kind.REMOVED, Level.WRITER_GROUP, path, connectionName, name, null));
      } else if (oldGroup.equals(newGroup)) {
        // unchanged
      } else if (!writerGroupShellEquals(oldGroup, newGroup)) {
        changes.add(new Change(Kind.CHANGED, Level.WRITER_GROUP, path, connectionName, name, null));
      } else {
        diffLeaves(
            connectionName,
            name,
            Level.DATA_SET_WRITER,
            byName(oldGroup.getDataSetWriters(), DataSetWriterConfig::getName),
            byName(newGroup.getDataSetWriters(), DataSetWriterConfig::getName),
            changes);
      }
    }
  }

  private static void diffReaderGroups(
      String connectionName,
      List<ReaderGroupConfig> oldGroups,
      List<ReaderGroupConfig> newGroups,
      List<Change> changes) {

    Map<String, ReaderGroupConfig> oldByName = byName(oldGroups, ReaderGroupConfig::getName);
    Map<String, ReaderGroupConfig> newByName = byName(newGroups, ReaderGroupConfig::getName);

    var names = new LinkedHashSet<String>();
    names.addAll(oldByName.keySet());
    names.addAll(newByName.keySet());

    for (String name : names) {
      ReaderGroupConfig oldGroup = oldByName.get(name);
      ReaderGroupConfig newGroup = newByName.get(name);
      String path = connectionName + "/" + name;

      if (oldGroup == null) {
        changes.add(new Change(Kind.ADDED, Level.READER_GROUP, path, connectionName, name, null));
      } else if (newGroup == null) {
        changes.add(new Change(Kind.REMOVED, Level.READER_GROUP, path, connectionName, name, null));
      } else if (oldGroup.equals(newGroup)) {
        // unchanged
      } else if (!readerGroupShellEquals(oldGroup, newGroup)) {
        changes.add(new Change(Kind.CHANGED, Level.READER_GROUP, path, connectionName, name, null));
      } else {
        diffLeaves(
            connectionName,
            name,
            Level.DATA_SET_READER,
            byName(oldGroup.getDataSetReaders(), DataSetReaderConfig::getName),
            byName(newGroup.getDataSetReaders(), DataSetReaderConfig::getName),
            changes);
      }
    }
  }

  private static <T> void diffLeaves(
      String connectionName,
      String groupName,
      Level level,
      Map<String, T> oldByName,
      Map<String, T> newByName,
      List<Change> changes) {

    var names = new LinkedHashSet<String>();
    names.addAll(oldByName.keySet());
    names.addAll(newByName.keySet());

    for (String name : names) {
      T oldValue = oldByName.get(name);
      T newValue = newByName.get(name);
      String path = connectionName + "/" + groupName + "/" + name;

      if (oldValue == null) {
        changes.add(new Change(Kind.ADDED, level, path, connectionName, groupName, name));
      } else if (newValue == null) {
        changes.add(new Change(Kind.REMOVED, level, path, connectionName, groupName, name));
      } else if (!oldValue.equals(newValue)) {
        changes.add(new Change(Kind.CHANGED, level, path, connectionName, groupName, name));
      }
    }
  }

  /** Writers referencing a changed PublishedDataSet must be restarted. */
  private static void addDataSetInducedChanges(
      PubSubConfig newConfig, Set<String> changedDataSets, List<Change> changes) {

    if (changedDataSets.isEmpty()) {
      return;
    }

    for (PubSubConnectionConfig connection : newConfig.connections()) {
      for (WriterGroupConfig group : connection.writerGroups()) {
        for (DataSetWriterConfig writer : group.getDataSetWriters()) {
          if (changedDataSets.contains(writer.getDataSet().name())) {
            String path = connection.name() + "/" + group.getName() + "/" + writer.getName();
            addIfNotCovered(
                changes,
                new Change(
                    Kind.CHANGED,
                    Level.DATA_SET_WRITER,
                    path,
                    connection.name(),
                    group.getName(),
                    writer.getName()));
          }
        }
      }
    }
  }

  /** Readers resolving metadata via a changed standalone SubscribedDataSet must be restarted. */
  private static void addStandaloneInducedChanges(
      PubSubConfig newConfig, Set<String> changedStandalone, List<Change> changes) {

    if (changedStandalone.isEmpty()) {
      return;
    }

    for (PubSubConnectionConfig connection : newConfig.connections()) {
      for (ReaderGroupConfig group : connection.readerGroups()) {
        for (DataSetReaderConfig reader : group.getDataSetReaders()) {
          if (reader.getDataSetMetaData() == null
              && reader.getSubscribedDataSet() instanceof StandaloneSubscribedDataSetRef ref
              && changedStandalone.contains(ref.name())) {

            String path = connection.name() + "/" + group.getName() + "/" + reader.getName();
            addIfNotCovered(
                changes,
                new Change(
                    Kind.CHANGED,
                    Level.DATA_SET_READER,
                    path,
                    connection.name(),
                    group.getName(),
                    reader.getName()));
          }
        }
      }
    }
  }

  /** Add a change unless it (or an ancestor in the connection tree) is already present. */
  private static void addIfNotCovered(List<Change> changes, Change change) {
    for (Change existing : changes) {
      if (existing.level() == Level.OTHER) {
        continue;
      }
      if (existing.path().equals(change.path())
          || change.path().startsWith(existing.path() + "/")) {
        return;
      }
    }
    changes.add(change);
  }

  private static boolean connectionShellEquals(PubSubConnectionConfig a, PubSubConnectionConfig b) {

    if (a instanceof UdpConnectionConfig udpA && b instanceof UdpConnectionConfig udpB) {
      return udpA.enabled() == udpB.enabled()
          && Objects.equals(udpA.publisherId(), udpB.publisherId())
          && udpA.getAddress().equals(udpB.getAddress())
          && udpA.properties().equals(udpB.properties())
          && Objects.equals(udpA.rawTransportSettings(), udpB.rawTransportSettings());
    } else if (a instanceof MqttConnectionConfig mqttA && b instanceof MqttConnectionConfig mqttB) {
      return mqttA.enabled() == mqttB.enabled()
          && Objects.equals(mqttA.publisherId(), mqttB.publisherId())
          && mqttA.getBrokerUri().equals(mqttB.getBrokerUri())
          && Objects.equals(mqttA.getBrokerSecurity(), mqttB.getBrokerSecurity())
          && mqttA.properties().equals(mqttB.properties())
          && Objects.equals(mqttA.rawTransportSettings(), mqttB.rawTransportSettings());
    } else {
      // different (or unknown future) connection types
      return false;
    }
  }

  private static boolean writerGroupShellEquals(WriterGroupConfig a, WriterGroupConfig b) {
    return a.isEnabled() == b.isEnabled()
        && a.getWriterGroupId().equals(b.getWriterGroupId())
        && a.getPublishingInterval().equals(b.getPublishingInterval())
        && Objects.equals(a.getKeepAliveTime(), b.getKeepAliveTime())
        && a.getPriority().equals(b.getPriority())
        && a.getMaxNetworkMessageSize().equals(b.getMaxNetworkMessageSize())
        && Objects.equals(a.getMessageSecurity(), b.getMessageSecurity())
        && a.getMessageSettings().equals(b.getMessageSettings())
        && Objects.equals(a.getBrokerTransport(), b.getBrokerTransport())
        && Objects.equals(a.getRawTransportSettings(), b.getRawTransportSettings())
        && Objects.equals(a.getRawMessageSettings(), b.getRawMessageSettings())
        && a.getProperties().equals(b.getProperties());
  }

  private static boolean readerGroupShellEquals(ReaderGroupConfig a, ReaderGroupConfig b) {
    return a.isEnabled() == b.isEnabled()
        && a.getMaxNetworkMessageSize().equals(b.getMaxNetworkMessageSize())
        && Objects.equals(a.getMessageSecurity(), b.getMessageSecurity())
        && Objects.equals(a.getRawTransportSettings(), b.getRawTransportSettings())
        && Objects.equals(a.getRawMessageSettings(), b.getRawMessageSettings())
        && a.getProperties().equals(b.getProperties());
  }
}
