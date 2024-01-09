// Importa cualquier acción o biblioteca que necesites
// Por ejemplo, si tienes un action creator llamado `logout`, asegúrate de importarlo aquí

const initialState = {
  isAuthenticated: false,
  user: null,

  allUsers: [], // Estado para almacenar todos los usuarios
  allUsersError: null, // Estado para almacenar errores relacionados con obtener todos los usuarios


  registerError: null,  // Para almacenar mensajes de error de registro
  loginError: null,  // Para almacenar mensajes de error de inicio de sesión
  registerSuccess: false,  // Para señalar si el registro fue exitoso
  forgotPasswordMessage: null,
  forgotPasswordError: null,
  resetPasswordMessage: null,
  resetPasswordError: null,

  videos: null, // Añadir un estado inicial para videos
  videosError: null, // Añadir un estado inicial para errores de videos
  paymentIntent: null,
  paymentIntentError: null,

  phones: null, // Estado para almacenar la información de teléfonos
  phonesError: null, // Estado para almacenar errores relacionados con teléfonos
  address: null, // Estado para almacenar la información de dirección
  addressError: null, // Estado para almacenar errores relacionados con dirección
  profilePicture: null, // Estado para almacenar la imagen de perfil del usuario
  profilePictureError: null, // Estado para almacenar errores relacionados con la imagen de perfil


};

const authReducer = (state = initialState, action) => {
  switch (action.type) {
    case 'LOGIN':
      return {
        ...state,
        isAuthenticated: true,
        user: action.payload,  // Actualizando el usuario con los datos del servidor
        registerError: null,
        loginError: null,
        registerSuccess: false,
      };
    // En tu reducer
    case 'INITIALIZE_AUTH':
      return {
        ...state,
        isAuthenticated: true,
        user: action.payload.user,
        token: action.payload.token, // Si necesitas el token en el estado
      };

    case 'LOGOUT':
      return {
        ...state,
        isAuthenticated: false,
        user: null,
        registerError: null,
        loginError: null,
        registerSuccess: false,
      };
    case 'LOGIN_ERROR':
      return {
        ...state,
        loginError: action.payload,
      };
    case 'REGISTER_ERROR':
      return {
        ...state,
        registerError: action.payload,
        registerSuccess: false,  // Restablecer el estado de éxito del registro
      };
    case 'REGISTER_SUCCESS':
      return {
        ...state,
        registerError: null,  // Limpiar cualquier mensaje de error previo
        registerSuccess: true,  // Indicar que el registro fue exitoso
      };

    case 'FORGOT_PASSWORD_SUCCESS':
      return {
        ...state,
        forgotPasswordMessage: action.payload,
        forgotPasswordError: null,
      };
    case 'FORGOT_PASSWORD_ERROR':
      return {
        ...state,
        forgotPasswordError: action.payload,
      };
      case 'RESET_PASSWORD_SUCCESS':
        return {
          ...state,
          resetPasswordMessage: action.payload, // Mensaje de éxito
          resetPasswordError: null
        };
      
      case 'RESET_PASSWORD_ERROR':
        return {
          ...state,
          resetPasswordError: action.payload // Mensaje de error
        };

        case 'FIND_ALL_USERS_BY_ADMIN_SUCCESS':
      return {
        ...state,
        allUsers: action.payload,
        allUsersError: null,
      };
    case 'FIND_ALL_USERS_BY_ADMIN_ERROR':
      return {
        ...state,
        
        allUsersError: action.payload,
      };

      
    case 'FIND_PAYMENT_INTENT_SUCCESS':
      return {
        ...state,
        paymentIntent: action.payload,
        paymentIntentError: null,
      };
    case 'FIND_PAYMENT_INTENT_ERROR':
      return {
        ...state,
        paymentIntentError: action.payload,
      };


    case 'FETCH_VIDEOS_SUCCESS':
      return {
        ...state,
        videos: action.payload,
        videosError: null,
      };
    case 'FETCH_VIDEOS_ERROR':
      return {
        ...state,
        videosError: action.payload,
      };
    case 'INSERT_VIDEO_SUCCESS':
      return {
        ...state,
        video: action.payload,  // almacenar el video recién creado en el estado
        insertVideoError: null,
      };

    case 'INSERT_VIDEO_ERROR':
      return {
        ...state,
        insertVideoError: action.payload,
      };

    case 'UPDATE_SECTION_NAME_SUCCESS':
      return {
        ...state,
        // Actualiza el estado según la respuesta del servidor si es necesario
        // Por ejemplo, si deseas actualizar el nombre de la sección en el estado:
        videos: state.videos.map((section) => {
          if (section.id === action.payload.sectionId) {
            return {
              ...section,
              name: action.payload.newName,
            };
          }
          return section;
        }),
      };
    case 'UPDATE_SECTION_NAME_ERROR':
      return {
        ...state,
        // Manejar el error si es necesario
        // Puedes agregar lógica aquí para mostrar mensajes de error o tomar medidas adicionales
        sectionNameUpdateError: action.payload, // Agregar un estado para el error de actualización del nombre de la sección
      };
    case 'UPDATE_VIDEO_TITLE_SUCCESS':
      return {
        ...state,
        // Actualiza el estado según la respuesta del servidor si es necesario
        // Por ejemplo, si deseas actualizar el título del video en el estado:
        videos: state.videos.map((section) => {
          return {
            ...section,
            videos: section.videos.map((video) => {
              if (video.id === action.payload.videoId) {
                return {
                  ...video,
                  title: action.payload.newTitle,
                };
              }
              return video;
            }),
          };
        }),
      };
    case 'UPDATE_VIDEO_TITLE_ERROR':
      return {
        ...state,
        // Manejar el error si es necesario
        // Puedes agregar lógica aquí para mostrar mensajes de error o tomar medidas adicionales
        videoTitleUpdateError: action.payload, // Agregar un estado para el error de actualización del título del video
      };

    case 'DEACTIVATE_VIDEO_SUCCESS':
      return {
        ...state,
        videos: state.videos.map((section) => {
          return {
            ...section,
            videos: section.videos.map((video) => {
              if (video.id === action.payload.videoId) {
                return {
                  ...video,
                  isActive: false, // Suponiendo que tienes un campo 'isActive' en tus objetos de video
                };
              }
              return video;
            }),
          };
        }),
      };

    case 'DEACTIVATE_VIDEO_ERROR':
      return {
        ...state,
        deactivateVideoError: action.payload,
      };

    case 'DEACTIVATE_SECTION_SUCCESS':
      return {
        ...state,
        videos: state.videos.map((section) => {
          if (section.id === action.payload.sectionId) {
            return {
              ...section,
              isActive: false, // Suponiendo que tienes un campo 'isActive' en tus objetos de sección
            };
          }
          return section;
        }),
      };

    case 'DEACTIVATE_SECTION_ERROR':
      return {
        ...state,
        deactivateSectionError: action.payload,
      };

    case 'UPDATE_VIDEO_URL_SUCCESS':
      // Agregar un nuevo caso para manejar la acción de actualización de URL del video
      return {
        ...state,
        videos: state.videos.map((section) => {
          return {
            ...section,
            videos: section.videos.map((video) => {
              if (video.id === action.payload.videoId) {
                return {
                  ...video,
                  url: action.payload.newUrl, // Actualizar la URL del video
                };
              }
              return video;
            }),
          };
        }),
      };

    case 'UPDATE_VIDEO_DESCRIPTION_SUCCESS':
      return {
        ...state,
        videos: state.videos.map((section) => {
          return {
            ...section,
            videos: section.videos.map((video) => {
              if (video.id === action.payload.videoId) {
                return {
                  ...video,
                  description: action.payload.newDescription,
                };
              }
              return video;
            }),
          };
        }),
      };
    case 'UPDATE_VIDEO_DESCRIPTION_ERROR':
      return {
        ...state,
        videosError: action.payload,
      };

    case 'UNMARK_VIDEO_AS_WATCHED_SUCCESS':
      return {
        ...state,
        videos: state.videos.map((section) => {
          return {
            ...section,
            videos: section.videos.map((video) => {
              if (video.id === action.payload.videoId) {
                return {
                  ...video,
                  isWatched: false, // Cambiar el estado de visto a falso
                };
              }
              return video;
            }),
          };
        }),
      };

    case 'UNMARK_VIDEO_AS_WATCHED_ERROR':
      return {
        ...state,
        unmarkVideoAsWatchedError: action.payload,
      };

    case 'FETCH_VIDEOS_BY_USER_SUCCESS':
      return {
        ...state,
        videos: action.payload, // Actualiza el estado con los videos obtenidos
        videosError: null, // Limpia cualquier error previo
      };

    case 'FETCH_VIDEOS_BY_USER_ERROR':
      return {
        ...state,
        videosError: action.payload, // Actualiza el estado con el mensaje de error
      };


    // Caso para actualizar los teléfonos en el estado
    case 'UPDATE_PHONES_SUCCESS':
      return {
        ...state,
        phones: action.payload,
        phonesError: null,
      };

    // Caso para manejar errores al actualizar los teléfonos
    case 'UPDATE_PHONES_ERROR':
      return {
        ...state,
        phonesError: action.payload,
      };

    // Caso para seleccionar la dirección en el estado
    case 'SELECT_ADDRESS_SUCCESS':
      return {
        ...state,
        address: action.payload,
        addressError: null,
      };

    // Caso para manejar errores al seleccionar la dirección
    case 'SELECT_ADDRESS_ERROR':
      return {
        ...state,
        addressError: action.payload,
      };

    // Caso para actualizar la imagen de perfil en el estado
    case 'UPDATE_PROFILE_PICTURE_SUCCESS':
      return {
        ...state,
        profilePicture: action.payload,
        profilePictureError: null,
      };

    // Caso para manejar errores al actualizar la imagen de perfil
    case 'UPDATE_PROFILE_PICTURE_ERROR':
      return {
        ...state,
        profilePictureError: action.payload,
      };

    default:
      return state;
  };




};




export default authReducer;
