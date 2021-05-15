// Material UI Components
import { makeStyles, Divider } from '@material-ui/core';

// Project components
import Logo from 'components/miscellaneous/Logo';
import Container from 'components/layout/Container';

const useStyles = makeStyles((theme) => ({
  root: {
    paddingTop: theme.spacing(2),
  },
  content: {
    width: '100%',
    padding: theme.spacing(5),
    margin: 'auto',
    display: 'flex',
    justifyContent: 'center',
  },
  logo: {
    maxWidth: '200px',
    width: '90%',
    height: 'auto',
  },
}));

const Footer = () => {
  const classes = useStyles();

  return (
    <footer className={classes.root}>
      <Container maxWidth='lg'>
        <Divider variant='middle' />
        <div className={classes.content}>
          <Logo className={classes.logo} />
        </div>
      </Container>
    </footer>
  );
};

export default Footer;
