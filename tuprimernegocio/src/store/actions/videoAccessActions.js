import axios from 'axios';

// Función para obtener el token
const getToken = () => {
  const token = localStorage.getItem('jwtToken');
  if (!token) throw new Error('Token no disponible');
  return token;
};

// Función para obtener el adminId
const getAdminId = async () => {
  const token = getToken();
  const userInfoResponse = await axios.get('http://localhost:8080/userInfo', {
    headers: { Authorization: `Bearer ${token}` },
  });
  const adminId = userInfoResponse.data.userId;
  if (!adminId) throw new Error('ID de usuario no disponible');
  return adminId;
};

// Acción para obtener usuarios con acceso activo a videos
export const getActiveVideoAccessUsers = () => async (dispatch) => {
  try {
    const token = getToken();
    const adminId = await getAdminId();

    const response = await axios.get(`http://localhost:8080/manual-video-access/active-users/${adminId}`, {
      headers: { Authorization: `Bearer ${token}` },
    });

    dispatch({
      type: 'GET_ACTIVE_VIDEO_ACCESS_USERS_SUCCESS',
      payload: response.data,
    });
  } catch (error) {
    dispatch({
      type: 'GET_ACTIVE_VIDEO_ACCESS_USERS_ERROR',
      payload: error.message,
    });
  }
};

// Acción para insertar acceso a video
export const insertVideoAccess = (videoAccessData) => async (dispatch) => {
  try {
    const token = getToken();
    const adminId = await getAdminId();
    videoAccessData.adminId = adminId;

    const response = await axios.post('http://localhost:8080/manual-video-access/insert-video-access', videoAccessData, {
      headers: { Authorization: `Bearer ${token}`, 'Content-Type': 'application/json' },
    });

    dispatch({
      type: 'INSERT_VIDEO_ACCESS_SUCCESS',
      payload: response.data,
    });
  } catch (error) {
    dispatch({
      type: 'INSERT_VIDEO_ACCESS_ERROR',
      payload: error.message,
    });
  }
};

// Acción para actualizar el email de acceso a video
export const updateVideoAccessEmail = (requestData) => async (dispatch) => {
  try {
    const token = getToken();
    const adminId = await getAdminId();
    requestData.adminId = adminId;

    const response = await axios.put('http://localhost:8080/manual-video-access/update-video-access-email', requestData, {
      headers: { Authorization: `Bearer ${token}`, 'Content-Type': 'application/json' },
    });

    dispatch({
      type: 'UPDATE_VIDEO_ACCESS_EMAIL_SUCCESS',
      payload: response.data,
    });
  } catch (error) {
    dispatch({
      type: 'UPDATE_VIDEO_ACCESS_EMAIL_ERROR',
      payload: error.message,
    });
  }
};

// Acción para desactivar el acceso a video
export const deactivateVideoAccess = (requestData) => async (dispatch) => {
  try {
    const token = getToken();
    const adminId = await getAdminId();
    requestData.adminId = adminId;

    const response = await axios.put('http://localhost:8080/manual-video-access/deactivate-video-access', requestData, {
      headers: { Authorization: `Bearer ${token}`, 'Content-Type': 'application/json' },
    });

    dispatch({
      type: 'DEACTIVATE_VIDEO_ACCESS_SUCCESS',
      payload: response.data,
    });
  } catch (error) {
    dispatch({
      type: 'DEACTIVATE_VIDEO_ACCESS_ERROR',
      payload: error.message,
    });
  }
};


