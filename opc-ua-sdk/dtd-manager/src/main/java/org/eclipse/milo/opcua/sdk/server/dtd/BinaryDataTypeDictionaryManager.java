/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.dtd;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Objects.requireNonNullElse;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import jakarta.xml.bind.JAXBException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.xml.namespace.QName;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.Reference.Direction;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.core.dtd.BinaryDataTypeCodec;
import org.eclipse.milo.opcua.sdk.core.dtd.BinaryDataTypeDictionary;
import org.eclipse.milo.opcua.sdk.core.dtd.BsdGenerator;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.Lifecycle;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataTypeEncodingTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.DataTypeDescriptionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.DataTypeDictionaryType;
import org.eclipse.milo.opcua.sdk.server.model.variables.DataTypeDictionaryTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilters;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumField;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.eclipse.milo.opcua.stack.core.util.Lazy;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.opcfoundation.opcua.binaryschema.ByteOrder;
import org.opcfoundation.opcua.binaryschema.EnumeratedType;
import org.opcfoundation.opcua.binaryschema.EnumeratedValue;
import org.opcfoundation.opcua.binaryschema.FieldType;
import org.opcfoundation.opcua.binaryschema.ImportDirective;
import org.opcfoundation.opcua.binaryschema.StructuredType;
import org.opcfoundation.opcua.binaryschema.TypeDictionary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinaryDataTypeDictionaryManager implements Lifecycle {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final List<UaNode> nodes = new CopyOnWriteArrayList<>();

  private final Lazy<File> dictionaryFile = new Lazy<>();

  private final Map<NodeId, StructureDescription> structureDescriptions = new ConcurrentHashMap<>();

  private final BinaryDataTypeDictionary dataTypeDictionary;

  private DataTypeDictionaryTypeNode dictionaryNode;

  private final TypeDictionary bsdTypeDictionary;

  private final UaNodeContext context;
  private final String namespaceUri;

  /**
   * Create a DataTypeDictionaryManager for a {@link BinaryDataTypeDictionary} in {@code
   * namespaceUri}.
   *
   * <p>Note that the namespace URI is that of the dictionary, and is not necessarily the same as
   * the namespace the Nodes created by the manager will reside in, i.e. datatype dictionaries are
   * namespaced independently of the namespaces server Nodes reside in.
   *
   * @param context a {@link UaNodeContext}. Nodes will be created and added using this context.
   * @param namespaceUri the namespace URI of the dictionary.
   */
  public BinaryDataTypeDictionaryManager(UaNodeContext context, String namespaceUri) {
    this.context = context;
    this.namespaceUri = namespaceUri;

    bsdTypeDictionary = new TypeDictionary();
    bsdTypeDictionary.setTargetNamespace(namespaceUri);
    bsdTypeDictionary.setDefaultByteOrder(ByteOrder.LITTLE_ENDIAN);

    dataTypeDictionary = new BinaryDataTypeDictionary(bsdTypeDictionary);
  }

  private UaNodeManager getNodeManager() {
    return (UaNodeManager) context.getNodeManager();
  }

  @Override
  public void startup() {
    context.getServer().getDataTypeManager().registerTypeDictionary(dataTypeDictionary);

    // Add a DataTypeDictionary Node...
    dictionaryNode =
        new DataTypeDictionaryTypeNode(
            context,
            newNodeId(namespaceUri),
            newQualifiedName(namespaceUri),
            LocalizedText.english(namespaceUri),
            LocalizedText.english("DataTypeDictionary for " + namespaceUri),
            uint(0),
            uint(0),
            null,
            null,
            null,
            new DataValue(Variant.NULL_VALUE),
            NodeIds.BaseDataType,
            ValueRanks.Scalar,
            null);

    dictionaryNode.setNamespaceUri(namespaceUri);

    dictionaryNode
        .getFilterChain()
        .addLast(
            AttributeFilters.getValue(
                context -> {
                  try {
                    File file =
                        dictionaryFile.get(
                            () -> {
                              try {
                                return writeDictionaryToFile();
                              } catch (IOException e) {
                                throw new RuntimeException("failed to write dictionary file", e);
                              }
                            });

                    assert file != null;

                    try {
                      byte[] bs = Files.readAllBytes(file.toPath());
                      return new DataValue(new Variant(ByteString.of(bs)));
                    } catch (IOException e) {
                      logger.warn("Failed to read dictionary file", e);

                      dictionaryFile.reset();

                      byte[] bs = writeDictionaryToMemory();
                      return new DataValue(new Variant(ByteString.of(bs)));
                    }
                  } catch (Throwable t) {
                    logger.warn("Failed to write dictionary file", t);

                    return new DataValue(new Variant(ByteString.NULL_VALUE));
                  }
                }));

    dictionaryNode.addReference(
        new Reference(
            dictionaryNode.getNodeId(),
            NodeIds.HasTypeDefinition,
            NodeIds.DataTypeDictionaryType.expanded(),
            Direction.FORWARD));

    dictionaryNode.addReference(
        new Reference(
            dictionaryNode.getNodeId(),
            NodeIds.HasComponent,
            NodeIds.OPCBinarySchema_TypeSystem.expanded(),
            Direction.INVERSE));

    addNode(dictionaryNode);
  }

  @Override
  public void shutdown() {
    dictionaryNode.delete();
    nodes.forEach(UaNode::delete);
    nodes.clear();
  }

  public BinaryDataTypeDictionary getDataTypeDictionary() {
    return dataTypeDictionary;
  }

  public void registerEnum(EnumDescription description) {
    // Note: enumerations don't need DataTypeDescription or DataTypeEncoding nodes.
    // Add a StructuredType to the BSD TypeDictionary
    EnumeratedType enumeratedType = createBsdEnumeratedType(description);
    dataTypeDictionary.addTypeDescription(enumeratedType);
    bsdTypeDictionary.getOpaqueTypeOrEnumeratedTypeOrStructuredType().add(enumeratedType);

    dictionaryFile.reset();
  }

  public void registerStructure(
      String name,
      NodeId dataTypeId,
      NodeId binaryEncodingId,
      BinaryDataTypeCodec codec,
      StructureDescription description) {

    BinaryDataTypeDictionary.BinaryType binaryType =
        new BinaryDataTypeDictionary.BinaryType(name, dataTypeId, binaryEncodingId, codec);

    dataTypeDictionary.registerType(binaryType);

    structureDescriptions.put(description.getDataTypeId(), description);

    // Add a DataTypeDescriptionTypeNode with a ComponentOf reference to
    // dictionaryNode.

    DataTypeDescriptionTypeNode descriptionNode =
        new DataTypeDescriptionTypeNode(
            context,
            newNodeId(String.format("%s.Description", description.getName().getName())),
            newQualifiedName(description.getName().getName()),
            LocalizedText.english(description.getName().getName()),
            LocalizedText.NULL_VALUE,
            uint(0),
            uint(0),
            null,
            null,
            null,
            new DataValue(Variant.NULL_VALUE),
            NodeIds.BaseDataType,
            ValueRanks.Scalar,
            null);

    descriptionNode.setValue(new DataValue(new Variant(description.getName().getName())));
    descriptionNode.setDataType(NodeIds.String);

    descriptionNode.addReference(
        new Reference(
            descriptionNode.getNodeId(),
            NodeIds.HasTypeDefinition,
            NodeIds.DataTypeDescriptionType.expanded(),
            Direction.FORWARD));

    descriptionNode.addReference(
        new Reference(
            descriptionNode.getNodeId(),
            NodeIds.HasComponent,
            dictionaryNode.getNodeId().expanded(),
            Direction.INVERSE));

    addNode(descriptionNode);

    // Add a DataTypeEncodingTypeNode with a HasDescription reference to
    // descriptionNode and an EncodingOf reference to the DataTypeNode.

    DataTypeEncodingTypeNode dataTypeEncodingNode =
        new DataTypeEncodingTypeNode(
            context,
            binaryEncodingId,
            new QualifiedName(0, "Default Binary"),
            LocalizedText.english("Default Binary"),
            LocalizedText.NULL_VALUE,
            uint(0),
            uint(0),
            null,
            null,
            null);

    dataTypeEncodingNode.addReference(
        new Reference(
            dataTypeEncodingNode.getNodeId(),
            NodeIds.HasTypeDefinition,
            NodeIds.DataTypeEncodingType.expanded(),
            Direction.FORWARD));

    dataTypeEncodingNode.addReference(
        new Reference(
            dataTypeEncodingNode.getNodeId(),
            NodeIds.HasDescription,
            descriptionNode.getNodeId().expanded(),
            Direction.FORWARD));

    dataTypeEncodingNode.addReference(
        new Reference(
            dataTypeEncodingNode.getNodeId(),
            NodeIds.HasEncoding,
            description.getDataTypeId().expanded(),
            Direction.INVERSE));

    addNode(dataTypeEncodingNode);

    // Add a StructuredType to the BSD TypeDictionary
    StructuredType structuredType = createBsdStructuredType(description);
    dataTypeDictionary.addTypeDescription(structuredType);
    bsdTypeDictionary.getOpaqueTypeOrEnumeratedTypeOrStructuredType().add(structuredType);

    dictionaryFile.reset();

    context
        .getServer()
        .getDataTypeManager()
        .registerType(dataTypeId, codec, binaryEncodingId, null, null);
  }

  private File writeDictionaryToFile() throws IOException {
    String encodedUri = URLEncoder.encode(namespaceUri, StandardCharsets.UTF_8);
    Path tempFilePath = Files.createTempFile(encodedUri, ".bsd.xml");

    try (FileOutputStream fos = new FileOutputStream(tempFilePath.toFile())) {
      writeDictionaryToStream(fos);

      logger.info("Wrote dictionary for '{}' to {}", namespaceUri, tempFilePath);
    }

    return tempFilePath.toFile();
  }

  private byte[] writeDictionaryToMemory() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    writeDictionaryToStream(baos);

    return baos.toByteArray();
  }

  private void writeDictionaryToStream(OutputStream outputStream) throws IOException {
    try {
      BsdGenerator.generate(bsdTypeDictionary, outputStream);
    } catch (JAXBException e) {
      throw new IOException(e);
    }
  }

  private UShort getNamespaceIndex() {
    return context.getNamespaceTable().getIndex(namespaceUri);
  }

  private NodeId newNodeId(String id) {
    return new NodeId(getNamespaceIndex(), id);
  }

  private QualifiedName newQualifiedName(String name) {
    return new QualifiedName(getNamespaceIndex(), name);
  }

  /**
   * Add {@code node} to the {@link UaNodeManager} and our own bookkeeping so it can be deleted
   * during shutdown.
   *
   * @param node the {@link UaNode} to add.
   */
  private void addNode(UaNode node) {
    getNodeManager().addNode(node);
    nodes.add(node);
  }

  private EnumeratedType createBsdEnumeratedType(EnumDescription description) {
    QualifiedName name = description.getName();
    EnumDefinition definition = description.getEnumDefinition();

    EnumeratedType enumeratedType = new EnumeratedType();
    enumeratedType.setName(name.getName());
    enumeratedType.setLengthInBits(32);

    for (EnumField field : requireNonNullElse(definition.getFields(), new EnumField[0])) {
      EnumeratedValue enumeratedValue = new EnumeratedValue();
      enumeratedValue.setName(field.getName());
      enumeratedValue.setValue(field.getValue().intValue());

      enumeratedType.getEnumeratedValue().add(enumeratedValue);
    }

    return enumeratedType;
  }

  private StructuredType createBsdStructuredType(StructureDescription description) {
    QualifiedName name = description.getName();

    StructureDefinition definition = description.getStructureDefinition();
    StructureType structureType = definition.getStructureType();

    StructuredType structuredType = new StructuredType();
    structuredType.setName(name.getName());

    // Create a combined list of StructuredFields from all parent types
    LinkedList<StructureDefinition> definitions = new LinkedList<>();
    definitions.addFirst(definition);

    NodeId baseDataTypeId = definition.getBaseDataType();

    while (baseDataTypeId != null
        && baseDataTypeId.isNotNull()
        && !NodeIds.Structure.equals(baseDataTypeId)
        && !NodeIds.Union.equals(baseDataTypeId)) {

      StructureDescription baseDescription = structureDescriptions.get(baseDataTypeId);
      StructureDefinition baseDefinition = baseDescription.getStructureDefinition();

      definitions.addFirst(baseDefinition);

      baseDataTypeId = baseDefinition.getBaseDataType();
    }

    LinkedHashMap<String, StructureField> allFields = new LinkedHashMap<>();

    for (StructureDefinition d : definitions) {
      for (StructureField f : requireNonNullElse(d.getFields(), new StructureField[0])) {
        allFields.put(f.getName(), f);
      }
    }

    List<StructureField> fields = new ArrayList<>(allFields.values());

    if (structureType == StructureType.StructureWithOptionalFields) {
      int optionalFieldCount = 0;

      for (StructureField field : fields) {
        if (field.getIsOptional()) {
          optionalFieldCount++;

          FieldType fieldType = new FieldType();
          fieldType.setName(field.getName() + "Present");
          fieldType.setTypeName(new QName(Namespaces.OPC_UA_BSD, "Bit"));

          structuredType.getField().add(fieldType);
        }
      }

      if (optionalFieldCount > 0) {
        int reservedFieldCount = (optionalFieldCount + 31) / 32;

        for (int i = 0; i < reservedFieldCount; i++) {
          long reservedBits = 32 - optionalFieldCount;
          optionalFieldCount -= 32;

          FieldType fieldType = new FieldType();
          fieldType.setLength(reservedBits);
          fieldType.setName("Reserved" + i);
          fieldType.setTypeName(new QName(Namespaces.OPC_UA_BSD, "Bit"));

          structuredType.getField().add(fieldType);
        }
      }
    } else if (structureType == StructureType.Union) {
      FieldType fieldType = new FieldType();
      fieldType.setName("SwitchField");
      fieldType.setTypeName(new QName(Namespaces.OPC_UA_BSD, "UInt32"));

      structuredType.getField().add(fieldType);
    }

    long switchValue = 0L;

    for (StructureField field : fields) {
      String fieldName = field.getName();
      NodeId fieldDataTypeId = field.getDataType();

      QualifiedTypeName qualifiedTypeName = getDataTypeLocation(fieldDataTypeId);

      String dataTypeName = qualifiedTypeName.dataTypeName;
      String dictionaryNamespaceUri = qualifiedTypeName.dictionaryNamespaceUri;

      if (!dictionaryNamespaceUri.isEmpty()) {
        boolean registeredNamespace =
            bsdTypeDictionary.getImport().stream()
                .anyMatch(id -> Objects.equals(id.getNamespace(), dictionaryNamespaceUri));
        if (!registeredNamespace) {
          ImportDirective importDirective = new ImportDirective();
          importDirective.setNamespace(dictionaryNamespaceUri);
          bsdTypeDictionary.getImport().add(importDirective);
        }
      }

      FieldType fieldType = new FieldType();
      fieldType.setName(fieldName);
      if (dictionaryNamespaceUri.isEmpty()) {
        fieldType.setTypeName(new QName(namespaceUri, dataTypeName));
      } else {
        fieldType.setTypeName(new QName(dictionaryNamespaceUri, dataTypeName));
      }

      if (structureType == StructureType.StructureWithOptionalFields) {
        if (field.getIsOptional()) {
          fieldType.setSwitchField(fieldName + "Present");
        }
      } else if (structureType == StructureType.Union) {
        fieldType.setSwitchField("SwitchField");
        fieldType.setSwitchValue(++switchValue);
      }

      if (field.getValueRank() >= 1) {
        // Fixed-dimension array... specify a LengthField
        FieldType lengthFieldType = new FieldType();
        lengthFieldType.setName(fieldName + "Length");
        lengthFieldType.setTypeName(new QName(Namespaces.OPC_UA_BSD, "Int32"));

        if (structureType == StructureType.StructureWithOptionalFields) {
          if (field.getIsOptional()) {
            lengthFieldType.setSwitchField(fieldName + "Present");
          }
        } else if (structureType == StructureType.Union) {
          fieldType.setSwitchField("SwitchField");
          fieldType.setSwitchValue(switchValue);
        }

        structuredType.getField().add(lengthFieldType);

        fieldType.setLengthField(fieldName + "Length");
      } else if (field.getValueRank() != -1) {
        // Not scalar, not fixed-dimension, not supported
        throw new IllegalArgumentException(
            "cannot encode field \""
                + fieldName
                + "\" "
                + "with ValueRank: %s"
                + field.getValueRank());
      }

      structuredType.getField().add(fieldType);
    }

    return structuredType;
  }

  private QualifiedTypeName getDataTypeLocation(NodeId dataTypeId) {
    AddressSpaceManager addressSpaceManager = context.getServer().getAddressSpaceManager();

    UaNode dataTypeNode = addressSpaceManager.getManagedNode(dataTypeId).orElse(null);

    checkNotNull(dataTypeNode, "dataTypeNode for dataTypeId=" + dataTypeId);

    if (dataTypeId.getNamespaceIndex().intValue() == 0) {
      long id = ((UInteger) dataTypeId.getIdentifier()).longValue();
      String uri = id <= 15L ? Namespaces.OPC_UA_BSD : Namespaces.OPC_UA;
      return new QualifiedTypeName(uri, dataTypeNode.getBrowseName().getName());
    }

    QualifiedName parentTypeName =
        dataTypeNode.getReferences().stream()
            .filter(Reference.SUBTYPE_OF)
            .flatMap(r -> addressSpaceManager.getManagedNode(r.getTargetNodeId()).stream())
            .findFirst()
            .map(UaNode::getBrowseName)
            .orElse(QualifiedName.NULL_VALUE);

    if (parentTypeName.equals(new QualifiedName(0, "Enumeration"))) {
      return new QualifiedTypeName("", dataTypeNode.getBrowseName().getName());
    }

    UaNode dataTypeEncodingNode =
        dataTypeNode.getReferences().stream()
            .filter(Reference.HAS_ENCODING_PREDICATE)
            .flatMap(r -> addressSpaceManager.getManagedNode(r.getTargetNodeId()).stream())
            .filter(n -> n.getBrowseName().equals(new QualifiedName(0, "Default Binary")))
            .findFirst()
            .orElse(null);

    checkNotNull(dataTypeEncodingNode, "dataTypeEncodingNode for dataTypeId=" + dataTypeId);

    UaNode dataTypeDescriptionNode =
        dataTypeEncodingNode.getReferences().stream()
            .filter(Reference.HAS_DESCRIPTION_PREDICATE)
            .flatMap(r -> addressSpaceManager.getManagedNode(r.getTargetNodeId()).stream())
            .findFirst()
            .orElse(null);

    checkNotNull(dataTypeDescriptionNode, "dataTypeDescriptionNode for dataTypeId=" + dataTypeId);

    String dataTypeName = dataTypeDescriptionNode.getBrowseName().getName();

    UaNode dictionaryNode =
        dataTypeDescriptionNode.getReferences().stream()
            .filter(Reference.COMPONENT_OF_PREDICATE)
            .flatMap(r -> addressSpaceManager.getManagedNode(r.getTargetNodeId()).stream())
            .findFirst()
            .orElse(null);

    checkNotNull(dictionaryNode, "dictionaryNode for dataTypeId=" + dataTypeId);

    String dictionaryNamespaceUri =
        dictionaryNode.getProperty(DataTypeDictionaryType.NAMESPACE_URI).orElse(null);

    checkNotNull(dictionaryNamespaceUri, "dictionaryNamespaceUri for dataTypeId=" + dataTypeId);

    return new QualifiedTypeName(dictionaryNamespaceUri, dataTypeName);
  }

  /** Pair of DataType name and the namespace URI of the DataTypeDictionary it's defined in. */
  public static class QualifiedTypeName {
    final String dictionaryNamespaceUri;
    final String dataTypeName;

    public QualifiedTypeName(String dictionaryNamespaceUri, String dataTypeName) {
      this.dictionaryNamespaceUri = dictionaryNamespaceUri;
      this.dataTypeName = dataTypeName;
    }
  }
}
