export interface IMascota {
  id?: string;
  nombre?: string;
  edad?: number;
  precio?: number;
  fechaNacimiento?: Date | null;
  fotoContentType?: string | null;
  foto?: string | null;
}

export class Mascota implements IMascota {
  constructor(
    public id?: string,
    public nombre?: string,
    public edad?: number,
    public precio?: number,
    public fechaNacimiento?: Date | null,
    public fotoContentType?: string | null,
    public foto?: string | null,
  ) {}
}
