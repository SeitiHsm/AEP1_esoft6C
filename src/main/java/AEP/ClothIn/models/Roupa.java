package AEP.ClothIn.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roupas")
public class Roupa {

    @Id
    private String id;

    private String tipo;
    private String tamanho;
    private String cor;
    private String estado;

    public Roupa(){
    }

    public Roupa(String id, String tipo, String tamanho, String cor, String estado) {
        this.id = id;
        this.tipo = tipo;
        this.tamanho = tamanho;
        this.cor = cor;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
