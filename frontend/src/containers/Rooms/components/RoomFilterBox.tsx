import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { formatDate } from 'utils';
import { parseISO } from 'date-fns';

// Material UI Components
import { makeStyles, Divider, useMediaQuery, Theme, Collapse, Button, Typography } from '@material-ui/core';

// Project Components
import DatePicker from 'components/inputs/DatePicker';
import TextField from 'components/inputs/TextField';
import Paper from 'components/layout/Paper';
import SubmitButton from 'components/inputs/SubmitButton';
import { RoomFilters } from 'containers/Rooms';

const useStyles = makeStyles((theme) => ({
  paper: {
    padding: theme.spacing(2),
    margin: theme.spacing(2, 0),
  },
  filter: {
    display: 'grid',
    gap: theme.spacing(0.5),
    gridTemplateColumns: '2fr auto 3fr auto 3fr auto 2fr',
    [theme.breakpoints.down('md')]: {
      gridTemplateColumns: '1fr',
    },
  },
  submit: {
    minHeight: '100%',
  },
}));

export type RoomFilterBoxProps = {
  updateFilters: (newFilters: RoomFilters) => void;
  defaultFilters: RoomFilters;
  filters: RoomFilters;
};
type FormValues = Pick<RoomFilters, 'name'> & {
  from: Date;
  to: Date;
};

const RoomFilterBox = ({ defaultFilters, filters, updateFilters }: RoomFilterBoxProps) => {
  const classes = useStyles();
  const [isOpen, setIsOpen] = useState(true);
  const mdDown = useMediaQuery((theme: Theme) => theme.breakpoints.down('md'));
  const { control, formState, handleSubmit, register, reset } = useForm<FormValues>({
    defaultValues: { from: parseISO(filters.from), to: parseISO(filters.to) },
  });
  const submit = async (data: FormValues) => {
    updateFilters({ ...data, to: data.to.toJSON(), from: data.from.toJSON() });
    setIsOpen(false);
  };
  const resetFilters = async () => {
    setIsOpen(true);
    reset();
    updateFilters(defaultFilters);
  };
  return (
    <Paper blurred border className={classes.paper}>
      <Collapse in={!isOpen}>
        {filters && (
          <>
            {Boolean(filters.name?.length) && <Typography>{`Søkeord: "${filters.name}"`}</Typography>}
            <Typography>{`Fra ${formatDate(parseISO(filters.from))}`}</Typography>
            <Typography>{`Til ${formatDate(parseISO(filters.to))}`}</Typography>
          </>
        )}
        <Button fullWidth onClick={() => setIsOpen(true)} variant='text'>
          Endre søk
        </Button>
        <Button color='secondary' fullWidth onClick={resetFilters} variant='text'>
          Nullstill
        </Button>
      </Collapse>
      <Collapse in={isOpen}>
        <form className={classes.filter} onSubmit={handleSubmit(submit)}>
          <TextField formState={formState} label='Navn' margin='dense' noDefaultShrink noOutline {...register('name')} />
          <Divider orientation={mdDown ? 'horizontal' : 'vertical'} />
          <DatePicker
            control={control}
            formState={formState}
            label='Fra'
            margin='dense'
            name='from'
            noOutline
            required
            rules={{ required: 'Du må oppgi en fra-dato' }}
            type='date-time'
          />
          <Divider orientation={mdDown ? 'horizontal' : 'vertical'} />
          <DatePicker
            control={control}
            formState={formState}
            label='Til'
            margin='dense'
            name='to'
            noOutline
            required
            rules={{ required: 'Du må oppgi en til-dato' }}
            type='date-time'
          />
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

export default RoomFilterBox;
