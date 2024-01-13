import axios from 'axios';

const BASE_URL = 'https://s1.tuprimernegocio.org';

// Función para configurar el token en los encabezados de Axios
const setAuthToken = (token) => {
  if (token) {
    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
  } else {
    delete axios.defaults.headers.common['Authorization'];
  }
};

// Acción asincrónica para iniciar sesión
export const login = (userData) => async (dispatch) => {
  try {
    const response = await axios.post(`${BASE_URL}/login`, userData);
    const token = response.data.jwtToken;

    if (!token) {
      throw new Error('Token no recibido o inválido');
    }

    // Almacenar el token en el almacenamiento local del navegador
    localStorage.setItem('jwtToken', token);

    // Configurar el token en los encabezados de axios para futuras solicitudes
    setAuthToken(token);

    dispatch({
      type: 'LOGIN',
      payload: response.data,
    });
  } catch (error) {
    console.error('Error durante el inicio de sesión:', error);
    dispatch({
      type: 'LOGIN_ERROR',
      payload: 'Error durante el inicio de sesión',
    });
  }
};

// Acción para inicializar la autenticación
export const initializeAuth = () => async (dispatch) => {
  const token = localStorage.getItem('jwtToken');

  if (token) {
    setAuthToken(token);

    try {
      const response = await axios.get(`${BASE_URL}/userInfo`);
      dispatch({
        type: 'INITIALIZE_AUTH',
        payload: { token, user: response.data },
      });
    } catch (error) {
      console.error('Could not fetch user info:', error);
      // Handle error
    }
  }
};
// Acción para cerrar sesión
export const logout = (message = '') => {
  // Eliminar el token de los encabezados de axios
  setAuthToken(null);

  // Eliminar el token del almacenamiento local
  localStorage.removeItem('jwtToken');

  return {
    type: 'LOGOUT',
    message,
  };
};

// Acción para actualizar el estado con el mensaje de error
export const registerError = (errorMessage) => ({
  type: 'REGISTER_ERROR',
  payload: errorMessage,
});

// Acción para registrar exitosamente
export const registerSuccess = () => ({
  type: 'REGISTER_SUCCESS',
});

// Acción asincrónica para registrarse
export const register = (userData) => async (dispatch) => {
  try {
    const response = await axios.post(`${BASE_URL}/register`, userData);
    const token = response.data.jwtToken;

    if (token) {
      localStorage.setItem('jwtToken', token);
      setAuthToken(token);
    }

    dispatch(registerSuccess());
    dispatch(login(response.data));
  } catch (error) {
    dispatch(registerError('Ha ocurrido un error al registrarse.'));
    console.error('Ha ocurrido un error al registrarse:', error);
  }
};


// Acción asincrónica para "Olvidé mi contraseña"
export const forgotPassword = (email) => async (dispatch) => {
  try {
    const response = await axios.post(`${BASE_URL}/forgotPassword`, { email });
    dispatch({
      type: 'FORGOT_PASSWORD_SUCCESS',
      payload: response.data,
    });
  } catch (error) {
    dispatch({
      type: 'FORGOT_PASSWORD_ERROR',
      payload: error.response?.data || 'Error al procesar la solicitud de restablecimiento de contraseña',
    });
  }
};

export const resetPassword = (token, newPassword) => async (dispatch) => {
  try {
    const response = await axios.post(`${BASE_URL}/resetPassword`, {
      token,
      newPassword
    });
    dispatch({
      type: 'RESET_PASSWORD_SUCCESS',
      payload: response.data
    });
  } catch (error) {
    dispatch({
      type: 'RESET_PASSWORD_ERROR',
      payload: error.response?.data || 'Error al restablecer la contraseña'
    });
  }
};

export const findAllUsersByAdmin = () => async (dispatch) => {
  try {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
      throw new Error('Token no disponible');
    }

    // Obtener el adminId (userId en este caso)
    const userInfoResponse = await axios.get(`${BASE_URL}/userInfo`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    const adminId = userInfoResponse.data.userId;

    if (!adminId) {
      throw new Error('ID de usuario no disponible');
    }

    const response = await axios.get(`${BASE_URL}/findAllUsersByAdmin/${adminId}`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    dispatch({
      type: 'FIND_ALL_USERS_BY_ADMIN_SUCCESS',
      payload: response.data,
    });
  } catch (error) {
    dispatch({
      type: 'FIND_ALL_USERS_BY_ADMIN_ERROR',
      payload: error.message || 'Error al obtener los usuarios',
    });
  }
};


// Acción asincrónica para validar el token
export const validateToken = () => async (dispatch) => {
  try {
    const token = localStorage.getItem('jwtToken');

    if (!token) {
      throw new Error('Token no disponible');
    }

    const response = await axios.get(`${BASE_URL}/validateToken`, {
      headers: { Authorization: `Bearer ${token}` },
    });

    if (response.data.isValid) {
      setAuthToken(token);
    } else {
      dispatch(logout('Sesión terminada por expiración, inicie sesión nuevamente.')); // Llama a la acción de logout si el token no es válido
    }
  } catch (error) {
    console.error('Error durante la validación del token:', error);
    dispatch(logout('Sesión terminada por expiración, inicie sesión nuevamente.')); // Llama a la acción de logout en caso de error
  }
};


//AUN SIN PROBAR:
export const updatePhones = (phoneData) => async (dispatch) => {
  try {
    const token = localStorage.getItem('jwtToken');

    if (!token) {
      throw new Error('Token no disponible');
    }

    // Obtener el userId
    const userInfoResponse = await axios.get(`${BASE_URL}/userInfo`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    const userId = userInfoResponse.data.userId;

    if (!userId) {
      throw new Error('ID de usuario no disponible');
    }

    // Agregar userId a phoneData
    phoneData.userId = userId;

    const apiUrl = `${BASE_URL}/updatePhones`;
    const headers = {
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json',
    };

    const response = await axios.post(apiUrl, phoneData, { headers });

    dispatch({
      type: 'UPDATE_PHONES_SUCCESS',
      payload: response.data,
    });
  } catch (error) {
    dispatch({
      type: 'UPDATE_PHONES_ERROR',
      payload: error.message,
    });
  }
};
export const selectPhones = () => async (dispatch) => {
  try {
    const token = localStorage.getItem('jwtToken');

    if (!token) {
      throw new Error('Token no disponible');
    }

    const userInfoResponse = await axios.get(`${BASE_URL}/userInfo`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    const userId = userInfoResponse.data.userId;

    if (!userId) {
      throw new Error('ID de usuario no disponible');
    }

    const apiUrl = `${BASE_URL}/selectPhones/${userId}`;
    const headers = {
      Authorization: `Bearer ${token}`,
    };

    const response = await axios.get(apiUrl, { headers });

    dispatch({
      type: 'SELECT_PHONES_SUCCESS',
      payload: response.data,
    });
  } catch (error) {
    dispatch({
      type: 'SELECT_PHONES_ERROR',
      payload: error.message,
    });
  }
};
export const selectAddress = () => async (dispatch) => {
  try {
    const token = localStorage.getItem('jwtToken');

    if (!token) {
      throw new Error('Token no disponible');
    }

    const userInfoResponse = await axios.get(`${BASE_URL}/userInfo`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    const userId = userInfoResponse.data.userId;

    if (!userId) {
      throw new Error('ID de usuario no disponible');
    }

    const apiUrl = `${BASE_URL}/selectAddress/${userId}`;
    const headers = {
      Authorization: `Bearer ${token}`,
    };

    const response = await axios.get(apiUrl, { headers });

    dispatch({
      type: 'SELECT_ADDRESS_SUCCESS',
      payload: response.data,
    });
  } catch (error) {
    dispatch({
      type: 'SELECT_ADDRESS_ERROR',
      payload: error.message,
    });
  }
};
export const updateAddress = (addressData) => async (dispatch) => {
  try {
    const token = localStorage.getItem('jwtToken');

    if (!token) {
      throw new Error('Token no disponible');
    }

    const userInfoResponse = await axios.get(`${BASE_URL}/userInfo`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    const userId = userInfoResponse.data.userId;

    if (!userId) {
      throw new Error('ID de usuario no disponible');
    }

    addressData.userId = userId;

    const apiUrl = `${BASE_URL}/updateAddress`;
    const headers = {
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json',
    };

    const response = await axios.post(apiUrl, addressData, { headers });

    dispatch({
      type: 'UPDATE_ADDRESS_SUCCESS',
      payload: response.data,
    });
  } catch (error) {
    dispatch({
      type: 'UPDATE_ADDRESS_ERROR',
      payload: error.message,
    });
  }
};
export const deleteAddress = () => async (dispatch) => {
  try {
    const token = localStorage.getItem('jwtToken');

    if (!token) {
      throw new Error('Token no disponible');
    }

    const userInfoResponse = await axios.get(`${BASE_URL}/userInfo`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    const userId = userInfoResponse.data.userId;

    if (!userId) {
      throw new Error('ID de usuario no disponible');
    }

    const apiUrl = `${BASE_URL}/deleteAddress/${userId}`;
    const headers = {
      Authorization: `Bearer ${token}`,
    };

    await axios.delete(apiUrl, { headers });

    dispatch({
      type: 'DELETE_ADDRESS_SUCCESS',
      payload: userId,
    });
  } catch (error) {
    dispatch({
      type: 'DELETE_ADDRESS_ERROR',
      payload: error.message,
    });
  }
};
export const updateUserProfilePicture = (imageFile) => async (dispatch) => {
  try {
    const token = localStorage.getItem('jwtToken');
    if (!token) throw new Error('Token no disponible');

    const userInfoResponse = await axios.get(`${BASE_URL}/userInfo`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    const userId = userInfoResponse.data.userId;
    if (!userId) throw new Error('ID de usuario no disponible');

    console.log('userId:', userId);

    let formData = new FormData();
    formData.append('userId', userId);
    formData.append('profilePicture', imageFile);

    const apiUrl = `${BASE_URL}/updateUserProfilePicture`;
    const headers = { Authorization: `Bearer ${token}` };

    const response = await axios.put(apiUrl, formData, { headers });

    dispatch({
      type: 'UPDATE_PROFILE_PICTURE_SUCCESS',
      payload: response.data,
    });
  } catch (error) {
    dispatch({
      type: 'UPDATE_PROFILE_PICTURE_ERROR',
      payload: error.message,
    });
  }
};


export const getUserProfilePicture = () => async (dispatch) => {
  try {
    const token = localStorage.getItem('jwtToken');

    if (!token) {
      throw new Error('Token no disponible');
    }

    const userInfoResponse = await axios.get(`${BASE_URL}/userInfo`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    const userId = userInfoResponse.data.userId;

    if (!userId) {
      throw new Error('ID de usuario no disponible');
    }

    const apiUrl = `${BASE_URL}/getUserProfilePicture/${userId}`;
    const headers = {
      Authorization: `Bearer ${token}`,
    };

    const response = await axios.get(apiUrl, { headers });

    dispatch({
      type: 'GET_USER_PROFILE_PICTURE_SUCCESS',
      payload: response.data,
    });
  } catch (error) {
    dispatch({
      type: 'GET_USER_PROFILE_PICTURE_ERROR',
      payload: error.message,
    });
  }
};

// VIDEOS SECCION DE VIDEOS VIDEOS VIDEOS VIDEOS VIDEOS VIDEOS VIDEOS VIDEOS 

// Acción asincrónica para obtener el Payment Intent por email
export const findPaymentIntentByEmail = (email) => async (dispatch) => {
  try {
    const response = await axios.get(`${BASE_URL}/api/stripe/findPaymentIntentByEmail/${email}`, {
      headers: {
        Authorization: 'Bearer pk_test_51OD8MCEmQl5nvMENtESdkwAHj7v81nB9lCUCO6rZjK0A07dxzNgdyrSkDFT2Gaqa2nYwBEpedUIZcMXy8p6UxLKe005tzoyZjT'
      }
    });
    console.log("Payment Intent data received:", response.data);
    // Despachar acción de éxito con los datos obtenidos
    dispatch({
      type: 'FIND_PAYMENT_INTENT_SUCCESS',
      payload: response.data,
    });
  } catch (error) {
    console.error('Error al obtener Payment Intent:', error);
    // Despachar acción de error
    dispatch({
      type: 'FIND_PAYMENT_INTENT_ERROR',
      payload: error.message || 'Error al obtener Payment Intent',
    });
  }
};


// Define tu acción fetchVideos
export const fetchVideos = () => {
  return async (dispatch) => {
    try {
      // Obtén el token del almacenamiento local del navegador
      const token = localStorage.getItem('jwtToken');

      // Imprime el token en la consola para verificarlo
      console.log('Token:', token);

      if (!token) {
        // Maneja el caso en que el token no esté disponible
        dispatch({
          type: 'FETCH_VIDEOS_ERROR',
          payload: 'Token no disponible',
        });
        return;
      }

      const response = await axios.get(`${BASE_URL}/dashboard/videos/getAllVideosGroupedBySections`, {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });

      // Luego del éxito, despacha una acción para almacenar los videos en tu estado
      dispatch({
        type: 'FETCH_VIDEOS_SUCCESS',
        payload: response.data, // <-- Usando la respuesta directamente
      });

    } catch (error) {
      // Despacha una acción en caso de error
      dispatch({
        type: 'FETCH_VIDEOS_ERROR',
        payload: error,
      });
    }
  };

};



export const createNewSection = (newSectionName) => {
  return async (dispatch, getState) => {
    try {
      const token = localStorage.getItem('jwtToken');

      console.log('Token:', token); // Imprimir el token

      if (!token) {
        throw new Error('Token no disponible');
      }

      // Intenta obtener el ID del usuario usando la API
      const userInfoResponse = await axios.get(`${BASE_URL}/:8080/userInfo`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      const userId = userInfoResponse.data.userId; // <-- Aquí es donde hicimos el cambio

      console.log('ID del usuario:', userId); // Imprimir el ID del usuario

      if (!userId) {
        throw new Error('ID de usuario no disponible');
      }

      const apiUrl = `${BASE_URL}/dashboard/videos/createSection`;
      const sectionData = {
        name: newSectionName,
        addedBy: userId,
      };

      const headers = {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
      };

      const response = await axios.post(apiUrl, sectionData, { headers });

      if (response.data) {
        dispatch(fetchVideos());
      }
    } catch (error) {
      console.error('Error al crear la nueva sección:', error);
      // Aquí puedes despachar una acción de error si lo deseas
    }
  };
};

export const insertVideo = (videoData) => {
  return async (dispatch) => {
    try {



      // Obtener el token del almacenamiento local
      const token = localStorage.getItem('jwtToken');

      if (!token) {
        throw new Error('Token no disponible');
      }

      // Intenta obtener el ID del usuario usando la API
      const userInfoResponse = await axios.get(`${BASE_URL}/userInfo`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      const userId = userInfoResponse.data.userId; // <-- Aquí es donde hicimos el cambio

      if (!userId) {
        throw new Error('ID de usuario no disponible');
      }

      // Agregar el ID del usuario al objeto videoData
      videoData.addedBy = userId;

      // Imprimir videoData en la consola para verificar qué se está enviando
      console.log('videoData:', videoData);

      // Llamada al endpoint /dashboard/videos/insertVideo
      const response = await axios.post(`${BASE_URL}/dashboard/videos/insertVideo`, videoData, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });

      // Después del éxito, puedes despachar una acción para actualizar tu estado o simplemente continuar
      dispatch({
        type: 'INSERT_VIDEO_SUCCESS',
        payload: response.data,
      });

    } catch (error) {
      // Despacha una acción en caso de error
      dispatch({
        type: 'INSERT_VIDEO_ERROR',
        payload: error,
      });
    }
  };
};


export const updateSectionSequence = (sectionId, newSequence) => {
  return async (dispatch) => {
    try {
      const token = localStorage.getItem('jwtToken');

      if (!token) {
        throw new Error('Token no disponible');
      }

      // Obtener el ID del usuario usando la API
      const userInfoResponse = await axios.get(`${BASE_URL}/userInfo`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      const userId = userInfoResponse.data.userId;

      if (!userId) {
        throw new Error('ID de usuario no disponible');
      }

      const apiUrl = `${BASE_URL}/dashboard/videos/updateSectionSequence`;
      const data = {
        section_id: sectionId,
        new_sequence_number: newSequence,
        addedBy: userId, // Usar el userId obtenido
      };

      const headers = {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
      };

      const response = await axios.post(apiUrl, data, { headers });

      if (response.data) {
        dispatch(fetchVideos());
      }
    } catch (error) {
      console.error('Error al actualizar la secuencia de la sección:', error);
    }
  };
};

export const updateVideoSequence = (sectionId, videoId, newSequence) => {
  return async (dispatch) => {
    try {
      const token = localStorage.getItem('jwtToken');

      if (!token) {
        throw new Error('Token no disponible');
      }

      // Obtener el ID del usuario usando la API
      const userInfoResponse = await axios.get(`${BASE_URL}/userInfo`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      const userId = userInfoResponse.data.userId;

      if (!userId) {
        throw new Error('ID de usuario no disponible');
      }

      const apiUrl = `${BASE_URL}/dashboard/videos/updateVideoSequence`;
      const data = {
        section_id: sectionId,
        video_id: videoId,
        new_sequence_number: newSequence,
        addedBy: userId, // Usar el userId obtenido
      };

      const headers = {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
      };

      const response = await axios.post(apiUrl, data, { headers });

      if (response.data) {
        dispatch(fetchVideos());
      }
    } catch (error) {
      console.error('Error al actualizar la secuencia del video:', error);
    }
  };
};

export const updateSectionName = (sectionId, newSectionName) => {
  return async (dispatch) => {
    try {
      const token = localStorage.getItem('jwtToken');

      if (!token) {
        throw new Error('Token no disponible');
      }

      const apiUrl = `${BASE_URL}/dashboard/videos/updateSectionName`;
      const data = {
        section_id: sectionId,
        new_name: newSectionName,
      };

      const headers = {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
      };

      const response = await axios.put(apiUrl, data, { headers });

      if (response.data) {
        dispatch({
          type: 'UPDATE_SECTION_NAME_SUCCESS',
          payload: response.data,
        });
      }
    } catch (error) {
      console.error('Error al actualizar el nombre de la sección:', error);
      dispatch({
        type: 'UPDATE_SECTION_NAME_ERROR',
        payload: error,
      });
    }
  };
};

export const updateVideoTitle = (videoId, newTitle) => {
  return async (dispatch) => {
    try {
      const token = localStorage.getItem('jwtToken');

      if (!token) {
        throw new Error('Token no disponible');
      }

      const apiUrl = `${BASE_URL}/dashboard/videos/updateVideoTitle`;
      const data = {
        video_id: videoId,
        new_title: newTitle,
      };

      const headers = {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
      };

      const response = await axios.put(apiUrl, data, { headers });

      if (response.data) {
        dispatch({
          type: 'UPDATE_VIDEO_TITLE_SUCCESS',
          payload: response.data,
        });
      }
    } catch (error) {
      console.error('Error al actualizar el título del video:', error);
      dispatch({
        type: 'UPDATE_VIDEO_TITLE_ERROR',
        payload: error,
      });
    }
  };
};


export const deactivateVideo = (videoId) => {
  return async (dispatch) => {
    try {
      const token = localStorage.getItem('jwtToken');

      if (!token) {
        throw new Error('Token no disponible');
      }

      const apiUrl = `${BASE_URL}/dashboard/videos/deactivateVideo`;
      const requestData = {
        video_id: videoId,
      };

      const headers = {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
      };

      const response = await axios.put(apiUrl, requestData, { headers });

      if (response.data) {
        dispatch({
          type: 'DEACTIVATE_VIDEO_SUCCESS',
          payload: { videoId },
        });
        dispatch(fetchVideos()); // Actualiza la lista de videos después de la desactivación
      }
    } catch (error) {
      console.error('Error al desactivar el video:', error);
      dispatch({
        type: 'DEACTIVATE_VIDEO_ERROR',
        payload: error,
      });
    }
  };
};

export const deactivateSection = (sectionId) => {
  return async (dispatch) => {
    try {
      const token = localStorage.getItem('jwtToken');

      if (!token) {
        throw new Error('Token no disponible');
      }

      const apiUrl = `${BASE_URL}/dashboard/videos/deactivateSection`;
      const requestData = {
        section_id: sectionId,
      };

      const headers = {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
      };

      const response = await axios.put(apiUrl, requestData, { headers });

      if (response.data) {
        dispatch({
          type: 'DEACTIVATE_SECTION_SUCCESS',
          payload: { sectionId },
        });
        dispatch(fetchVideos()); // Actualiza la lista de videos después de la desactivación
      }
    } catch (error) {
      console.error('Error al desactivar la sección:', error);
      dispatch({
        type: 'DEACTIVATE_SECTION_ERROR',
        payload: error,
      });
    }
  };
};

export const updateVideoUrl = (videoId, newUrl) => {
  return async (dispatch) => {
    try {
      // Obtener el token del almacenamiento local
      const token = localStorage.getItem('jwtToken');

      if (!token) {
        throw new Error('Token no disponible');
      }

      // Configurar la URL y el cuerpo de la solicitud
      const apiUrl = `${BASE_URL}/dashboard/videos/updateVideoUrl/${videoId}`;
      const requestData = {
        newUrl: newUrl,
      };

      // Configurar los encabezados de la solicitud
      const headers = {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
      };

      // Realizar la solicitud HTTP PUT
      const response = await axios.put(apiUrl, requestData, { headers });

      if (response.data) {
        // Despachar una acción de éxito o actualizar el estado según corresponda
        dispatch({
          type: 'UPDATE_VIDEO_URL_SUCCESS',
          payload: { videoId, newUrl },
        });
        // Agrega la llamada a fetchVideos() después de la acción exitosa
        dispatch(fetchVideos());
      }
    } catch (error) {
      // Manejar errores y despachar una acción de error si es necesario
      console.error('Error al actualizar la URL del video:', error);
      dispatch({
        type: 'UPDATE_VIDEO_URL_ERROR',
        payload: error,
      });
    }
  };
};

export const updateVideoDescription = (videoId, newDescription) => {
  return async (dispatch) => {
    try {
      const token = localStorage.getItem('jwtToken');

      if (!token) {
        throw new Error('Token no disponible');
      }

      const apiUrl = `${BASE_URL}/dashboard/videos/updateVideoDescription/${videoId}`;
      const data = {
        newDescription: newDescription,
      };

      const headers = {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
      };

      const response = await axios.put(apiUrl, data, { headers });

      if (response.data) {
        dispatch({
          type: 'UPDATE_VIDEO_DESCRIPTION_SUCCESS',
          payload: { videoId, newDescription },
        });
        // Agrega la llamada a fetchVideos() después de la acción exitosa
        dispatch(fetchVideos());
      }
    } catch (error) {
      console.error('Error al actualizar la descripción del video:', error);
      dispatch({
        type: 'UPDATE_VIDEO_DESCRIPTION_ERROR',
        payload: error,
      });
    }
  };
};

// Acción asincrónica para marcar un video como visto
export const markVideoAsWatched = (videoId) => {
  return async (dispatch) => {
    try {
      const token = localStorage.getItem('jwtToken');

      if (!token) {
        dispatch({
          type: 'MARK_VIDEO_AS_WATCHED_ERROR',
          payload: 'Token no disponible',
        });
        return;
      }

      // Llamada a la API para obtener userId
      const userInfoResponse = await axios.get(`${BASE_URL}/userInfo`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      const userId = userInfoResponse.data.userId;

      if (!userId) {
        dispatch({
          type: 'MARK_VIDEO_AS_WATCHED_ERROR',
          payload: 'ID de usuario no disponible',
        });
        return;
      }

      console.log(`userId: ${userId}, videoId: ${videoId}`);


      const apiUrl = `${BASE_URL}/dashboard/videos/markVideoAsWatched/${userId}/${videoId}`;
      const headers = {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
      };

      const response = await axios.put(apiUrl, null, { headers });

      if (response.data) {
        dispatch({
          type: 'MARK_VIDEO_AS_WATCHED_SUCCESS',
          payload: { videoId },
        });
      }
    } catch (error) {
      console.error('Error al marcar el video como visto:', error);
      dispatch({
        type: 'MARK_VIDEO_AS_WATCHED_ERROR',
        payload: error,
      });
    }
  };
};



// Acción asincrónica para desmarcar un video como visto
export const unmarkVideoAsWatched = (videoId) => {
  return async (dispatch) => {
    try {
      const token = localStorage.getItem('jwtToken');

      if (!token) {
        dispatch({
          type: 'UNMARK_VIDEO_AS_WATCHED_ERROR',
          payload: 'Token no disponible',
        });
        return;
      }

      // Llamada a la API para obtener userId
      const userInfoResponse = await axios.get(`${BASE_URL}/userInfo`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      const userId = userInfoResponse.data.userId;

      if (!userId) {
        dispatch({
          type: 'UNMARK_VIDEO_AS_WATCHED_ERROR',
          payload: 'ID de usuario no disponible',
        });
        return;
      }

      console.log(`userId: ${userId}, videoId: ${videoId}`);


      const apiUrl = `${BASE_URL}/dashboard/videos/unmarkVideoAsWatched/${userId}/${videoId}`;
      const headers = {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
      };

      const response = await axios.put(apiUrl, null, { headers });

      if (response.data) {
        dispatch({
          type: 'UNMARK_VIDEO_AS_WATCHED_SUCCESS',
          payload: { videoId },
        });
      }
    } catch (error) {
      console.error('Error al desmarcar el video como visto:', error);
      dispatch({
        type: 'UNMARK_VIDEO_AS_WATCHED_ERROR',
        payload: error,
      });
    }
  };
};

// Acción asincrónica para obtener videos por sección y usuario
export const fetchVideosByUser = () => {
  return async (dispatch) => {
    try {
      const token = localStorage.getItem('jwtToken');

      if (!token) {
        dispatch({
          type: 'FETCH_VIDEOS_BY_USER_ERROR',
          payload: 'Token no disponible',
        });
        return;
      }

      // Llamada a la API para obtener userId
      const userInfoResponse = await axios.get(`${BASE_URL}/userInfo`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      const userId = userInfoResponse.data.userId;

      if (!userId) {
        dispatch({
          type: 'FETCH_VIDEOS_BY_USER_ERROR',
          payload: 'ID de usuario no disponible',
        });
        return;
      }

      const response = await axios.get(`${BASE_URL}/dashboard/videos/getAllVideosBySectionsAndUser/${userId}`, {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });

      dispatch({
        type: 'FETCH_VIDEOS_BY_USER_SUCCESS',
        payload: response.data,
      });

    } catch (error) {
      dispatch({
        type: 'FETCH_VIDEOS_BY_USER_ERROR',
        payload: error.message || 'Error al obtener videos por usuario',
      });
    }
  };
};






