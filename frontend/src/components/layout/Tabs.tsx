import { ComponentType } from 'react';

// Material UI Components
import { makeStyles, Tabs as MaterialTabs, Tab } from '@material-ui/core';

const useStyles = makeStyles((theme) => ({
  tabsIndicator: {
    backgroundColor: theme.palette.primary.main,
    borderTopLeftRadius: 3,
    borderTopRightRadius: 3,
    height: 3,
  },
  tabRoot: {
    textTransform: 'none',
    fontSize: '0.9rem',
    minWidth: 92,
    color: theme.palette.text.primary,
    '&:hover': {
      color: theme.palette.primary.main,
      opacity: 0.85,
    },
    '&$selected': {
      color: theme.palette.primary.main,
    },
    '&:focus': {
      color: theme.palette.primary.dark,
    },
  },
  selected: {},
  icon: {
    verticalAlign: 'middle',
    marginRight: 7,
    marginBottom: 3,
  },
}));

const a11yProps = (value: string | number) => {
  return {
    id: `simple-tab-${value}`,
    'aria-controls': `tabpanel-${value}`,
  };
};

export type IProps = {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  selected: any;
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  setSelected: (newSelected: any) => void;
  tabs: Array<{
    icon?: ComponentType<{ className: string }>;
    label: string;
    value: number | string;
  }>;
  marginBottom?: boolean;
};

const Tabs = ({ tabs, selected, setSelected }: IProps) => {
  const classes = useStyles();

  return (
    <MaterialTabs
      aria-label='Tabs'
      classes={{ indicator: classes.tabsIndicator }}
      onChange={(e, newTab) => setSelected(newTab)}
      value={selected}
      variant='scrollable'>
      {tabs.map((tab, index) => (
        <Tab
          classes={{ root: classes.tabRoot, selected: classes.selected }}
          disableRipple
          key={index}
          label={
            <div>
              {tab.icon && <tab.icon className={classes.icon} />}
              {tab.label}
            </div>
          }
          value={tab.value}
          {...a11yProps(tab.value)}
        />
      ))}
    </MaterialTabs>
  );
};

export default Tabs;
