# docnix-sweguer-springboot

-- Criação do banco de dados
CREATE DATABASE maxdoc_simplificado;
USE maxdoc_simplificado;

-- Tabela principal de documentos
CREATE TABLE documento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, -- Identificador único
    titulo VARCHAR(255) NOT NULL, -- Título do documento
    descricao TEXT NOT NULL, -- Descrição do documento
    versao INT NOT NULL, -- Versão do documento
    sigla VARCHAR(50) NOT NULL, -- Sigla identificadora do documento
    fase VARCHAR(20) NOT NULL, -- Fase do documento (validação será feita na aplicação)
    UNIQUE (sigla, versao) -- Combinação única de sigla + versão
);

