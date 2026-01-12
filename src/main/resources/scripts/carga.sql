-- Tenant
INSERT INTO tenant (codigo, nome, ativo)
SELECT 'empresa-x', 'Empresa X LTDA', 1
    WHERE NOT EXISTS (SELECT 1 FROM tenant WHERE codigo='empresa-x');

SET @TENANT_ID := (SELECT id FROM tenant WHERE codigo='empresa-x' LIMIT 1);

-- Admin
INSERT INTO usuario (login, senha, nome, perfil, tenant_id, ativo)
SELECT 'admin',
       '$2a$10$u2pTqNY6gxsxiTaMCjYyQuzafxPLQNhbH0VOSzaUkd/KR/E4.3oX.',
       'Administrador',
       'ADMIN',
       @TENANT_ID,
       1
    WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE login='admin' AND tenant_id=@TENANT_ID);

UPDATE usuario
SET senha='$2a$10$u2pTqNY6gxsxiTaMCjYyQuzafxPLQNhbH0VOSzaUkd/KR/E4.3oX.'
WHERE login='admin' AND tenant_id=@TENANT_ID;

-- Relatório
INSERT INTO relatorio (descricao, nome, sql_texto, tipo, tenant_id, data_criacao)
SELECT 'Lista operações filtradas por data',
       'Relatório de Operações',
       'SELECT * FROM operacao WHERE data_operacao >= :dataInicial AND data_operacao <= :dataFinal',
       'TABELA',
       @TENANT_ID,
       NOW()
    WHERE NOT EXISTS (SELECT 1 FROM relatorio WHERE tenant_id=@TENANT_ID AND nome='Relatório de Operações');

SET @RELATORIO_ID := (
  SELECT id FROM relatorio
  WHERE tenant_id=@TENANT_ID AND nome='Relatório de Operações'
  ORDER BY id DESC
  LIMIT 1
);

-- Parâmetros do relatório
INSERT INTO parametro_relatorio (nome, obrigatorio, tipo, relatorio_id)
SELECT 'dataInicial', b'1', 'DATE', @RELATORIO_ID
    WHERE NOT EXISTS (SELECT 1 FROM parametro_relatorio WHERE relatorio_id=@RELATORIO_ID AND nome='dataInicial');

INSERT INTO parametro_relatorio (nome, obrigatorio, tipo, relatorio_id)
SELECT 'dataFinal', b'1', 'DATE', @RELATORIO_ID
    WHERE NOT EXISTS (SELECT 1 FROM parametro_relatorio WHERE relatorio_id=@RELATORIO_ID AND nome='dataFinal');

-- Dashboard
INSERT INTO dashboard (nome, descricao, tenant_codigo, criado_por, ativo, data_criacao, data_atualizacao)
SELECT 'Visão Geral', 'Dashboard inicial do sistema', 'empresa-x', 'seed', 1, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM dashboard WHERE tenant_codigo='empresa-x' AND nome='Visão Geral');

SET @DASHBOARD_ID := (
  SELECT id FROM dashboard
  WHERE tenant_codigo='empresa-x' AND nome='Visão Geral'
  ORDER BY id DESC
  LIMIT 1
);

-- Itens do dashboard
INSERT INTO dashboard_item
(dashboard_id, tipo, titulo, ordem, col_span, ativo, relatorio_id, coluna_rotulo, coluna_valor, agregacao, filtro_json, config_visual_json, meta_kpi)
SELECT @DASHBOARD_ID, 'KPI', 'Total de Operações', 1, 3, b'1', @RELATORIO_ID, 'total', 'valor', 'SUM', NULL, NULL, 10000
    WHERE NOT EXISTS (SELECT 1 FROM dashboard_item WHERE dashboard_id=@DASHBOARD_ID AND ordem=1);

INSERT INTO dashboard_item
(dashboard_id, tipo, titulo, ordem, col_span, ativo, relatorio_id, coluna_rotulo, coluna_valor, agregacao, filtro_json, config_visual_json, meta_kpi)
SELECT @DASHBOARD_ID, 'BARRA', 'Operações por Situação', 2, 6, b'1', @RELATORIO_ID, 'situacao', 'qtd', 'COUNT', NULL, NULL, NULL
    WHERE NOT EXISTS (SELECT 1 FROM dashboard_item WHERE dashboard_id=@DASHBOARD_ID AND ordem=2);

-- Operações
INSERT INTO operacao (data_operacao, valor, descricao, tenant_id)
SELECT '2025-01-10', 1500.00, 'Operação teste', @TENANT_ID
    WHERE NOT EXISTS (SELECT 1 FROM operacao WHERE tenant_id=@TENANT_ID AND descricao='Operação teste');

INSERT INTO operacao (data_operacao, valor, descricao, tenant_id)
SELECT '2025-02-20', 2500.00, 'Operação cliente X', @TENANT_ID
    WHERE NOT EXISTS (SELECT 1 FROM operacao WHERE tenant_id=@TENANT_ID AND descricao='Operação cliente X');
