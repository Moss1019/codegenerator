import { CircularProgress } from "@mui/material";
import { Box, styled } from "@mui/system";

const ModalBox = styled(Box)(({theme}) => ({
  height: '100vh',
  width: '100vw',
  position: 'absolute',
  top: '0',
  left: '0',
  zIndex: 1000,
  backgroundColor: theme.palette.mode === 'light' ? '#070707cc' : '#97979799',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  color: theme.palette.background.default
}));

function LoadingModal() {
  return (
    <ModalBox>
      <CircularProgress color="inherit" />
    </ModalBox>
  );
}

export default LoadingModal;
