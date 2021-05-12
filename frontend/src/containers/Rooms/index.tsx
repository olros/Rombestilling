import { useState } from 'react';
import Helmet from 'react-helmet';

// Material UI Components
import { makeStyles, Typography } from '@material-ui/core';

// Project Components
import Navigation from 'components/navigation/Navigation';
import RoomFilterBox from 'containers/Rooms/components/RoomFilterBox';
import RoomListItem from 'containers/Rooms/components/RoomListItem';
import { RoomList, SectionList } from 'types/Types';

const useStyles = makeStyles((theme) => ({
  list: {
    display: 'grid',
    gap: theme.spacing(1),
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
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const [filters, setFilters] = useState<RoomFilters | undefined>(undefined);
  return (
    <Navigation>
      <Helmet>
        <title>Finn rom - Rombestilling</title>
      </Helmet>
      <div className={classes.list}>
        <Typography variant='h1'>Finn rom</Typography>
        <RoomFilterBox updateFilters={(newFilters: RoomFilters) => setFilters(newFilters)} />
        {rooms.map((room) => (
          <RoomListItem key={room.id} room={room} />
        ))}
      </div>
    </Navigation>
  );
};

export default Rooms;
