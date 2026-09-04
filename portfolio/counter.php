<?php
$file = 'counter.txt';

// Si el archivo no existe, lo creamos con 0
if (!file_exists($file)) {
    file_put_contents($file, '0');
}

// Leemos el contenido actual
$current = file_get_contents($file);
$count = (int)str_replace(',', '', $current);

// Incrementamos
$count++;

// Guardamos el nuevo valor
file_put_contents($file, $count);

// Devolvemos el valor formateado para el frontend
echo number_format($count);
?>
