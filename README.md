# openpnp-capture-java

Java bindings for https://github.com/openpnp/openpnp-capture. Releases include all platform binaries
and support auto extraction for a "plug and play" experience.

# Build
See https://github.com/openpnp/openpnp-capture/releases to pick a release of the native
library to build against and specify it's tag name in the next command.

```
./scripts/download-openpnp-capture.sh v0.0.1
mvn clean package
```

# Release

1. Update the openpnp-capture version in `scripts/download-openpnp-capture.sh`.
2. Push tag to Github.

Travis will build the tag and deploy it to Github at https://github.com/openpnp/openpnp-capture-java/releases/latest
