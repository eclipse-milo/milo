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

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;
import org.eclipse.milo.opcua.sdk.pubsub.ComponentType;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetListener;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
import org.eclipse.milo.opcua.sdk.pubsub.MetaDataListener;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubComponents;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnosticsListener;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService.ReconfigureMode;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubStateListener;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetSource;
import org.eclipse.milo.opcua.sdk.pubsub.PublisherStatusListener;
import org.eclipse.milo.opcua.sdk.pubsub.ReconfigureResult;
import org.eclipse.milo.opcua.sdk.pubsub.SecurityKeyInfo;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.NodeFieldAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigValidationException;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

/**
 * {@link ManagedPubSubService} mediation: full delegation (without ever leaking the raw delegate),
 * pre-validation before any change is applied, ConfigurationVersion single-source maintenance, and
 * ordered, isolated {@link ManagedPubSubService.ReconfigureHook} execution.
 */
class ManagedPubSubServiceTest {

  private static final UInteger SEED_VERSION = uint(42);

  @Test
  void everyMethodReachesTheDelegate() throws Exception {
    var delegate = new FakePubSubService();
    ManagedPubSubService managed = managed(delegate, List.of());

    var handle = new PubSubHandle(ComponentType.CONNECTION, "conn");
    var readerRef = new DataSetReaderRef("conn", "group", "reader");
    var dataSetRef = new PublishedDataSetRef("ds");
    DataSetListener dataSetListener = event -> {};
    PubSubStateListener stateListener = event -> {};
    MetaDataListener metaDataListener = event -> {};
    PublisherStatusListener publisherStatusListener = event -> {};
    PubSubDiagnosticsListener diagnosticsListener = event -> {};
    PublishedDataSetSource source =
        context -> {
          throw new UnsupportedOperationException();
        };

    // the startup future must complete with the mediator, never the raw delegate: leaking
    // the delegate would hand callers a reconfigure path bypassing validation and hooks
    assertSame(managed, managed.startup().get());
    managed.shutdown().get();

    managed.components();
    managed.enable(handle);
    managed.disable(handle);
    assertEquals(PubSubState.Disabled, managed.state(handle));
    managed.bindSource(dataSetRef, source);
    managed.addDataSetListener(dataSetListener);
    managed.removeDataSetListener(dataSetListener);
    managed.addDataSetListener(readerRef, dataSetListener);
    managed.removeDataSetListener(readerRef, dataSetListener);
    managed.addStateListener(stateListener);
    managed.removeStateListener(stateListener);
    managed.addMetaDataListener(metaDataListener);
    managed.removeMetaDataListener(metaDataListener);
    managed.addPublisherStatusListener(publisherStatusListener);
    managed.removePublisherStatusListener(publisherStatusListener);
    managed.addDiagnosticsListener(diagnosticsListener);
    managed.removeDiagnosticsListener(diagnosticsListener);
    managed.diagnostics();
    assertNull(managed.securityKeyInfo(handle));
    assertEquals(uint(7), managed.nextDataSetMessageSequenceNumber(handle));
    managed.reconfigure(PubSubConfig.builder().build(), ReconfigureMode.DISABLE_AFFECTED);
    managed.update(config -> config);
    managed.close();

    assertEquals(
        List.of(
            "startup",
            "shutdown",
            "components",
            "enable",
            "disable",
            "state",
            "bindSource",
            "addDataSetListener",
            "removeDataSetListener",
            "addDataSetListener(reader)",
            "removeDataSetListener(reader)",
            "addStateListener",
            "removeStateListener",
            "addMetaDataListener",
            "removeMetaDataListener",
            "addPublisherStatusListener",
            "removePublisherStatusListener",
            "addDiagnosticsListener",
            "removeDiagnosticsListener",
            "diagnostics",
            "securityKeyInfo",
            "nextDataSetMessageSequenceNumber",
            "reconfigure",
            "update",
            "close"),
        delegate.calls);
  }

  @Test
  void reconfigureRunsHooksWithTheAppliedConfigAndResult() {
    var delegate = new FakePubSubService();
    var observed = new ArrayList<Object>();

    ManagedPubSubService managed =
        managed(
            delegate,
            List.of(
                (newConfig, result) -> {
                  observed.add(newConfig);
                  observed.add(result);
                }));

    PubSubConfig newConfig = PubSubConfig.builder().build();
    ReconfigureResult result = managed.reconfigure(newConfig, ReconfigureMode.DISABLE_AFFECTED);

    assertSame(delegate.result, result);
    assertEquals(List.of(newConfig, result), observed);
    assertSame(newConfig, delegate.current);
  }

  @Test
  void updateHooksReceiveTheTransformedConfig() {
    var delegate = new FakePubSubService();
    var observed = new ArrayList<PubSubConfig>();

    ManagedPubSubService managed =
        managed(delegate, List.of((newConfig, result) -> observed.add(newConfig)));

    PubSubConfig transformed = PubSubConfig.builder().build();
    ReconfigureResult result = managed.update(current -> transformed);

    assertSame(delegate.result, result);
    assertEquals(List.of(transformed), observed);
    assertSame(transformed, delegate.current);
  }

  @Test
  void preValidationFailureAppliesNothingAndSkipsHooks() {
    var delegate = new FakePubSubService();
    var hookRan = new ArrayList<PubSubConfig>();

    ManagedPubSubService managed =
        managed(delegate, List.of((newConfig, result) -> hookRan.add(newConfig)));

    assertThrows(
        PubSubConfigValidationException.class,
        () -> managed.reconfigure(unresolvableConfig(), ReconfigureMode.DISABLE_AFFECTED));

    // the delegate was never called: the failure precedes any engine change
    assertTrue(delegate.calls.isEmpty());
    assertTrue(hookRan.isEmpty());
    assertEquals(SEED_VERSION, managed.configurationVersion());
  }

  @Test
  void updatePreValidationFailurePropagatesAndSkipsHooks() {
    var delegate = new FakePubSubService();
    PubSubConfig original = delegate.current;
    var hookRan = new ArrayList<PubSubConfig>();

    ManagedPubSubService managed =
        managed(delegate, List.of((newConfig, result) -> hookRan.add(newConfig)));

    assertThrows(
        PubSubConfigValidationException.class,
        () -> managed.update(current -> unresolvableConfig()));

    // the throw aborted inside the delegate's transform application: nothing was applied
    assertEquals(List.of("update"), delegate.calls);
    assertSame(original, delegate.current);
    assertTrue(hookRan.isEmpty());
    assertEquals(SEED_VERSION, managed.configurationVersion());
  }

  @Test
  void engineValidationFailureSkipsHooks() {
    var delegate = new FakePubSubService();
    delegate.reconfigureFailure =
        new UaRuntimeException(StatusCodes.Bad_ConfigurationError, "rejected");
    var hookRan = new ArrayList<PubSubConfig>();

    ManagedPubSubService managed =
        managed(delegate, List.of((newConfig, result) -> hookRan.add(newConfig)));

    assertThrows(
        UaRuntimeException.class,
        () ->
            managed.reconfigure(PubSubConfig.builder().build(), ReconfigureMode.DISABLE_AFFECTED));

    assertTrue(hookRan.isEmpty());
    assertEquals(SEED_VERSION, managed.configurationVersion());
  }

  @Test
  void hookFailureIsIsolated() {
    var delegate = new FakePubSubService();
    var laterHookRan = new ArrayList<PubSubConfig>();

    ManagedPubSubService managed =
        managed(
            delegate,
            List.of(
                (newConfig, result) -> {
                  throw new IllegalStateException("hook failure");
                },
                (newConfig, result) -> laterHookRan.add(newConfig)));

    PubSubConfig newConfig = PubSubConfig.builder().build();
    ReconfigureResult result = managed.reconfigure(newConfig, ReconfigureMode.DISABLE_AFFECTED);

    // the change was applied and the result returned; the later hook still ran
    assertSame(delegate.result, result);
    assertEquals(List.of(newConfig), laterHookRan);
  }

  @Test
  void configurationVersionIsSeededAndBumpedOncePerSuccessfulApply() {
    var delegate = new FakePubSubService();
    var versionsSeenByHook = new ArrayList<UInteger>();

    ManagedPubSubService[] managedRef = new ManagedPubSubService[1];
    ManagedPubSubService managed =
        managed(
            delegate,
            List.of(
                (newConfig, result) ->
                    versionsSeenByHook.add(managedRef[0].configurationVersion())));
    managedRef[0] = managed;

    // seeded at construction (attach): the store-loaded non-zero version, else "now"
    assertEquals(SEED_VERSION, managed.configurationVersion());

    managed.reconfigure(PubSubConfig.builder().build(), ReconfigureMode.DISABLE_AFFECTED);
    UInteger afterFirst = managed.configurationVersion();

    managed.update(current -> current);
    UInteger afterSecond = managed.configurationVersion();

    // strictly increasing, exactly once per apply — even within the same VersionTime second
    assertTrue(afterFirst.longValue() > SEED_VERSION.longValue());
    assertTrue(afterSecond.longValue() > afterFirst.longValue());

    // the bump happens BEFORE the hooks run: each hook observed the already-advanced value
    assertEquals(List.of(afterFirst, afterSecond), versionsSeenByHook);
  }

  @Test
  void bindSourceIsObservedForUserBindingTracking() {
    var delegate = new FakePubSubService();
    var observed = new ArrayList<PublishedDataSetRef>();

    var managed =
        new ManagedPubSubService(
            delegate, new NamespaceTable(), List.of(), SEED_VERSION, observed::add);

    var ref = new PublishedDataSetRef("ds");
    managed.bindSource(
        ref,
        context -> {
          throw new UnsupportedOperationException();
        });

    // still plain delegation, but the observer saw the user-bound ref
    assertEquals(List.of("bindSource"), delegate.calls);
    assertEquals(List.of(ref), observed);
  }

  // region fixtures

  private static ManagedPubSubService managed(
      FakePubSubService delegate, List<ManagedPubSubService.ReconfigureHook> hooks) {

    return new ManagedPubSubService(delegate, new NamespaceTable(), hooks, SEED_VERSION, ref -> {});
  }

  /** A config whose published dataset field source namespace URI cannot be resolved. */
  private static PubSubConfig unresolvableConfig() {
    return PubSubConfig.builder()
        .publishedDataSet(
            PublishedDataSetConfig.builder("ds")
                .field(
                    FieldDefinition.builder("temperature")
                        .source(
                            NodeFieldAddress.parse(
                                "nsu=urn:milo:test:unresolvable;s=Temp", AttributeId.Value))
                        .dataType(NodeIds.Double)
                        .build())
                .build())
        .build();
  }

  /** Records call names; applies reconfigure/update to {@link #current} like the engine does. */
  private static final class FakePubSubService implements PubSubService {

    final List<String> calls = new ArrayList<>();

    PubSubConfig current = PubSubConfig.builder().build();
    ReconfigureResult result = new ReconfigureResult(List.of());
    @Nullable RuntimeException reconfigureFailure;

    private final PubSubComponents components =
        new PubSubComponents() {
          @Override
          public Optional<PubSubHandle> connection(String connectionName) {
            return Optional.empty();
          }

          @Override
          public Optional<PubSubHandle> writerGroup(String connectionName, String groupName) {
            return Optional.empty();
          }

          @Override
          public Optional<PubSubHandle> dataSetWriter(
              String connectionName, String groupName, String writerName) {
            return Optional.empty();
          }

          @Override
          public Optional<PubSubHandle> readerGroup(String connectionName, String groupName) {
            return Optional.empty();
          }

          @Override
          public Optional<PubSubHandle> dataSetReader(
              String connectionName, String groupName, String readerName) {
            return Optional.empty();
          }
        };

    private final PubSubDiagnostics diagnostics =
        new PubSubDiagnostics() {
          @Override
          public Map<String, ComponentDiagnostics> snapshot() {
            return Map.of();
          }

          @Override
          public void reset(String path) {}
        };

    @Override
    public CompletableFuture<PubSubService> startup() {
      calls.add("startup");
      return CompletableFuture.completedFuture(this);
    }

    @Override
    public CompletableFuture<Void> shutdown() {
      calls.add("shutdown");
      return CompletableFuture.completedFuture(null);
    }

    @Override
    public PubSubComponents components() {
      calls.add("components");
      return components;
    }

    @Override
    public void enable(PubSubHandle handle) {
      calls.add("enable");
    }

    @Override
    public void disable(PubSubHandle handle) {
      calls.add("disable");
    }

    @Override
    public PubSubState state(PubSubHandle handle) {
      calls.add("state");
      return PubSubState.Disabled;
    }

    @Override
    public ReconfigureResult reconfigure(PubSubConfig newConfig, ReconfigureMode mode) {
      calls.add("reconfigure");
      if (reconfigureFailure != null) {
        throw reconfigureFailure;
      }
      current = newConfig;
      return result;
    }

    @Override
    public ReconfigureResult update(UnaryOperator<PubSubConfig> transform) {
      calls.add("update");
      if (reconfigureFailure != null) {
        throw reconfigureFailure;
      }
      // like the engine: the transform is applied under the (here: notional) engine lock,
      // and a throw from it aborts before anything is applied
      current = requireNonNull(transform.apply(current));
      return result;
    }

    @Override
    public void bindSource(PublishedDataSetRef dataSet, PublishedDataSetSource source) {
      calls.add("bindSource");
    }

    @Override
    public void publishEvent(PublishedDataSetRef dataSet, List<Variant> fields) {
      calls.add("publishEvent");
    }

    @Override
    public void addDataSetListener(DataSetListener listener) {
      calls.add("addDataSetListener");
    }

    @Override
    public void removeDataSetListener(DataSetListener listener) {
      calls.add("removeDataSetListener");
    }

    @Override
    public void addDataSetListener(DataSetReaderRef reader, DataSetListener listener) {
      calls.add("addDataSetListener(reader)");
    }

    @Override
    public void removeDataSetListener(DataSetReaderRef reader, DataSetListener listener) {
      calls.add("removeDataSetListener(reader)");
    }

    @Override
    public void addStateListener(PubSubStateListener listener) {
      calls.add("addStateListener");
    }

    @Override
    public void removeStateListener(PubSubStateListener listener) {
      calls.add("removeStateListener");
    }

    @Override
    public void addMetaDataListener(MetaDataListener listener) {
      calls.add("addMetaDataListener");
    }

    @Override
    public void removeMetaDataListener(MetaDataListener listener) {
      calls.add("removeMetaDataListener");
    }

    @Override
    public void addPublisherStatusListener(PublisherStatusListener listener) {
      calls.add("addPublisherStatusListener");
    }

    @Override
    public void removePublisherStatusListener(PublisherStatusListener listener) {
      calls.add("removePublisherStatusListener");
    }

    @Override
    public void addDiagnosticsListener(PubSubDiagnosticsListener listener) {
      calls.add("addDiagnosticsListener");
    }

    @Override
    public void removeDiagnosticsListener(PubSubDiagnosticsListener listener) {
      calls.add("removeDiagnosticsListener");
    }

    @Override
    public PubSubDiagnostics diagnostics() {
      calls.add("diagnostics");
      return diagnostics;
    }

    @Override
    public @Nullable SecurityKeyInfo securityKeyInfo(PubSubHandle group) {
      calls.add("securityKeyInfo");
      return null;
    }

    @Override
    public UInteger nextDataSetMessageSequenceNumber(PubSubHandle writer) {
      calls.add("nextDataSetMessageSequenceNumber");
      return uint(7);
    }

    @Override
    public void close() {
      calls.add("close");
    }
  }

  // endregion
}
