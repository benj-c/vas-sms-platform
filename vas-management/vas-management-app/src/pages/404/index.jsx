import React from "react";
import { useHistory } from "react-router-dom";
import { PrimaryButton } from "@fluentui/react";
import { createUseStyles } from 'react-jss';

const useStyles = createUseStyles({
    f404: {
        height: '100vh',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        color: 'var(--themePrimary)',
        '& h1': {
            fontSize: '4rem',
            marginBottom: '1.25rem'
        },
        '& p': {
            marginBottom: '1rem'
        }
    }
});

const F404 = () => {
    const history = useHistory();
    const classes = useStyles();

    return (
        <div className={classes.f404}>
            <h1>404</h1>
            <p>The resource you've requested could not find</p>
            <PrimaryButton text="Store" onClick={() => history.push("/store")} />
        </div>
    )
}

export default F404;
