-- =========================
-- TENANT
-- =========================
INSERT INTO tenant (codigo, nome, ativo)
SELECT 'empresa-x', 'Empresa X LTDA', 1
    WHERE NOT EXISTS (SELECT 1 FROM tenant WHERE codigo='empresa-x');

-- =========================
-- ADMIN
-- =========================
INSERT INTO usuario (login, senha, nome, perfil, tenant_id, ativo)
SELECT
    'admin',
    '$2a$10$u2pTqNY6gxsxiTaMCjYyQuzafxPLQNhbH0VOSzaUkd/KR/E4.3oX.',
    'Administrador',
    'ADMIN',
    (SELECT id FROM tenant WHERE codigo='empresa-x' LIMIT 1),
  1
WHERE NOT EXISTS (
    SELECT 1
    FROM usuario u
    WHERE u.login='admin'
  AND u.tenant_id = (SELECT id FROM tenant WHERE codigo='empresa-x' LIMIT 1)
    );

UPDATE usuario
SET senha='$2a$10$u2pTqNY6gxsxiTaMCjYyQuzafxPLQNhbH0VOSzaUkd/KR/E4.3oX.'
WHERE login='admin'
  AND tenant_id = (SELECT id FROM tenant WHERE codigo='empresa-x' LIMIT 1);

-- =========================
-- RELATÓRIO
-- =========================
INSERT INTO relatorio (descricao, nome, sql_texto, tipo, tenant_id, data_criacao)
SELECT
    'Lista operações filtradas por data',
    'Relatório de Operações',
    'SELECT * FROM operacao WHERE data_operacao >= :dataInicial AND data_operacao <= :dataFinal',
    'TABELA',
    (SELECT id FROM tenant WHERE codigo='empresa-x' LIMIT 1),
  NOW()
WHERE NOT EXISTS (
    SELECT 1
    FROM relatorio r
    WHERE r.tenant_id = (SELECT id FROM tenant WHERE codigo='empresa-x' LIMIT 1)
  AND r.nome='Relatório de Operações'
    );

-- =========================
-- PARÂMETROS DO RELATÓRIO
-- =========================
INSERT INTO parametro_relatorio (nome, obrigatorio, tipo, relatorio_id)
SELECT
    'dataInicial', b'1', 'DATE',
    (SELECT id FROM relatorio
     WHERE tenant_id=(SELECT id FROM tenant WHERE codigo='empresa-x' LIMIT 1)
     AND nome='Relatório de Operações'
ORDER BY id DESC LIMIT 1)
WHERE NOT EXISTS (
    SELECT 1
    FROM parametro_relatorio pr
    WHERE pr.relatorio_id = (SELECT id FROM relatorio
    WHERE tenant_id=(SELECT id FROM tenant WHERE codigo='empresa-x' LIMIT 1)
  AND nome='Relatório de Operações'
    ORDER BY id DESC LIMIT 1)
  AND pr.nome='dataInicial'
    );

INSERT INTO parametro_relatorio (nome, obrigatorio, tipo, relatorio_id)
SELECT
    'dataFinal', b'1', 'DATE',
    (SELECT id FROM relatorio
     WHERE tenant_id=(SELECT id FROM tenant WHERE codigo='empresa-x' LIMIT 1)
     AND nome='Relatório de Operações'
ORDER BY id DESC LIMIT 1)
WHERE NOT EXISTS (
    SELECT 1
    FROM parametro_relatorio pr
    WHERE pr.relatorio_id = (SELECT id FROM relatorio
    WHERE tenant_id=(SELECT id FROM tenant WHERE codigo='empresa-x' LIMIT 1)
  AND nome='Relatório de Operações'
    ORDER BY id DESC LIMIT 1)
  AND pr.nome='dataFinal'
    );

-- =========================
-- DASHBOARD
-- =========================
INSERT INTO dashboard (nome, descricao, tenant_codigo, criado_por, ativo, data_criacao, data_atualizacao)
SELECT 'Visão Geral', 'Dashboard inicial do sistema', 'empresa-x', 'seed', 1, NOW(), NOW()
    WHERE NOT EXISTS (
  SELECT 1 FROM dashboard
  WHERE tenant_codigo='empresa-x' AND nome='Visão Geral'
);

-- =========================
-- ITENS DO DASHBOARD
-- =========================
-- KPI
INSERT INTO dashboard_item
(dashboard_id, tipo, titulo, ordem, col_span, ativo, relatorio_id, coluna_rotulo, coluna_valor, agregacao, filtro_json, config_visual_json, meta_kpi)
SELECT
    (SELECT id FROM dashboard WHERE tenant_codigo='empresa-x' AND nome='Visão Geral' ORDER BY id DESC LIMIT 1),
  'KPI', 'Total de Operações', 1, 3, b'1',
  (SELECT id FROM relatorio
   WHERE tenant_id=(SELECT id FROM tenant WHERE codigo='empresa-x' LIMIT 1)
     AND nome='Relatório de Operações'
   ORDER BY id DESC LIMIT 1),
  'total', 'valor', 'SUM',
  NULL, NULL, 10000
WHERE NOT EXISTS (
    SELECT 1
    FROM dashboard_item di
    WHERE di.dashboard_id = (SELECT id FROM dashboard WHERE tenant_codigo='empresa-x' AND nome='Visão Geral' ORDER BY id DESC LIMIT 1)
  AND di.ordem=1
    );

-- BARRA por STATUS
INSERT INTO dashboard_item
(dashboard_id, tipo, titulo, ordem, col_span, ativo, relatorio_id, coluna_rotulo, coluna_valor, agregacao, filtro_json, config_visual_json, meta_kpi)
SELECT
    (SELECT id FROM dashboard WHERE tenant_codigo='empresa-x' AND nome='Visão Geral' ORDER BY id DESC LIMIT 1),
  'BARRA', 'Operações por Status', 2, 6, b'1',
  (SELECT id FROM relatorio
   WHERE tenant_id=(SELECT id FROM tenant WHERE codigo='empresa-x' LIMIT 1)
     AND nome='Relatório de Operações'
   ORDER BY id DESC LIMIT 1),
  'status', 'id', 'COUNT',
  NULL, NULL, NULL
WHERE NOT EXISTS (
    SELECT 1
    FROM dashboard_item di
    WHERE di.dashboard_id = (SELECT id FROM dashboard WHERE tenant_codigo='empresa-x' AND nome='Visão Geral' ORDER BY id DESC LIMIT 1)
  AND di.ordem=2
    );

-- =========================
-- OPERAÇÕES (com status/categoria)
-- =========================
INSERT INTO operacao (data_operacao, valor, descricao, status, categoria, tenant_id)
SELECT '2025-01-10', 1500.00, 'Operação teste', 'EM_DIA', 'CREDITO',
       (SELECT id FROM tenant WHERE codigo='empresa-x' LIMIT 1)
WHERE NOT EXISTS (
    SELECT 1 FROM operacao
    WHERE tenant_id=(SELECT id FROM tenant WHERE codigo='empresa-x' LIMIT 1)
  AND descricao='Operação teste'
    );

INSERT INTO operacao (data_operacao, valor, descricao, status, categoria, tenant_id)
SELECT '2025-02-20', 2500.00, 'Operação cliente X', 'ATRASADO', 'CREDITO',
       (SELECT id FROM tenant WHERE codigo='empresa-x' LIMIT 1)
WHERE NOT EXISTS (
    SELECT 1 FROM operacao
    WHERE tenant_id=(SELECT id FROM tenant WHERE codigo='empresa-x' LIMIT 1)
  AND descricao='Operação cliente X'
    );

INSERT INTO operacao (data_operacao, valor, descricao, status, categoria, tenant_id)
SELECT '2025-02-25', 300.00, 'Tarifa pacote', 'LIQUIDADO', 'TARIFA',
       (SELECT id FROM tenant WHERE codigo='empresa-x' LIMIT 1)
WHERE NOT EXISTS (
    SELECT 1 FROM operacao
    WHERE tenant_id=(SELECT id FROM tenant WHERE codigo='empresa-x' LIMIT 1)
  AND descricao='Tarifa pacote'
    );

INSERT INTO operacao (data_operacao, valor, descricao, status, categoria, tenant_id)
SELECT '2025-03-05', 980.00, 'Operação débito', 'EM_DIA', 'DEBITO',
       (SELECT id FROM tenant WHERE codigo='empresa-x' LIMIT 1)
WHERE NOT EXISTS (
    SELECT 1 FROM operacao
    WHERE tenant_id=(SELECT id FROM tenant WHERE codigo='empresa-x' LIMIT 1)
  AND descricao='Operação débito'
    );
