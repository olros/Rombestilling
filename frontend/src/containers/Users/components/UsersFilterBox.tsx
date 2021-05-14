import { useState } from 'react';
import { useForm } from 'react-hook-form';

// Material UI Components
import { makeStyles, Divider, useMediaQuery, Theme, Collapse, Button, Typography } from '@material-ui/core';

// Project Components
import TextField from 'components/inputs/TextField';
import Paper from 'components/layout/Paper';
import SubmitButton from 'components/inputs/SubmitButton';
import { UsersFilters } from 'containers/Users';

const useStyles = makeStyles((theme) => ({
  paper: {
    padding: theme.spacing(2),
    margin: theme.spacing(2, 0),
  },
  filter: {
    display: 'grid',
    gap: theme.spacing(0.5),
    gridTemplateColumns: '1fr auto 130px',
    [theme.breakpoints.down('md')]: {
      gridTemplateColumns: '1fr',
    },
  },
  submit: {
    minHeight: '100%',
  },
}));

export type UsersFilterBoxProps = {
  updateFilters: (newFilters: UsersFilters) => void;
  filters: UsersFilters;
};

const UsersFilterBox = ({ filters, updateFilters }: UsersFilterBoxProps) => {
  const classes = useStyles();
  const [isOpen, setIsOpen] = useState(true);
  const mdDown = useMediaQuery((theme: Theme) => theme.breakpoints.down('md'));
  const { formState, handleSubmit, register, reset } = useForm<UsersFilters>();
  const submit = async (data: UsersFilters) => {
    updateFilters({ ...data });
    setIsOpen(false);
  };
  const resetFilters = async () => {
    setIsOpen(true);
    reset();
    updateFilters({});
  };
  return (
    <Paper blurred border className={classes.paper}>
      <Collapse in={!isOpen}>
        {filters && <>{Boolean(filters.search?.length) && <Typography>{`Søkeord: "${filters.search}"`}</Typography>}</>}
        <Button fullWidth onClick={() => setIsOpen(true)} variant='text'>
          Endre søk
        </Button>
        <Button color='secondary' fullWidth onClick={resetFilters} variant='text'>
          Nullstill
        </Button>
      </Collapse>
      <Collapse in={isOpen}>
        <form className={classes.filter} onSubmit={handleSubmit(submit)}>
          <TextField formState={formState} label='Søk etter navn, epost eller telefon' margin='dense' noDefaultShrink noOutline {...register('search')} />
          <Divider orientation={mdDown ? 'horizontal' : 'vertical'} />
          <div>
            <SubmitButton className={classes.submit} formState={formState} noFeedback variant='text'>
              Søk
            </SubmitButton>
          </div>
        </form>
      </Collapse>
    </Paper>
  );
};

export default UsersFilterBox;
