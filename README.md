![OpenPNP Logo](https://raw.githubusercontent.com/openpnp/openpnp-logo/develop/logo_small.png)

# openpnp-capture-java

Java bindings for [the openpnp-capture video capture library](https://github.com/openpnp/openpnp-capture). Releases include all platform binaries
and support auto extraction for a "plug and play" experience. Simply install the Maven dependency
and start capturing!

# Usage

## Maven

```
<dependency>
	<groupId>org.openpnp</groupId>
	<artifactId>openpnp-capture-java</artifactId>
</dependency>
```

## Example
```
public static void main(String[] args) {
    OpenPnpCapture capture = new OpenPnpCapture();
    CaptureDevice device = capture.getDevices().get(0);
    CaptureFormat format = device.getFormats().get(0);
    CaptureStream stream = device.openStream(format);
    BufferedImage image = stream.capture();
}
```

# Build
See https://github.com/openpnp/openpnp-capture/releases to pick a release of the native
library to build against and specify it's tag name in the next command.

```
./scripts/download-openpnp-capture.sh <RELEASE>
mvn clean package
```

# Release
Releases are built automatically using Github Actions. Binaries for non-tagged
commits can be downloaded from the Actions tab. Binaries for tagged releases
are deployed to Maven Central and Github Releases.

To release a new version to Github Releases and Maven central:

1. Choose an OpenPnP Capture release from https://github.com/openpnp/openpnp-capture/releases.
2. Update the version line in `.github/workflows/build.yml` to include that release name.
3. Update the version in `pom.xml`. Should match release. Append `-n` for sub-releases.
4. Commit and tag the changes with the new version number.
5. Push to master.
7. Github Actions will build and deploy the release.
