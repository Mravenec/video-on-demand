// videoAccessReducer.js

const initialState = {
    activeVideoAccessUsers: [],
    videoAccessError: null,
    insertVideoAccessSuccess: false,
    updateVideoAccessEmailSuccess: false,
    deactivateVideoAccessSuccess: false,
  };
  
  const videoAccessReducer = (state = initialState, action) => {
    switch (action.type) {
      case 'GET_ACTIVE_VIDEO_ACCESS_USERS_SUCCESS':
        return {
          ...state,
          activeVideoAccessUsers: action.payload,
          videoAccessError: null,
        };
      case 'GET_ACTIVE_VIDEO_ACCESS_USERS_ERROR':
        return {
          ...state,
          videoAccessError: action.payload,
        };
      case 'INSERT_VIDEO_ACCESS_SUCCESS':
        return {
          ...state,
          insertVideoAccessSuccess: true,
          videoAccessError: null,
        };
      case 'INSERT_VIDEO_ACCESS_ERROR':
        return {
          ...state,
          insertVideoAccessSuccess: false,
          videoAccessError: action.payload,
        };
      case 'UPDATE_VIDEO_ACCESS_EMAIL_SUCCESS':
        return {
          ...state,
          updateVideoAccessEmailSuccess: true,
          videoAccessError: null,
        };
      case 'UPDATE_VIDEO_ACCESS_EMAIL_ERROR':
        return {
          ...state,
          updateVideoAccessEmailSuccess: false,
          videoAccessError: action.payload,
        };
      case 'DEACTIVATE_VIDEO_ACCESS_SUCCESS':
        return {
          ...state,
          deactivateVideoAccessSuccess: true,
          videoAccessError: null,
        };
      case 'DEACTIVATE_VIDEO_ACCESS_ERROR':
        return {
          ...state,
          deactivateVideoAccessSuccess: false,
          videoAccessError: action.payload,
        };
      default:
        return state;
    }
  };
  
  export default videoAccessReducer;
  