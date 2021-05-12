package utn.frsf.isi.dan.grupotp.tplab.danmspedidos.model;

import java.util.Objects;

public class Obra {
    private Integer id;
    private String descripcion;

    public Obra() {
    }

    public Obra(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Obra obra = (Obra) o;
        return getId().equals(obra.getId()) && getDescripcion().equals(obra.getDescripcion());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescripcion());
    }
}
