{
  description = "Etna, the Android wallet for NOX Shield";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-24.11";
    rust-overlay.url = "github:oxalica/rust-overlay";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { nixpkgs, rust-overlay, flake-utils, ... }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs {
          inherit system;
          overlays = [ (import rust-overlay) ];
          config.allowUnfree = true;
          config.android_sdk.accept_license = true;
        };

        # Pinned so two machines building one tag agree. Raising a version is a
        # commit, with the reason in it.
        android = pkgs.androidenv.composeAndroidPackages {
          platformVersions = [ "35" ];
          buildToolsVersions = [ "35.0.0" ];
          includeNDK = true;
          ndkVersions = [ "27.0.12077973" ];
          cmdLineToolsVersion = "13.0";
        };

        rust = pkgs.rust-bin.stable."1.91.1".default.override {
          targets = [
            "aarch64-linux-android"
            "armv7-linux-androideabi"
            "x86_64-linux-android"
          ];
        };
      in
      {
        devShells.default = pkgs.mkShell {
          packages = [
            rust
            pkgs.cargo-ndk
            pkgs.jdk17
            pkgs.gradle
            android.androidsdk
            pkgs.ktlint
          ];

          ANDROID_HOME = "${android.androidsdk}/libexec/android-sdk";
          ANDROID_NDK_ROOT = "${android.androidsdk}/libexec/android-sdk/ndk-bundle";
          JAVA_HOME = "${pkgs.jdk17}";

          shellHook = ''
            echo "Etna Android shell. scripts/build-core.sh builds the core,"
            echo "then: gradle :app:assembleRelease"
          '';
        };
      });
}
