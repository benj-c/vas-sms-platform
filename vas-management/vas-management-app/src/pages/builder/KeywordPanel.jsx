//lib
import { ActionButton, IconButton, Panel } from "@fluentui/react";
import { useBoolean } from "@fluentui/react-hooks";
import { useEffect, useState, memo } from "react";
import { createUseStyles } from "react-jss";
import { useRecoilValue } from "recoil";
//app
import { getKeywordByActionsId } from "../../common/ApiHandler"
import { selectedActionAtom } from "../../state/atoms"
import UpdateKeywordForm from "../../common/forms/UpdateKeywordForm";
import AddKeywordForm from "../../common/forms/AddKeywordForm";

const useStyles = createUseStyles({
    keywords: {
        marginTop: '1.5rem',
        position: 'relative',
        borderRadius: '0.35rem',
        '& h2': {
            color: 'var(--themePrimary)',
            fontSize: '1rem',
            marginRight: '1rem',
            borderBottom: '1px solid rgba(255, 255, 255, 0.1)'
        },
        '& ul': {
            display: "grid",
            gridTemplateColumns: 'repeat(4, 1fr)',
            gap: '0.75rem',
            overflowY: 'auto',
            marginTop: '1rem',
            marginRight: '1rem',
            '& li': {
                padding: '1rem',
                background: 'rgba(255, 255, 255, 0.03)',
                borderRadius: '0.35rem',
                cursor: 'pointer',
                '&:hover i': {
                    visibility: 'visible',
                },
                '& i': {
                    visibility: 'hidden',
                    marginLeft: 'auto',
                    '&:hover': {
                        color: 'var(--themePrimary)'
                    },
                }
            },
        },
    },
    active: {
        background: 'rgba(255, 255, 255, 0.1)',
    },
    inner: {
        display: 'grid',
        gridTemplateColumns: '80% 20%'
    }
})

const KeywordsPanel = ({ }) => {
    const classes = useStyles();
    const [keywords, setKeywords] = useState([]);
    const [isOpen, { setTrue: openPanel, setFalse: dismissPanel }] = useBoolean(false);
    const [selectedKeyword, setSelectedKeyword] = useState();
    const [isUpdating, setIsUpdating] = useState(false);
    const action = useRecoilValue(selectedActionAtom);

    useEffect(() => {
        if (action) {
            loadKeywords(action.id);
        }
    }, [action])

    const onItemSelect = item => {
        setSelectedKeyword(item);
    }

    const loadKeywords = (actionId) => {
        getKeywordByActionsId(actionId)
            .then(res => {
                setKeywords(res.data);
            })
            .catch(e => { })
            .finally(() => { });

    }

    const onUpdate = () => {
        loadKeywords(action.id);
    }

    const showPanel = e => {
        openPanel()
        setIsUpdating(e)
    }

    return (
        <>
            <div className={classes.keywords}>
                <h2>
                    Keywords
                    <IconButton
                        style={{ float: 'right', bottom: '0.6rem' }}
                        iconProps={{ iconName: 'Add' }}
                        title="Add new keyword"
                        ariaLabel="Add"
                        onClick={() => showPanel(false)}
                    />
                </h2>
                <ul>
                    {keywords.length > 0 && keywords.map((k, i) => (
                        <li
                            style={{ background: (selectedKeyword && k.id === selectedKeyword.id) ? 'rgba(255, 255, 255, 0.1)' : 'rgba(255, 255, 255, 0.03)' }}
                            key={k.id}
                            onClick={() => onItemSelect(k)}
                        >
                            <div className={classes.inner}>
                                <div>
                                    <small>Regular Expression</small>
                                    <div style={{ paddingBottom: '0.5rem' }}></div>
                                    <code>{k.regex}</code>
                                </div>
                                <div style={{ display: 'flex', alignItems: 'center' }}>
                                    <i className="ms-Icon ms-Icon--Settings" aria-hidden="true" onClick={() => showPanel(true)}></i>
                                </div>
                            </div>
                        </li>
                    ))}
                </ul>
            </div>

            <Panel
                isOpen={isOpen}
                onDismiss={dismissPanel}
                headerText={isUpdating ? `Keyword settings` : 'Add new keyword'}
                closeButtonAriaLabel="Close"
                isFooterAtBottom={true}
            >
                {isUpdating ?
                    <UpdateKeywordForm {...selectedKeyword} dismissPanel={dismissPanel} onUpdate={onUpdate} />
                    :
                    <AddKeywordForm dismissPanel={dismissPanel} onUpdate={onUpdate} />
                }

            </Panel>
        </>
    )
}

export default KeywordsPanel;
