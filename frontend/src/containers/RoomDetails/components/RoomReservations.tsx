import { useMemo, useState } from 'react';
import { UseInfiniteQueryResult } from 'react-query';
import { Reservation, PaginationResponse, RequestResponse } from 'types/Types';
import { useSectionReservations, useUserReservations } from 'hooks/Reservation';
import { parseISO, startOfDay } from 'date-fns';
import { formatDate } from 'utils';

// Material UI Components
import { makeStyles, Collapse, Divider, List, ListItem, ListItemText } from '@material-ui/core';

// Icons
import ExpandMoreIcon from '@material-ui/icons/ExpandMoreRounded';
import ExpandLessIcon from '@material-ui/icons/ExpandLessRounded';

// Project Components
import Paper from 'components/layout/Paper';
import ReservationInfo from 'components/miscellaneous/ReservationInfo';
import Pagination from 'components/layout/Pagination';
import NotFoundIndicator from 'components/miscellaneous/NotFoundIndicator';

const useStyles = makeStyles((theme) => ({
  grid: {
    display: 'grid',
    gap: theme.spacing(1),
  },
  paper: {
    overflow: 'hidden',
  },
  wrapper: {
    alignItems: 'center',
  },
  listContent: {
    padding: theme.spacing(1),
  },
}));

export type UserReservationsProps = {
  userId?: string;
};

export const UserReservations = ({ userId }: UserReservationsProps) => {
  const filters = useMemo(() => ({ fromTime: startOfDay(new Date()).toJSON() }), []);
  const result = useUserReservations(userId, filters);
  return <Reservations result={result} />;
};

export type SectionReservationsProps = {
  sectionId: string;
};

export const SectionReservations = ({ sectionId }: SectionReservationsProps) => {
  const filters = useMemo(() => ({ fromTime: startOfDay(new Date()).toJSON() }), []);
  const result = useSectionReservations(sectionId, filters);
  return <Reservations result={result} />;
};

export type ReservationsProps = {
  result: UseInfiniteQueryResult<PaginationResponse<Reservation>, RequestResponse>;
};

const Reservations = ({ result: { data, error, hasNextPage, fetchNextPage, isFetching } }: ReservationsProps) => {
  const classes = useStyles();
  // const { data, error, hasNextPage, fetchNextPage, isFetching } = useSectionReservations(sectionId, {});
  const reservations = useMemo(() => (data !== undefined ? data.pages.map((page) => page.content).flat(1) : []), [data]);
  const isEmpty = useMemo(() => !reservations.length && !isFetching, [reservations, isFetching]);

  const Reservation = ({ reservation }: { reservation: Reservation }) => {
    const [expanded, setExpanded] = useState(false);
    return (
      <Paper className={classes.paper} noPadding>
        <ListItem button className={classes.wrapper} onClick={() => setExpanded((prev) => !prev)}>
          <ListItemText primary={`${formatDate(parseISO(reservation.fromTime))} - ${formatDate(parseISO(reservation.toTime))}`} />
          {expanded ? <ExpandLessIcon /> : <ExpandMoreIcon />}
        </ListItem>
        <Collapse in={expanded} mountOnEnter unmountOnExit>
          <Divider />
          <div className={classes.listContent}>
            <ReservationInfo reservationId={reservation.id} sectionId={reservation.section.id} />
          </div>
        </Collapse>
      </Paper>
    );
  };

  return (
    <div className={classes.grid}>
      <Pagination fullWidth hasNextPage={hasNextPage} isLoading={isFetching} nextPage={() => fetchNextPage()}>
        <List className={classes.grid} disablePadding>
          {isEmpty && <NotFoundIndicator header={error?.message || 'Fant ingen reservasjoner'} />}
          {reservations.map((reservation) => (
            <Reservation key={reservation.id} reservation={reservation} />
          ))}
        </List>
      </Pagination>
    </div>
  );
};

export default Reservations;
