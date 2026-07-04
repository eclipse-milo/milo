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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigFiles;
import org.eclipse.milo.opcua.sdk.pubsub.server.PubSubIdReservations.Grant;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.SessionListener;
import org.eclipse.milo.opcua.sdk.server.methods.MethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.Out;
import org.eclipse.milo.opcua.sdk.server.model.objects.FileType;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubConfigurationType;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationValueDataType;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The remote-configuration face: backs the ns0 {@code PublishSubscribe.PubSubConfiguration} file
 * object ({@code i=25451}, PubSubConfigurationType) with the eight Part 14 §9.1.3.7 method handlers
 * — Open/Close/Read/Write/GetPosition/SetPosition (FileType) plus ReserveIds and CloseAndUpdate —
 * and maintains the file object's property values (pinned decisions R1–R9).
 *
 * <p>Mirror of the {@link SksServerFace} pattern: loader-built ns0 nodes are mutated in place —
 * handler slots and values only — and restored at shutdown, with ONE sanctioned structural
 * exception (D19): the three optional properties the ns0 loader does not instantiate (MimeType
 * {@code i=25456}, MaxByteStringLength {@code i=25457}, LastModifiedTime {@code i=25458}) are
 * created with their reserved numeric ids inside <b>ns0's own node manager</b> (obtained from the
 * loader-built {@code i=25451} node) and removed at face shutdown. They cannot live in a fragment
 * node manager: service-level dispatch routes every ns0 NodeId to the ns0 namespace
 * first-filter-match, so fragment-hosted ns0-id nodes would read {@code Bad_NodeIdUnknown} over
 * real service calls. Typed create-on-set setters are never used (they would mint derived string
 * ids instead of the reserved numeric ids).
 *
 * <p>When {@link ServerPubSubOptions#isAllowRemoteConfiguration()} is {@code false} no face is
 * created: ns0 stays untouched — the eight methods keep the loader default {@code
 * Bad_NotImplemented} and the file property values stay loader-null (D20; preserves the Phase 2
 * untouched-ns0 contract).
 *
 * <p>Check order on every handler (K17.2 adapted, S13): session — a session-less invocation answers
 * {@code Bad_UserAccessDenied} (D29, not the SKS face's grandfathered {@code Bad_SessionIdInvalid})
 * — then arguments, then authorization via {@link PubSubMethodAuthorizer#checkConfigure} with any
 * bad code surfaced verbatim (R9/D41: ALL eight handlers are configure-gated, including the
 * read-side Open(0x01)/Read/GetPosition — a caller that may not configure may not read the
 * configuration file either), then handle existence and mode state, then work. No channel
 * security-mode gate exists: the spec pins no channel minimum for these methods ({@code i=25451}
 * carries no AccessRestrictions), so none is invented. SecurityGroup references additionally
 * consult {@link PubSubMethodAuthorizer#checkSksAdmin} once per CloseAndUpdate (R7).
 *
 * <p>Property values (R3/D43): {@code Size} always serves the real encoded length of the current
 * configuration file (never {@code Bad_NotSupported}); while a write handle is open it does not
 * track the buffer ("the size might not be accurate" is spec-tolerated). {@code Writable}/{@code
 * UserWritable} are capability values, {@code true} for any user (per-user enforcement lives in the
 * authorizer) and do not flip while a writer holds the lock. {@code OpenCount} is live on every
 * open/close/evict. {@code LastModifiedTime} starts at the face startup instant and then tracks
 * each apply instant, the same instant the ConfigurationVersion advances. Size and LastModifiedTime
 * refresh on <em>every</em> successful mediator apply — remote or owner {@code runtime()} — via
 * {@link #onConfigurationApplied}.
 *
 * <p>Snapshots and buffers are encoded/decoded with the {@link PubSubConfigFiles} DataType level;
 * the wire form always carries the mediator-owned ConfigurationVersion (D26) patched in via {@link
 * #withConfigurationVersion} — read as one atomically-swapped (config, version) pair, so a snapshot
 * racing an owner {@code runtime()} apply can never carry pre-apply content stamped with the
 * post-apply version (see {@link #applied}). Decoding a CloseAndUpdate buffer enforces the
 * §9.1.3.7.1 namespaces rule: a non-empty header must match the server's NamespaceTable
 * positionally, else {@code Bad_TypeMismatch} (D18).
 *
 * <p>Threading: one face lock serializes the eight handlers and session eviction; lock order is
 * one-way face lock &rarr; mediator lock &rarr; engine lock (CloseAndUpdate applies inside the
 * lock; the engine never calls into the face). {@link #onConfigurationApplied} is a mediator hook
 * and deliberately does NOT take the face lock — a hook (serialized by the mediator) taking the
 * face lock while a CloseAndUpdate holds it waiting on the mediator would deadlock; it touches only
 * volatile state and ns0 value writes. Those ns0 writes and {@link #shutdown()}'s value-restore
 * section share the {@link #ns0WriteLock} leaf monitor (never held while calling into the mediator
 * or engine), so a shutdown cannot interleave between the hook's {@code active} check and its
 * writes and be left with a stale Size on the loader-built node after restoring loader state.
 *
 * <p>Created by {@link ServerPubSub} when {@link ServerPubSubOptions#isAllowRemoteConfiguration()}
 * is {@code true}; {@link #startup()} and {@link #shutdown()} are driven by the owning {@link
 * ServerPubSub}'s lifecycle (same state machine as the fragment and SKS faces).
 */
final class PubSubConfigurationFace {

  private static final Logger LOGGER = LoggerFactory.getLogger(PubSubConfigurationFace.class);

  private final Object lock = new Object();

  /**
   * Leaf monitor guarding the ns0 property-value writes shared by {@link #onConfigurationApplied}
   * and {@link #shutdown()}'s restore section; taken after the face {@link #lock} when both are
   * held and never held while calling into the mediator or engine. The hook cannot take the face
   * lock (deadlock — see the class threading notes), but without mutual exclusion a concurrent
   * shutdown could null the property values between the hook's {@link #active} check and its
   * writes, leaving a stale Size value on the loader-built node.
   */
  private final Object ns0WriteLock = new Object();

  private final OpcUaServer server;
  private final PubSubMethodAuthorizer authorizer;
  private final @Nullable ConfigurationObjectIds configurationObjectIds;
  private final Supplier<UInteger> configurationVersion;
  private final Supplier<PubSubService> managedService;

  private final FileHandleManager handleManager = new FileHandleManager();
  private final PubSubIdReservations reservations;

  /** The read/write clamp: the served MaxByteStringLength value (R3; 1 MiB Milo default). */
  private final long maxByteStringLength;

  /**
   * The (configuration, ConfigurationVersion) pair the file reflects: seeded at attach, swapped as
   * ONE volatile record per mediator apply by {@link #onConfigurationApplied}. The pair is
   * published atomically because the mediator bumps its version BEFORE the hooks run: a pull path
   * that does not go through a hook (an Open snapshot via {@link #currentWireConfig()}, or
   * ReserveIds' live-id exclusion set) and races the hook window must observe the previous coherent
   * pair — never pre-apply content stamped with the post-apply version (D26: one clock, one value
   * per apply).
   */
  private volatile AppliedConfiguration applied;

  /**
   * Set under {@link #lock} by startup/shutdown (shutdown clears it under {@link #ns0WriteLock}
   * too); volatile because handlers and eviction read it under the face lock only while {@link
   * #onConfigurationApplied} re-checks it under {@link #ns0WriteLock}.
   */
  private volatile boolean active = false;

  /** The loader-built method nodes this face attached handlers to; guarded by {@link #lock}. */
  private final Map<UaMethodNode, MethodInvocationHandler> attachedHandlers = new LinkedHashMap<>();

  /** The three created optional property nodes (D19 carve-out); guarded by {@link #lock}. */
  private final List<UaNode> createdNodes = new ArrayList<>();

  private @Nullable SessionListener sessionListener;

  /**
   * @param initialConfigurationVersion the version the mediator is seeded with — the value its
   *     supplier serves until the first apply — pairing the attach-time {@code config} coherently
   *     from the start (the supplier itself cannot be read here: it is deferred until the owning
   *     {@link ServerPubSub}'s construction completed, and reading it lazily on first use would
   *     reintroduce the torn-pair window for the first apply).
   * @param configurationObjectIds the fragment-backed R11 lookup, or {@code null} when the
   *     information model is not exposed (CloseAndUpdate then returns the empty
   *     ConfigurationObjects array).
   * @param configurationVersion the mediator-owned ConfigurationVersion single source (D26);
   *     deferred — only read once the owning {@link ServerPubSub}'s construction completed.
   * @param managedService the mediator (S11); deferred like {@code configurationVersion}. All
   *     remote mutations are applied through it, never the raw engine service.
   */
  PubSubConfigurationFace(
      OpcUaServer server,
      PubSubConfig config,
      UInteger initialConfigurationVersion,
      PubSubMethodAuthorizer authorizer,
      @Nullable ConfigurationObjectIds configurationObjectIds,
      Supplier<UInteger> configurationVersion,
      Supplier<PubSubService> managedService) {

    this.server = server;
    this.applied = new AppliedConfiguration(config, initialConfigurationVersion);
    this.authorizer = authorizer;
    this.configurationObjectIds = configurationObjectIds;
    this.configurationVersion = configurationVersion;
    this.managedService = managedService;

    this.maxByteStringLength = server.getConfig().getLimits().getMaxByteStringLength().longValue();

    this.reservations = new PubSubIdReservations(firstEndpointPort(server));
  }

  /**
   * Attach the eight method handlers to the loader-built ns0 nodes, create the three optional
   * properties in ns0's node manager (D19), initialize the four mandatory property values, and
   * register the session-close eviction listener.
   */
  void startup() {
    synchronized (lock) {
      Optional<UaNode> node =
          server
              .getAddressSpaceManager()
              .getManagedNode(NodeIds.PublishSubscribe_PubSubConfiguration);

      UaNode configurationNode = node.orElse(null);
      if (configurationNode == null) {
        // modified-nodeset tolerance, the SksServerFace precedent
        LOGGER.warn(
            "ns0 PubSubConfiguration node not found: {}; the remote-configuration file model is"
                + " unavailable",
            NodeIds.PublishSubscribe_PubSubConfiguration);
        return;
      }

      attachHandler(NodeIds.PublishSubscribe_PubSubConfiguration_Open, OpenHandler::new);
      attachHandler(NodeIds.PublishSubscribe_PubSubConfiguration_Close, CloseHandler::new);
      attachHandler(NodeIds.PublishSubscribe_PubSubConfiguration_Read, ReadHandler::new);
      attachHandler(NodeIds.PublishSubscribe_PubSubConfiguration_Write, WriteHandler::new);
      attachHandler(
          NodeIds.PublishSubscribe_PubSubConfiguration_GetPosition, GetPositionHandler::new);
      attachHandler(
          NodeIds.PublishSubscribe_PubSubConfiguration_SetPosition, SetPositionHandler::new);
      attachHandler(
          NodeIds.PublishSubscribe_PubSubConfiguration_ReserveIds, ReserveIdsHandler::new);
      attachHandler(
          NodeIds.PublishSubscribe_PubSubConfiguration_CloseAndUpdate, CloseAndUpdateHandler::new);

      createProperty(
          configurationNode,
          NodeIds.PublishSubscribe_PubSubConfiguration_MimeType,
          "MimeType",
          NodeIds.String,
          Variant.ofString(PubSubConfigFiles.MIME_TYPE));
      createProperty(
          configurationNode,
          NodeIds.PublishSubscribe_PubSubConfiguration_MaxByteStringLength,
          "MaxByteStringLength",
          NodeIds.UInt32,
          Variant.ofUInt32(UInteger.valueOf(maxByteStringLength)));
      createProperty(
          configurationNode,
          NodeIds.PublishSubscribe_PubSubConfiguration_LastModifiedTime,
          "LastModifiedTime",
          NodeIds.DateTime,
          // D43: the initial value is the face startup instant — the instant the file came
          // into being on this server; each apply instant thereafter
          new Variant(DateTime.now()));

      setNs0Value(
          NodeIds.PublishSubscribe_PubSubConfiguration_Size,
          Variant.ofUInt64(ULong.valueOf(encodeCurrentFile().length)));
      // capability values, true for any user; they do NOT flip while a writer holds the lock
      setNs0Value(NodeIds.PublishSubscribe_PubSubConfiguration_Writable, Variant.ofBoolean(true));
      setNs0Value(
          NodeIds.PublishSubscribe_PubSubConfiguration_UserWritable, Variant.ofBoolean(true));
      setNs0Value(
          NodeIds.PublishSubscribe_PubSubConfiguration_OpenCount,
          Variant.ofUInt16(UShort.valueOf(0)));

      var listener =
          new SessionListener() {
            @Override
            public void onSessionClosed(Session session) {
              evictSession(session);
            }
          };
      server.getSessionManager().addSessionListener(listener);
      this.sessionListener = listener;

      active = true;
    }
  }

  /**
   * Restore {@link MethodInvocationHandler#NOT_IMPLEMENTED} on the eight method nodes (unless
   * another handler was attached after ours), delete the three created property nodes, null the
   * four mandatory property values, evict all handles and reservations, and remove the session
   * listener.
   */
  void shutdown() {
    synchronized (lock) {
      // the leaf monitor makes the restore atomic against a racing onConfigurationApplied:
      // once it clears `active`, no hook write can land on the restored/deleted nodes
      synchronized (ns0WriteLock) {
        active = false;

        createdNodes.forEach(UaNode::delete);
        createdNodes.clear();

        setNs0Value(NodeIds.PublishSubscribe_PubSubConfiguration_Size, Variant.NULL_VALUE);
        setNs0Value(NodeIds.PublishSubscribe_PubSubConfiguration_Writable, Variant.NULL_VALUE);
        setNs0Value(NodeIds.PublishSubscribe_PubSubConfiguration_UserWritable, Variant.NULL_VALUE);
        setNs0Value(NodeIds.PublishSubscribe_PubSubConfiguration_OpenCount, Variant.NULL_VALUE);
      }

      attachedHandlers.forEach(
          (node, handler) -> {
            if (node.getInvocationHandler() == handler) {
              node.setInvocationHandler(MethodInvocationHandler.NOT_IMPLEMENTED);
            } else {
              LOGGER.warn(
                  "ns0 method node {} no longer carries this face's handler; not restoring",
                  node.getNodeId());
            }
          });
      attachedHandlers.clear();

      handleManager.evictAll();
      reservations.clear();

      SessionListener listener = this.sessionListener;
      if (listener != null) {
        server.getSessionManager().removeSessionListener(listener);
        this.sessionListener = null;
      }
    }
  }

  /**
   * Mediator post-apply hook: retain the applied configuration for snapshots/ReserveIds, and
   * refresh the file's {@code Size} and {@code LastModifiedTime} — the apply instant, the same
   * instant the ConfigurationVersion advanced (D43). Runs for BOTH remote applies and owner {@code
   * runtime()} applies; serialized by the mediator; deliberately does not take the face lock (see
   * the class threading notes) — the ns0 writes are guarded by {@link #ns0WriteLock} instead, with
   * {@link #active} re-checked inside it so a racing shutdown's restore cannot be overwritten.
   */
  void onConfigurationApplied(PubSubConfig newConfig) {
    // ONE volatile swap publishes the coherent (config, version) pair: the hook runs after the
    // mediator's version bump, inside the mediator's critical section (applies serialize), so
    // the version read here is exactly the version of THIS apply
    applied = new AppliedConfiguration(newConfig, configurationVersion.get());

    synchronized (ns0WriteLock) {
      if (!active) {
        // not started (or already shut down): nothing exposed to refresh
        return;
      }

      try {
        setNs0Value(
            NodeIds.PublishSubscribe_PubSubConfiguration_Size,
            Variant.ofUInt64(ULong.valueOf(encodeCurrentFile().length)));
      } catch (Exception e) {
        LOGGER.warn("Error refreshing the PubSubConfiguration Size value", e);
      }

      // the created LastModifiedTime node lives in ns0's node manager while the face is active
      setNs0Value(
          NodeIds.PublishSubscribe_PubSubConfiguration_LastModifiedTime,
          new Variant(DateTime.now()));
    }
  }

  /**
   * Rebuild {@code wire} with {@code version} as its ConfigurationVersion: the mediator-owned
   * VersionTime is patched onto every observable wire form — snapshots, Size, and every store save,
   * including the attach-time save — retiring the mapper's {@code uint(0)} placeholder (D26).
   * Shared with {@link ServerPubSub}'s persistence.
   */
  static PubSubConfiguration2DataType withConfigurationVersion(
      PubSubConfiguration2DataType wire, UInteger version) {

    return new PubSubConfiguration2DataType(
        wire.getPublishedDataSets(),
        wire.getConnections(),
        wire.getEnabled(),
        wire.getSubscribedDataSets(),
        wire.getDataSetClasses(),
        wire.getDefaultSecurityKeyServices(),
        wire.getSecurityGroups(),
        wire.getPubSubKeyPushTargets(),
        version,
        wire.getConfigurationProperties());
  }

  // region internals

  /**
   * The current configuration in its file wire form, carrying the mediator version (D26). The
   * (config, version) pair is read atomically from {@link #applied}: reading the version supplier
   * here instead could pair pre-apply content with a post-apply version while the mediator hooks
   * are still running.
   */
  private PubSubConfiguration2DataType currentWireConfig() {
    AppliedConfiguration current = applied;
    PubSubConfiguration2DataType wire = current.config().toDataType(server.getNamespaceTable());
    return withConfigurationVersion(wire, current.version());
  }

  /** Encode the current configuration file (the Open snapshot and the Size source). */
  private byte[] encodeCurrentFile() {
    return PubSubConfigFiles.encodeDataType(currentWireConfig(), server.getStaticEncodingContext());
  }

  /** Session-close eviction (R3): driven by the registered {@link SessionListener} and tests. */
  void evictSession(Session session) {
    synchronized (lock) {
      NodeId sessionId = session.getSessionId();
      int evicted = handleManager.evictSession(sessionId);
      reservations.releaseSession(sessionId);
      if (evicted > 0 && active) {
        setOpenCountValue();
      }
    }
  }

  private void setOpenCountValue() {
    setNs0Value(
        NodeIds.PublishSubscribe_PubSubConfiguration_OpenCount,
        Variant.ofUInt16(UShort.valueOf(handleManager.openCount())));
  }

  private void attachHandler(
      NodeId methodNodeId,
      java.util.function.Function<UaMethodNode, MethodInvocationHandler> handlerFactory) {

    Optional<UaNode> node = server.getAddressSpaceManager().getManagedNode(methodNodeId);

    if (node.orElse(null) instanceof UaMethodNode methodNode) {
      if (methodNode.getInvocationHandler() != MethodInvocationHandler.NOT_IMPLEMENTED) {
        LOGGER.warn(
            "ns0 method node {} already has an invocation handler; replacing it", methodNodeId);
      }
      MethodInvocationHandler handler = handlerFactory.apply(methodNode);
      methodNode.setInvocationHandler(handler);
      attachedHandlers.put(methodNode, handler);
    } else {
      LOGGER.warn("ns0 UaMethodNode not found: {}", methodNodeId);
    }
  }

  /**
   * The D19 carve-out: create an optional property with its reserved ns0 numeric id inside ns0's
   * own node manager (the only routable mechanism — see the class Javadoc), grafted under {@code
   * i=25451} with the fragment's reference discipline. Removed at shutdown.
   */
  private void createProperty(
      UaNode configurationNode, NodeId nodeId, String name, NodeId dataTypeId, Variant value) {

    if (configurationNode.getNodeManager().containsNode(nodeId)) {
      LOGGER.warn("ns0 node already exists, not creating: {}", nodeId);
      return;
    }

    var node =
        new PropertyTypeNode(
            configurationNode.getNodeContext(),
            nodeId,
            new QualifiedName(0, name),
            LocalizedText.english(name),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            null,
            new DataValue(value),
            dataTypeId,
            ValueRanks.Scalar,
            null);

    node.addReference(
        new Reference(
            nodeId,
            NodeIds.HasTypeDefinition,
            NodeIds.PropertyType.expanded(),
            Reference.Direction.FORWARD));

    configurationNode.getNodeManager().addNode(node);

    node.addReference(
        new Reference(
            nodeId,
            NodeIds.HasProperty,
            configurationNode.getNodeId().expanded(),
            Reference.Direction.INVERSE));

    createdNodes.add(node);
  }

  /** Set the value of an existing loader-built ns0 variable node; never creates. */
  private void setNs0Value(NodeId nodeId, Variant value) {
    Optional<UaNode> node = server.getAddressSpaceManager().getManagedNode(nodeId);

    if (node.orElse(null) instanceof UaVariableNode variableNode) {
      variableNode.setValue(new DataValue(value));
    } else {
      LOGGER.warn("ns0 variable node not found: {}", nodeId);
    }
  }

  /**
   * The session-presence prefix shared by all eight handlers, first in the S13 check order: a
   * session-less invocation is {@code Bad_UserAccessDenied} (D29).
   */
  private Session checkedSession(
      org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
          context)
      throws UaException {

    return context
        .getSession()
        .orElseThrow(() -> new UaException(StatusCodes.Bad_UserAccessDenied));
  }

  /**
   * The authorization step shared by all eight handlers, run AFTER argument validation (the S13
   * check order: session &rarr; args &rarr; authorization): the authorizer's code is surfaced
   * verbatim (R9).
   */
  private void checkAuthorized(Session session) throws UaException {
    StatusCode checkResult = authorizer.checkConfigure(session);
    if (checkResult.isBad()) {
      throw new UaException(checkResult);
    }
  }

  private void checkActive() throws UaException {
    if (!active) {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }

  private static int firstEndpointPort(OpcUaServer server) {
    return server.getConfig().getEndpoints().stream()
        .findFirst()
        .map(endpoint -> endpoint.getBindPort())
        .orElse(PubSubIdReservations.DEFAULT_PORT);
  }

  // endregion

  // region handlers

  private final class OpenHandler extends FileType.OpenMethod {

    OpenHandler(UaMethodNode node) {
      super(node);
    }

    @Override
    protected void invoke(InvocationContext context, @Nullable UByte mode, Out<UInteger> fileHandle)
        throws UaException {

      Session session = checkedSession(context);

      if (mode == null) {
        throw new UaException(StatusCodes.Bad_InvalidArgument, "mode is null");
      }

      checkAuthorized(session);

      synchronized (lock) {
        checkActive();
        UInteger handle =
            handleManager.open(
                session.getSessionId(),
                mode.intValue(),
                PubSubConfigurationFace.this::encodeCurrentFile);
        setOpenCountValue();
        fileHandle.set(handle);
      }
    }
  }

  private final class CloseHandler extends FileType.CloseMethod {

    CloseHandler(UaMethodNode node) {
      super(node);
    }

    @Override
    protected void invoke(InvocationContext context, @Nullable UInteger fileHandle)
        throws UaException {

      Session session = checkedSession(context);

      if (fileHandle == null) {
        throw new UaException(StatusCodes.Bad_InvalidArgument, "fileHandle is null");
      }

      checkAuthorized(session);

      synchronized (lock) {
        checkActive();
        handleManager.close(session.getSessionId(), fileHandle);
        setOpenCountValue();
      }
    }
  }

  private final class ReadHandler extends FileType.ReadMethod {

    ReadHandler(UaMethodNode node) {
      super(node);
    }

    @Override
    protected void invoke(
        InvocationContext context,
        @Nullable UInteger fileHandle,
        @Nullable Integer length,
        Out<ByteString> data)
        throws UaException {

      Session session = checkedSession(context);

      if (fileHandle == null || length == null) {
        throw new UaException(StatusCodes.Bad_InvalidArgument, "fileHandle or length is null");
      }

      checkAuthorized(session);

      synchronized (lock) {
        checkActive();
        data.set(
            handleManager.read(session.getSessionId(), fileHandle, length, maxByteStringLength));
      }
    }
  }

  private final class WriteHandler extends FileType.WriteMethod {

    WriteHandler(UaMethodNode node) {
      super(node);
    }

    @Override
    protected void invoke(
        InvocationContext context, @Nullable UInteger fileHandle, @Nullable ByteString data)
        throws UaException {

      Session session = checkedSession(context);

      if (fileHandle == null) {
        throw new UaException(StatusCodes.Bad_InvalidArgument, "fileHandle is null");
      }

      checkAuthorized(session);

      synchronized (lock) {
        checkActive();
        handleManager.write(session.getSessionId(), fileHandle, data);
      }
    }
  }

  private final class GetPositionHandler extends FileType.GetPositionMethod {

    GetPositionHandler(UaMethodNode node) {
      super(node);
    }

    @Override
    protected void invoke(
        InvocationContext context, @Nullable UInteger fileHandle, Out<ULong> position)
        throws UaException {

      Session session = checkedSession(context);

      if (fileHandle == null) {
        throw new UaException(StatusCodes.Bad_InvalidArgument, "fileHandle is null");
      }

      checkAuthorized(session);

      synchronized (lock) {
        checkActive();
        position.set(ULong.valueOf(handleManager.position(session.getSessionId(), fileHandle)));
      }
    }
  }

  private final class SetPositionHandler extends FileType.SetPositionMethod {

    SetPositionHandler(UaMethodNode node) {
      super(node);
    }

    @Override
    protected void invoke(
        InvocationContext context, @Nullable UInteger fileHandle, @Nullable ULong position)
        throws UaException {

      Session session = checkedSession(context);

      if (fileHandle == null || position == null) {
        throw new UaException(StatusCodes.Bad_InvalidArgument, "fileHandle or position is null");
      }

      checkAuthorized(session);

      synchronized (lock) {
        checkActive();
        handleManager.setPosition(session.getSessionId(), fileHandle, position.longValue());
      }
    }
  }

  private final class ReserveIdsHandler extends PubSubConfigurationType.ReserveIdsMethod {

    ReserveIdsHandler(UaMethodNode node) {
      super(node);
    }

    @Override
    protected void invoke(
        InvocationContext context,
        @Nullable String transportProfileUri,
        @Nullable UShort numReqWriterGroupIds,
        @Nullable UShort numReqDataSetWriterIds,
        Out<Object> defaultPublisherId,
        Out<UShort[]> writerGroupIds,
        Out<UShort[]> dataSetWriterIds)
        throws UaException {

      Session session = checkedSession(context);

      if (transportProfileUri == null) {
        throw new UaException(StatusCodes.Bad_InvalidArgument, "TransportProfileUri is null");
      }

      checkAuthorized(session);

      synchronized (lock) {
        checkActive();

        // a reservation is NOT a configuration mutation: no store save, no version bump (R8);
        // the live-id exclusion set is read from the same atomically-swapped pair the file
        // snapshots use
        Grant grant =
            reservations.reserve(
                session.getSessionId(),
                transportProfileUri,
                numReqWriterGroupIds != null ? numReqWriterGroupIds.intValue() : 0,
                numReqDataSetWriterIds != null ? numReqDataSetWriterIds.intValue() : 0,
                applied.config());

        defaultPublisherId.set(grant.defaultPublisherId());
        writerGroupIds.set(grant.writerGroupIds());
        dataSetWriterIds.set(grant.dataSetWriterIds());
      }
    }
  }

  private final class CloseAndUpdateHandler extends PubSubConfigurationType.CloseAndUpdateMethod {

    CloseAndUpdateHandler(UaMethodNode node) {
      super(node);
    }

    @Override
    protected void invoke(
        InvocationContext context,
        @Nullable UInteger fileHandle,
        @Nullable Boolean requireCompleteUpdate,
        PubSubConfigurationRefDataType @Nullable [] configurationReferences,
        Out<Boolean> changesApplied,
        Out<StatusCode[]> referencesResults,
        Out<PubSubConfigurationValueDataType[]> configurationValues,
        Out<NodeId[]> configurationObjects)
        throws UaException {

      Session session = checkedSession(context);

      if (fileHandle == null || requireCompleteUpdate == null) {
        throw new UaException(
            StatusCodes.Bad_InvalidArgument, "fileHandle or requireCompleteUpdate is null");
      }

      checkAuthorized(session);

      synchronized (lock) {
        checkActive();

        NodeId sessionId = session.getSessionId();

        handleManager.checkWriteHandle(sessionId, fileHandle);

        // the handle/mode checks passed: the handle closes now, whether or not changes are
        // applied — including the Bad_NothingToDo and Bad_TypeMismatch failures below (D40);
        // the client re-opens to rewrite
        byte[] buffer = handleManager.closeWriteHandle(sessionId, fileHandle);
        setOpenCountValue();

        if (configurationReferences == null || configurationReferences.length == 0) {
          throw new UaException(StatusCodes.Bad_NothingToDo);
        }

        PubSubConfiguration2DataType file = decodeBuffer(buffer);

        var applier =
            new CloseAndUpdateApplier(
                managedService.get(),
                server.getNamespaceTable(),
                reservations,
                configurationObjectIds);

        CloseAndUpdateApplier.Outcome outcome =
            applier.apply(
                sessionId,
                // one checkSksAdmin evaluation per call, deferred until a bit-11 ref exists
                () -> authorizer.checkSksAdmin(session),
                file,
                requireCompleteUpdate,
                configurationReferences);

        if (outcome.changesApplied()) {
          // consumption only after a successful apply: a failed atomic CloseAndUpdate leaves
          // reservations intact
          reservations.consume(outcome.consumedReservations());
        }

        changesApplied.set(outcome.changesApplied());
        referencesResults.set(outcome.referencesResults());
        configurationValues.set(outcome.configurationValues());
        configurationObjects.set(outcome.configurationObjects());
      }
    }

    /**
     * Decode the buffer as a {@code UABinaryFileDataType} with a {@link
     * PubSubConfiguration2DataType} Body — the base {@link PubSubConfigurationDataType} is rejected
     * — enforcing the namespaces-header rule (D18); any failure is the method-level {@code
     * Bad_TypeMismatch} (the handle is already closed, D40).
     */
    private PubSubConfiguration2DataType decodeBuffer(byte[] buffer) throws UaException {
      return PubSubConfigFiles.decodeDataType(buffer, server.getStaticEncodingContext());
    }
  }

  // endregion

  /**
   * One apply's coherent (configuration, ConfigurationVersion) pair, published as a single volatile
   * swap so no pull path can pair one apply's content with another apply's version (D26).
   */
  private record AppliedConfiguration(PubSubConfig config, UInteger version) {}
}
