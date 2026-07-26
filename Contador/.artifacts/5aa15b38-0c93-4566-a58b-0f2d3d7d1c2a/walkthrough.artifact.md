# Walkthrough: Olimpo Adaptativo y la Ira de Hera

La aplicación ha sido mejorada significativamente para ser robusta, informativa y visualmente impecable en cualquier orientación.

## Nuevas Funcionalidades

### 1. Diseño Adaptativo (Responsive)
- **Modo Vertical**: Diseño optimizado para una sola mano, con el contador centrado y la consola de logs al final.
- **Modo Horizontal**: El diseño se divide en dos columnas. A la izquierda, el título y los controles; a la derecha, el contador y la consola de logs. ¡Adiós a los elementos cortados!

### 2. La Ira de Hera (Reinicio)
- Se ha añadido un botón temático **"IRA DE HERA (RESET)"**.
- Al pulsarlo, el contador vuelve a 0 y aparece el mensaje oscuro: **"Hera los mató a todos 💀"**.

### 3. Consola de Logs Integrada
- Ya no necesitas Logcat para ver el ciclo de vida.
- La aplicación incluye una **sección de logs** en la parte inferior (o a la derecha en horizontal) que muestra cada evento (`onCreate`, `onResume`, etc.) con su marca de tiempo en tiempo real.

## Cómo probar los logs del Ciclo de Vida
1. Abre la app: Verás `onCreate`, `onStart`, `onResume`.
2. Gira el celular: Verás la secuencia de destrucción (`onPause`, `onStop`, `onDestroy`) seguida de la recreación (`onCreate`, `onStart`, `onResume`).
3. Minimiza la app: Verás `onPause` y `onStop`.
4. Vuelve a entrar: Verás `onRestart`, `onStart` y `onResume`.

> [!IMPORTANT]
> **Persistencia en el Olimpo**: A pesar de que la actividad se destruye al rotar, el contador de hijos y el mensaje de Hera se mantienen intactos gracias a `rememberSaveable`.

> [!TIP]
> La consola de logs tiene scroll automático, por lo que siempre verás el evento más reciente al final.
