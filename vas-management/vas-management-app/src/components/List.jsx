import { useCallback, useState } from "react";
import { createUseStyles } from "react-jss";

const useStyles = createUseStyles({
    '@keyframes animateUp': {
        from: {
            height: '0.5rem'
        },
        to: {
            height: '1rem',
        }
    },
    item_list: {
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
    },
    active: {
        background: 'rgba(255, 255, 255, 0.1)',
        '&:before': {
            animation: '$animateUp 0.2s forwards'
        }
    },
})

const List = ({ items, onSelect }) => {
    const classes = useStyles();
    const [selectedGroup, setSelectedGroup] = useState(items[0]);

    const changeGroup = useCallback((e) => {
        setSelectedGroup(e);
        onSelect(e);
    }, [selectedGroup])

    return (
        <ul className={classes.item_list}>
            {items.length > 0 && selectedGroup && items.map((v, i) => (
                <li
                    key={i}
                    onClick={() => changeGroup(v)}
                    className={v.id === selectedGroup.id ? classes.active : ''}
                >
                    <span>{v.text}</span>
                    {v.id === selectedGroup.id && (
                        <i className={`ms-Icon ms-Icon--Forward`} aria-hidden="true"></i>
                    )}
                </li>
            ))}
        </ul>
    )
}

export default List;
