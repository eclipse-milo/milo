/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.gds.testing;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import org.bouncycastle.asn1.pkcs.Attribute;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.eclipse.milo.opcua.sdk.client.gds.GdsClient;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.server.ManagedNamespaceWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataTypeEncodingTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaDataTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilters;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder;
import org.eclipse.milo.opcua.stack.core.gds.DataTypeInitializer;
import org.eclipse.milo.opcua.stack.core.gds.GdsNodeIds;
import org.eclipse.milo.opcua.stack.core.gds.types.ApplicationRecordDataType;
import org.eclipse.milo.opcua.stack.core.types.DataTypeEncoding;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.OpenFileMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerOnNetwork;
import org.eclipse.milo.opcua.stack.core.types.structured.TrustListDataType;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.jspecify.annotations.Nullable;

/**
 * An in-memory GDS test fixture: the GDS namespace, the {@code ApplicationRecordDataType}, the
 * {@code Directory} object with every DirectoryType and CertificateDirectoryType method, two
 * certificate groups, and their TrustList files.
 *
 * <p>Behaviour is deliberately simple and controllable from tests: registrations are kept in a map,
 * signing requests are issued by a throwaway CA after a configurable number of {@code
 * Bad_NothingToDo} polls, and each TrustList records the FileType calls made against it.
 *
 * <p>Construct the fixture with the server under test, call {@link #startup()} before starting the
 * server, and call {@link #shutdown()} during teardown. Call {@link #reset()} between tests that
 * share an instance.
 */
public class FakeGdsNamespace extends ManagedNamespaceWithLifecycle {

  public static final String NAMESPACE_URI = GdsClient.NAMESPACE_URI;

  /** Access requirements for controlled GDS methods. */
  public enum MethodAccess {
    /** Allow calls from any activated session. */
    ANYONE,

    /** Allow calls only from a session activated with a UserName identity token. */
    CREDENTIALED,

    /** Deny every call with {@code Bad_UserAccessDenied}. */
    NOBODY
  }

  /** Recorded FileType calls and served body of one TrustList object. */
  public static final class TrustListFile {
    private volatile byte[] body = new byte[0];
    private volatile DateTime lastUpdateTime = DateTime.now();
    private volatile int failReadAfter = -1;
    private final AtomicInteger nextHandle = new AtomicInteger(1);
    private final Map<UInteger, Integer> positions = new ConcurrentHashMap<>();
    private final List<String> calls = new CopyOnWriteArrayList<>();

    /** The FileType calls made so far, e.g. {@code Open}, {@code Read(4096)}, {@code Close}. */
    public List<String> calls() {
      return List.copyOf(calls);
    }

    void reset() {
      calls.clear();
      positions.clear();
      failReadAfter = -1;
    }

    public DateTime lastUpdateTime() {
      return lastUpdateTime;
    }

    /** Serve {@code trustList}, encoded as a bare structure (Part 12 §7.8.2). */
    public void setTrustList(TrustListDataType trustList) {
      setBody(encodeBare(trustList));
    }

    /** Serve raw bytes, e.g. a malformed body. */
    public void setBody(byte[] body) {
      this.body = body;
      this.lastUpdateTime = DateTime.now();
    }

    /** Make Read fail with {@code Bad_UnexpectedError} after {@code reads} successful reads. */
    public void failReadAfter(int reads) {
      this.failReadAfter = reads;
    }

    public int openHandles() {
      return positions.size();
    }
  }

  private record SigningRequest(
      NodeId applicationId, @Nullable PKCS10CertificationRequest csr, AtomicInteger polls) {}

  private final KeyPair caKeyPair;
  private final X509Certificate caCertificate;

  private final Map<NodeId, ApplicationRecordDataType> applications = new ConcurrentHashMap<>();
  private final Map<NodeId, SigningRequest> requests = new ConcurrentHashMap<>();
  private final Map<NodeId, List<X509Certificate>> issued = new ConcurrentHashMap<>();
  private final Set<ByteString> revoked = ConcurrentHashMap.newKeySet();
  private final AtomicInteger nextId = new AtomicInteger(1);
  private final AtomicInteger registerApplicationCallCount = new AtomicInteger();
  private final AtomicInteger startSigningRequestCallCount = new AtomicInteger();
  private final AtomicInteger finishRequestCallCount = new AtomicInteger();

  private final TrustListFile applicationGroupTrustList = new TrustListFile();
  private final TrustListFile userTokenGroupTrustList = new TrustListFile();

  private volatile MethodAccess registerApplicationAccess = MethodAccess.ANYONE;
  private volatile MethodAccess certificateDirectoryAccess = MethodAccess.ANYONE;
  private volatile NodeId[] applicationGroupCertificateTypes = defaultCertificateTypes();
  private volatile @Nullable NodeId lastCertificateTypeId;
  private volatile int pollsBeforeIssued = 0;
  private volatile boolean rejectRequests = false;
  private volatile boolean updateRequired = true;

  public FakeGdsNamespace(OpcUaServer server) throws Exception {
    super(server, NAMESPACE_URI);

    caKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    caCertificate =
        new SelfSignedCertificateBuilder(caKeyPair)
            .setCommonName("Fake GDS CA")
            .setApplicationUri("urn:eclipse:milo:test:gds:ca")
            .build();

    getLifecycleManager().addStartupTask(this::addNodes);
  }

  private static NodeId[] defaultCertificateTypes() {
    return new NodeId[] {
      NodeIds.RsaSha256ApplicationCertificateType, NodeIds.EccNistP256ApplicationCertificateType
    };
  }

  // The GDS tests never subscribe to anything in this namespace.
  @Override
  public void onDataItemsCreated(List<DataItem> dataItems) {}

  @Override
  public void onDataItemsModified(List<DataItem> dataItems) {}

  @Override
  public void onDataItemsDeleted(List<DataItem> dataItems) {}

  @Override
  public void onMonitoringModeChanged(List<MonitoredItem> monitoredItems) {}

  // region test knobs

  public X509Certificate getCaCertificate() {
    return caCertificate;
  }

  public TrustListFile getApplicationGroupTrustList() {
    return applicationGroupTrustList;
  }

  /** The DefaultUserTokenGroup TrustList has no UpdateFrequency and no MaxByteStringLength. */
  public TrustListFile getUserTokenGroupTrustList() {
    return userTokenGroupTrustList;
  }

  /** Set the access required to call {@code RegisterApplication}. */
  public void setRegisterApplicationAccess(MethodAccess access) {
    this.registerApplicationAccess = Objects.requireNonNull(access);
  }

  /** Set the access required to call the CertificateDirectoryType methods. */
  public void setCertificateDirectoryAccess(MethodAccess access) {
    this.certificateDirectoryAccess = Objects.requireNonNull(access);
  }

  /** Configure the CertificateTypes advertised by the DefaultApplicationGroup. */
  public void setApplicationCertificateTypes(NodeId... certificateTypes) {
    this.applicationGroupCertificateTypes = Objects.requireNonNull(certificateTypes).clone();
  }

  /**
   * Number of calls made to {@code RegisterApplication} since the last {@link #reset()}, including
   * calls that were denied or rejected.
   */
  public int getRegisterApplicationCallCount() {
    return registerApplicationCallCount.get();
  }

  /**
   * Number of calls made to {@code StartSigningRequest} since the last {@link #reset()}, including
   * calls that were denied or rejected.
   */
  public int getStartSigningRequestCallCount() {
    return startSigningRequestCallCount.get();
  }

  /**
   * Number of calls made to {@code FinishRequest} since the last {@link #reset()}, including calls
   * that were denied or rejected.
   */
  public int getFinishRequestCallCount() {
    return finishRequestCallCount.get();
  }

  /**
   * Get the CertificateTypeId the client sent in the most recent {@code StartSigningRequest},
   * including calls that were denied or rejected.
   *
   * @return the CertificateTypeId from the last {@code StartSigningRequest}, or {@code null} if it
   *     was null or no request has been made since the last {@link #reset()}.
   */
  public @Nullable NodeId getLastCertificateTypeId() {
    return lastCertificateTypeId;
  }

  /** Pre-register an application as if it had been added by a GDS administrator. */
  public NodeId preRegister(String applicationUri) {
    NodeId applicationId = newNodeId("Applications/" + nextId.getAndIncrement());

    applications.put(
        applicationId,
        new ApplicationRecordDataType(
            applicationId,
            applicationUri,
            ApplicationType.Client,
            new LocalizedText[0],
            null,
            null,
            null));

    return applicationId;
  }

  /**
   * Number of FinishRequest calls that return {@code Bad_NothingToDo} before a request is issued.
   */
  public void setPollsBeforeIssued(int pollsBeforeIssued) {
    this.pollsBeforeIssued = pollsBeforeIssued;
  }

  /** When true, FinishRequest fails with {@code Bad_RequestNotAllowed} for every request. */
  public void setRejectRequests(boolean rejectRequests) {
    this.rejectRequests = rejectRequests;
  }

  public void setUpdateRequired(boolean updateRequired) {
    this.updateRequired = updateRequired;
  }

  public void reset() {
    applications.clear();
    requests.clear();
    issued.clear();
    revoked.clear();
    registerApplicationAccess = MethodAccess.ANYONE;
    certificateDirectoryAccess = MethodAccess.ANYONE;
    applicationGroupCertificateTypes = defaultCertificateTypes();
    registerApplicationCallCount.set(0);
    startSigningRequestCallCount.set(0);
    finishRequestCallCount.set(0);
    lastCertificateTypeId = null;
    pollsBeforeIssued = 0;
    rejectRequests = false;
    updateRequired = true;
    applicationGroupTrustList.reset();
    userTokenGroupTrustList.reset();
  }

  public NodeId nodeId(ExpandedNodeId gdsNodeId) {
    return gdsNodeId.toNodeId(getServer().getNamespaceTable()).orElseThrow();
  }

  // endregion

  private void addNodes() {
    try {
      addApplicationRecordDataType();
      addObjectTypes();

      UaObjectNode directory =
          addObject(
              nodeId(GdsNodeIds.Directory),
              newQualifiedName("Directory"),
              nodeId(GdsNodeIds.CertificateDirectoryType),
              NodeIds.ObjectsFolder,
              NodeIds.Organizes);

      UaObjectNode certificateGroups =
          addObject(
              nodeId(GdsNodeIds.Directory_CertificateGroups),
              newQualifiedName("CertificateGroups"),
              NodeIds.CertificateGroupFolderType,
              directory.getNodeId(),
              NodeIds.Organizes);

      addCertificateGroup(
          certificateGroups,
          GdsNodeIds.Directory_CertificateGroups_DefaultApplicationGroup,
          "DefaultApplicationGroup",
          GdsNodeIds.Directory_CertificateGroups_DefaultApplicationGroup_CertificateTypes,
          () -> applicationGroupCertificateTypes.clone(),
          GdsNodeIds.Directory_CertificateGroups_DefaultApplicationGroup_TrustList,
          applicationGroupTrustList,
          true);

      addCertificateGroup(
          certificateGroups,
          GdsNodeIds.Directory_CertificateGroups_DefaultUserTokenGroup,
          "DefaultUserTokenGroup",
          GdsNodeIds.Directory_CertificateGroups_DefaultUserTokenGroup_CertificateTypes,
          () -> new NodeId[0],
          GdsNodeIds.Directory_CertificateGroups_DefaultUserTokenGroup_TrustList,
          userTokenGroupTrustList,
          false);

      addDirectoryMethods(directory);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void addApplicationRecordDataType() {
    NodeId dataTypeId = nodeId(GdsNodeIds.ApplicationRecordDataType);
    NodeId binaryEncodingId = nodeId(GdsNodeIds.ApplicationRecordDataType_Encoding_DefaultBinary);

    var dataTypeNode =
        new UaDataTypeNode(
            getNodeContext(),
            dataTypeId,
            newQualifiedName("ApplicationRecordDataType"),
            LocalizedText.english("ApplicationRecordDataType"),
            LocalizedText.NULL_VALUE,
            uint(0),
            uint(0),
            false);

    dataTypeNode.addReference(
        new Reference(
            dataTypeId,
            NodeIds.HasSubtype,
            NodeIds.Structure.expanded(),
            Reference.Direction.INVERSE));

    dataTypeNode.setDataTypeDefinition(
        ApplicationRecordDataType.definition(getServer().getNamespaceTable()));

    getNodeManager().addNode(dataTypeNode);

    var encodingNode =
        new DataTypeEncodingTypeNode(
            getNodeContext(),
            binaryEncodingId,
            DataTypeEncoding.BINARY_ENCODING_NAME,
            LocalizedText.english("Default Binary"),
            LocalizedText.NULL_VALUE,
            uint(0),
            uint(0),
            null,
            null,
            null);

    encodingNode.addReference(
        new Reference(
            binaryEncodingId,
            NodeIds.HasTypeDefinition,
            NodeIds.DataTypeEncodingType.expanded(),
            Reference.Direction.FORWARD));
    encodingNode.addReference(
        new Reference(
            binaryEncodingId,
            NodeIds.HasEncoding,
            dataTypeId.expanded(),
            Reference.Direction.INVERSE));

    getNodeManager().addNode(encodingNode);

    DataTypeInitializer.initialize(
        getServer().getNamespaceTable(), getServer().getStaticDataTypeManager());
  }

  private void addObjectTypes() {
    addObjectType(nodeId(GdsNodeIds.DirectoryType), "DirectoryType", NodeIds.FolderType);
    addObjectType(
        nodeId(GdsNodeIds.CertificateDirectoryType),
        "CertificateDirectoryType",
        nodeId(GdsNodeIds.DirectoryType));
  }

  private void addObjectType(NodeId typeId, String name, NodeId supertypeId) {
    var typeNode =
        new UaObjectTypeNode(
            getNodeContext(),
            typeId,
            newQualifiedName(name),
            LocalizedText.english(name),
            LocalizedText.NULL_VALUE,
            uint(0),
            uint(0),
            false);

    typeNode.addReference(
        new Reference(
            typeId, NodeIds.HasSubtype, supertypeId.expanded(), Reference.Direction.INVERSE));

    getNodeManager().addNode(typeNode);
  }

  private UaObjectNode addObject(
      NodeId nodeId,
      QualifiedName browseName,
      NodeId typeDefinitionId,
      NodeId parentId,
      NodeId parentReferenceTypeId) {

    UaObjectNode node =
        UaObjectNode.build(
            getNodeContext(),
            b ->
                b.setNodeId(nodeId)
                    .setBrowseName(browseName)
                    .setDisplayName(LocalizedText.english(browseName.name()))
                    .setTypeDefinition(typeDefinitionId)
                    .build());

    node.addReference(
        new Reference(
            nodeId, parentReferenceTypeId, parentId.expanded(), Reference.Direction.INVERSE));

    getNodeManager().addNode(node);

    return node;
  }

  private void addCertificateGroup(
      UaObjectNode certificateGroups,
      ExpandedNodeId groupId,
      String name,
      ExpandedNodeId certificateTypesId,
      Supplier<NodeId[]> certificateTypes,
      ExpandedNodeId trustListId,
      TrustListFile file,
      boolean optionalProperties) {

    UaObjectNode group =
        addObject(
            nodeId(groupId),
            new QualifiedName(0, name),
            NodeIds.CertificateGroupType,
            certificateGroups.getNodeId(),
            NodeIds.HasComponent);

    addProperty(
        group,
        nodeId(certificateTypesId),
        "CertificateTypes",
        NodeIds.NodeId,
        ValueRanks.OneDimension,
        Variant.NULL_VALUE,
        () -> new Variant(certificateTypes.get()));

    UaObjectNode trustList =
        addObject(
            nodeId(trustListId),
            new QualifiedName(0, "TrustList"),
            NodeIds.TrustListType,
            group.getNodeId(),
            NodeIds.HasComponent);

    addProperty(
        trustList,
        newNodeId(name + "/TrustList/LastUpdateTime"),
        "LastUpdateTime",
        NodeIds.UtcTime,
        ValueRanks.Scalar,
        new Variant(DateTime.MIN_VALUE),
        () -> new Variant(file.lastUpdateTime));

    if (optionalProperties) {
      addProperty(
          trustList,
          newNodeId(name + "/TrustList/UpdateFrequency"),
          "UpdateFrequency",
          NodeIds.Duration,
          ValueRanks.Scalar,
          new Variant(60_000.0),
          null);
      addProperty(
          trustList,
          newNodeId(name + "/TrustList/MaxByteStringLength"),
          "MaxByteStringLength",
          NodeIds.UInt32,
          ValueRanks.Scalar,
          new Variant(uint(100)),
          null);
    }

    addFileTypeMethods(trustList, name, file);
  }

  private void addProperty(
      UaObjectNode parent,
      NodeId nodeId,
      String name,
      NodeId dataTypeId,
      int valueRank,
      Variant value,
      @Nullable Supplier<Variant> liveValue) {

    UaVariableNode property =
        new UaVariableNode.UaVariableNodeBuilder(getNodeContext())
            .setNodeId(nodeId)
            .setBrowseName(new QualifiedName(0, name))
            .setDisplayName(LocalizedText.english(name))
            .setDataType(dataTypeId)
            .setValueRank(valueRank)
            .setAccessLevel(AccessLevel.READ_ONLY)
            .setUserAccessLevel(AccessLevel.READ_ONLY)
            .setTypeDefinition(NodeIds.PropertyType)
            .setValue(new DataValue(value))
            .build();

    if (liveValue != null) {
      property
          .getFilterChain()
          .addLast(AttributeFilters.getValue(ctx -> new DataValue(liveValue.get())));
    }

    property.addReference(
        new Reference(
            nodeId,
            NodeIds.HasProperty,
            parent.getNodeId().expanded(),
            Reference.Direction.INVERSE));

    getNodeManager().addNode(property);
  }

  private void addFileTypeMethods(UaObjectNode trustList, String groupName, TrustListFile file) {
    addMethod(
        trustList,
        newNodeId(groupName + "/TrustList/Open"),
        new QualifiedName(0, "Open"),
        new Argument[] {argument("Mode", NodeIds.Byte, ValueRanks.Scalar)},
        new Argument[] {argument("FileHandle", NodeIds.UInt32, ValueRanks.Scalar)},
        (context, inputs) -> {
          file.calls.add("Open");

          UByte mode = (UByte) inputs[0].value();
          if (mode == null || mode.intValue() != OpenFileMode.Read.getValue()) {
            throw new UaException(StatusCodes.Bad_InvalidArgument, "only Read mode is supported");
          }

          UInteger handle = uint(file.nextHandle.getAndIncrement());
          file.positions.put(handle, 0);

          return new Variant[] {Variant.ofUInt32(handle)};
        });

    addMethod(
        trustList,
        newNodeId(groupName + "/TrustList/Read"),
        new QualifiedName(0, "Read"),
        new Argument[] {
          argument("FileHandle", NodeIds.UInt32, ValueRanks.Scalar),
          argument("Length", NodeIds.Int32, ValueRanks.Scalar)
        },
        new Argument[] {argument("Data", NodeIds.ByteString, ValueRanks.Scalar)},
        (context, inputs) -> {
          UInteger handle = (UInteger) inputs[0].value();
          int length = Objects.requireNonNull((Integer) inputs[1].value());

          file.calls.add("Read(" + length + ")");

          Integer position = file.positions.get(handle);
          if (position == null) {
            throw new UaException(StatusCodes.Bad_InvalidArgument, "unknown file handle");
          }

          long reads = file.calls.stream().filter(c -> c.startsWith("Read")).count();
          if (file.failReadAfter >= 0 && reads > file.failReadAfter) {
            throw new UaException(StatusCodes.Bad_UnexpectedError, "read failure injected");
          }

          byte[] body = file.body;
          int end = Math.min(body.length, position + length);
          byte[] chunk = Arrays.copyOfRange(body, Math.min(position, body.length), end);
          file.positions.put(handle, end);

          return new Variant[] {Variant.ofByteString(ByteString.of(chunk))};
        });

    addMethod(
        trustList,
        newNodeId(groupName + "/TrustList/Close"),
        new QualifiedName(0, "Close"),
        new Argument[] {argument("FileHandle", NodeIds.UInt32, ValueRanks.Scalar)},
        new Argument[0],
        (context, inputs) -> {
          file.calls.add("Close");

          UInteger handle = (UInteger) inputs[0].value();
          if (file.positions.remove(handle) == null) {
            throw new UaException(StatusCodes.Bad_InvalidArgument, "unknown file handle");
          }

          return new Variant[0];
        });
  }

  private void addDirectoryMethods(UaObjectNode directory) {
    NodeId applicationRecordId = nodeId(GdsNodeIds.ApplicationRecordDataType);

    addMethod(
        directory,
        nodeId(GdsNodeIds.Directory_FindApplications),
        newQualifiedName("FindApplications"),
        new Argument[] {argument("ApplicationUri", NodeIds.String, ValueRanks.Scalar)},
        new Argument[] {argument("Applications", applicationRecordId, ValueRanks.OneDimension)},
        (context, inputs) -> {
          String applicationUri = (String) inputs[0].value();

          ApplicationRecordDataType[] matches =
              applications.values().stream()
                  .filter(a -> Objects.equals(a.getApplicationUri(), applicationUri))
                  .toArray(ApplicationRecordDataType[]::new);

          return new Variant[] {new Variant(matches)};
        });

    addMethod(
        directory,
        nodeId(GdsNodeIds.Directory_RegisterApplication),
        newQualifiedName("RegisterApplication"),
        new Argument[] {argument("Application", applicationRecordId, ValueRanks.Scalar)},
        new Argument[] {argument("ApplicationId", NodeIds.NodeId, ValueRanks.Scalar)},
        (context, inputs) -> {
          registerApplicationCallCount.incrementAndGet();
          requireAccess(context, registerApplicationAccess);

          ApplicationRecordDataType record = applicationArgument(inputs[0]);
          NodeId applicationId = newNodeId("Applications/" + nextId.getAndIncrement());

          applications.put(applicationId, withId(record, applicationId));

          return new Variant[] {Variant.ofNodeId(applicationId)};
        });

    addMethod(
        directory,
        nodeId(GdsNodeIds.Directory_UpdateApplication),
        newQualifiedName("UpdateApplication"),
        new Argument[] {argument("Application", applicationRecordId, ValueRanks.Scalar)},
        new Argument[0],
        (context, inputs) -> {
          ApplicationRecordDataType record = applicationArgument(inputs[0]);

          requireApplication(record.getApplicationId());
          applications.put(record.getApplicationId(), record);

          return new Variant[0];
        });

    addMethod(
        directory,
        nodeId(GdsNodeIds.Directory_UnregisterApplication),
        newQualifiedName("UnregisterApplication"),
        new Argument[] {argument("ApplicationId", NodeIds.NodeId, ValueRanks.Scalar)},
        new Argument[0],
        (context, inputs) -> {
          NodeId applicationId = (NodeId) inputs[0].value();

          requireApplication(applicationId);
          applications.remove(applicationId);

          return new Variant[0];
        });

    addMethod(
        directory,
        nodeId(GdsNodeIds.Directory_GetApplication),
        newQualifiedName("GetApplication"),
        new Argument[] {argument("ApplicationId", NodeIds.NodeId, ValueRanks.Scalar)},
        new Argument[] {argument("Application", applicationRecordId, ValueRanks.Scalar)},
        (context, inputs) -> {
          NodeId applicationId = (NodeId) inputs[0].value();

          return new Variant[] {new Variant(requireApplication(applicationId))};
        });

    addMethod(
        directory,
        nodeId(GdsNodeIds.Directory_QueryApplications),
        newQualifiedName("QueryApplications"),
        new Argument[] {
          argument("StartingRecordId", NodeIds.UInt32, ValueRanks.Scalar),
          argument("MaxRecordsToReturn", NodeIds.UInt32, ValueRanks.Scalar),
          argument("ApplicationName", NodeIds.String, ValueRanks.Scalar),
          argument("ApplicationUri", NodeIds.String, ValueRanks.Scalar),
          argument("ApplicationType", NodeIds.UInt32, ValueRanks.Scalar),
          argument("ProductUri", NodeIds.String, ValueRanks.Scalar),
          argument("ServerCapabilities", NodeIds.String, ValueRanks.OneDimension)
        },
        new Argument[] {
          argument("LastCounterResetTime", NodeIds.UtcTime, ValueRanks.Scalar),
          argument("NextRecordId", NodeIds.UInt32, ValueRanks.Scalar),
          argument("Applications", NodeIds.ApplicationDescription, ValueRanks.OneDimension)
        },
        (context, inputs) -> {
          ApplicationDescription[] descriptions =
              applications.values().stream()
                  .map(FakeGdsNamespace::toDescription)
                  .toArray(ApplicationDescription[]::new);

          return new Variant[] {
            Variant.ofDateTime(DateTime.MIN_VALUE),
            Variant.ofUInt32(uint(0)),
            new Variant(descriptions)
          };
        });

    addMethod(
        directory,
        nodeId(GdsNodeIds.Directory_QueryServers),
        newQualifiedName("QueryServers"),
        new Argument[] {
          argument("StartingRecordId", NodeIds.UInt32, ValueRanks.Scalar),
          argument("MaxRecordsToReturn", NodeIds.UInt32, ValueRanks.Scalar),
          argument("ApplicationName", NodeIds.String, ValueRanks.Scalar),
          argument("ApplicationUri", NodeIds.String, ValueRanks.Scalar),
          argument("ProductUri", NodeIds.String, ValueRanks.Scalar),
          argument("ServerCapabilities", NodeIds.String, ValueRanks.OneDimension)
        },
        new Argument[] {
          argument("LastCounterResetTime", NodeIds.UtcTime, ValueRanks.Scalar),
          argument("Servers", NodeIds.ServerOnNetwork, ValueRanks.OneDimension)
        },
        (context, inputs) -> {
          ServerOnNetwork[] servers =
              applications.values().stream()
                  .map(
                      a ->
                          new ServerOnNetwork(
                              uint(0),
                              a.getApplicationNames() != null && a.getApplicationNames().length > 0
                                  ? a.getApplicationNames()[0].text()
                                  : null,
                              a.getDiscoveryUrls() != null && a.getDiscoveryUrls().length > 0
                                  ? a.getDiscoveryUrls()[0]
                                  : null,
                              a.getServerCapabilities()))
                  .toArray(ServerOnNetwork[]::new);

          return new Variant[] {Variant.ofDateTime(DateTime.MIN_VALUE), new Variant(servers)};
        });

    addMethod(
        directory,
        nodeId(GdsNodeIds.Directory_StartSigningRequest),
        newQualifiedName("StartSigningRequest"),
        new Argument[] {
          argument("ApplicationId", NodeIds.NodeId, ValueRanks.Scalar),
          argument("CertificateGroupId", NodeIds.NodeId, ValueRanks.Scalar),
          argument("CertificateTypeId", NodeIds.NodeId, ValueRanks.Scalar),
          argument("CertificateRequest", NodeIds.ByteString, ValueRanks.Scalar)
        },
        new Argument[] {argument("RequestId", NodeIds.NodeId, ValueRanks.Scalar)},
        (context, inputs) -> {
          startSigningRequestCallCount.incrementAndGet();
          lastCertificateTypeId = nonNullNodeId((NodeId) inputs[2].value());
          requireAccess(context, certificateDirectoryAccess);

          NodeId applicationId = (NodeId) inputs[0].value();
          ByteString certificateRequest = (ByteString) inputs[3].value();

          ApplicationRecordDataType record = requireApplication(applicationId);

          PKCS10CertificationRequest csr = parseCsr(certificateRequest);

          if (!Objects.equals(record.getApplicationUri(), sanUri(csr))) {
            throw new UaException(StatusCodes.Bad_CertificateUriInvalid);
          }

          NodeId requestId = newNodeId("Requests/" + nextId.getAndIncrement());
          requests.put(requestId, new SigningRequest(applicationId, csr, new AtomicInteger()));

          return new Variant[] {Variant.ofNodeId(requestId)};
        });

    addMethod(
        directory,
        nodeId(GdsNodeIds.Directory_StartNewKeyPairRequest),
        newQualifiedName("StartNewKeyPairRequest"),
        new Argument[] {
          argument("ApplicationId", NodeIds.NodeId, ValueRanks.Scalar),
          argument("CertificateGroupId", NodeIds.NodeId, ValueRanks.Scalar),
          argument("CertificateTypeId", NodeIds.NodeId, ValueRanks.Scalar),
          argument("SubjectName", NodeIds.String, ValueRanks.Scalar),
          argument("DomainNames", NodeIds.String, ValueRanks.OneDimension),
          argument("PrivateKeyFormat", NodeIds.String, ValueRanks.Scalar),
          argument("PrivateKeyPassword", NodeIds.String, ValueRanks.Scalar)
        },
        new Argument[] {argument("RequestId", NodeIds.NodeId, ValueRanks.Scalar)},
        (context, inputs) -> {
          requireAccess(context, certificateDirectoryAccess);

          NodeId applicationId = (NodeId) inputs[0].value();

          requireApplication(applicationId);

          NodeId requestId = newNodeId("Requests/" + nextId.getAndIncrement());
          requests.put(requestId, new SigningRequest(applicationId, null, new AtomicInteger()));

          return new Variant[] {Variant.ofNodeId(requestId)};
        });

    addMethod(
        directory,
        nodeId(GdsNodeIds.Directory_FinishRequest),
        newQualifiedName("FinishRequest"),
        new Argument[] {
          argument("ApplicationId", NodeIds.NodeId, ValueRanks.Scalar),
          argument("RequestId", NodeIds.NodeId, ValueRanks.Scalar)
        },
        new Argument[] {
          argument("Certificate", NodeIds.ByteString, ValueRanks.Scalar),
          argument("PrivateKey", NodeIds.ByteString, ValueRanks.Scalar),
          argument("IssuerCertificates", NodeIds.ByteString, ValueRanks.OneDimension)
        },
        (context, inputs) -> {
          finishRequestCallCount.incrementAndGet();
          requireAccess(context, certificateDirectoryAccess);

          NodeId applicationId = (NodeId) inputs[0].value();
          NodeId requestId = (NodeId) inputs[1].value();

          ApplicationRecordDataType record = requireApplication(applicationId);

          SigningRequest request = requests.get(requestId);
          if (request == null || !request.applicationId().equals(applicationId)) {
            throw new UaException(StatusCodes.Bad_NotFound);
          }
          if (rejectRequests) {
            throw new UaException(StatusCodes.Bad_RequestNotAllowed);
          }
          if (request.polls().getAndIncrement() < pollsBeforeIssued) {
            throw new UaException(StatusCodes.Bad_NothingToDo);
          }

          try {
            X509Certificate certificate;
            // The reference GDS answers a signing request with an empty ByteString rather than a
            // null Variant, so that is what a signing request returns here.
            ByteString privateKey = ByteString.NULL_VALUE;

            if (request.csr() != null) {
              certificate = sign(request.csr());
            } else {
              KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
              certificate =
                  sign(
                      CertificateUtil.generateCsr(
                          keyPair,
                          "CN=" + record.getApplicationUri(),
                          record.getApplicationUri(),
                          List.of(),
                          List.of(),
                          "SHA256withRSA"));
              privateKey = ByteString.of(keyPair.getPrivate().getEncoded());
            }

            issued
                .computeIfAbsent(applicationId, k -> new CopyOnWriteArrayList<>())
                .add(certificate);
            requests.remove(requestId);

            return new Variant[] {
              Variant.ofByteString(ByteString.of(certificate.getEncoded())),
              Variant.ofByteString(privateKey),
              Variant.ofByteStringArray(
                  new ByteString[] {ByteString.of(caCertificate.getEncoded())})
            };
          } catch (Exception e) {
            throw new UaException(StatusCodes.Bad_InternalError, e);
          }
        });

    addMethod(
        directory,
        nodeId(GdsNodeIds.Directory_GetCertificateGroups),
        newQualifiedName("GetCertificateGroups"),
        new Argument[] {argument("ApplicationId", NodeIds.NodeId, ValueRanks.Scalar)},
        new Argument[] {argument("CertificateGroupIds", NodeIds.NodeId, ValueRanks.OneDimension)},
        (context, inputs) -> {
          requireAccess(context, certificateDirectoryAccess);

          requireApplication((NodeId) inputs[0].value());

          return new Variant[] {
            Variant.ofNodeIdArray(
                new NodeId[] {
                  nodeId(GdsNodeIds.Directory_CertificateGroups_DefaultApplicationGroup),
                  nodeId(GdsNodeIds.Directory_CertificateGroups_DefaultUserTokenGroup)
                })
          };
        });

    addMethod(
        directory,
        nodeId(GdsNodeIds.Directory_GetCertificates),
        newQualifiedName("GetCertificates"),
        new Argument[] {
          argument("ApplicationId", NodeIds.NodeId, ValueRanks.Scalar),
          argument("CertificateGroupId", NodeIds.NodeId, ValueRanks.Scalar)
        },
        new Argument[] {
          argument("CertificateTypeIds", NodeIds.NodeId, ValueRanks.OneDimension),
          argument("Certificates", NodeIds.ByteString, ValueRanks.OneDimension)
        },
        (context, inputs) -> {
          requireAccess(context, certificateDirectoryAccess);

          NodeId applicationId = (NodeId) inputs[0].value();
          requireApplication(applicationId);

          List<X509Certificate> certificates = issued.getOrDefault(applicationId, List.of());
          var typeIds = new NodeId[certificates.size()];
          var encoded = new ByteString[certificates.size()];

          try {
            for (int i = 0; i < certificates.size(); i++) {
              typeIds[i] = NodeIds.RsaSha256ApplicationCertificateType;
              encoded[i] = ByteString.of(certificates.get(i).getEncoded());
            }
          } catch (Exception e) {
            throw new UaException(StatusCodes.Bad_InternalError, e);
          }

          return new Variant[] {Variant.ofNodeIdArray(typeIds), Variant.ofByteStringArray(encoded)};
        });

    addMethod(
        directory,
        nodeId(GdsNodeIds.Directory_GetTrustList),
        newQualifiedName("GetTrustList"),
        new Argument[] {
          argument("ApplicationId", NodeIds.NodeId, ValueRanks.Scalar),
          argument("CertificateGroupId", NodeIds.NodeId, ValueRanks.Scalar)
        },
        new Argument[] {argument("TrustListId", NodeIds.NodeId, ValueRanks.Scalar)},
        (context, inputs) -> {
          requireAccess(context, certificateDirectoryAccess);

          requireApplication((NodeId) inputs[0].value());

          NodeId groupId = (NodeId) inputs[1].value();

          if (groupId == null
              || groupId.isNull()
              || groupId.equals(
                  nodeId(GdsNodeIds.Directory_CertificateGroups_DefaultApplicationGroup))) {
            return new Variant[] {
              Variant.ofNodeId(
                  nodeId(GdsNodeIds.Directory_CertificateGroups_DefaultApplicationGroup_TrustList))
            };
          } else if (groupId.equals(
              nodeId(GdsNodeIds.Directory_CertificateGroups_DefaultUserTokenGroup))) {
            return new Variant[] {
              Variant.ofNodeId(
                  nodeId(GdsNodeIds.Directory_CertificateGroups_DefaultUserTokenGroup_TrustList))
            };
          } else {
            throw new UaException(StatusCodes.Bad_NotFound);
          }
        });

    addMethod(
        directory,
        nodeId(GdsNodeIds.Directory_GetCertificateStatus),
        newQualifiedName("GetCertificateStatus"),
        new Argument[] {
          argument("ApplicationId", NodeIds.NodeId, ValueRanks.Scalar),
          argument("CertificateGroupId", NodeIds.NodeId, ValueRanks.Scalar),
          argument("CertificateTypeId", NodeIds.NodeId, ValueRanks.Scalar)
        },
        new Argument[] {argument("UpdateRequired", NodeIds.Boolean, ValueRanks.Scalar)},
        (context, inputs) -> {
          requireAccess(context, certificateDirectoryAccess);

          requireApplication((NodeId) inputs[0].value());

          return new Variant[] {Variant.ofBoolean(updateRequired)};
        });

    addMethod(
        directory,
        nodeId(GdsNodeIds.Directory_RevokeCertificate),
        newQualifiedName("RevokeCertificate"),
        new Argument[] {
          argument("ApplicationId", NodeIds.NodeId, ValueRanks.Scalar),
          argument("Certificate", NodeIds.ByteString, ValueRanks.Scalar)
        },
        new Argument[0],
        (context, inputs) -> {
          requireAccess(context, certificateDirectoryAccess);

          requireApplication((NodeId) inputs[0].value());

          revoked.add((ByteString) inputs[1].value());

          return new Variant[0];
        });

    addMethod(
        directory,
        nodeId(GdsNodeIds.Directory_CheckRevocationStatus),
        newQualifiedName("CheckRevocationStatus"),
        new Argument[] {argument("Certificate", NodeIds.ByteString, ValueRanks.Scalar)},
        new Argument[] {
          argument("CertificateStatus", NodeIds.StatusCode, ValueRanks.Scalar),
          argument("ValidityTime", NodeIds.UtcTime, ValueRanks.Scalar)
        },
        (context, inputs) -> {
          requireAccess(context, certificateDirectoryAccess);

          ByteString certificate = (ByteString) inputs[0].value();

          StatusCode status =
              revoked.contains(certificate)
                  ? new StatusCode(StatusCodes.Bad_CertificateRevoked)
                  : StatusCode.GOOD;

          return new Variant[] {
            Variant.ofStatusCode(status),
            Variant.ofDateTime(new DateTime(new Date(System.currentTimeMillis() + 3_600_000)))
          };
        });
  }

  @FunctionalInterface
  private interface MethodBody {
    Variant[] invoke(InvocationContext context, Variant[] inputs) throws UaException;
  }

  private void addMethod(
      UaObjectNode parent,
      NodeId nodeId,
      QualifiedName browseName,
      Argument[] inputArguments,
      Argument[] outputArguments,
      MethodBody body) {

    UaMethodNode methodNode =
        UaMethodNode.builder(getNodeContext())
            .setNodeId(nodeId)
            .setBrowseName(browseName)
            .setDisplayName(LocalizedText.english(browseName.name()))
            .build();

    methodNode.addReference(
        new Reference(
            nodeId,
            NodeIds.HasComponent,
            parent.getNodeId().expanded(),
            Reference.Direction.INVERSE));

    methodNode.setInputArguments(inputArguments);
    methodNode.setOutputArguments(outputArguments);
    methodNode.setInvocationHandler(
        new AbstractMethodInvocationHandler(methodNode) {
          @Override
          public Argument[] getInputArguments() {
            return inputArguments;
          }

          @Override
          public Argument[] getOutputArguments() {
            return outputArguments;
          }

          @Override
          protected Variant[] invoke(InvocationContext invocationContext, Variant[] inputValues)
              throws UaException {
            return body.invoke(invocationContext, inputValues);
          }
        });

    getNodeManager().addNode(methodNode);
  }

  private static Argument argument(String name, NodeId dataTypeId, int valueRank) {
    return new Argument(name, dataTypeId, valueRank, null, LocalizedText.NULL_VALUE);
  }

  private static void requireAccess(InvocationContext context, MethodAccess access)
      throws UaException {

    boolean allowed =
        switch (access) {
          case ANYONE -> true;
          case CREDENTIALED ->
              context
                  .getSession()
                  .map(session -> session.getTokenType() == UserTokenType.UserName)
                  .orElse(false);
          case NOBODY -> false;
        };

    if (!allowed) {
      throw new UaException(StatusCodes.Bad_UserAccessDenied);
    }
  }

  private static @Nullable NodeId nonNullNodeId(@Nullable NodeId nodeId) {
    return nodeId == null || nodeId.isNull() ? null : nodeId;
  }

  private ApplicationRecordDataType requireApplication(@Nullable NodeId applicationId)
      throws UaException {

    ApplicationRecordDataType record =
        applicationId != null ? applications.get(applicationId) : null;

    if (record == null) {
      throw new UaException(StatusCodes.Bad_NotFound);
    }

    return record;
  }

  private static ApplicationRecordDataType applicationArgument(Variant input) throws UaException {
    if (input.value() instanceof ApplicationRecordDataType record) {
      return record;
    } else {
      throw new UaException(StatusCodes.Bad_InvalidArgument);
    }
  }

  private static ApplicationRecordDataType withId(
      ApplicationRecordDataType record, NodeId applicationId) {

    return new ApplicationRecordDataType(
        applicationId,
        record.getApplicationUri(),
        record.getApplicationType(),
        record.getApplicationNames(),
        record.getProductUri(),
        record.getDiscoveryUrls(),
        record.getServerCapabilities());
  }

  private static ApplicationDescription toDescription(ApplicationRecordDataType record) {
    LocalizedText[] names = record.getApplicationNames();

    return new ApplicationDescription(
        record.getApplicationUri(),
        record.getProductUri(),
        names != null && names.length > 0 ? names[0] : LocalizedText.NULL_VALUE,
        record.getApplicationType(),
        null,
        null,
        record.getDiscoveryUrls());
  }

  private static PKCS10CertificationRequest parseCsr(@Nullable ByteString certificateRequest)
      throws UaException {

    try {
      return new PKCS10CertificationRequest(
          certificateRequest != null ? certificateRequest.bytesOrEmpty() : new byte[0]);
    } catch (Exception e) {
      throw new UaException(StatusCodes.Bad_InvalidArgument, "certificateRequest", e);
    }
  }

  private static @Nullable String sanUri(PKCS10CertificationRequest csr) {
    Extensions extensions = requestedExtensions(csr);
    GeneralNames names =
        extensions != null
            ? GeneralNames.fromExtensions(extensions, Extension.subjectAlternativeName)
            : null;

    if (names == null) {
      return null;
    }

    return Arrays.stream(names.getNames())
        .filter(n -> n.getTagNo() == GeneralName.uniformResourceIdentifier)
        .map(n -> n.getName().toString())
        .findFirst()
        .orElse(null);
  }

  private static @Nullable Extensions requestedExtensions(PKCS10CertificationRequest csr) {
    Attribute[] attributes = csr.getAttributes(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest);

    if (attributes.length == 0) {
      return null;
    }

    return Extensions.getInstance(attributes[0].getAttrValues().getObjectAt(0));
  }

  private X509Certificate sign(PKCS10CertificationRequest csr) throws Exception {
    var builder =
        new X509v3CertificateBuilder(
            X500Name.getInstance(caCertificate.getSubjectX500Principal().getEncoded()),
            BigInteger.valueOf(System.nanoTime()),
            new Date(System.currentTimeMillis() - 60_000),
            new Date(System.currentTimeMillis() + 365L * 24 * 3_600_000),
            csr.getSubject(),
            csr.getSubjectPublicKeyInfo());

    Extensions extensions = requestedExtensions(csr);
    Extension san =
        extensions != null ? extensions.getExtension(Extension.subjectAlternativeName) : null;
    if (san != null) {
      builder.addExtension(san);
    }

    return new JcaX509CertificateConverter()
        .getCertificate(
            builder.build(
                new JcaContentSignerBuilder("SHA256withRSA").build(caKeyPair.getPrivate())));
  }

  static byte[] encodeBare(TrustListDataType trustList) {
    ByteBuf buffer = Unpooled.buffer();
    try {
      var encoder = new OpcUaBinaryEncoder(DefaultEncodingContext.INSTANCE).setBuffer(buffer);
      new TrustListDataType.Codec().encodeType(DefaultEncodingContext.INSTANCE, encoder, trustList);
      return ByteBufUtil.getBytes(buffer);
    } finally {
      buffer.release();
    }
  }

  /** The Directory's DefaultApplicationGroup id in the server's namespace table. */
  public NodeId defaultApplicationGroupId() {
    return nodeId(GdsNodeIds.Directory_CertificateGroups_DefaultApplicationGroup);
  }

  public NodeId defaultUserTokenGroupId() {
    return nodeId(GdsNodeIds.Directory_CertificateGroups_DefaultUserTokenGroup);
  }

  public NodeId defaultApplicationGroupTrustListId() {
    return nodeId(GdsNodeIds.Directory_CertificateGroups_DefaultApplicationGroup_TrustList);
  }

  public NodeId defaultUserTokenGroupTrustListId() {
    return nodeId(GdsNodeIds.Directory_CertificateGroups_DefaultUserTokenGroup_TrustList);
  }
}
