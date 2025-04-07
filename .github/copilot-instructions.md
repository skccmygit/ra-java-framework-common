# Copilot Guidelines for the SKCC AIKMS Project

## Overview
This document provides guidelines on how to effectively use GitHub Copilot in the SKCC AIKMS project. The project consists of the `ra-java-framework-common` repository, which includes backend services and configuration files for common management.

## Directory Structure

## ra-java-framework-common
A backend project based on Spring Boot, responsible for common management functionalities such as menu management, API management, and utility services.

```
ra-java-framework-common/
├── build/                      # Build-related files and directories
├── common-export/              # Common utilities and shared components
│   ├── src/                    # Source code directory
│       ├── main/               # Main application code
│           ├── java/           # Java source code
│               ├── com/
│                   ├── skcc/
│                       ├── ra/
│                           ├── common/ # Common logic
│                               ├── api/          # API-related classes
│                               ├── config/       # Configuration classes
│                               ├── domain/       # JPA entity classes
│                               ├── dto/          # Data Transfer Objects (DTOs)
│                               ├── repository/   # JPA repository interfaces
│                               ├── service/      # Service layer
│                               ├── util/         # Utility classes
│                               ├── trace/        # Tracing utilities
│       ├── test/               # Test code
│           ├── java/           # Java test code
│               ├── com/
│                   ├── skcc/
│                       ├── ra/
│                           ├── common/ # Common logic tests
├── common-service/             # Main service implementation
│   ├── src/                    # Source code directory
│       ├── main/               # Main application code
│           ├── java/           # Java source code
│               ├── com/
│                   ├── skcc/
│                       ├── ra/
│                           ├── common/ # Common logic
│                               ├── api/          # API-related classes
│                               ├── config/       # Configuration classes
│                               ├── domain/       # JPA entity classes
│                               ├── dto/          # Data Transfer Objects (DTOs)
│                               ├── repository/   # JPA repository interfaces
│                               ├── service/      # Service layer
│                               ├── util/         # Utility classes
│                               ├── trace/        # Tracing utilities
│       ├── test/               # Test code
│           ├── java/           # Java test code
│               ├── com/
│                   ├── skcc/
│                       ├── ra/
│                           ├── common/ # Common logic tests
├── docs/                       # Documentation directory
│   ├── db/                     # Database-related documentation
│   ├── imgs/                   # Image assets
├── gradle/                     # Gradle-related directory
│   ├── wrapper/                # Gradle Wrapper settings
```

---

# Technical Elements of the SKCC AIKMS Project

## ra-java-framework-common (Backend)
A backend project based on Spring Boot, responsible for common management functionalities such as menu management, API management, and utility services.

### Key Technical Elements
1. **Programming Languages and Frameworks**
   - Java
   - Spring Boot (REST API, JPA, Spring Data)

2. **Database**
   - PostgreSQL (SQL scripts for schema and data management)

3. **Build and Dependency Management**
   - Gradle (`build.gradle`, `settings.gradle`)

4. **Deployment and Containerization**
   - Docker (`Dockerfile`, `docker-compose.yml`)

5. **Testing**
   - JUnit (Java testing framework)

6. **Security**
   - Passwords and connection information are managed using Kubernetes Secrets and ConfigMaps.

7. **Others**
   - YAML configuration files (`application.yml`)
   - Lombok (code simplification)

---

## Common Technical Elements
1. **Version Control**
   - Git (GitHub repository)

2. **CI/CD**
   - GitHub Actions (CI/CD pipeline)

3. **Documentation**
   - Markdown (`README.md`, `INSTALL.md`, `CHANGELOG.md`)

4. **Security**
   - Passwords and connection information are managed using Kubernetes Secrets and ConfigMaps.

# SKCC AIKMS Project Naming Rules

Below are the naming rules used in the SKCC AIKMS project. These rules are defined to improve code readability, maintain consistency, and facilitate collaboration.

---

## **1. Java (Backend)**
### **1.1 Class Names**
- **Rule**: Use PascalCase
- **Description**: Class names should be clear, specific, and indicate their role.
- **Examples**:
  - `GuideServiceImpl`
  - `GuideCategoryDto`
  - `GuideRepository`

### **1.2 Method Names**
- **Rule**: Use camelCase
- **Description**: Method names should start with a verb and clearly indicate the action performed.
- **Examples**:
  - `findGuideById`
  - `updateGuideCategory`
  - `deleteGuide`

### **1.3 Variable Names**
- **Rule**: Use camelCase
- **Description**: Variable names should be clear, concise, and indicate the meaning of the data.
- **Examples**:
  - `guideId`
  - `categoryName`
  - `isActive`

### **1.4 Package Names**
- **Rule**: Use lowercase, separated by dots (.)
- **Description**: Package names should reflect the project structure and be hierarchical.
- **Examples**:
  - `com.skcc.tools.controller`
  - `com.skcc.tools.service`
  - `com.skcc.tools.domain`

---

## **2. TypeScript (Frontend)**
### **2.1 File Names**
- **Rule**: 
  - Use camelCase for utility/helper files
  - Use PascalCase for component files
  - Use `index.tsx` for main component files
- **Examples**:
  - `prepareFilters.ts` (utility)
  - `Section.tsx` (component)
  - `index.tsx` (main component)

### **2.2 Component Names**
- **Rule**: Use PascalCase
- **Description**: Component names should be clear, specific, and indicate UI elements.
- **Examples**:
  - `UserProfile`
  - `GuideList`
  - `NavigationBar`

### **2.3 Variable and Function Names**
- **Rule**: Use camelCase
- **Description**: Variable and function names should be clear, concise, and indicate their role.
- **Examples**:
  - `fetchUserData`
  - `isGuideActive`
  - `handleButtonClick`

### **2.4 Style Class Names**
- **Rule**: Use kebab-case
- **Description**: CSS class names should be written in lowercase, with words separated by hyphens (-).
- **Examples**:
  - `user-profile`
  - `navigation-bar`
  - `active-button`

- ### 2.5 Interface Names
- **Rule**: Use PascalCase with 'I' prefix or no prefix
- **Description**: Interface names should be clear and follow TypeScript conventions
- **Examples**:
  - `Props`
  - `GuideApiResponse` 
  - `StateContent`

### 2.6 Type Names  
- **Rule**: Use PascalCase
- **Description**: Type names should be descriptive and follow TypeScript conventions
- **Examples**:
  - `FilterCategory`
  - `SortDirection`
  - `ViewMode`

### 2.7 Constant Values
- **Rule**: Use UPPER_SNAKE_CASE
- **Description**: Constants should be all caps with underscore separators
- **Examples**:
  - `EXPLORE_PATH`
  - `DEFAULT_CLASSIFY`
  - `SORT_DIRECTION_PARAM`

---

## **3. YAML Files**
### **3.1 Key Names**
- **Rule**: Use snake_case
- **Description**: YAML keys should clearly indicate the meaning of the data and be written in lowercase.
- **Examples**:
  - `summary_business_use_case`
  - `guide_category`
  - `api_endpoint`

### **3.2 File Names**
- **Rule**: Use kebab-case
- **Description**: File names should indicate the role of the data and be written in lowercase.
- **Examples**:
  - `data.yml`
  - `guide.yml`
  - `settings.yml`

---

## **4. Rust (CLI and Core Libraries)**
### **4.1 Module and File Names**
- **Rule**: Use snake_case
- **Description**: Module and file names should be written in lowercase, with words separated by underscores (_).
- **Examples**:
  - `settings.rs`
  - `games.rs`
  - `data_parser.rs`

### **4.2 Struct and Enum Names**
- **Rule**: Use PascalCase
- **Description**: Struct and enum names should be clear and specific.
- **Examples**:
  - `LandscapeGames`
  - `FeaturedItemRule`
  - `EndUserRule`

### **4.3 Function and Variable Names**
- **Rule**: Use snake_case
- **Description**: Function and variable names should be written in lowercase, with words separated by underscores (_).
- **Examples**:
  - `validate_quiz`
  - `new_from_yaml`
  - `is_valid_game`

---

## **5. Common Rules**
### **5.1 Constant Names**
- **Rule**: Use UPPER_SNAKE_CASE
- **Description**: Constant names should be written in uppercase, with words separated by underscores (_).
- **Examples**:
  - `MAX_RETRY_COUNT`
  - `DEFAULT_TIMEOUT`
  - `API_BASE_URL`

### **5.2 Test File Names**
- **Rule**: Add `.test` or `_test` as a suffix to the file name
- **Description**: Test files should use the same name as the target file, with a suffix added.
- **Examples**:
  - `user-profile.test.tsx`
  - `guide_service_test.java`
  - `games_test.rs`

---

# Google Style Guides and SKCC AIKMS Rules Integration

## [JavaScript Style Guide](https://google.github.io/styleguide/jsguide.html)
- **Indentation**: Use 2 spaces.
- **Line Length**: Limit lines to 80 characters.
- **Braces**: Always use braces, with the opening brace on the same line.
- **Semicolons**: End every statement with a semicolon.
- **Naming Conventions**: Use camelCase for variables and functions, and PascalCase for classes.
- **Modularization**: Write one main class per file and use `import` and `export` for modules.

## [Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- **Class Names**: Use PascalCase.
- **Method Names**: Use camelCase, starting with a verb.
- **Variable Names**: Use camelCase.
- **Indentation**: Use 2 or 4 spaces.
- **Comments**: Use Javadoc style for methods and classes.
- **Code Organization**: Arrange methods and fields in logical order.

## [JSON Style Guide](https://google.github.io/styleguide/jsoncstyleguide.xml)
- **Key Names**: Use snake_case.
- **Values**: Always use double quotes for strings.
- **Indentation**: Use 2 spaces.
- **Sorting**: Sort keys alphabetically.
- **Comments**: Since JSON does not support comments, manage them in separate documentation.

## [Markdown Style Guide](https://google.github.io/styleguide/docguide/style.html)
- **Headers**: Use `#` and keep titles concise.
- **Lists**: Use `-` for unordered lists.
- **Code Blocks**: Wrap code blocks with backticks (```).
- **Links**: Use clear and descriptive text for links.
- **Images**: Include alt text for images.

## [TypeScript Style Guide](https://google.github.io/styleguide/tsguide.html)
- **Type Definitions**: Write explicit types and use interfaces and types effectively.
- **Naming Conventions**: Use camelCase for variables and functions, and PascalCase for classes.
- **Modules**: Use ES6 modules with `import` and `export`.
- **Comments**: Use JSDoc style for functions and classes.
- **Strict Mode**: Enable `strict` mode for type safety.

## [HTML/CSS Style Guide](https://google.github.io/styleguide/htmlcssguide.htm)
- **HTML**:
  - Use lowercase for tags.
  - Enclose attributes in double quotes.
  - Use 2 spaces for indentation.
  - Follow HTML5 standards.
- **CSS**:
  - Use kebab-case for class names.
  - Place braces on the same line.
  - Sort properties alphabetically.
  - For SCSS, use nested rules appropriately.

# SKCC AIKMS Rules Integration
- **JavaScript**: Follow Google Style Guide and automate code style with ESLint and Prettier.
- **Java**: Base on Google Style Guide while adhering to Spring Boot conventions.
- **JSON**: Follow Google JSON Style Guide and manage environment variables with Kubernetes ConfigMap.
- **Markdown**: Follow Google Markdown Style Guide and reflect project structure in links.
- **TypeScript**: Follow Google TypeScript Style Guide and consider integration with Solid.js.
- **HTML/CSS**: Follow Google HTML/CSS Style Guide and adhere to Vite and SCSS build processes.

# Reusable Assets
If a DB connection is required in a project whose code project name starts with `ra-`, it is implemented so that it can be executed without a DBMS by using H2 DB.
