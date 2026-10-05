package br.edu.catalogo.model;

/**
 * Entidade principal do catálogo: representa um livro, filme ou série.
 * Classe de modelo (POJO) com atributos privados (encapsulamento),
 * getters e setters.
 */
public class ItemMidia {

    /** Tipos de mídia aceitos pelo sistema. */
    public static final String[] TIPOS = {"Livro", "Filme", "Série"};

    private Integer id;
    private String titulo;
    private String autorDiretor;
    private Integer anoLancamento;
    private String genero;
    private String sinopse;
    private String tipoMidia;
    private Integer nota;      // 0 a 5 (extensão: avaliação)
    private String capaUrl;    // link externo da capa (extensão)

    public ItemMidia() {
    }

    public ItemMidia(Integer id, String titulo, String autorDiretor, Integer anoLancamento,
                     String genero, String sinopse, String tipoMidia, Integer nota, String capaUrl) {
        this.id = id;
        this.titulo = titulo;
        this.autorDiretor = autorDiretor;
        this.anoLancamento = anoLancamento;
        this.genero = genero;
        this.sinopse = sinopse;
        this.tipoMidia = tipoMidia;
        this.nota = nota;
        this.capaUrl = capaUrl;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutorDiretor() { return autorDiretor; }
    public void setAutorDiretor(String autorDiretor) { this.autorDiretor = autorDiretor; }

    public Integer getAnoLancamento() { return anoLancamento; }
    public void setAnoLancamento(Integer anoLancamento) { this.anoLancamento = anoLancamento; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getSinopse() { return sinopse; }
    public void setSinopse(String sinopse) { this.sinopse = sinopse; }

    public String getTipoMidia() { return tipoMidia; }
    public void setTipoMidia(String tipoMidia) { this.tipoMidia = tipoMidia; }

    public Integer getNota() { return nota; }
    public void setNota(Integer nota) { this.nota = nota; }

    public String getCapaUrl() { return capaUrl; }
    public void setCapaUrl(String capaUrl) { this.capaUrl = capaUrl; }
}
