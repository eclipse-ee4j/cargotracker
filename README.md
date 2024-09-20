# Eclipse Cargo Tracker Documentation Repository

This repository contains the official documentation for the Eclipse Cargo 
Tracker project. Here, you will find all guides, tutorials, and technical 
documentation related to Cargo Tracker.

## Table of Contents

- [Introduction](#introduction)
- [Documentation Structure](#documentation-structure)
- [Viewing the Documentation](#viewing-the-documentation)
- [Contributing to Documentation](#contributing-to-documentation)
- [Building the Documentation Site](#building-the-documentation-site)
- [Issue Tracking](#issue-tracking)

## Introduction

Welcome to the Cargo Tracker documentation repository! This repo is dedicated 
to all things documentation for the Cargo Tracker project. Whether you're 
looking for user guides, API references, or developer documentation, you'll 
find it all here.

## Documentation Structure

The documentation is organized as follows:

- **Getting Started**: Provides brief orientation on the project

- **Visual Studio Code**: Provides detailed instructions on how to get started 
  with Visual Studio Code

- **Jakarta EE and DDD**: Provides a brief overview of DDD as it relates to 
  this project and Jakarta EE

- **Characterization**: Overviews how the basic building blocks of the domain
  - entities, value objects, aggregates, services, repositories and factories 
  - are implemented in the application using Jakarta EE

- **Layers**: Explains the architectural layers in the application and how they 
  relate to various Jakarta EE APIs

- **Resources**: Provides some useful resources for learning more about DDD 
  and Jakarta EE

## Viewing the Documentation

To view the documentation, you can:

1. **Locally**: Clone the repository and browse the `.adoc` files.
2. **Online**: Visit the online documentation portal site.

### Cloning the Repository

```bash
git clone https://github.com/eclipse-ee4j/cargotracker.git
cd cargotracker
git checkout docs
```

Open the `.adoc` files in your preferred editor.

## Contributing to Documentation

We welcome contributions to improve and expand the Cargo Tracker documentation! 
Please follow the guidelines below:

1. Fork the repository.
2. Create a new branch for your changes.
3. Ensure that your changes adhere to the project's style and formatting 
   guidelines.
4. Submit a pull request with a clear description of your changes.

## Building the Documentation Site

The Cargo Tracker documentation site is built using the 
[jakartaee tutorial's playbook](https://github.com/jakartaee/jakartaee-documentation/blob/main/antora-playbook.yml).

Read through the 
[README.md](https://github.com/jakartaee/jakartaee-documentation/blob/main/README.md) 
of the
[jakartaee-documentation](https://github.com/jakartaee/jakartaee-documentation/blob/main/antora-playbook.yml) 
repository and learn how to build the Cargo Tracker documentation site.

## Issue Tracking

If you find any issues in the documentation or have suggestions for 
improvement, please report them in the 
[Issues](https://github.com/eclipse-ee4j/cargotracker/issues) section of this 
repository.
