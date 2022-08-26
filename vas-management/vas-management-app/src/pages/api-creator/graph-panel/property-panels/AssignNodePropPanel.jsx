import { PrimaryButton, Text, TextField } from '@fluentui/react';
import { useForm, Controller } from 'react-hook-form'

const AssignNodePropPanel = ({ node, onNodeDataChange }) => {
    const { control, handleSubmit } = useForm({
        defaultValues: {
            // name: node.data.name,
        }
    });

    // props: {
    //     id: 9,
    //     data: [],
    // }

    const onSubmit = data => { };

    return (
        <div>
            <Text variant='mediumPlus' style={{ textTransform: 'capitalize' }}>Node: {node.data.label}, ID: {node.id}</Text>
            <ul>
                {node.props && node.props?.data?.length > 0 && node.props?.data?.map((item, key) => (
                    <li key={key}></li>
                ))}
            </ul>
            <form style={{ marginTop: '1rem' }}>
                <Text variant='medium'>Variables</Text>
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
                <PrimaryButton text="Submit" onClick={handleSubmit(onSubmit)} />
            </form>
        </div>
    )
}

export default AssignNodePropPanel;
