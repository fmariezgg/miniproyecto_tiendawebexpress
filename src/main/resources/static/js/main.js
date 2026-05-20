document.addEventListener("DOMContentLoaded", function () {
  const menuToggle = document.getElementById("menu-toggle");
  const mobileMenu = document.getElementById("mobile-menu");

  if (menuToggle && mobileMenu) {
    menuToggle.addEventListener("click", function () {
      mobileMenu.classList.toggle("hidden");
    });
  }

  const selectProducto = document.getElementById("producto");
  if (selectProducto) {
    actualizarResumen();
  }
});

function filtrarPorCategoria(categoriaId) {
  categoriaId = parseInt(categoriaId);

  document.querySelectorAll(".filter-btn").forEach((btn) => {
    btn.classList.remove("active");
    if (parseInt(btn.dataset.category) === categoriaId) {
      btn.classList.add("active");
    }
  });

  const productos = document.querySelectorAll(".product-card");
  let visibles = 0;

  productos.forEach((prod) => {
    const catId = parseInt(prod.dataset.categoria);
    if (categoriaId === 0 || catId === categoriaId) {
      prod.style.display = "block";
      visibles++;
    } else {
      prod.style.display = "none";
    }
  });

  const mensajeVacio = document.getElementById("mensaje-vacio");
  if (mensajeVacio) {
    if (visibles === 0) {
      mensajeVacio.classList.remove("d-none");
    } else {
      mensajeVacio.classList.add("d-none");
    }
  }
}

function aumentar() {
  const input = document.getElementById("cantidad");
  if (input && parseInt(input.value) < parseInt(input.max)) {
    input.value = parseInt(input.value) + 1;
  }
}

function disminuir() {
  const input = document.getElementById("cantidad");
  if (input && parseInt(input.value) > 1) {
    input.value = parseInt(input.value) - 1;
  }
}

function actualizarResumen() {
  const select = document.getElementById("producto");
  const inputCantidad = document.getElementById("cantidad");

  if (!select || !inputCantidad) return;

  const botonConfirmar = document.getElementById("btn-confirmar-pedido");
  const stockDisponible = document.getElementById("stock-disponible");

  if (select.value) {
    const option = select.options[select.selectedIndex];
    const precio = parseFloat(option.dataset.precio || "0");
    const nombre = option.dataset.nombre || "No seleccionado";
    const stock = parseInt(option.dataset.stock || "0");

    inputCantidad.max = stock > 0 ? String(stock) : "1";
    let cantidad = parseInt(inputCantidad.value) || 1;
    if (stock > 0 && cantidad > stock) {
      cantidad = stock;
      inputCantidad.value = stock;
    }

    if (stock <= 0) {
      cantidad = 0;
      inputCantidad.value = 1;
      if (botonConfirmar) botonConfirmar.disabled = true;
    } else if (botonConfirmar) {
      botonConfirmar.disabled = false;
    }

    const subtotal = precio * cantidad;
    if (stockDisponible) {
      stockDisponible.textContent = "Stock disponible: " + stock;
    }

    document.getElementById("resumen-nombre").textContent = nombre;
    document.getElementById("resumen-cantidad").textContent =
      "Cantidad: " + cantidad;
    document.getElementById("resumen-precio").textContent =
      "C$ " + subtotal.toFixed(2);
    document.getElementById("total-precio").textContent =
      "C$ " + subtotal.toFixed(2);
  } else {
    if (botonConfirmar) botonConfirmar.disabled = false;
    if (stockDisponible) stockDisponible.textContent = "Stock disponible: 0";

    document.getElementById("resumen-nombre").textContent = "No seleccionado";
    document.getElementById("resumen-cantidad").textContent = "Cantidad: 0";
    document.getElementById("resumen-precio").textContent = "C$ 0.00";
    document.getElementById("total-precio").textContent = "C$ 0.00";
  }
}

function validarFormulario() {
  const inputNombre = document.getElementById("nombre");
  const inputCorreo = document.getElementById("correo");
  const selectProducto = document.getElementById("producto");
  const inputCantidad = document.getElementById("cantidad");

  if (!inputNombre || !inputCorreo) return true;

  const nombre = inputNombre.value.trim();
  const correo = inputCorreo.value.trim();
  let valido = true;

  if (nombre.length < 3) {
    document.getElementById("error-nombre").textContent = "Minimo 3 caracteres";
    inputNombre.classList.add("error");
    valido = false;
  } else {
    document.getElementById("error-nombre").textContent = "";
    inputNombre.classList.remove("error");
  }

  if (!correo.includes("@")) {
    document.getElementById("error-correo").textContent = "Correo invalido";
    inputCorreo.classList.add("error");
    valido = false;
  } else {
    document.getElementById("error-correo").textContent = "";
    inputCorreo.classList.remove("error");
  }

  if (selectProducto && inputCantidad) {
    const option = selectProducto.options[selectProducto.selectedIndex];
    const stock = option ? parseInt(option.dataset.stock || "0") : 0;
    const cantidad = parseInt(inputCantidad.value || "0");

    if (!selectProducto.value) {
      document.getElementById("error-producto").textContent = "Selecciona un producto";
      valido = false;
    } else if (stock <= 0 || cantidad < 1 || cantidad > stock) {
      document.getElementById("error-producto").textContent =
        "La cantidad excede el stock disponible.";
      valido = false;
    } else {
      document.getElementById("error-producto").textContent = "";
    }
  }

  return valido;
}

function irAlFormularioConDatos() {
  const prodId = document.querySelector('input[name="productoId"]').value;
  const cant = document.getElementById("cantidad").value;
  window.location.href = `/pedido/formulario?productoId=${prodId}&cantidad=${cant}`;
}
