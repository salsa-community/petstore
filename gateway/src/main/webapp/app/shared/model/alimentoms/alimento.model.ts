export interface IAlimento {
  id?: string;
  nombre?: string;
  precio?: number;
  descripcion?: string | null;
  fotoContentType?: string | null;
  foto?: string | null;
}

export class Alimento implements IAlimento {
  constructor(
    public id?: string,
    public nombre?: string,
    public precio?: number,
    public descripcion?: string | null,
    public fotoContentType?: string | null,
    public foto?: string | null,
  ) {}
}
