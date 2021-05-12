import { useMemo, useState } from 'react';
import Helmet from 'react-helmet';
import { useSections } from 'hooks/Section';

// Material UI Components
import { makeStyles, Typography, SwipeableDrawer, Button } from '@material-ui/core';

// Project Components
import Navigation from 'components/navigation/Navigation';
import ReserveForm from 'components/miscellaneous/ReserveForm';
import RoomFilterBox from 'containers/Rooms/components/RoomFilterBox';
import RoomListItem from 'containers/Rooms/components/RoomListItem';
import Pagination from 'components/layout/Pagination';
import NotFoundIndicator from 'components/miscellaneous/NotFoundIndicator';

const useStyles = makeStyles((theme) => ({
  list: {
    display: 'grid',
    gap: theme.spacing(1),
  },
  reservationPaper: {
    maxWidth: theme.breakpoints.values.md,
    margin: 'auto',
    padding: theme.spacing(3, 2, 5),
    borderRadius: `${theme.shape.borderRadius}px ${theme.shape.borderRadius}px 0 0`,
    background: theme.palette.background.paper,
  },
}));

export type RoomFilters = {
  name: string;
  from: string;
  to: string;
};

const Rooms = () => {
  const classes = useStyles();
  const [filters, setFilters] = useState<RoomFilters | undefined>(undefined);
  const { data, error, hasNextPage, fetchNextPage, isFetching } = useSections(filters);
  const results = useMemo(() => (data !== undefined ? data.pages.map((page) => page.content).flat(1) : []), [data]);
  const isEmpty = useMemo(() => !results.length && !isFetching, [results, isFetching]);
  const [reservationOpen, setReservationOpen] = useState(false);
  const [selectedSectionId, setSelectedSectionId] = useState<string | null>(null);
  const startReservation = async (sectionId: string) => {
    setSelectedSectionId(sectionId);
    setReservationOpen(true);
  };
  const stopReservation = () => {
    setReservationOpen(false);
    setSelectedSectionId(null);
  };
  return (
    <Navigation>
      <Helmet>
        <title>Finn rom - Rombestilling</title>
      </Helmet>
      <div className={classes.list}>
        <Typography variant='h1'>Finn rom</Typography>
        <RoomFilterBox filters={filters} updateFilters={setFilters} />
        <Pagination fullWidth hasNextPage={hasNextPage} isLoading={isFetching} nextPage={() => fetchNextPage()}>
          <div className={classes.list}>
            {isEmpty && <NotFoundIndicator header={error?.message || 'Fant ingen ledige rom'} />}
            {filters && results.map((room) => <RoomListItem key={room.id} reserve={startReservation} room={room} />)}
          </div>
        </Pagination>
      </div>
      <SwipeableDrawer
        anchor='bottom'
        classes={{ paper: classes.reservationPaper }}
        disableSwipeToOpen
        onClose={stopReservation}
        onOpen={() => setReservationOpen(true)}
        open={reservationOpen}
        swipeAreaWidth={56}>
        <div className={classes.list}>
          {selectedSectionId && filters && <ReserveForm from={filters.from} sectionId={selectedSectionId} to={filters.to} />}
          <Button onClick={stopReservation} variant='text'>
            Avbryt
          </Button>
        </div>
      </SwipeableDrawer>
    </Navigation>
  );
};

export default Rooms;
