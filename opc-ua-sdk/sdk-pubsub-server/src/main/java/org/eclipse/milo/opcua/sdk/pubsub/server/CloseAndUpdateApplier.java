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
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.MessageSecurityConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MqttConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigElements;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigValidationException;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.StandaloneSubscribedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.server.PubSubIdReservations.IdKind;
import org.eclipse.milo.opcua.sdk.pubsub.server.PubSubIdReservations.UsedReservation;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetReaderDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetWriterDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonWriterGroupMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefMask;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationValueDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReaderGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SecurityGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.StandaloneSubscribedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.WriterGroupDataType;
import org.jspecify.annotations.Nullable;

/**
 * The CloseAndUpdate element-operation engine (Part 14 §9.1.3.7.6, Tables 239/241/243), applied
 * against the live {@link PubSubConfig} through the managed runtime.
 *
 * <p><b>Reference model:</b> a reference is (operation bits, one element-kind bit, file-array
 * coordinates). Exactly five operation-bit rows are valid — Add, Match, Add+Match, Modify, Remove;
 * every other combination, a mask with zero operation bits, and a mask with zero or multiple
 * reference bits is a per-element {@code Bad_InvalidArgument}. Match is legal only for Connection,
 * WriterGroup, and ReaderGroup references, and the matched file element's name and id shall be
 * null. Bit 12 (PushTarget) references are rejected per-element with {@code Bad_InvalidArgument}:
 * push targets are unmodeled, so the rejection is a modeling gap, not an authorization outcome. Bit
 * 11 (SecurityGroup) references are supported against the configured SecurityGroup model, guarded
 * by one {@code checkSksAdmin} evaluation per call: denial fails each bit-11 reference per-element
 * — never the method.
 *
 * <p><b>Coordinates:</b> indices address the FILE; names bind file elements to LIVE elements. For
 * writer/reader references the ElementIndex indexes the <em>group's</em>
 * DataSetWriters/DataSetReaders array — the only coherent reading of Table 241's "array of the
 * PubSubConfiguration". The GroupIndex writer branch is read symmetrically to the reader branch:
 * Table 241's final writer-branch sentence ("the name of the ReaderGroup is used…") is an obvious
 * copy-paste defect in v1.05.06 §9.1.3.7.3 — by symmetry the WriterGroup name binds the live group.
 * Out-of-bounds indices are {@code Bad_InvalidArgument}; unused index fields are not validated
 * against 0 (junk in unused slots is tolerated).
 *
 * <p><b>Processing order:</b> all Removes first, in input order; then the non-remove pass, parents
 * before children — Milo-defined deterministic order: kind-major (SecurityGroups,
 * PublishedDataSets, standalone SubscribedDataSets, Connections, groups, writers/readers), within a
 * kind by file coordinates (ConnectionIndex, GroupIndex, ElementIndex), then input order. A child
 * whose parent add/match failed gets {@code Bad_NotFound}. Parent binding resolves against the
 * working state (post-removes), so a remove and re-add of one name in one call binds children to
 * the new element. Duplicate names in the file are legal (the file arrays are never unique-keyed);
 * unreferenced file elements are inert, and when used as parents only their name matters.
 *
 * <p><b>Working model:</b> Config-level surgery on the live {@link PubSubConfig} via the builders —
 * never a whole-config DataType round trip, whose documented mapper losses (broker credentials,
 * metadata policies, local security-policy overrides, …) would strip local-only state from every
 * untouched component and induce spurious restarts. Only the referenced FILE elements are mapped
 * DataType→Config (via {@link PubSubConfigElements}, resolving security-group references against
 * the post-remove working set); untouched elements are reused object-identical. After each applied
 * reference the working configuration is trial-built: a validation failure fails that reference
 * (messages naming a duplicate NAME map to {@code Bad_BrowseNameDuplicated}; everything else —
 * including duplicate wire-id collisions — to {@code Bad_InvalidArgument}) and the working state
 * reverts to last-good.
 *
 * <p><b>Match:</b> structural comparison of the Table 239 field sets, performed on the
 * CONFIG-mapped forms of both sides so both share one normalization; connection/group properties
 * compare only the keys present in the file element. Local-only config the wire cannot carry
 * (broker credentials, security-policy URI overrides, the group-level broker metadata
 * queue/update-time) is excluded. Match on a WriterGroup whose live candidate has the UADP
 * NetworkMessageContentMask GroupHeader bit set answers {@code Bad_InvalidState} — a config-only
 * predicate, independent of the group's runtime state; JSON groups have no GroupHeader concept and
 * can never trigger it. A pure-Match ref anchors parents and reports the resolved name/identifier
 * but changes nothing.
 *
 * <p><b>ElementAdd auto-assignment:</b> a null <em>or empty</em> name gets {@code "<Kind><n>"},
 * unique in the working scope (Milo-defined scheme); a null connection PublisherId gets the
 * §6.2.7.1 default for the element's transport profile; a WriterGroupId/DataSetWriterId of 0 (wire
 * null) is allocated from 0x8000–0xFFFF, preferring the calling session's outstanding reservations
 * for the element's transport profile (consumed only on successful apply). Explicit ids
 * 0x0001–0x7FFF are always accepted; explicit ids 0x8000–0xFFFF are accepted unless reserved by
 * another session ({@code Bad_InvalidArgument} — reservations are cross-session exclusive). The
 * same exclusivity rule guards a <b>Modify</b> that introduces a server-range id the bound live
 * element does not already carry: another session's reservation fails the reference per-element,
 * while the calling session's own reservation is consumed on successful apply. Auto-assignment
 * applies to the referenced element only; nested children carrying null names or zero ids fail
 * their reference with {@code Bad_InvalidArgument}. A per-element {@code enabled} flag in the
 * payload is honored as ordinary payload (the top-level ignore rule covers only the top-level
 * Enable field): an element added enabled comes up through the §6.2.1 state machine — and every
 * applied change goes through {@code DISABLE_AFFECTED}, so affected components bounce visibly
 * through the state machine; per the documented engine rules, a path-stable restart — what a Modify
 * produces — <em>preserves</em> DataSetMessage and NetworkMessage sequence numbering (added
 * elements start at 0; only cross-call remove+re-add and a 16/32-bit mapping-width switch restart
 * at 0); this visibility is intended (do not switch modes; STOP_AND_RESTART is not exposed).
 *
 * <p><b>Apply:</b> every reference is individually evaluated and ReferencesResults is full-length
 * and order-matched in BOTH RequireCompleteUpdate modes; the method-level result is Good even when
 * only element operations fail (documented Milo behavior — no method result code covers element
 * failure). Atomic mode ({@code true}) applies the working configuration only when every reference
 * succeeded; partial mode applies the survivors. Either way there is exactly ONE apply, driven
 * through the mediator's {@link PubSubService#update} — which uses {@code DISABLE_AFFECTED} and
 * runs the transform against the <em>current</em> configuration inside the mediator's critical
 * section, so a concurrent {@code runtime()} apply can never be silently overwritten. A pure-Match
 * call (or one where no mutating reference survived) applies nothing, reports {@code
 * ChangesApplied=false} with its individual results, and skips the reconfigure — so no version bump
 * and no store save. If the engine's reconfigure validation rejects the surviving configuration,
 * nothing was applied and every reference still marked Good inherits the extracted status code
 * (else {@code Bad_ConfigurationError}) — documented Milo behavior; no per-ref attribution is
 * possible without an engine dry-run seam.
 *
 * <p><b>Top-level fields</b> ([CU §7.1], applied only when a mutating apply happens): {@code
 * Enable} and {@code DataSetClasses} are ignored; {@code DefaultSecurityKeyServices} replaces the
 * existing services if non-empty; {@code ConfigurationVersion} is ignored on input (the mediator
 * maintains the VersionTime); {@code ConfigurationProperties} are merged — a key with a value is
 * inserted or replaced, a key with a null value is deleted, absent keys are untouched.
 *
 * <p><b>Outputs:</b> ConfigurationValues entries exist only for Add/Match references where a name
 * or identifier was assigned (Add) or resolved (Match), self-correlating via their {@code
 * ConfigurationElement}; identifiers are typed per element kind (connection → PublisherId, writer
 * group → WriterGroupId, dataset writer → DataSetWriterId, null otherwise). Add-side assignments
 * are reported only when the changes were applied: an aborted apply (atomic mode with a failed
 * reference, or an engine-validation rejection) assigned nothing — the element does not exist and
 * its reservation was not consumed, so a retry may legitimately assign different values — and only
 * Match resolutions, which name elements that exist regardless, are reported. ConfigurationObjects
 * is length-matched with {@link NodeId#NULL_VALUE} in the slots of Remove references, failed
 * references, and kinds the information model never materializes (SecurityGroups, standalone
 * SubscribedDataSets, PushTargets) — or EMPTY when the {@link ConfigurationObjectIds} lookup is
 * absent. The mediator's post-apply hooks run synchronously inside {@code update}, so the fragment
 * nodes exist by the time the lookups run.
 */
final class CloseAndUpdateApplier {

  private final PubSubService managedService;
  private final NamespaceTable namespaceTable;
  private final PubSubIdReservations reservations;
  private final @Nullable ConfigurationObjectIds configurationObjectIds;

  /**
   * @param managedService the mediator — every apply MUST go through it, never the raw engine
   *     service.
   * @param configurationObjectIds the fragment-backed lookup, or {@code null} when the information
   *     model is not exposed (ConfigurationObjects is then empty).
   */
  CloseAndUpdateApplier(
      PubSubService managedService,
      NamespaceTable namespaceTable,
      PubSubIdReservations reservations,
      @Nullable ConfigurationObjectIds configurationObjectIds) {

    this.managedService = managedService;
    this.namespaceTable = namespaceTable;
    this.reservations = reservations;
    this.configurationObjectIds = configurationObjectIds;
  }

  /**
   * Evaluate and apply {@code references} against the decoded file buffer.
   *
   * @param sessionId the calling session's id (reservation preference and exclusivity).
   * @param sksAdminCheck evaluated at most ONCE per call, iff any SecurityGroup reference is
   *     present ({@code Session.getRoleIds()} is recomputed per call).
   * @param file the decoded buffer content.
   * @param requireCompleteUpdate atomic ({@code true}) vs partial ({@code false}) mode.
   * @param references the ConfigurationReferences argument; never null or empty (the handler
   *     answers {@code Bad_NothingToDo} first).
   * @return the outcome; never throws for element-level failures.
   */
  Outcome apply(
      NodeId sessionId,
      Supplier<StatusCode> sksAdminCheck,
      PubSubConfiguration2DataType file,
      boolean requireCompleteUpdate,
      PubSubConfigurationRefDataType[] references) {

    int n = references.length;

    var results = new StatusCode[n];
    Arrays.fill(results, StatusCode.GOOD);

    var fileArrays = new FileArrays(file);

    var refs = new Ref[n];
    for (int i = 0; i < n; i++) {
      refs[i] = new Ref(i, references[i]);
      try {
        parseRef(refs[i], fileArrays);
      } catch (RefFailedException e) {
        results[i] = e.status;
      }
    }

    // bit-11 guard: one checkSksAdmin evaluation per call; denial fails each SecurityGroup
    // reference per-element (the authorizer's code surfaced verbatim), other references proceed
    boolean anySecurityGroupRef =
        Arrays.stream(refs)
            .anyMatch(ref -> ref.kind == Kind.SECURITY_GROUP && results[ref.index].isGood());
    if (anySecurityGroupRef) {
      StatusCode check = sksAdminCheck.get();
      if (check.isBad()) {
        for (Ref ref : refs) {
          if (ref.kind == Kind.SECURITY_GROUP && results[ref.index].isGood()) {
            results[ref.index] = check;
          }
        }
      }
    }

    var consumed = new LinkedHashSet<UsedReservation>();
    PubSubConfig[] applied = new PubSubConfig[1];

    try {
      managedService.update(
          current -> {
            consumed.clear();

            var pass = new Pass(current, fileArrays, sessionId, consumed);
            pass.run(refs, results);

            boolean anyBad = Arrays.stream(results).anyMatch(StatusCode::isBad);
            boolean anyMutation = Arrays.stream(refs).anyMatch(ref -> ref.mutationApplied);

            if ((requireCompleteUpdate && anyBad) || !anyMutation) {
              // atomic mode with a failed reference, or nothing mutating survived (e.g. a
              // pure-Match call): apply NOTHING — abort the update with the evaluation intact
              throw new NotAppliedException();
            }

            applied[0] = pass.applyTopLevel(file);
            return applied[0];
          });
    } catch (NotAppliedException e) {
      consumed.clear();
    } catch (Exception e) {
      // the mediator's pre-validation or the engine's reconfigure validation rejected the
      // surviving configuration: nothing was applied; every reference still marked Good
      // inherits the extracted status code
      consumed.clear();
      applied[0] = null;

      StatusCode code =
          UaException.extractStatusCode(e)
              .orElse(new StatusCode(StatusCodes.Bad_ConfigurationError));
      for (int i = 0; i < n; i++) {
        if (results[i].isGood()) {
          results[i] = code;
        }
      }
    }

    return new Outcome(
        applied[0] != null,
        results,
        configurationValues(refs, results, applied[0] != null),
        configurationObjects(refs, results),
        applied[0],
        consumed);
  }

  // region reference parsing

  private static final int REF_WRITER = 1;
  private static final int REF_READER = 2;
  private static final int REF_WRITER_GROUP = 3;
  private static final int REF_READER_GROUP = 4;
  private static final int REF_CONNECTION = 5;
  private static final int REF_PUB_DATASET = 6;
  private static final int REF_SUB_DATASET = 7;
  private static final int REF_SECURITY_GROUP = 8;
  private static final int REF_PUSH_TARGET = 9;

  private void parseRef(Ref ref, FileArrays file) {
    PubSubConfigurationRefMask mask = ref.wire.getConfigurationMask();
    if (mask == null) {
      throw invalidRef("null ConfigurationMask");
    }

    int opBits =
        (mask.getElementAdd() ? 1 : 0)
            | (mask.getElementMatch() ? 2 : 0)
            | (mask.getElementModify() ? 4 : 0)
            | (mask.getElementRemove() ? 8 : 0);

    ref.op =
        switch (opBits) {
          case 1 -> Op.ADD;
          case 2 -> Op.MATCH;
          case 3 -> Op.ADD_MATCH;
          case 4 -> Op.MODIFY;
          case 8 -> Op.REMOVE;
          default ->
              throw invalidRef("invalid element operation bits: 0x" + Integer.toHexString(opBits));
        };

    var refBits = new ArrayList<Integer>(2);
    if (mask.getReferenceWriter()) refBits.add(REF_WRITER);
    if (mask.getReferenceReader()) refBits.add(REF_READER);
    if (mask.getReferenceWriterGroup()) refBits.add(REF_WRITER_GROUP);
    if (mask.getReferenceReaderGroup()) refBits.add(REF_READER_GROUP);
    if (mask.getReferenceConnection()) refBits.add(REF_CONNECTION);
    if (mask.getReferencePubDataset()) refBits.add(REF_PUB_DATASET);
    if (mask.getReferenceSubDataset()) refBits.add(REF_SUB_DATASET);
    if (mask.getReferenceSecurityGroup()) refBits.add(REF_SECURITY_GROUP);
    if (mask.getReferencePushTarget()) refBits.add(REF_PUSH_TARGET);

    if (refBits.size() != 1) {
      throw invalidRef("exactly one reference bit shall be set; " + refBits.size() + " are");
    }

    ref.kind =
        switch (refBits.get(0)) {
          case REF_WRITER -> Kind.DATA_SET_WRITER;
          case REF_READER -> Kind.DATA_SET_READER;
          case REF_WRITER_GROUP -> Kind.WRITER_GROUP;
          case REF_READER_GROUP -> Kind.READER_GROUP;
          case REF_CONNECTION -> Kind.CONNECTION;
          case REF_PUB_DATASET -> Kind.PUBLISHED_DATA_SET;
          case REF_SUB_DATASET -> Kind.SUBSCRIBED_DATA_SET;
          case REF_SECURITY_GROUP -> Kind.SECURITY_GROUP;
          // unmodeled push targets are a modeling gap, rejected per-element
          default -> throw invalidRef("PubSubKeyPushTargets are not supported");
        };

    if ((ref.op == Op.MATCH || ref.op == Op.ADD_MATCH)
        && ref.kind != Kind.CONNECTION
        && ref.kind != Kind.WRITER_GROUP
        && ref.kind != Kind.READER_GROUP) {
      throw invalidRef("ElementMatch shall only be applied for connection and group references");
    }

    ref.connectionIndex = intValue(ref.wire.getConnectionIndex());
    ref.groupIndex = intValue(ref.wire.getGroupIndex());
    ref.elementIndex = intValue(ref.wire.getElementIndex());

    // bounds checks against the FILE arrays, per kind; unused indices are not validated
    switch (ref.kind) {
      case CONNECTION ->
          checkIndex(ref.connectionIndex, file.connections.length, "ConnectionIndex");
      case WRITER_GROUP -> {
        checkIndex(ref.connectionIndex, file.connections.length, "ConnectionIndex");
        checkIndex(ref.groupIndex, file.writerGroups(ref.connectionIndex).length, "GroupIndex");
      }
      case READER_GROUP -> {
        checkIndex(ref.connectionIndex, file.connections.length, "ConnectionIndex");
        checkIndex(ref.groupIndex, file.readerGroups(ref.connectionIndex).length, "GroupIndex");
      }
      case DATA_SET_WRITER -> {
        checkIndex(ref.connectionIndex, file.connections.length, "ConnectionIndex");
        checkIndex(ref.groupIndex, file.writerGroups(ref.connectionIndex).length, "GroupIndex");
        checkIndex(
            ref.elementIndex,
            file.dataSetWriters(ref.connectionIndex, ref.groupIndex).length,
            "ElementIndex");
      }
      case DATA_SET_READER -> {
        checkIndex(ref.connectionIndex, file.connections.length, "ConnectionIndex");
        checkIndex(ref.groupIndex, file.readerGroups(ref.connectionIndex).length, "GroupIndex");
        checkIndex(
            ref.elementIndex,
            file.dataSetReaders(ref.connectionIndex, ref.groupIndex).length,
            "ElementIndex");
      }
      case PUBLISHED_DATA_SET ->
          checkIndex(ref.elementIndex, file.publishedDataSets.length, "ElementIndex");
      case SUBSCRIBED_DATA_SET ->
          checkIndex(ref.elementIndex, file.subscribedDataSets.length, "ElementIndex");
      case SECURITY_GROUP ->
          checkIndex(ref.elementIndex, file.securityGroups.length, "ElementIndex");
    }

    // under Match "the Id and name shall be null" of the FILE element
    if (ref.op == Op.MATCH || ref.op == Op.ADD_MATCH) {
      switch (ref.kind) {
        case CONNECTION -> {
          PubSubConnectionDataType element = file.connections[ref.connectionIndex];
          Variant publisherId = element.getPublisherId();
          if (!nullOrEmpty(element.getName()) || (publisherId != null && !publisherId.isNull())) {
            throw invalidRef("the name and PublisherId of a matched connection shall be null");
          }
        }
        case WRITER_GROUP -> {
          WriterGroupDataType element = file.writerGroups(ref.connectionIndex)[ref.groupIndex];
          if (!nullOrEmpty(element.getName()) || intValue(element.getWriterGroupId()) != 0) {
            throw invalidRef("the name and WriterGroupId of a matched writer group shall be null");
          }
        }
        case READER_GROUP -> {
          ReaderGroupDataType element = file.readerGroups(ref.connectionIndex)[ref.groupIndex];
          if (!nullOrEmpty(element.getName())) {
            throw invalidRef("the name of a matched reader group shall be null");
          }
        }
        default -> throw invalidRef("unreachable");
      }
    }
  }

  private static void checkIndex(int index, int length, String name) {
    if (index >= length) {
      throw invalidRef(
          "%s %d is out of bounds (file array length %d)".formatted(name, index, length));
    }
  }

  // endregion

  // region working pass

  /** The mutable working state of one evaluation pass; runs inside the mediator's transform. */
  private final class Pass {

    private final FileArrays file;
    private final NodeId sessionId;
    private final Set<UsedReservation> consumed;

    private boolean enabled;
    private List<PubSubConnectionConfig> connections;
    private List<PublishedDataSetConfig> publishedDataSets;
    private List<StandaloneSubscribedDataSetConfig> standaloneSubscribedDataSets;
    private List<SecurityGroupConfig> securityGroups;
    private List<EndpointDescription> defaultSecurityKeyServices;
    private Map<QualifiedName, Variant> properties;

    /** The last-good build of the working state. */
    private PubSubConfig working;

    /** connIdx → resolved live connection name, recorded by successful connection references. */
    private final Map<Integer, String> connectionNames = new HashMap<>();

    private final Set<Integer> failedConnections = new HashSet<>();

    /** (writer?, connIdx, groupIdx) → resolved live group name. */
    private final Map<Long, String> groupNames = new HashMap<>();

    private final Set<Long> failedGroups = new HashSet<>();

    private Pass(
        PubSubConfig current, FileArrays file, NodeId sessionId, Set<UsedReservation> consumed) {
      this.file = file;
      this.sessionId = sessionId;
      this.consumed = consumed;

      this.enabled = current.isEnabled();
      this.connections = List.copyOf(current.connections());
      this.publishedDataSets = List.copyOf(current.publishedDataSets());
      this.standaloneSubscribedDataSets = List.copyOf(current.standaloneSubscribedDataSets());
      this.securityGroups = List.copyOf(current.securityGroups());
      this.defaultSecurityKeyServices = List.copyOf(current.defaultSecurityKeyServices());
      this.properties = new LinkedHashMap<>(current.properties());
      this.working = current;
    }

    private void run(Ref[] refs, StatusCode[] results) {
      // all Removes first, in input order
      for (Ref ref : refs) {
        if (ref.op == Op.REMOVE && results[ref.index].isGood()) {
          try {
            applyRemove(ref);
          } catch (RefFailedException e) {
            results[ref.index] = e.status;
          }
        }
      }

      // non-remove pass: kind-major, then file coordinates, then input order. A ref whose
      // parsing failed before its kind resolved (zero/multiple reference bits, bit-12
      // PushTargets) is excluded entirely: it is already marked Bad_InvalidArgument and
      // recordFailure is a no-op without a kind — admitting it would NPE the comparator.
      List<Ref> sorted =
          Arrays.stream(refs)
              .filter(ref -> ref.op != null && ref.kind != null && ref.op != Op.REMOVE)
              .sorted(
                  Comparator.comparingInt((Ref ref) -> ref.kind.rank)
                      .thenComparingInt(ref -> ref.connectionIndex)
                      .thenComparingInt(ref -> ref.groupIndex)
                      .thenComparingInt(ref -> ref.elementIndex)
                      .thenComparingInt(ref -> ref.index))
              .toList();

      for (Ref ref : sorted) {
        if (results[ref.index].isGood()) {
          try {
            applyNonRemove(ref);
          } catch (RefFailedException e) {
            results[ref.index] = e.status;
            recordFailure(ref);
          }
        } else {
          // structurally failed or denied earlier; children must see the failed parent
          recordFailure(ref);
        }
      }
    }

    /**
     * Marks a failed parent coordinate so dependent children answer {@code Bad_NotFound} — only for
     * Add/Match-shaped failures, per the [CU 3.3] trigger ("was not added or matched"). A failed
     * Modify leaves the live parent existing and the working state reverted to last-good, so
     * children still bind by name; a genuinely missing Modify target fails its children through the
     * name lookup instead ({@code parentNotFound}).
     */
    private void recordFailure(Ref ref) {
      if (ref.op == Op.MODIFY) {
        return;
      }
      if (ref.kind == Kind.CONNECTION) {
        failedConnections.add(ref.connectionIndex);
      } else if (ref.kind == Kind.WRITER_GROUP) {
        failedGroups.add(groupKey(true, ref.connectionIndex, ref.groupIndex));
      } else if (ref.kind == Kind.READER_GROUP) {
        failedGroups.add(groupKey(false, ref.connectionIndex, ref.groupIndex));
      }
    }

    // region removes

    private void applyRemove(Ref ref) {
      switch (ref.kind) {
        case CONNECTION -> {
          String name = requireBindingName(file.connections[ref.connectionIndex].getName());
          if (findConnection(name) == null) {
            throw noMatch("connection", name);
          }
          commitConnections(minus(connections, c -> c.name().equals(name)));
          ref.resolvedName = name;
        }
        case WRITER_GROUP -> {
          PubSubConnectionConfig parent = bindParentConnection(ref);
          String name =
              requireBindingName(file.writerGroups(ref.connectionIndex)[ref.groupIndex].getName());
          if (parent.writerGroups().stream().noneMatch(g -> g.getName().equals(name))) {
            throw noMatch("writer group", name);
          }
          List<WriterGroupConfig> groups =
              minus(parent.writerGroups(), g -> g.getName().equals(name));
          replaceConnection(parent, connectionWithGroups(parent, groups, parent.readerGroups()));
          ref.resolvedConnectionName = parent.name();
          ref.resolvedName = name;
        }
        case READER_GROUP -> {
          PubSubConnectionConfig parent = bindParentConnection(ref);
          String name =
              requireBindingName(file.readerGroups(ref.connectionIndex)[ref.groupIndex].getName());
          if (parent.readerGroups().stream().noneMatch(g -> g.getName().equals(name))) {
            throw noMatch("reader group", name);
          }
          List<ReaderGroupConfig> groups =
              minus(parent.readerGroups(), g -> g.getName().equals(name));
          replaceConnection(parent, connectionWithGroups(parent, parent.writerGroups(), groups));
          ref.resolvedConnectionName = parent.name();
          ref.resolvedName = name;
        }
        case DATA_SET_WRITER -> {
          PubSubConnectionConfig parent = bindParentConnection(ref);
          WriterGroupConfig group = bindParentWriterGroup(ref, parent);
          String name =
              requireBindingName(
                  file.dataSetWriters(ref.connectionIndex, ref.groupIndex)[ref.elementIndex]
                      .getName());
          if (group.getDataSetWriters().stream().noneMatch(w -> w.getName().equals(name))) {
            throw noMatch("dataset writer", name);
          }
          List<DataSetWriterConfig> writers =
              minus(group.getDataSetWriters(), w -> w.getName().equals(name));
          replaceWriterGroup(parent, group, groupWithWriters(group, writers));
          ref.resolvedConnectionName = parent.name();
          ref.resolvedGroupName = group.getName();
          ref.resolvedName = name;
        }
        case DATA_SET_READER -> {
          PubSubConnectionConfig parent = bindParentConnection(ref);
          ReaderGroupConfig group = bindParentReaderGroup(ref, parent);
          String name =
              requireBindingName(
                  file.dataSetReaders(ref.connectionIndex, ref.groupIndex)[ref.elementIndex]
                      .getName());
          if (group.getDataSetReaders().stream().noneMatch(r -> r.getName().equals(name))) {
            throw noMatch("dataset reader", name);
          }
          List<DataSetReaderConfig> readers =
              minus(group.getDataSetReaders(), r -> r.getName().equals(name));
          replaceReaderGroup(parent, group, groupWithReaders(group, readers));
          ref.resolvedConnectionName = parent.name();
          ref.resolvedGroupName = group.getName();
          ref.resolvedName = name;
        }
        case PUBLISHED_DATA_SET -> {
          String name = requireBindingName(file.publishedDataSets[ref.elementIndex].getName());
          if (publishedDataSets.stream().noneMatch(ds -> ds.getName().equals(name))) {
            throw noMatch("published dataset", name);
          }
          commitPublishedDataSets(minus(publishedDataSets, ds -> ds.getName().equals(name)));
          ref.resolvedName = name;
        }
        case SUBSCRIBED_DATA_SET -> {
          String name = requireBindingName(file.subscribedDataSets[ref.elementIndex].getName());
          if (standaloneSubscribedDataSets.stream().noneMatch(ds -> ds.getName().equals(name))) {
            throw noMatch("standalone subscribed dataset", name);
          }
          commitStandaloneSubscribedDataSets(
              minus(standaloneSubscribedDataSets, ds -> ds.getName().equals(name)));
          ref.resolvedName = name;
        }
        case SECURITY_GROUP -> {
          String name = requireBindingName(file.securityGroups[ref.elementIndex].getName());
          if (securityGroups.stream().noneMatch(sg -> sg.getName().equals(name))) {
            throw noMatch("security group", name);
          }
          // a group still referenced by a secured writer/reader group fails the trial build
          commitSecurityGroups(minus(securityGroups, sg -> sg.getName().equals(name)));
          ref.resolvedName = name;
        }
      }
      ref.mutationApplied = true;
    }

    // endregion

    // region non-removes

    private void applyNonRemove(Ref ref) {
      switch (ref.op) {
        case MODIFY -> applyModify(ref);
        case ADD -> applyAdd(ref);
        case MATCH, ADD_MATCH -> applyMatch(ref);
        default -> throw new IllegalStateException("unexpected op: " + ref.op);
      }
    }

    private void applyModify(Ref ref) {
      switch (ref.kind) {
        case CONNECTION -> {
          String name = requireBindingName(file.connections[ref.connectionIndex].getName());
          PubSubConnectionConfig existing = findConnection(name);
          if (existing == null) {
            throw noMatch("connection", name);
          }
          // full replacement, INCLUDING the child group arrays: the read-modify-write flow
          // uploads complete elements — modifying a connection with an empty groups array
          // deletes its groups
          PubSubConnectionConfig mapped =
              mapElement(
                  () ->
                      PubSubConfigElements.mapConnection(
                          file.connections[ref.connectionIndex], namespaceTable, securityGroups));
          replaceConnection(existing, mapped);
          recordConnection(ref, name);
        }
        case WRITER_GROUP -> {
          PubSubConnectionConfig parent = bindParentConnection(ref);
          WriterGroupDataType wire = file.writerGroups(ref.connectionIndex)[ref.groupIndex];
          String name = requireBindingName(wire.getName());
          WriterGroupConfig existing =
              parent.writerGroups().stream()
                  .filter(g -> g.getName().equals(name))
                  .findFirst()
                  .orElseThrow(() -> noMatch("writer group", name));
          UsedReservation introduced =
              checkIntroducedId(
                  IdKind.WRITER_GROUP, wire.getWriterGroupId(), existing.getWriterGroupId());
          WriterGroupConfig mapped =
              mapElement(
                  () -> PubSubConfigElements.mapWriterGroup(wire, namespaceTable, securityGroups));
          replaceWriterGroup(parent, existing, mapped);
          recordWriterGroup(ref, parent.name(), name);
          if (introduced != null) {
            consumed.add(introduced);
          }
        }
        case READER_GROUP -> {
          PubSubConnectionConfig parent = bindParentConnection(ref);
          ReaderGroupDataType wire = file.readerGroups(ref.connectionIndex)[ref.groupIndex];
          String name = requireBindingName(wire.getName());
          ReaderGroupConfig existing =
              parent.readerGroups().stream()
                  .filter(g -> g.getName().equals(name))
                  .findFirst()
                  .orElseThrow(() -> noMatch("reader group", name));
          ReaderGroupConfig mapped =
              mapElement(
                  () -> PubSubConfigElements.mapReaderGroup(wire, namespaceTable, securityGroups));
          replaceReaderGroup(parent, existing, mapped);
          recordReaderGroup(ref, parent.name(), name);
        }
        case DATA_SET_WRITER -> {
          PubSubConnectionConfig parent = bindParentConnection(ref);
          WriterGroupConfig group = bindParentWriterGroup(ref, parent);
          DataSetWriterDataType wire =
              file.dataSetWriters(ref.connectionIndex, ref.groupIndex)[ref.elementIndex];
          String name = requireBindingName(wire.getName());
          DataSetWriterConfig existing =
              group.getDataSetWriters().stream()
                  .filter(w -> w.getName().equals(name))
                  .findFirst()
                  .orElseThrow(() -> noMatch("dataset writer", name));
          UsedReservation introduced =
              checkIntroducedId(
                  IdKind.DATA_SET_WRITER, wire.getDataSetWriterId(), existing.getDataSetWriterId());
          DataSetWriterConfig mapped =
              mapElement(
                  () ->
                      PubSubConfigElements.mapDataSetWriter(wire, namespaceTable, securityGroups));
          List<DataSetWriterConfig> writers =
              replaceByName(group.getDataSetWriters(), name, DataSetWriterConfig::getName, mapped);
          replaceWriterGroup(parent, group, groupWithWriters(group, writers));
          ref.resolvedConnectionName = parent.name();
          ref.resolvedGroupName = group.getName();
          ref.resolvedName = name;
          if (introduced != null) {
            consumed.add(introduced);
          }
        }
        case DATA_SET_READER -> {
          PubSubConnectionConfig parent = bindParentConnection(ref);
          ReaderGroupConfig group = bindParentReaderGroup(ref, parent);
          DataSetReaderDataType wire =
              file.dataSetReaders(ref.connectionIndex, ref.groupIndex)[ref.elementIndex];
          String name = requireBindingName(wire.getName());
          if (group.getDataSetReaders().stream().noneMatch(r -> r.getName().equals(name))) {
            throw noMatch("dataset reader", name);
          }
          DataSetReaderConfig mapped =
              mapElement(
                  () ->
                      PubSubConfigElements.mapDataSetReader(wire, namespaceTable, securityGroups));
          List<DataSetReaderConfig> readers =
              replaceByName(group.getDataSetReaders(), name, DataSetReaderConfig::getName, mapped);
          replaceReaderGroup(parent, group, groupWithReaders(group, readers));
          ref.resolvedConnectionName = parent.name();
          ref.resolvedGroupName = group.getName();
          ref.resolvedName = name;
        }
        case PUBLISHED_DATA_SET -> {
          PublishedDataSetDataType wire = file.publishedDataSets[ref.elementIndex];
          String name = requireBindingName(wire.getName());
          if (publishedDataSets.stream().noneMatch(ds -> ds.getName().equals(name))) {
            throw noMatch("published dataset", name);
          }
          PublishedDataSetConfig mapped =
              mapElement(
                  () ->
                      PubSubConfigElements.mapPublishedDataSet(
                          wire, namespaceTable, securityGroups));
          commitPublishedDataSets(
              replaceByName(publishedDataSets, name, PublishedDataSetConfig::getName, mapped));
          ref.resolvedName = name;
        }
        case SUBSCRIBED_DATA_SET -> {
          StandaloneSubscribedDataSetDataType wire = file.subscribedDataSets[ref.elementIndex];
          String name = requireBindingName(wire.getName());
          if (standaloneSubscribedDataSets.stream().noneMatch(ds -> ds.getName().equals(name))) {
            throw noMatch("standalone subscribed dataset", name);
          }
          StandaloneSubscribedDataSetConfig mapped =
              mapElement(
                  () ->
                      PubSubConfigElements.mapStandaloneSubscribedDataSet(
                          wire, namespaceTable, securityGroups));
          commitStandaloneSubscribedDataSets(
              replaceByName(
                  standaloneSubscribedDataSets,
                  name,
                  StandaloneSubscribedDataSetConfig::getName,
                  mapped));
          ref.resolvedName = name;
        }
        case SECURITY_GROUP -> {
          SecurityGroupDataType wire = file.securityGroups[ref.elementIndex];
          String name = requireBindingName(wire.getName());
          if (securityGroups.stream().noneMatch(sg -> sg.getName().equals(name))) {
            throw noMatch("security group", name);
          }
          SecurityGroupConfig mapped =
              mapElement(() -> PubSubConfigElements.mapSecurityGroup(wire));
          commitSecurityGroups(
              replaceByName(securityGroups, name, SecurityGroupConfig::getName, mapped));
          ref.resolvedName = name;
        }
      }
      ref.mutationApplied = true;
    }

    private void applyAdd(Ref ref) {
      switch (ref.kind) {
        case CONNECTION -> {
          PubSubConnectionDataType wire = file.connections[ref.connectionIndex];

          String name = wire.getName();
          boolean nameAssigned = nullOrEmpty(name);
          if (nameAssigned) {
            name = uniqueName("Connection", names(connections, PubSubConnectionConfig::name));
          } else if (findConnection(name) != null) {
            throw duplicated("connection", name);
          }

          Variant publisherId = wire.getPublisherId();
          boolean idAssigned = false;
          Object assignedId = null;
          if ((publisherId == null || publisherId.isNull())
              && PubSubIdReservations.isSupportedProfile(wire.getTransportProfileUri())) {
            try {
              assignedId = reservations.defaultPublisherId(wire.getTransportProfileUri());
            } catch (UaException e) {
              throw new RefFailedException(e.getStatusCode(), e.getMessage());
            }
            idAssigned = true;
            publisherId =
                assignedId instanceof String s ? Variant.ofString(s) : new Variant(assignedId);
          }

          PubSubConnectionDataType patched = withConnectionIdentity(wire, name, publisherId);
          PubSubConnectionConfig mapped =
              mapElement(
                  () ->
                      PubSubConfigElements.mapConnection(patched, namespaceTable, securityGroups));

          commitConnections(plus(connections, mapped));
          recordConnection(ref, name);
          reportValue(ref, nameAssigned || idAssigned, publisherIdVariant(mapped.publisherId()));
        }
        case WRITER_GROUP -> {
          PubSubConnectionConfig parent = bindParentConnection(ref);
          WriterGroupDataType wire = file.writerGroups(ref.connectionIndex)[ref.groupIndex];

          Set<String> groupScope = groupNamesOf(parent);
          String name = wire.getName();
          boolean nameAssigned = nullOrEmpty(name);
          if (nameAssigned) {
            name = uniqueName("WriterGroup", groupScope);
          } else if (groupScope.contains(name)) {
            throw duplicated("writer group", name);
          }

          boolean json = wire.getMessageSettings() instanceof JsonWriterGroupMessageDataType;
          IdAssignment id =
              assignId(
                  IdKind.WRITER_GROUP, wire.getWriterGroupId(), transportProfile(parent, json));

          WriterGroupDataType patched = withWriterGroupIdentity(wire, name, id.id());
          WriterGroupConfig mapped =
              mapElement(
                  () ->
                      PubSubConfigElements.mapWriterGroup(patched, namespaceTable, securityGroups));

          replaceConnection(
              parent,
              connectionWithGroups(
                  parent, plus(parent.writerGroups(), mapped), parent.readerGroups()));
          recordWriterGroup(ref, parent.name(), name);
          id.consume(consumed);
          reportValue(ref, nameAssigned || id.assigned(), Variant.ofUInt16(id.id()));
        }
        case READER_GROUP -> {
          PubSubConnectionConfig parent = bindParentConnection(ref);
          ReaderGroupDataType wire = file.readerGroups(ref.connectionIndex)[ref.groupIndex];

          Set<String> groupScope = groupNamesOf(parent);
          String name = wire.getName();
          boolean nameAssigned = nullOrEmpty(name);
          if (nameAssigned) {
            name = uniqueName("ReaderGroup", groupScope);
          } else if (groupScope.contains(name)) {
            throw duplicated("reader group", name);
          }

          ReaderGroupDataType patched = withReaderGroupName(wire, name);
          ReaderGroupConfig mapped =
              mapElement(
                  () ->
                      PubSubConfigElements.mapReaderGroup(patched, namespaceTable, securityGroups));

          replaceConnection(
              parent,
              connectionWithGroups(
                  parent, parent.writerGroups(), plus(parent.readerGroups(), mapped)));
          recordReaderGroup(ref, parent.name(), name);
          reportValue(ref, nameAssigned, Variant.NULL_VALUE);
        }
        case DATA_SET_WRITER -> {
          PubSubConnectionConfig parent = bindParentConnection(ref);
          WriterGroupConfig group = bindParentWriterGroup(ref, parent);
          DataSetWriterDataType wire =
              file.dataSetWriters(ref.connectionIndex, ref.groupIndex)[ref.elementIndex];

          Set<String> writerScope = names(group.getDataSetWriters(), DataSetWriterConfig::getName);
          String name = wire.getName();
          boolean nameAssigned = nullOrEmpty(name);
          if (nameAssigned) {
            name = uniqueName("DataSetWriter", writerScope);
          } else if (writerScope.contains(name)) {
            throw duplicated("dataset writer", name);
          }

          boolean json = group.getMessageSettings() instanceof JsonWriterGroupSettings;
          IdAssignment id =
              assignId(
                  IdKind.DATA_SET_WRITER,
                  wire.getDataSetWriterId(),
                  transportProfile(parent, json));

          DataSetWriterDataType patched = withDataSetWriterIdentity(wire, name, id.id());
          DataSetWriterConfig mapped =
              mapElement(
                  () ->
                      PubSubConfigElements.mapDataSetWriter(
                          patched, namespaceTable, securityGroups));

          replaceWriterGroup(
              parent, group, groupWithWriters(group, plus(group.getDataSetWriters(), mapped)));
          ref.resolvedConnectionName = parent.name();
          ref.resolvedGroupName = group.getName();
          ref.resolvedName = name;
          id.consume(consumed);
          reportValue(ref, nameAssigned || id.assigned(), Variant.ofUInt16(id.id()));
        }
        case DATA_SET_READER -> {
          PubSubConnectionConfig parent = bindParentConnection(ref);
          ReaderGroupConfig group = bindParentReaderGroup(ref, parent);
          DataSetReaderDataType wire =
              file.dataSetReaders(ref.connectionIndex, ref.groupIndex)[ref.elementIndex];

          Set<String> readerScope = names(group.getDataSetReaders(), DataSetReaderConfig::getName);
          String name = wire.getName();
          boolean nameAssigned = nullOrEmpty(name);
          if (nameAssigned) {
            name = uniqueName("DataSetReader", readerScope);
          } else if (readerScope.contains(name)) {
            throw duplicated("dataset reader", name);
          }

          // a reader's WriterGroupId/DataSetWriterId identify the REMOTE publisher's components
          // and are never auto-assigned
          DataSetReaderDataType patched = withDataSetReaderName(wire, name);
          DataSetReaderConfig mapped =
              mapElement(
                  () ->
                      PubSubConfigElements.mapDataSetReader(
                          patched, namespaceTable, securityGroups));

          replaceReaderGroup(
              parent, group, groupWithReaders(group, plus(group.getDataSetReaders(), mapped)));
          ref.resolvedConnectionName = parent.name();
          ref.resolvedGroupName = group.getName();
          ref.resolvedName = name;
          reportValue(ref, nameAssigned, Variant.NULL_VALUE);
        }
        case PUBLISHED_DATA_SET -> {
          PublishedDataSetDataType wire = file.publishedDataSets[ref.elementIndex];

          Set<String> scope = names(publishedDataSets, PublishedDataSetConfig::getName);
          String name = wire.getName();
          boolean nameAssigned = nullOrEmpty(name);
          if (nameAssigned) {
            name = uniqueName("PublishedDataSet", scope);
          } else if (scope.contains(name)) {
            throw duplicated("published dataset", name);
          }

          PublishedDataSetDataType patched = withPublishedDataSetName(wire, name);
          PublishedDataSetConfig mapped =
              mapElement(
                  () ->
                      PubSubConfigElements.mapPublishedDataSet(
                          patched, namespaceTable, securityGroups));

          commitPublishedDataSets(plus(publishedDataSets, mapped));
          ref.resolvedName = name;
          reportValue(ref, nameAssigned, Variant.NULL_VALUE);
        }
        case SUBSCRIBED_DATA_SET -> {
          StandaloneSubscribedDataSetDataType wire = file.subscribedDataSets[ref.elementIndex];

          Set<String> scope =
              names(standaloneSubscribedDataSets, StandaloneSubscribedDataSetConfig::getName);
          String name = wire.getName();
          boolean nameAssigned = nullOrEmpty(name);
          if (nameAssigned) {
            name = uniqueName("SubscribedDataSet", scope);
          } else if (scope.contains(name)) {
            throw duplicated("standalone subscribed dataset", name);
          }

          StandaloneSubscribedDataSetDataType patched = withSubscribedDataSetName(wire, name);
          StandaloneSubscribedDataSetConfig mapped =
              mapElement(
                  () ->
                      PubSubConfigElements.mapStandaloneSubscribedDataSet(
                          patched, namespaceTable, securityGroups));

          commitStandaloneSubscribedDataSets(plus(standaloneSubscribedDataSets, mapped));
          ref.resolvedName = name;
          reportValue(ref, nameAssigned, Variant.NULL_VALUE);
        }
        case SECURITY_GROUP -> {
          SecurityGroupDataType wire = file.securityGroups[ref.elementIndex];

          Set<String> scope = names(securityGroups, SecurityGroupConfig::getName);
          String name = wire.getName();
          boolean nameAssigned = nullOrEmpty(name);
          if (nameAssigned) {
            name = uniqueName("SecurityGroup", scope);
          } else if (scope.contains(name)) {
            throw duplicated("security group", name);
          }

          SecurityGroupDataType patched = withSecurityGroupName(wire, name);
          SecurityGroupConfig mapped =
              mapElement(() -> PubSubConfigElements.mapSecurityGroup(patched));

          commitSecurityGroups(plus(securityGroups, mapped));
          ref.resolvedName = name;
          reportValue(ref, nameAssigned, Variant.NULL_VALUE);
        }
      }
      ref.mutationApplied = true;
      // any value reported above is an Add-side ASSIGNMENT — speculative until the apply
      // commits — unlike a Match resolution of an element that already exists
      ref.assignedByAdd = ref.reportValue;
    }

    private void applyMatch(Ref ref) {
      switch (ref.kind) {
        case CONNECTION -> {
          PubSubConnectionDataType wire = file.connections[ref.connectionIndex];
          PubSubConnectionConfig pattern =
              mapElement(
                  () ->
                      PubSubConfigElements.mapConnection(
                          sanitizedConnectionPattern(wire), namespaceTable, securityGroups));

          PubSubConnectionConfig matched =
              connections.stream()
                  .filter(candidate -> connectionMatches(candidate, pattern))
                  .findFirst()
                  .orElse(null);

          if (matched == null) {
            if (ref.op == Op.ADD_MATCH) {
              applyAdd(ref);
              return;
            }
            throw noMatch("connection", "<match>");
          }

          recordConnection(ref, matched.name());
          reportValue(ref, true, publisherIdVariant(matched.publisherId()));
        }
        case WRITER_GROUP -> {
          PubSubConnectionConfig parent = bindParentConnection(ref);
          WriterGroupDataType wire = file.writerGroups(ref.connectionIndex)[ref.groupIndex];

          WriterGroupConfig matched;
          if (wire.getHeaderLayoutUri() != null && !wire.getHeaderLayoutUri().isEmpty()) {
            // HeaderLayoutUri is IN the fixed CloseAndUpdate field set, but the config model
            // cannot host a header layout (mapper-dropped), so a live group never carries
            // one: a pattern demanding a non-empty HeaderLayoutUri differs in a compared
            // field and never matches. Checked on the wire element — the config-mapped
            // comparison below cannot see the field.
            matched = null;
          } else {
            WriterGroupConfig pattern =
                mapElement(
                    () ->
                        PubSubConfigElements.mapWriterGroup(
                            sanitizedWriterGroupPattern(wire), namespaceTable, securityGroups));

            matched =
                parent.writerGroups().stream()
                    .filter(candidate -> writerGroupMatches(candidate, pattern))
                    .findFirst()
                    .orElse(null);
          }

          if (matched == null) {
            if (ref.op == Op.ADD_MATCH) {
              applyAdd(ref);
              return;
            }
            throw noMatch("writer group", "<match>");
          }

          // "Match applied to ReferenceWriterGroup shall return Bad_InvalidState if the
          // GroupHeader is active for the WriterGroup" — the UADP NetworkMessageContentMask
          // GroupHeader bit, a config-only predicate; JSON groups can never trigger it
          if (matched.getMessageSettings() instanceof UadpWriterGroupSettings uadp
              && uadp.getNetworkMessageContentMask().getGroupHeader()) {
            throw new RefFailedException(
                StatusCodes.Bad_InvalidState,
                "the GroupHeader is active for writer group '%s'".formatted(matched.getName()));
          }

          recordWriterGroup(ref, parent.name(), matched.getName());
          reportValue(ref, true, Variant.ofUInt16(matched.getWriterGroupId()));
        }
        case READER_GROUP -> {
          PubSubConnectionConfig parent = bindParentConnection(ref);
          ReaderGroupDataType wire = file.readerGroups(ref.connectionIndex)[ref.groupIndex];
          ReaderGroupConfig pattern =
              mapElement(
                  () ->
                      PubSubConfigElements.mapReaderGroup(
                          sanitizedReaderGroupPattern(wire), namespaceTable, securityGroups));

          ReaderGroupConfig matched =
              parent.readerGroups().stream()
                  .filter(candidate -> readerGroupMatches(candidate, pattern))
                  .findFirst()
                  .orElse(null);

          if (matched == null) {
            if (ref.op == Op.ADD_MATCH) {
              applyAdd(ref);
              return;
            }
            throw noMatch("reader group", "<match>");
          }

          recordReaderGroup(ref, parent.name(), matched.getName());
          reportValue(ref, true, Variant.NULL_VALUE);
        }
        default -> throw new IllegalStateException("unreachable: " + ref.kind);
      }
    }

    // endregion

    // region parent binding

    private PubSubConnectionConfig bindParentConnection(Ref ref) {
      int ci = ref.connectionIndex;
      if (failedConnections.contains(ci)) {
        throw parentNotFound("connection reference " + ci);
      }
      String name = connectionNames.get(ci);
      if (name == null) {
        name = file.connections[ci].getName();
      }
      if (name == null) {
        throw parentNotFound("connection reference " + ci + " has no name");
      }
      PubSubConnectionConfig connection = findConnection(name);
      if (connection == null) {
        throw parentNotFound("connection '" + name + "'");
      }
      return connection;
    }

    private WriterGroupConfig bindParentWriterGroup(Ref ref, PubSubConnectionConfig parent) {
      long key = groupKey(true, ref.connectionIndex, ref.groupIndex);
      if (failedGroups.contains(key)) {
        throw parentNotFound("writer group reference " + ref.groupIndex);
      }
      String name = groupNames.get(key);
      if (name == null) {
        name = file.writerGroups(ref.connectionIndex)[ref.groupIndex].getName();
      }
      if (name == null) {
        throw parentNotFound("writer group reference " + ref.groupIndex + " has no name");
      }
      String groupName = name;
      return parent.writerGroups().stream()
          .filter(g -> g.getName().equals(groupName))
          .findFirst()
          .orElseThrow(() -> parentNotFound("writer group '" + groupName + "'"));
    }

    private ReaderGroupConfig bindParentReaderGroup(Ref ref, PubSubConnectionConfig parent) {
      long key = groupKey(false, ref.connectionIndex, ref.groupIndex);
      if (failedGroups.contains(key)) {
        throw parentNotFound("reader group reference " + ref.groupIndex);
      }
      String name = groupNames.get(key);
      if (name == null) {
        name = file.readerGroups(ref.connectionIndex)[ref.groupIndex].getName();
      }
      if (name == null) {
        throw parentNotFound("reader group reference " + ref.groupIndex + " has no name");
      }
      String groupName = name;
      return parent.readerGroups().stream()
          .filter(g -> g.getName().equals(groupName))
          .findFirst()
          .orElseThrow(() -> parentNotFound("reader group '" + groupName + "'"));
    }

    private void recordConnection(Ref ref, String name) {
      ref.resolvedName = name;
      connectionNames.put(ref.connectionIndex, name);
    }

    private void recordWriterGroup(Ref ref, String connectionName, String name) {
      ref.resolvedConnectionName = connectionName;
      ref.resolvedName = name;
      connectionNames.putIfAbsent(ref.connectionIndex, connectionName);
      groupNames.put(groupKey(true, ref.connectionIndex, ref.groupIndex), name);
    }

    private void recordReaderGroup(Ref ref, String connectionName, String name) {
      ref.resolvedConnectionName = connectionName;
      ref.resolvedName = name;
      connectionNames.putIfAbsent(ref.connectionIndex, connectionName);
      groupNames.put(groupKey(false, ref.connectionIndex, ref.groupIndex), name);
    }

    // endregion

    // region id assignment

    private IdAssignment assignId(IdKind kind, @Nullable UShort wireId, String transportProfile) {
      int idValue = wireId == null ? 0 : wireId.intValue();

      if (idValue == 0) {
        // wire 0 reads as null: allocate from 0x8000-0xFFFF, preferring the calling session's
        // outstanding reservations for the element's transport profile
        Set<UShort> used = usedIdsInWorking(kind);
        for (UShort candidate : reservations.outstanding(sessionId, transportProfile, kind)) {
          if (!used.contains(candidate)) {
            return new IdAssignment(candidate, true, new UsedReservation(kind, candidate));
          }
        }
        UShort allocated = reservations.allocateUnreserved(kind, used);
        if (allocated == null) {
          throw new RefFailedException(
              StatusCodes.Bad_ResourceUnavailable,
              "the 0x8000-0xFFFF %s id space is exhausted".formatted(kind));
        }
        return new IdAssignment(allocated, true, null);
      }

      if (idValue >= PubSubIdReservations.MIN_SERVER_ASSIGNED_ID) {
        if (reservations.isReservedByOtherSession(sessionId, kind, wireId)) {
          // reservations are cross-session exclusive (§9.1.3.7.5)
          throw invalidRef("%s id %d is reserved by another session".formatted(kind, idValue));
        }
        if (reservations.isReservedBySession(sessionId, kind, wireId)) {
          return new IdAssignment(wireId, false, new UsedReservation(kind, wireId));
        }
      }

      // 0x0001-0x7FFF (the external tool range) is always accepted; uniqueness is enforced by
      // the trial build's publisher-scope validation
      return new IdAssignment(wireId, false, null);
    }

    /**
     * The Modify-side counterpart of {@link #assignId}'s explicit-id rule, applied to the
     * referenced element's own wire id: a Modify that INTRODUCES a server-range id (0x8000–0xFFFF)
     * the bound live element does not already carry is rejected per-element when that id is
     * reserved by another session — reservations are cross-session exclusive (§9.1.3.7.5), and
     * without this guard the foreign grant would silently go live, deadening the reservation until
     * its session closes. An id the calling session itself reserved is accepted and its reservation
     * returned for consumption (recorded only after the arm's commit succeeded, mirroring the Add
     * flow). An unchanged id, a wire null/0, or an id below 0x8000 needs no reservation interplay.
     */
    private @Nullable UsedReservation checkIntroducedId(
        IdKind kind, @Nullable UShort wireId, UShort liveId) {

      int idValue = wireId == null ? 0 : wireId.intValue();
      if (idValue < PubSubIdReservations.MIN_SERVER_ASSIGNED_ID || wireId.equals(liveId)) {
        return null;
      }
      if (reservations.isReservedByOtherSession(sessionId, kind, wireId)) {
        // reservations are cross-session exclusive (§9.1.3.7.5)
        throw invalidRef("%s id %d is reserved by another session".formatted(kind, idValue));
      }
      if (reservations.isReservedBySession(sessionId, kind, wireId)) {
        return new UsedReservation(kind, wireId);
      }
      return null;
    }

    private Set<UShort> usedIdsInWorking(IdKind kind) {
      var ids = new HashSet<UShort>();
      for (PubSubConnectionConfig connection : connections) {
        for (WriterGroupConfig group : connection.writerGroups()) {
          if (kind == IdKind.WRITER_GROUP) {
            ids.add(group.getWriterGroupId());
          } else {
            for (DataSetWriterConfig writer : group.getDataSetWriters()) {
              ids.add(writer.getDataSetWriterId());
            }
          }
        }
      }
      return ids;
    }

    // endregion

    // region commits (trial build + revert-on-failure)

    private @Nullable PubSubConnectionConfig findConnection(String name) {
      return connections.stream().filter(c -> c.name().equals(name)).findFirst().orElse(null);
    }

    private void replaceConnection(
        PubSubConnectionConfig existing, PubSubConnectionConfig replacement) {
      var updated = new ArrayList<>(connections);
      updated.set(updated.indexOf(existing), replacement);
      commitConnections(List.copyOf(updated));
    }

    private void replaceWriterGroup(
        PubSubConnectionConfig parent, WriterGroupConfig existing, WriterGroupConfig replacement) {
      List<WriterGroupConfig> groups =
          replaceByName(
              parent.writerGroups(), existing.getName(), WriterGroupConfig::getName, replacement);
      replaceConnection(parent, connectionWithGroups(parent, groups, parent.readerGroups()));
    }

    private void replaceReaderGroup(
        PubSubConnectionConfig parent, ReaderGroupConfig existing, ReaderGroupConfig replacement) {
      List<ReaderGroupConfig> groups =
          replaceByName(
              parent.readerGroups(), existing.getName(), ReaderGroupConfig::getName, replacement);
      replaceConnection(parent, connectionWithGroups(parent, parent.writerGroups(), groups));
    }

    private void commitConnections(List<PubSubConnectionConfig> newConnections) {
      PubSubConfig trial =
          buildTrial(
              newConnections, publishedDataSets, standaloneSubscribedDataSets, securityGroups);
      connections = newConnections;
      working = trial;
    }

    private void commitPublishedDataSets(List<PublishedDataSetConfig> newDataSets) {
      PubSubConfig trial =
          buildTrial(connections, newDataSets, standaloneSubscribedDataSets, securityGroups);
      publishedDataSets = newDataSets;
      working = trial;
    }

    private void commitStandaloneSubscribedDataSets(
        List<StandaloneSubscribedDataSetConfig> newDataSets) {
      PubSubConfig trial = buildTrial(connections, publishedDataSets, newDataSets, securityGroups);
      standaloneSubscribedDataSets = newDataSets;
      working = trial;
    }

    private void commitSecurityGroups(List<SecurityGroupConfig> newGroups) {
      PubSubConfig trial =
          buildTrial(connections, publishedDataSets, standaloneSubscribedDataSets, newGroups);
      securityGroups = newGroups;
      working = trial;
    }

    /**
     * Trial-build the working configuration (cross-validation: name/wire-id uniqueness, reference
     * resolution); a validation failure fails the current reference and the working state stays
     * last-good. Configs are small; N trial builds are cheap.
     */
    private PubSubConfig buildTrial(
        List<PubSubConnectionConfig> connections,
        List<PublishedDataSetConfig> publishedDataSets,
        List<StandaloneSubscribedDataSetConfig> standaloneSubscribedDataSets,
        List<SecurityGroupConfig> securityGroups) {

      try {
        return buildConfig(
            enabled,
            connections,
            publishedDataSets,
            standaloneSubscribedDataSets,
            securityGroups,
            defaultSecurityKeyServices,
            properties);
      } catch (PubSubConfigValidationException e) {
        String message = e.getMessage() == null ? "" : e.getMessage();
        // only NAME duplication is Bad_BrowseNameDuplicated ("An element with the name already
        // exists"); wire-id collisions ("duplicate writerGroupId/dataSetWriterId ...") — like
        // every other validation failure — are the contract's catch-all Bad_InvalidArgument
        throw new RefFailedException(
            isNameDuplicationMessage(message)
                ? StatusCodes.Bad_BrowseNameDuplicated
                : StatusCodes.Bad_InvalidArgument,
            message);
      }
    }

    /**
     * Apply the top-level file fields ([CU §7.1]) and return the final working configuration:
     * Enable, DataSetClasses, and ConfigurationVersion are ignored; DefaultSecurityKeyServices is
     * replaced if non-empty; ConfigurationProperties are merged (a null value deletes its key,
     * absent keys are untouched).
     */
    private PubSubConfig applyTopLevel(PubSubConfiguration2DataType file) {
      EndpointDescription[] keyServices = file.getDefaultSecurityKeyServices();
      if (keyServices != null && keyServices.length > 0) {
        defaultSecurityKeyServices = Arrays.stream(keyServices).filter(Objects::nonNull).toList();
      }

      KeyValuePair[] configurationProperties = file.getConfigurationProperties();
      if (configurationProperties != null) {
        for (KeyValuePair property : configurationProperties) {
          if (property == null || property.getKey() == null) {
            continue;
          }
          Variant value = property.getValue();
          if (value == null || value.isNull()) {
            properties.remove(property.getKey());
          } else {
            properties.put(property.getKey(), value);
          }
        }
      }

      working =
          buildConfig(
              enabled,
              connections,
              publishedDataSets,
              standaloneSubscribedDataSets,
              securityGroups,
              defaultSecurityKeyServices,
              properties);
      return working;
    }

    // endregion
  }

  // endregion

  // region outputs

  private static PubSubConfigurationValueDataType[] configurationValues(
      Ref[] refs, StatusCode[] results, boolean changesApplied) {

    var values = new ArrayList<PubSubConfigurationValueDataType>();
    for (Ref ref : refs) {
      // an Add-side assignment is reported only if the apply committed ([CU §7.2] "where a
      // name and identifier was assigned"): when the update aborted, nothing was assigned —
      // the element does not exist and its reservation was not consumed. Match resolutions
      // name elements that exist regardless of the abort and are always reported.
      if (ref.reportValue
          && results[ref.index].isGood()
          && (changesApplied || !ref.assignedByAdd)) {
        values.add(
            new PubSubConfigurationValueDataType(
                ref.wire,
                ref.resolvedName,
                ref.identifier != null ? ref.identifier : Variant.NULL_VALUE));
      }
    }
    return values.toArray(new PubSubConfigurationValueDataType[0]);
  }

  private NodeId[] configurationObjects(Ref[] refs, StatusCode[] results) {
    ConfigurationObjectIds objectIds = configurationObjectIds;
    if (objectIds == null) {
      // the information-model lookup is absent: the spec's "null or empty" opt-out arm
      return new NodeId[0];
    }

    var ids = new NodeId[refs.length];
    Arrays.fill(ids, NodeId.NULL_VALUE);

    for (Ref ref : refs) {
      if (!results[ref.index].isGood() || ref.op == Op.REMOVE || ref.resolvedName == null) {
        continue;
      }
      NodeId id =
          switch (ref.kind) {
            case CONNECTION -> objectIds.connectionObjectId(ref.resolvedName);
            case WRITER_GROUP ->
                ref.resolvedConnectionName != null
                    ? objectIds.writerGroupObjectId(ref.resolvedConnectionName, ref.resolvedName)
                    : null;
            case READER_GROUP ->
                ref.resolvedConnectionName != null
                    ? objectIds.readerGroupObjectId(ref.resolvedConnectionName, ref.resolvedName)
                    : null;
            case DATA_SET_WRITER ->
                ref.resolvedConnectionName != null && ref.resolvedGroupName != null
                    ? objectIds.dataSetWriterObjectId(
                        ref.resolvedConnectionName, ref.resolvedGroupName, ref.resolvedName)
                    : null;
            case DATA_SET_READER ->
                ref.resolvedConnectionName != null && ref.resolvedGroupName != null
                    ? objectIds.dataSetReaderObjectId(
                        ref.resolvedConnectionName, ref.resolvedGroupName, ref.resolvedName)
                    : null;
            case PUBLISHED_DATA_SET -> objectIds.publishedDataSetObjectId(ref.resolvedName);
            // never materialized by the fragment: NULL_VALUE slots
            case SUBSCRIBED_DATA_SET, SECURITY_GROUP -> null;
          };
      if (id != null) {
        ids[ref.index] = id;
      }
    }

    return ids;
  }

  // endregion

  // region config surgery helpers

  private static PubSubConfig buildConfig(
      boolean enabled,
      List<PubSubConnectionConfig> connections,
      List<PublishedDataSetConfig> publishedDataSets,
      List<StandaloneSubscribedDataSetConfig> standaloneSubscribedDataSets,
      List<SecurityGroupConfig> securityGroups,
      List<EndpointDescription> defaultSecurityKeyServices,
      Map<QualifiedName, Variant> properties) {

    PubSubConfig.Builder builder = PubSubConfig.builder().enabled(enabled);
    connections.forEach(builder::connection);
    publishedDataSets.forEach(builder::publishedDataSet);
    standaloneSubscribedDataSets.forEach(builder::standaloneSubscribedDataSet);
    securityGroups.forEach(builder::securityGroup);
    defaultSecurityKeyServices.forEach(builder::defaultSecurityKeyService);
    properties.forEach(builder::property);
    return builder.build();
  }

  /**
   * Rebuild {@code connection} with the given group lists, copying every scalar field; untouched
   * groups are reused object-identical.
   */
  private static PubSubConnectionConfig connectionWithGroups(
      PubSubConnectionConfig connection,
      List<WriterGroupConfig> writerGroups,
      List<ReaderGroupConfig> readerGroups) {

    if (connection instanceof UdpConnectionConfig udp) {
      UdpConnectionConfig.Builder builder =
          UdpConnectionConfig.builder(udp.name()).enabled(udp.enabled()).address(udp.getAddress());
      if (udp.publisherId() != null) {
        builder.publisherId(udp.publisherId());
      }
      if (udp.getDiscoveryAddress() != null) {
        builder.discoveryAddress(udp.getDiscoveryAddress());
      }
      if (udp.rawTransportSettings() != null) {
        builder.rawTransportSettings(udp.rawTransportSettings());
      }
      udp.properties().forEach(builder::property);
      writerGroups.forEach(builder::writerGroup);
      readerGroups.forEach(builder::readerGroup);
      return builder.build();
    } else if (connection instanceof MqttConnectionConfig mqtt) {
      MqttConnectionConfig.Builder builder =
          MqttConnectionConfig.builder(mqtt.name())
              .enabled(mqtt.enabled())
              .brokerUri(mqtt.getBrokerUri());
      if (mqtt.publisherId() != null) {
        builder.publisherId(mqtt.publisherId());
      }
      if (mqtt.getBrokerSecurity() != null) {
        builder.brokerSecurity(mqtt.getBrokerSecurity());
      }
      if (mqtt.rawTransportSettings() != null) {
        builder.rawTransportSettings(mqtt.rawTransportSettings());
      }
      mqtt.properties().forEach(builder::property);
      writerGroups.forEach(builder::writerGroup);
      readerGroups.forEach(builder::readerGroup);
      return builder.build();
    } else {
      throw invalidRef("unsupported connection type: " + connection.getClass().getName());
    }
  }

  /** Rebuild {@code group} with {@code writers}, copying every scalar field. */
  private static WriterGroupConfig groupWithWriters(
      WriterGroupConfig group, List<DataSetWriterConfig> writers) {

    WriterGroupConfig.Builder builder =
        WriterGroupConfig.builder(group.getName())
            .enabled(group.isEnabled())
            .writerGroupId(group.getWriterGroupId())
            .publishingInterval(group.getPublishingInterval())
            .priority(group.getPriority())
            .maxNetworkMessageSize(group.getMaxNetworkMessageSize())
            .messageSettings(group.getMessageSettings());
    if (group.getKeepAliveTime() != null) {
      builder.keepAliveTime(group.getKeepAliveTime());
    }
    if (group.getMessageSecurity() != null) {
      builder.messageSecurity(group.getMessageSecurity());
    }
    if (group.getBrokerTransport() != null) {
      builder.brokerTransport(group.getBrokerTransport());
    }
    if (group.getRawTransportSettings() != null) {
      builder.rawTransportSettings(group.getRawTransportSettings());
    }
    if (group.getRawMessageSettings() != null) {
      builder.rawMessageSettings(group.getRawMessageSettings());
    }
    group.getProperties().forEach(builder::property);
    writers.forEach(builder::dataSetWriter);
    return builder.build();
  }

  /** Rebuild {@code group} with {@code readers}, copying every scalar field. */
  private static ReaderGroupConfig groupWithReaders(
      ReaderGroupConfig group, List<DataSetReaderConfig> readers) {

    ReaderGroupConfig.Builder builder =
        ReaderGroupConfig.builder(group.getName())
            .enabled(group.isEnabled())
            .maxNetworkMessageSize(group.getMaxNetworkMessageSize());
    if (group.getMessageSecurity() != null) {
      builder.messageSecurity(group.getMessageSecurity());
    }
    if (group.getRawTransportSettings() != null) {
      builder.rawTransportSettings(group.getRawTransportSettings());
    }
    if (group.getRawMessageSettings() != null) {
      builder.rawMessageSettings(group.getRawMessageSettings());
    }
    group.getProperties().forEach(builder::property);
    readers.forEach(builder::dataSetReader);
    return builder.build();
  }

  // endregion

  // region match comparison (config-mapped forms, both sides)

  /**
   * The connection match field set: TransportProfileUri (the config class distinguishes datagram
   * from broker; the UADP/JSON broker split is a per-group property with no connection-level
   * counterpart), Address, TransportSettings (the datagram discovery address and the raw escape
   * hatch), and the property keys present in the file element.
   */
  private static boolean connectionMatches(
      PubSubConnectionConfig candidate, PubSubConnectionConfig pattern) {

    if (candidate instanceof UdpConnectionConfig live && pattern instanceof UdpConnectionConfig p) {
      return Objects.equals(live.getAddress(), p.getAddress())
          && Objects.equals(live.getDiscoveryAddress(), p.getDiscoveryAddress())
          && Objects.equals(live.rawTransportSettings(), p.rawTransportSettings())
          && propertiesSubset(p.properties(), live.properties());
    } else if (candidate instanceof MqttConnectionConfig live
        && pattern instanceof MqttConnectionConfig p) {
      return Objects.equals(live.getBrokerUri(), p.getBrokerUri())
          && Objects.equals(live.rawTransportSettings(), p.rawTransportSettings())
          && propertiesSubset(p.properties(), live.properties());
    }
    return false;
  }

  /**
   * The writer group match field set: SecurityMode/SecurityGroupId/SecurityKeyServices,
   * MaxNetworkMessageSize, PublishingInterval, KeepAliveTime, Priority, TransportSettings, and
   * MessageSettings, plus the property keys present in the file element. HeaderLayoutUri — also in
   * the match set — has no config counterpart and cannot be compared here; the match arm enforces
   * it on the wire element instead (a non-empty pattern HeaderLayoutUri never matches, because a
   * live Milo group can never carry a layout). The local-only security-policy-URI override and the
   * Milo-local group-level broker metadata fields are excluded (the wire cannot carry them).
   */
  private static boolean writerGroupMatches(
      WriterGroupConfig candidate, WriterGroupConfig pattern) {
    return securityMatches(candidate.getMessageSecurity(), pattern.getMessageSecurity())
        && candidate.getMaxNetworkMessageSize().equals(pattern.getMaxNetworkMessageSize())
        && candidate.getPublishingInterval().equals(pattern.getPublishingInterval())
        && Objects.equals(candidate.getKeepAliveTime(), pattern.getKeepAliveTime())
        && candidate.getPriority().equals(pattern.getPriority())
        && Objects.equals(candidate.getMessageSettings(), pattern.getMessageSettings())
        && brokerTransportMatches(candidate.getBrokerTransport(), pattern.getBrokerTransport())
        && Objects.equals(candidate.getRawTransportSettings(), pattern.getRawTransportSettings())
        && Objects.equals(candidate.getRawMessageSettings(), pattern.getRawMessageSettings())
        && propertiesSubset(pattern.getProperties(), candidate.getProperties());
  }

  /**
   * The reader group match field set: SecurityMode/SecurityGroupId/SecurityKeyServices,
   * MaxNetworkMessageSize, TransportSettings, and MessageSettings (both raw escape hatches; the
   * config model has no typed reader-group settings), plus the property keys present in the file
   * element.
   */
  private static boolean readerGroupMatches(
      ReaderGroupConfig candidate, ReaderGroupConfig pattern) {
    return securityMatches(candidate.getMessageSecurity(), pattern.getMessageSecurity())
        && candidate.getMaxNetworkMessageSize().equals(pattern.getMaxNetworkMessageSize())
        && Objects.equals(candidate.getRawTransportSettings(), pattern.getRawTransportSettings())
        && Objects.equals(candidate.getRawMessageSettings(), pattern.getRawMessageSettings())
        && propertiesSubset(pattern.getProperties(), candidate.getProperties());
  }

  /**
   * Compares the wire-visible components of a writer group's broker transport settings (QueueName,
   * ResourceUri, AuthenticationProfileUri, RequestedDeliveryGuarantee), normalizing absent settings
   * to the defaults; the Milo-local group-level metadata fields
   * (metaDataQueueName/metaDataUpdateTime) are excluded — {@code
   * BrokerWriterGroupTransportDataType} cannot carry them, so the file-mapped pattern always has
   * them unset (both sides must share one normalization).
   */
  private static boolean brokerTransportMatches(
      @Nullable BrokerTransportSettings candidate, @Nullable BrokerTransportSettings pattern) {

    return Objects.equals(queueNameOf(candidate), queueNameOf(pattern))
        && Objects.equals(resourceUriOf(candidate), resourceUriOf(pattern))
        && Objects.equals(
            authenticationProfileUriOf(candidate), authenticationProfileUriOf(pattern))
        && deliveryGuaranteeOf(candidate) == deliveryGuaranteeOf(pattern);
  }

  private static @Nullable String queueNameOf(@Nullable BrokerTransportSettings settings) {
    return settings == null ? null : settings.getQueueName();
  }

  private static @Nullable String resourceUriOf(@Nullable BrokerTransportSettings settings) {
    return settings == null ? null : settings.getResourceUri();
  }

  private static @Nullable String authenticationProfileUriOf(
      @Nullable BrokerTransportSettings settings) {
    return settings == null ? null : settings.getAuthenticationProfileUri();
  }

  private static BrokerTransportQualityOfService deliveryGuaranteeOf(
      @Nullable BrokerTransportSettings settings) {
    return settings == null
        ? BrokerTransportQualityOfService.NotSpecified
        : settings.getRequestedDeliveryGuarantee();
  }

  /**
   * Compares the wire-visible security triple (mode, SecurityGroupId, SecurityKeyServices),
   * normalizing absent security to mode None; the local-only securityPolicyUri override is
   * excluded.
   */
  private static boolean securityMatches(
      @Nullable MessageSecurityConfig candidate, @Nullable MessageSecurityConfig pattern) {

    return modeOf(candidate) == modeOf(pattern)
        && Objects.equals(securityGroupIdOf(candidate), securityGroupIdOf(pattern))
        && keyServicesOf(candidate).equals(keyServicesOf(pattern));
  }

  private static org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode modeOf(
      @Nullable MessageSecurityConfig config) {
    return config == null
        ? org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode.None
        : config.getMode();
  }

  private static @Nullable String securityGroupIdOf(@Nullable MessageSecurityConfig config) {
    return config == null || config.getSecurityGroup() == null
        ? null
        : config.getSecurityGroup().name();
  }

  private static List<EndpointDescription> keyServicesOf(@Nullable MessageSecurityConfig config) {
    return config == null ? List.of() : config.getKeyServices();
  }

  /** Only the keys provided in the file element are compared; extra live keys never mismatch. */
  private static boolean propertiesSubset(
      Map<QualifiedName, Variant> pattern, Map<QualifiedName, Variant> live) {

    return pattern.entrySet().stream()
        .allMatch(entry -> Objects.equals(live.get(entry.getKey()), entry.getValue()));
  }

  // endregion

  // region wire element patching (identity assignment and match sanitizing)

  private static final String MATCH_PLACEHOLDER = "__match__";

  private static PubSubConnectionDataType withConnectionIdentity(
      PubSubConnectionDataType value, String name, @Nullable Variant publisherId) {
    return new PubSubConnectionDataType(
        name,
        value.getEnabled(),
        publisherId != null ? publisherId : Variant.NULL_VALUE,
        value.getTransportProfileUri(),
        value.getAddress(),
        value.getConnectionProperties(),
        value.getTransportSettings(),
        value.getWriterGroups(),
        value.getReaderGroups());
  }

  private static PubSubConnectionDataType sanitizedConnectionPattern(
      PubSubConnectionDataType value) {
    return new PubSubConnectionDataType(
        MATCH_PLACEHOLDER,
        value.getEnabled(),
        value.getPublisherId(),
        value.getTransportProfileUri(),
        value.getAddress(),
        value.getConnectionProperties(),
        value.getTransportSettings(),
        // children are ignored for match: "all other fields of the element are ignored"
        new WriterGroupDataType[0],
        new ReaderGroupDataType[0]);
  }

  private static WriterGroupDataType withWriterGroupIdentity(
      WriterGroupDataType value, String name, UShort writerGroupId) {
    return new WriterGroupDataType(
        name,
        value.getEnabled(),
        value.getSecurityMode(),
        value.getSecurityGroupId(),
        value.getSecurityKeyServices(),
        value.getMaxNetworkMessageSize(),
        value.getGroupProperties(),
        writerGroupId,
        value.getPublishingInterval(),
        value.getKeepAliveTime(),
        value.getPriority(),
        value.getLocaleIds(),
        value.getHeaderLayoutUri(),
        value.getTransportSettings(),
        value.getMessageSettings(),
        value.getDataSetWriters());
  }

  private static WriterGroupDataType sanitizedWriterGroupPattern(WriterGroupDataType value) {
    return new WriterGroupDataType(
        MATCH_PLACEHOLDER,
        value.getEnabled(),
        value.getSecurityMode(),
        value.getSecurityGroupId(),
        value.getSecurityKeyServices(),
        value.getMaxNetworkMessageSize(),
        value.getGroupProperties(),
        UShort.valueOf(1),
        value.getPublishingInterval(),
        value.getKeepAliveTime(),
        value.getPriority(),
        value.getLocaleIds(),
        value.getHeaderLayoutUri(),
        value.getTransportSettings(),
        value.getMessageSettings(),
        new DataSetWriterDataType[0]);
  }

  private static ReaderGroupDataType withReaderGroupName(ReaderGroupDataType value, String name) {
    return new ReaderGroupDataType(
        name,
        value.getEnabled(),
        value.getSecurityMode(),
        value.getSecurityGroupId(),
        value.getSecurityKeyServices(),
        value.getMaxNetworkMessageSize(),
        value.getGroupProperties(),
        value.getTransportSettings(),
        value.getMessageSettings(),
        value.getDataSetReaders());
  }

  private static ReaderGroupDataType sanitizedReaderGroupPattern(ReaderGroupDataType value) {
    return new ReaderGroupDataType(
        MATCH_PLACEHOLDER,
        value.getEnabled(),
        value.getSecurityMode(),
        value.getSecurityGroupId(),
        value.getSecurityKeyServices(),
        value.getMaxNetworkMessageSize(),
        value.getGroupProperties(),
        value.getTransportSettings(),
        value.getMessageSettings(),
        new DataSetReaderDataType[0]);
  }

  private static DataSetWriterDataType withDataSetWriterIdentity(
      DataSetWriterDataType value, String name, UShort dataSetWriterId) {
    return new DataSetWriterDataType(
        name,
        value.getEnabled(),
        dataSetWriterId,
        value.getDataSetFieldContentMask(),
        value.getKeyFrameCount(),
        value.getDataSetName(),
        value.getDataSetWriterProperties(),
        value.getTransportSettings(),
        value.getMessageSettings());
  }

  private static DataSetReaderDataType withDataSetReaderName(
      DataSetReaderDataType value, String name) {
    return new DataSetReaderDataType(
        name,
        value.getEnabled(),
        value.getPublisherId(),
        value.getWriterGroupId(),
        value.getDataSetWriterId(),
        value.getDataSetMetaData(),
        value.getDataSetFieldContentMask(),
        value.getMessageReceiveTimeout(),
        value.getKeyFrameCount(),
        value.getHeaderLayoutUri(),
        value.getSecurityMode(),
        value.getSecurityGroupId(),
        value.getSecurityKeyServices(),
        value.getDataSetReaderProperties(),
        value.getTransportSettings(),
        value.getMessageSettings(),
        value.getSubscribedDataSet());
  }

  private static PublishedDataSetDataType withPublishedDataSetName(
      PublishedDataSetDataType value, String name) {
    return new PublishedDataSetDataType(
        name,
        value.getDataSetFolder(),
        value.getDataSetMetaData(),
        value.getExtensionFields(),
        value.getDataSetSource());
  }

  private static StandaloneSubscribedDataSetDataType withSubscribedDataSetName(
      StandaloneSubscribedDataSetDataType value, String name) {
    return new StandaloneSubscribedDataSetDataType(
        name, value.getDataSetFolder(), value.getDataSetMetaData(), value.getSubscribedDataSet());
  }

  private static SecurityGroupDataType withSecurityGroupName(
      SecurityGroupDataType value, String name) {
    return new SecurityGroupDataType(
        name,
        value.getSecurityGroupFolder(),
        value.getKeyLifetime(),
        value.getSecurityPolicyUri(),
        value.getMaxFutureKeyCount(),
        value.getMaxPastKeyCount(),
        value.getSecurityGroupId(),
        value.getRolePermissions(),
        value.getGroupProperties());
  }

  // endregion

  // region small helpers

  private static String transportProfile(PubSubConnectionConfig connection, boolean json) {
    if (connection instanceof UdpConnectionConfig) {
      return PubSubIdReservations.PROFILE_UDP_UADP;
    }
    return json ? PubSubIdReservations.PROFILE_MQTT_JSON : PubSubIdReservations.PROFILE_MQTT_UADP;
  }

  private static Variant publisherIdVariant(
      org.eclipse.milo.opcua.sdk.pubsub.config.@Nullable PublisherId publisherId) {
    return publisherId == null ? Variant.NULL_VALUE : publisherId.toVariant();
  }

  private static <T> T mapElement(Supplier<T> mapping) {
    try {
      return mapping.get();
    } catch (PubSubConfigValidationException e) {
      throw new RefFailedException(StatusCodes.Bad_InvalidArgument, e.getMessage());
    }
  }

  private static <T> Set<String> names(
      List<T> elements, java.util.function.Function<T, String> name) {
    var names = new LinkedHashSet<String>();
    elements.forEach(element -> names.add(name.apply(element)));
    return names;
  }

  private static Set<String> groupNamesOf(PubSubConnectionConfig connection) {
    // writer group and reader group names share one scope within the connection
    var names = new LinkedHashSet<String>();
    connection.writerGroups().forEach(g -> names.add(g.getName()));
    connection.readerGroups().forEach(g -> names.add(g.getName()));
    return names;
  }

  /** The Milo-defined {@code "<Kind><n>"} auto-assignment scheme, unique in the working scope. */
  private static String uniqueName(String prefix, Set<String> existing) {
    for (int i = 1; ; i++) {
      String candidate = prefix + i;
      if (!existing.contains(candidate)) {
        return candidate;
      }
    }
  }

  private static <T> List<T> plus(List<T> list, T element) {
    var updated = new ArrayList<>(list);
    updated.add(element);
    return List.copyOf(updated);
  }

  private static <T> List<T> minus(List<T> list, java.util.function.Predicate<T> remove) {
    return list.stream().filter(remove.negate()).toList();
  }

  private static <T> List<T> replaceByName(
      List<T> list, String name, java.util.function.Function<T, String> nameOf, T replacement) {
    return list.stream().map(e -> nameOf.apply(e).equals(name) ? replacement : e).toList();
  }

  private static long groupKey(boolean writer, int connectionIndex, int groupIndex) {
    return ((writer ? 1L : 2L) << 33) | ((long) connectionIndex << 16) | groupIndex;
  }

  private static int intValue(@Nullable UShort value) {
    return value == null ? 0 : value.intValue();
  }

  private static boolean nullOrEmpty(@Nullable String value) {
    return value == null || value.isEmpty();
  }

  private static String requireBindingName(@Nullable String name) {
    if (name == null || name.isEmpty()) {
      // Modify/Remove (and parent addressing) bind to the live element by name
      throw new RefFailedException(
          StatusCodes.Bad_NoMatch, "the file element has no name to bind by");
    }
    return name;
  }

  /** The builder's top-level-scope name-duplication form: {@code duplicate <type> name '<n>'}. */
  private static final Pattern TOP_LEVEL_NAME_DUPLICATION =
      Pattern.compile("duplicate \\w+ name '.*'", Pattern.DOTALL);

  /** The builder's cross-connection group-name-duplication form (interpolation mid-message). */
  private static final Pattern PUBLISHER_SCOPE_NAME_DUPLICATION =
      Pattern.compile(
          "connection '.*' writerGroup '.*': duplicate writer group name within publisherId"
              + " scope .* \\(also defined on connection '.*'\\)",
          Pattern.DOTALL);

  /**
   * Matches the builder's five NAME-duplication message shapes, anchored at message start/end so
   * element names interpolated into OTHER messages (e.g. a wire-id collision against a group
   * literally named {@code "my name"}) can never flip a {@code Bad_InvalidArgument} failure to
   * {@code Bad_BrowseNameDuplicated}. Builder messages never begin with interpolated content (paths
   * start with a fixed {@code "connection '"} prefix) and the fixed suffixes below carry no
   * trailing interpolation, so the anchors are decisive for any name the file can carry.
   */
  private static boolean isNameDuplicationMessage(String message) {
    return TOP_LEVEL_NAME_DUPLICATION.matcher(message).matches()
        || message.endsWith(": duplicate group name within connection")
        || message.endsWith(": duplicate dataSetWriter name within writer group")
        || message.endsWith(": duplicate dataSetReader name within reader group")
        || PUBLISHER_SCOPE_NAME_DUPLICATION.matcher(message).matches();
  }

  private static RefFailedException invalidRef(String message) {
    return new RefFailedException(StatusCodes.Bad_InvalidArgument, message);
  }

  private static RefFailedException noMatch(String kind, String name) {
    return new RefFailedException(
        StatusCodes.Bad_NoMatch, "no %s with name '%s' exists".formatted(kind, name));
  }

  private static RefFailedException duplicated(String kind, String name) {
    return new RefFailedException(
        StatusCodes.Bad_BrowseNameDuplicated,
        "a %s with name '%s' already exists".formatted(kind, name));
  }

  private static RefFailedException parentNotFound(String detail) {
    return new RefFailedException(
        StatusCodes.Bad_NotFound,
        "a parent element does not exist or was not added or matched: " + detail);
  }

  // endregion

  // region types

  /** The outcome of one CloseAndUpdate evaluation. */
  record Outcome(
      boolean changesApplied,
      StatusCode[] referencesResults,
      PubSubConfigurationValueDataType[] configurationValues,
      NodeId[] configurationObjects,
      @Nullable PubSubConfig appliedConfig,
      Set<UsedReservation> consumedReservations) {}

  private enum Op {
    ADD,
    MATCH,
    ADD_MATCH,
    MODIFY,
    REMOVE
  }

  private enum Kind {
    SECURITY_GROUP(0),
    PUBLISHED_DATA_SET(1),
    SUBSCRIBED_DATA_SET(2),
    CONNECTION(3),
    WRITER_GROUP(4),
    READER_GROUP(4),
    DATA_SET_WRITER(5),
    DATA_SET_READER(5);

    private final int rank;

    Kind(int rank) {
      this.rank = rank;
    }
  }

  private static final class Ref {

    private final int index;
    private final PubSubConfigurationRefDataType wire;

    private @Nullable Op op;
    private @Nullable Kind kind;
    private int connectionIndex;
    private int groupIndex;
    private int elementIndex;

    private boolean mutationApplied;

    private @Nullable String resolvedName;
    private @Nullable String resolvedConnectionName;
    private @Nullable String resolvedGroupName;

    private boolean reportValue;
    private boolean assignedByAdd;
    private @Nullable Variant identifier;

    private Ref(int index, PubSubConfigurationRefDataType wire) {
      this.index = index;
      this.wire = wire;
    }
  }

  private static void reportValue(Ref ref, boolean report, Variant identifier) {
    if (report) {
      ref.reportValue = true;
      ref.identifier = identifier;
    }
  }

  private record IdAssignment(UShort id, boolean assigned, @Nullable UsedReservation reservation) {

    private void consume(Set<UsedReservation> consumed) {
      if (reservation != null) {
        consumed.add(reservation);
      }
    }
  }

  /** Null-tolerant views of the file arrays; duplicate names are legal and never unique-keyed. */
  private static final class FileArrays {

    private final PubSubConnectionDataType[] connections;
    private final PublishedDataSetDataType[] publishedDataSets;
    private final StandaloneSubscribedDataSetDataType[] subscribedDataSets;
    private final SecurityGroupDataType[] securityGroups;

    private FileArrays(PubSubConfiguration2DataType file) {
      this.connections =
          Objects.requireNonNullElse(file.getConnections(), new PubSubConnectionDataType[0]);
      this.publishedDataSets =
          Objects.requireNonNullElse(file.getPublishedDataSets(), new PublishedDataSetDataType[0]);
      this.subscribedDataSets =
          Objects.requireNonNullElse(
              file.getSubscribedDataSets(), new StandaloneSubscribedDataSetDataType[0]);
      this.securityGroups =
          Objects.requireNonNullElse(file.getSecurityGroups(), new SecurityGroupDataType[0]);
    }

    private WriterGroupDataType[] writerGroups(int connectionIndex) {
      return Objects.requireNonNullElse(
          connections[connectionIndex].getWriterGroups(), new WriterGroupDataType[0]);
    }

    private ReaderGroupDataType[] readerGroups(int connectionIndex) {
      return Objects.requireNonNullElse(
          connections[connectionIndex].getReaderGroups(), new ReaderGroupDataType[0]);
    }

    private DataSetWriterDataType[] dataSetWriters(int connectionIndex, int groupIndex) {
      return Objects.requireNonNullElse(
          writerGroups(connectionIndex)[groupIndex].getDataSetWriters(),
          new DataSetWriterDataType[0]);
    }

    private DataSetReaderDataType[] dataSetReaders(int connectionIndex, int groupIndex) {
      return Objects.requireNonNullElse(
          readerGroups(connectionIndex)[groupIndex].getDataSetReaders(),
          new DataSetReaderDataType[0]);
    }
  }

  /** Fails the current reference with {@code status}; never escapes {@link #apply}. */
  private static final class RefFailedException extends RuntimeException {

    private final StatusCode status;

    private RefFailedException(long code, @Nullable String message) {
      this(new StatusCode(code), message);
    }

    private RefFailedException(StatusCode status, @Nullable String message) {
      super(message, null, false, false);
      this.status = status;
    }
  }

  /** Aborts the mediator update with the evaluation intact; nothing was applied. */
  private static final class NotAppliedException extends RuntimeException {

    private NotAppliedException() {
      super(null, null, false, false);
    }
  }

  // endregion
}
