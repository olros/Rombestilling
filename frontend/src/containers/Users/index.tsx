import { useMemo, useState } from 'react';
import Helmet from 'react-helmet';
import { useUsers } from 'hooks/User';
import classnames from 'classnames';

// Material UI Components
import { makeStyles, Typography } from '@material-ui/core';

// Project Components
import Container from 'components/layout/Container';
import UsersFilterBox from 'containers/Users/components/UsersFilterBox';
import UserListItem from 'containers/Users/components/UserListItem';
import Pagination from 'components/layout/Pagination';
import CreateUser from 'components/miscellaneous/CreateUser';
import NotFoundIndicator from 'components/miscellaneous/NotFoundIndicator';

const useStyles = makeStyles((theme) => ({
  list: {
    display: 'grid',
    gap: theme.spacing(1),
  },
  top: {
    gridTemplateColumns: '1fr auto',
  },
  reservationPaper: {
    maxWidth: theme.breakpoints.values.md,
    margin: 'auto',
    padding: theme.spacing(3, 2, 5),
    borderRadius: `${theme.shape.borderRadius}px ${theme.shape.borderRadius}px 0 0`,
    background: theme.palette.background.paper,
  },
}));

export type UsersFilters = {
  search?: string;
};

const Users = () => {
  const classes = useStyles();
  const [filters, setFilters] = useState<UsersFilters>({});
  const { data, error, hasNextPage, fetchNextPage, isFetching } = useUsers(filters);
  const results = useMemo(() => (data !== undefined ? data.pages.map((page) => page.content).flat(1) : []), [data]);
  const isEmpty = useMemo(() => !results.length && !isFetching, [results, isFetching]);
  return (
    <Container>
      <Helmet>
        <title>Brukere - Rombestilling</title>
      </Helmet>
      <div className={classes.list}>
        <div className={classnames(classes.list, classes.top)}>
          <Typography variant='h1'>Brukere</Typography>
          <CreateUser>Opprett bruker</CreateUser>
        </div>
        <UsersFilterBox filters={filters} updateFilters={setFilters} />
        <Pagination fullWidth hasNextPage={hasNextPage} isLoading={isFetching} nextPage={() => fetchNextPage()}>
          <div className={classes.list}>
            {isEmpty && <NotFoundIndicator header={error?.message || 'Fant ingen brukere'} />}
            {results.map((user) => (
              <UserListItem key={user.id} user={user} />
            ))}
          </div>
        </Pagination>
      </div>
    </Container>
  );
};

export default Users;
