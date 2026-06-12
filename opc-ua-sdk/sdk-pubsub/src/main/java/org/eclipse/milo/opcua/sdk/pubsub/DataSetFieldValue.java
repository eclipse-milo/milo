/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub;

import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;

/**
 * A single decoded field of a received DataSet.
 *
 * @param dataSetFieldId the stable wire identity of the field, exposed so target mapping and
 *     delta-frame decoding share one identity.
 * @param name the name of the field.
 * @param index the index of the field within the DataSet.
 * @param value the decoded value of the field.
 */
public record DataSetFieldValue(UUID dataSetFieldId, String name, int index, DataValue value) {}
