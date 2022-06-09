//lib
import React, { useCallback, useEffect, useState } from "react";
import { createUseStyles } from "react-jss";
import { Link } from "react-router-dom";
import { useLocation } from "react-router-dom";
import { useRecoilState, useRecoilValue } from 'recoil'
//app
import { routes } from "./routes";
import AppIcon from "./AppIcon";
import { showSideBarNavAtom, resetSideBarAtom, selectedActionAtom } from '../state/atoms'

const useStyles = createUseStyles({
    '@keyframes animateUp': {
        from: {
            height: '0.5rem'
        },
        to: {
            height: '1.5rem',
        }
    },
    sideNav: {
        height: '100%',
        width: '100%',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        textAlign: 'center',
        background: '#1f2024',
    },
    logo: {
        marginTop: '0.75rem',
        marginBottom: '1rem',
    },
    topNavs: {
        width: '100%'
    },
    bottomNavs: {
        marginTop: 'auto',
        width: '100%'
    },
    navItem: {
        height: '3rem',
        padding: '0.1rem 0',
        margin: '0.5rem 0',
        cursor: 'pointer',
        borderRadius: '0.25rem',
        position: 'relative',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        flexDirection: 'column',
        background: 'transparent',
        color: 'rgba(255, 255, 255, 0.6)',
        '&:hover': {
            background: '#454545',
        },
        '&:before': {
            content: '""',
            height: '0rem',
            width: '3.5px',
            position: 'absolute',
            left: 0,
            borderRadius: '2.5px',
            background: 'var(--themePrimary)',
        }
    },
    navIcon: {
        fontSize: '1rem',
        lineHeight: '1.5rem',
    },
    active: {
        background: '#454545',
        color: 'var(--themePrimary)',
        '&:before': {
            animation: '$animateUp 0.2s forwards'
        }
    },
    hideNavLabel: {
        display: 'none'
    },
    navLabel: {
        fontSize: '0.7rem',
    }
})

const SideBar = () => {
    const classes = useStyles();
    const location = useLocation();
    const [selectedNav, setSelectedNav] = useState();
    const [_routes, setRoutes] = useState(routes.filter(r => !r.sideBarProps.hidden));
    const visibleSideNavItem = useRecoilValue(showSideBarNavAtom);
    const resetSideBar = useRecoilValue(resetSideBarAtom);
    const [selectedAction, setSelectedAction] = useRecoilState(selectedActionAtom);

    useEffect(() => {
        let p = _routes.filter(r => r.path === location.pathname)[0]
        setSelectedNav(p ? p.id : 0)
        setSelectedAction(null);
    }, [location])

    useEffect(() => {
        if (visibleSideNavItem != 0) {
            let r = [...routes];
            for (let i = 0; i < r.length; i++) {
                if (r[i].name === visibleSideNavItem) {
                    r[i].sideBarProps.hidden = !r[i].sideBarProps.hidden;
                    setSelectedNav(r[i].id);
                }
            }
            setRoutes(r)
        }
    }, [visibleSideNavItem])

    const onNavChange = useCallback(({ id }) => {
        setSelectedNav(id);
    }, [])

    const NavItem = ({ item }) => {
        return (
            <li
                key={item.id}
                onClick={() => onNavChange(item)}
            >
                <Link to={item.path}
                    className={`${classes.navItem} ${selectedNav === item.id ? classes.active : ''}`}>
                    <i
                        className={`ms-Icon ms-Icon--${item.sideBarProps.icon} ${classes.navIcon}`}
                        aria-hidden="true">
                    </i>
                    <p className={`${selectedNav === item.id ? classes.hideNavLabel : ''} ${classes.navLabel}`}>
                        {item.name}
                    </p>
                </Link>
            </li>
        )
    }

    return (
        <aside>
            <nav className={classes.sideNav}>
                <span className={classes.logo}>
                    <AppIcon />
                </span>
                <ul className={classes.topNavs}>
                    {_routes.length > 0 && _routes.filter(n => n.sideBarProps.pos === "top").map((item, i) => (
                        <NavItem item={item} key={i} />
                    ))}
                </ul>

                <ul className={classes.bottomNavs}>
                    {_routes.length > 0 && _routes.filter(n => n.sideBarProps.pos === "bottom").map((item, i) => (
                        <NavItem item={item} key={i} />
                    ))}
                </ul>
            </nav>
        </aside>
    )
}

export default SideBar;
