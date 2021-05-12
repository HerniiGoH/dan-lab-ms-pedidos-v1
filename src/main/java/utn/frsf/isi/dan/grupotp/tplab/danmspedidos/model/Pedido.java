package utn.frsf.isi.dan.grupotp.tplab.danmspedidos.model;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class Pedido {
    private Integer id;
    private Instant fechaPedido;
    private EstadoPedido estadoPedido;
    private List<DetallePedido> detallePedido;
    private Obra obra;

    public Pedido() {
    }

    public Pedido(Integer id, Instant fechaPedido, EstadoPedido estadoPedido, List<DetallePedido> detallePedido, Obra obra) {
        this.id = id;
        this.fechaPedido = fechaPedido;
        this.estadoPedido = estadoPedido;
        this.detallePedido = detallePedido;
        this.obra = obra;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Instant fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public EstadoPedido getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public List<DetallePedido> getDetallePedido() {
        return detallePedido;
    }

    public void setDetallePedido(List<DetallePedido> detallePedido) {
        this.detallePedido = detallePedido;
    }

    public Obra getObra() {
        return obra;
    }

    public void setObra(Obra obra) {
        this.obra = obra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return getId().equals(pedido.getId()) && getFechaPedido().equals(pedido.getFechaPedido()) && getEstadoPedido().equals(pedido.getEstadoPedido()) && getDetallePedido().equals(pedido.getDetallePedido()) && getObra().equals(pedido.getObra());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFechaPedido(), getEstadoPedido(), getDetallePedido(), getObra());
    }
}
