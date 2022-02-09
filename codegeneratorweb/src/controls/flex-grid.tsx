import { Grid } from "@mui/material";
import { styled } from "@mui/system";

const FlexGrid = styled(Grid)(() => ({
  flexGrow: 1,
  display: 'flex'
}))

export default FlexGrid;
