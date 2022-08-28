import { DefaultPalette, Stack, Text } from "@fluentui/react";
import { useEffect, useState } from "react";
import { createUseStyles } from "react-jss";
import { getSmsStats } from "../../common/ApiHandler";

const useStyles = createUseStyles({
    itemStyles: {
        alignItems: 'center',
        background: DefaultPalette.themePrimary,
        color: DefaultPalette.white,
        display: 'flex',
        padding: '1rem 0',
        justifyContent: 'center',
        flexDirection: 'column',
        borderRadius: '0.25rem',
        width: 150,
    }
})

const UsageStats = () => {
    const classes = useStyles();
    const [data, setData] = useState([]);

    useEffect(() => {
        // let interval = setInterval(() => loadStats(), (3000))
        // return () => clearInterval(interval)
        loadStats();
    }, [])

    const loadStats = () => {
        getSmsStats().then(res => {
            setData([
                {
                    item: 'SMS In',
                    value: res.data.smsInCount,
                    desc: 'Inbound SMS count'
                },
                {
                    item: 'SMS Out',
                    value: res.data.smsOutCount,
                    desc: 'Outgoing SMS count'
                }
            ])
        })
            .catch(e => { })
            .finally(() => { })
    }

    return (
        <Stack>
            <span style={{ margin: '0.75rem 0' }}>Service Statistics</span>
            <Stack horizontal tokens={{
                childrenGap: 10,
            }}>
                {data.length > 0 && data.map((item, key) => {
                    return (
                        <span className={classes.itemStyles} key={key}>
                            <Text variant="medium" style={{ textTransform: 'capitalize' }}>{item.item}</Text>
                            <Text variant="xLarge" style={{ fontWeight: 600, margin: '0rem 0' }}>{item.value}</Text>
                            <Text variant="small">{item.desc}</Text>
                        </span>
                    )
                })}
            </Stack>
        </Stack>
    )
}

export default UsageStats;
