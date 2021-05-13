import { useEffect, useMemo, useState, ReactNode } from 'react';
import { InfiniteData } from 'react-query';
import { Reservation, PaginationResponse } from 'types/Types';
import { useUserReservations, useSectionReservations } from 'hooks/Reservation';
import URLS from 'URLS';
import { Link } from 'react-router-dom';
import { endOfWeek, startOfWeek, endOfDay, startOfDay, endOfMonth, startOfMonth } from 'date-fns';
import { urlEncode } from 'utils';
import { ViewState, AppointmentModel } from '@devexpress/dx-react-scheduler';
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
} from '@devexpress/dx-react-scheduler-material-ui';

// Material-UI
import { Button, Typography, LinearProgress, makeStyles } from '@material-ui/core';

// Project components
import Paper from 'components/layout/Paper';

// Styles
const useStyles = makeStyles((theme) => ({
  root: {
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

const Calendar = ({ data, isLoading, setFilters }: CalendarProps) => {
  const classes = useStyles();
  const reservations = useMemo(() => (data !== undefined ? data.pages.map((page) => page.content).flat(1) : []), [data]);
  const [currentDate, setCurrentDate] = useState(new Date());
  const [currentViewName, setCurrentViewName] = useState<ViewTypes>('Week');
  const displayedReservations = useMemo(
    () =>
      reservations.map(
        (reservation) =>
          ({ ...reservation, startDate: reservation.fromTime, endDate: reservation.toTime, title: String(reservation.numberOfPeople) } as AppointmentModel),
      ),
    [reservations],
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

  const Appointment = ({ data }: AppointmentProps) => {
    return (
      <Link to={`${URLS.ROOMS}${data.id}/${urlEncode(data.title)}/`}>
        <Appointments.Appointment className={classes.appointment} data={data} draggable={false} resources={[]}>
          <Typography color='inherit' variant='caption'>
            {data.title}
          </Typography>
        </Appointments.Appointment>
      </Link>
    );
  };

  type ToolbarWithLoadingProps = ToolbarProps & {
    children?: ReactNode;
  };

  const ToolbarWithLoading = ({ children, ...restProps }: ToolbarWithLoadingProps) => (
    <div className={classes.toolbarRoot}>
      <Toolbar.Root {...restProps}>{children}</Toolbar.Root>
      <LinearProgress className={classes.progress} />
    </div>
  );

  return (
    <Paper className={classes.root} noPadding>
      <Scheduler data={displayedReservations} firstDayOfWeek={1} locale='no-NB'>
        <ViewState
          currentDate={currentDate}
          currentViewName={currentViewName}
          onCurrentDateChange={setCurrentDate}
          onCurrentViewNameChange={(newView) => setCurrentViewName(newView as ViewTypes)}
        />
        <DayView endDayHour={18} startDayHour={9} />
        <WeekView endDayHour={19} startDayHour={10} />
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
        <Appointments appointmentComponent={Appointment} />
      </Scheduler>
    </Paper>
  );
};
