//lib
import { createUseStyles } from "react-jss";
//app
import ServiceContainer from "./ServiceContainer";

const useStyles = createUseStyles({
    pageRoot: {
        padding: '1rem',
        height: '90vh',
        overflowY: 'auto'
    },
})

const Library = () => {
    const classes = useStyles();

    return (
        <div className={classes.pageRoot}>
            <ServiceContainer />
        </div>
    )
}

export default Library;
