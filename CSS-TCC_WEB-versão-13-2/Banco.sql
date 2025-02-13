

Create database banco_tesouro_azul;
use banco_tesouro_azul;


create table tb_cadastro
(id_cadastro int auto_increment primary key,
nome_cadastro varchar (35) not null,
CPF_cadastro char (11) unique not null,
CNPJ_cadastro char (17) unique,
dta_nasc_cadastro date not null,
email_cadastro varchar (35) not null,
senha_cadastro char (10) not null



);

select * from tb_cadastro;