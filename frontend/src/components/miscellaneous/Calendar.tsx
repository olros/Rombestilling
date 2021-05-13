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
import { Button, Typography, LinearProgress, makeStyles, SwipeableDrawer, Slide } from '@material-ui/core';

// Project components
import Paper from 'components/layout/Paper';
import ReserveForm from 'components/miscellaneous/ReserveForm';
import Container from 'components/layout/Container';

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
    return <Calendar data={data} isLoading={isLoading} sectionId={sectionId} setFilters={setFilters} />;
  }
  return null;
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
  const [currentViewName, setCurrentViewName] = useState<ViewTypes>('Week');
  const [addedAppointment, setAddedAppointment] = useState<AppointmentModel | undefined>();
  const [reservationOpen, setReservationOpen] = useState(false);
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

  const isTouchScreen = matchMedia('(hover: none)').matches;

  const stopReservation = () => {
    setReservationOpen(false);
    setAddedAppointment(undefined);
  };

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
      <LinearProgress className={classes.progress} />
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

  return (
    <Paper className={classes.root} noPadding>
      <Scheduler data={displayedReservations} firstDayOfWeek={1} locale='no-NB'>
        <ViewState
          currentDate={currentDate}
          currentViewName={currentViewName}
          onCurrentDateChange={setCurrentDate}
          onCurrentViewNameChange={(newView) => setCurrentViewName(newView as ViewTypes)}
        />
        <DayView cellDuration={60} timeTableCellComponent={DayViewTableCell} />
        <WeekView timeTableCellComponent={WeekViewTableCell} />
        <MonthView />
        <Toolbar {...(isLoading ? { rootComponent: ToolbarWithLoading } : null)} />
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
              <ReserveForm from={(addedAppointment.startDate as Date).toJSON()} sectionId={sectionId} to={(addedAppointment.endDate as Date).toJSON()} />
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
