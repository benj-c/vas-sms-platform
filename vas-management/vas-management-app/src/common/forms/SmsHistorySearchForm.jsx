//lib
import { PrimaryButton, SearchBox } from "@fluentui/react";
import { createUseStyles } from "react-jss";

const useStyles = createUseStyles({
    form: {
        display: 'grid',
        gridTemplateColumns: '20% 20%'
    }
})


const SmsHistorySearchForm = ({ onSearch, onSearchStopped }) => {
    const classes = useStyles();
    return (
        <form className={classes.form}>
            <SearchBox
                placeholder="Search by MSISDN"
                onSearch={onSearch}
                onEscape={onSearchStopped}
                style={{ width: '200' }}
            />
        </form>
    )
}

export default SmsHistorySearchForm;
