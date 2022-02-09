import { createSlice } from "@reduxjs/toolkit";
import { empty } from "../../model/codegenerate-request";

interface RequestState { 
  projectName: string;
  rootName: string;
  definition: string;
  databaseType: number;
  environment: number;
  externalSystems: number[];
  frontendSystems: number[];
}

const initialState: RequestState = {
  ...empty
}

export const requestSlice = createSlice({
  name: 'request',
  initialState,
  reducers: {
    setRequest: (state, action) => {
      return {...action.payload};
    }
  }
});

export const { setRequest } = requestSlice.actions;

export const selectProjectName = (state: RequestState) => state.projectName;
export const selectRootName = (state: RequestState) => state.rootName;
export const selectDefinition = (state: RequestState) => state.definition;
export const selectDataType = (state: RequestState) => state.databaseType;
export const selectEnvironment = (state: RequestState) => state.environment;
export const selectExternalSystems = (state: RequestState) => state.externalSystems;
export const selectFrontendSystems = (state: RequestState) => state.frontendSystems;

export default requestSlice.reducer;
