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

-- Trigger para inserir no estoque ao registrar compra
DELIMITER $$

CREATE TRIGGER trg_after_insert_item_compra_estoque
AFTER INSERT ON TB_ITEM_COMPRA
FOR EACH ROW
BEGIN
    DECLARE v_qtd DECIMAL(10,2);
    DECLARE v_valor_unitario DECIMAL(10,2);
    DECLARE v_valor_total DECIMAL(10,2);
    DECLARE v_id_usuario INT;

    -- Quantidade e valor total
    SET v_qtd = NEW.QUANTIDADE_ITEM;
    
    -- Buscar valor unitário da compra
    SELECT VALOR_PEDIDO / QTD_COMPRA, ID_USUARIO_FK
    INTO v_valor_unitario, v_id_usuario
    FROM TB_PEDIDO_COMPRA
    WHERE ID_COMPRA = NEW.ID_COMPRA;

    SET v_valor_total = v_valor_unitario * v_qtd;

    -- Se já existe no estoque, atualiza
    IF EXISTS (
        SELECT 1 FROM TB_ESTOQUE_PRODUTO
        WHERE ID_PRODUTO_FK = NEW.ID_PRODUTO AND ID_USUARIO_FK = v_id_usuario
    ) THEN
        UPDATE TB_ESTOQUE_PRODUTO
        SET 
            QTD_TOTAL_ESTOQUE_ESTOQUE = QTD_TOTAL_ESTOQUE_ESTOQUE + v_qtd,
            VALOR_GASTO_TOTAL_ESTOQUE = VALOR_GASTO_TOTAL_ESTOQUE + v_valor_total,
            VALOR_POTENCIAL_VENDA_ESTOQUE = VALOR_POTENCIAL_VENDA_ESTOQUE + (v_valor_total * 2) -- Exemplo: markup 100%
        WHERE ID_PRODUTO_FK = NEW.ID_PRODUTO AND ID_USUARIO_FK = v_id_usuario;

    -- Se não existe, insere
    ELSE
        INSERT INTO TB_ESTOQUE_PRODUTO (
            ID_PRODUTO_FK,
            ID_USUARIO_FK,
            QTD_TOTAL_ESTOQUE_ESTOQUE,
            VALOR_GASTO_TOTAL_ESTOQUE,
            VALOR_POTENCIAL_VENDA_ESTOQUE
        ) VALUES (
            NEW.ID_PRODUTO,
            v_id_usuario,
            v_qtd,
            v_valor_total,
            v_valor_total * 2 -- markup exemplo
        );
    END IF;
END$$

DELIMITER ;

-- Trigger para atualizar o estoque ao produto ser vendido
DELIMITER $$

CREATE TRIGGER trg_after_insert_item_venda_estoque
AFTER INSERT ON TB_ITEM_VENDA
FOR EACH ROW
BEGIN
    DECLARE v_qtd_vendida DECIMAL(10,2);
    DECLARE v_valor_unitario DECIMAL(10,2);
    DECLARE v_id_usuario INT;

    SET v_qtd_vendida = NEW.QTS_ITEM_VENDA;
    SET v_desconto_total = NEW.DESCONTO_ITEM_VENDA;

    -- Buscar ID do usuário associado ao produto
    SELECT ID_USUARIO_FK
    INTO v_id_usuario
    FROM TB_PRODUTO
    WHERE ID_PRODUTO = NEW.ID_PRODUTO;

    -- Custo médio por unidade
    SELECT 
        VALOR_GASTO_TOTAL_ESTOQUE / QTD_TOTAL_ESTOQUE_ESTOQUE
    INTO 
        v_valor_unitario
    FROM TB_ESTOQUE_PRODUTO
    WHERE ID_PRODUTO_FK = NEW.ID_PRODUTO AND ID_USUARIO_FK = v_id_usuario;

    -- Atualizar o estoque
    UPDATE TB_ESTOQUE_PRODUTO
    SET 
        QTD_TOTAL_ESTOQUE_ESTOQUE = QTD_TOTAL_ESTOQUE_ESTOQUE - v_qtd_vendida,
        VALOR_GASTO_TOTAL_ESTOQUE = VALOR_GASTO_TOTAL_ESTOQUE - (v_qtd_vendida * v_valor_unitario),
        VALOR_POTENCIAL_VENDA_ESTOQUE = VALOR_POTENCIAL_VENDA_ESTOQUE - (v_qtd_vendida * v_valor_unitario)
    WHERE ID_PRODUTO_FK = NEW.ID_PRODUTO AND ID_USUARIO_FK = v_id_usuario;
END$$

DELIMITER ;




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

-- Evento para conta desativada mais de um mês para excluir ela
