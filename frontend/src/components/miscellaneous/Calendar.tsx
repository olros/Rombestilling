import { useEffect, useMemo, useState, ReactNode } from 'react';
import { InfiniteData } from 'react-query';
import { Reservation, PaginationResponse } from 'types/Types';
import { useUserReservations, useSectionReservations } from 'hooks/Reservation';
import { useSnackbar } from 'hooks/Snackbar';
import { parseISO, endOfWeek, startOfWeek, endOfDay, startOfDay, endOfMonth, startOfMonth } from 'date-fns';
import { formatTime } from 'utils';
import { ViewState, AppointmentModel, EditingState, IntegratedEditing, ChangeSet, SchedulerDateTime } from '@devexpress/dx-react-scheduler';
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
import { Button, Typography, LinearProgress, makeStyles, SwipeableDrawer, Slide } from '@material-ui/core';

// Project components
import Paper from 'components/layout/Paper';
import ReserveForm from 'components/miscellaneous/ReserveForm';
import { ReservationInfoDialog } from 'components/miscellaneous/ReservationInfo';
import Container from 'components/layout/Container';

// Styles
const useStyles = makeStyles((theme) => ({
  root: {
    '& > div': {
      maxHeight: 600,
    },
    '& div:first-child': {
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
      background: `${theme.palette.background.paper}cc`,
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
  list: {
    display: 'grid',
    gap: theme.spacing(1),
  },
  reservationPaper: {
    maxWidth: theme.breakpoints.values.md,
    margin: 'auto',
    padding: theme.spacing(3, 2, 5),
    borderRadius: `${theme.shape.borderRadius}px ${theme.shape.borderRadius}px 0 0`,
    background: theme.palette.background.paper,
  },
  fixedBottom: {
    position: 'fixed',
    bottom: 0,
    left: 0,
    right: 0,
    paddingBottom: theme.spacing(1),
  },
  text: {
    padding: theme.spacing(0, 1),
  },
}));

type Filters = {
  fromTimeAfter: string;
  toTimeBefore: string;
};

const DEFAULT_FILTERS: Filters = {
  fromTimeAfter: startOfWeek(new Date(), { weekStartsOn: 1 }).toJSON(),
  toTimeBefore: endOfWeek(new Date(), { weekStartsOn: 1 }).toJSON(),
};

export type UserCalendarProps = {
  userId?: string;
};

export const UserCalendar = ({ userId }: UserCalendarProps) => {
  const [filters, setFilters] = useState<Filters>(DEFAULT_FILTERS);
  const { data, isLoading } = useUserReservations(userId, filters);
  return <Calendar data={data} isLoading={isLoading} setFilters={setFilters} />;
};

export type SectionCalendarProps = {
  sectionId: string;
};

export const SectionCalendar = ({ sectionId }: SectionCalendarProps) => {
  const [filters, setFilters] = useState<Filters>(DEFAULT_FILTERS);
  const { data, isLoading } = useSectionReservations(sectionId, filters);
  return <Calendar data={data} isLoading={isLoading} sectionId={sectionId} setFilters={setFilters} />;
};

export type CalendarProps = {
  data?: InfiniteData<PaginationResponse<Reservation>>;
  setFilters: React.Dispatch<React.SetStateAction<Filters>>;
  isLoading: boolean;
  sectionId?: string;
};

type ViewTypes = 'Day' | 'Week' | 'Month';
type NewAppointmentType = { endDate: Date; startDate: Date };
const NEW_APPOINTMENT = { title: 'Ny reservasjon', id: 'new-appointment' };

const Calendar = ({ data, isLoading, setFilters, sectionId }: CalendarProps) => {
  const classes = useStyles();
  const showSnackbar = useSnackbar();
  const [currentDate, setCurrentDate] = useState(new Date());
  const [currentViewName, setCurrentViewName] = useState<ViewTypes>('Day');
  const [addedAppointment, setAddedAppointment] = useState<AppointmentModel | undefined>();
  const [reservationOpen, setReservationOpen] = useState(false);
  const reservations = useMemo(() => (data !== undefined ? data.pages.map((page) => page.content).flat(1) : []), [data]);
  const displayedReservations = useMemo(
    () => [
      ...(addedAppointment ? [addedAppointment] : []),
      ...reservations.map(
        (reservation) => ({ ...reservation, startDate: reservation.fromTime, endDate: reservation.toTime, title: 'Reservert' } as AppointmentModel),
      ),
    ],
    [reservations, addedAppointment],
  );

  useEffect(() => {
    if (currentViewName === 'Day') {
      setFilters({ fromTimeAfter: startOfDay(currentDate).toJSON(), toTimeBefore: endOfDay(currentDate).toJSON() });
    } else if (currentViewName === 'Week') {
      setFilters({ fromTimeAfter: startOfWeek(currentDate, { weekStartsOn: 1 }).toJSON(), toTimeBefore: endOfWeek(currentDate, { weekStartsOn: 1 }).toJSON() });
    } else {
      setFilters({ fromTimeAfter: startOfMonth(currentDate).toJSON(), toTimeBefore: endOfMonth(currentDate).toJSON() });
    }
  }, [setFilters, currentViewName, currentDate]);

  const isTouchScreen = useMemo(() => matchMedia('(pointer: coarse)').matches, []);

  const stopReservation = () => {
    setReservationOpen(false);
    setAddedAppointment(undefined);
  };

  const schedulerDateTimeToDate = (time: SchedulerDateTime) => {
    if (time instanceof Date) {
      return time;
    } else if (typeof time === 'number') {
      return new Date(time);
    } else {
      return parseISO(time);
    }
  };

  type AppointmentProps = {
    data: AppointmentModel;
  };

  const Appointment = ({ data }: AppointmentProps) => {
    const [open, setOpen] = useState(false);
    return (
      <>
        <Appointments.Appointment className={classes.appointment} data={data} draggable={false} onClick={() => setOpen(true)} resources={[]}>
          <Typography color='inherit' variant='caption'>
            {data.title}
          </Typography>
          <br />
          <Typography color='inherit' variant='caption'>
            {`${formatTime(schedulerDateTimeToDate(data.startDate))} - ${formatTime(schedulerDateTimeToDate(data.endDate))}`}
          </Typography>
          <br />
          <Typography color='inherit' variant='caption'>
            {data.text}
          </Typography>
        </Appointments.Appointment>
        {open && data.id && data.id !== NEW_APPOINTMENT.id && sectionId && (
          <ReservationInfoDialog onClose={() => setOpen(false)} open={open} reservationId={String(data.id)} sectionId={String(sectionId)} />
        )}
      </>
    );
  };

  const DayViewTableCell = ({ onDoubleClick, ...restProps }: DayView.TimeTableCellProps) => (
    <DayView.TimeTableCell {...(isTouchScreen ? { onClick: onDoubleClick } : { onDoubleClick })} {...restProps} />
  );
  const WeekViewTableCell = ({ onDoubleClick, ...restProps }: WeekView.TimeTableCellProps) => (
    <WeekView.TimeTableCell {...(isTouchScreen ? { onClick: onDoubleClick } : { onDoubleClick })} {...restProps} />
  );

  type ToolbarWithLoadingProps = ToolbarProps & {
    children?: ReactNode;
  };

  const ToolbarWithLoading = ({ children, ...restProps }: ToolbarWithLoadingProps) => (
    <div className={classes.toolbarRoot}>
      <Toolbar.Root {...restProps}>{children}</Toolbar.Root>
      {isLoading && <LinearProgress className={classes.progress} />}
    </div>
  );

  const commitChanges = ({ changed }: ChangeSet) => {
    if (changed) {
      addAppointment(changed[NEW_APPOINTMENT.id]);
    }
  };

  const addAppointment = (newAppointment: NewAppointmentType) => {
    if (!sectionId) {
      return;
    }
    if (isOverlap(newAppointment)) {
      showSnackbar('Du kan ikke reservere en tid som overlapper med en annen tid', 'warning');
    } else {
      setAddedAppointment({ ...newAppointment, ...NEW_APPOINTMENT } as AppointmentModel);
    }
  };

  const isOverlap = (appointment: NewAppointmentType) =>
    reservations.some((reservation) => parseISO(reservation.fromTime) < appointment.endDate && parseISO(reservation.toTime) >= appointment.startDate);

  const ReactiveCalendar = () => (
    <Scheduler data={displayedReservations} firstDayOfWeek={1} locale='no-NB'>
      <ViewState
        currentDate={currentDate}
        currentViewName={currentViewName}
        onCurrentDateChange={setCurrentDate}
        onCurrentViewNameChange={(newView) => setCurrentViewName(newView as ViewTypes)}
      />
      <DayView cellDuration={60} endDayHour={20} startDayHour={6} timeTableCellComponent={DayViewTableCell} />
      <WeekView endDayHour={20} startDayHour={6} timeTableCellComponent={WeekViewTableCell} />
      <MonthView />
      <Toolbar rootComponent={ToolbarWithLoading} />
      <ViewSwitcher />
      <DateNavigator
        openButtonComponent={({ text, onVisibilityToggle }) => (
          <Button className={classes.button} onClick={onVisibilityToggle} variant='text'>
            {text}
          </Button>
        )}
      />
      <EditingState
        addedAppointment={addedAppointment}
        onAddedAppointmentChange={(e) => addAppointment(e as NewAppointmentType)}
        onCommitChanges={commitChanges}
      />
      <IntegratedEditing />
      <Appointments appointmentComponent={Appointment} />
      <DragDropProvider
        allowDrag={(appointment) => Boolean(sectionId) && appointment.id === NEW_APPOINTMENT.id}
        allowResize={(appointment) => Boolean(sectionId) && appointment.id === NEW_APPOINTMENT.id}
      />
      {currentViewName !== 'Month' && <AppointmentForm visible={false} />}
    </Scheduler>
  );

  return (
    <Paper className={classes.root} noPadding>
      {sectionId && (
        <Typography className={classes.text} variant='subtitle2'>
          {`${isTouchScreen ? 'Klikk' : 'Dobbelklikk'} på et tidspunkt for å opprette en reservasjon, dra på reservasjonen for å endre tiden.`}
        </Typography>
      )}
      <ReactiveCalendar />
      <Slide direction='up' in={Boolean(addedAppointment)}>
        <Container className={classes.fixedBottom} maxWidth='md'>
          <Button fullWidth onClick={() => setReservationOpen(true)}>
            Reserver
          </Button>
        </Container>
      </Slide>
      {sectionId && (
        <SwipeableDrawer
          anchor='bottom'
          classes={{ paper: classes.reservationPaper }}
          disableSwipeToOpen
          onClose={() => setReservationOpen(false)}
          onOpen={() => setReservationOpen(true)}
          open={reservationOpen}
          swipeAreaWidth={56}>
          <div className={classes.list}>
            {addedAppointment && (
              <ReserveForm
                from={schedulerDateTimeToDate(addedAppointment.startDate).toJSON()}
                onConfirm={stopReservation}
                sectionId={sectionId}
                to={schedulerDateTimeToDate(addedAppointment.endDate).toJSON()}
              />
            )}
            <Button onClick={stopReservation} variant='text'>
              Avbryt
            </Button>
          </div>
        </SwipeableDrawer>
      )}
    </Paper>
  );
};
