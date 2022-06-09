import { useEffect, useState } from "react";
import { createUseStyles } from "react-jss";
// import { getGropus } from "../../common/HttpClient"

const useStyles = createUseStyles({
    '@keyframes animateUp': {
        from: {
            height: '0.5rem'
        },
        to: {
            height: '1rem',
        }
    },
    storeNav: {
        '& h2': {
            fontSize: '1.25rem',
            marginBottom: '1rem',
        },
        '& ul': {
            padding: '0 0.25rem',
            '& li': {
                padding: '0.5rem 0.5rem 0.5rem 0',
                borderRadius: '0.25rem',
                margin: '0.25rem 0',
                display: 'flex',
                alignItems: 'center',
                '& span': {
                    paddingLeft: '0.8rem',
                },
                '& i': {
                    marginLeft: 'auto'
                },
                '&:hover': {
                    background: 'rgba(255, 255, 255, 0.1)'
                },
                '&:before': {
                    content: '""',
                    height: '0rem',
                    width: '3.5px',
                    position: 'absolute',
                    borderRadius: '2.5px',
                    background: 'var(--themePrimary)',
                }
            }
        }
    },
    active: {
        background: 'rgba(255, 255, 255, 0.1)',
        '&:before': {
            animation: '$animateUp 0.2s forwards'
        }
    },
})

const StoreNavigation = props => {
    const classes = useStyles();
    const [groups, setGroups] = useState([]);
    const [loadingGroups, setIsGroupsLoading] = useState(false);
    const [selectedGroup, setSelectedGroup] = useState(null);

    useEffect(() => {
        setIsGroupsLoading(true);
        // getGropus()
        //     .then(res => {
        //         let fs = res;
        //         setGroups(fs)
        //         setSelectedGroup(fs[0])
        //     })
        //     .catch(e => { })
        //     .finally(() => {
        //         setIsGroupsLoading(false);
        //     });
    }, [])

    const changeGroup = g => {
        setSelectedGroup(g);
    }

    return (
        <div className={classes.storeNav}>
            <h2>Collections</h2>
            <ul>
                {groups.length > 0 && selectedGroup && groups.map((v, i) => (
                    <li
                        key={i}
                        onClick={() => changeGroup(v)}
                        className={v.id === selectedGroup.id ? classes.active : ''}
                    >
                        <span>{v.title}</span>
                        {v.id === selectedGroup.id && (
                            <i className={`ms-Icon ms-Icon--Forward`} aria-hidden="true"></i>
                        )}
                    </li>
                ))}
            </ul>
        </div>
    )
}

export default StoreNavigation;
