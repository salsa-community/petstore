package mx.infotec.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link mx.infotec.domain.Tarea} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TareaDTO implements Serializable {

    private String id;

    private String nombre;

    private String descripcion;

    private Instant fechaLimite;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(Instant fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TareaDTO)) {
            return false;
        }

        TareaDTO tareaDTO = (TareaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tareaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TareaDTO{" +
            "id='" + getId() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", fechaLimite='" + getFechaLimite() + "'" +
            "}";
    }
}
