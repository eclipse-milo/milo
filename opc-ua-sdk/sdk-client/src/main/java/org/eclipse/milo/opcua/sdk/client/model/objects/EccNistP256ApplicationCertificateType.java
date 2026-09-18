package org.eclipse.milo.opcua.sdk.client.model.objects;

import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;

/**
 * Client API for the EccNistP256ApplicationCertificateType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.4/#7.8.4.11">Model
 *     documentation</a>
 */
public interface EccNistP256ApplicationCertificateType extends EccApplicationCertificateType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 23538L);
}
