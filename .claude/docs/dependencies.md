# Dependency Source Code

To examine dependency source code, check the `external/src` directory at the project root. This
directory contains unpacked source files from all dependencies, organized by package structure for
easy browsing and searching.

## Setup

If the directory doesn't exist or content is missing, run this command from the project root to
download and unpack all dependency sources:

```bash
mvn -q generate-resources -Pdownload-external-src
```

This creates the `external/src` directory with sources from all dependencies in a single top-level
location.
