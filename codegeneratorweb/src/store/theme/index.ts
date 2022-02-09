import { createSlice } from "@reduxjs/toolkit";
import { AppState } from "..";

interface ThemeState { 
  value: boolean
}

const initialState: ThemeState = {
  value: true
}

export const themeSlice = createSlice({
  name: 'light',
  initialState,
  reducers: {
    changeTheme: (state) => {
      return {
        value: !state.value
      }
    }
  }
});

export const { changeTheme } = themeSlice.actions;

export const selectDarkMode = (state: AppState) => state.lightMode.value;

export default themeSlice.reducer;
