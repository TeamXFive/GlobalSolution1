-- ============================================================================
--  SCRIPT: 03_consultas.sql  -  Consultas SQL de simulação - PostgreSQL
--  Demonstra as consultas básicas exigidas no projeto.
-- ============================================================================

-- 1) Buscar TODOS os satélites
SELECT id, nome, tipo, regiao_atual, visivel
FROM satelites
ORDER BY nome;

-- 2) Buscar as LOCALIZAÇÕES de um usuário (ex.: usuário de teste, id = 1)
SELECT l.id, l.nome, l.cidade, l.estado, l.latitude, l.longitude
FROM localizacoes l
WHERE l.usuario_id = 1
ORDER BY l.nome;

-- 3) Buscar as PRÓXIMAS PASSAGENS por localização (ex.: localizacao_id = 1)
SELECT p.id,
       s.nome  AS satelite,
       s.tipo  AS tipo_satelite,
       p.horario,
       p.direcao,
       p.visivel
FROM passagens p
JOIN satelites s ON s.id = p.satelite_id
WHERE p.localizacao_id = 1
ORDER BY p.horario;

-- 4) Buscar os ALERTAS ATIVOS de um usuário (ex.: usuário id = 1)
SELECT a.id, a.mensagem, a.criado_em
FROM alertas a
WHERE a.usuario_id = 1
  AND a.ativo = TRUE
ORDER BY a.criado_em DESC;

-- 5) Buscar as LEITURAS DOS SENSORES de uma localização (ex.: localizacao_id = 1)
SELECT ls.id,
       ls.temperatura,
       ls.chovendo,
       ls.nebulosidade,
       ls.luminosidade,
       ls.observacao_favoravel,
       ls.lido_em
FROM leituras_sensor ls
WHERE ls.localizacao_id = 1
ORDER BY ls.lido_em DESC;

-- ----------------------------------------------------------------------------
-- Consultas extras (úteis para a apresentação)
-- ----------------------------------------------------------------------------

-- Satélites visíveis agora
SELECT nome, regiao_atual FROM satelites WHERE visivel = TRUE;

-- Passagens com TODOS os dados (satélite + localização + usuário dono)
SELECT u.nome AS usuario, l.nome AS localizacao, s.nome AS satelite,
       p.horario, p.direcao, p.visivel
FROM passagens p
JOIN satelites    s ON s.id = p.satelite_id
JOIN localizacoes l ON l.id = p.localizacao_id
JOIN usuarios     u ON u.id = l.usuario_id
ORDER BY u.nome, p.horario;

-- Última leitura favorável de cada localização
SELECT localizacao_id, MAX(lido_em) AS ultima_leitura_favoravel
FROM leituras_sensor
WHERE observacao_favoravel = TRUE
GROUP BY localizacao_id;
