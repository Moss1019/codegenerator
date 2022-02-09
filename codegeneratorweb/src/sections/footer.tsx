import { 
  Box, 
  Grid, 
  Typography,
  styled
} from "@mui/material";

const FooterBox = styled(Box)(() => ({
  height: '10vh',
  display: 'flex',
}))

const FlexGrid = styled(Grid)(() => ({
  flexGrow: 1,
  display: 'flex'
}))

const JustifiedGridItem = styled(Grid)(() => ({
  display: 'flex',
  justifyContent: 'center',
  alignItems: 'center',
  flexGrow: 1
}))

function Footer() {
  return (
    <FooterBox>
      <FlexGrid container>
        <JustifiedGridItem item xs={12} sm={4}>
          <Typography variant="subtitle1">
            Copyright &copy; mossonthetree
          </Typography>
        </JustifiedGridItem>

        <JustifiedGridItem item xs={12} sm={4}>
          <Typography variant="subtitle1">
            version 1.0.1 @ 2021
          </Typography>
        </JustifiedGridItem>

        <JustifiedGridItem item xs={12} sm={4}>
          <Typography variant="subtitle1">
            codegenerator<sub>{process.env.NODE_ENV === 'production' ? 'prod' : 'dev'}</sub>
          </Typography>
        </JustifiedGridItem>
      </FlexGrid>
    </FooterBox>
  );
}

export default Footer;
