# Eclipse Milo
[![Actions](https://img.shields.io/github/actions/workflow/status/eclipse-milo/milo/.github%2Fworkflows%2Fmaven-verify.yml
)](https://github.com/eclipse-milo/milo/actions/workflows/maven-verify.yml)
[![Maven Central](https://img.shields.io/maven-central/v/org.eclipse.milo/milo.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.eclipse.milo%22%20AND%20a%3A%22milo%22)

Milo is an open-source implementation of OPC UA (currently targeting 1.05). It includes a high-performance stack (channels, serialization, data structures, security) as well as client and server SDKs built on top of the stack.

Stack Overflow tag: [milo](http://stackoverflow.com/questions/tagged/milo)

Mailing list: https://dev.eclipse.org/mailman/listinfo/milo-dev


## Maven

### Building Milo

**Using JDK 17**, run `mvn clean install` from the project root.

To maintain compatibility with Java 17 it is recommended that you build using JDK 17, however the library is runtime compatible with versions 17 and later (e.g. JDK 21, JDK 24).

### Releases

Releases are published to Maven Central and snapshots to Sonatype.

#### OPC UA Client SDK

```xml
<dependency>
    <groupId>org.eclipse.milo</groupId>
    <artifactId>milo-sdk-client</artifactId>
    <version>1.0.5</version>
</dependency>
```

#### OPC UA Server SDK

```xml
<dependency>
    <groupId>org.eclipse.milo</groupId>
    <artifactId>milo-sdk-server</artifactId>
    <version>1.0.5</version>
</dependency>
```

Referencing a `SNAPSHOT` release requires the Sonatype snapshot repository be added to your pom file:

```xml
<repository>
    <id>oss-sonatype</id>
    <name>oss-sonatype</name>
    <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
</repository>
```

## Public Demo Server

An internet-facing instance of this demo server is accessible at
`opc.tcp://milo.digitalpetri.com:62541/milo`.

It accepts both unsecured and secured connections. All incoming client certificates are automatically trusted.

Authenticate anonymously or with one of the following credential pairs:

- `User` / `password`
    - roles: `WellKnownRole_AuthenticatedUser`
- `UserA` / `password`
    - roles: `SiteA_Read`, `SiteA_Write`
- `UserB` / `password`
    - roles: `SiteB_Read`, `SiteB_Write`
- `SiteAdmin` / `password`
    - roles: `SiteA_Read`, `SiteB_Read`
- `SecurityAdmin` / `password`
    - roles: `WellKnownRole_SecurityAdmin`

The code powering the demo server is available here: https://github.com/digitalpetri/opc-ua-demo-server
