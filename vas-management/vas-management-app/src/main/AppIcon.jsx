import { createUseStyles } from 'react-jss';

const useStyles = createUseStyles({
	appIcon: {
		display: 'grid',
		gridTemplateColumns: '50% 50%',
		gap: '1px',
		'& div': {
			height: '9px',
			width: '9px',
			background: '#fff',
			borderRadius: '1px',
			'&:first-child': {
				borderRadius: '3px'
			}
		}
	}
})

const AppIcon = () => {
	const classes = useStyles();
	return (
		<div className={classes.appIcon}>
			<div></div>
			<div></div>
			<div></div>
			<div></div>
		</div>
	)
}

export default AppIcon;
