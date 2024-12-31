/// *
// * Copyright (c) 2019 the Eclipse Milo Authors
// *
// * This program and the accompanying materials are made
// * available under the terms of the Eclipse Public License 2.0
// * which is available at https://www.eclipse.org/legal/epl-2.0/
// *
// * SPDX-License-Identifier: EPL-2.0
// */
//
// package org.eclipse.milo.opcua.stack.client.config;
//
// import java.security.cert.X509Certificate;
// import java.util.List;
// import java.util.Optional;
//
// import org.eclipse.milo.opcua.stack.SecurityFixture;
// import org.eclipse.milo.opcua.stack.client.UaStackClientConfig;
// import org.eclipse.milo.opcua.stack.client.security.ClientCertificateValidator;
// import org.eclipse.milo.opcua.stack.core.Stack;
// import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
// import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
// import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
// import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
// import org.testng.annotations.Test;
//
// import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
// import static org.testng.Assert.assertEquals;
//
// public class UaStackClientConfigTest extends SecurityFixture {
//
//    private final EndpointDescription endpoint = new EndpointDescription(
//        "opc.tcp://localhost:62541",
//        null,
//        null,
//        null,
//        null,
//        new UserTokenPolicy[]{
//            new UserTokenPolicy(
//                "anonymous",
//                UserTokenType.Anonymous,
//                null, null, null)
//        },
//        null,
//        null
//    );
//
//    private final ClientCertificateValidator validator = new ClientCertificateValidator() {
//
//        @Override
//        public void validateCertificateChain(List<X509Certificate> certificateChain) {}
//
//        @Override
//        public void validateCertificateChain(
//            List<X509Certificate> certificateChain,
//            String applicationUri,
//            String... validHostNames
//        ) {}
//
//    };
//
//    @Test
//    public void testCopy() {
//        UaStackClientConfig original = UaStackClientConfig.builder()
//            .setEndpoint(endpoint)
//            .setKeyPair(clientKeyPair)
//            .setCertificate(clientCertificate)
//            .setCertificateChain(new X509Certificate[]{clientCertificate})
//            .setCertificateValidator(validator)
//            .setEncodingLimits(EncodingLimits.DEFAULT)
//            .setChannelLifetime(uint(1234))
//            .setExecutor(Stack.sharedExecutor())
//            .setEventLoop(Stack.sharedEventLoop())
//            .setWheelTimer(Stack.sharedWheelTimer())
//            .setAcknowledgeTimeout(uint(12345))
//            .build();
//
//        UaStackClientConfig copy = UaStackClientConfig.copy(original).build();
//
//        assertEquals(copy.getKeyPair(), original.getKeyPair());
//        assertEquals(copy.getCertificate(), original.getCertificate());
//        assertEquals(copy.getCertificateChain(), original.getCertificateChain());
//        assertEquals(copy.getCertificateValidator(), original.getCertificateValidator());
//        assertEquals(copy.getEncodingLimits(), original.getEncodingLimits());
//        assertEquals(copy.getChannelLifetime(), original.getChannelLifetime());
//        assertEquals(copy.getExecutor(), original.getExecutor());
//        assertEquals(copy.getEventLoop(), original.getEventLoop());
//        assertEquals(copy.getWheelTimer(), original.getWheelTimer());
//        assertEquals(copy.getAcknowledgeTimeout(), original.getAcknowledgeTimeout());
//    }
//
//    @Test
//    public void testCopyAndModify() {
//        UaStackClientConfig original = UaStackClientConfig.builder()
//            .setEndpoint(endpoint)
//            .setKeyPair(clientKeyPair)
//            .setCertificate(clientCertificate)
//            .setCertificateValidator(validator)
//            .setEncodingLimits(EncodingLimits.DEFAULT)
//            .setChannelLifetime(uint(1234))
//            .setExecutor(Stack.sharedExecutor())
//            .setEventLoop(Stack.sharedEventLoop())
//            .setWheelTimer(Stack.sharedWheelTimer())
//            .build();
//
//        UaStackClientConfig copy = UaStackClientConfig.copy(
//            original,
//            builder ->
//                builder
//                    .setKeyPair(null)
//                    .setCertificate(null)
//                    .setCertificateChain(null)
//                    .setCertificateValidator(null)
//                    .setEncodingLimits(null)
//                    .setChannelLifetime(uint(0))
//                    .setAcknowledgeTimeout(uint(12345))
//        );
//
//        assertEquals(copy.getKeyPair(), Optional.empty());
//        assertEquals(copy.getCertificate(), Optional.empty());
//        assertEquals(copy.getCertificateChain(), Optional.empty());
//        assertEquals(copy.getCertificateValidator(), null);
//        assertEquals(copy.getEncodingLimits(), null);
//        assertEquals(copy.getChannelLifetime(), uint(0));
//        assertEquals(copy.getAcknowledgeTimeout(), uint(12345));
//    }
//
// }
