import { PrimaryButton, TextField } from '@fluentui/react';
import { useForm, Controller } from 'react-hook-form'

const AssignNodePropPanel = ({ node, onNodeDataChange }) => {
    const { control, handleSubmit } = useForm({
        defaultValues: {
            name: node.data.name,
        }
    });

    const onSubmit = data => onNodeDataChange({ ...data, id: node.id });

    return (
        <div >
            <form>
                <Controller
                    name="name"
                    control={control}
                    rules={{ required: false }}
                    render={({ field }) => <TextField label="Node name" {...field} />}
                />
                <PrimaryButton text="Submit" onClick={handleSubmit(onSubmit)} />
            </form>
        </div>
    )
}

export default AssignNodePropPanel;
