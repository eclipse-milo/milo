package org.eclipse.milo.opcua.sdk.client.model.objects;

import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;

/**
 * Client API for the EccNistP384ApplicationCertificateType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.4/#7.8.4.12">Model
 *     documentation</a>
 */
public interface EccNistP384ApplicationCertificateType extends EccApplicationCertificateType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 23539L);
}
