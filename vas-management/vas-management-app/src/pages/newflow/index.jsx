//lib
import { ActionButton } from '@fluentui/react';
import { useRef, useState } from 'react';
import { createUseStyles } from 'react-jss';

//app
import CreateServiceForm from '../../common/forms/CreateServiceForm';
import CreateApiForm from '../../common/forms/CreateApiForm';

const useStyles = createUseStyles({
    nfRoot: {
        height: '80vh',
    },
    view1: {
        height: '100%',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        '& h2': {
            fontWeight: '200',
            fontSize: '2rem'
        },
        '& ul': {
            marginTop: '2rem',
            display: 'flex',
            flexDirection: 'row',
            gap: '0.75rem',
            '& li': {
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                flexDirection: 'column',
                padding: '1.5rem 1rem',
                borderRadius: '0.35rem',
                background: 'rgba(255, 255, 255, 0.1)',
                width: '200px',
                cursor: 'pointer',
                '&:hover': {
                    background: 'rgba(255, 255, 255, 0.2)',
                },
                '& h3': {
                    fontSize: '1.2rem',
                },
                '& i': {
                    fontSize: '2rem',
                    color: 'var(--themePrimary)',
                    marginBottom: '0.5rem',
                }
            }
        }
    },
    creatorView: {
        '& h2': {
            display: 'flex',
            alignItems: 'center'
        }
    },
    creatorContainer: {
        height: '100%',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        '& h1': {
            fontWeight: '200',
            fontSize: '2rem'
        },
    }
})

const NewFlow = () => {
    const classes = useStyles();
    const [selectedView, setSelectedView] = useState(null);
    const fileInput = useRef(null);

    const setFile = file => {
        console.log(file);
    }

    return (
        <div className={classes.nfRoot}>
            {!selectedView && (
                <div className={classes.view1}>
                    <h2>Let's create a new service or workflow</h2>
                    <div style={{ height: '1px', width: '4rem', background: 'var(--themePrimary)', margin: '1rem 0' }}></div>
                    <ul>
                        <li onClick={() => setSelectedView("SERVICE")}>
                            <i className="ms-Icon ms-Icon--WebAppBuilderFragmentCreate" aria-hidden="true"></i>
                            <h3>Create Service</h3>
                        </li>
                        <li onClick={() => setSelectedView("API")}>
                            <i className="ms-Icon ms-Icon--AppIconDefaultAdd" aria-hidden="true"></i>
                            <h3>Create Workflow</h3>
                        </li>
                        <li onClick={() => fileInput.current.click()}>
                            <i className="ms-Icon ms-Icon--Upload" aria-hidden="true"></i>
                            <h3>Import Service</h3>
                        </li>
                    </ul>
                </div>
            )}

            {(selectedView && selectedView === "SERVICE") && (
                <div className={classes.creatorView}>
                    <h2>
                        <ActionButton
                            iconProps={{ iconName: 'ChromeBack' }}
                            onClick={() => setSelectedView(null)}
                        >
                            Back
                        </ActionButton>
                    </h2>
                    <div className={classes.creatorContainer} >
                        <h1>Service creator | <span style={{ fontWeight: 400 }}>New Service</span></h1>
                        <CreateServiceForm onDismiss={() => setSelectedView(null)} />
                    </div>
                </div>
            )}
            {(selectedView && selectedView === "API") && (
                <div className={classes.creatorView}>
                    <h2>
                        <ActionButton
                            iconProps={{ iconName: 'ChromeBack' }}
                            onClick={() => setSelectedView(null)}
                        >
                            Back
                        </ActionButton>
                    </h2>
                    <div className={classes.creatorContainer} >
                        <h1>Service creator | <span style={{ fontWeight: 400 }}>New API</span></h1>
                        <CreateApiForm onDismiss={() => setSelectedView(null)} />
                    </div>
                </div>
            )}

            <input
                type="file"
                style={{display: 'none'}}
                ref={fileInput}
                onChange={e => setFile(e.target.files[0])}
            />
        </div>
    )
}

export default NewFlow;
