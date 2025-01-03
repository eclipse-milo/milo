# Eclipse Milo
[![Jenkins](https://img.shields.io/jenkins/build/https/ci.eclipse.org/milo/job/Milo_Deploy.svg)](https://ci.eclipse.org/milo/)
[![Maven Central](https://img.shields.io/maven-central/v/org.eclipse.milo/milo.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.eclipse.milo%22%20AND%20a%3A%22milo%22)

Milo is an open-source implementation of OPC UA (currently targeting 1.05). It includes a high-performance stack (channels, serialization, data structures, security) as well as client and server SDKs built on top of the stack.

Stack Overflow tag: [milo](http://stackoverflow.com/questions/tagged/milo)

Mailing list: https://dev.eclipse.org/mailman/listinfo/milo-dev


## Maven

### Building Milo

**Using JDK 8**, run `mvn clean install` from the project root.

To maintain compatibility with Java 8 it is recommended that you build using JDK 8, however the library is runtime compatible with versions 8 and later (e.g. JDK 11, JDK 17).

### Releases

Releases are published to Maven Central and snapshots to Sonatype.

#### OPC UA Client SDK

```xml
<dependency>
    <groupId>org.eclipse.milo</groupId>
    <artifactId>milo-sdk-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### OPC UA Server SDK

```xml
<dependency>
    <groupId>org.eclipse.milo</groupId>
    <artifactId>milo-sdk-server</artifactId>
    <version>1.0.0</version>
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

An internet-facing demo server is accessible at `opc.tcp://milo.digitalpetri.com:62541/milo`.

It accepts both unsecured and secured connections. Before connecting with security you must upload your client's DER-encoded X509 certificate using the form at http://milo.digitalpetri.com.

Authenticate anonymously or with one of the following credential pairs:
- `user1` / `password`
- `user2` / `password`
- `admin` / `password`

The code powering the demo server is available here: https://github.com/digitalpetri/opc-ua-demo-server
