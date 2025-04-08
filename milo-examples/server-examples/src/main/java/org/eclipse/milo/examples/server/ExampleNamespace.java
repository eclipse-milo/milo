/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import java.lang.reflect.Array;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.eclipse.milo.examples.server.methods.GenerateEventMethod;
import org.eclipse.milo.examples.server.methods.SqrtMethod;
import org.eclipse.milo.examples.server.types.CustomEnumType;
import org.eclipse.milo.examples.server.types.CustomStructType;
import org.eclipse.milo.examples.server.types.CustomUnionType;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.ValueRank;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.core.dtd.BinaryDataTypeCodec;
import org.eclipse.milo.opcua.sdk.core.types.DynamicEnumType;
import org.eclipse.milo.opcua.sdk.core.types.DynamicStructType;
import org.eclipse.milo.opcua.sdk.core.types.codec.DynamicCodecFactory;
import org.eclipse.milo.opcua.sdk.core.types.codec.DynamicStructCodec;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.sdk.server.Lifecycle;
import org.eclipse.milo.opcua.sdk.server.ManagedNamespaceWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.dtd.BinaryDataTypeDictionaryManager;
import org.eclipse.milo.opcua.sdk.server.identity.Identity;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataTypeEncodingTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.AnalogItemTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaDataTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.factories.NodeFactory;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilters;
import org.eclipse.milo.opcua.sdk.server.util.SubscriptionModel;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumField;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleNamespace extends ManagedNamespaceWithLifecycle {

  public static final String NAMESPACE_URI = "urn:eclipse:milo:hello-world";

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private volatile Thread eventThread;
  private volatile boolean keepPostingEvents = true;

  private final Random random = new Random();

  private final BinaryDataTypeDictionaryManager dictionaryManager;

  private final SubscriptionModel subscriptionModel;

  ExampleNamespace(OpcUaServer server) {
    super(server, NAMESPACE_URI);

    subscriptionModel = new SubscriptionModel(server, this);
    dictionaryManager = new BinaryDataTypeDictionaryManager(getNodeContext(), NAMESPACE_URI);

    getLifecycleManager().addLifecycle(dictionaryManager);
    getLifecycleManager().addLifecycle(subscriptionModel);

    getLifecycleManager().addStartupTask(this::createAndAddNodes);

    getLifecycleManager()
        .addLifecycle(
            new Lifecycle() {
              @Override
              public void startup() {
                startBogusEventNotifier();
              }

              @Override
              public void shutdown() {
                try {
                  keepPostingEvents = false;
                  eventThread.interrupt();
                  eventThread.join();
                } catch (InterruptedException ignored) {
                  // ignored
                }
              }
            });
  }

  private void createAndAddNodes() {
    // Create a "HelloWorld" folder and add it to the node manager
    NodeId folderNodeId = newNodeId("HelloWorld");

    UaFolderNode folderNode =
        new UaFolderNode(
            getNodeContext(),
            folderNodeId,
            newQualifiedName("HelloWorld"),
            LocalizedText.english("HelloWorld"));

    getNodeManager().addNode(folderNode);

    // Make sure our new folder shows up under the server's Objects folder.
    folderNode.addReference(
        new Reference(
            folderNode.getNodeId(), NodeIds.Organizes, NodeIds.ObjectsFolder.expanded(), false));

    // Add the rest of the nodes
    addVariableNodes(folderNode);

    addSqrtMethod(folderNode);

    addGenerateEventMethod(folderNode);

    try {
      registerCustomEnumType();
      addCustomEnumTypeVariable(folderNode);
    } catch (Exception e) {
      logger.warn("Failed to register custom enum type", e);
    }

    try {
      registerCustomStructType();
      addCustomStructTypeVariable(folderNode);
    } catch (Exception e) {
      logger.warn("Failed to register custom struct type", e);
    }

    try {
      registerCustomUnionType();
      addCustomUnionTypeVariable(folderNode);
    } catch (Exception e) {
      logger.warn("Failed to register custom struct type", e);
    }

    try {
      DataType dataType = registerDynamicStructType();

      addDynamicStructTypeVariable(folderNode, dataType);
    } catch (Exception e) {
      logger.warn("Failed to register dynamic struct type", e);
    }

    try {
      DataType enumDataType = registerDynamicEnumType();

      addDynamicEnumTypeVariable(folderNode, enumDataType);
    } catch (Exception e) {
      logger.warn("Failed to register dynamic enum type", e);
    }

    addCustomObjectTypeAndInstance(folderNode);
  }

  private void startBogusEventNotifier() {
    // Set the EventNotifier bit on Server Node for Events.
    UaNode serverNode =
        getServer().getAddressSpaceManager().getManagedNode(NodeIds.Server).orElse(null);

    if (serverNode instanceof ServerTypeNode) {
      ((ServerTypeNode) serverNode).setEventNotifier(ubyte(1));

      // Post a bogus Event every couple seconds
      eventThread =
          new Thread(
              () -> {
                while (keepPostingEvents) {
                  try {
                    BaseEventTypeNode eventNode =
                        getServer()
                            .getEventFactory()
                            .createEvent(newNodeId(UUID.randomUUID()), NodeIds.BaseEventType);

                    eventNode.setBrowseName(new QualifiedName(1, "foo"));
                    eventNode.setDisplayName(LocalizedText.english("foo"));
                    eventNode.setEventId(ByteString.of(new byte[] {0, 1, 2, 3}));
                    eventNode.setEventType(NodeIds.BaseEventType);
                    eventNode.setSourceNode(serverNode.getNodeId());
                    eventNode.setSourceName(serverNode.getDisplayName().text());
                    eventNode.setTime(DateTime.now());
                    eventNode.setReceiveTime(DateTime.NULL_VALUE);
                    eventNode.setMessage(LocalizedText.english("event message!"));
                    eventNode.setSeverity(ushort(2));

                    getServer().getEventNotifier().fire(eventNode);

                    eventNode.delete();
                  } catch (Throwable e) {
                    logger.error("Error creating EventNode: {}", e.getMessage(), e);
                  }

                  try {
                    //noinspection BusyWait
                    Thread.sleep(2_000);
                  } catch (InterruptedException ignored) {
                    // ignored
                  }
                }
              },
              "bogus-event-poster");

      eventThread.start();
    }
  }

  private void addVariableNodes(UaFolderNode rootNode) {
    addArrayNodes(rootNode);
    addScalarNodes(rootNode);
    addAdminReadableNodes(rootNode);
    addAdminWritableNodes(rootNode);
    addDynamicNodes(rootNode);
    addDataAccessNodes(rootNode);
    addWriteOnlyNodes(rootNode);
  }

  private void addArrayNodes(UaFolderNode rootNode) {
    UaFolderNode arrayTypesFolder =
        new UaFolderNode(
            getNodeContext(),
            newNodeId("HelloWorld/ArrayTypes"),
            newQualifiedName("ArrayTypes"),
            LocalizedText.english("ArrayTypes"));

    getNodeManager().addNode(arrayTypesFolder);
    rootNode.addOrganizes(arrayTypesFolder);

    for (Object[] os : ExampleData.STATIC_ARRAY_NODES) {
      String name = (String) os[0];
      NodeId typeId = (NodeId) os[1];
      Object value = os[2];
      Object array = Array.newInstance(value.getClass(), 5);
      for (int i = 0; i < 5; i++) {
        Array.set(array, i, value);
      }
      Variant variant = Variant.of(array);

      UaVariableNode.build(
          getNodeContext(),
          builder -> {
            builder.setNodeId(newNodeId("HelloWorld/ArrayTypes/" + name));
            builder.setAccessLevel(AccessLevel.READ_WRITE);
            builder.setUserAccessLevel(AccessLevel.READ_WRITE);
            builder.setBrowseName(newQualifiedName(name));
            builder.setDisplayName(LocalizedText.english(name));
            builder.setDataType(typeId);
            builder.setTypeDefinition(NodeIds.BaseDataVariableType);
            builder.setValueRank(ValueRank.OneDimension.getValue());
            builder.setArrayDimensions(new UInteger[] {uint(0)});
            builder.setValue(new DataValue(variant));

            builder.addAttributeFilter(new AttributeLoggingFilter(AttributeId.Value::equals));

            builder.addReference(
                new Reference(
                    builder.getNodeId(),
                    NodeIds.Organizes,
                    arrayTypesFolder.getNodeId().expanded(),
                    Reference.Direction.INVERSE));

            return builder.buildAndAdd();
          });
    }
  }

  private void addScalarNodes(UaFolderNode rootNode) {
    UaFolderNode scalarTypesFolder =
        new UaFolderNode(
            getNodeContext(),
            newNodeId("HelloWorld/ScalarTypes"),
            newQualifiedName("ScalarTypes"),
            LocalizedText.english("ScalarTypes"));

    getNodeManager().addNode(scalarTypesFolder);
    rootNode.addOrganizes(scalarTypesFolder);

    for (Object[] os : ExampleData.STATIC_SCALAR_NODES) {
      String name = (String) os[0];
      NodeId typeId = (NodeId) os[1];
      Variant variant = (Variant) os[2];

      UaVariableNode node =
          new UaVariableNode.UaVariableNodeBuilder(getNodeContext())
              .setNodeId(newNodeId("HelloWorld/ScalarTypes/" + name))
              .setAccessLevel(AccessLevel.READ_WRITE)
              .setUserAccessLevel(AccessLevel.READ_WRITE)
              .setBrowseName(newQualifiedName(name))
              .setDisplayName(LocalizedText.english(name))
              .setDataType(typeId)
              .setTypeDefinition(NodeIds.BaseDataVariableType)
              .build();

      node.setValue(new DataValue(variant));

      node.getFilterChain().addLast(new AttributeLoggingFilter(AttributeId.Value::equals));

      getNodeManager().addNode(node);
      scalarTypesFolder.addOrganizes(node);
    }
  }

  private void addWriteOnlyNodes(UaFolderNode rootNode) {
    UaFolderNode writeOnlyFolder =
        new UaFolderNode(
            getNodeContext(),
            newNodeId("HelloWorld/WriteOnly"),
            newQualifiedName("WriteOnly"),
            LocalizedText.english("WriteOnly"));

    getNodeManager().addNode(writeOnlyFolder);
    rootNode.addOrganizes(writeOnlyFolder);

    String name = "String";
    UaVariableNode node =
        new UaVariableNode.UaVariableNodeBuilder(getNodeContext())
            .setNodeId(newNodeId("HelloWorld/WriteOnly/" + name))
            .setAccessLevel(AccessLevel.WRITE_ONLY)
            .setUserAccessLevel(AccessLevel.WRITE_ONLY)
            .setBrowseName(newQualifiedName(name))
            .setDisplayName(LocalizedText.english(name))
            .setDataType(NodeIds.String)
            .setTypeDefinition(NodeIds.BaseDataVariableType)
            .build();

    node.setValue(new DataValue(new Variant("can't read this")));

    getNodeManager().addNode(node);
    writeOnlyFolder.addOrganizes(node);
  }

  private void addAdminReadableNodes(UaFolderNode rootNode) {
    UaFolderNode adminFolder =
        new UaFolderNode(
            getNodeContext(),
            newNodeId("HelloWorld/OnlyAdminCanRead"),
            newQualifiedName("OnlyAdminCanRead"),
            LocalizedText.english("OnlyAdminCanRead"));

    getNodeManager().addNode(adminFolder);
    rootNode.addOrganizes(adminFolder);

    String name = "String";
    UaVariableNode node =
        new UaVariableNode.UaVariableNodeBuilder(getNodeContext())
            .setNodeId(newNodeId("HelloWorld/OnlyAdminCanRead/" + name))
            .setAccessLevel(AccessLevel.READ_WRITE)
            .setBrowseName(newQualifiedName(name))
            .setDisplayName(LocalizedText.english(name))
            .setDataType(NodeIds.String)
            .setTypeDefinition(NodeIds.BaseDataVariableType)
            .build();

    node.setValue(new DataValue(new Variant("shh... don't tell the lusers")));

    node.getFilterChain()
        .addLast(
            new RestrictedAccessFilter(
                identity -> {
                  if (identity instanceof Identity.UsernameIdentity ui) {
                    if (ui.getUsername().equals("admin")) {
                      return AccessLevel.READ_WRITE;
                    } else {
                      return AccessLevel.NONE;
                    }
                  } else {
                    return AccessLevel.NONE;
                  }
                }));

    getNodeManager().addNode(node);
    adminFolder.addOrganizes(node);
  }

  private void addAdminWritableNodes(UaFolderNode rootNode) {
    UaFolderNode adminFolder =
        new UaFolderNode(
            getNodeContext(),
            newNodeId("HelloWorld/OnlyAdminCanWrite"),
            newQualifiedName("OnlyAdminCanWrite"),
            LocalizedText.english("OnlyAdminCanWrite"));

    getNodeManager().addNode(adminFolder);
    rootNode.addOrganizes(adminFolder);

    String name = "String";
    UaVariableNode node =
        new UaVariableNode.UaVariableNodeBuilder(getNodeContext())
            .setNodeId(newNodeId("HelloWorld/OnlyAdminCanWrite/" + name))
            .setAccessLevel(AccessLevel.READ_WRITE)
            .setBrowseName(newQualifiedName(name))
            .setDisplayName(LocalizedText.english(name))
            .setDataType(NodeIds.String)
            .setTypeDefinition(NodeIds.BaseDataVariableType)
            .build();

    node.setValue(new DataValue(new Variant("admin was here")));

    node.getFilterChain()
        .addLast(
            new RestrictedAccessFilter(
                identity -> {
                  if (identity instanceof Identity.UsernameIdentity ui) {
                    if (ui.getUsername().equals("admin")) {
                      return AccessLevel.READ_WRITE;
                    } else {
                      return AccessLevel.NONE;
                    }
                  } else {
                    return AccessLevel.NONE;
                  }
                }));

    getNodeManager().addNode(node);
    adminFolder.addOrganizes(node);
  }

  private void addDynamicNodes(UaFolderNode rootNode) {
    UaFolderNode dynamicFolder =
        new UaFolderNode(
            getNodeContext(),
            newNodeId("HelloWorld/Dynamic"),
            newQualifiedName("Dynamic"),
            LocalizedText.english("Dynamic"));

    getNodeManager().addNode(dynamicFolder);
    rootNode.addOrganizes(dynamicFolder);

    // Dynamic Boolean
    {
      String name = "Boolean";
      NodeId typeId = NodeIds.Boolean;
      Variant variant = new Variant(false);

      UaVariableNode node =
          new UaVariableNode.UaVariableNodeBuilder(getNodeContext())
              .setNodeId(newNodeId("HelloWorld/Dynamic/" + name))
              .setAccessLevel(AccessLevel.READ_WRITE)
              .setBrowseName(newQualifiedName(name))
              .setDisplayName(LocalizedText.english(name))
              .setDataType(typeId)
              .setTypeDefinition(NodeIds.BaseDataVariableType)
              .build();

      node.setValue(new DataValue(variant));

      node.getFilterChain()
          .addLast(
              new AttributeLoggingFilter(),
              AttributeFilters.getValue(ctx -> new DataValue(new Variant(random.nextBoolean()))));

      getNodeManager().addNode(node);
      dynamicFolder.addOrganizes(node);
    }

    // Dynamic Int32
    {
      String name = "Int32";
      NodeId typeId = NodeIds.Int32;
      Variant variant = new Variant(0);

      UaVariableNode node =
          new UaVariableNode.UaVariableNodeBuilder(getNodeContext())
              .setNodeId(newNodeId("HelloWorld/Dynamic/" + name))
              .setAccessLevel(AccessLevel.READ_WRITE)
              .setBrowseName(newQualifiedName(name))
              .setDisplayName(LocalizedText.english(name))
              .setDataType(typeId)
              .setTypeDefinition(NodeIds.BaseDataVariableType)
              .build();

      node.setValue(new DataValue(variant));

      node.getFilterChain()
          .addLast(
              new AttributeLoggingFilter(),
              AttributeFilters.getValue(ctx -> new DataValue(new Variant(random.nextInt()))));

      getNodeManager().addNode(node);
      dynamicFolder.addOrganizes(node);
    }

    // Dynamic Double
    {
      String name = "Double";
      NodeId typeId = NodeIds.Double;
      Variant variant = new Variant(0.0);

      UaVariableNode node =
          new UaVariableNode.UaVariableNodeBuilder(getNodeContext())
              .setNodeId(newNodeId("HelloWorld/Dynamic/" + name))
              .setAccessLevel(AccessLevel.READ_WRITE)
              .setBrowseName(newQualifiedName(name))
              .setDisplayName(LocalizedText.english(name))
              .setDataType(typeId)
              .setTypeDefinition(NodeIds.BaseDataVariableType)
              .build();

      node.setValue(new DataValue(variant));

      node.getFilterChain()
          .addLast(
              new AttributeLoggingFilter(),
              AttributeFilters.getValue(ctx -> new DataValue(new Variant(random.nextDouble()))));

      getNodeManager().addNode(node);
      dynamicFolder.addOrganizes(node);
    }
  }

  private void addDataAccessNodes(UaFolderNode rootNode) {
    // DataAccess folder
    UaFolderNode dataAccessFolder =
        new UaFolderNode(
            getNodeContext(),
            newNodeId("HelloWorld/DataAccess"),
            newQualifiedName("DataAccess"),
            LocalizedText.english("DataAccess"));

    getNodeManager().addNode(dataAccessFolder);
    rootNode.addOrganizes(dataAccessFolder);

    try {
      AnalogItemTypeNode node =
          (AnalogItemTypeNode)
              getNodeFactory()
                  .createNode(
                      newNodeId("HelloWorld/DataAccess/AnalogValue"),
                      NodeIds.AnalogItemType,
                      new NodeFactory.InstantiationCallback() {
                        @Override
                        public boolean includeOptionalNode(
                            NodeId typeDefinitionId, QualifiedName browseName) {
                          return true;
                        }
                      });

      node.setBrowseName(newQualifiedName("AnalogValue"));
      node.setDisplayName(LocalizedText.english("AnalogValue"));
      node.setDataType(NodeIds.Double);
      node.setValue(new DataValue(new Variant(3.14d)));

      node.setEuRange(new Range(0.0, 100.0));

      getNodeManager().addNode(node);
      dataAccessFolder.addOrganizes(node);
    } catch (UaException e) {
      logger.error("Error creating AnalogItemType instance: {}", e.getMessage(), e);
    }
  }

  private void addSqrtMethod(UaFolderNode folderNode) {
    UaMethodNode methodNode =
        UaMethodNode.builder(getNodeContext())
            .setNodeId(newNodeId("HelloWorld/sqrt(x)"))
            .setBrowseName(newQualifiedName("sqrt(x)"))
            .setDisplayName(new LocalizedText(null, "sqrt(x)"))
            .setDescription(
                LocalizedText.english(
                    "Returns the correctly rounded positive square root of a double value."))
            .build();

    SqrtMethod sqrtMethod = new SqrtMethod(methodNode);
    methodNode.setInputArguments(sqrtMethod.getInputArguments());
    methodNode.setOutputArguments(sqrtMethod.getOutputArguments());
    methodNode.setInvocationHandler(sqrtMethod);

    getNodeManager().addNode(methodNode);

    methodNode.addReference(
        new Reference(
            methodNode.getNodeId(),
            NodeIds.HasComponent,
            folderNode.getNodeId().expanded(),
            false));
  }

  private void addGenerateEventMethod(UaFolderNode folderNode) {
    UaMethodNode methodNode =
        UaMethodNode.builder(getNodeContext())
            .setNodeId(newNodeId("HelloWorld/generateEvent(eventTypeId)"))
            .setBrowseName(newQualifiedName("generateEvent(eventTypeId)"))
            .setDisplayName(new LocalizedText(null, "generateEvent(eventTypeId)"))
            .setDescription(
                LocalizedText.english(
                    "Generate an Event with the TypeDefinition indicated by eventTypeId."))
            .build();

    GenerateEventMethod generateEventMethod = new GenerateEventMethod(methodNode);
    methodNode.setInputArguments(generateEventMethod.getInputArguments());
    methodNode.setOutputArguments(generateEventMethod.getOutputArguments());
    methodNode.setInvocationHandler(generateEventMethod);

    getNodeManager().addNode(methodNode);

    methodNode.addReference(
        new Reference(
            methodNode.getNodeId(),
            NodeIds.HasComponent,
            folderNode.getNodeId().expanded(),
            false));
  }

  private void addCustomObjectTypeAndInstance(UaFolderNode rootFolder) {
    // Define a new ObjectType called "MyObjectType".
    UaObjectTypeNode objectTypeNode =
        UaObjectTypeNode.builder(getNodeContext())
            .setNodeId(newNodeId("ObjectTypes/MyObjectType"))
            .setBrowseName(newQualifiedName("MyObjectType"))
            .setDisplayName(LocalizedText.english("MyObjectType"))
            .setIsAbstract(false)
            .build();

    // "Foo" and "Bar" are members. These nodes are what are called "instance declarations" by the
    // spec.
    UaVariableNode foo =
        UaVariableNode.build(
            getNodeContext(),
            b ->
                b.setNodeId(newNodeId("ObjectTypes/MyObjectType.Foo"))
                    .setAccessLevel(AccessLevel.READ_WRITE)
                    .setBrowseName(newQualifiedName("Foo"))
                    .setDisplayName(LocalizedText.english("Foo"))
                    .setDataType(NodeIds.Int16)
                    .setTypeDefinition(NodeIds.BaseDataVariableType)
                    .build());

    foo.addReference(
        new Reference(
            foo.getNodeId(),
            NodeIds.HasModellingRule,
            NodeIds.ModellingRule_Mandatory.expanded(),
            true));

    foo.setValue(new DataValue(new Variant(0)));
    objectTypeNode.addComponent(foo);

    UaVariableNode bar =
        UaVariableNode.build(
            getNodeContext(),
            b ->
                b.setNodeId(newNodeId("ObjectTypes/MyObjectType.Bar"))
                    .setAccessLevel(AccessLevel.READ_WRITE)
                    .setBrowseName(newQualifiedName("Bar"))
                    .setDisplayName(LocalizedText.english("Bar"))
                    .setDataType(NodeIds.String)
                    .setTypeDefinition(NodeIds.BaseDataVariableType)
                    .build());

    bar.addReference(
        new Reference(
            bar.getNodeId(),
            NodeIds.HasModellingRule,
            NodeIds.ModellingRule_Mandatory.expanded(),
            true));

    bar.setValue(new DataValue(new Variant("bar")));
    objectTypeNode.addComponent(bar);

    // Tell the ObjectTypeManager about our new type.
    // This lets us use NodeFactory to instantiate instances of the type.
    getServer()
        .getObjectTypeManager()
        .registerObjectType(
            objectTypeNode.getNodeId(),
            UaObjectNode.class,
            (context, nodeId, browseName, displayName, description, writeMask, userWriteMask) ->
                new UaObjectNode(
                    context,
                    nodeId,
                    browseName,
                    displayName,
                    description,
                    writeMask,
                    userWriteMask));

    // Add the inverse SubtypeOf relationship.
    objectTypeNode.addReference(
        new Reference(
            objectTypeNode.getNodeId(),
            NodeIds.HasSubtype,
            NodeIds.BaseObjectType.expanded(),
            false));

    // Add type definition and declarations to address space.
    getNodeManager().addNode(objectTypeNode);
    getNodeManager().addNode(foo);
    getNodeManager().addNode(bar);

    // Use NodeFactory to create instance of MyObjectType called "MyObject".
    // NodeFactory takes care of recursively instantiating MyObject member nodes
    // as well as adding all nodes to the address space.
    try {
      UaObjectNode myObject =
          (UaObjectNode)
              getNodeFactory()
                  .createNode(newNodeId("HelloWorld/MyObject"), objectTypeNode.getNodeId());
      myObject.setBrowseName(newQualifiedName("MyObject"));
      myObject.setDisplayName(LocalizedText.english("MyObject"));

      // Add forward and inverse references from the root folder.
      rootFolder.addOrganizes(myObject);

      myObject.addReference(
          new Reference(
              myObject.getNodeId(), NodeIds.Organizes, rootFolder.getNodeId().expanded(), false));
    } catch (UaException e) {
      logger.error("Error creating MyObjectType instance: {}", e.getMessage(), e);
    }
  }

  private void registerCustomEnumType() throws Exception {
    NodeId dataTypeId = CustomEnumType.TYPE_ID.toNodeIdOrThrow(getServer().getNamespaceTable());

    // Add a custom DataTypeNode with a SubtypeOf reference to Enumeration
    UaDataTypeNode dataTypeNode =
        new UaDataTypeNode(
            getNodeContext(),
            dataTypeId,
            newQualifiedName("CustomEnumType"),
            LocalizedText.english("CustomEnumType"),
            LocalizedText.NULL_VALUE,
            uint(0),
            uint(0),
            false);

    dataTypeNode.addReference(
        new Reference(
            dataTypeId,
            NodeIds.HasSubtype,
            NodeIds.Enumeration.expanded(),
            Reference.Direction.INVERSE));

    getNodeManager().addNode(dataTypeNode);

    // Define the enum
    EnumField[] fields =
        new EnumField[] {
          new EnumField(0L, LocalizedText.english("Field0"), LocalizedText.NULL_VALUE, "Field0"),
          new EnumField(1L, LocalizedText.english("Field1"), LocalizedText.NULL_VALUE, "Field1"),
          new EnumField(2L, LocalizedText.english("Field2"), LocalizedText.NULL_VALUE, "Field2")
        };

    EnumDefinition definition = new EnumDefinition(fields);

    // This Enum is zero-based and naturally incrementing, so we set the EnumStrings property.
    // If it were more complex the EnumValues property would be used instead.
    dataTypeNode.setEnumStrings(
        new LocalizedText[] {
          LocalizedText.english("Field0"),
          LocalizedText.english("Field1"),
          LocalizedText.english("Field2")
        });

    // Populate the OPC UA 1.04+ DataTypeDefinition attribute
    dataTypeNode.setDataTypeDefinition(definition);

    // Prior to OPC UA 1.04, clients that needed to interpret custom types read the
    // DataTypeDictionary from the server. We describe the type using StructureDefinition
    // or EnumDefinition and register it with the dictionary manager.
    // The dictionary manager will add all the necessary nodes to the AddressSpace and
    // generate the required dictionary bsd.xml file.

    EnumDescription description =
        new EnumDescription(
            dataTypeId,
            new QualifiedName(getNamespaceIndex(), "CustomEnumType"),
            definition,
            ubyte(OpcUaDataType.Int32.getTypeId()));

    dictionaryManager.registerEnum(description);
  }

  private void registerCustomStructType() throws Exception {
    // Get the NodeId for the DataType and encoding Nodes.
    NodeId dataTypeId = CustomStructType.TYPE_ID.toNodeIdOrThrow(getServer().getNamespaceTable());
    NodeId binaryEncodingId =
        CustomStructType.BINARY_ENCODING_ID.toNodeIdOrThrow(getServer().getNamespaceTable());

    // Add a custom DataTypeNode with a SubtypeOf reference to Structure
    UaDataTypeNode dataTypeNode =
        new UaDataTypeNode(
            getNodeContext(),
            dataTypeId,
            newQualifiedName("CustomStructType"),
            LocalizedText.english("CustomStructType"),
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

    getNodeManager().addNode(dataTypeNode);

    // Define the structure
    StructureField[] fields =
        new StructureField[] {
          new StructureField(
              "foo",
              LocalizedText.NULL_VALUE,
              NodeIds.String,
              ValueRanks.Scalar,
              null,
              getServer().getConfig().getLimits().getMaxStringLength(),
              false),
          new StructureField(
              "bar",
              LocalizedText.NULL_VALUE,
              NodeIds.UInt32,
              ValueRanks.Scalar,
              null,
              uint(0),
              false),
          new StructureField(
              "baz",
              LocalizedText.NULL_VALUE,
              NodeIds.Boolean,
              ValueRanks.Scalar,
              null,
              uint(0),
              false),
          new StructureField(
              "customEnumType",
              LocalizedText.NULL_VALUE,
              CustomEnumType.TYPE_ID.toNodeIdOrThrow(getServer().getNamespaceTable()),
              ValueRanks.Scalar,
              null,
              uint(0),
              false)
        };

    StructureDefinition definition =
        new StructureDefinition(
            binaryEncodingId, NodeIds.Structure, StructureType.Structure, fields);

    // Populate the OPC UA 1.04+ DataTypeDefinition attribute
    dataTypeNode.setDataTypeDefinition(definition);

    // Register Codecs for each supported encoding with DataTypeManager
    getNodeContext()
        .getServer()
        .getStaticDataTypeManager()
        .registerType(dataTypeId, new CustomStructType.Codec(), binaryEncodingId, null, null);

    // Prior to OPC UA 1.04, clients that needed to interpret custom types read the
    // DataTypeDictionary from the server. We describe the type using StructureDefinition
    // or EnumDefinition and register it with the dictionary manager.
    // The dictionary manager will add all the necessary nodes to the AddressSpace and
    // generate the required dictionary bsd.xml file.
    StructureDescription description =
        new StructureDescription(
            dataTypeId, new QualifiedName(getNamespaceIndex(), "CustomStructType"), definition);

    dictionaryManager.registerStructure(
        "CustomStructType",
        dataTypeId,
        binaryEncodingId,
        BinaryDataTypeCodec.from(new CustomStructType.Codec()),
        description);
  }

  private void registerCustomUnionType() throws Exception {
    NodeId dataTypeId = CustomUnionType.TYPE_ID.toNodeIdOrThrow(getServer().getNamespaceTable());

    NodeId binaryEncodingId =
        CustomUnionType.BINARY_ENCODING_ID.toNodeIdOrThrow(getServer().getNamespaceTable());

    // Add a custom DataTypeNode with a SubtypeOf reference to Union
    UaDataTypeNode dataTypeNode =
        new UaDataTypeNode(
            getNodeContext(),
            dataTypeId,
            newQualifiedName("CustomUnionType"),
            LocalizedText.english("CustomUnionType"),
            LocalizedText.NULL_VALUE,
            uint(0),
            uint(0),
            false);

    dataTypeNode.addReference(
        new Reference(
            dataTypeId, NodeIds.HasSubtype, NodeIds.Union.expanded(), Reference.Direction.INVERSE));

    getNodeManager().addNode(dataTypeNode);

    StructureField[] fields =
        new StructureField[] {
          new StructureField(
              "foo",
              LocalizedText.NULL_VALUE,
              NodeIds.UInt32,
              ValueRanks.Scalar,
              null,
              getServer().getConfig().getLimits().getMaxStringLength(),
              false),
          new StructureField(
              "bar",
              LocalizedText.NULL_VALUE,
              NodeIds.String,
              ValueRanks.Scalar,
              null,
              uint(0),
              false)
        };

    StructureDefinition definition =
        new StructureDefinition(binaryEncodingId, NodeIds.Structure, StructureType.Union, fields);

    dataTypeNode.setDataTypeDefinition(definition);

    // Register Codecs for each supported encoding with DataTypeManager
    getNodeContext()
        .getServer()
        .getStaticDataTypeManager()
        .registerType(dataTypeId, new CustomUnionType.Codec(), binaryEncodingId, null, null);

    StructureDescription description =
        new StructureDescription(
            dataTypeId, new QualifiedName(getNamespaceIndex(), "CustomUnionType"), definition);

    dictionaryManager.registerStructure(
        "CustomUnionType",
        dataTypeId,
        binaryEncodingId,
        BinaryDataTypeCodec.from(new CustomUnionType.Codec()),
        description);
  }

  private DataType registerDynamicEnumType() throws Exception {
    // Define NodeId for DataType
    NodeId dataTypeId =
        ExpandedNodeId.parse("nsu=%s;s=DataType.DynamicEnumType".formatted(NAMESPACE_URI))
            .toNodeIdOrThrow(getServer().getNamespaceTable());

    // Add a custom DataTypeNode with a SubtypeOf reference to Enumeration
    UaDataTypeNode dataTypeNode =
        new UaDataTypeNode(
            getNodeContext(),
            dataTypeId,
            newQualifiedName("DynamicEnumType"),
            LocalizedText.english("DynamicEnumType"),
            LocalizedText.NULL_VALUE,
            uint(0),
            uint(0),
            false);

    dataTypeNode.addReference(
        new Reference(
            dataTypeId,
            NodeIds.HasSubtype,
            NodeIds.Enumeration.expanded(),
            Reference.Direction.INVERSE));

    getNodeManager().addNode(dataTypeNode);

    // Define the enum fields
    EnumField[] fields =
        new EnumField[] {
          new EnumField(
              0L, LocalizedText.english("DynField0"), LocalizedText.NULL_VALUE, "DynField0"),
          new EnumField(
              1L, LocalizedText.english("DynField1"), LocalizedText.NULL_VALUE, "DynField1"),
          new EnumField(
              2L, LocalizedText.english("DynField2"), LocalizedText.NULL_VALUE, "DynField2")
        };

    EnumDefinition definition = new EnumDefinition(fields);

    // Set the EnumStrings property
    dataTypeNode.setEnumStrings(
        new LocalizedText[] {
          LocalizedText.english("DynField0"),
          LocalizedText.english("DynField1"),
          LocalizedText.english("DynField2")
        });

    // Set the DataTypeDefinition attribute
    dataTypeNode.setDataTypeDefinition(definition);

    DataTypeTree dataTypeTree = getNodeContext().getServer().updateDataTypeTree();
    DataType dataType = dataTypeTree.getDataType(dataTypeId);
    assert dataType != null;

    // Register with the dynamic DataTypeManager
    getNodeContext()
        .getServer()
        .getDynamicDataTypeManager()
        .registerType(
            dataTypeId, DynamicCodecFactory.create(dataType, dataTypeTree), null, null, null);

    return dataType;
  }

  private DataType registerDynamicStructType() throws Exception {
    // Define NodeIds for DataType and encoding Nodes
    NodeId dataTypeId =
        ExpandedNodeId.parse("nsu=%s;s=DataType.DynamicStructType".formatted(NAMESPACE_URI))
            .toNodeIdOrThrow(getServer().getNamespaceTable());

    NodeId binaryEncodingId =
        ExpandedNodeId.parse(
                "nsu=%s;s=DataType.DynamicStructType.BinaryEncoding".formatted(NAMESPACE_URI))
            .toNodeIdOrThrow(getServer().getNamespaceTable());

    // Add a custom DataTypeNode with a SubtypeOf reference to Structure
    UaDataTypeNode dataTypeNode =
        new UaDataTypeNode(
            getNodeContext(),
            dataTypeId,
            newQualifiedName("DynamicStructType"),
            LocalizedText.english("DynamicStructType"),
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

    getNodeManager().addNode(dataTypeNode);

    // Add a DataTypeEncodingNode for binary encoding of the new DataType
    DataTypeEncodingTypeNode dataTypeEncodingNode =
        new DataTypeEncodingTypeNode(
            getNodeContext(),
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
            Reference.Direction.FORWARD));

    dataTypeEncodingNode.addReference(
        new Reference(
            dataTypeEncodingNode.getNodeId(),
            NodeIds.HasEncoding,
            dataTypeId.expanded(),
            Reference.Direction.INVERSE));

    getNodeManager().addNode(dataTypeEncodingNode);

    // Define the structure
    StructureField[] fields =
        new StructureField[] {
          new StructureField(
              "foo",
              LocalizedText.NULL_VALUE,
              NodeIds.String,
              ValueRanks.Scalar,
              null,
              getServer().getConfig().getLimits().getMaxStringLength(),
              false),
          new StructureField(
              "bar",
              LocalizedText.NULL_VALUE,
              NodeIds.UInt32,
              ValueRanks.Scalar,
              null,
              uint(0),
              false),
          new StructureField(
              "baz",
              LocalizedText.NULL_VALUE,
              NodeIds.Boolean,
              ValueRanks.Scalar,
              null,
              uint(0),
              false)
        };

    StructureDefinition definition =
        new StructureDefinition(
            binaryEncodingId, NodeIds.Structure, StructureType.Structure, fields);

    // Set the DataTypeDefinition attribute
    dataTypeNode.setDataTypeDefinition(definition);

    DataTypeTree dataTypeTree = getNodeContext().getServer().updateDataTypeTree();
    DataType dataType = dataTypeTree.getDataType(dataTypeId);
    assert dataType != null;

    // Register Codecs for each supported encoding with DataTypeManager
    var codec = new DynamicStructCodec(dataType, dataTypeTree);

    getNodeContext()
        .getServer()
        .getDynamicDataTypeManager()
        .registerType(dataTypeId, codec, binaryEncodingId, null, null);

    return dataType;
  }

  private void addCustomEnumTypeVariable(UaFolderNode rootFolder) throws Exception {
    NodeId dataTypeId = CustomEnumType.TYPE_ID.toNodeIdOrThrow(getServer().getNamespaceTable());

    UaVariableNode customEnumTypeVariable =
        UaVariableNode.build(
            getNodeContext(),
            b ->
                b.setNodeId(newNodeId("HelloWorld/CustomEnumTypeVariable"))
                    .setAccessLevel(AccessLevel.READ_WRITE)
                    .setUserAccessLevel(AccessLevel.READ_WRITE)
                    .setBrowseName(newQualifiedName("CustomEnumTypeVariable"))
                    .setDisplayName(LocalizedText.english("CustomEnumTypeVariable"))
                    .setDataType(dataTypeId)
                    .setTypeDefinition(NodeIds.BaseDataVariableType)
                    .build());

    customEnumTypeVariable.setValue(new DataValue(new Variant(CustomEnumType.Field1)));

    getNodeManager().addNode(customEnumTypeVariable);

    customEnumTypeVariable.addReference(
        new Reference(
            customEnumTypeVariable.getNodeId(),
            NodeIds.Organizes,
            rootFolder.getNodeId().expanded(),
            false));
  }

  private void addCustomStructTypeVariable(UaFolderNode rootFolder) throws Exception {
    NodeId dataTypeId = CustomStructType.TYPE_ID.toNodeIdOrThrow(getServer().getNamespaceTable());

    NodeId binaryEncodingId =
        CustomStructType.BINARY_ENCODING_ID.toNodeIdOrThrow(getServer().getNamespaceTable());

    UaVariableNode customStructTypeVariable =
        UaVariableNode.build(
            getNodeContext(),
            b ->
                b.setNodeId(newNodeId("HelloWorld/CustomStructTypeVariable"))
                    .setAccessLevel(AccessLevel.READ_WRITE)
                    .setUserAccessLevel(AccessLevel.READ_WRITE)
                    .setBrowseName(newQualifiedName("CustomStructTypeVariable"))
                    .setDisplayName(LocalizedText.english("CustomStructTypeVariable"))
                    .setDataType(dataTypeId)
                    .setTypeDefinition(NodeIds.BaseDataVariableType)
                    .build());

    CustomStructType value = new CustomStructType("foo", uint(42), true, CustomEnumType.Field0);

    ExtensionObject xo = ExtensionObject.encode(getServer().getStaticEncodingContext(), value);

    customStructTypeVariable.setValue(new DataValue(new Variant(xo)));

    getNodeManager().addNode(customStructTypeVariable);

    customStructTypeVariable.addReference(
        new Reference(
            customStructTypeVariable.getNodeId(),
            NodeIds.Organizes,
            rootFolder.getNodeId().expanded(),
            false));
  }

  private void addCustomUnionTypeVariable(UaFolderNode rootFolder) throws Exception {
    NodeId dataTypeId = CustomUnionType.TYPE_ID.toNodeIdOrThrow(getServer().getNamespaceTable());

    NodeId binaryEncodingId =
        CustomUnionType.BINARY_ENCODING_ID.toNodeIdOrThrow(getServer().getNamespaceTable());

    UaVariableNode customUnionTypeVariable =
        UaVariableNode.build(
            getNodeContext(),
            b ->
                b.setNodeId(newNodeId("HelloWorld/CustomUnionTypeVariable"))
                    .setAccessLevel(AccessLevel.READ_WRITE)
                    .setUserAccessLevel(AccessLevel.READ_WRITE)
                    .setBrowseName(newQualifiedName("CustomUnionTypeVariable"))
                    .setDisplayName(LocalizedText.english("CustomUnionTypeVariable"))
                    .setDataType(dataTypeId)
                    .setTypeDefinition(NodeIds.BaseDataVariableType)
                    .build());

    CustomUnionType value = CustomUnionType.ofBar("hello");

    ExtensionObject xo = ExtensionObject.encode(getServer().getStaticEncodingContext(), value);

    customUnionTypeVariable.setValue(new DataValue(new Variant(xo)));

    getNodeManager().addNode(customUnionTypeVariable);

    customUnionTypeVariable.addReference(
        new Reference(
            customUnionTypeVariable.getNodeId(),
            NodeIds.Organizes,
            rootFolder.getNodeId().expanded(),
            false));
  }

  private void addDynamicEnumTypeVariable(UaFolderNode rootFolder, DataType dataType) {
    NodeId nodeId = newNodeId("HelloWorld/DynamicEnumTypeVariable");

    UaVariableNode dynamicEnumTypeVariable =
        new UaVariableNode.UaVariableNodeBuilder(getNodeContext())
            .setNodeId(nodeId)
            .setAccessLevel(AccessLevel.READ_WRITE)
            .setUserAccessLevel(AccessLevel.READ_WRITE)
            .setBrowseName(newQualifiedName("DynamicEnumTypeVariable"))
            .setDisplayName(LocalizedText.english("DynamicEnumTypeVariable"))
            .setDataType(dataType.getNodeId())
            .setTypeDefinition(NodeIds.BaseDataVariableType)
            .build();

    // Create an instance of DynamicEnumType with value 0 (DynField0)
    DynamicEnumType value = DynamicEnumType.newInstance(dataType, 0);
    dynamicEnumTypeVariable.setValue(new DataValue(new Variant(value)));

    getNodeManager().addNode(dynamicEnumTypeVariable);
    rootFolder.addOrganizes(dynamicEnumTypeVariable);
  }

  private void addDynamicStructTypeVariable(UaFolderNode rootFolder, DataType dataType) {
    UaVariableNode dynamicStructTypeVariable =
        UaVariableNode.build(
            getNodeContext(),
            b ->
                b.setNodeId(newNodeId("HelloWorld/DynamicStructTypeVariable"))
                    .setAccessLevel(AccessLevel.READ_WRITE)
                    .setUserAccessLevel(AccessLevel.READ_WRITE)
                    .setBrowseName(newQualifiedName("DynamicStructTypeVariable"))
                    .setDisplayName(LocalizedText.english("DynamicStructTypeVariable"))
                    .setDataType(dataType.getNodeId())
                    .setTypeDefinition(NodeIds.BaseDataVariableType)
                    .build());

    LinkedHashMap<String, Object> members = new LinkedHashMap<>();
    members.put("foo", "foo");
    members.put("bar", uint(42));
    members.put("baz", true);
    DynamicStructType value = new DynamicStructType(dataType, members);

    // Note that we're using getDynamicEncodingContext() here
    NodeId binaryEncodingId = dataType.getBinaryEncodingId();
    assert binaryEncodingId != null;

    ExtensionObject xo = ExtensionObject.encode(getServer().getDynamicEncodingContext(), value);

    dynamicStructTypeVariable.setValue(new DataValue(Variant.ofExtensionObject(xo)));

    getNodeManager().addNode(dynamicStructTypeVariable);

    dynamicStructTypeVariable.addReference(
        new Reference(
            dynamicStructTypeVariable.getNodeId(),
            NodeIds.Organizes,
            rootFolder.getNodeId().expanded(),
            false));
  }

  @Override
  public void onDataItemsCreated(List<DataItem> dataItems) {
    subscriptionModel.onDataItemsCreated(dataItems);
  }

  @Override
  public void onDataItemsModified(List<DataItem> dataItems) {
    subscriptionModel.onDataItemsModified(dataItems);
  }

  @Override
  public void onDataItemsDeleted(List<DataItem> dataItems) {
    subscriptionModel.onDataItemsDeleted(dataItems);
  }

  @Override
  public void onMonitoringModeChanged(List<MonitoredItem> monitoredItems) {
    subscriptionModel.onMonitoringModeChanged(monitoredItems);
  }
}
