//lib
import { PrimaryButton, Spinner, SpinnerSize, TextField } from '@fluentui/react';
import { useState } from 'react';
import { createUseStyles } from 'react-jss';
//app
import { login } from "../../common/ApiHandler";
import { persist } from "../../common/Storage"

const useStyles = createUseStyles({
    container: {
        height: '100vh',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        textAlign: 'left',
        '& h2': {
            fontSize: '1rem',
            // marginBottom: '1.5rem',
        },
        '& h1': {
            fontSize: '1.5rem',
            marginBottom: '1.5rem',
        },
        '& form': {
            width: '280px'
        },
    },
    submitBtn: {
        width: '100%',
    },
    submitAction: {
        height: '1rem'
    }
})

const Login = () => {
    const classes = useStyles();
    const [loginData, setLoginData] = useState({ username: null, password: null });
    const [loading, setLoading] = useState(false);

    const onSubmit = e => {
        setLoading(true)
        login(loginData)
            .then(res => {
                let d = res.data;
                let user = {
                    id: d.id,
                    email: d.email,
                    name: d.name,
                    scope: d.scope,
                    user_name: d.username,
                    active: d.active
                }
                persist('user', user);
                persist('token', d.token)
                window.location.reload();
            })
            .catch(e => {
                setLoading(false)
            })
    }

    return (
        <div className={classes.container}>
            <div>
                <h2>Login</h2>
                <h1>VAS SMS Management</h1>
                <form>
                    <TextField
                        label="Name"
                        onChange={(e, v) => setLoginData({ ...loginData, ...{ username: v } })}
                    />
                    <br />
                    <TextField
                        label="Password"
                        type="password"
                        revealPasswordAriaLabel="Show"
                        onChange={(e, v) => setLoginData({ ...loginData, ...{ password: v } })}
                    />
                    <br />
                    <div className={classes.submitAction}>
                        {loading ?
                            <Spinner size={SpinnerSize.medium} />
                            :
                            <PrimaryButton text="Login" onClick={onSubmit} className={classes.submitBtn} />
                        }
                    </div>
                </form>
            </div>
        </div>
    )
}

export default Login;
