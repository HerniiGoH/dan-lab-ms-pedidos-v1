package utn.frsf.isi.dan.grupotp.tplab.danmspedidos.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.model.DetallePedido;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.model.Pedido;

import java.util.List;

@RestController
@RequestMapping("/api/pedido")
public class PedidoRest {

    @GetMapping
    public ResponseEntity<List<Pedido>> buscarTodos() {
        //TODO hacer esto
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPedidoPorId(@PathVariable Integer id) {
        //TODO hacer esto
        return null;
    }

    @GetMapping("/obra/{id}")
    public ResponseEntity<List<Pedido>> buscarPedidosPorIdObra(@PathVariable Integer id){
        //TODO hacer esto
        return null;
    }

    @GetMapping("/cliente")
    public ResponseEntity<List<Pedido>> buscarPedidosPorCliente(@RequestParam(required = false, name = "id") Integer id, @RequestParam(required = false, name = "cuit") String cuit){
        //TODO hacer esto
        return null;
    }

    @GetMapping("/{idPedido}/detalle/{id}")
    public ResponseEntity<DetallePedido> buscarDetallePedidoPorId(@PathVariable Integer idPedido, @PathVariable Integer id){
        //TODO hacer esto
        return null;
    }

    @PostMapping
    public ResponseEntity<Pedido> crearPedido(@RequestBody Pedido nuevoPedido){
        //TODO hacer esto
        return null;
    }

    @PostMapping("/{id}/detalle")
    public ResponseEntity<DetallePedido> crearDetallePedido(@RequestBody DetallePedido nuevoDetallePedido, @PathVariable Integer id){
        //TODO hacer esto
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> actualizarPedido(@RequestBody Pedido nuevoPedido, @PathVariable Integer id){
        //TODO hacer esto
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Pedido> borrarPedido(@PathVariable Integer id){
        //TODO hacer esto
        return null;
    }

    @DeleteMapping("/{idPedido}/detalle/{id}")
    public ResponseEntity<DetallePedido> borrarDetallePedido(@PathVariable Integer idPedido, @PathVariable Integer id){
        //TODO hacer esto
        return null;
    }
}
