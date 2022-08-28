//lib
import { createUseStyles } from "react-jss";
//app
import ApiContainer from "./ApiContainer";

const useStyles = createUseStyles({
    pageRoot: {
        padding: '1rem',
        height: '80vh',
    },
})

const ApiGallary = () => {
    const classes = useStyles();

    return (
        <div className={classes.pageRoot}>
            <ApiContainer />
        </div>
    )
}

export default ApiGallary;
