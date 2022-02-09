import light from './light';
import dark from './dark';
import { ThemeOptions } from '@mui/material';

const resolve = (isLight: boolean): ThemeOptions => {
  return isLight ? light : dark;
}

export default resolve;
