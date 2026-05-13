# SalesApp — Aplicación de Ventas Android

## Tecnologías
- **Lenguaje:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **Base de datos:** Room (SQLite)
- **Arquitectura:** MVVM
- **Navegación:** Navigation Compose
- **Concurrencia:** Coroutines + Flow

---

## Estructura del proyecto

```
app/src/main/java/com/example/salesapp/
├── MainActivity.kt                        ← Punto de entrada
├── data/
│   ├── entities/
│   │   ├── Articulo.kt                    ← Entidad Room
│   │   ├── Venta.kt                       ← Entidad Room (FK → Articulo)
│   │   └── VentaConArticulo.kt            ← Modelo JOIN para consultas
│   ├── dao/
│   │   ├── ArticuloDao.kt                 ← Operaciones SQL sobre artículos
│   │   └── VentaDao.kt                    ← Operaciones SQL sobre ventas
│   ├── database/
│   │   └── SalesDatabase.kt              ← Singleton de la BD Room
│   └── repository/
│       └── SalesRepository.kt            ← Fuente única de verdad (SSOT)
├── viewmodel/
│   └── SalesViewModel.kt                 ← Lógica de negocio + estado UI
└── ui/
    ├── theme/
    │   └── Theme.kt                       ← Colores y tema Material 3
    ├── navigation/
    │   ├── Screen.kt                      ← Rutas selladas
    │   └── SalesNavGraph.kt              ← Grafo de navegación
    └── screens/
        ├── HomeScreen.kt                  ← Menú principal (3 botones)
        ├── RegistroArticuloScreen.kt      ← Formulario de artículos
        ├── RegistroVentaScreen.kt         ← Formulario de ventas
        └── ConsultaVentasScreen.kt        ← Listado y consulta por grupo
```

---

## Cómo importar en Android Studio

1. Abre Android Studio → **File → New → Import Project**
2. Selecciona la carpeta **SalesApp**
3. Espera a que Gradle sincronice las dependencias
4. Ejecuta en un emulador o dispositivo físico (API 26+)

---

## Flujo de prueba recomendado

### 1. Registrar artículos
1. Toca **"Registro de Artículos"** en el menú principal
2. Ingresa los siguientes artículos de prueba:

| Código | Nombre       | Descripción          | Precio  |
|--------|--------------|----------------------|---------|
| 1001   | Laptop Dell  | 15" 8GB RAM 512 SSD  | 2500000 |
| 1002   | Mouse Genius | Inalámbrico 1200 DPI | 45000   |
| 1003   | Teclado HK   | Mecánico RGB USB-C   | 120000  |

3. Toca **"Guardar Artículo"** después de cada uno
4. Verás la confirmación en el Snackbar

### 2. Registrar ventas
1. Toca **"Registro de Ventas"** en el menú principal
2. Selecciona un artículo del dropdown
3. Ingresa grupo y cantidad. Prueba con:

| Artículo    | Grupo | Cantidad |
|-------------|-------|----------|
| Laptop Dell | 1     | 2        |
| Mouse Genius| 1     | 5        |
| Teclado HK  | 1     | 3        |
| Laptop Dell | 2     | 1        |
| Mouse Genius| 2     | 10       |

4. Toca **"Guardar Venta"** en cada registro

### 3. Consultar ventas
1. Toca **"Consulta de Ventas"** en el menú principal
2. Selecciona el **Grupo 1** del dropdown
3. Toca **"Buscar"**
4. Verás la lista de artículos con cantidad y valor
5. Toca cualquier artículo para ver el **AlertDialog** con detalles
6. Observa el **Total del Grupo** al fondo de la pantalla

---

## Arquitectura MVVM — Flujo de datos

```
UI (Compose) 
    ↓ eventos (onClick)
ViewModel (StateFlow, SaveResult)
    ↓ suspend functions
Repository (abstracción de datos)
    ↓ DAO calls
Room Database (SQLite)
    ↑ Flow<List<T>> (reactivo)
```

Los datos fluyen hacia arriba de forma reactiva gracias a `Flow` + `StateFlow`.
La UI nunca llama directamente a Room; siempre pasa por el ViewModel.

---

## Notas de arquitectura

- **State hoisting:** Cada pantalla eleva su estado local al Composable raíz;
  los callbacks se pasan hacia abajo (sin acoplar hijos con el ViewModel).
- **Single source of truth:** El repositorio es la única fuente de verdad;
  el ViewModel la consume y expone StateFlow inmutables a la UI.
- **Coroutines:** Todas las operaciones de BD se ejecutan en `viewModelScope`
  para no bloquear el hilo principal.
- **Flow reactivo:** La UI se actualiza automáticamente cuando Room detecta
  cambios en las tablas; no hay polling manual.
