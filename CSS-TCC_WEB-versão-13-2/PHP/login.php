<?php

header('Content-Type: text/html; charset=utf-8');



include 'conexao.php';
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $nome=$_POST['nome_login'];
    $email = $_POST['email'];
    $tipo = $_POST['log_pessoa'];
 
    $senha=$_POST['senha_log'];

    if(strlen ($tipo == 0))
    {
        echo "Preencha  seu cpf ou CNPJ!";


    }


    else if(strlen($nome == 0))
    {
        echo "Preencha seu nome";
    }
    else if(strlen($senha== 0))
    {
        echo "Preencha sua senha!!";

    }

    $sql = "SELECT nome_cadastro, CPF_cadastro, senha_cadastro FROM tb_cadastro  ";



        if (strlen($tipo) == 11) {
            // Se for CPF (11 caracteres)
            $sql .= " WHERE CPF_cadastro = ?";
        } else if (strlen($tipo) == 14) {
            // Se for CNPJ (14 caracteres)
            $sql .= " WHERE CNPJ_cadastro = ?";
        } else {
            echo "CPF ou CNPJ inválido!";
            exit;
        }

        $comando = $conn->prepare($sql);
        $comando->bind_param(1,   $nome);
        $comando->bind_param(2, $email);
        $comando->bind_param(3, $email);

        $comando->execute();
        $result = $comando->get_result();






    
}

?>