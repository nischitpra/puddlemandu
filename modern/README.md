# puddlemandu-modern

This folder is a **modern Gradle/Android build** for the legacy libGDX game code in
`../nischitpra-puddlemandu-f687cb8bcf16/`.

## Important: Java 25 vs Android

- **Gradle** can run on Java 25 (with Gradle 9.1+).
- **Android Gradle Plugin (AGP) 9** requires **JDK 17** to run Android builds.
- Android bytecode/tooling also does not support “Java 25 bytecode” for apps.

So: you can have **Java 25 installed**, but you should **build this Android app with Java 17**.

## Requirements

- **JDK 17** (recommended via Homebrew): `brew install --cask temurin@17`
- Android Studio + Android SDK (API 36 / build-tools 36) installed via SDK Manager

## How to open/build

- Open the `modern/` folder in Android Studio.
- Let Android Studio sync Gradle.
- Build/run the `android` module.

Or from terminal (after setting Java 17):

```bash
brew install --cask temurin@17
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
java -version
./gradlew :android:assembleDebug
```

## iOS (RoboVM/MobiVM)

This repo now includes an `:ios` module that can run the same libGDX game on iOS via RoboVM (MobiVM).

### Requirements

- **macOS + Xcode** (and Xcode command line tools)
- **JDK 17+** (Gradle 9 requires it to *run*, even though we compile game bytecode to Java 8)

### Build / run

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
./gradlew :ios:build

# Launch in simulator (device type can be changed via RoboVM properties)
./gradlew :ios:launchIPhoneSimulator
```

## What’s being reused (no mass copy)

This build points `sourceSets` at the legacy directories:

- `core` sources: `../nischitpra-puddlemandu-f687cb8bcf16/core/src`
- Android sources: `../nischitpra-puddlemandu-f687cb8bcf16/android/src`
- Android manifest/res/assets: `../nischitpra-puddlemandu-f687cb8bcf16/android/AndroidManifest.xml`, `res/`, `assets/`

