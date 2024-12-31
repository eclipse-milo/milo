/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core;

import org.eclipse.milo.opcua.stack.core.types.structured.ServiceFault;

public class UaServiceFaultException extends UaException {

  private final ServiceFault serviceFault;

  public UaServiceFaultException(ServiceFault serviceFault) {
    super(serviceFault.getResponseHeader().getServiceResult());

    this.serviceFault = serviceFault;
  }

  public UaServiceFaultException(ServiceFault serviceFault, String message) {
    super(serviceFault.getResponseHeader().getServiceResult(), message);

    this.serviceFault = serviceFault;
  }

  public ServiceFault getServiceFault() {
    return serviceFault;
  }
}
