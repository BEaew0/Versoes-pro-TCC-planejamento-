-- Triggers e eventos

-- TRIGGERS
-- Trigger para calcular validade da assinatura com base em segundos
DELIMITER //
CREATE TRIGGER TG_BEFORE_INSERT_USUARIO
BEFORE INSERT ON TB_USUARIO
FOR EACH ROW
BEGIN
    DECLARE segundos INT;
    SELECT DURACAO_SEGUNDOS_ASSINATURA INTO segundos FROM TB_ASSINATURA 
    WHERE ID_ASSINATURA = NEW.ID_ASSINATURA_FK;
    SET NEW.DATA_VALIDADE_ASSINATURA_USUARIO = DATE_ADD(NEW.DATA_INICIO_ASSINATURA_USUARIO, INTERVAL segundos SECOND);
END;//
DELIMITER ; 

-- Trigger para calcular VALOR_LUCRO de TB_LUCRO
DELIMITER //
CREATE TRIGGER TG_CALCULA_LUCRO
AFTER INSERT ON TB_ITENS_LUCRO
FOR EACH ROW
BEGIN
    DECLARE v_venda DECIMAL(10,2);
    DECLARE v_compra DECIMAL(10,2);
    DECLARE v_lucro DECIMAL(10,2);

    -- Buscar valores das tabelas relacionadas
    SELECT VALOR_VENDA INTO v_venda FROM TB_PEDIDO_VENDA
    WHERE ID_VENDA = NEW.ID_VENDA_FK;

    SELECT VALOR_PEDIDO INTO v_compra FROM TB_PEDIDO_COMPRA
    WHERE ID_COMPRA = NEW.ID_COMPRA_FK;

    -- Calcular lucro
    SET v_lucro = v_venda - v_compra;

    -- Atualizar o valor do lucro na tabela TB_LUCRO
    UPDATE TB_LUCRO
    SET VALOR_LUCRO = IFNULL(VALOR_LUCRO, 0) + v_lucro
    WHERE ID_LUCRO = NEW.ID_LUCRO_FK;
END;
//
DELIMITER ;


-- Trigger para conta desativada mais de um mês para excluir ela

-- Trigger para remover item do estoque em sua venda

-- Trigger para adicionar item no estoque em sua compra

-- 

-- EVENTOS
-- Evento para desativar usuários com assinatura vencida
SET GLOBAL event_scheduler = ON;
DELIMITER //
CREATE EVENT IF NOT EXISTS EV_VERIFICA_ASSINATURA_USUARIO
ON SCHEDULE EVERY 1 HOUR
DO
BEGIN
    UPDATE TB_USUARIO
    SET ID_ASSINATURA_FK = 1
    WHERE DATA_VALIDADE_ASSINATURA <= NOW();
END;//
DELIMITER ;
