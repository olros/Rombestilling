import { useState } from 'react';
import Helmet from 'react-helmet';
import { Link } from 'react-router-dom';
import URLS from 'URLS';

// Material UI Components
import { makeStyles, Typography } from '@material-ui/core';

// Project Components
import Navigation from 'components/navigation/Navigation';
import RoomFilterBox from 'containers/Rooms/components/RoomFilterBox';

const useStyles = makeStyles((theme) => ({}));

export type RoomFilters = {
  name: string;
  from: string;
  to: string;
};

const Rooms = () => {
  const classes = useStyles();
  const [filters, setFilters] = useState<RoomFilters | undefined>(undefined);
  return (
    <Navigation>
      <Helmet>
        <title>Finn rom - Rombestilling</title>
      </Helmet>
      <div>
        <Typography variant='h1'>Finn rom</Typography>
        <RoomFilterBox updateFilters={(newFilters: RoomFilters) => setFilters(newFilters)} />
      </div>
    </Navigation>
  );
};

export default Rooms;
