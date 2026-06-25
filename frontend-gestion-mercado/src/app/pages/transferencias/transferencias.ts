import { Component, OnInit, computed, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { GestionApi, Puesto, Socio, Transferencia, Usuario } from '../../core/gestion-api';

@Component({
  selector: 'app-transferencias',
  imports: [FormsModule],
  templateUrl: './transferencias.html',
  styleUrl: './transferencias.css',
})
export class Transferencias implements OnInit {
  transferencias = signal<Transferencia[]>([]);
  puestos = signal<Puesto[]>([]);
  socios = signal<Socio[]>([]);
  usuarios = signal<Usuario[]>([]);
  cargando = signal(true);
  procesando = signal(false);
  error = signal<string | null>(null);
  mensaje = signal<string | null>(null);
  modalAbierto = signal(false);
  transferenciaForm: Transferencia = this.nuevaTransferencia();

  deudaAsumidaTotal = computed(() =>
    this.transferencias().reduce(
      (total, transferencia) => total + Number(transferencia.montoDeudaAsumida ?? 0),
      0,
    ),
  );

  constructor(private readonly api: GestionApi) {}

  ngOnInit(): void {
    this.cargarDatos();
  }

  cargarDatos(): void {
    this.cargando.set(true);
    this.error.set(null);

    forkJoin({
      transferencias: this.api.listarTransferencias(),
      puestos: this.api.listarPuestos(),
      socios: this.api.listarSocios(),
      usuarios: this.api.listarUsuarios(),
    }).subscribe({
      next: ({ transferencias, puestos, socios, usuarios }) => {
        this.transferencias.set(transferencias);
        this.puestos.set(puestos);
        this.socios.set(socios);
        this.usuarios.set(usuarios);
        this.transferenciaForm.idPuesto = puestos[0]?.idPuesto ?? 0;
        this.transferenciaForm.idSocioEntrante =
          socios.find((socio) => !socio.esAsociacion)?.idSocio ?? 0;
        this.transferenciaForm.idUsuarioTramite = usuarios[0]?.idUsuario ?? 0;
        this.actualizarSocioSaliente();
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('No se pudieron cargar datos para transferencias desde el backend.');
        this.cargando.set(false);
      },
    });
  }

  actualizarSocioSaliente(): void {
    const puesto = this.puestos().find(
      (item) => item.idPuesto === Number(this.transferenciaForm.idPuesto),
    );
    this.transferenciaForm.idSocioSaliente = puesto?.idSocioActual;
  }

  validarDeuda(): void {
    if (!this.modalAbierto()) {
      this.abrirModal();
    }
    if (!this.transferenciaForm.idPuesto) {
      this.error.set('Selecciona un puesto para validar deuda.');
      return;
    }

    this.procesando.set(true);
    this.error.set(null);
    this.mensaje.set(null);

    this.api.obtenerDeudaPorPuesto(Number(this.transferenciaForm.idPuesto)).subscribe({
      next: (deuda) => {
        this.transferenciaForm.asumeDeuda = Boolean(deuda.tieneDeuda);
        this.mensaje.set(
          deuda.tieneDeuda
            ? `El puesto tiene deuda pendiente por ${this.formatoMoneda(deuda.totalDeuda)}.`
            : 'El puesto no registra deuda pendiente.',
        );
        this.procesando.set(false);
      },
      error: () => {
        this.error.set('No se pudo validar la deuda del puesto.');
        this.procesando.set(false);
      },
    });
  }

  registrarTransferencia(): void {
    this.actualizarSocioSaliente();
    this.procesando.set(true);
    this.error.set(null);
    this.mensaje.set(null);

    this.api.registrarTransferencia(this.transferenciaForm).subscribe({
      next: () => {
        this.mensaje.set('Transferencia registrada correctamente.');
        this.modalAbierto.set(false);
        this.procesando.set(false);
        this.cargarDatos();
      },
      error: () => {
        this.error.set('No se pudo registrar la transferencia. Valida deuda, puesto y socios.');
        this.procesando.set(false);
      },
    });
  }

  abrirModal(): void {
    this.modalAbierto.set(true);
    this.error.set(null);
    this.mensaje.set(null);
  }

  cerrarModal(): void {
    this.modalAbierto.set(false);
  }

  formatoMoneda(valor: number | null | undefined): string {
    return `S/ ${Number(valor ?? 0).toFixed(2)}`;
  }

  formatoFecha(valor: string | undefined): string {
    return valor ? new Date(valor).toLocaleDateString('es-PE') : 'Sin fecha';
  }

  nombreSocio(idSocio: number | undefined): string {
    const socio = this.socios().find((item) => item.idSocio === idSocio);
    return socio ? `${socio.nombre} ${socio.apellido}` : idSocio ? String(idSocio) : 'Asociacion';
  }

  numeroPuesto(idPuesto: number): string {
    const puesto = this.puestos().find((item) => item.idPuesto === idPuesto);
    return puesto?.numeroPuesto || String(idPuesto);
  }

  private nuevaTransferencia(): Transferencia {
    return {
      idPuesto: 0,
      idSocioEntrante: 0,
      idUsuarioTramite: 0,
      costoTransferencia: 0,
      asumeDeuda: false,
    };
  }
}
