package it.traininghelper.valueobject;

import java.util.Date;

/**
 * Created by warez on 31/05/15.
 */
public class TrainingImageVO {

    private Long id;
    private Date date;
    private String description;
    private String image;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
