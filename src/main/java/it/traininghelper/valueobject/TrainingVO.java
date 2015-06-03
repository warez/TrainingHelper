package it.traininghelper.valueobject;

import java.util.Date;

/**
 * Created by warez on 31/05/15.
 */
public class TrainingVO {

    private Long id;
    private String nome;
    private String descrizione;
    public Date data;

    private TrainingImageVO[] images;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public TrainingImageVO[] getImages() {
        return images;
    }

    public void setImages(TrainingImageVO[] images) {
        this.images = images;
    }
}
