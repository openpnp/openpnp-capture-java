1. Choose an OpenPnP Capture release from https://github.com/openpnp/openpnp-capture/releases.
2. `./scripts/download-openpnp-capture.sh <RELEASE>`
3. Set versions in `pom.xml`. Should match release. Append `-n` for sub-releases.
4. `mvn clean deploy`