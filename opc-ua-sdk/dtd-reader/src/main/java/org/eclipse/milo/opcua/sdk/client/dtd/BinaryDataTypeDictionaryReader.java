/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.dtd;

import static java.util.Objects.requireNonNull;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.util.FutureUtils.failedFuture;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;
import com.google.common.primitives.Ints;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import jakarta.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.core.dtd.BinaryDataTypeCodec;
import org.eclipse.milo.opcua.sdk.core.dtd.BinaryDataTypeDictionary;
import org.eclipse.milo.opcua.sdk.core.dtd.BsdParser;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.DataTypeDictionary;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.*;
import org.eclipse.milo.opcua.stack.core.util.FutureUtils;
import org.eclipse.milo.opcua.stack.core.util.Lists;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.opcfoundation.opcua.binaryschema.StructuredType;
import org.opcfoundation.opcua.binaryschema.TypeDictionary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinaryDataTypeDictionaryReader {

  private static final int DEFAULT_FRAGMENT_SIZE = 8192;
  private static final int PARTITION_SIZE = 64;
  private static final QualifiedName QN_DEFAULT_BINARY = new QualifiedName(0, "Default Binary");

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final OpcUaClient client;

  public BinaryDataTypeDictionaryReader(OpcUaClient client) {
    this.client = client;
  }

  public List<DataTypeDictionary> readDataTypeDictionaries(BinaryCodecFactory codecFactory)
      throws UaException {
    try {
      return readDataTypeDictionariesAsync(codecFactory).get();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    } catch (ExecutionException e) {
      throw UaException.extract(e)
          .orElseThrow(() -> new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  public CompletableFuture<List<DataTypeDictionary>> readDataTypeDictionariesAsync(
      BinaryCodecFactory codecFactory) {
    CompletableFuture<List<ReferenceDescription>> browseFuture =
        browseNode(
            new BrowseDescription(
                NodeIds.OPCBinarySchema_TypeSystem,
                BrowseDirection.Forward,
                NodeIds.HasComponent,
                false,
                uint(NodeClass.Variable.getValue()),
                uint(BrowseResultMask.All.getValue())));

    CompletableFuture<Stream<NodeId>> dictionaryNodeIds =
        browseFuture.thenApply(
            references ->
                references.stream()
                    .filter(r -> r.getTypeDefinition().equalTo(NodeIds.DataTypeDictionaryType))
                    .flatMap(r -> r.getNodeId().toNodeId(client.getNamespaceTable()).stream()));

    return dictionaryNodeIds
        .thenApply(
            nodeIds -> nodeIds.map(this::readDataTypeDictionary).collect(Collectors.toList()))
        .thenCompose(FutureUtils::sequence)
        .thenApply(
            list ->
                list.stream()
                    .filter(Objects::nonNull)
                    .map(info -> createDataTypeDictionary(info, codecFactory))
                    .collect(Collectors.toList()));
  }

  private DataTypeDictionary createDataTypeDictionary(
      TypeDictionaryInfo info, BinaryCodecFactory codecFactory) {
    var dictionary = new BinaryDataTypeDictionary(info.typeDictionary);

    Map<String, StructuredType> structuredTypes = new HashMap<>();

    info.typeDictionary.getOpaqueTypeOrEnumeratedTypeOrStructuredType().stream()
        .filter(typeDescription -> typeDescription instanceof StructuredType)
        .map(StructuredType.class::cast)
        .forEach(structuredType -> structuredTypes.put(structuredType.getName(), structuredType));

    info.structEncodingInfos.forEach(
        structEncodingInfo -> {
          if (structEncodingInfo == null) {
            System.out.println("sei null");
            return;
          }
          if (structEncodingInfo.description == null) {
            System.out.println("sei desc null");
            return;
          }

          StructuredType structuredType = structuredTypes.get(structEncodingInfo.description);

          BinaryDataTypeCodec codec =
              codecFactory.createCodec(
                  info.typeDictionary.getTargetNamespace(),
                  structEncodingInfo.dataTypeId,
                  structEncodingInfo.encodingId,
                  structuredType);

          BinaryDataTypeDictionary.BinaryType binaryType =
              new BinaryDataTypeDictionary.BinaryType(
                  structEncodingInfo.description,
                  structEncodingInfo.dataTypeId,
                  structEncodingInfo.encodingId,
                  codec);

          dictionary.registerType(binaryType);
        });

    return dictionary;
  }

  private CompletableFuture<TypeDictionaryInfo> readDataTypeDictionary(NodeId nodeId) {
    logger.debug("Reading DataTypeDictionary nodeId={}", nodeId);

    return readDataTypeDictionaryBytes(nodeId, DEFAULT_FRAGMENT_SIZE)
        .thenCompose(bs -> createTypeDictionaryInfo(nodeId, bs))
        .exceptionally(
            ex -> {
              logger.warn("Failed to create DataTypeDictionary nodeId={}", nodeId, ex);
              return null;
            });
  }

  CompletableFuture<ByteString> readDataTypeDictionaryBytes(NodeId nodeId, int fragmentSize) {
    if (NodeIds.OpcUa_BinarySchema.equals(nodeId)) {
      try (InputStream inputStream =
          BinaryDataTypeDictionaryReader.class.getResourceAsStream("/Opc.Ua.Types.bsd")) {

        assert inputStream != null;

        ByteString bs = ByteString.of(ByteStreams.toByteArray(inputStream));

        return completedFuture(bs);
      } catch (IOException e) {
        return failedFuture(e);
      }
    } else {
      CompositeByteBuf fragmentBuffer = Unpooled.compositeBuffer();

      CompletableFuture<ByteBuf> future = readFragments(nodeId, fragmentBuffer, fragmentSize, 0);

      return future.thenApply(
          buffer -> {
            // trim any junk at the end. some servers have a bug
            // that cause a null byte to be appended to the end,
            // which makes it invalid XML.
            int length = buffer.readableBytes();

            for (int i = buffer.writerIndex() - 1; i >= 0; i--) {
              byte lastByte = buffer.getByte(i);

              boolean empty =
                  (lastByte == 0
                      || Character.isWhitespace(lastByte)
                      || Character.isSpaceChar(lastByte));

              if (!empty) break;
              else length -= 1;
            }

            byte[] bs = new byte[length];
            buffer.readBytes(bs, 0, length);

            if (logger.isDebugEnabled()) {
              String xmlString = new String(bs);
              logger.debug("Dictionary XML: {}", xmlString);
            }

            return ByteString.of(bs);
          });
    }
  }

  private CompletableFuture<ByteBuf> readFragments(
      NodeId nodeId, CompositeByteBuf fragmentBuffer, int fragmentSize, int index) {

    Preconditions.checkArgument(fragmentSize > 0, "fragmentSize=" + fragmentSize);

    String indexRange =
        fragmentSize == 1
            ? String.valueOf(index)
            : String.format("%d:%d", index, index + fragmentSize - 1);

    CompletableFuture<DataValue> valueFuture =
        readNode(
            new ReadValueId(nodeId, AttributeId.Value.uid(), indexRange, QualifiedName.NULL_VALUE));

    return valueFuture.thenComposeAsync(
        value -> {
          StatusCode statusCode = value.statusCode();

          if (statusCode == null || statusCode.isGood()) {
            ByteString fragmentBytes = (ByteString) value.value().value();

            if (fragmentBytes != null) {
              int bytesRead = fragmentBytes.length();

              if (bytesRead > 0) {
                fragmentBuffer.addComponent(Unpooled.wrappedBuffer(fragmentBytes.bytesOrEmpty()));
                fragmentBuffer.writerIndex(fragmentBuffer.writerIndex() + bytesRead);
              }

              if (bytesRead < fragmentSize) {
                // A partial fragment means this is the last read that will
                // succeed; don't bother trying to read the next fragment.
                return completedFuture(fragmentBuffer);
              } else if (bytesRead > fragmentSize) {
                // Some servers don't support index range properly and just
                // return the entire contents. when this happens, we can assume
                // we've read everything there is to read.
                // An edge case where the dictionary size is exactly equal to the
                // fragment size still exists. In this case we must hope the server
                // properly terminates the subsequent request with something like
                // Bad_IndexRangeNoData or else the infinite loop could still happen.
                return completedFuture(fragmentBuffer);
              } else {
                return readFragments(nodeId, fragmentBuffer, fragmentSize, index + bytesRead);
              }
            } else {
              logger.warn(
                  "Read a null type dictionary " + "fragment at indexRange=\"{}\"", indexRange);

              return completedFuture(fragmentBuffer);
            }
          } else {
            if (statusCode.value() != StatusCodes.Bad_IndexRangeNoData) {
              logger.warn(
                  "Reading type dictionary fragments expected to "
                      + "terminate with Bad_IndexRangeNoData but got {}",
                  statusCode);
            }

            return completedFuture(fragmentBuffer);
          }
        },
        client.getTransport().getConfig().getExecutor());
  }

  private CompletableFuture<TypeDictionaryInfo> createTypeDictionaryInfo(
      NodeId dictionaryNodeId, ByteString bs) {
    ByteArrayInputStream is = new ByteArrayInputStream(bs.bytesOrEmpty());

    try {
      TypeDictionary typeDictionary = BsdParser.parse(is);

      return readStructEncodingInfos(dictionaryNodeId, typeDictionary)
          .thenApply(
              structEncodingInfos -> new TypeDictionaryInfo(typeDictionary, structEncodingInfos));
    } catch (JAXBException e) {
      return failedFuture(e);
    }
  }

  public CompletableFuture<List<StructEncodingInfo>> readStructEncodingInfos(
      NodeId dictionaryNodeId, TypeDictionary typeDictionary) {

    List<StructuredType> structuredTypes =
        typeDictionary.getOpaqueTypeOrEnumeratedTypeOrStructuredType().stream()
            .filter(typeDescription -> typeDescription instanceof StructuredType)
            .map(StructuredType.class::cast)
            .collect(Collectors.toList());

    String namespaceUri = typeDictionary.getTargetNamespace();

    if (Namespaces.OPC_UA.equals(namespaceUri)) {
      return readBuiltinStructEncodingInfos(structuredTypes);
    } else {
      return readCustomStructEncodingInfos(dictionaryNodeId, structuredTypes);
    }
  }

  private CompletableFuture<List<StructEncodingInfo>> readBuiltinStructEncodingInfos(
      List<StructuredType> structuredTypes) {

    List<StructEncodingInfo> structEncodingInfos =
        structuredTypes.stream()
            .map(
                structuredType -> {
                  String description = structuredType.getName();

                  BinaryDataTypeInfo.DataTypeInfo dataTypeInfo =
                      BinaryDataTypeInfo.getDataTypeInfo(description);

                  if (dataTypeInfo != null) {
                    return new StructEncodingInfo(
                        description, dataTypeInfo.dataTypeId, dataTypeInfo.encodingId);
                  } else {
                    // expected for some types
                    logger.debug("no builtin DataTypeInfo found: " + description);
                    return null;
                  }
                })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

    return CompletableFuture.completedFuture(structEncodingInfos);
  }

  private CompletableFuture<List<StructEncodingInfo>> readCustomStructEncodingInfos(
      NodeId dictionaryNodeId, List<StructuredType> structuredTypes) {

    CompletableFuture<List<NodeId>> descriptionNodeIds =
        browseDataTypeDescriptionNodeIds(dictionaryNodeId);

    CompletableFuture<List<String>> descriptionValues =
        descriptionNodeIds.thenCompose(this::readDataTypeDescriptionValues);

    if (logger.isTraceEnabled()) {
      try {
        List<NodeId> ids = descriptionNodeIds.get();
        List<String> values = descriptionValues.get();

        if (ids.size() != values.size()) {
          throw new IllegalStateException("size mismatch");
        }

        for (int i = 0; i < ids.size(); i++) {
          NodeId id = ids.get(i);
          String value = values.get(i);

          logger.trace("description NodeId={} value={}", id, value);
        }
      } catch (Exception e) {
        logger.error("Error reading description NodeIds", e);
      }
    }

    CompletableFuture<List<NodeId>> encodingIdsFuture =
        descriptionNodeIds.thenCompose(this::browseDataTypeEncodingNodeIds);

    return encodingIdsFuture.thenCompose(
        encodingIds ->
            browseDataTypeIds(encodingIds)
                .thenCompose(
                    dataTypeIds ->
                        descriptionValues.thenApply(
                            descriptions -> {
                              Map<String, NodeId> encodingIdMap = new HashMap<>();
                              Map<String, NodeId> dataTypeIdMap = new HashMap<>();

                              if (descriptions.size() != encodingIds.size()) {
                                throw new IllegalStateException(
                                    String.format(
                                        "descriptions.size() != encodingIds.size() (%s != %s)",
                                        descriptions.size(), encodingIds.size()));
                              }

                              if (encodingIds.size() != dataTypeIds.size()) {
                                throw new IllegalStateException(
                                    String.format(
                                        "encodingIds.size() != dataTypeIds.size() (%s != %s)",
                                        encodingIds.size(), dataTypeIds.size()));
                              }

                              Iterator<String> descriptionIter = descriptions.iterator();
                              Iterator<NodeId> encodingIdIter = encodingIds.iterator();
                              Iterator<NodeId> dataTypeIdIter = dataTypeIds.iterator();

                              while (descriptionIter.hasNext()
                                  && encodingIdIter.hasNext()
                                  && dataTypeIdIter.hasNext()) {
                                String description = descriptionIter.next();
                                encodingIdMap.put(description, encodingIdIter.next());
                                dataTypeIdMap.put(description, dataTypeIdIter.next());
                              }

                              return structuredTypes.stream()
                                  .map(
                                      structuredType -> {
                                        String description = structuredType.getName();
                                        NodeId encodingId = encodingIdMap.get(description);
                                        NodeId dataTypeId = dataTypeIdMap.get(description);

                                        if (encodingId == null || encodingId.isNull()) {
                                          if (dataTypeId != null
                                              && dataTypeId.getNamespaceIndex().intValue() != 0) {
                                            logger.warn(
                                                "encodingId is null for description={}",
                                                description);
                                          } else {
                                            // There's a number of missing structures in the
                                            // built-in type dictionary;
                                            // namely the service request and response structures.
                                            // It's expected that
                                            // we won't be able to create codecs for these.
                                            logger.debug(
                                                "dataTypeId and encodingId is null for"
                                                    + " description={}",
                                                description);
                                          }
                                          return null;
                                        } else if (dataTypeId == null || dataTypeId.isNull()) {
                                          logger.warn(
                                              "dataTypeId is null for description={}", description);

                                          return null;
                                        } else {
                                          logger.debug(
                                              "Found description={} dataTypeId={} encodingId={}",
                                              description,
                                              dataTypeId,
                                              encodingId);

                                          return new StructEncodingInfo(
                                              description, dataTypeId, encodingId);
                                        }
                                      })
                                  .filter(Objects::nonNull)
                                  .collect(Collectors.toList());
                            })));
  }

  private CompletableFuture<List<NodeId>> browseDataTypeDescriptionNodeIds(
      NodeId dictionaryNodeId) {
    CompletableFuture<List<ReferenceDescription>> browseResult =
        browseNode(
            new BrowseDescription(
                dictionaryNodeId,
                BrowseDirection.Forward,
                NodeIds.HasComponent,
                false,
                uint(NodeClass.Variable.getValue()),
                uint(BrowseResultMask.All.getValue())));

    return browseResult.thenApply(
        references ->
            references.stream()
                .filter(r -> NodeIds.DataTypeDescriptionType.equalTo(r.getTypeDefinition()))
                .flatMap(r -> r.getNodeId().toNodeId(client.getNamespaceTable()).stream())
                .collect(Collectors.toList()));
  }

  private CompletableFuture<List<String>> readDataTypeDescriptionValues(List<NodeId> nodeIds) {
    CompletableFuture<UInteger> maxNodesPerRead =
        readNode(
                new ReadValueId(
                    NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerRead,
                    AttributeId.Value.uid(),
                    null,
                    QualifiedName.NULL_VALUE))
            .thenApply(dv -> (UInteger) dv.value().value());

    CompletableFuture<Integer> getPartitionSize =
        maxNodesPerRead
            .thenApply(m -> Math.max(1, Ints.saturatedCast(m.longValue())))
            .exceptionally(ex -> PARTITION_SIZE);

    return getPartitionSize.thenCompose(
        partitionSize -> {
          Stream<List<NodeId>> partitions = Lists.partition(nodeIds, partitionSize);

          CompletableFuture<List<List<DataValue>>> sequence =
              FutureUtils.sequence(
                  partitions.map(
                      list -> {
                        List<ReadValueId> readValueIds =
                            list.stream()
                                .map(
                                    nodeId ->
                                        new ReadValueId(
                                            nodeId,
                                            AttributeId.Value.uid(),
                                            null,
                                            QualifiedName.NULL_VALUE))
                                .collect(Collectors.toList());

                        return readNodes(readValueIds);
                      }));

          return sequence.thenApply(
              values ->
                  values.stream()
                      .flatMap(List::stream)
                      .map(v -> (String) v.value().value())
                      .collect(Collectors.toList()));
        });
  }

  private CompletableFuture<List<NodeId>> browseDataTypeEncodingNodeIds(
      List<NodeId> descriptionNodeIds) {
    Stream<CompletableFuture<NodeId>> futures =
        descriptionNodeIds.stream()
            .map(
                nodeId -> {
                  CompletableFuture<List<ReferenceDescription>> browse =
                      browseNode(
                          new BrowseDescription(
                              nodeId,
                              BrowseDirection.Inverse,
                              NodeIds.HasDescription,
                              false,
                              uint(NodeClass.Object.getValue()),
                              uint(BrowseResultMask.All.getValue())));

                  return browse.thenApply(
                      references -> {
                        Optional<ReferenceDescription> ref =
                            references.stream()
                                .filter(
                                    r ->
                                        QN_DEFAULT_BINARY.equals(r.getBrowseName())
                                            && NodeIds.DataTypeEncodingType.equalTo(
                                                r.getTypeDefinition()))
                                .findFirst();

                        return ref.map(
                                r ->
                                    r.getNodeId()
                                        .toNodeId(client.getNamespaceTable())
                                        .orElse(NodeId.NULL_VALUE))
                            .orElse(NodeId.NULL_VALUE);
                      });
                });

    return FutureUtils.sequence(futures);
  }

  private CompletableFuture<List<NodeId>> browseDataTypeIds(List<NodeId> dataTypeEncodingIds) {
    Stream<CompletableFuture<NodeId>> futures =
        dataTypeEncodingIds.stream()
            .map(
                nodeId -> {
                  CompletableFuture<List<ReferenceDescription>> browse =
                      browseNode(
                          new BrowseDescription(
                              nodeId,
                              BrowseDirection.Inverse,
                              NodeIds.HasEncoding,
                              false,
                              uint(NodeClass.DataType.getValue()),
                              uint(BrowseResultMask.All.getValue())));

                  return browse.thenApply(
                      references -> {
                        Optional<ReferenceDescription> ref = references.stream().findFirst();

                        return ref.map(
                                r ->
                                    r.getNodeId()
                                        .toNodeId(client.getNamespaceTable())
                                        .orElse(NodeId.NULL_VALUE))
                            .orElse(NodeId.NULL_VALUE);
                      });
                });

    return FutureUtils.sequence(futures);
  }

  private CompletableFuture<List<ReferenceDescription>> browseNode(
      BrowseDescription browseDescription) {
    return client
        .browseAsync(browseDescription)
        .thenApply(result -> Arrays.asList(requireNonNull(result.getReferences())));
  }

  private CompletionStage<List<ReferenceDescription>> maybeBrowseNext(
      BrowseResult result, List<ReferenceDescription> references) {

    if (result.getStatusCode().isGood()) {
      ReferenceDescription[] rds = result.getReferences();
      if (rds != null) Collections.addAll(references, rds);

      ByteString continuationPoint = result.getContinuationPoint();

      if (continuationPoint.isNotNull()) {
        logger.debug("Continuation point was non-null; calling BrowseNext");

        return browseNextAsync(continuationPoint, references);
      } else {
        logger.debug("Browse finished with {} references", references.size());

        return completedFuture(references);
      }
    } else {
      return completedFuture(references);
    }
  }

  private CompletableFuture<List<ReferenceDescription>> browseNextAsync(
      ByteString continuationPoint, List<ReferenceDescription> references) {

    return client
        .browseNextAsync(false, List.of(continuationPoint))
        .thenApply(BrowseNextResponse::getResults)
        .thenCompose(result -> maybeBrowseNext(result[0], references));
  }

  private CompletableFuture<DataValue> readNode(ReadValueId readValueId) {
    return readNodes(List.of(readValueId)).thenApply(values -> values.get(0));
  }

  private CompletableFuture<List<DataValue>> readNodes(List<ReadValueId> readValueIds) {
    return client
        .getSessionAsync()
        .thenCompose(
            session -> {
              RequestHeader requestHeader =
                  client.newRequestHeader(
                      session.getAuthenticationToken(), client.getConfig().getRequestTimeout());

              ReadRequest readRequest =
                  new ReadRequest(
                      requestHeader,
                      0.0,
                      TimestampsToReturn.Neither,
                      readValueIds.toArray(new ReadValueId[0]));

              return client
                  .getTransport()
                  .sendRequestMessage(readRequest)
                  .thenApply(ReadResponse.class::cast)
                  .thenApply(
                      r -> org.eclipse.milo.opcua.stack.core.util.Lists.ofNullable(r.getResults()));
            });
  }

  public static class TypeDictionaryInfo {

    public final TypeDictionary typeDictionary;
    public final List<StructEncodingInfo> structEncodingInfos;

    public TypeDictionaryInfo(
        TypeDictionary typeDictionary, List<StructEncodingInfo> structEncodingInfos) {
      this.typeDictionary = typeDictionary;
      this.structEncodingInfos = structEncodingInfos;
    }
  }

  public static class StructEncodingInfo {

    public final String description;
    public final NodeId dataTypeId;
    public final NodeId encodingId;

    public StructEncodingInfo(String description, NodeId dataTypeId, NodeId encodingId) {
      this.description = description;
      this.dataTypeId = dataTypeId;
      this.encodingId = encodingId;
    }
  }
}
