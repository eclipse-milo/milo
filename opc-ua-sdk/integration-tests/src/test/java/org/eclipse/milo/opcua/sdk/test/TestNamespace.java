/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.test;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.server.Lifecycle;
import org.eclipse.milo.opcua.sdk.server.ManagedNamespaceWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.InvalidArgumentException;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataTypeEncodingTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.AnalogItemTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaDataTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.factories.NodeFactory;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.DataTypeEncoding;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.PermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestNamespace extends ManagedNamespaceWithLifecycle {

  public static final String NAMESPACE_URI = "urn:eclipse:milo:test";

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private volatile Thread eventThread;
  private volatile boolean keepPostingEvents = true;

  public TestNamespace(OpcUaServer server) {
    super(server, NAMESPACE_URI);

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

    getLifecycleManager()
        .addStartupTask(
            () -> {
              UaVariableNode testInt32Node =
                  new UaVariableNode.UaVariableNodeBuilder(getNodeContext())
                      .setNodeId(newNodeId("TestInt32"))
                      .setAccessLevel(AccessLevel.READ_WRITE)
                      .setUserAccessLevel(AccessLevel.READ_WRITE)
                      .setBrowseName(newQualifiedName("TestInt32"))
                      .setDisplayName(LocalizedText.english("TestInt32"))
                      .setDataType(NodeIds.Int32)
                      .setTypeDefinition(NodeIds.BaseDataVariableType)
                      .setRolePermissions(
                          new RolePermissionType[] {
                            new RolePermissionType(newNodeId("roleId"), new PermissionType(uint(0)))
                          })
                      .setUserRolePermissions(
                          new RolePermissionType[] {
                            new RolePermissionType(newNodeId("roleId"), new PermissionType(uint(0)))
                          })
                      .setAccessRestrictions(new AccessRestrictionType(ushort(0)))
                      .build();

              testInt32Node.setValue(new DataValue(new Variant(0)));

              testInt32Node.addReference(
                  new Reference(
                      testInt32Node.getNodeId(),
                      NodeIds.HasComponent,
                      NodeIds.ObjectsFolder.expanded(),
                      Reference.Direction.INVERSE));

              getNodeManager().addNode(testInt32Node);
            });

    getLifecycleManager()
        .addStartupTask(
            () -> {
              try {
                AnalogItemTypeNode node =
                    (AnalogItemTypeNode)
                        getNodeFactory()
                            .createNode(
                                newNodeId("TestAnalogValue"),
                                NodeIds.AnalogItemType,
                                new NodeFactory.InstantiationCallback() {
                                  @Override
                                  public boolean includeOptionalNode(
                                      NodeId typeDefinitionId, QualifiedName browseName) {
                                    return true;
                                  }
                                });

                node.setBrowseName(newQualifiedName("TestAnalogValue"));
                node.setDisplayName(LocalizedText.english("TestAnalogValue"));
                node.setDataType(NodeIds.Double);
                node.setValue(new DataValue(new Variant(3.14d)));

                node.setEuRange(new Range(0.0, 100.0));

                getNodeManager().addNode(node);
              } catch (UaException e) {
                throw new RuntimeException(e);
              }
            });

    getLifecycleManager()
        .addStartupTask(
            () -> {
              UaMethodNode.build(
                  getNodeContext(),
                  b -> {
                    b.setNodeId(newNodeId("sqrt(x)"));
                    b.setBrowseName(newQualifiedName("sqrt(x)"));
                    b.setDisplayName(LocalizedText.english("sqrt(x)"));

                    b.addReference(
                        new Reference(
                            b.getNodeId(),
                            NodeIds.HasComponent,
                            NodeIds.ObjectsFolder.expanded(),
                            Reference.Direction.INVERSE));

                    UaMethodNode methodNode = b.buildAndAdd();

                    SqrtMethod sqrtMethod = new SqrtMethod(methodNode);
                    methodNode.setInputArguments(sqrtMethod.getInputArguments());
                    methodNode.setOutputArguments(sqrtMethod.getOutputArguments());
                    methodNode.setInvocationHandler(sqrtMethod);

                    return methodNode;
                  });

              UaMethodNode.build(
                  getNodeContext(),
                  b -> {
                    b.setNodeId(newNodeId("sqrt2(x)"));
                    b.setBrowseName(newQualifiedName("sqrt2(x)"));
                    b.setDisplayName(LocalizedText.english("sqrt2(x)"));

                    b.addReference(
                        new Reference(
                            b.getNodeId(),
                            NodeIds.HasOrderedComponent,
                            NodeIds.ObjectsFolder.expanded(),
                            Reference.Direction.INVERSE));

                    UaMethodNode methodNode = b.buildAndAdd();

                    SqrtMethod sqrtMethod = new SqrtMethod(methodNode);
                    methodNode.setInputArguments(sqrtMethod.getInputArguments());
                    methodNode.setOutputArguments(sqrtMethod.getOutputArguments());
                    methodNode.setInvocationHandler(sqrtMethod);

                    return methodNode;
                  });

              UaMethodNode.build(
                  getNodeContext(),
                  b -> {
                    b.setNodeId(newNodeId("hasNoInputsOrOutputs()"));
                    b.setBrowseName(newQualifiedName("hasNoInputsOrOutputs()"));
                    b.setDisplayName(LocalizedText.english("hasNoInputsOrOutputs()"));

                    b.addReference(
                        new Reference(
                            b.getNodeId(),
                            NodeIds.HasOrderedComponent,
                            NodeIds.ObjectsFolder.expanded(),
                            Reference.Direction.INVERSE));

                    UaMethodNode methodNode = b.buildAndAdd();

                    methodNode.setInvocationHandler(
                        new AbstractMethodInvocationHandler(methodNode) {
                          @Override
                          public Argument[] getInputArguments() {
                            return new Argument[0];
                          }

                          @Override
                          public Argument[] getOutputArguments() {
                            return new Argument[0];
                          }

                          @Override
                          protected Variant[] invoke(
                              InvocationContext invocationContext, Variant[] inputValues) {
                            return new Variant[0];
                          }
                        });

                    return methodNode;
                  });

              UaMethodNode.build(
                  getNodeContext(),
                  b -> {
                    b.setNodeId(newNodeId("onlyAcceptsPositiveInputs()"));
                    b.setBrowseName(newQualifiedName("onlyAcceptsPositiveInputs()"));
                    b.setDisplayName(LocalizedText.english("onlyAcceptsPositiveInputs()"));

                    b.addReference(
                        new Reference(
                            b.getNodeId(),
                            NodeIds.HasOrderedComponent,
                            NodeIds.ObjectsFolder.expanded(),
                            Reference.Direction.INVERSE));

                    UaMethodNode methodNode = b.buildAndAdd();

                    methodNode.setInvocationHandler(
                        new AbstractMethodInvocationHandler(methodNode) {
                          @Override
                          public Argument[] getInputArguments() {
                            return new Argument[] {
                              new Argument(
                                  "i",
                                  NodeIds.Int32,
                                  ValueRanks.Scalar,
                                  null,
                                  LocalizedText.NULL_VALUE)
                            };
                          }

                          @Override
                          public Argument[] getOutputArguments() {
                            return new Argument[0];
                          }

                          @Override
                          protected void validateInputArgumentValues(Variant[] inputArgumentValues)
                              throws InvalidArgumentException {

                            int i = (int) inputArgumentValues[0].getValue();

                            if (i < 0) {
                              StatusCode[] inputArgumentResults = {
                                new StatusCode(StatusCodes.Bad_OutOfRange)
                              };

                              throw new InvalidArgumentException(inputArgumentResults);
                            }
                          }

                          @Override
                          protected Variant[] invoke(
                              InvocationContext invocationContext, Variant[] inputValues) {
                            return new Variant[0];
                          }
                        });

                    return methodNode;
                  });

              UaMethodNode.build(
                  getNodeContext(),
                  b -> {
                    b.setNodeId(newNodeId("scalarAbstractTypeEcho()"));
                    b.setBrowseName(newQualifiedName("scalarAbstractTypeEcho()"));
                    b.setDisplayName(LocalizedText.english("scalarAbstractTypeEcho()"));

                    b.addReference(
                        new Reference(
                            b.getNodeId(),
                            NodeIds.HasOrderedComponent,
                            NodeIds.ObjectsFolder.expanded(),
                            Reference.Direction.INVERSE));

                    UaMethodNode methodNode = b.buildAndAdd();
                    methodNode.setInvocationHandler(new ScalarAbstractTypeMethod(methodNode));

                    return methodNode;
                  });

              UaMethodNode.build(
                  getNodeContext(),
                  b -> {
                    b.setNodeId(newNodeId("scalarSimpleTypeEcho()"));
                    b.setBrowseName(newQualifiedName("scalarSimpleTypeEcho()"));
                    b.setDisplayName(LocalizedText.english("scalarSimpleTypeEcho()"));

                    b.addReference(
                        new Reference(
                            b.getNodeId(),
                            NodeIds.HasOrderedComponent,
                            NodeIds.ObjectsFolder.expanded(),
                            Reference.Direction.INVERSE));

                    UaMethodNode methodNode = b.buildAndAdd();
                    methodNode.setInvocationHandler(new ScalarSimpleTypeMethod(methodNode));

                    return methodNode;
                  });

              UaMethodNode.build(
                  getNodeContext(),
                  b -> {
                    b.setNodeId(newNodeId("scalarStructureEcho()"));
                    b.setBrowseName(newQualifiedName("scalarStructureEcho()"));
                    b.setDisplayName(LocalizedText.english("scalarStructureEcho()"));

                    b.addReference(
                        new Reference(
                            b.getNodeId(),
                            NodeIds.HasOrderedComponent,
                            NodeIds.ObjectsFolder.expanded(),
                            Reference.Direction.INVERSE));

                    UaMethodNode methodNode = b.buildAndAdd();
                    methodNode.setInvocationHandler(new ScalarStructureMethod(methodNode));

                    return methodNode;
                  });

              UaMethodNode.build(
                  getNodeContext(),
                  b -> {
                    b.setNodeId(newNodeId("scalarAbstractStructureEcho()"));
                    b.setBrowseName(newQualifiedName("scalarAbstractStructureEcho()"));
                    b.setDisplayName(LocalizedText.english("scalarAbstractStructureEcho()"));

                    b.addReference(
                        new Reference(
                            b.getNodeId(),
                            NodeIds.HasOrderedComponent,
                            NodeIds.ObjectsFolder.expanded(),
                            Reference.Direction.INVERSE));

                    UaMethodNode methodNode = b.buildAndAdd();
                    methodNode.setInvocationHandler(new ScalarAbstractStructureMethod(methodNode));

                    return methodNode;
                  });
            });

    getLifecycleManager()
        .addStartupTask(
            () -> {
              try {
                registerMatrixTestType();

                UaVariableNode node =
                    new UaVariableNode.UaVariableNodeBuilder(getNodeContext())
                        .setNodeId(newNodeId("MatrixTestTypeValue"))
                        .setAccessLevel(AccessLevel.READ_WRITE)
                        .setUserAccessLevel(AccessLevel.READ_WRITE)
                        .setBrowseName(newQualifiedName("MatrixTestTypeValue"))
                        .setDisplayName(LocalizedText.english("MatrixTestTypeValue"))
                        .setDataType(
                            MatrixTestType.TYPE_ID.toNodeIdOrThrow(server.getNamespaceTable()))
                        .setTypeDefinition(NodeIds.BaseDataVariableType)
                        .setRolePermissions(
                            new RolePermissionType[] {
                              new RolePermissionType(
                                  newNodeId("roleId"), new PermissionType(uint(0)))
                            })
                        .setUserRolePermissions(
                            new RolePermissionType[] {
                              new RolePermissionType(
                                  newNodeId("roleId"), new PermissionType(uint(0)))
                            })
                        .setAccessRestrictions(new AccessRestrictionType(ushort(0)))
                        .build();

                MatrixTestType value =
                    new MatrixTestType(
                        new Integer[][] {
                          new Integer[] {0, 1},
                          new Integer[] {2, 3}
                        },
                        new ApplicationType[][] {
                          new ApplicationType[] {ApplicationType.Server, ApplicationType.Client},
                          new ApplicationType[] {
                            ApplicationType.ClientAndServer, ApplicationType.DiscoveryServer
                          }
                        },
                        new XVType[][] {
                          new XVType[] {new XVType(0.0d, 1.0f), new XVType(2.0d, 3.0f)},
                          new XVType[] {new XVType(4.0d, 5.0f), new XVType(6.0d, 7.0f)}
                        });

                node.setValue(new DataValue(new Variant(value)));

                node.addReference(
                    new Reference(
                        node.getNodeId(),
                        NodeIds.HasComponent,
                        NodeIds.ObjectsFolder.expanded(),
                        Reference.Direction.INVERSE));

                getNodeManager().addNode(node);
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            });
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
                try {
                  Thread.sleep(2_000);
                } catch (InterruptedException ignored) {
                  // ignored
                }

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
                    eventNode.setSourceName(serverNode.getDisplayName().getText());
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

  private void registerMatrixTestType() throws Exception {
    // Get the NodeId for the DataType and encoding Nodes.
    NodeId dataTypeId = MatrixTestType.TYPE_ID.toNodeIdOrThrow(getServer().getNamespaceTable());
    NodeId binaryEncodingId =
        MatrixTestType.BINARY_ENCODING_ID.toNodeIdOrThrow(getServer().getNamespaceTable());
    NodeId jsonEncodingId =
        MatrixTestType.JSON_ENCODING_ID.toNodeIdOrThrow(getServer().getNamespaceTable());

    // Add a custom DataTypeNode with a SubtypeOf reference to Structure
    UaDataTypeNode dataTypeNode =
        new UaDataTypeNode(
            getNodeContext(),
            dataTypeId,
            newQualifiedName("MatrixTestType"),
            LocalizedText.english("MatrixTestType"),
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

    // Add encoding nodes
    addEncodingNode(dataTypeId, binaryEncodingId, DataTypeEncoding.BINARY_ENCODING_NAME);
    addEncodingNode(dataTypeId, jsonEncodingId, DataTypeEncoding.JSON_ENCODING_NAME);

    // Define the structure
    StructureField[] fields =
        new StructureField[] {
          new StructureField(
              "BuiltinMatrix",
              LocalizedText.NULL_VALUE,
              NodeIds.Int32,
              2,
              new UInteger[] {uint(2), uint(2)},
              getServer().getConfig().getLimits().getMaxStringLength(),
              false),
          new StructureField(
              "EnumMatrix",
              LocalizedText.NULL_VALUE,
              NodeIds.ApplicationType,
              2,
              new UInteger[] {uint(2), uint(2)},
              getServer().getConfig().getLimits().getMaxStringLength(),
              false),
          new StructureField(
              "StructMatrix",
              LocalizedText.NULL_VALUE,
              NodeIds.XVType,
              2,
              new UInteger[] {uint(2), uint(2)},
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
        .registerType(
            dataTypeId, new MatrixTestType.Codec(), binaryEncodingId, null, jsonEncodingId);
  }

  private void addEncodingNode(NodeId dataTypeId, NodeId encodingId, QualifiedName encodingName) {
    DataTypeEncodingTypeNode dataTypeEncodingNode =
        new DataTypeEncodingTypeNode(
            getNodeContext(),
            encodingId,
            encodingName,
            LocalizedText.english(encodingName.getName()),
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
  }

  @Override
  public void onDataItemsCreated(List<DataItem> dataItems) {}

  @Override
  public void onDataItemsModified(List<DataItem> dataItems) {}

  @Override
  public void onDataItemsDeleted(List<DataItem> dataItems) {}

  @Override
  public void onMonitoringModeChanged(List<MonitoredItem> monitoredItems) {}

  public void configure(BiConsumer<UaNodeContext, UaNodeManager> consumer) {
    consumer.accept(getNodeContext(), getNodeManager());
  }

  static class ScalarAbstractTypeMethod extends AbstractEchoMethod {

    public ScalarAbstractTypeMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    protected Argument getInputArgument() {
      return new Argument(
          "Input", NodeIds.Number, ValueRanks.Scalar, null, LocalizedText.NULL_VALUE);
    }
  }

  static class ScalarSimpleTypeMethod extends AbstractEchoMethod {

    public ScalarSimpleTypeMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    protected Argument getInputArgument() {
      return new Argument(
          "Input", NodeIds.Duration, ValueRanks.Scalar, null, LocalizedText.NULL_VALUE);
    }
  }

  static class ScalarStructureMethod extends AbstractEchoMethod {

    public ScalarStructureMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    protected Argument getInputArgument() {
      return new Argument(
          "Input", NodeIds.XVType, ValueRanks.Scalar, null, LocalizedText.NULL_VALUE);
    }
  }

  static class ScalarAbstractStructureMethod extends AbstractEchoMethod {

    public ScalarAbstractStructureMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    protected Argument getInputArgument() {
      return new Argument(
          "Input", NodeIds.Structure, ValueRanks.Scalar, null, LocalizedText.NULL_VALUE);
    }
  }

  abstract static class AbstractEchoMethod extends AbstractMethodInvocationHandler {

    /**
     * @param node the {@link UaMethodNode} this handler will be installed on.
     */
    public AbstractEchoMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return new Argument[] {getInputArgument()};
    }

    @Override
    public Argument[] getOutputArguments() {
      Argument inputArgument = getInputArgument();

      return new Argument[] {
        new Argument(
            "Output",
            inputArgument.getDataType(),
            inputArgument.getValueRank(),
            inputArgument.getArrayDimensions(),
            LocalizedText.english("Echo of the input argument."))
      };
    }

    @Override
    protected Variant[] invoke(InvocationContext invocationContext, Variant[] inputValues)
        throws UaException {
      return new Variant[] {inputValues[0]};
    }

    protected abstract Argument getInputArgument();
  }
}
