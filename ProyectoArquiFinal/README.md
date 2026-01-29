# Sistema de Transmisión Hamming con Simulación de Ruido

##  Descripción General

Sistema interactivo en Java que simula la transmisión de datos a través de un canal ruidoso. Implementa el **Código de Hamming (7,4) y (11,7)** para detectar y corregir automáticamente errores de transmisión.

**Objetivo**: Demostrar cómo se pueden detectar y corregir errores en comunicaciones de datos cuando están expuestos a ruido en el canal.

---

##  ARQUITECTURA DEL SISTEMA

### Flujo General de Transmisión

```
ENTRADA (Texto/Archivo)
    ↓
CONVERSIÓN A BINARIO (BinaryConverter)
    ↓
DIVISIÓN EN BLOQUES (4 bits o 7 bits)
    ↓
CODIFICACIÓN HAMMING (inserción bits de paridad)
    ↓
CANAL DE RUIDO (simulación de errores)
    ↓
DECODIFICACIÓN HAMMING (detección y corrección)
    ↓
EXTRACCIÓN DE DATOS
    ↓
SALIDA + ESTADÍSTICAS
```

---

##  CLASES Y SUS FUNCIONES DETALLADAS

### 1. **Main.java** - Interfaz Interactiva del Usuario

**Propósito**: Coordina todo el flujo del programa y maneja la interacción con el usuario.

**Funciones principales**:

#### `main(String[] args)`
- **Qué hace**: Función principal que controla todo el programa
- **Pasos**:
  1. Muestra menú para seleccionar tipo de entrada (texto/archivo)
  2. Solicita probabilidad de error del canal
  3. Selecciona tipo de Hamming (7,4 o 11,7)
  4. Coordina: conversión → codificación → ruido → decodificación
  5. Muestra resultados y estadísticas

**Variables importantes**:
- `errorProbability`: Probabilidad de que cada bit sea alterado (0.0 a 1.0)
- `inputType`: Tipo de entrada seleccionada (1=texto, 2=imagen, 3=audio)
- `hammingType`: Tipo de codificación (1=Hamming(7,4), 2=Hamming(11,7))

---

### 2. **BinaryConverter.java** - Conversión de Datos a Bloques

**Propósito**: Convertir texto/archivos a binario y dividir en bloques procesables.

**Funciones principales**:

#### `convertTextToBlocks(String text, int blockSize)`
- **Qué hace**: Convierte un texto a bloques de bits
- **Proceso**:
  1. Toma cada carácter del texto
  2. Convierte a código ASCII (8 bits)
  3. Divide en bloques de 4 o 7 bits según blockSize
- **Ejemplo**:
  ```
  Entrada: "AB"
  ASCII: A=65 (01000001), B=66 (01000010)
  Binario: 0100000101000010
  Bloques de 4 bits: [0100, 0001, 0100, 0010]
  ```

#### `convertBytesToBlocks(byte[] data, int blockSize)`
- **Qué hace**: Convierte bytes de archivo a bloques binarios
- **Útil para**: Procesar imágenes y audio como secuencias de bytes

#### `convertBlocksToText(List<int[]> blocks)`
- **Qué hace**: Reconstruye texto desde bloques de bits
- **Proceso inverso**:
  ```
  Bloques: [0100, 0001, 0100, 0010]
  Binario agrupado en bytes: 01000001 01000010
  ASCII: A B
  Texto recuperado: "AB"
  ```

#### `convertBlocksToBytes(List<int[]> blocks)`
- **Qué hace**: Reconstruye datos binarios a bytes originales

---

### 3. **Hamming.java** - Codificación Hamming

**Propósito**: Insertar bits de paridad para detectar y corregir errores.

**Funciones principales**:

#### `encode(List<int[]> BloquesDeDatos)`
- **Qué hace**: Codifica cada bloque de datos con bits de paridad
- **Recorre**: Todos los bloques de 4 o 7 bits
- **Devuelve**: Bloques codificados de 7 o 11 bits respectivamente

#### `InsertarDatos(int[] data)` (privada)
- **Qué hace**: Prepara un bloque individual para insertar paridad
- **Proceso**:
  1. Determina si es Hamming(7,4) o (11,7)
  2. Inserta datos en posiciones NO potencia de 2
  3. Pone 0 en posiciones potencia de 2 (reservadas para paridad)
  4. Llama a Paridad.calcularParidad() para llenar los huecos

**Ejemplo para Hamming(7,4)**:
```
Datos originales: 1, 0, 1, 0
Posiciones:       1  2  3  4  5  6  7
Después inserción: p  p  1  p  0  1  0
Después paridad:  p1 p2 1 p4 0  1  0  (p1,p2,p4 calculados)
```

---

### 4. **Paridad.java** - Cálculo de Bits de Paridad

**Propósito**: Calcular los bits de paridad mediante XOR.

**Función principal**:

#### `calcularParidad(int[] bloque)` (estática)
- **Qué hace**: Llena los bits de paridad usando la operación XOR
- **Cálculo XOR**: XOR de varios bits es 1 si hay número impar de 1s

**Para Hamming(7,4)**:
```
p1 = XOR de posiciones 1,3,5,7 (binaria de posición: ...001)
p2 = XOR de posiciones 2,3,6,7 (binaria de posición: ...010)
p4 = XOR de posiciones 4,5,6,7 (binaria de posición: ...100)
```

**¿Por qué?**: Cada bit de paridad en posición 2^n cubre todas las posiciones que tienen ese bit en su representación binaria.

---

### 5. **NoiseChannel.java** - Simulación del Ruido

**Propósito**: Simula errores de transmisión alterando bits aleatoriamente.

**Función principal**:

#### `NoiseChannel(double errorProbability)`
- **Constructor**: Inicializa con la probabilidad de error
- **Parámetro**: errorProbability (0.0 = sin error, 1.0 = todo error)

#### `transmitAll(List<int[]> encodedBlocks)`
- **Qué hace**: Aplica ruido a todos los bloques
- **Devuelve**: Bloques con posibles errores introducidos

#### `transmit(int[] block)` (privada)
- **Qué hace**: Aplica ruido a UN bloque individual
- **Proceso**:
  1. Para cada bit del bloque
  2. Genera número aleatorio entre 0 y 1
  3. Si número < errorProbability, invierte el bit (bit flip)
  4. Cuenta bits alterados

**Ejemplo**:
```
errorProbability = 0.05 (5%)
Para cada bit:
  - Si random() < 0.05 → alterar bit
  - Si random() >= 0.05 → mantener bit

Bloque original:  1010010
Número aleatorio: 0.03 0.12 0.02 0.15 0.04 0.11 0.09
Comparación:      < 0.05 >= >= >= < 0.05 >= >=
Bloque ruidoso:   0010110 (bits 0 y 4 alterados)
```

---

### 6. **RandomNoiseGenerator.java** - Generador de Aleatorios

**Propósito**: Generar números aleatorios para la simulación de ruido.

**Función principal**:

#### `shouldFlip(double probability)`
- **Qué hace**: Decide si debe haber error para este bit
- **Lógica**: Compara un número aleatorio con la probabilidad
- **Retorna**: true si debe haber error, false si no

**Fórmula**:
```java
return random.nextDouble() < probability;
```

---

### 7. **FileHandler.java** - Manejo de Archivos

**Propósito**: Leer archivos (imágenes, audio) y convertirlos en bytes para procesamiento.

**Funciones principales**:

#### `readFile(String filePath)` (estática)
- **Qué hace**: Lee un archivo completo y devuelve su contenido como array de bytes
- **Parámetros**:
  - `filePath`: Ruta completa del archivo (ej: `C:\Users\usuario\Imágenes\foto.png`)
- **Proceso**:
  1. Verifica que el archivo existe
  2. Verifica que NO es una carpeta
  3. Verifica que no supera 10 MB
  4. Lee todos los bytes del archivo
  5. Retorna el array de bytes

**Ejemplo**:
```
Entrada: "C:\Users\usuario\Imágenes\foto.jpg"
Lectura: Lee todos los bytes del archivo JPG
Salida: byte[] con 45325 bytes (tamaño del archivo)
```

#### `writeFile(String filePath, byte[] data)` (estática)
- **Qué hace**: Escribe bytes a un archivo (para guardar resultados)
- **Parámetros**:
  - `filePath`: Ruta destino del archivo
  - `data`: Array de bytes a escribir
- **Retorna**: true si fue exitoso, false si hubo error

#### `getFileInfo(String filePath)` (estática)
- **Qué hace**: Obtiene información sobre un archivo
- **Información devuelta**:
  - Nombre del archivo
  - Tamaño en bytes
  - Tipo de archivo

**Ejemplo**:
```
Entrada: "C:\Users\usuario\Imágenes\foto.png"
Salida: "Archivo: foto.png, Tamaño: 45325 bytes, Tipo: Imagen (PNG)"
```

#### `getFileType(String filePath)` (estática)
- **Qué hace**: Determina el tipo de archivo según su extensión
- **Identifica**:
  - Imágenes: PNG, JPG, JPEG, BMP, GIF, TIFF
  - Audio: MP3, WAV, FLAC, AAC, OGG, M4A
  - Video: MP4, AVI, MOV, FLV, WMV, WEBM
  - Documentos: TXT, PDF, DOC, DOCX, JSON, XML
- **Retorna**: String con el tipo de archivo

**Ejemplo**:
```
Entrada: "archivo.jpg"
Salida: "Imagen (JPG)"

Entrada: "cancion.mp3"
Salida: "Audio (MP3)"

Entrada: "documento.pdf"
Salida: "Documento (PDF)"
```

**Limitaciones y Validaciones**:
- Máximo tamaño: 10 MB (10,485,760 bytes)
- Si es carpeta: Error
- Si no existe: Error
- Tipos soportados: Cualquiera (se identifican por extensión)

**¿Por qué es importante?**
- Permite procesar archivos reales (fotos, audios)
- Valida que el archivo sea accesible
- Controla el tamaño para evitar problemas de memoria
- Identifica el tipo para mejor procesamiento

---

### 8. **ErrorCorrector.java** - Detección y Corrección

**Propósito**: Detecta la posición del error y lo corrige.

**Funciones principales**:

#### `detectError(int[] block)`
- **Qué hace**: Calcula el síndrome para encontrar la posición del error
- **Proceso**:
  1. Recalcula los bits de paridad recibidos
  2. Compara con los valores esperados
  3. Las diferencias forman el síndrome (posición del error)

**Ejemplo detallado**:
```
Bloque recibido: 1010110
Recalcular paridades:
  p1_recalc = XOR(1,1,1,0) = 1
  p2_recalc = XOR(0,1,1,0) = 0
  p4_recalc = XOR(0,1,1,0) = 0

Síndrome = p1_recalc + 2*p2_recalc + 4*p4_recalc = 1 + 0 + 0 = 1
Resultado: Error en posición 1
```

#### `correct(int[] block, int errorPosition)`
- **Qué hace**: Corrige el error invirtiendo el bit en la posición indicada
- **Proceso**: `block[errorPosition - 1] ^= 1` (XOR con 1 invierte el bit)
- **Ejemplo**:
  ```
  errorPosition = 1
  block[0] = 1
  block[0] ^= 1  → block[0] = 0 (se invierte)
  ```

**Limitaciones**:
- Solo corrige 1 error por bloque
- 2+ errores en el mismo bloque = irrecuperable (error fatal)

---

#### `HammingDecoder.java` - Decodificación Completa

**Propósito**: Detecta errores, los corrige y extrae los datos.

**Funciones principales**:

#### `decode(int[] block)`
- **Qué hace**: Procesa UN bloque completo
- **Pasos**:
  1. Llama a ErrorCorrector.detectError()
  2. Si hay error (posición ≠ 0), intenta corregir
  3. Registra estadísticas
  4. Extrae los datos originales

#### `extractDataBits(int[] block)` (privada)
- **Qué hace**: Extrae solo los datos, descartando bits de paridad

**Para Hamming(7,4)**:
```
Bloque corregido: [p1, p2, d1, p4, d2, d3, d4]
Índices:           0   1   2   3   4   5   6
Datos a extraer:      índices 2, 4, 5, 6 (posiciones 3, 5, 6, 7)
Resultado: [d1, d2, d3, d4]
```

#### `decodeAll(List<int[]> blocks)`
- **Qué hace**: Decodifica todos los bloques de la transmisión

---

### 9. **StatisticsReport.java** - Reporte de Estadísticas

**Propósito**: Rastrear y mostrar métricas de transmisión.

**Variables**:
- `totalBlocks`: Total de bloques procesados
- `detectedErrors`: Bloques con errores detectados
- `correctedErrors`: Errores que se pudieron corregir
- `fatalErrors`: Errores no corregibles (2+ bits)

**Funciones principales**:

#### `registerBlock()`
- Incrementa contador de bloques procesados

#### `registerDetected()` / `registerCorrected()` / `registerFatal()`
- Incrementan contadores según el tipo de error

#### `printReport()`
- **Muestra**:
  ```
  Bloques totales: 10
  Errores detectados: 7 (70.00%)
  Errores corregidos: 7 (100.00%)
  Errores fatales: 0 (0.00%)
  Tasa de integridad: 100.00%
  ```

**Cálculo de tasa de integridad**:
```
Integridad = (Bloques totales - Errores fatales) / Bloques totales * 100%
```

---

### 10. **HammingTest.java** - Pruebas Automatizadas

**Propósito**: Verifica que el sistema funciona correctamente ejecutando 7 pruebas automáticas.

**¿Por qué es importante?**
- Permite validar sin interacción manual
- Detecta errores en el código
- Demuestra que todas las funciones funcionan

**Funciones principales**:

#### `main(String[] args)`
- **Qué hace**: Ejecuta todas las pruebas automáticamente
- **Pruebas que ejecuta**:
  1. Conversión de texto a binario
  2. Recuperación de texto desde bloques
  3. Codificación Hamming(7,4)
  4. Codificación Hamming(11,7)
  5. Inyección de ruido
  6. Detección y corrección de errores
  7. Transmisión completa

**Salida esperada**:
```
=== PRUEBAS AUTOMATIZADAS DEL SISTEMA HAMMING ===

--- PRUEBA 1: Conversión de Texto ---
[PASÓ] Conversión texto a binario: Entrada: 'AB' -> 0100000101000010

--- PRUEBA 2: Convertidor Binario ---
[PASÓ] Recuperación de texto desde bloques: Original: 'Hola' -> Recuperado: 'Hola'

... (más pruebas) ...

==================================================
RESUMEN DE PRUEBAS
==================================================
Pruebas pasadas: 7/7
Tasa de éxito: 100,00%
==================================================
```

**Cómo ejecutar**:
```bash
java -cp bin Proyect.HammingTest
```

**Métodos internos**:

#### `testTextConversion()`
- Prueba que "AB" se convierte correctamente a binario (01000001 01000010)

#### `testBinaryConverter()`
- Prueba que "Hola" se convierte a binario y se recupera correctamente

#### `testHammingEncoding74()`
- Prueba que 4 bits se codifican a 7 bits con Hamming(7,4)

#### `testHammingEncoding117()`
- Prueba que 7 bits se codifican a 11 bits con Hamming(11,7)

#### `testNoiseChannel()`
- Prueba que el canal de ruido efectivamente altera bits

#### `testErrorCorrection()`
- Prueba que se detecta y corrige un error introducido

#### `testCompleteTransmission()`
- Prueba la transmisión completa: conversión → codificación → ruido → decodificación

---

##  RESUMEN: CLASES ESENCIALES vs COMPLEMENTARIAS

### CLASES ESENCIALES (Núcleo del Programa)
Sin estas, el programa NO funciona:

| Clase | Función |
|-------|---------|
| **Main.java** | Coordina todo, interfaz del usuario |
| **BinaryConverter.java** | Convierte texto/bytes a bloques binarios |
| **Hamming.java** | Codifica datos con bits de paridad |
| **Paridad.java** | Calcula bits de paridad con XOR |
| **NoiseChannel.java** | **CLAVE**: Simula errores de transmisión |
| **RandomNoiseGenerator.java** | Genera números aleatorios para ruido |
| **ErrorCorrector.java** | Detecta y corrige errores |
| **HammingDecoder.java** | Decodifica datos y extrae información |

###  CLASES COMPLEMENTARIAS (Mejoran la Experiencia)
El programa funciona sin ellas, pero son muy útiles:

| Clase | Función | Obligatoria |
|-------|---------|----------|
| **StatisticsReport.java** | Muestra estadísticas bonitas de la transmisión | No, pero recomendada |
| **FileHandler.java** | Lee archivos (imágenes, audio) | No, puedes usar solo texto |
| **HammingTest.java** | Pruebas automatizadas | No, solo para validación |

---



### Ejemplo 1: Conversión de Texto "A"

```
PASO 1: Convertir a ASCII
'A' → 65 (decimal) → 01000001 (binario)

PASO 2: Dividir en bloques de 4 bits para Hamming(7,4)
01000001 → [0100, 0001]

PASO 3: Codificar primer bloque [0, 1, 0, 0]
Inserción en Hamming(7,4):
Posición: 1    2    3    4    5    6    7
Tipo:     p1   p2   d1   p4   d2   d3   d4
Valor:    ?    ?    0    ?    1    0    0

PASO 4: Calcular paridades con XOR
p1 = XOR(d1, d2, d4) = XOR(0, 1, 0) = 1
p2 = XOR(d1, d3, d4) = XOR(0, 0, 0) = 0
p4 = XOR(d2, d3, d4) = XOR(1, 0, 0) = 1

Resultado codificado: [1, 0, 0, 1, 1, 0, 0]

PASO 5: Pasar por canal de ruido con probabilidad 10%
Random para cada bit: 0.03, 0.15, 0.08, 0.12, 0.02, 0.11, 0.09
Comparar con 0.10:   <    >    <    >    <    >    >
Bits alterados en:   posición 0, 2, 4
Resultado ruidoso:   [0, 0, 1, 1, 0, 0, 0]

PASO 6: Detectar error
Recalcular paridades del bloque ruidoso:
p1_calc = XOR(1, 0, 0) = 1 vs p1_recibido = 0 → diferencia 1
p2_calc = XOR(1, 0, 0) = 1 vs p2_recibido = 0 → diferencia 1
p4_calc = XOR(0, 0, 0) = 0 vs p4_recibido = 1 → diferencia 1

Síndrome = 1 + 2*1 + 4*1 = 7 → Error en posición 7

PASO 7: Corregir
Invertir bloque[6] (posición 7): 0 → 1
Resultado corregido: [1, 0, 0, 1, 1, 0, 1]

NOTA: El error detectado fue en posición 7 (la última), pero ocurrieron 3 errores.
Como Hamming solo corrige 1 error por bloque, los otros 2 no se pueden corregir.
Este sería un ERROR FATAL en la práctica.
```

### Ejemplo 2: Probabilidad de Error Explicada

```
¿QUÉ SIGNIFICA 10% DE PROBABILIDAD DE ERROR?

Para CADA BIT transmitido:
- Se genera un número aleatorio entre 0 y 1
- Si número < 0.10 → El bit se altera (bit flip)
- Si número >= 0.10 → El bit se mantiene igual

RESULTADO ESTADÍSTICO:
En 100 bits transmitidos:
- Aproximadamente 10 bits tendrán error
- Aproximadamente 90 bits se transmitirán correctamente

EJEMPLO CON 7 BITS:
Números aleatorios: 0.03, 0.12, 0.08, 0.15, 0.02, 0.11, 0.09
Comparar con 0.10:  <    >    <    >    <    >    >
Resultado:          ERROR BIEN ERROR BIEN ERROR BIEN BIEN
Bits alterados:     3 (posiciones 0, 2, 4)

POR QUÉ ES IMPORTANTE:
- 0% = Canal perfecto (no hay errores)
- 5% = Canal bueno (Wi-Fi típico)
- 10% = Canal con ruido moderado
- 20% = Canal muy ruidoso
- 50% = Canal completamente saturado de ruido
```

---

##  FLUJO COMPLETO CON ESTADÍSTICAS

```
ENTRADA: "Hola" con probabilidad 5% y Hamming(7,4)

1. CONVERSIÓN A BINARIO
   H=72, o=111, l=108, a=97
   01001000 01101111 01101100 01100001
   
2. DIVISIÓN EN BLOQUES DE 4 BITS
   [0100, 1000, 0110, 1111, 0110, 1100, 0110, 0001]
   Total: 8 bloques

3. CODIFICACIÓN HAMMING(7,4)
   Cada bloque de 4 bits → 7 bits con paridad
   Total: 8 bloques de 7 bits = 56 bits

4. CANAL DE RUIDO (5% probabilidad)
   Esperado: 56 * 0.05 = 2.8 ≈ 3 bits alterados
   Actual: puede variar (es aleatorio)
   Digamos: 2 bits alterados

5. DETECCIÓN Y CORRECCIÓN
   8 bloques procesados
   2 errores detectados
   2 errores corregidos (si fueron de 1 bit cada uno)
   0 errores fatales

6. EXTRACCIÓN DE DATOS
   8 bloques de 4 bits → 32 bits de datos
   32 bits → 4 caracteres

7. RECONSTRUCCIÓN DE TEXTO
   01001000 01101111 01101100 01100001
   H        o        l        a
   Texto recuperado: "Hola"

8. ESTADÍSTICAS FINALES
   Bloques totales: 8
   Errores detectados: 2 (25%)
   Errores corregidos: 2 (100%)
   Errores fatales: 0 (0%)
   Integridad de datos: 100%
```

---

##  CASOS DE USO Y RESULTADOS ESPERADOS

### Caso A: Transmisión Sin Ruido (probabilidad = 0%)
```
Entrada: "Test"
Probabilidad: 0.0
Hamming: 7,4

Resultado:
- Bits alterados en canal: 0
- Errores detectados: 0
- Mensaje recuperado: "Test" ✓ PERFECTO
```

### Caso B: Transmisión con Ruido Bajo (5%)
```
Entrada: "Arquitectura"
Probabilidad: 0.05
Hamming: 7,4

Resultado:
- Bits alterados en canal: ~5
- Errores detectados: ~5
- Todos corregidos (si es 1 por bloque)
- Mensaje recuperado: "Arquitectura" ✓ CORRECTO
```

### Caso C: Transmisión con Ruido Alto (20%)
```
Entrada: "Computadores"
Probabilidad: 0.2
Hamming: 11,7

Resultado:
- Bits alterados en canal: ~25
- Errores detectados: ~25
- Algunos corregidos, otros fatales
- Mensaje recuperado: Parcialmente correcto con errores
- Integridad: 80-90% (dependiendo del bloque)
```

---

##  COMPILACIÓN Y EJECUCIÓN

**Compilar**:
```bash
javac -d bin src/Proyect/*.java
```

**Ejecutar**:
```bash
java -cp bin Proyect.Main
```

**Pruebas**:
```bash
java -cp bin Proyect.HammingTest
```

---

##  CONCEPTOS CLAVE EXPLICADOS

**XOR (Operación Exclusive OR)**:
- 0 XOR 0 = 0
- 0 XOR 1 = 1
- 1 XOR 0 = 1
- 1 XOR 1 = 0

**Bit Flip**:
- Cambiar 0 → 1 o 1 → 0
- Se usa para simular errores de transmisión

**Síndrome de Paridad**:
- Número que indica la posición del error (0 = sin error)
- Se calcula comparando paridades recalculadas

**Integridad de Datos**:
- Porcentaje de datos que llegaron sin errores irrecuperables
- Fórmula: (Bloques totales - Errores fatales) / Bloques totales

---

##  LIMITACIONES EN LA RECUPERACIÓN DE ARCHIVOS DE IMAGEN/AUDIO

### ¿Por qué a veces las imágenes no se recuperan al 100%?

El **Código de Hamming (11,7)** es eficaz para corregir errores, pero tiene limitaciones importantes:

####  Limitación Principal: 1 Error por Bloque

**Cada bloque Hamming solo puede corregir 1 error de bit**. Si un bloque recibe múltiples bits alterados, el código:
-  Puede detectar que hay error
-  NO puede corregir todos los bits incorrectos
-  Resultado: **Error fatal** (bits incorrectos en la salida)

**Ejemplo**:
```
Bloque original (11 bits):  [1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0]
Transmisión (2 errores):    [1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0]  ← bits 2 y 5 alterados
Detección:  Síndrome ≠ 0 → "Hay error"
Corrección: Intenta corregir pero solo encuentra 1 posición
Resultado:  [1, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0]  ← Aún hay 1 error (bit 5 incorrecto)
```

####  ¿Cuándo Ocurren Errores Múltiples?

Con **probabilidad de error P**, la chance de que un bloque de 11 bits tenga 2+ errores es:

$$P_{fallo} = 1 - (1-P)^{11} - 11P(1-P)^{10}$$

| Probabilidad | 2+ Errores | Tasa de Bloques Fallidos |
|--------------|-----------|--------------------------|
| **0.0%** | 0% |  0% (Perfecto) |
| **0.1%** | 0.06% |  < 1 de 2000 bloques |
| **0.5%** | 1.5% |  ~1 de 67 bloques |
| **1.0%** | 5.8% |  ~1 de 17 bloques |
| **5.0%** | 50%+ |  Inútil |

####  Impacto en Archivos de Imagen

Para una imagen de **239 KB** (239,675 bytes):

**Con Hamming(11,7)**:
- Total de bits: 239,675 × 8 = 1,917,400 bits
- Bloques: 1,917,400 ÷ 7 ≈ 273,915 bloques

| Probabilidad | Bits Alterados | Bloques Fallidos | Resultado |
|--------------|----------------|------------------|-----------|
| **0.0%** | 0 | 0 |  Imagen perfecta |
| **0.5%** | ~9,587 | ~4,100 |  Imagen corrupta |
| **1.0%** | ~19,174 | ~15,900 |  Muy corrupta |

####  Consecuencias de Errores No Corregibles

Cuando un bloque tiene 2+ errores que no pueden corregirse:

1. **En archivos PNG/JPEG**:
   - Los bytes se alteran de forma impredecible
   - La imagen se ve **pixelada, distorsionada o con manchas**
   - La estructura del archivo puede dañarse (no abre)

2. **En archivos MP3/WAV**:
   - El audio tiene **ruido, cortes, o distorsión**
   - Fragmentos de sonido se vuelven ininteligibles

3. **En archivos de texto**:
   - Caracteres aleatorios aparecen
   - Palabras se cambian o desaparecen

#### Cuándo Funciona Bien

El sistema recupera archivos **perfectamente** cuando:
-  **Probabilidad ≤ 0.1%**: Canales de comunicación de buena calidad
-  **Probabilidad = 0%**: Simulaciones sin error (demostraciones)
-  **Textos pequeños**: Menos bloques = menos chance de múltiples errores

####  Soluciones (No Implementadas)

Para mejorar la recuperación en canales ruidosos:

1. **Códigos de Hamming Extendidos**: Permiten detectar 2 errores (aunque solo corrigen 1)
2. **Códigos Reed-Solomon**: Corrigen múltiples errores por bloque
3. **ARQ (Automatic Repeat Request)**: Solicita retransmisión de bloques defectuosos
4. **Interleaving**: Distribuye bits de un bloque en múltiples posiciones (reduce concentración de errores)

---



Este sistema demuestra cómo el **Código de Hamming** permite:
1. **Detectar** errores de 1-2 bits
2. **Corregir** automáticamente errores de 1 bit
3. **Recuperar** datos en canales ruidosos
4. **Mantener integridad** de la información transmitida

Es fundamental en comunicaciones de datos, almacenamiento y sistemas embebidos.

---

**Grupo 2 - B4 | Arquitectura de Computadores**
