import { ActivityItem, DefaultPalette } from "@fluentui/react";
import { useEffect, useState } from "react"
import { getUserActions } from "../../common/ApiHandler"

const UserActionLog = () => {
    const [activities, setActivities] = useState([]);
    const [isActionsLoading, toggleActionsLoading] = useState(false);

    useEffect(() => {
        loadData();
    }, [])

    const loadData = () => {
        toggleActionsLoading(true);
        getUserActions().then(res => {
            if (res.data.length > 0) {
                let arr = [];
                res.data.forEach((item, i) => {
                    arr.push({
                        key: i,
                        activityDescription: [
                            <a key={1} style={{ fontWeight: 'bold', color: '#e7e7e7' }} >
                                {item.user}
                            </a>,
                            <span key={2} style={{ color: '#e2e2e2' }}> {item.type} </span>,
                            <span key={3} style={{ color: DefaultPalette.themePrimary }}>
                                {item.target}
                            </span>,
                        ],
                        activityPersonas: [{ imageInitials: item.user.substring(0,2).toUpperCase(), text: item.user }],
                        comments: <span style={{ color: DefaultPalette.themeTertiary }}>{item.comment != "null" ? item.comment : ''}</span>,
                        timeStamp: <span style={{ color: DefaultPalette.white }}>{item.timestamp}</span>,
                    })
                })
                setActivities(arr);
            }
        })
            .catch(e => { })
            .finally(() => {
                toggleActionsLoading(false);
            })
    }

    return (
        <>
            <span>Activity Log</span>
            <div style={{ marginTop: '0.75rem' }}>
                {activities.map((item) => (
                    <ActivityItem {...item} key={item.key} style={{marginBottom: '0.75rem'}} />
                ))}
            </div>
        </>
    )
}

export default UserActionLog;
