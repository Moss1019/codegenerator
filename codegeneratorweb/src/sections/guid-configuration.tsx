import { 
  Alert,
  Button,
  ButtonGroup,
  Container, 
  Grid, 
  Snackbar
} from "@mui/material";
import { Fragment, useState, useMemo } from "react";
import ContentBox from "../controls/content-paper";
import StyledPaper from "../controls/styled-paper";
import { generateGuids } from "../http";
import { emptyRequest, GuidRequest } from "../model/guid-request";
import ButtonGroupBox from "../controls/button-group-box";
import FlexGrid from "../controls/flex-grid";
import GuidSettings from "../containers/guid-settings";
import GuidItem from "../containers/guid-item";


function GuidConfiguration() {
  const [request, setRequest] = useState<GuidRequest>(emptyRequest);
  const [guids, setGuids] = useState<string[]>([]);
  const [showError, setShowError] = useState<boolean>(false);
  const [errorMessage, setErrorMessage] = useState<string>('');

  const guidList = useMemo(() => {
    return guids.map((guid: string, i: number) => {
      return (
        <Fragment key={i}>
          <GuidItem guid={guid}/>
        </Fragment>
      ); 
    });
  }, [guids])

  const onGenerate = () => {
    if(request.amount > 10) {
      setShowError(true);
      setErrorMessage('A maximum of 10 guids can be generated');
      return;
    }
    const req: GuidRequest = {
      amount: request.amount,
      braced: request.braced,
      dashed: request.dashed
    };
    generateGuids(req, setGuids, console.log);
  }

  const reset = () => {
    setRequest(emptyRequest);
  }

  return (
    <>
    <Container>
      <ContentBox>

        <FlexGrid container>
          <Grid item xs={12} sm={6}>
            <GuidSettings
              setRequest={setRequest}
            />
          </Grid>

          <Grid item xs={12} sm={6}>
            {guids.length > 0 && 
            <StyledPaper>
              {guidList}
            </StyledPaper>}
          </Grid>
        </FlexGrid>

        <ButtonGroupBox>
          <ButtonGroup>
            <Button variant="contained" onClick={() => onGenerate()}>
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
      autoHideDuration={3000} 
      onClose={() => setShowError(false)}
    >
      <Alert onClose={() => setShowError(false)} severity="error" sx={{ width: '100%' }}>
        {errorMessage}
      </Alert>
    </Snackbar>
    </>
  );
}

export default GuidConfiguration;
