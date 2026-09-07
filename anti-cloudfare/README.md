# anti-cloudfare

PoC para bypass de Cloudflare usando Playwright.

## Requisitos

- JDK 17+
- Maven 3.8+
- Node.js 18+ (para instalar Playwright browsers)

## Instalar dependencias

```bash
# 1. Descargar dependencias de Maven
mvn clean install

# 2. Instalar browsers de Playwright
npx playwright install chromium
```

Si usas macOS con Apple Silicon y tienes problemas con Chromium:

```bash
npx playwright install --with-deps chromium
```

## Compilar

```bash
mvn package
```

El JAR empaquetado se genera en `target/`.

## Ejecutar

```bash
# Con el JAR empaquetado
java -jar target/cve-2024-4666-1.0.0.jar

# O directamente desde Maven
mvn exec:java -Dexec.mainClass="com.exploit.CVE20244666Exploit" -Dexec.args="--url https://ejemplo.com"

# Especificar un archivo a probar
mvn exec:java -Dexec.mainClass="com.exploit.CVE20244666Exploit" -Dexec.args="--url https://ejemplo.com --file /etc/passwd"
```

### Argumentos

| Argumento | Descripción |
|-----------|-------------|
| `--url <url>` | URL del objetivo (default: https://www.octopus.mx) |
| `--file <path>` | Archivo a incluir, reemplaza la lista por defecto |

## Ejemplo simple — visitar una web con Playwright

```java
import com.microsoft.playwright.*;

public class SimpleVisit {
    public static void main(String[] args) {
        try (Playwright pw = Playwright.create()) {
            Browser browser = pw.chromium().launch();
            Page page = browser.newPage();

            page.navigate("https://example.com");
            System.out.println("Title: " + page.title());
            System.out.println(page.content());

            browser.close();
        }
    }
}
```

## Estructura del proyecto

```
.
├── pom.xml
├── README.md
└── src/main/java/com/exploit/
    └── CVE20244666Exploit.java
```
