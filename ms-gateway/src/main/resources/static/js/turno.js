const API_BASE = "";

const URL_TURNOS = API_BASE + "/api/turnos";
const URL_MASCOTAS = API_BASE + "/api/mascotas";
const URL_VETERINARIOS = API_BASE + "/api/veterinarios";
const URL_DUENIOS = API_BASE + "/api/duenos";

let turnos = [];
let mascotas = [];
let veterinarios = [];
let duenios = [];


const tablaTurnos = document.getElementById("tablaTurnos");
const sinTurnos = document.getElementById("sinTurnos");
const alerta = document.getElementById("alerta");

const formTurno = document.getElementById("formTurno");
const formEstado = document.getElementById("formEstado");

const modalTurnoEl = document.getElementById("modalTurno");
const modalEstadoEl = document.getElementById("modalEstado");

const modalTurno = new bootstrap.Modal(modalTurnoEl);
const modalEstado = new bootstrap.Modal(modalEstadoEl);


async function fetchAPI(url, opciones = {}) {

    const respuesta = await VetAuth.request(url, opciones);

    if (!respuesta.ok) {

        let mensaje = "Error " + respuesta.status;

        try {
            const error = await respuesta.json();
            mensaje =
                error.mensaje ||
                error.message ||
                mensaje;
        } catch {
        }

        throw new Error(mensaje);
    }

    if (respuesta.status === 204) {
        return null;
    }

    return await respuesta.json();
}


function mostrarAlerta(mensaje, tipo = "success") {

    alerta.textContent = mensaje;
    alerta.className = "alert alert-" + tipo;

    setTimeout(() => {
        alerta.classList.add("d-none");
    }, 4000);
}


function escapar(texto) {

    if (texto === null || texto === undefined) {
        return "";
    }

    return String(texto)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;");
}


// ------------------------------------------------
// CARGAR DATOS AUXILIARES
// ------------------------------------------------

async function cargarDatosAuxiliares() {

    try {

        const resultados = await Promise.all([
            fetchAPI(URL_MASCOTAS),
            fetchAPI(URL_VETERINARIOS),
            fetchAPI(URL_DUENIOS)
        ]);

        mascotas = resultados[0];
        veterinarios = resultados[1];
        duenios = resultados[2];

        cargarComboMascotas();
        cargarComboVeterinarios();

    } catch (error) {

        mostrarAlerta(
            "No se pudieron cargar mascotas, veterinarios o dueños: " +
            error.message,
            "danger"
        );
    }
}


// ------------------------------------------------
// COMBO MASCOTAS
// ------------------------------------------------

function cargarComboMascotas() {

    const select = document.getElementById("turnoMascota");

    select.innerHTML =
        '<option value="">Seleccione una mascota</option>';

    mascotas.forEach(mascota => {

        const duenio = obtenerDuenioMascota(mascota);

        const nombreDuenio = duenio
            ? `${duenio.nombre} ${duenio.apellido}`
            : "Sin dueño";

        const option = document.createElement("option");

        option.value = mascota.id;

        option.textContent =
            `${mascota.nombre} — Dueño: ${nombreDuenio}`;

        select.appendChild(option);
    });
}


// ------------------------------------------------
// COMBO VETERINARIOS
// ------------------------------------------------

function cargarComboVeterinarios() {

    const select =
        document.getElementById("turnoVeterinario");

    select.innerHTML =
        '<option value="">Seleccione un veterinario</option>';

    veterinarios.forEach(veterinario => {

        const option = document.createElement("option");

        option.value = veterinario.id;

        option.textContent =
            `${veterinario.nombre} ${veterinario.apellido}`;

        select.appendChild(option);
    });
}


// ------------------------------------------------
// OBTENER DUEÑO DE UNA MASCOTA
// ------------------------------------------------

function obtenerDuenioMascota(mascota) {

    if (!mascota) {
        return null;
    }

    // Si el backend devuelve el objeto dueño completo
    if (mascota.duenio) {

        if (mascota.duenio.nombre) {
            return mascota.duenio;
        }

        if (mascota.duenio.id) {

            return duenios.find(
                d => d.id === mascota.duenio.id
            );
        }
    }


    // Por si el DTO devuelve duenioId
    if (mascota.duenioId) {

        return duenios.find(
            d => d.id === mascota.duenioId
        );
    }


    return null;
}


// ------------------------------------------------
// LISTAR TURNOS
// ------------------------------------------------

async function cargarTurnos() {

    try {

        turnos = await fetchAPI(URL_TURNOS);

        pintarTurnos(turnos);

    } catch (error) {

        mostrarAlerta(
            "No se pudieron cargar los turnos: " +
            error.message,
            "danger"
        );
    }
}


function pintarTurnos(datos) {

    tablaTurnos.innerHTML = "";

    if (!datos || datos.length === 0) {

        sinTurnos.classList.remove("d-none");
        return;
    }

    sinTurnos.classList.add("d-none");


    datos.forEach(turno => {

        const mascota = mascotas.find(
            m => Number(m.id) === Number(turno.mascotaId)
        );

        const veterinario = veterinarios.find(
            v => Number(v.id) === Number(turno.veterinarioId)
        );

        const nombreMascota = mascota
            ? mascota.nombre
            : "-";

        const nombreVeterinario = veterinario
            ? `${veterinario.nombre} ${veterinario.apellido}`
            : "-";

        const duenio = obtenerDuenioMascota(mascota);

        const nombreDuenio = duenio
            ? `${duenio.nombre} ${duenio.apellido}`
            : "-";

        const fila = document.createElement("tr");

        fila.innerHTML = `

            <td>${turno.id}</td>

            <td>${formatearFecha(turno.fecha)}</td>

            <td>${formatearHora(turno.hora)}</td>

            <td>${escapar(nombreDuenio)}</td>

           <td>${escapar(nombreMascota)}</td>
<td>${escapar(nombreVeterinario)}</td>

            <td>
                ${escapar(turno.motivo)}
            </td>

            <td>
                <span class="badge ${claseEstado(turno.estado)}">
                    ${formatearEstado(turno.estado)}
                </span>
            </td>

            <td>
                ${escapar(turno.observaciones || "-")}
            </td>

            <td>

                <button
                    class="btn btn-sm btn-warning"
                    onclick="abrirEstado(${turno.id})"
                >
                    Estado
                </button>

            </td>
        `;

        tablaTurnos.appendChild(fila);
    });
}


// ------------------------------------------------
// CREAR TURNO
// ------------------------------------------------

formTurno.addEventListener("submit", async function (evento) {

    evento.preventDefault();

    const errorTurno =
        document.getElementById("errorTurno");

    errorTurno.classList.add("d-none");


    const hora =
        document.getElementById("turnoHora").value;


    const turnoRequest = {

        fecha:
            document.getElementById("turnoFecha").value,

        hora:
            hora.length === 5
                ? hora + ":00"
                : hora,

        motivo:
            document.getElementById("turnoMotivo")
                .value
                .trim(),

        mascotaId:
            Number(
                document.getElementById("turnoMascota").value
            ),

        veterinarioId:
            Number(
                document.getElementById("turnoVeterinario").value
            )
    };


    try {

        await fetchAPI(URL_TURNOS, {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(turnoRequest)
        });


        modalTurno.hide();

        formTurno.reset();

        mostrarAlerta(
            "Turno creado correctamente"
        );

        await cargarTurnos();

    } catch (error) {

        errorTurno.textContent = error.message;
        errorTurno.classList.remove("d-none");
    }
});


// ------------------------------------------------
// BUSCAR TURNO POR ID
// ------------------------------------------------

document
    .getElementById("btnBuscarTurno")
    .addEventListener("click", buscarTurno);


document
    .getElementById("buscarTurnoId")
    .addEventListener("keydown", evento => {

        if (evento.key === "Enter") {
            buscarTurno();
        }
    });


async function buscarTurno() {

    const id =
        document.getElementById("buscarTurnoId")
            .value
            .trim();

    if (!id) {
        return;
    }


    try {

        const turno =
            await fetchAPI(URL_TURNOS + "/" + id);

        pintarTurnos([turno]);

    } catch (error) {

        pintarTurnos([]);

        mostrarAlerta(
            error.message,
            "danger"
        );
    }
}


document
    .getElementById("btnMostrarTodos")
    .addEventListener("click", () => {

        document.getElementById("buscarTurnoId").value = "";

        cargarTurnos();
    });

document.addEventListener("DOMContentLoaded", async () => {
    await cargarDatosAuxiliares();
    await cargarTurnos();
});
function formatearFecha(fecha) {
    if (!fecha) return "-";

    const partes = fecha.split("-");

    if (partes.length !== 3) return fecha;

    return `${partes[2]}/${partes[1]}/${partes[0]}`;
}

function formatearHora(hora) {
    if (!hora) return "-";

    // Convierte 10:30:00 en 10:30
    return String(hora).substring(0, 5);
}

function claseEstado(estado) {
    const clases = {
        PENDIENTE: "bg-warning text-dark",
        CONFIRMADO: "bg-primary",
        CANCELADO: "bg-danger",
        COMPLETADO: "bg-success",
        REALIZADO: "bg-success"
    };

    return clases[estado] || "bg-secondary";
}
function formatearEstado(estado) {
    if (!estado) return "-";

    const nombres = {
        PENDIENTE: "Pendiente",
        CONFIRMADO: "Confirmado",
        CANCELADO: "Cancelado",
        COMPLETADO: "Completado",
        REALIZADO: "Realizado"
    };

    return nombres[estado] || estado;
}



function abrirEstado(id) {
    const turno = turnos.find(t => t.id === id);

    if (!turno) {
        mostrarAlerta("No se encontró el turno", "danger");
        return;
    }

    document.getElementById("estadoTurnoId").value = id;
    document.getElementById("nuevoEstado").value =
        turno.estado || "PENDIENTE";
    document.getElementById("observacionesEstado").value =
        turno.observaciones || "";

    modalEstado.show();
}

formEstado.addEventListener("submit", async function (evento) {
    evento.preventDefault();

    const id = document.getElementById("estadoTurnoId").value;
    const estado = document.getElementById("nuevoEstado").value;
    const observaciones =
        document.getElementById("observacionesEstado").value;

    const parametros = new URLSearchParams({ estado, observaciones });

    try {
        await fetchAPI(
            `${URL_TURNOS}/${id}/estado?${parametros.toString()}`,
            { method: "PATCH" }
        );

        modalEstado.hide();
        mostrarAlerta("Estado actualizado correctamente");
        await cargarTurnos();

    } catch (error) {
        mostrarAlerta(error.message, "danger");
    }
});

//