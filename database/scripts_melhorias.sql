-- Scripts SQL para implementação das melhorias do Sistema GestCon
-- Criação das novas tabelas para workflow, pagamentos e notas fiscais

-- =====================================================
-- TABELA: processos_contrato
-- Descrição: Armazena o histórico de workflow dos contratos
-- =====================================================
CREATE TABLE IF NOT EXISTS processos_contrato (
    id BIGSERIAL PRIMARY KEY,
    contrato_id BIGINT NOT NULL,
    status_anterior VARCHAR(50) NOT NULL,
    status_atual VARCHAR(50) NOT NULL,
    acao_realizada VARCHAR(255) NOT NULL,
    observacoes TEXT,
    data_acao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    usuario_responsavel VARCHAR(100) NOT NULL,
    documento_anexo VARCHAR(500),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    
    CONSTRAINT fk_processo_contrato 
        FOREIGN KEY (contrato_id) REFERENCES contratos(id) ON DELETE CASCADE
);

-- Índices para otimização de consultas
CREATE INDEX idx_processos_contrato_id ON processos_contrato(contrato_id);
CREATE INDEX idx_processos_data_acao ON processos_contrato(data_acao);
CREATE INDEX idx_processos_status_atual ON processos_contrato(status_atual);
CREATE INDEX idx_processos_usuario ON processos_contrato(usuario_responsavel);

-- =====================================================
-- TABELA: pagamentos
-- Descrição: Controla os pagamentos de cada contrato
-- =====================================================
CREATE TABLE IF NOT EXISTS pagamentos (
    id BIGSERIAL PRIMARY KEY,
    contrato_id BIGINT NOT NULL,
    numero_parcela VARCHAR(50) NOT NULL,
    valor_previsto DECIMAL(15,2) NOT NULL,
    valor_pago DECIMAL(15,2),
    data_vencimento DATE NOT NULL,
    data_pagamento DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    observacoes TEXT,
    numero_nota_fiscal VARCHAR(100),
    documento_comprobatorio VARCHAR(500),
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    usuario_responsavel VARCHAR(100) NOT NULL,
    
    CONSTRAINT fk_pagamento_contrato 
        FOREIGN KEY (contrato_id) REFERENCES contratos(id) ON DELETE CASCADE,
    CONSTRAINT chk_pagamento_status 
        CHECK (status IN ('PENDENTE', 'PAGO', 'ATRASADO', 'CANCELADO'))
);

-- Índices para otimização
CREATE INDEX idx_pagamentos_contrato_id ON pagamentos(contrato_id);
CREATE INDEX idx_pagamentos_data_vencimento ON pagamentos(data_vencimento);
CREATE INDEX idx_pagamentos_status ON pagamentos(status);
CREATE INDEX idx_pagamentos_data_pagamento ON pagamentos(data_pagamento);

-- =====================================================
-- TABELA: notas_fiscais
-- Descrição: Gerencia as notas fiscais dos contratos
-- =====================================================
CREATE TABLE IF NOT EXISTS notas_fiscais (
    id BIGSERIAL PRIMARY KEY,
    contrato_id BIGINT NOT NULL,
    pagamento_id BIGINT,
    numero_nota VARCHAR(100) NOT NULL UNIQUE,
    serie VARCHAR(10) NOT NULL,
    valor_total DECIMAL(15,2) NOT NULL,
    valor_impostos DECIMAL(15,2),
    valor_liquido DECIMAL(15,2),
    data_emissao DATE NOT NULL,
    data_vencimento DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    descricao_servicos TEXT,
    observacoes TEXT,
    arquivo_pdf VARCHAR(500),
    arquivo_xml VARCHAR(500),
    chave_acesso VARCHAR(44),
    data_upload TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_aprovacao TIMESTAMP,
    usuario_upload VARCHAR(100),
    usuario_aprovacao VARCHAR(100),
    motivo_rejeicao TEXT,
    
    CONSTRAINT fk_nota_fiscal_contrato 
        FOREIGN KEY (contrato_id) REFERENCES contratos(id) ON DELETE CASCADE,
    CONSTRAINT fk_nota_fiscal_pagamento 
        FOREIGN KEY (pagamento_id) REFERENCES pagamentos(id) ON DELETE SET NULL,
    CONSTRAINT chk_nota_fiscal_status 
        CHECK (status IN ('PENDENTE', 'APROVADA', 'REJEITADA', 'PAGA'))
);

-- Índices para otimização
CREATE INDEX idx_notas_fiscais_contrato_id ON notas_fiscais(contrato_id);
CREATE INDEX idx_notas_fiscais_numero ON notas_fiscais(numero_nota);
CREATE INDEX idx_notas_fiscais_status ON notas_fiscais(status);
CREATE INDEX idx_notas_fiscais_data_emissao ON notas_fiscais(data_emissao);
CREATE INDEX idx_notas_fiscais_chave_acesso ON notas_fiscais(chave_acesso);

-- =====================================================
-- TABELA: documentos
-- Descrição: Armazena documentos anexos aos contratos
-- =====================================================
CREATE TABLE IF NOT EXISTS documentos (
    id BIGSERIAL PRIMARY KEY,
    contrato_id BIGINT NOT NULL,
    nome_arquivo VARCHAR(255) NOT NULL,
    tipo_documento VARCHAR(50) NOT NULL,
    caminho_arquivo VARCHAR(500) NOT NULL,
    tamanho_arquivo BIGINT,
    tipo_mime VARCHAR(100),
    data_upload TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    usuario_upload VARCHAR(100) NOT NULL,
    versao INTEGER DEFAULT 1,
    ativo BOOLEAN DEFAULT TRUE,
    observacoes TEXT,
    
    CONSTRAINT fk_documento_contrato 
        FOREIGN KEY (contrato_id) REFERENCES contratos(id) ON DELETE CASCADE
);

-- Índices para documentos
CREATE INDEX idx_documentos_contrato_id ON documentos(contrato_id);
CREATE INDEX idx_documentos_tipo ON documentos(tipo_documento);
CREATE INDEX idx_documentos_data_upload ON documentos(data_upload);

-- =====================================================
-- TABELA: notificacoes
-- Descrição: Sistema de notificações in-app
-- =====================================================
CREATE TABLE IF NOT EXISTS notificacoes (
    id BIGSERIAL PRIMARY KEY,
    contrato_id BIGINT,
    usuario_destinatario VARCHAR(100) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    mensagem TEXT NOT NULL,
    lida BOOLEAN DEFAULT FALSE,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_leitura TIMESTAMP,
    prioridade VARCHAR(10) DEFAULT 'NORMAL',
    
    CONSTRAINT fk_notificacao_contrato 
        FOREIGN KEY (contrato_id) REFERENCES contratos(id) ON DELETE CASCADE,
    CONSTRAINT chk_notificacao_prioridade 
        CHECK (prioridade IN ('BAIXA', 'NORMAL', 'ALTA', 'URGENTE'))
);

-- Índices para notificações
CREATE INDEX idx_notificacoes_usuario ON notificacoes(usuario_destinatario);
CREATE INDEX idx_notificacoes_lida ON notificacoes(lida);
CREATE INDEX idx_notificacoes_data_criacao ON notificacoes(data_criacao);
CREATE INDEX idx_notificacoes_tipo ON notificacoes(tipo);

-- =====================================================
-- TABELA: logs_auditoria
-- Descrição: Log de auditoria para todas as operações
-- =====================================================
CREATE TABLE IF NOT EXISTS logs_auditoria (
    id BIGSERIAL PRIMARY KEY,
    tabela VARCHAR(50) NOT NULL,
    operacao VARCHAR(10) NOT NULL,
    registro_id BIGINT NOT NULL,
    dados_anteriores JSONB,
    dados_novos JSONB,
    usuario VARCHAR(100) NOT NULL,
    data_operacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_origem VARCHAR(45),
    user_agent TEXT,
    
    CONSTRAINT chk_auditoria_operacao 
        CHECK (operacao IN ('INSERT', 'UPDATE', 'DELETE'))
);

-- Índices para auditoria
CREATE INDEX idx_logs_auditoria_tabela ON logs_auditoria(tabela);
CREATE INDEX idx_logs_auditoria_registro_id ON logs_auditoria(registro_id);
CREATE INDEX idx_logs_auditoria_usuario ON logs_auditoria(usuario);
CREATE INDEX idx_logs_auditoria_data ON logs_auditoria(data_operacao);

-- =====================================================
-- VIEWS PARA RELATÓRIOS E DASHBOARDS
-- =====================================================

-- View: Contratos com status atual
CREATE OR REPLACE VIEW vw_contratos_status AS
SELECT 
    c.id,
    c.numero_contrato,
    e.nome as empresa_nome,
    c.objeto,
    c.valor_total,
    c.vigencia_inicio,
    c.vigencia_fim,
    c.status,
    c.responsavel,
    CASE 
        WHEN c.vigencia_fim < CURRENT_DATE THEN 'VENCIDO'
        WHEN c.vigencia_fim <= CURRENT_DATE + INTERVAL '30 days' THEN 'VENCENDO'
        ELSE 'VIGENTE'
    END as situacao_vigencia,
    (c.vigencia_fim - CURRENT_DATE) as dias_para_vencimento
FROM contratos c
JOIN empresas e ON c.empresa_id = e.id;

-- View: Resumo financeiro por contrato
CREATE OR REPLACE VIEW vw_resumo_financeiro AS
SELECT 
    c.id as contrato_id,
    c.numero_contrato,
    c.valor_total,
    COALESCE(SUM(p.valor_previsto), 0) as total_previsto,
    COALESCE(SUM(p.valor_pago), 0) as total_pago,
    COALESCE(SUM(CASE WHEN p.status = 'PENDENTE' THEN p.valor_previsto ELSE 0 END), 0) as total_pendente,
    COUNT(p.id) as total_parcelas,
    COUNT(CASE WHEN p.status = 'PAGO' THEN 1 END) as parcelas_pagas,
    COUNT(CASE WHEN p.status = 'PENDENTE' THEN 1 END) as parcelas_pendentes,
    COUNT(CASE WHEN p.status = 'ATRASADO' THEN 1 END) as parcelas_atrasadas
FROM contratos c
LEFT JOIN pagamentos p ON c.id = p.contrato_id
GROUP BY c.id, c.numero_contrato, c.valor_total;

-- View: Dashboard executivo
CREATE OR REPLACE VIEW vw_dashboard_executivo AS
SELECT 
    COUNT(*) as total_contratos,
    COUNT(CASE WHEN status = 'ATIVO' THEN 1 END) as contratos_ativos,
    COUNT(CASE WHEN status = 'EM_RENOVACAO' THEN 1 END) as contratos_renovacao,
    COUNT(CASE WHEN status = 'VENCIDO' THEN 1 END) as contratos_vencidos,
    SUM(valor_total) as valor_total_contratos,
    SUM(CASE WHEN status = 'ATIVO' THEN valor_total ELSE 0 END) as valor_contratos_ativos,
    AVG(valor_total) as valor_medio_contratos
FROM contratos;

-- =====================================================
-- TRIGGERS PARA AUDITORIA AUTOMÁTICA
-- =====================================================

-- Função para auditoria automática
CREATE OR REPLACE FUNCTION fn_auditoria() RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        INSERT INTO logs_auditoria (tabela, operacao, registro_id, dados_novos, usuario)
        VALUES (TG_TABLE_NAME, TG_OP, NEW.id, row_to_json(NEW), current_user);
        RETURN NEW;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO logs_auditoria (tabela, operacao, registro_id, dados_anteriores, dados_novos, usuario)
        VALUES (TG_TABLE_NAME, TG_OP, NEW.id, row_to_json(OLD), row_to_json(NEW), current_user);
        RETURN NEW;
    ELSIF TG_OP = 'DELETE' THEN
        INSERT INTO logs_auditoria (tabela, operacao, registro_id, dados_anteriores, usuario)
        VALUES (TG_TABLE_NAME, TG_OP, OLD.id, row_to_json(OLD), current_user);
        RETURN OLD;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- Aplicar triggers de auditoria nas tabelas principais
CREATE TRIGGER tg_auditoria_contratos
    AFTER INSERT OR UPDATE OR DELETE ON contratos
    FOR EACH ROW EXECUTE FUNCTION fn_auditoria();

CREATE TRIGGER tg_auditoria_empresas
    AFTER INSERT OR UPDATE OR DELETE ON empresas
    FOR EACH ROW EXECUTE FUNCTION fn_auditoria();

CREATE TRIGGER tg_auditoria_pagamentos
    AFTER INSERT OR UPDATE OR DELETE ON pagamentos
    FOR EACH ROW EXECUTE FUNCTION fn_auditoria();

CREATE TRIGGER tg_auditoria_notas_fiscais
    AFTER INSERT OR UPDATE OR DELETE ON notas_fiscais
    FOR EACH ROW EXECUTE FUNCTION fn_auditoria();

-- =====================================================
-- FUNÇÃO PARA ATUALIZAR STATUS AUTOMATICAMENTE
-- =====================================================
CREATE OR REPLACE FUNCTION fn_atualizar_status_pagamentos() RETURNS void AS $$
BEGIN
    -- Marcar pagamentos como atrasados
    UPDATE pagamentos 
    SET status = 'ATRASADO' 
    WHERE status = 'PENDENTE' 
    AND data_vencimento < CURRENT_DATE;
    
    -- Log da operação
    INSERT INTO logs_auditoria (tabela, operacao, registro_id, dados_novos, usuario)
    SELECT 'pagamentos', 'UPDATE', id, 
           json_build_object('status', 'ATRASADO', 'data_atualizacao', CURRENT_TIMESTAMP),
           'SISTEMA'
    FROM pagamentos 
    WHERE status = 'ATRASADO' 
    AND data_vencimento < CURRENT_DATE;
END;
$$ LANGUAGE plpgsql;

-- =====================================================
-- DADOS INICIAIS PARA TESTES
-- =====================================================

-- Status válidos para contratos
INSERT INTO processos_contrato (contrato_id, status_anterior, status_atual, acao_realizada, usuario_responsavel, observacoes)
SELECT 
    id, 
    'NOVO', 
    status, 
    'Criação inicial do contrato', 
    responsavel,
    'Processo criado automaticamente na migração'
FROM contratos 
WHERE NOT EXISTS (
    SELECT 1 FROM processos_contrato WHERE contrato_id = contratos.id
);

-- Comentários nas tabelas
COMMENT ON TABLE processos_contrato IS 'Histórico de workflow e mudanças de status dos contratos';
COMMENT ON TABLE pagamentos IS 'Controle de pagamentos e parcelas dos contratos';
COMMENT ON TABLE notas_fiscais IS 'Gestão de notas fiscais vinculadas aos contratos';
COMMENT ON TABLE documentos IS 'Armazenamento de documentos anexos aos contratos';
COMMENT ON TABLE notificacoes IS 'Sistema de notificações in-app para usuários';
COMMENT ON TABLE logs_auditoria IS 'Log de auditoria para rastreamento de todas as operações';

-- =====================================================
-- GRANTS DE PERMISSÃO (ajustar conforme necessário)
-- =====================================================
-- GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO gestcon_user;
-- GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO gestcon_user;
