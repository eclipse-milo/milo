/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.uadp;

import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;

/**
 * A DataSetMetaData discovery announcement decoded from a NetworkMessage.
 *
 * @param dataSetWriterId the id of the DataSetWriter the metadata describes.
 * @param metaData the announced metadata.
 */
public record DecodedMetaData(UShort dataSetWriterId, DataSetMetaDataType metaData) {}
