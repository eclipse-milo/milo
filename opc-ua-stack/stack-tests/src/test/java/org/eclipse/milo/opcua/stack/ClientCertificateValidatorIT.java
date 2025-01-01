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
// package org.eclipse.milo.opcua.stack;
//
// import java.security.cert.X509Certificate;
// import java.util.List;
// import java.util.concurrent.CountDownLatch;
// import java.util.concurrent.TimeUnit;
//
// import org.eclipse.milo.opcua.stack.client.UaStackClientConfigBuilder;
// import org.eclipse.milo.opcua.stack.client.security.ClientCertificateValidator;
// import org.eclipse.milo.opcua.stack.core.UaException;
// import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
// import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.testng.annotations.Test;
//
// import static org.testng.Assert.assertTrue;
//
// public class ClientCertificateValidatorIT extends StackIntegrationTest {
//
//    private final Logger logger = LoggerFactory.getLogger(getClass());
//
//    private final CountDownLatch latch = new CountDownLatch(1);
//
//    private final ClientCertificateValidator validator = new ClientCertificateValidator() {
//
//        @Override
//        public void validateCertificateChain(List<X509Certificate> certificateChain) throws
// UaException {
//            X509Certificate certificate = certificateChain.get(0);
//            logger.info("verifyTrustChain: {}", certificate.getSubjectX500Principal());
//            latch.countDown();
//        }
//
//        @Override
//        public void validateCertificateChain(
//            List<X509Certificate> certificateChain,
//            String applicationUri,
//            String... validHostNames
//        ) throws UaException {
//
//            validateCertificateChain(certificateChain);
//        }
//
//    };
//
//    @Test
//    public void testClientCertificateValidatorIsCalled() throws InterruptedException {
//        assertTrue(latch.await(10, TimeUnit.SECONDS), "latch not obtained!");
//    }
//
//    @Override
//    protected UaStackClientConfigBuilder configureClient(UaStackClientConfigBuilder builder) {
//        return builder
//            .setCertificate(clientCertificate)
//            .setCertificateValidator(validator);
//    }
//
//    @Override
//    protected EndpointDescription selectEndpoint(List<EndpointDescription> endpoints) {
//        return endpoints.stream()
//            .filter(e -> !SecurityPolicy.None.getUri().equals(e.getSecurityPolicyUri()))
//            .findFirst()
//            .orElseThrow(() -> new RuntimeException("no secure endpoint found!"));
//    }
//
// }
