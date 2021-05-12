import Helmet from 'react-helmet';
import { Link } from 'react-router-dom';
import URLS from 'URLS';
import { useIsAuthenticated } from 'hooks/User';

// Material UI Components
import { makeStyles, Typography, Button } from '@material-ui/core';

// Project Components
import Navigation from 'components/navigation/Navigation';
import Logo from 'components/miscellaneous/Logo';
import Container from 'components/layout/Container';

const useStyles = makeStyles((theme) => ({
  cover: {
    position: 'relative',
    height: '100vh',
    width: '100%',
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
  },
  img: {
    position: 'absolute',
    top: 0,
    bottom: 0,
    left: 0,
    right: 0,
    zIndex: -1,
  },
  activityContainer: {
    textAlign: 'center',
    paddingTop: theme.spacing(2),
  },
  btnGroup: {
    display: 'grid',
    gap: theme.spacing(1),
    paddingTop: theme.spacing(1),
    gridTemplateColumns: 'auto auto',
  },
  button: {
    color: theme.palette.common.white,
    borderColor: theme.palette.common.white,
  },
  logoWrapper: {
    display: 'flex',
    margin: 'auto',
    marginTop: theme.spacing(2),
    maxWidth: 200,
    maxHeight: 200,
    marginBottom: theme.spacing(2),
  },
  logo: {
    minWidth: '250px',
    width: '46%',
    maxWidth: '100%',
    height: 'auto',
    margin: theme.spacing(5, 'auto'),
    [theme.breakpoints.down('md')]: {
      minWidth: '200px',
    },
  },
}));

const Landing = () => {
  const classes = useStyles();
  const isAuthenticated = useIsAuthenticated();

  return (
    <Navigation maxWidth={false}>
      <Helmet>
        <title>Forsiden - Rombestilling</title>
      </Helmet>
      <div className={classes.cover}>
        <div className={classes.img} />
        <div className={classes.logoWrapper}>
          <Logo />
        </div>
        <Typography align='center' color='inherit' variant='h1'>
          Rombestilling
        </Typography>
        <Typography align='center' color='inherit' variant='h3'>
          Finn ditt rom nå!
        </Typography>
        {!isAuthenticated && (
          <div className={classes.btnGroup}>
            <Button className={classes.button} component={Link} to={URLS.LOGIN} variant='outlined'>
              Logg inn
            </Button>
            <Button className={classes.button} component={Link} to={URLS.SIGNUP} variant='outlined'>
              Registrer deg
            </Button>
          </div>
        )}
      </div>
      <Container className={classes.activityContainer}>
        <Typography gutterBottom variant='h1'>
          Bestill rom
        </Typography>
      </Container>
    </Navigation>
  );
};

export default Landing;
