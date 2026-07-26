# Local Resource Integration for Disney+ Clone

This plan updates the application to prioritize local images stored within the project, while keeping network URLs as a fallback.

## User Review Required

> [!IMPORTANT]
> You must manually copy your image files to the `drawable` folder after I finish updating the code. If the files are not there, the app might show placeholders or fall back to the old URLs.

## Proposed Changes

### [Component] Data Models

#### [MODIFY] [Models.kt](file:///C:/Users/ionly/AndroidStudioProjects/AplicacionDisney/shared/src/commonMain/kotlin/com/example/aplicaciondisney/Models.kt)
- Import `org.jetbrains.compose.resources.DrawableResource`.
- Add `localImage` and `localBackdrop` fields to the `Movie` class.
- Update `sampleMovies` to use resource references (e.g., `Res.drawable.percy_poster`) once the user adds them.

### [Component] UI Logic

#### [MODIFY] [DisneyHome.kt](file:///C:/Users/ionly/AndroidStudioProjects/AplicacionDisney/shared/src/commonMain/kotlin/com/example/aplicaciondisney/DisneyHome.kt)
- Implement a helper composable or logic inside `MovieCard` and `HeroBanner` to check if a local resource is available.
- Use `painterResource(Res.drawable.filename)` for local images and `AsyncImage` for remote ones.

#### [MODIFY] [MovieDetailScreen.kt](file:///C:/Users/ionly/AndroidStudioProjects/AplicacionDisney/shared/src/commonMain/kotlin/com/example/aplicaciondisney/MovieDetailScreen.kt)
- Update the backdrop display logic to prioritize local resources.

## Instructions for the User

### 1. Where to put the images
Copy your files to:
`C:/Users/ionly/AndroidStudioProjects/AplicacionDisney/shared/src/commonMain/composeResources/drawable/`

### 2. Naming Rules
- Use only **lowercase letters**, **numbers**, and **underscores**.
- **NO** spaces, **NO** capital letters, **NO** special characters (except `_`).
- Example: `percy_jackson.jpg`, `marvel_logo.png`.

## Verification Plan

### Manual Verification
1.  **Code Check**: Ensure the project compiles after model changes.
2.  **Resource Check**: After the user adds a file (e.g., `test.png`), update `Models.kt` to reference `Res.drawable.test` and verify it renders on the device.
