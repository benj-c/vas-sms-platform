import { Persona, PersonaPresence, PersonaSize } from "@fluentui/react";
import { useEffect, useState } from "react";
import { createUseStyles } from "react-jss";
import { get } from "../../common/Storage";

import ActionList from "./ActionList";
import Appearance from "./Appearance";
import Profile from "./Profile";

const useStyles = createUseStyles({
    '@keyframes animateUp': {
        from: {
            transform: 'translateY(2rem)'
        },
        to: {
            transform: 'translateY(0)',
        }
    },
    pageRoot: {
    },
    pageHeader: {
        position: 'relative',
        display: 'flex',
        flexDirection: 'row',
        alignItems: 'center'
    },
    pageTitle: {
        fontSize: '1.5rem',
        lineHeight: '2rem',
    },
    menu: {
        display: 'grid',
        gridTemplateColumns: '20% 80%',
        height: '100%',
        padding: '1rem 0 0 1rem',
        '& ul': {
            paddingTop: '1.5rem',
            '& li': {
                padding: '0.5rem 0.25rem',
                borderRadius: '0.25rem',
                margin: '0.25rem 0',
                display: 'flex',
                alignItems: 'center',
                '&:hover': {
                    background: 'rgba(255, 255, 255, 0.1)'
                },
                '& i': {
                    marginLeft: 'auto'
                }
            }
        }
    },
    active: {
        background: 'rgba(255, 255, 255, 0.1)',
    },
    profileInfo: {
        padding: '0.5rem',
        borderRadius: '0.25rem',
        '&:hover': {
            background: 'rgba(255, 255, 255, 0.1)'
        }
    },
    settings: {
        animation: '$animateUp 0.2s forwards',
        paddingLeft: '1rem',
        '& h1': {
            fontSize: '2rem',
        }
    },
})

const InnerPages = [
    {
        name: 'Profile',
        component: Profile
    },
    {
        name: 'Appearance',
        component: Appearance,
    },
    {
        name: 'Action List',
        component: ActionList,
    }
]

const Settings = () => {
    const classes = useStyles();
    const [user, setUser] = useState(get('user'));
    const [page, setPage] = useState(InnerPages[1]);

    useEffect(() => {
        console.log(page)
    }, [page])

    return (
        <div className={classes.pageRoot}>
            <div className={classes.pageHeader}>
            </div>
            <div className={classes.menu}>
                <div>
                    <article className={classes.profileInfo}>
                        <Persona
                            imageUrl={user.img}
                            // imageInitials={user.user_name.substring(0, 2)}
                            text={user.name}
                            secondaryText={user.email}
                            size={PersonaSize.size56}
                            hidePersonaDetails={false}
                            presence={PersonaPresence.none}
                            imageAlt={user.name}
                        />
                    </article>

                    <ul>
                        {InnerPages.map((p, i) => (
                            <li key={i} onClick={() => setPage(p)} className={p.name === page.name ? classes.active : ''}>
                                {p.name}
                                {p.name === page.name && (
                                    <i className={`ms-Icon ms-Icon--Forward`} aria-hidden="true"></i>
                                )}
                            </li>
                        ))}
                    </ul>
                </div>
                <div className={classes.settings}>
                    {page && (<page.component />)}
                </div>
            </div>
        </div>
    )
}

export default Settings;
