import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { UserCreate } from 'types/Types';
import { useSnackbar } from 'hooks/Snackbar';
import { useCreateUser } from 'hooks/User';
import { EMAIL_REGEX } from 'constant';

// Material UI Components
import { Button, ButtonProps } from '@material-ui/core';

// Project components
import Dialog from 'components/layout/Dialog';
import TextField from 'components/inputs/TextField';
import SubmitButton from 'components/inputs/SubmitButton';
import { SingleImageUpload } from 'components/inputs/Upload';

const CreateUser = ({ children, ...props }: ButtonProps) => {
  const [open, setOpen] = useState(false);
  const showSnackbar = useSnackbar();
  const createUser = useCreateUser();
  const { register, formState, handleSubmit } = useForm<UserCreate>();

  const submit = async (data: UserCreate) => {
    createUser.mutate(data, {
      onSuccess: () => {
        showSnackbar('Brukeren ble opprettet og har mottatt en epost med link for opprettelse av passord', 'success');
        setOpen(false);
      },
      onError: (e) => {
        showSnackbar(e.message, 'error');
      },
    });
  };

  return (
    <>
      <Button fullWidth variant='outlined' {...props} onClick={() => setOpen(true)}>
        {children}
      </Button>
      <Dialog onClose={() => setOpen(false)} open={open} titleText='Opprett bruker'>
        <form onSubmit={handleSubmit(submit)}>
          <TextField
            disabled={createUser.isLoading}
            formState={formState}
            label='Fornavn'
            {...register('firstName', { required: 'Feltet er påkrevd' })}
            required
          />
          <TextField
            disabled={createUser.isLoading}
            formState={formState}
            label='Etternavn'
            {...register('surname', { required: 'Feltet er påkrevd' })}
            required
          />
          <TextField
            disabled={createUser.isLoading}
            formState={formState}
            label='Epost'
            {...register('email', {
              required: 'Feltet er påkrevd',
              pattern: {
                value: EMAIL_REGEX,
                message: 'Ugyldig e-post',
              },
            })}
            required
            type='email'
          />
          <TextField
            disabled={createUser.isLoading}
            formState={formState}
            InputProps={{ type: 'number' }}
            label='Telefonnummer'
            {...register('phoneNumber', { required: 'Feltet er påkrevd' })}
            required
          />
          <SubmitButton disabled={createUser.isLoading} formState={formState}>
            Opprett bruker
          </SubmitButton>
        </form>
      </Dialog>
    </>
  );
};

export default CreateUser;
