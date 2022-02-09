import { configureStore } from '@reduxjs/toolkit';

import themeReducer from './theme';
import requestReducer from './request';
import loadingReducer from './loading';

const store = configureStore({
  reducer: {
    lightMode: themeReducer,
    request: requestReducer,
    loading: loadingReducer
  }
});

export type AppState = ReturnType<typeof store.getState>

export type AppDispatch = typeof store.dispatch;

export default store;
