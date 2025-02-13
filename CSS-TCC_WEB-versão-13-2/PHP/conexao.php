<?php
$servername = "localhost"; 
$username = "root"; 
$password = "";
$banconame = "banco_tesouro_azul"; 

$conn = new mysqli($servername, $username, $password, $banconame);
//cria a conexão


if ($conn->connect_error) {

    die("Conexão falhou: " . $conn->connect_error);
} else {
    
}
?>
