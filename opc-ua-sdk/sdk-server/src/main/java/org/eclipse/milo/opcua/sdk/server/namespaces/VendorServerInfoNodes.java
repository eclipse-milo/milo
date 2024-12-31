/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.namespaces;

import com.sun.management.UnixOperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.util.Optional;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilters;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

public class VendorServerInfoNodes {

  public static void add(UaNodeContext context) {
    addVendorServerInfoNodes(context);
  }

  private static void addVendorServerInfoNodes(UaNodeContext context) {
    Optional<UaNode> maybeVendorServerInfo =
        context
            .getServer()
            .getAddressSpaceManager()
            .getManagedNode(NodeIds.Server_VendorServerInfo);

    maybeVendorServerInfo.ifPresent(
        node -> {
          UaObjectNode vendorServerInfo = (UaObjectNode) node;

          // standard Java API
          addVendorInfoPlainJava(context, vendorServerInfo);

          // JMX API
          addVendorInfoJmx(context, vendorServerInfo);

          // com.sun API
          addVendorInfoSunJmx(context, vendorServerInfo);
        });
  }

  private static void addVendorInfoPlainJava(UaNodeContext context, UaObjectNode vendorServerInfo) {
    UaVariableNode availableProcessors =
        UaVariableNode.build(
            context,
            b ->
                b.setNodeId(new NodeId(1, "VendorServerInfo/AvailableProcessors"))
                    .setBrowseName(new QualifiedName(1, "AvailableProcessors"))
                    .setDisplayName(LocalizedText.english("AvailableProcessors"))
                    .setDataType(NodeIds.Int32)
                    .buildAndAdd());

    availableProcessors
        .getFilterChain()
        .addLast(
            AttributeFilters.getValue(
                ctx -> new DataValue(new Variant(Runtime.getRuntime().availableProcessors()))));

    vendorServerInfo.addComponent(availableProcessors);
  }

  private static void addVendorInfoJmx(UaNodeContext context, UaObjectNode vendorServerInfo) {
    OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
    MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();

    UaVariableNode usedMemory =
        UaVariableNode.build(
            context,
            b ->
                b.setNodeId(new NodeId(1, "VendorServerInfo/UsedMemory"))
                    .setBrowseName(new QualifiedName(1, "UsedMemory"))
                    .setDisplayName(LocalizedText.english("UsedMemory"))
                    .setDataType(NodeIds.Int64)
                    .buildAndAdd());

    usedMemory
        .getFilterChain()
        .addLast(
            AttributeFilters.getValue(
                ctx -> new DataValue(new Variant(memoryBean.getHeapMemoryUsage().getUsed()))));

    UaVariableNode maxMemory =
        UaVariableNode.build(
            context,
            b ->
                b.setNodeId(new NodeId(1, "VendorServerInfo/MaxMemory"))
                    .setBrowseName(new QualifiedName(1, "MaxMemory"))
                    .setDisplayName(LocalizedText.english("MaxMemory"))
                    .setDataType(NodeIds.Int64)
                    .buildAndAdd());

    maxMemory
        .getFilterChain()
        .addLast(
            AttributeFilters.getValue(
                ctx -> new DataValue(new Variant(memoryBean.getHeapMemoryUsage().getMax()))));

    UaVariableNode osName =
        UaVariableNode.build(
            context,
            b ->
                b.setNodeId(new NodeId(1, "VendorServerInfo/OsName"))
                    .setBrowseName(new QualifiedName(1, "OsName"))
                    .setDisplayName(LocalizedText.english("OsName"))
                    .setDataType(NodeIds.String)
                    .buildAndAdd());

    osName
        .getFilterChain()
        .addLast(AttributeFilters.getValue(ctx -> new DataValue(new Variant(osBean.getName()))));

    UaVariableNode osArch =
        UaVariableNode.build(
            context,
            b ->
                b.setNodeId(new NodeId(1, "VendorServerInfo/OsArch"))
                    .setBrowseName(new QualifiedName(1, "OsArch"))
                    .setDisplayName(LocalizedText.english("OsArch"))
                    .setDataType(NodeIds.String)
                    .buildAndAdd());

    osArch
        .getFilterChain()
        .addLast(AttributeFilters.getValue(ctx -> new DataValue(new Variant(osBean.getArch()))));

    UaVariableNode osVersion =
        UaVariableNode.build(
            context,
            b ->
                b.setNodeId(new NodeId(1, "VendorServerInfo/OsVersion"))
                    .setBrowseName(new QualifiedName(1, "OsVersion"))
                    .setDisplayName(LocalizedText.english("OsVersion"))
                    .setDataType(NodeIds.String)
                    .buildAndAdd());

    osVersion
        .getFilterChain()
        .addLast(AttributeFilters.getValue(ctx -> new DataValue(new Variant(osBean.getVersion()))));

    vendorServerInfo.addComponent(usedMemory);
    vendorServerInfo.addComponent(maxMemory);
    vendorServerInfo.addComponent(osName);
    vendorServerInfo.addComponent(osArch);
    vendorServerInfo.addComponent(osVersion);
  }

  private static void addVendorInfoSunJmx(UaNodeContext context, UaObjectNode vendorServerInfo) {
    OperatingSystemMXBean osMxBean = ManagementFactory.getOperatingSystemMXBean();

    if (osMxBean instanceof com.sun.management.OperatingSystemMXBean) {
      com.sun.management.OperatingSystemMXBean sunOsMxBean =
          (com.sun.management.OperatingSystemMXBean) osMxBean;

      UaVariableNode processCpuLoad =
          UaVariableNode.build(
              context,
              b ->
                  b.setNodeId(new NodeId(1, "VendorServerInfo/ProcessCpuLoad"))
                      .setBrowseName(new QualifiedName(1, "ProcessCpuLoad"))
                      .setDisplayName(LocalizedText.english("ProcessCpuLoad"))
                      .setDataType(NodeIds.Double)
                      .buildAndAdd());

      processCpuLoad
          .getFilterChain()
          .addLast(
              AttributeFilters.getValue(
                  ctx -> new DataValue(new Variant(sunOsMxBean.getProcessCpuLoad() * 100d))));

      UaVariableNode systemCpuLoad =
          UaVariableNode.build(
              context,
              b ->
                  b.setNodeId(new NodeId(1, "VendorServerInfo/SystemCpuLoad"))
                      .setBrowseName(new QualifiedName(1, "SystemCpuLoad"))
                      .setDisplayName(LocalizedText.english("SystemCpuLoad"))
                      .setDataType(NodeIds.Double)
                      .buildAndAdd());

      systemCpuLoad
          .getFilterChain()
          .addLast(
              AttributeFilters.getValue(
                  ctx -> new DataValue(new Variant(sunOsMxBean.getSystemCpuLoad() * 100d))));

      vendorServerInfo.addComponent(processCpuLoad);
      vendorServerInfo.addComponent(systemCpuLoad);

      if (sunOsMxBean instanceof UnixOperatingSystemMXBean) {
        UnixOperatingSystemMXBean unixBean = (UnixOperatingSystemMXBean) sunOsMxBean;

        UaVariableNode openFileDescriptors =
            UaVariableNode.build(
                context,
                b ->
                    b.setNodeId(new NodeId(1, "VendorServerInfo/OpenFileDescriptors"))
                        .setBrowseName(new QualifiedName(1, "OpenFileDescriptors"))
                        .setDisplayName(LocalizedText.english("OpenFileDescriptors"))
                        .setDataType(NodeIds.Int64)
                        .buildAndAdd());

        openFileDescriptors
            .getFilterChain()
            .addLast(
                AttributeFilters.getValue(
                    ctx -> new DataValue(new Variant(unixBean.getOpenFileDescriptorCount()))));

        UaVariableNode maxFileDescriptors =
            UaVariableNode.build(
                context,
                b ->
                    b.setNodeId(new NodeId(1, "VendorServerInfo/MaxFileDescriptors"))
                        .setBrowseName(new QualifiedName(1, "MaxFileDescriptors"))
                        .setDisplayName(LocalizedText.english("MaxFileDescriptors"))
                        .setDataType(NodeIds.Int64)
                        .buildAndAdd());

        maxFileDescriptors
            .getFilterChain()
            .addLast(
                AttributeFilters.getValue(
                    ctx -> new DataValue(new Variant(unixBean.getMaxFileDescriptorCount()))));

        vendorServerInfo.addComponent(openFileDescriptors);
        vendorServerInfo.addComponent(maxFileDescriptors);
      }
    }
  }
}
