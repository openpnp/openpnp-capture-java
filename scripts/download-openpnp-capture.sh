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

mkdir -p openpnp-capture/binaries/darwin
mkdir -p openpnp-capture/binaries/win32-x86
mkdir -p openpnp-capture/binaries/win32-x86-64
mkdir -p openpnp-capture/binaries/linux-x86-64
mkdir -p openpnp-capture/binaries/linux-arm64

curl -L -o openpnp-capture/binaries/darwin/libopenpnp-capture.dylib https://github.com/openpnp/openpnp-capture/releases/download/$1/libopenpnp-capture.dylib
curl -L -o openpnp-capture/binaries/win32-x86/openpnp-capture.dll https://github.com/openpnp/openpnp-capture/releases/download/$1/openpnp-capture_Win32.dll
curl -L -o openpnp-capture/binaries/win32-x86-64/openpnp-capture.dll https://github.com/openpnp/openpnp-capture/releases/download/$1/openpnp-capture_x64.dll
curl -L -o openpnp-capture/binaries/linux-x86-64/libopenpnp-capture.so https://github.com/openpnp/openpnp-capture/releases/download/$1/libopenpnp-capture.so
curl -L -o openpnp-capture/binaries/linux-arm64/libopenpnp-capture.so https://github.com/openpnp/openpnp-capture/releases/download/$1/libopenpnp-capture-arm64.so
