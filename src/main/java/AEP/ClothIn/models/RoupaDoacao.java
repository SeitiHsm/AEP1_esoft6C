package AEP.ClothIn.models;

public class RoupaDoacao {

    private String tipo;
    private String tamanho;
    private String cor;
    private String estado;

    public RoupaDoacao() {
    }

    public RoupaDoacao(String tipo, String tamanho, String cor, String estado) {
        this.tipo = tipo;
        this.tamanho = tamanho;
        this.cor = cor;
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
