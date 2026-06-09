-- ============================================================================
--  SCRIPT: 02_insert_dados.sql  -  Dados de exemplo (DML) - PostgreSQL
--  Os mesmos dados mockados usados no app mobile.
-- ============================================================================

-- ---------------------------------------------------------------------------
-- USUARIOS
-- A senha em texto puro do usuário de teste é "123456".
-- O hash abaixo é o BCrypt correspondente a "123456" (mesmo gerado pela API).
-- ---------------------------------------------------------------------------
INSERT INTO usuarios (nome, email, senha_hash) VALUES
    ('Usuário Teste', 'teste@email.com',
     '$2b$10$0tku5JtEoBzKdUqycu0/yu98B/eB7Qq9IXY5gaIPD.4fHyeEF87KW'),
    ('Caroline Nunes', 'carol@email.com',
     '$2b$10$0tku5JtEoBzKdUqycu0/yu98B/eB7Qq9IXY5gaIPD.4fHyeEF87KW');

-- ---------------------------------------------------------------------------
-- SATELITES
-- ---------------------------------------------------------------------------
INSERT INTO satelites (nome, tipo, regiao_atual, visivel) VALUES
    ('ISS',           'Estação Espacial',                    'Sobre o Oceano Atlântico Sul', TRUE),
    ('Sentinel-2',    'Observação da Terra',                 'Sobre a Europa Central',       FALSE),
    ('Landsat 8',     'Monitoramento Ambiental',             'Sobre a América do Norte',     FALSE),
    ('Amazonia-1',    'Satélite Brasileiro de Observação',   'Sobre a Floresta Amazônica',   FALSE),
    ('Hubble',        'Telescópio Espacial',                 'Sobre o Oceano Pacífico',      FALSE),
    ('Starlink-1130', 'Internet por Satélite',               'Sobre a África Ocidental',     TRUE),
    ('NOAA-19',       'Satélite Meteorológico',              'Sobre a Ásia Oriental',        FALSE),
    ('CBERS-04A',     'Observação da Terra (Brasil-China)',  'Sobre a Oceania',              FALSE);

-- ---------------------------------------------------------------------------
-- LOCALIZACOES  (do usuário de teste, id = 1)
-- ---------------------------------------------------------------------------
INSERT INTO localizacoes (usuario_id, nome, cidade, estado, latitude, longitude) VALUES
    (1, 'Niterói - RJ',        'Niterói',        'RJ', -22.883300, -43.103600),
    (1, 'Rio de Janeiro - RJ', 'Rio de Janeiro', 'RJ', -22.906800, -43.172900),
    (2, 'São Paulo - SP',      'São Paulo',      'SP', -23.550500, -46.633300);

-- ---------------------------------------------------------------------------
-- PASSAGENS  (sobre Niterói - RJ, localizacao_id = 1)
-- ---------------------------------------------------------------------------
INSERT INTO passagens (satelite_id, localizacao_id, horario, direcao, visivel) VALUES
    (1, 1, '20:42', 'Noroeste → Sudeste', TRUE),
    (2, 1, '21:15', 'Norte → Sul',        FALSE),
    (3, 1, '22:05', 'Oeste → Leste',      FALSE),
    (4, 1, '23:10', 'Sul → Norte',        FALSE);

-- ---------------------------------------------------------------------------
-- ALERTAS
-- ---------------------------------------------------------------------------
INSERT INTO alertas (usuario_id, mensagem, ativo) VALUES
    (1, 'A ISS passará sobre sua região às 20:42. Condições favoráveis para observação.', TRUE),
    (1, 'Starlink-1130 passará às 00:20. Céu parcialmente favorável.', FALSE);

-- ---------------------------------------------------------------------------
-- LEITURAS_SENSOR  (simulação IoT sobre Niterói - RJ, localizacao_id = 1)
-- Regra: favorável = não chove E nebulosidade < 40 E luminosidade < 50
-- ---------------------------------------------------------------------------
INSERT INTO leituras_sensor
    (localizacao_id, temperatura, chovendo, nebulosidade, luminosidade, observacao_favoravel) VALUES
    (1, 24, FALSE, 20, 18, TRUE),
    (1, 23, FALSE, 35, 40, TRUE),
    (1, 22, TRUE,  80, 60, FALSE);
