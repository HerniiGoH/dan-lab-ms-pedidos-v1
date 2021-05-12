package utn.frsf.isi.dan.grupotp.tplab.danmspedidos.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.Objects;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class DetallePedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer cantidad;
    private Double precio;
    @OneToOne
    private Producto producto;
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    private Pedido pedido;

    public DetallePedido() {
    }

    public DetallePedido(Integer id, Integer cantidad, Double precio, Producto producto, Pedido pedido) {
        this.id = id;
        this.cantidad = cantidad;
        this.precio = precio;
        this.producto = producto;
        this.pedido = pedido;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetallePedido that = (DetallePedido) o;
        return getId().equals(that.getId()) && getCantidad().equals(that.getCantidad()) && getPrecio().equals(that.getPrecio()) && getProducto().equals(that.getProducto()) && getPedido().equals(that.getPedido());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCantidad(), getPrecio(), getProducto(), getPedido());
    }
}
