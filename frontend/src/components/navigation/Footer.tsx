// Material UI Components
import { makeStyles } from '@material-ui/core/styles';
import Divider from '@material-ui/core/Divider';

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
    display: 'grid',
    gap: theme.spacing(2),
    gridTemplateColumns: '1fr',
    color: theme.palette.text.primary,
    maxWidth: theme.breakpoints.values.md,
    margin: 'auto',
    [theme.breakpoints.down('lg')]: {
      padding: theme.spacing(4),
      gap: theme.spacing(1),
    },
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
