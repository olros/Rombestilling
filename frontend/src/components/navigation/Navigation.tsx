import { ReactNode, ReactElement } from 'react';
import Helmet from 'react-helmet';

// Material UI Components
import { makeStyles } from '@material-ui/core/styles';
import LinearProgress from '@material-ui/core/LinearProgress';

// Project Components
import Footer from 'components/navigation/Footer';
import Topbar from 'components/navigation/Topbar';
import Container from 'components/layout/Container';

const useStyles = makeStyles((theme) => ({
  main: {
    minHeight: '101vh',
    paddingTop: 64,
    [theme.breakpoints.down('sm')]: {
      paddingTop: 56,
    },
  },
}));

export type NavigationProps = {
  children?: ReactNode;
  banner?: ReactElement;
  maxWidth?: false | 'xs' | 'sm' | 'md' | 'lg' | 'xl';
  isLoading?: boolean;
  noFooter?: boolean;
};

const Navigation = ({ isLoading = false, noFooter = false, maxWidth, banner, children }: NavigationProps) => {
  const classes = useStyles();

  return (
    <>
      <Helmet>
        <title>Rombestilling - Bestill et rom nå!</title>
      </Helmet>
      <Topbar />
      <main className={classes.main}>
        {isLoading && <LinearProgress />}
        {banner}
        {maxWidth === false ? <>{children}</> : <Container maxWidth={maxWidth || 'xl'}>{children || <></>}</Container>}
      </main>
      {!noFooter && !isLoading && <Footer />}
    </>
  );
};

export default Navigation;
