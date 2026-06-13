# jme3-imgui-plugin

[![Java Version](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/technologies/)
[![jMonkeyEngine](https://img.shields.io/badge/jMonkeyEngine-3.10.0--beta1-blue.svg)](https://jmonkeyengine.org/)
[![Dear ImGui](https://img.shields.io/badge/Dear%20ImGui-1.92.0-brightgreen.svg)](https://github.com/ocornut/imgui)
[![License](https://img.shields.io/badge/License-BSD%203--Clause-blue.svg)](LICENSE)

A Dear ImGui integration plugin for jMonkeyEngine 3, providing fast and flexible immediate-mode user interfaces for debugging, tooling, and editor development.

## 🚀 Features

* **Multi-Platform Support:** Pre-configured native bindings for Windows, Linux, and macOS.
* **Modern Pipeline:** Optimized for **jME 3.9.0+** and **LWJGL3** backends.
* **Low Overhead:** Native performance, ideal for real-time diagnostics, inspector panels, and developer consoles.

## Prerequisites & Compatibility

| Component | Target Version |
| :--- |:---------------|
| **Java JDK** | `17+`          |
| **jMonkeyEngine** | `3.9.0+`       |
| **Dear ImGui** | `1.92.0`       |

## 📦 Installation & Local Setup

Since this plugin is currently in development (`1.0.0-SNAPSHOT`), you need to build and install it to your local Maven repository (`.m2`) before using it in your projects.

###  Gradle (Groovy) inside your Project:

```groovy
repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation 'com.jme3.imgui:jme3-imgui-plugin:1.0.0-SNAPSHOT'

    // Add only the ones you need, or keep all three for cross-platform desktop support:
    runtimeOnly "io.github.spair:imgui-java-natives-windows:1.92.0"
    runtimeOnly "io.github.spair:imgui-java-natives-linux:1.92.0"
    runtimeOnly "io.github.spair:imgui-java-natives-macos:1.92.0"
}
```

## 📄 License

This project is licensed under the BSD 3-Clause License - see the LICENSE file for details.