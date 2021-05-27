package utn.frsf.isi.dan.grupotp.tplab.danmspedidos.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.standard.DateTimeFormatterFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Objects;

@Entity
@JsonIdentityInfo(scope = Pedido.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Pedido implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime fechaPedido;
    @OneToOne
    private EstadoPedido estadoPedido;
    @JsonIdentityReference
    @OneToMany(mappedBy = "pedido")
    private List<DetallePedido> detallePedido;
    @Transient
    private Obra obra;
    private Integer obraId;

    public Pedido() {
    }

    public Pedido(Integer id, LocalDateTime fechaPedido, EstadoPedido estadoPedido, List<DetallePedido> detallePedido, Obra obra, Integer obraId) {
        this.id = id;
        this.fechaPedido = fechaPedido;
        this.estadoPedido = estadoPedido;
        this.detallePedido = detallePedido;
        this.obra = obra;
        this.obraId = obraId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
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

    public Integer getObraId() {
        return obraId;
    }

    public void setObraId(Integer obraId) {
        this.obraId = obraId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return getId().equals(pedido.getId()) && getFechaPedido().equals(pedido.getFechaPedido()) && getEstadoPedido().equals(pedido.getEstadoPedido()) && getDetallePedido().equals(pedido.getDetallePedido()) && Objects.equals(getObra(), pedido.getObra()) && getObraId().equals(pedido.getObraId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFechaPedido(), getEstadoPedido(), getDetallePedido(), getObra(), getObraId());
    }
}
