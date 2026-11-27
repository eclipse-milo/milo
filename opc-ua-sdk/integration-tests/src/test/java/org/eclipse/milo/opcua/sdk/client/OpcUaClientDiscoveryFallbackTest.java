/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.UnresolvedAddressException;
import java.time.Duration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.ServiceSets;
import org.eclipse.milo.opcua.sdk.server.servicesets.DiscoveryServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultDiscoveryServiceSet;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaServiceFaultException;
import org.eclipse.milo.opcua.stack.core.channel.ExceptionHandler;
import org.eclipse.milo.opcua.stack.core.channel.messages.HelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.MessageType;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageDecoder;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.FindServersOnNetworkRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.FindServersOnNetworkResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.FindServersRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.FindServersResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.GetEndpointsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.GetEndpointsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterServer2Request;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterServer2Response;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterServerRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterServerResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OpcUaClientDiscoveryFallbackTest {

  private static final UserTokenPolicy USERNAME_POLICY =
      new UserTokenPolicy(
          "username", UserTokenType.UserName, null, null, SecurityPolicy.None.getUri());

  // When GetEndpoints succeeds but no endpoint matches the default selector, OpcUaClient.create
  // must report Bad_ConfigurationError directly without retrying /discovery.
  @Test
  void noMatchingEndpointReportsConfigurationErrorWithoutRetryingDiscovery() throws Exception {
    int port = freePort();
    var helloUrls = new CopyOnWriteArrayList<String>();
    EndpointConfig usernameOnly = endpointConfig(port, "/app", USERNAME_POLICY);

    OpcUaServer server =
        startServer(Set.of(usernameOnly), helloUrls, url -> false, new ServiceSets() {});
    try {
      String url = "opc.tcp://127.0.0.1:" + port + "/app";

      UaException exception = assertThrows(UaException.class, () -> OpcUaClient.create(url));

      assertEquals(StatusCodes.Bad_ConfigurationError, exception.getStatusCode().value());
      assertTrue(exception.getMessage().contains("no endpoint selected"));
      assertEquals(0, exception.getSuppressed().length);
      assertEquals(List.of(url), helloUrls);
    } finally {
      server.shutdown().get(10, TimeUnit.SECONDS);
    }
  }

  // Exceptions thrown by a custom selectEndpoint callback happen after discovery succeeded and
  // must propagate directly without retrying /discovery.
  @Test
  void selectEndpointThrowsPropagatesWithoutRetryingDiscovery() throws Exception {
    int port = freePort();
    var helloUrls = new CopyOnWriteArrayList<String>();
    EndpointConfig base =
        endpointConfig(port, "/app", OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS);
    var expected = new IllegalStateException("custom selection failure");

    OpcUaServer server = startServer(Set.of(base), helloUrls, url -> false, new ServiceSets() {});
    try {
      String url = "opc.tcp://127.0.0.1:" + port + "/app";

      IllegalStateException actual =
          assertThrows(
              IllegalStateException.class,
              () ->
                  OpcUaClient.create(
                      url,
                      endpoints -> {
                        throw expected;
                      },
                      b -> {},
                      c -> {}));

      assertSame(expected, actual);
      assertEquals(List.of(url), helloUrls);
    } finally {
      server.shutdown().get(10, TimeUnit.SECONDS);
    }
  }

  // A blocking selectEndpoint callback must remain bounded by discoveryTimeout and fail with
  // Bad_Timeout without retrying /discovery.
  @Test
  void blockingSelectEndpointTimesOutWithoutRetryingDiscovery() throws Exception {
    int port = freePort();
    var helloUrls = new CopyOnWriteArrayList<String>();
    var selectorEntered = new CountDownLatch(1);
    var releaseSelector = new CountDownLatch(1);
    EndpointConfig base =
        endpointConfig(port, "/app", OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS);
    EndpointConfig discovery =
        endpointConfig(port, "/app/discovery", OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS);

    OpcUaServer server =
        startServer(Set.of(base, discovery), helloUrls, url -> false, new ServiceSets() {});
    try {
      String url = "opc.tcp://127.0.0.1:" + port + "/app";

      UaException exception =
          assertThrows(
              UaException.class,
              () ->
                  OpcUaClient.create(
                      url,
                      endpoints -> {
                        selectorEntered.countDown();
                        try {
                          releaseSelector.await(10, TimeUnit.SECONDS);
                        } catch (InterruptedException e) {
                          Thread.currentThread().interrupt();
                        }
                        return endpoints.stream().findFirst();
                      },
                      b -> {},
                      c -> {},
                      Duration.ofMillis(250)));

      assertTrue(selectorEntered.await(5, TimeUnit.SECONDS));
      assertEquals(StatusCodes.Bad_Timeout, exception.getStatusCode().value());
      assertInstanceOf(TimeoutException.class, exception.getCause());
      assertEquals(0, exception.getSuppressed().length);
      assertEquals(List.of(url), helloUrls);
    } finally {
      releaseSelector.countDown();
      server.shutdown().get(10, TimeUnit.SECONDS);
    }
  }

  // A non-security GetEndpoints ServiceFault (such as Bad_ServiceUnsupported) came from the
  // discovery service itself and must be thrown directly without retrying /discovery.
  @Test
  void serverRejectsGetEndpointsReportsActualFaultWithoutRetryingDiscovery() throws Exception {
    int port = freePort();
    var helloUrls = new CopyOnWriteArrayList<String>();
    EndpointConfig base =
        endpointConfig(port, "/app", OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS);
    ServiceSets rejecting =
        serviceSetsWithGetEndpoints(
            (delegate, ctx, req) -> {
              throw new UaException(StatusCodes.Bad_ServiceUnsupported, "GetEndpoints disabled");
            });

    OpcUaServer server = startServer(Set.of(base), helloUrls, url -> false, rejecting);
    try {
      String url = "opc.tcp://127.0.0.1:" + port + "/app";

      UaServiceFaultException fault =
          assertThrows(UaServiceFaultException.class, () -> OpcUaClient.create(url));

      assertEquals(StatusCodes.Bad_ServiceUnsupported, fault.getStatusCode().value());
      assertEquals(0, fault.getSuppressed().length);
      assertEquals(List.of(url), helloUrls);
    } finally {
      server.shutdown().get(10, TimeUnit.SECONDS);
    }
  }

  // When a wrong path triggers the /discovery fallback and the fallback also fails, the original
  // Bad_TcpEndpointUrlInvalid naming the caller's URL must be thrown with the fallback failure
  // attached as a suppressed exception.
  @Test
  void wrongPathWithoutDiscoveryFallbackThrowsOriginalErrorWithSuppressedFallback()
      throws Exception {
    int port = freePort();
    var helloUrls = new CopyOnWriteArrayList<String>();
    EndpointConfig realEndpoint =
        endpointConfig(port, "/actual", OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS);

    OpcUaServer server =
        startServer(Set.of(realEndpoint), helloUrls, url -> false, new ServiceSets() {});
    try {
      String wrongUrl = "opc.tcp://127.0.0.1:" + port + "/wrong";
      String expectedFallbackUrl = wrongUrl + "/discovery";

      UaException exception = assertThrows(UaException.class, () -> OpcUaClient.create(wrongUrl));

      assertEquals(StatusCodes.Bad_TcpEndpointUrlInvalid, exception.getStatusCode().value());
      assertTrue(
          exception.getMessage().contains(wrongUrl),
          "primary error should reference original URL, got: " + exception.getMessage());
      assertEquals(1, exception.getSuppressed().length);
      UaException suppressed = assertInstanceOf(UaException.class, exception.getSuppressed()[0]);
      assertEquals(StatusCodes.Bad_TcpEndpointUrlInvalid, suppressed.getStatusCode().value());
      assertTrue(
          suppressed.getMessage().contains(expectedFallbackUrl),
          "suppressed error should reference fallback URL, got: " + suppressed.getMessage());
      assertEquals(List.of(wrongUrl, expectedFallbackUrl), helloUrls);
    } finally {
      server.shutdown().get(10, TimeUnit.SECONDS);
    }
  }

  // Connection refused cannot succeed at /discovery; configureTransport should only run for the
  // initial client transport config plus a single discovery attempt.
  @Test
  void refusedConnectionIsNotRetried() throws Exception {
    String url = "opc.tcp://127.0.0.1:" + freePort() + "/app";
    var configureTransportCalls = new AtomicInteger();

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                OpcUaClient.create(
                    url,
                    endpoints -> endpoints.stream().findFirst(),
                    b -> {
                      configureTransportCalls.incrementAndGet();
                      b.setConnectTimeout(uint(1000));
                    },
                    c -> {}));

    assertEquals(StatusCodes.Bad_ConnectionRejected, exception.getStatusCode().value());
    assertEquals(0, exception.getSuppressed().length);
    assertEquals(2, configureTransportCalls.get());
  }

  // Unresolvable hostnames cannot succeed at /discovery and must make only a single attempt.
  @Test
  void unresolvableHostIsNotRetried() {
    String url = "opc.tcp://does-not-exist.invalid:4840/repro";
    var configureTransportCalls = new AtomicInteger();

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                OpcUaClient.create(
                    url,
                    endpoints -> endpoints.stream().findFirst(),
                    b -> {
                      configureTransportCalls.incrementAndGet();
                      b.setConnectTimeout(uint(1000));
                    },
                    c -> {}));

    assertEquals(0, exception.getSuppressed().length);
    assertTrue(
        hasCause(exception, UnknownHostException.class)
            || hasCause(exception, UnresolvedAddressException.class),
        "expected DNS resolution failure in cause chain, got: " + exception);
    assertEquals(2, configureTransportCalls.get());
  }

  // A silent peer that accepts TCP without sending Acknowledge must fail with Bad_Timeout after a
  // single accepted socket rather than opening a second connection to /discovery.
  @Test
  void acknowledgeTimeoutIsNotRetried() throws Exception {
    var acceptedCount = new AtomicInteger();
    var firstAccepted = new CountDownLatch(1);

    try (var listener = new ServerSocket(0, 2, InetAddress.getByName("127.0.0.1"))) {
      listener.setSoTimeout(5000);
      String url = "opc.tcp://127.0.0.1:" + listener.getLocalPort() + "/app";

      Thread peer =
          new Thread(
              () -> {
                try (Socket accepted = listener.accept()) {
                  acceptedCount.incrementAndGet();
                  firstAccepted.countDown();
                  accepted.setSoTimeout(5000);
                  readUaTcpFrame(accepted.getInputStream());
                  while (accepted.getInputStream().read() != -1) {}
                } catch (Exception ignored) {
                  // Expected once client closes socket on acknowledge timeout.
                }
              },
              "test-ack-timeout-peer");
      peer.setDaemon(true);
      peer.start();

      UaException exception =
          assertThrows(
              UaException.class,
              () ->
                  OpcUaClient.create(
                      url,
                      endpoints -> endpoints.stream().findFirst(),
                      b -> b.setConnectTimeout(uint(2000)).setAcknowledgeTimeout(uint(250)),
                      c -> {}));

      assertTrue(firstAccepted.await(5, TimeUnit.SECONDS));
      assertEquals(StatusCodes.Bad_Timeout, exception.getStatusCode().value());
      assertEquals(0, exception.getSuppressed().length);
      assertEquals(1, acceptedCount.get());

      listener.setSoTimeout(300);
      assertThrows(
          SocketTimeoutException.class,
          listener::accept,
          "acknowledge timeout must not open a second connection to /discovery");
    }
  }

  // Part 6 §7.6: when the base URL fails Hello with Bad_TcpEndpointUrlInvalid or rejects unsecured
  // OpenSecureChannel while <url>/discovery serves discovery, fallback must succeed and install an
  // EndpointResolver targeting <url>/discovery.
  @ParameterizedTest(name = "rejectOpnOnBasePath={0}")
  @ValueSource(booleans = {false, true})
  void fallbackSucceedsWhenDiscoveryEndpointExists(boolean rejectOpnOnBasePath) throws Exception {
    int port = freePort();
    var helloUrls = new CopyOnWriteArrayList<String>();
    String baseUrl = "opc.tcp://127.0.0.1:" + port + "/app";
    String expectedDiscoveryUrl = baseUrl + "/discovery";
    String expectedServiceUrl = "opc.tcp://127.0.0.1:" + port + "/service";

    Set<EndpointConfig> endpoints = new LinkedHashSet<>();
    if (rejectOpnOnBasePath) {
      endpoints.add(endpointConfig(port, "/app", OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS));
    }
    endpoints.add(
        endpointConfig(port, "/app/discovery", OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS));
    endpoints.add(endpointConfig(port, "/service", OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS));

    OpcUaServer server =
        startServer(
            endpoints,
            helloUrls,
            url -> rejectOpnOnBasePath && baseUrl.equals(url),
            new ServiceSets() {});
    try {
      OpcUaClient client =
          OpcUaClient.create(
              baseUrl,
              discovered ->
                  discovered.stream()
                      .filter(e -> expectedServiceUrl.equals(e.getEndpointUrl()))
                      .findFirst(),
              b -> {},
              c -> {});

      assertEquals(expectedServiceUrl, client.getConfig().getEndpoint().getEndpointUrl());
      assertEquals(List.of(baseUrl, expectedDiscoveryUrl), helloUrls);

      // Verify the installed EndpointResolver also targets the working /discovery URL.
      helloUrls.clear();
      EndpointResolver resolver = client.getConfig().getEndpointResolver().orElseThrow();
      EndpointConfiguration refreshed = resolver.resolve().get(5, TimeUnit.SECONDS);
      assertEquals(expectedServiceUrl, refreshed.endpoint().getEndpointUrl());
      assertEquals(List.of(expectedDiscoveryUrl), helloUrls);
    } finally {
      server.shutdown().get(10, TimeUnit.SECONDS);
    }
  }

  // A URL already targeting /discovery must never append a second /discovery segment.
  @ParameterizedTest
  @ValueSource(strings = {"/app/discovery", "/app/discovery/"})
  void alreadyEndingInDiscoveryDoesNotRetry(String discoveryPath) throws Exception {
    int port = freePort();
    var helloUrls = new CopyOnWriteArrayList<String>();
    EndpointConfig other =
        endpointConfig(port, "/other", OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS);

    OpcUaServer server = startServer(Set.of(other), helloUrls, url -> false, new ServiceSets() {});
    try {
      String url = "opc.tcp://127.0.0.1:" + port + discoveryPath;

      UaException exception = assertThrows(UaException.class, () -> OpcUaClient.create(url));

      assertEquals(StatusCodes.Bad_TcpEndpointUrlInvalid, exception.getStatusCode().value());
      assertEquals(0, exception.getSuppressed().length);
      assertEquals(List.of(url), helloUrls);
    } finally {
      server.shutdown().get(10, TimeUnit.SECONDS);
    }
  }

  private static boolean hasCause(Throwable failure, Class<? extends Throwable> expectedType) {
    for (Throwable current = failure; current != null; current = current.getCause()) {
      if (expectedType.isInstance(current)) {
        return true;
      }
      if (current.getCause() == current) {
        break;
      }
    }
    return false;
  }

  private static void readUaTcpFrame(InputStream inputStream) throws Exception {
    byte[] header = inputStream.readNBytes(8);
    assertEquals(8, header.length, "expected full 8-byte UA-TCP header");
    int messageLength = ByteBuffer.wrap(header, 4, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
    byte[] body = inputStream.readNBytes(messageLength - 8);
    assertEquals(messageLength - 8, body.length, "expected full UA-TCP message body");
  }

  private static EndpointConfig endpointConfig(int port, String path, UserTokenPolicy tokenPolicy) {
    return EndpointConfig.newBuilder()
        .setBindAddress("127.0.0.1")
        .setHostname("127.0.0.1")
        .setBindPort(port)
        .setPath(path)
        .setSecurityPolicy(SecurityPolicy.None)
        .setSecurityMode(MessageSecurityMode.None)
        .addTokenPolicy(tokenPolicy)
        .build();
  }

  private static OpcUaServer startServer(
      Set<EndpointConfig> endpoints,
      List<String> helloUrls,
      Predicate<String> rejectOpenSecureChannelForUrl,
      ServiceSets serviceSets)
      throws Exception {

    OpcUaServerConfig config =
        OpcUaServerConfig.builder()
            .setApplicationUri("urn:eclipse:milo:test:discovery-fallback-server")
            .setApplicationName(LocalizedText.english("Discovery Fallback Test Server"))
            .setProductUri("urn:eclipse:milo:test:discovery-fallback-server")
            .setCertificateManager(new DefaultCertificateManager())
            .setEndpoints(new LinkedHashSet<>(endpoints))
            .build();

    OpcTcpServerTransportConfig transportConfig =
        OpcTcpServerTransportConfig.newBuilder()
            .setChannelPipelineCustomizer(
                pipeline ->
                    pipeline.addFirst(
                        new HelloObserverAndOpnRejector(helloUrls, rejectOpenSecureChannelForUrl)))
            .build();

    OpcUaServer server =
        new OpcUaServer(config, profile -> new OpcTcpServerTransport(transportConfig), serviceSets);
    server.startup().get(10, TimeUnit.SECONDS);
    return server;
  }

  private static ServiceSets serviceSetsWithGetEndpoints(GetEndpointsHandler handler) {
    return new ServiceSets() {
      @Override
      public DiscoveryServiceSet createDiscoveryServiceSet(OpcUaServer server) {
        DiscoveryServiceSet delegate = new DefaultDiscoveryServiceSet(server);
        return new DiscoveryServiceSet() {
          @Override
          public FindServersResponse onFindServers(
              ServiceRequestContext context, FindServersRequest request) throws UaException {
            return delegate.onFindServers(context, request);
          }

          @Override
          public FindServersOnNetworkResponse onFindServersOnNetwork(
              ServiceRequestContext context, FindServersOnNetworkRequest request)
              throws UaException {
            return delegate.onFindServersOnNetwork(context, request);
          }

          @Override
          public GetEndpointsResponse onGetEndpoints(
              ServiceRequestContext context, GetEndpointsRequest request) throws UaException {
            return handler.handle(delegate, context, request);
          }

          @Override
          public RegisterServerResponse onRegisterServer(
              ServiceRequestContext context, RegisterServerRequest request) throws UaException {
            return delegate.onRegisterServer(context, request);
          }

          @Override
          public RegisterServer2Response onRegisterServer2(
              ServiceRequestContext context, RegisterServer2Request request) throws UaException {
            return delegate.onRegisterServer2(context, request);
          }
        };
      }
    };
  }

  private static int freePort() throws Exception {
    try (var socket = new ServerSocket(0, 1, InetAddress.getByName("127.0.0.1"))) {
      return socket.getLocalPort();
    }
  }

  @FunctionalInterface
  private interface GetEndpointsHandler {
    GetEndpointsResponse handle(
        DiscoveryServiceSet delegate, ServiceRequestContext context, GetEndpointsRequest request)
        throws UaException;
  }

  private static final class HelloObserverAndOpnRejector extends ByteToMessageDecoder {

    private final List<String> helloUrls;
    private final Predicate<String> rejectOpenSecureChannelForUrl;
    private String lastHelloUrl;

    HelloObserverAndOpnRejector(
        List<String> helloUrls, Predicate<String> rejectOpenSecureChannelForUrl) {
      this.helloUrls = helloUrls;
      this.rejectOpenSecureChannelForUrl = rejectOpenSecureChannelForUrl;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
        throws Exception {
      if (in.readableBytes() < 8) {
        return;
      }

      int messageLength = (int) in.getUnsignedIntLE(in.readerIndex() + 4);
      if (in.readableBytes() < messageLength) {
        return;
      }

      ByteBuf frame = in.readRetainedSlice(messageLength);
      MessageType messageType = MessageType.fromMediumInt(frame.getMediumLE(frame.readerIndex()));

      if (messageType == MessageType.Hello) {
        HelloMessage hello = TcpMessageDecoder.decodeHello(frame.duplicate());
        lastHelloUrl = hello.getEndpointUrl();
        helloUrls.add(lastHelloUrl);
      } else if (messageType == MessageType.OpenSecureChannel
          && rejectOpenSecureChannelForUrl.test(lastHelloUrl)) {
        frame.release();
        ExceptionHandler.sendErrorMessage(
            ctx,
            new UaException(
                StatusCodes.Bad_SecurityPolicyRejected,
                "unsecured OpenSecureChannel rejected for " + lastHelloUrl));
        return;
      }

      out.add(frame);
    }
  }
}
