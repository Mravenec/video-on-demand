import { createStore, combineReducers, applyMiddleware } from 'redux';
import thunk from 'redux-thunk';
import authReducer from './reducers/authReducer';
import videoAccessReducer from './reducers/videoAccessReducer'; // Asegúrate de importar correctamente tu videoAccessReducer


const rootReducer = combineReducers({
  auth: authReducer,
  videoAccess: videoAccessReducer, // Agrega el videoAccessReducer aquí
});

// Aplicar middleware redux-thunk
const store = createStore(
  rootReducer,
  applyMiddleware(thunk)
);

export default store;
