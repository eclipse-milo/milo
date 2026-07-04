/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.config;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.ServerTable;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingManager;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder;
import org.eclipse.milo.opcua.stack.core.types.DataTypeDictionary;
import org.eclipse.milo.opcua.stack.core.types.DataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetReaderDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetReaderMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetReaderTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetWriterDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetWriterMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetWriterTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReaderGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReaderGroupMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReaderGroupTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscribedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UABinaryFileDataType;
import org.jspecify.annotations.Nullable;

/**
 * Encodes and decodes Part 14 binary PubSub configuration files: a {@code UABinaryFileDataType}
 * container (Part 5 Table 88) whose {@code Body} is a {@link PubSubConfiguration2DataType} wrapped
 * in an ExtensionObject.
 *
 * <p>Two API levels are offered:
 *
 * <ul>
 *   <li><b>DataType level</b> ({@link #encodeDataType} / {@link #decodeDataType}): the raw
 *       container codec, e.g. for CloseAndUpdate file buffers. No {@link PubSubConfig} validation
 *       is applied, so content that a valid config could never produce (duplicate names, dangling
 *       references) passes through untouched. A non-empty namespaces header must match the
 *       context's namespace table positionally.
 *   <li><b>{@link PubSubConfig} level</b> ({@link #encode} / {@link #decode} / {@link #write} /
 *       {@link #read}): a portable file convenience that delegates through {@link
 *       PubSubConfig#toDataType} / {@link PubSubConfig#fromDataType}. A non-empty namespaces header
 *       supplies the decode-side namespace table, so namespace-URI field addresses survive a round
 *       trip between peers with differently ordered namespace tables.
 * </ul>
 *
 * <p>Container shape: the Table 88 {@code Namespaces} header carries the encoding context's
 * namespace table minus namespace 0 in order ({@code namespaces[k]} corresponds to index {@code k +
 * 1}; "the OPC UA namespace is skipped" — the Milo reading of Table 88); all other header fields
 * are null; the {@code Body} Variant holds the configuration ExtensionObject. The outer structure
 * is serialized directly in OPC UA Binary, with no ExtensionObject wrapper. Trailing bytes after
 * the outer structure are ignored on decode, never an error.
 *
 * <p>The {@code Body} must be a {@link PubSubConfiguration2DataType}; the base {@link
 * PubSubConfigurationDataType} named by Table 88 is rejected with {@code Bad_TypeMismatch} at both
 * API levels.
 *
 * <p>Failure contract: a malformed stream, wrong outer structure, wrong {@code Body} type, or
 * namespaces-header mismatch throws {@link UaException} with {@code Bad_TypeMismatch}; semantic
 * config invalidity (builder validation, unresolvable namespace URIs or indices) throws {@link
 * PubSubConfigValidationException}.
 *
 * <p>Null optional settings: the abstract-typed {@code TransportSettings}/{@code MessageSettings}
 * members of DataSetWriters, ReaderGroups, and DataSetReaders are legitimately absent (Part 14
 * defines no datagram DataSetWriter/DataSetReader transport type and no concrete ReaderGroup
 * transport or message type), and are encoded as null ExtensionObjects and decoded back to null at
 * both API levels.
 */
public final class PubSubConfigFiles {

  /** The conventional file extension of Part 14 binary PubSub configuration files. */
  public static final String FILE_EXTENSION = ".uabinary";

  /** The MIME type of the file content (Part 5 §12.36). */
  public static final String MIME_TYPE = "application/opcua+uabinary";

  private PubSubConfigFiles() {}

  /**
   * Encode {@code config} as a binary PubSub configuration file.
   *
   * <p>DataType level: no {@link PubSubConfig} validation is applied. The Table 88 namespaces
   * header is {@code context}'s namespace table minus namespace 0, in order.
   *
   * @param config the configuration to encode.
   * @param context the {@link EncodingContext} used to encode the container and its body.
   * @return the encoded file bytes.
   */
  public static byte[] encodeDataType(
      PubSubConfiguration2DataType config, EncodingContext context) {
    String[] tableUris = context.getNamespaceTable().toArray();
    String[] namespaces = Arrays.copyOfRange(tableUris, 1, tableUris.length);

    EncodingContext fileContext = fileEncodingContext(context);

    var file =
        new UABinaryFileDataType(
            namespaces,
            null,
            null,
            null,
            null,
            null,
            Variant.ofExtensionObject(ExtensionObject.encode(fileContext, config)));

    ByteBuf buffer = Unpooled.buffer();
    try {
      OpcUaBinaryEncoder encoder = new OpcUaBinaryEncoder(fileContext).setBuffer(buffer);
      encoder.encodeStruct(null, file, UABinaryFileDataType.TYPE_ID);

      byte[] fileBytes = new byte[buffer.readableBytes()];
      buffer.readBytes(fileBytes);
      return fileBytes;
    } finally {
      buffer.release();
    }
  }

  /**
   * Decode a binary PubSub configuration file to its {@link PubSubConfiguration2DataType}.
   *
   * <p>DataType level: no {@link PubSubConfig} validation is applied. An empty or null namespaces
   * header is accepted; a non-empty header must match {@code context}'s namespace table
   * positionally (namespace 0 skipped: header entry {@code k} against table index {@code k + 1}).
   * Trailing bytes after the outer structure are ignored.
   *
   * @param fileBytes the file bytes to decode.
   * @param context the {@link EncodingContext} used to decode the container and its body.
   * @return the decoded {@link PubSubConfiguration2DataType}.
   * @throws UaException with {@code Bad_TypeMismatch} if the stream is malformed, the content is
   *     not a {@code UABinaryFileDataType}, the {@code Body} is not a {@link
   *     PubSubConfiguration2DataType} (including the base {@link PubSubConfigurationDataType}), or
   *     a non-empty namespaces header does not match the context's namespace table.
   */
  public static PubSubConfiguration2DataType decodeDataType(
      byte[] fileBytes, EncodingContext context) throws UaException {

    DecodedFile file = decodeFile(fileBytes, context);

    String[] namespaces = file.namespaces();
    if (namespaces != null && namespaces.length > 0) {
      NamespaceTable namespaceTable = context.getNamespaceTable();

      for (int i = 0; i < namespaces.length; i++) {
        String uri = namespaceTable.get(i + 1);
        if (uri == null || !uri.equals(namespaces[i])) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "namespaces header entry %d (%s) does not match namespace table index %d (%s)"
                  .formatted(i, namespaces[i], i + 1, uri));
        }
      }
    }

    return file.config();
  }

  /**
   * Encode {@code config} as a binary PubSub configuration file.
   *
   * <p>{@link PubSubConfig} level: delegates through {@link PubSubConfig#toDataType} against {@code
   * context}'s namespace table, which is also written as the Table 88 namespaces header (minus
   * namespace 0, in order).
   *
   * @param config the configuration to encode.
   * @param context the {@link EncodingContext} used to encode the container and its body.
   * @return the encoded file bytes.
   * @throws PubSubConfigValidationException if a namespace URI used by a field address cannot be
   *     resolved against {@code context}'s namespace table.
   */
  public static byte[] encode(PubSubConfig config, EncodingContext context)
      throws PubSubConfigValidationException {

    return encodeDataType(config.toDataType(context.getNamespaceTable()), context);
  }

  /**
   * Decode a binary PubSub configuration file to a {@link PubSubConfig}.
   *
   * <p>{@link PubSubConfig} level: a non-empty namespaces header supplies the decode-side namespace
   * table (namespace 0 followed by the header URIs at indices 1..n) passed to {@link
   * PubSubConfig#fromDataType}, so namespace-URI field addresses are portable regardless of the
   * namespace table order of the encoding peer; an empty or null header falls back to {@code
   * context}'s namespace table. Trailing bytes after the outer structure are ignored.
   *
   * @param fileBytes the file bytes to decode.
   * @param context the {@link EncodingContext} used to decode the container and its body.
   * @return the decoded {@link PubSubConfig}.
   * @throws UaException with {@code Bad_TypeMismatch} if the stream is malformed, the content is
   *     not a {@code UABinaryFileDataType}, the {@code Body} is not a {@link
   *     PubSubConfiguration2DataType} (including the base {@link PubSubConfigurationDataType}), or
   *     the namespaces header is itself malformed (null or duplicate entries).
   * @throws PubSubConfigValidationException if the decoded configuration does not map to a valid
   *     config, e.g. builder validation failures or namespace indices unresolvable against the
   *     decode-side namespace table.
   */
  public static PubSubConfig decode(byte[] fileBytes, EncodingContext context)
      throws UaException, PubSubConfigValidationException {

    DecodedFile file = decodeFile(fileBytes, context);

    String[] namespaces = file.namespaces();

    NamespaceTable namespaceTable;
    if (namespaces == null || namespaces.length == 0) {
      namespaceTable = context.getNamespaceTable();
    } else {
      namespaceTable = headerNamespaceTable(namespaces);
    }

    return PubSubConfig.fromDataType(file.config(), namespaceTable);
  }

  /**
   * Encode {@code config} as a binary PubSub configuration file and write it to {@code file}.
   *
   * <p>The {@value #FILE_EXTENSION} extension is the Part 14 convention, documented but never
   * enforced. See {@link #encode(PubSubConfig, EncodingContext)} for the encoding contract.
   *
   * @param file the path to write to; an existing file is replaced.
   * @param config the configuration to encode.
   * @param context the {@link EncodingContext} used to encode the container and its body.
   * @throws IOException if writing {@code file} fails.
   * @throws PubSubConfigValidationException if a namespace URI used by a field address cannot be
   *     resolved against {@code context}'s namespace table.
   */
  public static void write(Path file, PubSubConfig config, EncodingContext context)
      throws IOException, PubSubConfigValidationException {

    Files.write(file, encode(config, context));
  }

  /**
   * Read a binary PubSub configuration file from {@code file} and decode it to a {@link
   * PubSubConfig}.
   *
   * <p>See {@link #decode(byte[], EncodingContext)} for the decoding and failure contract.
   *
   * @param file the path to read from.
   * @param context the {@link EncodingContext} used to decode the container and its body.
   * @return the decoded {@link PubSubConfig}.
   * @throws IOException if reading {@code file} fails.
   * @throws UaException with {@code Bad_TypeMismatch} if the content is malformed; see {@link
   *     #decode(byte[], EncodingContext)}.
   * @throws PubSubConfigValidationException if the decoded configuration does not map to a valid
   *     config.
   */
  public static PubSubConfig read(Path file, EncodingContext context)
      throws IOException, UaException, PubSubConfigValidationException {

    return decode(Files.readAllBytes(file), context);
  }

  /**
   * Decode the outer {@code UABinaryFileDataType} and its {@code Body}, applying the checks common
   * to both API levels; header handling is the caller's.
   */
  private static DecodedFile decodeFile(byte[] fileBytes, EncodingContext context)
      throws UaException {

    EncodingContext fileContext = fileEncodingContext(context);

    UABinaryFileDataType file;
    ByteBuf buffer = Unpooled.wrappedBuffer(fileBytes);
    try {
      OpcUaBinaryDecoder decoder = new OpcUaBinaryDecoder(fileContext).setBuffer(buffer);
      file = (UABinaryFileDataType) decoder.decodeStruct(null, UABinaryFileDataType.TYPE_ID);
    } catch (UaSerializationException | IndexOutOfBoundsException e) {
      throw new UaException(
          StatusCodes.Bad_TypeMismatch, "content does not decode as UABinaryFileDataType", e);
    } finally {
      buffer.release();
    }

    if (!(file.getBody().getValue() instanceof ExtensionObject xo)) {
      throw new UaException(StatusCodes.Bad_TypeMismatch, "Body is not an ExtensionObject");
    }

    UaStructuredType decoded;
    try {
      decoded = xo.decode(fileContext);
    } catch (UaSerializationException e) {
      throw new UaException(
          StatusCodes.Bad_TypeMismatch, "Body does not decode as PubSubConfiguration2DataType", e);
    }

    if (decoded instanceof PubSubConfiguration2DataType config) {
      return new DecodedFile(file.getNamespaces(), config);
    } else if (decoded instanceof PubSubConfigurationDataType) {
      throw new UaException(
          StatusCodes.Bad_TypeMismatch,
          "Body is the base PubSubConfigurationDataType; PubSubConfiguration2DataType is required");
    } else {
      throw new UaException(
          StatusCodes.Bad_TypeMismatch,
          "Body is not a PubSubConfiguration2DataType: " + decoded.getClass().getName());
    }
  }

  /**
   * Build the decode-side namespace table from a non-empty namespaces header: namespace 0 followed
   * by the header URIs at indices 1..n.
   */
  private static NamespaceTable headerNamespaceTable(String[] namespaces) throws UaException {
    var namespaceTable = new NamespaceTable();

    for (int i = 0; i < namespaces.length; i++) {
      String uri = namespaces[i];
      if (uri == null || namespaceTable.getIndex(uri) != null) {
        throw new UaException(
            StatusCodes.Bad_TypeMismatch,
            "namespaces header entry %d is null or a duplicate: %s".formatted(i, uri));
      }
      namespaceTable.add(uri);
    }

    return namespaceTable;
  }

  private record DecodedFile(String @Nullable [] namespaces, PubSubConfiguration2DataType config) {}

  // region null-tolerant codec overrides

  /**
   * Codecs overriding the generated ones for the three configuration structures whose optional
   * abstract-typed members are legitimately null (see the class Javadoc): the generated codecs
   * reject null in those slots, so these field-identical replacements encode null as a null
   * ExtensionObject and decode a null ExtensionObject back to null. Keyed by both the DataType
   * NodeId (struct-field resolution) and the binary encoding NodeId (ExtensionObject resolution).
   */
  private static final Map<NodeId, DataTypeCodec> OVERRIDE_CODECS = overrideCodecs();

  private static Map<NodeId, DataTypeCodec> overrideCodecs() {
    var codecs = new HashMap<NodeId, DataTypeCodec>();

    registerOverride(
        codecs,
        new NullTolerantDataSetWriterCodec(),
        DataSetWriterDataType.TYPE_ID,
        DataSetWriterDataType.BINARY_ENCODING_ID);
    registerOverride(
        codecs,
        new NullTolerantReaderGroupCodec(),
        ReaderGroupDataType.TYPE_ID,
        ReaderGroupDataType.BINARY_ENCODING_ID);
    registerOverride(
        codecs,
        new NullTolerantDataSetReaderCodec(),
        DataSetReaderDataType.TYPE_ID,
        DataSetReaderDataType.BINARY_ENCODING_ID);

    return Map.copyOf(codecs);
  }

  private static void registerOverride(
      Map<NodeId, DataTypeCodec> codecs, DataTypeCodec codec, ExpandedNodeId... ids) {

    var namespaceTable = new NamespaceTable();
    for (ExpandedNodeId id : ids) {
      codecs.put(id.toNodeId(namespaceTable).orElseThrow(), codec);
    }
  }

  /**
   * Wrap {@code context} so codec lookups consult {@link #OVERRIDE_CODECS} first and delegate
   * everything else, leaving the caller's context untouched.
   */
  private static EncodingContext fileEncodingContext(EncodingContext context) {
    DataTypeManager delegate = context.getDataTypeManager();

    var dataTypeManager =
        new DataTypeManager() {
          @Override
          public void registerType(
              NodeId dataTypeId,
              DataTypeCodec codec,
              @Nullable NodeId binaryEncodingId,
              @Nullable NodeId xmlEncodingId,
              @Nullable NodeId jsonEncodingId) {
            delegate.registerType(
                dataTypeId, codec, binaryEncodingId, xmlEncodingId, jsonEncodingId);
          }

          @Override
          public @Nullable DataTypeCodec getCodec(NodeId id) {
            DataTypeCodec override = OVERRIDE_CODECS.get(id);
            return override != null ? override : delegate.getCodec(id);
          }

          @Override
          public @Nullable NodeId getBinaryEncodingId(NodeId dataTypeId) {
            return delegate.getBinaryEncodingId(dataTypeId);
          }

          @Override
          public @Nullable NodeId getXmlEncodingId(NodeId dataTypeId) {
            return delegate.getXmlEncodingId(dataTypeId);
          }

          @Override
          public @Nullable NodeId getJsonEncodingId(NodeId dataTypeId) {
            return delegate.getJsonEncodingId(dataTypeId);
          }

          @Override
          public @Nullable DataTypeDictionary getTypeDictionary(String namespaceUri) {
            return delegate.getTypeDictionary(namespaceUri);
          }

          @Override
          public void registerTypeDictionary(DataTypeDictionary dictionary) {
            delegate.registerTypeDictionary(dictionary);
          }
        };

    return new EncodingContext() {
      @Override
      public DataTypeManager getDataTypeManager() {
        return dataTypeManager;
      }

      @Override
      public EncodingManager getEncodingManager() {
        return context.getEncodingManager();
      }

      @Override
      public EncodingLimits getEncodingLimits() {
        return context.getEncodingLimits();
      }

      @Override
      public NamespaceTable getNamespaceTable() {
        return context.getNamespaceTable();
      }

      @Override
      public ServerTable getServerTable() {
        return context.getServerTable();
      }
    };
  }

  /** Encode a nullable abstract-typed settings member: null becomes a null ExtensionObject. */
  private static void encodeNullableSettings(
      EncodingContext context, UaEncoder encoder, String field, @Nullable UaStructuredType value) {

    encoder.encodeExtensionObject(
        field, value != null ? ExtensionObject.encode(context, value) : null);
  }

  /** Decode a nullable abstract-typed settings member: a null ExtensionObject becomes null. */
  private static @Nullable UaStructuredType decodeNullableSettings(
      EncodingContext context, UaDecoder decoder, String field) {

    ExtensionObject xo = decoder.decodeExtensionObject(field);
    return xo == null || xo.isNull() ? null : xo.decode(context);
  }

  private static final class NullTolerantDataSetWriterCodec
      extends GenericDataTypeCodec<DataSetWriterDataType> {

    @Override
    public Class<DataSetWriterDataType> getType() {
      return DataSetWriterDataType.class;
    }

    @Override
    public DataSetWriterDataType decodeType(EncodingContext context, UaDecoder decoder) {
      String name = decoder.decodeString("Name");
      Boolean enabled = decoder.decodeBoolean("Enabled");
      UShort dataSetWriterId = decoder.decodeUInt16("DataSetWriterId");
      var dataSetFieldContentMask =
          new DataSetFieldContentMask(decoder.decodeUInt32("DataSetFieldContentMask"));
      UInteger keyFrameCount = decoder.decodeUInt32("KeyFrameCount");
      String dataSetName = decoder.decodeString("DataSetName");
      KeyValuePair[] dataSetWriterProperties =
          (KeyValuePair[])
              decoder.decodeStructArray("DataSetWriterProperties", KeyValuePair.TYPE_ID);
      var transportSettings =
          (DataSetWriterTransportDataType)
              decodeNullableSettings(context, decoder, "TransportSettings");
      var messageSettings =
          (DataSetWriterMessageDataType)
              decodeNullableSettings(context, decoder, "MessageSettings");
      return new DataSetWriterDataType(
          name,
          enabled,
          dataSetWriterId,
          dataSetFieldContentMask,
          keyFrameCount,
          dataSetName,
          dataSetWriterProperties,
          transportSettings,
          messageSettings);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, DataSetWriterDataType value) {
      encoder.encodeString("Name", value.getName());
      encoder.encodeBoolean("Enabled", value.getEnabled());
      encoder.encodeUInt16("DataSetWriterId", value.getDataSetWriterId());
      encoder.encodeUInt32(
          "DataSetFieldContentMask", value.getDataSetFieldContentMask().getValue());
      encoder.encodeUInt32("KeyFrameCount", value.getKeyFrameCount());
      encoder.encodeString("DataSetName", value.getDataSetName());
      encoder.encodeStructArray(
          "DataSetWriterProperties", value.getDataSetWriterProperties(), KeyValuePair.TYPE_ID);
      encodeNullableSettings(context, encoder, "TransportSettings", value.getTransportSettings());
      encodeNullableSettings(context, encoder, "MessageSettings", value.getMessageSettings());
    }
  }

  private static final class NullTolerantReaderGroupCodec
      extends GenericDataTypeCodec<ReaderGroupDataType> {

    @Override
    public Class<ReaderGroupDataType> getType() {
      return ReaderGroupDataType.class;
    }

    @Override
    public ReaderGroupDataType decodeType(EncodingContext context, UaDecoder decoder) {
      String name = decoder.decodeString("Name");
      Boolean enabled = decoder.decodeBoolean("Enabled");
      MessageSecurityMode securityMode =
          MessageSecurityMode.from(decoder.decodeEnum("SecurityMode"));
      String securityGroupId = decoder.decodeString("SecurityGroupId");
      EndpointDescription[] securityKeyServices =
          (EndpointDescription[])
              decoder.decodeStructArray("SecurityKeyServices", EndpointDescription.TYPE_ID);
      UInteger maxNetworkMessageSize = decoder.decodeUInt32("MaxNetworkMessageSize");
      KeyValuePair[] groupProperties =
          (KeyValuePair[]) decoder.decodeStructArray("GroupProperties", KeyValuePair.TYPE_ID);
      var transportSettings =
          (ReaderGroupTransportDataType)
              decodeNullableSettings(context, decoder, "TransportSettings");
      var messageSettings =
          (ReaderGroupMessageDataType) decodeNullableSettings(context, decoder, "MessageSettings");
      DataSetReaderDataType[] dataSetReaders =
          (DataSetReaderDataType[])
              decoder.decodeStructArray("DataSetReaders", DataSetReaderDataType.TYPE_ID);
      return new ReaderGroupDataType(
          name,
          enabled,
          securityMode,
          securityGroupId,
          securityKeyServices,
          maxNetworkMessageSize,
          groupProperties,
          transportSettings,
          messageSettings,
          dataSetReaders);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, ReaderGroupDataType value) {
      encoder.encodeString("Name", value.getName());
      encoder.encodeBoolean("Enabled", value.getEnabled());
      encoder.encodeEnum("SecurityMode", value.getSecurityMode());
      encoder.encodeString("SecurityGroupId", value.getSecurityGroupId());
      encoder.encodeStructArray(
          "SecurityKeyServices", value.getSecurityKeyServices(), EndpointDescription.TYPE_ID);
      encoder.encodeUInt32("MaxNetworkMessageSize", value.getMaxNetworkMessageSize());
      encoder.encodeStructArray(
          "GroupProperties", value.getGroupProperties(), KeyValuePair.TYPE_ID);
      encodeNullableSettings(context, encoder, "TransportSettings", value.getTransportSettings());
      encodeNullableSettings(context, encoder, "MessageSettings", value.getMessageSettings());
      encoder.encodeStructArray(
          "DataSetReaders", value.getDataSetReaders(), DataSetReaderDataType.TYPE_ID);
    }
  }

  private static final class NullTolerantDataSetReaderCodec
      extends GenericDataTypeCodec<DataSetReaderDataType> {

    @Override
    public Class<DataSetReaderDataType> getType() {
      return DataSetReaderDataType.class;
    }

    @Override
    public DataSetReaderDataType decodeType(EncodingContext context, UaDecoder decoder) {
      String name = decoder.decodeString("Name");
      Boolean enabled = decoder.decodeBoolean("Enabled");
      Variant publisherId = decoder.decodeVariant("PublisherId");
      UShort writerGroupId = decoder.decodeUInt16("WriterGroupId");
      UShort dataSetWriterId = decoder.decodeUInt16("DataSetWriterId");
      var dataSetMetaData =
          (DataSetMetaDataType)
              decoder.decodeStruct("DataSetMetaData", DataSetMetaDataType.TYPE_ID);
      var dataSetFieldContentMask =
          new DataSetFieldContentMask(decoder.decodeUInt32("DataSetFieldContentMask"));
      Double messageReceiveTimeout = decoder.decodeDouble("MessageReceiveTimeout");
      UInteger keyFrameCount = decoder.decodeUInt32("KeyFrameCount");
      String headerLayoutUri = decoder.decodeString("HeaderLayoutUri");
      MessageSecurityMode securityMode =
          MessageSecurityMode.from(decoder.decodeEnum("SecurityMode"));
      String securityGroupId = decoder.decodeString("SecurityGroupId");
      EndpointDescription[] securityKeyServices =
          (EndpointDescription[])
              decoder.decodeStructArray("SecurityKeyServices", EndpointDescription.TYPE_ID);
      KeyValuePair[] dataSetReaderProperties =
          (KeyValuePair[])
              decoder.decodeStructArray("DataSetReaderProperties", KeyValuePair.TYPE_ID);
      var transportSettings =
          (DataSetReaderTransportDataType)
              decodeNullableSettings(context, decoder, "TransportSettings");
      var messageSettings =
          (DataSetReaderMessageDataType)
              decodeNullableSettings(context, decoder, "MessageSettings");
      var subscribedDataSet =
          (SubscribedDataSetDataType) decodeNullableSettings(context, decoder, "SubscribedDataSet");
      return new DataSetReaderDataType(
          name,
          enabled,
          publisherId,
          writerGroupId,
          dataSetWriterId,
          dataSetMetaData,
          dataSetFieldContentMask,
          messageReceiveTimeout,
          keyFrameCount,
          headerLayoutUri,
          securityMode,
          securityGroupId,
          securityKeyServices,
          dataSetReaderProperties,
          transportSettings,
          messageSettings,
          subscribedDataSet);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, DataSetReaderDataType value) {
      encoder.encodeString("Name", value.getName());
      encoder.encodeBoolean("Enabled", value.getEnabled());
      encoder.encodeVariant("PublisherId", value.getPublisherId());
      encoder.encodeUInt16("WriterGroupId", value.getWriterGroupId());
      encoder.encodeUInt16("DataSetWriterId", value.getDataSetWriterId());
      encoder.encodeStruct(
          "DataSetMetaData", value.getDataSetMetaData(), DataSetMetaDataType.TYPE_ID);
      encoder.encodeUInt32(
          "DataSetFieldContentMask", value.getDataSetFieldContentMask().getValue());
      encoder.encodeDouble("MessageReceiveTimeout", value.getMessageReceiveTimeout());
      encoder.encodeUInt32("KeyFrameCount", value.getKeyFrameCount());
      encoder.encodeString("HeaderLayoutUri", value.getHeaderLayoutUri());
      encoder.encodeEnum("SecurityMode", value.getSecurityMode());
      encoder.encodeString("SecurityGroupId", value.getSecurityGroupId());
      encoder.encodeStructArray(
          "SecurityKeyServices", value.getSecurityKeyServices(), EndpointDescription.TYPE_ID);
      encoder.encodeStructArray(
          "DataSetReaderProperties", value.getDataSetReaderProperties(), KeyValuePair.TYPE_ID);
      encodeNullableSettings(context, encoder, "TransportSettings", value.getTransportSettings());
      encodeNullableSettings(context, encoder, "MessageSettings", value.getMessageSettings());
      encodeNullableSettings(context, encoder, "SubscribedDataSet", value.getSubscribedDataSet());
    }
  }

  // endregion
}
