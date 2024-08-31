package mx.infotec.mascotams.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link mx.infotec.mascotams.domain.Mascota} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MascotaDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    @Size(min = 3, max = 50)
    private String nombre;

    @NotNull(message = "must not be null")
    @Max(value = 200)
    private Integer edad;

    @NotNull(message = "must not be null")
    private Float precio;

    private LocalDate fechaNacimiento;

    private byte[] foto;

    private String fotoContentType;

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

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getFotoContentType() {
        return fotoContentType;
    }

    public void setFotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MascotaDTO)) {
            return false;
        }

        MascotaDTO mascotaDTO = (MascotaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mascotaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MascotaDTO{" +
            "id='" + getId() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", edad=" + getEdad() +
            ", precio=" + getPrecio() +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            ", foto='" + getFoto() + "'" +
            "}";
    }
}
