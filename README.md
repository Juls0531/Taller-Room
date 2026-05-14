# SalesApp — Aplicación de Ventas Android


  el ViewModel la consume y expone StateFlow inmutables a la UI.
- **Coroutines:** Todas las operaciones de BD se ejecutan en `viewModelScope`
  para no bloquear el hilo principal.
- **Flow reactivo:** La UI se actualiza automáticamente cuando Room detecta
  cambios en las tablas; no hay polling manual.
