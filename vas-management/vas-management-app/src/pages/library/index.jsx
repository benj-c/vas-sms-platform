//lib
import { createUseStyles } from "react-jss";
import { useRecoilValue } from "recoil";
//app
import { selectedTopBarMenuAtom } from "../../state/atoms"
import ServiceContainer from "./ServiceContainer";
import ApiContainer from "./ApiContainer";

const useStyles = createUseStyles({
    pageRoot: {
        padding: '1rem',
        height: '80vh',
    },
})

const Library = () => {
    const classes = useStyles();
    const selectedMenu = useRecoilValue(selectedTopBarMenuAtom);

    return (
        <div className={classes.pageRoot}>
            {selectedMenu.title === "Services" ? <ServiceContainer /> : <ApiContainer />}
        </div>
    )
}

export default Library;
