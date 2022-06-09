//lib
import { useEffect } from 'react';
import { createUseStyles } from 'react-jss';
import { useRecoilState } from 'recoil';
//app
import { selectedTopBarMenuAtom } from '../state/atoms'

const useStyles = createUseStyles({
    innerPageNav: {
        '& span': {
            display: 'inline',
            paddingBottom: '0.1rem',
            cursor: 'pointer',
            textTransform: 'uppercase',
            '&:nth-child(even)': {
                margin: '0 1rem'
            },
        }
    },
    activeTab: {
        borderBottom: '0.2rem solid var(--themePrimary)',
    },
})

const TopBarMenu = ({ menu }) => {
    const classes = useStyles();
    const [selectedMenu, setMenu] = useRecoilState(selectedTopBarMenuAtom)

    useEffect(() => {
        if (menu.length > 0) {
            changeMenu(menu[0])
        }
    }, [menu])

    const changeMenu = (menu) => {
        setMenu(menu)
    }

    return (
        <>
            {menu.length > 0 && (
                <>
                    <i className="ms-Icon ms-Icon--ChevronRight" aria-hidden="true"></i>
                    <span className={classes.innerPageNav}>
                        {menu.map((m, i) => (
                            <span
                                key={i}
                                onClick={() => changeMenu(m)}
                                className={selectedMenu.id === m.id ? classes.activeTab : ''}>
                                {m.title}
                            </span>
                        ))}
                    </span>
                </>
            )}
        </>
    )
}

export default TopBarMenu;
