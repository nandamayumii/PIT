-- Script de criação do banco do Catálogo (MySQL 8+)
SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS catalogo
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE catalogo;

CREATE TABLE IF NOT EXISTS item_midia (
    id             INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    titulo         VARCHAR(255) NOT NULL,
    autor_diretor  VARCHAR(255),
    ano_lancamento INT,
    genero         VARCHAR(100),
    sinopse        TEXT,
    tipo_midia     VARCHAR(50)  NOT NULL,   -- 'Livro', 'Filme' ou 'Série'
    nota           INT,                     -- extensão: avaliação de 0 a 5 estrelas
    capa_url       VARCHAR(500),            -- extensão: link externo da imagem de capa
    CONSTRAINT ck_nota CHECK (nota IS NULL OR nota BETWEEN 0 AND 5)
);

-- Dados de exemplo (opcional)
INSERT INTO item_midia (titulo, autor_diretor, ano_lancamento, genero, sinopse, tipo_midia, nota) VALUES
('Dom Casmurro', 'Machado de Assis', 1899, 'Romance', 'Bentinho narra suas memórias e a dúvida sobre a fidelidade de Capitu.', 'Livro', 5),
('Cidade de Deus', 'Fernando Meirelles', 2002, 'Drama', 'A vida de jovens numa comunidade do Rio de Janeiro entre as décadas de 60 e 80.', 'Filme', 5),
('Dark', 'Baran bo Odar', 2017, 'Ficção', 'Desaparecimentos numa cidade alemã revelam segredos que atravessam gerações.', 'Série', 4);
