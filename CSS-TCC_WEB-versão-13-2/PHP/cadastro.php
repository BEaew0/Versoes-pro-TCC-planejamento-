<?php
session_start();

include 'conexao.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $nome = $_POST['nome_usuario'];
    $CPF = $_POST['CPF_usuario'];
    $CNPJ = $_POST['CNPJ_usuario'];
    $dta = $_POST['dta_nascimento'];
    $email = $_POST['email_usuario'];
    $email_conf = $_POST['conf_email'];
    $senha = $_POST['senha_cad'];
    $senha_conf = $_POST['conf_senha'];

    $erro = "";

    if ($senha != $senha_conf)
     {
        $erro = "Erro. As senhas estão diferentes.";
    } 
    elseif ($email != $email_conf)
    {
        $erro = "Erro. Os emails não coincidem.";
    } else {
        // Prevenir SQL Injection usando prepared statements
        $comando= $conn->prepare("INSERT INTO tb_cadastro (nome_cadastro, CPF_cadastro, CNPJ_cadastro, dta_nasc_cadastro, email_cadastro, senha_cadastro) 
                                VALUES (?, ?, ?, ?, ?, ?)");
        $comando->bind_param(1,$nome);
        $comando->bind_param(2,$CPF);
        $comando->bind_param(3,$CNPJ);
        $comando->bind_param(4,$dta);
        $comando->bind_param(5, $email);
        $comando->bind_param(6, $senha);
        





        if ($stmt->execute() == true) {
            echo "<script>
            alert('Cadastro realizado com sucesso');

            window.location.href = 'logar.html';
          </script>";

            exit();
        } 
        else
        {
            $erro = "Erro ao cadastrar: " . $stmt->error;
        }

        $stmt->close();
    }

    if (!empty($erro)) 
    {
        echo "<script>alert('$erro');</script>";
    }
}

$conn->close();
?>