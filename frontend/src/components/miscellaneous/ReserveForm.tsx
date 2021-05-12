import { useForm } from 'react-hook-form';
import classnames from 'classnames';
import { formatDate } from 'utils';
import { parseISO } from 'date-fns';
import { useSectionById } from 'hooks/Section';

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

type FormValues = {
  amount: string;
  description: string;
};

const ReserveForm = ({ onConfirm, sectionId, from, to, className }: ReserveFormProps) => {
  const classes = useStyles();
  const { data: section } = useSectionById(sectionId);
  const { formState, handleSubmit, register } = useForm<FormValues>();
  const submit = async (data: FormValues) => {
    // eslint-disable-next-line no-console
    console.log({ ...data, sectionId });
    if (onConfirm) {
      onConfirm();
    }
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
        {...register('amount', { required: 'Du må oppgi antall personer' })}
      />
      <TextField formState={formState} label='Beskrivelse' {...register('description')} />
      <SubmitButton formState={formState}>Reserver</SubmitButton>
    </form>
  );
};

export default ReserveForm;
