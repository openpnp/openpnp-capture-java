# openpnp-capture-java

Java bindings for https://github.com/openpnp/openpnp-capture. Releases include all platform binaries
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

To release a new version to Maven central:

1. Choose an OpenPnP Capture release from https://github.com/openpnp/openpnp-capture/releases.
2. `./scripts/download-openpnp-capture.sh <RELEASE>`
3. Set versions in `pom.xml`. Should match release. Append `-n` for sub-releases.
4. `mvn clean deploy`

The deploy step requires credentials and private keys for Sonatype. Please contact
a maintainer to discuss a public release.