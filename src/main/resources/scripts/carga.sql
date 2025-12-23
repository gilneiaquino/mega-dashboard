UPDATE usuario
SET senha = '$2a$10$u2pTqNY6gxsxiTaMCjYyQuzafxPLQNhbH0VOSzaUkd/KR/E4.3oX.'
WHERE login = 'admin';


INSERT INTO tenant (codigo, nome, ativo)
VALUES ('empresa-x', 'Empresa X LTDA', 1);

-- 2) Criar usuário ADMIN vinculado ao tenant
-- ATENÇÃO: troque <HASH_BCRYPT> pelo hash gerado da senha (ex: admin123)
INSERT INTO usuario (login, senha, nome, perfil, tenant_id, ativo)
VALUES (
    'admin',
    '$2a$10$u2pTqNY6gxsxiTaMCjYyQuzafxPLQNhbH0VOSzaUkd/KR/E4.3oX.',
    'Administrador',
    'ADMIN',
    (SELECT id FROM tenant WHERE codigo = 'empresa-x'),
    1
);

INSERT INTO operacao (data_operacao, valor, descricao, tenant_id)
VALUES
    ('2025-01-10', 1500.00, 'Operação teste', 1),
    ('2025-02-20', 2500.00, 'Operação cliente X', 1);



-- ==========================
-- SEED EXTRA (EXEMPLOS)
-- ==========================

-- Variáveis base
SET @TENANT_ID := (SELECT id FROM tenant WHERE codigo = 'empresa-x' LIMIT 1);

-- Relatório base
INSERT INTO relatorio (descricao, nome, sql_texto, tipo, tenant_id, data_criacao)
SELECT
    'Lista operações filtradas por data',
    'Relatório de Operações',
    'SELECT * FROM operacao WHERE data_operacao >= :dataInicial AND data_operacao <= :dataFinal',
    'TABELA',
    @TENANT_ID,
    NOW()
    WHERE NOT EXISTS (
  SELECT 1 FROM relatorio
  WHERE tenant_id=@TENANT_ID AND nome='Relatório de Operações'
);

SET @RELATORIO_ID := (
  SELECT id FROM relatorio
  WHERE tenant_id=@TENANT_ID AND nome='Relatório de Operações'
  ORDER BY id DESC
  LIMIT 1
);


-- Dashboard Visão Geral
INSERT INTO dashboard (nome, descricao, tenant_codigo, criado_por, ativo, data_criacao, data_atualizacao)
SELECT
    'Visão Geral',
    'Dashboard inicial do sistema',
    'empresa-x',
    'seed',
    1,
    NOW(),
    NOW()
    WHERE NOT EXISTS (
  SELECT 1 FROM dashboard
  WHERE tenant_codigo='empresa-x' AND nome='Visão Geral'
);

SET @DASHBOARD_ID := (
  SELECT id FROM dashboard
  WHERE tenant_codigo='empresa-x' AND nome='Visão Geral'
  LIMIT 1
);

-- KPI Total
INSERT INTO dashboard_item
(dashboard_id, tipo, titulo, ordem, col_span, ativo,
 relatorio_id, coluna_rotulo, coluna_valor, agregacao, filtro_json, config_visual_json, meta_kpi)
SELECT
    @DASHBOARD_ID, 'KPI', 'Total de Operações', 1, 3, b'1',
    @RELATORIO_ID, 'total', 'valor', 'SUM', NULL, NULL, 10000
    WHERE NOT EXISTS (
  SELECT 1 FROM dashboard_item
  WHERE dashboard_id=@DASHBOARD_ID AND ordem=1
);

-- Gráfico de Barra
INSERT INTO dashboard_item
(dashboard_id, tipo, titulo, ordem, col_span, ativo,
 relatorio_id, coluna_rotulo, coluna_valor, agregacao, filtro_json, config_visual_json, meta_kpi)
SELECT
    @DASHBOARD_ID, 'BARRA', 'Operações por Situação', 2, 6, b'1',
    @RELATORIO_ID, 'situacao', 'qtd', 'COUNT', NULL, NULL, NULL
    WHERE NOT EXISTS (
  SELECT 1 FROM dashboard_item
  WHERE dashboard_id=@DASHBOARD_ID AND ordem=2
);

-- Operações exemplo
INSERT INTO operacao (data_operacao, valor, descricao, tenant_id)
SELECT '2025-01-10', 1500.00, 'Operação teste', @TENANT_ID
    WHERE NOT EXISTS (
  SELECT 1 FROM operacao WHERE descricao='Operação teste' AND tenant_id=@TENANT_ID
);

INSERT INTO operacao (data_operacao, valor, descricao, tenant_id)
SELECT '2025-02-20', 2500.00, 'Operação cliente X', @TENANT_ID
    WHERE NOT EXISTS (
  SELECT 1 FROM operacao WHERE descricao='Operação cliente X' AND tenant_id=@TENANT_ID
);


INSERT INTO parametro_relatorio (nome, obrigatorio, tipo, relatorio_id)
SELECT 'dataInicial', b'1', 'DATE', @RELATORIO_ID
    WHERE NOT EXISTS (
  SELECT 1 FROM parametro_relatorio
  WHERE relatorio_id = @RELATORIO_ID AND nome = 'dataInicial'
);

INSERT INTO parametro_relatorio (nome, obrigatorio, tipo, relatorio_id)
SELECT 'dataFinal', b'1', 'DATE', @RELATORIO_ID
    WHERE NOT EXISTS (
  SELECT 1 FROM parametro_relatorio
  WHERE relatorio_id = @RELATORIO_ID AND nome = 'dataFinal'
);
