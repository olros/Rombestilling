import { useEffect, useState } from 'react';
import classNames from 'classnames';

// Material UI Components
import { makeStyles, Theme } from '@material-ui/core/styles';
import Skeleton from '@material-ui/core/Skeleton';

const useStyles = makeStyles<Theme, Pick<AspectRatioImgProps, 'ratio'>>((theme) => ({
  imgContainer: {
    position: 'relative',
    '&::before': {
      height: 0,
      content: '""',
      display: 'block',
      paddingBottom: (props) => `calc(100% / ( ${props.ratio} ))`,
    },
  },
  img: {
    position: 'absolute',
    top: 0,
    left: 0,
    width: '100%',
    height: '100%',
    objectFit: 'cover',
  },
  jpg: {
    '&:not([src*=".jpg"])': {
      background: theme.palette.common.white,
    },
  },
}));

export type AspectRatioImgProps = {
  alt: string;
  className?: string;
  imgClassName?: string;
  ratio?: number;
  src?: string;
};

const FallbackLogo = '/DefaultBackground.jpg';

const AspectRatioImg = ({ alt, className, imgClassName, ratio = 21 / 9, src }: AspectRatioImgProps) => {
  const classes = useStyles({ ratio });
  const [imgUrl, setImgUrl] = useState(src || FallbackLogo);
  useEffect(() => {
    setImgUrl(src || FallbackLogo);
  }, [src]);
  return (
    <div className={classNames(classes.imgContainer, className)}>
      <img alt={alt} className={classNames(classes.img, classes.jpg, imgClassName)} onError={() => setImgUrl(FallbackLogo)} src={imgUrl} />
    </div>
  );
};
export default AspectRatioImg;

export const AspectRatioLoading = ({ className, imgClassName, ratio = 21 / 9 }: Pick<AspectRatioImgProps, 'className' | 'imgClassName' | 'ratio'>) => {
  const classes = useStyles({ ratio });
  return (
    <div className={classNames(classes.imgContainer, className)}>
      <Skeleton className={classNames(classes.img, classes.skeleton, imgClassName)} variant='rectangular' />
    </div>
  );
};
