#!/bin/bash

if [ "$#" -ne 1 ]; then
    echo "Usage: pull-release-binaries.sh <release/tag name>"
    echo "  Downloads source and binaries for the native portion of the library"
    echo "  from Github releases so that this library can be built."
    exit
fi

rm -rf openpnp-capture
mkdir -p openpnp-capture
curl -L -o openpnp-capture/source.tar.gz https://github.com/openpnp/openpnp-capture/archive/$1.tar.gz
tar -C openpnp-capture -xzf openpnp-capture/source.tar.gz --strip 1

mkdir -p openpnp-capture/binaries/darwin-x86_64
mkdir -p openpnp-capture/binaries/darwin-aarch64
mkdir -p openpnp-capture/binaries/windows-x86_32
mkdir -p openpnp-capture/binaries/windows-x86_64
mkdir -p openpnp-capture/binaries/linux-x86_64
mkdir -p openpnp-capture/binaries/linux-aarch64

curl -L -o openpnp-capture/binaries/darwin-x86_64/libopenpnp-capture.dylib https://github.com/openpnp/openpnp-capture/releases/download/$1/libopenpnp-capture.dylib
curl -L -o openpnp-capture/binaries/darwin-aarch64/libopenpnp-capture.dylib https://github.com/openpnp/openpnp-capture/releases/download/$1/libopenpnp-capture.dylib
curl -L -o openpnp-capture/binaries/windows-x86_32/openpnp-capture.dll https://github.com/openpnp/openpnp-capture/releases/download/$1/openpnp-capture_x86_32.dll
curl -L -o openpnp-capture/binaries/windows-x86_64/openpnp-capture.dll https://github.com/openpnp/openpnp-capture/releases/download/$1/openpnp-capture_x86_64.dll
curl -L -o openpnp-capture/binaries/linux-x86_64/libopenpnp-capture.so https://github.com/openpnp/openpnp-capture/releases/download/$1/libopenpnp-capture.so
curl -L -o openpnp-capture/binaries/linux-aarch64/libopenpnp-capture.so https://github.com/openpnp/openpnp-capture/releases/download/$1/libopenpnp-capture-arm64.so
