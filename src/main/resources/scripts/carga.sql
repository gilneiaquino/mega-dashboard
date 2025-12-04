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