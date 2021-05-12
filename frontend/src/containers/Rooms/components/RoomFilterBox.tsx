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
    gap: theme.spacing(1),
    gridTemplateColumns: '1fr auto 1fr auto 1fr auto 1fr',
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
  filters: RoomFilters | undefined;
};
type FormValues = Pick<RoomFilters, 'name'> & {
  from: Date;
  to: Date;
};

const RoomFilterBox = ({ filters, updateFilters }: RoomFilterBoxProps) => {
  const classes = useStyles();
  const [isOpen, setIsOpen] = useState(true);
  const mdDown = useMediaQuery((theme: Theme) => theme.breakpoints.down('md'));
  const { control, formState, handleSubmit, register } = useForm<FormValues>();
  const submit = async (data: FormValues) => {
    updateFilters({ ...data, to: data.to.toJSON(), from: data.from.toJSON() });
    setIsOpen(false);
  };
  return (
    <Paper blurred border className={classes.paper}>
      <Collapse in={!isOpen}>
        {filters && (
          <>
            <Typography>{`Søkeord: "${filters.name}"`}</Typography>
            <Typography>{`Fra ${formatDate(parseISO(filters.from))}`}</Typography>
            <Typography>{`Til ${formatDate(parseISO(filters.to))}`}</Typography>
          </>
        )}
        <Button fullWidth onClick={() => setIsOpen(true)} variant='text'>
          Endre søk
        </Button>
      </Collapse>
      <Collapse in={isOpen}>
        <form className={classes.filter} onSubmit={handleSubmit(submit)}>
          <TextField
            formState={formState}
            label='Navn'
            margin='dense'
            noDefaultShrink
            noOutline
            required
            {...register('name', { required: 'Du må oppgi et søkeord' })}
          />
          <Divider orientation={mdDown ? 'horizontal' : 'vertical'} />
          <DatePicker
            control={control}
            formState={formState}
            label='Fra'
            margin='dense'
            name='from'
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
