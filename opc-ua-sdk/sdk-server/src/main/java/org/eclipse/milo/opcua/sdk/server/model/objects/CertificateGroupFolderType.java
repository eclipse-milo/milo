package org.eclipse.milo.opcua.sdk.server.model.objects;

public interface CertificateGroupFolderType extends FolderType {
    CertificateGroupType getDefaultApplicationGroupNode();

    CertificateGroupType getDefaultHttpsGroupNode();

    CertificateGroupType getDefaultUserTokenGroupNode();
}
