// frontend/js/veterinarios.js
// CRUD de Veterinarios - Bootstrap 5 + JavaScript vanilla + Fetch API

const URL_VETERINARIOS = "http://localhost:8080/api/veterinario";

let veterinarios = [];
let veterinarioEditandoId = null;

// ===============================
// REFERENCIAS DEL DOM
// ===============================

const tablaVeterinarios =
    document.getElementById("tablaVeterinarios");

const sinVeterinarios =
    document.getElementById("sinVeterinarios");

const formVeterinario =
    document.getElementById("formVeterinario");

const errorFormVeterinario =
    document.getElementById("errorFormVeterinario");

const modalVeterinarioEl =
    document.getElementById("modalVeterinario");

const btnNuevoVeterinario =
    document.getElementById("btnNuevoVeterinario");

const buscarVeterinarioId =
    document.getElementById("buscarVeterinarioId");

const btnBuscarVeterinario =
    document.getElementById("btnBuscarVeterinario");

const btnMostrarTodosVeterinarios =
    document.getElementById("btnMostrarTodosVeterinarios");

const modalVeterinario = modalVeterinarioEl
    ? bootstrap.Modal.getOrCreateInstance(modalVeterinarioEl)
    : null;

// ===============================
// FUNCIONES AUXILIARES
// ===============================

function escapar(texto) {

    if (texto === null || texto === undefined) {
        return "";
    }

    return String(texto)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}


function mostrarAlerta(mensaje, tipo = "info") {

    const alerta =
        document.getElementById("alerta");

    if (!alerta) {
        console.log(mensaje);
        return;
    }

    alerta.textContent = mensaje;
    alerta.className = "alert alert-" + tipo;
}


function limpiarErrorFormulario() {

    if (!errorFormVeterinario) {
        return;
    }

    errorFormVeterinario.textContent = "";
    errorFormVeterinario.classList.add("d-none");
}


function mostrarErrorFormulario(mensaje) {

    if (!errorFormVeterinario) {
        return;
    }

    errorFormVeterinario.textContent = mensaje;
    errorFormVeterinario.classList.remove("d-none");
}


function formatearEspecialidad(especialidad) {

    const nombres = {
        "clínica_general": "Clínica General",
        "dermatología": "Dermatología",
        "cirugía": "Cirugía"
    };

    return nombres[especialidad] ?? especialidad ?? "";
}


async function extraerMensajeError(respuesta) {

    const texto =
        await respuesta.text().catch(() => "");

    if (!texto) {
        return "El servidor respondió con estado " + respuesta.status;
    }

    try {

        const cuerpo = JSON.parse(texto);

        if (cuerpo.mensaje) {
            return cuerpo.mensaje;
        }

        if (cuerpo.message) {
            return cuerpo.message;
        }

        if (
            cuerpo.errors &&
            typeof cuerpo.errors === "object"
        ) {
            return Object.entries(cuerpo.errors)
                .map(([campo, mensaje]) => campo + ": " + mensaje)
                .join(" | ");
        }

        return texto;

    } catch {
        return texto;
    }
}


async function fetchAPI(url, opciones = {}) {

    const respuesta = await fetch(url, opciones);

    if (!respuesta.ok) {

        const mensaje =
            await extraerMensajeError(respuesta);

        const error = new Error(mensaje);
        error.status = respuesta.status;

        throw error;
    }

    if (respuesta.status === 204) {
        return null;
    }

    const texto = await respuesta.text();

    return texto
        ? JSON.parse(texto)
        : null;
}

// ===============================
// GET - LISTAR VETERINARIOS
// ===============================

async function cargarVeterinarios() {

    try {

        veterinarios =
            await fetchAPI(URL_VETERINARIOS) || [];

        pintarTablaVeterinarios();

    } catch (error) {

        mostrarAlerta(
            "No se pudieron cargar los veterinarios: " + error.message,
            "danger"
        );
    }
}

// ===============================
// GET - BUSCAR POR ID
// ===============================

async function buscarVeterinarioPorId() {

    const valor =
        buscarVeterinarioId?.value.trim();

    if (!valor) {
        mostrarAlerta(
            "Ingresá un ID para buscar.",
            "warning"
        );
        return;
    }

    const id = Number(valor);

    if (!Number.isInteger(id) || id <= 0) {
        mostrarAlerta(
            "El ID debe ser un número válido.",
            "warning"
        );
        return;
    }

    try {

        const veterinario =
            await fetchAPI(URL_VETERINARIOS + "/" + id);

        veterinarios = veterinario
            ? [veterinario]
            : [];

        pintarTablaVeterinarios();

        mostrarAlerta(
            "Veterinario encontrado.",
            "success"
        );

    } catch (error) {

        veterinarios = [];
        pintarTablaVeterinarios();

        if (error.status === 404) {
            mostrarAlerta(
                "No se encontró el veterinario con ID " + id + ".",
                "warning"
            );
            return;
        }

        mostrarAlerta(
            "No se pudo realizar la búsqueda: " + error.message,
            "danger"
        );
    }
}


async function mostrarTodosVeterinarios() {

    if (buscarVeterinarioId) {
        buscarVeterinarioId.value = "";
    }

    await cargarVeterinarios();
}

// ===============================
// PINTAR TABLA
// ===============================

function pintarTablaVeterinarios() {

    if (!tablaVeterinarios) {
        return;
    }

    tablaVeterinarios.innerHTML = "";

    if (veterinarios.length === 0) {

        if (sinVeterinarios) {
            sinVeterinarios.classList.remove("d-none");
        }

        return;
    }

    if (sinVeterinarios) {
        sinVeterinarios.classList.add("d-none");
    }

    veterinarios.forEach(veterinario => {

        const fila = document.createElement("tr");

        fila.innerHTML = `
            <td>${escapar(veterinario.id)}</td>
            <td>${escapar(veterinario.nombre)}</td>
            <td>${escapar(veterinario.apellido)}</td>
            <td>${escapar(veterinario.matricula)}</td>
            <td>${escapar(formatearEspecialidad(veterinario.especialidad))}</td>
            <td>${escapar(veterinario.email)}</td>
            <td>${escapar(veterinario.telefono)}</td>

            <td class="text-end text-nowrap">
                <button
                    type="button"
                    class="btn btn-sm btn-warning me-1"
                    onclick="editarVeterinario(${Number(veterinario.id)})"
                >
                    Editar
                </button>

                <button
                    type="button"
                    class="btn btn-sm btn-danger"
                    onclick="eliminarVeterinario(${Number(veterinario.id)})"
                >
                    Eliminar
                </button>
            </td>
        `;

        tablaVeterinarios.appendChild(fila);
    });
}

// ===============================
// NUEVO VETERINARIO
// ===============================

function prepararNuevoVeterinario() {

    veterinarioEditandoId = null;

    if (formVeterinario) {
        formVeterinario.reset();
    }

    limpiarErrorFormulario();

    const titulo =
        document.getElementById("tituloModalVeterinario");

    const btnGuardar =
        document.getElementById("btnGuardarVeterinario");

    if (titulo) {
        titulo.textContent = "Registrar nuevo veterinario";
    }

    if (btnGuardar) {
        btnGuardar.textContent = "Guardar";
    }

    if (modalVeterinario) {
        modalVeterinario.show();
    }
}

// ===============================
// EDITAR VETERINARIO
// ===============================

function editarVeterinario(id) {

    const veterinario = veterinarios.find(
        v => Number(v.id) === Number(id)
    );

    if (!veterinario) {
        mostrarAlerta(
            "No se encontró el veterinario seleccionado.",
            "danger"
        );
        return;
    }

    veterinarioEditandoId = Number(id);

    limpiarErrorFormulario();

    document.getElementById("tituloModalVeterinario").textContent =
        "Editar veterinario";

    document.getElementById("btnGuardarVeterinario").textContent =
        "Guardar cambios";

    document.getElementById("veterinarioNombre").value =
        veterinario.nombre ?? "";

    document.getElementById("veterinarioApellido").value =
        veterinario.apellido ?? "";

    document.getElementById("veterinarioMatricula").value =
        veterinario.matricula ?? "";

    document.getElementById("veterinarioEspecialidad").value =
        veterinario.especialidad ?? "";

    document.getElementById("veterinarioEmail").value =
        veterinario.email ?? "";

    document.getElementById("veterinarioTelefono").value =
        veterinario.telefono ?? "";

    if (modalVeterinario) {
        modalVeterinario.show();
    }
}

// ===============================
// POST / PUT
// ===============================

async function guardarVeterinario(evento) {

    evento.preventDefault();
    limpiarErrorFormulario();

    const telefonoValor =
        document.getElementById("veterinarioTelefono").value.trim();

    const veterinarioDTO = {
        nombre:
            document.getElementById("veterinarioNombre").value.trim(),

        apellido:
            document.getElementById("veterinarioApellido").value.trim(),

        matricula:
            document.getElementById("veterinarioMatricula").value.trim(),

        especialidad:
            document.getElementById("veterinarioEspecialidad").value,

        email:
            document.getElementById("veterinarioEmail").value.trim() || null,

        telefono:
            telefonoValor
                ? Number(telefonoValor)
                : null
    };

    const editando =
        veterinarioEditandoId !== null;

    const url = editando
        ? URL_VETERINARIOS + "/" + veterinarioEditandoId
        : URL_VETERINARIOS;

    const metodo = editando
        ? "PUT"
        : "POST";

    try {

        await fetchAPI(
            url,
            {
                method: metodo,
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(veterinarioDTO)
            }
        );

        if (modalVeterinario) {
            modalVeterinario.hide();
        }

        if (formVeterinario) {
            formVeterinario.reset();
        }

        veterinarioEditandoId = null;

        mostrarAlerta(
            editando
                ? "Veterinario actualizado correctamente."
                : "Veterinario creado correctamente.",
            "success"
        );

        await cargarVeterinarios();

    } catch (error) {
        mostrarErrorFormulario(error.message);
    }
}

// ===============================
// DELETE
// ===============================

async function eliminarVeterinario(id) {

    const veterinario = veterinarios.find(
        v => Number(v.id) === Number(id)
    );

    const nombre = veterinario
        ? `${veterinario.nombre ?? ""} ${veterinario.apellido ?? ""}`.trim()
        : "este veterinario";

    const confirmar = window.confirm(
        `¿Seguro que querés eliminar a ${nombre}?`
    );

    if (!confirmar) {
        return;
    }

    try {

        await fetchAPI(
            URL_VETERINARIOS + "/" + id,
            {
                method: "DELETE"
            }
        );

        mostrarAlerta(
            "Veterinario eliminado correctamente.",
            "success"
        );

        await cargarVeterinarios();

    } catch (error) {
        mostrarAlerta(
            "No se pudo eliminar el veterinario: " + error.message,
            "danger"
        );
    }
}

// ===============================
// EVENTOS
// ===============================

if (btnBuscarVeterinario) {
    btnBuscarVeterinario.addEventListener(
        "click",
        buscarVeterinarioPorId
    );
}


if (btnMostrarTodosVeterinarios) {
    btnMostrarTodosVeterinarios.addEventListener(
        "click",
        mostrarTodosVeterinarios
    );
}


if (buscarVeterinarioId) {
    buscarVeterinarioId.addEventListener(
        "keydown",
        event => {
            if (event.key === "Enter") {
                event.preventDefault();
                buscarVeterinarioPorId();
            }
        }
    );
}


if (formVeterinario) {
    formVeterinario.addEventListener(
        "submit",
        guardarVeterinario
    );
}


if (btnNuevoVeterinario) {
    btnNuevoVeterinario.addEventListener(
        "click",
        prepararNuevoVeterinario
    );
}


if (modalVeterinarioEl) {
    modalVeterinarioEl.addEventListener(
        "hidden.bs.modal",
        () => {
            limpiarErrorFormulario();
            veterinarioEditandoId = null;

            if (formVeterinario) {
                formVeterinario.reset();
            }
        }
    );
}

// ===============================
// FUNCIONES GLOBALES
// ===============================

window.editarVeterinario = editarVeterinario;
window.eliminarVeterinario = eliminarVeterinario;

// ===============================
// INICIO
// ===============================

document.addEventListener(
    "DOMContentLoaded",
    cargarVeterinarios
);