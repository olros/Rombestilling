import { useState } from 'react';
import Helmet from 'react-helmet';
import URLS from 'URLS';
import { Link, useParams } from 'react-router-dom';
import { useSectionById } from 'hooks/Section';
import classnames from 'classnames';

// Material UI Components
import { makeStyles, Typography, Collapse } from '@material-ui/core';

// Icons
import CalendarIcon from '@material-ui/icons/EventRounded';
import SectionsIcon from '@material-ui/icons/ViewModuleRounded';

// Project Components
import Http404 from 'containers/Http404';
import Navigation from 'components/navigation/Navigation';
import Paper from 'components/layout/Paper';
import Tabs from 'components/layout/Tabs';
import RoomSection from 'containers/RoomDetails/components/RoomSection';
import CreateRoom from 'components/miscellaneous/CreateRoom';
import EditRoom from 'components/miscellaneous/EditRoom';
import { SectionCalendar } from 'components/miscellaneous/Calendar';

const useStyles = makeStyles((theme) => ({
  grid: {
    display: 'grid',
    gap: theme.spacing(1),
    alignItems: 'self-start',
  },
  top: {
    gridTemplateColumns: '1fr auto',
  },
  content: {
    gap: theme.spacing(2),
    gridTemplateColumns: '1fr 350px',
    [theme.breakpoints.down('xl')]: {
      gridTemplateColumns: '1fr 300px',
    },
    [theme.breakpoints.down('lg')]: {
      gridTemplateColumns: '1fr',
    },
  },
  description: {
    whiteSpace: 'break-spaces',
  },
  image: {
    borderRadius: theme.shape.borderRadius,
    width: '100%',
  },
}));

export type RoomFilters = {
  name: string;
  from: string;
  to: string;
};

const RoomDetails = () => {
  const classes = useStyles();
  const { id } = useParams();
  const { data, isLoading, isError } = useSectionById(id);
  const calendarTab = { value: 'calendar', label: 'Kalender', icon: CalendarIcon };
  const sectionsTab = { value: 'sections', label: 'Deler', icon: SectionsIcon };
  const tabs = [calendarTab, ...(data?.type === 'room' ? [sectionsTab] : [])];
  const [tab, setTab] = useState(calendarTab.value);

  if (isError) {
    return <Http404 />;
  }
  return (
    <Navigation>
      <Helmet>
        <title>{`${data?.name || 'Laster rom...'} - Rombestilling`}</title>
      </Helmet>
      {data && !isLoading ? (
        <div>
          <div className={classes.grid}>
            <div className={classnames(classes.grid, classes.top)}>
              <div className={classes.grid}>
                <Typography variant='h1'>{data.name}</Typography>
                {data.type === 'section' && (
                  <Typography variant='subtitle2'>
                    {`Del av rommet: `}
                    <Link to={`${URLS.ROOMS}${data.parent.id}/`}>{data.parent.name}</Link>
                  </Typography>
                )}
              </div>
              <EditRoom room={data} sectionType='room'>
                Endre rom
              </EditRoom>
            </div>
            <div className={classnames(classes.grid, classes.content)}>
              <div className={classes.grid}>
                <Tabs selected={tab} setSelected={setTab} tabs={tabs} />
                <div>
                  <Collapse in={tab === calendarTab.value} mountOnEnter>
                    <SectionCalendar sectionId={id} />
                  </Collapse>
                  <Collapse in={tab === sectionsTab.value} mountOnEnter>
                    <Paper className={classes.grid}>
                      {data.children.map((section) => (
                        <RoomSection key={section.id} section={section} />
                      ))}
                      <CreateRoom parentId={id}>Opprett ny del av rom</CreateRoom>
                    </Paper>
                  </Collapse>
                </div>
              </div>
              <div className={classes.grid}>
                <Typography className={classes.description}>{data.description}</Typography>
                {data.image && <img alt={data.name} className={classes.image} src={data.image} />}
              </div>
            </div>
          </div>
        </div>
      ) : (
        <></>
      )}
    </Navigation>
  );
};

export default RoomDetails;
