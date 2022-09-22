/*
 * Copyright (c) 2022 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.uasc;

import java.util.List;

import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.transport.server.ServiceInterface;

public interface UascServerConfig {

    List<EndpointDescription> getEndpointDescriptions();

    CertificateManager getCertificateManager();

    CertificateValidator getCertificateValidator();

    EncodingLimits getEncodingLimits();

    UInteger getMaximumSecureChannelLifetime();

    UInteger getMinimumSecureChannelLifetime();

    ServiceInterface getServiceInterface();

}
