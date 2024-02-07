/*
 * Copyright (c) 2022 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.enumerated;

import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumField;
import org.jetbrains.annotations.Nullable;

/**
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.3.1/#5.3.1.7">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.3.1/#5.3.1.7</a>
 */
public enum TsnTalkerStatus implements UaEnumeratedType {
    /**
     * No Talker detected.
     */
    None(0),

    /**
     * Talker ready (configured).
     */
    Ready(1),

    /**
     * Talker failed.
     */
    Failed(2);

    private final int value;

    TsnTalkerStatus(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public ExpandedNodeId getTypeId() {
        return TypeInfo.TYPE_ID;
    }

    public static @Nullable TsnTalkerStatus from(int value) {
        switch (value) {
            case 0:
                return None;
            case 1:
                return Ready;
            case 2:
                return Failed;
            default:
                return null;
        }
    }

    public static EnumDefinition definition() {
        return new EnumDefinition(new EnumField[]{
            new EnumField(0L, LocalizedText.NULL_VALUE, new LocalizedText("", "No Talker detected."), "None"),
            new EnumField(1L, LocalizedText.NULL_VALUE, new LocalizedText("", "Talker ready (configured)."), "Ready"),
            new EnumField(2L, LocalizedText.NULL_VALUE, new LocalizedText("", "Talker failed."), "Failed")
        });
    }

    public static final class TypeInfo {
        public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=24222");
    }
}