package it.traininghelper.dataobject;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;

import java.util.Date;
import java.util.List;

/**
 * Created by warez on 29/05/15.
 */
 @Entity
public class Training {

    @Id private Long id;

    @Index private String user;

    private String nome;

    private String descrizione;

    @Index public Date data;

    @Load private List<Ref<TrainingImage>> images;

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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<Ref<TrainingImage>> getImages() {
        return images;
    }

    public void setImages(List<Ref<TrainingImage>> images) {
        this.images = images;
    }
}
