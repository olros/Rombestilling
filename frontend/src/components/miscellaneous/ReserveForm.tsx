import { useForm } from 'react-hook-form';
import classnames from 'classnames';
import { formatDate } from 'utils';
import { parseISO } from 'date-fns';
import { useCreateReservation } from 'hooks/Reservation';
import { useSectionById } from 'hooks/Section';
import { useSnackbar } from 'hooks/Snackbar';
import { useUser } from 'hooks/User';
import { ReservationCreate } from 'types/Types';

// Material UI
import { makeStyles, Typography } from '@material-ui/core';

// Project Components
import Paper from 'components/layout/Paper';
import TextField from 'components/inputs/TextField';
import SubmitButton from 'components/inputs/SubmitButton';

const useStyles = makeStyles(() => ({
  form: {
    display: 'grid',
  },
}));

export type ReserveFormProps = {
  onConfirm?: () => void;
  sectionId: string;
  from: string;
  to: string;
  className?: string;
};

type FormValues = Pick<ReservationCreate, 'nrOfPeople' | 'text'>;

const ReserveForm = ({ onConfirm, sectionId, from, to, className }: ReserveFormProps) => {
  const classes = useStyles();
  const { data: user } = useUser();
  const { data: section } = useSectionById(sectionId);
  const reserve = useCreateReservation(sectionId);
  const { formState, handleSubmit, register } = useForm<FormValues>();
  const showSnackbar = useSnackbar();
  const submit = async (data: FormValues) => {
    if (!user) {
      return;
    }
    reserve.mutate(
      { ...data, userId: user.id, fromTime: from, toTime: to },
      {
        onSuccess: () => {
          showSnackbar('Reservasjonen var vellykket!', 'success');
          if (onConfirm) {
            onConfirm();
          }
        },
        onError: (e) => {
          showSnackbar(e.message, 'error');
        },
      },
    );
  };
  if (!section) {
    return null;
  }
  return (
    <form className={classnames(classes.form, className)} onSubmit={handleSubmit(submit)}>
      <Typography variant='h2'>{`Reserver ${section.name}`}</Typography>
      <Paper border>
        <Typography variant='subtitle2'>
          {section.type === 'room' ? 'Du reserverer et helt rom' : `Du reserverer en del av rommet: ${section.parent.name}`}
        </Typography>
        <Typography variant='subtitle2'>{`Kapasitet: ${section.capacity}`}</Typography>
        <Typography variant='subtitle2'>{`Fra: ${formatDate(parseISO(from))}`}</Typography>
        <Typography variant='subtitle2'>{`Til: ${formatDate(parseISO(to))}`}</Typography>
      </Paper>
      <TextField
        formState={formState}
        InputProps={{ type: 'number' }}
        label='Antall personer'
        required
        {...register('nrOfPeople', { valueAsNumber: true, required: 'Du må oppgi antall personer' })}
      />
      <TextField formState={formState} label='Beskrivelse' maxRows={5} minRows={2} multiline {...register('text')} />
      <SubmitButton formState={formState}>Reserver</SubmitButton>
    </form>
  );
};

export default ReserveForm;
