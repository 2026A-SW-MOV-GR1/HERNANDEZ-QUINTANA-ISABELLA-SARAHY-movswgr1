# Implementación de Diseño Adaptativo y Registro de Eventos: "La Ira de Hera"

Este plan detalla las mejoras para optimizar el diseño en horizontal, añadir un botón de reinicio temático y mostrar los logs del ciclo de vida directamente en la interfaz.

## User Review Required

> [!IMPORTANT]
> - **Logs en Pantalla**: Los eventos del ciclo de vida capturados en `MainActivity` se enviarán a la UI de Compose a través de un mecanismo de estado compartido para que el estudiante pueda verlos sin abrir Logcat.
> - **Gag de Hera**: El botón de reinicio tendrá un tono oscuro, representando la "limpieza" que Hera hace de la prole de Zeus.

## Proposed Changes

### [Component] Gestión de Logs y Estado (Android & Shared)

#### [MODIFY] [MainActivity.kt](file:///C:/Users/ionly/AndroidStudioProjects/Contador/androidApp/src/main/kotlin/com/example/contador/MainActivity.kt)
- Implementar un mecanismo para pasar los logs a la UI (ej. una lista observable).
- Cada override del ciclo de vida añadirá un mensaje a esta lista.

#### [MODIFY] [App.kt](file:///C:/Users/ionly/AndroidStudioProjects/Contador/shared/src/commonMain/kotlin/com/example/contador/App.kt)
- Añadir un parámetro `lifecycleLogs: List<String>` a la función `App`.
- Implementar el botón "Ira de Hera" para reiniciar el contador.
- Mostrar el mensaje "Hera los mató a todos" dinámicamente.

### [Component] Interfaz Adaptativa (Compose)

#### [MODIFY] [App.kt](file:///C:/Users/ionly/AndroidStudioProjects/Contador/shared/src/commonMain/kotlin/com/example/contador/App.kt)
- **Layout Adaptativo**: Uso de `BoxWithConstraints`.
    - **Vertical**: Pregunta -> Contador -> Botones -> Consola de Logs (abajo).
    - **Horizontal**: Izquierda (Pregunta + Botones) / Derecha (Contador + Consola de Logs).
- **Consola de Logs**: Sección con scroll al final de la pantalla que muestra los últimos eventos capturados.

## Verification Plan

### Manual Verification
- **Reinicio**: Pulsar el botón de Hera, verificar que el contador sea 0 y que aparezca el mensaje.
- **Logs en Vivo**: Realizar acciones (minimizar, rotar) y verificar que la lista de logs en la app se actualice automáticamente.
- **Adaptabilidad**: Rotar el dispositivo y confirmar que la consola de logs y el contador se reposicionan correctamente sin solaparse.
