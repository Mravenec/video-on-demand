import React, { useEffect, useCallback } from 'react';
import { connect } from 'react-redux';
import { findAllUsersByAdmin } from '../../../../store/actions/authActions';
import './UserManagement.css';

const UserManagement = ({ users, error, findAllUsersByAdmin }) => {
  // Use useCallback to memoize the function
  const fetchUsers = useCallback(() => {
    findAllUsersByAdmin();
  }, [findAllUsersByAdmin]);

  // Use useEffect to fetch users when the component mounts or when fetchUsers changes
  useEffect(() => {
    fetchUsers();
  }, [fetchUsers]);

  const handleEdit = (userId) => {
    console.log(`Editar usuario con ID: ${userId}`);
  };

  const handleDelete = (userId) => {
    console.log(`Eliminar usuario con ID: ${userId}`);
  };

  if (error) {
    return <div className="error">Error al cargar los usuarios: {error}</div>;
  }

  return (
    <div className="user-management">
      <h2>Mantenimiento de Usuarios</h2>
      <div className="user-table-container">
        <table className="user-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Email</th>
              <th>Nombre Completo</th>
              <th>Activo</th>
              <th>Administrador</th>
              <th>Dirección 1</th>
              <th>Dirección 2</th>
              <th>Provincia</th>
              <th>Cantón</th>
              <th>Código Postal</th>
              <th>WhatsApp</th>
              <th>Otros Números</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.userId}>
                <td>{user.userId}</td>
                <td>{user.email}</td>
                <td>{user.fullName}</td>
                <td>{user.userIsActive ? 'Sí' : 'No'}</td>
                <td>{user.adminIsActive ? 'Sí' : 'No'}</td>
                <td>{user.addressLine1}</td>
                <td>{user.addressLine2}</td>
                <td>{user.province}</td>
                <td>{user.canton}</td>
                <td>{user.postalCode}</td>
                <td>{user.whatsapp}</td>
                <td>{user.otherNumbers}</td>
                <td>
                  <button className="edit-button" onClick={() => handleEdit(user.userId)}>
                    Editar
                  </button>
                  <button className="delete-button" onClick={() => handleDelete(user.userId)}>
                    Eliminar
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

const mapStateToProps = (state) => ({
  users: state.auth.allUsers,
  error: state.auth.allUsersError,
});

const mapDispatchToProps = {
  findAllUsersByAdmin,
};

export default connect(mapStateToProps, mapDispatchToProps)(UserManagement);
