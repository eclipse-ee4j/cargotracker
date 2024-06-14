# Cargotracker-documentation

This is the repo for building the Eclipse Cargotracker Documentation site.

## Prerequisites

- [JDK](https://jdk.java.net/)
- [Maven](https://maven.apache.org/)

## Setup

Ensure JDK and Maven are installed. Additionally, install the `asciidoctor-pdf` gem:

```bash
sudo gem install asciidoctor-pdf
```

## Building

To build the documentation, execute:

```bash
mvn clean package
```

The output will be in `target/generated-docs`.
To view, just open [`target/generated-docs/cargotracker/current/index.html`](target/generated-docs/cargotracker/current/index.html) in a browser.

```bash
browse target/generated-docs/cargotracker/current/index.html
```

If you encounter a build failure with the following log entry indicating "Command not found: asciidoctor-pdf":

> [INFO] {"level":"fatal","time":1684333903235,"name":"antora","hint":"Add the --stacktrace option to see the cause of the error.","msg":"Command not found: asciidoctor-pdf"}

Then you need to run beforehand:

```bash
source ~/.rvm/scripts/rvm
```
