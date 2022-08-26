import { DefaultPalette, PrimaryButton, Text, TextField } from '@fluentui/react';
import { useEffect } from 'react';
import { useState } from 'react';
import { useForm, Controller } from 'react-hook-form'
import { createUseStyles } from 'react-jss';

const useStyles = createUseStyles({
    varItem: {
        color: DefaultPalette.white,
        padding: '1rem',
        background: 'rgba(255, 255, 255, 0.1)',
        borderRadius: '0.25rem',
        margin: '0.75rem 0',
        borderBottom: '2px solid transparent',
        '&:hover': {
            borderBottomColor: DefaultPalette.themePrimary,
        }
    },
})

const AssignNodePropPanel = ({ node, onNodeDataChange, data }) => {
    const classes = useStyles();
    const { control, handleSubmit } = useForm({
        defaultValues: {
            // name: node.data.name,
        }
    });
    const [variables, setVariables] = useState(data?.props || []);

    // props: {
    //     id: 9,
    //     data: [],
    // }

    const onSubmit = data => {
        setVariables(vars => (vars || []).concat(data))
    };

    useEffect(() => {
        if (variables.length > 0) {
            let props = { id: node.id, data: variables }
            onNodeDataChange(props);
        }
    }, [variables])

    return (
        <div>
            {/* <Text variant='mediumPlus' style={{ textTransform: 'capitalize' }}>{node.data.label} node</Text> */}
            <br />
            {/* <Text variant='medium'>Variables</Text> */}

            <ul>
                {variables.length > 0 && variables.map((item, key) => (
                    <li key={key} className={classes.varItem}>
                        Variable name: {item.name}
                        <br />
                        Value: {item.value}
                    </li>
                ))}
            </ul>

            {variables.length == 0 && (
                <Text variant='mediumPlus'>No variables</Text>
            )}

            <form style={{ marginTop: '1rem' }}>
                <div style={{ margin: '1rem 0' }}>
                    <Controller
                        name="name"
                        control={control}
                        rules={{ required: true }}
                        render={({ field }) => <TextField label="Variable name" {...field} />}
                    />
                    <Controller
                        name="value"
                        control={control}
                        rules={{ required: true }}
                        render={({ field }) => <TextField
                            label="Value/Expression"
                            {...field}
                            multiline
                            rows={4}
                        />}
                    />
                </div>
                <PrimaryButton text="Set" onClick={handleSubmit(onSubmit)} />
            </form>
        </div>
    )
}

export default AssignNodePropPanel;
