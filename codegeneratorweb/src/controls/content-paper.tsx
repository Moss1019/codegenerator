import { Box, styled } from "@mui/material";

const ContentBox = styled(Box)(() => ({
  minHeight: 'calc(90vh - 4rem)',
  display: 'flex',
  flexFlow: 'column'
}));

export default ContentBox;