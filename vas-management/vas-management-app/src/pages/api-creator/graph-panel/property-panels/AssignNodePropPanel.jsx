import { DefaultButton, DefaultPalette, Dialog, DialogFooter, DialogType, IconButton, PrimaryButton, Text, TextField } from '@fluentui/react';
import { useEffect } from 'react';
import { useState } from 'react';
import { useForm, Controller } from 'react-hook-form'
import { createUseStyles } from 'react-jss';
import useBoolean from '../../../../common/hooks/useBoolean';

const useStyles = createUseStyles({
    varItem: {
        color: DefaultPalette.white,
        padding: '1rem',
        background: 'rgba(255, 255, 255, 0.03)',
        borderRadius: '0.25rem',
        margin: '0.75rem 0',
        borderBottom: '2px solid transparent',
        display: 'grid',
        gridTemplateColumns: '90% 5% 5%',
        alignItems: 'center',
        gap: '0.25rem',
        '&:hover': {
            borderBottomColor: DefaultPalette.themePrimary,
            '& i': {
                display: 'block'
            }
        },
        '& i': {
            display: 'none',
            cursor: 'pointer'
        }
    },
})

const AssignNodePropPanel = ({ node, onNodeDataChange, data }) => {
    const classes = useStyles();
    const { control, handleSubmit, setValue } = useForm({
        defaultValues: {
        }
    });
    const [variables, setVariables] = useState(data?.props || []);
    const { value: isDeleteDialogVisible, toggle: toggleDeleteDialog, } = useBoolean(false);
    const [selectedVar, setSelectedVar] = useState(null);

    // props: {
    //     id: 9,
    //     data: [],
    // }

    const onSubmit = data => {
        let isExistingVarUpdate = false;
        setVariables(vars => (vars || []).map(itm => {
            if (data.name == itm.name) {
                isExistingVarUpdate = true;
                return { ...itm, ...data };
            }
            return itm;
        }))
        if (!isExistingVarUpdate) {
            setVariables(vars => vars.concat(data));
        }
    };

    useEffect(() => {
        if (variables.length > 0) {
            let props = { id: node.id, data: variables }
            onNodeDataChange(props);
        }
    }, [variables])

    const onEditVariableClick = (item) => {
        setValue('name', item.name);
        setValue('value', item.value);
    }

    const onDeleteVariableClick = (data) => {
        setSelectedVar(data);
        toggleDeleteDialog();
    }

    const deleteVariable = () => {
        setVariables(vars => vars.filter(e => e.name != selectedVar.name))
        setSelectedVar(null);
        toggleDeleteDialog();
    }

    return (
        <div>
            <ul>
                {variables.length > 0 && variables.map((item, key) => (
                    <li key={key} className={classes.varItem}>
                        <div>
                            Variable name: {item.name}
                            <br />
                            Value: {item.value}
                        </div>
                        <div>
                            <IconButton iconProps={{ iconName: 'Settings' }} title="Edit" ariaLabel="Edit" onClick={() => onEditVariableClick(item)} />
                        </div>
                        <div>
                            <IconButton iconProps={{ iconName: 'Delete' }} title="Delete" ariaLabel="Delete" onClick={() => onDeleteVariableClick(item)} />
                        </div>
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

            <Dialog
                hidden={!isDeleteDialogVisible}
                onDismiss={toggleDeleteDialog}
                dialogContentProps={{
                    type: DialogType.normal,
                    title: 'Delete variable',
                    closeButtonAriaLabel: 'Close',
                    subText: 'The selected variable will be deleted, Are you sure ?',
                }}
            >

                <DialogFooter>
                    <PrimaryButton onClick={deleteVariable} text="Yes" />
                    <DefaultButton onClick={toggleDeleteDialog} text="No" />
                </DialogFooter>
            </Dialog>
        </div>
    )
}

export default AssignNodePropPanel;
