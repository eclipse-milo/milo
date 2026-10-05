/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.wiki.snippets;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.sdk.client.DiscoveryClient;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.ManagedNamespaceWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigBuilder;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigLimits;
import org.eclipse.milo.opcua.sdk.server.RoleMapper;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.conditions.ExclusiveLimitAlarm;
import org.eclipse.milo.opcua.sdk.server.identity.Identity;
import org.eclipse.milo.opcua.sdk.server.identity.IdentityValidator;
import org.eclipse.milo.opcua.sdk.server.identity.UsernameIdentityValidator;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilter;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilterContext;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.InstantiationRequest;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.InstantiationResult;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.AbstractCertificateFactory;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateGroup;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.DefaultServerCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.FileBasedCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.FileBasedTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.KeyStoreCertificateStore;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Server samples from the Wiki's server guide pages. */
public final class ServerSnippets {

  private static final Logger logger = LoggerFactory.getLogger(ServerSnippets.class);

  private ServerSnippets() {}

  static void validateSetpointWrites(UaVariableNode setpoint) {
    // snippet:data-filter:start
    setpoint
        .getFilterChain()
        .addLast(
            new AttributeFilter() {
              @Override
              public void writeAttribute(
                  AttributeFilterContext ctx, AttributeId attributeId, Object value)
                  throws UaException {
                if (attributeId == AttributeId.Value) {
                  DataValue dataValue = (DataValue) value;
                  Object requested = dataValue.value().value();
                  if (!(requested instanceof Double newSetpoint)) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch);
                  }
                  if (!Double.isFinite(newSetpoint) || newSetpoint < 0.0 || newSetpoint > 100.0) {
                    throw new UaException(StatusCodes.Bad_OutOfRange);
                  }
                }

                ctx.writeAttribute(attributeId, value);
              }
            });
    // snippet:data-filter:end
  }

  // snippet:event:start
  static void fireSetpointChanged(OpcUaServer server, UaObjectNode thermostat, double setpoint)
      throws UaException {

    UShort namespaceIndex = thermostat.getNodeId().getNamespaceIndex();
    NodeId eventNodeId = new NodeId(namespaceIndex, UUID.randomUUID());

    BaseEventTypeNode event =
        server.getEventInstantiator().createEvent(eventNodeId, NodeIds.BaseEventType);
    try {
      byte[] eventId = UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8);
      DateTime now = DateTime.now();

      event.setEventId(ByteString.of(eventId));
      event.setSourceNode(thermostat.getNodeId());
      event.setSourceName(thermostat.getDisplayName().text());
      event.setTime(now);
      event.setReceiveTime(now);
      event.setMessage(LocalizedText.english("Setpoint changed to " + setpoint));
      event.setSeverity(ushort(100));

      server.getEventNotifier().fire(event);
    } finally {
      event.delete();
    }
  }

  // snippet:event:end

  /**
   * Stands in for the tutorial's namespace, so the event and alarm samples can call the protected
   * namespace methods, such as {@code registerEventNotifier}, the way code in that namespace does.
   */
  static final class ThermostatNamespace extends ManagedNamespaceWithLifecycle {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    ThermostatNamespace(OpcUaServer server) {
      super(server, "urn:eclipse:milo:wiki");
    }

    void publishSetpointEvents(UaObjectNode thermostat, UaVariableNode setpoint) {
      // snippet:setpoint-events:start
      registerEventNotifier(thermostat);

      setpoint.addAttributeObserver(
          (node, attributeId, value) -> {
            if (attributeId != AttributeId.Value) {
              return;
            }

            DataValue dataValue = (DataValue) value;
            if (!(dataValue.value().value() instanceof Double newSetpoint)) {
              return;
            }

            try {
              fireSetpointChanged(getServer(), thermostat, newSetpoint);
            } catch (UaException e) {
              logger.warn("Failed to fire setpoint changed event", e);
            }
          });
      // snippet:setpoint-events:end
    }

    ExclusiveLimitAlarm addHighTemperatureAlarm(UaObjectNode thermostat, UaVariableNode temperature)
        throws UaException {
      // snippet:alarm:start
      ExclusiveLimitAlarm alarm =
          ExclusiveLimitAlarm.create(
              getNodeContext(),
              builder ->
                  builder
                      .nodeId(newNodeId("HighTemperatureAlarm"))
                      .browseName(newQualifiedName("HighTemperatureAlarm"))
                      .conditionSource(thermostat)
                      .inputNode(temperature.getNodeId())
                      .highLimit(80.0));
      getServer().getConditionManager().register(alarm);

      DataValue currentValue = temperature.getValue();
      if (currentValue.value().value() instanceof Double currentTemperature) {
        alarm.evaluate(currentTemperature);
      }
      // snippet:alarm:end
      return alarm;
    }
  }

  // snippet:alarm-reading:start
  static void updateTemperature(
      UaVariableNode temperature, ExclusiveLimitAlarm alarm, double reading) {

    DataValue value = new DataValue(new Variant(reading));
    temperature.setValue(value);

    alarm.evaluate(reading);
  }

  // snippet:alarm-reading:end

  static List<EndpointDescription> discoverEndpoints(String discoveryUrl) throws Exception {
    // snippet:discovery:start
    List<EndpointDescription> endpoints =
        DiscoveryClient.getEndpoints(discoveryUrl).get(10, TimeUnit.SECONDS);
    // snippet:discovery:end
    return endpoints;
  }

  /**
   * The security resources the thermostat creates. The application closes the trust-list manager
   * and the certificate store after the server shuts down.
   */
  record ThermostatSecurity(
      DefaultCertificateManager certificateManager,
      FileBasedTrustListManager trustListManager,
      KeyStoreCertificateStore certificateStore) {}

  static ThermostatSecurity createThermostatSecurity(
      Path securityDir, Supplier<char[]> keyStorePassword, String applicationUri, String hostname)
      throws Exception {

    // snippet:server-identity:start
    var certificateStore =
        KeyStoreCertificateStore.createAndInitialize(
            new KeyStoreCertificateStore.Settings(
                securityDir.resolve("thermostat.pfx"),
                keyStorePassword,
                alias -> keyStorePassword.get()));

    var certificateFactory =
        new AbstractCertificateFactory() {
          @Override
          protected X509Certificate[] createRsaSha256CertificateChain(KeyPair keyPair)
              throws Exception {
            X509Certificate certificate =
                SelfSignedCertificateBuilder.forRsaApplicationCertificate(keyPair)
                    .setCommonName("Thermostat")
                    .setApplicationUri(applicationUri)
                    .addDnsName(hostname)
                    .build();

            return new X509Certificate[] {certificate};
          }
        };
    // snippet:server-identity:end

    // snippet:trust:start
    Path pkiDir = securityDir.resolve("pki");
    var trustListManager = FileBasedTrustListManager.createAndInitialize(pkiDir);

    var quarantine =
        FileBasedCertificateQuarantine.create(pkiDir.resolve("rejected").resolve("certs"));

    var validator = new DefaultServerCertificateValidator(trustListManager, quarantine);
    // snippet:trust:end

    // snippet:certificate-group:start
    var group =
        new DefaultCertificateGroup(trustListManager, certificateStore, quarantine, validator);
    certificateFactory.createMissingCertificates(group);

    var certificateManager = new DefaultCertificateManager(group);
    // snippet:certificate-group:end

    return new ThermostatSecurity(certificateManager, trustListManager, certificateStore);
  }

  static void configureSecureServer(
      OpcUaServerConfigBuilder builder,
      String hostname,
      String applicationUri,
      CertificateManager certificateManager,
      BiPredicate<String, String> checkPassword) {

    // snippet:secure-endpoint:start
    EndpointConfig endpoint =
        EndpointConfig.newBuilder()
            .setBindAddress("0.0.0.0")
            .setHostname(hostname)
            .setBindPort(12686)
            .setPath("/wiki")
            .setSecurityPolicy(SecurityPolicy.Basic256Sha256)
            .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
            .addTokenPolicy(OpcUaServerConfig.USER_TOKEN_POLICY_USERNAME)
            .build();
    // snippet:secure-endpoint:end

    // snippet:user-authentication:start
    var identityValidator =
        new UsernameIdentityValidator(
            challenge -> checkPassword.test(challenge.getUsername(), challenge.getPassword()));

    builder
        .setApplicationUri(applicationUri)
        .setCertificateManager(certificateManager)
        .setEndpoints(Set.of(endpoint))
        .setIdentityValidator(identityValidator);
    // snippet:user-authentication:end
  }

  static InstantiationResult<UaObjectNode> addDevicesFolder(
      OpcUaServer server, UaNodeManager nodeManager, NodeId folderId, QualifiedName folderName)
      throws UaException {
    // snippet:instantiate:start
    InstantiationRequest<UaObjectNode> request =
        InstantiationRequest.of(UaObjectNode.class, NodeIds.FolderType)
            .nodeId(folderId)
            .browseName(folderName)
            .displayName(LocalizedText.english("Devices"))
            .parent(NodeIds.ObjectsFolder, NodeIds.Organizes)
            .target(nodeManager)
            .build();
    InstantiationResult<UaObjectNode> result = server.getNodeInstantiator().instantiate(request);
    // snippet:instantiate:end
    return result;
  }

  static void configureRoles(OpcUaServerConfigBuilder builder, Set<String> operators) {
    // snippet:role-mapper:start
    RoleMapper roleMapper =
        identity -> {
          if (identity instanceof Identity.UsernameIdentity user) {
            if (operators.contains(user.getUsername())) {
              return List.of(NodeIds.WellKnownRole_Operator);
            }
            return List.of(NodeIds.WellKnownRole_AuthenticatedUser);
          }

          // Anonymous and any other identity get the least-privileged role.
          return List.of(NodeIds.WellKnownRole_Anonymous);
        };

    builder.setRoleMapper(roleMapper);
    // snippet:role-mapper:end
  }

  // snippet:operator-filter:start
  static final class OperatorOnlyFilter implements AttributeFilter {

    @Override
    public @Nullable Object getAttribute(AttributeFilterContext ctx, AttributeId attributeId) {
      Object value = ctx.getAttribute(attributeId);

      if (attributeId == AttributeId.UserAccessLevel && value instanceof UByte stored) {
        if (isOperatorOrInternal(ctx)) {
          return stored;
        }
        EnumSet<AccessLevel> levels = AccessLevel.fromValue(stored);
        levels.remove(AccessLevel.CurrentWrite);
        return AccessLevel.toValue(levels);
      }

      if (attributeId == AttributeId.UserExecutable && !isOperatorOrInternal(ctx)) {
        return false;
      }

      return value;
    }

    private static boolean isOperatorOrInternal(AttributeFilterContext ctx) {
      Optional<Session> session = ctx.getSession();
      if (session.isEmpty()) {
        return true;
      }

      List<NodeId> roleIds = session.get().getRoleIds().orElse(List.of());
      return roleIds.contains(NodeIds.WellKnownRole_Operator);
    }
  }

  // snippet:operator-filter:end

  static void restrictSetpointChanges(UaVariableNode setpoint, UaMethodNode adjustSetpoint) {
    // snippet:operator-filter-install:start
    var operatorOnly = new OperatorOnlyFilter();
    setpoint.getFilterChain().addLast(operatorOnly);
    adjustSetpoint.getFilterChain().addLast(operatorOnly);
    // snippet:operator-filter-install:end
  }

  static void revokeReadAccess(OpcUaServer server, UaVariableNode setpoint) {
    // snippet:access-revoke:start
    setpoint.setUserAccessLevel(AccessLevel.toValue(AccessLevel.NONE));
    server.getAccessControlManager().invalidateReadAccess(setpoint.getNodeId());
    // snippet:access-revoke:end
  }

  static void configureServer(
      OpcUaServerConfigBuilder builder,
      int port,
      X509Certificate certificate,
      String applicationUri,
      CertificateManager certificateManager,
      IdentityValidator usernameValidator) {
    // snippet:server-config:start
    EndpointConfig endpoint =
        EndpointConfig.newBuilder()
            .setBindAddress("127.0.0.1")
            .setHostname("localhost")
            .setBindPort(port)
            .setPath("/wiki")
            .setCertificate(certificate)
            .setSecurityPolicy(SecurityPolicy.Basic256Sha256)
            .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
            .addTokenPolicy(OpcUaServerConfig.USER_TOKEN_POLICY_USERNAME)
            .build();
    builder
        .setApplicationUri(applicationUri)
        .setCertificateManager(certificateManager)
        .setEndpoints(Set.of(endpoint))
        .setIdentityValidator(usernameValidator)
        .setLimits(
            new OpcUaServerConfigLimits() {
              @Override
              public UInteger getMaxNodesPerRead() {
                return uint(1_000);
              }
            });
    // snippet:server-config:end
  }

  static void runWithNamespace(OpcUaServer server, ManagedNamespaceWithLifecycle namespace)
      throws Exception {
    // snippet:lifecycle:start
    server.addLifecycleParticipant(namespace);
    try {
      server.startup().get(10, TimeUnit.SECONDS);
      if (server.getBoundEndpoints().isEmpty()) {
        throw new IllegalStateException("server has no bound endpoint");
      }
      // Application work runs here while the server owns the namespace lifecycle.
    } finally {
      server.shutdown().get(10, TimeUnit.SECONDS);
    }
    // snippet:lifecycle:end
  }

  // snippet:deployment-endpoints:start
  static Set<EndpointConfig> deploymentEndpoints(List<String> advertisedHostnames, int port) {
    var endpoints = new LinkedHashSet<EndpointConfig>();

    for (String hostname : advertisedHostnames) {
      EndpointConfig endpoint =
          EndpointConfig.newBuilder()
              .setBindAddress("0.0.0.0")
              .setHostname(hostname)
              .setBindPort(port)
              .setPath("/wiki")
              .setSecurityPolicy(SecurityPolicy.Basic256Sha256)
              .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
              .addTokenPolicy(OpcUaServerConfig.USER_TOKEN_POLICY_USERNAME)
              .build();
      endpoints.add(endpoint);
    }

    return endpoints;
  }

  // snippet:deployment-endpoints:end

  // snippet:service-main:start
  static void runUntilStopped(OpcUaServer server) throws Exception {
    var stopped = new CountDownLatch(1);
    Runnable stopOnExit =
        () -> {
          stopServer(server);
          stopped.countDown();
        };
    Runtime.getRuntime().addShutdownHook(new Thread(stopOnExit, "opcua-server-shutdown"));

    server.startup().get(10, TimeUnit.SECONDS);

    Set<EndpointConfig> configured = server.getConfig().getEndpoints();
    List<EndpointConfig> bound = server.getBoundEndpoints();
    if (!bound.containsAll(configured)) {
      throw new IllegalStateException("Some configured endpoints did not bind: " + bound);
    }
    logger.info("Server ready on {}", bound);

    stopped.await();
  }

  static void stopServer(OpcUaServer server) {
    try {
      server.shutdown().get(10, TimeUnit.SECONDS);
    } catch (Exception e) {
      logger.warn("Server shutdown did not complete", e);
    } finally {
      Stack.releaseSharedResources();
    }
  }
  // snippet:service-main:end
}
