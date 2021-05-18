package utn.frsf.isi.dan.grupotp.tplab.danmspedidos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.model.EstadoPedido;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.model.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    List<Pedido> findAll();
    Optional<Pedido> findById(Integer integer);
    @Query(value = "SELECT p FROM Pedido p WHERE (:idObra = p.obraId)")
    Optional<List<Pedido>> findByIdObra(Integer idObra);
    @Query(value = "SELECT ep FROM EstadoPedido ep WHERE (:estado = ep.estado)")
    Optional<EstadoPedido> buscarEstadoPedido(String estado);
    @Query(value = "SELECT p FROM Pedido p WHERE (:estado = p.estadoPedido.estado)")
    Optional<List<Pedido>> buscarPedidoPorEstado(String estado);
}
