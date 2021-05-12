package utn.frsf.isi.dan.grupotp.tplab.danmspedidos.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String descripcion;
    private Double precio;

    public Producto() {
    }

    public Producto(Integer id, String descripcion, Double precio) {
        this.id = id;
        this.descripcion = descripcion;
        this.precio = precio;
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

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return getId().equals(producto.getId()) && getDescripcion().equals(producto.getDescripcion()) && getPrecio().equals(producto.getPrecio());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescripcion(), getPrecio());
    }
}
