import { useEffect, useState } from 'react';
import Helmet from 'react-helmet';
import { useParams, useNavigate } from 'react-router-dom';
import classnames from 'classnames';
import URLS from 'URLS';
import { useUser, useLogout } from 'hooks/User';
import { urlEncode } from 'utils';

// Material UI Components
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import Avatar from '@material-ui/core/Avatar';
import Collapse from '@material-ui/core/Collapse';

// Icons
import EditIcon from '@material-ui/icons/EditRounded';
import PostsIcon from '@material-ui/icons/ViewAgendaRounded';
import ListIcon from '@material-ui/icons/ViewStreamRounded';

// Project Components
import Navigation from 'components/navigation/Navigation';
import Paper from 'components/layout/Paper';
import Tabs from 'components/layout/Tabs';
import Http404 from 'containers/Http404';
import EditProfile from 'containers/Profile/components/EditProfile';
import { UserCalendar } from 'components/miscellaneous/Calendar';
import { UserReservations } from 'containers/RoomDetails/components/RoomReservations';

const useStyles = makeStyles((theme) => ({
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
    marginTop: theme.spacing(2),
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
  const { data: signedInUser } = useUser();
  const { data: user, isLoading, isError } = useUser(userId);
  const logout = useLogout();
  const reservationsTab = { value: 'reservations', label: 'Reservasjoner', icon: ListIcon };
  const bookings = { value: 'bookings', label: 'Kalender', icon: PostsIcon };
  const editTab = { value: 'edit', label: 'Rediger profil', icon: EditIcon };
  const tabs = [reservationsTab, bookings, ...(userId ? [] : [editTab])];
  const [tab, setTab] = useState(reservationsTab.value);
  const navigate = useNavigate();

  useEffect(() => {
    if (user && signedInUser && user.id === signedInUser.id) {
      navigate(`${URLS.PROFILE}`, { replace: true });
    } else if (userId && user) {
      navigate(`${URLS.USERS}${user.id}/${urlEncode(`${user.firstName} ${user.surname}`)}/`, { replace: true });
    }
  }, [user, signedInUser, navigate]);

  if (isError) {
    return <Http404 />;
  }
  if (isLoading || !user) {
    return <Navigation isLoading />;
  }

  return (
    <Navigation>
      <Helmet>
        <title>{`${user.firstName} ${user.surname} - Rombestilling`}</title>
      </Helmet>
      <div className={classnames(classes.grid, classes.root)}>
        <div className={classes.grid}>
          <Paper blurred className={classes.grid}>
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
            <Collapse in={tab === reservationsTab.value} mountOnEnter>
              <UserReservations userId={userId} />
            </Collapse>
            <Collapse in={tab === bookings.value} mountOnEnter>
              <UserCalendar userId={userId} />
            </Collapse>
            <Collapse in={tab === editTab.value} mountOnEnter>
              <Paper>
                <EditProfile user={user} />
              </Paper>
            </Collapse>
          </div>
        </div>
      </div>
    </Navigation>
  );
};

export default Profile;
