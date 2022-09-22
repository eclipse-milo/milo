/*
 * Copyright (c) 2022 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.tcp;

import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.transport.server.OpcServerTransportConfig;

public interface OpcTcpServerTransportConfig extends OpcServerTransportConfig {

    UInteger getMaximumSecureChannelLifetime();

    UInteger getMinimumSecureChannelLifetime();

    static OpcTcpServerTransportConfigBuilder newBuilder() {
        return new OpcTcpServerTransportConfigBuilder();
    }

}
