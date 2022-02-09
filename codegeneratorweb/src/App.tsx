import store from './store';
import { useMemo } from 'react';
import resolveTheme from './theme';
import { Provider } from 'react-redux';
import Header from './sections/header';
import Footer from './sections/footer';
import { useAppSelector } from './store/hooks';
import SyntaxGuide from './pages/syntax-guide';
import AppGenerator from './pages/app-generator';
import GuidGenerator from './pages/guid-generator';
import { createTheme, CssBaseline, ThemeProvider } from '@mui/material';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import LoadingModal from './sections/loading-modal';

function ThemeSelector() {
  const light = useAppSelector((state) => state.lightMode.value);
  const loading = useAppSelector((state) => state.loading.value);
  
  const theme = useMemo(() => {
    return createTheme(resolveTheme(light));
  }, [light])

  const showLoading = useMemo(() => {
    return loading;
  }, [loading])

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      {showLoading &&
      <LoadingModal />}
      <Router>
        <Header />
          <Routes>
            <Route path="/" element={<AppGenerator />} />
            <Route path="/guide" element={<SyntaxGuide />} />
            <Route path="/guids" element={<GuidGenerator />} />
          </Routes>
        <Footer />
      </Router>
    </ThemeProvider> 
  );
}

function App() {
  return (
    <div className="App">
      <Provider store={store}>
        <ThemeSelector />
      </Provider>
    </div>
  );
}

export default App;
