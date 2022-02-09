import { Alert, IconButton, Snackbar, Typography } from "@mui/material";
import CopyIcon from "@mui/icons-material/CopyAll";
import { Box } from "@mui/system";
import copy from "copy-to-clipboard";
import { useState } from "react";

interface GuidItemProps {
  guid: string;
}

function GuidItem({
  guid
}: GuidItemProps) {
  const [showCopied, setShowCopied] = useState<boolean>(false);

  const copyGuid = () => {
    copy(guid);
    setShowCopied(true);
  }

  return (
    <>
    <Box sx={ {display: 'flex', alignItems: 'center', justifyContent: 'space-between'} }>
      <Typography variant="h5">{guid}</Typography>
      <IconButton
        onClick={copyGuid}
      >
        <CopyIcon /> 
      </IconButton>
    </Box>
    <Snackbar
      anchorOrigin={{ vertical: 'top', horizontal: 'center' }} 
      open={showCopied} 
      autoHideDuration={3000} 
      onClose={() => setShowCopied(false)}
    >
      <Alert onClose={() => setShowCopied(false)} severity="success" sx={{ width: '100%' }}>
        {guid} copied!
      </Alert>
    </Snackbar>
    </>
  );
}

export default GuidItem;
