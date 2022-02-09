import { 
  Checkbox,
  FormControl, 
  FormControlLabel, 
  Grid, 
  TextField 
} from "@mui/material";
import { useEffect, useState } from "react";
import StyledPaper from "../controls/styled-paper";
import { GuidRequest } from "../model/guid-request";

export interface GuidSettingsProps {
  setRequest: (newRequest: GuidRequest) => void;
}

function GuidSettings({
  setRequest
}: GuidSettingsProps) {
  const [amount, setAmount] = useState<number>(0);
  const [dashed, setDashed] = useState<boolean>(true);
  const [braced, setBraced] = useState<boolean>(false);

  useEffect(() => {
    const newRequest = {
      amount,
      dashed,
      braced
    };
    setRequest(newRequest);
  }, [amount, dashed, braced, setRequest])

  const onAmountChanged = (value: string) => {
    const valueAsNumer = parseInt(value);
    if (isNaN(valueAsNumer)) {
      setAmount(0);
    } else {
      setAmount(valueAsNumer);
    }
  }

  return (
    <StyledPaper>
      <FormControl fullWidth>
        <TextField
          value={amount}
          label="Amount"
          onChange={(ev: any) => onAmountChanged(ev.target.value)}
        />
      </FormControl>
      
      <Grid container>
        <Grid item xs={12} sm={6}>
          <FormControl>
            <FormControlLabel
              label="Add dashes"
              value={dashed}
              control={
                <Checkbox checked={dashed} onChange={(ev: any) => setDashed(ev.target.checked)} />
              }
            />
          </FormControl>
        </Grid>

        <Grid item xs={12} sm={6}>
          <FormControl>
            <FormControlLabel
              label="Add braces"
              value={braced}
              control={
                <Checkbox checked={braced} onChange={(ev: any) => setBraced(ev.target.checked)} />
              }
              />
          </FormControl>
        </Grid>
      </Grid>
    </StyledPaper>
  );
}

export default GuidSettings;
