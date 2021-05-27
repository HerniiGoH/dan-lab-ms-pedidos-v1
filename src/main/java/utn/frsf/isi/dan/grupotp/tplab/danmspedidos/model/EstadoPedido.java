package utn.frsf.isi.dan.grupotp.tplab.danmspedidos.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class EstadoPedido implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String estado;

    public EstadoPedido() {
    }

    public EstadoPedido(Integer id, String estado) {
        this.id = id;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EstadoPedido that = (EstadoPedido) o;
        return getId().equals(that.getId()) && getEstado().equals(that.getEstado());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEstado());
    }
}
