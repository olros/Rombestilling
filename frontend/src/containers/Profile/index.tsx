import { useState } from 'react';
import Helmet from 'react-helmet';
import classnames from 'classnames';
import { useUser, useLogout } from 'hooks/User';
import { useParams } from 'react-router-dom';

// Material UI Components
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import Avatar from '@material-ui/core/Avatar';
import Collapse from '@material-ui/core/Collapse';

// Icons
import EditIcon from '@material-ui/icons/EditRounded';
import AktivitiesIcon from '@material-ui/icons/DateRangeRounded';
import FollowIcon from '@material-ui/icons/ClearAllRounded';
import PostsIcon from '@material-ui/icons/ViewAgendaRounded';

// Project Components
import Navigation from 'components/navigation/Navigation';
import Container from 'components/layout/Container';
import Paper from 'components/layout/Paper';
import Tabs from 'components/layout/Tabs';
import Http404 from 'containers/Http404';
import EditProfile from 'containers/Profile/components/EditProfile';

const useStyles = makeStyles((theme) => ({
  backgroundImg: {
    background: `${theme.palette.colors.gradient}`,
    width: '100%',
    height: 300,
    backgroundSize: 'cover',
  },
  avatarContainer: {
    position: 'relative',
    marginTop: -120,
    zIndex: 2,
    alignItems: 'center',
    [theme.breakpoints.down('lg')]: {
      marginTop: -80,
    },
  },
  avatar: {
    height: 120,
    width: 120,
    margin: 'auto',
    fontSize: '3rem',
    [theme.breakpoints.down('md')]: {
      height: 100,
      width: 100,
      fontSize: '2rem',
    },
  },
  grid: {
    display: 'grid',
    gap: theme.spacing(1),
    alignItems: 'self-start',
  },
  root: {
    gridTemplateColumns: '300px 1fr',
    gap: theme.spacing(2),
    [theme.breakpoints.down('lg')]: {
      gridTemplateColumns: '1fr',
    },
  },
  logout: {
    color: theme.palette.error.main,
    borderColor: theme.palette.error.main,
    '&:hover': {
      color: theme.palette.error.light,
      borderColor: theme.palette.error.light,
    },
  },
}));

const Profile = () => {
  const classes = useStyles();
  const { userId }: { userId?: string } = useParams();
  const { data: user, isLoading, isError } = useUser(userId);
  const logout = useLogout();
  const posts = { value: 'posts', label: 'Innlegg', icon: PostsIcon };
  const activitiesTab = { value: 'activities', label: 'Aktiviteter', icon: AktivitiesIcon };
  const followTab = { value: 'follow', label: 'Følger', icon: FollowIcon };
  const editTab = { value: 'edit', label: 'Rediger profil', icon: EditIcon };
  const tabs = [posts, activitiesTab, followTab, ...(userId ? [] : [editTab])];
  const [tab, setTab] = useState(posts.value);

  if (isError) {
    return <Http404 />;
  }
  if (isLoading || !user) {
    return <Navigation isLoading />;
  }

  return (
    <Navigation maxWidth={false}>
      <Helmet>
        <title>{`${user.firstName} ${user.surname} - Rombestilling`}</title>
      </Helmet>
      <div className={classes.backgroundImg} />
      <Container className={classnames(classes.grid, classes.root)}>
        <div className={classes.grid}>
          <Paper blurred className={classnames(classes.grid, classes.avatarContainer)}>
            <Avatar className={classes.avatar} src={user.image}>{`${user.firstName.substr(0, 1)}${user.surname.substr(0, 1)}`}</Avatar>
            <div>
              <Typography align='center' variant='h2'>{`${user.firstName} ${user.surname}`}</Typography>
              <Typography align='center' variant='subtitle2'>
                {user.email}
              </Typography>
            </div>
          </Paper>
          <Button className={classes.logout} fullWidth onClick={logout} variant='outlined'>
            Logg ut
          </Button>
        </div>
        <div className={classes.grid}>
          <Tabs selected={tab} setSelected={setTab} tabs={tabs} />
          <div>
            <Collapse in={tab === editTab.value} mountOnEnter>
              <Paper>
                <EditProfile user={user} />
              </Paper>
            </Collapse>
          </div>
        </div>
      </Container>
    </Navigation>
  );
};

export default Profile;
