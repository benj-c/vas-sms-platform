//lib
import { DetailsList, DetailsListLayoutMode, SelectionMode } from "@fluentui/react";
import { useState } from "react";
import { createUseStyles } from "react-jss";
//app
import SmsHistorySearchForm from "../../common/forms/SmsHistorySearchForm";
import { getSmsHistoryByMsisdn } from "../../common/ApiHandler"

const useStyles = createUseStyles({
    pageRoot: {
        padding: '1rem',
    }
})

const _columns = [
    {
        key: 'msisdn',
        name: 'MSISDN',
        fieldName: 'msisdn',
        minWidth: 150,
        maxWidth: 150,
        isResizable: false,
    },
    {
        key: 'incomingSms',
        name: 'Incoming SMS',
        fieldName: 'incomingSms',
        minWidth: 100,
        maxWidth: 200,
        isResizable: false,
    },
    {
        key: 'responseSms',
        name: 'Response SMS',
        fieldName: 'responseSms',
        minWidth: 200,
        maxWidth: 400,
        isResizable: true,
    },
    {
        key: 'receivedTime',
        name: 'Received Time',
        fieldName: 'receivedTime',
        minWidth: 100,
        maxWidth: 200,
        isResizable: false,
        onRender: (item) => {
            let t = item.receivedTime.split("T");
            return (
                <span>{t[0]} {t[1]}</span>
            )
        }
    },
    {
        key: 'sentTime',
        name: 'Sent Time',
        fieldName: 'sentTime',
        minWidth: 100,
        maxWidth: 200,
        isResizable: false,
        onRender: (item) => {
            let t = item.sentTime.split("T");
            return (
                <span>{t[0]} {t[1]}</span>
            )
        }
    },
];

const SmsHistory = () => {
    const classes = useStyles();
    const [data, setData] = useState([]);

    const onSearchByMsisdn = term => {
        loadDataByMsisdn(term);
    }

    const loadDataByMsisdn = (msisdn) => {
        getSmsHistoryByMsisdn(msisdn)
            .then(res => {
                setData(res.data);
            })
            .catch(e => { })
            .finally(() => { })
    }

    return (
        <div className={classes.pageRoot}>
            <SmsHistorySearchForm onSearch={onSearchByMsisdn} />
            <div style={{marginTop: '1rem'}}>
                <DetailsList
                    compact={true}
                    items={data}
                    columns={_columns}
                    isHeaderVisible={true}
                    setKey="none"
                    selectionMode={SelectionMode.none}
                    layoutMode={DetailsListLayoutMode.justified}
                />
            </div>
        </div>
    )
}

export default SmsHistory;
