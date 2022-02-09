import { 
  Checkbox,
  FormControl,
  FormControlLabel,
  Grid,
  MenuItem,
  Radio,
  RadioGroup,
  Select,
  TextField,
  Typography,
  styled,
  FormHelperText
 } from "@mui/material";
import StyledPaper from "../controls/styled-paper";
import { CodegenerateRequest } from "../model/codegenerate-request";
import appEnvironments, { AppEnvironment } from '../util/app-environments';
import { elasticSearchDependency, ExternalDependency, flutterDependency, FrontendDependency, kafkaDependency, reactDependency } from "../util/external-dependencies";
import databaseOptions, { DatabaseOption } from '../util/database-options';
import { ValidationError } from "../util/validation-error";
import { useMemo } from "react";

const MarginedGridItem = styled(Grid)(() => ({
  marginBottom: '1.2rem'
}))

const ErrorFormHelperText = styled(FormHelperText)(({theme}) => ({
  color: theme.palette.error.main
}))

interface FormCheckboxProps {
  dep: ExternalDependency | FrontendDependency;
  value: boolean;
  onChange: (checked: boolean) => void;
}

function FormCheckbox({
  dep,
  value,
  onChange
}: FormCheckboxProps) {
  return (
    <FormControlLabel 
      label={dep.dependency}
      value={dep.value}
      control={
        <Checkbox checked={value} onChange={(ev:any) => onChange(ev.target.checked)}/>
      }
    />
  );
}

interface RequiredFormControlProps {
  label: string;
  fieldName: string;
  errors: ValidationError[];
  config: CodegenerateRequest;
  onConfigUpdate: (fieldName: string, value: any) => void
}

function RequiredFormControl({
  label,
  fieldName,
  errors,
  config,
  onConfigUpdate
}: RequiredFormControlProps) {
  const hasError = useMemo(() => {
    return errors.find(e => e.fieldName === fieldName) !== undefined;
  }, [errors, fieldName]);

  const errorMessage = useMemo(() => {
    return errors.find(e => e.fieldName === fieldName)?.error;
  }, [errors, fieldName])

  const value = useMemo(() => {
    const obj: any = { ...config };
    return obj[fieldName];
  }, [config, fieldName])

  return (
    <FormControl fullWidth>
      <TextField 
        required
        error={hasError}
        label={label}
        variant="standard" 
        value={value}
        onChange={(ev) => onConfigUpdate(fieldName, ev.target.value)}
      />
      {hasError && 
      <ErrorFormHelperText>{errorMessage}</ErrorFormHelperText>}
    </FormControl>
  );
}

interface ConfigurationProps {
  config: CodegenerateRequest;
  errors: ValidationError[];
  onConfigUpdate: (field: string, value: any) => void;
}

const environmentItems = appEnvironments.map((env: AppEnvironment, i: number) => (
  <MenuItem key={i} value={env.value}>{env.environment}</MenuItem>
))

const databaseRadio = databaseOptions.map((opt: DatabaseOption, i: number) => (
  <FormControlLabel
    key={i}
    label={opt.database}
    value={opt.value}
    control={
      <Radio value={opt.value} />
    }
  />
));

function Configuration({
  config,
  errors,
  onConfigUpdate
}: ConfigurationProps) {
  const environmentInError = useMemo(() => {
    return errors.find(e => e.fieldName === 'environment') !== undefined
  }, [errors])

  const environmentError = useMemo(() => {
    return errors.find(e => e.fieldName === 'environment')?.error;
  }, [errors])

  const onExternalsUpdate = (isSet: boolean, value: number) => {
    const newExternals = isSet ? 
      config.externalSystems.concat(value) :
      config.externalSystems.filter(v => v !== value);
    onConfigUpdate('externalSystems', newExternals);
  }

  const onFrontendUpdate = (isSet: boolean, value: number) => {
    const newFrontends = isSet ?
      config.frontendSystems.concat(value) :
      config.frontendSystems.filter(v => v !== value);
    onConfigUpdate('frontendSystems', newFrontends);
  }

  const isExternalSet = (value: number) => {
    return config.externalSystems.filter(v => v === value).length > 0;
  }

  const isFrontendSet = (value: number) => {
    return config.frontendSystems.filter(v => v === value).length > 0;
  }

  return (
    <StyledPaper>
      <Grid container>
        <MarginedGridItem item xs={12}>
          <FormControl fullWidth>
            <Typography variant="h6" color="primary">
              Environment
            </Typography>
            <Select
              value={config.environment}
              onChange={(ev: any) => onConfigUpdate('environment', ev.target.value)}
            >
              {config.environment === -1 &&
              <MenuItem value={-1}>Select...</MenuItem>}
              {environmentItems}
            </Select>
            {environmentInError &&
            <ErrorFormHelperText>
              {environmentError}
            </ErrorFormHelperText>}
          </FormControl>
        </MarginedGridItem>

        <MarginedGridItem item xs={12}>
          <Typography variant="h6" color="primary">
            Project name
          </Typography>
          <Grid container>
            <Grid item xs={12} sm={6}>
              <RequiredFormControl 
                label="Root package/namespace" 
                fieldName="rootName"
                config={config}
                errors={errors}
                onConfigUpdate={onConfigUpdate}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <RequiredFormControl 
                label="Project name"
                fieldName="projectName"
                config={config}
                errors={errors}
                onConfigUpdate={onConfigUpdate}
              />
            </Grid>
          </Grid>
        </MarginedGridItem>

        <MarginedGridItem item xs={12} sm={6}>

          <FormControl>
            <Typography variant="h6" color="primary">
              External dependencies
            </Typography>
            <FormCheckbox 
              dep={elasticSearchDependency}
              value={isExternalSet(elasticSearchDependency.value)}
              onChange={(v) => onExternalsUpdate(v, elasticSearchDependency.value)}
            />
            <FormCheckbox 
              dep={kafkaDependency}
              value={isExternalSet(kafkaDependency.value)}
              onChange={(v) => onExternalsUpdate(v, kafkaDependency.value)}
            /> 
          </FormControl>

        </MarginedGridItem>

        <MarginedGridItem item xs={12} sm={6}>
          
          <FormControl>
            <Typography variant="h6" color="primary">
              Data store
            </Typography>
            <RadioGroup
              value={config.databaseType}
              onChange={(ev: any) => onConfigUpdate('databaseType', ev.target.value)}
            >
              {databaseRadio}
            </RadioGroup>
          </FormControl>

        </MarginedGridItem>

        <MarginedGridItem item xs={12} sm={6}>
          <Typography variant="h6" color="primary">
            Front end HTTP clients
          </Typography>
          <FormControl>
            <FormCheckbox 
              dep={reactDependency}
              value={isFrontendSet(reactDependency.value)}
              onChange={(v) => onFrontendUpdate(v, reactDependency.value)}
            />
            <FormCheckbox 
              dep={flutterDependency}
              value={isFrontendSet(flutterDependency.value)}
              onChange={(v) => onFrontendUpdate(v, flutterDependency.value)}
            />
          </FormControl>
        </MarginedGridItem>
      </Grid>
    </StyledPaper>
  );
}

export default Configuration;
