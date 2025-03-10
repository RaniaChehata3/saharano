# MVC Architecture Refactoring

This document outlines the refactoring of the AtlantaFX Sampler application to follow the Model-View-Controller (MVC) architectural pattern.

## Overview

The MVC pattern separates an application into three main components:

1. **Model**: Manages data, logic, and rules of the application
2. **View**: Displays data to the user and handles user interface
3. **Controller**: Processes incoming requests, manipulates data using the Model, and interacts with Views

## Project Structure

The refactored project structure follows this pattern:

```
sampler/src/main/java/atlantafx/sampler/
├── controller/
│   ├── NavigationController.java
│   ├── SearchController.java
│   └── ThemeController.java
├── model/
│   └── ApplicationModel.java
├── view/
│   ├── MainView.java
│   └── SearchView.java
├── MVCLauncher.java
└── ... (existing files)
```

## Components

### Model

- **ApplicationModel**: Singleton class that holds the core application state, including the selected page, current view layer, and navigation tree.

### Controllers

- **NavigationController**: Handles navigation between pages and view layers.
- **SearchController**: Manages search functionality for finding pages.
- **ThemeController**: Controls theme selection and application.

### Views

- **MainView**: The main application view that displays content based on the selected page and view layer.
- **SearchView**: UI component for searching pages.

## How It Works

1. The `MVCLauncher` initializes the application and sets up the main components.
2. The `ApplicationModel` stores the application state.
3. Controllers handle user actions and update the model accordingly.
4. Views observe changes in the model and update the UI.

## Benefits of MVC

- **Separation of Concerns**: Each component has a specific responsibility.
- **Maintainability**: Easier to maintain and extend the codebase.
- **Testability**: Components can be tested in isolation.
- **Reusability**: Components can be reused in different parts of the application.

## Implementation Notes

- The original application had some MVC-like structure but with mixed responsibilities.
- The refactoring creates a clearer separation between data, logic, and presentation.
- Controllers act as intermediaries between views and models.
- Views observe models for changes and update accordingly.

## Usage

To use the MVC architecture, run the application using the `MVCLauncher` class instead of the original `Launcher` class.

```java
java -cp <classpath> atlantafx.sampler.MVCLauncher
```

## Future Improvements

- Further refine the separation of concerns.
- Add more specialized controllers for different features.
- Implement a more robust event system for communication between components.
- Create more view components for different parts of the UI. 