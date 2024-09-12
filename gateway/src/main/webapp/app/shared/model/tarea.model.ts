export interface ITarea {
  nombre?: string;
  descripcion?: string | null;
}

export class Tarea implements ITarea {
  constructor(
    public nombre?: string,
    public descripcion?: string | null
  ) {}
}
