import { useForm } from 'react-hook-form';

// Material UI Components
import { makeStyles, Divider } from '@material-ui/core';

// Project Components
import DatePicker from 'components/inputs/DatePicker';
import TextField from 'components/inputs/TextField';
import Paper from 'components/layout/Paper';
import SubmitButton from 'components/inputs/SubmitButton';
import { RoomFilters } from 'containers/Rooms';

const useStyles = makeStyles((theme) => ({
  paper: {
    padding: theme.spacing(2),
    margin: theme.spacing(2),
  },
  filter: {
    display: 'grid',
    gridTemplateColumns: '1fr auto 1fr auto 1fr auto 1fr',
    gap: theme.spacing(1),
  },
  submit: {
    height: '100%',
  },
}));

export type RoomFilterBoxProps = {
  updateFilters: (newFilters: RoomFilters) => void;
};
type FormValues = Pick<RoomFilters, 'name'> & {
  from: Date;
  to: Date;
};

const RoomFilterBox = ({ updateFilters }: RoomFilterBoxProps) => {
  const classes = useStyles();
  const { control, formState, handleSubmit, register } = useForm<FormValues>();
  const submit = async (data: FormValues) => {
    console.log({ ...data, to: data.to.toJSON(), from: data.from.toJSON() });
    updateFilters({ ...data, to: data.to.toJSON(), from: data.from.toJSON() });
  };
  return (
    <Paper className={classes.paper}>
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
        <Divider orientation='vertical' />
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
        <Divider orientation='vertical' />
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
        <Divider orientation='vertical' />
        <div>
          <SubmitButton className={classes.submit} formState={formState} noFeedback variant='text'>
            Søk
          </SubmitButton>
        </div>
      </form>
    </Paper>
  );
};

export default RoomFilterBox;
