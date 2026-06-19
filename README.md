# laa-spring-boot-ui-template
[![Ministry of Justice Repository Compliance Badge](https://github-community.service.justice.gov.uk/repository-standards/api/laa-spring-boot-ui-template/badge)](https://github-community.service.justice.gov.uk/repository-standards/laa-spring-boot-ui-template)

### ⚠️ WORK IN PROGRESS ⚠️
This template is still under development and features may be added or subject to change.

## Overview

Template GitHub repository used for Spring Boot Java UI projects.

The project uses the `laa-spring-boot-gradle-plugin` Gradle plugin, which provides sensible defaults for the following plugins:

- [Checkstyle](https://docs.gradle.org/current/userguide/checkstyle_plugin.html)
- [Dependency Management](https://plugins.gradle.org/plugin/io.spring.dependency-management)
- [Jacoco](https://docs.gradle.org/current/userguide/jacoco_plugin.html)
- [Java](https://docs.gradle.org/current/userguide/java_plugin.html)
- [Spring Boot](https://plugins.gradle.org/plugin/org.springframework.boot)
- [Test Logger](https://github.com/radarsh/gradle-test-logger-plugin)
- [Versions](https://github.com/ben-manes/gradle-versions-plugin)

The plugin is provided by [laa-spring-boot-common](https://github.com/ministryofjustice/laa-spring-boot-common), where you can find more information regarding setup and usage.

This template targets:

- Spring Boot 4 via `laa-spring-boot-gradle-plugin`
- Java 25
- Single-module Gradle builds
- Spring MVC with Thymeleaf
- GOV.UK Frontend and MOJ Frontend assets served through WebJars

## Project Structure

This repository is a single-module application and includes:

- `src/main/java` - Spring Boot application, MVC configuration, controllers, and exception handling
- `src/main/resources` - application configuration, message bundles, static assets, and Thymeleaf templates
- `src/test/java` - unit and MVC slice tests
- `src/integrationTest/java` - integration tests using the plugin-managed `integrationTest` source set
- `.github/workflows` - PR and main-branch automation for build, test, and security scanning

## Setup Instructions

Once you have created your repository using this template, perform the following steps.

### Update README
Edit this `README.md` file to document your project accurately. Include the service purpose, deployment details, operational support information, and any domain-specific guidance.

### Update Repository Description
Change the repository description shown in GitHub so it clearly explains the application.

### Grant Team Permissions
Assign the appropriate Ministry of Justice teams. Ensure at least one team has Admin permissions.

### Add Branch Protection Rules
Protect the `main` branch with required checks and review rules.

### Update CODEOWNERS
Review `.github/CODEOWNERS` and update approvals if your team or service requires different ownership.

### Configure Dependabot
Review `.github/dependabot.yml`, uncomment the private package registry if needed, and update any package patterns that should be grouped differently for your application.

### Add Repository To Snyk
Add the repository to the [Legal Aid Agency Snyk](https://app.snyk.io/org/legal-aid-agency) organisation and configure `snyk_client_id` and `snyk_client_secret` as repository secrets for an OAuth 2.0 service account.

### Update Project Files
When turning this template into a real service, update:

- the Java package name under `src/main/java`, `src/test/java`, and `src/integrationTest/java`
- `src/main/resources/application.yml`
- `Dockerfile`
- `.github/workflows/*.yml`
- any displayed product or service copy in `messages.properties`

## Build And Run Application

### Build application
`./gradlew clean build`

### Run integration tests
`./gradlew integrationTest`

### Run application
`./gradlew bootRun`

### Run application via Docker
`docker compose up`

### Local Development Logging
When running with the `local` profile, structured logging is disabled for readable console output:

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

## Logging Configuration

This application uses **ECS (Elastic Common Schema) structured logging** for production environments and plain console logging for local development.

### Structured Logging (Default/Production)
By default, the application outputs logs in ECS JSON format so log processors can parse service metadata consistently.

```json
{
  "@timestamp": "2026-03-06T16:25:18.992904Z",
  "ecs": {
    "version": "8.11"
  },
  "log": {
    "level": "INFO",
    "logger": "uk.gov.justice.laa.springboot.ui.controller.HomeController"
  },
  "message": "Rendering home page",
  "process": {
    "pid": 49402,
    "thread": {
      "name": "http-nio-8082-exec-2"
    }
  },
  "service": {
    "environment": "local",
    "name": "laa-spring-boot-ui-template",
    "node": {
      "name": "unknown"
    },
    "version": "1.0.0"
  }
}
```

## Application Endpoints

### Application
- http://localhost:8082/
- http://localhost:8082/components

### Actuator
- http://localhost:8182/actuator/health

## Application Configuration

### Sentry
To integrate with Sentry, configure the following properties in `src/main/resources/application.yml`:

```yaml
sentry:
  dsn: <configure sentry dsn url here>
  environment: <configure environment name here>
```

## Available Starters

> Additional starters are available from [`laa-spring-boot-common`](https://github.com/ministryofjustice/laa-spring-boot-common) if you want to extend the template.

- [`laa-spring-boot-starter-cookie-consent`](https://github.com/ministryofjustice/laa-spring-boot-common)
- [`laa-spring-boot-starter-govuk-dialect`](https://github.com/ministryofjustice/laa-spring-boot-common)
