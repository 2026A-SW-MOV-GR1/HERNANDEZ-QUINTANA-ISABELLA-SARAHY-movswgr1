# Fase A: Análisis y Selección - Disney+

## 1. Definición de Mercado
**Audiencia**: Público masivo intergeneracional (de 4 a 80+ años).
- **Intereses**: Nostalgia (Clásicos Disney), Acción/Sci-Fi (Marvel/Star Wars), Educación (NatGeo) y contenido familiar.
- **Nivel Socioeconómico**: Medio-Alto (requiere suscripción y conexión a internet estable).
- **Uso**: Consumo intensivo en dispositivos móviles, tablets y TVs.

## 2. Psicología del Color y Marca
Disney+ utiliza una paleta de colores "Cinemática" y "Premium":
- **Azul Espacial Profundo (#1A1D29)**: Utilizado como fondo principal. Transmite estabilidad, confianza y profundidad (como una sala de cine a oscuras). Reduce la fatiga visual y resalta los colores de los posters.
- **Azul Resplandor (Glow Blue #0072D2)**: Usado para acentos y estados activos. Evoca magia y tecnología.
- **Blanco/Plata (#F9F9F9)**: Para tipografía de alta legibilidad y contraste.

## 3. Auditoría de Componentes (Listas)
Se implementarán los siguientes 3 tipos de elementos iterables:
1. **Hero Banner Carousel**: Lista horizontal de pantalla completa (Featured Content).
2. **Brand Row**: Lista de tarjetas con degradados y logotipos (Disney, Pixar, Marvel, etc.).
3. **Movie Horizontal Rows**: Listas infinitas de posters verticales (Trending, Recommended, Continue Watching).

## 4. Análisis Crítico y Propuesta de Mejora
- **Falla Identificada**: La navegación principal en la app original a veces oculta el contenido debajo de la barra de navegación en scrolls rápidos, y la falta de retroalimentación táctil clara en algunas tarjetas de marcas.
- **Propuesta**: Implementar micro-interacciones de escalado (1.05x) al presionar cualquier tarjeta y un degradado dinámico en la barra superior que cambie sutilmente según el contenido del Hero Banner.
