import {
  Alert,
  Button,
  ButtonGroup,
  Container,
  Snackbar,
} from "@mui/material";
import { generateApp } from "../http";
import { setRequest } from '../store/request';
import Definition from "../containers/definition";
import ContentBox from "../controls/content-paper";
import Configuration from "../containers/configuration";
import { useAppDispatch, useAppSelector } from "../store/hooks";
import { CodegenerateRequest, empty, example } from "../model/codegenerate-request";
import { useState } from "react";
import { ValidationError } from "../util/validation-error";
import ButtonGroupBox from "../controls/button-group-box";
import FlexGrid from "../controls/flex-grid";
import { changeLoading } from "../store/loading";

function AppConfiguration() {
  const config: CodegenerateRequest = useAppSelector((state) => state.request);

  const [errors, setErrors] = useState<ValidationError[]>([]);
  const [showError, setShowError] = useState<boolean>(false);
  const [errorMsg, setErrorMsg] = useState<string>('');

  const dispatch = useAppDispatch();

  const onConfigUpdate = (field: string, value: any) => {
    const newConfig = {
      ...config,
      [field]: value
    };
    dispatch(setRequest(newConfig));
  }

  const onDefinitionChange = (value: string) => {
    onConfigUpdate('definition', value);
  }

  const base64ToArrayBuffer = (base64: string): Uint8Array => {
    const binaryString = window.atob(base64);
    const binaryLen = binaryString.length;
    const bytes = new Uint8Array(binaryLen);
    for (let i = 0; i < binaryLen; ++i) {
      const ascii = binaryString.charCodeAt(i);
      bytes[i] = ascii;
    }
    return bytes;
  };

  const saveByteArray = (fileName: string, byte: Uint8Array) => {
    const blob = new Blob([byte], { type: "application/gzip" });
    const link = document.createElement("a");
    link.href = window.URL.createObjectURL(blob);
    link.download = `${fileName}.tar.gz`;
    link.click();
    link.remove();
  };

  const generateProject = () => {
    const errors: ValidationError[] = [];
    if(config.definition.length === 0) {
      errors.push({
        fieldName: 'definition',
        error: 'Please specify a definition'
      })
    } else if (config.definition.length > 1000) {
      errors.push({
        fieldName: 'definition',
        error: 'Specified definition exceeds maximum of 1000 characters'
      })
    }
    if(config.environment === -1) {
      errors.push({
        fieldName: 'environment',
        error: 'Please specify an environment'
      })
    }
    if(config.projectName.length < 3) {
      errors.push({
        fieldName: 'projectName',
        error: 'Project name is too short'
      })
    }
    if(config.rootName.length < 3) {
      errors.push({
        fieldName: 'rootName',
        error: 'Root package is too short'
      })
    }
    setErrors(errors);
    if(errors.length === 0) {
      dispatch(changeLoading(true));
      generateApp(config, 
        (base64: string) => {
          saveByteArray(config.projectName, base64ToArrayBuffer(base64));
          dispatch(changeLoading(false));
        }, 
        (err: any) => { 
          dispatch(changeLoading(false)); 
          setErrorMsg(err.message); 
          setShowError(true); 
        });
    } else {
      setErrorMsg(errors[0].error || '');
      setShowError(true);
    }
  }

  const loadExample = () => {
    dispatch(setRequest(example));
  }

  const reset = () => {
    dispatch(setRequest(empty));
  }

  return (
    <>
    <Container>
      <ContentBox>

        <FlexGrid container>
          <FlexGrid item xs={12} sm={6}>
            <Definition
              definition={config.definition}
              onChange={onDefinitionChange}
            />
          </FlexGrid>

          <FlexGrid item xs={12} sm={6}>
            <Configuration
              config={config}
              errors={errors}
              onConfigUpdate={onConfigUpdate}
            />
          </FlexGrid>
        </FlexGrid>

        <ButtonGroupBox>
          <ButtonGroup>
            <Button variant="outlined" onClick={() => loadExample()}>
              example
            </Button>
            <Button variant="contained" onClick={() => generateProject()}>
              generate
            </Button>
            <Button variant="outlined" onClick={() => reset()}>
              reset
            </Button>
          </ButtonGroup>
        </ButtonGroupBox>

      </ContentBox>
    </Container>
    <Snackbar 
      anchorOrigin={{ vertical: 'top', horizontal: 'center' }} 
      open={showError} 
      autoHideDuration={6000} 
      onClose={() => setShowError(false)}
    >
      <Alert onClose={() => setShowError(false)} severity="error" sx={{ width: '100%' }}>
        {errorMsg}
      </Alert>
    </Snackbar>
    </>
  );
}

export default AppConfiguration;
