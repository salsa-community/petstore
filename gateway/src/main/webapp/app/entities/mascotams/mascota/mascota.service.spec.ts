/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import MascotaService from './mascota.service';
import { DATE_FORMAT } from '@/shared/composables/date-format';
import { Mascota } from '@/shared/model/mascotams/mascota.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('Mascota Service', () => {
    let service: MascotaService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new MascotaService();
      currentDate = new Date();
      elemDefault = new Mascota('ABC', 'AAAAAAA', 0, 0, currentDate, 'image/png', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { fechaNacimiento: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find('ABC').then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find('ABC')
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a Mascota', async () => {
        const returnedFromService = { id: 'ABC', fechaNacimiento: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
        const expected = { fechaNacimiento: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Mascota', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Mascota', async () => {
        const returnedFromService = {
          nombre: 'BBBBBB',
          edad: 1,
          precio: 1,
          fechaNacimiento: dayjs(currentDate).format(DATE_FORMAT),
          foto: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { fechaNacimiento: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Mascota', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Mascota', async () => {
        const patchObject = { nombre: 'BBBBBB', edad: 1, fechaNacimiento: dayjs(currentDate).format(DATE_FORMAT), ...new Mascota() };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { fechaNacimiento: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Mascota', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Mascota', async () => {
        const returnedFromService = {
          nombre: 'BBBBBB',
          edad: 1,
          precio: 1,
          fechaNacimiento: dayjs(currentDate).format(DATE_FORMAT),
          foto: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { fechaNacimiento: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Mascota', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Mascota', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete('ABC').then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Mascota', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete('ABC')
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
