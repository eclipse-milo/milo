/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.mqtt;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5ClientBuilder;
import com.hivemq.client.mqtt.mqtt5.message.auth.Mqtt5SimpleAuth;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import io.netty.buffer.Unpooled;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetFieldValue;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetSource;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerSecurityConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderMessageSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterMessageSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetReaderSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.MqttConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetReaderSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupMessageSettings;
import org.eclipse.milo.opcua.sdk.pubsub.transport.BrokerTopics;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedDataSetMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedField;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedMetaData;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpMessageMapping;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldMetaData;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.jspecify.annotations.Nullable;

/**
 * Interactive command line harness for PubSub interop testing against third-party implementations
 * (e.g. a Prosys OPC UA server publishing or subscribing on the local network, or any MQTT broker
 * such as Mosquitto or HiveMQ).
 *
 * <p>Not a JUnit test; run it via the exec plugin from the test classpath (this module's test
 * classpath sees sdk-pubsub with the built-in "uadp" and "json" mappings, the MQTT transport, and
 * the HiveMQ client):
 *
 * <pre>{@code
 * mvn -q -pl opc-ua-sdk/sdk-pubsub-mqtt -am test-compile exec:java \
 *   -Dexec.classpathScope=test \
 *   -Dexec.mainClass=org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubInteropTool \
 *   -Dexec.args="subscribe 224.0.2.14 4840"
 * }</pre>
 *
 * <p>Modes:
 *
 * <ul>
 *   <li>{@code subscribe}: a PubSubService DataSetReader over UDP/UADP with wildcard filters by
 *       default and {@link MetadataPolicy#ACCEPT_DISCOVERED}, printing every received DataSet,
 *       metadata announcement, and state change, plus a diagnostics dump every 10 seconds.
 *   <li>{@code raw}: no engine; receives datagrams on a plain socket, hexdumps each packet,
 *       attempts a UADP decode, and saves every packet to /tmp/pubsub-capture as a golden interop
 *       fixture.
 *   <li>{@code publish}: publishes the "MiloInterop" dataset (Ramp, Counter, Message, Toggle) over
 *       UDP/UADP with interop-friendly header masks.
 *   <li>{@code mqtt-publish}: publishes the "MiloInterop" dataset through an MQTT broker with the
 *       "json" (default) or "uadp" mapping; topics default to the Part 14 §7.3.4.7 standardized
 *       tree (printed at startup) and metadata is published retained by the engine.
 *   <li>{@code mqtt-subscribe}: a PubSubService DataSetReader on broker topics; the data topic is
 *       required, field names come from configured {@code --field} metadata or from an optional
 *       metadata topic.
 *   <li>{@code mqtt-raw}: no engine; a plain MQTT 5 client subscribed to a topic filter, printing
 *       every message as pretty JSON or a hex dump.
 * </ul>
 *
 * <p>For example, to publish the "MiloInterop" dataset over MQTT with the default "json" mapping
 * (the resolved topic tree is printed at startup):
 *
 * <pre>{@code
 * mvn -q -pl opc-ua-sdk/sdk-pubsub-mqtt -am test-compile exec:java \
 *   -Dexec.classpathScope=test \
 *   -Dexec.mainClass=org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubInteropTool \
 *   -Dexec.args="mqtt-publish mqtt://localhost:1883"
 * }</pre>
 *
 * <p>And, against the same broker, to subscribe to that publisher's JSON data topic (metadata
 * supplies the field names, so no {@code --field} is required):
 *
 * <pre>{@code
 * mvn -q -pl opc-ua-sdk/sdk-pubsub-mqtt -am test-compile exec:java \
 *   -Dexec.classpathScope=test \
 *   -Dexec.mainClass=org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubInteropTool \
 *   -Dexec.args="mqtt-subscribe mqtt://localhost:1883 \
 *       --topic opcua/json/data/62541/MiloInterop \
 *       --metadata-topic opcua/json/metadata/62541/MiloInterop/writer"
 * }</pre>
 *
 * <p>The {@code uadp} mapping works the same way; publish with {@code --mapping uadp}:
 *
 * <pre>{@code
 * mvn -q -pl opc-ua-sdk/sdk-pubsub-mqtt -am test-compile exec:java \
 *   -Dexec.classpathScope=test \
 *   -Dexec.mainClass=org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubInteropTool \
 *   -Dexec.args="mqtt-publish mqtt://localhost:1883 --mapping uadp"
 * }</pre>
 *
 * <p>And subscribe on the {@code uadp} topic tree (the binary UADP payload carries no field names,
 * so either subscribe to the metadata topic or declare the fields with {@code --field}):
 *
 * <pre>{@code
 * mvn -q -pl opc-ua-sdk/sdk-pubsub-mqtt -am test-compile exec:java \
 *   -Dexec.classpathScope=test \
 *   -Dexec.mainClass=org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubInteropTool \
 *   -Dexec.args="mqtt-subscribe mqtt://localhost:1883 --mapping uadp \
 *       --topic opcua/uadp/data/62541/MiloInterop \
 *       --field Ramp:double --field Counter:int32 \
 *       --field Message:string --field Toggle:bool"
 * }</pre>
 */
public final class PubSubInteropTool {

  private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

  private static final Path CAPTURE_DIR = Path.of("/tmp/pubsub-capture");

  private static final Gson PRETTY_GSON =
      new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().create();

  private static final String USAGE =
      """
      usage: PubSubInteropTool <mode> ...

      UDP/UADP modes: <mode> <address> <port> [options]

        subscribe <address> <port> [--interface <name>] [--publisher-id <type:value>]
                  [--writer-group-id <n>] [--data-set-writer-id <n>]
            Run a PubSubService DataSetReader and print every received DataSet,
            metadata announcement, and state change, plus a diagnostics dump every
            10 seconds. All filters are wildcards unless specified (id 0 = wildcard).

        raw <address> <port> [--interface <name>]
            No engine: receive raw datagrams, hexdump each packet, attempt a UADP
            decode, and save every packet to /tmp/pubsub-capture/.

        publish <address> <port> [--interface <name>] [--publisher-id <type:value>]
                [--writer-group-id <n>] [--data-set-writer-id <n>]
                [--interval-ms <n>] [--datavalue]
            Publish the "MiloInterop" dataset: Ramp (Double sawtooth 0..100),
            Counter (Int32), Message (String "milo-<counter>"), Toggle (Boolean).
            Defaults: publisher-id uint16:62541, writer-group-id 100,
            data-set-writer-id 1, interval-ms 100. Fields are Variant-encoded by
            default; --datavalue switches to DataValue with StatusCode and
            SourceTimestamp.

        <address> is treated as multicast if it is a multicast IP, unicast otherwise.

      MQTT modes: <mode> <brokerUri> [options]
        <brokerUri> e.g. mqtt://localhost:1883 (mqtts:// for TLS). All MQTT modes
        accept --username <u>, --password <p>, and --client-id <id>; the engine modes
        also accept --mqtt-version 5.0|3.1.1|best (default best = MQTT 5 with
        fallback to 3.1.1).

        mqtt-publish <brokerUri> [--mapping json|uadp] [--publisher-id <type:value>]
                     [--group <name>] [--topic <dataTopic>] [--metadata-topic <t>]
                     [--metadata-interval <ms>] [--qos 0|1|2] [--interval <ms>]
            Publish the "MiloInterop" dataset (as above, Variant-encoded) through an
            MQTT broker. Defaults: mapping json, publisher-id uint16:62541, group
            "MiloInterop", writer-group-id 100, data-set-writer-id 1, interval 1000,
            qos 0. Topics default to the Part 14 standardized tree
            opcua/<json|uadp>/data/<publisherId>/<group> (metadata:
            .../metadata/<publisherId>/<group>/writer); the resolved topics are
            printed at startup. Metadata is published retained at activation and on
            change; --metadata-interval > 0 republishes it periodically.

        mqtt-subscribe <brokerUri> --topic <dataTopic> [--mapping json|uadp]
                       [--metadata-topic <t>] [--field <name>:<type>]...
                       [--publisher-id <type:value>] [--writer-group-id <n>]
                       [--data-set-writer-id <n>]
            Run a PubSubService DataSetReader on broker topics, printing DataSets,
            metadata, and state changes like the subscribe mode. --topic is REQUIRED
            (MQTT readers cannot derive the publisher's topic names). Filters are
            wildcards unless specified; a --publisher-id filter also matches JSON
            string ids with the same canonical value. Repeatable --field entries
            configure named metadata (REQUIRE_CONFIGURED, ConfigurationVersion 1.1,
            matching the publish modes); without --field the policy is
            ACCEPT_DISCOVERED and field names arrive via --metadata-topic or stay
            generic. Field types: bool|sbyte|byte|int16|uint16|int32|uint32|int64|
            uint64|float|double|string|datetime.

        mqtt-raw <brokerUri> [--topic <filter>]
            No engine: a plain MQTT 5 client subscribed to --topic (default
            "opcua/#", MQTT wildcards allowed) printing each message's topic, retain
            flag, QoS, and payload size, plus the payload pretty-printed as JSON when
            it parses as UTF-8 JSON, else hexdumped (first 256 bytes).

      --publisher-id types: byte|uint8, uint16, uint32, uint64, string
          (e.g. uint16:2234 or string:line-7)

      examples:
        subscribe 224.0.2.14 4840
        publish 127.0.0.1 4840 --interval-ms 500 --datavalue
        mqtt-publish mqtt://localhost:1883 --metadata-interval 5000
        mqtt-publish mqtt://localhost:1883 --mapping uadp --qos 1
        mqtt-subscribe mqtt://localhost:1883 --topic opcua/json/data/62541/MiloInterop
            --metadata-topic opcua/json/metadata/62541/MiloInterop/writer
        mqtt-subscribe mqtt://localhost:1883 --topic opcua/uadp/data/62541/MiloInterop
            --mapping uadp --field Ramp:double --field Counter:int32
        mqtt-raw mqtt://localhost:1883 --topic "opcua/#"\
      """;

  private PubSubInteropTool() {}

  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      throw usageError(null);
    }

    switch (args[0]) {
      case "subscribe" ->
          runSubscribe(
              parseArgs(
                  args,
                  Set.of(
                      "--interface",
                      "--publisher-id",
                      "--writer-group-id",
                      "--data-set-writer-id")));
      case "raw" -> runRaw(parseArgs(args, Set.of("--interface")));
      case "publish" ->
          runPublish(
              parseArgs(
                  args,
                  Set.of(
                      "--interface",
                      "--publisher-id",
                      "--writer-group-id",
                      "--data-set-writer-id",
                      "--interval-ms",
                      "--datavalue")));
      case "mqtt-publish" ->
          runMqttPublish(
              parseMqttArgs(
                  args,
                  Set.of(
                      "--mapping",
                      "--publisher-id",
                      "--group",
                      "--topic",
                      "--metadata-topic",
                      "--metadata-interval",
                      "--qos",
                      "--interval",
                      "--username",
                      "--password",
                      "--client-id",
                      "--mqtt-version")));
      case "mqtt-subscribe" ->
          runMqttSubscribe(
              parseMqttArgs(
                  args,
                  Set.of(
                      "--mapping",
                      "--topic",
                      "--metadata-topic",
                      "--field",
                      "--publisher-id",
                      "--writer-group-id",
                      "--data-set-writer-id",
                      "--username",
                      "--password",
                      "--client-id",
                      "--mqtt-version")));
      case "mqtt-raw" ->
          runMqttRaw(
              parseMqttArgs(args, Set.of("--topic", "--username", "--password", "--client-id")));
      default -> throw usageError("unknown mode: " + args[0]);
    }
  }

  // region subscribe

  private static void runSubscribe(Args args) throws Exception {
    UdpDatagramAddress address = datagramAddress(args);

    // Wildcard filters by default; ACCEPT_DISCOVERED with no configured metadata, so the engine
    // names fields "Field_<i>" until a metadata announcement is received.
    DataSetReaderConfig.Builder reader =
        DataSetReaderConfig.builder("reader").metadataPolicy(MetadataPolicy.ACCEPT_DISCOVERED);
    if (args.publisherId != null) {
      reader.publisherId(args.publisherId);
    }
    if (args.writerGroupId != 0) {
      reader.writerGroupId(ushort(args.writerGroupId));
    }
    if (args.dataSetWriterId != 0) {
      reader.dataSetWriterId(ushort(args.dataSetWriterId));
    }

    PubSubConfig config =
        PubSubConfig.builder()
            .connection(
                PubSubConnectionConfig.udp("conn")
                    .address(address)
                    .readerGroup(
                        ReaderGroupConfig.builder("group").dataSetReader(reader.build()).build())
                    .build())
            .build();

    PubSubService service = PubSubService.create(config);

    addDataSetLogging(service);
    addMetaDataLogging(service);
    addStateAndDiagnosticsLogging(service);
    closeOnShutdown(service);

    log("subscribing on %s", address);
    service.startup().get();
    log("service started; Ctrl-C to exit");

    while (true) {
      Thread.sleep(10_000);
      dumpDiagnostics(service);
    }
  }

  private static void dumpDiagnostics(PubSubService service) {
    Map<String, PubSubDiagnostics.ComponentDiagnostics> snapshot =
        new TreeMap<>(service.diagnostics().snapshot());

    log("---- diagnostics (%d components) ----", snapshot.size());
    snapshot.forEach(
        (path, d) ->
            log(
                "  %s nmSent=%d nmRecv=%d dsmSent=%d dsmRecv=%d decodeErrors=%d sourceErrors=%d"
                    + " lastError=%s",
                path,
                d.networkMessagesSent(),
                d.networkMessagesReceived(),
                d.dataSetMessagesSent(),
                d.dataSetMessagesReceived(),
                d.decodeErrors(),
                d.sourceErrors(),
                d.lastError() == null ? "-" : formatStatus(d.lastError())));
  }

  // endregion

  // region raw

  private static void runRaw(Args args) throws Exception {
    InetAddress inetAddress = InetAddress.getByName(args.address);
    boolean multicast = inetAddress.isMulticastAddress();

    Files.createDirectories(CAPTURE_DIR);

    var mapping = new UadpMessageMapping();
    DecodeContext decodeContext = DecodeContext.of(DefaultEncodingContext.INSTANCE);

    try (var socket = new MulticastSocket(null)) {
      socket.setReuseAddress(true);
      socket.bind(new InetSocketAddress(args.port));

      if (multicast) {
        NetworkInterface networkInterface =
            args.networkInterface != null ? findNetworkInterface(args.networkInterface) : null;
        socket.joinGroup(new InetSocketAddress(inetAddress, args.port), networkInterface);
        log(
            "joined multicast group %s on %s",
            args.address,
            networkInterface != null ? networkInterface.getName() : "the default interface");
      }

      log(
          "listening on port %d (%s %s); saving packets to %s; Ctrl-C to exit",
          args.port, multicast ? "multicast" : "unicast", args.address, CAPTURE_DIR);

      byte[] buffer = new byte[65535];
      int packetCount = 0;

      while (true) {
        var packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        packetCount++;

        byte[] data = Arrays.copyOfRange(buffer, 0, packet.getLength());

        log("packet #%d: %d bytes from %s", packetCount, data.length, packet.getSocketAddress());
        hexDump(data);
        decodeAndPrint(mapping, decodeContext, data);

        Path file =
            CAPTURE_DIR.resolve(
                String.format("packet-%03d-%d.bin", packetCount, System.currentTimeMillis()));
        Files.write(file, data);
        log("saved %s", file);
      }
    }
  }

  private static void hexDump(byte[] data) {
    var sb = new StringBuilder();
    for (int offset = 0; offset < data.length; offset += 16) {
      sb.setLength(0);
      sb.append(String.format("%06x  ", offset));
      for (int i = 0; i < 16; i++) {
        if (i == 8) {
          sb.append(' ');
        }
        if (offset + i < data.length) {
          sb.append(String.format("%02x ", data[offset + i]));
        } else {
          sb.append("   ");
        }
      }
      sb.append(" |");
      for (int i = 0; i < 16 && offset + i < data.length; i++) {
        int b = data[offset + i] & 0xFF;
        sb.append(b >= 0x20 && b < 0x7F ? (char) b : '.');
      }
      sb.append('|');
      System.out.println(sb);
    }
  }

  private static void decodeAndPrint(
      UadpMessageMapping mapping, DecodeContext decodeContext, byte[] data) {

    DecodedNetworkMessage decoded;
    try {
      decoded = mapping.decode(decodeContext, Unpooled.wrappedBuffer(data));
    } catch (Exception e) {
      log("UADP decode FAILED: %s", e);
      return;
    }

    log(
        "UADP decode OK: publisherId=%s writerGroupId=%s groupVersion=%s nmNumber=%s seq=%s"
            + " timestamp=%s messages=%d metaData=%d",
        decoded.publisherId() != null ? formatPublisherId(decoded.publisherId()) : "-",
        orDash(decoded.writerGroupId()),
        orDash(decoded.groupVersion()),
        orDash(decoded.networkMessageNumber()),
        orDash(decoded.sequenceNumber()),
        formatTime(decoded.timestamp()),
        decoded.messages().size(),
        decoded.metaData().size());

    for (DecodedDataSetMessage message : decoded.messages()) {
      log(
          "  DSM writerId=%s kind=%s valid=%s seq=%s timestamp=%s status=%s version=%s fields=%d",
          orDash(message.dataSetWriterId()),
          message.kind(),
          message.valid(),
          orDash(message.sequenceNumber()),
          formatTime(message.timestamp()),
          message.status() != null ? formatStatus(message.status()) : "-",
          message.configurationVersion() != null
              ? message.configurationVersion().getMajorVersion()
                  + "."
                  + message.configurationVersion().getMinorVersion()
              : "-",
          message.fields().size());

      for (DecodedField field : message.fields()) {
        DataValue value = field.value();
        log(
            "    [%d] %s status=%s sourceTime=%s",
            field.index(),
            value.value().value(),
            formatStatus(value.statusCode()),
            formatTime(value.sourceTime()));
      }
    }

    for (DecodedMetaData metaData : decoded.metaData()) {
      FieldMetaData[] fields = metaData.metaData().getFields();
      log(
          "  METADATA writerId=%s name=%s fields=%d",
          metaData.dataSetWriterId(),
          orDash(metaData.metaData().getName()),
          fields == null ? 0 : fields.length);
    }
  }

  // endregion

  // region publish

  private static void runPublish(Args args) throws Exception {
    UdpDatagramAddress address = datagramAddress(args);

    PublisherId publisherId =
        args.publisherId != null ? args.publisherId : PublisherId.uint16(ushort(62541));
    int writerGroupId = args.writerGroupId != 0 ? args.writerGroupId : 100;
    int dataSetWriterId = args.dataSetWriterId != 0 ? args.dataSetWriterId : 1;

    PublishedDataSetConfig dataSet = miloInteropDataSet();

    // Interop-friendly explicit masks: full group header plus payload header on the
    // NetworkMessage; version, sequence number, timestamp, and status on the DataSetMessage.
    UadpWriterGroupSettings groupSettings =
        UadpWriterGroupSettings.builder()
            .networkMessageContentMask(
                UadpNetworkMessageContentMask.of(
                    UadpNetworkMessageContentMask.Field.PublisherId,
                    UadpNetworkMessageContentMask.Field.GroupHeader,
                    UadpNetworkMessageContentMask.Field.WriterGroupId,
                    UadpNetworkMessageContentMask.Field.GroupVersion,
                    UadpNetworkMessageContentMask.Field.NetworkMessageNumber,
                    UadpNetworkMessageContentMask.Field.SequenceNumber,
                    UadpNetworkMessageContentMask.Field.PayloadHeader))
            .build();

    UadpDataSetWriterSettings writerSettings =
        UadpDataSetWriterSettings.builder()
            .dataSetMessageContentMask(
                UadpDataSetMessageContentMask.of(
                    UadpDataSetMessageContentMask.Field.MajorVersion,
                    UadpDataSetMessageContentMask.Field.MinorVersion,
                    UadpDataSetMessageContentMask.Field.SequenceNumber,
                    UadpDataSetMessageContentMask.Field.Timestamp,
                    UadpDataSetMessageContentMask.Field.Status))
            .build();

    DataSetFieldContentMask fieldContentMask =
        args.dataValue
            ? DataSetFieldContentMask.of(
                DataSetFieldContentMask.Field.StatusCode,
                DataSetFieldContentMask.Field.SourceTimestamp)
            : DataSetFieldContentMask.of();

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(dataSet)
            .connection(
                PubSubConnectionConfig.udp("conn")
                    .publisherId(publisherId)
                    .address(address)
                    .writerGroup(
                        WriterGroupConfig.builder("group")
                            .writerGroupId(ushort(writerGroupId))
                            .publishingInterval(Duration.ofMillis(args.intervalMs))
                            .messageSettings(groupSettings)
                            .dataSetWriter(
                                DataSetWriterConfig.builder("writer")
                                    .dataSet(dataSet.ref())
                                    .dataSetWriterId(ushort(dataSetWriterId))
                                    .fieldContentMask(fieldContentMask)
                                    .settings(writerSettings)
                                    .build())
                            .build())
                    .build())
            .build();

    var counter = new AtomicLong();
    boolean asDataValue = args.dataValue;

    PubSubService service =
        PubSubService.create(
            config,
            PubSubBindings.builder()
                .source(dataSet.ref(), miloInteropSource(counter, asDataValue))
                .build());

    addStateAndDiagnosticsLogging(service);
    closeOnShutdown(service);

    log(
        "publishing \"MiloInterop\" to %s: publisherId=%s writerGroupId=%d dataSetWriterId=%d"
            + " intervalMs=%d fieldEncoding=%s",
        address,
        formatPublisherId(publisherId),
        writerGroupId,
        dataSetWriterId,
        args.intervalMs,
        asDataValue ? "DataValue (StatusCode|SourceTimestamp)" : "Variant");
    service.startup().get();
    log("service started; Ctrl-C to exit");

    while (true) {
      Thread.sleep(5_000);
      long sent =
          service
              .diagnostics()
              .component("conn/group")
              .map(PubSubDiagnostics.ComponentDiagnostics::networkMessagesSent)
              .orElse(0L);
      log("publishing: counter=%d networkMessagesSent=%d", counter.get(), sent);
    }
  }

  private static DataValue publishValue(Variant variant, boolean asDataValue) {
    return asDataValue
        ? new DataValue(variant, StatusCode.GOOD, DateTime.now())
        : new DataValue(variant);
  }

  // endregion

  // region mqtt-publish

  private static void runMqttPublish(Args args) throws Exception {
    PublisherId publisherId =
        args.publisherId != null ? args.publisherId : PublisherId.uint16(ushort(62541));
    int writerGroupId = args.writerGroupId != 0 ? args.writerGroupId : 100;
    int dataSetWriterId = args.dataSetWriterId != 0 ? args.dataSetWriterId : 1;

    PublishedDataSetConfig dataSet = miloInteropDataSet();

    // Mapping-default content masks: JSON's empty masks resolve to the recommended ua-data
    // header/Verbose defaults, UADP's to the Annex A UADP-Dynamic profile.
    WriterGroupMessageSettings groupSettings =
        args.mapping.equals("uadp")
            ? UadpWriterGroupSettings.builder().build()
            : JsonWriterGroupSettings.builder().build();
    DataSetWriterMessageSettings writerSettings =
        args.mapping.equals("uadp")
            ? UadpDataSetWriterSettings.builder().build()
            : JsonDataSetWriterSettings.builder().build();

    // Unset queue names stay null so the engine derives the §7.3.4.7 standardized topic tree;
    // metaDataUpdateTime 0 means metadata is published at activation and on change only.
    BrokerTransportSettings.Builder brokerTransport = BrokerTransportSettings.builder();
    if (args.topic != null) {
      brokerTransport.queueName(args.topic);
    }
    if (args.metadataTopic != null) {
      brokerTransport.metaDataQueueName(args.metadataTopic);
    }
    if (args.metadataIntervalMs > 0) {
      brokerTransport.metaDataUpdateTime(Duration.ofMillis(args.metadataIntervalMs));
    }
    if (args.qos != -1) {
      brokerTransport.requestedDeliveryGuarantee(deliveryGuarantee(args.qos));
    }

    DataSetWriterConfig writer =
        DataSetWriterConfig.builder("writer")
            .dataSet(dataSet.ref())
            .dataSetWriterId(ushort(dataSetWriterId))
            .settings(writerSettings)
            .build();

    WriterGroupConfig group =
        WriterGroupConfig.builder(args.group)
            .writerGroupId(ushort(writerGroupId))
            .publishingInterval(Duration.ofMillis(args.intervalMs))
            .messageSettings(groupSettings)
            .brokerTransport(brokerTransport.build())
            .dataSetWriter(writer)
            .build();

    MqttConnectionConfig connection =
        mqttConnection(args).publisherId(publisherId).writerGroup(group).build();

    PubSubConfig config =
        PubSubConfig.builder().publishedDataSet(dataSet).connection(connection).build();

    var counter = new AtomicLong();

    PubSubService service =
        PubSubService.create(
            config,
            PubSubBindings.builder()
                .source(dataSet.ref(), miloInteropSource(counter, false))
                .build(),
            mqttServiceConfig());

    addStateAndDiagnosticsLogging(service);
    closeOnShutdown(service);

    String dataTopic =
        BrokerTopics.resolveDataQueueName(connection, args.mapping, publisherId, group);
    String metaDataTopic =
        BrokerTopics.resolveMetaDataQueueName(connection, args.mapping, group, writer);

    log(
        "publishing \"MiloInterop\" over MQTT/%s to %s: publisherId=%s writerGroupId=%d"
            + " dataSetWriterId=%d intervalMs=%d qos=%d mqttVersion=%s",
        args.mapping,
        args.brokerUri,
        formatPublisherId(publisherId),
        writerGroupId,
        dataSetWriterId,
        args.intervalMs,
        args.qos != -1 ? args.qos : 0,
        args.mqttVersion != null ? args.mqttVersion : "best");
    log("data topic: %s%s", dataTopic, args.topic == null ? " (derived)" : "");
    log(
        "metadata topic: %s%s, published retained %s",
        metaDataTopic,
        args.metadataTopic == null ? " (derived)" : "",
        args.metadataIntervalMs > 0
            ? "at activation/on change and every " + args.metadataIntervalMs + " ms"
            : "at activation/on change only");

    service.startup().get();
    log("service started; Ctrl-C to exit");

    while (true) {
      Thread.sleep(5_000);
      long sent =
          service
              .diagnostics()
              .component("conn/" + args.group)
              .map(PubSubDiagnostics.ComponentDiagnostics::networkMessagesSent)
              .orElse(0L);
      log("published %d cycles (networkMessagesSent=%d)", counter.get(), sent);
    }
  }

  // endregion

  // region mqtt-subscribe

  private static void runMqttSubscribe(Args args) throws Exception {
    if (args.topic == null) {
      throw usageError(
          "mqtt-subscribe requires --topic <dataTopic>: MQTT readers cannot derive the"
              + " publisher's topic names; pass the data topic, e.g."
              + " opcua/json/data/62541/MiloInterop");
    }

    DataSetReaderMessageSettings readerSettings =
        args.mapping.equals("uadp")
            ? UadpDataSetReaderSettings.builder().build()
            : JsonDataSetReaderSettings.builder().build();

    DataSetReaderConfig.Builder reader =
        DataSetReaderConfig.builder("reader").settings(readerSettings);

    if (args.fields.isEmpty()) {
      // No configured metadata: names arrive via the metadata topic (when subscribed) or the
      // engine falls back to generic "Field_<i>" names.
      reader.metadataPolicy(MetadataPolicy.ACCEPT_DISCOVERED);
    } else {
      // --field entries configure named decode; ConfigurationVersion defaults to 1.1, matching
      // the MiloInterop publish modes (REQUIRE_CONFIGURED drops on major-version mismatch).
      DataSetMetaDataConfig.Builder metaData = DataSetMetaDataConfig.builder("configured");
      args.fields.forEach(metaData::field);
      reader.dataSetMetaData(metaData.build()).metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED);
    }

    if (args.publisherId != null) {
      reader.publisherId(args.publisherId);
    }
    if (args.writerGroupId != 0) {
      reader.writerGroupId(ushort(args.writerGroupId));
    }
    if (args.dataSetWriterId != 0) {
      reader.dataSetWriterId(ushort(args.dataSetWriterId));
    }

    BrokerTransportSettings.Builder brokerTransport =
        BrokerTransportSettings.builder().queueName(args.topic);
    if (args.metadataTopic != null) {
      brokerTransport.metaDataQueueName(args.metadataTopic);
    }
    reader.brokerTransport(brokerTransport.build());

    MqttConnectionConfig connection =
        mqttConnection(args)
            .readerGroup(ReaderGroupConfig.builder("group").dataSetReader(reader.build()).build())
            .build();

    PubSubConfig config = PubSubConfig.builder().connection(connection).build();

    PubSubService service =
        PubSubService.create(config, PubSubBindings.builder().build(), mqttServiceConfig());

    addDataSetLogging(service);
    addMetaDataLogging(service);
    addStateAndDiagnosticsLogging(service);
    closeOnShutdown(service);

    log(
        "subscribing over MQTT/%s to %s: data topic=%s metadata topic=%s policy=%s",
        args.mapping,
        args.brokerUri,
        args.topic,
        orDash(args.metadataTopic),
        args.fields.isEmpty()
            ? "ACCEPT_DISCOVERED"
            : "REQUIRE_CONFIGURED (" + args.fields.size() + " configured fields)");
    service.startup().get();
    log("service started; Ctrl-C to exit");

    while (true) {
      Thread.sleep(10_000);
      dumpDiagnostics(service);
    }
  }

  // endregion

  // region mqtt-raw

  private static void runMqttRaw(Args args) throws Exception {
    URI brokerUri = parseBrokerUri(args.brokerUri);
    String host = brokerUri.getHost();
    if (host == null) {
      throw usageError("invalid <brokerUri>, expected mqtt://host[:port]: " + args.brokerUri);
    }
    boolean tls = "mqtts".equals(brokerUri.getScheme());
    int port = brokerUri.getPort() != -1 ? brokerUri.getPort() : (tls ? 8883 : 1883);
    String topicFilter = args.topic != null ? args.topic : "opcua/#";
    String clientId =
        args.clientId != null
            ? args.clientId
            : "milo-raw-" + UUID.randomUUID().toString().substring(0, 8);

    Mqtt5ClientBuilder clientBuilder =
        MqttClient.builder()
            .useMqttVersion5()
            .identifier(clientId)
            .serverHost(host)
            .serverPort(port)
            .addDisconnectedListener(context -> log("disconnected: %s", context.getCause()));
    if (tls) {
      clientBuilder.sslWithDefaultConfig();
    }
    Mqtt5SimpleAuth simpleAuth = simpleAuth(args);
    if (simpleAuth != null) {
      clientBuilder.simpleAuth(simpleAuth);
    }

    Mqtt5AsyncClient client = clientBuilder.buildAsync();

    log("connecting to %s:%d%s as %s", host, port, tls ? " (tls)" : "", clientId);
    client.connect().get(10, TimeUnit.SECONDS);
    log("connected");

    var messageCount = new AtomicLong();
    client
        .subscribeWith()
        .topicFilter(topicFilter)
        .qos(MqttQos.EXACTLY_ONCE)
        .callback(publish -> printMqttMessage(messageCount.incrementAndGet(), publish))
        .send()
        .get(10, TimeUnit.SECONDS);

    log("subscribed to %s; Ctrl-C to exit", topicFilter);

    while (true) {
      Thread.sleep(60_000);
    }
  }

  private static @Nullable Mqtt5SimpleAuth simpleAuth(Args args) {
    if (args.username != null && args.password != null) {
      return Mqtt5SimpleAuth.builder()
          .username(args.username)
          .password(args.password.getBytes(StandardCharsets.UTF_8))
          .build();
    } else if (args.username != null) {
      return Mqtt5SimpleAuth.builder().username(args.username).build();
    } else if (args.password != null) {
      return Mqtt5SimpleAuth.builder()
          .password(args.password.getBytes(StandardCharsets.UTF_8))
          .build();
    } else {
      return null;
    }
  }

  private static void printMqttMessage(long count, Mqtt5Publish publish) {
    byte[] payload = publish.getPayloadAsBytes();
    log(
        "message #%d: topic=%s retained=%s qos=%d %d bytes",
        count, publish.getTopic(), publish.isRetain(), publish.getQos().getCode(), payload.length);

    String json = tryPrettyJson(payload);
    if (json != null) {
      System.out.println(json);
    } else {
      hexDump(Arrays.copyOf(payload, Math.min(payload.length, 256)));
      if (payload.length > 256) {
        System.out.println("... (" + (payload.length - 256) + " more bytes)");
      }
    }
  }

  /** Pretty-printed JSON if the payload is a UTF-8 JSON document, else {@code null}. */
  private static @Nullable String tryPrettyJson(byte[] payload) {
    String text;
    try {
      text =
          StandardCharsets.UTF_8
              .newDecoder()
              .onMalformedInput(CodingErrorAction.REPORT)
              .onUnmappableCharacter(CodingErrorAction.REPORT)
              .decode(ByteBuffer.wrap(payload))
              .toString();
    } catch (CharacterCodingException e) {
      return null;
    }

    String trimmed = text.trim();
    if (!trimmed.startsWith("{") && !trimmed.startsWith("[")) {
      return null;
    }
    try {
      JsonElement element = JsonParser.parseString(trimmed);
      return PRETTY_GSON.toJson(element);
    } catch (JsonParseException e) {
      return null;
    }
  }

  // endregion

  // region shared fixtures and listeners

  /** The "MiloInterop" dataset with stable dataSetFieldIds so captures stay reproducible. */
  private static PublishedDataSetConfig miloInteropDataSet() {
    return PublishedDataSetConfig.builder("MiloInterop")
        .field(
            FieldDefinition.builder("Ramp")
                .dataType(NodeIds.Double)
                .dataSetFieldId(new UUID(0L, 1L))
                .build())
        .field(
            FieldDefinition.builder("Counter")
                .dataType(NodeIds.Int32)
                .dataSetFieldId(new UUID(0L, 2L))
                .build())
        .field(
            FieldDefinition.builder("Message")
                .dataType(NodeIds.String)
                .dataSetFieldId(new UUID(0L, 3L))
                .build())
        .field(
            FieldDefinition.builder("Toggle")
                .dataType(NodeIds.Boolean)
                .dataSetFieldId(new UUID(0L, 4L))
                .build())
        .build();
  }

  /** The "MiloInterop" value animation: Ramp sawtooth, Counter, Message string, Toggle. */
  private static PublishedDataSetSource miloInteropSource(AtomicLong counter, boolean asDataValue) {
    return context -> {
      long c = counter.getAndIncrement();
      return DataSetSnapshot.builder(context)
          .field("Ramp", publishValue(Variant.ofDouble((double) (c % 101)), asDataValue))
          .field("Counter", publishValue(Variant.ofInt32((int) c), asDataValue))
          .field("Message", publishValue(Variant.ofString("milo-" + c), asDataValue))
          .field("Toggle", publishValue(Variant.ofBoolean(c % 2 == 0), asDataValue))
          .build();
    };
  }

  private static void addDataSetLogging(PubSubService service) {
    // DataSetReceivedEvent does not expose wire sequence numbers, so keep a local receive count
    // per publisher/group/writer as the sequence proxy.
    var receiveCounts = new ConcurrentHashMap<String, AtomicLong>();

    service.addDataSetListener(
        event -> {
          String writerKey =
              formatPublisherId(event.publisherId())
                  + "/"
                  + event.writerGroupId()
                  + "/"
                  + event.dataSetWriterId();
          long rxCount =
              receiveCounts.computeIfAbsent(writerKey, k -> new AtomicLong()).incrementAndGet();

          log(
              "DATA publisherId=%s writerGroupId=%s dataSetWriterId=%s dataSet=%s fields=%d"
                  + " rxCount=%d",
              formatPublisherId(event.publisherId()),
              event.writerGroupId(),
              event.dataSetWriterId(),
              orDash(event.dataSetName()),
              event.fields().size(),
              rxCount);

          for (DataSetFieldValue field : event.fields()) {
            DataValue value = field.value();
            log(
                "  [%d] %s = %s status=%s sourceTime=%s",
                field.index(),
                field.name(),
                value.value().value(),
                formatStatus(value.statusCode()),
                formatTime(value.sourceTime()));
          }
        });
  }

  private static void addMetaDataLogging(PubSubService service) {
    service.addMetaDataListener(
        event -> {
          var names = new StringBuilder();
          FieldMetaData[] fields = event.metaData().getFields();
          if (fields != null) {
            for (FieldMetaData field : fields) {
              if (names.length() > 0) {
                names.append(", ");
              }
              names.append(field.getName());
            }
          }
          log(
              "META reader=%s dataSet=%s version=%s.%s fields=[%s]",
              event.reader().path(),
              orDash(event.dataSetName()),
              event.configurationVersion().getMajorVersion(),
              event.configurationVersion().getMinorVersion(),
              names);
        });
  }

  private static void addStateAndDiagnosticsLogging(PubSubService service) {
    service.addStateListener(
        event ->
            log(
                "STATE %s %s: %s -> %s (%s)",
                event.component().componentType(),
                event.component().path(),
                event.oldState(),
                event.newState(),
                formatStatus(event.statusCode())));

    service.addDiagnosticsListener(
        event ->
            log(
                "DIAG %s status=%s message=%s%s",
                event.path(),
                formatStatus(event.statusCode()),
                event.message(),
                event.error() != null ? " error=" + event.error() : ""));
  }

  private static void closeOnShutdown(PubSubService service) {
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  log("shutting down...");
                  service.close();
                }));
  }

  /** A {@link PubSubServiceConfig} with a fresh {@link MqttTransportProvider} registered. */
  private static PubSubServiceConfig mqttServiceConfig() {
    return PubSubServiceConfig.builder().transportProvider(MqttTransportProvider.create()).build();
  }

  /** A connection builder for the broker URI plus client id, version, and credential options. */
  private static MqttConnectionConfig.Builder mqttConnection(Args args) {
    MqttConnectionConfig.Builder connection =
        PubSubConnectionConfig.mqtt("conn").brokerUri(parseBrokerUri(args.brokerUri));

    if (args.clientId != null) {
      connection.property(
          MqttTransportProvider.MQTT_CLIENT_ID_PROPERTY, Variant.ofString(args.clientId));
    }
    if (args.mqttVersion != null) {
      // "best" leaves the property unset (the provider default is BestAvailable)
      connection.property(
          MqttTransportProvider.MQTT_VERSION_PROPERTY, Variant.ofString(args.mqttVersion));
    }
    if (args.username != null || args.password != null) {
      BrokerSecurityConfig.Builder security = BrokerSecurityConfig.builder();
      if (args.username != null) {
        security.username(args.username);
      }
      if (args.password != null) {
        security.password(args.password.toCharArray());
      }
      connection.brokerSecurity(security.build());
    }

    return connection;
  }

  private static URI parseBrokerUri(String value) {
    try {
      return URI.create(value);
    } catch (IllegalArgumentException e) {
      throw usageError("invalid <brokerUri>: " + value);
    }
  }

  // endregion

  // region argument parsing

  private static Args parseArgs(String[] args, Set<String> allowedOptions) {
    if (args.length < 3) {
      throw usageError("missing <address> <port>");
    }

    var parsed = new Args();
    parsed.address = args[1];
    parsed.port = parseInt("<port>", args[2]);
    if (parsed.port < 1 || parsed.port > 65535) {
      throw usageError("port must be in the range [1, 65535]: " + parsed.port);
    }

    int i = 3;
    while (i < args.length) {
      String option = args[i];
      if (!allowedOptions.contains(option)) {
        throw usageError("unknown or inapplicable option for mode '" + args[0] + "': " + option);
      }
      if (option.equals("--datavalue")) {
        parsed.dataValue = true;
        i += 1;
        continue;
      }
      if (i + 1 >= args.length) {
        throw usageError("missing value for " + option);
      }
      String value = args[i + 1];
      switch (option) {
        case "--interface" -> parsed.networkInterface = value;
        case "--publisher-id" -> parsed.publisherId = parsePublisherId(value);
        case "--writer-group-id" -> parsed.writerGroupId = parseInt(option, value);
        case "--data-set-writer-id" -> parsed.dataSetWriterId = parseInt(option, value);
        case "--interval-ms" -> parsed.intervalMs = parseInt(option, value);
        default -> throw usageError("unknown option: " + option);
      }
      i += 2;
    }

    return parsed;
  }

  private static Args parseMqttArgs(String[] args, Set<String> allowedOptions) {
    if (args.length < 2) {
      throw usageError("missing <brokerUri>");
    }

    var parsed = new Args();
    parsed.brokerUri = args[1];
    parsed.intervalMs = 1000;

    int i = 2;
    while (i < args.length) {
      String option = args[i];
      if (!allowedOptions.contains(option)) {
        throw usageError("unknown or inapplicable option for mode '" + args[0] + "': " + option);
      }
      if (i + 1 >= args.length) {
        throw usageError("missing value for " + option);
      }
      String value = args[i + 1];
      switch (option) {
        case "--mapping" -> parsed.mapping = parseMapping(value);
        case "--publisher-id" -> parsed.publisherId = parsePublisherId(value);
        case "--group" -> parsed.group = value;
        case "--topic" -> parsed.topic = value;
        case "--metadata-topic" -> parsed.metadataTopic = value;
        case "--metadata-interval" -> parsed.metadataIntervalMs = parseInt(option, value);
        case "--qos" -> parsed.qos = parseQos(value);
        case "--interval" -> parsed.intervalMs = parseInt(option, value);
        case "--field" -> parsed.fields.add(parseField(value));
        case "--writer-group-id" -> parsed.writerGroupId = parseInt(option, value);
        case "--data-set-writer-id" -> parsed.dataSetWriterId = parseInt(option, value);
        case "--username" -> parsed.username = value;
        case "--password" -> parsed.password = value;
        case "--client-id" -> parsed.clientId = value;
        case "--mqtt-version" -> parsed.mqttVersion = parseMqttVersion(value);
        default -> throw usageError("unknown option: " + option);
      }
      i += 2;
    }

    return parsed;
  }

  private static PublisherId parsePublisherId(String spec) {
    int separator = spec.indexOf(':');
    if (separator <= 0 || separator == spec.length() - 1) {
      throw usageError("invalid --publisher-id, expected <type:value>: " + spec);
    }

    String type = spec.substring(0, separator).toLowerCase(Locale.ROOT);
    String value = spec.substring(separator + 1);

    try {
      return switch (type) {
        case "byte", "uint8" -> PublisherId.ubyte(ubyte(value));
        case "uint16" -> PublisherId.uint16(ushort(value));
        case "uint32" -> PublisherId.uint32(uint(value));
        case "uint64" -> PublisherId.uint64(ulong(value));
        case "string" -> PublisherId.string(value);
        default -> throw usageError("invalid --publisher-id type: " + type);
      };
    } catch (NumberFormatException e) {
      throw usageError("invalid --publisher-id value: " + spec);
    }
  }

  private static String parseMapping(String value) {
    return switch (value) {
      case "json", "uadp" -> value;
      default -> throw usageError("invalid --mapping, expected json|uadp: " + value);
    };
  }

  private static int parseQos(String value) {
    return switch (value) {
      case "0" -> 0;
      case "1" -> 1;
      case "2" -> 2;
      default -> throw usageError("invalid --qos, expected 0|1|2: " + value);
    };
  }

  private static BrokerTransportQualityOfService deliveryGuarantee(int qos) {
    return switch (qos) {
      case 0 -> BrokerTransportQualityOfService.AtMostOnce;
      case 1 -> BrokerTransportQualityOfService.AtLeastOnce;
      case 2 -> BrokerTransportQualityOfService.ExactlyOnce;
      default -> throw new IllegalArgumentException("qos: " + qos);
    };
  }

  private static @Nullable String parseMqttVersion(String value) {
    return switch (value) {
      case "5.0" -> MqttTransportProvider.MQTT_VERSION_5_0;
      case "3.1.1" -> MqttTransportProvider.MQTT_VERSION_3_1_1;
      case "best" -> null; // omit the property; the provider default is BestAvailable
      default -> throw usageError("invalid --mqtt-version, expected 5.0|3.1.1|best: " + value);
    };
  }

  private static DataSetMetaDataConfig.Field parseField(String spec) {
    int separator = spec.lastIndexOf(':');
    if (separator <= 0 || separator == spec.length() - 1) {
      throw usageError("invalid --field, expected <name>:<type>: " + spec);
    }

    String name = spec.substring(0, separator);
    String type = spec.substring(separator + 1).toLowerCase(Locale.ROOT);

    NodeId dataTypeId =
        switch (type) {
          case "bool", "boolean" -> NodeIds.Boolean;
          case "sbyte" -> NodeIds.SByte;
          case "byte" -> NodeIds.Byte;
          case "int16" -> NodeIds.Int16;
          case "uint16" -> NodeIds.UInt16;
          case "int32" -> NodeIds.Int32;
          case "uint32" -> NodeIds.UInt32;
          case "int64" -> NodeIds.Int64;
          case "uint64" -> NodeIds.UInt64;
          case "float" -> NodeIds.Float;
          case "double" -> NodeIds.Double;
          case "string" -> NodeIds.String;
          case "datetime" -> NodeIds.DateTime;
          default ->
              throw usageError(
                  "invalid --field type, expected bool|sbyte|byte|int16|uint16|int32|uint32|"
                      + "int64|uint64|float|double|string|datetime: "
                      + type);
        };

    return new DataSetMetaDataConfig.Field(name, dataTypeId, UUID.randomUUID(), -1, null);
  }

  private static int parseInt(String name, String value) {
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      throw usageError("invalid value for " + name + ": " + value);
    }
  }

  private static RuntimeException usageError(String message) {
    if (message != null) {
      System.out.println("error: " + message);
      System.out.println();
    }
    System.out.println(USAGE);
    System.exit(2);
    return new IllegalStateException("unreachable");
  }

  /** Parsed command line arguments; 0 ids and null references mean "not specified". */
  private static final class Args {
    String address;
    int port;
    String networkInterface;
    PublisherId publisherId;
    int writerGroupId;
    int dataSetWriterId;
    int intervalMs = 100;
    boolean dataValue;

    // MQTT modes
    String brokerUri;
    String mapping = "json";
    String group = "MiloInterop";
    String topic;
    String metadataTopic;
    int metadataIntervalMs = 0;
    int qos = -1; // -1 = not specified: engine defaults (data QoS 0, metadata QoS 1)
    final List<DataSetMetaDataConfig.Field> fields = new ArrayList<>();
    String username;
    String password;
    String clientId;
    String mqttVersion; // null = best available (property omitted)
  }

  // endregion

  // region helpers

  private static UdpDatagramAddress datagramAddress(Args args) throws UnknownHostException {
    boolean multicast = InetAddress.getByName(args.address).isMulticastAddress();

    UdpDatagramAddress address =
        multicast
            ? UdpDatagramAddress.multicast(args.address, args.port)
            : UdpDatagramAddress.unicast(args.address, args.port);

    if (args.networkInterface != null) {
      address = address.networkInterface(args.networkInterface);
    }

    return address;
  }

  private static NetworkInterface findNetworkInterface(String nameOrAddress) throws Exception {
    NetworkInterface byName = NetworkInterface.getByName(nameOrAddress);
    if (byName != null) {
      return byName;
    }
    NetworkInterface byAddress =
        NetworkInterface.getByInetAddress(InetAddress.getByName(nameOrAddress));
    if (byAddress != null) {
      return byAddress;
    }
    throw usageError("network interface not found: " + nameOrAddress);
  }

  private static String formatPublisherId(PublisherId publisherId) {
    if (publisherId instanceof PublisherId.ByteId id) {
      return "byte:" + id.value();
    } else if (publisherId instanceof PublisherId.UInt16Id id) {
      return "uint16:" + id.value();
    } else if (publisherId instanceof PublisherId.UInt32Id id) {
      return "uint32:" + id.value();
    } else if (publisherId instanceof PublisherId.UInt64Id id) {
      return "uint64:" + id.value();
    } else if (publisherId instanceof PublisherId.StringId id) {
      return "string:" + id.value();
    } else {
      return String.valueOf(publisherId);
    }
  }

  private static String formatStatus(StatusCode statusCode) {
    return String.format("0x%08X", statusCode.value());
  }

  private static String formatTime(DateTime time) {
    return time == null ? "-" : time.getJavaInstant().toString();
  }

  private static Object orDash(Object value) {
    return value == null ? "-" : value;
  }

  private static void log(String format, Object... args) {
    System.out.println(LocalTime.now().format(TIME_FORMAT) + " | " + String.format(format, args));
  }

  // endregion
}
