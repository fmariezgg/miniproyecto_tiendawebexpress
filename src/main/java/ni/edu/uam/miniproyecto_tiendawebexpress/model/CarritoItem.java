package ni.edu.uam.miniproyecto_tiendawebexpress.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public class CarritoItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long productoId;
    private String nombre;
    private String imagen;
    private BigDecimal precioUnitario;
    private Integer cantidad;

    public CarritoItem() {
    }

    public CarritoItem(Long productoId, String nombre, String imagen, BigDecimal precioUnitario, Integer cantidad) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.imagen = imagen;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}

