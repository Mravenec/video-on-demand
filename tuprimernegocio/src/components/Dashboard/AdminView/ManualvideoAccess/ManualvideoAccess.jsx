import React, { useEffect, useState } from 'react';
import Modal from 'react-modal';
import { useForm } from 'react-hook-form';
import { useDispatch, useSelector } from 'react-redux';
import { FaEdit, FaTrashAlt } from 'react-icons/fa';
import {
  getActiveVideoAccessUsers,
  insertVideoAccess,
  updateVideoAccessEmail,
  deactivateVideoAccess
} from '../../../../store/actions/videoAccessActions';
import './ManualvideoAccess.css';



const ManualvideoAccess = () => {
  const { register, handleSubmit, setValue, reset } = useForm();
  const dispatch = useDispatch();
  const activeUsers = useSelector(state => state.videoAccess.activeVideoAccessUsers || []);
  const [editAccessId, setEditAccessId] = useState(null);
  const [editModalIsOpen, setEditModalIsOpen] = useState(false);
  const [deactivateModalIsOpen, setDeactivateModalIsOpen] = useState(false);
  const [selectedEmail, setSelectedEmail] = useState('');
  const [selectedAccessId, setSelectedAccessId] = useState(null);

  useEffect(() => {
    dispatch(getActiveVideoAccessUsers());
  }, [dispatch]);

  const refreshActiveUsers = () => {
    dispatch(getActiveVideoAccessUsers());
  };

  const onSubmit = data => {
    if (editAccessId) {
      dispatch(updateVideoAccessEmail({ accessId: editAccessId, newEmail: data.email }))
        .then(() => {
          setEditAccessId(null);
          reset();
          setEditModalIsOpen(false);
          refreshActiveUsers();
        })
        .catch(error => {
          console.error('Error al editar el correo electrónico:', error);
        });
    } else {
      dispatch(insertVideoAccess(data))
        .then(() => {
          reset();
          setEditModalIsOpen(false);
          refreshActiveUsers();
        })
        .catch(error => {
          console.error('Error al agregar el acceso de video:', error);
        });
    }
  };

  const startEdit = user => {
    setEditAccessId(user.id);
    setSelectedEmail(user.email);
    setValue('email', user.email); // Actualiza el valor del campo de email con setValue
    setEditModalIsOpen(true);
  };

  const deactivateAccess = id => {
    setSelectedAccessId(id);
    setSelectedEmail(activeUsers.find(user => user.id === id).email);
    setDeactivateModalIsOpen(true);
  };

  const closeEditModal = () => {
    setEditAccessId(null);
    reset();
    setEditModalIsOpen(false);
  };

  const closeDeactivateModal = () => {
    setSelectedAccessId(null);
    setDeactivateModalIsOpen(false);
  };

  const confirmDeactivate = () => {
    if (selectedAccessId) {
      dispatch(deactivateVideoAccess({ accessId: selectedAccessId }))
        .then(() => {
          setSelectedAccessId(null);
          setDeactivateModalIsOpen(false);
          refreshActiveUsers();
        })
        .catch(error => {
          console.error('Error al desactivar el acceso de video:', error);
        });
    }
  };

  return (
    <div className="user-table">
      <form onSubmit={handleSubmit(onSubmit)}>
        <input {...register('email')} placeholder="Correo electrónico" />
        <button type="submit">{editAccessId ? 'Actualizar' : 'Agregar'}</button>
      </form>
      {activeUsers.length > 0 ? (
        <table>
          <thead>
            <tr>
              <th className="user-table">Email</th>
              <th className="user-table">Acciones</th>
            </tr>
          </thead>
          <tbody>
            {activeUsers.map(user => (
              <tr key={user.id}>
                <td>{user.email}</td>
                <td>
                  <FaEdit className="icon" onClick={() => startEdit(user)} />
                  <FaTrashAlt className="icon" onClick={() => deactivateAccess(user.id)} />
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>No hay usuarios activos para mostrar.</p>
      )}

      <Modal
        isOpen={editModalIsOpen}
        onRequestClose={closeEditModal}
        contentLabel="Editar Correo Electrónico"
      >
        <h2>Editar Correo Electrónico</h2>
        <form onSubmit={handleSubmit(onSubmit)}> {/* Asegúrate de que el formulario se envía correctamente */}
          <input {...register('email')} placeholder="Nuevo Correo Electrónico" />
          <button type="submit">Guardar</button> {/* El botón debe ser de tipo submit para enviar el formulario */}
        </form>
        <button onClick={closeEditModal}>Cancelar</button>
      </Modal>

      <Modal
        isOpen={deactivateModalIsOpen}
        onRequestClose={closeDeactivateModal}
        contentLabel="Desactivar Acceso"
      >
        <h2>¿Estás seguro de que deseas desactivar este acceso?</h2>
        <p>Correo electrónico: {selectedEmail}</p>
        <button onClick={closeDeactivateModal}>Cancelar</button>
        <button onClick={confirmDeactivate}>Confirmar</button>
      </Modal>
    </div>
  );
};


Modal.setAppElement('#root');

export default ManualvideoAccess;
