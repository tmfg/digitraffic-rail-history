package fi.livi.trainhistoryupdater.entities;

import jakarta.persistence.Entity;
import java.io.Serializable;

@Entity
public class Composition extends JsonEntity implements Serializable {
    public Composition() {
    }
}
