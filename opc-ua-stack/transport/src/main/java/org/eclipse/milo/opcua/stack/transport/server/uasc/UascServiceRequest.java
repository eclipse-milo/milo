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

import org.eclipse.milo.opcua.stack.core.channel.SecureChannel;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequest;

public class UascServiceRequest extends ServiceRequest {

    private final long requestId;

    public UascServiceRequest(String endpointUrl, SecureChannel secureChannel, UaRequestMessageType requestMessage, long requestId) {
        super(endpointUrl, secureChannel, requestMessage);

        this.requestId = requestId;
    }

    public long getRequestId() {
        return requestId;
    }

}
