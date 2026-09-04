"""Lectura y transformación de datos — Fases 3 y 4."""

from __future__ import annotations

import csv
import json
from pathlib import Path


class DataSourceError(Exception):
    """Error genérico cuando la fuente de datos no se puede leer."""


def load_data(path: str) -> list[tuple[str, str, str]]:
    """Cargar datos desde un archivo CSV o JSON.

    Args:
        path: ruta relativa al archivo (definida en .env como DATA_SOURCE_PATH).

    Returns:
        Lista de tuplas ``(métrica, valor, dirección)`` aptas para la tabla KPIs.

    Raises:
        DataSourceError: si el archivo no existe o el formato es inválido.
    """
    full_path = Path(path)
    if not full_path.exists():
        raise DataSourceError(f"Archivo no encontrado: {full_path}")

    suffix = full_path.suffix.lower()

    if suffix == ".csv":
        return _load_csv(full_path)
    elif suffix == ".json":
        return _load_json(full_path)
    else:
        raise DataSourceError(
            f"Formato de archivo no soportado: {suffix}. Use .csv o .json."
        )


def _load_csv(path: Path) -> list[tuple[str, str, str]]:
    """Parsear un CSV con columnas: Métrica, Valor, Dirección."""
    rows: list[tuple[str, str, str]] = []
    try:
        with path.open(encoding="utf-8", newline="") as fh:
            reader = csv.reader(fh)
            header = next(reader, None)  # saltar cabecera si existe
            for row in reader:
                if not row or all(c.strip() == "" for c in row):
                    continue  # líneas vacías
                metrica = row[0].strip() if len(row) > 0 else ""
                valor = row[1].strip() if len(row) > 1 else ""
                direccion = row[2].strip() if len(row) > 2 else ""
                rows.append((metrica, valor, direccion))
    except csv.Error as exc:
        raise DataSourceError(f"CSV inválido en {path}: {exc}") from exc
    return rows


def _load_json(path: Path) -> list[tuple[str, str, str]]:
    """Parsear un JSON con estructura de lista de dicts o dict simple."""
    try:
        data = json.loads(path.read_text(encoding="utf-8"))
    except json.JSONDecodeError as exc:
        raise DataSourceError(f"JSON inválido en {path}: {exc}") from exc

    # Caso 1: lista de objetos {"nombre": ..., "valor": ..., ...}
    if isinstance(data, list):
        return [
            (
                str(item.get("Metrica", item.get("name", ""))),
                str(item.get("Valor", item.get("value", ""))),
                str(item.get("Direccion", item.get("direction", ""))),
            )
            for item in data
        ]

    # Caso 2: objeto {"Métrica": {...}, ...} → aplanado
    if isinstance(data, dict):
        rows: list[tuple[str, str, str]] = []
        for key, val in data.items():
            if isinstance(val, dict):
                metrica = key
                valor = str(val.get("Valor", val.get("value", "")))
                direccion = str(val.get("Direccion", val.get("direction", "")))
            else:
                metrica = key
                valor = str(val)
                direccion = ""
            rows.append((metrica, valor, direccion))
        return rows

    raise DataSourceError(f"JSON con estructura inesperada en {path}")


def load_bar_chart_data(path: str | None = None) -> list[tuple[str, float]]:
    """Cargar datos numéricos para el gráfico de barras.

    Si *path* es ``None``, lee del mismo archivo CSV definido en .env
    (DATA_SOURCE_PATH). Las filas se interpretan como (Métrica, Valor), donde
    Valor debe ser parseable a ``float``.

    Returns:
        Lista de ``(etiqueta, valor_numerico)``.
    """
    if path is None:
        from dotenv import load_dotenv
        from pathlib import Path

        env_path = Path(__file__).resolve().parent.parent / ".env"
        load_dotenv(env_path)
        import os as _os

        path = _os.getenv("DATA_SOURCE_PATH", "data/sample.csv")

    rows = load_data(path)
    result: list[tuple[str, float]] = []
    for metrica, valor, _direccion in rows:
        try:
            # Limpiar símbolos no numéricos ($, %, etc.)
            clean = valor.replace("$", "").replace("%", "").replace(",", "")
            result.append((metrica, float(clean)))
        except ValueError:
            continue  # saltar filas con valores no numéricos
    return result
