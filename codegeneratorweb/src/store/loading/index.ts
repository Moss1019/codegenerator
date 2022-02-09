import { createSlice } from "@reduxjs/toolkit";
import { AppState } from "..";

interface LoadingState { 
  value: boolean
}

const initialState: LoadingState = {
  value: false
}

export const loadingSlice = createSlice({
  name: 'loading',
  initialState,
  reducers: {
    changeLoading: (state, action) => {
      return {value: action.payload}
    }
  }
});

export const { changeLoading } = loadingSlice.actions;

export const selectLoading = (state: AppState) => state.lightMode.value;

export default loadingSlice.reducer;
