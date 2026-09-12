/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.7">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.7</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface BuildInfoType extends BaseDataVariableType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getProductUriNode();

  /** Gets the existing node's local value. */
  @Nullable String getProductUri();

  /** Sets the existing node's local value. */
  void setProductUri(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getManufacturerNameNode();

  /** Gets the existing node's local value. */
  @Nullable String getManufacturerName();

  /** Sets the existing node's local value. */
  void setManufacturerName(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getProductNameNode();

  /** Gets the existing node's local value. */
  @Nullable String getProductName();

  /** Sets the existing node's local value. */
  void setProductName(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSoftwareVersionNode();

  /** Gets the existing node's local value. */
  @Nullable String getSoftwareVersion();

  /** Sets the existing node's local value. */
  void setSoftwareVersion(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getBuildNumberNode();

  /** Gets the existing node's local value. */
  @Nullable String getBuildNumber();

  /** Sets the existing node's local value. */
  void setBuildNumber(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getBuildDateNode();

  /** Gets the existing node's local value. */
  @Nullable DateTime getBuildDate();

  /** Sets the existing node's local value. */
  void setBuildDate(@Nullable DateTime value);
}
