import { useState } from 'react';
import Helmet from 'react-helmet';

// Material UI Components
import { makeStyles, Typography, SwipeableDrawer, Button } from '@material-ui/core';

// Project Components
import Navigation from 'components/navigation/Navigation';
import ReserveForm from 'components/miscellaneous/ReserveForm';
import RoomFilterBox from 'containers/Rooms/components/RoomFilterBox';
import RoomListItem from 'containers/Rooms/components/RoomListItem';
import { RoomList, SectionList } from 'types/Types';

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

const rooms: Array<RoomList | SectionList> = [
  {
    id: '123',
    name: 'Labben',
    capacity: 45,
    type: 'section',
    parent: {
      id: '456',
      name: 'A-blokka',
      capacity: 92,
      type: 'room',
    },
  },
  {
    id: '456',
    name: 'A-blokka',
    capacity: 92,
    type: 'room',
    children: [
      {
        id: '123',
        name: 'Labben',
        capacity: 45,
        type: 'section',
      },
      {
        id: '987',
        name: 'Kontoret',
        capacity: 13,
        type: 'section',
      },
    ],
  },
];

const Rooms = () => {
  const classes = useStyles();
  const [filters, setFilters] = useState<RoomFilters | undefined>(undefined);
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
        <RoomFilterBox filters={filters} updateFilters={(newFilters: RoomFilters) => setFilters(newFilters)} />
        {filters && rooms.map((room) => <RoomListItem key={room.id} reserve={startReservation} room={room} />)}
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
