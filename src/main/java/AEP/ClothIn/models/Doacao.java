package AEP.ClothIn.models;

import AEP.ClothIn.enums.StatusDoacao;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "doacoes")
public class Doacao {

    @Id
    private String id;

    private String doadorId;
    private String responsavelId;

    private LocalDate data;
    private StatusDoacao status;

    private List<RoupaDoacao> roupas;

    public Doacao() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoadorId() {
        return doadorId;
    }

    public void setDoadorId(String doadorId) {
        this.doadorId = doadorId;
    }

    public String getResponsavelId() {
        return responsavelId;
    }

    public void setResponsavelId(String responsavelId) {
        this.responsavelId = responsavelId;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public StatusDoacao getStatus() {
        return status;
    }

    public void setStatus(StatusDoacao status) {
        this.status = status;
    }

    public List<RoupaDoacao> getRoupas() {
        return roupas;
    }

    public void setRoupas(List<RoupaDoacao> roupas) {
        this.roupas = roupas;
    }
}
