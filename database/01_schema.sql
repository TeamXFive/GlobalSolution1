-- ============================================================================
--  PROJETO: Tem Satélite Passando Por Mim Agora?  (Global Solution FIAP)
--  SCRIPT:  01_schema.sql  -  Criação das tabelas (DDL) - PostgreSQL
-- ============================================================================
--  Entidades:  usuarios, localizacoes, satelites, passagens, alertas,
--              leituras_sensor
--
--  Relacionamentos:
--    Usuario      1:N  Localizacao
--    Localizacao  1:N  Passagem
--    Satelite     1:N  Passagem
--    Usuario      1:N  Alerta
--    Localizacao  1:N  LeituraSensor
-- ============================================================================

-- Recria o esquema do zero (facilita reexecução em ambiente acadêmico).
DROP TABLE IF EXISTS leituras_sensor CASCADE;
DROP TABLE IF EXISTS alertas         CASCADE;
DROP TABLE IF EXISTS passagens       CASCADE;
DROP TABLE IF EXISTS localizacoes    CASCADE;
DROP TABLE IF EXISTS satelites       CASCADE;
DROP TABLE IF EXISTS usuarios        CASCADE;

-- ----------------------------------------------------------------------------
-- USUARIOS
-- ----------------------------------------------------------------------------
CREATE TABLE usuarios (
    id          BIGSERIAL    PRIMARY KEY,
    nome        VARCHAR(120) NOT NULL,
    email       VARCHAR(160) NOT NULL UNIQUE,
    -- senha sempre armazenada com hash BCrypt (nunca em texto puro)
    senha_hash  VARCHAR(120) NOT NULL,
    criado_em   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ----------------------------------------------------------------------------
-- SATELITES
-- ----------------------------------------------------------------------------
CREATE TABLE satelites (
    id            BIGSERIAL    PRIMARY KEY,
    nome          VARCHAR(120) NOT NULL,
    tipo          VARCHAR(120) NOT NULL,
    regiao_atual  VARCHAR(160),
    visivel       BOOLEAN      NOT NULL DEFAULT FALSE
);

-- ----------------------------------------------------------------------------
-- LOCALIZACOES  (cada localização pertence a um usuário)
-- ----------------------------------------------------------------------------
CREATE TABLE localizacoes (
    id         BIGSERIAL    PRIMARY KEY,
    usuario_id BIGINT       NOT NULL,
    nome       VARCHAR(160) NOT NULL,         -- ex.: "Niterói - RJ"
    cidade     VARCHAR(120),
    estado     VARCHAR(2),
    latitude   DECIMAL(9,6),
    longitude  DECIMAL(9,6),
    CONSTRAINT fk_localizacao_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuarios (id) ON DELETE CASCADE
);

-- ----------------------------------------------------------------------------
-- PASSAGENS  (passagem de um satélite sobre uma localização)
-- ----------------------------------------------------------------------------
CREATE TABLE passagens (
    id             BIGSERIAL   PRIMARY KEY,
    satelite_id    BIGINT      NOT NULL,
    localizacao_id BIGINT      NOT NULL,
    horario        VARCHAR(5)  NOT NULL,      -- "20:42"
    direcao        VARCHAR(60) NOT NULL,      -- "Noroeste → Sudeste"
    visivel        BOOLEAN     NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_passagem_satelite
        FOREIGN KEY (satelite_id)    REFERENCES satelites (id)    ON DELETE CASCADE,
    CONSTRAINT fk_passagem_localizacao
        FOREIGN KEY (localizacao_id) REFERENCES localizacoes (id) ON DELETE CASCADE
);

-- ----------------------------------------------------------------------------
-- ALERTAS  (alerta de observação gerado para um usuário)
-- ----------------------------------------------------------------------------
CREATE TABLE alertas (
    id         BIGSERIAL     PRIMARY KEY,
    usuario_id BIGINT        NOT NULL,
    mensagem   VARCHAR(300)  NOT NULL,
    ativo      BOOLEAN       NOT NULL DEFAULT TRUE,
    criado_em  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_alerta_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuarios (id) ON DELETE CASCADE
);

-- ----------------------------------------------------------------------------
-- LEITURAS_SENSOR  (simulação IoT: condição do céu por localização)
-- ----------------------------------------------------------------------------
CREATE TABLE leituras_sensor (
    id                    BIGSERIAL  PRIMARY KEY,
    localizacao_id        BIGINT     NOT NULL,
    temperatura           INTEGER    NOT NULL,   -- °C
    chovendo              BOOLEAN    NOT NULL,
    nebulosidade          INTEGER    NOT NULL,   -- % de nuvens (0-100)
    luminosidade          INTEGER    NOT NULL,   -- % poluição luminosa (0-100)
    observacao_favoravel  BOOLEAN    NOT NULL,
    lido_em               TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_leitura_localizacao
        FOREIGN KEY (localizacao_id) REFERENCES localizacoes (id) ON DELETE CASCADE,
    CONSTRAINT ck_nebulosidade  CHECK (nebulosidade BETWEEN 0 AND 100),
    CONSTRAINT ck_luminosidade  CHECK (luminosidade BETWEEN 0 AND 100)
);

-- Índices para acelerar as consultas mais usadas pela API.
CREATE INDEX idx_localizacoes_usuario   ON localizacoes  (usuario_id);
CREATE INDEX idx_passagens_localizacao  ON passagens     (localizacao_id);
CREATE INDEX idx_passagens_satelite     ON passagens     (satelite_id);
CREATE INDEX idx_alertas_usuario        ON alertas       (usuario_id);
CREATE INDEX idx_leituras_localizacao   ON leituras_sensor (localizacao_id);
