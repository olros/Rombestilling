import { useEffect, useMemo, useState, ReactNode } from 'react';
import { InfiniteData } from 'react-query';
import { Reservation, PaginationResponse } from 'types/Types';
import { useUserReservations, useSectionReservations } from 'hooks/Reservation';
import { useSnackbar } from 'hooks/Snackbar';
import URLS from 'URLS';
import { Link } from 'react-router-dom';
import { parseISO, endOfWeek, startOfWeek, endOfDay, startOfDay, endOfMonth, startOfMonth } from 'date-fns';
import { formatTime, urlEncode } from 'utils';
import { ViewState, AppointmentModel, EditingState, IntegratedEditing, ChangeSet } from '@devexpress/dx-react-scheduler';
import {
  Scheduler,
  MonthView,
  WeekView,
  DayView,
  Toolbar,
  ToolbarProps,
  DateNavigator,
  ViewSwitcher,
  Appointments,
  AppointmentForm,
  DragDropProvider,
} from '@devexpress/dx-react-scheduler-material-ui';

// Material-UI
import { Button, Typography, LinearProgress, makeStyles } from '@material-ui/core';

// Project components
import Paper from 'components/layout/Paper';

// Styles
const useStyles = makeStyles((theme) => ({
  root: {
    '& > div': {
      maxHeight: 600,
    },
    '& div:first-child': {
      overflowY: 'hidden',
      whiteSpace: 'break-spaces',
    },
    '& table': {
      minWidth: 'unset',
    },
  },
  button: {
    color: theme.palette.get<string>({ light: theme.palette.common.black, dark: theme.palette.common.white }),
  },
  appointment: {
    color: theme.palette.get<string>({ light: theme.palette.common.black, dark: theme.palette.common.white }),
    background: theme.palette.background.paper,
    '&:hover': {
      background: `${theme.palette.background.paper}aa`,
    },
  },
  toolbarRoot: {
    position: 'relative',
  },
  progress: {
    position: 'absolute',
    width: '100%',
    bottom: 0,
    left: 0,
  },
}));

type Filters = {
  fromTime: string;
  toTime: string;
};

export type UserCalendarProps = {
  userId?: string;
};

export const UserCalendar = ({ userId }: UserCalendarProps) => {
  const [filters, setFilters] = useState<Filters>({
    fromTime: startOfWeek(new Date(), { weekStartsOn: 1 }).toJSON(),
    toTime: endOfWeek(new Date(), { weekStartsOn: 1 }).toJSON(),
  });
  const { data, isLoading } = useUserReservations(userId, filters);

  if (!data) {
    return <Calendar data={data} isLoading={isLoading} setFilters={setFilters} />;
  }
  return null;
};

export type SectionCalendarProps = {
  sectionId: string;
};

export const SectionCalendar = ({ sectionId }: SectionCalendarProps) => {
  const [filters, setFilters] = useState<Filters>({
    fromTime: startOfWeek(new Date(), { weekStartsOn: 1 }).toJSON(),
    toTime: endOfWeek(new Date(), { weekStartsOn: 1 }).toJSON(),
  });
  const { data, isLoading } = useSectionReservations(sectionId, filters);

  if (!data) {
    return <Calendar data={data} isLoading={isLoading} setFilters={setFilters} />;
  }
  return null;
};

export type CalendarProps = {
  data?: InfiniteData<PaginationResponse<Reservation>>;
  setFilters: React.Dispatch<React.SetStateAction<Filters>>;
  isLoading: boolean;
};

type ViewTypes = 'Day' | 'Week' | 'Month';
type NewAppointmentType = { endDate: Date; startDate: Date };

const NEW_APPOINTMENT = { title: 'Ny reservasjon', id: 'new-appointment' };

const Calendar = ({ data, isLoading, setFilters }: CalendarProps) => {
  const classes = useStyles();
  const showSnackbar = useSnackbar();
  const [currentDate, setCurrentDate] = useState(new Date());
  const [currentViewName, setCurrentViewName] = useState<ViewTypes>('Week');
  const [addedAppointment, setAddedAppointment] = useState<AppointmentModel | undefined>();
  const reservations = useMemo(() => (data !== undefined ? data.pages.map((page) => page.content).flat(1) : []), [data]);
  const displayedReservations = useMemo(
    () => [
      ...(addedAppointment ? [addedAppointment] : []),
      ...reservations.map(
        (reservation) =>
          ({ ...reservation, startDate: reservation.fromTime, endDate: reservation.toTime, title: String(reservation.numberOfPeople) } as AppointmentModel),
      ),
    ],
    [reservations, addedAppointment],
  );

  useEffect(() => {
    if (currentViewName === 'Day') {
      setFilters({ fromTime: startOfDay(currentDate).toJSON(), toTime: endOfDay(currentDate).toJSON() });
    } else if (currentViewName === 'Week') {
      setFilters({ fromTime: startOfWeek(currentDate, { weekStartsOn: 1 }).toJSON(), toTime: endOfWeek(currentDate, { weekStartsOn: 1 }).toJSON() });
    } else {
      setFilters({ fromTime: startOfMonth(currentDate).toJSON(), toTime: endOfMonth(currentDate).toJSON() });
    }
  }, [setFilters, currentViewName, currentDate]);

  type AppointmentProps = {
    data: AppointmentModel;
  };

  const Appointment = ({ data }: AppointmentProps) => (
    <Link to={`${URLS.ROOMS}${data.id}/${urlEncode(data.title)}/`}>
      <Appointments.Appointment className={classes.appointment} data={data} draggable={false} resources={[]}>
        <Typography color='inherit' variant='caption'>
          {data.title}
        </Typography>
        <br />
        <Typography color='inherit' variant='caption'>
          {`${formatTime(data.startDate as Date)} - ${formatTime(data.endDate as Date)}`}
        </Typography>
      </Appointments.Appointment>
    </Link>
  );

  type ToolbarWithLoadingProps = ToolbarProps & {
    children?: ReactNode;
  };

  const ToolbarWithLoading = ({ children, ...restProps }: ToolbarWithLoadingProps) => (
    <div className={classes.toolbarRoot}>
      <Toolbar.Root {...restProps}>{children}</Toolbar.Root>
      <LinearProgress className={classes.progress} />
    </div>
  );

  const commitChanges = ({ changed }: ChangeSet) => {
    if (changed) {
      // setAddedAppointment((prev) => ({ ...prev, ...changed[NEW_APPOINTMENT.id] }));
      addAppointment(changed[NEW_APPOINTMENT.id]);
    }
  };

  const addAppointment = (newAppointment: NewAppointmentType) => {
    if (isOverlap(newAppointment)) {
      showSnackbar('Du kan ikke reservere en tid som overlapper med en annen tid', 'warning');
    } else {
      setAddedAppointment({ ...newAppointment, ...NEW_APPOINTMENT } as AppointmentModel);
    }
  };

  const isOverlap = (appointment: NewAppointmentType) =>
    reservations.some((reservation) => parseISO(reservation.fromTime) < appointment.endDate && parseISO(reservation.toTime) >= appointment.startDate);

  return (
    <Paper className={classes.root} noPadding>
      <Scheduler data={displayedReservations} firstDayOfWeek={1} locale='no-NB'>
        <ViewState
          currentDate={currentDate}
          currentViewName={currentViewName}
          onCurrentDateChange={setCurrentDate}
          onCurrentViewNameChange={(newView) => setCurrentViewName(newView as ViewTypes)}
        />
        <DayView cellDuration={60} />
        <WeekView />
        <MonthView />
        <Toolbar {...(isLoading ? { rootComponent: ToolbarWithLoading } : null)} />
        <ViewSwitcher />
        <EditingState
          addedAppointment={addedAppointment}
          onAddedAppointmentChange={(e) => addAppointment(e as NewAppointmentType)}
          onCommitChanges={commitChanges}
        />
        <IntegratedEditing />
        <DateNavigator
          openButtonComponent={({ text, onVisibilityToggle }) => (
            <Button className={classes.button} onClick={onVisibilityToggle} variant='text'>
              {text}
            </Button>
          )}
        />
        <Appointments appointmentComponent={Appointment} />
        <DragDropProvider
          allowDrag={(appointment) => appointment.id === NEW_APPOINTMENT.id}
          allowResize={(appointment) => appointment.id === NEW_APPOINTMENT.id}
        />
        {currentViewName !== 'Month' && <AppointmentForm visible={false} />}
      </Scheduler>
    </Paper>
  );
};
