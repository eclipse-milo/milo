/*
 * Copyright (c) 2021 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.serialization;

import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;

/**
 * Identifies an OPC UA structured type.
 */
public interface UaStructure extends UaSerializableType {

    default ExpandedNodeId getBinaryEncodingId() {
        return ExpandedNodeId.NULL_VALUE;
    }

    default ExpandedNodeId getXmlEncodingId() {
        return ExpandedNodeId.NULL_VALUE;
    }

    default ExpandedNodeId getJsonEncodingId() {
        return ExpandedNodeId.NULL_VALUE;
    }

}
