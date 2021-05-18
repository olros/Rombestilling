import { useEffect, useState } from 'react';
import Helmet from 'react-helmet';
import { useParams } from 'react-router-dom';
import { useGroup } from 'hooks/Group';
import { useUser } from 'hooks/User';
import classnames from 'classnames';
import { isUserAdmin } from 'utils';

// Material UI Components
import { makeStyles, Typography, Collapse } from '@material-ui/core';

// Icons
import CalendarIcon from '@material-ui/icons/EventRounded';
import ListIcon from '@material-ui/icons/ViewStreamRounded';
import UsersIcon from '@material-ui/icons/PeopleOutlineRounded';

// Project Components
import Http404 from 'containers/Http404';
import Container from 'components/layout/Container';
import Tabs from 'components/layout/Tabs';
// import { GroupReservations } from 'containers/RoomDetails/components/RoomReservations';
import EditGroup from 'components/miscellaneous/EditGroup';
// import { GroupCalendar } from 'components/miscellaneous/Calendar';

const useStyles = makeStyles((theme) => ({
  grid: {
    display: 'grid',
    gap: theme.spacing(1),
    alignItems: 'self-start',
  },
  top: {
    gridTemplateColumns: '1fr auto',
  },
}));

const GroupDetails = () => {
  const classes = useStyles();
  const { data: user } = useUser();
  const { id } = useParams();
  const { data, isLoading, isError } = useGroup(id);
  const isAdmin = isUserAdmin(user);
  const reservationsTab = { value: 'reservations', label: 'Reservasjoner', icon: ListIcon };
  const calendarTab = { value: 'calendar', label: 'Kalender', icon: CalendarIcon };
  const membersTab = { value: 'members', label: 'Medlemmer', icon: UsersIcon };
  const tabs = [reservationsTab, calendarTab, ...(isAdmin ? [membersTab] : [])];
  const [tab, setTab] = useState(reservationsTab.value);

  useEffect(() => {
    setTab(reservationsTab.value);
  }, [id]);

  if (isError) {
    return <Http404 />;
  }
  return (
    <Container>
      <Helmet>
        <title>{`${data?.name || 'Laster gruppe...'} - Rombestilling`}</title>
      </Helmet>
      {data && !isLoading && (
        <div className={classes.grid}>
          <div className={classnames(classes.grid, classes.top)}>
            <Typography variant='h1'>{data.name}</Typography>
            {isAdmin && <EditGroup group={data}>Endre gruppe</EditGroup>}
          </div>
          <div className={classes.grid}>
            <Tabs selected={tab} setSelected={setTab} tabs={tabs} />
            <div>
              <Collapse in={tab === reservationsTab.value} mountOnEnter>
                <Typography>Reservasjoner her</Typography>
                {/* <GroupReservations sectionId={id} /> */}
              </Collapse>
              <Collapse in={tab === calendarTab.value} mountOnEnter>
                <Typography>Kalender her</Typography>
                {/* <GroupCalendar sectionId={id} /> */}
              </Collapse>
              <Collapse in={tab === membersTab.value} mountOnEnter>
                <Typography>Medlemmer her</Typography>
              </Collapse>
            </div>
          </div>
        </div>
      )}
    </Container>
  );
};

export default GroupDetails;
