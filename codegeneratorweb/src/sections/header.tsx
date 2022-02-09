import { 
  AppBar,
  Box, 
  Drawer, 
  IconButton, 
  List, 
  ListItem, 
  ListItemText,
  styled,
  Toolbar,
  Typography
} from "@mui/material";
import { useNavigate } from 'react-router-dom';
import { useState } from "react";
import { changeTheme } from '../store/theme';
import MenuIcon from "@mui/icons-material/Menu";
import DarkModeIcon from "@mui/icons-material/DarkMode";
import LightModeIcon from "@mui/icons-material/LightMode";
import { useAppDispatch, useAppSelector } from "../store/hooks";
import routes, { RouteInfo } from "../util/routes";

const LogoTypography = styled(Typography)(() => ({
  display: 'flex', 
  alignItems: 'center' 
}))

interface MenuDrawerProp {
  showDrawer: boolean;
  onMenuDrawerClose: (isOpen: boolean) => void;
}

function MenuDrawer({
  showDrawer,
  onMenuDrawerClose
}: MenuDrawerProp) {
  const navigate = useNavigate();

  const links = routes.map((r: RouteInfo, i: number) => (
    <ListItem key={i} button onClick={() => { 
          navigate(r.routePath, {replace: true});
          onMenuDrawerClose(false);
        }
      }>
      <ListItemText primary={r.routeText} />
    </ListItem>
  ));

  return (
    <Drawer
      open={showDrawer}
      anchor="left"
      onClose={() => onMenuDrawerClose(false)}
    >
      <Box>
        <List>
          <ListItem>
            <LogoTypography 
              variant="h4" 
              color="primary">
              <img src="iceberg.png" alt="logo" height="75px"/>
              Polar Tundra
            </LogoTypography>
          </ListItem>

          {links}

        </List>
      </Box>
    </Drawer>
  );
}

function Header() {
  const light = useAppSelector((state) => state.lightMode.value);
  const dispatch = useAppDispatch();

  const [showDrawer, setShowDrawer] = useState<boolean>(false);

  return (
    <>
    <MenuDrawer 
      showDrawer={showDrawer}
      onMenuDrawerClose={(isOpen) => setShowDrawer(isOpen)}
    />
    <Box sx={ { flexGrow: 1 } }>
      <AppBar position="static">
        <Toolbar>
          <IconButton
            size="large"
            edge="start"
            aria-label="open drawer"
            color="inherit"
            sx={{mr: 2}}
            onClick={() => setShowDrawer(!showDrawer)}
          >
            <MenuIcon />
          </IconButton>

          <Box sx={ { flexGrow: 1 } }>

          </Box>

          <Box>
            <IconButton
              color="inherit"
              onClick={() => dispatch(changeTheme())}
            >
              {!light && 
              <LightModeIcon />}
              {light &&
              <DarkModeIcon />}
            </IconButton>
          </Box>
        </Toolbar>
      </AppBar>
    </Box>
    </>
  );
}

export default Header;
