package utn.frsf.isi.dan.grupotp.tplab.danmspedidos.model;

import java.util.Objects;

public class DetallePedido {
    private Integer id;
    private Integer cantidad;
    private Double precio;
    private Producto producto;

    public DetallePedido() {
    }

    public DetallePedido(Integer id, Integer cantidad, Double precio, Producto producto) {
        this.id = id;
        this.cantidad = cantidad;
        this.precio = precio;
        this.producto = producto;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetallePedido that = (DetallePedido) o;
        return getId().equals(that.getId()) && getCantidad().equals(that.getCantidad()) && getPrecio().equals(that.getPrecio()) && getProducto().equals(that.getProducto());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCantidad(), getPrecio(), getProducto());
    }
}
