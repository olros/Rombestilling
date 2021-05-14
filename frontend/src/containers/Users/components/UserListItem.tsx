import classnames from 'classnames';
import { Link } from 'react-router-dom';
import URLS from 'URLS';
import { UserList } from 'types/Types';

// Material UI Components
import { makeStyles, Button, Typography } from '@material-ui/core';

// Icons
import ArrowIcon from '@material-ui/icons/ArrowForwardRounded';

// Project Components
import Paper from 'components/layout/Paper';

const useStyles = makeStyles((theme) => ({
  grid: {
    display: 'grid',
    gap: theme.spacing(1),
  },
  paper: {
    padding: theme.spacing(2, 3),
  },
  top: {
    gridTemplateColumns: '1fr auto',
  },
  topText: {
    gap: 0,
  },
}));

export type UserListItemProps = {
  user: UserList;
};

const UserListItem = ({ user }: UserListItemProps) => {
  const classes = useStyles();
  return (
    <Paper className={classnames(classes.paper, classes.grid)}>
      <div className={classnames(classes.top, classes.grid)}>
        <div className={classnames(classes.topText, classes.grid)}>
          <Typography variant='h3'>{`${user.firstName} ${user.surname}`}</Typography>
          <Typography variant='caption'>{`${user.email} | ${user.phoneNumber}`}</Typography>
        </div>
        <Button component={Link} endIcon={<ArrowIcon />} to={`${URLS.USERS}${user.id}/`} variant='outlined'>
          Profil
        </Button>
      </div>
    </Paper>
  );
};

export default UserListItem;
